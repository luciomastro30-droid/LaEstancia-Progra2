package modelo;

import modelo.enums.Rol;
import java.time.LocalDateTime;

public class Jugador extends Usuario {

    private Partida partidaActual;

    public Jugador(int idUsuario, String nombreUsuario, String email, String contraseniaHash, boolean activo,
                   LocalDateTime fechaCreacion) {
        super(idUsuario, nombreUsuario, email, contraseniaHash, activo, fechaCreacion);

    }

    public Jugador(String nombreUsuario, String email, String contraseniaHash) {
        super(nombreUsuario, email, contraseniaHash);
    }

    @Override
    public Rol getRol() {return Rol.JUGADOR;}

    public boolean tienePartidaActual() {return partidaActual != null;}

    public Partida getPartidaActual() {return partidaActual;}
    public void setPartidaActual(Partida partidaActual) {this.partidaActual = partidaActual;}

    @Override
    public String toString() {
        return "Jugador{" + super.toString() +
                ", tienePartida=" + tienePartidaActual() +
                '}';
    }
}
