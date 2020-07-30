package ru.startandroid.android_komsomol;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.KeyListener;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;

public class MainActivity extends AppCompatActivity {

    private Button mainButton;
    private ImageView settingsButton;
    private Spinner spinner;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findViews();
        setListeners();

        fillSpinner();
    }

    private void findViews() {
        mainButton = findViewById(R.id.mainButton);
        settingsButton = findViewById(R.id.btnSettings);
        spinner = findViewById(R.id.spinCities);
    }

    private void setListeners() {
        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, WeatherCardActivity.class));
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, SettingsActivity.class));
            }
        });
    }

    private void fillSpinner() {
        spinner.setAdapter(
                ArrayAdapter.createFromResource(this, R.array.popularCities, R.layout.support_simple_spinner_dropdown_item));
    }
}