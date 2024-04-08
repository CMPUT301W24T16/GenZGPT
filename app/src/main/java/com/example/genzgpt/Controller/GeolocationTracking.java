/*
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
 */

package com.example.genzgpt.Controller;


import static com.example.genzgpt.BuildConfig.DEFAULT_API_KEY;

import android.Manifest;
import android.annotation.SuppressLint;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;


import androidx.annotation.NonNull;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


import com.example.genzgpt.Model.Event;


import com.example.genzgpt.Model.User;
import com.example.genzgpt.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.GeoPoint;

import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.model.PlaceLikelihood;
import com.google.android.libraries.places.api.net.FindCurrentPlaceRequest;
import com.google.android.libraries.places.api.net.FindCurrentPlaceResponse;
import com.google.android.libraries.places.api.net.PlacesClient;

import android.location.Geocoder;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;


/**
 * This class handles Geolocation Tracking for the user, includes permission requests and location pulls.
 */
public class GeolocationTracking extends Fragment implements OnMapReadyCallback {

    public Event event;

    public Firebase firebase;
    public static final String TAG = GeolocationTracking.class.getSimpleName();
    private GoogleMap googleMap;
    private FusedLocationProviderClient fusedLocationProviderClient;
    //This will be the default position for the camera (Edmonton, Alberta)
    private final LatLng defaultLocation = new LatLng(53.5460983, -113.4937266);
    private static final int DEFAULT_ZOOM = 15;
    public static final int FINE_LOCATION_PERMISSION = 101;
    private PlacesClient placesClient;
    private boolean locationPermissionGranted;
    private CameraPosition cameraPosition;
    private Location lastKnownLocation;
    private static final String KEY_CAMERA_POSITION = "cameraPosition";
    private static final String KEY_LOCATION = "location";
    private static final int MAX_ENTRIES = 5;
    private String[] likelyPlaceNames;
    private String[] likelyPlaceAddresses;
    private List[] likelyPlaceAttributions;
    private LatLng[] likelyPlaceLatLngs;
    GeoPoint userLocation;

    //Constructor that takes in an event object
    public GeolocationTracking(Event event) {
        this.event = event;
    }

    //Empty required constructor
    public GeolocationTracking() {
    }

