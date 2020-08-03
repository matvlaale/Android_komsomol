package ru.startandroid.android_komsomol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Switch;

import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    Spinner spinner;
    Switch themeSwitch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        findViews();
        fillSpinner();
    }

    private void findViews(){
        spinner = findViewById(R.id.spinLanguages);
        themeSwitch = findViewById(R.id.swtTheme);
    }

    private void fillSpinner(){
        spinner.setAdapter(
                ArrayAdapter.createFromResource(this, R.array.languages, R.layout.support_simple_spinner_dropdown_item));
    }
}