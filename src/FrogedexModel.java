import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.annotations.Expose;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Manages the data for the Frogedex (i.e. collection of frogs)
 */
public class FrogedexModel {
    @Expose
    private List<Frog> frogs;
    @Expose
    private String selectedFrogName;
    private Frog selectedFrog; // Frog that is selected in the list view, i.e. its info should be displayed
    private List<FrogedexChangeListener> listeners = new ArrayList<>();

    public enum ChangeType {
        ADD_FROG,
        SELECT_FROG,
        EXPERIENCE_UPDATE,
        OTHER
    }
    public FrogedexModel(){
        this.frogs = new ArrayList<Frog>();
        Frog defaultFrog = new Frog("media" + File.separator + "frog_1.png");
        addFrog(defaultFrog);
    }

    public List<Frog> getFrogs() { // TODO: probably return unmodifiable list
        return frogs;
    }
    public String getSelectedFrogName() {
        return selectedFrogName;
    }

    public void addFrog(Frog frog){
        this.frogs.add(frog);
        if(selectedFrog == null && !frogs.isEmpty()) setSelectedFrog(frogs.getFirst());
        notifyChangeListener(ChangeType.ADD_FROG, frog);
    }

    public Frog getSelectedFrog() {
        return selectedFrog;
    }

    public void setSelectedFrog(Frog selectedFrog) {
        this.selectedFrog = selectedFrog;
        this.selectedFrogName = selectedFrog.getName();
        notifyChangeListener(ChangeType.SELECT_FROG, selectedFrog);
    }

    public void addChangeListener(FrogedexChangeListener listener){
        listeners.add(listener);
    }


    public void notifyChangeListener(ChangeType changeType, Frog frog){
        FrogedexChangeEvent changeEvent = new FrogedexChangeEvent(this, changeType, frog);
        for(FrogedexChangeListener listener : listeners){
            listener.stateChanged(changeEvent);
        }
    }

    public void notifyChangeListener(){
        notifyChangeListener(ChangeType.OTHER, null);
    }

    public void saveToFile(String path){
        try(Writer writer = new FileWriter(path)){
            Gson gson = new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .setPrettyPrinting()
                    .create();
            gson.toJson(this, writer);
        } catch(IOException e){
            System.err.println("Error writing to file");
        }
    }

    /**
     * Loads Frogedex data from a file
     * @param fileName file to load data from
     * @return reconstructed FrogedexModel
     */
    public static FrogedexModel loadFromFile(String fileName){
        try(Reader reader = new FileReader(fileName)){
            Gson  gson = new GsonBuilder()
                    .excludeFieldsWithoutExposeAnnotation()
                    .create();

            FrogedexModel model = gson.fromJson(reader, FrogedexModel.class);
            model.reconstruct();
            return model;
        } catch(IOException e){
            System.out.println("File does not exist.");
        }
        return new FrogedexModel(); // return new model if file doesnt exist
    }

    /**
     * Reconstructs non-serialized field after deserialization
     */
    public void reconstruct(){
        this.listeners = new ArrayList<>();

        // Reconstruct selected frog reference
        if(selectedFrogName != null && frogs != null){
            for(Frog frog : frogs){
                if(frog.getName().equals(selectedFrogName)){
                    this.selectedFrog = frog;
                    break;
                }
            }
        }
        // Reconstruct frog images
        if(frogs != null) {
            for(Frog frog : frogs){
                frog.reconstruct();
            }
        }
    }
}
