package com.yogs.loadervip;

import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import androidx.appcompat.app.AppCompatDelegate;
import com.google.android.material.color.DynamicColors;
import com.topjohnwu.superuser.Shell;

import java.io.IOException;


import top.niunaijun.blackbox.BlackBoxCore;
import top.niunaijun.blackbox.app.configuration.AppLifecycleCallback;
import top.niunaijun.blackbox.app.configuration.ClientConfiguration;


public class BoxApplication extends Application {

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);

        if (!Shell.rootAccess()) {
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
                public boolean isEnableDaemonService() {
                    return true;
                }
            });
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();

        if (!Shell.rootAccess()) {
            BlackBoxCore.get().doCreate();
        }
    }
}
