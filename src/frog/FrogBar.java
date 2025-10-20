package frog;

import frogedex.Frogedex;
import main.Constants;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

/**
 * This class represents the frog.FrogBar, a window that displays active frogs.
 * A frog is considered active if it is on top of the taskbar.
 */
public class FrogBar extends JWindow {
    private JLayeredPane layeredPane;
    private final Map<Frog, FrogComponent> frogComponents = new HashMap<>();
    private Timer updateTimer;

    /**
     * Constructor for a frog.FrogBar.
     *
     * @param frogedex The frogedex.Frogedex containing the frogs to be displayed in the frog.FrogBar.
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

        updateTimer = new Timer(50, e -> updateAllInfoPanels());
        updateTimer.start();
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

    public void addFrog(Frog frog, Point position){
        // Necessary to make sure frogComponent is only created once.
        SwingUtilities.invokeLater(() -> {
            if (frogComponents.containsKey(frog)) return;

            FrogComponent frogComponent = new FrogComponent(frog);

            frogComponent.addChangeListener(e -> onFrogInfoDisplayToggled(frogComponent));


            frogComponents.put(frog, frogComponent);
            layeredPane.add(frogComponent);
            frogComponent.setBottomLeftAnchor(position.x, position.y);

            if (frog.getAnimation() == null) frog.idle();

            // Frog info panel
            layeredPane.add(frogComponent.getFrogInfoPanel(), JLayeredPane.POPUP_LAYER);
            frogComponent.setFrogInfoPanelVisible(false);

            updateInfoPanelPosition(frogComponent);

            layeredPane.revalidate();
            layeredPane.repaint();
        });
    }

    public void addFrog(Frog frog) {
        addFrog(frog, new Point(0,0));
    }

    public void removeFrog(Frog frog) {
        FrogComponent frogComponent = frogComponents.get(frog);
        if (frogComponent != null) {
            layeredPane.remove(frogComponents.get(frog));
            layeredPane.remove(frogComponent.getFrogInfoPanel());
            frogComponent.stopTimer();
            frogComponents.remove(frog);


            layeredPane.revalidate();
            layeredPane.repaint();
        }
    }


    private void updateInfoPanelPosition(FrogComponent frogComponent) {
        if(frogComponent == null) return;

        JPanel infoPanel = frogComponent.getFrogInfoPanel();
        if(infoPanel != null && frogComponent.getDisplayFrogInfo()) { // Panel should be visible
            Point frogLocation = frogComponent.getLocation();
            int panelX = frogLocation.x + (frogComponent.getWidth() - infoPanel.getWidth()) / 2; // Center it relative to frog component
            int panelY = frogLocation.y - infoPanel.getHeight() - 10;
            infoPanel.setLocation(panelX, panelY);
            infoPanel.setVisible(true);
        } else if(infoPanel != null && !frogComponent.getDisplayFrogInfo()) // Panel should not be visible
            infoPanel.setVisible(false);
    }

    public void updateAllInfoPanels(){
        for(FrogComponent frogComponent : frogComponents.values()){
            updateInfoPanelPosition(frogComponent);
        }
    }

    public void onFrogInfoDisplayToggled(FrogComponent frogComponent) {
        updateInfoPanelPosition(frogComponent);
    }

}
