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

public class GeolocationTracking extends Activity {
    private FusedLocationProviderClient fusedLocationClient;
    private String[] permissions = {android.Manifest.permission.ACCESS_COARSE_LOCATION, Manifest.permission.ACCESS_FINE_LOCATION};
    class RequestCode{
        static final int COARSE_LOCATION_PERMISSION = 100;
        static final int FINE_LOCATION_PERMISSION = 101;
    }
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
    public void checkPermission(String permission, int requestCode){
        if (ContextCompat.checkSelfPermission(this, permission) == PackageManager.PERMISSION_DENIED){
            ActivityCompat.requestPermissions(this, permissions, requestCode);
        }
        else{
            Toast.makeText(this, "Permission already granted", Toast.LENGTH_SHORT).show();
        }
    }
}

