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

            // Setup animation idle
            for(int i = 0 ; i < frogedex.getFrogs().size() ; i++){
                Frog frog = frogedex.getFrogs().get(i);
                frog.loadAnimation("idle");
            }

            setVisible(true);
            setAlwaysOnTop(true);    // To always appear regardless of user activity
        }

        private void setUpWindow() {
            setBackground(new Color(255, 255, 255, 0)); // Transparent
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            setSize(screenSize.width, screenSize.height - Constants.TASKBAR_OFFSET);
            setLocation(0, 0);
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
