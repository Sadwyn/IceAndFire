package com.sadwyn.iceandfire.presenters;

import android.os.Bundle;
import android.view.View;

import com.sadwyn.iceandfire.fragments.ContentFragmentCallback;


public abstract class BasePresenter {
    abstract public void onViewCreated(View view, Bundle bundle);
    abstract public void onDestroyView();
    abstract public void onSaveInstanceState(Bundle bundle);
}
