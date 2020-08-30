package ru.startandroid.android_komsomol.addMaterials;

public class Singleton {
    private static Singleton instance;
    private boolean dark;
    private boolean alrChanged;
    private Singleton () {}

    public static Singleton getInstance() {
        if (instance == null) {
            instance = new Singleton();
            instance.dark = false;
        }
        return instance;
    }

    public boolean getDark(){
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