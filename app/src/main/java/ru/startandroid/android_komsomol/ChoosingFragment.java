package ru.startandroid.android_komsomol;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Configuration;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.preference.PreferenceManager;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.textfield.TextInputEditText;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Objects;

import ru.startandroid.android_komsomol.addMaterials.PushesService;
import ru.startandroid.android_komsomol.sharedPreferences.Database;
import ru.startandroid.android_komsomol.addMaterials.EventBus;
import ru.startandroid.android_komsomol.addMaterials.IRVOnItemClick;
import ru.startandroid.android_komsomol.addMaterials.RecyclerDataAdapter;
import ru.startandroid.android_komsomol.addMaterials.Singleton;

@RequiresApi(api = Build.VERSION_CODES.N)
public class ChoosingFragment extends Fragment {

    private MaterialButton mainButton;
    private RecyclerView spinner;
    private CheckBox pressureCB;
    private CheckBox windCB;
    private TextInputEditText cityET;

    private Location location;
    private String cityGeo = "My location";
    private final String cityKey = "CityName";
    private SharedPreferences preferences;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        location();
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
        sharedPreferencesManage();
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    private void location() {
        LocationManager locationManager = (LocationManager) Objects.requireNonNull(getContext()).getSystemService(Context.LOCATION_SERVICE);
        if (locationManager != null) {
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                ActivityCompat.requestPermissions(Objects.requireNonNull(getActivity()),
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 11);
            }
            location = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if(location == null) location = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
        }

    }

    public void addCityToList() {
        String cityName = Objects.requireNonNull(cityET.getText()).toString();
        if (!((RecyclerDataAdapter) Objects.requireNonNull(spinner.getAdapter())).add(cityName)) {
            new AlertDialog.Builder(getContext())
                    .setTitle(R.string.same_word).setCancelable(true)
                    .setPositiveButton(getString(R.string.alert_back), new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {

                        }
                    }).create().show();
        }
    }

    public void removeCityFromList() {
        ((RecyclerDataAdapter) Objects.requireNonNull(spinner.getAdapter())).remove();
    }

    private void findViews(@NonNull View view) {
        mainButton = view.findViewById(R.id.mainButton);
        spinner = view.findViewById(R.id.spinCities);
        windCB = view.findViewById(R.id.cbWindSpeed);
        pressureCB = view.findViewById(R.id.cbPressure);
        cityET = view.findViewById(R.id.etCity);
    }

    public Bundle getData() {
        String cityName = Objects.requireNonNull(cityET.getText()).toString();
        Bundle bundle = new Bundle();
        bundle.putBoolean("wind", windCB.isChecked());
        bundle.putBoolean("pressure", pressureCB.isChecked());
        bundle.putString(cityKey, cityName);
        return bundle;
    }

    private void setListeners() {
        mainButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Network net = ((ConnectivityManager) Objects.requireNonNull(getContext()).getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetwork();
                if (Objects.requireNonNull(cityET.getText()).toString().trim().isEmpty()) {
                    Snackbar.make(Objects.requireNonNull(getView()), getString(R.string.error_empty_city), Snackbar.LENGTH_LONG).show();
                } else if (net == null) {
                    Snackbar.make(Objects.requireNonNull(getView()), getString(R.string.internet_lost), Snackbar.LENGTH_LONG).show();
                } else {
                    Bundle bundle = getData();
                    showWeather(bundle);
                }
            }
        });
    }

    private void sharedPreferencesManage() {
        Singleton.getInstance().setDatabase(Room.databaseBuilder
                (Objects.requireNonNull(getContext()), Database.class, Database.DB_NAME).build());
        preferences = PreferenceManager.getDefaultSharedPreferences(Objects.requireNonNull(getContext()));
        String cityName = preferences.getString(cityKey, "");
        new AlertDialog.Builder(getContext())
                .setTitle(R.string.open_city).setCancelable(true)
                .setPositiveButton(cityName, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Bundle bundle = getData();
                        showWeather(bundle);
                    }
                }).setNegativeButton(cityGeo, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                //cityET.setText(cityGeo);
                Bundle bundle = getData();
                bundle.putBoolean("isCoord", true);
                bundle.putDouble("lat", location.getLatitude());
                bundle.putDouble("lon", location.getLongitude());
                showWeather(bundle);
            }
        }).setNeutralButton(getString(R.string.alert_back), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        }).create().show();
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

    private void showWeather(Bundle bundle) {
        @SuppressLint("CommitPrefEdits") SharedPreferences.Editor editor = preferences.edit();
        editor.putString(cityKey, bundle.getString(cityKey));
        editor.apply();
        if (getResources().getConfiguration().orientation == Configuration.ORIENTATION_PORTRAIT) {
            Intent weatherCardIntent = new Intent(Objects.requireNonNull(getActivity()), WeatherCardActivity.class);
            weatherCardIntent.putExtra("Bundle", bundle);
            startActivity(weatherCardIntent);
        } else {
            if (Objects.requireNonNull(getActivity()).getSupportFragmentManager().findFragmentById(R.id.containerWCF) == null) {
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