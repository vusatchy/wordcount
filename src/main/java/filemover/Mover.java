package filemover;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Mover {

    private String root = "root";

    private List<String> allowedFolders = Arrays.asList("test2", "test4");

    private String destination = "destination";

    public Mover(String root, List<String> allowedFolders, String destination) {
        this.root = root;
        this.allowedFolders = allowedFolders;
        this.destination = destination;
    }

    public Map<String, String> move() throws IOException {
        long size = 0;
        Map<String, String> history = new HashMap<>();
        String separator = StringEscapeUtils.escapeJava(File.separator);
        File file = new File(destination);
        if (!file.exists()) {
            if (file.mkdir()) {
                System.out.println("Directory is created!");
            } else {
                System.out.println("Failed to create directory!");
            }
        }
        List<String> toMove = new ArrayList<>();
        Files.walk(Paths.get(root))
            .filter(Files::isRegularFile)
            .filter(path -> {
                String[] splitPath = path.toString().split(separator);
                return allowedFolders.contains(splitPath[splitPath.length - 2]);
            })
            .forEach(path -> toMove.add(path.toString()));

        for (String path : toMove) {
            File source = new File(path);
            size += source.length();
            FileUtils.move(source, new File(destination));
            history.put(path, destination);
        }
        System.out.println("Moved " + size + " bytes");
        return history;
    }
}
