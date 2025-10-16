import java.awt.*;
import java.awt.event.MouseEvent;
import java.util.LinkedList;
import java.util.List;

public class DraggingState implements FrogState{
    private final FrogComponent  frogComponent;
    private Point dragOffset;
    private final List<Point> mousePositions = new LinkedList<>();
    private final List<Long> timestamps = new LinkedList<>();
    private static final int MAX_SIZE = 7;
    private double velocityX = 0;
    private double velocityY = 0;


    public DraggingState(FrogComponent frogComponent){
        this.frogComponent = frogComponent;
    }
    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        frogComponent.setState(new ThrownState(frogComponent, velocityX, velocityY));
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
        frogComponent.anchorX = newX;
        frogComponent.anchorY = parent.getHeight() - newY - frogComponent.getHeight();
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
    public void mouseClicked(MouseEvent e) {

    }

    @Override
    public void update() {

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

}