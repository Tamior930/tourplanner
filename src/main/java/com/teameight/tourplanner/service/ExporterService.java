package com.teameight.tourplanner.service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

/**
 * Service for exporting images and other data
 */
public class ExporterService {

    /**
     * Export a BufferedImage to a PNG file
     * @param bufferedImage The image to export
     * @param filename The filename (without extension)
     * @return true if export was successful, false otherwise
     */
    public boolean exportImage(BufferedImage bufferedImage, String filename) {
        if (bufferedImage == null || filename == null || filename.trim().isEmpty()) {
            return false;
        }
        
        try {
            File file = new File(filename + ".png");
            ImageIO.write(bufferedImage, "png", file);
            System.out.println("Image exported to: " + file.getAbsolutePath());
            return true;
        } catch (IOException e) {
            System.err.println("Error exporting image: " + e.getMessage());
            e.printStackTrace();
            return false;
        }
    }
} 