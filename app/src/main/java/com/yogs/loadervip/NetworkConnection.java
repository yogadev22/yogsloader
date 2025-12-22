package com.yogs.loadervip;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.widget.Toast;

public class NetworkConnection {

    public static class CheckInternet {
        Context context;
        boolean isShow = false;

        public CheckInternet(Context ctx) {
            context = ctx;
        }

        public void registerNetworkCallback() {
            try {
                ConnectivityManager connectivityManager =
                        (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkRequest.Builder builder = new NetworkRequest.Builder();
                connectivityManager.registerDefaultNetworkCallback(
                        new ConnectivityManager.NetworkCallback() {
                            @Override
                            public void onAvailable(Network network) {
                                isShow = false;
                                BoxApplication.get().setInternetAvailable(true);
                            }

                            @Override
                            public void onLost(Network network) {
                                BoxApplication.get().setInternetAvailable(false);
                                if (!isShow) {
                                    // 🔹 Default Android Toast instead of TastyToast
                                    Toast.makeText(context,
                                            "No Internet Connection",
                                            Toast.LENGTH_LONG).show();
                                    isShow = true;
                                }
                            }
                        });
                BoxApplication.get().setInternetAvailable(false);
            } catch (Exception e) {
                BoxApplication.get().setInternetAvailable(false);
            }
        }
    }
}