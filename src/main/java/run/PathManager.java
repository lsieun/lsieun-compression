package run;

import java.util.Objects;

public class PathManager {
    public static String getFilePath(String relativePath) {
        String dir = Objects.requireNonNull(PathManager.class.getResource("/")).getPath();
        String filepath = dir + relativePath;
        if (filepath.contains(":")) {
            return filepath.substring(1);
        }
        return filepath;
    }
}
