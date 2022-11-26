package com.example.accident;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class ViewHospital extends AppCompatActivity {
    ListView l1;
    SharedPreferences sh;
    ArrayList<String> hid,name,place,description,number,latitude,longitude;
    public static final String SHARED_PREFS = "shared_prefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_hospital);
        sh = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        l1=findViewById(R.id.listView);
        String url ="http://"+sh.getString("ip", "") + ":5000/viewHospitals";
        RequestQueue queue = Volley.newRequestQueue(ViewHospital.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++",response);
                try {

                    JSONArray ar=new JSONArray(response);
                    hid= new ArrayList<>();
                    name= new ArrayList<>();
                    place= new ArrayList<>();
                    description=new ArrayList<>();
                    number=new ArrayList<>();
                    latitude=new ArrayList<>();
                    longitude=new ArrayList<>();


                    for(int i=0;i<ar.length();i++)
                    {
                        JSONObject jo=ar.getJSONObject(i);
                        hid.add(jo.getString("HID"));
                        name.add(jo.getString("NAME"));
                        place.add(jo.getString("PLACE"));
                        description.add(jo.getString("DISCRIPTION"));
                        number.add(jo.getString("CONTACT NUMBER"));
                        latitude.add(jo.getString("LATITUDE"));
                        longitude.add(jo.getString("LONGITUDE"));


                    }

                    // ArrayAdapter<String> ad=new ArrayAdapter<>(Home.this,android.R.layout.simple_list_item_1,name);
                    //lv.setAdapter(ad);

                    l1.setAdapter(new costom4(ViewHospital.this,name,place,description,number));
                    l1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
//                            Toast.makeText(ViewHospital.this, String.valueOf(latitude.get(position)), Toast.LENGTH_SHORT).show();
                            Intent intent=new Intent(Intent.ACTION_VIEW);
                            intent.setData(Uri.parse("http://maps.google.com?q="+latitude.get(position)+","+longitude.get(position)));
                            startActivity(intent);
                        }
                    });

                } catch (Exception e) {
                    Log.d("=========", e.toString());
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(ViewHospital.this, "err"+error, Toast.LENGTH_SHORT).show();
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
}