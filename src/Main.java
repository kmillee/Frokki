import com.formdev.flatlaf.FlatLightLaf;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        FlatLightLaf.setup();
        Constants.setUpFrogList();

        Pond pond = new Pond();
        Frogedex frogedex = new Frogedex();
        FrogBar frogbar = new FrogBar(frogedex);

        pond.setFrogedex(frogedex);

        frogedex.show();

        // -- the following code was taken from https://stackoverflow.com/a/5824066
        Runtime.getRuntime().addShutdownHook(new Thread(new Runnable() {
            public void run() {
                frogedex.saveData();
            }
        }, "Shutdown-thread"));
    }
}