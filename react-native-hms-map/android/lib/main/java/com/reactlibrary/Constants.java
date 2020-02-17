package com.reactlibrary;

public class Constants {
    public static final boolean ENABLE_COMPASS = true;
    public static final boolean ENABLE_ZOOM_CONTROLS = true;

    public static final String EVENT_MARKER_PRESS = "onMarkerPress";
    public static final String EVENT_MARKER_LONG_PRESS = "onMarkerLongPress";
    public static final String EVENT_MAP_READY = "onMapReady";
    public static final String EVENT_MAP_PRESS = "onMapPress";
    public static final String EVENT_MAP_LONG_PRESS = "onMapLongPress";

    public static final String[] EVENTS = {
            EVENT_MAP_READY,
            EVENT_MAP_LONG_PRESS,
            EVENT_MARKER_LONG_PRESS,
            EVENT_MARKER_PRESS,
            EVENT_MAP_PRESS
    };

    public static final int MARKER_WIDTH = 75, MARKER_HEIGHT = 120;

    public static final String COMMAND_CLEAR_ID = "clear";
    public static final int COMMAND_CLEAR = 0;

    public static final String COMMAND_ANIMATE_CAMERA_TO_COORDINATE_ID = "animateCameraToCoordinate";
    public static final int COMMAND_ANIMATE_CAMERA_TO_COORDINATE = 1;

    public static final String COMMAND_ANIMATE_CAMERA_TO_REGION_ID = "animateCameraToRegion";
    public static final int COMMAND_ANIMATE_CAMERA_TO_REGION = 2;

    public static final String COMMAND_ANIMATE_MARKER_TO_COORDINATE_ID = "animateMarkerToCoordinate";
    public static final int COMMAND_ANIMATE_MARKER_TO_COORDINATE = 3;

    public static final String MARKER_ID = "id",
            MARKER_TITLE = "title",
            MARKER_DESCRIPTION = "description",
            LATITUDE = "latitude",
            LONGITUDE = "longitude",
            MARKER_ICON = "image";

    public static final String UI_SETTINGS_ZOOM_CONTROLS = "zoomControls";
    public static final String UI_SETTINGS_MY_LOCATION_BUTTON = "myLocationButton";
    public static final String UI_SETTINGS_MAP_TOOLBAR  = "mapToolbar";
    public static final String UI_SETTINGS_COMPASS = "compass";
    public static final String UI_SETTINGS_INDOOR_LEVEL_PICKER = "indoorLevelPicker";

    public static final int MARKER_ANIMATION_DURATION = 500;

}

