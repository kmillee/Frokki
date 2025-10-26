package pond;

import UI.Toolbox;
import main.Constants;
import main.Utils;
import sound.Sound;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.LinkedList;
import javax.sound.sampled.Clip;
import javax.swing.*;

// Manipulates Model and View
public class PondController implements MouseListener, MouseMotionListener {
    private final Pond pond;
    private final PondModel model;
    private final PondView view;
    private final Toolbox toolbox;

    private Timer reedLilyTimer, rotTimer, crocoTimer, frogTimer;

    private Tile grabbedTile;
    private Image grabbedImg;
    private double crocoSpawnChance = 0.1;


    private final Sound croakSound = new Sound(Constants.RESOURCES_PATH + File.separator+ "sound" + File.separator+ "croak.wav");
    private final Sound crocoSound = new Sound(Constants.RESOURCES_PATH + File.separator+ "sound" + File.separator+ "jaws.wav");
    private final Sound scissorsSound = new Sound(Constants.RESOURCES_PATH + File.separator+ "sound" + File.separator+ "scissors.wav");
    private final Sound bellsound = new Sound( Constants.RESOURCES_PATH + File.separator + "sound" + File.separator +"bell.wav");
    private final Sound endbellsound = new Sound(Constants.RESOURCES_PATH + File.separator + "sound" + File.separator + "bell_short.wav");

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
    private Point lastCursorPoint;

    public PondController(Pond pond, PondModel model, PondView view) {
        this.pond = pond;
        this.model = model;
        this.view = view;
        this.toolbox = new Toolbox();

        //TODO: sounds

        view.addMouseListener(this);
        view.addMouseMotionListener(this);

        installToolboxListeners();
        setUpTimers();
    }

    private void installToolboxListeners() {
        toolbox.getFrame().addMouseListener(this);
        toolbox.getFrame().addMouseMotionListener(this);
        toolbox.getFrame().requestFocus();
    }

    // Random timer for spawning mechanics
    public void setUpTimers(){
        setupReedLilyTimer();
        setupRotTimer();
        setupCrocoTimer();
        setupFrogTimer();

    }

    public void pauseTimers(){
        if (reedLilyTimer != null) reedLilyTimer.stop();
        if (rotTimer != null) rotTimer.stop();
        if (crocoTimer != null) crocoTimer.stop();
        if (frogTimer != null) frogTimer.stop();
    }

    private void setupReedLilyTimer() {
        int delay = Utils.randomBetween(Constants.MIN_REEDLILY_TIMER, Constants.MAX_REEDLILY_TIMER);

//        System.out.println("ree/lily timer: " + delay);
        reedLilyTimer = new javax.swing.Timer(delay, e -> {
            if (Math.random() < 0.2) {      //80% reed, 20% lily pad
                trySpawnLily();
            } else {
                trySpawnReed();
            }
            setupReedLilyTimer(); // reschedule randomly
        });
        reedLilyTimer.setRepeats(false);
        reedLilyTimer.start();
    }

    private void setupRotTimer() {
        int delay = Utils.randomBetween(Constants.MIN_ROT_TIMER, Constants.MAX_ROT_TIMER);

//        System.out.println("rot timer: " + delay);

        rotTimer = new javax.swing.Timer(delay, e -> {
            if (Math.random() < 0.3) {  // 30% chance to rot a lily pad (if any available)
                trySpawnRotten();
            }
            setupRotTimer();
        });
        rotTimer.setRepeats(false);
        rotTimer.start();
    }

    private void setupFrogTimer() {
        int delay = Utils.randomBetween(Constants.MIN_FROG_TIMER, Constants.MAX_FROG_TIMER);

        frogTimer = new Timer(delay, e -> {
            trySpawnFrog();
            setupFrogTimer();
        });
        frogTimer.setRepeats(false);
        frogTimer.start();
    }

    private void setupCrocoTimer() {
        int delay = Utils.randomBetween(Constants.MIN_CROCO_TIMER, Constants.MAX_CROCO_TIMER);

//        System.out.println("croco timer: " + delay);

        crocoTimer = new Timer(delay, e -> {
            if (Math.random() < crocoSpawnChance && !model.hasCroco()) {        // set up a low chance of spawn
                trySpawnCroco();
                crocoSpawnChance = 0.1;     // reset spawn chance
            }
            else{
                crocoSpawnChance = Math.min(0.5, crocoSpawnChance * 1.5);   // slowly increase chance of appearing
            }
            setupCrocoTimer();
        });
        crocoTimer.setRepeats(false);
        crocoTimer.start();
    }


    public void trySpawnFrog() {
        if (model.spawnFrog()) {
            croakSound.play();
            view.repaint();
        }
    }

    public void trySpawnLily() {
        if (model.spawnLily()) {
            view.repaint();
        }
    }

    public void trySpawnReed() {
        if (model.spawnReed()) {
            view.repaint();
        }
    }

    public void trySpawnRotten() {
        if (model.spawnRotten()) {
            view.repaint();
        }
    }

    public void trySpawnCroco() {
        if (model.spawnCroco()) {
            crocoSound.play();
            view.repaint();
        }
    }

// TOOLS


    // ---- TOOLS ----

