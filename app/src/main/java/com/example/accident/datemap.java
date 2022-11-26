package com.example.accident;


import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class datemap extends AppCompatActivity {
    EditText e1;
    Button b;
    String ipad="",date,uid;
    SharedPreferences sp;
    public static final String SHARED_PREFS = "shared_prefs";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_datemap);
        b=(Button)findViewById(R.id.bd);
        e1=(EditText)findViewById(R.id.editTextTextPersonName4);
        sp = getSharedPreferences(SHARED_PREFS, Context.MODE_PRIVATE);
        ipad=sp.getString("ip","");
        uid=sp.getString("lid","");
        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(datemap.this, e1.getText().toString(), Toast.LENGTH_SHORT).show();

                date = e1.getText().toString();
                if (date.equals("")) {
                    e1.setError("Provide date");
                } else {
                    Intent i=new Intent(getApplicationContext(),viewmap.class);
                    i.putExtra("date",date);
                    startActivity(i);
                }
            }
        });
    }
}