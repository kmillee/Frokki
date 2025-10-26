package pond.View;

import java.awt.*;
import java.util.ArrayList;


/**
 * PondImages holds the visual assets for different objects in the pond.
 * <p>
 * This class provides easy access to images for rendering tiles in the pond.
 */
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
