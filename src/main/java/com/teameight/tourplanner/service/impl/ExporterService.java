package com.teameight.tourplanner.service.impl;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

public class ExporterService {

    private static final Logger LOGGER = LogManager.getLogger(ExporterService.class);

    public void export(BufferedImage bufferedImage, String filename) {
        File file = new File("%s.png".formatted(filename));
        try {
            ImageIO.write(bufferedImage, "png", file);
            LOGGER.info("Image saved to " + file.getAbsolutePath());
        } catch (Exception e) {
            LOGGER.error("Error exporting map", e);
        }
    }
}