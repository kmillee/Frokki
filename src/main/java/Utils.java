package main.java;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Utils {

    /**
     * Resizes an ImageIcon to the specified width and height.
     * @param icon The ImageIcon to resize.
     * @param width The desired width.
     * @param height The desired height.
     * @return The resized ImageIcon.
     */
    public static ImageIcon resizeImageIcon(ImageIcon icon, int width, int height) {
        Image image = icon.getImage();
        Image resizedImage = image.getScaledInstance(width, height,  Image.SCALE_DEFAULT);
        return new ImageIcon(resizedImage);
    }

    /**
     * Resizes an ImageIcon while keeping its aspect ratio.
     * @param icon The ImageIcon to resize.
     * @param maxWidth The maximum desired width.
     * @param maxHeight The maximum desired height.
     * @return The resized ImageIcon.
     */
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

    /**
     * Loads a font from the specified path and size.
     * @param fontPath The path to the font file.
     * @param size The desired font size.
     * @return The loaded Font, or a default font if loading fails.
     */
    public static Font loadFont(String fontPath, float size) {
        try {
            Font font = Font.createFont(Font.TRUETYPE_FONT, new File(Constants.RESOURCES_PATH + File.separator + fontPath));
            return font.deriveFont(size);
        } catch(Exception e) {
            e.printStackTrace();
            return new  Font("Arial", Font.PLAIN, (int)size);
        }
    }

    /**
     * Sets a fixed size for a JComponent.
     * @param component The component to set the size for
     * @param width The desired width
     * @param height The desired height
     */
    public static void setFixedSize(JComponent component, int width, int height) {
        component.setPreferredSize(new Dimension(width, height));
        component.setMaximumSize(new Dimension(width, height));
        component.setMinimumSize(new Dimension(width, height));
        component.setSize(width, height);
    }

    /**
     * Sets a fixed size for a JFrame.
     * @param frame The frame to set the size for
     * @param width The desired width
     * @param height The desired height
     */
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

    /**
     * Loads all image frames from a specified directory.
     * @param directoryPath The path to the directory containing the image frames.
     * @return A list of ImageIcons representing the image frames.
     */
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


    public static void setCustomCursor(ImageIcon cursor, Container container){
        // Set a custom cursor
        Toolkit toolkit = Toolkit.getDefaultToolkit();
        Cursor customCursor = toolkit.createCustomCursor(cursor.getImage(), new Point(0, 0), "Custom Cursor");
        container.setCursor(customCursor);
    }

}
