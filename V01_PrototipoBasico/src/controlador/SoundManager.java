package controlador;
import java.awt.Toolkit;
import javax.sound.sampled.*;

public class SoundManager {

    /** 🔴 Beep estándar del sistema (error, muy sencillo) */
    public static void error() {
        Toolkit.getDefaultToolkit().beep();
    }

    /** 🟢 Tono de éxito (ejemplo: 1000 Hz, 200 ms) */
    public static void success() {
        playTone(1000, 200);
    }

    /** 🟡 Tono de advertencia (ejemplo: 400 Hz, 800 ms) */
    public static void warning() {
        playTone(400, 800);
    }

    /**
     * Genera un tono de una frecuencia y duración determinada
     * @param hz frecuencia en Hz (ej: 800)
     * @param msecs duración en milisegundos
     */
    private static void playTone(int hz, int msecs) {
        try {
            float sampleRate = 44100;
            byte[] buf = new byte[1];
            AudioFormat af = new AudioFormat(sampleRate, 8, 1, true, false);
            SourceDataLine sdl = AudioSystem.getSourceDataLine(af);
            sdl.open(af);
            sdl.start();
            for (int i = 0; i < msecs * 8; i++) {
                double angle = i / (sampleRate / hz) * 2.0 * Math.PI;
                buf[0] = (byte) (Math.sin(angle) * 127.0);
                sdl.write(buf, 0, 1);
            }
            sdl.drain();
            sdl.stop();
            sdl.close();
        } catch (Exception e) {
            Toolkit.getDefaultToolkit().beep(); // fallback
        }
    }
}
