package servicio;

import conexion.ConexionBD;
import dao.PartidaDao;
import dao.ProgresoMisionDao;
import excepciones.AccionNoPermitidaException;
import modelo.Partida;
import modelo.ProgresoMision;
import modelo.enums.DefinicionMision;
import modelo.enums.EstadoMision;
import modelo.enums.TipoObjetivo;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class MisionServicio {

    // El jugador puede tener como maximo 3 misiones sin reclamar.
    public static final int MAX_MISIONES_ACTIVAS = 3;

    private final ProgresoMisionDao progresoMisionDao;
    private final PartidaDao partidaDao;


    public MisionServicio() {
        this.progresoMisionDao = new ProgresoMisionDao();
        this.partidaDao = new PartidaDao();
    }


    public Set<String> codigosTomados(Partida partida) throws SQLException {

        /*
         * Uso Set porque un codigo no se repite y solamente necesito
         * comprobar rapidamente si una mision ya fue tomada.
         */
        Set<String> codigos = new HashSet<>();

        for (ProgresoMision progreso :
                progresoMisionDao.listarPorPartida(partida.getIdPartida())) {

            codigos.add(progreso.getCodigoMision());
        }

        return codigos;
    }


    public List<DefinicionMision> misionesDisponibles(Partida partida)
            throws SQLException {

        Set<String> codigosTomados = codigosTomados(partida);
        List<DefinicionMision> disponibles = new ArrayList<>();

        /*
         * Las definiciones son fijas en Java.
         * Solo muestro las que esta partida nunca tomo.
         */
        for (DefinicionMision mision : DefinicionMision.values()) {

            if (!codigosTomados.contains(mision.name())) {
                disponibles.add(mision);
            }
        }

        return disponibles;
    }


    public List<ProgresoMision> misionesEnCurso(Partida partida)
            throws SQLException {

        List<ProgresoMision> enCurso = new ArrayList<>();

        for (ProgresoMision progreso :
                progresoMisionDao.listarPorPartida(partida.getIdPartida())) {

            /*
             * Una mision COMPLETADA sigue ocupando lugar hasta
             * que el jugador reclame su recompensa.
             */
            if (progreso.getEstado() == EstadoMision.ACTIVA
                    || progreso.getEstado() == EstadoMision.COMPLETADA) {

                enCurso.add(progreso);
            }
        }

        return enCurso;
    }


    public void aceptar(Partida partida, DefinicionMision mision)
            throws SQLException, AccionNoPermitidaException {

        if (mision == null) {
            throw new IllegalArgumentException(
                    "La mision no puede ser null."
            );
        }

        /*
         * Compruebo cualquier estado, porque una mision reclamada
         * tampoco puede volver a aceptarse.
         */
        ProgresoMision existente =
                progresoMisionDao.consultarPorPartidaYCodigo(
                        partida.getIdPartida(),
                        mision.name()
                );

        if (existente != null) {
            throw new AccionNoPermitidaException(
                    "Ya tomaste esa mision."
            );
        }

        if (misionesEnCurso(partida).size() >= MAX_MISIONES_ACTIVAS) {
            throw new AccionNoPermitidaException(
                    "Ya tenes el maximo de misiones en curso."
            );
        }

        /*
         * El constructor nuevo de ProgresoMision comienza
         * automaticamente con progreso 0 y estado ACTIVA.
         */
        ProgresoMision nuevo = new ProgresoMision(
                partida.getIdPartida(),
                mision.name()
        );

        progresoMisionDao.alta(nuevo);
    }


    public List<DefinicionMision> registrarProgreso(
            Partida partida,
            TipoObjetivo tipo,
            int cantidad
    ) throws SQLException {

        if (tipo == null) {
            throw new IllegalArgumentException(
                    "El tipo de objetivo no puede ser null."
            );
        }

        if (cantidad <= 0) {
            throw new IllegalArgumentException(
                    "La cantidad de progreso debe ser mayor a 0."
            );
        }

        List<DefinicionMision> completadas = new ArrayList<>();

        for (ProgresoMision progreso :
                progresoMisionDao.listarPorPartida(partida.getIdPartida())) {

            // Solo avanzan las misiones que todavia estan activas.
            if (progreso.getEstado() != EstadoMision.ACTIVA) {
                continue;
            }

            DefinicionMision definicion =
                    DefinicionMision.valueOf(
                            progreso.getCodigoMision()
                    );

            /*
             * Una accion solo hace avanzar las misiones
             * que tengan ese mismo tipo de objetivo.
             */
            if (definicion.getTipoObjetivo() != tipo) {
                continue;
            }

            /*
             * Math.min evita que el progreso pueda superar
             * el objetivo de la mision.
             */
            int nuevoProgreso = Math.min(
                    progreso.getProgreso() + cantidad,
                    definicion.getObjetivo()
            );

            progreso.setProgreso(nuevoProgreso);

            if (nuevoProgreso == definicion.getObjetivo()) {

                progreso.setEstado(EstadoMision.COMPLETADA);
                completadas.add(definicion);
            }

            progresoMisionDao.modificar(progreso);
        }

        return completadas;
    }


    public DefinicionMision reclamar(
            Partida partida,
            String codigoMision
    ) throws SQLException, AccionNoPermitidaException {

        ProgresoMision progreso =
                progresoMisionDao.consultarPorPartidaYCodigo(
                        partida.getIdPartida(),
                        codigoMision
                );

        if (progreso == null) {
            throw new AccionNoPermitidaException(
                    "No tenes esa mision."
            );
        }

        if (progreso.getEstado() != EstadoMision.COMPLETADA) {
            throw new AccionNoPermitidaException(
                    "La mision todavia no esta completada."
            );
        }

        DefinicionMision definicion =
                DefinicionMision.valueOf(codigoMision);

        /*
         * Guardo los valores anteriores porque, si la transaccion
         * falla, tambien debo restaurar el objeto Partida en memoria.
         */
        int monedasAnteriores = partida.getMonedas();
        int nivelAnterior = partida.getNivel();
        int experienciaAnterior = partida.getExperiencia();

        try (Connection con = ConexionBD.obtenerConexion()) {

            con.setAutoCommit(false);

            try {

                /*
                 * La mision y la recompensa se guardan en una misma
                 * transaccion: no puede quedar una sin la otra.
                 */
                progreso.setEstado(EstadoMision.RECLAMADA);
                progresoMisionDao.modificar(con, progreso);

                partida.setMonedas(
                        partida.getMonedas()
                                + definicion.getRecompensaMonedas()
                );

                partida.sumarExperiencia(
                        definicion.getRecompensaExperiencia()
                );

                partidaDao.modificar(con, partida);

                con.commit();

                return definicion;

            } catch (SQLException e) {

                /*
                 * El rollback restaura MySQL, pero no los objetos Java.
                 * Por eso tambien vuelvo sus valores al estado anterior.
                 */
                con.rollback();

                progreso.setEstado(EstadoMision.COMPLETADA);
                partida.setMonedas(monedasAnteriores);
                partida.setNivel(nivelAnterior);
                partida.setExperiencia(experienciaAnterior);

                throw e;
            }
        }
    }
}