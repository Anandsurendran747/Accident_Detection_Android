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
import android.widget.ArrayAdapter;
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

public class ViewNews extends AppCompatActivity implements AdapterView.OnItemClickListener {
    ListView l1;
    SharedPreferences sh;
    public static final String SHARED_PREFS = "shared_prefs";
    ArrayList<String> news,link;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_news);
        sh = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        l1=findViewById(R.id.listView2);
        String url ="http://"+sh.getString("ip", "") + ":5000/viewNews";
        RequestQueue queue = Volley.newRequestQueue(ViewNews.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++",response);
                try {

                    JSONArray ar=new JSONArray(response);
                    news= new ArrayList<>();
                    link= new ArrayList<>();


                    for(int i=0;i<ar.length();i++)
                    {
                        JSONObject jo=ar.getJSONObject(i);
                        news.add(jo.getString("news"));
                        link.add(jo.getString("link"));




                    }

                     ArrayAdapter<String> ad=new ArrayAdapter<>(ViewNews.this,android.R.layout.simple_list_item_1,news);
                    l1.setAdapter(ad);

//                    l1.setAdapter(new costom4(ViewNews.this,name,place,description,number));
                    l1.setOnItemClickListener(ViewNews.this);

                } catch (Exception e) {
                    Log.d("=========", e.toString());
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(ViewNews.this, "err"+error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();



                return params;
            }
        };
        queue.add(stringRequest);
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent=new Intent(Intent.ACTION_VIEW);
        intent.setData(Uri.parse(link.get(position)));
        startActivity(intent);
    }
}