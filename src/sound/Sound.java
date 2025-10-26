package sound;

import javax.sound.sampled.*;
import java.io.File;

public class Sound {
    private static boolean mute = false; // mute menu button
    public Clip clip;

    public Sound(String filename) {
        setFile(filename);
    }

    public void setFile(String soundFileName) {
        try {
            File file = new File(soundFileName);
            AudioInputStream sound = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(sound);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (clip == null || mute) return;
        clip.setFramePosition(0); // rewind to start
        clip.start();
    }


    public void pause() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    public static void setMute(boolean b) {
        mute = b;
    }

    public static boolean isMute() {
        return mute;
    }

}
