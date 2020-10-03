package ru.startandroid.android_komsomol;

import androidx.appcompat.app.AppCompatActivity;
import androidx.preference.PreferenceManager;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.Spinner;
import android.widget.Switch;

import com.google.android.material.snackbar.Snackbar;

import ru.startandroid.android_komsomol.addMaterials.Singleton;

public class SettingsActivity extends AppCompatActivity {

    private Spinner spinner;
    private Switch themeSwitch;
    private boolean notFirst;
    private final String booleanKey = "isDark";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Singleton.getInstance().getDark())
            setTheme(R.style.DarkTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        findViews();
        otherOptions();
    }

    private void findViews() {
        spinner = findViewById(R.id.spinLanguages);
        themeSwitch = findViewById(R.id.swtTheme);
    }

    private void otherOptions() {
        if (Singleton.getInstance().getDark()) themeSwitch.setChecked(true);
        themeSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                Singleton.getInstance().setDark(themeSwitch.isChecked());
                Singleton.getInstance().setAlrChanged(false);
                SharedPreferences.Editor editor = PreferenceManager.getDefaultSharedPreferences(getApplicationContext()).edit();
                editor.putBoolean(booleanKey, Singleton.getInstance().getDark());
                editor.apply();
                recreate();
            }
        });
        notFirst = false;
        spinner.setAdapter(
                ArrayAdapter.createFromResource(this, R.array.languages, R.layout.support_simple_spinner_dropdown_item));
        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (notFirst) Snackbar.make(parent, "Your language is " + spinner.getSelectedItem() + "!", Snackbar.LENGTH_LONG).show();
                else notFirst = true;
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }
}