package com.example.genzgpt;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;

/**
 * This class handles Geolocation Tracking for the user, includes permission requests and location pulls.
 */
public class GeolocationTracking extends Activity {
    private FusedLocationProviderClient fusedLocationClient;
    private String[] permissions = {android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};

    /**
     * This class represents request codes for the specified permissions.
     */
    class RequestCode{
        static final int COARSE_LOCATION_PERMISSION = 100;
        static final int FINE_LOCATION_PERMISSION = 101;
    }

    /**
     * This method is an onCreate for the activity
     * @param savedInstanceState If the activity is being re-initialized after
     *     previously being shut down then this Bundle contains the data it most
     *     recently supplied in {@link #onSaveInstanceState}.  <b><i>Note: Otherwise it is null.</i></b>
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            //Permission is not granted, so request it.
            checkPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION, GeolocationTracking.RequestCode.COARSE_LOCATION_PERMISSION);
            checkPermission(android.Manifest.permission.ACCESS_FINE_LOCATION, GeolocationTracking.RequestCode.FINE_LOCATION_PERMISSION);
        } else {
            fusedLocationClient.getLastLocation().addOnSuccessListener(this, new OnSuccessListener<Location>() {
                /**
                 * Method that gets the location if it is successful, if not successful throw exception
                 * @param location
                 */
                @Override
                public void onSuccess(Location location) {
                    if (location != null) {
                        double latitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        String position = location.toString();
                        String locationString = ("Latitude: " + latitude + "| Longitude: " + longitude);
                    }else{
                        throw new NullPointerException("Location turned off");
                    }
                }
            });
        }
    }

    /**
     * This method will output the status of the permission request as a Toast prompt
     * @param requestCode The request code passed in {@link #requestPermissions(String[], int)}.
     * @param permissions The requested permissions. Never null.
     * @param grantResults The grant results for the corresponding permissions
     *     which is either {@link android.content.pm.PackageManager#PERMISSION_GRANTED}
     *     or {@link android.content.pm.PackageManager#PERMISSION_DENIED}. Never null.
     *
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == GeolocationTracking.RequestCode.COARSE_LOCATION_PERMISSION || requestCode == GeolocationTracking.RequestCode.FINE_LOCATION_PERMISSION){
            if (grantResults.length > 0){
                for (int i = 0; i < grantResults.length; i++){
                    if (grantResults[i] == PackageManager.PERMISSION_GRANTED){
                        Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show();

                    }else{
                        Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show();
                    }
                }
            } else{
                Toast.makeText(this, "Please select a permission", Toast.LENGTH_SHORT).show();
                checkPermission(android.Manifest.permission.ACCESS_COARSE_LOCATION, GeolocationTracking.RequestCode.COARSE_LOCATION_PERMISSION);
                checkPermission(android.Manifest.permission.ACCESS_FINE_LOCATION, GeolocationTracking.RequestCode.FINE_LOCATION_PERMISSION);
            }
        }
    }

    /**
     * This method will ask for permissions if they are not already granted.
     * @param permission
     * @param requestCode
     */
    public void checkPermission(String permission, int requestCode){
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, permissions, requestCode);
        }
        else{
            Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }
}

