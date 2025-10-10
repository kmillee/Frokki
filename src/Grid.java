import Toolbox.Toolbox;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;


// Class handling input of the pond
public class Grid extends JPanel implements MouseListener, MouseMotionListener, KeyListener {

    // Basic variable
    public final int cols, rows, cellSize;
    private Pond pond;

    // Media
    public Image image;
    private Image reeveImg = new ImageIcon("media/reeve.jpg").getImage();
    private Image lilyImg = new ImageIcon("media/lilypad.png").getImage();
    private Image rottenImg = new ImageIcon("media/rotten.jpg").getImage();


    // Tile management
    private final ArrayList<Tile> grid, water_grid, lily_grid, reeve_grid, frog_grid, rotten_grid;

    // Relative position help
    private Point gridOrigin;
    private int dx, dy;

    // Tools
    private Toolbox toolbox;
    private Frogedex frogedex;

    // Other
    private Timer timer;
    private boolean multSelect, ctrlPressed = false;



    // ---- SETTING UP ----
    public Grid(Pond pond, int cellSize, String imagePath) {
        this.pond = pond;

        this.image = new ImageIcon(imagePath).getImage();
        this.cellSize = cellSize ;
        this.cols = image.getWidth(null) / cellSize;
        this.rows = image.getHeight(null) / cellSize;

        this.grid = new ArrayList<>();
        for(int i = 0; i < this.rows; i++){
            for(int j = 0; j < this.cols; j++){
                grid.add(new Tile(j*cellSize,i*cellSize,cellSize,cellSize));
            }
        }

        this.water_grid = new ArrayList<>();
        for (Tile tile : grid){
            if (Utils.contains(Constants.WATER_TILES, tile.getId())) {
                water_grid.add(tile);
            }
        }

        this.lily_grid = new ArrayList<>();
        this.reeve_grid = new ArrayList<>();
        this.frog_grid = new ArrayList<>();
        this.rotten_grid = new ArrayList<>();

        this.toolbox = new Toolbox();

        installUI();
        setUpTimer();

    }

    public void installUI(){
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addKeyListener(this);
        this.requestFocus();
    }

    private void setUpTimer(){
        int delay = (int) (Math.random() * Constants.MAX_DELAY);

//        System.out.println("setUpTimer:" +  delay);

        timer = new Timer(delay, e -> {
//            System.out.println("rotten");
            spawnRotten();
            setUpTimer();
        });

        timer.setRepeats(false);
        timer.start();

    }


