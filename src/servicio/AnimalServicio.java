package servicio;

import conexion.ConexionBD;
import dao.AnimalDao;
import dao.InventarioDao;
import dao.ItemDao;
import dao.PartidaDao;
import dao.TipoAnimalDao;
import excepciones.AccionNoPermitidaException;
import excepciones.MonedasInsuficientesException;
import modelo.Animal;
import modelo.Inventario;
import modelo.Item;
import modelo.Partida;
import modelo.TipoAnimal;
import modelo.enums.Alojamiento;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Gestiona la lógica relacionada con los animales de la granja.
 * Coordina compras, alimentación, producción, inventario y avance del tiempo.
 */
public class AnimalServicio {

    public static final int MINUTOS_ALIMENTAR = 15;
    public static final int MINUTOS_RECOLECTAR_PRODUCTOS = 15;
    public static final int MINUTOS_POR_DIA = 1440;

    // Los alimentos se consumen respetando este orden de prioridad.
    private static final int[] IDS_ALIMENTO = {53, 54, 55};

    private final AnimalDao animalDao;
    private final TipoAnimalDao tipoAnimalDao;
    private final InventarioDao inventarioDao;
    private final ItemDao itemDao;
    private final PartidaDao partidaDao;


    public AnimalServicio() {
        this.animalDao = new AnimalDao();
        this.tipoAnimalDao = new TipoAnimalDao();
        this.inventarioDao = new InventarioDao();
        this.itemDao = new ItemDao();
        this.partidaDao = new PartidaDao();
    }


    public List<TipoAnimal> tiposDisponibles() throws SQLException {
        return tipoAnimalDao.listar();
    }


    // Cuenta cuántos animales ocupan actualmente un tipo de alojamiento.
    public int ocupados(Partida partida, Alojamiento alojamiento)
            throws SQLException {

        int cantidad = 0;

        List<Animal> animales =
                animalDao.listarPorPartida(partida.getIdPartida());

        Map<Integer, TipoAnimal> tipos =
                tiposDe(animales);

        for (Animal animal : animales) {

            TipoAnimal tipo =
                    tipos.get(animal.getIdTipoAnimal());

            if (tipo.getAlojamiento() == alojamiento) {
                cantidad++;
            }
        }

        return cantidad;
    }


    // Agrupa los animales por tipo para mostrar cuántos hay de cada uno.
    public Map<String, Integer> cantidadPorTipo(
            Partida partida,
            Alojamiento alojamiento
    ) throws SQLException {

        Map<String, Integer> cantidades =
                new LinkedHashMap<>();

        List<Animal> animales =
                animalDao.listarPorPartida(partida.getIdPartida());

        Map<Integer, TipoAnimal> tipos =
                tiposDe(animales);

        for (Animal animal : animales) {

            TipoAnimal tipo =
                    tipos.get(animal.getIdTipoAnimal());

            if (tipo.getAlojamiento() == alojamiento) {

                cantidades.merge(
                        tipo.getNombre(),
                        1,
                        Integer::sum
                );
            }
        }

        return cantidades;
    }


