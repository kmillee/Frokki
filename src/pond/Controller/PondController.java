package pond.Controller;

import main.Constants;
import main.Utils;
import pond.Pond;
import pond.PondModel;
import pond.View.PondView;
import pond.Tile;
import sound.Sound;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.LinkedList;
import javax.sound.sampled.Clip;
import javax.swing.*;

/**
 * PondController handles user interactions with the Pond.
 * <p>
 * Responsibilities:
 * - Manages mouse events and interactions with tiles.
 * - Controls tools from the Toolbox (Net, Scissors, Grab, Bell).
 * - Handles automatic spawning of frogs, lily pads, reeds, rotten pads, and croco.
 * - Plays corresponding sounds and updates the PondView.
 */
public class PondController implements MouseListener, MouseMotionListener {
    private final Pond pond;
    private final PondModel model;
    private final PondView view;
    private final Toolbox toolbox;

    // Timers for automatic pond mechanics
    private Timer reedLilyTimer, rotTimer, crocoTimer, frogTimer;

    // Currently grabbed tile
    private Tile grabbedTile;
    private Image grabbedImg;


    // Chance for croco to spawn
    private double crocoSpawnChance = 0.1;

    // Sounds
    private final Sound croakSound = new Sound("/sound/croak.wav");
    private final Sound crocoSound = new Sound("/sound/jaws.wav");
    private final Sound scissorsSound = new Sound( "/sound/scissors.wav");
    private final Sound bellsound = new Sound(  "/sound/bell.wav");
    private final Sound endbellsound = new Sound("/sound/bell_short.wav");

    // ---- BELL MECHANICS ----
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

    // Offsets from view for drawing tiles accurately
    private int dx, dy;


