package com.sadwyn.iceandfire;

import android.app.Application;
import android.content.Context;

import com.crashlytics.android.Crashlytics;
import com.sadwyn.iceandfire.activities.MainActivity;
import com.sadwyn.iceandfire.data.DatabaseManager;
import com.sadwyn.iceandfire.data.HeroesDbHelper;
import com.sadwyn.iceandfire.fragments.CharactersFragment;
import com.sadwyn.iceandfire.fragments.DetailFragment;
import com.sadwyn.iceandfire.modules.ApiModule;
import com.sadwyn.iceandfire.modules.PresenterModule;
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

        componentDagger = DaggerComponentDagger.builder()
                .apiModule(new ApiModule())
                .presenterModule(new PresenterModule(getApplicationContext(), CharactersFragment.newInstance() ))
                .build();

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
