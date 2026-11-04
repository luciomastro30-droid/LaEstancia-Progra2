package modelo;

import modelo.enums.EstadoPartida;
import modelo.enums.Rareza;

import java.time.LocalDateTime;

public class Partida {

    public static final int MONEDAS_INICIALES = 500;
    public static final int NIVEL_INICIAL = 1;
    public static final int MINUTO_INICIAL = 480; //Dia 0, 08:00
    public static final int PARCELAS_INICIALES = 2;
    public static final int NIVEL_TERRENO_INICIAL = 1;


    private int idPartida;
    private int idUsuario;
    private String nombreGranja;
    private int nivel;
    private int experiencia;
    private int monedas;
    private long minutoJuegoTotal;
    private int parcelasDesbloqueadas;
    private int nivelTerreno;
    private boolean tieneGallinero;
    private boolean tieneEstablo;
    private Rareza rarezaHacha;
    private Rareza rarezaPico;
    private Rareza rarezaCania;
    private Integer diaUltimoEvento; //puede ser null
    private EstadoPartida estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaGuardado;
    private boolean activo;

    public Partida(int idPartida, int idUsuario, String nombreGranja, int nivel, int experiencia,
                   int monedas, long minutoJuegoTotal, int parcelasDesbloqueadas, int nivelTerreno,
                   boolean tieneGallinero, boolean tieneEstablo, Rareza rarezaHacha,
                   Rareza rarezaPico, Rareza rarezaCania, Integer diaUltimoEvento,
                   EstadoPartida estado, LocalDateTime fechaCreacion,
                   LocalDateTime fechaGuardado, boolean activo) {
        this.idPartida = idPartida;
        this.idUsuario = idUsuario;
        this.nombreGranja = nombreGranja;
        this.nivel = nivel;
        this.experiencia = experiencia;
        this.monedas = monedas;
        this.minutoJuegoTotal = minutoJuegoTotal;
        this.parcelasDesbloqueadas = parcelasDesbloqueadas;
        this.nivelTerreno = nivelTerreno;
        this.tieneGallinero = tieneGallinero;
        this.tieneEstablo = tieneEstablo;
        this.rarezaHacha = rarezaHacha;
        this.rarezaPico = rarezaPico;
        this.rarezaCania = rarezaCania;
        this.diaUltimoEvento = diaUltimoEvento;
        this.estado = estado;
        this.fechaCreacion = fechaCreacion;
        this.fechaGuardado = fechaGuardado;
        this.activo = activo;
    }

    public Partida(int idUsuario, String nombreGranja) {
        this(0, idUsuario, nombreGranja, NIVEL_INICIAL, 0, MONEDAS_INICIALES, MINUTO_INICIAL, PARCELAS_INICIALES, NIVEL_TERRENO_INICIAL,
                false, false, Rareza.COMUN, Rareza.COMUN, Rareza.COMUN,
                null, EstadoPartida.ACTIVA, LocalDateTime.now(), null, true);
    }

    public int getIdPartida() {return idPartida;}
    public void setIdPartida(int idPartida) {this.idPartida = idPartida;}

    public int getIdUsuario() {return idUsuario;}
    public void setIdUsuario(int idUsuario) {this.idUsuario = idUsuario;}

    public String getNombreGranja() {return nombreGranja;}
    public void setNombreGranja(String nombreGranja) {this.nombreGranja = nombreGranja;}

    public int getNivel() {return nivel;}
    public void setNivel(int nivel) {this.nivel = nivel;}

    public int getExperiencia() {return experiencia;}
    public void setExperiencia(int experiencia) {this.experiencia = experiencia;}

    public int getMonedas() {return monedas;}
    public void setMonedas(int monedas) {this.monedas = monedas;}

    public long getMinutoJuegoTotal() {return minutoJuegoTotal;}
    public void setMinutoJuegoTotal(long minutoJuegoTotal) {this.minutoJuegoTotal = minutoJuegoTotal;}

    public int getParcelasDesbloqueadas() {return parcelasDesbloqueadas;}
    public void setParcelasDesbloqueadas(int parcelasDesbloqueadas) {this.parcelasDesbloqueadas = parcelasDesbloqueadas;}

    public int getNivelTerreno() {return nivelTerreno;}
    public void setNivelTerreno(int nivelTerreno) {this.nivelTerreno = nivelTerreno;}

    public boolean isTieneGallinero() {return tieneGallinero;}
    public void setTieneGallinero(boolean tieneGallinero) {this.tieneGallinero = tieneGallinero;}

    public boolean isTieneEstablo() {return tieneEstablo;}
    public void setTieneEstablo(boolean tieneEstablo) {this.tieneEstablo = tieneEstablo;}

    public Rareza getRarezaHacha() {return rarezaHacha;}
    public void setRarezaHacha(Rareza rarezaHacha) {this.rarezaHacha = rarezaHacha;}

    public Rareza getRarezaPico() {return rarezaPico;}
    public void setRarezaPico(Rareza rarezaPico) {this.rarezaPico = rarezaPico;}

    public Rareza getRarezaCania() {return rarezaCania;}
    public void setRarezaCania(Rareza rarezaCania) {this.rarezaCania = rarezaCania;}

    public Integer getDiaUltimoEvento() {return diaUltimoEvento;}
    public void setDiaUltimoEvento(Integer diaUltimoEvento) {this.diaUltimoEvento = diaUltimoEvento;}

    public EstadoPartida getEstado() {return estado;}
    public void setEstado(EstadoPartida estado) {this.estado = estado;}

    //SIN SET PARA NO MODIFICAR
    public LocalDateTime getFechaCreacion() {return fechaCreacion;}

    public LocalDateTime getFechaGuardado() {return fechaGuardado;}
    public void setFechaGuardado(LocalDateTime fechaGuardado) {this.fechaGuardado = fechaGuardado;}

    public boolean isActivo() {return activo;}
    public void setActivo(boolean activo) {this.activo = activo;}

    @Override
    public String toString() {
        return "Partida{idPartida=" + idPartida +
                ", idUsuario=" + idUsuario +
                ", nombreGranja='" + nombreGranja + '\'' +
                ", nivel=" + nivel +
                ", experiencia=" + experiencia +
                ", monedas=" + monedas +
                ", minutoJuegoTotal=" + minutoJuegoTotal +
                ", parcelasDesbloqueadas=" + parcelasDesbloqueadas +
                ", nivelTerreno=" + nivelTerreno +
                ", tieneGallinero=" + tieneGallinero +
                ", tieneEstablo=" + tieneEstablo +
                ", rarezaHacha=" + rarezaHacha +
                ", rarezaPico=" + rarezaPico +
                ", rarezaCania=" + rarezaCania +
                ", diaUltimoEvento=" + diaUltimoEvento +
                ", estado=" + estado +
                ", fechaCreacion=" + fechaCreacion +
                ", fechaGuardado=" + fechaGuardado +
                ", activo=" + activo +
                '}';
    }
}
