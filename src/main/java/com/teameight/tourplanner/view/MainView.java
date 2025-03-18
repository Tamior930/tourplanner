package com.teameight.tourplanner.view;

import com.teameight.tourplanner.ViewFactory;
import com.teameight.tourplanner.presentation.MainViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.StackPane;

import java.net.URL;
import java.util.ResourceBundle;

public class MainView implements Initializable {

    private final MainViewModel viewModel;

    @FXML
    private BorderPane mainLayout;

    @FXML
    private StackPane contentArea;

    private TourFormView tourFormView;

    public MainView(MainViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        tourFormView = (TourFormView) ViewFactory.getInstance().create(TourFormView.class);

        contentArea.getChildren().add(tourFormView.getRootContainer());

        tourFormView.getRootContainer().visibleProperty().bind(viewModel.tourFormVisibleProperty());
        mainLayout.centerProperty().get().visibleProperty().bind(viewModel.contentVisibleProperty());

        viewModel.initialize();
    }
}
