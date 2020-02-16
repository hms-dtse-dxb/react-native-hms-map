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

import static com.reactlibrary.Constants.LAT;
import static com.reactlibrary.Constants.LNG;


public class Map {
    public static final String TAG = "HMSMap map";

    public static void onMapReady(ReactContext reactContext, int id) {
        WritableMap data = Arguments.createMap();


        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                id,
                Constants.EVENT_MAP_READY,
                data);
    }

    public static void onMapClick(ReactContext reactContext, int id, LatLng latLng) {
        WritableMap data = Arguments.createMap();
        data.putDouble(LAT, latLng.longitude);
        data.putDouble(LNG, latLng.longitude);

        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                id,
                Constants.EVENT_MAP_PRESS,
                data);
    }

    public static void onMapLongPress(ReactContext reactContext, int id, LatLng latLng) {
        WritableMap data = Arguments.createMap();

        data.putDouble(LAT, latLng.latitude);
        data.putDouble(LNG, latLng.longitude);

        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                id,
                Constants.EVENT_MAP_LONG_PRESS,
                data);
    }

    /**
     * animate the camera to a location (latlng)
     *
     * @param huaweiMap an instance of huaweiMap
     * @param value     object received from react native when the component's state changes,
     *                  needed the get the location where the camera will move
     */
    public static void animateToCoordinate(HuaweiMap huaweiMap, ReadableArray value) {
        if (huaweiMap == null || value == null) return;

        ReadableMap value2 = value.getMap(0);
        int zoom = value.getInt(1);

        Log.d(TAG, "selectedMarker, "+ " ,zoom: "+ zoom );

        if (value2 == null) return;

        double lat = value2.getDouble(LAT);
        double lng = value2.getDouble(LNG);

        huaweiMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(lat, lng),zoom));
    }

    /**
     * animate the camera to a region bounded by 2 or more locations (latlng)
     *
     * @param huaweiMap an instance of the huawei map
     * @param value     array of object received from react native when the component's state changes,
     *                  needed the calculate the minimum zoom to include them all in the viewport
     */
    public static void animateToRegion(HuaweiMap huaweiMap, ReadableArray value) {
        if (huaweiMap == null || value == null) return;


        if (value.size() == 0) {
            return;
        }

        LatLngBounds.Builder bounds = new LatLngBounds.Builder();
        ReadableMap latlng;
        ReadableArray coordinates = value.getArray(0);
        int zoom = value.getInt(1);


        if (coordinates == null || coordinates.size() == 0) {
            Log.d(TAG, "cannot animateToRegion, no coordinates found");
            return;
        }

        for (int i = 0; i < coordinates.size(); i++) {
            latlng = coordinates.getMap(i);
            if (latlng == null)
                continue;
            double lat = latlng.getDouble(LAT);
            double lng = latlng.getDouble(LNG);

            bounds.include(new LatLng(lat, lng));
        }
        Log.d(TAG, "animateToRegion coords:" + coordinates.size() + " ,zoom: "+ zoom );
        huaweiMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), zoom));
    }

    /**
     * removes all the markers from the map
     *
     * @param huaweiMap an instance of huawei map,
     */
    public static void clear(HuaweiMap huaweiMap,  java.util.Map<Integer, Marker> markers) {
        if (huaweiMap == null) return;
        markers.clear();
        huaweiMap.clear();
    }
}
