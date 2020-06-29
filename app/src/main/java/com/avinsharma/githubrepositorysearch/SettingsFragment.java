package com.avinsharma.githubrepositorysearch;

import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.preference.ListPreference;
import androidx.preference.Preference;
import androidx.preference.PreferenceFragmentCompat;
import androidx.preference.PreferenceScreen;

public class SettingsFragment extends PreferenceFragmentCompat implements SharedPreferences.OnSharedPreferenceChangeListener {
    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        addPreferencesFromResource(R.xml.settings_pref);
        updateListPreferenceSummary();
    }

    private void updateListPreferenceSummary() {
        PreferenceScreen preferenceScreen = getPreferenceScreen();
        SharedPreferences preferences = preferenceScreen.getSharedPreferences();
        int count = preferenceScreen.getPreferenceCount();

        for (int i = 0; i < count; i++){
            Preference preference = preferenceScreen.getPreference(i);
            if (preference instanceof ListPreference) {
                String activeValue = preferences.getString(preference.getKey(), null);
                ListPreference listPreference = (ListPreference) preference;
                int index = listPreference.findIndexOfValue(activeValue);
                if (index >= 0){
                    listPreference.setSummary(activeValue);
                }
            }
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String key) {
        updateListPreferenceSummary();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }
}
