package com.sadwyn.iceandfire.modules;

import android.content.Context;

import com.sadwyn.iceandfire.CharacterView;
import com.sadwyn.iceandfire.presenters.CharactersListPresenter;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;


@Module
public class PresenterModule {
    private Context context;
    private CharacterView characterView;

    public PresenterModule(Context context, CharacterView view) {
        this.context = context;
        this.characterView = view;
    }

    @Provides
    CharactersListPresenter providePresenter(){
        return new CharactersListPresenter(context, characterView);
    }
}
