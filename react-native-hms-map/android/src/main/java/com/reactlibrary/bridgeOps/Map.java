package com.reactlibrary.bridgeOps;

import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.RCTEventEmitter;
import com.huawei.hms.maps.CameraUpdateFactory;
import com.huawei.hms.maps.HuaweiMap;
import com.huawei.hms.maps.model.LatLng;
import com.huawei.hms.maps.model.LatLngBounds;
import com.huawei.hms.maps.model.Marker;
import com.reactlibrary.Constants;

import java.util.ArrayList;

import static com.reactlibrary.Constants.LATITUDE;
import static com.reactlibrary.Constants.LONGITUDE;


public class Map {
    public static final String TAG = "HMSMap map";

    /**
     * event that will be sent to react native when the is drawn on the screen and ready to be interacted with
     *
     * @param reactContext an instance of ReactContext
     * @param id           id of the map view
     */
    public static void onMapReady(ReactContext reactContext, int id) {
        WritableMap data = Arguments.createMap();

        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                id,
                Constants.EVENT_MAP_READY,
                data);
    }

    /**
     * Event that will be sent to react native when a the user's press (click/tap) a marker on the map
     *
     * @param reactContext an instance of ReactContext
     * @param id           the id of the marker that the user pressed (clicked/tapped)
     * @param latLng       the coordinate (location) of the marker that was pressed
     */
    public static boolean onMapClick(ReactContext reactContext, int id, LatLng latLng) {
        WritableMap data = Arguments.createMap();
        data.putDouble(LATITUDE, latLng.longitude);
        data.putDouble(LONGITUDE, latLng.longitude);

        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                id,
                Constants.EVENT_MAP_PRESS,
                data);
        return false;
    }


    /**
     * Event that will be sent to react native when a the user's long press (long click) a marker on the map
     *
     * @param reactContext an instance of ReactContext
     * @param id           the id of the marker that the user long pressed (long clicked)
     * @param latLng       the coordinate (location) of the marker that was long pressed
     */
    public static boolean onMapLongPress(ReactContext reactContext, int id, LatLng latLng) {
        WritableMap data = Arguments.createMap();

        data.putDouble(LATITUDE, latLng.latitude);
        data.putDouble(LONGITUDE, latLng.longitude);

        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                id,
                Constants.EVENT_MAP_LONG_PRESS,
                data);
        return false;
    }

    /**
     * Command received from react native to animate the camera to a location (latlng)
     *
     * @param huaweiMap an instance of huaweiMap
     * @param value     object received from react native when the component's state changes,
     *                  needed the get the location where the camera will move
     */
    public static void animateCameraToCoordinate(HuaweiMap huaweiMap, ReadableArray value) {
        if (huaweiMap == null || value == null) return;

        ReadableMap value2 = value.getMap(0);
        int zoom = value.getInt(1);

        Log.d(TAG, "selectedMarker, " + " ,zoom: " + zoom);

        if (value2 == null) return;

        double latitude = value2.getDouble(LATITUDE);
        double longitude = value2.getDouble(LONGITUDE);

        huaweiMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(latitude, longitude), zoom));
    }

    /**
     * Command received from react native to animate the camera to a region bounded by 2 or more locations (latlng)
     *
     * @param huaweiMap an instance of the huawei map
     * @param value     array of object received from react native when the component's state changes,
     *                  needed the calculate the minimum zoom to include them all in the viewport
     */
    public static void animateCameraToRegion(HuaweiMap huaweiMap, ReadableArray value) {
        if (huaweiMap == null || value == null) return;


        if (value.size() == 0) {
            return;
        }

        int zoom = value.getInt(1);
        ReadableArray coordinates = value.getArray(0);
        if (coordinates == null || coordinates.size() == 0) {
            Log.d(TAG, "cannot animateCameraToRegion, no coordinates found");
            return;
        }
        LatLngBounds.Builder bounds = new LatLngBounds.Builder();
        ReadableMap latlng;

        for (int i = 0; i < coordinates.size(); i++) {
            latlng = coordinates.getMap(i);
            if (latlng == null)
                continue;
            double latitude = latlng.getDouble(LATITUDE);
            double longitude = latlng.getDouble(LONGITUDE);

            bounds.include(new LatLng(latitude, longitude));
        }
        Log.d(TAG, "animateCameraToRegion coords:" + coordinates.size() + " ,zoom: " + zoom);
        huaweiMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), zoom));

    }

    /**
     * Animate the camera to a region contiaing all the markers, called when the flag autoUpdatedCamera is true
     *
     * @param huaweiMap an instance of the huawei map
     * @param value     set of markers
     */
    public static void animateCameraToRegion(HuaweiMap huaweiMap,
                                             int zoom,
                                             java.util.Map<Integer, Marker> value) {

        if (huaweiMap == null || value == null) return;

        if (value.size() == 0) {
            return;
        }

        LatLngBounds.Builder bounds = new LatLngBounds.Builder();

        for (Marker marker : value.values()) {

            double latitude = marker.getPosition().latitude;
            double longitude = marker.getPosition().longitude;

            bounds.include(new LatLng(latitude, longitude));
        }

        Log.d(TAG, "animateCameraToRegion coords:" + value.size() + " ,zoom: " + zoom);
        huaweiMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), zoom));
    }

    /**
     * Command received from react native to removes all the markers from the map
     *
     * @param huaweiMap an instance of huawei map,
     */
    public static void clear(HuaweiMap huaweiMap, java.util.Map<Integer, Marker> markers) {
        if (huaweiMap == null) return;
        huaweiMap.clear();
        markers.clear();
    }
}
