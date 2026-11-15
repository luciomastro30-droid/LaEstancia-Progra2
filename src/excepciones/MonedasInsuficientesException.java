package excepciones;

public class MonedasInsuficientesException extends Exception {

    public MonedasInsuficientesException(int necesarias, int disponibles) {
        super("Monedas insuficientes: necesitas " + necesarias + " y tenes " + disponibles + ".");
    }
}