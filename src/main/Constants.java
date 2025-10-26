package main;

import frog.Frog;
import frog.FrogSpecies;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

public class Constants {

    public static final String RESOURCES_PATH = "src" +File.separator + "resources";
    /** Total experience needed to reach the next level */
    public static final int EXPERIENCE_THRESHOLD = 100;

    public static final List<Frog> FROGS = new ArrayList<>();

    public static void setUpFrogList(){
        for (int i = 0 ; i < 13 ; i++){
            Frog frog = new Frog(RESOURCES_PATH + File.separator + "frog_" + i + ".png", "The Frog "+ i, FrogSpecies.getSpecies(i), "01/01/2025");
            FROGS.add(frog);
        }
    }


    // FROGBAR CONSTANTS
    public static final int TASKBAR_FROG_SIZE = 100;
    public static final int TASKBAR_OFFSET = 48;





    // POND CONSTANTS
    public static final String POND_IMAGE = RESOURCES_PATH + File.separator + "pond" + File.separator + "pond.png";
    public static final int CELL_SIZE = 25;
    public static final int[] WATER_TILES = {175, 176, 177, 191, 192, 207, 208, 209, 210, 211, 221, 222, 223, 224, 225, 226, 241, 242, 243, 244, 245, 246, 247, 248, 249, 250, 251, 252, 253, 254, 255, 256, 257, 258, 259, 274, 275, 276, 277, 278, 279, 280, 281, 282, 283, 284, 285, 288, 289, 290, 291, 307, 308, 309, 310, 311, 312, 313, 314, 315, 316, 317, 318, 321, 322, 323, 324, 342, 343, 344, 345, 346, 347, 348, 349, 350, 351, 354, 355, 356, 357, 376, 377, 378, 379, 380, 381, 382, 383, 384, 385, 386, 387, 388, 389, 411, 412, 413, 414, 415, 416, 417 };

    /** Maximum delay for random timer activating spawn of objects */
    public static final int MIN_REEDLILY_TIMER = 30000; //30 sec
    public static final int MAX_REEDLILY_TIMER = 90000; // 90sec

    public static final int MIN_ROT_TIMER = 120000; // 2min
    public static final int MAX_ROT_TIMER = 300000; // 5min

    public static final int MIN_FROG_TIMER = 180000; // 3min
    public static final int MAX_FROG_TIMER = 300000; // 5min

    public static final int MIN_CROCO_TIMER = 300000; // 10min
    public static final int MAX_CROCO_TIMER = 18000000; //30min



    public static final int MAX_OBJECTS = 30; // Maximum number of objects on pond simultaneously (reeve, lily pads & rotten lily pads)


    // TOOLBAR SHARED main.resources
    public static ImageIcon TOOLS_IMG = new ImageIcon(RESOURCES_PATH + File.separator + "tool"+ File.separator + "tools.png");

    public static ImageIcon SCISSORS_IMG = new ImageIcon(RESOURCES_PATH + File.separator + "tool"+ File.separator + "scissors.png");
    public static ImageIcon NET_IMG = new ImageIcon(RESOURCES_PATH + File.separator + "tool" + File.separator + "net.png");
    public static ImageIcon GRAB_BEFORE_IMG = new ImageIcon(RESOURCES_PATH + File.separator + "tool" + File.separator + "grab_before.png");
    public static ImageIcon GRAB_WHILE_IMG = new ImageIcon(RESOURCES_PATH + File.separator + "tool" + File.separator + "grab_while.png");
    public static ImageIcon BIN_IMG = new ImageIcon(RESOURCES_PATH + File.separator +"tool"+ File.separator +"bin.png");
    public static ImageIcon BELL_IMG = new ImageIcon(RESOURCES_PATH + File.separator +"tool"+ File.separator +"bell.png");

    // BELL SPEED CONSTANTS
    public static final int MAX_HISTORY_MS = 500; // look at last xx ms of movement
    public static final int INACTIVITY_MS = 200;   // how long to wait before "stopped"
    public static final double SPEED_THRESHOLD = 0.5; // pixel/ms

    // File paths
    public static final String DATA_FILE = RESOURCES_PATH + File.separator + "frogedex_data.json";
}
