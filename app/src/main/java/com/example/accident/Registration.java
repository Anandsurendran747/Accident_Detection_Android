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
import android.widget.TableLayout;
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

public class Registration extends AppCompatActivity {
    EditText e1,e2,e3,e4,e5,e6,e7,e8;
    Button b1;
    SharedPreferences sh;
    public static final String SHARED_PREFS = "shared_prefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
        sh = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        e1=findViewById(R.id.fname);
        e2=findViewById(R.id.lname);
        e3=findViewById(R.id.place);
        e4=findViewById(R.id.post);
        e5=findViewById(R.id.pin);
        e6=findViewById(R.id.phone);
        e7=findViewById(R.id.username);
        e8=findViewById(R.id.password);
        b1=findViewById(R.id.button);
        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final  String fname=e1.getText().toString();
                final  String lname=e2.getText().toString();
                final  String place=e3.getText().toString();
                final  String post=e4.getText().toString();
                final  String pin=e5.getText().toString();
                final  String phone=e6.getText().toString();
                final  String username=e7.getText().toString();
                final  String password=e8.getText().toString();
                RequestQueue queue = Volley.newRequestQueue(Registration.this);

                String   url = "http://" + sh.getString("ip","") + ":5000/registration";

                // Request a string response from the provided URL.
                if (fname.equals("")){
                    e1.setError("Type first name");
                }if(lname.equals("")){
                    e2.setError("Type last name");
                }if(place.equals("")){
                    e3.setError("Type place");
                }if(post.equals("")){
                    e4.setError("Type post");
                }if(pin.equals("")){
                    e5.setError("Type pin");
                }if(phone.equals("")){
                    e6.setError("Type phone number");
                }if(username.equals("")){
                    e7.setError("Type username");
                }if(password.equals("")){
                    e8.setError("Type password");
                }
                else{
                StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>(){
                    @Override
                    public void onResponse(String response) {
                        // Display the response string.
                        Log.d("+++++++++++++++++", response);
                        try {
                            JSONObject json = new JSONObject(response);
                            String res = json.getString("task");

                            if (res.equalsIgnoreCase("succes")) {

                                Intent ik = new Intent(getApplicationContext(), MainActivity.class);
                                startActivity(ik);

                            } else {

                                Toast.makeText(Registration.this, "Invalid username or password", Toast.LENGTH_SHORT).show();

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
                        params.put("fname", fname);
                        params.put("lname", lname);
                        params.put("place", place);
                        params.put("post", post);
                        params.put("pin", pin);
                        params.put("phone", phone);
                        params.put("username", username);
                        params.put("password", password);

                        return params;
                    }
                };
                queue.add(stringRequest);
                }}
        });
    }
}