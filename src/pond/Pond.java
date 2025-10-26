package pond;

import com.formdev.flatlaf.FlatClientProperties;
import frogedex.Frogedex;
import main.Constants;
import main.Utils;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

/**
 * This class is responsible for creating and displaying the Pond frame,
 * with which user can interact to catch frogs
 */
public class Pond {

    private JFrame frame;
    private final String imagePath;
    private PondView pondView;
    private PondModel model;
    private PondController controller;
    private Frogedex frogedex;

    // Developer mode variables
    private JPanel devPanel;
    private boolean devModeEnabled;


    public Pond(Frogedex frogedex) {
        this.frogedex = frogedex;
        this.imagePath = Constants.POND_IMAGE;

        setUpFrame();
        setUpModelViewController();
        setUpKeyboardShortcut();
        setUpDevMode();

        frame.setVisible(false);
        frame.pack();
        frame.setFocusable(true);
    }

    // ---- FRAME SETUP ----
    private void setUpFrame() {
        frame = new JFrame("Pond");
        frame.setResizable(false);
        frame.setPreferredSize(new Dimension(860, 610));
        frame.setIconImage(new ImageIcon(Constants.POND_IMAGE).getImage());

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

    // ---- MVC SETUP ----
    private void setUpModelViewController() {
        ImageIcon pondImg = new ImageIcon(imagePath);
        int width = pondImg.getIconWidth();
        int height = pondImg.getIconHeight();

        model = new PondModel(width, height, Constants.CELL_SIZE);

        pondView = new PondView(model, pondImg.getImage());
        frame.add(pondView, BorderLayout.CENTER);
        pondView.setPreferredSize(new Dimension(width, height));

        controller = new PondController(this, model, pondView);
        pondView.setController(controller);
        pondView.addToolboxButton(controller);


    }


    // ---- DEV MODE ----
    private void setUpDevMode() {

        devPanel = new JPanel(new FlowLayout());
        devPanel.setVisible(false);

        JButton frogButton = new JButton("Frog");
        JButton reedButton = new JButton("Reed");
        JButton lilyButton = new JButton("Lily pad");
        JButton crocoButton = new JButton("Croco");

        frogButton.addActionListener(e -> controller.trySpawnFrog());
        reedButton.addActionListener(e -> controller.trySpawnReed());
        lilyButton.addActionListener(e -> controller.trySpawnLily());
        crocoButton.addActionListener(e -> controller.trySpawnCroco());

        devPanel.add(frogButton);
        devPanel.add(reedButton);
        devPanel.add(lilyButton);
        devPanel.add(crocoButton);

        frame.add(devPanel, BorderLayout.SOUTH);
//        JPanel buttonBar = new JPanel();
//        buttonBar.setLayout(new FlowLayout());
//
//        JButton frogButton = new JButton("Frog");
//        JButton reedButton = new JButton("Reed");
//        JButton lilyButton = new JButton("Lily pad");
//        JButton crocoButton = new JButton("Croco");
//        JButton showToolButton = new JButton("Show Tool");
//
//        frogButton.addActionListener(e -> controller.trySpawnFrog());
//        reedButton.addActionListener(e -> controller.trySpawnReed());
//        lilyButton.addActionListener(e -> controller.trySpawnLily());
//        crocoButton.addActionListener(e -> controller.trySpawnCroco());
//        showToolButton.addActionListener(e -> controller.getToolbox().show());
//
//        buttonBar.add(frogButton);
//        buttonBar.add(reedButton);
//        buttonBar.add(lilyButton);
//        buttonBar.add(crocoButton);
//        buttonBar.add(showToolButton);
//
//        frame.add(buttonBar, BorderLayout.SOUTH);
    }

    private void setUpKeyboardShortcut() {
        setUpDevMode();

        // Key listener for secret shortcut: Ctrl + Shift + D
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println(e.getKeyCode());
                if (e.isControlDown() && e.isShiftDown() && e.getKeyCode() == KeyEvent.VK_D) {
                    toggleDevMode();

                }
            }
        });
    }

    public void toggleDevMode() {
        devModeEnabled = !devModeEnabled;
        devPanel.setVisible(devModeEnabled);
        frame.revalidate();
        frame.repaint();

        System.out.println(devModeEnabled ? "[DEV MODE ENABLED]" : "[DEV MODE DISABLED]");
    }

    public boolean toggleSound() {
        boolean newState = !sound.Sound.isMute();
        sound.Sound.setMute(newState);
        System.out.println("Mute: " + newState);
        return newState;
    }


    // ---- GETTERS / SETTERS ----
    public Frogedex getFrogedex() {
        return frogedex;
    }

    public void setFrogedex(Frogedex frogedex) {
        this.frogedex = frogedex;
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
