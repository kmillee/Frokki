package pond;

import com.formdev.flatlaf.FlatClientProperties;
import frogedex.Frogedex;
import main.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Pond extends JComponent {

    private JFrame frame;
    private final String imagePath;
    private Grid grid;
    private Frogedex frogedex;

    public Pond() {
        // change image size
        this.imagePath = Constants.POND_IMAGE;
        setUpFrame();
        setUpPond();
        setUpDevMode();

        frame.setVisible(false);
        frame.pack();
    }



    private void setUpFrame() {
        frame = new JFrame();
        frame.setTitle("Pond");
        frame.setResizable(false);
        frame.setPreferredSize(new Dimension(860, 600));

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                deactivate();
            }
        });
    }

    private void deactivate() {
        frame.setVisible(false);
        grid.pauseTimers();
        if (grid.getToolbox() != null) {
            grid.getToolbox().getFrame().setVisible(false);
        }
    }

    public void activate() {
        frame.setVisible(true);
        grid.setUpTimers();
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
        JButton lilyButton = new JButton("Lily pad");


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


