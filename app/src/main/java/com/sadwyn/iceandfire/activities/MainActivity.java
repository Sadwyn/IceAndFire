package com.sadwyn.iceandfire.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.widget.Toast;

import com.mikepenz.iconics.typeface.FontAwesome;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.sadwyn.iceandfire.R;
import com.sadwyn.iceandfire.fragments.CharactersFragment;
import com.sadwyn.iceandfire.fragments.ContentFragmentCallback;
import com.sadwyn.iceandfire.fragments.DetailFragment;
import com.sadwyn.iceandfire.fragments.SettingsFragment;
import com.sadwyn.iceandfire.fragments.SourceChangeCallBack;
import com.sadwyn.iceandfire.models.Character;
import com.sadwyn.iceandfire.utils.ChangeLanguageCallBack;
import com.sadwyn.iceandfire.utils.LocaleUtils;
import com.sadwyn.iceandfire.views.widgets.CharacterWidget;

import org.parceler.Parcels;

import java.util.Locale;

import static com.sadwyn.iceandfire.Constants.CHARACTERS_FRAGMENT_TAG;
import static com.sadwyn.iceandfire.Constants.DETAIL_FRAGMENT_TAG;
import static com.sadwyn.iceandfire.Constants.LANG_PREF;
import static com.sadwyn.iceandfire.Constants.REQUEST_FOR_WRITE_TO_CSV;
import static com.sadwyn.iceandfire.Constants.SETTINGS_FRAGMENT_TAG;

public class MainActivity extends AppCompatActivity implements ContentFragmentCallback,
        ChangeLanguageCallBack,
        SourceChangeCallBack {

    Drawer.Result drawer;
    boolean doubleBackToExitPressedOnce = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        restoreSavedLocale();
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        drawer = initializeDrawer(toolbar);
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragContainer);

        if (fragment == null)
            replaceFragment(CharactersFragment.newInstance(), false, CHARACTERS_FRAGMENT_TAG);
        setSelectedItemInDrawer(fragment);
    }

    private void setSelectedItemInDrawer(Fragment fragment) {
        if(fragment == getSupportFragmentManager().findFragmentByTag(CHARACTERS_FRAGMENT_TAG))
            drawer.setSelection(0);
        else drawer.setSelection(1);
    }

    @Override
    public void onBackPressed() {
        if (drawer != null && drawer.isDrawerOpen()) {
            drawer.closeDrawer();
        } else if (getSupportFragmentManager().findFragmentById(R.id.fragContainer) ==
                getSupportFragmentManager().findFragmentByTag(DETAIL_FRAGMENT_TAG)) {
            super.onBackPressed();
        } else {
            if (doubleBackToExitPressedOnce) {
                Intent homeIntent = new Intent(Intent.ACTION_MAIN);
                homeIntent.addCategory(Intent.CATEGORY_HOME);
                homeIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(homeIntent);
            } else {
                this.doubleBackToExitPressedOnce = true;
                Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

                new Handler().postDelayed(() -> doubleBackToExitPressedOnce = false, 2000);
            }
        }
    }

    private Drawer.Result initializeDrawer(Toolbar toolbar) {
        return new Drawer()
                .withActivity(this)
                .withToolbar(toolbar)
                .withActionBarDrawerToggle(true)
                .withHeader(R.layout.drawer_header)
                .withDrawerWidthDp(300)
                .addDrawerItems(
                        new PrimaryDrawerItem().withName(R.string.drawer_item_characters).withIdentifier(1).withIcon(FontAwesome.Icon.faw_home),
                        new PrimaryDrawerItem().withName(R.string.drawer_item_settings).
                                withIcon(FontAwesome.Icon.faw_wrench).withIdentifier(2)
                )
                .withOnDrawerItemClickListener((parent, view, position, id, drawerItem) -> {
                    if (position == 1) {
                        replaceFragment(getSupportFragmentManager().findFragmentByTag(CHARACTERS_FRAGMENT_TAG), true, CHARACTERS_FRAGMENT_TAG);
                    } else if (position == 2) {
                        replaceFragment(SettingsFragment.newInstance(), true, SETTINGS_FRAGMENT_TAG);
                    }
                }).build();
    }

    @Override
    public void onItemClick(Character character) {
        DetailFragment detailFragment = DetailFragment.newInstance(character);
        detailFragment.show(getSupportFragmentManager(), DETAIL_FRAGMENT_TAG);
    }

    private void replaceFragment(Fragment fragment, boolean addToBackStack, String TAG) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.replace(R.id.fragContainer, fragment, TAG);
        if (addToBackStack) transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        outState.putParcelable("DRAWER_RESULT", Parcels.wrap(drawer));
    }

    @Override
    public void changeLanguage() {
        Intent i = getBaseContext().getPackageManager()
                .getLaunchIntentForPackage(getBaseContext().getPackageName());
        i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(i);

    }

    public void restoreSavedLocale() {
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        String lang = sharedPreferences.getString(LANG_PREF, Locale.getDefault().getLanguage());
        Locale newLocale = new Locale(lang);
        LocaleUtils.setLocale(getApplicationContext(), newLocale);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        CharacterWidget.updateWidget(getApplicationContext());
    }

    @Override
    public void onSourceChanged() {
        FragmentManager manager = getSupportFragmentManager();
        CharactersFragment charactersFragment = (CharactersFragment) manager.findFragmentByTag(CHARACTERS_FRAGMENT_TAG);
        charactersFragment.getPresenter().initializeData();
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode) {
            case REQUEST_FOR_WRITE_TO_CSV: {
                Intent intent = ((SettingsFragment) getSupportFragmentManager().findFragmentByTag(SETTINGS_FRAGMENT_TAG)).getIntent();
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    startService(intent);
                } else Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
