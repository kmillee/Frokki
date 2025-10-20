package main.java.frog.frogbar;

import main.java.frog.Frog;

import java.util.ArrayList;
import java.util.List;

public class FrogBarModel {
    private final List<Frog> activeFrogs;

    public FrogBarModel(){
        activeFrogs = new ArrayList<>();
    }
    public List<Frog> getActiveFrogs() {
        return activeFrogs;
    }

    public void addFrog(Frog frog){
        if(!activeFrogs.contains(frog))
            activeFrogs.add(frog);
    }

    public void removeFrog(Frog frog){
        activeFrogs.remove(frog);
    }

    public boolean containsFrog(Frog frog){
        return activeFrogs.contains(frog);
    }
}
