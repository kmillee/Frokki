package frog.states;

import frog.Frog;
import frog.FrogComponent;
import frog.FrogState;
import main.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;

public class ThrownState implements FrogState {
    private final FrogComponent frogComponent;
    private final double velocityX;
    private final double velocityY;
    public ThrownState(FrogComponent frogComponent, double velocityX, double velocityY) {
        this.frogComponent = frogComponent;
        this.velocityX = velocityX;
        this.velocityY = velocityY;
    }
    @Override
    public void mousePressed(MouseEvent e) {
    }

    @Override
    public void mouseReleased(MouseEvent e) {
    }

    @Override
    public void mouseDragged(MouseEvent e) {
    }

    @Override
    public void update() {
    }

    @Override
    public void enterState() {
        Container parent = frogComponent.getParent();
        if(parent != null){
            frogComponent.getPhysicsBody().start(
                    frogComponent.getX(),
                    frogComponent.getY(),
                    velocityX * 5,
                    -velocityY,
                    parent.getHeight(),
                    frogComponent.getHeight()
            );
        }
    }

    @Override
    public void exitState() {

    }

    @Override
    public ImageIcon getCurrentImage() {
        Frog frog = frogComponent.getFrog();
        return new ImageIcon("media" + File.separator + "animation_sprite" + File.separator + "hop" + File.separator + frog.getSpecies().toInt() + File.separator+ "hop_3.png");
    }

    @Override
    public Dimension getCurrentSize() {
        return new Dimension(Constants.TASKBAR_FROG_SIZE, Constants.TASKBAR_FROG_SIZE);
    }
}
