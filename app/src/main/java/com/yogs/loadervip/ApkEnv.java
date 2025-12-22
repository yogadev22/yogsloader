package com.yogs.loadervip;

import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.widget.Toast;
import java.io.File;

import org.lsposed.lsparanoid.Obfuscate;

import top.niunaijun.blackbox.BlackBoxCore;
import top.niunaijun.blackbox.core.env.BEnvironment;
import top.niunaijun.blackbox.entity.pm.InstallResult;
import android.util.Log;
import android.widget.Toast;



@Obfuscate
public class ApkEnv {
    private static ApkEnv singleton;

    public static ApkEnv getInstance() {
        if (singleton == null) {
            singleton = new ApkEnv();
        }
        return singleton;
    }

    public ApplicationInfo getApplicationInfo(String packageName) {
    ApplicationInfo applicationInfo = null;
    try {
        applicationInfo = BoxApplication.get()
                .getPackageManager()
                .getApplicationInfo(packageName, 0);
    } catch (PackageManager.NameNotFoundException err) {
        Toast.makeText(
                BoxApplication.get(),
                err.getMessage(),
                Toast.LENGTH_SHORT
        ).show();
        return null;
    }
    return applicationInfo;
}

    public boolean isInstalled(String packageName) {
        return BlackBoxCore.get().isInstalled(packageName, 0);
    }

  /*  public boolean isRunning(String packageName) {
        return BlackBoxCore.get().isInstalled(packageName, 0);
    }*/

public boolean isRunning(String packageName) {
    return false;
}

    
     // FIXED: Reference style install logic (install via packageName)
    public boolean installBySystem(String packageName) {
        InstallResult result = BlackBoxCore.get().installPackageAsUser(packageName, 0);
        return result.success;
    }

    // Original installByFile method untouched
    public boolean installByFile(String packageName) {
        ApplicationInfo applicationInfo = getApplicationInfo(packageName);
        if (applicationInfo == null) return false;

        InstallResult result = BlackBoxCore.get().installPackageAsUser(applicationInfo.sourceDir, 0);
        return result.success;
    }

    public boolean installByPackage(String packageName) {
        InstallResult result = BlackBoxCore.get().installPackageAsUser(packageName, 0);
        return result.success;
    }
    
    
    
    
    public void unInstallApp(String packageName) {
        BlackBoxCore.get().uninstallPackageAsUser(packageName, 0);
    }

    public void stopRunningApp(String packageName) {
        BlackBoxCore.get().stopPackage(packageName, 0);
    }
    
    
    

    public File getObbContainerPath(String packageName) {
        return new File(BEnvironment.getExternalUserDir(0), "Android/obb/" + packageName);
    }


 
    
    
   public void launchApk(String packageName) {
    if (!isInstalled(packageName)) {
        Toast.makeText(BoxApplication.get(), "Client not installed", Toast.LENGTH_SHORT).show();
        return;
    }
    try {
        Log.d("LAUNCH_APP", "Launching app: " + packageName);
        BlackBoxCore.get().launchApk(packageName, 0);
    } catch (Exception e) {
        Log.e("LAUNCH_APP", "Launch error: " + e.getMessage());
        Toast.makeText(BoxApplication.get(), "Launch failed: " + e.getMessage(), Toast.LENGTH_LONG).show();
    }
}

    
    
    
    
    
    
}
