package com.ryabov.garage;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.ryabov.garage.Pogreb.DataService;
import com.ryabov.garage.Pogreb.Pogreb_graph;
import com.ryabov.garage.Pogreb.PogrebokV1;
import com.ryabov.garage.Weather.ForecastDays;
import com.ryabov.garage.Weather.RequestManager;
import com.ryabov.garage.Weather.WeatherRequest;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Calendar;

//import android.app.AlertDialog;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    String jsonString, jsonString1, data_set;

    int backPress=0;

    private static long BackPressed;

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
    Button Report;

    PogrebokV1 pogreb;

    Pogreb_graph pogreb_graph;

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
        Report = findViewById(R.id.countTurnOn);

        new progress_Task().execute("http://37.193.0.199:1010/home2.txt", "New");

        Refresh.setOnClickListener(this);
        TempMax.setOnClickListener(this);
        TempMin.setOnClickListener(this);
        ForecastTemp.setOnClickListener(this);
        Report.setOnClickListener(this);
    }

    // Запрос к Апи-погоды
    @Override
    public void onStart()
    {
        super.onStart();

        //WeatherV1 data=new WeatherV1();
        RequestManager requestManager=RequestManager.getInstance(this);
        WeatherRequest request=new WeatherRequest(Request.Method.GET, null, null, new Response.Listener() {
            @Override
            public void onResponse(Object response) {
                ForecastDays data = (ForecastDays) response;
                ForecastTemp.setText(getResources().getString(R.string.Forecast)+ "\n\n" + data.Temperature + " " + getResources().getString(R.string.gradus));
            }
        },new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                // Add error handling here
            }
        });
        requestManager.addToRequestQueue(request);
    }

    //Выход по двойному нажатию на Back из приложения
    @Override
    public void onBackPressed() {

        if (BackPressed + 2000 > System.currentTimeMillis())
        {
            super.onBackPressed();
        }

        if (backPress==0 || BackPressed + 2100 < System.currentTimeMillis()){
            Toast.makeText(this,"Нажмите еще раз для выхода", Toast.LENGTH_SHORT).show();
            BackPressed=System.currentTimeMillis();
            backPress++;
        }
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.Refresh:
                //new refresh_data().execute("http://37.193.0.199:1010/home2.txt");
                new progress_Task().execute("http://37.193.0.199:1010/home2.txt", "Refresh_data");
                break;
            case R.id.TempMax:
                new progress_Task().execute("http://37.193.0.199:1010/Tmax_Tmin.php?Tmax_Tmin", "Max");
                break;
            case R.id.TempMin:
                new progress_Task().execute("http://37.193.0.199:1010/Tmax_Tmin.php?Tmax_Tmin", "Min");
                break;
            case R.id.buttWeather:
               // WeatherV1 data=new WeatherV1();
               /* RequestManager requestManager=RequestManager.getInstance(this);
                WeatherRequest request=new WeatherRequest(Request.Method.GET, null, null, new Response.Listener() {
                    @Override
                    public void onResponse(Object response) {
                        ForecastDays data = (ForecastDays) response;
                        ForecastTemp.setText(getResources().getString(R.string.Forecast)+ "\n\n" + data.Temperature);
                    }
                },new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Add error handling here
                    }
                });
                requestManager.addToRequestQueue(request);*/
                Intent intent = new Intent(this, ForecastActivity.class);
                startActivity(intent);
                overridePendingTransition(R.anim.trans, R.anim.alpha);
                break;
            case R.id.countTurnOn:
                showDialog(1);
                break;
            case R.id.button2:

                Intent intent1 = new Intent(MainActivity.this, tempGraph.class);
                //intent1.putExtra("dateCalendar", data_set);
                startActivity(intent1);

                break;
        }
    }

    //Создание диалого календаря
    protected Dialog onCreateDialog (int id){
        if(id==1){
            Calendar currentDate= Calendar.getInstance();
            DatePickerDialog dialogCal = new DatePickerDialog(this, myCallBack, currentDate.get(Calendar.YEAR), currentDate.get(Calendar.MONTH), currentDate.get(Calendar.DAY_OF_MONTH));
            dialogCal.getDatePicker().setMaxDate(System.currentTimeMillis()); //Установка максимальной даты в диалоге

            return dialogCal;

        }
        return super.onCreateDialog(id);
}

    //Вызов диалога календаря
    DatePickerDialog.OnDateSetListener myCallBack = new DatePickerDialog.OnDateSetListener() {

        @Override
    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {

            data_set = year + "." + (month + 1) + "." + dayOfMonth;

            Intent intent1 = new Intent(MainActivity.this, CalendarActivity.class);
            intent1.putExtra("dateCalendar", data_set);
            startActivity(intent1);

        }
};


    // Первичная загрузка данных при запуске приложения (на потом, попытаться условиями разрулить обновление и первый запуск)
    public class progress_Task extends AsyncTask <String, Void, String> {
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
                                builder.setPositiveButton("Ok", null);
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
                    ConsumptionEnergy.setText(ConsumptionEnergy.getText() + "\n\n" + pogreb.kwt_full + " кВт");
                    CostEnergy.setText(CostEnergy.getText() + "\n\n" + pogreb.price_kWt + " руб");
                    DateHange.setText(pogreb.date_hange);
                    Pressure.setText(Pressure.getText() + "\n\n" + pogreb.pressure);
                    TempHome.setText(TempHome.getText() + "\n\n" + pogreb.home_temp + " °C");

                    TempMax.setText(TempMax.getText() + "\n\n" + pogreb.street_temp_max + " °C");
                    TempMin.setText(TempMin.getText() + "\n\n" + pogreb.street_temp_min + " °C");
                    TempPogrebok.setText(TempPogrebok.getText() + "\n\n" + pogreb.cellar_temp + " °C");
                    TempStreet.setText(TempStreet.getText() + "\n\n" + pogreb.street_temp_current + " °C");
                    TimeWarm.setText(TimeWarm.getText() + "\n\n" + pogreb.time_power);
                    if ("1".equals(pogreb.heating))
                      On_Off.setBackgroundResource(R.drawable.power_on);
                    else if ("0".equals(pogreb.heating))
                        On_Off.setBackgroundResource(R.drawable.power_off_1);
                } else if (test=="Max"){
                    pogreb = DataService.GetPorgrebokData_temp(jsonString);
                    CustomDialogFragment dialog = CustomDialogFragment.newInstance(pogreb.street_temp_max_byDate, "Максимальная температура ");
                    dialog.show(getSupportFragmentManager(),"Custom");
                } else if (test=="Min"){
                    pogreb = DataService.GetPorgrebokData_temp(jsonString);

                    CustomDialogFragment dialog = CustomDialogFragment.newInstance(pogreb.street_temp_max_byDate, "Минимальная температура ");
                    dialog.show(getSupportFragmentManager(),"Custom");

//                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//                    builder.setTitle("Минимальная температура");
//                    builder.setMessage("Минимальная температура на улице за месяц " + pogreb.street_temp_min_byDate + " °C");
//                    builder.setNeutralButton("За месяц", myClickAlert);
//                    //builder.setNegativeButton("За неделю", null);
//                    builder.setPositiveButton("За 24 часа", myClickList);
//                    //builder.setView(R.layout.dialog_popup);
//                    builder.show();
                } else if (test=="Refresh_data"){
                    pogreb = DataService.GetPorgrebokData(jsonString);

                    ConsumptionEnergy.setText(getResources().getString(R.string.EnergyConsumption) + "\n\n" + pogreb.kwt_full + " кВт");
                    CostEnergy.setText(getResources().getString(R.string.PriceEnergy) + "\n\n" + pogreb.price_kWt + " руб");
                    DateHange.setText(pogreb.date_hange);
                    Pressure.setText(getResources().getString(R.string.Pressure) + "\n\n" + pogreb.pressure);
                    TempHome.setText(getResources().getString(R.string.TempHome) + "\n\n" + pogreb.home_temp + " °C");
                    TempMax.setText(getResources().getString(R.string.TempMax) + "\n\n" + pogreb.street_temp_max + " °C");
                    TempMin.setText(getResources().getString(R.string.TempMin) + "\n\n" + pogreb.street_temp_min + " °C");
                    TempPogrebok.setText(getResources().getString(R.string.TempPogrebok) + "\n\n" + pogreb.cellar_temp + " °C");
                    TempStreet.setText(getResources().getString(R.string.TempStreet) + "\n\n" + pogreb.street_temp_current + " °C");
                    TimeWarm.setText(getResources().getString(R.string.AmountHours) +"\n\n" + pogreb.time_power);
                    On_Off.setBackgroundResource(R.drawable.power_on);
                    //pogreb.heating="0";
                    if("1".equals(pogreb.heating)) {
                        On_Off.setBackgroundResource(R.drawable.power_on);
                    }else if ("0".equals(pogreb.heating))
                        On_Off.setBackgroundResource(R.drawable.power_off_1);
                }

            }catch(IOException e){
                    e.printStackTrace();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Ошибка");
                builder.setMessage(e.getMessage());
                builder.setPositiveButton("Ok", null);
                builder.show();
                }catch(JSONException e){
                    e.printStackTrace();
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setTitle("Ошибка");
                builder.setMessage(e.getMessage());
                builder.setPositiveButton("Ok", null);
                builder.show();
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



            // Обновление данных погребка / гаража(рефреш)

//    public class refresh_data extends AsyncTask<String, Void, String> {
//
//    @Override
//        protected String doInBackground(String... string) {
//
//        final String errMessage;
//            try {
//                jsonString = getContent(string[0]);
//            } catch (IOException ex) {
//                errMessage = ex.getMessage();
//                MainActivity.this.runOnUiThread(new Runnable() {
//                    @Override
//                    public void run() {
//                        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
//                        builder.setTitle("Ошибка");
//                        builder.setMessage(errMessage);
//                        builder.setPositiveButton("Ok", null);
//                        builder.show();
//                    }
//                });
//            }
//            return jsonString;
//        }
//
//        @Override
//        protected void onPostExecute (String content) {
//            super.onPostExecute(content);
//
//            try {
//                pogreb = DataService.GetPorgrebokData(jsonString);
//
//                ConsumptionEnergy.setText(getResources().getString(R.string.EnergyConsumption) + "\n\n" + pogreb.kwt_full + " кВт");
//                CostEnergy.setText(getResources().getString(R.string.PriceEnergy) + "\n\n" + pogreb.price_kWt + " руб");
//                DateHange.setText(pogreb.date_hange);
//                Pressure.setText(getResources().getString(R.string.Pressure) + "\n\n" + pogreb.pressure);
//                TempHome.setText(getResources().getString(R.string.TempHome) + "\n\n" + pogreb.home_temp + " °C");
//                TempMax.setText(getResources().getString(R.string.TempMax) + "\n\n" + pogreb.street_temp_max + " °C");
//                TempMin.setText(getResources().getString(R.string.TempMin) + "\n\n" + pogreb.street_temp_min + " °C");
//                TempPogrebok.setText(getResources().getString(R.string.TempPogrebok) + "\n\n" + pogreb.cellar_temp + " °C");
//                TempStreet.setText(getResources().getString(R.string.TempStreet) + "\n\n" + pogreb.street_temp_current + " °C");
//                TimeWarm.setText(getResources().getString(R.string.AmountHours) +"\n\n" + pogreb.time_power);
//                On_Off.setBackgroundResource(R.drawable.power_on);
//                //pogreb.heating="0";
//                if("1".equals(pogreb.heating)) {
//                    On_Off.setBackgroundResource(R.drawable.power_on);
//                }else if ("0".equals(pogreb.heating))
//                    On_Off.setBackgroundResource(R.drawable.power_off_1);
//
//            }catch (IOException ex) {
//                ex.printStackTrace();
//            }catch (JSONException ex) {
//                ex.printStackTrace();
//            }
//        }
//
//        private String getContent(String path) throws IOException {
//
//            BufferedReader bufferedReader=null;
//
//            try {
//                URL url = new URL(path);
//                HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
//                urlConnection.setRequestMethod("GET");
//                urlConnection.connect();
//
//                InputStream inputStream = urlConnection.getInputStream();
//                InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
//                bufferedReader = new BufferedReader(inputStreamReader);
//                String line = bufferedReader.readLine();
//
//                return line;
//            }
//            finally {
//                if (bufferedReader !=null)
//                    bufferedReader.close();
//            }
//        }
//    }

}

