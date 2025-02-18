module com.team8.tourplanner {
    requires javafx.controls;
    requires javafx.fxml;


    opens com.team8.tourplanner to javafx.fxml;
    exports com.team8.tourplanner;
}