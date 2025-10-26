package pond;

import frog.Frog;
import frog.FrogSpecies;
import main.Constants;
import main.FrogNameGenerator;
import main.Utils;

import java.awt.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * PondModel represents the current state of the pond game.
 * <p>
 * It manages the grid of tiles, spawns objects (frogs, lily pads, reeds, rotten pads, crocodiles),
 * and keeps track of object placement and interactions.
 */
public class PondModel {
    private int cellSize;
    private final int cols, rows;
    private final List<Tile> tiles = new ArrayList<>();
    private final ArrayList<Tile> waterTiles = new ArrayList<>();
    private final List<Tile> lilyPads = new ArrayList<>();
    private final List<Tile> reeds = new ArrayList<>();
    private final List<Tile> frogs = new ArrayList<>();
    private final List<Tile> rottedPads = new ArrayList<>();

    private boolean crocoPresent = false;

    /**
     * Constructs a PondModel for the given pond dimensions.
     *
     * @param pondWidth  Pond width in pixels
     * @param pondHeight Pond height in pixels
     * @param cellSize   Size of a tile in pixels
     */
    public PondModel(int pondWidth, int pondHeight, int cellSize) {
        this.cellSize = cellSize;
        this.cols = pondWidth / cellSize;
        this.rows = pondHeight / cellSize;

        // Initialize tiles
        for(int i = 0; i < this.rows; i++){
            for(int j = 0; j < this.cols; j++){
                tiles.add(new Tile(j*cellSize,i*cellSize,cellSize,cellSize));
            }
        }

        // Identify water tiles (playable) from Constants.WATER_TILES
        for (Tile tile : tiles){
            if (Utils.contains(Constants.WATER_TILES, tile.getId())) {
                waterTiles.add(tile);
            }
        }
    }


    // ---- GETTERS ----
    public int getCellSize(){return cellSize;}
    public int getCols() {return cols;}
    public int getRows() {return rows;}
    public ArrayList<Tile> getWaterTiles() { return waterTiles;}
    public List<Tile> getLilyPads() {return lilyPads;}
    public List<Tile> getReeds() {return reeds;}
    public List<Tile> getFrogs() {return frogs;}
    public List<Tile> getRottedPads() {return rottedPads;}
    public List<Tile> getTiles() { return tiles; }
    public boolean hasCroco(){return crocoPresent;}
    public void setCroco(boolean b){crocoPresent = b;}



    // ---- SPAWN METHODS ----
    /** Spawn a frog on a random available lily pad, if no crocodile is present */
    public boolean spawnFrog(){
        if (crocoPresent){return false;}

        FrogSpecies frogSpecies = FrogSpecies.getRandom();
        String name = FrogNameGenerator.generateName();
        Frog frog = new Frog(name,frogSpecies, Utils.getFormattedDate());

        Tile tile = getRandomLilyTile();
        if (tile == null){return false;}

        tile.setFrog(frog);
        frogs.add(tile);
        return true;
    }

    /** Spawn a reed on a random available water tile */
    public boolean spawnReed(){
        if (getObjectTotal() >= Constants.MAX_OBJECTS){return false;}

        Tile tile = getRandomAvailableTile();
        if (tile == null){ return false;}

        tile.setReed(true);
        reeds.add(tile);
        return true;

    }

    /** Spawn a lily pad on a random available water tile */
    public boolean spawnLily(){
        if (getObjectTotal() >= Constants.MAX_OBJECTS){return false;}

        Tile tile = getRandomAvailableTile();
        if (tile == null){ return false;}

        tile.setLily(true);
        lilyPads.add(tile);
        return true;


    }

    /** Turn a random lily pad into a rotten pad */
    public boolean spawnRotten(){
        Tile tile = getRandomLilyTile();
        if (tile == null){ return false; }

        tile.setRotten(true);
        lilyPads.remove(tile);
        rottedPads.add(tile);
        return true;
    }

    /** Spawn a crocodile on a random available tile, removing all frogs */
    public boolean spawnCroco(){
        Tile tile = getRandomAvailableTile();
        if (tile == null){ return false; }

        tile.setCroco(true);
        crocoPresent = true;
        clearFrogs();
        return true;

    }

    // ---- HELPERS ----

    /** Returns all water tiles that are unoccupied */
    public ArrayList<Tile> getAvailableTiles(){
        ArrayList<Tile> temp = new ArrayList<>();
        for (Tile tile : tiles) {
            if (!tile.isOccupied() && waterTiles.contains(tile)) {
                temp.add(tile);
            }
        }
        return temp;
    }

    /** Returns all lily pad tiles without frogs */
    public ArrayList<Tile> getAvailableLilyTiles(){
        ArrayList<Tile> temp = new ArrayList<>();
        for (Tile tile : tiles) {
            if (tile.isLily() && tile.getFrog() == null) {
                temp.add(tile);
            }
        }
        return temp;
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

    /** Removes all frogs from the pond */
    private void clearFrogs(){
        for (Tile tile : frogs){
            tile.setFrog(null);
        }
        frogs.clear();
    }

    /** Counts all objects (lily pads, reeds, rotten pads) currently in the pond */
    private int getObjectTotal(){
        int total = 0;
        for (Tile tile : tiles){
            if (tile.isLily() ||tile.isReed() || tile.isRotten()){
                total++;
            }
        }
        return total;
    }

    // ---- RESET & ACCESS ----

    /** Clears the pond of all objects and resets the state */
    public void reset() {
        for (Tile t : tiles) {
            t.setFrog(null);
            t.clean();
        }
        lilyPads.clear();
        reeds.clear();
        frogs.clear();
        rottedPads.clear();
        crocoPresent = false;
    }

    /** Returns the tile containing the given point, or null if none */
    public Tile getTileAt(Point p){
        for (Tile t : tiles) {
            if (t.contains(p)) return t;
        }
        return null;
    }


    // ---- NEIGHBOR ACCESS ----
    public Tile getUpperTile(Tile tile){
        int up_id = tile.getId() - cols;
        if (up_id < 0) return null;

        return tiles.get(up_id);
    }

    public Tile getLowerTile(Tile tile){
        int down_id = tile.getId() + cols;
        if (down_id >= tiles.size()) return null;
        return tiles.get(down_id);
    }

    public Tile getRightTile(Tile tile){
        if (tile.getId() % cols == cols - 1) {  // already right-most column
            return null;
        }
        return tiles.get(tile.getId()+1);
    }

    public Tile getLeftTile(Tile tile){
        if (tile.getId() % cols == 0) {  // already left-most column
            return null;
        }
        return tiles.get(tile.getId()-1);
    }
}
