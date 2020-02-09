package com.ryabov.garage;

import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ryabov.garage.Weather.ForecastDays;
import com.ryabov.garage.Weather.RequestManager;
import com.ryabov.garage.Weather.WeatherRequest;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class ForecastActivity extends AppCompatActivity {

    TextView Temperature, Visibility, Pressure, Sunrise, Sunset, Wind, Humidity;

    TextView date_forecast, day_forecast, tempH_forecast, tempL_forecast;

    ForecastDays data=new ForecastDays();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forecast);

        Temperature=findViewById(R.id.cityWeatherTemp);
        Wind = findViewById(R.id.cityWeatherWind);
        Visibility=findViewById(R.id.cityWeatherVisible);
        Pressure=findViewById(R.id.cityWeatherPressure);
        Humidity=findViewById(R.id.cityWeatherHumidity);
        Sunrise=findViewById(R.id.cityWeatherSunrise);
        Sunset=findViewById(R.id.cityWeatherSunset);


        RequestManager requestManager=RequestManager.getInstance(this);
        WeatherRequest request=new WeatherRequest(Request.Method.GET, null, null, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                data = (ForecastDays) response;
                Temperature.setText(data.Temperature);
                Wind.setText(data.Wind);
                Visibility.setText(data.Visibility);
                Pressure.setText(data.Pressure);
                Humidity.setText(data.Humidity);

                Date dt;

                SimpleDateFormat sdfAm = new SimpleDateFormat("hh:mm a");
                SimpleDateFormat sdf24 = new SimpleDateFormat("HH:mm");
                try {
                    dt = sdfAm.parse(data.Sunrise);
                    Sunrise.setText(sdf24.format(dt));

                    dt = sdfAm.parse(data.Sunset);
                    Sunset.setText(sdf24.format(dt));

                } catch (ParseException e) {
                    e.printStackTrace();
                }

                int date_id = R.id.Date_0;
                int day_id = R.id.Day_0;
                int tempH_id = R.id.tempHigh_0;
                int tempL_id= R.id.tempLow_0;

                for (int i = 0; i < 10; i++){

                    date_forecast=findViewById(date_id++);
                    date_forecast.setText(data.date[i]);

                    day_forecast=findViewById(day_id++);
                    day_forecast.setText(data.day[i]);

                    tempH_forecast=findViewById(tempH_id++);
                    tempH_forecast.setText(data.tempHigh[i] + " " + getResources().getString(R.string.gradus));

                    tempL_forecast=findViewById(tempL_id++);
                    tempL_forecast.setText(data.tempLow[i] + " " + getResources().getString(R.string.gradus));
                }
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Add error handling here
            }
        });
        requestManager.addToRequestQueue(request);

    }
}
