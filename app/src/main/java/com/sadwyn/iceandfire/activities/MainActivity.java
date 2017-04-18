package com.sadwyn.iceandfire.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Toast;

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

import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.sadwyn.iceandfire.Constants.CHARACTERS_FRAGMENT_TAG;
import static com.sadwyn.iceandfire.Constants.DETAIL_FRAGMENT_TAG;
import static com.sadwyn.iceandfire.Constants.LANG_PREF;
import static com.sadwyn.iceandfire.Constants.REQUEST_FOR_WRITE_TO_CSV;
import static com.sadwyn.iceandfire.Constants.SETTINGS_FRAGMENT_TAG;

public class MainActivity extends AppCompatActivity implements ContentFragmentCallback,
        ChangeLanguageCallBack,
        SourceChangeCallBack {


    boolean doubleBackToExitPressedOnce = false;
    @BindView(R.id.drawer_layout)
    DrawerLayout drawerLayout;
    @BindView(R.id.left_drawer)
    NavigationView navigationView;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        restoreSavedLocale();
        initializeDrawer();
        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragContainer);
        if (fragment == null)
            replaceFragment(CharactersFragment.newInstance(), false, CHARACTERS_FRAGMENT_TAG);
    }

    private void initializeDrawer() {
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setHomeButtonEnabled(true);
        toggle = new ActionBarDrawerToggle(this, drawerLayout, R.string.drawer_open, R.string.drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!(getSupportFragmentManager().findFragmentByTag(CHARACTERS_FRAGMENT_TAG) ==
                        getSupportFragmentManager().findFragmentById(R.id.fragContainer))) {
                    getSupportFragmentManager().popBackStack();
                    toggle.setDrawerIndicatorEnabled(true);
                }
                else {
                   drawerLayout.openDrawer(3);
                }
            }
        });
        View header = View.inflate(getApplicationContext(), R.layout.drawer_header, null);
        navigationView.addHeaderView(header);
        navigationView.inflateMenu(R.menu.drawer_menu);

        navigationView.setNavigationItemSelectedListener(item -> {
            selectItem(item.getItemId());
            drawerLayout.closeDrawer(3);
            return true;
        });
    }


    @Override
    public void onBackPressed() {
        if (drawerLayout != null && drawerLayout.isDrawerOpen(3)) {
            drawerLayout.closeDrawer(3);
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


    private void selectItem(int id) {
        if (id == R.id.character_item) {
            replaceFragment(getSupportFragmentManager().findFragmentByTag(CHARACTERS_FRAGMENT_TAG), true, CHARACTERS_FRAGMENT_TAG);
            toggle.setDrawerIndicatorEnabled(true);
        } else if (id == R.id.settings_item) {
            toggle.setDrawerIndicatorEnabled(false);
            replaceFragment(SettingsFragment.newInstance(), true, SETTINGS_FRAGMENT_TAG);
        }
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
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
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
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    ((SettingsFragment) getSupportFragmentManager()
                            .findFragmentByTag(SETTINGS_FRAGMENT_TAG)).exportDataAfterRequest();
                } else Toast.makeText(this, "Failed!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
