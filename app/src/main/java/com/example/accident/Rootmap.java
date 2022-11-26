package com.example.accident;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;

public class Rootmap extends AppCompatActivity {
    Button b1,b2;
    SharedPreferences sp;
    public static final String SHARED_PREFS = "shared_prefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_rootmap);
        b1=(Button)findViewById(R.id.all);
        b2=(Button)findViewById(R.id.date);
        sp = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);

        b1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor ed=sp.edit();
                ed.putString("share","all");
                ed.commit();

                Intent i=new Intent(getApplicationContext(),viewmap.class);
                i.putExtra("date","na");
                startActivity(i);
            }
        });
        b2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences.Editor ed=sp.edit();
                ed.putString("share","date");
                ed.commit();
                Intent i=new Intent(getApplicationContext(),datemap.class);
                i.putExtra("date","na");
                startActivity(i);
            }
        });
    }
}