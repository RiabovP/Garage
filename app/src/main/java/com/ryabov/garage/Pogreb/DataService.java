package com.ryabov.garage.Pogreb;

import org.json.JSONException;
import org.json.JSONObject;
import java.lang.Float;

import java.io.IOException;
import java.util.Locale;

public class DataService {

    public static PogrebokV1 GetPorgrebokData(String jsonString) throws IOException, JSONException {

        JSONObject jsonObject = new JSONObject(jsonString);

        JSONObject contents = jsonObject.getJSONObject("contents");

        PogrebokV1 data = new PogrebokV1();
       // Double priceKwt;

        data.cellar_temp=contents.getString("temp_cellar");
        data.date_hange=contents.getString("date_hange");
        data.heating=contents.getString("heating");
        data.home_temp=contents.getString("temp_home");
        data.kwt_full= contents.getString("kwt_full");
        data.pressure=contents.getString("pressure");
        data.street_temp_current=contents.getString("temp_street");
        data.street_temp_max=contents.getString("temp_street_max");
        data.street_temp_min=contents.getString("temp_street_min");
        data.time_power=contents.getString("time_power");
        data.price_kWt = contents.getString("kwt_full");

//        priceKwt=Double.parseDouble(data.price_kWt);
//        priceKwt=(double)Math.round(priceKwt*2.68);
       // data.price_kWt= Double.toString(priceKwt);

        //data.price_kWt=Float.toString((float) (Float.parseFloat(data.price_kWt)*2.68));
        data.price_kWt=String.format(Locale.US, "%.2f",(float) (Float.parseFloat(data.price_kWt)*2.68));
        data.time_power=String.format(Locale.US,"%.2f",(float)(Float.parseFloat(data.time_power)/60));

        return data;
    }

    public static PogrebokV1 GetPorgrebokData_temp (String jsonString) throws IOException, JSONException {

        JSONObject jsonObject = new JSONObject(jsonString);

        JSONObject contents = jsonObject.getJSONObject("contents");

        PogrebokV1 data = new PogrebokV1();
        // Double priceKwt;

        data.street_temp_max_byDate=contents.getString("Tmax");
        data.street_temp_min_byDate=contents.getString("Tmin");




        return data;
    }
}
