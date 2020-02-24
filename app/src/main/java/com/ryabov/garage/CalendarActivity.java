package com.ryabov.garage;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.ryabov.garage.Pogreb.DataService;
import com.ryabov.garage.Pogreb.PogrebokV1;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class CalendarActivity extends AppCompatActivity {

    String dateFromCalendar, jsonString;
    TextView TempMax, TempMin, DatePicker;
    PogrebokV1 pogreb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);

        Intent intent = getIntent();
        dateFromCalendar = intent.getStringExtra("dateCalendar");

        TempMax=findViewById(R.id.MaxTempDateCal);
        TempMin=findViewById(R.id.MinTempDateCal);
        DatePicker=findViewById(R.id.DatePicker);

        new progress_Task1().execute("http://37.193.0.199:1010/Tmax_Tmin.php?Tmax_Tmin&date=" + dateFromCalendar);

    }

    public class progress_Task1 extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... string) {

            final String errMessage;
            try {
                jsonString = getContent(string[0]);
            } catch (IOException ex) {
                errMessage = ex.getMessage();
            }
            return jsonString;
        }


        @Override
        protected void onPostExecute(String content) {
            super.onPostExecute(content);

            try {
                pogreb = DataService.GetPorgrebokData_temp_calendar(jsonString);

                TempMax.setText(TempMax.getText() + " " + pogreb.street_temp_max_byDateCal + getResources().getString(R.string.gradus));
                TempMin.setText(TempMin.getText() + " " + pogreb.street_temp_min_byDateCal + getResources().getString(R.string.gradus));
                DatePicker.setText(DatePicker.getText() + " " + dateFromCalendar);

            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (JSONException ex) {
                ex.printStackTrace();
            }
        }

        private String getContent(String path) throws IOException {

            BufferedReader bufferedReader = null;

            try {
                URL url = new URL(path);
                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
                bufferedReader = new BufferedReader(inputStreamReader);
                String line = bufferedReader.readLine();

                return line;
            } finally {
                if (bufferedReader != null)
                    bufferedReader.close();
            }
        }

    }
}
