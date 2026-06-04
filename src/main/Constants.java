package main;

import frog.Frog;
import frog.FrogSpecies;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Centralized constants for game configuration, asset paths, and UI behavior.
 * This class contains no runtime logic except for initializing global frog data.
 */
public class Constants {

    // ---- GENERAL PATHS ----
    public static final String RESOURCES_PATH = "src" +File.separator + "resources";

    
    public static String getJarDirectory() {
        try {
            return new File(
                    Constants.class
                            .getProtectionDomain()
                            .getCodeSource()
                            .getLocation()
                            .toURI()
            ).getParent();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static final String DATA_FILE =
            getJarDirectory() + File.separator + "frogedex_data.json";

    // ---- FROG DATA ----

    /** Global list of all frog species registered in the game. */
    public static final List<Frog> FROGS = new ArrayList<>();
    /**
     * Initialize the frog list (called in main)
     * Automatically loads 13 frogs with default names, images, and species.
     */
    public static void setUpFrogList(){
        if (!FROGS.isEmpty()) return;
        for (int i = 0 ; i < 13 ; i++){
            Frog frog = new Frog("/frog_" + i + ".png", "The Frog "+ i, FrogSpecies.getSpecies(i), "01/01/2025");
            FROGS.add(frog);
        }
    }

    /** Total experience needed to reach the next level */
    public static final int EXPERIENCE_THRESHOLD = 100;

    // ---- FROGBAR CONSTANTS ----
    public static final int TASKBAR_FROG_SIZE = 100;
    public static final int TASKBAR_OFFSET = 48;

    // ---- UI ICONS ----
//    public static final ImageIcon SOUND_ON = new ImageIcon( RESOURCES_PATH + File.separator + "sound_on.png");
//    public static final ImageIcon SOUND_OFF = new ImageIcon( RESOURCES_PATH + File.separator + "sound_off.png");
    public static final ImageIcon SOUND_ON = loadIcon("/sound_on.png");

    public static final ImageIcon SOUND_OFF = loadIcon("/sound_off.png");

    // ---- POND CONFIGURATION ----
//    public static final String POND_IMAGE = RESOURCES_PATH + File.separator + "pond" + File.separator + "pond.png";
    public static final String POND_IMAGE = "/pond/pond.png";;
    public static final int CELL_SIZE = 25;
    public static final int[] WATER_TILES = {175, 176, 177, 191, 192, 207, 208, 209, 210, 211, 221, 222, 223, 224, 225, 226, 241, 242, 243, 244, 245, 246, 247, 248, 249, 250, 251, 252, 253, 254, 255, 256, 257, 258, 259, 274, 275, 276, 277, 278, 279, 280, 281, 282, 283, 284, 285, 288, 289, 290, 291, 307, 308, 309, 310, 311, 312, 313, 314, 315, 316, 317, 318, 321, 322, 323, 324, 342, 343, 344, 345, 346, 347, 348, 349, 350, 351, 354, 355, 356, 357, 376, 377, 378, 379, 380, 381, 382, 383, 384, 385, 386, 387, 388, 389, 411, 412, 413, 414, 415, 416, 417 };
    public static final int MAX_OBJECTS = 30; // Maximum number of objects on pond simultaneously (reeve, lily pads & rotten lily pads)


    // ---- SPAWN TIMERS (in ms) ----
    public static final int MIN_REEDLILY_TIMER = 30000; //30 sec
    public static final int MAX_REEDLILY_TIMER = 90000; // 90sec

    public static final int MIN_ROT_TIMER = 120000; // 2min
    public static final int MAX_ROT_TIMER = 300000; // 5min

    public static final int MIN_FROG_TIMER = 180000; // 3min
    public static final int MAX_FROG_TIMER = 300000; // 5min

    public static final int MIN_CROCO_TIMER = 300000; // 10min
    public static final int MAX_CROCO_TIMER = 18000000; //30min



    // ---- TOOL ICONS ----
    public static final ImageIcon SCISSORS_IMG = loadIcon("/tool/scissors.png");
    public static final ImageIcon NET_IMG = loadIcon("/tool/net.png");
    public static final ImageIcon GRAB_BEFORE_IMG = loadIcon("/tool/grab_before.png");
    public static final ImageIcon GRAB_WHILE_IMG = loadIcon("/tool/grab_while.png");
    public static final ImageIcon BIN_IMG = loadIcon("/tool/bin.png");
    public static final ImageIcon BELL_IMG = loadIcon("/tool/bell.png");


    // ---- BELL MOVEMENT CONSTANTS ----
    public static final int MAX_HISTORY_MS = 500; // look at last 500ms of movement
    public static final int INACTIVITY_MS = 200;   // tome to consider movement as "stopped"
    public static final double SPEED_THRESHOLD = 0.5; // pixel/ms




    private static java.net.URL getResource(String path) {
        return Constants.class.getResource(path);
    }

    public static ImageIcon loadIcon(String path) {
        java.net.URL url = getResource(path);

        if (url == null) {
            System.err.println("Missing resource: " + path);
            return null;
        }

        return new ImageIcon(url);
    }
}
