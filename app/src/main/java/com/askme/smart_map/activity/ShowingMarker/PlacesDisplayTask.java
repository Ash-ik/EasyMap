package com.askme.smart_map.activity.ShowingMarker;

import android.content.Context;
import android.graphics.Color;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.askme.smart_map.activity.util.MapUtilHandler;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.json.JSONObject;

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class PlacesDisplayTask extends AsyncTask<Object, Integer, List<HashMap<String, String>>> {

    JSONObject googlePlacesJson;
    GoogleMap googleMap;
    Context context;
    MapUtilHandler mapTool;

    public PlacesDisplayTask(Context context) {
        this.context = context;
    }

    @Override
    protected List<HashMap<String, String>> doInBackground(Object... inputObj) {

        List<HashMap<String, String>> googlePlacesList = new List<HashMap<String, String>>() {
            @Override
            public void add(int i, HashMap<String, String> stringStringHashMap) {

            }

            @Override
            public boolean add(HashMap<String, String> stringStringHashMap) {
                return false;
            }

            @Override
            public boolean addAll(int i, Collection<? extends HashMap<String, String>> collection) {
                return false;
            }

            @Override
            public boolean addAll(Collection<? extends HashMap<String, String>> collection) {
                return false;
            }

            @Override
            public void clear() {

            }

            @Override
            public boolean contains(Object o) {
                return false;
            }

            @Override
            public boolean containsAll(Collection<?> collection) {
                return false;
            }

            @Override
            public HashMap<String, String> get(int i) {
                return null;
            }

            @Override
            public int indexOf(Object o) {
                return 0;
            }

            @Override
            public boolean isEmpty() {
                return false;
            }

            @NonNull
            @Override
            public Iterator<HashMap<String, String>> iterator() {
                return null;
            }

            @Override
            public int lastIndexOf(Object o) {
                return 0;
            }

            @Override
            public ListIterator<HashMap<String, String>> listIterator() {
                return null;
            }

            @NonNull
            @Override
            public ListIterator<HashMap<String, String>> listIterator(int i) {
                return null;
            }

            @Override
            public HashMap<String, String> remove(int i) {
                return null;
            }

            @Override
            public boolean remove(Object o) {
                return false;
            }

            @Override
            public boolean removeAll(Collection<?> collection) {
                return false;
            }

            @Override
            public boolean retainAll(Collection<?> collection) {
                return false;
            }

            @Override
            public HashMap<String, String> set(int i, HashMap<String, String> stringStringHashMap) {
                return null;
            }

            @Override
            public int size() {
                return 0;
            }

            @NonNull
            @Override
            public List<HashMap<String, String>> subList(int i, int i1) {
                return null;
            }

            @NonNull
            @Override
            public Object[] toArray() {
                return new Object[0];
            }

            @NonNull
            @Override
            public <T> T[] toArray(T[] ts) {
                return null;
            }
        };
        Places placeJsonParser = new Places();
        try {
            googleMap = (GoogleMap) inputObj[0];
            googlePlacesJson = new JSONObject((String) inputObj[1]);
            googlePlacesList = placeJsonParser.parse(googlePlacesJson);
        } catch (Exception e) {
            Log.d("Exception", e.toString());
        }
        return googlePlacesList;
    }

    @Override
    protected void onPostExecute(List<HashMap<String, String>> list) {
        googleMap.clear();
        Marker locationMarker;
        for (int i = 0; i < list.size(); i++) {
            final MarkerOptions markerOptions = new MarkerOptions();
            HashMap<String, String> googlePlace = list.get(i);
            double lat = Double.parseDouble(googlePlace.get("lat"));
            double lng = Double.parseDouble(googlePlace.get("lng"));
            String placeName = googlePlace.get("place_name");
            String vicinity = googlePlace.get("vicinity");
            final LatLng latLng = new LatLng(lat, lng);
            markerOptions.position(latLng);
            markerOptions.title(placeName + " : " + vicinity);
            locationMarker = googleMap.addMarker(markerOptions);
        }

        GetMyLocation myLocation = new GetMyLocation(context);
        Location lastKnownLocation;

        if (myLocation.canGetLocation()) {
            lastKnownLocation = myLocation.getLocationValue();

            // Move the camera instantly to hamburg with a zoom of 15.
            mapTool=new MapUtilHandler(context,googleMap);
            LatLng location=new LatLng(lastKnownLocation.getLatitude(),lastKnownLocation.getLongitude());

            mapTool.circleAnimation(location,Color.BLUE,10000,2000);
            mapTool.animateCamera(location,10,11.5f);
        }

    }
}

