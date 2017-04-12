package com.sadwyn.iceandfire.components;


import com.sadwyn.iceandfire.fragments.CharactersFragment;
import com.sadwyn.iceandfire.modules.CharactersPresenterModule;

import dagger.Component;

@Component(modules = {CharactersPresenterModule.class})
public interface CharactersPresenterComponent {
    void inject(CharactersFragment fragment);
}
