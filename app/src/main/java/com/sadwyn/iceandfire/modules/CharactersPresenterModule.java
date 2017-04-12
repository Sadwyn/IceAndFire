package com.sadwyn.iceandfire.modules;

import android.content.Context;

import com.sadwyn.iceandfire.CharacterView;
import com.sadwyn.iceandfire.presenters.CharactersFragmentPresenter;

import dagger.Module;
import dagger.Provides;


@Module
public class CharactersPresenterModule {

    private Context context;
    private CharacterView characterView;

    public CharactersPresenterModule(Context context, CharacterView view) {
        this.context = context;
        this.characterView = view;
    }

    @Provides
    CharactersFragmentPresenter providePresenter(){
        return new CharactersFragmentPresenter(context, characterView);
    }
}
