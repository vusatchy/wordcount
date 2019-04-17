package filemover;

import org.apache.commons.lang3.StringEscapeUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class FileMoverApp {

    private final static String root = "root";

    private final static List<String> allowedFolders = Arrays.asList("test2", "test4");

    private final static String destination = "destination";

    public static void main(String[] args) throws IOException {
        test("root", Arrays.asList("test2", "test4"), "destination");
    }

    public static void test(String root, List<String> allowedFolders, String destination) throws IOException {
        String separator = StringEscapeUtils.escapeJava(File.separator);
        Mover mover = new Mover(root, allowedFolders, destination);
        Map<String, String> history = mover.move();
        Files.walk(Paths.get(root))
            .filter(Files::isDirectory)
            .filter(Files::exists)
            .filter(path -> {
                String[] splitPath = path.toString().split(separator);
                return splitPath.length >= 2 && allowedFolders.contains(splitPath[splitPath.length - 2]);
            })
            .forEach(path ->
                     {
                         System.out.println("Checking emptiness of " + path);
                         if (new File(String.valueOf(path)).list().length != 0) {
                             throw new RuntimeException("Folder " + path + " is not empty");
                         }
                     });
        System.out.println("Test passed successfully");

        history.forEach((source, dest) -> {
            String[] paths = source.split(separator);
            String newSource = destination + separator + paths[paths.length - 1];
            List<String> pathsList = new ArrayList<>(Arrays.asList(paths));
            pathsList.remove(pathsList.size() - 1);
            String newDestination = pathsList.stream().collect(Collectors.joining(separator));
            FileUtils.move(newSource, newDestination);
        });

    }
}
