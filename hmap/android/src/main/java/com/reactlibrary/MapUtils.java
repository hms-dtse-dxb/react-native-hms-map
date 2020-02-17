package com.reactlibrary;


import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;

import com.facebook.react.bridge.ReadableArray;
import com.facebook.react.bridge.ReadableMap;
import com.huawei.hms.maps.CameraUpdateFactory;
import com.huawei.hms.maps.HuaweiMap;
import com.huawei.hms.maps.model.BitmapDescriptor;
import com.huawei.hms.maps.model.BitmapDescriptorFactory;
import com.huawei.hms.maps.model.LatLng;
import com.huawei.hms.maps.model.LatLngBounds;
import com.huawei.hms.maps.model.Marker;
import com.huawei.hms.maps.model.MarkerOptions;

import java.net.URL;
import java.util.Map;

import static com.reactlibrary.Constants.LATITUDE;
import static com.reactlibrary.Constants.LONGITUDE;
import static com.reactlibrary.Constants.MARKER_DESCRIPTION;
import static com.reactlibrary.Constants.MARKER_ICON;
import static com.reactlibrary.Constants.MARKER_ID;
import static com.reactlibrary.Constants.MARKER_TITLE;
import static com.reactlibrary.MapHmsManager.DEFAULT_ZOOM;

public class MapUtils {
    public static final String TAG = "HMSMapUtils";
    /**
     * add a markers to the map
     * @param huaweiMap the map view
     * @param value the value from javascript, represent an array of markers
     * @param markers list of existing markers
     * @param defaultMarkerIconUrl the image url of the default image
     * @param defaultMarkerIcon the downloaded bitmap from the image url, to avoid downloading it again
     */
    public static void addMarker(HuaweiMap huaweiMap,
                                 ReadableMap value,
                                 Map<Integer, Marker> markers,
                                 String defaultMarkerIconUrl,
                                 BitmapDescriptor defaultMarkerIcon,
                                 boolean updateCamera) {
        if (value == null) {
            return;
        }

        double latitude = value.getDouble(LATITUDE);
        double longitude = value.getDouble(LONGITUDE);

        String image = null, title = null, description = null;
        if (value.hasKey(MARKER_ICON)) {
            image = value.getString(MARKER_ICON);
            if (image != null && image.equals(defaultMarkerIconUrl)) {
                //marker icon equals the default icon,
                // set it to null to avoid download the same icon again
                image = null;
            }
        }
        if (value.hasKey(MARKER_TITLE)) {
            title = value.getString(MARKER_TITLE);
        }
        if (value.hasKey(MARKER_DESCRIPTION)) {
            description = value.getString(MARKER_DESCRIPTION);
        }

        MapUtils.createMarker(huaweiMap, markers, defaultMarkerIcon, value.getInt(MARKER_ID), latitude, longitude,
                title, description, image,updateCamera);
    }

    /**
     * add list of markers to the map
     * @param huaweiMap the map view
     * @param value the value from javascript, represent an array of markers
     * @param markers list of existing markers
     * @param defaultMarkerIconUrl the image url of the default image
     * @param defaultMarkerIcon the downloaded bitmap from the image url, to avoid downloading it again
     */
    public static void addMarkers(HuaweiMap huaweiMap,
                                  ReadableArray value,
                                  Map<Integer, Marker> markers,
                                  String defaultMarkerIconUrl,
                                  BitmapDescriptor defaultMarkerIcon,
                                  boolean updateCamera) {

        if (value == null || value.size() == 0) {
            return;
        }
        ReadableMap marker = null;
        LatLngBounds.Builder bounds = new LatLngBounds.Builder();

        for (int i = 0; i < value.size(); i++) {
            marker = value.getMap(i);

            if (marker == null)
                continue;

            addMarker(huaweiMap, marker, markers, defaultMarkerIconUrl, defaultMarkerIcon,updateCamera);

            double latitude = marker.getDouble(LATITUDE);
            double longitude = marker.getDouble(LONGITUDE);

            bounds.include(new LatLng(latitude, longitude));
        }

        Log.d(TAG, "add markers, data length: " + value.size() + " ,all: " + markers.size());
    }

