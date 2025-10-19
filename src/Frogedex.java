/**
 * Created: 01/10/2025
 * This class is responsible for creating and displaying the "Frogedex", a frame
 * that displays all the frogs a player owns.
 * The left panel displays a frog's information (species, picture, name, experience
 * level, acquisition date). The right part displays a list of all the frogs the
 * player owns.
 */


import javax.swing.*;
import javax.swing.border.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.List;

public class Frogedex {
    private FrogedexModel frogedexModel;
    private FrogedexView frogedexView;
    private JFrame frame;

    Frogedex() {
        frogedexModel = new FrogedexModel();
        frogedexView = new FrogedexView();

        frame = new JFrame("Frogedex");
        frame.setLayout(new BorderLayout());
        frame.add(frogedexView, BorderLayout.CENTER);
        Utils.setFixedSize(frame, 858, 430);
        frame.setResizable(false);

        // Just for testing, should be removed later on
        List<Frog> frogs = new ArrayList<>();
        for(int i = 0 ; i < 13 ; i++){
            Frog frog = new Frog("media/frog_" + i + ".png", "The Frog "+ i, FrogSpecies.getSpecies(i), "01/01/2025");
            frogs.add(frog);
        }

        frogs.forEach(frogedexModel::addFrog);
        frogs.get(1).setActive(true);
        // Remove that after

        frogedexView.installUI(this);

        frogedexModel.getFrogs().forEach(frog -> {
            frog.addChangeListeners(e -> {
                if (e.getSource() instanceof Frog updatedFrog) {
                    frogedexModel.notifyChangeListener(FrogedexModel.ChangeType.EXPERIENCE_UPDATE, updatedFrog);
                }
            });
        });

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
    
}
