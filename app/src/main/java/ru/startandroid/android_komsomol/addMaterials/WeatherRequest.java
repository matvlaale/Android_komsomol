package ru.startandroid.android_komsomol.addMaterials;

import com.google.gson.annotations.SerializedName;

public class WeatherRequest {
    @SerializedName("weather") public Weather[] weather;
    @SerializedName("main") public Main main;
    @SerializedName("wind") public Wind wind;

    public static class Weather{
        @SerializedName("main") public String main;
    }

    public static class Main {
        @SerializedName("temp") public float temp;
        @SerializedName("feels_like") public float feelsLike;
        @SerializedName("pressure") public int pressure;
    }

    public static class Wind {
        @SerializedName("speed") public float speed;
    }
}