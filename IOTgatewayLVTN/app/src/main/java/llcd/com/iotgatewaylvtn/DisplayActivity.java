package llcd.com.iotgatewaylvtn;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import llcd.com.iotgatewaylvtn.GetDataVolley.GetDataDB;

public class DisplayActivity extends AppCompatActivity {
    TextView NDtxt, DAtxt, AStxt, Prttxt, DVname, TTtxt;
    String Pro, Stt, MCU = "";

    String SetND, SetDA, SetAS, SetTT, SetName = "";
    String UrlDB = "";
    String url_control = Urls.Control_path;
    Button HistoryBtn, BtnOn, BtnOff, ChartBtn;
    public static CountDownTimer timerDisplay;
    public static int flattimer = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

        HistoryBtn = findViewById(R.id.History_Btn);
        ChartBtn = findViewById(R.id.Chart_Btn);

        NDtxt = findViewById(R.id.NDView1);
        DAtxt = findViewById(R.id.DAView1);
        AStxt = findViewById(R.id.ASView1);
        Prttxt = findViewById(R.id.PrtView);
        DVname = findViewById(R.id.DVName);
        TTtxt = findViewById(R.id.TTView1);

        BtnOn = findViewById(R.id.BtnOn);
        BtnOff = findViewById(R.id.BtnOff);

        Pro = BtnActivity.Pro;
        MCU = BtnActivity.MCU;
        Prttxt.setText(BtnActivity.Protxt);
        UrlDB = BtnActivity.GETDATA_URL;

       timerDisplay = new CountDownTimer(1000000000, 1000) {
           @Override
            public void onTick(long millisUntilFinished) {
               flattimer =1;
               GetDataDB data = new GetDataDB();
               data.getJSONArray(DisplayActivity.this, UrlDB);
               DVname.setText(GetDataDB.DVnamestr);
               NDtxt.setText(GetDataDB.NDstr);
               DAtxt.setText(GetDataDB.DAstr);
               AStxt.setText(GetDataDB.ASstr);
               TTtxt.setText(GetDataDB.TTstr);
            }

            @Override
            public void onFinish() {
                flattimer =0;
            }
        }.start();

        HistoryBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DisplayActivity.this, HistoryDisplayActivity.class);
                startActivity(intent);
            }
        });
        ChartBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(DisplayActivity.this, ChartActivity.class);
                startActivity(intent);
            }
        });

        BtnOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Stt = "1";
                BtnOn.setBackgroundColor(Color.GREEN);
                BtnOff.setBackgroundColor(Color.GRAY);
                ESPcontrol(Pro, Stt, MCU);
            }
        });
        BtnOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Stt = "0";
                BtnOn.setBackgroundColor(Color.GRAY);
                BtnOff.setBackgroundColor(Color.RED);
                ESPcontrol(Pro, Stt, MCU);
            }
        });

    }

    private void ESPcontrol(String Pro, String Stt, String MCU) {
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url_control, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                System.out.println(response.toString());
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        }) {
            @Nullable
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<>();
                params.put("Pro", Pro);
                params.put("Stt", Stt);
                params.put("MCU", MCU);

                return params;
            }
        };
        RequestQueue queue = Volley.newRequestQueue(DisplayActivity.this);
        queue.add(stringRequest);
    }

}




