package com.example.accident;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
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

public class MainActivity extends AppCompatActivity {
    EditText e1,e2;
    Button b1;
    TextView t;
    SharedPreferences sh;
    public static final String SHARED_PREFS = "shared_prefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        sh = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        e1=findViewById(R.id.editTextTextPersonName);
        e2=findViewById(R.id.editTextTextPersonName2);
        b1=findViewById(R.id.button);
        t=findViewById(R.id.register);
//
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final  String username=e1.getText().toString();
                final  String password=e2.getText().toString();
                if (username.equals("")){
                    e1.setError("Type Username");
                }if(password.equals("")){
                    e2.setError("Type Password");
                }



                RequestQueue queue = Volley.newRequestQueue(MainActivity.this);
             String   url = "http://" + sh.getString("ip","") + ":5000/login";
                Toast.makeText(MainActivity.this,  url+username+password ,Toast.LENGTH_SHORT).show();
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
                                String lid = json.getString("id");
                                String type = json.getString("type");
                                SharedPreferences.Editor edp = sh.edit();
                                edp.putString("lid", lid);
                                edp.putString("type",type);
                                edp.apply();
                                if(type.equalsIgnoreCase("user")) {
                                    Intent in = new Intent(getApplicationContext(), LocationService.class);
                                    startService(in);
                                    Intent in1 = new Intent(getApplicationContext(), LocationServiceno.class);
                                    startService(in1);
                                    Intent ik = new Intent(getApplicationContext(), Home.class);
                                    ik.putExtra("date","na");
                                    startActivity(ik);
                                }else if(type.equalsIgnoreCase("emergency")){
                                    Intent ik = new Intent(getApplicationContext(), EmergencyHome.class);
                                    ik.putExtra("date","na");
                                    startActivity(ik);
                                    Intent in = new Intent(getApplicationContext(), LocationService1.class);
                                    startService(in);
                                    Intent in1 = new Intent(getApplicationContext(), LocationServiceno.class);
                                    startService(in1);
                                }
                                else {

                                    Toast.makeText(MainActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();

                                }

                            } else {

                                Toast.makeText(MainActivity.this, "Invalid username or password", Toast.LENGTH_SHORT).show();

                            }
                        } catch (JSONException e) {
                            Toast.makeText(MainActivity.this, String.valueOf(e), Toast.LENGTH_SHORT).show();
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
                        params.put("username", username);
                        params.put("password", password);

                        return params;
                    }
                };
                queue.add(stringRequest);




            }
        });
        t.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,Registration.class);
                startActivity(intent);
            }
        });
    }
    @Override
    public void onBackPressed() {
        Intent ik = new Intent(getApplicationContext(), SetIp.class);
        startActivity(ik);
        super.onBackPressed();
    }
}