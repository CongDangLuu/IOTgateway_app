package llcd.com.iotgatewaylvtn;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonArrayRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import llcd.com.iotgatewaylvtn.GetDataVolley.GetDataDB;

public class HistoryDisplayActivity extends AppCompatActivity {

    ListView LVHistory;
    ArrayList<DataHistory> arrayData;
    HistoryAdapter adapter;

    private Spinner spnCategory;
    private List<String> list;

    String Timest, day, Pre_day ,set_day="";

    String URL;
    public static TextView DVName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        LVHistory = findViewById(R.id.LVData);


        DVName = findViewById(R.id.DVName);
        spnCategory = findViewById(R.id.spnCategoryHistory);

        URL = BtnActivity.GETDATA_URL;
        HistoryDisplayActivity.DVName.setText(GetDataDB.DVnamestr);

        combobox(URL);
    }

    private void Getdata(String url, String Set_day){
        arrayData = new ArrayList<>();
        adapter = new HistoryAdapter(this, R.layout.activity_history_view, arrayData);
        LVHistory.setAdapter(adapter);
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i =0; i< response.length() ; i++){
                            try {
                                JSONObject object = response.getJSONObject(i);
                                Timest = object.getString("Time");
                                String splits[] = Timest.split(" ");
                                day = splits[0];
                                if(day.equals(set_day)){
                                    arrayData.add(new DataHistory(
                                            splits[1],
                                            object.getString("ND"),
                                            object.getString("DA"),
                                            object.getString("AS"),
                                            object.getString("TT")
                                    ));
                                }


                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                        adapter.notifyDataSetChanged();
                       //Toast.makeText(HistoryDisplayActivity.this, String.valueOf(response), Toast.LENGTH_SHORT).show();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                    }
                }
        );
        requestQueue.add(jsonArrayRequest);
    }

    private void combobox(String url) {
        list = new ArrayList<>();

        // 1.Khởi tạo request
        RequestQueue queue = Volley.newRequestQueue(this);
        // 2.truyền đường dẫn vào request
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, url, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // chuyen mang thanh chuoi
                Pre_day=day="";

                for (int i = response.length() - 1; i >= 0; i--) {
                    try {
                        JSONObject person = response.getJSONObject(i);
                        Timest = person.getString("Time");
                        String Spl[] = Timest.split(" ");
                        day = Spl[0];
                        if(!(day.equals(Pre_day))){
                            list.add(day);
                            Pre_day=day;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                showCombobox(list);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
            }
        });
        queue.add(req);
    }

    private void showCombobox(List<String> list){
        ArrayAdapter<String> adapter = new ArrayAdapter(this, R.layout.spinner_list,list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spnCategory.setAdapter(adapter);
        spnCategory.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long l) {
                set_day = list.get(position);
                Getdata(URL, set_day);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                set_day= list.get(0);
                Getdata(URL, set_day);
            }
        });
    }

}
