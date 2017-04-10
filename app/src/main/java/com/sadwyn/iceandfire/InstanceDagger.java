package com.sadwyn.iceandfire;

import com.sadwyn.iceandfire.fragments.CharactersFragment;
import com.sadwyn.iceandfire.fragments.DetailFragment;
import com.sadwyn.iceandfire.presenters.CharactersListPresenter;
import com.sadwyn.iceandfire.presenters.DetailBackgroundPresenter;

import dagger.Component;

@Component(
        modules = {
                CharactersListPresenter.class,
                DetailBackgroundPresenter.class
        }
)
public interface InstanceDagger {
    void inject(DetailFragment detailFragment);
    void inject(CharactersFragment charactersFragment);

}
