package com.sadwyn.iceandfire.activities;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.sadwyn.iceandfire.Constants;
import com.sadwyn.iceandfire.R;
import com.sadwyn.iceandfire.fragments.DetailFragment;
import com.sadwyn.iceandfire.models.Character;

import org.parceler.Parcels;

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
