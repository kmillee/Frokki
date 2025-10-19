import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Sound {
    private Clip clip;
    private AudioInputStream sound;

    private Clip clipEnd;
    private AudioInputStream endSound;
    private FloatControl gainControl;

    public Sound(String filename) {
        setFile(filename);
    }

    public void setFile(String soundFileName) {
        try {
            // Main bell loop
            File file = new File(soundFileName);
            sound = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(sound);

            // Short bell at the end
            file = new File("media/sound/bell_short.wav");
            endSound = AudioSystem.getAudioInputStream(file);
            clipEnd = AudioSystem.getClip();
            clipEnd.open(endSound);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void play() {
        if (clip == null) return;
        if (!clip.isRunning()) {
            clip.setFramePosition(0);
            clip.loop(Clip.LOOP_CONTINUOUSLY);
        }
    }

    public void pause() {
        if (clip != null && clip.isRunning()) {
            clip.stop();
            playEndBell();
        }
    }

    public void stop() throws IOException {
        if (clip != null) {
            clip.stop();
            clip.close();
        }
        if (sound != null) sound.close();
        playEndBell();
    }

    /** Plays bell_short once */
    private void playEndBell() {
        if (clipEnd == null) return;
        clipEnd.stop();              // stop if already playing
        clipEnd.setFramePosition(0); // restart from beginning
        clipEnd.start();             // play once
    }

}
