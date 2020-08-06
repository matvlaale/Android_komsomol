package ru.startandroid.android_komsomol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.method.KeyListener;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    private Button mainButton;
    private ImageView settingsButton;
    private Spinner spinner;
    private CheckBox pressureCB;
    private CheckBox windCB;
    private EditText cityET;

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
        windCB = findViewById(R.id.cbWindSpeed);
        pressureCB = findViewById(R.id.cbPressure);
        cityET = findViewById(R.id.etCity);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        boolean[] temp = new boolean[]{windCB.isChecked(), pressureCB.isChecked()};
        outState.putBooleanArray("CheckBoxes", temp);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        boolean[] temp = savedInstanceState.getBooleanArray("CheckBoxes");
        if (temp != null) {
            windCB.setChecked(temp[0]);
            pressureCB.setChecked(temp[1]);
        }
    }

    private void setListeners() {
        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent weatherCardIntent = new Intent(MainActivity.this, WeatherCardActivity.class);
                String cityName = cityET.getText().toString();
                weatherCardIntent.putExtra("wind", windCB.isChecked());
                weatherCardIntent.putExtra("pressure", pressureCB.isChecked());
                weatherCardIntent.putExtra("CityName", cityName);
                startActivity(weatherCardIntent);
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