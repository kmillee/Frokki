import javax.swing.*;

public class Frog {


    private String imagePath,name,species;
    private ImageIcon image;

    private int exp;
    // String rarity; ?
    // Point barPos; (to drag and drop them on the bar, make them jump, interact)

    public Frog(String imagePath, String name, String species){
        this.imagePath = imagePath;
        this.image = new ImageIcon(imagePath);
        this.name = name;
        this.species = species;
        this.exp = 0;
    }

    public Frog(String imagePath){
        this.imagePath = imagePath;
        this.image = new ImageIcon(imagePath);
        this.name = "John Toad";
        this.species = "basic bitch";
        this.exp = 0;
    }

    public String getImagePath() {
        return imagePath;
    }

    public void setImagePath(String imagePath) {
        this.imagePath = imagePath;
    }


}
