package servicio;

import conexion.ConexionBD;
import dao.InventarioDao;
import dao.PartidaDao;
import dao.UsuarioDao;
import excepciones.AccionNoPermitidaException;
import modelo.Inventario;
import modelo.Jugador;
import modelo.Partida;
import modelo.Usuario;
import util.Seguridad;

import java.sql.Connection;
import java.sql.SQLException;

public class RegistroServicio {

    private static final int ID_ITEM_SEMILLA_INICIAL = 1; //semilla de trigo
    private static final int CANTIDAD_SEMILLA_INICIAL = 5;

    public Partida crearPartidaNueva(Connection con, int idUsuario, String nombreGranja) throws SQLException {

        PartidaDao partidaDao = new PartidaDao();
        Partida nuevaPartida = new Partida(idUsuario, nombreGranja);
        partidaDao.alta(con,nuevaPartida);

        InventarioDao inventarioDao = new InventarioDao();
        Inventario nuevoInventario = new Inventario(nuevaPartida.getIdPartida(),ID_ITEM_SEMILLA_INICIAL,CANTIDAD_SEMILLA_INICIAL);
        inventarioDao.alta(con,nuevoInventario);

        return nuevaPartida;
    }

    public Jugador registrar(String nombreUsuario, String email, String contrasenia, String nombreGranja) throws AccionNoPermitidaException, SQLException {

        UsuarioDao usuarioDao = new UsuarioDao();

        if(nombreUsuario == null || nombreUsuario.isBlank() || contrasenia == null || contrasenia.isBlank() || nombreGranja == null || nombreGranja.isBlank()){
            throw new AccionNoPermitidaException("El nombre de usuario,contraseña o nombre de la granja no puede estar vacio");
        }

        if (usuarioDao.existeNombre(nombreUsuario)){
            throw new AccionNoPermitidaException("Ese nombre de usuario ya existe");
        }

        if(email != null && email.isBlank()){
            email = null;
        }

        if(email != null && usuarioDao.existeEmail(email)){
            throw new AccionNoPermitidaException("Ese email ya existe");
        }

        String passHash = Seguridad.hashear(contrasenia);
        Jugador jugador;

        try {
            jugador = new Jugador(nombreUsuario,email,passHash);
        }catch (IllegalArgumentException e){
            throw new AccionNoPermitidaException(e.getMessage());
        }

        try (Connection con = ConexionBD.obtenerConexion()){
            con.setAutoCommit(false);
            try {
                usuarioDao.alta(con,jugador);
                Partida partida = crearPartidaNueva(con, jugador.getIdUsuario(), nombreGranja);
                jugador.setPartidaActual(partida);
                con.commit();
                return jugador;
            }catch (SQLException e){
                con.rollback();
                throw e;
            } catch (IllegalArgumentException e) {
                con.rollback();
                throw new AccionNoPermitidaException(e.getMessage());
            }


        }
    }
}


