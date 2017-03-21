package com.sadwyn.iceandfire.models;

import android.content.Context;

import com.bumptech.glide.DrawableTypeRequest;
import com.bumptech.glide.Glide;
import com.sadwyn.iceandfire.Constants;

/**
 * Created by Kipris on 16.03.2017.
 */

public class DetailBackgroundModelImpl implements DetailBackgroundModel {
    private Context context;

    public DetailBackgroundModelImpl(Context context) {
        this.context = context;
    }

    @Override
    public DrawableTypeRequest<String> getBackgroundImageRequest() {
       return Glide.with(context).load(Constants.BACKGROUND_IMAGE_URL);
    }
}
