package pond;

import java.awt.*;
import java.util.ArrayList;

public class PondImages {
    public Image lilyImg;
    public Image rottenImg;
    public Image crocoImg;
    public ArrayList<Image> reedImages;

    public PondImages(Image lilyImg, Image rottenImg, Image crocoImg, ArrayList<Image> reedImages) {
        this.lilyImg = lilyImg;
        this.rottenImg = rottenImg;
        this.crocoImg = crocoImg;
        this.reedImages = reedImages;
    }
}
