package modelo;

import modelo.enums.Rol;
import java.time.LocalDateTime;

public class Jugador extends Usuario {

    private Partida partida;

    public Jugador(int idUsuario, String nombreUsuario, String email, String contraseniaHash, boolean activo,
                   LocalDateTime fechaCreacion) {
        super(idUsuario, nombreUsuario, email, contraseniaHash, activo, fechaCreacion);

    }

    public Jugador(String nombreUsuario, String email, String contraseniaHash) {
        super(nombreUsuario, email, contraseniaHash);
    }

    @Override
    public Rol getRol() {return Rol.JUGADOR;}

    public boolean tienePartida() {return partida != null;}

    public Partida getPartida() {return partida;}
    public void setPartida(Partida partida) {this.partida = partida;}
}
