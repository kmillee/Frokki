import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the data for the Frogedex (i.e. collection of frogs)
 */
public class FrogedexModel {
    private List<Frog> frogs;
    private Frog selectedFrog; // Frog that is selected in the list view, i.e. its info should be displayed
    private List<ChangeListener> listeners = new ArrayList<>();

    public FrogedexModel(){
        this.frogs = new ArrayList<Frog>();
        Frog defaultFrog = new Frog("media" + File.separator + "frog_1.png");
        addFrog(defaultFrog);
    }

    public List<Frog> getFrogs() { // TODO: probably return unmodifiable list
        return frogs;
    }

    public void addFrog(Frog frog){
        this.frogs.add(frog);
        if(selectedFrog == null && !frogs.isEmpty()) setSelectedFrog(frogs.getFirst());
        notifyChangeListener();
    }

    public Frog getSelectedFrog() {
        return selectedFrog;
    }

    public void setSelectedFrog(Frog selectedFrog) {
        this.selectedFrog = selectedFrog;
        notifyChangeListener();
    }

    public void addChangeListener(ChangeListener listener){
        listeners.add(listener);
    }

    public void notifyChangeListener(){
        for(ChangeListener listener : listeners){
            listener.stateChanged(new ChangeEvent(this));
        }
    }
}
