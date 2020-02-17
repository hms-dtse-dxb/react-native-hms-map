/**
 * represent a point in the map with two values, in other word a location, coordinate  or a position
 */
export declare interface Coordinate {
  /**
   * real number represents latitude
   *  ex:  36.23
   */
  latitude: number;
  /**
   * real number represents latitude
   * longitude, ex: -55.3234
   */
  longitude: number;
}

/**
 * represent a marker (An graphic element shaped like a heart (♥) or a pin (⚲) thats supposed to be drawn on the map, duh!)
 */
export declare interface Marker {
  /**
   * unique number represent this markers
   * required for some functions like: {@link animateCameraToCoordinate(latitude,longitude,zoom) }
   * ex: 234
   */
  id: number;
  /**
   * location of the marker on the map,
   * ex: {
   *    latitude:34.3,
   *    longitude:-14.4432
   * }
   */
  location: Coordinate;
  /**
   * title of the marker
   * ex: Korean restaurant
   */
  title?: string;
  /**
   * description of the marker
   * ex: we have wide range of authentic korean food
   */
  description?: string;
  /**
//a url of the icon(image) that will be drawn on the map as the marker (overrides the default image)
 * 
 */
  image?: string;
}

/**
 * represent the options that the maps needs to set the initial position of the camera
 */
export declare interface CameraOptions {
  /**
   * real number represents latitude
   *  ex:  36.23
   */
  latitude: number;
  /**
   * real number represents latitude
   * longitude, ex: -55.3234
   */
  longitude: number;
  /**
   * a number represent the zoom of the camera toward the ground,
   * must in range [1,20]
   * const ZOOM = {
   *            WORLD: 1,
   *             CONTINENT: 5,
   *             STATE: 8,
   *            CITY: 10,
   *            STREET: 15,
   *            BUILDING: 20,
   *    };
   * ex: 5
   */
  zoom: number;
}
/**
 * represent a set of flags (booleans) that will control the visibility of some of the map's UI (buttons and icons)
 */
export declare interface UiSettings {
  /**
   * Show zoom controls (buttons) on the right side of the map (view)
   */
  zoomControls?: boolean;
  /**
   * Show button on the bottom right of the map (view), which will show the user's current position when clicked (pressed)
   */
  myLocationButton?: boolean;
  /**
   * Show the compass (north direction) on the top left corner of the map (view)
   */
  compass?: boolean;
  /**
   *
   */
  mapToolbar?: boolean;
  /**
   *
   */
  indoorLevelPicker?: boolean;
}
/************** native events (java -> javascript) ***************/
/**
 * this method will be called when the map is drawn and ready to be interacted with, ex:adding markers
 */
export declare function onMapReady(): void;
/**
 * Will be called when the user presses (click/tap) on the map
 * @param coordinate the location {@link Coordinate} where the user pressed
 */
export declare function onMapPress(coordinate: Coordinate): void;
/**
 * Will be called when the user long presses (long click/long tap) on the map
 *  @param coordinate the location {@link Coordinate} where the user long pressed
 */
export declare function onMapLongPress(coordinate: Coordinate): void;
/**
 * Will be called when the user presses (click/tap) on a marker on the map
 * @param marker the marker that the users pressed
 */
export declare function onMarkerPress(marker: Marker): void;

/**************** commands (javascript -> java) ***************/

/**
 * Move the camera of the map to the specified location
 * @param latitude  latitude of the location where the camera will move
 * @param longitude  longitude of the location where the camera will move
 * @param zoom the zoom level of the camera ,must be in range [1,20]; ex:1 (world),10(city)
 */
export declare function animateCameraToCoordinate(
  latitude: number,
  longitude: number,
  zoom: number,
): void;
/**
 * Move the camera of the map to a location where all the coordinates (or markers) are in the viewport (visible part of the map)
 * @param coordinates an array of coordinates or markers that will used to calculate the location of the camera
 * @param zoom the zoom level of the camera ,must be in range [1,20]; ex:1 (world),10(city)
 */
export declare function animateCameraToRegion(
  coordinates: Coordinate[],
  zoom: number,
): void;
/**
 * Animates (moves) a marker in the map from its coordinate (location) to a new coordinate (location)
 * @param markerId the id of the marker that will be animates (moved)
 * @param latitude new latitude of the coordinate (location) where the marker will move
 * @param longitude new longitude of the coordinate (location) where the marker will move
 */
export declare function animateMarkerToCoordinate(
  markerId: number,
  latitude: number,
  longitude: number,
): void;

/************** props **************/
/**
 * Add a marker to the existing set of markers on the map
 *  @param marker the marker {@link Marker} that will be added to the map
 */
export declare function addMarker(marker: Marker): void;

/**
 * Add a list of markers to the existing set of markers on the map
 *  @param markers an array of markers {@link Marker} that will be added to the map
 */
export declare function addMarkers(markers: Marker[]): void;
/**
 * Add a list of markers to the map, after removing all the existing markers
 *  @param markers an array of markers {@see Marker}, that will be added to the map
 */
export declare function setMarkers(markers: Marker[]): void;

/**
 * Sets the default marker's image (icon) that overrides the default one (TomTom black marker)
 * the marker should be used if the add marker(s) does not have an image (@see Marker#image)
 * using large images cal impact the performance
 * @param defaultMarkerImage the url of the image(icon) that will be the default marker image (icon)
 */
export declare function setDefaultMarkerImage(defaultMarkerImage: string): void;

/**
 * Shows the user's current location (position) on the map, represented by a blue dot
 * @param value true to show the user's location, false otherwise
 */
export declare function setMyLocationEnabled(value: boolean): void;
/**
 * Automatically moves the animates (moves) the camera to the newly added marker(s)
 * @param value true to show the user's location, false otherwise
 */
export declare function setAutoUpdateCamera(value:boolean):void;
/**
 * Set the initial camera options, including coordinates (location) and zoom
 * @param cameraOptions the object holding the camera options  {@see CameraOptions}
 */
export declare function setCameraOptions(cameraOptions: CameraOptions): void;

/**
 * Control the visibility of some of the map's UI (buttons and icons)
 * @param uiSettings the object holding the UI settings (flags) {@see UiSettings}
 */
export declare function setUiSettings(uiSettings: UiSettings): void;

/**
 * Clear the map by removing all the graphical element (ex:markers)
 * in other word, reset the map to its initial state except for the camera's location
 */
export declare function clear(): void;
