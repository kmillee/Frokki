package main;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.nio.file.*;
import java.util.*;
public class FrogNameGenerator {
    private static List<String> titles;
    private static List<String> adjectives;
    private static List<String> frogWords;
    private static List<String> patterns;

    static {
        titles = getList( "title.txt");
        adjectives = getList( "adjective.txt");
        frogWords = getList( "frog_related.txt");
        patterns = getList( "patterns.txt");
    }
    private FrogNameGenerator(){} // This class should not be instantiated

    private static List<String> getList(String fileName) {
        String path = Constants.RESOURCES_PATH + File.separator + "name" + File.separator + fileName;

        try {
            String content = Files.readString(Path.of(path));
            String[] items = content.split("[,\\r\\n]+"); // Separate content by comma and new line
            List<String> list = new ArrayList<>();
            for (String item : items) {
                if (!item.isBlank()) list.add(item.trim());
            }
            return list;
        } catch(IOException e){
            System.err.println("Could not read file: " + e.getMessage());
        }
        return null;
    }

    public static String generateName(){
        String pattern = patterns.get((int)Math.floor(Math.random()*patterns.size()));

        String name = pattern
                .replace("{Title}", getRandom(titles))
                .replace("{Adjective}", getRandom(adjectives))
                .replace("{FrogWord}", getRandom(frogWords));

        return name;
    }

    private static String getRandom(List<String> list){
        return list.get((int)Math.floor(Math.random()*list.size()));
    }
}
