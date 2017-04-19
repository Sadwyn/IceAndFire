package com.sadwyn.iceandfire.modules;

import android.content.Context;

import com.sadwyn.iceandfire.DetailBackgroundView;
import com.sadwyn.iceandfire.fragments.SwipeCharacterCallback;
import com.sadwyn.iceandfire.presenters.DetailFragmentPresenter;

import dagger.Module;
import dagger.Provides;


@Module
public class DetailsPresenterModule {
    private Context context;
    private DetailBackgroundView view;
    private SwipeCharacterCallback swipeCallback;

    public DetailsPresenterModule(Context context, DetailBackgroundView view, SwipeCharacterCallback swipeCallback) {
        this.context = context;
        this.view = view;
        this.swipeCallback = swipeCallback;
    }

    @Provides
    DetailFragmentPresenter providePresenter(){
        return new DetailFragmentPresenter(context, view, swipeCallback);
    }
}
