package modelo;

import modelo.enums.Rol;

import java.time.LocalDateTime;

public class Administrador extends Usuario {

    public Administrador(int idUsuario, String nombreUsuario, String email,
                         String contraseniaHash, boolean activo, LocalDateTime fechaCreacion) {
        super(idUsuario, nombreUsuario, email, contraseniaHash, activo, fechaCreacion);
    }

    public Administrador(String nombreUsuario, String email, String contraseniaHash) {
        super(nombreUsuario, email, contraseniaHash);
    }

    @Override
    public Rol getRol() {return Rol.ADMIN;}
}
