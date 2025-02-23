module com.teameight.tourplanner {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.team8.tourplanner to javafx.fxml;
    exports com.team8.tourplanner;
    exports com.team8.tourplanner.ui.screens;
    opens com.team8.tourplanner.ui.screens to javafx.fxml;
}