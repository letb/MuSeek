package com.letb.museek;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent intent = getIntent();
        String token = intent.getStringExtra("PLEER.COM ACCESS TOKEN");
        Toast.makeText(MainActivity.this, "Success!: " + token, Toast.LENGTH_SHORT).show();

    }
}