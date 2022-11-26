package com.example.accident;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
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

import java.util.HashMap;
import java.util.Map;

public class ViewUserAccount extends AppCompatActivity {
    TextView e1,e2,e3,e4,e5,e6;
    EditText t1,t2,t3,t4,t5,t6,t7;
    Button b1;
    ImageView i,i1;
    LinearLayout l;
    SharedPreferences sh;
    public static final String SHARED_PREFS = "shared_prefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_user_account);
        sh = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        e1=findViewById(R.id.name);
        l=findViewById(R.id.editsub);
        i=findViewById(R.id.imageView);
        i1=findViewById(R.id.imageView3);
        e2=findViewById(R.id.place);
        e3=findViewById(R.id.post);
        e4=findViewById(R.id.pin);
        e5=findViewById(R.id.phone);
        e6=findViewById(R.id.account);
        t1=findViewById(R.id.editfname);
        t2=findViewById(R.id.editlname);
        t3=findViewById(R.id.editplace);
        t4=findViewById(R.id.editpost);
        t5=findViewById(R.id.editpin);
        t6=findViewById(R.id.editnumber);
        String url ="http://"+sh.getString("ip", "") + ":5000/viewProfile";
        RequestQueue queue = Volley.newRequestQueue(ViewUserAccount.this);

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
                        e1.setText(jo.getString("FNAME")+" "+jo.getString("LNAME"));
                        e6.setText(jo.getString("FNAME")+" "+jo.getString("LNAME"));
                        e2.setText(jo.getString("PLACE"));
                        e3.setText(jo.getString("POST"));
                        e4.setText(jo.getString("PIN"));
                        e5.setText(jo.getString("PHONE"));
                        t1.setText(jo.getString("FNAME"));
                        t2.setText(jo.getString("LNAME"));
                        t3.setText(jo.getString("PLACE"));
                        t4.setText(jo.getString("POST"));
                        t5.setText(jo.getString("PIN"));
                        t6.setText(jo.getString("PHONE"));




                    }


                } catch (Exception e) {
                    Log.d("=========", e.toString());
                }


            }

        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {

                Toast.makeText(ViewUserAccount.this, "err"+error, Toast.LENGTH_SHORT).show();
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

    }
    public void editProfile(View view){
        l.setVisibility(View.VISIBLE);
        i.setVisibility(View.GONE);
        i1.setVisibility(View.VISIBLE);
        e1.setVisibility(View.GONE);
        t1.setVisibility(View.VISIBLE);
        t2.setVisibility(View.VISIBLE);
        e2.setVisibility(View.GONE);
        t3.setVisibility(View.VISIBLE);
        e3.setVisibility(View.GONE);
        t4.setVisibility(View.VISIBLE);
        e4.setVisibility(View.GONE);
        t5.setVisibility(View.VISIBLE);
        e5.setVisibility(View.GONE);
        t6.setVisibility(View.VISIBLE);

        Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
    }
    public void editRequest(View view){
        final  String fname=t1.getText().toString();
        final  String lname=t2.getText().toString();
        final  String place=t3.getText().toString();
        final  String post=t4.getText().toString();
        final  String pin=t5.getText().toString();
        final  String phone=t6.getText().toString();
        String   url = "http://" + sh.getString("ip","") + ":5000/editProfile";
        RequestQueue queue = Volley.newRequestQueue(ViewUserAccount.this);
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
                        ik.putExtra("date","na");
                        startActivity(ik);

                    } else {

                        Toast.makeText(ViewUserAccount.this, "Error", Toast.LENGTH_SHORT).show();

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
    public void reset(View view){
        Intent i=new Intent(this,ViewUserAccount.class);
        startActivity(i);
    }
    public void back(View view){
        Intent ik = new Intent(getApplicationContext(), Home.class);
        ik.putExtra("date","na");
        startActivity(ik);
    }

    @Override
    public void onBackPressed() {
        Intent ik = new Intent(getApplicationContext(), Home.class);
        ik.putExtra("date","na");
        startActivity(ik);
        super.onBackPressed();
    }
}