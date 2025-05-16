package com.teameight.tourplanner.view;

import com.teameight.tourplanner.presentation.MapViewModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.image.WritableImage;
import javafx.scene.web.WebView;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.net.URL;
import java.util.ResourceBundle;
import java.util.function.Consumer;

public class MapView implements Initializable {

    private static final Logger LOGGER = LogManager.getLogger(MapView.class);

    private final MapViewModel viewModel;

    @FXML
    private WebView webViewMap;

    public MapView(MapViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        this.viewModel.setWebEngine(webViewMap.getEngine());
        this.viewModel.setSnapshotProvider(this::onSnapShot);
        this.viewModel.init();

        LOGGER.info("Map View initialized");
    }

    private void onSnapShot(Consumer<WritableImage> writableImageConsumer) {
        writableImageConsumer.accept(webViewMap.snapshot(null, null));
    }
} 