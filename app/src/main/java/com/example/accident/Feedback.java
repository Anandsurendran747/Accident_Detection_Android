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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Feedback extends AppCompatActivity {
    EditText e1;
    TextView b1;
    SharedPreferences sh;
    public static final String SHARED_PREFS = "shared_prefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feedback);
        sh = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        e1=findViewById(R.id.feedback);

        b1=findViewById(R.id.button);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final  String feedback=e1.getText().toString();
                if (feedback.equals("")){
                    e1.setError("Type Feedback");
                }else {
                    RequestQueue queue = Volley.newRequestQueue(Feedback.this);
                    String url = "http://" + sh.getString("ip", "") + ":5000/sendFeedback";

                    // Request a string response from the provided URL.
                    StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {
                            // Display the response string.
                            Log.d("+++++++++++++++++", response);
                            try {
                                JSONObject json = new JSONObject(response);
                                String res = json.getString("task");

                                if (res.equalsIgnoreCase("succes")) {

                                    Intent ik = new Intent(getApplicationContext(), Home.class);
                                    ik.putExtra("date", "na");
                                    startActivity(ik);

                                } else {

                                    Toast.makeText(Feedback.this, "error", Toast.LENGTH_SHORT).show();

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
                            params.put("feedback", feedback);
                            params.put("id", sh.getString("lid", ""));

                            return params;
                        }
                    };
                    queue.add(stringRequest);
                }
            }
        });
    }
}