package menu.menuJugador;

import util.Radio;

/**
 * Proporciona las opciones del menu relacionadas con la radio.
 * Permite mostrar su estado actual y alternar entre encendida y apagada.
 */
public class MenuRadio {


    //opcion de radio
    public static String opcion(int numero) {
        return "  " + numero + ". Radio: " + (Radio.isEncendida() ? "ON" : "OFF");
    }

    // prende o apaga la radio y devuelve el mensaje para mostrar en pantalla
    public static String alternar() {
        if (Radio.alternar()) {
            return "Radio prendida. Suena: " + Radio.getTemaActual();
        }
        if (!Radio.hayCanciones()) {
            return "No hay canciones. Pone archivos .wav en la carpeta musica.";
        }
        return "Radio apagada.";
    }
}