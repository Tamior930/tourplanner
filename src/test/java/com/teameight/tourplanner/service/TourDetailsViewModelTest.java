package com.teameight.tourplanner.presentation;

import com.teameight.tourplanner.events.EventListener;
import com.teameight.tourplanner.events.EventManager;
import com.teameight.tourplanner.events.Events;
import com.teameight.tourplanner.model.Tour;
import com.teameight.tourplanner.model.TransportType;
import com.teameight.tourplanner.service.TourService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TourDetailsViewModelTest {

    @Mock
    private TourService tourService;
    @Mock
    private EventManager eventManager;

    private TourDetailsViewModel viewModel;
    private EventListener tourSelectedListener;

    @BeforeEach
    void setUp() {
        viewModel = new TourDetailsViewModel(eventManager, tourService);

        // Capture the listener to simulate events
        ArgumentCaptor<EventListener> captor = ArgumentCaptor.forClass(EventListener.class);
        verify(eventManager).subscribe(eq(Events.TOUR_SELECTED), captor.capture());
        tourSelectedListener = captor.getValue();
    }

    @Test
    void testInitialState_isEmptyAndNotSelected() {
        assertEquals("", viewModel.tourNameProperty().get());
        assertEquals("", viewModel.tourDescriptionProperty().get());
        assertFalse(viewModel.tourSelectedProperty().get());
    }

    @Test
    void testTourSelectedEvent_withValidId_updatesProperties() {
        Tour tour = new Tour("tour1", "Danube Biking", "A scenic route.", "Passau", "Vienna", TransportType.BIKE, "300 km", "20h");
        when(tourService.getTourById("tour1")).thenReturn(tour);

        // Simulate event
        tourSelectedListener.event("tour1");

        assertTrue(viewModel.tourSelectedProperty().get());
        assertEquals("Danube Biking", viewModel.tourNameProperty().get());
        assertEquals("A scenic route.", viewModel.tourDescriptionProperty().get());
        assertEquals("Passau", viewModel.tourOriginProperty().get());
        assertEquals("Vienna", viewModel.tourDestinationProperty().get());
        assertEquals("Bike", viewModel.tourTransportTypeProperty().get());
    }

    @Test
    void testTourSelectedEvent_withNullId_clearsProperties() {
        // First, select a tour to ensure properties are populated
        Tour tour = new Tour("tour1", "Danube Biking", "A scenic route.", "Passau", "Vienna", TransportType.BIKE, "300 km", "20h");
        when(tourService.getTourById("tour1")).thenReturn(tour);
        tourSelectedListener.event("tour1");
        assertTrue(viewModel.tourSelectedProperty().get());

        // Now, simulate deselecting (null event)
        tourSelectedListener.event(null);

        assertFalse(viewModel.tourSelectedProperty().get());
        assertEquals("", viewModel.tourNameProperty().get());
        assertEquals("", viewModel.tourDescriptionProperty().get());
    }

    @Test
    void testTourSelectedEvent_withInvalidId_clearsProperties() {
        when(tourService.getTourById("invalidId")).thenReturn(null);

        // Simulate event with an ID that won't be found
        tourSelectedListener.event("invalidId");

        assertFalse(viewModel.tourSelectedProperty().get());
        assertEquals("", viewModel.tourNameProperty().get());
    }
}