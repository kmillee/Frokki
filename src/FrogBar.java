import javax.swing.*;
import java.awt.*;
import java.awt.image.*;
import java.io.*;
import java.util.HashMap;
import java.util.Map;
import javax.imageio.*;

/**
 * This class represents the FrogBar, a window that displays active frogs.
 * A frog is considered active if it is on top of the taskbar.
 */
public class FrogBar extends JWindow {
        private final Frogedex frogedex;
        private JLayeredPane layeredPane;

        /**
         * Constructor for a FrogBar.
         * @param frogedex The Frogedex containing the frogs to be displayed in the FrogBar.
         */
        public FrogBar(Frogedex frogedex) {
            this.frogedex = frogedex;

            for (Frog frog : frogedex.getFrogs()) {
                frog.addChangeListeners(e -> updateFrogBar());

            }

            setUpWindow();
            setupLayeredPane();
            updateFrogBar();


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

        /**
         * Updates the FrogBar to reflect the current active frogs and their positions.
         * The positions of the frogs already in the bar are preserved.
         */
        public void updateFrogBar(){
            // Get current positions of frogs in the bar for consistency
            Map<Frog, FrogComponent> existingComponents = new  HashMap<>();

            for(Component component : layeredPane.getComponents()){
                if(component instanceof FrogComponent frogComponent){
                    existingComponents.put(frogComponent.getFrog(), frogComponent);
                }
            }
            
            // Add active frogs to the bar
            int posX = 0;
            for(Frog frog: frogedex.getFrogs()){
                FrogComponent frogComponent = existingComponents.get(frog);
                if(frog.isActive()) {
                    if(frogComponent == null){
                        frogComponent = new FrogComponent(frog);
                        layeredPane.add(frogComponent);
                        Point position = new Point(posX, 0);
                        frogComponent.setBottomLeftAnchor(position.x, position.y);

                        posX += Constants.TASKBAR_FROG_SIZE + 20;
                        if(posX+Constants.TASKBAR_FROG_SIZE > getWidth()){
                            posX = 0;
                        }
                    }

                    // Get rid of component if the frog is not active anymore

                } else {
                    if(frogComponent != null) {
                        layeredPane.remove(frogComponent);
                    }
                }
            }

            // Load animation if needed
            for(Component component : layeredPane.getComponents()){
                if(component instanceof FrogComponent frogComponent){
                    Frog frog = frogComponent.getFrog();
                    if(frog.getAnimation() == null){
                        frog.loadAnimation("idle");
                        //frogComponent.attachAnimationListener();
                        frog.startAnimation();
                    }
                }
            }
            layeredPane.revalidate();
            layeredPane.repaint();
        }
}