    /**
     * create a marker and adds it to the map
     * @param huaweiMap the map view
     * @param markers   list of existing markers
     * @param defaultIcon the default icon if it was set
     * @param id     the id of the marker
     * @param latitude   the LATITUDE of the marker's location
     * @param longitude   the longitude of the marker's location
     * @param title the title of the marker
     * @param snippet the description of the marker
     * @param imageUrl the image url that is set as the default marker image
     */
    public static void createMarker(HuaweiMap huaweiMap,
                                    Map<Integer, Marker> markers,
                                    BitmapDescriptor defaultIcon,
                                    int id,
                                    double latitude,
                                    double longitude,
                                    String title,
                                    String snippet,
                                    String imageUrl,
                                    boolean updateCamera) {

        if (huaweiMap == null) return;

        MarkerOptions m = new MarkerOptions();

        m.position(new LatLng(latitude, longitude));
        m.title(title);
        m.snippet(snippet);

        if (imageUrl == null || imageUrl.length() < 3) {
            if (defaultIcon != null) {
                m.icon(defaultIcon);
            }
            Marker marker = huaweiMap.addMarker(m);
            marker.setTag(id);
            markers.put(id, marker);
            if(updateCamera){
                huaweiMap.animateCamera(
                        CameraUpdateFactory.newLatLngZoom(
                                new LatLng(latitude, longitude),
                                DEFAULT_ZOOM));
            }
        } else {
            new Thread(() -> {
                BitmapDescriptor image = markerIconFromUrl(imageUrl);
                m.icon(image);
                Marker marker = huaweiMap.addMarker(m);
                marker.setTag(id);
                markers.put(id, marker);
                if(updateCamera){
                    huaweiMap.animateCamera(
                            CameraUpdateFactory.newLatLngZoom(
                                    new LatLng(latitude, longitude),
                                    DEFAULT_ZOOM));
                }
            }).start();
        }

    }


    /**
     * downloads the bitmap from the link and scaling it down
     *
     * @param link link to image (marker icon)
     * @return bitmapDescriptor to be used by the map the show the icon as marker in the map
     */
    public static BitmapDescriptor markerIconFromUrl(String link) {
        Log.d(MapHmsManager.TAG, "makeIconFromLink: " + link);
        if ("default".equals(link)) {
            return null;
        }
        try {//dimens : 250 * 235
            URL url = new URL(link);

            Bitmap b = null;
            try {
                b = BitmapFactory.decodeStream(url.openConnection().getInputStream());

            } catch (Exception e) {
                //

            }

            if (b == null) {
                return null;
            }
            //b = Bitmap.createScaledBitmap(b, MARKER_WIDTH, MARKER_HEIGHT, false);
            b = resize(b, Constants.MARKER_WIDTH, Constants.MARKER_HEIGHT);

            Log.d(TAG, "Utils > bitmap info " + b.getByteCount() + " " + b.getHeight() + "X" + b.getWidth());
            return BitmapDescriptorFactory.fromBitmap(b);
        } catch (Exception e) {
            Log.d(TAG, "failed to get bitmap " + e.toString());
        }
        return null;
    }

    /**
     * resize a map while keeping the aspect ratio, the image dimension will equal
     * or be smaller than the specified dimension
     *
     * @param image     the bitmap to be resized
     * @param maxWidth  target width
     * @param maxHeight target height
     * @return new scaled down bitmap
     */
    private static Bitmap resize(Bitmap image, int maxWidth, int maxHeight) {
        if (maxHeight > 0 && maxWidth > 0) {
            int width = image.getWidth();
            int height = image.getHeight();
            float ratioBitmap = (float) width / (float) height;
            float ratioMax = (float) maxWidth / (float) maxHeight;
            int finalWidth = maxWidth;
            int finalHeight = maxHeight;
            if (ratioMax > 1) {
                finalWidth = (int) ((float) maxHeight * ratioBitmap);
            } else {
                finalHeight = (int) ((float) maxWidth / ratioBitmap);
            }
            image = Bitmap.createScaledBitmap(image, finalWidth, finalHeight, true);
            return image;
        } else {
            return image;
        }
    }

}
