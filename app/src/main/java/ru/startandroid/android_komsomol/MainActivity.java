package ru.startandroid.android_komsomol;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import ru.startandroid.android_komsomol.addMaterials.Singleton;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Singleton.getInstance().getDark())
            setTheme(R.style.DarkTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}