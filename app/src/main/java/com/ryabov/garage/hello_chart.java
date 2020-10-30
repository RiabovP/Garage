package com.ryabov.garage;

import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.ryabov.garage.Pogreb.DataService;
import com.ryabov.garage.Pogreb.Pogreb_graph;

import org.json.JSONException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import lecho.lib.hellocharts.gesture.ZoomType;
import lecho.lib.hellocharts.listener.ColumnChartOnValueSelectListener;
import lecho.lib.hellocharts.model.Axis;
import lecho.lib.hellocharts.model.AxisValue;
import lecho.lib.hellocharts.model.Column;
import lecho.lib.hellocharts.model.ColumnChartData;
import lecho.lib.hellocharts.model.Line;
import lecho.lib.hellocharts.model.LineChartData;
import lecho.lib.hellocharts.model.PointValue;
import lecho.lib.hellocharts.model.SelectedValue;
import lecho.lib.hellocharts.model.SubcolumnValue;
import lecho.lib.hellocharts.model.Viewport;
import lecho.lib.hellocharts.util.ChartUtils;
import lecho.lib.hellocharts.view.ColumnChartView;
import lecho.lib.hellocharts.view.LineChartView;


public class hello_chart extends AppCompatActivity {

    private static String jsonString;
    private static Pogreb_graph pogreb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_hello_chart);

        new progress_Task3().execute("http://37.193.0.199:1010/temp_street.php?temp_street&date=2020.02.10");

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().add(R.id.container, new PlaceholderFragment()).commit();
        }

    }
    public static class PlaceholderFragment extends Fragment {
        public final static String[] months = new String[]{"Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug",
                "Sep", "Oct", "Nov", "Dec",};

        public final static String[] days = new String[]{"Mon", "Tue", "Wen", "Thu", "Fri", "Sat", "Sun",};

        private static LineChartView chartTop;
        private ColumnChartView chartBottom;

        private static LineChartData lineData;
        private ColumnChartData columnData;

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_line_column_dependency, container, false);

                        // *** TOP LINE CHART ***
            chartTop = (LineChartView) rootView.findViewById(R.id.chart_top);

            // Generate and set data for line chart
            generateInitialLineData();

            // *** BOTTOM COLUMN CHART ***

            chartBottom = (ColumnChartView) rootView.findViewById(R.id.chart_bottom);

            generateColumnData();

            return rootView;
        }

        private void generateColumnData() {

            int numSubcolumns = 1;
            int numColumns = months.length;

            List<AxisValue> axisValues = new ArrayList<AxisValue>();
            List<Column> columns = new ArrayList<Column>();
            List<SubcolumnValue> values;
            for (int i = 0; i < numColumns; ++i) {

                values = new ArrayList<SubcolumnValue>();
                for (int j = 0; j < numSubcolumns; ++j) {
                    values.add(new SubcolumnValue((float) Math.random() * 50f + 5, ChartUtils.pickColor()));
                }

                axisValues.add(new AxisValue(i).setLabel(months[i]));

                columns.add(new Column(values).setHasLabelsOnlyForSelected(true));
            }

            columnData = new ColumnChartData(columns);

            columnData.setAxisXBottom(new Axis(axisValues).setHasLines(true));
            columnData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(2));

            chartBottom.setColumnChartData(columnData);

            // Set value touch listener that will trigger changes for chartTop.
            chartBottom.setOnValueTouchListener(new ValueTouchListener());

            // Set selection mode to keep selected month column highlighted.
            chartBottom.setValueSelectionEnabled(true);

            chartBottom.setZoomType(ZoomType.HORIZONTAL);

             chartBottom.setOnClickListener(new View.OnClickListener() {

             @Override
             public void onClick(View v) {
             SelectedValue sv = chartBottom.getSelectedValue();
             if (!sv.isSet()) {
             generateInitialLineData();
             }

             }
             });

        }

        /**
         * Generates initial data for line chart. At the begining all Y values are equals 0. That will change when user
         * will select value on column chart.
         */
        private void generateInitialLineData() {
            int numValues = 72;
            int sr= 0;


            List<AxisValue> axisValues = new ArrayList<AxisValue>();
            List<PointValue> values = new ArrayList<PointValue>();
            for (int i = 0; i < numValues; ++i) {
                //String dateStr=pogreb.date_temp[i];
                values.add(new PointValue(i, 0));
                axisValues.add(new AxisValue(i).setLabel((String.valueOf(sr++))));
            }

            Line line = new Line(values);
            line.setColor(ChartUtils.COLOR_GREEN).setCubic(true);

            List<Line> lines = new ArrayList<Line>();
            lines.add(line);

            lineData = new LineChartData(lines);
            lineData.setAxisXBottom(new Axis(axisValues).setHasLines(true));
            lineData.setAxisYLeft(new Axis().setHasLines(true).setMaxLabelChars(3));

            chartTop.setLineChartData(lineData);

            // For build-up animation you have to disable viewport recalculation.
            chartTop.setViewportCalculationEnabled(false);

            // And set initial max viewport and current viewport- remember to set viewports after data.
            Viewport v = new Viewport(0, 10, 73, -20);
            chartTop.setMaximumViewport(v);
            chartTop.setCurrentViewport(v);

            chartTop.setZoomType(ZoomType.HORIZONTAL);
        }

        private void generateLineData(int color, float range) {
            // Cancel last animation if not finished.
            chartTop.cancelDataAnimation();
//---
            Line line = lineData.getLines().get(0);// For this example there is always only one line.
            line.setColor(color);
            for (PointValue value : line.getValues()) {
                // Change target only for Y value.
                //String dateStr=pogreb.date_temp[(int) value.getX()];
                value.setTarget(value.getX(), (float) Double.parseDouble(pogreb.temp_streetByDate[(int)value.getX()]));
            }

            //-----
            // Modify data targets
//            Line line = lineData.getLines().get(0);// For this example there is always only one line.
//            line.setColor(color);
//            for (PointValue value : line.getValues()) {
//                // Change target only for Y value.
//                value.setTarget(value.getX(), (float) Math.random() * range);
//            }

            // Start new data animation with 300ms duration;
            chartTop.startDataAnimation(500);
        }

        private class ValueTouchListener implements ColumnChartOnValueSelectListener {

            @Override
            public void onValueSelected(int columnIndex, int subcolumnIndex, SubcolumnValue value) {

                //new progress_Task3().execute("http://37.193.0.199:1010/temp_street.php?temp_street&date=2020.02.1"+String.valueOf(columnIndex));
                int p=Integer.parseInt(pogreb.Request_json);
                generateLineData(value.getColor(), p);


            }

            @Override
            public void onValueDeselected() {

                generateLineData(ChartUtils.COLOR_GREEN, 0);

            }
        }


    }

    public static class progress_Task3 extends AsyncTask<String, Void, String> {



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


                // Cancel last animation if not finished.
                //chartTop.cancelDataAnimation();

//            // Modify data targets
//            Line line = lineData.getLines().get(0);// For this example there is always only one line.
//            line.setColor(12);
//            for (PointValue value : line.getValues()) {
//                // Change target only for Y value.
//                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");
//
//                String dateStr=pogreb.date_temp[(int) value.getX()];
//                value.setTarget(value.getX(), (float) Double.parseDouble(pogreb.temp_streetByDate[(int)value.getX()])+20);
//            }
//
//            // Start new data animation with 300ms duration;
//            chartTop.startDataAnimation(300);



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