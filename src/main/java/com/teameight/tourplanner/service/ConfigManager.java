package com.teameight.tourplanner.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.IOException;
import java.util.Properties;

public class ConfigManager {

    private static final Logger logger = LogManager.getLogger(ConfigManager.class);
    private final Properties properties;

    public ConfigManager() {
        logger.info("Initializing ConfigManager");
        this.properties = new Properties();

        try {
            logger.debug("Loading configuration from config.properties");
            this.properties.load(
                    getClass().getClassLoader().getResourceAsStream(
                            "config.properties"
                    )
            );
            logger.info("Configuration loaded successfully");
        } catch (IOException e) {
            logger.error("Failed to load configuration file", e);
            throw new RuntimeException(e);
        }
    }

    public String get(String key) {
        logger.debug("Getting configuration value for key: {}", key);
        return this.properties.getProperty(key);
    }
}