import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class IdleState implements FrogState{
    private final FrogComponent frogComponent;
    public IdleState(FrogComponent frogComponent) {
        this.frogComponent = frogComponent;
    }
    @Override
    public void mousePressed(MouseEvent e) {
        frogComponent.setState(new DraggingState(frogComponent,  System.currentTimeMillis()));
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }


    @Override
    public void update() {
        Frog frog = frogComponent.getFrog();
        Animation animation = frog.getAnimation();
        if(animation == null || !animation.isRunning()){
            frog.idle();
        }
    }

    @Override
    public void enterState() {
        Frog frog =  frogComponent.getFrog();
        if(frog.getAnimation() == null) {
            frog.idle();
        } else {
            frog.startAnimation();
        }

        attachAnimationListener();
    }

    @Override
    public void exitState() {
        Frog frog = frogComponent.getFrog();
        Animation animation = frog.getAnimation();
        if(animation != null && animation.isRunning()){
            animation.stop();
        }
    }

    @Override
    public ImageIcon getCurrentImage() {
        Animation animation = frogComponent.getFrog().getAnimation();
        if(animation != null){
            return animation.getCurrentFrame();
        }
        return frogComponent.getFrog().getImage(); // Default
    }

    @Override
    public Dimension getCurrentSize() {
        Frog frog = frogComponent.getFrog();
        Animation animation = frog.getAnimation();
        if(animation != null){
            return animation.getFrameSize();
        }
        return new Dimension(Constants.TASKBAR_FROG_SIZE, Constants.TASKBAR_FROG_SIZE);
    }


    private void attachAnimationListener() {
        Frog frog = frogComponent.getFrog();
        Animation animation = frog.getAnimation();
        if (animation != null) {
            animation.addChangeListener(event -> {
                // Update component size and position
                Dimension frameSize = animation.getFrameSize();
                frogComponent.setSize(frameSize);
                Container parent = frogComponent.getParent();
                if (parent != null) {
                    frogComponent.updateLocationFromAnchor(parent);
                }
                frogComponent.revalidate();
                frogComponent.repaint();
            });
        }
    }
}
