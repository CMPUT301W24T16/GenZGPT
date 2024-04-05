package com.example.genzgpt.Controller;


import static android.app.PendingIntent.getActivity;
import static androidx.camera.core.impl.utils.ContextUtil.getApplicationFromContext;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;


import com.example.genzgpt.R;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import org.osmdroid.api.IMapController;
import org.osmdroid.tileprovider.tilesource.TileSourceFactory;
import org.osmdroid.util.GeoPoint;
import org.osmdroid.views.MapView;
import org.osmdroid.views.overlay.Marker;
import android.location.Geocoder;

import java.io.IOException;
import java.util.List;

/**
 * This class handles Geolocation Tracking for the user, includes permission requests and location pulls.
 */
public class GeolocationTracking extends AppCompatActivity implements OnMapReadyCallback {
    private MapView mapView;
    private FusedLocationProviderClient fusedLocationClient;
    private LocationManager locationManager;

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
     *
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        if(!(checkPermission(android.Manifest.permission.ACCESS_FINE_LOCATION))){
            requestPermission();
        }
        view = inflater.inflate(R.layout.map_view_fragment, container, false);
        return view;
    }
     */

    @SuppressLint("RestrictedApi")
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_view_fragment);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Request location permission
        if (ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(getParent(),
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    RequestCode.FINE_LOCATION_PERMISSION);
        } else {
            // Start retrieving the user's location
            //startLocationUpdates();
        }
        // Retrieve attendee locations from Firebase and add markers to the map

            //FIXME: Do this
        }
        @Override
        public void onMapReady(GoogleMap googleMap){
            googleMap.addMarker(new MarkerOptions()
                    .position(new LatLng(0, 0))
                    .title("Marker"));
        }
        /*
    private void startLocationUpdates() {
        locationManager = (LocationManager) requireContext().getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_FINE_LOCATION)
                    == PackageManager.PERMISSION_GRANTED) {
                locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, this);
            }
        }
    }

    private GeoPoint getLocationFromAddress(String address) {
        GeoPoint geoPoint = null;
        try {
            @SuppressLint("RestrictedAPI")
            List<Address> addresses = new Nominatim(getApplicationContext(requireActivity())).getFromLocationName(address, 1);
            if (addresses != null && !addresses.isEmpty()) {
                Address firstAddress = addresses.get(0);
                geoPoint = new GeoPoint(firstAddress.getLatitude(), firstAddress.getLongitude());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return geoPoint;
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
         */

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
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, RequestCode.FINE_LOCATION_PERMISSION);
    }

    /**
     * This method will check if permissions are granted or denied
     * @param permission
     * @return a boolean value
     */
    public boolean checkPermission(String permission){
        return ContextCompat.checkSelfPermission(getApplicationContext(), permission) == PackageManager.PERMISSION_GRANTED;
    }
}

