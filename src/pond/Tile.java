package pond;

import frog.Frog;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Tile extends Rectangle{
    public static int total = 0;
    private int id;
    private int randomReed; // to assign a random reed option
    private boolean hovered;
    private boolean selected;
    private boolean occupied;   // already has a frog or a reeve
    private Frog frog;
    private boolean reed;
    private boolean lily;
    private boolean rotten;
    private boolean croco;

    public Tile(int x, int y, int width, int height) {
        super(x, y, width, height);
        selected = false;
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

    public void clean(){
        lily = false;
        reed = false;
        rotten = false;
        frog = null;

        randomReed = (int) (Math.random() * 3);
    }

    public void draw(Graphics g, int dx, int dy, PondImages images,
                     Tile grabbedTile, Image grabbedImg, ArrayList<Tile> waterGrid) {


        // Base object rendering (in fixed draw order)
        if (isLily()) {
            g.drawImage(images.lilyImg, x + dx + 1, y + dy + 1, width, height, null);
        }

        if (getFrog() != null) {
            ImageIcon frogIcon = getFrog().getImage();
            Image frogImg = frogIcon.getImage();
            g.drawImage(frogImg, x + dx + 1, y + dy + 1, width, height, null);
        }

        if (isReed()) {
            g.drawImage(images.reedImages.get(randomReed), x + dx + 1, y + dy + 1, width, height, null);
        }

        if (isRotten()) {
            g.drawImage(images.rottenImg, x + dx + 1, y + dy + 1, width, height, null);
        }

        if (isCroco()) {
            g.drawImage(images.crocoImg, x + dx + 1, y + dy + 1, width, height, null);
        }

        // Hover overlay
        if (isHovered()) {
            if (grabbedTile != null) {
                // draw the grabbed image over hover
                g.drawImage(grabbedImg, x + dx + 1, y + dy + 1, width, height, null);

                // determine color (available vs unavailable)
                if ((waterGrid.contains(this) && !isOccupied() && !isLily() && !isRotten()) || (this == grabbedTile)) {
                    g.setColor(new Color(100, 255, 100, 100)); // green
                } else {
                    g.setColor(new Color(255, 100, 100, 100)); // red
                }
            } else {
                g.setColor(new Color(255, 255, 255, 100)); // white hover
            }
            g.fillRect(x + dx, y + dy, width, height);
        }

        // Selection overlay (optional)
        if (isSelected()) {
            g.setColor(new Color(255, 143, 248, 180)); // pink highlight
            g.fillRect(x + dx, y + dy, width, height);
        }
    }


    // Getters and Setters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
    public boolean isHovered() {
        return hovered;
    }

    public void setHovered(boolean hovered) {
        this.hovered = hovered;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
    }

    public boolean isOccupied() {
        return occupied;
    }

    public void setOccupied(boolean occupied) {
        this.occupied = occupied;
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


    public int getRandomReed() {
        return randomReed;
    }

    public void setRandomReed(int randomReed) {
        this.randomReed = randomReed;
    }
}