    /**
     * Creates the map to be displayed. If any saved instances, get them and return view.
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return the view
     */
    @SuppressLint("RestrictedApi")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view;
        Places.initialize(requireActivity().getApplicationContext(), DEFAULT_API_KEY);
        placesClient = Places.createClient(requireContext());
        if (savedInstanceState != null) {
            lastKnownLocation = savedInstanceState.getParcelable(KEY_LOCATION);
            cameraPosition = savedInstanceState.getParcelable(KEY_CAMERA_POSITION);
        }
        view = inflater.inflate(R.layout.map_view_fragment, container, false);
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(requireActivity());

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                .findFragmentById(R.id.map);
        try {
            mapFragment.getMapAsync(this);
        } catch (NullPointerException e) {
            Log.e("Exception: %s", e.getMessage(), e);
        }
        return view;
    }

    /**
     * Saves preferences for the map.
     * @param outState Bundle in which to place your saved state.
     */
    @Override
    public void onSaveInstanceState(Bundle outState) {
        if (googleMap != null) {
            outState.putParcelable(KEY_CAMERA_POSITION, googleMap.getCameraPosition());
            outState.putParcelable(KEY_LOCATION, lastKnownLocation);
        }
        super.onSaveInstanceState(outState);
    }

    /**
     * When the map is ready, display this.
     * @param googleMap
     * The view for GoogleMaps.
     */
    @Override
    public void onMapReady(GoogleMap googleMap) {
        firebase = new Firebase();
        this.googleMap = googleMap;
        if (lastKnownLocation != null) {
            dropMarker(googleMap, lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude(), "Event");
        }
        requestLocationPermission();
        firebase.retrieveLocations(event.getEventName(), new Firebase.OnLocationsRetrievedListener() {
            @Override
            public void onLocationsRetrieved(List<GeoPoint> locations) {
                for (GeoPoint location : locations) {
                    double lat = location.getLatitude();
                    double lng = location.getLongitude();
                    dropMarker(googleMap, lat, lng, "User");
                }
            }

            @Override
            public void onLocationsRetrievalFailed(Exception e) {
                Log.e("Geolocation", "Location retrieval failed.");
            }
        });
        updateLocationUI();
        getEventLocation();
    }

    /**
     * Drops marker at user's current location
     * @param googleMap
     * The view for googleMaps.
     */
    public void dropMarker(GoogleMap googleMap, double latitude, double longitude, String title) {
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(latitude, longitude))
                .title(title));
    }

    /**
     * obtains the user's location
     * @return GeoPoint of user's location
     */
    public GeoPoint getUserLocation() {
            if (ActivityCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED){
            fusedLocationProviderClient.getLastLocation().addOnSuccessListener(requireActivity(), new OnSuccessListener<Location>() {
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        userLocation = new GeoPoint(latitude, longitude);
                    } else {
                        throw new NullPointerException("Location turned off");
                    }
                }
            });
        }else{
                requestLocationPermission();
                userLocation = new GeoPoint(0,0);
            }
        return userLocation;
    }


    /**
     * Obtains the event's location to set the center of the map
     */
    private void getEventLocation(){
                String eventLocation = event.getLocation();
                GeoPoint geoPoint = getLocationFromAddress(eventLocation);
            try{
                if(locationPermissionGranted){
                    @SuppressLint("MissingPermission")
                    Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                    locationResult.addOnCompleteListener(requireActivity(), new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful()){
                                lastKnownLocation = task.getResult();
                                if (lastKnownLocation!=null){
                                    googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(
                                            new LatLng(geoPoint.getLatitude(), geoPoint.getLongitude()), DEFAULT_ZOOM));
                                }
                            }else{
                                Log.d(TAG, "Current location is null. Using defaults.");
                                Log.e(TAG,"Exception: %s", task.getException());
                                googleMap.moveCamera(CameraUpdateFactory
                                        .newLatLngZoom(defaultLocation, DEFAULT_ZOOM));
                                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                            }
                        }
                    });
                }
            } catch (SecurityException e){
                Log.e("Exception: %s", e.getMessage(), e);
            }
        }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == R.id.option_get_place) {
            showCurrentPlace();
        }
        return true;
    }
    /**
     * Gets the current location of the device, and positions the map's camera.
     */
    private void getDeviceLocation() {
        /*
         * Get the best and most recent location of the device, which may be null in rare
         * cases when a location is not available.
         */
        try {
            if (locationPermissionGranted) {
                Task<Location> locationResult = fusedLocationProviderClient.getLastLocation();
                locationResult.addOnCompleteListener(requireActivity(), new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful()) {
                            // Set the map's camera position to the current location of the device.
                            lastKnownLocation = task.getResult();
                            if (lastKnownLocation != null) {
                                        LatLng latlng = new LatLng(lastKnownLocation.getLatitude(),
                                                lastKnownLocation.getLongitude());
                                        userLocation = new GeoPoint(latlng.latitude, latlng.longitude);

                            }
                        } else {
                            Log.d(TAG, "Current location is null. Using defaults.");
                            Log.e(TAG, "Exception: %s", task.getException());
                            userLocation = new GeoPoint(0,0);
                            googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                        }
                    }
                });
            }
        } catch (SecurityException e)  {
            Log.e("Exception: %s", e.getMessage(), e);
        }
    }
    /**
     * Prompts the user to select the current place from a list of likely places, and shows the
     * current place on the map - provided the user has granted location permission.
     */
    private void showCurrentPlace() {
        if (googleMap == null) {
            return;
        }

        if (locationPermissionGranted) {
            // Use fields to define the data types to return.
            List<Place.Field> placeFields = Arrays.asList(Place.Field.NAME, Place.Field.ADDRESS,
                    Place.Field.LAT_LNG);

            // Use the builder to create a FindCurrentPlaceRequest.
            FindCurrentPlaceRequest request =
                    FindCurrentPlaceRequest.newInstance(placeFields);

            // Get the likely places - that is, the businesses and other points of interest that
            // are the best match for the device's current location.
            @SuppressWarnings("MissingPermission") final
            Task<FindCurrentPlaceResponse> placeResult =
                    placesClient.findCurrentPlace(request);
            placeResult.addOnCompleteListener (new OnCompleteListener<FindCurrentPlaceResponse>() {
                @Override
                public void onComplete(@NonNull Task<FindCurrentPlaceResponse> task) {
                    if (task.isSuccessful() && task.getResult() != null) {
                        FindCurrentPlaceResponse likelyPlaces = task.getResult();

                        // Set the count, handling cases where less than 5 entries are returned.
                        int count;
                        if (likelyPlaces.getPlaceLikelihoods().size() < MAX_ENTRIES) {
                            count = likelyPlaces.getPlaceLikelihoods().size();
                        } else {
                            count = MAX_ENTRIES;
                        }

                        int i = 0;
                        likelyPlaceNames = new String[count];
                        likelyPlaceAddresses = new String[count];
                        likelyPlaceAttributions = new List[count];
                        likelyPlaceLatLngs = new LatLng[count];

                        for (PlaceLikelihood placeLikelihood : likelyPlaces.getPlaceLikelihoods()) {
                            // Build a list of likely places to show the user.
                            likelyPlaceNames[i] = placeLikelihood.getPlace().getName();
                            likelyPlaceAddresses[i] = placeLikelihood.getPlace().getAddress();
                            likelyPlaceAttributions[i] = placeLikelihood.getPlace()
                                    .getAttributions();
                            likelyPlaceLatLngs[i] = placeLikelihood.getPlace().getLatLng();

                            i++;
                            if (i > (count - 1)) {
                                break;
                            }
                        }

                        // Show a dialog offering the user the list of likely places, and add a
                        // marker at the selected place.
                        GeolocationTracking.this.openPlacesDialog();
                    }
                    else {
                        Log.e(TAG, "Exception: %s", task.getException());
                    }
                }
            });
        } else {
            // The user has not granted permission.
            Log.i(TAG, "The user did not grant location permission.");

            // Add a default marker, because the user hasn't selected a place.
            googleMap.addMarker(new MarkerOptions()
                    .title(getString(R.string.default_info_title))
                    .position(defaultLocation)
                    .snippet(getString(R.string.default_info_snippet)));

            // Prompt the user for permission.
            requestLocationPermission();
        }
    }
    /**
     * Displays a form allowing the user to select a place from a list of likely places.
     */
    private void openPlacesDialog() {
        // Ask the user to choose the place where they are now.
        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // The "which" argument contains the position of the selected item.
                LatLng markerLatLng = likelyPlaceLatLngs[which];
                String markerSnippet = likelyPlaceAddresses[which];
                if (likelyPlaceAttributions[which] != null) {
                    markerSnippet = markerSnippet + "\n" + likelyPlaceAttributions[which];
                }

                // Add a marker for the selected place, with an info window
                // showing information about that place.
                googleMap.addMarker(new MarkerOptions()
                        .title(likelyPlaceNames[which])
                        .position(markerLatLng)
                        .snippet(markerSnippet));

                // Position the map's camera at the location of the marker.
                googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(markerLatLng,
                        DEFAULT_ZOOM));
            }
        };

        // Display the dialog.
        AlertDialog dialog = new AlertDialog.Builder(getContext())
                .setTitle("Pick a place")
                .setItems(likelyPlaceNames, listener)
                .show();
    }

    /**
     * Takes in an address (event location) and uses that to get a latitude and longitude
     * @param strAddress
     * An address for the event location represented as a string.
     * @return a geopoint that contains the latitude and longitude of an event.
     */
    public GeoPoint getLocationFromAddress(String strAddress){
            Geocoder coder = new Geocoder(requireContext());
            List<Address> address;
            GeoPoint point = null;
            try{
                address = coder.getFromLocationName(strAddress, MAX_ENTRIES);
                if (address == null){
                    return null;
                }
                Address location = address.get(0);
                location.getLatitude();
                location.getLongitude();
                point = new GeoPoint((double) (location.getLatitude()),
                        (double) (location.getLongitude()));
                return point;
            }catch (IOException e) {
            Log.e(TAG,"Address not found");
            }
            return null;
        }

    /**
     * Handles the result of the request for location permissions.
     */
    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        locationPermissionGranted = false;
        if (requestCode
                == FINE_LOCATION_PERMISSION) {// If request is cancelled, the result arrays are empty.
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                locationPermissionGranted = true;
            }
        } else {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
        updateLocationUI();
    }
    /**
     * This method will ask for permissions and also check if permissions are already granted
     *
     */
    public void requestLocationPermission() {
        // Request location permission
        if (ContextCompat.checkSelfPermission(requireActivity(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    FINE_LOCATION_PERMISSION);
        }
    }

    /**
     * Will set the user's settings for the user interface based on permissions.
     */
    private void updateLocationUI(){
        if (googleMap == null){
            return;
        }
        try{
            if(locationPermissionGranted){
                googleMap.setMyLocationEnabled(true);
                googleMap.getUiSettings().setMyLocationButtonEnabled(true);
            }else {
                googleMap.setMyLocationEnabled(false);
                googleMap.getUiSettings().setMyLocationButtonEnabled(false);
                lastKnownLocation = null;
                requestLocationPermission();
            }
        }catch(SecurityException e){
            Log.e("Exception: %s", e.getMessage());
        }
    }

    /**
     * A getter for the location of a user.
     * @param user
     * The user to get a location for.
     * @return
     * The location of the user.
     */
    public LatLng getLocation(User user){
        return new LatLng(lastKnownLocation.getLatitude(), lastKnownLocation.getLongitude());
    }
}

