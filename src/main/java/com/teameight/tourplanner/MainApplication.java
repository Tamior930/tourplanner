package com.teameight.tourplanner;

import com.teameight.tourplanner.ui.ViewFactory;
import javafx.application.Application;
import javafx.stage.Stage;

public class MainApplication extends Application {
    
    public static void main(String[] args) {
        launch(args);
    }
    
    @Override
    public void start(Stage primaryStage) {
        // Create ViewFactory and show main window
        ViewFactory viewFactory = new ViewFactory();
        viewFactory.showMainWindow();
    }
}