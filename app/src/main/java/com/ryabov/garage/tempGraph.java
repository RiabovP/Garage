package com.ryabov.garage;

import android.os.AsyncTask;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.LegendRenderer;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.ryabov.garage.Pogreb.DataService;
import com.ryabov.garage.Pogreb.Pogreb_graph;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class tempGraph extends AppCompatActivity {

    Pogreb_graph pogreb;
    String jsonString;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temp_graph);

        new progress_Task2().execute("http://37.193.0.199:1010/temp_street.php?temp_street&date=2020.02.10");
    }


    public class progress_Task2 extends AsyncTask<String, Void, String> {

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
                pogreb = DataService.GetPogrebData_Graph_temp(jsonString);
                int p=Integer.parseInt(pogreb.Request_json);

                GraphView graphView = (GraphView) findViewById(R.id.graph);

                DataPoint[] points = new DataPoint[p];
                Date date[]=new Date[p];
                Double X, Y;

                for ( int i = 0; i < p; i++ ) //не выводит данные в график
                {

                    //points[i]=new DataPoint(2,3);
                    //points[i++]=new DataPoint(2,3);

                   SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

                   String dateStr=pogreb.date_temp[i];

                   String substr1 = dateStr.substring(11);

                   date[i] = format.parse(substr1);


                   // Парсинг текстовой температуры в double

                    Y=Double.parseDouble(pogreb.temp_streetByDate[i]);

                    points[i]=new DataPoint(date[i],Y);
                }


                LineGraphSeries<DataPoint> series = new LineGraphSeries<>(points);

                //активация лейблов как даты
                graphView.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(tempGraph.this));

                // Дефолтное положение горизонтальной оси Х в момент открытия
                graphView.getViewport().setXAxisBoundsManual(true);
                graphView.getViewport().setMinX(date[0].getTime());


                // активация горизонтального масштабирования и прокрутки https://github.com/jjoe64/GraphView/wiki/Basics-of-GraphView
                graphView.getViewport().setScalable(true);

                graphView.addSeries(series);

                series.setTitle("Максимальная температура за месяц");
                series.setDrawDataPoints(true);
                graphView.getLegendRenderer().setVisible(true);
                graphView.getLegendRenderer().setAlign(LegendRenderer.LegendAlign.TOP);



            } catch (IOException ex) {
                ex.printStackTrace();
            } catch (JSONException ex) {
                ex.printStackTrace();
            } catch (ParseException e) {
                e.printStackTrace();
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
