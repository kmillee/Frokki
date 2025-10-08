import javax.swing.*;

public class Frog {


    private String imagePath, name, species, acquisitionDate;
    private ImageIcon image;

    private int experience;
    private boolean active = false; // If active is true, it means the frog is on the toolbar
    // String rarity; ?
    // Point barPos; (to drag and drop them
    // on the bar, make them jump, interact)


    public Frog(String imagePath, String name, String species, String acquisitionDate) {
        this.imagePath = imagePath;
        this.image = new ImageIcon(imagePath);
        this.name = name;
        this.species = species;
        this.acquisitionDate = acquisitionDate;
        this.experience = 50;
    }

    public Frog(String imagePath){
        this(imagePath, "John Toad", "basic bitch", "00/00/0000");
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
    }

    public boolean isActive() {
        return active;
    }
}
