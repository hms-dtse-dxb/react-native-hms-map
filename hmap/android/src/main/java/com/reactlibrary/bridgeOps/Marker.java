package com.reactlibrary.bridgeOps;

import android.util.Log;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReactContext;
import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.uimanager.events.RCTEventEmitter;

import com.reactlibrary.Constants;
import com.reactlibrary.MapHmsManager;
import com.reactlibrary.MarkerUtils;


import java.util.Map;

import static com.reactlibrary.Constants.EVENT_MARKER_PRESS;
import static com.reactlibrary.Constants.LATITUDE;
import static com.reactlibrary.Constants.LONGITUDE;
import static com.reactlibrary.Constants.MARKER_DESCRIPTION;
import static com.reactlibrary.Constants.MARKER_ID;
import static com.reactlibrary.Constants.MARKER_TITLE;


public class Marker {

    /**
     * send marker pressed event to react native
     *
     * @param id          the order of the marker in the array list
     * @param latitude    LATITUDE of the marker
     * @param longitude   longitude of the marker
     * @param title       title of the marker
     * @param description snippet of the marker
     */
    public static void onMarkerPress(ReactContext reactContext,
                                     int id,
                                     int markerId,
                                     double latitude,
                                     double longitude,
                                     String title,
                                     String description
    ) {
        WritableMap data = Arguments.createMap();

        data.putInt(MARKER_ID, markerId);
        data.putDouble(LATITUDE, latitude);
        data.putDouble(LONGITUDE, longitude);
        data.putString(MARKER_TITLE, title);
        data.putString(MARKER_DESCRIPTION, description);

        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                id,
                EVENT_MARKER_PRESS,
                data);
    }

    /**
     * send marker long pressed event to react native
     *
     * @param id          the order of the marker in the array list
     * @param latitude    LATITUDE of the marker
     * @param longitude   longitude of the marker
     * @param title       title of the marker
     * @param description snippet of the marker
     */

    public static void onMarkerLongPress(ReactContext reactContext,
                                         int id,
                                         double latitude,
                                         double longitude,
                                         String title,
                                         String description) {
        WritableMap data = Arguments.createMap();

        data.putInt(MARKER_ID, id);
        data.putDouble(LONGITUDE, latitude);
        data.putDouble(LATITUDE, longitude);
        data.putString(MARKER_TITLE, title);
        data.putString(MARKER_DESCRIPTION, description);

        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                id,
                Constants.EVENT_MAP_LONG_PRESS,
                data);
    }

    /**
     * command received from react native to Animate (move) a marker to a new position on the map
     *
     * @param markers the marker that will animated (moved)
     * @param args    array of argument contains: markerId and coordinates of the new location
     */
    public static void animateMarkerToCoordinate(Map<Integer, com.huawei.hms.maps.model.Marker> markers,
                                                 ReadableArray args) {
        if (args == null) {
            return;
        }
        int id = args.getInt(0);
        if (markers.size() < 1 || !markers.containsKey(id)) {
            return;
        }
        double latitude = args.getDouble(1);
        double longitude = args.getDouble(2);
        Log.d(MapHmsManager.TAG, "id = " + id);

        MarkerUtils.animateMarkerToCoordinate(markers.get(id), latitude, longitude);
    }


}
