import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.io.File;

public class FrogComponent extends JComponent implements MouseListener, MouseMotionListener {
    private final Frog frog;
    private FrogState currentState;
    int anchorX;
    int anchorY;

    private PhysicsBody physicsBody;
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
        ImageIcon frogImage;
        Dimension frameSize;

        Animation animation = this.frog.getAnimation();
        boolean isThrown = currentState instanceof ThrownState;

        if(animation != null && animation.isRunning()){
            frogImage = animation.getCurrentFrame();
            frameSize = animation.getFrameSize();
        } else if(isThrown){
            frogImage = (new ImageIcon("media" + File.separator + "animation_sprite" + File.separator + "hop" + File.separator + frog.getSpecies().toInt() + File.separator+ "hop_3.png"));
            frameSize = new Dimension(Constants.TASKBAR_FROG_SIZE, Constants.TASKBAR_FROG_SIZE);
        } else {
            frogImage = frog.getImage();
            frameSize = new Dimension(Constants.TASKBAR_FROG_SIZE, Constants.TASKBAR_FROG_SIZE);
        }

        setSize(frameSize);
        Container parent = getParent();
        if (parent != null) {
            updateLocationFromAnchor(parent);
        }

        g2d.drawImage(frogImage.getImage(), 0, 0, getWidth(), getHeight(), null);
        g2d.dispose();
    }

}