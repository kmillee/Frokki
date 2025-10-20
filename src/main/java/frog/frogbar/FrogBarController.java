package main.java.frog.frogbar;


import main.java.frog.Frog;
import main.java.frog.FrogComponent;
import main.java.frogedex.Frogedex;
import main.java.Constants;

import javax.swing.*;
import java.awt.*;

public class FrogBarController extends JWindow {
    private final FrogBarModel model;
    private final FrogBarView view;
    private Timer updateTimer; // Timer that updates infoPanels

    public FrogBarController(Frogedex frogedex){
        model = new FrogBarModel();
        view = new FrogBarView();

        initialize(frogedex);
    }

    private void initialize(Frogedex frogedex){
        setUpWindow();
        setupFrogListeners(frogedex);
        initializeActiveFrogs(frogedex);
        startUpdateTimer();
        setVisible(true);
        setAlwaysOnTop(true);
    }

    private void setUpWindow() {
        setBackground(new Color(255, 255, 255, 0)); // Transparent
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(screenSize.width, screenSize.height - Constants.TASKBAR_OFFSET);
        setLocation(0, 0);

        add(view.getLayeredPane());
    }

    private void setupFrogListeners(Frogedex frogedex){
        for(Frog frog : frogedex.getFrogs()){
            frog.addChangeListeners(e -> onFrogStateChanged(frog));
        }
    }

    private void initializeActiveFrogs(Frogedex frogedex){
        for(Frog frog : frogedex.getFrogs()){
            if(frog.isActive())
                addFrog(frog);
        }
    }

    private void startUpdateTimer(){
        updateTimer = new Timer(50, e -> view.updateAllInfoPanels());
        updateTimer.start();
    }
    private void onFrogStateChanged(Frog frog) {
        if (frog.isActive()) {
            if (!model.containsFrog(frog)) addFrog(frog);
        } else {
            if (model.containsFrog(frog)) removeFrog(frog);
        }
    }

    private void addFrog(Frog frog){
        addFrog(frog, new Point(0,0));
    }

    private void addFrog(Frog frog, Point positon){
        SwingUtilities.invokeLater(() -> {
            if(model.containsFrog(frog)) return; // frog already exists
            FrogComponent frogComponent = new FrogComponent(frog);

            frogComponent.addChangeListener(e -> onFrogInfoDisplayToggled(frogComponent));

            model.addFrog(frog);
            view.addFrogComponent(frog, frogComponent, positon);

            if (frog.getAnimation() == null) frog.idle();

            view.refreshView();
        });
    }

    private void onFrogInfoDisplayToggled(FrogComponent frogComponent) {
        view.updateInfoPanelPosition(frogComponent);
    }

    private void removeFrog(Frog frog){
        SwingUtilities.invokeLater(() -> {
            if(!model.containsFrog(frog)) return;
            FrogComponent frogComponent = new FrogComponent(frog);
            frogComponent.stopTimer();

            model.removeFrog(frog);
            view.removeFrogComponent(frog);
            view.refreshView();
        });

    }
}
