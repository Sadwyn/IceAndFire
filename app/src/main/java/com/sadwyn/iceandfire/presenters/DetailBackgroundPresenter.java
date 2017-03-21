package com.sadwyn.iceandfire.presenters;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.View;

import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.animation.GlideAnimation;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.target.Target;
import com.sadwyn.iceandfire.DetailBackgroundView;
import com.sadwyn.iceandfire.R;
import com.sadwyn.iceandfire.models.DetailBackgroundModelImpl;

public class DetailBackgroundPresenter extends BasePresenter {
    private DetailBackgroundView detailBackgroundView;
    private Context detailFragmentContext;
    private DetailBackgroundModelImpl backgroundModel;

    public DetailBackgroundPresenter(Context context, DetailBackgroundView view) {
        this.detailBackgroundView = view;
        this.detailFragmentContext = context;
        this.backgroundModel = new DetailBackgroundModelImpl(context);
    }

    @Override
    public void onViewCreated(View view, Bundle bundle) {
        loadBackgroundImage(detailFragmentContext);
    }

    @Override
    public void onDestroyView() {

    }

    @Override
    public void onSaveInstanceState(Bundle bundle) {

    }

    private void loadBackgroundImage(Context context) {
        backgroundModel.getBackgroundImageRequest().listener(new RequestListener<String, GlideDrawable>() {
            @Override
            public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
               Drawable drawable = (context.getResources().getDrawable(R.drawable.background));
                detailBackgroundView.onSetBackground(drawable);
                return false;
            }

            @Override
            public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                return false;
            }
        }).into(new SimpleTarget<GlideDrawable>() {
            @Override
            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
                Drawable drawable = resource.getCurrent();
                detailBackgroundView.onSetBackground(drawable);
            }
        });
    }
}
