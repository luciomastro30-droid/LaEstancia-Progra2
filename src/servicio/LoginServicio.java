package servicio;

import dao.PartidaDao;
import dao.UsuarioDao;
import excepciones.LoginInvalidoException;
import modelo.Jugador;
import modelo.Partida;
import modelo.Usuario;
import util.Seguridad;

import java.sql.SQLException;
import java.util.List;

/**
 * Gestiona el inicio de sesión de los usuarios del sistema.
 * Valida las credenciales y, si el usuario es un jugador,
 * carga su partida asociada.
 */
public class LoginServicio {

    public Usuario iniciarSesion(String nombreUsuario, String contrasenia)
            throws LoginInvalidoException, SQLException {

        if ((nombreUsuario == null || nombreUsuario.isBlank())
                || (contrasenia == null || contrasenia.isBlank())) {
            throw new LoginInvalidoException();
        }

        UsuarioDao usuarioDao = new UsuarioDao();
        Usuario nuevoUsuario = usuarioDao.consultarPorNombreUsuario(nombreUsuario);

        if (nuevoUsuario == null) {
            throw new LoginInvalidoException();
        }

        // La contraseña ingresada se hashea antes de compararla
        // con la almacenada en la base de datos.
        String passHash = Seguridad.hashear(contrasenia);

        if (!passHash.equalsIgnoreCase(nuevoUsuario.getContraseniaHash())) {
            throw new LoginInvalidoException();
        }

        // Los jugadores recuperan su partida para poder continuar
        // con el estado que tenían guardado.
        PartidaDao partidaDao = new PartidaDao();

        if (nuevoUsuario instanceof Jugador jugador) {
            List<Partida> partidas =
                    partidaDao.listarPorUsuario(nuevoUsuario.getIdUsuario());

            if (!partidas.isEmpty()) {
                jugador.setPartidaActual(partidas.get(0));
            }
        }

        return nuevoUsuario;
    }
}