    /*
     * Compra un animal validando alojamiento, capacidad y monedas.
     * El alta del animal y el descuento de monedas se guardan en una misma transacción.
     */
    public void comprar(
            Partida partida,
            TipoAnimal tipoAnimal,
            String nombre
    ) throws SQLException,
            AccionNoPermitidaException,
            MonedasInsuficientesException {

        Alojamiento alojamiento =
                tipoAnimal.getAlojamiento();

        validarAlojamientoConstruido(
                partida,
                alojamiento
        );

        if (ocupados(partida, alojamiento)
                >= alojamiento.getCapacidad()) {

            String lugar =
                    alojamiento == Alojamiento.GALLINERO
                            ? "gallinero"
                            : "establo";

            throw new AccionNoPermitidaException(
                    "El " + lugar + " esta lleno."
            );
        }

        int precio =
                tipoAnimal.getPrecioCompra();

        if (partida.getMonedas() < precio) {

            throw new MonedasInsuficientesException(
                    precio,
                    partida.getMonedas()
            );
        }

        int monedasAnteriores =
                partida.getMonedas();

        long minutoActual =
                partida.getMinutoJuegoTotal();

        Animal nuevo = new Animal(
                partida.getIdPartida(),
                tipoAnimal.getId(),
                nombre
        );

        /*
         * La produccion comienza a contar desde
         * el momento en que se compra el animal.
         */
        nuevo.setMinutoUltimaProduccion(
                minutoActual
        );

        try (Connection con =
                     ConexionBD.obtenerConexion()) {

            con.setAutoCommit(false);

            try {

                partida.setMonedas(
                        monedasAnteriores - precio
                );

                if (!animalDao.alta(
                        con,
                        nuevo
                )) {

                    throw new SQLException(
                            "No se pudo guardar el animal."
                    );
                }

                if (!partidaDao.modificar(
                        con,
                        partida
                )) {

                    throw new SQLException(
                            "No se pudo guardar la compra del animal."
                    );
                }

                con.commit();

            } catch (SQLException e) {

                con.rollback();

                // El rollback no modifica el objeto Java.
                partida.setMonedas(
                        monedasAnteriores
                );

                throw e;
            }
        }
    }


    /*
     * Alimenta a los animales que lo necesitan usando el alimento disponible
     * según el orden de prioridad definido en IDS_ALIMENTO.
     */
    public int alimentar(Partida partida)
            throws SQLException,
            AccionNoPermitidaException {

        long minutoActual =
                partida.getMinutoJuegoTotal();

        List<Animal> hambrientos =
                new ArrayList<>();

        for (Animal animal :
                animalDao.listarPorPartida(
                        partida.getIdPartida()
                )) {

            if (!estaAlimentado(
                    animal,
                    minutoActual
            )) {

                hambrientos.add(animal);
            }
        }

        if (hambrientos.isEmpty()) {

            throw new AccionNoPermitidaException(
                    "Tus animales no tienen hambre."
            );
        }

        /*
         * LinkedHashMap mantiene el orden 53, 54, 55,
         * que es la prioridad definida para los alimentos.
         */
        Map<Integer, Inventario> alimentos =
                new LinkedHashMap<>();

        int alimentoDisponible = 0;

        for (int idAlimento : IDS_ALIMENTO) {

            Inventario fila =
                    inventarioDao.consultarPorPartidaEItem(
                            partida.getIdPartida(),
                            idAlimento
                    );

            alimentos.put(
                    idAlimento,
                    fila
            );

            if (fila != null) {
                alimentoDisponible +=
                        fila.getCantidad();
            }
        }

        if (alimentoDisponible == 0) {

            throw new AccionNoPermitidaException(
                    "No tenes alimento. Compralo en el pueblo."
            );
        }

        long minutoAnterior =
                partida.getMinutoJuegoTotal();

        try (Connection con =
                     ConexionBD.obtenerConexion()) {

            con.setAutoCommit(false);

            try {

                int alimentados = 0;

                /*
                 * Recorremos los alimentos en prioridad:
                 * primero 53, despues 54 y finalmente 55.
                 */
                for (int idAlimento : IDS_ALIMENTO) {

                    Inventario fila =
                            alimentos.get(idAlimento);

                    if (fila == null
                            || fila.getCantidad() == 0) {

                        continue;
                    }

                    int cantidadAnterior =
                            fila.getCantidad();

                    int usados = 0;

                    while (usados < cantidadAnterior
                            && alimentados < hambrientos.size()) {

                        Animal animal =
                                hambrientos.get(alimentados);

                        animal.setHambre(100);

                        animal.setMinutoUltimaAlimentacion(
                                minutoActual
                        );

                        /*
                         * Si el animal todavia no tenia iniciado
                         * su ciclo de produccion, comienza ahora.
                         */
                        if (animal.getMinutoUltimaProduccion() == null) {

                            animal.setMinutoUltimaProduccion(
                                    minutoActual
                            );
                        }

                        if (!animalDao.modificar(
                                con,
                                animal
                        )) {

                            throw new SQLException(
                                    "No se pudo actualizar el animal."
                            );
                        }

                        usados++;
                        alimentados++;
                    }

                    if (usados > 0) {

                        fila.setCantidad(
                                cantidadAnterior - usados
                        );

                        if (!inventarioDao.modificar(
                                con,
                                fila
                        )) {

                            throw new SQLException(
                                    "No se pudo descontar el alimento."
                            );
                        }
                    }

                    if (alimentados
                            == hambrientos.size()) {

                        break;
                    }
                }

                partida.avanzarMinutos(
                        MINUTOS_ALIMENTAR
                );

                if (!partidaDao.modificar(
                        con,
                        partida
                )) {

                    throw new SQLException(
                            "No se pudo guardar el avance del tiempo."
                    );
                }

                con.commit();

                return Math.min(
                        hambrientos.size(),
                        alimentoDisponible
                );

            } catch (SQLException e) {

                con.rollback();

                partida.setMinutoJuegoTotal(
                        minutoAnterior
                );

                throw e;
            }
        }
    }


