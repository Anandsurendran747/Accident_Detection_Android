package com.example.accident;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Set;

public class SetIp extends AppCompatActivity {
    EditText e1;
    Button b1;
    SharedPreferences sh;
    String id;
    String type;
    public static final String SHARED_PREFS = "shared_prefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_ip);
        sh = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        id=sh.getString("lid",null);
        type=sh.getString("type",null);
        b1=findViewById(R.id.button13);
        e1=findViewById(R.id.editTextTextPersonName12);
        requestPermission();

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String ip=e1.getText().toString();

                SharedPreferences.Editor edp = sh.edit();
                edp.putString("ip", ip);
                edp.commit();

                if (ip.equals("")){
                    e1.setError("Type ip address");
                }else{
                    if (ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&ActivityCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED  ){
                        Toast.makeText(SetIp.this, "Please access Permisisons to continue", Toast.LENGTH_LONG).show();
                        requestPermission();

                    }else{
                        Intent intent=new Intent(SetIp.this,MainActivity.class);
                        startActivity(intent);
                    }


                }


            }
        });
    }
    public void requestPermission(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED &&ActivityCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS) != PackageManager.PERMISSION_GRANTED  ) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(SetIp.this,
                    Manifest.permission.ACCESS_FINE_LOCATION)) {
                ActivityCompat.requestPermissions(SetIp.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.SEND_SMS}, 1);
            } else {
                ActivityCompat.requestPermissions(SetIp.this,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,Manifest.permission.SEND_SMS}, 1);
            }

        }
    }
    @Override
    public void onBackPressed() {
        Intent ik = new Intent(getApplicationContext(), SetIp.class);
        startActivity(ik);
        super.onBackPressed();
    }
    @Override
    protected void onStart() {
        super.onStart();
        if (id != null ) {
            Toast.makeText(this, type, Toast.LENGTH_SHORT).show();
            if (type.equalsIgnoreCase("user")){
                Intent in = new Intent(getApplicationContext(), LocationService.class);
                startService(in);
                Intent in1 = new Intent(getApplicationContext(), LocationServiceno.class);
                startService(in1);
                Intent i = new Intent(SetIp.this, Home.class);
                i.putExtra("date","na");
                startActivity(i);
            }else{
                Intent in = new Intent(getApplicationContext(), LocationService1.class);
                startService(in);
                Intent in1 = new Intent(getApplicationContext(), LocationServiceno.class);
                startService(in1);
                Intent ik = new Intent(getApplicationContext(), EmergencyHome.class);
                ik.putExtra("date","na");
                startActivity(ik);

            }
        }
    }
}