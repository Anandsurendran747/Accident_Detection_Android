package com.example.accident;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class EmergencyHome extends AppCompatActivity implements OnMapReadyCallback {
    Button b1,b2,b3;
    SharedPreferences sh;
    public static final String SHARED_PREFS = "shared_prefs";
    PopupMenu popup;
    private DrawerLayout drawer;
    androidx.appcompat.widget.Toolbar toolbar;
    ArrayList<String> lat;
    ArrayList<String> lon,datetime;
    int s=0;

    public static String latitude,longitude;
    private GoogleMap Mmap;
    ArrayList<LatLng> arrayList = new ArrayList<LatLng>();
    private FusedLocationProviderClient fusedLocationClient;
    String date="",share="",uid;
    String url="",ip="";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_emergency_home);
        sh = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        toolbar=findViewById(R.id.toolbar1);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_account_circle_24);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //toolbar.setNavigationIcon(R.drawable.ic_toolbar);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        //toolbar.setLogo(R.drawable.ic_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                popup = new PopupMenu(getApplicationContext(), view);
                MenuInflater inflater = popup.getMenuInflater();
                inflater.inflate(R.menu.emergency_menu, popup.getMenu());
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        // Toast message on menu item clicked
                        Toast.makeText(EmergencyHome.this, "You Clicked " + menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                        int id = menuItem.getItemId();
//                Toast.makeText(this, String.valueOf(id), Toast.LENGTH_SHORT).show();
                        if (id == R.id.logout) {
                            SharedPreferences.Editor editor = sh.edit();
                            editor.clear();
                            editor.apply();
                            Intent intent1 = new Intent(getApplicationContext(),SetIp.class);
                            startActivity(intent1);
                            return true;
                        }
                        return true;

                    }
                });
                popup.show();

            }
        });
        share=sh.getString("share","");
        //  Toast.makeText(getApplicationContext(),share,Toast.LENGTH_LONG).show();

        date=getIntent().getStringExtra("date");

        ip=sh.getString("ip","");
//

        url ="http://"+ip+":5000/view_rootmap1";
        RequestQueue queue = Volley.newRequestQueue(EmergencyHome.this);
        Toast.makeText(this, url, Toast.LENGTH_SHORT).show();

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                // Display the response string.
                Log.d("+++++++++++++++++",response);
                try {

                    JSONArray ar=new JSONArray(response);

                    lat=new ArrayList<>();

                    lon=new ArrayList<>();
                    datetime=new ArrayList<>();
                    for(int i=0;i<ar.length();i++)
                    {
                        JSONObject jo=ar.getJSONObject(i);
                        lat.add(jo.getString("LATITUDE"));
                        lon.add(jo.getString("LONGITUDE"));
                        datetime.add(jo.getString("DATE"));
                        LatLng loc=new LatLng( Double.parseDouble(jo.getString("LATITUDE")),Double.parseDouble(jo.getString("LONGITUDE")));
                        arrayList.add(loc);
                    }

                    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                            .findFragmentById(R.id.map);
                    mapFragment.getMapAsync((OnMapReadyCallback) EmergencyHome.this);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(EmergencyHome.this, "error", Toast.LENGTH_SHORT).show();
                        Toast.makeText(getApplicationContext(),String.valueOf(error), Toast.LENGTH_LONG).show();
                    }
                }) {
            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("uid", sh.getString("lid",""));
                params.put("date", date);

                return params;
            }
        };
        // Add the request to the RequestQueue.
        queue.add(stringRequest);
//        b1=findViewById(R.id.button8);
//        b2=findViewById(R.id.button9);
//        b3=findViewById(R.id.button10);
//        b1.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(EmergencyHome.this,EmergencyViewNearestHospital.class);
//                startActivity(intent);
//            }
//        });
//        b2.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent=new Intent(EmergencyHome.this,ViewAccidentHistory.class);
//                startActivity(intent);
//            }
//        });
//        b3.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                SharedPreferences.Editor editor = sh.edit();
//                editor.clear();
//                editor.apply();
//                Intent intent1 = new Intent(getApplicationContext(),SetIp.class);
//                startActivity(intent1);
//
//            }
//        });
    }
    public void viewhospitals(View view){
        Intent intent=new Intent(EmergencyHome.this,ViewHospital.class);
        startActivity(intent);
    }
    public void viewAccidentHistory(View view){
        Intent intent=new Intent(EmergencyHome.this,ViewAccidentHistory.class);
        startActivity(intent);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        Mmap = googleMap;
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.

            Toast.makeText(this, "Please allow location permission to view map", Toast.LENGTH_SHORT).show();
            return;
        }else{
            LatLng l=new LatLng(11.222,73.222);
            Mmap.setMyLocationEnabled(true);
            Mmap.setTrafficEnabled(true);
            Mmap.setBuildingsEnabled(true);
            fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        }
        Log.d("location", String.valueOf(fusedLocationClient));
        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            // Logic to handle location object
                            LatLng loc=new LatLng(location.getLatitude(),location.getLongitude());
                            Log.d("location", String.valueOf(location.getLatitude()));
                            Mmap.moveCamera(CameraUpdateFactory.newLatLngZoom(loc,15f));
                        }
                    }
                });

        for (int i = 0; i < arrayList.size(); i++) {
            Mmap.addMarker(new MarkerOptions()
                    .position(arrayList.get(i)).title(datetime.get(i)));
            // [START_EXCLUDE silent]
            Mmap.animateCamera(CameraUpdateFactory.zoomTo(15.0f));
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(arrayList.get(i)));
            // [END_EXCLUDE]
        }
    }
    @Override
    public void onBackPressed() {
        Intent ik = new Intent(getApplicationContext(), EmergencyHome.class);
        ik.putExtra("date","na");
        startActivity(ik);
        super.onBackPressed();
    }
}