    /*
     * Recolecta los productos de los animales que están alimentados y completaron
     * su tiempo de producción, actualizando inventario y ciclo productivo.
     */
    public Map<String, Integer> recolectarProductos(
            Partida partida
    ) throws SQLException,
            AccionNoPermitidaException {

        long minutoActual =
                partida.getMinutoJuegoTotal();

        List<Animal> animales =
                animalDao.listarPorPartida(
                        partida.getIdPartida()
                );

        Map<Integer, TipoAnimal> tipos =
                tiposDe(animales);

        List<Animal> productores =
                new ArrayList<>();

        /*
         * Agrupo por ID del producto para que varios animales
         * que produzcan lo mismo sumen sobre una sola fila.
         */
        Map<Integer, Integer> cantidadesPorItem =
                new LinkedHashMap<>();

        for (Animal animal : animales) {

            TipoAnimal tipo =
                    tipos.get(
                            animal.getIdTipoAnimal()
                    );

            Long ultimaProduccion =
                    animal.getMinutoUltimaProduccion();

            if (!estaAlimentado(
                    animal,
                    minutoActual
            )
                    || tipo.getIdItemProducto() == null
                    || ultimaProduccion == null
                    || minutoActual - ultimaProduccion
                    < tipo.getTiempoProduccionMin()) {

                continue;
            }

            productores.add(animal);

            cantidadesPorItem.merge(
                    tipo.getIdItemProducto(),
                    tipo.getCantidadProducto(),
                    Integer::sum
            );
        }

        if (productores.isEmpty()) {
            throw new AccionNoPermitidaException(
                    "Todavia no hay productos para recolectar."
            );
        }

        Map<Integer, Inventario> filasInventario =
                new LinkedHashMap<>();

        Map<Integer, String> nombres =
                new LinkedHashMap<>();

        for (int idItem : cantidadesPorItem.keySet()) {

            Item item =
                    itemDao.consultarPorId(
                            idItem
                    );

            if (item == null) {

                throw new SQLException(
                        "No existe el item producto con ID "
                                + idItem
                                + "."
                );
            }

            nombres.put(
                    idItem,
                    item.getNombre()
            );

            filasInventario.put(
                    idItem,
                    inventarioDao.consultarPorPartidaEItem(
                            partida.getIdPartida(),
                            idItem
                    )
            );
        }

        long minutoAnterior =
                partida.getMinutoJuegoTotal();

        try (Connection con =
                     ConexionBD.obtenerConexion()) {

            con.setAutoCommit(false);

            try {

                /*
                 * Primero guardo todos los productos
                 * obtenidos dentro de la misma transaccion.
                 */
                for (Map.Entry<Integer, Integer> entrada
                        : cantidadesPorItem.entrySet()) {

                    int idItem =
                            entrada.getKey();

                    int cantidad =
                            entrada.getValue();

                    Inventario fila =
                            filasInventario.get(
                                    idItem
                            );

                    if (fila == null) {

                        if (!inventarioDao.alta(
                                con,
                                new Inventario(
                                        partida.getIdPartida(),
                                        idItem,
                                        cantidad
                                )
                        )) {

                            throw new SQLException(
                                    "No se pudo guardar el producto recolectado."
                            );
                        }

                    } else {

                        fila.setCantidad(
                                fila.getCantidad()
                                        + cantidad
                        );

                        if (!inventarioDao.modificar(
                                con,
                                fila
                        )) {

                            throw new SQLException(
                                    "No se pudo actualizar el producto recolectado."
                            );
                        }
                    }
                }

                /*
                 * Al recolectar comienza un nuevo ciclo
                 * de produccion para esos animales.
                 */
                for (Animal animal
                        : productores) {

                    animal.setMinutoUltimaProduccion(
                            minutoActual
                    );

                    if (!animalDao.modificar(
                            con,
                            animal
                    )) {

                        throw new SQLException(
                                "No se pudo actualizar el ciclo de produccion del animal."
                        );
                    }
                }

                partida.avanzarMinutos(
                        MINUTOS_RECOLECTAR_PRODUCTOS
                );

                if (!partidaDao.modificar(
                        con,
                        partida
                )) {

                    throw new SQLException(
                            "No se pudo guardar el avance del tiempo."
                    );
                }

                con.commit();

                Map<String, Integer> resultado =
                        new LinkedHashMap<>();

                for (Map.Entry<Integer, Integer> entrada
                        : cantidadesPorItem.entrySet()) {

                    resultado.put(
                            nombres.get(
                                    entrada.getKey()
                            ),
                            entrada.getValue()
                    );
                }

                return resultado;

            } catch (SQLException e) {

                con.rollback();

                partida.setMinutoJuegoTotal(
                        minutoAnterior
                );

                throw e;
            }
        }
    }


