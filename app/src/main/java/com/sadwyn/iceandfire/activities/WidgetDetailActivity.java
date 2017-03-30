package com.sadwyn.iceandfire.activities;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sadwyn.iceandfire.Constants;
import com.sadwyn.iceandfire.R;
import com.sadwyn.iceandfire.content_providers.DataProviderImpl;
import com.sadwyn.iceandfire.fragments.DetailFragment;
import com.sadwyn.iceandfire.models.Character;

import org.parceler.Parcels;

import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TASK;
import static android.content.Intent.FLAG_ACTIVITY_CLEAR_TOP;
import static android.content.Intent.FLAG_ACTIVITY_SINGLE_TOP;
import static com.sadwyn.iceandfire.Constants.CURRENT_HERO_ID;
import static com.sadwyn.iceandfire.Constants.HERO_DETAIL_REQUESTED;
import static com.sadwyn.iceandfire.Constants.INSTANT_ID;
import static com.sadwyn.iceandfire.Constants.NEXT_HERO_ID;
import static com.sadwyn.iceandfire.Constants.NEXT_HERO_NAME;
import static com.sadwyn.iceandfire.Constants.NEXT_HERO_SWITCH;
import static com.sadwyn.iceandfire.Constants.PREV_HERO_ID;
import static com.sadwyn.iceandfire.Constants.PREV_HERO_NAME;
import static com.sadwyn.iceandfire.Constants.PREV_HERO_SWITCH;

public class WidgetDetailActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_widget_detail);
        Character character = Parcels.unwrap(getIntent().getParcelableExtra(Constants.WRAPPED_CHARACTER_FROM_RECEIVER));
        replaceFragment(DetailFragment.newInstance(character),false,Constants.DETAIL_FRAGMENT_TAG);
    }

    private void replaceFragment(Fragment fragment, boolean addToBackStack, String TAG) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.replace(R.id.widgetContainer, fragment, TAG);
        if (addToBackStack) transaction.addToBackStack(null);
        transaction.commit();
    }


}
