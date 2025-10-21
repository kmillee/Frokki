package frogedex;
/**
 * Created: 01/10/2025
 * This class is responsible for creating and displaying the "frogedex.Frogedex", a frame
 * that displays all the frogs a player owns.
 * The left panel displays a frog's information (species, picture, name, experience
 * level, acquisition date). The right part displays a list of all the frogs the
 * player owns.
 */


import frog.Frog;
import main.Constants;
import main.Utils;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class Frogedex {
    private FrogedexModel frogedexModel;
    private FrogedexView frogedexView;
    private JFrame frame;

    private Timer saveTimer;
    public Frogedex() {
        frogedexModel = FrogedexModel.loadFromFile(Constants.DATA_FILE);
        frogedexView = new FrogedexView();

        frame = new JFrame("Frogedex");
        frame.setLayout(new BorderLayout());
        frame.add(frogedexView, BorderLayout.CENTER);
        Utils.setFixedSize(frame, 858, 430);
        frame.setResizable(false);

        frogedexView.installUI(this);
        setupListeners();

        saveTimer = new Timer(60000, e -> onFrogDataChanged()); // Auto-save data every minute
        saveTimer.start();
    }

    private void setupFrogListeners(Frog frog) {
        frog.addChangeListeners(e -> {
            if (e.getSource() instanceof Frog updatedFrog) {
                frogedexModel.notifyChangeListener(FrogedexModel.ChangeType.EXPERIENCE_UPDATE, updatedFrog);
            }
        });


    }
    public void setupListeners(){
        frogedexModel.getFrogs().forEach(this::setupFrogListeners);

        frogedexModel.addChangeListener(e -> {
            switch (e.getChangeType()) {
                case ADD_FROG -> {
                    Frog newFrog = e.getFrog();
                    if(newFrog != null) {
                        frogedexView.addFrogToList(newFrog);
                    }
                }
                case SELECT_FROG -> {
                    Frog selectedFrog = e.getFrog();
                    if(selectedFrog != null) {
                        frogedexView.updateFrogInfo(selectedFrog);
                    }
                }
                case EXPERIENCE_UPDATE -> {
                    Frog frog =  e.getFrog();
                    if(frog != null && getSelectedFrog().equals(frog)) {
                        frogedexView.updateFrogInfo(frog);

                    }
                }
                default -> {}
            }
        });
    }

    public void saveData(){
        System.out.println("Saving data...");
        frogedexModel.saveToFile(Constants.DATA_FILE);
    }

    public void onFrogDataChanged(){
        saveData();
    }
    public void show(){
        frame.setVisible(true);
    }

    public void selectFrog(Frog frog) {
        frogedexModel.setSelectedFrog(frog);
    }

    public Frog getSelectedFrog(){
        return frogedexModel.getSelectedFrog();
    }

    public List<Frog> getFrogs() {
        return frogedexModel.getFrogs();
    }

    public void addFrog(Frog frog) {
        frogedexModel.addFrog(frog);
    }

    public void addChangeListener(FrogedexChangeListener e) {
        frogedexModel.addChangeListener(e);
    }
}
