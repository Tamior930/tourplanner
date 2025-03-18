package com.teameight.tourplanner.service;

import com.teameight.tourplanner.model.Tour;
import com.teameight.tourplanner.model.TransportType;
import com.teameight.tourplanner.service.impl.TourServiceImpl;
import javafx.collections.ObservableList;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class TourServiceTest {

    private TourService tourService;
    private Tour testTour;

    @BeforeEach
    public void setUp() {
        tourService = new TourServiceImpl();
        
        testTour = new Tour(
                "test-id-123",
                "Test Tour",
                "A test tour for unit tests",
                "Vienna",
                "Graz",
                TransportType.CAR,
                "200 km",
                "2 hours",
                null
        );
    }

    @Test
    public void testAddTour() {
        
        Tour addedTour = tourService.addTour(testTour);
        
        assertEquals(testTour, addedTour);
        

        Tour foundTour = tourService.getTourById(testTour.getId());
        assertNotNull(foundTour);
        assertEquals(testTour.getName(), foundTour.getName());
        assertEquals(testTour.getOrigin(), foundTour.getOrigin());
        assertEquals(testTour.getDestination(), foundTour.getDestination());
    }

    @Test
    public void testUpdateTour() {
        tourService.addTour(testTour);
        
        testTour.setName("Modified Test Tour");
        testTour.setDescription("Description has been changed");
        
        boolean updated = tourService.updateTour(testTour);
        
        assertTrue(updated);
        

        Tour updatedTour = tourService.getTourById(testTour.getId());
        assertEquals("Modified Test Tour", updatedTour.getName());
        assertEquals("Description has been changed", updatedTour.getDescription());
    }

    @Test
    public void testDeleteTour() {
        tourService.addTour(testTour);
        
        boolean deleted = tourService.deleteTour(testTour);
        
        assertTrue(deleted);
        
        Tour foundTour = tourService.getTourById(testTour.getId());
        assertNull(foundTour);
    }

    @Test
    public void testSearchTours() {
        tourService.addTour(testTour);
        
        ObservableList<Tour> searchResults = tourService.searchTours("Test");
        
        assertFalse(searchResults.isEmpty());
        assertTrue(searchResults.contains(testTour));
        
        ObservableList<Tour> emptyResults = tourService.searchTours("NonExistent");
        
        assertTrue(emptyResults.isEmpty());
    }
} 