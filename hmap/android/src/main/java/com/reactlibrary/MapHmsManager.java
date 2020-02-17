package com.reactlibrary;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.common.MapBuilder;
import com.facebook.react.uimanager.SimpleViewManager;
import com.facebook.react.uimanager.ThemedReactContext;
import com.facebook.react.uimanager.annotations.ReactProp;
import com.huawei.hms.maps.CameraUpdateFactory;
import com.huawei.hms.maps.MapView;

import com.huawei.hms.maps.OnMapReadyCallback;
import com.huawei.hms.maps.HuaweiMap;
import com.huawei.hms.maps.UiSettings;
import com.huawei.hms.maps.model.LatLng;
import com.huawei.hms.maps.model.BitmapDescriptor;
import com.facebook.react.bridge.ReactContext;

import com.reactlibrary.bridgeOps.Marker;

import android.os.Handler;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.arch.core.util.Function;

import java.util.HashMap;
import java.util.Map;

import static com.reactlibrary.Constants.COMMAND_ANIMATE_MARKER_TO_COORDINATE;
import static com.reactlibrary.Constants.COMMAND_ANIMATE_MARKER_TO_COORDINATE_ID;
import static com.reactlibrary.Constants.COMMAND_ANIMATE_CAMERA_TO_COORDINATE;
import static com.reactlibrary.Constants.COMMAND_ANIMATE_CAMERA_TO_COORDINATE_ID;
import static com.reactlibrary.Constants.COMMAND_ANIMATE_CAMERA_TO_REGION;
import static com.reactlibrary.Constants.COMMAND_ANIMATE_CAMERA_TO_REGION_ID;
import static com.reactlibrary.Constants.COMMAND_CLEAR;
import static com.reactlibrary.Constants.COMMAND_CLEAR_ID;
import static com.reactlibrary.Constants.EVENTS;
import static com.reactlibrary.Constants.LATITUDE;
import static com.reactlibrary.Constants.LONGITUDE;
import static com.reactlibrary.Constants.UI_SETTINGS_COMPASS;
import static com.reactlibrary.Constants.UI_SETTINGS_INDOOR_LEVEL_PICKER;
import static com.reactlibrary.Constants.UI_SETTINGS_MAP_TOOLBAR;
import static com.reactlibrary.Constants.UI_SETTINGS_MY_LOCATION_BUTTON;
import static com.reactlibrary.Constants.UI_SETTINGS_ZOOM_CONTROLS;

public class MapHmsManager extends SimpleViewManager<MapView> implements OnMapReadyCallback {

    public static final String TAG = "HMSMap";
    public static final String REACT_CLASS = "HMSMapView";

    private MapView mapView;
    private HuaweiMap huaweiMap;
    private String defaultMarkerIconUrl;
    private BitmapDescriptor defaultMarkerIcon;
    private Map<Integer, com.huawei.hms.maps.model.Marker> markers = new HashMap<>();
    public static int DEFAULT_ZOOM = 6;

    //flags
    private boolean autoUpdateCamera = false;
    //these options are sent before the map is ready, so we save them to set them when the map is ready
    private ReadableMap cameraOptions;
    private ReadableMap uiSettings;
    private ReadableArray markersProp;

    @Override
    public String getName() {
        return REACT_CLASS;
    }

    // TODO: add safety tests

    @Override
    protected MapView createViewInstance(ThemedReactContext reactContext) {
        mapView = new MapView(reactContext);
        mapView.getMapAsync(this);
        mapView.onCreate(null);
        // onMount(mapView,true);
        mapView.onStart();
        mapView.onResume();

        return mapView;
    }

    @Override
    public void onMapReady(HuaweiMap huaweiMap) {
        this.huaweiMap = huaweiMap;

        com.reactlibrary.bridgeOps.Map.onMapReady(getReactContext(), getId());
        registerEvents();
        Log.d(TAG, "Map is Ready");
        setCameraOptions(mapView, cameraOptions);
        setUiSettings(mapView, uiSettings);
        setMarkers(mapView, markersProp);
    }

    /******************************************************************
     * REACT PROPS
     *******************************************************************/


    /**
     * Add a marker to existing set of marker on the map
     *
     * @param mapView instance of the mapView
     * @param value   the marker received from javascript to be drawn on the map
     */
    @ReactProp(name = "addMarker")
    public void addMarker(MapView mapView, ReadableMap value) {
        Log.d(TAG, "addMarker" + " ,isValueNull= " + (value == null ? "true" : "false"));
        if (huaweiMap == null) {
            Log.e(TAG, "MapIsNotReady");
            return;
        }
        MapUtils.addMarker(huaweiMap, value, markers, defaultMarkerIconUrl, defaultMarkerIcon, autoUpdateCamera);

    }

