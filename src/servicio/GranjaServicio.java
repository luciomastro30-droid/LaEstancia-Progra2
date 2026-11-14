package servicio;

import conexion.ConexionBD;
import dao.CultivoDao;
import dao.InventarioDao;
import dao.PartidaDao;
import dao.PlantacionDao;
import excepciones.AccionNoPermitidaException;
import excepciones.StockInsuficienteException;
import modelo.Cultivo;
import modelo.Inventario;
import modelo.Partida;
import modelo.Plantacion;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

public class GranjaServicio {

    public static final int MINUTOS_PLANTAR = 15;

    private final PlantacionDao plantacionDao;
    private final CultivoDao cultivoDao;
    private final InventarioDao inventarioDao;
    private final PartidaDao partidaDao;


    public GranjaServicio() {
        this.plantacionDao = new PlantacionDao();
        this.cultivoDao = new CultivoDao();
        this.inventarioDao = new InventarioDao();
        this.partidaDao = new PartidaDao();
    }

    //Devuelve lo q hay plantado en cada parcela
    public Map<Integer, Plantacion> plantacionesPorParcela(Partida partida) throws SQLException {
        Map<Integer, Plantacion> plantaciones = new HashMap<>();
        for (Plantacion plantacion : plantacionDao.listarPorPartida(partida.getIdPartida())) {
            plantaciones.put(plantacion.getNumeroParcela(), plantacion);
        }
        return plantaciones;
    }


    //Devuelve los cultivos de esas plantaciones y si dos parcelas tienen el mismo cultivo se busca una vez
    public Map<Integer, Cultivo> cultivosDe(Map<Integer, Plantacion> plantaciones) throws SQLException {
        Map<Integer, Cultivo> cultivos = new HashMap<>();
        for (Plantacion plantacion : plantaciones.values()) {
            int idCultivo = plantacion.getIdCultivo();
            if (!cultivos.containsKey(idCultivo)) {
                cultivos.put(idCultivo, cultivoDao.consultarPorId(idCultivo));
            }
        }
        return cultivos;
    }

    //Devuelve los cultivos que el juegdor puede plantar
    public Map<Cultivo, Integer> semillasDisponibles(Partida partida) throws SQLException {
        Map<Cultivo, Integer> semillas = new LinkedHashMap<>();
        for (Cultivo cultivo : cultivoDao.listar()) {
            if (cultivo.getIdItemSemilla() == null) {
                continue;
            }
            Inventario inventario = inventarioDao.consultarPorPartidaEItem(
                    partida.getIdPartida(), cultivo.getIdItemSemilla());
            if (inventario != null && inventario.getCantidad() > 0) {
                semillas.put(cultivo, inventario.getCantidad());
            }
        }
        return semillas;
    }

    //planta un cultivo en una parcela, resta una semilla, crea la plantacion y guarda el avance del tiempo.
    //si algo falla se hace rollback
    public void plantar(Partida partida, Cultivo cultivo, int numeroParcela)
            throws SQLException, AccionNoPermitidaException, StockInsuficienteException {

        if (numeroParcela < 1 || numeroParcela > partida.getParcelasDesbloqueadas()) {
            throw new AccionNoPermitidaException("La parcela " + numeroParcela + " no esta desbloqueada.");
        }

        if (plantacionDao.consultarPorParcela(partida.getIdPartida(), numeroParcela) != null) {
            throw new AccionNoPermitidaException("La parcela " + numeroParcela + " ya esta ocupada.");
        }

        if (cultivo.getIdItemSemilla() == null) {
            throw new AccionNoPermitidaException("Ese cultivo no tiene semilla asociada.");
        }

        Inventario semillas = inventarioDao.consultarPorPartidaEItem(
                partida.getIdPartida(), cultivo.getIdItemSemilla());
        int disponibles = (semillas == null) ? 0 : semillas.getCantidad();
        if (disponibles < 1) {
            throw new StockInsuficienteException(1, disponibles);
        }

        long minutoAnterior = partida.getMinutoJuegoTotal();
        Plantacion nueva = new Plantacion(partida.getIdPartida(), cultivo.getId(), numeroParcela, minutoAnterior);

        try (Connection con = ConexionBD.obtenerConexion()) {
            con.setAutoCommit(false);
            try {
                semillas.setCantidad(disponibles - 1);
                inventarioDao.modificar(con, semillas);

                plantacionDao.alta(con, nueva);

                partida.avanzarMinutos(MINUTOS_PLANTAR);
                partidaDao.modificar(con, partida);

                con.commit();
            } catch (SQLException e) {
                con.rollback();
                // la base volvio atras, asi que el objeto en memoria tambien
                partida.setMinutoJuegoTotal(minutoAnterior);
                throw e;
            }
        }
    }

    // duerme hasta la manana siguiente y lo guarda
    public void dormir(Partida partida) throws SQLException {
        long minutoAnterior = partida.getMinutoJuegoTotal();
        partida.dormir();
        try {
            partidaDao.modificar(partida);
        } catch (SQLException e) {
            partida.setMinutoJuegoTotal(minutoAnterior);
            throw e;
        }
    }

}
