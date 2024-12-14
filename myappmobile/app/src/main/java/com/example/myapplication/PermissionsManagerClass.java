package com.example.myapplication;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionsManagerClass {

    private static final int REQUEST_CODE_WRITE_STORAGE = 100;

    public static void requestWritePermission(AppCompatActivity activity) {
        // Vérifier si l'application fonctionne sur Android 10 ou supérieur
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            // Pas besoin de demander la permission WRITE_EXTERNAL_STORAGE dans la plupart des cas
            Toast.makeText(activity, "Scoped Storage is in effect on Android 10 and above", Toast.LENGTH_SHORT).show();
        } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    != PackageManager.PERMISSION_GRANTED) {
                // Si la permission n'est pas accordée, la demander
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                        REQUEST_CODE_WRITE_STORAGE);
            }
        }
    }

    public static boolean isWritePermissionGranted(AppCompatActivity activity) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return ContextCompat.checkSelfPermission(activity, Manifest.permission.WRITE_EXTERNAL_STORAGE)
                    == PackageManager.PERMISSION_GRANTED;
        } else {
            // Pour les versions Android inférieures à 6.0, supposer que les permissions sont accordées

            return true;
        }
    }
}
