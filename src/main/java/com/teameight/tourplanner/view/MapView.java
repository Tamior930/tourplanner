package com.teameight.tourplanner.view;

import com.teameight.tourplanner.presentation.MapViewModel;
import com.teameight.tourplanner.provider.SnapshotProvider;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.WritableImage;
import javafx.scene.layout.AnchorPane;
import javafx.scene.web.WebView;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class MapView implements Initializable, SnapshotProvider {

    private final MapViewModel viewModel;

    @FXML
    private AnchorPane mapContainer;

    @FXML
    private WebView webViewMap;

    public MapView(MapViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Connect the WebView to the viewModel
        viewModel.setWebEngine(webViewMap.getEngine());
        
        // Register this view as the snapshot provider
        viewModel.setSnapshotProvider(this);
        
        // Initialize the map
        viewModel.initialize();
        
        // Make sure WebView resizes with parent
        AnchorPane.setTopAnchor(webViewMap, 0.0);
        AnchorPane.setRightAnchor(webViewMap, 0.0);
        AnchorPane.setBottomAnchor(webViewMap, 0.0);
        AnchorPane.setLeftAnchor(webViewMap, 0.0);
    }

    @Override
    public void requestSnapshot(Consumer<WritableImage> callback) {
        // Create a snapshot of the WebView and pass it to the callback
        WritableImage image = webViewMap.snapshot(null, null);
        callback.accept(image);
    }
} 