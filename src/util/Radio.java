package util;


import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Controla la radio del juego y reproduce en orden las canciones
 * con formato .wav disponibles dentro de la carpeta "musica".
 *
 * Permite encender, apagar, pausar y reanudar la reproduccion,
 * pasando automaticamente a la siguiente cancion cuando termina la actual.
 */
//Toca en bucle todas las canciones .wav de la carpeta "musica" (en orden alfabetico).
//No se elige el tema: cuando una cancion termina pasa sola a la siguiente,
//y si se apaga y se vuelve a prender, arranca la siguiente.
public class Radio {

    private static final String CARPETA = "musica";
    private static List<File> canciones = new ArrayList<>();
    private static int indiceActual = -1;
    private static Clip clip;
    private static boolean encendida = false;
    private static boolean pausada= false;

    //prende la radio y toca la siguiente cancion de la lista
    public static synchronized void encender(){
        if(encendida){
            return;
        }
        cargarCanciones();
        if(canciones.isEmpty()) {
            return;
        }
        encendida = true;
        pausada = false;
        siguiente();
    }

    // apaga la radio, al volver a prenderla sigue con la proxima cancion
    public static synchronized void apagar() {
        encendida = false;
        pausada = false;
        cerrarClip();
    }

    // prende o apaga la radio, devuelve true si quedo prendida
    public static synchronized boolean alternar(){
        if(encendida){
            apagar();
        }else{
            encender();
        }
        return encendida;

    }

    // frena la cancion un momento sin perder por donde iba
    public static synchronized  void pausar(){
        if(encendida && clip != null && clip.isRunning()){
            pausada = true;
            clip.stop();
        }
    }

    // sigue la cancion desde donde quedo
    public static synchronized void reanudar() {
        if (encendida && pausada && clip != null) {
            pausada = false;
            clip.start();
        }
    }

    // true si hay al menos una cancion .wav en la carpeta
    public static synchronized boolean hayCanciones() {
        cargarCanciones();
        return !canciones.isEmpty();
    }

    public static synchronized boolean isEncendida(){
        return encendida;
    }

    // nombre del tema que suena
    public static synchronized String getTemaActual() {
        if (!encendida || indiceActual < 0 || indiceActual >= canciones.size()) {
            return "";
        }
        String nombre = canciones.get(indiceActual).getName();
        return nombre.substring(0, nombre.length() - 4).replace('_', ' ');
    }

    // lee la carpeta para agregar las canciones sin codigo
    private static void cargarCanciones() {
        File carpeta = new File(CARPETA);
        File[] archivos = carpeta.listFiles((dir, nombre) -> nombre.toLowerCase().endsWith(".wav"));
        canciones = new ArrayList<>();
        if (archivos != null) {
            Arrays.sort(archivos);
            canciones.addAll(Arrays.asList(archivos));
        }
        if (indiceActual >= canciones.size()) {
            indiceActual = -1;
        }
    }

    // pasa a la proxima cancion, si una no se puede abrir, prueba con la siguiente
    private static void siguiente() {
        cerrarClip();
        for (int intentos = 0; intentos < canciones.size(); intentos++) {
            indiceActual = (indiceActual + 1) % canciones.size();
            if (tocar(canciones.get(indiceActual))) {
                return;
            }
        }
        // ninguna cancion se pudo reproducir, la radio queda apagada
        encendida = false;
    }

    private static boolean tocar(File archivo) {
        try (AudioInputStream audio = AudioSystem.getAudioInputStream(archivo)) {
            Clip nuevo = AudioSystem.getClip();
            nuevo.open(audio);
            nuevo.addLineListener(evento -> {
                // STOP llega cuando la cancion termina, pero tambien cuando se apaga o se pausa
                if (evento.getType() == LineEvent.Type.STOP && termino(nuevo)) {
                    // se cambia de cancion en otro hilo para no trabar el de audio
                    new Thread(Radio::pasarSiSigueSonando).start();
                }
            });
            clip = nuevo;
            clip.start();
            return true;
        } catch (UnsupportedAudioFileException | IOException | LineUnavailableException | IllegalArgumentException e) {
            return false;
        }
    }

    private static boolean termino(Clip c) {
        return c.getFramePosition() >= c.getFrameLength();
    }

    private static synchronized void pasarSiSigueSonando() {
        if (encendida && !pausada) {
            siguiente();
        }
    }

    private static void cerrarClip() {
        if (clip != null) {
            Clip viejo = clip;
            clip = null;
            viejo.stop();
            viejo.close();
        }
    }

}