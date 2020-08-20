package ru.startandroid.android_komsomol.addMaterials;

public class Singleton {
    private static Singleton instance;
    private boolean dark;
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
}