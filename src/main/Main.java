package main;

import com.formdev.flatlaf.FlatLightLaf;
import frog.frogbar.FrogBarController;
import frogedex.Frogedex;
import menu.Menu;
import pond.Pond;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        FlatLightLaf.setup();
        Constants.setUpFrogList();

        Menu menu = new Menu();

        // -- the following code was taken from https://stackoverflow.com/a/5824066
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                menu.getFrogedex().saveData();
            }
        }, "Shutdown-thread"));
    }
}