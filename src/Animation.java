import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Animation {
    private final List<ImageIcon> frames;

    private int currentFrameIndex = 0;
    private final int frameDelay; // delay between two frames in ms
    private Timer timer;
    private boolean isRunning;
    private List<ChangeListener> listeners = new ArrayList<>();

    public Animation(List<ImageIcon> frames, int frameDelay) {
        this.frames = frames;
        this.frameDelay = frameDelay;
    }

    public void start() {

        if(!isRunning && frames.size() > 1) {
            timer = new Timer(frameDelay, e -> {
                currentFrameIndex = (currentFrameIndex + 1) % frames.size();
                notifyChangeListener();
            });
            timer.start();
            isRunning = true;
        }
    }
    private void advanceFrame() {
        currentFrameIndex = (currentFrameIndex + 1) % frames.size();
        notifyChangeListener();
    }
    public void stop() {
        if(isRunning) {
            timer.stop();
            timer =  null;
            isRunning = false;
        }
    }

    public ImageIcon getCurrentFrame() {
        return frames.get(currentFrameIndex);
    }

    public boolean isRunning() {
        return isRunning;
    }

    public Dimension getFrameSize() {
        ImageIcon currentFrame = getCurrentFrame();
        return new Dimension(currentFrame.getIconWidth(), currentFrame.getIconHeight());
    }

    public void addChangeListener(ChangeListener changeListener) {
        listeners.add(changeListener);
    }
    public void notifyChangeListener(){
        for(ChangeListener listener : listeners){
            listener.stateChanged(new ChangeEvent(this));
        }
    }

}
