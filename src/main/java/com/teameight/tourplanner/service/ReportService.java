package com.teameight.tourplanner.service;

import com.teameight.tourplanner.model.Tour;

import java.io.File;
import java.util.List;

public interface ReportService {

    // Generate a report for a single tour
    boolean generateTourReport(Tour tour, File outputFile);

    // Generate a report for multiple tours
    boolean generateSummarizeReport(List<Tour> tours, File outputFile);
} 