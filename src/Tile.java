import java.awt.*;

public class Tile extends Rectangle{
    private boolean hovered;
    private boolean selected;

    // private Object object;  // does the tile contains lilypad/reeve

    public Tile(int x, int y, int width, int height) {
        super(x, y, width, height);
        selected = false;
        hovered = false;
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

}
