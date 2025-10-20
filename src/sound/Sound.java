package sound;

import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Sound {
    public Clip clip;
    private AudioInputStream sound;

    private Clip clipEnd;
    private AudioInputStream endSound;
    private FloatControl gainControl;

    public Sound(String filename) {
        setFile(filename);
    }

    public void setFile(String soundFileName) {
        try {
            File file = new File(soundFileName);
            sound = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(sound);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (clip == null) return;
        clip.setFramePosition(0); // rewind to start
        clip.start();
    }


    public void pause() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
        }
    }

    public void stop() throws IOException {
        if (clip != null) {
            clip.stop();
            clip.close();
        }
        if (sound != null) sound.close();
    }

}
