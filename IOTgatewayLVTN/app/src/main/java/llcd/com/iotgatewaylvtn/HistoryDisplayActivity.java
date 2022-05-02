package llcd.com.iotgatewaylvtn;

import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.ListView;
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

import llcd.com.iotgatewaylvtn.GetDataVolley.GetDataDB;

public class HistoryDisplayActivity extends AppCompatActivity {

    ListView LVHistory;
    ArrayList<DataHistory> arrayData;
    HistoryAdapter adapter;
    public static TextView DVName;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history);
        LVHistory = findViewById(R.id.LVData);
        arrayData = new ArrayList<>();
        adapter = new HistoryAdapter(this, R.layout.activity_history_view, arrayData);
        LVHistory.setAdapter(adapter);

        DVName = findViewById(R.id.DVName);
        HistoryDisplayActivity.DVName.setText(GetDataDB.DVnamestr);

        Getdata(BtnActivity.GETDATA_URL);


    }

    private void Getdata(String url){
        RequestQueue requestQueue = Volley.newRequestQueue(this);
        JsonArrayRequest jsonArrayRequest = new JsonArrayRequest(Request.Method.GET, url, null,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        for (int i = response.length() ; i >= 0 ; i--){
                            try {

                                JSONObject object = response.getJSONObject(i);

                                arrayData.add(new DataHistory(
                                        object.getString("Time"),
                                        object.getString("ND"),
                                        object.getString("DA"),
                                        object.getString("AS"),
                                        object.getString("TT")
                                ));
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
}
