package modelo;

import modelo.enums.Rol;
import java.time.LocalDateTime;


public abstract class Usuario {

    private int idUsuario;
    private String nombreUsuario;
    private String email;
    private String contraseniaHash;
    private boolean activo;
    private LocalDateTime fechaCreacion;

    protected Usuario(int idUsuario, String nombreUsuario, String email,
                      String contraseniaHash, boolean activo, LocalDateTime fechaCreacion) {

        this.idUsuario = idUsuario;
        this.nombreUsuario = nombreUsuario;
        this.email = email;
        this.contraseniaHash = contraseniaHash;
        this.activo = activo;
        this.fechaCreacion = fechaCreacion;
    }

    //Este constructor completa los parametros en vacio y llama al constructor principal
    //Asigna valores por defecto para que luego sean autocompletados
    protected Usuario(String nombreUsuario, String email, String contraseniaHash) {

        this(0,nombreUsuario,email,contraseniaHash,true,LocalDateTime.now());
    }

    public abstract Rol getRol();

    public int getIdUsuario() {return idUsuario;}
    public void setIdUsuario(int idUsuario) {this.idUsuario = idUsuario;}

    public String getNombreUsuario() {return nombreUsuario;}
    public void setNombreUsuario(String nombreUsuario) {this.nombreUsuario = nombreUsuario;}

    public String getEmail() {return email;}
    public void setEmail(String email) {this.email = email;}

    public String getContraseniaHash() {return contraseniaHash;}
    public void setContraseniaHash(String contraseniaHash) {this.contraseniaHash = contraseniaHash;}

    public boolean isActivo() {return activo;}
    public void setActivo(boolean activo) {this.activo = activo;}

    //SIN SET PARA NO MODIFICAR
    public LocalDateTime getFechaCreacion() {return fechaCreacion;}

    @Override
    public String toString() {
        return getRol() + " #" + idUsuario + " - " + nombreUsuario + " (" + email + ")";
    }
}
