package com.teameight.tourplanner.service;

import com.teameight.tourplanner.model.Tour;
import com.teameight.tourplanner.model.TransportType;
import com.teameight.tourplanner.repository.TourRepository;
import com.teameight.tourplanner.service.impl.TourServiceImpl;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TourServiceTest {

    @Mock
    private TourRepository tourRepository;

    private TourService tourService;

    private Tour testTour1;
    private Tour testTour2;

    @BeforeEach
    public void setUp() {
        // Mock initial state of the repository
        testTour1 = new Tour("id1", "Vienna City Tour", "A tour of Vienna", "Vienna", "Vienna", TransportType.FOOT_WALKING, "10km", "3h");
        testTour2 = new Tour("id2", "Bike Trip Graz", "A trip through Graz", "Graz", "Graz", TransportType.BIKE, "25km", "2h");
        List<Tour> initialTours = new ArrayList<>(List.of(testTour1, testTour2));
        when(tourRepository.findAll()).thenReturn(initialTours);

        // Inject mock into service
        tourService = new TourServiceImpl(tourRepository);
    }

    @Test
    void testAddTour_shouldSaveAndReturnTour() {
        when(tourRepository.save(any(Tour.class))).thenReturn(testTour1);
        Tour addedTour = tourService.addTour(testTour1);

        assertNotNull(addedTour);
        assertEquals("Vienna City Tour", addedTour.getName());
        verify(tourRepository, times(1)).save(testTour1);
        assertTrue(tourService.getAllTours().contains(testTour1));
    }

    @Test
    void testAddTour_withNullTour_shouldReturnNull() {
        Tour addedTour = tourService.addTour(null);
        assertNull(addedTour);
        verify(tourRepository, never()).save(any());
    }

    @Test
    void testUpdateTour_existingTour_shouldUpdateAndReturnTrue() {
        when(tourRepository.save(any(Tour.class))).thenReturn(testTour1);
        testTour1.setName("Updated Vienna Tour");

        boolean result = tourService.updateTour(testTour1);

        assertTrue(result);
        verify(tourRepository, times(1)).save(testTour1);
        assertEquals("Updated Vienna Tour", tourService.getAllTours().get(0).getName());
    }

    @Test
    void testUpdateTour_nonExistingTour_shouldReturnFalse() {
        Tour nonExistentTour = new Tour("id3", "Non Existent", "desc", "A", "B", TransportType.CAR, "1", "1");
        when(tourRepository.save(nonExistentTour)).thenReturn(nonExistentTour);

        boolean result = tourService.updateTour(nonExistentTour);

        assertFalse(result); // Fails to update in the list, but is saved to repo
        verify(tourRepository, times(1)).save(nonExistentTour);
    }

    @Test
    void testDeleteTour_existingTour_shouldDeleteAndReturnTrue() {
        boolean result = tourService.deleteTour(testTour1);

        assertTrue(result);
        verify(tourRepository, times(1)).delete(testTour1);
        assertFalse(tourService.getAllTours().contains(testTour1));
    }

    @Test
    void testDeleteTour_nonExistingTour_shouldReturnFalse() {
        Tour nonExistentTour = new Tour("id3", "Non Existent", "desc", "A", "B", TransportType.CAR, "1", "1");
        boolean result = tourService.deleteTour(nonExistentTour);

        assertFalse(result);
        verify(tourRepository, times(1)).delete(nonExistentTour);
    }

    @Test
    void testGetTourById_existing_shouldReturnTour() {
        when(tourRepository.find("id1")).thenReturn(Optional.of(testTour1));
        Tour foundTour = tourService.getTourById("id1");

        assertNotNull(foundTour);
        assertEquals("id1", foundTour.getId());
        verify(tourRepository, times(1)).find("id1");
    }

    @Test
    void testGetTourById_nonExisting_shouldReturnNull() {
        when(tourRepository.find("id_nonexistent")).thenReturn(Optional.empty());
        Tour foundTour = tourService.getTourById("id_nonexistent");

        assertNull(foundTour);
        verify(tourRepository, times(1)).find("id_nonexistent");
    }

    @Test
    void testSearchTours_shouldReturnFilteredList() {
        ObservableList<Tour> results = tourService.searchTours("graz");

        assertEquals(1, results.size());
        assertEquals("id2", results.get(0).getId());
    }

    @Test
    void testSearchTours_withEmptyQuery_shouldReturnAllTours() {
        ObservableList<Tour> results = tourService.searchTours("");

        assertEquals(2, results.size());
    }

    @Test
    void testSearchTours_byDescription_shouldFindTour() {
        ObservableList<Tour> results = tourService.searchTours("trip through");

        assertEquals(1, results.size());
        assertTrue(results.contains(testTour2));
    }

    @Test
    void testRefreshTours_shouldReloadFromRepo() {
        when(tourRepository.findAll()).thenReturn(List.of(testTour1)); // Simulate a change in the DB

        tourService.refreshTours();

        verify(tourRepository, times(2)).findAll(); // Initial + refresh
        assertEquals(1, tourService.getAllTours().size());
        assertFalse(tourService.getAllTours().contains(testTour2));
    }
}