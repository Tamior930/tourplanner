package com.teameight.tourplanner.provider;

import javafx.scene.image.WritableImage;

import java.util.function.Consumer;

/**
 * Interface for components that can provide snapshots
 */
public interface SnapshotProvider {
    /**
     * Request a snapshot from the provider
     * @param callback Callback to handle the snapshot image
     */
    void requestSnapshot(Consumer<WritableImage> callback);
} 