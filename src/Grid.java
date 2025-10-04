import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;


// Class handling input of the pond
public class Grid extends JPanel implements MouseListener, MouseMotionListener, KeyListener {
    //TODO: highlight tile when cursor goes on it

    public int cols;
    public int rows;
    public int cellSize;
    public Image image;

    private ArrayList<Tile> grid;

    // Relative position help
    private Point gridOrigin;
    private int dx;
    private int dy;

    // Other
    private boolean multSelect = false;


    public Grid(int cellSize, String imagePath) {

        this.image = new ImageIcon(imagePath).getImage();
        this.cellSize = cellSize ;
        this.cols = image.getWidth(null) / cellSize;
        this.rows = image.getHeight(null) / cellSize;

        System.out.println("cols: " + cols + " rows: " + rows);
        System.out.println("image size: " + image.getWidth(null) + " " + image.getHeight(null));

        this.grid = new ArrayList<>();
        for(int i = 0; i < this.rows; i++){
            for(int j = 0; j < this.cols; j++){
                grid.add(new Tile(j*cellSize,i*cellSize,cellSize,cellSize));
            }
        }
        System.out.println("grid: " + grid.toString());

    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        g.setColor(Color.pink);
        drawGrid(g, rows, cols, cellSize,image);
        updateGrid(g);
    }

    private void drawGrid(Graphics g, int rows, int cols, int cellSize, Image image) {
        Dimension frameSize = getParent().getSize();
        gridOrigin = new Point((frameSize.width - image.getWidth(null )) / 2, (frameSize.height - image.getHeight(null)) / 2);
        dx = gridOrigin.x;
        dy = gridOrigin.y;

        g.drawImage(image, gridOrigin.x, gridOrigin.y, image.getWidth(null), image.getHeight(null), null);

        // Drawing the rows
        for (int row = 0; row <= rows; row++) {
            g.drawLine(dx,  row * cellSize + dy, cols * cellSize + dx, row * cellSize + dy);
        }

        // Drawing the lines
        for (int col = 0; col <= cols; col++) {
            g.drawLine(col * cellSize + dx, dy, col * cellSize + dx, rows * cellSize + dy);
        }
    }

    // Highlights selected and hovered tiles
    private void updateGrid(Graphics g) {

        for (Tile tile : grid) {
            if (tile.isHovered()) {
                g.setColor(new Color(255, 255, 255,100));
                g.fillRect(tile.x + dx, tile.y + dy, tile.width, tile.height);
            }

            if (tile.isSelected()){
                g.setColor(new Color(255, 143, 248,180));
                g.fillRect(tile.x + dx, tile.y + dy, tile.width, tile.height);
            }
        }
    }


    // Switches tile to selected when clicked
    @Override
    public void mouseClicked(MouseEvent e) {
//        System.out.println("mouseClicked");
        dx = gridOrigin.x;
        dy = gridOrigin.y;
        Point gridCursor = new Point(e.getX() - dx, e.getY() - dy);

        for (Tile tile : grid) {
            if (tile.contains(gridCursor)) {
                tile.setSelected(true);
                repaint();
            }
            else if (!multSelect) {
                tile.setSelected(false);
            }

        }

    }

    @Override
    public void mousePressed(MouseEvent e) {

    }

    @Override
    public void mouseReleased(MouseEvent e) {

    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    @Override
    public void mouseDragged(MouseEvent e) {

    }

    // Switches tile to hovered or not
    @Override
    public void mouseMoved(MouseEvent e) {
        int dx = gridOrigin.x;
        int dy = gridOrigin.y;
//        System.out.println("mouse moved: " + dx + " " + dy);

        Point gridCursor = new Point(e.getX() - dx, e.getY() - dy);
//        System.out.println("mouseMoved: " + e.getPoint());
        for (Tile tile : grid) {
            if (tile.contains(gridCursor)) {
                System.out.println(tile);
                tile.setHovered(true);
                repaint();
            }
            else{
                tile.setHovered(false);
            }

        }

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    // ctrl pressed enables multiple selection mode
    @Override
    public void keyPressed(KeyEvent e) {
//        System.out.println("pressed: " +  e.getKeyChar());
        if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
            multSelect = true;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
            multSelect = false;
        }

    }
}