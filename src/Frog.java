import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Frog {
    private String imagePath, name, acquisitionDate; // Name should be unique
    private ImageIcon image;
    private FrogSpecies species;
    private int experience;
    private boolean active = false; // If active is true, it means the frog is on the toolbar
    private Animation animation;

    private final List<ChangeListener> listeners = new ArrayList<ChangeListener>();

    public Frog(String imagePath, String name, FrogSpecies species, String acquisitionDate) {
        this.imagePath = imagePath;
        this.image = new ImageIcon(imagePath);
        this.name = name;
        this.species = species;
        this.acquisitionDate = acquisitionDate;
        this.experience = 50;
    }

    public Frog(String imagePath){
        this(imagePath, "John Toad", FrogSpecies.GREEN, "00/00/0000");
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }

    public ImageIcon getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getSpecies(){
        return species.toString();
    }

    public String getAcquisitionDate(){
        return acquisitionDate;
    }
    
    public int getExperience(){
        return experience % Constants.EXPERIENCE_THRESHOLD;
    }

    public int getLevel(){
        return experience / Constants.EXPERIENCE_THRESHOLD;
    }

    public void setActive(boolean active) {
        this.active = active;
        notifyChangeListeners();
    }

    public boolean isActive() {
        return active;
    }

    public void setAnimation(Animation animation) {
        this.animation = animation;
        if(animation != null) {
            animation.start();
            notifyChangeListeners();
        }
    }

    public Animation getAnimation() {
        return this.animation;
    }

    public void loadAnimation(String animationType){
        String animationPath = "media" + File.separator + "animation_sprite" +
                File.separator + animationType + File.separator + species.toInt();
        List<ImageIcon> frames = Utils.loadFrames(animationPath);
        // resize frames
        List<ImageIcon> resizedFrames = new ArrayList<>();
        for(ImageIcon frame : frames) {
            ImageIcon image = new ImageIcon(frame.getImage());
            image = Utils.resizeKeepingRatio(image, Constants.TASKBAR_FROG_SIZE, Constants.TASKBAR_FROG_SIZE);
            resizedFrames.add(image);
        }
        if(!resizedFrames.isEmpty()){
            Animation animation = new Animation(resizedFrames, 100);
            setAnimation(animation);
        }
    }

    public void addChangeListeners(ChangeListener changeListener) {
        listeners.add(changeListener);
    }

    public void notifyChangeListeners(){
        for(ChangeListener listener : listeners){
            listener.stateChanged(new ChangeEvent(this));
        }
    }
}
