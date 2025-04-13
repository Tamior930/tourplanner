package com.teameight.tourplanner;

import com.teameight.tourplanner.impl.TourLogServiceImpl;
import com.teameight.tourplanner.impl.TourServiceImpl;
import com.teameight.tourplanner.presentation.*;
import com.teameight.tourplanner.service.TourLogService;
import com.teameight.tourplanner.service.TourService;
import com.teameight.tourplanner.view.*;

public class ViewFactory {

    private static ViewFactory instance;

    private final TourService tourService;
    private final TourLogService tourLogService;

    private final MainViewModel mainViewModel;
    private final NavbarViewModel navbarViewModel;
    private final SearchViewModel searchViewModel;
    private final TourListViewModel tourListViewModel;
    private final TourDetailsViewModel tourDetailsViewModel;
    private final TourFormViewModel tourFormViewModel;
    private final TourLogViewModel tourLogViewModel;

    private ViewFactory() {

        tourService = new TourServiceImpl();
        tourLogService = new TourLogServiceImpl();

        mainViewModel = new MainViewModel();
        searchViewModel = new SearchViewModel(tourService);
        tourListViewModel = new TourListViewModel(searchViewModel, tourService);
        tourDetailsViewModel = new TourDetailsViewModel();
        tourFormViewModel = new TourFormViewModel(tourService);
        navbarViewModel = new NavbarViewModel(tourService);
        tourLogViewModel = new TourLogViewModel(tourLogService);
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
        }

        throw new IllegalArgumentException("Unknown view class: " + viewClass.getName());
    }
}