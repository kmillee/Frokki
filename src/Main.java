import com.formdev.flatlaf.FlatLightLaf;

import javax.swing.*;
import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        FlatLightLaf.setup();

        Pond mainApp = new Pond("media/pond.png");

        Frogedex frogedex = new Frogedex();
        FrogBar frogbar = new FrogBar(frogedex);
        frogedex.show();
    }
}