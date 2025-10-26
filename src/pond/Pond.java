package pond;

import com.formdev.flatlaf.FlatClientProperties;
import frogedex.Frogedex;
import main.Constants;

import javax.swing.*;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

public class Pond {

    private JFrame frame;
    private final String imagePath;
    private PondView pondView;
    private PondModel model;
    private PondController controller;
    private Frogedex frogedex;

    public Pond(Frogedex frogedex) {
        this.frogedex = frogedex;
        this.imagePath = Constants.POND_IMAGE;

        setUpFrame();
        setUpModelViewController();
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
        if (controller != null) controller.pauseTimers();
        if (controller != null && controller.getToolbox() != null) {
            controller.getToolbox().getFrame().setVisible(false);
        }
    }

    public void activate() {
        frame.setVisible(true);
        if (controller != null) controller.setUpTimers();
    }

    private void setUpModelViewController() {
        ImageIcon pondImg = new ImageIcon(imagePath);
        int width = pondImg.getIconWidth();
        int height = pondImg.getIconHeight();

        // 1. Model
        model = new PondModel(width, height, Constants.CELL_SIZE);

        // 2. View
        pondView = new PondView(this, model, pondImg.getImage());
        frame.add(pondView, BorderLayout.CENTER);
        pondView.setPreferredSize(new Dimension(width, height));

        // 3. Controller
        controller = new PondController(this, model, pondView);
        pondView.setController(controller);

    }

    private void setUpDevMode() {
        JPanel buttonBar = new JPanel();
        buttonBar.setLayout(new FlowLayout());

        JButton frogButton = new JButton("Frog");
        JButton reedButton = new JButton("Reed");
        JButton lilyButton = new JButton("Lily pad");
        JButton crocoButton = new JButton("Croco");
        JButton showToolButton = new JButton("Show Tool");

        frogButton.addActionListener(e -> controller.trySpawnFrog());
        reedButton.addActionListener(e -> controller.trySpawnReed());
        lilyButton.addActionListener(e -> controller.trySpawnLily());
        crocoButton.addActionListener(e -> controller.trySpawnCroco());
        showToolButton.addActionListener(e -> controller.getToolbox().show());

        buttonBar.add(frogButton);
        buttonBar.add(reedButton);
        buttonBar.add(lilyButton);
        buttonBar.add(crocoButton);
        buttonBar.add(showToolButton);

        frame.add(buttonBar, BorderLayout.SOUTH);
    }

    // ---- Getters / Setters ----
    public Frogedex getFrogedex() {
        return frogedex;
    }

    public void setFrogedex(Frogedex frogedex) {
        this.frogedex = frogedex;
    }

    public PondView getPondView() {
        return pondView;
    }

    public PondModel getModel() {
        return model;
    }

    public PondController getController() {
        return controller;
    }
}
















//import java.awt.event.WindowEvent;
//
//public class Pond extends JComponent {
//
//    private JFrame frame;
//    private final String imagePath;
//    private Grid grid;
//    private Frogedex frogedex;
//
//    public Pond() {
//        // change image size
//        this.imagePath = Constants.POND_IMAGE;
//        setUpFrame();
//        setUpPond();
//        setUpDevMode();
//
//        frame.setVisible(false);
//        frame.pack();
//    }
//
//
//
//    private void setUpFrame() {
//        frame = new JFrame();
//        frame.setTitle("Pond");
//        frame.setResizable(false);
//        frame.setPreferredSize(new Dimension(860, 600));
//
//        frame.addWindowListener(new WindowAdapter() {
//            public void windowClosing(WindowEvent e) {
//                deactivate();
//            }
//        });
//    }
//
//    private void deactivate() {
//        frame.setVisible(false);
//        grid.pauseTimers();
//        if (grid.getToolbox() != null) {
//            grid.getToolbox().getFrame().setVisible(false);
//        }
//    }
//
//    public void activate() {
//        frame.setVisible(true);
//        grid.setUpTimers();
//    }
//
//    private void setUpPond(){
//        grid = new Grid(this, Constants.CELL_SIZE, imagePath);
//        frame.add(grid, BorderLayout.CENTER);
//        frame.pack();
//        grid.installUI();
//    }
//
//    private void setUpDevMode(){
//        JPanel buttonBar = new JPanel();
//        buttonBar.setLayout(new FlowLayout());
//
//        JButton frogButton = new JButton("Frog");
//        JButton reedButton = new JButton("Reed");
//        JButton lilyButton = new JButton("Lily pad");
//
//
//        buttonBar.add(frogButton);
//        frogButton.addActionListener(e -> {grid.spawnFrog();});
//        buttonBar.add(reedButton);
//        reedButton.addActionListener(e -> {grid.spawnReed();});
//        buttonBar.add(lilyButton);
//        lilyButton.addActionListener(e -> {grid.spawnLily();});
//
//        JButton showToolButton = new JButton("Show Tool");
//        buttonBar.add(showToolButton);
//        showToolButton.addActionListener(e -> {
//            grid.getToolbox().show();
//        });
//
//        JButton crocoButton = new JButton("Croco");
//        buttonBar.add(crocoButton);
//        crocoButton.addActionListener(e -> {
//            grid.spawnCroco();
//        });
//
//        frame.add(buttonBar, BorderLayout.SOUTH);
//    }
//
//    public Frogedex getFrogedex() {
//        return frogedex;
//    }
//
//    public void setFrogedex(Frogedex frogedex) {
//        this.frogedex = frogedex;
//    }
//
//}
//
//
