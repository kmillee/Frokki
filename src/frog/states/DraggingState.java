package frog.states;

import UI.Animation;
import frog.Frog;
import frog.FrogComponent;
import frog.FrogState;
import main.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

public class DraggingState implements FrogState {
    private final FrogComponent frogComponent;
    private Point dragOffset;
    private final List<Point> mousePositions = new LinkedList<>();
    private final List<Long> timestamps = new LinkedList<>();
    private static final int MAX_SIZE = 7;
    private double velocityX = 0;
    private double velocityY = 0;
    private long timeAtPressed;


    public DraggingState(FrogComponent frogComponent, long timeAtPressed) {
        this.frogComponent = frogComponent;
        this.timeAtPressed = timeAtPressed;
    }
    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if(System.currentTimeMillis() - this.timeAtPressed < 200) // This is considered a click, we apply a jump-like throw
        {
            int randomDirection = Math.random() < 0.5 ? 1 : -1;
            frogComponent.setDirection(randomDirection);
            frogComponent.setState(new ThrownState(frogComponent, randomDirection * 100 * frogComponent.getFrog().getLevel(), 100 * frogComponent.getFrog().getLevel()));
        }

        else {
            if(!mousePositions.isEmpty()){
                int sign = mousePositions.getLast().x - mousePositions.getFirst().x;
                int direction = sign > 0 ? 1 : -1;
                frogComponent.setDirection(direction);
            }
            frogComponent.setState(new ThrownState(frogComponent, velocityX * 5, velocityY));
        }

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        Container parent = frogComponent.getParent();

        if(parent == null) return;
        if(dragOffset == null) dragOffset = e.getPoint();

        // For velocity calculations
        recordMouseMovement(e);

        // Calculate new position
        Point oldLocation = frogComponent.getLocation();
        int dx = e.getX() - dragOffset.x;
        int dy = e.getY() - dragOffset.y;
        int newX = oldLocation.x + dx;
        int newY = oldLocation.y + dy;

        // Clamp into parent's bounds
        Rectangle parentBounds = parent.getBounds();
        newX = Math.max(0, Math.min(newX, parentBounds.width - frogComponent.getWidth()));
        newY = Math.max(0, Math.min(newY, parentBounds.height - frogComponent.getHeight()));

        frogComponent.setLocation(newX, newY);

        frogComponent.setAnchor(newX, parent.getHeight() - newY - frogComponent.getHeight());
    }
    private void recordMouseMovement(MouseEvent e){
        Point currentMousePosition = e.getPoint();
        long currentTime = System.currentTimeMillis();

        mousePositions.add(currentMousePosition);
        timestamps.add(currentTime);

        if (mousePositions.size() > MAX_SIZE) {
            mousePositions.removeFirst();
            timestamps.removeFirst();
        }

        // Compute velocity
        if (mousePositions.size() > 1) {
            Point oldestPosition = mousePositions.getFirst();
            Point newestPosition = mousePositions.getLast();
            long oldestTime = timestamps.getFirst();
            long newestTime = timestamps.getLast();

            int dx = newestPosition.x - oldestPosition.x;
            int dy = newestPosition.y - oldestPosition.y;
            long dt = newestTime - oldestTime;

            if (dt > 0) {
                velocityX = dx / (dt / 1000.0);
                velocityY = dy / (dt / 1000.0);
            }
        }
    }


    @Override
    public void enterState() {
        Frog frog = frogComponent.getFrog();
        Animation animation = frog.getAnimation();
        if(animation != null && animation.isRunning()) {
            animation.stop();
            frog.removeAnimation();
        }
        frogComponent.repaint();
    }

    @Override
    public void exitState() {
        mousePositions.clear();
        timestamps.clear();
        dragOffset = null;
    }

    @Override
    public ImageIcon getCurrentImage() {
        Frog frog = frogComponent.getFrog();
        return new ImageIcon(Constants.RESOURCES_PATH + File.separator + "animation_sprite" + File.separator + "hop" + File.separator + frog.getSpecies().toInt() + File.separator+ "hop_3.png");
    }

    @Override
    public Dimension getCurrentSize() {
        return new Dimension(Constants.TASKBAR_FROG_SIZE, Constants.TASKBAR_FROG_SIZE);
    }

}