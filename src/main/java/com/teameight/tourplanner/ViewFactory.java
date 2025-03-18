package com.teameight.tourplanner;

import com.teameight.tourplanner.presentation.*;
import com.teameight.tourplanner.service.TourService;
import com.teameight.tourplanner.service.impl.TourServiceImpl;
import com.teameight.tourplanner.view.*;

public class ViewFactory {

    private static ViewFactory instance;
    private final TourService tourService;
    private SearchViewModel searchViewModel;
    private TourDetailsViewModel tourDetailsViewModel;
    private TourListViewModel tourListViewModel;
    private NavbarViewModel navbarViewModel;
    private TourFormViewModel tourFormViewModel;
    private MainViewModel mainViewModel;

    private ViewFactory() {
        tourService = new TourServiceImpl();
        searchViewModel = new SearchViewModel(tourService);
        tourDetailsViewModel = new TourDetailsViewModel();
        tourListViewModel = new TourListViewModel(searchViewModel, tourService);
        navbarViewModel = new NavbarViewModel(tourService);
        tourFormViewModel = new TourFormViewModel(tourService);
        mainViewModel = new MainViewModel();
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
        }
        
        throw new IllegalArgumentException("Unknown view class: " + viewClass.getName());
    }
}