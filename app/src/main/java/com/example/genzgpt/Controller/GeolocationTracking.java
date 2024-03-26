package com.example.genzgpt.Controller;


import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.example.genzgpt.Model.AppUser;
import com.example.genzgpt.Model.User;
import com.example.genzgpt.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;

/**
 * This class handles Geolocation Tracking for the user, includes permission requests and location pulls.
 */
public class GeolocationTracking extends Fragment implements LocationListener {
    private MapView mapView;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationManager locationManager;
    private User userCurrent;
    private Firebase firebase;


    /**
     * This class represents request codes for the specified permissions.
     */
    public class RequestCode{
        public static final int FINE_LOCATION_PERMISSION = 101;
    }

    /**
     * This method is an onCreate for the activity
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        firebase = new Firebase();
        firebase.getUserData(AppUser.getUserId(), new Firebase.OnUserLoadedListener() {
            @Override
            public void onUserLoaded(User user) {
                userCurrent = user;
            }

            @Override
            public void onUserNotFound() {
                Log.d("Firebase", "User not found.");
            }

            @Override
            public void onUserLoadFailed(Exception e) {
                Log.e("Firebase", "User retrieval failed.");
            }
        });
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        if (checkPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) && userCurrent.isGeolocation()){
            view = inflater.inflate(R.layout.map_view_fragment, container, false);
        }else if(checkPermission(Manifest.permission.ACCESS_FINE_LOCATION) && !userCurrent.isGeolocation()){
            Toast.makeText(getContext(), "Geolocation is disabled. It can be enabled in your settings.", Toast.LENGTH_SHORT).show();
            view = inflater.inflate(R.layout.map_view_fragment, container, false);
        }else{
            requestPermission();
            view = inflater.inflate(R.layout.map_view_fragment, container, false);
        }
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mapView = view.findViewById(R.id.mapView);
        mapView.setTileSource(TileSourceFactory.MAPNIK);
        mapView.setMultiTouchControls(true);

        // Request location permission
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(requireActivity(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    RequestCode.FINE_LOCATION_PERMISSION);
        } else {
            // Start retrieving the user's location
            startLocationUpdates();
        }
        // Retrieve attendee locations from Firebase and add markers to the map

            //FIXME: Do this
        }
    private void startLocationUpdates() {
        locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            }
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        // Update map center with the user's current location
        GeoPoint userLocation = new GeoPoint(location.getLatitude(), location.getLongitude());
        IMapController mapController = mapView.getController();
        mapController.setCenter(userLocation);
    }

        private void addMarkerToMap(double latitude, double longitude, String title) {
            Marker marker = new Marker(mapView);
            marker.setPosition(new GeoPoint(latitude, longitude));
            marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM);
            marker.setTitle(title);
            mapView.getOverlays().add(marker);
            mapView.invalidate();
        }

        @Override
        public void onResume() {
            super.onResume();
            mapView.onResume();
        }

        @Override
        public void onPause() {
            super.onPause();
            mapView.onPause();
        }
    /**
     * This method will ask for permissions.
     *
     */
    public void requestPermission(){
            ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, RequestCode.FINE_LOCATION_PERMISSION);
    }

    /**
     * This method will check if permissions are granted or denied
     * @param permission
     * @return a boolean value
     */
    public boolean checkPermission(String permission){
        return ContextCompat.checkSelfPermission(getActivity(), permission) == PackageManager.PERMISSION_GRANTED;
    }
    /**
     * Gets the user data from the firebase
     * @param user
     */

}

