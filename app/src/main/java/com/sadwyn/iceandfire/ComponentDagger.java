package com.sadwyn.iceandfire;

import com.sadwyn.iceandfire.fragments.CharactersFragment;
import com.sadwyn.iceandfire.fragments.SettingsFragment;
import com.sadwyn.iceandfire.models.CharacterModelImpl;
import com.sadwyn.iceandfire.modules.ApiModule;
import com.sadwyn.iceandfire.modules.CharactersPresenterModule;
import com.sadwyn.iceandfire.modules.SettingsPresenterModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApiModule.class, CharactersPresenterModule.class, SettingsPresenterModule.class})
public interface ComponentDagger {
    void inject(CharactersFragment charactersFragment);
    void inject(CharacterModelImpl model);
    void inject(SettingsFragment settingsFragment);
}
