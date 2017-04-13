package com.sadwyn.iceandfire;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.sadwyn.iceandfire.components.ComponentDagger;
import com.sadwyn.iceandfire.components.DaggerComponentDagger;
import com.sadwyn.iceandfire.data.DatabaseManager;
import com.sadwyn.iceandfire.data.HeroesDbHelper;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;

import io.fabric.sdk.android.Fabric;


public class App extends Application {
    private static ComponentDagger componentDagger;

    private RefWatcher refWatcher;

    public static RefWatcher getRefWatcher(Context context) {
        App application = (App) context.getApplicationContext();
        return application.refWatcher;
    }

    public static ComponentDagger getComponentDagger() {
        return componentDagger;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        componentDagger = DaggerComponentDagger.builder().build();

        if (getResources().getBoolean(R.bool.isDebug)) {
            if (LeakCanary.isInAnalyzerProcess(this)) {
                return;
            } else {
                refWatcher = LeakCanary.install(this);
                RefWatcher refWatcher = App.getRefWatcher(this);
                refWatcher.watch(this);
            }
        }
        DatabaseManager.initializeInstance(new HeroesDbHelper(getApplicationContext()));
        Fabric.with(this, new Crashlytics());
    }
}
