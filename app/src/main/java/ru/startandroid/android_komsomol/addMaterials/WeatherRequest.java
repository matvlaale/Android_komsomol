package ru.startandroid.android_komsomol.addMaterials;

public class WeatherRequest {
    private Main main;
    private Wind wind;
    private int cod;

    public Main getMain() {
        return main;
    }

    public Wind getWind() {
        return wind;
    }

    public int getCod() {
        return cod;
    }

    public static class Main {
        private float temp;
        private int pressure;

        public float getTemp() {
            return temp - 273.15f;
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