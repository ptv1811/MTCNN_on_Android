package com.example.mtcnn_demo;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    private String TAG = "MainActivity";
    private int REQUEST_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        if (Constants.INSTANCE.getLOG_DEBUG())
            Log.i(TAG, "into MainActivity");

        if (hasPermission()) {
            if (null == savedInstanceState) {
                setFragment();
            }
        } else {
            requestPermission();
        }
    }

    private void requestPermission() {
        if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)){
            Toast.makeText(this, "Camera permission are required for this demo", Toast.LENGTH_LONG).show();
        }
        requestPermissions(new String[]{Manifest.permission.CAMERA}, REQUEST_PERMISSION);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == REQUEST_PERMISSION){
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                setFragment();
            }
            else{
                requestPermission();
            }
        }
        else{
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        }
    }

    private void setFragment() {
        getSupportFragmentManager().beginTransaction().replace(R.id.container, CameraConnectFragment.newInstance())
                .commitAllowingStateLoss();
    }

    private boolean hasPermission() {
        return checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED;
    }
}