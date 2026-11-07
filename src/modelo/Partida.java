package modelo;

import modelo.enums.EstadoPartida;
import modelo.enums.Rareza;

import java.time.LocalDateTime;

public class Partida {

    public static final int MONEDAS_INICIALES = 500;
    public static final int NIVEL_INICIAL = 1;
    public static final int MINUTO_INICIAL = 480; // Dia 0, 08:00
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
    private Integer diaUltimoEvento;
    private EstadoPartida estado;
    private LocalDateTime fechaCreacion;
    private LocalDateTime fechaGuardado;
    private boolean activo;


    public Partida(int idPartida, int idUsuario, String nombreGranja,
                   int nivel, int experiencia, int monedas,
                   long minutoJuegoTotal, int parcelasDesbloqueadas,
                   int nivelTerreno, boolean tieneGallinero,
                   boolean tieneEstablo, Rareza rarezaHacha,
                   Rareza rarezaPico, Rareza rarezaCania,
                   Integer diaUltimoEvento, EstadoPartida estado,
                   LocalDateTime fechaCreacion,
                   LocalDateTime fechaGuardado, boolean activo) {

        // Uso los setters para aplicar las mismas validaciones
        // tanto al crear como al modificar una partida.
        setIdPartida(idPartida);
        setIdUsuario(idUsuario);
        setNombreGranja(nombreGranja);
        setNivel(nivel);
        setExperiencia(experiencia);
        setMonedas(monedas);
        setMinutoJuegoTotal(minutoJuegoTotal);
        setParcelasDesbloqueadas(parcelasDesbloqueadas);
        setNivelTerreno(nivelTerreno);

        this.tieneGallinero = tieneGallinero;
        this.tieneEstablo = tieneEstablo;

        setRarezaHacha(rarezaHacha);
        setRarezaPico(rarezaPico);
        setRarezaCania(rarezaCania);
        setDiaUltimoEvento(diaUltimoEvento);
        setEstado(estado);

        if (fechaCreacion == null) {
            throw new IllegalArgumentException(
                    "La fecha de creacion no puede ser null."
            );
        }

        this.fechaCreacion = fechaCreacion;
        this.fechaGuardado = fechaGuardado;
        this.activo = activo;
    }


    public Partida(int idUsuario, String nombreGranja) {

        this(
                0,
                idUsuario,
                nombreGranja,
                NIVEL_INICIAL,
                0,
                MONEDAS_INICIALES,
                MINUTO_INICIAL,
                PARCELAS_INICIALES,
                NIVEL_TERRENO_INICIAL,
                false,
                false,
                Rareza.COMUN,
                Rareza.COMUN,
                Rareza.COMUN,
                null,
                EstadoPartida.ACTIVA,
                LocalDateTime.now().withNano(0),
                null,
                true
        );
    }


    public int getIdPartida() {
        return idPartida;
    }

    public void setIdPartida(int idPartida) {

        // Permito 0 porque una partida nueva todavia
        // no tiene el ID generado por MySQL.
        if (idPartida < 0) {
            throw new IllegalArgumentException(
                    "El ID de la partida no puede ser negativo."
            );
        }

        this.idPartida = idPartida;
    }


    public int getIdUsuario() {
        return idUsuario;
    }

    public void setIdUsuario(int idUsuario) {

        // Una partida siempre tiene que pertenecer a un usuario existente.
        if (idUsuario <= 0) {
            throw new IllegalArgumentException(
                    "El ID del usuario debe ser mayor a 0."
            );
        }

        this.idUsuario = idUsuario;
    }


    public String getNombreGranja() {
        return nombreGranja;
    }

    public void setNombreGranja(String nombreGranja) {

        if (nombreGranja == null || nombreGranja.isBlank()) {
            throw new IllegalArgumentException(
                    "El nombre de la granja no puede estar vacio."
            );
        }

        // En MySQL nombre_granja es VARCHAR(100).
        if (nombreGranja.length() > 100) {
            throw new IllegalArgumentException(
                    "El nombre de la granja no puede superar los 100 caracteres."
            );
        }

        this.nombreGranja = nombreGranja.trim();
    }


    public int getNivel() {
        return nivel;
    }

    public void setNivel(int nivel) {

        if (nivel < 1) {
            throw new IllegalArgumentException(
                    "El nivel de la partida debe ser al menos 1."
            );
        }

        this.nivel = nivel;
    }


    public int getExperiencia() {
        return experiencia;
    }

