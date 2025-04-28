package com.teameight.tourplanner.service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

public class ExporterService {

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