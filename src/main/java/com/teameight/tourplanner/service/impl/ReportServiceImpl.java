package com.teameight.tourplanner.service.impl;

import com.teameight.tourplanner.events.EventManager;
import com.teameight.tourplanner.events.Events;
import com.teameight.tourplanner.model.Tour;
import com.teameight.tourplanner.model.TourLog;
import com.teameight.tourplanner.service.ReportService;
import com.teameight.tourplanner.service.TourLogService;
import javafx.collections.ObservableList;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.pdmodel.PDPage;
import org.apache.pdfbox.pdmodel.PDPageContentStream;
import org.apache.pdfbox.pdmodel.common.PDRectangle;
import org.apache.pdfbox.pdmodel.font.PDType1Font;
import org.apache.pdfbox.pdmodel.font.Standard14Fonts.FontName;
import org.apache.pdfbox.pdmodel.graphics.image.PDImageXObject;

import java.io.File;
import java.io.IOException;
import java.time.format.DateTimeFormatter;
import java.util.List;

public class ReportServiceImpl implements ReportService {
    private static final Logger logger = LogManager.getLogger(ReportServiceImpl.class);

    private final TourLogService tourLogService;
    private final EventManager eventManager;

    public ReportServiceImpl(TourLogService tourLogService, EventManager eventManager) {
        this.tourLogService = tourLogService;
        this.eventManager = eventManager;
    }

