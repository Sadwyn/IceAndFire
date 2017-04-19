package com.sadwyn.iceandfire.components;

import com.sadwyn.iceandfire.fragments.DetailFragment;
import com.sadwyn.iceandfire.modules.DetailsPresenterModule;

import dagger.Component;

/**
 * Created by Sadwyn on 19.04.2017.
 */

@Component(modules = {DetailsPresenterModule.class})
public interface DetailsPresenterComponent {
    void inject(DetailFragment detailFragment);
}
