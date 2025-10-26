package menu;

import com.formdev.flatlaf.FlatClientProperties;
import frog.frogbar.FrogBarController;
import frogedex.Frogedex;
import main.Constants;
import main.Utils;
import pond.Pond;

import javax.swing.*;
import javax.swing.border.Border;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;


public class Menu {
    private JFrame menuFrame;
    private Frogedex frogedex;
    private final Pond pond;

    public Menu() {
        setupSystemTray();

        frogedex = new Frogedex();
        pond = new Pond(frogedex);

        FrogBarController frogbar = new FrogBarController(frogedex);
        pond.setFrogedex(frogedex);

        toggleMenu();
    }
    public Frogedex getFrogedex() {
        return frogedex;
    }

    public void setFrogedex(Frogedex frogedex) {
        this.frogedex = frogedex;
    }

    private void setupSystemTray() {
        if (!SystemTray.isSupported()) {
            System.err.println("System tray not supported");
            return;
        }

        try {
            Image image = new ImageIcon(Constants.RESOURCES_PATH + File.separator + "frog_1.png").getImage();

            TrayIcon trayIcon = new TrayIcon(image, "Frog Game");
            trayIcon.setImageAutoSize(true);

            trayIcon.addActionListener(e -> {toggleMenu();});

            SystemTray tray = SystemTray.getSystemTray();
            tray.add(trayIcon);

        } catch (Exception ignored) {
        }
    }


    private void toggleMenu() {
        if (menuFrame == null) createMenu();
        menuFrame.setVisible(!menuFrame.isVisible());
    }

    private void createMenu() {
        menuFrame = new JFrame("Menu");
        menuFrame.setSize(200, 250);
        menuFrame.setResizable(false);
        menuFrame.setLayout(new BorderLayout());
        menuFrame.setLocationRelativeTo(null);
        menuFrame.setIconImage(new ImageIcon(Constants.RESOURCES_PATH + File.separator+ "pond" + File.separator + "lilypad.png").getImage());

        Font fontButton = Utils.loadFont("Gaegu" + File.separator + "Gaegu-Regular.ttf", 18);
        Font fontTitle = Utils.loadFont("Gaegu" + File.separator + "Gaegu-Regular.ttf", 24);

        // Title
        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBorder(new EmptyBorder(5, 5, 0, 5));
        JLabel titleLabel = new JLabel("Frokki");
        titleLabel.setFont(fontTitle);
        titlePanel.add(titleLabel);

        // Menu buttons
        JButton pondButton = new JButton("Pond");
        JButton frogedexButton = new JButton("Frogedex");
        JButton quitButton = new JButton("Quit");
        JButton helpButton = new JButton("Help");

        pondButton.setToolTipText("Open the pond and catch new frogs");
        frogedexButton.setToolTipText("Open your frog collection and summon them on your taskbar");
        quitButton.setToolTipText("Quit the application");
        helpButton.setToolTipText("Some explanations about the game");

        pondButton.putClientProperty(FlatClientProperties.STYLE, "arc: 20;");
        frogedexButton.putClientProperty(FlatClientProperties.STYLE, "arc: 20;");
        quitButton.putClientProperty(FlatClientProperties.STYLE, "arc: 15;" +
                "background: #f6685e;" +
                "disabledBackground: #f6685e;" +
                "focusedBackground: #f6685e;");
        helpButton.putClientProperty(FlatClientProperties.STYLE, "arc: 15;");


        pondButton.setFont(fontButton);
        frogedexButton.setFont(fontButton);
        quitButton.setFont(fontButton);
        helpButton.setFont(fontButton);

        // Toggle buttons: Sound and Dev mode
        ImageIcon soundOnIcon = new ImageIcon(Utils.resizeKeepingRatio(Constants.SOUND_ON, 20, 20).getImage());
        ImageIcon soundOffIcon = new ImageIcon(Utils.resizeKeepingRatio(Constants.SOUND_OFF, 20, 20).getImage());

        JToggleButton soundButton = new JToggleButton(soundOnIcon);
        JToggleButton devButton = new JToggleButton("Dev OFF");

        soundButton.putClientProperty("soundOn", true);
        devButton.putClientProperty("devMode", false);

        soundButton.putClientProperty(FlatClientProperties.STYLE, """
                arc:15;
                background:null;
                selectedBackground:null;
                focusedBackground:null;
                hoverBackground:null;
                """);
        devButton.putClientProperty(FlatClientProperties.STYLE, "arc: 15;");
        devButton.setFont(Utils.loadFont("Gaegu" + File.separator + "Gaegu-Regular.ttf", 14));
        devButton.setToolTipText("Developer mode: enable spawn buttons on the pond.");


        soundButton.addActionListener(e -> {
            boolean selected = soundButton.isSelected();
            soundButton.setIcon(selected ? soundOnIcon : soundOffIcon);
            pond.toggleSound();
        });

        devButton.addActionListener(e -> {
            boolean selected = devButton.isSelected();
            devButton.setText(selected ? "Dev ON" : "Dev OFF");
            pond.toggleDevMode();
        });

        JPanel togglePanel = new JPanel(new GridLayout(1, 2, 5, 0));
        togglePanel.add(soundButton);
        togglePanel.add(devButton);


        // Layout
        JPanel menuButtonPanel = new JPanel();
        menuButtonPanel.setLayout(new GridLayout(0, 1, 0, 5));
        menuButtonPanel.add(pondButton);
        menuButtonPanel.add(frogedexButton);
        menuButtonPanel.add(togglePanel);
        menuButtonPanel.setBorder(new EmptyBorder(5, 10, 30, 10));

        JPanel quitHelpPanel = new JPanel();
        quitHelpPanel.setLayout(new GridLayout(0,2, 10, 5));
        quitHelpPanel.add(quitButton);
        quitHelpPanel.add(helpButton);
        quitHelpPanel.setBorder(new EmptyBorder(0, 10, 10, 10));

        pondButton.addActionListener(e -> openPond());
        frogedexButton.addActionListener(e -> openFrogedex());
        helpButton.addActionListener(e -> {
            new HelpDialog(menuFrame).setVisible(true);
        });
        quitButton.addActionListener(e -> quitGame());

        menuFrame.add(titlePanel, BorderLayout.NORTH);
        menuFrame.add(menuButtonPanel, BorderLayout.CENTER);
        menuFrame.add(quitHelpPanel, BorderLayout.SOUTH);
    }

    private void quitGame() {
        int choice = JOptionPane.showConfirmDialog(
                menuFrame,
                "Are you sure you want to quit Frokki? \n(Your collection will be saved, but the pond state will reset)",
                "Quit Game",
                JOptionPane.YES_NO_OPTION,
                JOptionPane.WARNING_MESSAGE
        );

        if (choice == JOptionPane.YES_OPTION) {
            System.exit(0);
        }
    }


    private void openFrogedex() {
        frogedex.show();
    }

    private void openPond() {
//        if (pond == null){
//            pond = new Pond();
//            System.out.print("no pond found, creating a new one");
//        }
        pond.activate();
    }


}
