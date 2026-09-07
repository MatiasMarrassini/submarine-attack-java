package Vista;

import java.io.File;
import java.util.HashMap;
import java.util.Map;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;

public class Sonido {

    private static Clip clipMusica;

    // creamos un map para cargar los sonidos en la ram, porque los efectos al hacerlos por hilos, llegaba un punto
    // que habia muchas explosiones y no se escuchaban bien los audios, o tardaban en escucharse.
    private static Map<String, Clip> efectosPrecargados = new HashMap<>();

    /**
     * Carga los audios en la RAM.
     */
    public static void precargarSonidos() {
        try {
            System.out.println("Precargando efectos de sonido en la RAM...");


            String[] archivos = {
                    "recursos/Sonido/explosion.wav",
                    "recursos/Sonido/nivel.wav",
                    "recursos/Sonido/final.wav",
                    "recursos/Sonido/pausa.wav",
                    "recursos/Sonido/pausaMusica.wav"
            };

            for (String ruta : archivos) {
                File archivo = new File(ruta);
                if (archivo.exists()) {
                    Clip clip = AudioSystem.getClip();
                    clip.open(AudioSystem.getAudioInputStream(archivo));
                    efectosPrecargados.put(ruta, clip);
                } else {
                    System.out.println("No se pudo precargar: " + ruta);
                }
            }
            System.out.println("¡Precarga completada con éxito!");
        } catch (Exception e) {
            System.out.println("Error al precargar sonidos: " + e.getMessage());
        }
    }

    /**
     * Reproduce un efecto de sonido.
     */
    public static void reproducirEfecto(String rutaArchivo, float volumen) {
        // Buscamos el audio cargado en la RAM
        Clip clip = efectosPrecargados.get(rutaArchivo);

        if (clip != null) {
            // Cramos un hilo para que el audio no trabje lo visual, para que se haga "por un costado"
            new Thread(() -> {
                synchronized (clip) {
                    // Si el sonido ya se estaba escuchando, lo ponemos del principio
                    if (clip.isRunning()) {
                        clip.stop();
                    }
                    clip.setFramePosition(0);

                    // Ajustamos el volumen
                    if (clip.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                        FloatControl gainControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
                        gainControl.setValue(volumen);
                    }

                    clip.start();
                }
            }).start();
        }

    }

    /**
     * Música de fondo en loop.
     */
    public static void iniciarMusica(String rutaArchivo, float volumenDb) {
        // Cramos un hilo para que el audio no trabje lo visual, para que se haga "por un costado"
        new Thread(() -> {
            try {
                detenerMusica();
                File archivo = new File(rutaArchivo);
                if (archivo.exists()) {
                    clipMusica = AudioSystem.getClip();
                    clipMusica.open(AudioSystem.getAudioInputStream(archivo));

                    // Configuramos el volumen
                    if (clipMusica.isControlSupported(FloatControl.Type.MASTER_GAIN)) {
                        FloatControl gainControl = (FloatControl) clipMusica.getControl(FloatControl.Type.MASTER_GAIN);
                        gainControl.setValue(volumenDb);
                    }

                    clipMusica.loop(Clip.LOOP_CONTINUOUSLY);
                    clipMusica.start();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public static void detenerMusica() {
        if (clipMusica != null && clipMusica.isRunning()) {
            clipMusica.stop();
            clipMusica.close();
        }
    }
}