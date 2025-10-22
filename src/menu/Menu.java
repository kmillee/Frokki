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

        pond = new Pond();
        frogedex = new Frogedex();
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

        JPanel titlePanel = new JPanel();
        titlePanel.setLayout(new FlowLayout(FlowLayout.CENTER));
        titlePanel.setBorder(new EmptyBorder(5, 5, 0, 5));
        JLabel titleLabel = new JLabel("Frokki");
        titleLabel.setFont(fontTitle);
        titlePanel.add(titleLabel);

        // Menu buttons
        JButton pondButton = new JButton("Show pond");
        JButton frogedexButton = new JButton("Frogedex");
        //JButton settingsButton = new JButton("Settings");
        JButton quitButton = new JButton("Quit");
        JButton helpButton = new JButton("Help");

        pondButton.putClientProperty(FlatClientProperties.STYLE, "arc: 20;");
        frogedexButton.putClientProperty(FlatClientProperties.STYLE, "arc: 20;");
        //settingsButton.putClientProperty(FlatClientProperties.STYLE, "arc: 20;");
        quitButton.putClientProperty(FlatClientProperties.STYLE, "arc: 15;" +
                "background: #f6685e;" +
                "disabledBackground: #f6685e;" +
                "focusedBackground: #f6685e;");
        helpButton.putClientProperty(FlatClientProperties.STYLE, "arc: 15;");

        pondButton.setFont(fontButton);
        frogedexButton.setFont(fontButton);
        //settingsButton.setFont(fontButton);
        quitButton.setFont(fontButton);
        helpButton.setFont(fontButton);


        helpButton.addActionListener(e -> {
            new HelpDialog(menuFrame).setVisible(true);
        });

        //
        JPanel menuButtonPanel = new JPanel();
        menuButtonPanel.setLayout(new GridLayout(0, 1, 0, 5));
        menuButtonPanel.add(pondButton);
        menuButtonPanel.add(frogedexButton);
        //menuButtonPanel.add(settingsButton);
        menuButtonPanel.setBorder(new EmptyBorder(5, 10, 30, 10));

        JPanel quitHelpPanel = new JPanel();
        quitHelpPanel.setLayout(new GridLayout(0,2, 10, 5));
        quitHelpPanel.add(quitButton);
        quitHelpPanel.add(helpButton);
        quitHelpPanel.setBorder(new EmptyBorder(0, 10, 10, 10));

        pondButton.addActionListener(e -> openPond());
        frogedexButton.addActionListener(e -> openFrogedex());
        //settingsButton.addActionListener(e -> openSettings());
        quitButton.addActionListener(e -> quitGame());

        menuFrame.add(titlePanel, BorderLayout.NORTH);
        menuFrame.add(menuButtonPanel, BorderLayout.CENTER);
        menuFrame.add(quitHelpPanel, BorderLayout.SOUTH);
    }

    private void quitGame() {
        System.exit(0);
        //todo: mettre un pop up de vérif
    }

    private void openSettings() {
        //todo
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
