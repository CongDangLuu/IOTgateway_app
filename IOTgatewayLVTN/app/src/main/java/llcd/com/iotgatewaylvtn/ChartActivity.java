package llcd.com.iotgatewaylvtn;


import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;
import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.Description;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ChartActivity extends AppCompatActivity {
    Button NDChart, DAChart, ASChart;
    LineChart chart;
    Float ValOfChart;
    String URL;
    String Lable = "Nhiệt độ";
    String typeData = "ND";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chart);
        chart = findViewById(R.id.NhietdoChart);
        NDChart = findViewById(R.id.BtnNDChart);
        ASChart = findViewById(R.id.BtnASChart);
        DAChart = findViewById(R.id.BtnDAChart);

        URL = BtnActivity.GETDATA_URL;
        new CountDownTimer(1000000000, 1000) {

            @Override
            public void onTick(long millisUntilFinished) {
                GetChart(URL, chart, Lable, typeData);

            }

            @Override
            public void onFinish() {

            }
        }.start();

        NDChart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Lable = "Nhiệt độ";
                typeData = "ND";
            }
        });

        DAChart.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                Lable = "Độ ẩm";
                typeData = "DA";
            }
        });

        ASChart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Lable = "Ánh sáng";
                typeData = "AS";
            }
        });

    }
//set data--------------------------------

    private void GetChart(String url, LineChart lineChart, String lable, String type) {
        RequestQueue queue = Volley.newRequestQueue(this);
        // 2.truyền đường dẫn vào request
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {

                lineChart.setDragEnabled(true);
                lineChart.setScaleEnabled(true);

                ArrayList<Entry> dataSet = new ArrayList<Entry>();
                for (int i = response.length() - 10; i < response.length(); i++) {
                    try {
                        JSONObject person = response.getJSONObject(i);
                        ValOfChart = Float.parseFloat(person.getString(type));
                        String Timest = person.getString("Time");
                        dataSet.add(new Entry(i, ValOfChart));
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }

                LineDataSet lineDataSet = new LineDataSet(dataSet, lable);
                ArrayList<ILineDataSet> iLineDataSets = new ArrayList<>();
                iLineDataSets.add(lineDataSet);

                LineData lineData = new LineData(iLineDataSets);
                lineChart.setData(lineData);
                lineChart.invalidate();

                //if you want set background color use below method
                //lineChart.setBackgroundColor(Color.RED);

                // set text if data are are not available
                lineChart.setNoDataText("Data not Available");

                //you can modify your line chart graph according to your requirement there are lots of method available in this library
                //now customize line chart
                switch (type) {
                    case "ND":
                        lineDataSet.setColor(Color.RED);
                        break;
                    case "DA":
                        lineDataSet.setColor(Color.BLUE);
                        break;
                    case "AS":
                        lineDataSet.setColor(Color.YELLOW);
                        break;
                    default:
                        lineDataSet.setColor(Color.RED);
                        break;
                }


                lineDataSet.setDrawCircles(false);

                lineDataSet.setLineWidth(3);
                //lineDataSet.setCircleRadius(10);
                //lineDataSet.setCircleHoleRadius(10);
                lineDataSet.setValueTextSize(13);
                lineDataSet.setValueTextColor(Color.BLACK);

                Legend legend = lineChart.getLegend();
                legend.setTextSize(15);
                legend.setForm(Legend.LegendForm.LINE);
                legend.setXEntrySpace(15);
                legend.setFormSize(17);
                legend.setFormToTextSpace(5);

                Description descrip = new Description();
                descrip.setText(" ");
                descrip.setTextColor(Color.BLACK);
                descrip.setTextSize(16);
                lineChart.setDescription(descrip);


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        //4. add
        queue.add(req);
    }
}


