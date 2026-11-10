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

public class LoginServicio {

    public Usuario iniciarSesion(String nombreUsuario, String contrasenia) throws LoginInvalidoException, SQLException {

        if((nombreUsuario == null || nombreUsuario.isBlank()) || ((contrasenia == null || contrasenia.isBlank()))){
            throw new LoginInvalidoException();
        }

        UsuarioDao usuarioDao = new UsuarioDao();
        Usuario nuevoUsuario = usuarioDao.consultarPorNombreUsuario(nombreUsuario);

        if(nuevoUsuario == null){
            throw new LoginInvalidoException();
        }

        String passHash = Seguridad.hashear(contrasenia);
        if(!passHash.equalsIgnoreCase(nuevoUsuario.getContraseniaHash())){
            throw new LoginInvalidoException();
        }

        PartidaDao partidaDao = new PartidaDao();
        if(nuevoUsuario instanceof Jugador jugador){
            List<Partida> partidas = partidaDao.listarPorUsuario(nuevoUsuario.getIdUsuario());
            if(!partidas.isEmpty()){
                jugador.setPartidaActual(partidas.get(0));
            }
        }

        return nuevoUsuario;
    }



}
