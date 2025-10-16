import java.awt.event.MouseEvent;

public interface FrogState {
    void mousePressed(MouseEvent e);
    void mouseReleased(MouseEvent e);
    void mouseDragged(MouseEvent e);
    void mouseClicked(MouseEvent e);
    void update();
    void enterState();
    void exitState();
}
