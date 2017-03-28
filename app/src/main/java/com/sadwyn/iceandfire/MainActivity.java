package com.sadwyn.iceandfire;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.sadwyn.iceandfire.content_providers.DataProviderImpl;
import com.sadwyn.iceandfire.fragments.CharactersFragment;
import com.sadwyn.iceandfire.fragments.ContentFragmentCallback;
import com.sadwyn.iceandfire.fragments.DetailFragment;
import com.sadwyn.iceandfire.fragments.SettingsFragment;
import com.sadwyn.iceandfire.fragments.SourceChangeCallBack;
import com.sadwyn.iceandfire.models.Character;
import com.sadwyn.iceandfire.utils.ChangeLanguageCallBack;
import com.sadwyn.iceandfire.utils.LocaleUtils;

import org.parceler.Parcels;

import java.util.Locale;

import static com.sadwyn.iceandfire.Constants.CHARACTERS_FRAGMENT_TAG;
import static com.sadwyn.iceandfire.Constants.DETAIL_FRAGMENT_TAG;
import static com.sadwyn.iceandfire.Constants.HERO_DETAIL_REQUESTED;
import static com.sadwyn.iceandfire.Constants.CURRENT_HERO_ID;
import static com.sadwyn.iceandfire.Constants.INSTANT_ID;
import static com.sadwyn.iceandfire.Constants.LANG_PREF;
import static com.sadwyn.iceandfire.Constants.NEXT_HERO_ID;
import static com.sadwyn.iceandfire.Constants.NEXT_HERO_NAME;
import static com.sadwyn.iceandfire.Constants.NEXT_HERO_SWITCH;
import static com.sadwyn.iceandfire.Constants.PREV_HERO_ID;
import static com.sadwyn.iceandfire.Constants.PREV_HERO_NAME;
import static com.sadwyn.iceandfire.Constants.PREV_HERO_SWITCH;
import static com.sadwyn.iceandfire.Constants.REQUEST_FOR_WRITE_TO_CSV;
import static com.sadwyn.iceandfire.Constants.SETTINGS_FRAGMENT_TAG;
import static com.sadwyn.iceandfire.Constants.START_DETAIL_FROM_WIDGET;

public class MainActivity extends AppCompatActivity implements ContentFragmentCallback,
        ChangeLanguageCallBack,
        SourceChangeCallBack {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        restoreSavedLocale();

        Fragment fragment = getSupportFragmentManager().findFragmentById(R.id.fragContainer);

        if (fragment == null)
            replaceFragment(CharactersFragment.newInstance(), false, CHARACTERS_FRAGMENT_TAG);
        if(getIntent().getBooleanExtra(START_DETAIL_FROM_WIDGET, false))
        {
            Character character = Parcels.unwrap(getIntent().getParcelableExtra(Constants.WRAPPED_CHARACTER_FROM_RECEIVER));
            replaceFragment(DetailFragment.newInstance(character), false, DETAIL_FRAGMENT_TAG);
        }
    }

    @Override
    protected void onPause() {
        if(getIntent().getBooleanExtra(START_DETAIL_FROM_WIDGET, false))
        {
            finish();
        }
        super.onPause();
    }

    @Override
    public void onItemClick(Character character) {
        replaceFragment(DetailFragment.newInstance(character), true, DETAIL_FRAGMENT_TAG);
    }

    private void replaceFragment(Fragment fragment, boolean addToBackStack, String TAG) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
        transaction.replace(R.id.fragContainer, fragment, TAG);
        if (addToBackStack) transaction.addToBackStack(null);
        transaction.commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_settings: {
                replaceFragment(SettingsFragment.newInstance(), true, SETTINGS_FRAGMENT_TAG);
            }
        }
        return true;
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

    public static class WidgetIntentReceiver extends BroadcastReceiver {
        DataProviderImpl provider = new DataProviderImpl();
        public WidgetIntentReceiver() {}

        @Override
        public void onReceive(Context context, Intent intent) {
            String s = intent.getStringExtra(Constants.INCOMING_INTENT);
            switch (s) {
                case HERO_DETAIL_REQUESTED:
                    int currentId = intent.getIntExtra(CURRENT_HERO_ID, 1);
                    Character character = provider.getCharacterById(context, currentId);
                    provider.showDetailsOfChosenHero(context, character);

                    break;
                case PREV_HERO_SWITCH:
                    int prevId = (int) (Math.random() * provider.getCharactersCount(context) + 1);
                    character =  provider.getCharacterById(context,prevId);
                    Intent prevHeroIntent = new Intent("com.sadwyn.update.widget");
                    prevHeroIntent.putExtra(PREV_HERO_ID, prevId);
                    prevHeroIntent.putExtra(PREV_HERO_NAME, character.getName());
                    context.sendBroadcast(prevHeroIntent);

                    break;
                case NEXT_HERO_SWITCH:
                    int nextId = (int) (Math.random() * provider.getCharactersCount(context) + 1);
                    character =  provider.getCharacterById(context,nextId);
                    Intent nextHeroIntent = new Intent("com.sadwyn.update.widget");
                    nextHeroIntent.putExtra(NEXT_HERO_ID, nextId);
                    nextHeroIntent.putExtra(NEXT_HERO_NAME, character.getName());
                    context.sendBroadcast(nextHeroIntent);
                    break;
                case INSTANT_ID :
                    int startID = intent.getIntExtra("HERO_START_ID",0);
                    character =  provider.getCharacterById(context,startID);
                    Intent instantHeroIntent = new Intent("com.sadwyn.update.widget");
                    instantHeroIntent.putExtra("INSTANT_HERO_ID", startID);
                    instantHeroIntent.putExtra("INSTANT_HERO_NAME", character.getName());
                    context.sendBroadcast(instantHeroIntent);
                    break;

            }
        }

    }
}
