package com.sadwyn.iceandfire.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import android.util.Log;

import com.sadwyn.iceandfire.App;
import com.sadwyn.iceandfire.Constants;
import com.sadwyn.iceandfire.presenters.SettingsFragmentPresenter;
import com.sadwyn.iceandfire.services.ExportDataService;
import com.sadwyn.iceandfire.activities.MainActivity;
import com.sadwyn.iceandfire.R;
import com.sadwyn.iceandfire.models.Character;
import com.sadwyn.iceandfire.models.CharacterModelImpl;
import com.sadwyn.iceandfire.utils.ChangeLanguageCallBack;
import com.sadwyn.iceandfire.utils.LocaleUtils;
import com.sadwyn.iceandfire.views.notifications.ExportDataNotification;

import org.parceler.Parcels;

import java.util.List;
import java.util.Locale;

import javax.inject.Inject;

import static com.sadwyn.iceandfire.Constants.DATA_SOURCE_PREF;
import static com.sadwyn.iceandfire.Constants.IS_PERMANENT_SAVE_CHECKED;
import static com.sadwyn.iceandfire.Constants.LANG_PREF;
import static com.sadwyn.iceandfire.Constants.REQUEST_FOR_WRITE_TO_CSV;


public class SettingsFragment extends PreferenceFragmentCompat implements ActivityCompat.OnRequestPermissionsResultCallback {

    private ChangeLanguageCallBack callBack;
    private SourceChangeCallBack sourceChangeCallBack;

    @Inject
    public SettingsFragmentPresenter presenter;

    public SettingsFragmentPresenter getPresenter() {
        return presenter;
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof MainActivity) {
            callBack = (MainActivity) context;
            sourceChangeCallBack = (MainActivity) context;
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        App.getComponentDagger().inject(this);
        addPreferencesFromResource(R.xml.preferences);
        setExportButtonPreference();
        setPermanentSaveCheckboxPreference();
    }

    public void setPermanentSaveCheckboxPreference() {
        Preference permanentSaveCheckbox = getPreferenceManager().findPreference(getString(R.string.permanentSaveCheckbox));
        permanentSaveCheckbox.setOnPreferenceChangeListener((preference, newValue) -> {
            if ((boolean) newValue)
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
                if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE)
                        == PackageManager.PERMISSION_GRANTED)
                    presenter.onExportButtonClick(getContext());
                else
                    ActivityCompat.requestPermissions(getActivity(),
                            new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, REQUEST_FOR_WRITE_TO_CSV);
                return false;
            }
        });
    }

    public void exportDataAfterRequest(){
        presenter.onExportButtonClick(getContext());
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

    }

    public void saveOneStringToPref(String key, String str) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, str);
        editor.apply();
    }

    public void saveOneBooleanToPref(String key, boolean bool) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putBoolean(key, bool);
        editor.apply();
    }

    public static SettingsFragment newInstance() {
        return new SettingsFragment();
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {
        if (preference.getKey().equals(getString(R.string.languages_list_key))) {
            ListPreference languagePreferences = (ListPreference) preference;
            if (languagePreferences.getValue() == null)
                languagePreferences.setValue(Locale.getDefault().getLanguage());

            languagePreferences.setOnPreferenceChangeListener((preference1, newValue) -> {
                if (!newValue.equals(languagePreferences.getValue())) {
                    String lang = (String) newValue;
                    Locale newLocale = new Locale(lang);
                    LocaleUtils.setLocale(getContext(), newLocale);
                    saveOneStringToPref(LANG_PREF, lang);
                    callBack.changeLanguage();
                }
                return true;
            });
        } else if (preference.getKey().equals(getString(R.string.data_sources_key))) {
            ListPreference dataSourcePreferences = (ListPreference) preference;
            dataSourcePreferences.setOnPreferenceChangeListener((preference12, newValue) -> {
                if (!newValue.equals(dataSourcePreferences.getValue())) {
                    String sourceValue = (String) newValue;
                    dataSourcePreferences.setValue((String) newValue);
                    saveOneStringToPref(DATA_SOURCE_PREF, sourceValue);
                    sourceChangeCallBack.onSourceChanged();
                }
                return false;
            });
        }
        return false;
    }

    @Override
    public void onPause() {
        super.onPause();
        presenter.onPause();
    }
}
