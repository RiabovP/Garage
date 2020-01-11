package com.ryabov.garage;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.ryabov.garage.Pogreb.DataService;
import com.ryabov.garage.Pogreb.PogrebokV1;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    String jsonString, jsonString1;

    TextView DateHange;

    Button ConsumptionEnergy;
    Button CostEnergy;
    Button ForecastTemp;
    Button Pressure;
    Button TempHome;
    Button TempMax;
    Button TempMin;
    Button TempPogrebok;
    Button TempStreet;
    Button TimeWarm;
    Button On_Off;
    Button Refresh;

    PogrebokV1 pogreb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);

        ConsumptionEnergy=findViewById(R.id.RashodEE);
        CostEnergy=findViewById(R.id.PriceEE);
        DateHange=findViewById(R.id.dateUpdate);
        ForecastTemp=findViewById(R.id.buttWeather);
        Pressure=findViewById(R.id.Pressure);
        TempHome=findViewById(R.id.TempHome);
        TempMax=findViewById(R.id.TempMax);
        TempMin=findViewById(R.id.TempMin);
        TempPogrebok=findViewById(R.id.TempPogrebok);
        TempStreet=findViewById(R.id.TempStreet);
        TimeWarm=findViewById(R.id.TimeWarm);
        On_Off=findViewById(R.id.butsOnOff);
        Refresh=findViewById(R.id.Refresh);

        new progress_Task().execute("http://37.193.0.199:1010/home2.txt", "New");

        Refresh.setOnClickListener(this);
        TempMax.setOnClickListener(this);
        TempMin.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.Refresh:
                new refresh_data().execute("http://37.193.0.199:1010/home2.txt");
                break;
            case R.id.TempMax:
                new progress_Task().execute("http://37.193.0.199:1010/Tmax_Tmin.php?Tmax_Tmin", "Max");
                break;
            case R.id.TempMin:
                new progress_Task().execute("http://37.193.0.199:1010/Tmax_Tmin.php?Tmax_Tmin", "Min");
                break;
        }
    }

    // Первичная загрузка данных при запуске приложения (на потом, попытаться условиями разрулить обновление и первый запуск)
    public class progress_Task extends AsyncTask <String, Void, String> {

       // AlertDialog.Builder builder;

        String test;

        @Override
        protected String doInBackground(String... string){
            final String errmessage;
            try{
                jsonString=getContent(string[0]);
                test=string[1];
            } catch (IOException ex){
                errmessage = ex.getMessage();
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Ошибка");
                        builder.setMessage(errmessage);
                        //builder.setNeutralButton("Ok",null);
                        //builder.setNeutralButton("График за месяц", null);
                        builder.setPositiveButton("Ok", null);
                        //builder.setNegativeButton("Cancel", null);
                        builder.show();
                    }
                });

            }
            return jsonString;
        }

        @Override
        protected void onPostExecute(String content) {
            super.onPostExecute(content);

            try {
                if (test == "New") {
                    pogreb = DataService.GetPorgrebokData(jsonString);
                    ConsumptionEnergy.setText(ConsumptionEnergy.getText() + "\n\n" + pogreb.kwt_full + " руб");
                    CostEnergy.setText(CostEnergy.getText() + "\n\n" + pogreb.price_kWt + " кВт");
                    DateHange.setText(pogreb.date_hange);
                    Pressure.setText(Pressure.getText() + "\n\n" + pogreb.pressure);
                    TempHome.setText(TempHome.getText() + "\n\n" + pogreb.home_temp + " °C");
                    TempMax.setText(TempMax.getText() + "\n\n" + pogreb.street_temp_max + " °C");
                    TempMin.setText(TempMin.getText() + "\n\n" + pogreb.street_temp_min + " °C");
                    TempPogrebok.setText(TempPogrebok.getText() + "\n\n" + pogreb.cellar_temp + " °C");
                    TempStreet.setText(TempStreet.getText() + "\n\n" + pogreb.street_temp_current + " °C");
                    TimeWarm.setText(TimeWarm.getText() + "\n\n" + pogreb.time_power);
                    if (pogreb.heating == "1")
                        On_Off.setBackgroundResource(R.drawable.power_on);
                } else if (test=="Max"){
                    pogreb = DataService.GetPorgrebokData_temp(jsonString);

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Максимальная температура");
                    builder.setMessage("Максимальная температура на улице за месяц " + pogreb.street_temp_max_byDate + " °C");
                    //builder.setNeutralButton("Ok",null);
                    builder.setNeutralButton("График за месяц", null);
                    builder.setPositiveButton("Ok", null);
                    //builder.setNegativeButton("Cancel", null);
                    builder.show();

                } else if (test=="Min"){
                    pogreb = DataService.GetPorgrebokData_temp(jsonString);

                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Минимальная температура");
                    builder.setMessage("Минимальная температура на улице за месяц " + pogreb.street_temp_min_byDate + " °C");
                    //builder.setNeutralButton("Ok",null);
                    builder.setNeutralButton("График за месяц", null);
                    builder.setPositiveButton("Ok", null);
                    //builder.setNegativeButton("Cancel", null);
                    builder.show();
                }

            }catch(IOException e){
                    e.printStackTrace();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Ошибка");
                builder.setMessage(e.getMessage());
                //builder.setNeutralButton("Ok",null);
                //builder.setNeutralButton("График за месяц", null);
                builder.setPositiveButton("Ok", null);
                //builder.setNegativeButton("Cancel", null);
                builder.show();
                }catch(JSONException e){
                    e.printStackTrace();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Ошибка");
                builder.setMessage(e.getMessage());
                //builder.setNeutralButton("Ok",null);
                //builder.setNeutralButton("График за месяц", null);
                builder.setPositiveButton("Ok", null);
                //builder.setNegativeButton("Cancel", null);
                builder.show();
                }

            }

        }

        private String getContent(String path) throws IOException {

            BufferedReader bufferedReader=null;

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
            }
            finally {
                if (bufferedReader !=null)
                    bufferedReader.close();
            }
        }

    // Обновление данных погребка / гаража(рефреш)

    public class refresh_data extends AsyncTask<String, Void, String> {

    @Override
        protected String doInBackground(String... string) {

        final String errMessage;
            try {
                jsonString = getContent(string[0]);
            } catch (IOException ex) {
                errMessage = ex.getMessage();
                MainActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                        builder.setTitle("Ошибка");
                        builder.setMessage(errMessage);
                        //builder.setNeutralButton("Ok",null);
                        //builder.setNeutralButton("График за месяц", null);
                        builder.setPositiveButton("Ok", null);
                        //builder.setNegativeButton("Cancel", null);
                        builder.show();
                    }
                });
            }
            return jsonString;
        }

        @Override
        protected void onPostExecute (String content) {
            super.onPostExecute(content);

            try {
                pogreb = DataService.GetPorgrebokData(jsonString);

                ConsumptionEnergy.setText(getResources().getString(R.string.EnergyConsumption) + "\n\n" + pogreb.kwt_full);
                CostEnergy.setText(getResources().getString(R.string.PriceEnergy) + "\n\n" + pogreb.price_kWt);
                DateHange.setText(pogreb.date_hange);
                Pressure.setText(getResources().getString(R.string.Pressure) + "\n\n" + pogreb.pressure);
                TempHome.setText(getResources().getString(R.string.TempHome) + "\n\n" + pogreb.home_temp + " °C");
                TempMax.setText(getResources().getString(R.string.TempMax) + "\n\n" + pogreb.street_temp_max + " °C");
                TempMin.setText(getResources().getString(R.string.TempMin) + "\n\n" + pogreb.street_temp_min + " °C");
                TempPogrebok.setText(getResources().getString(R.string.TempPogrebok) + "\n\n" + pogreb.cellar_temp + " °C");
                TempStreet.setText(getResources().getString(R.string.TempStreet) + "\n\n" + pogreb.street_temp_current + " °C");
                TimeWarm.setText(getResources().getString(R.string.AmountHours) +"\n\n" + pogreb.time_power);
                if(pogreb.heating == "1")
                    On_Off.setBackgroundResource(R.drawable.power_on);

            }catch (IOException ex) {
                ex.printStackTrace();
            }catch (JSONException ex) {
                ex.printStackTrace();
            }
        }

        private String getContent(String path) throws IOException {

            BufferedReader bufferedReader=null;

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
            }
            finally {
                if (bufferedReader !=null)
                    bufferedReader.close();
            }
        }
    }

}

