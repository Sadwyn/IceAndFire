package com.sadwyn.iceandfire;

import android.app.Application;
import android.content.Context;

import com.sadwyn.iceandfire.data.DatabaseManager;
import com.sadwyn.iceandfire.data.HeroesDbHelper;
import com.sadwyn.iceandfire.fragments.CharactersFragment;
import com.sadwyn.iceandfire.modules.ApiModule;
import com.sadwyn.iceandfire.modules.CharactersPresenterModule;
import com.sadwyn.iceandfire.modules.SettingsPresenterModule;
import com.squareup.leakcanary.LeakCanary;
import com.squareup.leakcanary.RefWatcher;


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
                .charactersPresenterModule(new CharactersPresenterModule(getApplicationContext(), CharactersFragment.newInstance()))
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
        //Fabric.with(this, new Crashlytics());
    }
}
