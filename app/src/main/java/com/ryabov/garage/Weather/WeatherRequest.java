package com.ryabov.garage.Weather;


import com.android.volley.AuthFailureError;
import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.android.volley.toolbox.JsonRequest;
import com.google.gson.JsonSyntaxException;

import net.oauth.OAuth;
import net.oauth.OAuthAccessor;
import net.oauth.OAuthConsumer;
import net.oauth.OAuthException;
import net.oauth.OAuthMessage;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class WeatherRequest<T> extends JsonRequest<T> {

    final String appId = "Bms2Me7c";
    final String CONSUMER_KEY = "dj0yJmk9TXBOOWQ0RzBvUHN4JmQ9WVdrOVFtMXpNazFsTjJNbWNHbzlNQS0tJnM9Y29uc3VtZXJzZWNyZXQmeD0yOQ--";
    final String CONSUMER_SECRET = "898c0801e3a3a92043ae49255b2b978ace36295c";
    final String baseUrl = "https://weather-ydn-yql.media.yahoo.com/forecastrss";
    private Object IOException;
    private Object URISyntaxException;

    public WeatherRequest(int method, String url, String requestBody, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, requestBody, listener, errorListener);
    }

    @Override
    public Map<String, String> getHeaders() throws AuthFailureError {

        Map<String, String> headers = new HashMap<>();
        OAuthConsumer consumer = new OAuthConsumer(null, CONSUMER_KEY, CONSUMER_SECRET, null);
        consumer.setProperty(OAuth.OAUTH_SIGNATURE_METHOD, OAuth.HMAC_SHA1);
        OAuthAccessor accessor = new OAuthAccessor(consumer);
        OAuthMessage request = null;

        try {
            request = accessor.newRequestMessage(OAuthMessage.GET, getUrl(), null);
        } catch (OAuthException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (URISyntaxException e) {
            e.printStackTrace();
        }
        String authorization = null;
        try {
            authorization = request.getAuthorizationHeader(null);
        } catch (IOException e) {
            e.printStackTrace();
        }
        headers.put("Authorization", authorization);

        headers.put("X-Yahoo-App-Id", appId);
        headers.put("Content-Type", "application/json");
        return headers;
    }

    @Override
    public String getUrl() {
        return baseUrl + "?woeid=2122541&u=c&format=json";
    }

    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            String json = new String(
                    response.data,
                    HttpHeaderParser.parseCharset(response.headers));
            T parsedResponse = parseResponse(json);
            return Response.success(
                    parsedResponse,
                    HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException | JsonSyntaxException e) {
            return Response.error(new ParseError(e));
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (java.io.IOException e) {
            e.printStackTrace();
        }
        return null;
    }


    // Работа с данными из сети, парсинг строки Json
    private T parseResponse(String jsonString) throws IOException, JSONException {

        ForecastDays forecast_days = new ForecastDays();


        JSONObject jsonObject = new JSONObject(jsonString);

        JSONObject contents = jsonObject.getJSONObject("current_observation");

        JSONObject contents2=contents.getJSONObject("wind");
        forecast_days.Wind=contents2.getString("speed");

        contents2=contents.getJSONObject("atmosphere");
        forecast_days.Humidity=contents2.getString("humidity");
        forecast_days.Visibility=contents2.getString("visibility");
        forecast_days.Pressure=contents2.getString("pressure");

        contents2=contents.getJSONObject("astronomy");
        forecast_days.Sunrise=contents2.getString("sunrise");
        forecast_days.Sunset=contents2.getString("sunset");

        contents2=contents.getJSONObject("condition");
        forecast_days.Temperature= contents2.getString("temperature");

        JSONArray arrayContents = jsonObject.getJSONArray("forecasts");

        Date date;
        //Long longdate;

        for (int i=0; i<arrayContents.length(); i++)
        {
            JSONObject contents3 = arrayContents.getJSONObject(i);
            //longdate = Long.parseLong(contents3.getString("date"));
            date= new Date(Long.parseLong(contents3.getString("date"))*1000);
            SimpleDateFormat sdf = new SimpleDateFormat("dd.MM.yyyy");

            forecast_days.date[i]=sdf.format(date);
            forecast_days.day[i]=contents3.getString("day");
            forecast_days.tempHigh[i]=contents3.getString("high");
            forecast_days.tempLow[i]=contents3.getString("low");

        }




    return (T) forecast_days; // Add response parsing here
    }
}
