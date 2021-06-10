package de.ur.mi.android.demos.roadstoregensburg.sensors;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.os.Looper;

import androidx.annotation.NonNull;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

import de.ur.mi.android.demos.roadstoregensburg.helper.PermissionHelper;

public class BearingProvider {

    private final FusedLocationProviderClient fusedLocationClient;
    private final LocationCallback locationCallback;
    private final Context context;
    private final Location target;
    private final BearingProviderListener listener;

    public BearingProvider(Context context, Location target, BearingProviderListener listener) {
        this.context = context;
        this.target = target;
        this.listener = listener;
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(@NonNull LocationResult locationResult) {
                super.onLocationResult(locationResult);
                onLocationUpdate(locationResult);
            }
        };
    }


    @SuppressLint("MissingPermission")
    public void start() {
        if (PermissionHelper.permissionIsGranted(context, Manifest.permission.ACCESS_FINE_LOCATION)) {
            LocationRequest locationRequest = createLocationRequest();
            fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
        }
    }

    private LocationRequest createLocationRequest() {
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setInterval(5000);
        locationRequest.setFastestInterval(1000);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    public void stop() {
        fusedLocationClient.removeLocationUpdates(locationCallback);
    }

    private void onLocationUpdate(LocationResult locationResult) {
        Location currentLocation = locationResult.getLastLocation();
        if (currentLocation != null) {
            float bearingInDegrees = currentLocation.bearingTo(target);
            listener.onTargetBearingChange(bearingInDegrees);
        }
    }

    public interface BearingProviderListener {
        void onTargetBearingChange(float newTargetBearing);
    }
}
