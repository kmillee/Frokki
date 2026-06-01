package pond;

import frogedex.Frogedex;
import main.Constants;
import pond.Controller.PondController;
import pond.View.PondView;

import javax.swing.*;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

/**
 * The Pond class is the main container for the pond component of the app.
 * <p>
 * Responsibilities:
 * - Sets up the JFrame that displays the pond.
 * - Initializes the MVC components: PondModel, PondView, PondController.
 * - Handles developer mode (spawning objects manually, activated by Ctrl + Shift + D).
 * - Provides activation/deactivation methods for showing or hiding the pond.
 */
public class Pond {

    private JFrame frame;   // Main game window
    private final String imagePath; // Background pond image path
    private PondView view;
    private PondModel model;
    private PondController controller;
    private Frogedex frogedex;

    // Developer mode variables
    private JPanel devPanel;
    private boolean devModeEnabled;


    /**
     * Constructor: Initializes the pond with Frogedex reference.
     * Sets up the frame, MVC components, keyboard shortcuts, and dev mode.
     */
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
        ImageIcon icon = Constants.loadIcon("/pond/pond.png");

        if(icon != null) frame.setIconImage(icon.getImage());

        frame.addWindowListener(new WindowAdapter() {
            public void windowClosing(WindowEvent e) {
                deactivate();
            }
        });
    }

    /** Hides the pond and stops timers/toolbox */
    private void deactivate() {
        frame.setVisible(false);
        if (controller != null) controller.pauseTimers();
        if (controller != null && controller.getToolbox() != null) {
            controller.getToolbox().getFrame().setVisible(false);
        }
    }

    /** Shows the pond and starts timers */
    public void activate() {
        frame.setVisible(true);
        if (controller != null) controller.setUpTimers();
    }

    // ---- MVC SETUP ----
    private void setUpModelViewController() {
        ImageIcon pondImg = Constants.loadIcon(Constants.POND_IMAGE);
        int width = pondImg.getIconWidth();
        int height = pondImg.getIconHeight();

        // Model: represents pond grid and objects
        model = new PondModel(width, height, Constants.CELL_SIZE);

        // View: handles rendering and mouse coordinates
        view = new PondView(model, pondImg.getImage());
        frame.add(view, BorderLayout.CENTER);
        view.setPreferredSize(new Dimension(width, height));

        // Controller: handles interaction logic
        controller = new PondController(this, model, view);
        view.setController(controller);
        view.addToolboxButton(controller);


    }


    // ---- DEV MODE ----

    /** Creates a panel with buttons to spawn objects manually */
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
    }

    /** Sets up keyboard shortcut Ctrl+D to toggle developer mode */
    private void setUpKeyboardShortcut() {
        setUpDevMode();

        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                System.out.println(e.getKeyCode());
                if (e.isControlDown() && e.getKeyCode() == KeyEvent.VK_D) {
                    toggleDevMode();

                }
            }
        });
    }

    /** Toggles visibility of dev mode panel */
    public void toggleDevMode() {
        devModeEnabled = !devModeEnabled;
        devPanel.setVisible(devModeEnabled);
        frame.revalidate();
        frame.repaint();

        System.out.println(devModeEnabled ? "[DEV MODE ENABLED]" : "[DEV MODE DISABLED]");
    }

    public void toggleSound() {
        boolean newState = !sound.Sound.isMute();
        sound.Sound.setMute(newState);
        System.out.println("Mute: " + newState);
    }


    // ---- GETTERS / SETTERS ----
    public Frogedex getFrogedex() { return frogedex; }
    public void setFrogedex(Frogedex frogedex) { this.frogedex = frogedex; }
}