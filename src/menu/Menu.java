package menu;

import frog.frogbar.FrogBarController;
import frogedex.Frogedex;
import main.Constants;
import pond.Pond;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;


public class Menu {
    private JFrame menuFrame;

    public Frogedex getFrogedex() {
        return frogedex;
    }

    public void setFrogedex(Frogedex frogedex) {
        this.frogedex = frogedex;
    }

    private Frogedex frogedex;
    private final Pond pond;

    public Menu() {
        setupSystemTray();

        pond = new Pond();
        frogedex = new Frogedex();
        FrogBarController frogbar = new FrogBarController(frogedex);
        pond.setFrogedex(frogedex);
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
        menuFrame = new JFrame("Frog Game Menu");
        menuFrame.setUndecorated(true);
        menuFrame.setSize(200, 250);
        menuFrame.setLayout(new GridLayout(0, 1, 5, 5));
        menuFrame.setAlwaysOnTop(true);

        JButton pondButton = new JButton("Pond");
        JButton frogedexButton = new JButton("Frogedex");
        JButton settingsButton = new JButton("Settings");
        JButton quitButton = new JButton("Quit");
        quitButton.setBackground(Color.RED);
        quitButton.setForeground(Color.WHITE);

        pondButton.addActionListener(e -> openPond());
        frogedexButton.addActionListener(e -> openFrogedex());
        settingsButton.addActionListener(e -> openSettings());
        quitButton.addActionListener(e -> quitGame());

        menuFrame.add(pondButton);
        menuFrame.add(frogedexButton);
        menuFrame.add(settingsButton);
        menuFrame.add(quitButton);

        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        menuFrame.setLocation(screenSize.height / 2 - menuFrame.getHeight() / 2, screenSize.width / 2 - menuFrame.getWidth() / 2);
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
