package frog.states;

import frog.Frog;
import frog.FrogComponent;
import frog.FrogState;
import main.Constants;
import main.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.net.URL;

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
        if(e.getButton() == MouseEvent.BUTTON1) // left click
        {
            frogComponent.setState(new DraggingState(frogComponent, System.currentTimeMillis()));
        }
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
        Container parent = frogComponent.getParent();
        if(parent != null){
            frogComponent.getPhysicsBody().start(
                    frogComponent.getX(),
                    frogComponent.getY(),
                    velocityX,
                    -velocityY,
                    parent.getHeight(),
                    frogComponent.getHeight()
            );
        }
    }

    @Override
    public void exitState() {
        if(frogComponent.getPhysicsBody().isActive())
            frogComponent.getPhysicsBody().stop();
    }

    @Override
    public ImageIcon getCurrentImage() {
        Frog frog = frogComponent.getFrog();
        String path = "/animation_sprite/hop/" + frog.getSpecies().toInt() + "/hop_3.png";
        URL url = Utils.class.getResource(path);
        if (url == null){
            System.err.println("Error loading animation sprite : " + path);
            return null;
        }
        return new ImageIcon(url);
    }

    @Override
    public Dimension getCurrentSize() {
        return new Dimension(Constants.TASKBAR_FROG_SIZE, Constants.TASKBAR_FROG_SIZE);
    }
}
