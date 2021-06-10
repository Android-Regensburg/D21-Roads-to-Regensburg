package de.ur.mi.android.demos.roadstoregensburg.helper;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;

import androidx.activity.ComponentActivity;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.core.app.ActivityCompat;

public class PermissionHelper {

    public static void requestPermission(ComponentActivity activity, String permission, PermissionRequestListener listener) {
        ActivityResultLauncher<String> requestPermissionLauncher = activity.registerForActivityResult(new ActivityResultContracts.RequestPermission(), new ActivityResultCallback<Boolean>() {
            @Override
            public void onActivityResult(Boolean result) {
                if (result) {
                    listener.onGranted();
                } else {
                    listener.onDenied();
                }
            }
        });
        requestPermissionLauncher.launch(Manifest.permission.ACCESS_FINE_LOCATION);
    }

    public static boolean permissionIsGranted(Context context, String permission) {
        return ActivityCompat.checkSelfPermission(context, permission) == PackageManager.PERMISSION_GRANTED;
    }


    public interface PermissionRequestListener {
        void onGranted();

        void onDenied();
    }

}