    /**
     * Constructor: Sets up controller, listeners, and timers.
     */
    public PondController(Pond pond, PondModel model, PondView view) {
        this.pond = pond;
        this.model = model;
        this.view = view;
        this.toolbox = new Toolbox(this);

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

    // ---- TIMERS ----
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

        reedLilyTimer = new Timer(delay, e -> {
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


        rotTimer = new Timer(delay, e -> {
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


    // ---- SPAWN METHODS ----
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


    // ---- TOOLS ----

    /** Removes frog from tile and adds it to Frogedex */
    public void useNet(Tile tile){
        Utils.setCustomCursor(Constants.NET_IMG, view);
        if (tile.getFrog() != null){

            pond.getFrogedex().addFrog(tile.getFrog());
            model.getFrogs().remove(tile);
            tile.setFrog(null);

            view.repaint();
        }
    }

    /** Removes reeds from tile */
    public void useScissors(Tile tile){
        Utils.setCustomCursor(Constants.SCISSORS_IMG, view);
        if (tile.isReed()){
            tile.clean();
            model.getReeds().remove(tile);
            scissorsSound.play();

            view.repaint();
        }
    }

    /** Sets cursor to grab tool */
    public void useGrab(){
        Utils.setCustomCursor(Constants.GRAB_BEFORE_IMG, view);
    }

    // ---- CROCO MECHANICS ----


    /**
     * Determines if the bell is being jiggled by the mouse quickly enough.
     */
    private boolean isJiggling() {
        if (history.size() < 10) return false;

        // Compute average speed
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

        return speed > Constants.SPEED_THRESHOLD;
    }


    /**
     * Moves croco away from cursor when bell is used.
     */
    private void scareCroco(Point cursor){
        for (Tile tile : model.getTiles()){
            if (tile.isCroco()){
                // find running direction
                Tile neighbor;

                float croco_x = tile.x + (float) model.getCellSize() / 2;
                float croco_y = tile.y + (float) model.getCellSize() / 2;
                float dx = cursor.x - croco_x;
                float dy = cursor.y - croco_y;

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
                }
                else{
                    model.setCroco(false);
                }

            }
        }

    }

    /** Stops bell sound and croco timer */
    private void stopBellandCroco() {
        bellsound.pause();
        endbellsound.play();

        if (crocoTimer != null) {
            crocoTimer.stop();
            crocoTimer = null;
        }

        alreadyJiggling = false;
    }


    // ---- MOUSE EVENTS ----

    @Override
    public void mouseClicked(MouseEvent e) {

        Point gridCursor = view.toModelCoords(e.getPoint());

        for (Tile tile : model.getTiles()) {
            if (tile.contains(gridCursor)) {
                switch (toolbox.getCurrentTool()) {
                    case SCISSORS -> useScissors(tile);
                    case GRAB -> useGrab();
                    case NET -> useNet(tile);
                    case BELL -> { /* handled during drag */ }
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
                    grabbedTile = tile;
                    grabbedImg = tile.isLily() ? view.getLilyImg() : view.getRottenImg();
                    tile.clean();   // empty the grabbed tile
                }
            }
        }

        view.repaint();
    }

    @Override
    public void mouseReleased(MouseEvent e) {
        Point gridCursor = view.toModelCoords(e.getPoint());
        boolean placed = false;

        if (toolbox.getCurrentTool() == Toolbox.Tool.GRAB_WHILE) {

            // Mouse released outside of bin
            if (!(toolbox.getBinRectangle().contains(e.getX(), e.getY()))) {

                for (Tile tile : model.getTiles()) {

                    // Place dragged object on new tile
                    if (tile.contains(gridCursor) && model.getWaterTiles().contains(tile) && !tile.isOccupied() && !tile.isLily()) {
                        if (grabbedImg == view.getLilyImg()) {
                            tile.setLily(true);
                        } else if (grabbedImg == view.getRottenImg()) {
                            tile.setRotten(true);
                        }
                        placed = true;
                    }
                }

                // If an object is dragged to an unavailable tile, put it back to initial place
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
            stopBellandCroco();
        }
    }

    @Override
    public void mouseDragged(MouseEvent e) {

        // Bell mechanics: updates mouseData history list and compute speed
        if (toolbox.getCurrentTool() == Toolbox.Tool.BELL){
            long now = System.currentTimeMillis();
            history.add(new MouseData(e.getPoint(), now));
            lastCursorPoint = e.getPoint(); // Save latest position to get crocodile run direction

            // Remove old data
            while (!history.isEmpty() && (now - history.getFirst().time > Constants.MAX_HISTORY_MS)) {
                history.removeFirst();
            }

            boolean jigglingNow = isJiggling();
            if (jigglingNow) {
                // start bell sound if not already running
                if (!alreadyJiggling) {
                    bellsound.play();
                    bellsound.clip.loop(Clip.LOOP_CONTINUOUSLY);

                    // Start periodic scareCroco timer (every 0.5s)
                    crocoTimer = new Timer(500, evt -> scareCroco(lastCursorPoint));
                    crocoTimer.start();
                }
            }

            alreadyJiggling = jigglingNow;
        }

        // Update tool to "grab while" or "grab before" icon
        if (toolbox.getCurrentTool() != Toolbox.Tool.NONE){
            Utils.setCustomCursor(toolbox.getToolIcon(toolbox.getCurrentTool()), view);
        }

        // Update grabbed object position
        Point gridCursor = view.toModelCoords(e.getPoint());
        for (Tile tile : model.getTiles()) {
            tile.setHovered(tile.contains(gridCursor));

        }
        view.repaint();
    }

    @Override
    public void mouseMoved(MouseEvent e) {
        // Update cursor with current tool
        Utils.setCustomCursor(toolbox.getToolIcon(toolbox.getCurrentTool()), view);

        Point gridCursor = view.toModelCoords(e.getPoint());
        for (Tile tile : model.getTiles()) {tile.setHovered(tile.contains(gridCursor));}
        view.repaint();
    }

    @Override
    public void mouseEntered(MouseEvent e) {
        // If the grabbed object has been thrown into the Toolbox's bin, remove it
        if (toolbox.getCurrentTool() != Toolbox.Tool.GRAB_WHILE) {
            grabbedImg = null;
        }
    }

    @Override
    public void mouseExited(MouseEvent e) {}

    // ---- GETTERS & SETTERS ----

    public Image getGrabbedImg() {return grabbedImg;}
    public Tile getGrabbedTile() {return grabbedTile;}
    public Toolbox getToolbox() {return toolbox;}
    public void setDx(int dx) { this.dx = dx; }
    public void setDy(int dy) { this.dy = dy; }
    public int getDx() { return dx; }
    public int getDy() { return dy; }

    public void updateToolButtonIcon(Toolbox.Tool currentTool) {
        view.updateToolboxButtonIcon(currentTool);
    }


}
