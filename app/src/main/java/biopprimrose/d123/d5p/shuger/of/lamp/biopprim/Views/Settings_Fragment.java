package biopprimrose.d123.d5p.shuger.of.lamp.biopprim.Views;

import android.os.Bundle;
import android.preference.PreferenceFragment;

import biopprimrose.d123.d5p.shuger.of.lamp.biopprim.R;

public class Settings_Fragment extends PreferenceFragment {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.activity_settings);
    }
}
