package com.example.accident;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewAccidentHistory extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView l1;
    SharedPreferences sh;
    ArrayList<String> aid,date,latitude,longitude,status,time;
    public static final String SHARED_PREFS = "shared_prefs";
    String pos;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_accident_history);
        sh = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        l1=findViewById(R.id.listView5);
        String url ="http://"+sh.getString("ip", "") + ":5000/viewEmergency";
        RequestQueue queue = Volley.newRequestQueue(ViewAccidentHistory.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++",response);
                try {

                    JSONArray ar=new JSONArray(response);
                    date= new ArrayList<>();
                    latitude= new ArrayList<>();
                    longitude= new ArrayList<>();
                    status=new ArrayList<>();
                    aid=new ArrayList<>();
                    time=new ArrayList<>();

                    for(int i=0;i<ar.length();i++)
                    {
                        JSONObject jo=ar.getJSONObject(i);
                        aid.add(jo.getString("AID"));
                        latitude.add(jo.getString("LATITUDE"));
                        longitude.add(jo.getString("LONGITUDE"));
                        status.add(jo.getString("status"));
                        date.add(jo.getString("DATE"));
                        time.add(jo.getString("time"));



                    }

                    // ArrayAdapter<String> ad=new ArrayAdapter<>(Home.this,android.R.layout.simple_list_item_1,name);
                    //lv.setAdapter(ad);

                    l1.setAdapter(new costom2(ViewAccidentHistory.this,date,time,status));
                    l1.setOnItemClickListener(ViewAccidentHistory.this);

                } catch (Exception e) {
                    Log.d("=========", e.toString());
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(ViewAccidentHistory.this, "err"+error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("lati", LocationServiceno.lati);
                params.put("longi",LocationServiceno.logi );


                return params;
            }
        };
        queue.add(stringRequest);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {


//        pos=position;


        AlertDialog.Builder ald=new AlertDialog.Builder(ViewAccidentHistory.this);
        ald.setTitle("File")
                .setPositiveButton(" Track ", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent=new Intent(Intent.ACTION_VIEW);
                        intent.setData(Uri.parse("http://maps.google.com?q="+latitude.get(position)+","+longitude.get(position)));
                        startActivity(intent);
                    }
                })
                .setNegativeButton("Update Status ", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {

//
                        Intent intent=new Intent(ViewAccidentHistory.this,UpdateStatus.class);
                        intent.putExtra("aid",aid.get(position));
                        startActivity(intent);

                    }
                });

        AlertDialog al=ald.create();
        al.show();
    }
}