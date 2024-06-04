package com.example.loginapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.os.BatteryManager;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private EditText passwordEditText;
    private Button loginButton;

    private FusedLocationProviderClient fusedLocationClient;
    private LocationCallback locationCallback;
    private Location currentLocation;

    private ActivityResultLauncher<String[]> locationPermissionRequest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        passwordEditText = findViewById(R.id.passwordEditText);
        loginButton = findViewById(R.id.loginButton);

        // Initialize FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Initialize location permission request
        locationPermissionRequest = registerForActivityResult(new ActivityResultContracts.RequestMultiplePermissions(), result -> {
            Boolean fineLocationGranted = getPermissionResult(result, Manifest.permission.ACCESS_FINE_LOCATION);
            Boolean coarseLocationGranted = getPermissionResult(result, Manifest.permission.ACCESS_COARSE_LOCATION);

            if (fineLocationGranted != null && fineLocationGranted) {
                Toast.makeText(this, "Fine Location Permission Granted", Toast.LENGTH_SHORT).show();
                requestLocationUpdates();
            } else if (coarseLocationGranted != null && coarseLocationGranted) {
                Toast.makeText(this, "Coarse Location Permission Granted", Toast.LENGTH_SHORT).show();
                requestLocationUpdates();
            } else {
                Toast.makeText(this, "Location Permission Denied", Toast.LENGTH_SHORT).show();
            }
        });

        // Check and request necessary permissions
        checkPermissions();

        // Set click listener for login button
        loginButton.setOnClickListener(v -> {
            if (checkConditions()) {
                Toast.makeText(MainActivity.this, "Login Successful", Toast.LENGTH_SHORT).show();
                // Navigate to HomeActivity on successful login
                Intent intent = new Intent(MainActivity.this, HomeActivity.class);
                startActivity(intent);
            } else {
                Toast.makeText(MainActivity.this, "Login Failed", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void checkPermissions() {
        // Check and request location permissions
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_FINE_LOCATION) ||
                    ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                new AlertDialog.Builder(this)
                        .setTitle("Location Permission Needed")
                        .setMessage("This app needs location permissions to check if the device is in the Northern Hemisphere.")
                        .setPositiveButton("OK", (dialog, which) -> locationPermissionRequest.launch(new String[]{
                                Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION
                        }))
                        .setNegativeButton("Cancel", (dialog, which) -> dialog.dismiss())
                        .create().show();
            } else {
                locationPermissionRequest.launch(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION
                });
            }
        } else {
            // Permissions already granted
            requestLocationUpdates();
        }
    }

    private void requestLocationUpdates() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED &&
                ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // Permissions are not granted, return early
            return;
        }

        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(10000);
        locationRequest.setFastestInterval(5000);

        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                if (locationResult == null) {
                    return;
                }
                for (Location location : locationResult.getLocations()) {
                    currentLocation = location;
                }
            }
        };

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, null);
    }

    private boolean checkConditions() {
        // Check all login conditions
        boolean batteryCondition = checkBatteryCondition();
        boolean orientationCondition = checkOrientationCondition();
        boolean locationCondition = checkLocationCondition();

        return batteryCondition && orientationCondition && locationCondition;
    }

    private boolean checkBatteryCondition() {
        // Check if entered password matches battery percentage
        BatteryManager bm = (BatteryManager) getSystemService(BATTERY_SERVICE);
        int batteryLevel = bm.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
        String password = passwordEditText.getText().toString();

        return String.valueOf(batteryLevel).equals(password);
    }

    private boolean checkOrientationCondition() {
        // Check if phone is in portrait mode
        int orientation = getResources().getConfiguration().orientation;
        return orientation == Configuration.ORIENTATION_PORTRAIT;
    }

    private boolean checkLocationCondition() {
        // Check if device is in Northern Hemisphere (latitude > 0)
        if (currentLocation == null) {
            Toast.makeText(this, "Location Not Found", Toast.LENGTH_SHORT).show();
            return false;
        }
        double latitude = currentLocation.getLatitude();
        return latitude > 0;
    }

    private Boolean getPermissionResult(@NonNull Map<String, Boolean> result, String permission) {
        return result.containsKey(permission) ? result.get(permission) : false;
    }
}
