package com.sadwyn.iceandfire.fragments;

import android.Manifest;
import android.app.Activity;
import android.app.Notification;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Parcel;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.preference.CheckBoxPreference;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import com.sadwyn.iceandfire.Constants;
import com.sadwyn.iceandfire.services.ExportDataService;
import com.sadwyn.iceandfire.activities.MainActivity;
import com.sadwyn.iceandfire.R;
import com.sadwyn.iceandfire.models.Character;
import com.sadwyn.iceandfire.models.CharacterModelImpl;
import com.sadwyn.iceandfire.services.ExportDataToService;
import com.sadwyn.iceandfire.utils.ChangeLanguageCallBack;
import com.sadwyn.iceandfire.utils.LocaleUtils;
import com.sadwyn.iceandfire.views.notifications.ExportDataNotification;

import org.parceler.Parcels;

import java.util.List;
import java.util.Locale;

import static com.sadwyn.iceandfire.Constants.DATA_SOURCE_PREF;
import static com.sadwyn.iceandfire.Constants.IS_PERMANENT_SAVE_CHECKED;
import static com.sadwyn.iceandfire.Constants.LANG_PREF;
import static com.sadwyn.iceandfire.Constants.REQUEST_FOR_WRITE_TO_CSV;


public class SettingsFragment extends PreferenceFragmentCompat implements ActivityCompat.OnRequestPermissionsResultCallback{

    ChangeLanguageCallBack callBack;
    SourceChangeCallBack sourceChangeCallBack;

    public Intent getIntent() {
        return intent;
    }

    private Intent intent;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if(context instanceof MainActivity){
            callBack = (MainActivity)context;
            sourceChangeCallBack = (MainActivity)context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.preferences);
        setExportButtonPreference();
        setPermanentSaveCheckboxPreference();
    }

    public void setPermanentSaveCheckboxPreference() {
        Preference permanentSaveCheckbox = getPreferenceManager().findPreference(getString(R.string.permanentSaveCheckbox));
        permanentSaveCheckbox.setOnPreferenceChangeListener((preference, newValue) -> {
            if((boolean)newValue)
                saveOneBooleanToPref(IS_PERMANENT_SAVE_CHECKED, true);
            else saveOneBooleanToPref(IS_PERMANENT_SAVE_CHECKED, false);
            return true;
        });
    }

    public void setExportButtonPreference() {
        Preference exportButton = getPreferenceManager().findPreference(getString(R.string.exportButtonKey));
        exportButton.setOnPreferenceClickListener(new Preference.OnPreferenceClickListener() {
            @Override
            public boolean onPreferenceClick(Preference preference13) {
                Context context = getContext();
                Activity activity = getActivity();
                intent = new Intent(context, ExportDataService.class);
                Thread exportThread = new Thread(new ExportDataToService(context, activity, intent));
                exportThread.start();
                ExportDataNotification notification = new ExportDataNotification(context);
                notification.showNotification();
                return false;
            }
        });
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

    }

    public void saveOneStringToPref(String key, String str)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, str);
        editor.apply();
    }

    public void saveOneBooleanToPref(String key, boolean bool)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, bool);
        editor.apply();
        Log.i("TAG", String.valueOf( bool));
    }

    public static SettingsFragment newInstance(){
        return new SettingsFragment();
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        if(preference.getKey().equals(getString(R.string.languages_list_key))) {
            ListPreference languagePreferences = (ListPreference) preference;
            if(languagePreferences.getValue() == null)
            languagePreferences.setValue(Locale.getDefault().getLanguage());

            languagePreferences.setOnPreferenceChangeListener((preference1, newValue) -> {
                if(!newValue.equals(languagePreferences.getValue())) {
                    String lang = (String)newValue;
                    Locale newLocale = new Locale(lang);
                    LocaleUtils.setLocale(getContext(),newLocale);
                    saveOneStringToPref(LANG_PREF ,lang);
                    callBack.changeLanguage();
                }
                return true;
            });
        }
        else if(preference.getKey().equals(getString(R.string.data_sources_key))){
            ListPreference dataSourcePreferences = (ListPreference) preference;
            dataSourcePreferences.setOnPreferenceChangeListener((preference12, newValue) -> {
                if(!newValue.equals(dataSourcePreferences.getValue())) {
                    String sourceValue = (String)newValue;
                    dataSourcePreferences.setValue((String) newValue);
                    saveOneStringToPref(DATA_SOURCE_PREF, sourceValue);
                    sourceChangeCallBack.onSourceChanged();
                }
                return false;
            });
        }
        return false;
    }

}
