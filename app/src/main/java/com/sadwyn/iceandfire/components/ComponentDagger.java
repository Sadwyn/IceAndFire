package com.sadwyn.iceandfire.components;

import com.sadwyn.iceandfire.fragments.SettingsFragment;
import com.sadwyn.iceandfire.models.CharacterModelImpl;
import com.sadwyn.iceandfire.modules.ApiModule;
import com.sadwyn.iceandfire.modules.SettingsPresenterModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApiModule.class, SettingsPresenterModule.class})
public interface ComponentDagger {
    void inject(CharacterModelImpl model);
    void inject(SettingsFragment settingsFragment);
}
