import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;
import java.util.LinkedList;
import java.util.List;


/**
 * This class represent an individual frog in the frogBar. 
 */
public class FrogComponent extends JComponent implements MouseListener, MouseMotionListener {
    private final Frog frog;
    private boolean isDragging = false;
    private Point dragOffset;
    private int anchorX = 0;
    private int anchorY = 0;

    // -- Physics related
    private PhysicsBody physicsBody = new PhysicsBody();
    private final List<Point> mousePositions = new LinkedList<>();
    private final List<Long> timestamps = new LinkedList<>();
    private static final int max_size = 7; // We record only max_size mousePositons and time
    private double velocityX = 0;
    private double velocityY = 0;

    /**
     * Constructor for a FrogComponent.
     * @param frog The frog to be represented by this component. Must not be null.
     */
    public FrogComponent(Frog frog){
        this.frog = frog;
        setSize(Constants.TASKBAR_FROG_SIZE,Constants.TASKBAR_FROG_SIZE);
        setOpaque(false);
        addMouseListener(this);
        addMouseMotionListener(this);

        physicsBody.addChangeListener(e -> {
            if(!physicsBody.isActive()){
                frog.loadAnimation("idle");
            }

            Container parent = getParent();
            if(parent != null) {
                anchorX = physicsBody.getX();
                anchorY = parent.getHeight() - physicsBody.getY() - getHeight();
            }
            repaint();
        });


        Animation animation = frog.getAnimation();
        if(animation != null){
            animation.addChangeListener(e -> {
                Dimension frameSize = animation.getFrameSize();
                setSize(frameSize);
                Container parent =  getParent();
                if(parent != null){
                    updateLocationFromAnchor(parent);
                }
                revalidate();
                repaint();
            });
        }
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

    /**
     * Updates the location of the frog component based on its anchor point. 
     * There is clamping to ensure the frog does not go out of bounds of the parent container.
     * @param parent The parent container of the frog component. Must not be null.
     */
    private void updateLocationFromAnchor(Container parent){
        int topLeftX = anchorX;
        int topLeftY = parent.getHeight() - anchorY - getHeight();

        topLeftX = Math.max(0, Math.min(topLeftX, parent.getWidth() - getWidth()));
        topLeftY = Math.max(0, Math.min(topLeftY, parent.getHeight() - getHeight()));
        setLocation(topLeftX, topLeftY);
    }


    @Override
    public void mousePressed(MouseEvent e) {
        isDragging = true;
        dragOffset = e.getPoint();

        // stop idle animation
        Animation animation = frog.getAnimation();
        if(animation != null && animation.isRunning()){
            animation.stop();
        }
//
        frog.setImage(new ImageIcon("media" + File.separator + "animation_sprite" + File.separator + "hop" + File.separator + frog.getSpecies().toInt() + File.separator+ "hop_3.png"));
//
//
        repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        isDragging = false;
        Container parent = getParent();
        if(parent != null){
            physicsBody.start(getX(), getY(), velocityX*5, -velocityY, parent.getHeight(), getHeight());
        }

        mousePositions.clear();
        timestamps.clear();
        velocityX = 0;
        velocityY = 0;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // Moves the frog component based on dragging input from the user
        if(isDragging){
            // Check that the frog doesn't go out of bounds of the parent
            Container parent = getParent();
            if(parent == null) return; // Should not happen

            // Get current mouse position and time (used to compute speed)
            Point currentMousePosition = e.getPoint();
            long currentTime = System.currentTimeMillis();

            mousePositions.add(currentMousePosition);
            timestamps.add(currentTime);

            if(mousePositions.size() > max_size){
                mousePositions.removeFirst();
                timestamps.removeFirst();
            }
            // Compute speed
            if(mousePositions.size() > 1){
                Point oldestPosition = mousePositions.getFirst();
                Point newestPosition = mousePositions.getLast();
                long oldestTime = timestamps.getFirst();
                long newestTime = timestamps.getLast();

                int dx = newestPosition.x - oldestPosition.x;
                int dy = newestPosition.y - oldestPosition.y;
                long dt = newestTime - oldestTime;

                if(dt > 0) {
                    velocityX = dx / (dt/1000.0);
                    velocityY = dy / (dt/1000.0);
                }
            }

            Rectangle parentBounds = parent.getBounds();

            // Compute new position
            Point oldLocation = getLocation();
            int dx = e.getX() - dragOffset.x;
            int dy = e.getY() - dragOffset.y;
            int newX = oldLocation.x + dx;
            int newY = oldLocation.y + dy;


            // Clamp the computed position so that it stays inside the bounds of parent
            newX = Math.max(0, Math.min(newX, parentBounds.width - getWidth()));
            newY = Math.max(0, Math.min(newY, parentBounds.height - getHeight()));

            setLocation(newX, newY);

            anchorX = newX;
            anchorY = parent.getHeight() - newY - getHeight();
        }
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2d = (Graphics2D) g;

        // Draw the frog
        ImageIcon frogImage;
        Animation animation = this.frog.getAnimation();
        Dimension frameSize;

        if(animation != null && animation.isRunning()){
            frogImage = animation.getCurrentFrame();
            frameSize = animation.getFrameSize();
        } else {
            frogImage = frog.getImage();
            frameSize = new Dimension(Constants.TASKBAR_FROG_SIZE, Constants.TASKBAR_FROG_SIZE);
        }
        setSize(frameSize);
        Container parent = getParent();
        if(parent != null){ updateLocationFromAnchor(parent);}
        g2d.drawImage(frogImage.getImage(), 0,0, getWidth(), getHeight(), null);

        g2d.dispose();
    }

    
    /**
     * Gets the current bottom-left anchor position of the frog component.
     * @return The current bottom-left anchor position.
     */
    public Point getPosition(){
        return new Point(anchorX, anchorY);
    }

    /**
     * Gets the frog associated with this component.
     * @return The frog associated with this component.
     */
    public Frog getFrog() {
        return frog;
    }

    @Override
    public void mouseEntered(MouseEvent e) {

    }
    @Override
    public void mouseExited(MouseEvent e) {

    }
    @Override
    public void mouseMoved(MouseEvent e) {

    }
    @Override
    public void mouseClicked(MouseEvent e) {
        // TODO: frog jump
        if(e.getClickCount() == 2){
        }
    }

    /**
     * Gets the physics body associated with this frog component.
     * @return The physics body associated with this frog component.
     */
    public PhysicsBody getPhysicsBody() {
        return physicsBody;
    }
}
