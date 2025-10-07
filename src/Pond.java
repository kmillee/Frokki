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
    private Grid grid;

    public Pond(String imagePath) {
        // change image size
        this.imagePath = imagePath;
        this.pondImage = new ImageIcon(imagePath);
        setUpFrame();
        setUpPond();
        setUpDevMode();

        frame.setVisible(true);
        frame.pack();
    }

    private void setUpFrame() {
        frame = new JFrame();
        frame.setTitle("Pond");
//        frame.setUndecorated(true);
        frame.setPreferredSize(new Dimension(1100, 600));
//        setBackground(new Color(255, 255, 255,40));

    }

    private void setUpPond(){
        grid = new Grid(25, imagePath);
        frame.add(grid, BorderLayout.CENTER);
        frame.pack();
        grid.installUI();

    }

    private void setUpDevMode(){
        JPanel buttonBar = new JPanel();
        buttonBar.setLayout(new FlowLayout());

        JButton frogButton = new JButton("Frog");
        JButton reeveButton = new JButton("Reeve");
        JButton lilyButton = new JButton("Lilypad");

        buttonBar.add(frogButton);
        frogButton.addActionListener(e -> {grid.spawnFrog();});
        buttonBar.add(reeveButton);
        reeveButton.addActionListener(e -> {grid.spawnReeve();});
        buttonBar.add(lilyButton);
        lilyButton.addActionListener(e -> {grid.spawnLily();});



        frame.add(buttonBar, BorderLayout.SOUTH);
    }



}


