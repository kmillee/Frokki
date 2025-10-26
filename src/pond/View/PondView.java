package pond.View;

import pond.Controller.Toolbox;
import main.Constants;
import main.Utils;
import pond.Controller.PondController;
import pond.PondModel;
import pond.Tile;

import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

/**
 * PondView handles rendering of the pond, tiles, and objects.
 * <p>
 * It works together with PondModel (game state) and PondController (user interactions).
 * Supports mouse coordinate translation, tile rendering, and toolbox button management.
 */
public class PondView extends JPanel {
    private final PondModel model; // Pond game state
    private PondController controller; // User input handler

    // Images for rendering
    private final Image background;
    private final Image lilyImg;
    private final Image rottenImg;
    private final Image crocoImg;
    private final ArrayList<Image> reedImages;
    private final PondImages images;    // Container for pond sprites

    // Toolbox button
    private JButton toolboxBtn;



    /**
     * Constructs a PondView with a specified background, pond, and model.
     *
     * @param model      The PondModel containing game state
     * @param background Background image for the pond
     */
    public PondView(PondModel model, Image background) {
        this.model = model;
        this.background = background;

        // Load pond sprites
         reedImages = new ArrayList<>() {{
                add(new ImageIcon(Constants.RESOURCES_PATH + File.separator + "pond" + File.separator + "reed_1.png").getImage());
                add(new ImageIcon(Constants.RESOURCES_PATH + File.separator + "pond" + File.separator + "reed_2.png").getImage());
                add(new ImageIcon(Constants.RESOURCES_PATH + File.separator + "pond" + File.separator + "reed_3.png").getImage());

            }};
        lilyImg = new ImageIcon(Constants.RESOURCES_PATH + File.separator+ "pond" + File.separator + "lilypad.png").getImage();
        rottenImg = new ImageIcon(Constants.RESOURCES_PATH + File.separator+ "pond" + File.separator + "rotten.png").getImage();
        crocoImg = new ImageIcon(Constants.RESOURCES_PATH + File.separator+ "pond" + File.separator + "croco.png").getImage();
        images = new PondImages(lilyImg, rottenImg, crocoImg, reedImages);

        setFocusable(true);
        setPreferredSize(new Dimension(background.getWidth(null), background.getHeight(null)));
    }

    public void setController(PondController controller) {
        this.controller = controller;
    }

    /**
     * Adds the toolbox button on top of the pond.
     * Uses absolute positioning and updates dynamically on panel resize.
     *
     * @param controller The PondController handling toolbox state
     */
    public void addToolboxButton(PondController controller) {
        this.setLayout(null); // absolute positioning

        int btnSize = 50;
        int margin = 20;

        toolboxBtn = new JButton();
        toolboxBtn.setToolTipText("Open toolbox");
        toolboxBtn.setBackground(Color.PINK);

        // Set initial icon according to the current tool (default: NET)
        updateToolboxButtonIcon(controller.getToolbox().getCurrentTool());

        toolboxBtn.addActionListener(e -> controller.getToolbox().show());
        this.add(toolboxBtn);

        // Adjust button position when the panel resizes
        this.addComponentListener(new java.awt.event.ComponentAdapter() {
            @Override
            public void componentResized(java.awt.event.ComponentEvent e) {
                toolboxBtn.setBounds(getWidth() - btnSize - margin, getHeight() - btnSize - margin, btnSize, btnSize);
                repaint();
            }
        });
    }

    /**Updates button icon according to the current tool.
     * @param currentTool current selected tool from Toolbox
     */
    public void updateToolboxButtonIcon(Toolbox.Tool currentTool) {
        ImageIcon icon = controller.getToolbox().getToolIcon(currentTool);
        toolboxBtn.setIcon(Utils.resizeKeepingRatio(icon, 45, 45));
    }


    // ---- PAINTING ----

    @Override
    protected void paintComponent(Graphics pen) {
        super.paintComponent(pen);
        drawBackground(pen);
        updateTiles(pen);
    }

    /**
     * Draws the pond background and grid overlay.
     * Updates controller offsets for tile rendering.
     */
    private void drawBackground(Graphics pen) {

        // Center pond image on the panel
        int dx = (getWidth() - background.getWidth(null)) / 2;
        int dy = (getHeight() - background.getHeight(null)) / 2;

        pen.drawImage(background, dx, dy, background.getWidth(null), background.getHeight(null), null);

        // Update controller offsets to draw tiles accurately
        if (controller != null) {
            controller.setDx(dx);
            controller.setDy(dy);
        }

        // Grid overlay
        int rows = model.getRows();
        int cols = model.getCols();
        int cellSize = model.getCellSize();

        pen.setColor(new Color(120,120,120,120));

        for (int row = 0; row <= rows; row++) {
            pen.drawLine(dx,  row * cellSize + dy, cols * cellSize + dx, row * cellSize + dy);
        }

        for (int col = 0; col <= cols; col++) {
            pen.drawLine(col * cellSize + dx, dy, col * cellSize + dx, rows * cellSize + dy);
        }

    }


    /**
     * Draws all tiles in the pond using their internal draw logic.
     *
     * @param pen Graphics context
     */
    private void updateTiles(Graphics pen){
        Tile grabbedTile = controller.getGrabbedTile();
        Image grabbedImg = controller.getGrabbedImg();

        for (Tile tile : model.getTiles()){
            tile.draw(pen, controller.getDx() , controller.getDy(), images, grabbedTile, grabbedImg, model.getWaterTiles());
        }

    }


    // ---- HELPERS ----
    /**
     * Converts mouse coordinates from panel space to model coordinates.
     *
     * @param mousePoint Point in panel coordinates
     * @return Corresponding point in pond model coordinates
     */
    public Point toModelCoords(Point mousePoint){
        return new Point(mousePoint.x - controller.getDx(), mousePoint.y - controller.getDy());
    }

    // ---- GETTERS ----
    public Image getRottenImg() {return rottenImg;}
    public Image getLilyImg() {return lilyImg;}


}
