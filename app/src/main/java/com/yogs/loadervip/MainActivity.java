package com.yogs.loadervip;

import android.Manifest;
import android.app.Activity;
import android.app.ActivityManager;
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

            Button menugl = findViewById(R.id.menugl);
            Button menugl1 = findViewById(R.id.menugl1);

            updateState(menugl);

            menugl.setOnClickListener(view -> {
                if (installanduninstall("com.tencent.ig")) {
                    updateState(menugl);
                }
            });

            menugl1.setOnClickListener(view -> {
                BlackBoxCore.get().launchApk("com.tencent.ig", 0);
                if (!isServiceRunning()) {
                    startService(new Intent(MainActivity.this, FloatLogo.class));
                } else {
                    Toast.makeText(MainActivity.this, "Service is already running", Toast.LENGTH_SHORT).show();
                }
            });

            Button menukr = findViewById(R.id.menukr);
            Button menukr1 = findViewById(R.id.menukr1);

            updateState(menukr);

            menukr.setOnClickListener(view -> {
                if (installanduninstall("com.pubg.krmobile")) {
                    updateState(menukr);
                }
            });

            menukr1.setOnClickListener(view -> {
                BlackBoxCore.get().launchApk("com.pubg.krmobile", 0);
                if (!isServiceRunning()) {
                    startService(new Intent(MainActivity.this, FloatLogo.class));
                } else {
                    Toast.makeText(MainActivity.this, "Service is already running", Toast.LENGTH_SHORT).show();
                }
            });

            Button menutw = findViewById(R.id.menukr);
            Button menutw1 = findViewById(R.id.menukr1);

            updateState(menutw);

            menutw.setOnClickListener(view -> {
                if (installanduninstall("com.rekoo.pubgm")) {
                    updateState(menutw);
                }
            });

            menutw1.setOnClickListener(view -> {
                BlackBoxCore.get().launchApk("com.rekoo.pubgm", 0);
                if (!isServiceRunning()) {
                    startService(new Intent(MainActivity.this, FloatLogo.class));
                } else {
                    Toast.makeText(MainActivity.this, "Service is already running", Toast.LENGTH_SHORT).show();
                }
            });

            Button menuvng = findViewById(R.id.menuvng);
            Button menuvng1 = findViewById(R.id.menuvng1);

            updateState(menuvng);

            menuvng.setOnClickListener(view -> {
                if (installanduninstall("com.vng.pubgmobile")) {
                    updateState(menuvng);
                }
            });

            menuvng1.setOnClickListener(view -> {
                BlackBoxCore.get().launchApk("com.vng.pubgmobile", 0);
                if (!isServiceRunning()) {
                    startService(new Intent(MainActivity.this, FloatLogo.class));
                } else {
                    Toast.makeText(MainActivity.this, "Service is already running", Toast.LENGTH_SHORT).show();
                }
            });

        } catch (Exception e) {
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isServiceRunning() {
        ActivityManager manager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        if (manager != null) {
            for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(Integer.MAX_VALUE)) {
                if (FloatLogo.class.getName().equals(service.service.getClassName())) {
                    return true;
                }
            }
        }
        return false;
    }

    private boolean installanduninstall(String packageName) {
        if (BlackBoxCore.get().isInstalled(packageName, 0)) {
            BlackBoxCore.get().uninstallPackageAsUser(packageName, 0);
            Toast.makeText(MainActivity.this, packageName + " Uninstalled", Toast.LENGTH_SHORT).show();
            return true;
        } else {
            InstallResult installResult = BlackBoxCore.get().installPackageAsUser(packageName, 0);
            if (installResult.success || !BlackBoxCore.get().isInstalled(packageName, 0)) {
                Toast.makeText(MainActivity.this, packageName + " Installed", Toast.LENGTH_SHORT).show();
                return true;
            }
        }
        return false;
    }

    private void updateState(Button haha) {
        if (BlackBoxCore.get().isInstalled("com.tencent.ig", 0)) {
            haha.setText("UNINSTALL");
        } else {
            haha.setText("INSTALL");
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