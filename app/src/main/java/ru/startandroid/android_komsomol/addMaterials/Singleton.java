package ru.startandroid.android_komsomol.addMaterials;

import ru.startandroid.android_komsomol.sharedPreferences.Database;

public class Singleton {
    private static Singleton instance;
    private boolean dark;
    private boolean alrChanged;
    private String city;
    private Database database;

    private Singleton() {
    }

    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
            instance.dark = false;
        }
        return instance;
    }

    public Database getDatabase() {
        return database;
    }

    public void setDatabase(Database database) {
        this.database = database;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public boolean getDark() {
        return dark;
    }

    public void setDark(boolean dark) {
        this.dark = dark;
    }

    public boolean isAlrChanged() {
        return alrChanged;
    }

    public void setAlrChanged(boolean alrChanged) {
        this.alrChanged = alrChanged;
    }
}