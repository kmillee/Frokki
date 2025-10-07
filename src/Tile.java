import java.awt.*;

public class Tile extends Rectangle{
    private boolean hovered;
    private boolean selected;
    private boolean occupied;

    private Frog frog;
    private boolean reeve;
    private boolean lily;


    // private Object object;  // does the tile contains lilypad/reeve

    public Tile(int x, int y, int width, int height) {
        super(x, y, width, height);
        selected = false;
        hovered = false;
        occupied = false;
        frog = null;
    }


    // Getters and Setters
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
        this.occupied = true;
    }

    public boolean isReeve() {
        return reeve;
    }

    public void setReeve(boolean reeve) {
        this.reeve = reeve;
        this.occupied = true;
    }
}
