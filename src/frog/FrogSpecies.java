package frog;

// Enum representing different species of frogs
public enum FrogSpecies {
    BLUE,
    GREEN,
    DARK_BLUE,
    OCRE,
    VIOLET,
    DARK_PINK,
    RED,
    TEAL,
    ORANGE,
    PINK,
    ALBINOS,
    BROWN,
    RAINETTE;

    @Override
    public String toString() {return name().replace("_", " ").toLowerCase();}

    public int toInt(){
        return ordinal();
    }

    public static FrogSpecies getSpecies(int index){
        return values()[index];
    }

    public static FrogSpecies getRandom(){
        int rand =(int)(Math.random()*values().length);
        return getSpecies(rand);
    }
}

