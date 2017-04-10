package com.sadwyn.iceandfire;

import com.sadwyn.iceandfire.activities.MainActivity;
import com.sadwyn.iceandfire.fragments.CharactersFragment;
import com.sadwyn.iceandfire.models.CharacterModelImpl;
import com.sadwyn.iceandfire.modules.ApiModule;
import com.sadwyn.iceandfire.modules.PresenterModule;

import javax.inject.Singleton;

import dagger.Component;

@Singleton
@Component(modules = {ApiModule.class, PresenterModule.class})
public interface ComponentDagger {
    void inject(CharactersFragment fragment);
    void inject(CharacterModelImpl model);
}
