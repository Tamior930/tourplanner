module com.teameight.tourplanner {
    requires javafx.controls;
    requires javafx.fxml;
    requires javafx.base; // Das ist Property-Bindung


    opens com.teameight.tourplanner to javafx.fxml;
    exports com.teameight.tourplanner;
    exports com.teameight.tourplanner.ui.screens;
    opens com.teameight.tourplanner.ui.screens to javafx.fxml;
    exports com.teameight.tourplanner.presentation;
    opens com.teameight.tourplanner.presentation to javafx.fxml;
}