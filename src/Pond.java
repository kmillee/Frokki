import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class Pond extends JComponent {

    //image pond
    //list animaux
    //gestion vie du pond >> dechet / apparition d'animaux
    // gestion de position (matrice de tiles)
    // classe tile (check if frog, water,

    // display pond image in the frame and a grid on top of it

    private JFrame frame;
    private ImageIcon pondImage;
    private String imagePath;

    public Pond(String imagePath) {
        // change image size
        this.imagePath = imagePath;
        this.pondImage = new ImageIcon(imagePath);
        setUpFrame();
        setUpPond();

        frame.setVisible(true);
        frame.pack();
    }

    private void setUpFrame() {
        frame = new JFrame();
        frame.setTitle("Pond");
        frame.setPreferredSize(new Dimension(1100, 600));
//        setBackground(new Color(255, 255, 255,40));

    }

    private void setUpPond(){
        Grid grid = new Grid(25, imagePath);
        frame.add(grid, BorderLayout.CENTER);
        frame.pack();
        grid.addMouseListener(grid);
        grid.addMouseMotionListener(grid);
        grid.addKeyListener(grid);
        grid.requestFocus();

    }



}


