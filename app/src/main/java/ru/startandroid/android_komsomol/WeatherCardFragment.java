package ru.startandroid.android_komsomol;

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
import android.widget.ProgressBar;
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

    private TextView wind, pressure, temp, city, header;
    private ProgressBar progressBar;
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
        setVisibility(false);
        showProgress(true);
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
        progressBar = view.findViewById(R.id.progressBar);
    }

    private void setVisibility(boolean visible) {
        int visibility = visible ? View.VISIBLE : View.INVISIBLE;
        city.setVisibility(visibility);
        temp.setVisibility(visibility);
        header.setVisibility(visibility);
        urlBtn.setVisibility(visibility);
        if (visible) {
            showProgress(false);
            useBundle();
        } else {
            wind.setVisibility(visibility);
            pressure.setVisibility(visibility);
        }
    }

    private void showProgress(boolean show) {
        progressBar.setVisibility(show ? View.VISIBLE : View.INVISIBLE);
    }

    private void otherOptions() {
        alert = new AlertDialog.Builder(activity)
                .setTitle(R.string.error).setCancelable(false)
                .setPositiveButton(getString(R.string.alert_retry), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        showProgress(true);
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
        String cityName = defaultData.getString("CityName");
        if (cityName != null) city.setText(cityName);
    }

    private void useBundle() {
        if (defaultData.getBoolean("wind", true))
            wind.setVisibility(View.VISIBLE);
        else wind.setVisibility(View.GONE);
        if (defaultData.getBoolean("pressure", true))
            pressure.setVisibility(View.VISIBLE);
        else pressure.setVisibility(View.GONE);
    }

    public void showWeather() {
        DataHandler handler = new SimpleDataHandler();
        handler.setParcier(new SimpleParcier());
        handler.getWeather(city.getText().toString());
    }

    public void showError(Handler handler, final String error) {
        handler.post(new Runnable() {
            @Override
            public void run() {
                showProgress(false);
                alert.setMessage(error).create().show();
            }
        });

    }

    public void displayWeather(WeatherBundle weather) {
        setVisibility(true);
        String temperature = String.format("%.0f (%.0f) " + getString(R.string.temperature), weather.temperature, weather.tempFeels);
        String pressure = String.format("%d " + getString(R.string.pressure), weather.pressure);
        String wind = String.format("%.0f " + getString(R.string.windSpeed), weather.windSpeed);
        this.temp.setText(temperature);
        this.pressure.setText(pressure);
        this.wind.setText(wind);
    }

    private class WeatherBundle {
        public float temperature;
        public float tempFeels;
        public int pressure;
        public float windSpeed;
    }

    public interface DataParcier {
        void sendWeatherToFragment(String result, Handler handler);
    }

    public interface DataHandler {
        DataParcier parcier = null;

        void getWeather(String city);

        void setParcier(DataParcier parcier);
    }

    private class SimpleDataHandler implements DataHandler {
        private static final String WEATHER_URL = "https://api.openweathermap.org/data/2.5/weather?q=";
        private static final String TAG = "WeatherDataHandler";
        private DataParcier parcier;
        private BufferedReader in;

        @Override
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
                            parcier.sendWeatherToFragment(result, handler);
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

        @Override
        public void setParcier(DataParcier parcier) {
            this.parcier = parcier;
        }

        private String getLines() {
            return in.lines().collect(Collectors.joining("\n"));
        }
    }

    private class SimpleParcier implements DataParcier {
        @Override
        public void sendWeatherToFragment(final String result, Handler handler) {
            final WeatherRequest rawData = formRequest(result);
            final WeatherBundle weather = formBundle(rawData);
            handler.post(new Runnable() {
                @Override
                public void run() {
                    displayWeather(weather);
                }
            });
        }

        private WeatherBundle formBundle(WeatherRequest data) {
            WeatherBundle weather = new WeatherBundle();
            WeatherRequest.Main main = data.getMain();
            weather.temperature = main.getTemp();
            weather.tempFeels = main.getFeels_like();
            weather.pressure = main.getPressure();
            weather.windSpeed = data.getWind().getSpeed();
            return weather;
        }

        private WeatherRequest formRequest(String result) {
            return new Gson().fromJson(result, WeatherRequest.class);
        }
    }
}