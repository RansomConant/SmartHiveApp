package com.example.smarthiveapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void tempHistory(View view) {
        Intent intent = new Intent(this, DisplayTempHistory.class);
        startActivity(intent);
    }

    public void humidityHistory(View view) {
        Intent intent = new Intent(this, DisplayHumidityHistory.class);
        startActivity(intent);
    }

}