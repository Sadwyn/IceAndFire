package com.sadwyn.iceandfire;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.sadwyn.iceandfire.data.HeroesDataContract;
import com.sadwyn.iceandfire.data.HeroesDbHelper;
import com.sadwyn.iceandfire.network.Api;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import io.fabric.sdk.android.Fabric;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;


public class App extends Application {
    private static Api api;
    private boolean isLoggerEnabled = false;
    private RefWatcher refWatcher;


    public static RefWatcher getRefWatcher(Context context) {
       App application = (App) context.getApplicationContext();
        return application.refWatcher;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        if(getResources().getBoolean(R.bool.isDebug)) {
            if (LeakCanary.isInAnalyzerProcess(this)) {
                return;
            } else {
                refWatcher = LeakCanary.install(this);
                RefWatcher refWatcher = App.getRefWatcher(this);
                refWatcher.watch(this);
            }
        }
        Fabric.with(this, new Crashlytics());

        HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
        logging.setLevel(HttpLoggingInterceptor.Level.BASIC);
        OkHttpClient logger;
        if(isLoggerEnabled) {
             logger = new OkHttpClient.Builder()
                    .addInterceptor(logging)
                    .build();
        }
        else {
             logger = new OkHttpClient.Builder()
                .build();
        }

        Retrofit retrofit = new Retrofit.Builder()
                .client(logger)
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        api = retrofit.create(Api.class);
    }


    public static Api getApi() {
        return api;
    }
}
