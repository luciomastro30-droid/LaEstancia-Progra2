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
                      String contraseniaHash, boolean activo,
                      LocalDateTime fechaCreacion) {

        setIdUsuario(idUsuario);
        setNombreUsuario(nombreUsuario);
        setEmail(email);
        setContraseniaHash(contraseniaHash);

        if (fechaCreacion == null) {
            throw new IllegalArgumentException(
                    "La fecha de creacion no puede ser null."
            );
        }

        this.activo = activo;
        this.fechaCreacion = fechaCreacion;
    }

    //Este constructor completa los parametros en vacio y llama al constructor principal
    //Asigna valores por defecto para que luego sean autocompletados
    protected Usuario(String nombreUsuario, String email,
                      String contraseniaHash) {

        this(
                0,
                nombreUsuario,
                email,
                contraseniaHash,
                true,
                LocalDateTime.now().withNano(0)
        );
    }


    public abstract Rol getRol();


    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {

        // El 0 se permite porque un usuario nuevo todavia no tiene
        // el ID autogenerado por MySQL.
        if (idUsuario < 0) {
            throw new IllegalArgumentException(
                    "El ID del usuario no puede ser negativo."
            );
        }

        this.idUsuario = idUsuario;
    }


    public String getNombreUsuario() {
        return nombreUsuario;
    }

    public void setNombreUsuario(String nombreUsuario) {

        if (nombreUsuario == null || nombreUsuario.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre de usuario no puede estar vacio."
            );
        }

        // En MySQL nombre_usuario es VARCHAR(50).
        if (nombreUsuario.length() > 50) {
            throw new IllegalArgumentException(
                    "El nombre de usuario no puede superar los 50 caracteres."
            );
        }

        this.nombreUsuario = nombreUsuario.trim();
    }


    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {

        // El email puede ser null porque la base tambien lo permite.
        if (email != null) {

            if (email.isBlank()) {
                throw new IllegalArgumentException(
                        "El email no puede estar vacio."
                );
            }

            if (email.length() > 100) {
                throw new IllegalArgumentException(
                        "El email no puede superar los 100 caracteres."
                );
            }

            // Validacion simple para evitar agregar logica innecesariamente compleja.
            if (!email.contains("@") || email.startsWith("@") || email.endsWith("@")) {
                throw new IllegalArgumentException(
                        "El formato del email no es valido."
                );
            }
        }

        this.email = email.trim();
    }


    public String getContraseniaHash() {
        return contraseniaHash;
    }

    public void setContraseniaHash(String contraseniaHash) {

        if (contraseniaHash == null || contraseniaHash.isBlank()) {
            throw new IllegalArgumentException(
                    "El hash de la contrasenia no puede estar vacio."
            );
        }

        // La columna de MySQL es VARCHAR(255).
        if (contraseniaHash.length() > 255) {
            throw new IllegalArgumentException(
                    "El hash de la contrasenia supera el tamaño permitido."
            );
        }

        this.contraseniaHash = contraseniaHash;
    }


    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }


    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }


    @Override
    public String toString() {
        return "idUsuario=" + idUsuario +
                ", nombreUsuario='" + nombreUsuario + '\'' +
                ", email='" + email + '\'' +
                ", rol=" + getRol() +
                ", activo=" + activo +
                ", fechaCreacion=" + fechaCreacion;
    }
}