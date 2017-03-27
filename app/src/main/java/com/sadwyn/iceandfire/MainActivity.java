package com.sadwyn.iceandfire;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.sadwyn.iceandfire.data.HeroesDataContract;
import com.sadwyn.iceandfire.fragments.CharactersFragment;
import com.sadwyn.iceandfire.fragments.ContentFragmentCallback;
import com.sadwyn.iceandfire.fragments.DetailFragment;
import com.sadwyn.iceandfire.fragments.SettingsFragment;
import com.sadwyn.iceandfire.fragments.SourceChangeCallBack;
import com.sadwyn.iceandfire.models.Character;
import com.sadwyn.iceandfire.utils.ChangeLanguageCallBack;
import com.sadwyn.iceandfire.utils.LocaleUtils;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.Locale;

import static com.sadwyn.iceandfire.Constants.CHARACTERS_FRAGMENT_TAG;
import static com.sadwyn.iceandfire.Constants.DETAIL_FRAGMENT_TAG;
import static com.sadwyn.iceandfire.Constants.HERO_DETAIL_REQUESTED;
import static com.sadwyn.iceandfire.Constants.LANG_PREF;
import static com.sadwyn.iceandfire.Constants.NEXT_HERO_SWITCH;
import static com.sadwyn.iceandfire.Constants.PREV_HERO_SWITCH;
import static com.sadwyn.iceandfire.Constants.REQUEST_FOR_WRITE_TO_CSV;
import static com.sadwyn.iceandfire.Constants.SETTINGS_FRAGMENT_TAG;

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



    }

    @Override
    protected void onResume() {
        super.onResume();
        if(getIntent().getBooleanExtra("START_DETAIL_FROM_WIDGET", false))
        {
            Character character = Parcels.unwrap(getIntent().getParcelableExtra(Constants.WRAPPED_CHARACTER_FROM_RECEIVER));
            replaceFragment(DetailFragment.newInstance(character), true, DETAIL_FRAGMENT_TAG);
        }
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

        public WidgetIntentReceiver() {
        }

        @Override
        public void onReceive(Context context, Intent intent) {
            String s = intent.getStringExtra(Constants.INCOMING_INTENT);
            switch (s) {
                case HERO_DETAIL_REQUESTED:
                    Character character = getCharacterById(context, 1);
                    Intent startDetail = new Intent(context.getApplicationContext(), MainActivity.class);

                    startDetail.putExtra("START_DETAIL_FROM_WIDGET", true);
                    startDetail.putExtra(Constants.WRAPPED_CHARACTER_FROM_RECEIVER, Parcels.wrap(character));
                    startDetail.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    context.startActivity(startDetail);

                    break;
                case PREV_HERO_SWITCH:
                    Log.i("TAG", "LEFT_HERO");
                    break;
                case NEXT_HERO_SWITCH:
                    Log.i("TAG", "RIGHT_HERO");
                    break;
            }
        }

        private Character getCharacterById(Context context, int targetId) {
            Character character = new Character();

            ContentResolver resolver = context.getContentResolver();
            Cursor mainCursor = resolver.query(Uri.parse("content://com.sadwyn.iceandfire.provider.contract/characters/"+targetId),
                    null, null, null, null, null);
            if (mainCursor != null) {
                try {
                    if (mainCursor.moveToFirst()) {
                        int id = mainCursor.getInt(mainCursor.getColumnIndex(HeroesDataContract.MainDataStructure._ID));
                        character.setName(mainCursor.getString(mainCursor.getColumnIndex(HeroesDataContract.MainDataStructure.COLUMN_NAME)));
                        character.setBorn(mainCursor.getString(mainCursor.getColumnIndex(HeroesDataContract.MainDataStructure.COLUMN_BORN)));
                        character.setCulture(mainCursor.getString(mainCursor.getColumnIndex(HeroesDataContract.MainDataStructure.COLUMN_KINGDOM)));
                        character.setGender(mainCursor.getString(mainCursor.getColumnIndex(HeroesDataContract.MainDataStructure.COLUMN_GENDER)));
                        character.setFather(mainCursor.getString(mainCursor.getColumnIndex(HeroesDataContract.MainDataStructure.COLUMN_FATHER)));
                        character.setMother(mainCursor.getString(mainCursor.getColumnIndex(HeroesDataContract.MainDataStructure.COLUMN_MOTHER)));
                        character.setDied(mainCursor.getString(mainCursor.getColumnIndex(HeroesDataContract.MainDataStructure.COLUMN_DEAD)));
                    }
                } finally {
                    mainCursor.close();
                }
            }

            Cursor aliasesCursor = resolver.query(Uri.parse("content://com.sadwyn.iceandfire.provider.contract/aliases/"+targetId),
                    null, null, null, null, null);

            ArrayList<String> aliases = new ArrayList<>();

            if (aliasesCursor != null)
                try {
                    while (aliasesCursor.moveToNext()) {
                        String alias = aliasesCursor.getString(aliasesCursor.getColumnIndex(HeroesDataContract.AliasesStructure.COLUMN_NICKNAME));
                        aliases.add(alias);
                    }
                }
                finally {
                    aliasesCursor.close();
                }
                character.setAliases(aliases);

            return character;
        }
    }
}
