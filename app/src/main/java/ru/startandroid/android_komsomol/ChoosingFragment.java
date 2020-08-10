package ru.startandroid.android_komsomol;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;

import java.util.Objects;

public class ChoosingFragment extends Fragment {

    private Button mainButton;
    private ImageView settingsButton;
    private Spinner spinner;
    private CheckBox pressureCB;
    private CheckBox windCB;
    private EditText cityET;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_choosing, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setRetainInstance(true);
        findViews(view);
        setListeners();
        otherOptions();
    }

    private void findViews(@NonNull View view) {
        mainButton = view.findViewById(R.id.mainButton);
        settingsButton = view.findViewById(R.id.btnSettings);
        spinner = view.findViewById(R.id.spinCities);
        windCB = view.findViewById(R.id.cbWindSpeed);
        pressureCB = view.findViewById(R.id.cbPressure);
        cityET = view.findViewById(R.id.etCity);
    }

    public Bundle getData(){
        String cityName = cityET.getText().toString();
        Bundle bundle = new Bundle();
        bundle.putBoolean("wind", windCB.isChecked());
        bundle.putBoolean("pressure", pressureCB.isChecked());
        bundle.putString("CityName", cityName);
        return bundle;
    }

    private void setListeners() {
        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
                    Intent weatherCardIntent = new Intent(Objects.requireNonNull(getActivity()), WeatherCardActivity.class);
                    weatherCardIntent.putExtra("Bundle", getData());
                    startActivity(weatherCardIntent);
                } else {
                    if (Objects.requireNonNull(getActivity()).getSupportFragmentManager().findFragmentById(R.id.containerWCF) == null){
                        WeatherCardFragment weatherCard = new WeatherCardFragment();
                        FragmentTransaction ft = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                        ft.add(R.id.containerWCF, weatherCard);
                        ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                        ft.commit();
                    }
                    EventBus.getBus().post(getData());
                }
            }
        });

        settingsButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Objects.requireNonNull(getActivity()), SettingsActivity.class));
            }
        });

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                cityET.setText(spinner.getItemAtPosition(position).toString());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    private void otherOptions() {
        spinner.setAdapter(
                ArrayAdapter.createFromResource(Objects.requireNonNull(getActivity()), R.array.popularCities, R.layout.support_simple_spinner_dropdown_item));
    }
}