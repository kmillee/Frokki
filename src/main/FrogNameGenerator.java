package main;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
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

//    private static List<String> getList(String fileName) {
//        String path = Constants.RESOURCES_PATH + File.separator + "name" + File.separator + fileName;
//
//        try {
//            String content = Files.readString(Path.of(path));
//            String[] items = content.split("[,\\r\\n]+"); // Separate content by comma and new line
//            List<String> list = new ArrayList<>();
//            for (String item : items) {
//                if (!item.isBlank()) list.add(item.trim());
//            }
//            return list;
//        } catch(IOException e){
//            System.err.println("Could not read file: " + e.getMessage());
//        }
//        return null;
//    }

    private static List<String> getList(String fileName) {

        String resourcePath =
                "/name/" + fileName;

        try (InputStream is =
                     Utils.class.getResourceAsStream(resourcePath)) {

            if (is == null) {
                System.err.println("Resource not found: " + resourcePath);
                return List.of();
            }

            String content =
                    new String(is.readAllBytes(), StandardCharsets.UTF_8);

            String[] items = content.split("[,\\r\\n]+");

            List<String> list = new ArrayList<>();

            for (String item : items) {
                if (!item.isBlank()) {
                    list.add(item.trim());
                }
            }

            return list;

        } catch (IOException e) {
            System.err.println("Could not read file: " + e.getMessage());
            return List.of();
        }
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
