package com.teameight.tourplanner.service;

import com.teameight.tourplanner.model.Difficulty;
import com.teameight.tourplanner.model.TourLog;
import com.teameight.tourplanner.repository.TourLogRepository;
import com.teameight.tourplanner.service.impl.TourLogServiceImpl;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TourLogServiceTest {

    @Mock
    private TourLogRepository tourLogRepository;

    private TourLogService tourLogService;

    private TourLog testLog1;
    private TourLog testLog2;

    @BeforeEach
    void setUp() {
        tourLogService = new TourLogServiceImpl(tourLogRepository);
        testLog1 = new TourLog("logId1", "tourId1", LocalDateTime.now(), "Comment 1", Difficulty.EASY, 10.5, 60, 4);
        testLog2 = new TourLog("logId2", "tourId1", LocalDateTime.now(), "Comment 2", Difficulty.HARD, 5.2, 30, 5);
    }

    @Test
    void getLogsForTour_whenNotCached_shouldFetchFromRepository() {
        when(tourLogRepository.findByTourId("tourId1")).thenReturn(List.of(testLog1, testLog2));

        ObservableList<TourLog> logs = tourLogService.getLogsForTour("tourId1");

        assertEquals(2, logs.size());
        verify(tourLogRepository, times(1)).findByTourId("tourId1");
    }

    @Test
    void getLogsForTour_whenCached_shouldNotFetchFromRepository() {
        when(tourLogRepository.findByTourId("tourId1")).thenReturn(List.of(testLog1));
        tourLogService.getLogsForTour("tourId1"); // First call to cache it

        // Second call, should not use repository
        ObservableList<TourLog> logs = tourLogService.getLogsForTour("tourId1");

        assertEquals(1, logs.size());
        verify(tourLogRepository, times(1)).findByTourId("tourId1");
    }

    @Test
    void addLog_shouldSaveAndAddToCache() {
        when(tourLogRepository.save(any(TourLog.class))).thenReturn(testLog1);
        tourLogService.getLogsForTour("tourId1"); // pre-cache

        TourLog addedLog = tourLogService.addLog(testLog1);

        assertNotNull(addedLog);
        verify(tourLogRepository, times(1)).save(testLog1);
        assertTrue(tourLogService.getLogsForTour("tourId1").contains(testLog1));
    }

    @Test
    void updateLog_shouldUpdateInRepositoryAndCache() {
        when(tourLogRepository.save(any(TourLog.class))).thenReturn(testLog1);
        when(tourLogRepository.findByTourId("tourId1")).thenReturn(List.of(testLog1));
        tourLogService.getLogsForTour("tourId1"); // pre-cache

        testLog1.setComment("Updated Comment");
        boolean result = tourLogService.updateLog(testLog1);

        assertTrue(result);
        verify(tourLogRepository, times(1)).save(testLog1);
        assertEquals("Updated Comment", tourLogService.getLogsForTour("tourId1").get(0).getComment());
    }

    @Test
    void deleteLog_shouldDeleteFromRepositoryAndCache() {
        when(tourLogRepository.findByTourId("tourId1")).thenReturn(new java.util.ArrayList<>(List.of(testLog1)));
        tourLogService.getLogsForTour("tourId1"); // pre-cache

        boolean result = tourLogService.deleteLog(testLog1);

        assertTrue(result);
        verify(tourLogRepository, times(1)).delete(testLog1);
        assertTrue(tourLogService.getLogsForTour("tourId1").isEmpty());
    }
}