    /**
     * replace the existing markers on the map with new list of markers
     *
     * @param mapView instance of the mapView
     * @param value   list of markers received from javascript to be drawn on the map
     */
    @ReactProp(name = "addMarkers")
    public void addMarkers(MapView mapView, ReadableArray value) {
        Log.d(TAG, "addMarkers " + " ,isValueNull= " + (value == null ? "true" : "false"));
        if (huaweiMap == null) {
            Log.e(TAG, "MapIsNotReady");
            return;
        }
        MapUtils.addMarkers(huaweiMap, value, markers, defaultMarkerIconUrl, defaultMarkerIcon, autoUpdateCamera);
    }


    /**
     * adds a list of markers to map, removing the existing ones if any
     *
     * @param mapView instance of the mapView
     * @param value   list of markers received from javascript to be drawn on the map
     */
    @ReactProp(name = "setMarkers")
    public void setMarkers(MapView mapView, ReadableArray value) {
        Log.d(TAG, "setMarkers " + " ,isValueNull= " + (value == null ? "true" : "false"));
        if (huaweiMap == null) {
            Log.e(TAG, "MapIsNotReady");
            return;
        }
        com.reactlibrary.bridgeOps.Map.clear(this.huaweiMap, this.markers);
        addMarkers(mapView, value);
    }


    /**
     * list of marker received from react native to be drawn on the map
     * same as {@link this#setMarkers(MapView, ReadableArray)}
     *
     * @param mapView instance of the mapView
     * @param value   list of marker to be received when the component's state in
     *                changed on the react native side
     */
    @ReactProp(name = "markers")
    public void markers(MapView mapView, ReadableArray value) {
        Log.d(TAG, "markers ,isValueNull= " + (value == null ? "true" : "false"));
        markersProp = value;
        if (huaweiMap == null || value == null) return;
        this.setMarkers(mapView, value);

    }

    /**
     * Show the user's current location on the map
     *
     * @param mapView an instance of the mapView
     * @param value   the state from javascript that tells whether to show the
     *                current location on map or not
     */
    @ReactProp(name = "myLocationEnabled")
    public void setMyLocationEnabled(MapView mapView, boolean value) {
        Log.d(TAG, "myLocationEnabled " + (value ? "true " : "false "));
        if (huaweiMap == null) {
            Log.e(TAG, "MapIsNotReady");
            return;
        }

        huaweiMap.setMyLocationEnabled(value);
    }

    /**
     * Show the user's current location on the map
     *
     * @param mapView an instance of the mapView
     * @param value   the state from javascript that tells whether to show the
     *                current location on map or not
     */
    @ReactProp(name = "autoUpdateCamera")
    public void setAnimateCameraToMarkers(MapView mapView, boolean value) {
        Log.d(TAG, "setAnimateCameraToMarkers" + (value ? "true " : "false "));
        this.autoUpdateCamera = value;
    }

    /**
     * Sets the default marker's image (icon) that overrides the default one (TomTom black marker)
     * the marker should be used if the add marker(s) does not have an image (@see Marker#image)
     * using large images cal impact the performance
     *
     * @param mapView an instance of the mapView
     * @param value   the url of the image(icon) that will be the default marker image (icon)
     */
    @ReactProp(name = "defaultMarkerImage")
    public void setDefaultMarkerImage(MapView mapView, String value) {
        System.out.println("--------------------");
        Log.d(TAG, "set defaultMarkerImage : " + value + " ,isValueNull= " + (value == null ? "true" : "false"));

        if (this.defaultMarkerIconUrl != null && this.defaultMarkerIconUrl.equals(value)) {
            Log.d(TAG, "defaultMarkerUrl is already set");
            return;
        }
        new Thread(() -> {
            try {

                this.defaultMarkerIcon = MapUtils.markerIconFromUrl(value);
                this.defaultMarkerIconUrl = value;
            } catch (Exception e) {
                Log.d(TAG, "failed to default marker icon: " + e.toString());
                e.printStackTrace();

            }
        }).start();
    }

