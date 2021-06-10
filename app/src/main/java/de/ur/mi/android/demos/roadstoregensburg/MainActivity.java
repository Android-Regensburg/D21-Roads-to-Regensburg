package de.ur.mi.android.demos.roadstoregensburg;

import android.Manifest;
import android.animation.LayoutTransition;
import android.content.Context;
import android.hardware.SensorManager;
import android.location.Location;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import de.ur.mi.android.demos.roadstoregensburg.helper.PermissionHelper;
import de.ur.mi.android.demos.roadstoregensburg.sensors.BearingProvider;
import de.ur.mi.android.demos.roadstoregensburg.sensors.OrientationProvider;

public class MainActivity extends AppCompatActivity implements OrientationProvider.OrientationProviderListener, BearingProvider.BearingProviderListener {

    private static final double[] UNI_REGENSBURG_KUGEL_COORDINATES = {48.9982709d, 12.0946318d};

    private OrientationProvider orientationProvider;
    private BearingProvider bearingProvider;
    private ImageView mainCompassView;
    private ImageView bavarianCompassView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        initUI();
        initOrientationSensor();
        requestLocationPermission();
    }

    @Override
    protected void onResume() {
        super.onResume();
        try {
            orientationProvider.start();
            bearingProvider.start();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        try {
            orientationProvider.stop();
            bearingProvider.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void initUI() {
        setContentView(R.layout.activity_main);
        enableSmoothLayoutTransition();
        mainCompassView = findViewById(R.id.main_compass_view);
        bavarianCompassView = findViewById(R.id.bavarian_compass_view);
    }

    private void enableSmoothLayoutTransition() {
        FrameLayout compassFrame = findViewById(R.id.compass_frame_view);
        LayoutTransition transition = new LayoutTransition();
        transition.enableTransitionType(LayoutTransition.CHANGING);
        compassFrame.setLayoutTransition(transition);
        compassFrame.setLayoutTransition(transition);
    }

    private void initOrientationSensor() {
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        orientationProvider = new OrientationProvider(sensorManager, this);
    }

    private void initBearingProvider() {
        Location targetLocation = new Location("");
        targetLocation.setLatitude(UNI_REGENSBURG_KUGEL_COORDINATES[0]);
        targetLocation.setLongitude(UNI_REGENSBURG_KUGEL_COORDINATES[1]);
        bearingProvider = new BearingProvider(getApplicationContext(), targetLocation, this);
        bearingProvider.start();
    }


    private void requestLocationPermission() {
        PermissionHelper.requestPermission(this, Manifest.permission.ACCESS_FINE_LOCATION, new PermissionHelper.PermissionRequestListener() {
            @Override
            public void onGranted() {
                initBearingProvider();
            }

            @Override
            public void onDenied() {
                bavarianCompassView.setVisibility(View.GONE);
            }
        });
    }

    @Override
    public void onAzimuthChange(float azimuth) {
        mainCompassView.setRotation(-azimuth);
    }

    @Override
    public void onTargetBearingChange(float newTargetBearing) {
        bavarianCompassView.setRotation(newTargetBearing);
    }
}