 package com.teameight.tourplanner.ui;

import javafx.fxml.FXMLLoader;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 * Helper class for loading FXML files with dependency injection
 */
public class FXMLDependencyInjector {
    
    /**
     * Creates an FXMLLoader for the specified FXML file with resource bundle
     * 
     * @param fxmlPath Path to the FXML file relative to the resources folder
     * @param locale Locale for internationalization
     * @return Configured FXMLLoader instance
     */
    public static FXMLLoader loader(String fxmlPath, Locale locale) {
        ResourceBundle bundle = ResourceBundle.getBundle("com.teameight.tourplanner.i18n", locale);
        return new FXMLLoader(
                FXMLDependencyInjector.class.getResource("/com/teameight/tourplanner/" + fxmlPath),
                bundle
        );
    }
    
    /**
     * Creates an FXMLLoader for the specified FXML file with default locale
     * 
     * @param fxmlPath Path to the FXML file relative to the resources folder
     * @return Configured FXMLLoader instance
     */
    public static FXMLLoader loader(String fxmlPath) {
        return loader(fxmlPath, Locale.getDefault());
    }
}