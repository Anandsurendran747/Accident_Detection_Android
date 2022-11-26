package com.example.accident;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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

public class ViewProfile extends AppCompatActivity {
    SharedPreferences sh;
    EditText e1,e2,e3,e4,e5,e6;
    Button b1;
    public static final String SHARED_PREFS = "shared_prefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_profile);
        sh = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);


            e1=findViewById(R.id.editTextTextPersonName4);
            e2=findViewById(R.id.editTextTextPersonName5);
            e3=findViewById(R.id.editTextTextPersonName6);
            e4=findViewById(R.id.editTextTextPersonName7);
            e5=findViewById(R.id.editTextTextPersonName8);
            e6=findViewById(R.id.editTextTextPersonName9);

            b1=findViewById(R.id.button5);
        String url ="http://"+sh.getString("ip", "") + ":5000/viewProfile";
        RequestQueue queue = Volley.newRequestQueue(ViewProfile.this);

        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++",response);
                try {
                    JSONArray ar=new JSONArray(response);
                    for(int i=0;i<ar.length();i++)
                    {
                        JSONObject jo=ar.getJSONObject(i);
                        e1.setText(jo.getString("FNAME"));
                        e2.setText(jo.getString("LNAME"));
                        e3.setText(jo.getString("PLACE"));
                        e4.setText(jo.getString("POST"));
                        e5.setText(jo.getString("PIN"));
                        e6.setText(jo.getString("PHONE"));




                    }


                } catch (Exception e) {
                    Log.d("=========", e.toString());
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(ViewProfile.this, "err"+error, Toast.LENGTH_SHORT).show();
            }
        }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<>();
                params.put("id", sh.getString("lid",""));



                return params;
            }
        };
        queue.add(stringRequest);

       b1.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
               final  String fname=e1.getText().toString();
               final  String lname=e2.getText().toString();
               final  String place=e3.getText().toString();
               final  String post=e4.getText().toString();
               final  String pin=e5.getText().toString();
               final  String phone=e6.getText().toString();
               String   url = "http://" + sh.getString("ip","") + ":5000/editProfile";

               // Request a string response from the provided URL.
               StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>(){
                   @Override
                   public void onResponse(String response) {
                       // Display the response string.
                       Log.d("+++++++++++++++++", response);
                       try {
                           JSONObject json = new JSONObject(response);
                           String res = json.getString("task");

                           if (res.equalsIgnoreCase("succes")) {

                               Intent ik = new Intent(getApplicationContext(), Home.class);
                               startActivity(ik);

                           } else {

                               Toast.makeText(ViewProfile.this, "Invalid username or password", Toast.LENGTH_SHORT).show();

                           }
                       } catch (JSONException e) {
                           e.printStackTrace();
                       }


                   }
               }, new Response.ErrorListener() {
                   @Override
                   public void onErrorResponse(VolleyError error) {


                       Toast.makeText(getApplicationContext(), "Error" + error, Toast.LENGTH_LONG).show();
                   }
               }) {
                   @Override
                   protected Map<String, String> getParams() {
                       Map<String, String> params = new HashMap<String, String>();
                       params.put("id",sh.getString("lid",""));
                       params.put("fname", fname);
                       params.put("lname", lname);
                       params.put("place", place);
                       params.put("post", post);
                       params.put("pin", pin);
                       params.put("phone", phone);


                       return params;
                   }
               };
               queue.add(stringRequest);
           }
       });
    }
}