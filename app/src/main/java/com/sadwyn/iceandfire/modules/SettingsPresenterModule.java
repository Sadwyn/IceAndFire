package com.sadwyn.iceandfire.modules;

import com.sadwyn.iceandfire.presenters.SettingsFragmentPresenter;

import dagger.Module;
import dagger.Provides;

@Module
public class SettingsPresenterModule {

    @Provides
    SettingsFragmentPresenter provideSettingsPresenter(){
        return new SettingsFragmentPresenter();
    }
}
