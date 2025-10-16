import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

/**
 * This class is responsible for handling frame-based animations.
 */
public class Animation {
    private final List<ImageIcon> frames;

    private int currentFrameIndex = 0;
    private final int frameDelay; // delay between two frames in ms
    private Timer timer;
    private boolean isRunning;
    public List<ChangeListener> listeners = new ArrayList<>();

    /**
     * Constructor for an Animation.
     * @param frames The list of frames for the animation.
     * @param frameDelay The delay between two frames in milliseconds.
     */
    public Animation(List<ImageIcon> frames, int frameDelay) {
        this.frames = frames;
        this.frameDelay = frameDelay;
    }

    /**
     * Starts the animation if it is not already running.
     */
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

    /**
     * Stops the animation if it is currently running.
     */
    public void stop() {
        if(isRunning) {
            timer.stop();
            timer =  null;
            isRunning = false;
        }
    }

    /**
     * Gets the current frame of the animation.
     * @return The current frame of the animation.
     */
    public ImageIcon getCurrentFrame() {
        return frames.get(currentFrameIndex);
    }

    /**
     * Checks if the animation is currently running.
     * @return True if the animation is running, false otherwise.
     */
    public boolean isRunning() {
        return isRunning;
    }

    /**
     * Gets the size of the current frame.
     * @return The size of the current frame.
     */
    public Dimension getFrameSize() {
        ImageIcon currentFrame = getCurrentFrame();
        return new Dimension(currentFrame.getIconWidth(), currentFrame.getIconHeight());
    }

    public void addChangeListener(ChangeListener changeListener) {
        listeners.add(changeListener);
    }
    
    private void notifyChangeListener(){
        for(ChangeListener listener : listeners){
            listener.stateChanged(new ChangeEvent(this));
        }
    }

}
