import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.*;

/**
 * This class represents the FrogBar, a window that displays active frogs.
 * A frog is considered active if it is on top of the taskbar.
 */
public class FrogBar extends JWindow {
    private JLayeredPane layeredPane;
    private Map<Frog, FrogComponent> frogComponents = new HashMap<>();

    /**
     * Constructor for a FrogBar.
     *
     * @param frogedex The Frogedex containing the frogs to be displayed in the FrogBar.
     */
    public FrogBar(Frogedex frogedex) {
        for (Frog frog : frogedex.getFrogs()) {
            frog.addChangeListeners(e -> onStateChanged(frog));

        }

        setUpWindow();
        setupLayeredPane();


        for (Frog frog : frogedex.getFrogs()) {
            if (frog.isActive()) addFrog(frog);
        }

        setVisible(true);
        setAlwaysOnTop(true);    // To always appear regardless of user activity
    }

    private void setUpWindow() {
        setBackground(new Color(255, 255, 255, 0)); // Transparent
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height - Constants.TASKBAR_OFFSET);
        setLocation(0, 0);
    }

    private void setupLayeredPane() {
        layeredPane = new JLayeredPane();
        layeredPane.setOpaque(false);
        layeredPane.setLayout(null); // allows absolute positioning
        add(layeredPane);
    }

    private void onStateChanged(Frog frog) {
        if (frog.isActive()) {
            if (!frogComponents.containsKey(frog)) addFrog(frog);
        } else {
            if (frogComponents.containsKey(frog)) removeFrog(frog);
        }
    }

    public void addFrog(Frog frog) {
        // Necessary to make sure frogComponent is only created once.
        SwingUtilities.invokeLater(() -> {
            if (frogComponents.containsKey(frog)) return;
            System.out.println("adding frog");
            FrogComponent frogComponent = new FrogComponent(frog);
            frogComponents.put(frog, frogComponent);
            layeredPane.add(frogComponent);
            Point position = new Point(0, 0);
            frogComponent.setBottomLeftAnchor(position.x, position.y);

            if (frog.getAnimation() == null) frog.idle();

            layeredPane.revalidate();
            layeredPane.repaint();
        });
    }

    public void removeFrog(Frog frog) {
        FrogComponent frogComponent = frogComponents.get(frog);
        if (frogComponent != null) {
            layeredPane.remove(frogComponents.get(frog));
            frogComponents.remove(frog);
            layeredPane.revalidate();
            layeredPane.repaint();
        }
    }

}
