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

import android.widget.Toast;

import com.huawei.hms.maps.model.LatLngBounds;
import com.reactlibrary.bridgeOps.Marker;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.arch.core.util.Function;

import java.util.HashMap;
import java.util.Map;

import static com.reactlibrary.Constants.COMMAND_ANIMATE_MARKER_TO_COORDINATE;
import static com.reactlibrary.Constants.COMMAND_ANIMATE_MARKER_TO_COORDINATE_ID;
import static com.reactlibrary.Constants.COMMAND_ANIMATE_TO_COORDINATE;
import static com.reactlibrary.Constants.COMMAND_ANIMATE_TO_COORDINATE_ID;
import static com.reactlibrary.Constants.COMMAND_ANIMATE_TO_REGION;
import static com.reactlibrary.Constants.COMMAND_ANIMATE_TO_REGION_ID;
import static com.reactlibrary.Constants.COMMAND_CLEAR;
import static com.reactlibrary.Constants.COMMAND_CLEAR_ID;
import static com.reactlibrary.Constants.ENABLE_COMPASS;
import static com.reactlibrary.Constants.ENABLE_ZOOM_CONTROLS;
import static com.reactlibrary.Constants.EVENTS;
import static com.reactlibrary.Constants.LAT;
import static com.reactlibrary.Constants.LNG;
import static com.reactlibrary.Constants.MARKER_DESCRIPTION;
import static com.reactlibrary.Constants.MARKER_ICON;
import static com.reactlibrary.Constants.MARKER_ID;
import static com.reactlibrary.Constants.MARKER_TITLE;
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
        Log.e(TAG, "Map Ready to use callback");
    }

    /******************************************************************
     * REACT PROPS
     *******************************************************************/


    /**
     * marker received from react native to be drawn on the map
     *
     * @param mapView instance of the mapView
     * @param value   marker to be received when the component's state in changed on
     *                the react native side
     */
    @ReactProp(name = "addMarker")
    public void addMarker(MapView mapView, ReadableMap value) {
        Log.e(TAG, "addMarker"  );
        if (huaweiMap == null) return;


        if (value == null) {
            return;
        }

        double lat = value.getDouble(LAT);
        double lng = value.getDouble(LNG);

        String image = null, title = null, description = null;
        if (value.hasKey(MARKER_ICON)) {
            image = value.getString(MARKER_ICON);
        }
        if (value.hasKey(MARKER_TITLE)) {
            title = value.getString(MARKER_TITLE);
        }
        if (value.hasKey(MARKER_DESCRIPTION)) {
            description = value.getString(MARKER_DESCRIPTION);
        }

        MapUtils.createMarker(huaweiMap, markers, defaultMarkerIcon, value.getInt(MARKER_ID), lat, lng,
                title, description, image);
    }

    /**
     * replace the existing markers on the map with new list of markers
     *
     * @param mapView instance of the mapView
     * @param value   list of marker to be received when the component's state in
     *                changed on the react native side
     */
    @ReactProp(name = "addMarkers")
    public void addMarkers(MapView mapView, ReadableArray value) {
        Log.e(TAG, "addMarkers "  );
        if (huaweiMap == null) return;


        if (value == null || value.size() == 0) {
            return;
        }
        ReadableMap marker = null;
        // huaweiMap.clear();

        LatLngBounds.Builder bounds = new LatLngBounds.Builder();

        for (int i = 0; i < value.size(); i++) {
            marker = value.getMap(i);
            if (marker == null)
                continue;
            double lat = marker.getDouble(LAT);
            double lng = marker.getDouble(LNG);

            String image = null, title = null, description = null;
            if (marker.hasKey(MARKER_ICON)  ) {
                image = marker.getString(MARKER_ICON);
                if(image != null && image.equals(defaultMarkerIconUrl)){
                    //marker icon equals the default icon,
                    // set it to null to avoid download the same icon again
                    image=  null ;
                }
            }
            if (marker.hasKey(MARKER_TITLE)) {
                title = marker.getString(MARKER_TITLE);
            }
            if (marker.hasKey(MARKER_DESCRIPTION)) {
                description = marker.getString(MARKER_DESCRIPTION);
            }
            MapUtils.createMarker(huaweiMap,
                    markers,
                    defaultMarkerIcon,
                    marker.getInt(MARKER_ID),
                    lat,
                    lng,
                    title, description,
                    image);
            bounds.include(new LatLng(lat, lng));
        }

        Log.e(TAG, "add markers, data length: " + value.size() + " ,all: " + markers.size());

        //  huaweiMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 7));
        // selectMarker(null , marker);
    }


    /**
     * list of marker received from react native to be drawn on the map
     *
     * @param mapView instance of the mapView
     * @param value   list of marker to be received when the component's state in
     *                changed on the react native side
     */
    @ReactProp(name = "setMarkers")
    public void setMarkers(MapView mapView, ReadableArray value) {
        Log.e(TAG, "setMarkers "  );
        if (huaweiMap == null) return;

        com.reactlibrary.bridgeOps.Map.clear(this.huaweiMap, this.markers);
        addMarkers(mapView, value);
    }


    /**
     * list of marker received from react native to be drawn on the map
     *
     * @param mapView instance of the mapView
     * @param value   list of marker to be received when the component's state in
     *                changed on the react native side
     */
    @ReactProp(name = "markers")
    public void markers(MapView mapView, ReadableArray value) {
        Log.e(TAG, "markers "  );
        Log.e(TAG, "markers "  );
        Log.e(TAG, "markers "  );
        Log.e(TAG, "markers "  );
        Log.e(TAG, "markers "  );
        if (huaweiMap == null) return;
        this.setMarkers(mapView,value );
    }
    /**
     * @param mapView an instance of the mapView
     * @param value   the state from javascript that tells whether to show the
     *                current location on map or not
     */
    @ReactProp(name = "myLocationEnabled")
    public void setMyLocationEnabled(MapView mapView, boolean value) {
        Log.e(TAG, "myLocationEnabled " + (value ? "true " : "false "));
        if (huaweiMap == null) return;

        huaweiMap.setMyLocationEnabled(value);
    }

    /**
     * set the default icon for the markers,
     * this will be set when the marker does not have an image specified
     *
     * @param mapView
     * @param value
     */
    @ReactProp(name = "defaultMarkerImage")
    public void setDefaultMarkerImage(MapView mapView, String value) {
        System.out.println("--------------------");
        Log.e(TAG, "set defaultMarkerImage : " + value);

        if (this.defaultMarkerIconUrl != null && this.defaultMarkerIconUrl.equals(value)) {
            Log.e(TAG, "defaultMarkerUrl is already set");
            return;
        }
        new Thread(() -> {
            try {

                this.defaultMarkerIcon = MapUtils.markerIconFromUrl(value);
                this.defaultMarkerIconUrl  = value;
            } catch (Exception e) {
                Log.e(TAG, "failed to default marker icon: " + e.toString());
                e.printStackTrace();

            }
        }).start();
    }

    // setZoomToNewMarkers
    // setZoomToNewMarker


    /**
     * setCameraOptions
     *
     * @param mapView
     * @param value
     */
    @ReactProp(name = "cameraOptions")
    public void setCameraOptions(MapView mapView, ReadableMap value) {
        Log.e(TAG, "set cameraOptions: " + value);
        if (huaweiMap == null) return;

        int zoom = value.getInt("zoom");
        double lat = value.getDouble(LAT);
        double lng = value.getDouble(LNG);
        LatLng position = new LatLng(lat, lng);
        this.huaweiMap.animateCamera(CameraUpdateFactory.newLatLngZoom(position, zoom));
    }

    /**
     * setUiSettings: myLocationButton, zoomButtons, compass, indoor level picker,map toolbar
     *
     * @param mapView
     * @param value
     */
    @ReactProp(name = "uiSettings")
    public void setUiSettings(MapView mapView, ReadableMap value) {
        Log.e(TAG, "set uiSettings: ");
        if (huaweiMap == null) return;

        UiSettings uiSettings = this.huaweiMap.getUiSettings();
        Function<String, Boolean> getFlag = name -> {
            if (value.hasKey(name)) {
                Log.e(TAG , name + " -> true ");
                return value.getBoolean(name);
            }
            Log.e(TAG , name + " -> false ");
            return false;
        };
        uiSettings.setZoomControlsEnabled(getFlag.apply(UI_SETTINGS_ZOOM_CONTROLS));
        uiSettings.setCompassEnabled(getFlag.apply(UI_SETTINGS_COMPASS));
        uiSettings.setMyLocationButtonEnabled(getFlag.apply(UI_SETTINGS_MY_LOCATION_BUTTON));
        uiSettings.setMapToolbarEnabled(getFlag.apply(UI_SETTINGS_MAP_TOOLBAR));
        uiSettings.setIndoorLevelPickerEnabled(getFlag.apply(UI_SETTINGS_INDOOR_LEVEL_PICKER));
    }

