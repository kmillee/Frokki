package main.java.frog.frogbar;

import main.java.frog.Frog;
import main.java.frog.FrogComponent;

import javax.swing.*;
import java.awt.*;
import java.util.HashMap;
import java.util.Map;

public class FrogBarView {
    private final JLayeredPane layeredPane;
    private final Map<Frog, FrogComponent> frogComponents;

    public FrogBarView(){
        layeredPane = new JLayeredPane();
        layeredPane.setOpaque(false);
        layeredPane.setLayout(null);
        this.frogComponents = new HashMap<>();
    }

    public JLayeredPane getLayeredPane(){
        return layeredPane;
    }

    public void addFrogComponent(Frog frog, FrogComponent frogComponent, Point position){
        frogComponents.put(frog, frogComponent);
        layeredPane.add(frogComponent);
        frogComponent.setBottomLeftAnchor(position.x, position.y);

        // Add frog info panel
        layeredPane.add(frogComponent.getFrogInfoPanel(), JLayeredPane.POPUP_LAYER);
        frogComponent.setFrogInfoPanelVisible(false);

        updateInfoPanelPosition(frogComponent);
    }

    public void removeFrogComponent(Frog frog){
        FrogComponent frogComponent = frogComponents.get(frog);
        if(frogComponent != null){
            layeredPane.remove(frogComponent.getFrogInfoPanel());
            layeredPane.remove(frogComponent);
            frogComponents.remove(frog);
        }
    }

    public void updateInfoPanelPosition(FrogComponent frogComponent){
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

    public void refreshView(){
        layeredPane.revalidate();
        layeredPane.repaint();
    }
}
