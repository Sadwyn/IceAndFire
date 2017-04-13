package com.sadwyn.iceandfire.modules;

import android.content.Context;

import com.sadwyn.iceandfire.CharactersView;
import com.sadwyn.iceandfire.presenters.CharactersFragmentPresenter;

import dagger.Module;
import dagger.Provides;


@Module
public class CharactersPresenterModule {

    private Context context;
    private CharactersView charactersView;

    public CharactersPresenterModule(Context context, CharactersView view) {
        this.context = context;
        this.charactersView = view;
    }

    @Provides
    CharactersFragmentPresenter providePresenter(){
        return new CharactersFragmentPresenter(context, charactersView);
    }
}
