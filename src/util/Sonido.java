package util;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.LineEvent;
import java.io.File;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;


  //Efecto de sonido
 //Mientras suena el efecto, la radio se pausa.
 //Cuando termina, la radio se reanuda.

public class Sonido {

    /*
     * Reproduce un .wav y espera a que termine.
     * Si el archivo no existe o no hay sonido,
     * no hace nada y el juego sigue igual.
     */
    public static void reproducirYEsperar(String ruta) {

        File archivo = new File(ruta);
        if (!archivo.exists()) {
            return;
        }

        Radio.pausar();

        try (AudioInputStream audio =
                     AudioSystem.getAudioInputStream(archivo)) {

            Clip clip = AudioSystem.getClip();
            clip.open(audio);

            // el latch se libera cuando el clip termina
            CountDownLatch termino = new CountDownLatch(1);
            clip.addLineListener(evento -> {
                if (evento.getType() == LineEvent.Type.STOP) {
                    termino.countDown();
                }
            });

            clip.start();

            // espero lo que dura el audio, mas un margen
            long duracion = clip.getMicrosecondLength() / 1000;
            termino.await(duracion + 2000, TimeUnit.MILLISECONDS);

            clip.close();

        } catch (Exception e) {
            // sin sonido: se sigue jugando sin el efecto
        }

        Radio.reanudar();
    }
}