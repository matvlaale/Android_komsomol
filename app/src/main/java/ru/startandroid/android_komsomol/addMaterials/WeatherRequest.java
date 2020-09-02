package ru.startandroid.android_komsomol.addMaterials;

public class WeatherRequest {
    private Main main;
    private Wind wind;

    public Main getMain() {
        return main;
    }

    public Wind getWind() {
        return wind;
    }

    public static class Main {
        private float temp;
        private float feels_like;
        private int pressure;

        public float getTemp() {
            return temp - 273.15f;
        }

        public float getFeels_like() {
            return feels_like - 273.15f;
        }

        public int getPressure() {
            return pressure;
        }
    }

    public static class Wind {
        private float speed;

        public float getSpeed() {
            return speed;
        }
    }
}