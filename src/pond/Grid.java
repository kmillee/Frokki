package pond;

import UI.Toolbox;
import frog.Frog;
import frogedex.Frogedex;
import main.Constants;
import main.Utils;
import sound.Sound;

import javax.sound.sampled.Clip;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.util.LinkedList;


// Class handling input of the pond
public class Grid extends JPanel implements MouseListener, MouseMotionListener, KeyListener {

    // Basic variable
    public final int cols, rows, cellSize;
    private Pond pond;

    // Media
    public Image image;
    public Image grabbed_img; // image of currently grabbed item
    private final Image reedImg = new ImageIcon(Constants.RESOURCES_PATH + File.separator+ "pond" + File.separator + "reed.jpg").getImage();
    private final ArrayList<Image> reedImages = new ArrayList<Image>() {
        {
            add(new ImageIcon(Constants.RESOURCES_PATH + File.separator+ "pond" + File.separator + "reed_1.png").getImage());
            add(new ImageIcon(Constants.RESOURCES_PATH + File.separator+ "pond" + File.separator + "reed_2.png").getImage());
            add(new ImageIcon(Constants.RESOURCES_PATH + File.separator+ "pond" + File.separator + "reed_3.png").getImage());

        }
    };
    private final Image lilyImg = new ImageIcon(Constants.RESOURCES_PATH + File.separator+ "pond" + File.separator + "lilypad.png").getImage();
    private final Image rottenImg = new ImageIcon(Constants.RESOURCES_PATH + File.separator+ "pond" + File.separator + "rotten.png").getImage();
    private final Image crocoImg = new ImageIcon(Constants.RESOURCES_PATH + File.separator+ "pond" + File.separator + "croco.png").getImage();




    // Tile management
    private final ArrayList<Tile> grid, water_grid, lily_grid, reed_grid, frog_grid, rotten_grid;

    // Relative position help
    private Point gridOrigin;
    private int dx, dy;

    // Tools
    private Toolbox toolbox;
    private final Sound bellsound = new Sound( Constants.RESOURCES_PATH + File.separator + "sound" + File.separator +"bell.wav");
    private final Sound endbellsound = new Sound(Constants.RESOURCES_PATH + File.separator + "sound" + File.separator + "bell_short.wav");

    // Timers
    private Timer reedLilyTimer;
    private Timer rotTimer;
    private Timer crocoSpawnTimer;
    private Timer frogSpawnTimer;


    // Other
    private Timer timer;
    private boolean multSelect, ctrlPressed = false;
    private Tile grabbedTile;
    private boolean croco; // check to block frog spawn

    // BELL MECHANICS
    static class MouseData {
        Point position;
        long time;
        MouseData(Point p, long t){
            position = p;
            time = t;
        }
    }

    private final LinkedList<MouseData> history = new LinkedList<>();
    private boolean alreadyJiggling = false;
    private Timer crocoTimer;
    private Point lastCursorPoint;





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
        this.reed_grid = new ArrayList<>();
        this.frog_grid = new ArrayList<>();
        this.rotten_grid = new ArrayList<>();

        this.toolbox = new Toolbox();

