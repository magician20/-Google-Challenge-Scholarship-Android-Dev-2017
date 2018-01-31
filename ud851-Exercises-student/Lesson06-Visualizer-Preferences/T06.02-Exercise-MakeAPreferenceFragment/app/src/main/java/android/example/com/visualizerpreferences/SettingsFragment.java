package android.example.com.visualizerpreferences;


import android.os.Bundle;
import android.support.v7.preference.PreferenceFragmentCompat;

/**
 * Created by magic on 11/15/2017.
 * SettingsFragment
 */

public class SettingsFragment extends PreferenceFragmentCompat {

    @Override
    public void onCreatePreferences(Bundle savedInstanceState, String rootKey) {
        //add the preference file to the Fragment (inflate xml)
           addPreferencesFromResource(R.xml.pref_visualizer);
    }
}
