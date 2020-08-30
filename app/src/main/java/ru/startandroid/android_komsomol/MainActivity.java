package ru.startandroid.android_komsomol;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import ru.startandroid.android_komsomol.addMaterials.Singleton;

@RequiresApi(api = Build.VERSION_CODES.N)
public class MainActivity extends AppCompatActivity {

    ChoosingFragment mainFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        if (Singleton.getInstance().getDark())
            setTheme(R.style.DarkTheme);
        Singleton.getInstance().setAlrChanged(true);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (!Singleton.getInstance().isAlrChanged()) recreate();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        handleMenuItem(item);
        return super.onOptionsItemSelected(item);
    }

    private void handleMenuItem(MenuItem item){
        int id = item.getItemId();
        switch (id){
            case R.id.menuAdd: {
                if (findMainFragment()){
                    mainFragment.addCityToList();
                }
                break;
            }
            case R.id.menuRemove: {
                if (findMainFragment()){
                    mainFragment.removeCityFromList();
                }
                break;
            }
            case R.id.menuSettings: {
                startActivity(new Intent(this, SettingsActivity.class));
            }
        }
    }

    private boolean findMainFragment(){
        if (mainFragment == null)
            mainFragment = (ChoosingFragment) getSupportFragmentManager().findFragmentById(R.id.choosingFragment);
        return mainFragment != null;
    }
}