    private void validarAlojamientoConstruido(
            Partida partida,
            Alojamiento alojamiento
    ) throws AccionNoPermitidaException {

        if (alojamiento == Alojamiento.GALLINERO
                && !partida.isTieneGallinero()) {

            throw new AccionNoPermitidaException(
                    "Primero construi el gallinero."
            );
        }

        if (alojamiento == Alojamiento.ESTABLO
                && !partida.isTieneEstablo()) {

            throw new AccionNoPermitidaException(
                    "Primero construi el establo."
            );
        }
    }


    private boolean estaAlimentado(
            Animal animal,
            long minutoActual
    ) {

        Long ultimaAlimentacion =
                animal.getMinutoUltimaAlimentacion();

        return ultimaAlimentacion != null
                && minutoActual - ultimaAlimentacion
                < MINUTOS_POR_DIA;
    }


    private Map<Integer, TipoAnimal> tiposDe(
            List<Animal> animales
    ) throws SQLException {

        Map<Integer, TipoAnimal> tipos =
                new HashMap<>();

        for (Animal animal : animales) {

            int idTipo =
                    animal.getIdTipoAnimal();

            if (!tipos.containsKey(
                    idTipo
            )) {

                TipoAnimal tipo =
                        tipoAnimalDao.consultarPorId(
                                idTipo
                        );

                if (tipo == null) {

                    throw new SQLException(
                            "No existe el tipo de animal con ID "
                                    + idTipo
                                    + "."
                    );
                }

                tipos.put(
                        idTipo,
                        tipo
                );
            }
        }

        return tipos;
    }
}