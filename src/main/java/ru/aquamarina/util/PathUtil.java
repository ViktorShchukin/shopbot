package ru.aquamarina.util;

public class PathUtil {

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
        return currentPath + folderName + "/";
    }
}
