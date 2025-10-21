package frog;

import UI.Animation;
import com.google.gson.annotations.Expose;
import main.Constants;
import main.Utils;
import sound.Sound;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import static main.Constants.RESOURCES_PATH;

/**
 * This class represents a Frog object.
 */
public class Frog {
    @Expose
    private String imagePath, name, acquisitionDate; // Name should be unique
    private ImageIcon image; // This image is the image used as frog profile
    @Expose
    private FrogSpecies species;
    @Expose
    private int experience;
    @Expose
    private boolean active = false; // If active is true, it means the frog is on the toolbar
    private Animation animation;
    private Sound sound;

    private final List<ChangeListener> listeners;

    public Frog(){
        this.listeners = new ArrayList<>();
    }

    public Frog(String imagePath, String name, FrogSpecies species, String acquisitionDate) {
        this();
        this.imagePath = imagePath;
        this.imagePath = Constants.RESOURCES_PATH + File.separator + "frog_" + species.toInt() + ".png";

        this.image = new ImageIcon(imagePath);
        this.name = name;
        this.species = species;
        this.acquisitionDate = acquisitionDate;
        this.experience = 0;
        this.sound = new Sound(RESOURCES_PATH + File.separator + "sound" + File.separator + "croak.wav");
    }

    public Frog(String name, FrogSpecies species, String acquisitionDate) {
        this();

        this.imagePath = Constants.RESOURCES_PATH + File.separator + "frog_" + species.toInt() + ".png";

        this.image = new ImageIcon(imagePath);
        this.name = name;
        this.species = species;
        this.acquisitionDate = acquisitionDate;
        this.experience = 0;
        this.sound = new Sound(RESOURCES_PATH + File.separator + "sound" + File.separator + "croak.wav");
    }

    /**
     * Constructor for a frog.Frog with default values.
     * @param imagePath The path to the image representing the frog.
     */
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

    public String getSpeciesName(){
        return species.toString();
    }
    public FrogSpecies getSpecies() {
        return species;
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

    public Sound getSound(){
        return sound;
    }

    public void increaseExperience() {
        this.experience++;
        notifyChangeListeners(); // Notify general listeners
        for (ChangeListener listener : listeners) {
            listener.stateChanged(new  ChangeEvent(this));
        }
    }

    public boolean isActive() {
        return active;
    }

    /**
     * Starts an animation for the frog.
     * @param animation The animation to be set for the frog.
     */
    private void setAnimation(Animation animation) {
        this.animation = animation;
        notifyChangeListeners();
    }

    public void startAnimation(){
        if(!animation.isRunning()){
            animation.start();
            notifyChangeListeners();
        }
    }
    public Animation getAnimation() {
        return this.animation;
    }

    /**
     * Loads an animation for the frog based on its species and the specified animation type.
     * @param animationType The type of animation to load.
     */
    private void loadAnimation(String animationType){
        String animationPath = RESOURCES_PATH + File.separator + "animation_sprite" +
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
            Animation animation = new Animation(resizedFrames, 200);
            setAnimation(animation);
        }
    }


    public void idle(){
        if(animation != null && animation.isRunning()){
            animation.stop();
        }

        loadAnimation("idle");
        startAnimation();
    }

    public void addChangeListeners(ChangeListener changeListener) {
        listeners.add(changeListener);
    }

    public void notifyChangeListeners(){
        // Create a copy to avoid ConcurrentModificationException
        List<ChangeListener> listenersCopy =  new ArrayList<>(listeners);
        for(ChangeListener listener : listenersCopy){
            listener.stateChanged(new ChangeEvent(this));
        }
    }

    public void removeAnimation(){
        animation.stop();
        this.animation = null;
    }

    public void reconstruct() {
        if(imagePath != null)
            this.image = new ImageIcon(imagePath);
        this.sound = new Sound(RESOURCES_PATH + File.separator + "sound" + File.separator + "croak.wav");
        this.animation = null;
    }
}
