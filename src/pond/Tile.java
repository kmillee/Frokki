package pond;

import frog.Frog;
import pond.View.PondImages;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Tile represents a single cell in the Pond game grid.
 * <p>
 * Each tile can contain various objects such as a frog, lily pad, reed, rotten lily pad, or crocodile.
 * It maintains flags for occupancy and hover state to manage rendering and interactions.
 */
public class Tile extends Rectangle{
    private static int total = 0;
    private final int id;

    private boolean hovered;
    private boolean occupied;   // True if a frog/reed occupies the tile

    private Frog frog;
    private int randomReed; // Index of the reed image to display

    private boolean reed;
    private boolean lily;
    private boolean rotten;
    private boolean croco;

    /**
     * Constructs a new Tile at the specified position and size.
     * @param x      X-coordinate of the top-left corner
     * @param y      Y-coordinate of the top-left corner
     * @param width  Width of the tile
     * @param height Height of the tile
     */
    public Tile(int x, int y, int width, int height) {
        super(x, y, width, height);
        hovered = false;
        occupied = false;

        lily = false;
        reed = false;
        rotten = false;
        croco = false;
        frog = null;

        id = total++;
        randomReed = (int) (Math.random() * 3);
    }

    /**
     * Draws this tile and its contents on the given Graphics context.
     * @param g           Graphics context for drawing
     * @param dx          x offset for rendering
     * @param dy          y offset for rendering
     * @param images      PondImages object containing all the preloaded tile images
     * @param grabbedTile Tile currently being dragged by the player (can be null)
     * @param grabbedImg  Image of the dragged object (can be null)
     * @param waterGrid   List of all water tiles (used to determine valid placement)
     */
    public void draw(Graphics g, int dx, int dy, PondImages images,
                     Tile grabbedTile, Image grabbedImg, ArrayList<Tile> waterGrid) {

        // Compute drawing coordinates with offset
        int draw_x = x + dx ;
        int draw_y = y + dy ;
        int draw_width = width - 1;     // 1px border to avoid overlap
        int draw_height = height - 1;

        // Draw base objects in fixed order

        // Lily pad
        if (isLily()) {
            g.drawImage(images.lilyImg, draw_x, draw_y, draw_width, draw_height, null);
        }

        // Frog
        if (getFrog() != null) {
            ImageIcon frogIcon = getFrog().getImage();
            Image frogImg = frogIcon.getImage();
            g.drawImage(frogImg, draw_x, draw_y, draw_width, draw_height, null);
        }

        // Reed
        if (isReed()) {
            g.drawImage(images.reedImages.get(randomReed), draw_x, draw_y, draw_width, draw_height, null);
        }

        // Rotten lily pad
        if (isRotten()) {
            g.drawImage(images.rottenImg, draw_x, draw_y, draw_width, draw_height, null);
        }

        // Crocodile
        if (isCroco()) {
            g.drawImage(images.crocoImg, draw_x, draw_y, draw_width, draw_height, null);
        }

        // Draw hover overlay if needed
        if (isHovered()) {
            if (grabbedTile != null) {
                // Draw the grabbed object image over this tile
                g.drawImage(grabbedImg, draw_x, draw_y, draw_width, draw_height, null);

                // Determine hover color: green = valid placement, red = invalid
                if ((waterGrid.contains(this) && !isOccupied() && !isLily() && !isRotten()) || (this == grabbedTile)) {
                    g.setColor(new Color(100, 255, 100, 100)); // translucent green
                } else {
                    g.setColor(new Color(255, 100, 100, 100)); //  translucent red
                }
            } else {
                g.setColor(new Color(255, 255, 255, 100)); // white hover
            }
            g.fillRect(draw_x, draw_y, draw_width, draw_height);
        }

    }


    /** Clears all object flags on this tile*/
    public void clean(){
        lily = false;
        reed = false;
        rotten = false;
        croco = false;
        occupied = false;
        frog = null;

        randomReed = (int) (Math.random() * 3);
    }


    // ---- GETTERS & SETTERS ----
    public int getId() {
        return id;
    }

    public boolean isHovered() {
        return hovered;
    }

    public void setHovered(boolean hovered) {
        this.hovered = hovered;
    }
    public boolean isOccupied() {
        return occupied;
    }

    public Frog getFrog() {
        return frog;
    }

    public void setFrog(Frog frog) {
        this.frog = frog;
        this.occupied = true;
    }

    public boolean isLily() {
        return lily;
    }

    public void setLily(boolean lily) {
        this.lily = lily;
    }

    public boolean isReed() {
        return reed;
    }

    public void setReed(boolean reed) {
        this.reed = reed;
        this.occupied = true;
    }

    public boolean isCroco() {
        return croco;
    }

    public void setCroco(boolean croco) {
        this.croco = croco;
    }


    public boolean isRotten() {
        return rotten;
    }
    public void setRotten(boolean rotten) {
        this.rotten = rotten;
        this.lily = false;

    }

    public boolean isMovable(){
        return (rotten || (lily && frog == null));
    }

}