    /**
     * Set the initial camera options, including coordinates (location) and zoom
     *
     * @param mapView instance of map view
     * @param value   cameraOptions the object holding the camera options
     */
    @ReactProp(name = "cameraOptions")
    public void setCameraOptions(MapView mapView, ReadableMap value) {
        Log.d(TAG, "set cameraOptions: " + value + " ,isValueNull= " + (value == null ? "true" : "false"));
        this.cameraOptions = value;
        if (huaweiMap == null || value == null) return;

        int zoom = value.getInt("zoom");
        double latitude = value.getDouble(LATITUDE);
        double longitude = value.getDouble(LONGITUDE);
        LatLng position = new LatLng(latitude, longitude);
        this.huaweiMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, zoom));
    }

    /**
     * Control the visibility of some of the map's UI (buttons and icons)
     *
     * @param mapView instance of map view
     * @param value   the object holding the UI settings (flags
     */
    @ReactProp(name = "uiSettings")
    public void setUiSettings(MapView mapView, ReadableMap value) {
        Log.d(TAG, "set uiSettings: " + " ,isValueNull= " + (value == null ? "true" : "false"));
        this.uiSettings = value;
        if (huaweiMap == null || value == null) return;

        UiSettings uiSettings = this.huaweiMap.getUiSettings();
        Function<String, Boolean> getFlag = name -> {
            if (value.hasKey(name)) {
                Log.d(TAG, name + " -> true ");
                return value.getBoolean(name);
            }
            Log.d(TAG, name + " -> false ");
            return false;
        };
        uiSettings.setZoomControlsEnabled(getFlag.apply(UI_SETTINGS_ZOOM_CONTROLS));
        uiSettings.setCompassEnabled(getFlag.apply(UI_SETTINGS_COMPASS));
        uiSettings.setMyLocationButtonEnabled(getFlag.apply(UI_SETTINGS_MY_LOCATION_BUTTON));
        uiSettings.setMapToolbarEnabled(getFlag.apply(UI_SETTINGS_MAP_TOOLBAR));
        uiSettings.setIndoorLevelPickerEnabled(getFlag.apply(UI_SETTINGS_INDOOR_LEVEL_PICKER));
    }

    /**************** COMMANDS *****************/

    @Nullable
    @Override
    public Map<String, Integer> getCommandsMap() {
        // You need to implement this method and return a map with the readable
        // name and constant for each of your commands. The name you specify
        // here is what you'll later use to access it in react-native.
        Map<String, Integer> map = MapBuilder.of();
        map.put(COMMAND_CLEAR_ID, COMMAND_CLEAR);
        map.put(COMMAND_ANIMATE_CAMERA_TO_COORDINATE_ID,
                COMMAND_ANIMATE_CAMERA_TO_COORDINATE);
        map.put(COMMAND_ANIMATE_CAMERA_TO_REGION_ID, COMMAND_ANIMATE_CAMERA_TO_REGION);
        map.put(COMMAND_ANIMATE_MARKER_TO_COORDINATE_ID, COMMAND_ANIMATE_MARKER_TO_COORDINATE);
        return map;
    }

    @Override
    public void receiveCommand(@NonNull MapView root, String commandId, @Nullable ReadableArray args) {
        //super.receiveCommand(root, commandId, args);
        //   Toast.makeText(getReactContext(),"command",Toast.LENGTH_SHORT).show();
        Log.d(TAG, "receivedCommand id:" + commandId);
        if (args != null) {
            Log.d(TAG, " args size: " + args.size());
        }
        switch (commandId) {
            case COMMAND_CLEAR_ID:
                com.reactlibrary.bridgeOps.Map.clear(huaweiMap, markers);
                break;
            case COMMAND_ANIMATE_CAMERA_TO_COORDINATE_ID:
                com.reactlibrary.bridgeOps.Map.animateCameraToCoordinate(huaweiMap, args);
                break;
            case COMMAND_ANIMATE_CAMERA_TO_REGION_ID:
                com.reactlibrary.bridgeOps.Map.animateCameraToRegion(huaweiMap, args);
                break;
            case COMMAND_ANIMATE_MARKER_TO_COORDINATE_ID:
                Marker.animateMarkerToCoordinate(markers, args);
                break;
        }
    }

    /**************************************
     * EVENTS
     ********************************************/
    public void registerEvents() {
        onMarkerClick();
        onMarkerDrag();
        onMapClick();
        onMapLongPress();
    }

    /**
     * registers a click listener (callback) to be invoked when a marker is clicked
     */
    public void onMarkerClick() {
        Log.d(TAG, "Marker Click");
        huaweiMap.setOnMarkerClickListener(marker -> {
            Marker.onMarkerPress(getReactContext(), getId(), (int) marker.getTag(), marker.getPosition().latitude,
                    marker.getPosition().longitude, marker.getTitle(), marker.getSnippet());
            return false;
        });

    }


    public void onMapClick() {
        huaweiMap.setOnMapClickListener(latLng ->
                com.reactlibrary.bridgeOps.Map.onMapClick(getReactContext(), getId(), latLng));
    }

    public void onMapLongPress() {
        huaweiMap.setOnMapLongClickListener(latlng ->
                com.reactlibrary.bridgeOps.Map.onMapLongPress(
                        getReactContext(),
                        getId(),
                        latlng)
        );
    }

    public void onMarkerDrag() {

    }

    /******************************************************************************************************/

    public ReactContext getReactContext() {
        return (ReactContext) mapView.getContext();
    }

    public int getId() {
        return mapView.getId();
    }

    @Nullable
    @Override
    public Map<String, Object> getExportedCustomBubblingEventTypeConstants() {
        MapBuilder.Builder<String, Object> mapBuilder = MapBuilder.builder();

        for (String event : EVENTS) {
            mapBuilder.put(event, MapBuilder.of("phasedRegistrationNames", MapBuilder.of("bubbled", event)));
        }

        return mapBuilder.build();
    }
}
