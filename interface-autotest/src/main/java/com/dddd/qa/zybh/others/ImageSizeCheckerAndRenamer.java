package com.dddd.qa.zybh.others;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ImageSizeCheckerAndRenamer {

    //大于3MB的图片重命名
    public static void main(String[] args) {
        String folderPath = "/Users/zhangshichao/Downloads/1/";
        checkAndRenameLargeImages(folderPath);
        
        System.out.println("Image checking and renaming completed.");
    }

    /**
     * Checks all image files in the specified folder and renames those larger than 3MB.
     *
     * @param folderPath The path of the folder to check.
     */
    public static void checkAndRenameLargeImages(String folderPath) {
        Path folder = Paths.get(folderPath);
        
        try {
            Files.walk(folder)
                 .filter(Files::isRegularFile)
                 .filter(ImageSizeCheckerAndRenamer::isImageFile)
                 .forEach(file -> {
                     long fileSize = getFileSize(file);
                     if (fileSize < 3 * 1024 * 1024) { // 3 MB
                         renameLargeImage(file);
                     }
                 });
        } catch (IOException e) {
            System.err.println("Failed to walk through the directory: " + e.getMessage());
        }
    }

    /**
     * Checks if the file is an image based on its extension.
     *
     * @param file The file to check.
     * @return true if the file is an image, false otherwise.
     */
    private static boolean isImageFile(Path file) {
        String fileName = file.getFileName().toString();
        String extension = fileName.substring(fileName.lastIndexOf('.') + 1).toLowerCase();
        return extension.equals("svg");
    }

    /**
     * Gets the size of the file.
     *
     * @param file The file to get the size of.
     * @return The size of the file in bytes.
     */
    private static long getFileSize(Path file) {
        try {
            return Files.size(file);
        } catch (IOException e) {
            System.err.println("Failed to get file size: " + e.getMessage());
            return -1; // Return -1 on failure
        }
    }

    /**
     * Renames the image file by appending "_greaterThan3MB" to its name.
     *
     * @param file The file to rename.
     */
    private static void renameLargeImage(Path file) {
        Path parent = file.getParent();
        String fileName = file.getFileName().toString();
        String newName = fileName.substring(0, fileName.length() - 4) + ".png";
        Path newFilePath = parent.resolve(newName);

        try {
            Files.move(file, newFilePath);
            System.out.println("Image renamed: " + fileName + " -> " + newName);
        } catch (IOException e) {
            System.err.println("Failed to rename image: " + e.getMessage());
        }
    }
}