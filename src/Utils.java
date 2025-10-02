import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Utils {

    public static ImageIcon resizeImageIcon(ImageIcon icon, int width, int height) {
        Image image = icon.getImage();
        Image resizedImage = image.getScaledInstance(width, height, 4);
        return new ImageIcon(resizedImage);
    }

    public static Font loadFont(String fontPath, float size) {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File("media" + File.separator + fontPath));
            return font.deriveFont(size);
        } catch(Exception e) {
            e.printStackTrace();
            return new  Font("Arial", Font.PLAIN, (int)size);
        }
    }
}
