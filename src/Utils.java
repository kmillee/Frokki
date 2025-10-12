import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    public static ImageIcon resizeImageIcon(ImageIcon icon, int width, int height) {
        Image image = icon.getImage();
        Image resizedImage = image.getScaledInstance(width, height,  Image.SCALE_DEFAULT);
        return new ImageIcon(resizedImage);
    }

    public static ImageIcon resizeKeepingRatio(ImageIcon icon, int maxWidth, int maxHeight) {
        Image image = icon.getImage();
        int width = image.getWidth(null);
        int height = image.getHeight(null);

        double widthRatio = (double) maxWidth / width;
        double heightRatio = (double) maxHeight / height;
        double scaleRatio = Math.min(widthRatio, heightRatio);

        int newWidth = (int) (width * scaleRatio);
        int newHeight = (int) (height * scaleRatio);

        // Resize image
        Image scaledImage =  image.getScaledInstance(newWidth, newHeight,  Image.SCALE_DEFAULT);
        return new ImageIcon(scaledImage);
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

    public static List<ImageIcon> loadFrames(String directoryPath){
        List<ImageIcon> frames = new ArrayList<ImageIcon>();
        File directory = new File(directoryPath);

        if(directory.exists() && directory.isDirectory()){
            File[] files = directory.listFiles();
            if(files != null){
                for(File file: files){
                    frames.add(new ImageIcon(file.getAbsolutePath()));
                }
            }
        }
        return frames;
    }
}
