import javax.sound.sampled.*;
import java.io.File;
import java.io.IOException;

public class Sound {
    Clip clip;
    AudioInputStream sound;

    public Sound(String filename){
        setFile(filename);
    }

    public void setFile(String soundFileName) {
        try {
            File file = new File(soundFileName);
            sound = AudioSystem.getAudioInputStream(file);
            clip = AudioSystem.getClip();
            clip.open(sound);
        } catch (Exception e) {

        }
    }

    public void play() {
        clip.start();
        clip.loop(Clip.LOOP_CONTINUOUSLY);
    }

    public void pause() {
        clip.stop();
    }

    public void stop() throws IOException {
        sound.close();
        clip.close();
        clip.stop();
    }
}