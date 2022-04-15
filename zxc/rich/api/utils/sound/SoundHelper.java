package zxc.rich.api.utils.sound;

import zxc.rich.Main;

import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import java.io.BufferedInputStream;
import java.io.InputStream;

public class SoundHelper {

    public static synchronized void playSound(final String url, final float volume, final boolean stop) {
        final Clip[] clip = new Clip[1];
        final InputStream[] audioSrc = new InputStream[1];
        final InputStream[] bufferedIn = new InputStream[1];
        final AudioInputStream[] inputStream = new AudioInputStream[1];
        final FloatControl[] gainControl = new FloatControl[1];
        new Thread(() -> {
            try {
                clip[0] = AudioSystem.getClip();
                audioSrc[0] = SoundHelper.class.getResourceAsStream("/assets/minecraft/rich/songs/" + url);
                bufferedIn[0] = new BufferedInputStream(audioSrc[0]);
                inputStream[0] = AudioSystem.getAudioInputStream(bufferedIn[0]);
                clip[0].open(inputStream[0]);
                gainControl[0] = (FloatControl) clip[0].getControl(FloatControl.Type.MASTER_GAIN);
                gainControl[0].setValue(volume);
                clip[0].start();
                if (stop) {
                    clip[0].stop();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }
}