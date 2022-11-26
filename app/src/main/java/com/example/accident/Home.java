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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.PopupMenu;
import android.widget.Toast;
import android.widget.Toolbar;

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

public class Home extends AppCompatActivity implements OnMapReadyCallback{
    Button b1,b2,b3,b4,b5,b6,b7,b8;
    SharedPreferences sh;
    ArrayList<String> lat;
    ArrayList<String> lon,datetime;
    int s=0;

    public static String latitude,longitude;
    private GoogleMap Mmap;
    ArrayList<LatLng> arrayList = new ArrayList<LatLng>();
    private FusedLocationProviderClient fusedLocationClient;
    String date="na",share="",uid;
    String url="",ip="";
    public static final String SHARED_PREFS = "shared_prefs";
    Toolbar t;
    PopupMenu popup;
    private DrawerLayout drawer;
    androidx.appcompat.widget.Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main3);
        getWindow().setFlags(WindowManager.LayoutParams.FIRST_APPLICATION_WINDOW,
                WindowManager.LayoutParams.FIRST_APPLICATION_WINDOW);

        toolbar=findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
//        toolbar.setNavigationIcon(R.drawable.ic_baseline_account_circle_24);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        //toolbar.setNavigationIcon(R.drawable.ic_toolbar);
        toolbar.setTitle("");
        toolbar.setSubtitle("");
        //toolbar.setLogo(R.drawable.ic_toolbar);
        toolbar.setNavigationIcon(R.drawable.ic_baseline_account_circle_24);
toolbar.setNavigationOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View view) {
         popup = new PopupMenu(getApplicationContext(), view);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.profile_menu, popup.getMenu());
        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem menuItem) {
                // Toast message on menu item clicked
//                Toast.makeText(Home.this, "You Clicked " + menuItem.getTitle(), Toast.LENGTH_SHORT).show();
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
                if (id==R.id.viewProfile){
                    Intent intent1 = new Intent(getApplicationContext(),ViewUserAccount.class);
                    startActivity(intent1);
                    return true;
                }
                return true;

            }
        });
        popup.show();

    }
});


//        popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
//            @Override
//            public boolean onMenuItemClick(MenuItem item) {
//                int id = item.getItemId();
//                Toast.makeText(getApplicationContext(), String.valueOf(id), Toast.LENGTH_SHORT).show();
//                return true;
//            }
//        });
        //toolbar.setLogo(R.drawable.ic_toolbar);


        sh = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);


        share=sh.getString("share","");
        //  Toast.makeText(getApplicationContext(),share,Toast.LENGTH_LONG).show();

        date=getIntent().getStringExtra("date");

        ip=sh.getString("ip","");
//

        url ="http://"+ip+":5000/view_rootmap1";



        RequestQueue queue = Volley.newRequestQueue(Home.this);
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
                    mapFragment.getMapAsync((OnMapReadyCallback) Home.this);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Toast.makeText(Home.this, "error", Toast.LENGTH_SHORT).show();
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



    }


//    @Override
//    public boolean onMenuItemClick(MenuItem item) {
//        Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
//        if (item.getGroupId() == R.id.logout) {
//            Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
//            // edit colors menu
//        } else {
//            // edit shape menu
//        }
//        return true;
//    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater inflater=getMenuInflater();
//        inflater.inflate(R.menu.profile_menu,menu);
//        return true;
//    }
//public  boolean onMenuItemClick (MenuItem item){
//    int id = item.getItemId();
//    Toast.makeText(this, String.valueOf(id), Toast.LENGTH_SHORT).show();
//        return true;
//}
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle item selection
//        int id = item.getItemId();
//        Toast.makeText(this, String.valueOf(id), Toast.LENGTH_SHORT).show();
//        if (id == R.id.logout) {
//            SharedPreferences.Editor editor = sh.edit();
//            editor.clear();
//            editor.apply();
//            Intent intent1 = new Intent(this,SetIp.class);
//            this.startActivity(intent1);
//            return true;
//        }
//
//
//
//        return super.onOptionsItemSelected(item);
//    }





    public void viewmap(View view){
        Intent i=new Intent(getApplicationContext(),viewmap.class);
        i.putExtra("date","na");
                startActivity(i);
    }
    public void viewhospitals(View view){
        Intent intent=new Intent(Home.this,ViewHospital.class);
                startActivity(intent);
    }
    public void setemergencynum(View view){
        Intent intent=new Intent(Home.this,SetEmergencyNumber.class);
                startActivity(intent);
    }
    public void addaccident(View view){
        Intent intent=new Intent(Home.this,ManuallyAddAccident.class);
                startActivity(intent);
    }
    public void viewnews(View view){
        Intent intent=new Intent(Home.this,ViewNews.class);
        startActivity(intent);
    }
    public void sendfeedback(View view){
        Intent intent=new Intent(Home.this,Feedback.class);
        startActivity(intent);
    }

//    public void samp(View view){
////        Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
//        NavigationView n=findViewById(R.id.nav_view);
//        if (s==0){
//            n.setVisibility(View.VISIBLE);
//            s=1;
//        }else{
//            n.setVisibility(View.INVISIBLE);
//            s=0;
//        }
//
//    }





    @Override
    public void onMapReady(GoogleMap googleMap) {
        // [START_EXCLUDE silent]
        // Add a marker in Sydney, Australia,
        // and move the map's camera to the same location.
        // [END_EXCLUDE]
        //LatLng sydney = new LatLng(-33.852, 151.211);

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
        // [END maps_marker_on_map_ready_add_marker]
    }
    @Override
    public void onBackPressed() {
        Intent ik = new Intent(getApplicationContext(), Home.class);
        ik.putExtra("date","na");
        startActivity(ik);
        super.onBackPressed();
    }


//    @Override
//    public boolean onMenuItemClick(MenuItem item) {
//        Toast.makeText(this, "", Toast.LENGTH_SHORT).show();
//        if (item.getGroupId()==1000385){
//            SharedPreferences.Editor editor = sh.edit();
//            editor.clear();
//            editor.apply();
//            Intent intent=new Intent(Home.this,SetIp.class);
//            startActivity(intent);
//            finish();
//        }
//        return false;
//    }
}