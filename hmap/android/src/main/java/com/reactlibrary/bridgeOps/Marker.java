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
import static com.reactlibrary.Constants.MARKER_DESCRIPTION;


public class Marker {

    /**
     * send marker pressed event to react native
     * @param id      the order of the marker in the array list
     * @param lat     latitude of the marker
     * @param lng     longitude of the marker
     * @param title   title of the marker
     * @param snippet snippet of the marker
     */
    public static void onMarkerPress(ReactContext reactContext,
                                     int id,
                                     int markerId,
                                     double lat,
                                     double lng,
                                     String title,
                                     String snippet
    ) {
        WritableMap data = Arguments.createMap();

        data.putInt("id", markerId);
        data.putDouble("lat", lat);
        data.putDouble("lng", lng);
        data.putString("title", title);
        data.putString(MARKER_DESCRIPTION, snippet);

        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                id,
                EVENT_MARKER_PRESS,
                data);
    }

    /**
     * send marker long pressed event to react native
     * @param id      the order of the marker in the array list
     * @param lat     latitude of the marker
     * @param lng     longitude of the marker
     * @param title   title of the marker
     * @param description snippet of the marker
     */

    public static void onMarkerLongPress(ReactContext reactContext,
                                         int id,
                                         double lat,
                                         double lng,
                                         String title,
                                         String description) {
        WritableMap data = Arguments.createMap();

        data.putInt("id", id);
        data.putDouble("lat", lat);
        data.putDouble("lng", lng);
        data.putString("title", title);
        data.putString(MARKER_DESCRIPTION, description);

        reactContext.getJSModule(RCTEventEmitter.class).receiveEvent(
                id,
                Constants.EVENT_MAP_LONG_PRESS,
                data);
    }

    public static void animateMarkerToCoordinate(Map<Integer,com.huawei.hms.maps.model.Marker> markers, ReadableArray args){
       if(args == null ){
           return;
       }
            int id = args.getInt(0);
            double lat= args.getDouble(1);
            double lng = args.getDouble(2);
        Log.d(MapHmsManager.TAG  , "id = " + id );

        MarkerUtils.animateMarkerToCoordinate (markers.get(id) , lat , lng );

    }
}
