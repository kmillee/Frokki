import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;

// TODO: Animate frog, frog should fall back down if y != 0 in parent
/**
 * This class represent an individual frog in the frogBar.
 */
public class FrogComponent extends JComponent implements MouseListener, MouseMotionListener {
    private final Frog frog;
    private boolean isDragging = false;
    private Point dragOffset;
    private Point position; // Position on the taskbar
    public FrogComponent(Frog frog){
        this.frog = frog;
        //Utils.setFixedSize(this, Constants.TASKBAR_FROG_SIZE,Constants.TASKBAR_FROG_SIZE);
        setSize(Constants.TASKBAR_FROG_SIZE,Constants.TASKBAR_FROG_SIZE);
        addMouseListener(this);
        addMouseMotionListener(this);

        //frog.addChangeListeners(e -> repaint());

        Animation animation = frog.getAnimation();
        if(animation != null){
            animation.addChangeListener(e -> {
                Dimension frameSize = animation.getFrameSize();
                setSize(frameSize);
                revalidate();
                repaint();
            });
        }
    }



    @Override
    public void mousePressed(MouseEvent e) {
        isDragging = true;
        dragOffset = e.getPoint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        isDragging = false;
    }

    @Override
    public void mouseDragged(MouseEvent e) {
        // Moves the frog component based on dragging input from the user
        if(isDragging){
            // Check that the frog doesn't go out of bounds of the parent
            Container parent = getParent();
            if(parent == null) return; // Should not happen
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
        g2d.drawImage(frogImage.getImage(), 0,0,frameSize.width, frameSize.height, null);

        g2d.dispose();
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

    }
}
