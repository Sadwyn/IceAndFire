package com.sadwyn.iceandfire.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.preference.ListPreference;
import android.support.v7.preference.Preference;
import android.support.v7.preference.PreferenceFragmentCompat;
import android.support.v7.preference.PreferenceManager;
import com.sadwyn.iceandfire.MainActivity;
import com.sadwyn.iceandfire.R;
import com.sadwyn.iceandfire.utils.ChangeLanguageCallBack;
import com.sadwyn.iceandfire.utils.LocaleUtils;
import java.util.Locale;
import static com.sadwyn.iceandfire.Constants.DATA_SOURCE_PREF;
import static com.sadwyn.iceandfire.Constants.LANG_PREF;


public class SettingsFragment extends PreferenceFragmentCompat{

    ChangeLanguageCallBack callBack;
    SourceChangeCallBack sourceChangeCallBack;

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
    }

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {

    }

    public void saveOneStringToPref(String key, String lang)
    {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getContext());
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, lang);
        editor.commit();
    }

    public static SettingsFragment newInstance(){
        return new SettingsFragment();
    }

    @Override
    public boolean onPreferenceTreeClick(Preference preference) {

        if(preference.getKey().equals(getString(R.string.languages_list_key))) {
            ListPreference languagePreferences = (ListPreference) preference;
            if(languagePreferences.getValue()==null)
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

   /* @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
        locale = new Locale(lang);
        Locale.setDefault(locale);
        Configuration config = new Configuration();
        config.locale = locale;
        getActivity().getBaseContext().getResources().updateConfiguration(config, null);
    }*/
}
