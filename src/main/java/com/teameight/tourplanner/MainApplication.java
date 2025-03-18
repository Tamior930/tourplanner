package com.teameight.tourplanner;

import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.util.Locale;

public class MainApplication extends Application {

    public static void main(String[] args) {
        launch();
    }

    @Override
    public void start(Stage stage) throws Exception {

        Parent mainView = FXMLDependencyInjector.load(
                "main-view.fxml",
                Locale.ENGLISH
        );
        Scene scene = new Scene(mainView);
        stage.setTitle("Tour Planner");
        stage.setScene(scene);
        stage.show();
    }

}