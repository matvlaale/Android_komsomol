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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toast.makeText(this, "Create", Toast.LENGTH_SHORT).show();
        Log.d("Main", "Create");
        findViews();
        setListeners();
        fillSpinner();
    }

    @Override
    protected void onStart() {
        super.onStart();
        Toast.makeText(this, "Start", Toast.LENGTH_SHORT).show();
        Log.d("Main", "Start");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Toast.makeText(this, "Resume", Toast.LENGTH_SHORT).show();
        Log.d("Main", "Resume");
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Toast.makeText(this, "Restart", Toast.LENGTH_SHORT).show();
        Log.d("Main", "Restart");
    }

    @Override
    protected void onPause() {
        super.onPause();
        Toast.makeText(this, "Pause", Toast.LENGTH_SHORT).show();
        Log.d("Main", "Pause");
    }

    @Override
    protected void onStop() {
        super.onStop();
        Toast.makeText(this, "Stop", Toast.LENGTH_SHORT).show();
        Log.d("Main", "Stop");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Toast.makeText(this, "Destroy", Toast.LENGTH_SHORT).show();
        Log.d("Main", "Destroy");
    }

    private void findViews() {
        mainButton = findViewById(R.id.mainButton);
        settingsButton = findViewById(R.id.btnSettings);
        spinner = findViewById(R.id.spinCities);
        windCB = findViewById(R.id.cbWindSpeed);
        pressureCB = findViewById(R.id.cbPressure);
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        boolean[] temp = new boolean[]{windCB.isSelected(), pressureCB.isSelected()};
        outState.putBooleanArray("CheckBoxes", temp);
        super.onSaveInstanceState(outState);
    }

    @Override
    protected void onRestoreInstanceState(@NonNull Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        boolean[] temp = savedInstanceState.getBooleanArray("CheckBoxes");
        if (temp != null) {
            windCB.setSelected(temp[0]);
            pressureCB.setSelected(temp[1]);
        }
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