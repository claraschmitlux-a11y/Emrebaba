package com.emailextractor;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity extends Activity {
    private Button btnStartService, btnStopService, btnPermission;
    private static final int OVERLAY_PERMISSION_REQ_CODE = 1;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnStartService = findViewById(R.id.btnStartService);
        btnStopService = findViewById(R.id.btnStopService);
        btnPermission = findViewById(R.id.btnPermission);

        btnStartService.setOnClickListener(v -> {
            if (checkOverlayPermission()) {
                startFloatingService();
            } else {
                requestOverlayPermission();
            }
        });

        btnStopService.setOnClickListener(v -> stopFloatingService());

        btnPermission.setOnClickListener(v -> requestOverlayPermission());
    }

    private boolean checkOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            return Settings.canDrawOverlays(this);
        }
        return true;
    }

    private void requestOverlayPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            Intent intent = new Intent(
                Settings.ACTION_MANAGE_OVERLAY_PERMISSION,
                Uri.parse("package:" + getPackageName())
            );
            startActivityForResult(intent, OVERLAY_PERMISSION_REQ_CODE);
        }
    }

    private void startFloatingService() {
        Intent intent = new Intent(this, FloatingService.class);
        startService(intent);
        Toast.makeText(this, "Floating buton başlatıldı", Toast.LENGTH_SHORT).show();
        finish();
    }

    private void stopFloatingService() {
        Intent intent = new Intent(this, FloatingService.class);
        stopService(intent);
        Toast.makeText(this, "Floating buton durduruldu", Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == OVERLAY_PERMISSION_REQ_CODE) {
            if (checkOverlayPermission()) {
                startFloatingService();
            } else {
                Toast.makeText(this, "İzin gerekli", Toast.LENGTH_LONG).show();
            }
        }
    }
}