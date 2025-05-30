package ru.aquamarina.util;


import java.util.Arrays;
import java.util.List;

public class PathUtil {

    public static String getFoldersInDirectory(String currentDirectory, List<String> subDirs) {
        return subDirs.stream()
                .filter(sub -> sub.contains(currentDirectory))
                .map(sub -> sub.substring(currentDirectory.length()))
                .map(sub -> sub.split("/"))
                .flatMap(Arrays::stream)
                .findFirst()
                .map(sub -> new StringBuilder(currentDirectory).append(sub).append("/").toString())
                // todo not safe to call get. redo this.
                .get();

    }

    public static String getFolderName(String folderPath) {
        String[] split = folderPath.split("/");
        return split[split.length - 1];
    }

    public static String getSubfolder(String currentPath, String folderPath) {
        if (!folderPath.contains(currentPath)) {
            // todo get rid of this throw. Normally this situation should not be exist
            throw new RuntimeException("wrong current path");
        }
        String folderEnding = folderPath.substring(currentPath.length());
        String[] split = folderEnding.split("/");
        String folderName = split[0];
        return new StringBuilder(currentPath).append(folderName).append("/").toString();
    }
}
