package filemover;

import java.io.File;
import java.io.IOException;

public class FileUtils {

    public static void move(String source, String destination) {
        try {
            org.apache.commons.io.FileUtils.copyFileToDirectory(new File(source), new File(destination));
            org.apache.commons.io.FileUtils.forceDelete(new File(source));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void move(File source, File destination) {
        try {
            org.apache.commons.io.FileUtils.copyFileToDirectory(source, destination);
            org.apache.commons.io.FileUtils.forceDelete(source);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
