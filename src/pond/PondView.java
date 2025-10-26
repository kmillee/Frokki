package pond;

import UI.Toolbox;
import main.Constants;
import main.Utils;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

// Handles rendering and mouse coordinates mechanics
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
    private final Point gridOrigin = new Point(0,0);
    private int dx, dy;
    private PondImages images;

    private JButton toolboxBtn;




    public PondView(Pond pond, PondModel model, Image background) {
        this.pond = pond;
        this.model = model;
        this.background = background;

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
        images = new PondImages(lilyImg, rottenImg, crocoImg, reedImages);
        setFocusable(true);
//        setDoubleBuffered(true);
        setPreferredSize(new Dimension(background.getWidth(null), background.getHeight(null)));
    }

    public void setController(PondController controller) {
        this.controller = controller;
    }

    public void addToolboxButton(PondController controller) {
        setLayout(null); // absolute positioning

        int btnSize = 50;
        int margin = 15;

        toolboxBtn = new JButton();
        toolboxBtn.setToolTipText("Open toolbox");
        toolboxBtn.setBackground(Color.PINK);
//        toolboxBtn.setIcon(Utils.resizeKeepingRatio(icon, 45,45));

        // Default tool: NET
        updateToolboxButtonIcon(controller.getToolbox().getCurrentTool());

        toolboxBtn.addActionListener(e -> controller.getToolbox().show());

        add(toolboxBtn);

        // Position after resize
        addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                toolboxBtn.setBounds(
                        getWidth() - btnSize - margin,
                        getHeight() - btnSize - margin,
                        btnSize,
                        btnSize
                );
                repaint();
            }
        });
    }
    public void updateToolboxButtonIcon(Toolbox.Tool currentTool) {
        ImageIcon icon = controller.getToolbox().getToolIcon(currentTool);
        toolboxBtn.setIcon(Utils.resizeKeepingRatio(icon, 45, 45));
    }


//    public void addToolboxButton(PondController controller) {
//
//        setLayout(null); // Absolute positioning on top of the pond
//
//        // Load icon
//        ImageIcon icon = new ImageIcon(Constants.NET_IMG.getImage());
//        int btnSize = 50;
//
//        JButton toolboxBtn = new JButton(icon);
//        toolboxBtn.setToolTipText("Open toolbox");
//        toolboxBtn.setBackground(Color.PINK);
//        toolboxBtn.setIcon(Utils.resizeKeepingRatio(icon, 45,45));
//
//        // bottom-right corner
//        int margin = 15;
//        toolboxBtn.setBounds(
//                getWidth() - btnSize - margin,
//                getHeight() - btnSize - margin,
//                btnSize,
//                btnSize
//        );
//
//        toolboxBtn.addActionListener(e -> controller.getToolbox().show());
//
//        add(toolboxBtn);
//
//        // Wait until the panel is sized, then position the button
//        addComponentListener(new java.awt.event.ComponentAdapter() {
//            @Override
//            public void componentResized(java.awt.event.ComponentEvent e) {
//                int margin = 20;
//                toolboxBtn.setBounds(
//                        getWidth() - btnSize - margin,
//                        getHeight() - btnSize - margin,
//                        btnSize,
//                        btnSize
//                );
//                repaint();
//            }
//        });
//
//    }



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

        // update controller offsets so tiles draw correctly
        if (controller != null) {
            controller.setDx(dx);
            controller.setDy(dy);
        }

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

    }

    private void updateTiles(Graphics pen){
        Tile grabbedTile = controller.getGrabbedTile();
        Image grabbedImg = controller.getGrabbedImg();

        for (Tile tile : model.getTiles()){
            tile.draw(pen, controller.getDx() , controller.getDy(), images, grabbedTile, grabbedImg, model.getWaterTiles());
        }

    }


    //Helper
    public Point toModelCoords(Point mousePoint){
        return new Point(mousePoint.x - controller.getDx(), mousePoint.y - controller.getDy());
    }

    public void repaintTile(Tile tile){
        repaint(tile.getBounds());
    }

}
