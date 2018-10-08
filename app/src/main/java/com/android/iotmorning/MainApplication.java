package com.android.iotmorning;

import android.app.Application;

/**
 * Created by akhil on 8/10/18.
 */

public class MainApplication extends Application {
    private static RestClient restClient;

    public static RestClient getRestClient() {
        return restClient;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        restClient = new RestClient();
    }


}
