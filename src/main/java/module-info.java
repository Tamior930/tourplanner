module com.teameight.tourplanner {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.teameight.tourplanner to javafx.fxml;
    exports com.teameight.tourplanner;
    exports com.teameight.tourplanner.ui.screens;
    opens com.teameight.tourplanner.ui.screens to javafx.fxml;
}