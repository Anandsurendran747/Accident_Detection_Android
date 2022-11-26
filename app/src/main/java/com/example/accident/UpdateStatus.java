package com.example.accident;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UpdateStatus extends AppCompatActivity{
    Spinner s;
    Button b;
    ArrayList<String> hid,name,place,description,number;
    Object val=null;
    SharedPreferences sh;
    public static final String SHARED_PREFS = "shared_prefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_status);
        s=findViewById(R.id.spinner);
        b=findViewById(R.id.button15);
        sh = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        Intent intent=getIntent();
        String aid=intent.getStringExtra("aid");
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.accident_status, android.R.layout.simple_spinner_item);
// Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
// Apply the adapter to the spinner
        s.setAdapter(adapter);
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String text = s.getSelectedItem().toString();
                Toast.makeText(UpdateStatus.this,text,Toast.LENGTH_SHORT).show();
                RequestQueue queue = Volley.newRequestQueue(UpdateStatus.this);
                        String   url = "http://" + sh.getString("ip","") + ":5000/update_stat";

                        // Request a string response from the provided URL.
                        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>(){
                            @Override
                            public void onResponse(String response) {
                                // Display the response string.
                                Log.d("+++++++++++++++++", response);
                                try {
                                    JSONObject json = new JSONObject(response);
                                    String res = json.getString("task");

                                    if (res.equalsIgnoreCase("valid")) {
                                        Toast.makeText(UpdateStatus.this, "updated", Toast.LENGTH_SHORT).show();
                                        Intent ik = new Intent(getApplicationContext(), EmergencyHome.class);
                                        ik.putExtra("date","na");
                                        startActivity(ik);


                                    } else {

                                        Toast.makeText(UpdateStatus.this, "failed", Toast.LENGTH_SHORT).show();
                                        Intent ik = new Intent(getApplicationContext(), EmergencyHome.class);
                                        ik.putExtra("date","na");
                                        startActivity(ik);
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
                                params.put("aid",aid);
                                params.put("status",text);

                                return params;
                            }
                        };
                        queue.add(stringRequest);
            }
        });

    }


}