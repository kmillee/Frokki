package main;

import frog.Frog;
import frog.FrogSpecies;

import javax.swing.*;
import java.util.ArrayList;
import java.util.List;

public class Constants {

    /** Total experience needed to reach the next level */
    public static final int EXPERIENCE_THRESHOLD = 100;

    public static final List<Frog> FROGS = new ArrayList<>();

    public static void setUpFrogList(){
        for (int i = 0 ; i < 13 ; i++){
            Frog frog = new Frog("media/frog_" + i + ".png", "The frog.Frog "+ i, FrogSpecies.getSpecies(i), "01/01/2025");
            FROGS.add(frog);
        }
    }


    // FROGBAR CONSTANTS
    public static final int TASKBAR_FROG_SIZE = 100;
    public static final int TASKBAR_OFFSET = 48;

    // POND CONSTANTS
    public static final String POND_IMAGE =  "media/pond/pond.png";
    public static final int CELL_SIZE = 25;
    public static final int[] WATER_TILES = {175, 176, 177, 191, 192, 207, 208, 209, 210, 211, 221, 222, 223, 224, 225, 226, 241, 242, 243, 244, 245, 246, 247, 248, 249, 250, 251, 252, 253, 254, 255, 256, 257, 258, 259, 274, 275, 276, 277, 278, 279, 280, 281, 282, 283, 284, 285, 288, 289, 290, 291, 307, 308, 309, 310, 311, 312, 313, 314, 315, 316, 317, 318, 321, 322, 323, 324, 342, 343, 344, 345, 346, 347, 348, 349, 350, 351, 354, 355, 356, 357, 376, 377, 378, 379, 380, 381, 382, 383, 384, 385, 386, 387, 388, 389, 411, 412, 413, 414, 415, 416, 417 };

    /** Maximum delay for random timer activating spawn of objects */
    public static final int MAX_DELAY = 10000; // Maximum delay for rotten spawn

    public static final int MAX_OBJECTS = 30; // Maximum number of objects on pond simultaneously (reeve, lily pads & rotten lily pads)

    // TOOLBAR SHARED MEDIA
    public static ImageIcon SCISSORS_IMG = new ImageIcon("media/tool/scissors.png");
    public static ImageIcon NET_IMG = new ImageIcon("media/tool/net.png");
    public static ImageIcon GRAB_BEFORE_IMG = new ImageIcon("media/tool/grab_before.png");
    public static ImageIcon GRAB_WHILE_IMG = new ImageIcon("media/tool/grab_while.png");
    public static ImageIcon BIN_IMG = new ImageIcon("media/tool/bin.png");
    public static ImageIcon BELL_IMG = new ImageIcon("media/tool/bell.png");

    // BELL SPEED CONSTANTS
    public static final int MAX_HISTORY_MS = 500; // look at last xx ms of movement
    public static final int INACTIVITY_MS = 200;   // how long to wait before "stopped"
    public static final double SPEED_THRESHOLD = 0.5; // pixel/ms

    // File paths
    public static final String SAVE_FILE = "frogedex_data.json";
}
