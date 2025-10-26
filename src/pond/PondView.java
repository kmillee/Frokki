package pond;

import main.Constants;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

// Access model read-only
public class PondView extends JPanel {
    private final Pond pond;
    private final PondModel model; // model later
    private PondController controller;

    // Images
    private final Image background;
    private final Image lilyImg;

    public Image getRottenImg() {
        return rottenImg;
    }

    public Image getLilyImg() {
        return lilyImg;
    }

    private final Image rottenImg;
    private final Image crocoImg;
    private final ArrayList<Image> reedImages;

    // Grid parameters
    private Point gridOrigin = new Point(0,0);
    private int dx, dy;


    public PondView(Pond pond, PondModel model) {
        this.pond = pond;
        this.model = model;
        this.background = new ImageIcon(Constants.POND_IMAGE).getImage();

        // Load game sprites
         reedImages = new ArrayList<>() {
            {
                add(new ImageIcon(Constants.RESOURCES_PATH + File.separator + "pond" + File.separator + "reed_1.png").getImage());
                add(new ImageIcon(Constants.RESOURCES_PATH + File.separator + "pond" + File.separator + "reed_2.png").getImage());
                add(new ImageIcon(Constants.RESOURCES_PATH + File.separator + "pond" + File.separator + "reed_3.png").getImage());

            }
        };
        lilyImg = new ImageIcon(Constants.RESOURCES_PATH + File.separator+ "pond" + File.separator + "lilypad.png").getImage();
        rottenImg = new ImageIcon(Constants.RESOURCES_PATH + File.separator+ "pond" + File.separator + "rotten.png").getImage();
        crocoImg = new ImageIcon(Constants.RESOURCES_PATH + File.separator+ "pond" + File.separator + "croco.png").getImage();

        setFocusable(true);
//        setDoubleBuffered(true);
        setPreferredSize(new Dimension(background.getWidth(null), background.getHeight(null)));
    }

    public void setController(PondController controller) {
        this.controller = controller;
    }


    // ---- PAINTING ----
    // paintComponent
    @Override
    protected void paintComponent(Graphics pen) {
        super.paintComponent(pen);
        drawBackground(pen);
        updateTiles(pen);
    }

    private void drawBackground(Graphics pen) {
        // center the image in the panel
        int dx = (getWidth() - background.getWidth(null)) / 2;
        int dy = (getHeight() - background.getHeight(null)) / 2;

        pen.drawImage(background, dx, dy, background.getWidth(null), background.getHeight(null), null);


        pen.setColor(new Color(120,120,120,120));

        int rows = model.getRows();
        int cols = model.getCols();
        int cellSize = model.getCellSize();

        // Drawing the rows
        for (int row = 0; row <= rows; row++) {
            pen.drawLine(dx,  row * cellSize + dy, cols * cellSize + dx, row * cellSize + dy);
        }

        // Drawing the lines
        for (int col = 0; col <= cols; col++) {
            pen.drawLine(col * cellSize + dx, dy, col * cellSize + dx, rows * cellSize + dy);
        }
        // update controller offsets so tiles draw correctly
        if (controller != null) {
            controller.setDx(dx);
            controller.setDy(dy);
        }
    }

    private void updateTiles(Graphics pen){
        PondImages images = new PondImages(lilyImg, rottenImg, crocoImg, reedImages);
        Tile grabbedTile = controller.getGrabbedTile();
        Image grabbedImg = controller.getGrabbedImg();

        for (Tile tile : model.getTiles()){
            tile.draw(pen, dx , dy, images, grabbedTile, grabbedImg, model.getWaterTiles());
        }

            // if tile selected, display in full pink
//            if (tile.isSelected()){
//                g.setColor(new Color(255, 143, 248,180));
//                g.fillRect(tile.x + dx, tile.y + dy, tile.width, tile.height);
//
//                Tile left = getLeftTile(tile);
//                Tile right = getRightTile(tile);
//                Tile up = getUpperTile(tile);
//                Tile down = getLowerTile(tile);
//
//                if(left!=null){
//                    g.setColor(new Color(100,255,100,100));
//                    g.fillRect(left.x + dx, left.y + dy, tile.width, tile.height);
//                }
//
//                if(right!=null){
//                    g.setColor(new Color(147, 114, 3, 228));
//                    g.fillRect(right.x + dx, right.y + dy, tile.width, tile.height);
//                }
//
//                if(up!=null){
//                    g.setColor(new Color(0, 255, 205,100));
//                    g.fillRect(up.x + dx, up.y + dy, tile.width, tile.height);
//                }
//
//                if(down!=null){
//
//                    g.setColor(Color.yellow);
//                    g.fillRect(down.x + dx, down.y + dy, tile.width, tile.height);
//                }
//
//            }
        }


    //Helper
    public Point toModelCoords(Point mousePoint){
        return new Point(mousePoint.x - gridOrigin.x, mousePoint.y - gridOrigin.y);
    }

    public void repaintTile(Tile tile){
        repaint(tile.getBounds());
    }

}
