package ru.startandroid.android_komsomol;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Objects;

public class WeatherCardActivity extends AppCompatActivity {

    TextView wind;
    TextView pressure;
    TextView city;
    Button urlBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_weather_card);
        findViews();
        setListeners();
        addActions();
        getPrevData();
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        super.onOptionsItemSelected(item);
        if(item.getItemId() == android.R.id.home){
            finish();
        }
        return true;
    }

    private void findViews() {
        city = findViewById(R.id.textCity);
        wind = findViewById(R.id.textWindSpeed);
        pressure = findViewById(R.id.textPressure);
        urlBtn = findViewById(R.id.btnAboutCity);
    }

    private void setListeners() {
        urlBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse(getString(R.string.wikiRegion) + "wikipedia.org/wiki/" + city.getText());
                Intent cityInfoIntent = new Intent(Intent.ACTION_VIEW, uri);
                startActivity(cityInfoIntent);
            }
        });
    }

    private void addActions() {
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
    }

    private void getPrevData() {
        if (getIntent().getBooleanExtra("wind", true))
            wind.setVisibility(View.VISIBLE);
        else wind.setVisibility(View.GONE);
        if (getIntent().getBooleanExtra("pressure", true))
            pressure.setVisibility(View.VISIBLE);
        else pressure.setVisibility(View.INVISIBLE);
        String cityName = getIntent().getStringExtra("CityName");
        if (cityName != null) city.setText(cityName);
    }
}