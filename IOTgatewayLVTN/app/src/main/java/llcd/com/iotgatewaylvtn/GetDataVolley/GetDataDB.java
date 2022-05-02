package llcd.com.iotgatewaylvtn.GetDataVolley;

import android.content.Context;
import android.os.Bundle;
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

import llcd.com.iotgatewaylvtn.DisplayActivity;
import llcd.com.iotgatewaylvtn.R;

public class GetDataDB extends AppCompatActivity {
    public static String DVnamestr, TTstr, NDstr, DAstr, ASstr= "";


    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display);

    }
    public void getJSONArray(Context context, String GETDATA_URL) {


        // 1.Khởi tạo request
        RequestQueue queue = Volley.newRequestQueue(context);
        // 2.truyền đường dẫn vào request
        JsonArrayRequest req = new JsonArrayRequest(Request.Method.GET, GETDATA_URL, null, new Response.Listener<JSONArray>() {
            @Override
            public void onResponse(JSONArray response) {
                // chuyen mang thanh chuoi

                DVnamestr = TTstr = NDstr = DAstr = ASstr = "";
                for (int i = response.length()-1; i>=0; i--) {
                    try {
                        JSONObject person = response.getJSONObject(i);

                        String DVname = person.getString("Name");
                        String TT = person.getString("TT");
                        String ND = person.getString("ND");
                        String DA = person.getString("DA");
                        String AS = person.getString("AS");

                        if( (TT.isEmpty()) || (ND.isEmpty()) || (DA.isEmpty()) || (AS.isEmpty()) ){
                            continue;
                        }else{
                            DVnamestr = DVname ;
                            NDstr = ND +" *C";
                            DAstr = DA +"  %";
                            ASstr = AS + " lx";
                            TTstr = TT;
                            break;
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }


            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

            }
        });
        queue.add(req);
    }


}