package com.teameight.tourplanner;

import com.teameight.tourplanner.events.EventManager;
import com.teameight.tourplanner.presentation.*;
import com.teameight.tourplanner.service.*;
import com.teameight.tourplanner.service.impl.*;
import com.teameight.tourplanner.view.*;

public class ViewFactory {

    private static ViewFactory instance;

    private final ConfigManager configManager;
    private final EventManager eventManager;
    private final TourService tourService;
    private final TourLogService tourLogService;
    private final MapService mapService;
    private final ExporterService exporterService;
    private final TourImportExportService tourImportExportService;
    private final ReportService reportService;

    private final MainViewModel mainViewModel;
    private final NavbarViewModel navbarViewModel;
    private final SearchViewModel searchViewModel;
    private final TourListViewModel tourListViewModel;
    private final TourDetailsViewModel tourDetailsViewModel;
    private final TourFormViewModel tourFormViewModel;
    private final TourLogFormViewModel tourLogFormViewModel;
    private final TourLogViewModel tourLogViewModel;
    private final MapViewModel mapViewModel;

    private ViewFactory() {
        configManager = new ConfigManager();
        eventManager = new EventManager();
        tourService = new TourServiceImpl();
        tourLogService = new TourLogServiceImpl();
        mapService = new OpenRouteServiceApi(configManager);
        exporterService = new ExporterService();
        tourImportExportService = new TourImportExportServiceImpl();
        reportService = new ReportServiceImpl(tourLogService, eventManager);

        mainViewModel = new MainViewModel();
        searchViewModel = new SearchViewModel(tourService, eventManager);
        tourListViewModel = new TourListViewModel(searchViewModel, tourService, eventManager);
        tourDetailsViewModel = new TourDetailsViewModel(eventManager, tourService);
        tourFormViewModel = new TourFormViewModel(tourService, eventManager);
        navbarViewModel = new NavbarViewModel(tourService, eventManager, tourImportExportService, reportService);
        tourLogFormViewModel = new TourLogFormViewModel(tourLogService, eventManager);
        tourLogViewModel = new TourLogViewModel(tourLogService, tourService, eventManager, tourLogFormViewModel);
        mapViewModel = new MapViewModel(eventManager, tourService, mapService, exporterService);
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
        } else if (viewClass == MapView.class) {
            return new MapView(mapViewModel);
        }

        throw new IllegalArgumentException("Unknown view class: " + viewClass.getName());
    }
}