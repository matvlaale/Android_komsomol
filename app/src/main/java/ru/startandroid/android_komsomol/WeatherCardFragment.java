package ru.startandroid.android_komsomol;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.squareup.otto.Subscribe;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import ru.startandroid.android_komsomol.addMaterials.EventBus;
import ru.startandroid.android_komsomol.addMaterials.IRVOnItemClick;
import ru.startandroid.android_komsomol.addMaterials.RecyclerDataAdapter;

public class WeatherCardFragment extends Fragment {

    private TextView wind;
    private TextView pressure;
    private TextView city;
    private MaterialButton urlBtn;
    private RecyclerView moreDays;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getBus().register(this);
        if (Objects.requireNonNull(getActivity()).getClass() == WeatherCardActivity.class) {
            ((WeatherCardActivity) getActivity()).getDataFromIntent();
        } else {
            onBundle(((ChoosingFragment) Objects.requireNonNull(getActivity().
                    getSupportFragmentManager().findFragmentById(R.id.choosingFragment))).getData());
        }
    }

    @Override
    public void onStop() {
        EventBus.getBus().unregister(this);
        super.onStop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_weather_card, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        findViews(view);
        setListeners();
        otherOptions();
    }

    private void findViews(@NonNull View view) {
        city = view.findViewById(R.id.textCity);
        wind = view.findViewById(R.id.textWindSpeed);
        pressure = view.findViewById(R.id.textPressure);
        urlBtn = view.findViewById(R.id.btnAboutCity);
        moreDays = view.findViewById(R.id.rvThreeDays);
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

    @Subscribe
    public void onBundle(Bundle bundle) {
        if (bundle.getBoolean("wind", true))
            wind.setVisibility(View.VISIBLE);
        else wind.setVisibility(View.GONE);
        if (bundle.getBoolean("pressure", true))
            pressure.setVisibility(View.VISIBLE);
        else pressure.setVisibility(View.GONE);
        String cityName = bundle.getString("CityName");
        if (cityName != null) city.setText(cityName);
    }

    public void otherOptions(){
        LinearLayoutManager manager = new GridLayoutManager(getContext(), 3);
        RecyclerDataAdapter adapter = new RecyclerDataAdapter(
                new ArrayList<>(Arrays.asList("26°C", "28°C", "22°C")), new IRVOnItemClick() {
            @Override
            public void onItemClicked(String itemText) {

            }
        });
        moreDays.setLayoutManager(manager);
        moreDays.setAdapter(adapter);
    }
}