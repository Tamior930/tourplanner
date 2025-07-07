package com.teameight.tourplanner.presentation;

import com.teameight.tourplanner.events.EventListener;
import com.teameight.tourplanner.events.EventManager;
import com.teameight.tourplanner.events.Events;
import com.teameight.tourplanner.model.RouteInfo;
import com.teameight.tourplanner.model.Tour;
import com.teameight.tourplanner.model.TransportType;
import com.teameight.tourplanner.service.MapService;
import com.teameight.tourplanner.service.TourService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TourFormViewModelTest {

    @Mock
    private TourService tourService;
    @Mock
    private MapService mapService;
    @Mock
    private EventManager eventManager;

    private TourFormViewModel viewModel;

    private EventListener tourUpdatedListener;

    @BeforeEach
    void setUp() {
        // Capture the listener for the TOUR_UPDATED event to simulate editing
        ArgumentCaptor<EventListener> captor = ArgumentCaptor.forClass(EventListener.class);
        doNothing().when(eventManager).subscribe(eq(Events.TOUR_UPDATED), captor.capture());

        // Also, explicitly allow the subscription for the TOUR_ADDED event
        doNothing().when(eventManager).subscribe(eq(Events.TOUR_ADDED), any(EventListener.class));

        // Now, we can safely construct the ViewModel
        viewModel = new TourFormViewModel(tourService, mapService, eventManager);

        // Get the captured listener for use in tests
        tourUpdatedListener = captor.getValue();
    }

    @Test
    void testValidation_withEmptyName_isInvalid() {
        viewModel.tourNameProperty().set("");
        viewModel.tourOriginProperty().set("Vienna");
        viewModel.tourDestinationProperty().set("Graz");

        viewModel.validateForm();

        assertFalse(viewModel.formValidProperty().get());
        assertEquals("Name is required", viewModel.errorMessageProperty().get());
    }

    @Test
    void testValidation_withValidFields_isValid() {
        viewModel.tourNameProperty().set("Test Tour");
        viewModel.tourOriginProperty().set("Vienna");
        viewModel.tourDestinationProperty().set("Graz");

        viewModel.validateForm();

        assertTrue(viewModel.formValidProperty().get());
        assertEquals("", viewModel.errorMessageProperty().get());
    }

    @Test
    void testSaveNewTour_withApiError_showsErrorAndDoesNotSave() {
        when(mapService.getDirections(anyString(), anyString(), any())).thenReturn(Optional.empty());
        viewModel.tourNameProperty().set("Test Tour");
        viewModel.tourOriginProperty().set("Vienna");
        viewModel.tourDestinationProperty().set("Graz");

        boolean result = viewModel.saveTour();

        assertFalse(result);
        assertEquals("Could not find a route. Check origin/destination.", viewModel.errorMessageProperty().get());
        verify(tourService, never()).addTour(any());
    }

    @Test
    void testSaveNewTour_withSuccess_callsServiceAndPublishesEvent() {
        RouteInfo routeInfo = new RouteInfo("200 km", "2h", null);
        when(mapService.getDirections("Vienna", "Graz", TransportType.CAR)).thenReturn(Optional.of(routeInfo));

        viewModel.tourNameProperty().set("Test Tour");
        viewModel.tourOriginProperty().set("Vienna");
        viewModel.tourDestinationProperty().set("Graz");
        viewModel.tourTransportTypeProperty().set(TransportType.CAR);

        boolean result = viewModel.saveTour();

        assertTrue(result);
        verify(tourService, times(1)).addTour(any(Tour.class));
        verify(eventManager, times(1)).publish(eq(com.teameight.tourplanner.events.Events.TOUR_ADDED), any());
    }

    @Test
    void testLoadTourForEditing_populatesFieldsCorrectly() {
        Tour tour = new Tour("id1", "Old Name", "Old Desc", "A", "B", TransportType.BIKE, "10km", "1h");
        when(tourService.getTourById("id1")).thenReturn(tour);

        // Simulate the event that triggers edit mode
        tourUpdatedListener.event("id1");

        assertEquals("Old Name", viewModel.tourNameProperty().get());
        assertEquals("Old Desc", viewModel.tourDescriptionProperty().get());
        assertEquals("A", viewModel.tourOriginProperty().get());
        assertEquals("B", viewModel.tourDestinationProperty().get());
        assertEquals(TransportType.BIKE, viewModel.tourTransportTypeProperty().get());
        assertEquals("Edit Tour: Old Name", viewModel.formTitleProperty().get());
    }
}