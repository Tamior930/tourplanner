package com.teameight.tourplanner.service;

import com.teameight.tourplanner.model.Tour;

import java.io.File;
import java.util.List;

public interface TourImportExportService {

    // Export multiple tours with their logs to a file in JSON format
    boolean exportToursWithLogs(List<Tour> tours, File file);

    // Import multiple tours with their logs from a JSON file and save to database
    boolean importToursWithLogs(File file);
} 