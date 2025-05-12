package com.teameight.tourplanner;

import com.teameight.tourplanner.events.EventManager;
import com.teameight.tourplanner.presentation.*;
import com.teameight.tourplanner.service.TourLogService;
import com.teameight.tourplanner.service.TourService;
import com.teameight.tourplanner.service.impl.TourLogServiceImpl;
import com.teameight.tourplanner.service.impl.TourServiceImpl;
import com.teameight.tourplanner.view.*;

public class ViewFactory {

    private static ViewFactory instance;

    private final TourService tourService;
    private final TourLogService tourLogService;
    private final EventManager eventManager;

    private final MainViewModel mainViewModel;
    private final NavbarViewModel navbarViewModel;
    private final SearchViewModel searchViewModel;
    private final TourListViewModel tourListViewModel;
    private final TourDetailsViewModel tourDetailsViewModel;
    private final TourFormViewModel tourFormViewModel;
    private final TourLogViewModel tourLogViewModel;
    private final TourLogFormViewModel tourLogFormViewModel;

    private ViewFactory() {
        eventManager = new EventManager();
        tourService = new TourServiceImpl();
        tourLogService = new TourLogServiceImpl();
        mainViewModel = new MainViewModel();
        searchViewModel = new SearchViewModel(tourService, eventManager);
        tourListViewModel = new TourListViewModel(searchViewModel, tourService, eventManager);
        tourDetailsViewModel = new TourDetailsViewModel(eventManager, tourService);
        tourFormViewModel = new TourFormViewModel(tourService, eventManager);
        navbarViewModel = new NavbarViewModel(tourService, eventManager);
        tourLogViewModel = new TourLogViewModel(tourLogService, tourService, eventManager);
        tourLogFormViewModel = new TourLogFormViewModel(tourLogService, eventManager);
    }

    public static ViewFactory getInstance() {
        if (instance == null) {
            instance = new ViewFactory();
        }
        return instance;
    }

    public Object create(Class<?> viewClass) {
        if (viewClass == MainView.class) {
            return new MainView(mainViewModel);
        } else if (viewClass == NavbarView.class) {
            return new NavbarView(navbarViewModel);
        } else if (viewClass == SearchView.class) {
            return new SearchView(searchViewModel);
        } else if (viewClass == TourListView.class) {
            return new TourListView(tourListViewModel);
        } else if (viewClass == TourDetailsView.class) {
            return new TourDetailsView(tourDetailsViewModel);
        } else if (viewClass == TourFormView.class) {
            return new TourFormView(tourFormViewModel);
        } else if (viewClass == TourLogView.class) {
            return new TourLogView(tourLogViewModel);
        } else if (viewClass == TourLogFormView.class) {
            return new TourLogFormView(tourLogFormViewModel);
        }

        throw new IllegalArgumentException("Unknown view class: " + viewClass.getName());
    }

    // Getter for TourLogFormViewModel to use when opening the window
    public TourLogFormViewModel getTourLogFormViewModel() {
        return tourLogFormViewModel;
    }
}