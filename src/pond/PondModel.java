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

// Current Game State
// Handle spawning, notify change
public class PondModel {
    private int cellSize = 25;
    private int cols, rows;
    private List<Tile> tiles = new ArrayList<>();
    private ArrayList<Tile> waterTiles = new ArrayList<>();
    private List<Tile> lilyPads = new ArrayList<>();
    private List<Tile> reeds = new ArrayList<>();
    private List<Tile> frogs = new ArrayList<>();
    private List<Tile> rottedPads = new ArrayList<>();

    private boolean crocoPresent = false;

    private final Random rand = new Random();

    public PondModel(int pondWidth, int pondHeight, int cellSize) {
        this.cellSize = cellSize;
        this.cols = pondWidth / cellSize;
        this.rows = pondHeight / cellSize;

        for(int i = 0; i < this.rows; i++){
            for(int j = 0; j < this.cols; j++){
                tiles.add(new Tile(j*cellSize,i*cellSize,cellSize,cellSize));
            }
        }

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



    // ---- CORE MECHANICS ----
    public boolean spawnFrog(){
        if (crocoPresent){
            System.out.print("no forg while croco is here!");
            return false;
        }

        FrogSpecies frogSpecies = FrogSpecies.getRandom();
        String name = FrogNameGenerator.generateName();
        Frog frog = new Frog(name,frogSpecies, Utils.getFormattedDate());

        Tile tile = getRandomLilyTile();
        if (tile == null){
            System.out.println("No lily pad available to spawn frog.");
            return false;
        }

        tile.setFrog(frog);
        frogs.add(tile);
        return true;
    }


    public boolean spawnReed(){

        if (getObjectTotal() >= Constants.MAX_OBJECTS){
            System.out.println("Too many objects in the pond already");
            return false;
        }
        Tile tile = getRandomAvailableTile();
        if (tile == null){ return false; }

        tile.setReed(true);
        reeds.add(tile);
        return true;

    }

    public boolean spawnLily(){
        if (getObjectTotal() >= Constants.MAX_OBJECTS){
            System.out.println("Too many objects in the pond already");
            return false;
        }

        Tile tile = getRandomAvailableTile();
        if (tile == null){ return false; }

        tile.setLily(true);
        lilyPads.add(tile);
        return true;


    }

    public boolean spawnRotten(){
        Tile tile = getRandomLilyTile();
        if (tile == null){ return false; }

        tile.setRotten(true);
        lilyPads.remove(tile);
        rottedPads.add(tile);
        return true;
    }

    public boolean spawnCroco(){
        Tile tile = getRandomAvailableTile();
        if (tile == null){ return false; }

        tile.setCroco(true);
        crocoPresent = true;
        clearFrogs();
        return true;

    }

    public void clearCroco() {
        crocoPresent = false;
        for (Tile tile : tiles) {
            if (tile.isCroco()) {
                tile.setCroco(false);
            }
        }
    }

    // Helpers

    public ArrayList<Tile> getOccupiedTile(){
        ArrayList<Tile> temp = new ArrayList<>();
        for (Tile tile : tiles) {
            if (tile.isOccupied()) {
                temp.add(tile);
            }
        }
        return temp;
    }

    public ArrayList<Tile> getSelectedTiles(){
        ArrayList<Tile> temp = new ArrayList<>();
        for (Tile tile : tiles) {
            if (tile.isSelected()) {
                temp.add(tile);
            }

        }
//        System.out.println("Selected tiles: " + tiles);
        return temp;
    }

    public ArrayList<Integer> getSelectedTilesId(){
        ArrayList<Integer> temp = new ArrayList<>();
        for (Tile tile : tiles) {
            if (tile.isSelected()) {
                temp.add(tile.getId());
            }

        }
//        System.out.println("Selected tiles: " + tiles);
        return temp;
    }

    public ArrayList<Tile> getAvailableTiles(){
        ArrayList<Tile> temp = new ArrayList<>();
        for (Tile tile : tiles) {
            if (!tile.isOccupied() && waterTiles.contains(tile)) {
                temp.add(tile);
            }
        }
        return temp;
    }

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

    private Frog getRandomFrog(){
        int size = FrogSpecies.values().length;

        int random = (int) (Math.random() * size);

        return Constants.FROGS.get(random);
    }

    private void clearFrogs(){
        for (Tile tile : frogs){
            tile.setFrog(null);
        }
        frogs.clear();
    }

    private int getObjectTotal(){
        int total = 0;
        for (Tile tile : tiles){
            if (tile.isLily() ||tile.isReed() || tile.isRotten()){
                total++;
            }
        }
        return total;
    }

    // ---- MODEL SETUP ----

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

    public Tile getTileAt(Point p){
        for (Tile t : tiles) {
            if (t.contains(p)) return t;
        }
        return null;
    }


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
