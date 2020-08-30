package ru.startandroid.android_komsomol;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.gson.Gson;
import com.squareup.otto.Subscribe;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Objects;
import java.util.stream.Collectors;

import javax.net.ssl.HttpsURLConnection;

import ru.startandroid.android_komsomol.addMaterials.EventBus;
import ru.startandroid.android_komsomol.addMaterials.WeatherRequest;

@RequiresApi(api = Build.VERSION_CODES.N)
public class WeatherCardFragment extends Fragment {

    private TextView wind;
    private TextView pressure;
    private TextView temp;
    private TextView city;
    private TextView header;
    private Bundle defaultData;
    private AlertDialog.Builder alert;
    private FragmentActivity activity;
    private MaterialButton urlBtn;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onStart() {
        super.onStart();
        EventBus.getBus().register(this);
        if (activity.getClass() == WeatherCardActivity.class) {
            ((WeatherCardActivity) activity).getDataFromIntent();
        } else {
            onBundle(((ChoosingFragment) Objects.requireNonNull(activity.
                    getSupportFragmentManager().findFragmentById(R.id.choosingFragment))).getData());
        }
        showWeather();
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
        otherOptions();
        setListeners();
    }

    private void findViews(@NonNull View view) {
        activity = Objects.requireNonNull(getActivity());
        city = view.findViewById(R.id.textCity);
        wind = view.findViewById(R.id.textWindSpeed);
        pressure = view.findViewById(R.id.textPressure);
        temp = view.findViewById(R.id.textTemperature);
        header = view.findViewById(R.id.mainHeader);
        urlBtn = view.findViewById(R.id.btnAboutCity);
    }

    private void setVisibility(boolean visible){
        int visibility = visible ? View.VISIBLE : View.INVISIBLE;
        city.setVisibility(visibility);
        temp.setVisibility(visibility);
        header.setVisibility(visibility);
        urlBtn.setVisibility(visibility);
        if (visible) onBundle(defaultData);
        else {
            wind.setVisibility(visibility);
            pressure.setVisibility(visibility);
        }
    }

    private void otherOptions() {
        alert = new AlertDialog.Builder(activity)
                .setTitle(R.string.error).setCancelable(false)
                .setPositiveButton(getString(R.string.alert_retry), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        setVisibility(true);
                        showWeather();
                    }
                }).setNegativeButton(getString(R.string.alert_back), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                activity.finish();
            }
        });
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
        defaultData = bundle;
        if (bundle.getBoolean("wind", true))
            wind.setVisibility(View.VISIBLE);
        else wind.setVisibility(View.GONE);
        if (bundle.getBoolean("pressure", true))
            pressure.setVisibility(View.VISIBLE);
        else pressure.setVisibility(View.GONE);
        String cityName = bundle.getString("CityName");
        if (cityName != null) city.setText(cityName);
    }

    public void showWeather() {
        DataHandler handler = new DataHandler();
        handler.getWeather(city.getText().toString());
    }

    public void showError(Handler handler, final String error) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                setVisibility(false);
                alert.setMessage(error).create().show();
            }
        });

    }

    @SuppressLint({"DefaultLocale", "SetTextI18n"})
    public void displayWeather(WeatherRequest weather) {
        String temperature = String.format("%.0f", weather.getMain().getTemp()) + " " + getString(R.string.temperature);
        String pressure = String.format("%d", weather.getMain().getPressure()) + " " + getString(R.string.pressure);
        String wind = String.format("%.0f", weather.getWind().getSpeed()) + " " + getString(R.string.windSpeed);
        this.temp.setText(temperature);
        this.pressure.setText(pressure);
        this.wind.setText(wind);
    }

    private class DataHandler {
        private static final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather?q=";
        private static final String TAG = "WeatherDataHandler";
        private BufferedReader in;

        public void getWeather(String city) {
            final Handler handler = new Handler();
            try {
                final URL uri = new URL(WEATHER_URL + city + "&appid=d7abcb6e6b0ede6bb3282873e3d67ccc");
                new Thread(new Runnable() {
                    public void run() {
                        HttpsURLConnection urlConnection = null;
                        try {
                            urlConnection = (HttpsURLConnection) uri.openConnection();
                            urlConnection.setRequestMethod("GET");
                            urlConnection.setReadTimeout(10000);
                            in = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                            String result = getLines();
                            Gson gson = new Gson();
                            final WeatherRequest weatherRequest = gson.fromJson(result, WeatherRequest.class);
                            handler.post(new Runnable() {
                                @Override
                                public void run() {
                                    displayWeather(weatherRequest);
                                }
                            });
                        } catch (Exception e) {
                            Log.e(TAG, "Fail connection", e);
                            e.printStackTrace();
                            showError(handler, "Error of getting");
                        } finally {
                            if (null != urlConnection) {
                                urlConnection.disconnect();
                            }
                        }
                    }
                }).start();
            } catch (MalformedURLException e) {
                Log.e(TAG, "Fail URI", e);
                e.printStackTrace();
                showError(handler, "Error of connecting");
            }
        }

        private String getLines() {
            return in.lines().collect(Collectors.joining("\n"));
        }
    }
}