//    private boolean getFlag(ReadableMap map, String name) {
//        if (map.hasKey(name)) {
//            return map.getBoolean(name);
//        }
//        return false;
//    }

    /**************** COMMANDS *****************/

    @Nullable
    @Override
    public Map<String, Integer> getCommandsMap() {
        // You need to implement this method and return a map with the readable
        // name and constant for each of your commands. The name you specify
        // here is what you'll later use to access it in react-native.
        Map<String, Integer> map = MapBuilder.of();
        map.put(COMMAND_CLEAR_ID, COMMAND_CLEAR);
        map.put(COMMAND_ANIMATE_TO_COORDINATE_ID,
                COMMAND_ANIMATE_TO_COORDINATE);
        map.put(COMMAND_ANIMATE_TO_REGION_ID, COMMAND_ANIMATE_TO_REGION);
        map.put(COMMAND_ANIMATE_MARKER_TO_COORDINATE_ID, COMMAND_ANIMATE_MARKER_TO_COORDINATE);
        return map;
    }

    @Override
    public void receiveCommand(@NonNull MapView root, String commandId, @Nullable ReadableArray args) {
        //super.receiveCommand(root, commandId, args);
        //   Toast.makeText(getReactContext(),"command",Toast.LENGTH_SHORT).show();
        Log.e(TAG, "receivedCommand id:" + commandId);
        if (args != null) {
            Log.e(TAG, " args size: " + args.size());
        }
        switch (commandId) {
            case COMMAND_CLEAR_ID:
                com.reactlibrary.bridgeOps.Map.clear(huaweiMap, markers);
                break;
            case COMMAND_ANIMATE_TO_COORDINATE_ID:
                com.reactlibrary.bridgeOps.Map.animateToCoordinate(huaweiMap, args);
                break;
            case COMMAND_ANIMATE_TO_REGION_ID:
                com.reactlibrary.bridgeOps.Map.animateToRegion(huaweiMap, args);
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
        Log.e(TAG, "Marker Click");
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
