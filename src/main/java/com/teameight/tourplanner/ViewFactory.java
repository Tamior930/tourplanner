package com.teameight.tourplanner;

import com.teameight.tourplanner.presentation.*;
import com.teameight.tourplanner.service.ExporterService;
import com.teameight.tourplanner.service.MapService;
import com.teameight.tourplanner.service.TourLogService;
import com.teameight.tourplanner.service.TourService;
import com.teameight.tourplanner.service.impl.OpenRouteServiceMapImpl;
import com.teameight.tourplanner.service.impl.TourLogServiceImpl;
import com.teameight.tourplanner.service.impl.TourServiceImpl;
import com.teameight.tourplanner.view.*;

public class ViewFactory {

    private static ViewFactory instance;

    private final TourService tourService;
    private final TourLogService tourLogService;
    private final MapService mapService;
    private final ExporterService exporterService;

    private final MainViewModel mainViewModel;
    private final NavbarViewModel navbarViewModel;
    private final SearchViewModel searchViewModel;
    private final TourListViewModel tourListViewModel;
    private final TourDetailsViewModel tourDetailsViewModel;
    private final TourFormViewModel tourFormViewModel;
    private final TourLogViewModel tourLogViewModel;
    private final MapViewModel mapViewModel;

    private ViewFactory() {

        tourService = new TourServiceImpl();
        tourLogService = new TourLogServiceImpl();
        mapService = new OpenRouteServiceMapImpl();
        exporterService = new ExporterService();

        mainViewModel = new MainViewModel();
        searchViewModel = new SearchViewModel(tourService);
        tourListViewModel = new TourListViewModel(searchViewModel, tourService);
        tourDetailsViewModel = new TourDetailsViewModel();
        tourFormViewModel = new TourFormViewModel(tourService);
        navbarViewModel = new NavbarViewModel(tourService);
        tourLogViewModel = new TourLogViewModel(tourLogService);
        mapViewModel = new MapViewModel(mapService, exporterService);
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
        } else if (viewClass == MapView.class) {
            return new MapView(mapViewModel);
        }

        throw new IllegalArgumentException("Unknown view class: " + viewClass.getName());
    }
}