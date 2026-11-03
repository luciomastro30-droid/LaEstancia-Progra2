package modelo;

public class Cultivo extends ElementoGranja{

    private int precioSemilla;
    private int tiempoCrecimientoMin;
    private int experienciaRecompensa;
    private Integer idItemSemilla; //puede ser null
    private Integer idItemCosecha;  //puede ser null

    public Cultivo(int id, String nombre,int precioSemilla,int precioVenta,
                   int tiempoCrecimientoMin, int experienciaRecompensa, Integer idItemSemilla,
                   Integer idItemCosecha,boolean activo) {
        super(id, nombre, precioVenta, activo);
        this.precioSemilla = precioSemilla;
        this.tiempoCrecimientoMin = tiempoCrecimientoMin;
        this.experienciaRecompensa = experienciaRecompensa;
        this.idItemSemilla = idItemSemilla;
        this.idItemCosecha = idItemCosecha;
    }

    public Cultivo(String nombre, int precioSemilla, int precioVenta, int tiempoCrecimientoMin,
                   int experienciaRecompensa, Integer idItemSemilla, Integer idItemCosecha) {
        this(0, nombre, precioSemilla, precioVenta, tiempoCrecimientoMin,
                experienciaRecompensa, idItemSemilla, idItemCosecha, true);
    }

    @Override
    public int getPrecioCompra() {return precioSemilla;}

    @Override
    public int getTiempoProduccionMin() {return tiempoCrecimientoMin;}

    public int getPrecioSemilla() {return precioSemilla;}
    public void setPrecioSemilla(int precioSemilla) {this.precioSemilla = precioSemilla;}

    public int getTiempoCrecimientoMin() {return tiempoCrecimientoMin;}
    public void setTiempoCrecimientoMin(int tiempoCrecimientoMin) {this.tiempoCrecimientoMin = tiempoCrecimientoMin;}

    public int getExperienciaRecompensa() {return experienciaRecompensa;}
    public void setExperienciaRecompensa(int experienciaRecompensa) {this.experienciaRecompensa = experienciaRecompensa;}

    public Integer getIdItemSemilla() {return idItemSemilla;}
    public void setIdItemSemilla(Integer idItemSemilla) {this.idItemSemilla = idItemSemilla;}

    public Integer getIdItemCosecha() {return idItemCosecha;}
    public void setIdItemCosecha(Integer idItemCosecha) {this.idItemCosecha = idItemCosecha;}
}