    // Remove frog from lilypad and add it to frogedex
    public void useNet(Tile tile){
        Utils.setCustomCursor(Constants.NET_IMG, view);
        if (tile.getFrog() != null){

            pond.getFrogedex().addFrog(tile.getFrog());
            model.getFrogs().remove(tile);
            tile.setFrog(null);

            view.repaint();
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



    private void scareCroco(Point cursor){
        for (Tile tile : model.getTiles()){
            if (tile.isCroco()){
                // find running direction
                float croco_x = tile.x + (float) model.getCellSize() / 2;
                float croco_y = tile.y + (float) model.getCellSize() / 2;
                float dx = cursor.x - croco_x;
                float dy = cursor.y - croco_y;

                int moveX, moveY;
                Tile neighbor;
                if (Math.abs(dx) > Math.abs(dy)){
                    neighbor = (dx > 0) ? model.getLeftTile(tile) : model.getRightTile(tile); // left or right
                }
                else{
                    neighbor = (dy > 0) ? model.getUpperTile(tile) : model.getLowerTile(tile); // up or down
                }

                tile.setCroco(false);
                if (neighbor != null){
                    neighbor.setCroco(true);
                    view.repaint();
                    System.out.println("moving croco");
                }
                else{
                    model.setCroco(false);
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
        Utils.setCustomCursor(Constants.GRAB_BEFORE_IMG, view);
    }

    // Remove reed from tiles
    public void useScissors(Tile tile){
        Utils.setCustomCursor(Constants.SCISSORS_IMG, view);
        if (tile.isReed()){
            tile.clean();
            model.getReeds().remove(tile);
            scissorsSound.play();

            view.repaint();
        }

    }





    @Override
    public void mouseClicked(MouseEvent e) {

//        System.out.println("mouseClicked");
//        System.out.println("Current Tool: " + toolbox.getCurrentTool());
        Point gridCursor = view.toModelCoords(e.getPoint());

        for (Tile tile : model.getTiles()) {
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
                }
            }
        }
        view.repaint();
    }

    @Override
    public void mousePressed(MouseEvent e) {

        Point gridCursor = view.toModelCoords(e.getPoint());

        if (toolbox.getCurrentTool() == Toolbox.Tool.GRAB) {
            for (Tile tile : model.getTiles()) {
                if (tile.contains(gridCursor) && tile.isMovable()) {
                    toolbox.setCurrentTool(Toolbox.Tool.GRAB_WHILE);
//                    System.out.println("Current tool: " + toolbox.getCurrentTool());
                    grabbedTile = tile;
                    if (tile.isLily()){
                        grabbedImg = view.getLilyImg();
                    }
                    else if (tile.isRotten()){
                        grabbedImg = view.getRottenImg();
                    }
                    tile.clean();   // empty the grabbed tile
                }
            }
        }

        view.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {

        Point gridCursor = view.toModelCoords(e.getPoint());

        boolean placed = false; // check if the grabbed object has already been placed
        if (toolbox.getCurrentTool() == Toolbox.Tool.GRAB_WHILE) {

            //mouse released outside of bin
            if (!(toolbox.getBinRectangle().contains(e.getX(), e.getY()))) {

                for (Tile tile : model.getTiles()) {

                    // set object in a free spot
                    if (tile.contains(gridCursor) && model.getWaterTiles().contains(tile) && !tile.isOccupied() && !tile.isLily()) {
                        if (grabbedImg == view.getLilyImg()) {
                            tile.setLily(true);
                        } else if (grabbedImg == view.getRottenImg()) {
                            tile.setRotten(true);
                        }
                        placed = true;
                    }
                }
                // user try to set a grabbed object on an unavailable tile
                if (!placed) {
                    if (grabbedImg == view.getLilyImg()) {
                        grabbedTile.setLily(true);
                    } else if (grabbedImg == view.getRottenImg()) {
                        grabbedTile.setRotten(true);
                    }
                }

            }

            view.repaint();
        }

        // In any case, reset all grabbed status
        grabbedTile = null;
        grabbedImg = null;
        if (toolbox.getCurrentTool() == Toolbox.Tool.GRAB_WHILE) {
            toolbox.setCurrentTool(Toolbox.Tool.GRAB);
        }

        if (toolbox.getCurrentTool() == Toolbox.Tool.BELL) {
            // bell sound stop
            stopBellandCroco();
        }
    }


    @Override
    public void mouseEntered(MouseEvent e) {
        if (toolbox.getCurrentTool() != Toolbox.Tool.GRAB_WHILE) {
            grabbedImg = null;
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {

    }

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

        // update tool to "grab while" or "grab before" icon
        if (toolbox.getCurrentTool() != Toolbox.Tool.NONE){
            Utils.setCustomCursor(toolbox.getToolIcon(toolbox.getCurrentTool()), view);
        }

        // update grabbed object position
        Point gridCursor = view.toModelCoords(e.getPoint());
        for (Tile tile : model.getTiles()) {
            // System.out.println(tile);
            tile.setHovered(tile.contains(gridCursor));

        }
        view.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // Update cursor with current tool
        if (toolbox.getCurrentTool() != Toolbox.Tool.NONE){
            Utils.setCustomCursor(toolbox.getToolIcon(toolbox.getCurrentTool()), view);
        }

//         System.out.println("mouse moved: " + e.getX() + " " + e.getY());

        Point gridCursor = view.toModelCoords(e.getPoint());
        for (Tile tile : model.getTiles()) {
            tile.setHovered(tile.contains(gridCursor));
        }
        view.repaint();
    }

    public Image getGrabbedImg() {
        return grabbedImg;
    }

    public void setGrabbedImg(Image grabbedImg) {
        this.grabbedImg = grabbedImg;
    }

    public Tile getGrabbedTile() {
        return grabbedTile;
    }

    public void setGrabbedTile(Tile grabbedTile) {
        this.grabbedTile = grabbedTile;
    }

    public Toolbox getToolbox() {
        return toolbox;
    }

    private int dx, dy;
    public void setDx(int dx) { this.dx = dx; }
    public void setDy(int dy) { this.dy = dy; }
    public int getDx() { return dx; }
    public int getDy() { return dy; }

}
