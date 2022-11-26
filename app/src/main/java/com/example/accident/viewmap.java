package com.example.accident;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.PopupMenu;
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

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;


import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

//
//import static com.example.accident.LocationService.latitude;
//import static com.example.accident.LocationService.longitude;

public class viewmap extends AppCompatActivity implements OnMapReadyCallback {
    androidx.appcompat.widget.Toolbar toolbar;
    SharedPreferences sp;
    String url = "", ip = "";
    final Calendar myCalendar= Calendar.getInstance();
    private FusedLocationProviderClient fusedLocationClient;
    ArrayList<String> lat;
    ArrayList<String> lon, datetime;
    public static String latitude, longitude;
    public static final String SHARED_PREFS = "shared_prefs";
    private GoogleMap Mmap;
    ArrayList<LatLng> arrayList = new ArrayList<LatLng>();
    String date = "", share = "", uid;
    Button b;
    PopupMenu popup;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewmap);
        toolbar=findViewById(R.id.toolbar2);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_date_range_24);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        sp = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        DatePickerDialog.OnDateSetListener mydate =new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i1, int i2) {
//                myCalendar.set(Calendar.YEAR, i);
//                myCalendar.set(Calendar.MONTH,i1);
//                myCalendar.set(Calendar.DAY_OF_MONTH,i2);
//                String myFormat="yy/MM/dd";
//                SimpleDateFormat dateFormat=new SimpleDateFormat(myFormat, Locale.US);
//                Toast.makeText(viewmap.this, String.valueOf(i), Toast.LENGTH_SHORT).show();
                String m=String.valueOf(i1+1);
                String d1=String.valueOf(i2);
                if (i1+1<10){
                     m="0"+String.valueOf(i1+1);
                }
                if (i2<10){
                    d1="0"+String.valueOf(i2);
                }
                String d=String.valueOf(i)+"-"+m+"-"+d1;
                Toast.makeText(viewmap.this, d, Toast.LENGTH_SHORT).show();
                Intent ik=new Intent(getApplicationContext(),viewmap.class);
                ik.putExtra("date",d);
                startActivity(ik);
            }

        };
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(viewmap.this,mydate,myCalendar.get(Calendar.YEAR),myCalendar.get(Calendar.MONTH),myCalendar.get(Calendar.DAY_OF_MONTH)).show();

            }
        });
        sp = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
//        b=(Button)findViewById(R.id.button);


        share = sp.getString("share", "");
        //  Toast.makeText(getApplicationContext(),share,Toast.LENGTH_LONG).show();
        date = getIntent().getStringExtra("date");
        Toast.makeText(this, date, Toast.LENGTH_SHORT).show();
        ip = sp.getString("ip", "");
//        if(share.equalsIgnoreCase("share") )
//        {
//            uid=sp.getString("sid","");
//            b.setVisibility(View.INVISIBLE);
//        }
//
//        else  if(share.equalsIgnoreCase("all"))
//        {
//            uid=sp.getString("uid","");
//            b.setVisibility(View.INVISIBLE);
//
//        }
//        else
//        {
//            uid=sp.getString("uid","");
//            b.setVisibility(View.VISIBLE);
//        }

//        b.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent i=new Intent(getApplicationContext(),ShareLoc.class);
//                i.putExtra("date",date);
//                startActivity(i);
//            }
//        });

        url = "http://" + ip + ":5000/view_rootmap1";

        // Create a Uri from an intent string. Use the result to create an Intent.
//        Uri gmmIntentUri = Uri.parse("google.streetview:cbll=11.2571,75.7849");
//
//// Create an Intent from gmmIntentUri. Set the action to ACTION_VIEW
//        Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
//// Make the Intent explicit by setting the Google Maps package
//        mapIntent.setPackage("com.google.android.apps.maps");
//
//// Attempt to start an activity that can handle the Intent
//        startActivity(mapIntent);


        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(viewmap.this);


        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++", response);
                    try {

                        JSONArray ar = new JSONArray(response);
                        if (ar.length()==0){
                            Toast.makeText(viewmap.this, "", Toast.LENGTH_SHORT).show();
                        }
                        lat = new ArrayList<>();

                        lon = new ArrayList<>();
                        datetime = new ArrayList<>();
                        for (int i = 0; i < ar.length(); i++) {
                            JSONObject jo = ar.getJSONObject(i);
                            lat.add(jo.getString("LATITUDE"));
                            lon.add(jo.getString("LONGITUDE"));
                            datetime.add(jo.getString("DATE"));
                            LatLng loc = new LatLng(Double.parseDouble(jo.getString("LATITUDE")), Double.parseDouble(jo.getString("LONGITUDE")));
                            arrayList.add(loc);
                        }

                        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                                .findFragmentById(R.id.map);
                        mapFragment.getMapAsync((OnMapReadyCallback) viewmap.this);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {

                        Toast.makeText(getApplicationContext(), String.valueOf(error), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", sp.getString("lid", ""));
                params.put("date", date);

                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);


    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        // [START_EXCLUDE silent]
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        // [END_EXCLUDE]
        //LatLng sydney = new LatLng(-33.852, 151.211);

        Mmap = googleMap;

        for (int i = 0; i < arrayList.size(); i++) {
            Mmap.addMarker(new MarkerOptions()
                    .position(arrayList.get(i)).title(datetime.get(i)));
            // [START_EXCLUDE silent]
            Mmap.animateCamera(CameraUpdateFactory.zoomTo(35.0f));
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }else {
                Mmap.setMyLocationEnabled(true);
                Mmap.setTrafficEnabled(true);
                Mmap.setBuildingsEnabled(true);
                fusedLocationClient.getLastLocation()
                        .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
                                    // Logic to handle location object

                                    LatLng loc = new LatLng(location.getLatitude(), location.getLongitude());
                                    Log.d("location", String.valueOf(location.getLatitude()));
                                    Mmap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc, 10f));
                                }
                            }
                        });
            }
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(arrayList.get(i)));
            // [END_EXCLUDE]
        }
        // [END maps_marker_on_map_ready_add_marker]
    }
    @Override
    public void onBackPressed() {
        Intent ik = new Intent(getApplicationContext(), Home.class);
        ik.putExtra("date","na");
        startActivity(ik);
        super.onBackPressed();
    }
}
