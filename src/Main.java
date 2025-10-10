import Toolbox.Toolbox;
import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
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
    }
}