package com.teameight.tourplanner.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.type.TypeFactory;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.teameight.tourplanner.model.Tour;
import com.teameight.tourplanner.model.TourLog;
import com.teameight.tourplanner.repository.TourLogRepository;
import com.teameight.tourplanner.repository.TourLogRepositoryOrm;
import com.teameight.tourplanner.repository.TourRepository;
import com.teameight.tourplanner.repository.TourRepositoryOrm;
import com.teameight.tourplanner.service.TourImportExportService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class TourImportExportServiceImpl implements TourImportExportService {

    private static final Logger LOGGER = LogManager.getLogger(TourImportExportServiceImpl.class);
    private final ObjectMapper objectMapper;
    private final TourRepository tourRepository;
    private final TourLogRepository tourLogRepository;

    public TourImportExportServiceImpl() {
        this.objectMapper = new ObjectMapper();
        this.objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        this.objectMapper.registerModule(new JavaTimeModule());

        this.tourRepository = new TourRepositoryOrm();
        this.tourLogRepository = new TourLogRepositoryOrm();
    }

    @Override
    public boolean exportToursWithLogs(List<Tour> tours, File file) {
        if (tours == null || tours.isEmpty() || file == null) {
            LOGGER.error("Cannot export tours with logs: tours list is empty or file is null");
            return false;
        }

        try {
            // Create export data structure - a map with tours and logs
            Map<String, Object> exportData = new HashMap<>();
            List<Map<String, Object>> toursList = new ArrayList<>();

            // Process each tour and its logs
            for (Tour tour : tours) {
                Map<String, Object> tourData = new HashMap<>();

                // Add tour data
                tourData.put("tour", tour);

                // Add associated logs
                List<TourLog> logs = tourLogRepository.findByTourId(tour.getId());
                tourData.put("logs", logs != null ? logs : new ArrayList<TourLog>());

                toursList.add(tourData);
            }

            exportData.put("toursWithLogs", toursList);

            // Write to file
            objectMapper.writeValue(file, exportData);
            LOGGER.info("Tours with logs successfully exported to {}", file.getAbsolutePath());
            return true;
        } catch (IOException e) {
            LOGGER.error("Failed to export tours with logs to JSON file", e);
            return false;
        }
    }

    @Override
    public boolean importToursWithLogs(File file) {
        if (file == null || !file.exists()) {
            LOGGER.error("Cannot import tours with logs: file is null or does not exist");
            return false;
        }

        try {
            // Read the JSON file
            Map<String, Object> importedData = objectMapper.readValue(
                    file,
                    new TypeReference<Map<String, Object>>() {
                    }
            );

            // Process the imported data
            if (importedData != null && importedData.containsKey("toursWithLogs")) {
                List<Map<String, Object>> toursList = objectMapper.convertValue(
                        importedData.get("toursWithLogs"),
                        new TypeReference<List<Map<String, Object>>>() {
                        }
                );

                for (Map<String, Object> tourData : toursList) {
                    // Process tour
                    Tour tour = objectMapper.convertValue(tourData.get("tour"), Tour.class);
                    if (tour != null) {
                        // Save tour to database
                        tourRepository.save(tour);

                        // Process logs
                        List<TourLog> logs = objectMapper.convertValue(
                                tourData.get("logs"),
                                TypeFactory.defaultInstance().constructCollectionType(List.class, TourLog.class)
                        );

                        if (logs != null) {
                            for (TourLog log : logs) {
                                // Save log to database
                                tourLogRepository.save(log);
                            }
                        }
                    }
                }

                LOGGER.info("Tours with logs successfully imported from {}", file.getAbsolutePath());
                return true;
            } else {
                LOGGER.error("Invalid import file format, missing 'toursWithLogs' key");
                return false;
            }
        } catch (IOException e) {
            LOGGER.error("Failed to import tours with logs from JSON file", e);
            return false;
        }
    }
} 