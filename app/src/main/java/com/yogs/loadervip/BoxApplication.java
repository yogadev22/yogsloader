package com.yogs.loadervip;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import androidx.appcompat.app.AppCompatDelegate;
import com.google.android.material.color.DynamicColors;
import java.io.IOException;


import top.niunaijun.blackbox.BlackBoxCore;
import top.niunaijun.blackbox.app.configuration.AppLifecycleCallback;
import top.niunaijun.blackbox.app.configuration.ClientConfiguration;


public class BoxApplication extends Application {
    public static final String STATUS_BY = "online";
    private native String BoxApp();
    public static BoxApplication gApp;
    
    private boolean isNetworkConnected = false;
    
    public static BoxApplication get() {
        return gApp;
    }
    
    public boolean isInternetAvailable() {
        return isNetworkConnected;
    }
    
    public void setInternetAvailable(boolean b) {
        isNetworkConnected = b;
    }
    
    static {
        try {
            System.loadLibrary("Starkskillz");
        } catch (Exception w) {
            w.printStackTrace();
        }
    }
    
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //Thread.setDefaultUncaughtExceptionHandler(new CrashHandler(base));
        try {
            BlackBoxCore.get().doAttachBaseContext(base, new ClientConfiguration() {
                @Override
                public String getHostPackageName() {
                    return base.getPackageName();
                }
                @Override
                public boolean isHideRoot() {
                    return true;
                }

                @Override
                public boolean isHideXposed() {
                    return true;
                }

                @Override
                public boolean isEnableDaemonService(){
                    return true;
                }
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        
    }

    @Override
    public void onCreate() {
        super.onCreate();
        gApp = this;
      
        BlackBoxCore.get().doCreate();

        DynamicColors.applyToActivitiesIfAvailable(this);
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        NetworkConnection.CheckInternet network = new NetworkConnection.CheckInternet(this);
        network.registerNetworkCallback();
    }
    
    
    
    /*public static boolean checkRootAccess() {
        if (Overlay.rootAccess()){
            FLog.info("Root granted");
            return true;
        } else {
            FLog.info("Root not granted");
            return false;
        }
    }*/
    
    public static void doExe(String shell) {
        /*if (checkRootAccess()) {
            Overlay.Shell(shell);
        } else {*/
            try {
                Runtime.getRuntime().exec(shell);
            } catch (Exception e) {
                e.printStackTrace();
            }
        //}
    }
    
    public void doExecute(String shell) {
        doChmod(shell, 777);
        doExe(shell);
    }

    public static void doChmod(String shell, int mask) {
        doExe("chmod " + mask + " " + shell);
    }
}
