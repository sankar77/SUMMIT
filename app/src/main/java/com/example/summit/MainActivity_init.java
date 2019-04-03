package com.example.summit;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class MainActivity_init extends AppCompatActivity {

    Button onlineBtn,offlineBtn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_init);
        onlineBtn = findViewById(R.id.online_btn);
        offlineBtn = findViewById(R.id.offline_btn);
        onlineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Online Selected",Toast.LENGTH_LONG).show();
                Intent it = new Intent(MainActivity_init.this,MainActivity.class);
                startActivity(it);
            }
        });
        offlineBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getApplicationContext(),"Offline Selected", Toast.LENGTH_LONG).show();
                Intent it1 = new Intent(MainActivity_init.this,MainActivity_1.class);
                startActivity(it1);
            }
        });
    }



}
