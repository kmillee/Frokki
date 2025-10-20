package frog;

import frog.states.IdleState;
import main.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

public class FrogComponent extends JComponent implements MouseListener, MouseMotionListener {
    private final Frog frog;
    private FrogState currentState;
    private int anchorX;
    private int anchorY;
    private int direction = 1; // Use to get the direction of the frog, supposed to be either 1 or -1

    Timer experienceTimer;
    private final PhysicsBody physicsBody;
    public FrogComponent(Frog frog) {
        this.frog = frog;
        this.physicsBody = new PhysicsBody();

        setSize(Constants.TASKBAR_FROG_SIZE,Constants.TASKBAR_FROG_SIZE);
        setOpaque(false);
        addMouseListener(this);
        addMouseMotionListener(this);

        setState(new IdleState(this));

        physicsBody.addChangeListener(e -> {
            if(!physicsBody.isActive()){
                // Ground was reached, we transition back to idle state
                if(!(currentState instanceof IdleState)){
                    setState(new IdleState(this));
                }
            }

            Container parent =  getParent();
            if(parent != null){
                anchorX = physicsBody.getX();
                anchorY = parent.getHeight() - physicsBody.getY() - getHeight();
            }
            repaint();
        });

        experienceTimer = new Timer(5000, e -> {
            frog.increaseExperience();
        });
        experienceTimer.setRepeats(true);
        experienceTimer.start();

        frog.addChangeListeners(e -> repaint());
    }

    public void setState(FrogState state) {
        if(currentState != null) currentState.exitState();
        this.currentState = state;
        currentState.enterState();
    }

    public Frog getFrog(){
        return frog;
    }

    public void setAnchor(int x, int y){
        anchorX = x;
        anchorY = y;
    }
    public PhysicsBody getPhysicsBody(){
        return this.physicsBody;
    }

    /**
     * Updates the location of the frog component based on its anchor point.
     * There is clamping to ensure the frog does not go out of bounds of the parent container.
     * @param parent The parent container of the frog component. Must not be null.
     */
    public void updateLocationFromAnchor(Container parent){
        int topLeftX = anchorX;
        int topLeftY = parent.getHeight() - anchorY - getHeight();

        topLeftX = Math.max(0, Math.min(topLeftX, parent.getWidth() - getWidth()));
        topLeftY = Math.max(0, Math.min(topLeftY, parent.getHeight() - getHeight()));
        setLocation(topLeftX, topLeftY);
    }

    /**
     * Sets the bottom-left anchor point of the frog component relative to its parent container.
     * @param x The x-coordinate of the bottom-left anchor point.
     * @param yFromBottom The y-coordinate of the bottom-left anchor point measured from
     * the bottom of the parent container. If the value is 0, the frog is at the bottom of
     * the parent container.
     */
    public void setBottomLeftAnchor(int x, int yFromBottom){
        this.anchorX = x;
        this.anchorY = yFromBottom;
        Container parent = getParent();
        if(parent != null){
            updateLocationFromAnchor(parent);
        }
    }

    // --- Mouse events
    @Override
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void mousePressed(MouseEvent e) {
        currentState.mousePressed(e);
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        currentState.mouseReleased(e);
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {
        currentState.mouseDragged(e);
    }

    @Override
    public void mouseMoved(MouseEvent e) {

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Draw the frog
        ImageIcon frogImage = currentState.getCurrentImage();
        Dimension frameSize = currentState.getCurrentSize();

        setSize(frameSize);
        Container parent = getParent();
        if (parent != null) {
            updateLocationFromAnchor(parent);
        }

        if (direction > 0) {
            // Draw the image as is
            g2d.drawImage(frogImage.getImage(), 0, 0, getWidth(), getHeight(), null);
        } else {
            // Flip the image horizontally and adjust its size
            g2d.drawImage(frogImage.getImage(), getWidth(), 0, 0, getHeight(), 0, 0, frogImage.getIconWidth(), frogImage.getIconHeight(), null);
        }

//        Image image = frogImage.getImage();
//        if(direction < 0) {
//            AffineTransform transform = new AffineTransform();
//            transform.scale(-1 , 1); // Flip horizontally
//            transform.translate(-image.getWidth(null), 0); // Adjust position after flipping
//            g2d.drawImage(image, transform, null);
//        }
//        else
//            g2d.drawImage(image, 0, 0, getWidth(), getHeight(), null);
        g2d.dispose();
    }

    public void setDirection(int direction){
        this.direction = direction;
    }

    public int getDirection(){
        return direction;
    }

    public void stopTimer(){
        experienceTimer.stop();
    }

}