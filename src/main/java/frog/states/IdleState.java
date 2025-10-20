package main.java.frog.states;

import main.java.UI.Animation;
import main.java.frog.Frog;
import main.java.frog.FrogComponent;
import main.java.frog.FrogState;
import main.java.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public class IdleState implements FrogState {
    private final FrogComponent frogComponent;
    public IdleState(FrogComponent frogComponent) {
        this.frogComponent = frogComponent;
    }
    @Override
    public void mousePressed(MouseEvent e) {
        if(e.getButton() == MouseEvent.BUTTON1) // left click
            frogComponent.setState(new DraggingState(frogComponent,  System.currentTimeMillis()));
        if(e.getButton() == MouseEvent.BUTTON3) // right click
            frogComponent.invertDisplayFrogInfo();
    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

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
