import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.*;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import javax.imageio.*;

public class FrogBar extends JFrame {
        private JWindow window;

        private List<Frog> frogList = new ArrayList<>();
        int jump = -1;

        public FrogBar() throws IOException {
            setUpWindow();
            setUpFrogList();
            setUpFrogs();
            window.setVisible(true);
            window.setAlwaysOnTop(true);    // To always appear regardless of user activity
        }

        private void setUpWindow() {
            window = new JWindow();
            window.setBackground(new Color(255, 255, 255, 0));
            // set the location

            // set the size of the window
            Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
            window.setSize(screenSize.width, 100);
            window.setLocation(0, screenSize.height - window.getHeight() - 45);

        }

        private void setUpFrogList() throws IOException {
            frogList.add(new Frog("media/frog_og.png"));
            frogList.add(new Frog("media/frog_sand.png"));
            frogList.add(new Frog("media/frog_red.png"));
        }


        private void setUpFrogs() throws IOException {


                // Code taken: https://www.geeksforgeeks.org/java/jswing-create-translucent-shaped-windows-java/
                // create a panel
                JPanel p = new JPanel() {

                    // paint the panel
                    public void paintComponent(Graphics g)
                    {

                        // this was to check if mouselistener was working by default, needs to be rewritten
                        System.out.println("jump value = " + jump);
                        if (jump>=0){
                            jump+=10;
                            System.out.println("jump:"+jump);
                        }
                        if (jump>50){
                            jump=-1;
                            System.out.println("jump too high:"+jump);
                        }
                        else{
                            System.out.println("no jump");
                        }

                        // Draw frog from froglist on the toolbar
                        for (Frog frog : frogList) {
                            int frog_nb = frogList.indexOf(frog) + 1 ;
                            BufferedImage i = null;
                            try {
                                i = ImageIO.read(new File(frog.getImagePath()));
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }

                            // extract the pixel of the image
                            for (int ii = 1; ii < i.getHeight(); ii++)
                                for (int j = 1; j < i.getWidth(); j++) {
                                    // get the color of pixel
                                    Color ty = new Color(i.getRGB(j, ii));

                                    // if the color is more than 78 % white ignore it keep it transparent
                                    if (ty.getRed() < 10 && ty.getGreen() < 10 && ty.getBlue() < 10)
                                        g.setColor(new Color(0, 0, 0, 0));
                                        // else set  the color
                                    else
                                        g.setColor(new Color(i.getRGB(j, ii)));

                                    // draw a pixel using a line.
                                    int dy = window.getHeight() - i.getHeight();
                                    g.drawLine(j + 200 * frog_nb, ii +dy - jump, j + 200 * frog_nb, ii +dy - jump);
                                }

                        }

                    }

                };

                p.addMouseListener(new MouseAdapter() {
                    public void mouseClicked(MouseEvent e) {
                        System.out.println("Jump!");
                        jump = 0;
                        p.repaint();
                    }
                });
                window.add(p);

            }

        }
