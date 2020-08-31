package ru.startandroid.android_komsomol;

import android.content.Context;
import android.graphics.Canvas;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class WeatherCompound extends RelativeLayout {
    private ImageView weatherImage;
    private TextView humidity, weatherInfo;

    public WeatherCompound(Context context) {
        super(context);
        initViews(context);
    }

    public WeatherCompound(Context context, AttributeSet attrs) {
        super(context, attrs);
        initViews(context);
    }

    public WeatherCompound(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initViews(context);
    }

    private void initViews(Context context) {
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        inflater.inflate(R.layout.weather_compound_view, this);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        findViews();
    }

    private void findViews() {
        weatherImage = findViewById(R.id.weatherImage);
        humidity = findViewById(R.id.textHumidity);
        weatherInfo = findViewById(R.id.textWeatherInfo);
    }
}