    @Override
    public boolean generateTourReport(Tour tour, File outputFile) {
        try (PDDocument document = new PDDocument()) {
            // Create a new page
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            float yPosition = page.getMediaBox().getHeight() - 50;

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Add report title
                contentStream.beginText();
                contentStream.setFont(new PDType1Font(FontName.HELVETICA_BOLD), 16);
                contentStream.newLineAtOffset(50, yPosition);
                contentStream.showText("Tour Report: " + tour.getName());
                contentStream.endText();

                yPosition -= 15 * 2;

                // Add tour details section header
                contentStream.beginText();
                contentStream.setFont(new PDType1Font(FontName.HELVETICA_BOLD), 12);
                contentStream.newLineAtOffset(50, yPosition);
                contentStream.showText("Tour Details");
                contentStream.endText();

                yPosition -= 15;

                // Add tour information
                try {
                    addText(contentStream, "Description: " + tour.getDescription(), 50, yPosition);
                    yPosition -= 15;

                    addText(contentStream, "From: " + tour.getOrigin() + " To: " + tour.getDestination(), 50, yPosition);
                    yPosition -= 15;

                    addText(contentStream, "Transport Type: " + tour.getTransportType(), 50, yPosition);
                    yPosition -= 15;

                    addText(contentStream, "Distance: " + tour.getDistance(), 50, yPosition);
                    yPosition -= 15;

                    addText(contentStream, "Estimated Time: " + tour.getEstimatedTime(), 50, yPosition);
                    yPosition -= 15 * 2;
                } catch (IOException e) {
                    logger.error("Error adding text to report", e);
                }

                // Request map export and then add the map image
                try {
                    // Trigger map export by publishing event
                    eventManager.publish(Events.MAP_EXPORT_CLICKED, tour.getId());

                    // Wait a moment for the map to export
                    Thread.sleep(500);

                    // Try to load the exported map
                    String mapImagePath = "tour_map.png";
                    File mapFile = new File(mapImagePath);
                    if (mapFile.exists()) {
                        PDImageXObject image = PDImageXObject.createFromFile(mapImagePath, document);
                        float imageWidth = 200;
                        float imageHeight = imageWidth * image.getHeight() / image.getWidth();
                        contentStream.drawImage(image, 50, yPosition - imageHeight, imageWidth, imageHeight);
                        yPosition -= (imageHeight + 15);
                    } else {
                        logger.warn("Map image not found after export: " + mapFile.getAbsolutePath());
                    }
                } catch (Exception e) {
                    logger.error("Error adding map image to report", e);
                }

                yPosition -= 15;

                // Add tour logs section header
                try {
                    contentStream.beginText();
                    contentStream.setFont(new PDType1Font(FontName.HELVETICA_BOLD), 12);
                    contentStream.newLineAtOffset(50, yPosition);
                    contentStream.showText("Tour Logs");
                    contentStream.endText();

                    yPosition -= 15;
                } catch (IOException e) {
                    logger.error("Error adding logs section header", e);
                }

                // Get all logs for this tour
                ObservableList<TourLog> logs = tourLogService.getLogsForTour(tour.getId());

                if (logs.isEmpty()) {
                    try {
                        addText(contentStream, "No logs available for this tour.", 50, yPosition);
                    } catch (IOException e) {
                        logger.error("Error adding no logs message", e);
                    }
                } else {
                    // Add each log entry
                    DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

                    for (TourLog log : logs) {
                        // If we're running out of space, create a new page
                        if (yPosition < 50 + 100) {
                            try {
                                contentStream.close();

                                // Create a new page
                                page = new PDPage(PDRectangle.A4);
                                document.addPage(page);
                                yPosition = page.getMediaBox().getHeight() - 50;

                                try (PDPageContentStream newContentStream = new PDPageContentStream(document, page)) {
                                    // Continue adding logs on the new page
                                    addLogEntry(newContentStream, log, formatter, 50, yPosition);
                                    yPosition -= 15 * 7;
                                }
                            } catch (IOException e) {
                                logger.error("Error creating new page for logs", e);
                            }
                        } else {
                            try {
                                // Add log to the current page
                                addLogEntry(contentStream, log, formatter, 50, yPosition);
                                yPosition -= 15 * 7;
                            } catch (IOException e) {
                                logger.error("Error adding log entry", e);
                            }
                        }
                    }
                }
            }

            document.save(outputFile);
            logger.info("Tour report generated successfully");
            return true;

        } catch (IOException e) {
            logger.error("Error generating tour report", e);
            return false;
        }
    }

    private void addLogEntry(PDPageContentStream contentStream, TourLog log, DateTimeFormatter formatter, float x, float y) throws IOException {
        float currentY = y;

        addText(contentStream, "Date: " + log.getDateTime().format(formatter), x, currentY);
        currentY -= 15;

        addText(contentStream, "Difficulty: " + log.getDifficulty(), x, currentY);
        currentY -= 15;

        addText(contentStream, "Distance: " + log.getDistance() + " km", x, currentY);
        currentY -= 15;

        addText(contentStream, "Total Time: " + formatTime(log.getTotalTime()), x, currentY);
        currentY -= 15;

        addText(contentStream, "Rating: " + log.getRating() + "/5", x, currentY);
        currentY -= 15;

        addText(contentStream, "Comment: " + log.getComment(), x, currentY);
    }

    @Override
    public boolean generateSummarizeReport(List<Tour> tours, File outputFile) {
        try (PDDocument document = new PDDocument()) {
            PDPage page = new PDPage(PDRectangle.A4);
            document.addPage(page);

            float yPosition = page.getMediaBox().getHeight() - 50;

            try (PDPageContentStream contentStream = new PDPageContentStream(document, page)) {
                // Add report title
                contentStream.beginText();
                contentStream.setFont(new PDType1Font(FontName.HELVETICA_BOLD), 16);
                contentStream.newLineAtOffset(10, yPosition);
                contentStream.showText("Tour Statistics Report");
                contentStream.endText();

                yPosition -= 15 * 2;

                // Add column headers
                contentStream.beginText();
                contentStream.setFont(new PDType1Font(FontName.HELVETICA_BOLD), 10);
                contentStream.newLineAtOffset(10, yPosition);
                contentStream.showText("Tour Name");
                contentStream.newLineAtOffset(120, 0);
                contentStream.showText("Avg. Distance (km)");
                contentStream.newLineAtOffset(120, 0);
                contentStream.showText("Avg. Time");
                contentStream.newLineAtOffset(100, 0);
                contentStream.showText("Avg. Rating");
                contentStream.endText();

                yPosition -= 15 * 1.5f;

                // Draw a separator line
                contentStream.moveTo(10, yPosition);
                contentStream.lineTo(page.getMediaBox().getWidth() - 10, yPosition);
                contentStream.stroke();

                yPosition -= 15;

                // Add statistics for each tour
                for (Tour tour : tours) {
                    // If we're running out of space, create a new page
                    if (yPosition < 10 + 30) {
                        try {
                            contentStream.close();

                            // Create a new page
                            page = new PDPage(PDRectangle.A4);
                            document.addPage(page);
                            yPosition = page.getMediaBox().getHeight() - 10;

                            try (PDPageContentStream newContentStream = new PDPageContentStream(document, page)) {
                                addTourStatistics(newContentStream, tour, 10, yPosition);
                                yPosition -= 15 * 1.5f;
                            }
                        } catch (IOException e) {
                            logger.error("Error creating new page for statistics", e);
                        }
                    } else {
                        try {
                            // Add statistics to the current page
                            addTourStatistics(contentStream, tour, 10, yPosition);
                            yPosition -= 15 * 1.5f;
                        } catch (IOException e) {
                            logger.error("Error adding tour statistics", e);
                        }
                    }
                }
            }

            document.save(outputFile);
            logger.info("Summary report generated successfully");
            return true;

        } catch (IOException e) {
            logger.error("Error generating summary report", e);
            return false;
        }
    }

    private void addTourStatistics(PDPageContentStream contentStream, Tour tour, float x, float y) throws IOException {
        // Get tour logs
        ObservableList<TourLog> logs = tourLogService.getLogsForTour(tour.getId());

        // Calculate averages
        double avgDistance = 0;
        int avgTime = 0;
        double avgRating = 0;

        if (!logs.isEmpty()) {
            for (TourLog log : logs) {
                avgDistance += log.getDistance();
                avgTime += log.getTotalTime();
                avgRating += log.getRating();
            }

            avgDistance /= logs.size();
            avgTime /= logs.size();
            avgRating /= logs.size();
        }

        // Add statistics row
        contentStream.beginText();
        contentStream.setFont(new PDType1Font(FontName.HELVETICA), 10);
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(tour.getName());
        contentStream.newLineAtOffset(120, 0);
        contentStream.showText(String.format("%.2f", avgDistance));
        contentStream.newLineAtOffset(120, 0);
        contentStream.showText(formatTime(avgTime));
        contentStream.newLineAtOffset(100, 0);
        contentStream.showText(String.format("%.1f/5", avgRating));
        contentStream.endText();
    }

    private void addText(PDPageContentStream contentStream, String text, float x, float y) throws IOException {
        contentStream.beginText();
        contentStream.setFont(new PDType1Font(FontName.HELVETICA), 10);
        contentStream.newLineAtOffset(x, y);
        contentStream.showText(text);
        contentStream.endText();
    }

    private String formatTime(int totalMinutes) {
        int hours = totalMinutes / 60;
        int minutes = totalMinutes % 60;
        return String.format("%d:%02d", hours, minutes);
    }
} 