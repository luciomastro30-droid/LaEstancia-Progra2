package excepciones;

public class LoginInvalidoException extends Exception {
    public LoginInvalidoException() {
        super("El nombre o contraseña es incorrecto... Intentelo nuevamente");
    }
}
