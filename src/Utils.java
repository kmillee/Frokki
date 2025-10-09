import javax.swing.*;
import java.awt.*;
import java.io.File;

public class Utils {

    public static ImageIcon resizeImageIcon(ImageIcon icon, int width, int height) {
        Image image = icon.getImage();
        Image resizedImage = image.getScaledInstance(width, height,  Image.SCALE_DEFAULT);
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

    public static void setFixedSize(JComponent component, int width, int height) {
        component.setPreferredSize(new Dimension(width, height));
        component.setMaximumSize(new Dimension(width, height));
        component.setMinimumSize(new Dimension(width, height));
        component.setSize(width, height);
    }

    public static void setFixedSize(JFrame frame, int width, int height) {
        frame.setPreferredSize(new Dimension(width, height));
        frame.setMaximumSize(new Dimension(width, height));
        frame.setMinimumSize(new Dimension(width, height));
        frame.setSize(width, height);
    }

    public static boolean contains(int[] list, int id){
        if (list == null) return false;
        for (int j : list) {
            if (j == id) return true;
        }
        return false;
    }
}