        installUI();
        setUpTimers();
    }

    public void installUI(){
        this.addMouseListener(this);
        this.addMouseMotionListener(this);
        this.addKeyListener(this);
        this.requestFocus();
    }

    // Random timer for spawning mechanics
    private void setUpTimers(){
//        int delay = (int) (Math.random() * Constants.MAX_DELAY);
//
////        System.out.println("setUpTimer:" +  delay);
//
//        timer = new Timer(delay, e -> {
////            System.out.println("rotten");
//            spawnRotten();
//            setUpTimer();
//        });
//
//        timer.setRepeats(false);
//        timer.start();

        setupReedLilyTimer();
        setupRotTimer();
        setupCrocoTimer();
        setupFrogTimer();

    }

    private void setupReedLilyTimer() {
        //int delay = Constants.MIN_REEDLILY_TIMER + (int) (Math.random() * Constants.MAX_DELAY);

        int delay = 3000 + (int) (Math.random() * 5000); // 3-8sec
        reedLilyTimer = new Timer(delay, e -> {
            if (Math.random() < 0.2) {      //80% reed, 20% lily pad
                spawnLily();
            } else {
                spawnReed();
            }
            setupReedLilyTimer(); // reschedule randomly
        });
        reedLilyTimer.setRepeats(false);
        reedLilyTimer.start();
    }

    private void setupRotTimer() {
        int delay = 5000 + (int) (Math.random() * 7000); // 5–12sec
        rotTimer = new Timer(delay, e -> {
            spawnRotten();
            setupRotTimer();
        });
        rotTimer.setRepeats(false);
        rotTimer.start();
    }

    private void setupCrocoTimer() {
        int delay = 20000 + (int) (Math.random() * 20000); // 20–40s
        crocoSpawnTimer = new Timer(delay, e -> {
            if (Math.random() < 0.1 && !croco) {        // set up a low chance of spawn
                spawnCroco();
            }
            setupCrocoTimer();
        });
        crocoSpawnTimer.setRepeats(false);
        crocoSpawnTimer.start();
    }

    private void setupFrogTimer() {
        int delay = 7000 + (int) (Math.random() * 5000); // 7–12s
        frogSpawnTimer = new Timer(delay, e -> {
            if (!croco) {
                spawnFrog();
            }
            setupFrogTimer();
        });
        frogSpawnTimer.setRepeats(false);
        frogSpawnTimer.start();
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

    // Update tile display according to their state (reed, lilypad, frogs...) and user input (selection, hovering)
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
            if (tile.isReed()){
                // select random image among the reed options
                g.drawImage(reedImages.get(tile.getRandomReed()),tile.x + dx +1, tile.y + dy +1, tile.width, tile.height, null);
            }

            if (tile.isRotten()){
                g.drawImage(rottenImg, tile.x + dx +1, tile.y + dy +1, tile.width, tile.height, null);
            }

            if (tile.isCroco()){
                g.drawImage(crocoImg, tile.x + dx +1, tile.y + dy +1, tile.width, tile.height, null);
            }

            // Color tile with low opacity when hovering
            if (tile.isHovered()) {

                // if grabbing something
                if (grabbedTile != null) {
//                    System.out.println("hovering with:"+ grabbedTile);
                    g.drawImage(grabbed_img, tile.x + dx +1, tile.y + dy +1, tile.width, tile.height, null);

                    // Available tile for grabbed object, display in green
                    if ((water_grid.contains(tile) && !tile.isOccupied() && !tile.isLily() && !tile.isRotten()) || (tile == grabbedTile)) {
                        g.setColor(new Color(100,255,100,100));
                    }

                    // Cannot move grabbed tile here, display in red
                    else{
                        g.setColor(new Color(255,100,100, 100));
                    }
                }

                // If nothing is grabbed, display in white
                else{
                    g.setColor(new Color(255, 255, 255,100));
                }

                g.fillRect(tile.x + dx, tile.y + dy, tile.width, tile.height);
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

    }


    // ---- POND MODIFICATION ----
    // SPAWN & DELETE
    public void spawnFrog(){
        if (croco){
            System.out.print("no forg while croco is here!");
            return;
        }
        Frog frog = getRandomFrog();    // need to randomize frog by rarity
        Tile tile = getRandomLilyTile();
        if (tile != null){
            tile.setFrog(frog);
            frog_grid.add(tile);
            repaint();
        }

        else{
            System.out.println("No lily pad available to spawn frog.");
        }
    }


    public void spawnReed(){

        if (getObjectTotal() >= Constants.MAX_OBJECTS){
            System.out.println("Too many objects in the pond already");
        }
        else{
            Tile tile = getRandomAvailableTile();
            if (tile != null){
                tile.setReed(true);
                reed_grid.add(tile);
                repaint();
            }
        }
    }

    public void spawnLily(){
        if (getObjectTotal() >= Constants.MAX_OBJECTS){
            System.out.println("Too many objects in the pond already");
            return;
        }
        else{
            Tile tile = getRandomAvailableTile();
            if (tile != null){
                tile.setLily(true);
                lily_grid.add(tile);
                repaint();
            }
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

//        else{
////            System.out.println("no lily pad available");
//        }

    }

    public void spawnCroco(){
        Tile tile = getRandomAvailableTile();
        if (tile != null){
            tile.setCroco(true);
            croco = true;
            clearFrogs();
            repaint();
        }
    }


    // ---- TOOLS ----

    // Remove frog from lilypad and add it to frogedex
    public void useNet(Tile tile){
        Utils.setCustomCursor(Constants.NET_IMG, this);
        if (tile.getFrog() != null){

            pond.getFrogedex().addFrog(tile.getFrog());
            frog_grid.remove(tile);
            tile.setFrog(null);

            repaint();
        }
    }


    // Activated periodically to stop bell when it is still
//    private void checkInactivity() {
//        long now = System.currentTimeMillis();
////        System.out.println("\n##############\nchecking inactivity");
////        System.out.println("Last move:" + (now -  lastMoveTime) + "ms ago");
////        System.out.println("Inactivity threshold: " + main.Constants.INACTIVITY_MS);
//
//        if (alreadyJiggling && (now - lastMoveTime > Constants.INACTIVITY_MS)) {
//            alreadyJiggling = false;
//            System.out.println("stopped jiggling");
//            bellsound.pause();
//            endbellsound.play();
//        }
//    }


    private void scareCroco(Point cursor){
        for (Tile tile : grid){
            if (tile.isCroco()){
                // find running direction
                float croco_x = tile.x + (float) cellSize / 2;
                float croco_y = tile.y + (float) cellSize / 2;
                float dx = cursor.x - croco_x;
                float dy = cursor.y - croco_y;

                int moveX, moveY;
                Tile neighbor;
                if (Math.abs(dx) > Math.abs(dy)){
                    neighbor = (dx > 0) ? getLeftTile(tile) : getRightTile(tile); // left or right
                }
                else{
                    neighbor = (dy > 0) ? getUpperTile(tile) : getLowerTile(tile); // up or down
                }

                tile.setCroco(false);
                if (neighbor != null){
                    neighbor.setCroco(true);
                    repaint();
                    System.out.println("moving croco");
                }
                else{
                    croco = false;
                    System.out.println("croco out");
                }


            }
        }

    }

    private void stopBellandCroco() {
        if (bellsound != null) bellsound.pause();
        if (endbellsound != null) endbellsound.play();

        if (crocoTimer != null) {
            crocoTimer.stop();
            crocoTimer = null;
        }

        alreadyJiggling = false;
    }



    // Only used to change the cursor, the actual work is done in listeners
    public void useGrab(){
        Utils.setCustomCursor(Constants.GRAB_BEFORE_IMG, this);
    }

    // Remove reed from tiles
    public void useScissors(Tile tile){
        Utils.setCustomCursor(Constants.SCISSORS_IMG, this);
        if (tile.isReed()){
            tile.clean();
            reed_grid.remove(tile);
            //TODO: play a sound

            repaint();
        }

    }

    private boolean isJiggling() {
        if (history.size() < 10) return false;

        // compute average speed
        double totalDist = 0;
        long totalTime = history.getLast().time - history.getFirst().time;

        for (int i = 1; i < history.size(); i++) {
            Point p1 = history.get(i - 1).position;
            Point p2 = history.get(i).position;
            totalDist += p1.distance(p2);
        }

        double speed = totalDist / Math.max(totalTime, 1); // pixels/ms

        if (totalTime > Constants.INACTIVITY_MS) return false;
        if (totalDist < 20) return false;

//        System.out.println("Total dist in the last move: " + totalDist);
//        System.out.println("Total time in the last move: " + totalTime);
//        System.out.println("Speed: " + speed);
//        System.out.println("Jiggles: " + (speed > main.Constants.SPEED_THRESHOLD));

        return speed > Constants.SPEED_THRESHOLD /*&& directionChanges > 2*/;
    }



    // ---- LISTENERS ----

    // Use function associated to currentTool
    @Override
    public void mouseClicked(MouseEvent e) {

//        System.out.println("mouseClicked");
//        System.out.println("Current Tool: " + toolbox.getCurrentTool());
        dx = gridOrigin.x;
        dy = gridOrigin.y;
        Point gridCursor = new Point(e.getX() - dx, e.getY() - dy);

        for (Tile tile : grid) {
            if (tile.contains(gridCursor)) {
                switch (toolbox.getCurrentTool()) {
                    case Toolbox.Tool.BELL:
                        break;
                    case Toolbox.Tool.SCISSORS:
                        useScissors(tile);
                        break;
                    case Toolbox.Tool.GRAB:
                        useGrab();
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
        repaint();
    }

    // If mouse is pressed on a grabbable tile when the GRAB tool is on, currentTool switches to GRAB_WHILE
    @Override
    public void mousePressed(MouseEvent e) {
        multSelect = true;
//        System.out.println("mousepressed");

        Point gridCursor = new Point(e.getX() - gridOrigin.x, e.getY() - gridOrigin.y);

        if (toolbox.getCurrentTool() == Toolbox.Tool.GRAB) {
            for (Tile tile : grid) {
                if (tile.contains(gridCursor) && tile.isMovable()) {
                    toolbox.setCurrentTool(Toolbox.Tool.GRAB_WHILE);
//                    System.out.println("Current tool: " + toolbox.getCurrentTool());
                    grabbedTile = tile;
                    if (tile.isLily()){
                        grabbed_img = lilyImg;
                    }
                    else if (tile.isRotten()){
                        grabbed_img = rottenImg;
                    }
                    tile.clean();   // empty the grabbed tile
                }
            }
        }

        repaint();
    }

    // checks if the mouse has been released on the toolbox's bin icon when grabbing an object
    // if not, check if the object has been placed on an available tile
    // if not, put the object back in its initial place
    @Override
    public void mouseReleased(MouseEvent e) {
        if (!ctrlPressed) {
            multSelect = false;
        }

        Point gridCursor = new Point(e.getX() - gridOrigin.x, e.getY() - gridOrigin.y);

        boolean placed = false; // check if the grabbed object has already been placed
        if (toolbox.getCurrentTool() == Toolbox.Tool.GRAB_WHILE) {

            //mouse released outside of bin
            if (!(toolbox.getBinRectangle().contains(e.getX(), e.getY()))) {

                for (Tile tile : grid) {

                    // set object in a free spot
                    if (tile.contains(gridCursor) && water_grid.contains(tile) && !tile.isOccupied() && !tile.isLily()) {
                        if (grabbed_img == lilyImg) {
                            tile.setLily(true);
                        } else if (grabbed_img == rottenImg) {
                            tile.setRotten(true);
                        }
                        placed = true;
                    }
                }
                // user try to set a grabbed object on an unavailable tile
                if (!placed) {
                    if (grabbed_img == lilyImg) {
                        grabbedTile.setLily(true);
                    } else if (grabbed_img == rottenImg) {
                        grabbedTile.setRotten(true);
                    }
                }

            }
//            else{
//                System.out.println("mouse in bin!  " + e.getX() + ", " + e.getY());
//            }
            repaint();
        }

        // In any case, reset all grabbed status
        grabbedTile = null;
        grabbed_img = null;
        if (toolbox.getCurrentTool() == Toolbox.Tool.GRAB_WHILE) {
            toolbox.setCurrentTool(Toolbox.Tool.GRAB);
        }

        if (toolbox.getCurrentTool() == Toolbox.Tool.BELL) {
            // bell sound stop
            stopBellandCroco();
        }

//        System.out.println("Mouse released.");

    }

    // update the grabbed tile if we threw it in the bin
    @Override
    public void mouseEntered(MouseEvent e) {
        if (toolbox.getCurrentTool() != Toolbox.Tool.GRAB_WHILE) {
            grabbed_img = null;
        }

    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

    // Multiple tiles are selected if dragging with ctrl pressed down or dragging right click
    // If the grab tool is on, cursor will change image whether the user is grabbing a tile or not
    // It will also display the grabbed object is low opacity on hovered tiles
    @Override
    public void mouseDragged(MouseEvent e) {
        // Bell mechanics
        if (toolbox.getCurrentTool() == Toolbox.Tool.BELL){

            long now = System.currentTimeMillis();
            history.add(new MouseData(e.getPoint(), now));
            lastCursorPoint = e.getPoint(); // save latest cursor for croco

            // remove old data
            while (!history.isEmpty() && (now - history.getFirst().time > Constants.MAX_HISTORY_MS)) {
                history.removeFirst();
//                System.out.println("Old data removed, size og the list: " + history.size());
            }

            boolean jigglingNow = isJiggling();
//            System.out.println("Is jiggling: " + jigglingNow);

            if (jigglingNow) {
                // start bell sound if not already running
                if (!alreadyJiggling) {
                    bellsound.play();
                    bellsound.clip.loop(Clip.LOOP_CONTINUOUSLY);

                    // start periodic croco timer (every 0.5s)
                    crocoTimer = new Timer(500, evt -> scareCroco(lastCursorPoint));
                    crocoTimer.start();
                }
            }

            alreadyJiggling = jigglingNow;
        }

        if ((ctrlPressed) ||(SwingUtilities.isRightMouseButton(e))) {
            Point gridCursor = new Point(e.getX() - dx, e.getY() - dy);
            // System.out.println("mouseMoved: " + e.getPoint());
            for (Tile tile : grid) {
                if (tile.contains(gridCursor)) {
                    tile.setSelected(true);
                    repaint();
                }
            }

        }

        // update tool to "grab while" or "grab before" icon
        if (toolbox.getCurrentTool() != Toolbox.Tool.NONE){
            Utils.setCustomCursor(toolbox.getToolIcon(toolbox.getCurrentTool()), this);
        }

        // System.out.println("mouseMoved: " + e.getPoint());

        // update grabbed object position
        Point gridCursor = new Point(e.getX() - dx, e.getY() - dy);
        for (Tile tile : grid) {
            // System.out.println(tile);
            tile.setHovered(tile.contains(gridCursor));

        }
        repaint();

    }

    // Switches tile to hovered or not
    @Override
    public void mouseMoved(MouseEvent e) {
        // Update cursor with current tool
        if (toolbox.getCurrentTool() != Toolbox.Tool.NONE){
            Utils.setCustomCursor(toolbox.getToolIcon(toolbox.getCurrentTool()), this);
        }



//         System.out.println("mouse moved: " + e.getX() + " " + e.getY());

        Point gridCursor = new Point(e.getX() - dx, e.getY() - dy);
        for (Tile tile : grid) {
            tile.setHovered(tile.contains(gridCursor));
        }
        repaint();

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
//        System.out.println("Selected tiles: " + tiles);
        return tiles;
    }

    public ArrayList<Integer> getSelectedTilesId(){
        ArrayList<Integer> tiles = new ArrayList<>();
        for (Tile tile : grid) {
            if (tile.isSelected()) {
                tiles.add(tile.getId());
            }

        }
//        System.out.println("Selected tiles: " + tiles);
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

    private void clearFrogs(){
        for (Tile tile : frog_grid){
            tile.setFrog(null);
        }
        frog_grid.clear();
    }

    private Tile getUpperTile(Tile tile){
        int up_id = tile.getId() - cols;
        if (up_id < 0) return null;

        return grid.get(up_id);
    }

    private Tile getLowerTile(Tile tile){
        int down_id = tile.getId() + cols;
        if (down_id >= grid.size()) return null;
        return grid.get(down_id);
    }

    private Tile getRightTile(Tile tile){
        if (tile.getId() % cols == cols - 1) {  // already right-most column
            return null;
        }
        return grid.get(tile.getId()+1);
    }

    private Tile getLeftTile(Tile tile){
        if (tile.getId() % cols == 0) {  // already left-most column
            return null;
        }
        return grid.get(tile.getId()-1);
    }

    private int getObjectTotal(){
        int total = 0;
        for (Tile tile : grid){
            if (tile.isLily() ||tile.isReed() || tile.isRotten()){
                total++;
            }
        }
        return total;
    }



}