    public void setExperiencia(int experiencia) {

        if (experiencia < 0) {
            throw new IllegalArgumentException(
                    "La experiencia no puede ser negativa."
            );
        }

        this.experiencia = experiencia;
    }


    public int getMonedas() {
        return monedas;
    }

    public void setMonedas(int monedas) {

        if (monedas < 0) {
            throw new IllegalArgumentException(
                    "Las monedas no pueden ser negativas."
            );
        }

        this.monedas = monedas;
    }


    public long getMinutoJuegoTotal() {
        return minutoJuegoTotal;
    }

    public void setMinutoJuegoTotal(long minutoJuegoTotal) {

        if (minutoJuegoTotal < 0) {
            throw new IllegalArgumentException(
                    "El tiempo total del juego no puede ser negativo."
            );
        }

        this.minutoJuegoTotal = minutoJuegoTotal;
    }


    public int getParcelasDesbloqueadas() {
        return parcelasDesbloqueadas;
    }

    public void setParcelasDesbloqueadas(int parcelasDesbloqueadas) {

        if (parcelasDesbloqueadas < 1) {
            throw new IllegalArgumentException(
                    "La partida debe tener al menos una parcela desbloqueada."
            );
        }

        this.parcelasDesbloqueadas = parcelasDesbloqueadas;
    }


    public int getNivelTerreno() {
        return nivelTerreno;
    }

    public void setNivelTerreno(int nivelTerreno) {

        if (nivelTerreno < 1) {
            throw new IllegalArgumentException(
                    "El nivel del terreno debe ser al menos 1."
            );
        }

        this.nivelTerreno = nivelTerreno;
    }


    public boolean isTieneGallinero() {
        return tieneGallinero;
    }

    public void setTieneGallinero(boolean tieneGallinero) {
        this.tieneGallinero = tieneGallinero;
    }


    public boolean isTieneEstablo() {
        return tieneEstablo;
    }

    public void setTieneEstablo(boolean tieneEstablo) {
        this.tieneEstablo = tieneEstablo;
    }


    public Rareza getRarezaHacha() {
        return rarezaHacha;
    }

    public void setRarezaHacha(Rareza rarezaHacha) {

        if (rarezaHacha == null) {
            throw new IllegalArgumentException(
                    "La rareza del hacha no puede ser null."
            );
        }

        this.rarezaHacha = rarezaHacha;
    }


    public Rareza getRarezaPico() {
        return rarezaPico;
    }

    public void setRarezaPico(Rareza rarezaPico) {

        if (rarezaPico == null) {
            throw new IllegalArgumentException(
                    "La rareza del pico no puede ser null."
            );
        }

        this.rarezaPico = rarezaPico;
    }


    public Rareza getRarezaCania() {
        return rarezaCania;
    }

    public void setRarezaCania(Rareza rarezaCania) {

        if (rarezaCania == null) {
            throw new IllegalArgumentException(
                    "La rareza de la caña no puede ser null."
            );
        }

        this.rarezaCania = rarezaCania;
    }


    public Integer getDiaUltimoEvento() {
        return diaUltimoEvento;
    }

    public void setDiaUltimoEvento(Integer diaUltimoEvento) {

        // Puede ser null porque null significa que todavia
        // no ocurrio ningun evento aleatorio.
        if (diaUltimoEvento != null && diaUltimoEvento < 0) {
            throw new IllegalArgumentException(
                    "El dia del ultimo evento no puede ser negativo."
            );
        }

        this.diaUltimoEvento = diaUltimoEvento;
    }


    public EstadoPartida getEstado() {
        return estado;
    }

    public void setEstado(EstadoPartida estado) {

        if (estado == null) {
            throw new IllegalArgumentException(
                    "El estado de la partida no puede ser null."
            );
        }

        this.estado = estado;
    }


    // No agrego setter porque la fecha de creacion
    // no deberia modificarse una vez creada la partida.
    public LocalDateTime getFechaCreacion() {
        return fechaCreacion;
    }


    public LocalDateTime getFechaGuardado() {
        return fechaGuardado;
    }

    public void setFechaGuardado(LocalDateTime fechaGuardado) {

        // Puede ser null porque una partida nueva
        // todavia puede no haber sido guardada.
        this.fechaGuardado = fechaGuardado;
    }


    public boolean isActivo() {
        return activo;
    }

    public void setActivo(boolean activo) {
        this.activo = activo;
    }


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