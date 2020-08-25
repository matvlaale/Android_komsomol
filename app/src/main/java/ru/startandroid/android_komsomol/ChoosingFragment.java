package ru.startandroid.android_komsomol;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.Toolbar;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import ru.startandroid.android_komsomol.addMaterials.EventBus;
import ru.startandroid.android_komsomol.addMaterials.IRVOnItemClick;
import ru.startandroid.android_komsomol.addMaterials.RecyclerDataAdapter;

@RequiresApi(api = Build.VERSION_CODES.N)
public class ChoosingFragment extends Fragment {

    private MaterialButton mainButton;
    private RecyclerView spinner;
    private CheckBox pressureCB;
    private CheckBox windCB;
    private TextInputEditText cityET;

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

    public void addCityToList (){
        String cityName = Objects.requireNonNull(cityET.getText()).toString();
        ((RecyclerDataAdapter) Objects.requireNonNull(spinner.getAdapter())).add(cityName);
    }

    public void removeCityFromList (){
        ((RecyclerDataAdapter) Objects.requireNonNull(spinner.getAdapter())).remove();
    }

    private void findViews(@NonNull View view) {
        mainButton = view.findViewById(R.id.mainButton);
        spinner = view.findViewById(R.id.spinCities);
        windCB = view.findViewById(R.id.cbWindSpeed);
        pressureCB = view.findViewById(R.id.cbPressure);
        cityET = view.findViewById(R.id.etCity);
    }

    public Bundle getData(){
        String cityName = Objects.requireNonNull(cityET.getText()).toString();
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
                Bundle bundle = getData();
                showWeather(bundle);
            }
        });
    }

    private void otherOptions() {
        LinearLayoutManager manager = new GridLayoutManager(getContext(), 2);
        RecyclerDataAdapter adapter = new RecyclerDataAdapter(
                new ArrayList<>(Arrays.asList(getResources().getStringArray(R.array.popularCities))), new IRVOnItemClick() {
            @Override
            public void onItemClicked(String itemText) {
                cityET.setText(itemText);
                Bundle bundle = getData();
                showWeather(bundle);
            }
        });
        spinner.setLayoutManager(manager);
        spinner.setAdapter(adapter);
    }

    private void showWeather(Bundle bundle){
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            Intent weatherCardIntent = new Intent(Objects.requireNonNull(getActivity()), WeatherCardActivity.class);
            weatherCardIntent.putExtra("Bundle", bundle);
            startActivity(weatherCardIntent);
        } else {
            if (Objects.requireNonNull(getActivity()).getSupportFragmentManager().findFragmentById(R.id.containerWCF) == null){
                WeatherCardFragment weatherCard = new WeatherCardFragment();
                FragmentTransaction ft = Objects.requireNonNull(getActivity()).getSupportFragmentManager().beginTransaction();
                ft.add(R.id.containerWCF, weatherCard);
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
                ft.commit();
            }
            EventBus.getBus().post(bundle);
        }
    }
}