    // ---- PAINT MECHANICS ----
    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        drawGrid(g, rows, cols, cellSize,image);
        updateGrid(g);
    }

    // Draw image and grid on top of it, in the middle of the frame
    private void drawGrid(Graphics g, int rows, int cols, int cellSize, Image image) {
        Dimension frameSize = getParent().getSize();
        gridOrigin = new Point((frameSize.width - image.getWidth(null )) / 2, (frameSize.height - image.getHeight(null)) / 2);
        dx = gridOrigin.x;
        dy = gridOrigin.y;

        g.drawImage(image, gridOrigin.x, gridOrigin.y, image.getWidth(null), image.getHeight(null), null);

        g.setColor(new Color(120,120,120,120));

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

            if (tile.isLily()){
                g.drawImage(lilyImg,tile.x + dx +1, tile.y + dy +1, tile.width, tile.height, null);
            }

            if (tile.getFrog() != null){
                ImageIcon frogIcon = tile.getFrog().getImage();
                Image frogImg = frogIcon.getImage();
                // change size
                //frogImg = frogImg.getScaledInstance(tile.width -2, tile.height -2, Image.SCALE_DEFAULT);
                g.drawImage(frogImg,tile.x + dx +1, tile.y + dy +1, tile.width, tile.height, null);

            }
            if (tile.isReeve()){
                g.drawImage(reeveImg,tile.x + dx +1, tile.y + dy +1, tile.width, tile.height, null);
            }

            if (tile.isRotten()){
                g.drawImage(rottenImg, tile.x + dx +1, tile.y + dy +1, tile.width, tile.height, null);
            }

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


    // ---- POND MODIFICATION ----
    // SPAWN & DELETE
    public void spawnFrog(){
        Frog frog = getRandomFrog();    // need to randomize frog by rarity
        Tile tile = getRandomLilyTile();
        if (tile != null){
            tile.setFrog(frog);
            frog_grid.add(tile);
            repaint();
        }

        else{
            System.out.println("no lily pad available");
        }
    }

    public void spawnReeve(){
        Tile tile = getRandomAvailableTile();
        if (tile != null){
            tile.setReeve(true);
            reeve_grid.add(tile);
            repaint();
        }

    }

    public void spawnLily(){
        Tile tile = getRandomAvailableTile();
        if (tile != null){
            tile.setLily(true);
            lily_grid.add(tile);
            repaint();
        }

    }

    public void spawnRotten(){
        Tile tile = getRandomLilyTile();
        if (tile != null){
            tile.setRotten(true);
            lily_grid.remove(tile);
            rotten_grid.add(tile);
            repaint();
        }

        else{
//            System.out.println("no lily pad available");
        }

    }

    public void useNet(Tile tile){
        if (tile.getFrog() != null){

            pond.getFrogedex().addFrog(tile.getFrog());
            frog_grid.remove(tile);
            tile.setFrog(null);

            repaint();
        }


    }

    public void useBell(Tile tile){
        // compute mouseSpeed + distance with croc?

    }

    public void useGrab(Tile tile){
        // put rotten lily pad in the bin
        // move lily pad around
    }

    public void useScissors(Tile tile){
        if (tile.isReeve()){
            tile.clean();
            reeve_grid.remove(tile);

            repaint();
        }

    }







    // ---- LISTENERS ----

    // Switches tile to selected when clicked
    @Override
    public void mouseClicked(MouseEvent e) {
        System.out.println("mouseClicked");
        System.out.println("Current Tool: " + toolbox.getCurrentTool());
        dx = gridOrigin.x;
        dy = gridOrigin.y;
        Point gridCursor = new Point(e.getX() - dx, e.getY() - dy);

        for (Tile tile : grid) {
            if (tile.contains(gridCursor)) {
                switch (toolbox.getCurrentTool()) {
                    case Toolbox.Tool.BELL:
                        useBell(tile);
                        break;
                    case Toolbox.Tool.SCISSORS:
                        useScissors(tile);
                        break;
                    case Toolbox.Tool.GRAB:
                        useGrab(tile);
                        break;
                    case Toolbox.Tool.NET:
                        useNet(tile);
                        break;
                    default:
                        tile.setSelected(true);
                }
            }
            else if (!multSelect) {
                tile.setSelected(false);
            }

        }
//        getSelectedTilesId();
        repaint();


    }

    @Override
    public void mousePressed(MouseEvent e) {
        multSelect = true;
//        System.out.println("mouse pressed");

    }

    @Override
    public void mouseReleased(MouseEvent e) {
        if (!ctrlPressed) {
            multSelect = false;
        }
    }

    @Override
    public void mouseEntered(MouseEvent e) {
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    // Multiple tiles are selected if dragging with ctrl pressed down or dragging right click
    @Override
    public void mouseDragged(MouseEvent e) {
        if ((ctrlPressed) ||(SwingUtilities.isRightMouseButton(e))) {
            Point gridCursor = new Point(e.getX() - dx, e.getY() - dy);
//        System.out.println("mouseMoved: " + e.getPoint());
            for (Tile tile : grid) {
                if (tile.contains(gridCursor)) {
                    tile.setSelected(true);
                    repaint();
                }
            }

        }
//        getSelectedTilesId();

    }

    // Switches tile to hovered or not
    @Override
    public void mouseMoved(MouseEvent e) {
//        System.out.println("mouse moved: " + dx + " " + dy);

        Point gridCursor = new Point(e.getX() - dx, e.getY() - dy);
//        System.out.println("mouseMoved: " + e.getPoint());
        for (Tile tile : grid) {
            if (tile.contains(gridCursor)) {
//                System.out.println(tile);
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
            ctrlPressed = true;
        }

    }

    @Override
    public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_CONTROL) {
            multSelect = false;
            ctrlPressed = false;

        }

    }

    // ---- GETTERS & SETTERS ----

    public Toolbox getToolbox(){
        return toolbox;
    }

    public void setToolbox(Toolbox toolbox){
        this.toolbox = toolbox;
    }

    public ArrayList<Tile> getOccupiedTile(){
        ArrayList<Tile> tiles = new ArrayList<>();
        for (Tile tile : grid) {
            if (tile.isOccupied()) {
                tiles.add(tile);
            }
        }
        return tiles;
    }

    public ArrayList<Tile> getSelectedTiles(){
        ArrayList<Tile> tiles = new ArrayList<>();
        for (Tile tile : grid) {
            if (tile.isSelected()) {
                tiles.add(tile);
            }

        }
        System.out.println("Selected tiles: " + tiles);
        return tiles;
    }

    public ArrayList<Integer> getSelectedTilesId(){
        ArrayList<Integer> tiles = new ArrayList<>();
        for (Tile tile : grid) {
            if (tile.isSelected()) {
                tiles.add(tile.getId());
            }

        }
        System.out.println("Selected tiles: " + tiles);
        return tiles;
    }

    public ArrayList<Tile> getAvailableTiles(){
        ArrayList<Tile> tiles = new ArrayList<>();
        for (Tile tile : grid) {
            if (!tile.isOccupied() && water_grid.contains(tile)) {
                tiles.add(tile);
            }
        }
        return tiles;
    }

    public ArrayList<Tile> getAvailableLilyTiles(){
        ArrayList<Tile> tiles = new ArrayList<>();
        for (Tile tile : grid) {
            if (tile.isLily() && tile.getFrog() == null) {
                tiles.add(tile);
            }
        }
        return tiles;
    }


    private Tile getRandomAvailableTile(){
        ArrayList<Tile> availableTiles = getAvailableTiles();
        if (availableTiles.isEmpty()) return null;
        int ind = (int) (Math.random() * availableTiles.size());
        return availableTiles.get(ind);
    }

    private Tile getRandomLilyTile(){
        ArrayList<Tile> availableTiles = getAvailableLilyTiles();
        if (availableTiles.isEmpty()) return null;
        int ind = (int) (Math.random() * availableTiles.size());
        return availableTiles.get(ind);
    }

    private Frog getRandomFrog(){
        int size = Constants.FROGS.size();
        int random = (int) (Math.random() * size);

        return Constants.FROGS.get(random);
    }


}