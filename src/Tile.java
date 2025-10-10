import java.awt.*;

public class Tile extends Rectangle{
    public static int total = 0;


    private int id;
    private boolean hovered;
    private boolean selected;
    private boolean occupied;   // already has a frog or a reeve
    private Frog frog;
    private boolean reeve;
    private boolean lily;
    private boolean rotten;

    public Tile(int x, int y, int width, int height) {
        super(x, y, width, height);
        selected = false;
        hovered = false;
        occupied = false;

        lily = false;
        reeve = false;
        rotten = false;
        frog = null;
        id = total++;
    }

    public void clean(){
        lily = false;
        reeve = false;
        rotten = false;
        frog = null;
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

    public boolean isReeve() {
        return reeve;
    }

    public void setReeve(boolean reeve) {
        this.reeve = reeve;
        this.occupied = true;
    }

    public boolean isRotten() {
        return rotten;
    }
    public void setRotten(boolean rotten) {
        this.rotten = rotten;
        this.lily = false;

    }


}
