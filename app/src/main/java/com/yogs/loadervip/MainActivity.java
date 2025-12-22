package com.yogs.loadervip;

import android.Manifest;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.google.android.material.snackbar.Snackbar;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Environment;
import android.provider.Settings;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import org.lsposed.lsparanoid.Obfuscate;

import java.io.File;

import top.niunaijun.blackbox.BlackBoxCore;
import top.niunaijun.blackbox.app.configuration.ClientConfiguration;
import top.niunaijun.blackbox.entity.pm.InstallResult;
import top.niunaijun.blackbox.fake.service.IActivityManagerProxy;


@Obfuscate
public class MainActivity extends Activity {

    private AppBarConfiguration appBarConfiguration;
    private BlackBoxCore blackBoxCore;
    SharedPreferences preferences;
    private static final int REQUEST_MANAGE_STORAGE_PERMISSION = 100;
    private static final int REQUEST_MANAGE_UNKNOWN_APP_SOURCES = 200;
    public static String daemon64;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        try {

            if (!Settings.canDrawOverlays(this)) {
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, 123);
            }

            if (!isStoragePermissionGranted()) {
                requestStoragePermissionDirect();
            }

            if (!canRequestPackageInstalls()) {
                requestUnknownAppPermissionsDirect();
            }

            preferences = this.getSharedPreferences("install_status", this.MODE_PRIVATE);

            TextView apkStatus = findViewById(R.id.apkstatus);
            Button menugl = findViewById(R.id.menugl);
            Button menugl1 = findViewById(R.id.menugl1);

            daemon64 = this.getFilesDir() + "/sock64";

            menugl.setOnClickListener(view -> {
                installanduninstall();
            });

            menugl1.setOnClickListener(view -> {
                ApkEnv.getInstance().launchApk("com.tencent.ig");
                //Overlay.Shell("chmod 777 " + MainActivity.this.getFilesDir() + "/sock64");
                startService(new Intent(MainActivity.this, FloatLogo.class));
            });
        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private void installanduninstall() {
        if (ApkEnv.getInstance().isInstalled("com.tencent.ig")) {
            ApkEnv.getInstance().unInstallApp("com.tencent.ig");
            Toast.makeText(MainActivity.this, "Pubg Global Uninstalled", Toast.LENGTH_SHORT).show();
        } else {
            ApkEnv.getInstance().installByPackage("com.tencent.ig");

            if (!ApkEnv.getInstance().isInstalled("com.tencent.ig"))
                return;

            Toast.makeText(MainActivity.this, "Pubg Global Installed", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isStoragePermissionGranted() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.R || Environment.isExternalStorageManager();
    }

    private void requestStoragePermissionDirect() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION);
            intent.setData(Uri.fromParts("package", getPackageName(), null));
            startActivityForResult(intent, REQUEST_MANAGE_STORAGE_PERMISSION);
        } else {
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                    REQUEST_MANAGE_STORAGE_PERMISSION);
        }
    }

    private boolean canRequestPackageInstalls() {
        return Build.VERSION.SDK_INT < Build.VERSION_CODES.O
                || getPackageManager().canRequestPackageInstalls();
    }

    private void requestUnknownAppPermissionsDirect() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Intent intent = new Intent(Settings.ACTION_MANAGE_UNKNOWN_APP_SOURCES,
                    Uri.parse("package:" + getPackageName()));
            startActivityForResult(intent, REQUEST_MANAGE_UNKNOWN_APP_SOURCES);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        stopService(new Intent(MainActivity.this, Overlay.class));
        stopService(new Intent(MainActivity.this, FloatLogo.class));
    }

}