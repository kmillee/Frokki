package main.java.pond;

import main.java.frogedex.Frogedex;
import main.java.Constants;

import javax.swing.*;
import java.awt.*;

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

    private Frogedex frogedex;

    public Pond() {
        // change image size
        this.imagePath = Constants.POND_IMAGE;
        this.pondImage = new ImageIcon(imagePath);
        setUpFrame();
        setUpPond();
        setUpDevMode();

        frame.setVisible(true);
        frame.pack();
    }

    private void setUpFrame() {
        frame = new JFrame();
        frame.setTitle("pond.Pond");
//        frame.setUndecorated(true);
        frame.setPreferredSize(new Dimension(1100, 600));
//        setBackground(new Color(255, 255, 255,40));

    }

    private void setUpPond(){
        grid = new Grid(this, Constants.CELL_SIZE, imagePath);
        frame.add(grid, BorderLayout.CENTER);
        frame.pack();
        grid.installUI();

    }

    private void setUpDevMode(){
        JPanel buttonBar = new JPanel();
        buttonBar.setLayout(new FlowLayout());

        JButton frogButton = new JButton("Frog");
        JButton reedButton = new JButton("Reed");
        JButton lilyButton = new JButton("Lilypad");


        buttonBar.add(frogButton);
        frogButton.addActionListener(e -> {grid.spawnFrog();});
        buttonBar.add(reedButton);
        reedButton.addActionListener(e -> {grid.spawnReed();});
        buttonBar.add(lilyButton);
        lilyButton.addActionListener(e -> {grid.spawnLily();});

        JButton showToolButton = new JButton("Show Tool");
        buttonBar.add(showToolButton);
        showToolButton.addActionListener(e -> {
            grid.getToolbox().show();
        });

        JButton crocoButton = new JButton("Croco");
        buttonBar.add(crocoButton);
        crocoButton.addActionListener(e -> {
            grid.spawnCroco();
        });

        frame.add(buttonBar, BorderLayout.SOUTH);
    }

    public Frogedex getFrogedex() {
        return frogedex;
    }

    public void setFrogedex(Frogedex frogedex) {
        this.frogedex = frogedex;
    }

}


