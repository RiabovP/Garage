package com.ryabov.garage.Weather;

public class ForecastDays extends WeatherV1 {
    public String[] date;
    public String[] day;
    public String[] tempHigh;
    public String[] tempLow;

    public ForecastDays(){
        date = new String[10];
        day = new String[10];
        tempHigh = new String[10];
        tempLow = new String[10];
    }
}
