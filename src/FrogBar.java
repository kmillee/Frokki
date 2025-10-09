import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.List;
import javax.imageio.*;

public class FrogBar extends JWindow {
        private final Frogedex frogedex;
        private JLayeredPane layeredPane;


        public FrogBar(Frogedex frogedex) {
            this.frogedex = frogedex;

            for(Frog frog : frogedex.getFrogs()) {
                frog.addChangeListeners(e -> updateFrogBar());
            }

            setUpWindow();
            setupLayeredPane();
            updateFrogBar();

            setVisible(true);
            setAlwaysOnTop(true);    // To always appear regardless of user activity
        }

        private void setUpWindow() {
            setBackground(new Color(255, 255, 255, 0));
            // set the location

            // set the size of the window
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            setSize(screenSize.width, 2 * Constants.TASKBAR_FROG_SIZE);
            setLocation(0, screenSize.height - getHeight() - 48);
        }

        private void setupLayeredPane(){
            layeredPane = new JLayeredPane();
            layeredPane.setOpaque(false);
            layeredPane.setLayout(null); // allows absolute positioning
            add(layeredPane);
        }

        public void updateFrogBar(){
            layeredPane.removeAll(); // Clear the bar

            // Add active frogs to the frog bar
            List<Frog> frogs = frogedex.getFrogs();
            int posX = 10; // initial x position for frog's placement
            int posY = getHeight() - Constants.TASKBAR_FROG_SIZE;
            for(Frog frog : frogs){
                if(frog.isActive()){
                    FrogComponent frogComponent = new FrogComponent(frog);
                    frogComponent.setBounds(posX,posY, Constants.TASKBAR_FROG_SIZE,Constants.TASKBAR_FROG_SIZE);
                    layeredPane.add(frogComponent);
                    posX += Constants.TASKBAR_FROG_SIZE + 20;
                }
            }
            layeredPane.revalidate();
            layeredPane.repaint();
        }
}
