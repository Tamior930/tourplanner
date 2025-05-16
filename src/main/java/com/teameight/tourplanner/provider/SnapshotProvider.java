package com.teameight.tourplanner.provider;

import javafx.scene.image.WritableImage;

import java.util.function.Consumer;

public interface SnapshotProvider {
    void requestSnapshot(Consumer<WritableImage> callback);
} 