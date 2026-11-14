package excepciones;

public class StockInsuficienteException extends Exception {
    public StockInsuficienteException(int queres,int disponible ) {
        super("Stock insuficiente: pediste " + queres + " y tenes " + disponible + ".");
    }
}
