package frog;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;

public interface FrogState {
    void mousePressed(MouseEvent e);
    void mouseReleased(MouseEvent e);
    void mouseDragged(MouseEvent e);
    void update();
    void enterState();
    void exitState();
    ImageIcon getCurrentImage();
    Dimension getCurrentSize();

}
