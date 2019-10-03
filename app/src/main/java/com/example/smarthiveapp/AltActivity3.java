package com.example.smarthiveapp;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;


public class AltActivity3 extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    LineGraphSeries<DataPoint> tempSeries;
    LineGraphSeries<DataPoint> humSeries;
    LineGraphSeries<DataPoint> soundSeries;
    GraphView graph;
    double x,y;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Spinner optionsSpinner = (Spinner) findViewById(R.id.options_spinner);
        optionsSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        optionsSpinner.setAdapter(adapter);



        GraphView graph = (GraphView) findViewById(R.id.graph);
        tempSeries = new LineGraphSeries<DataPoint>();
        humSeries = new LineGraphSeries<DataPoint>();
        soundSeries = new LineGraphSeries<DataPoint>();
        for(int i = 0; i < 750; i++){
            x = x + 0.1;
            y = Math.sin(x / 5) + 1;
            tempSeries.appendData(new DataPoint(x,y), true, 750);
            y = Math.cos(x / 2) + 1;
            humSeries.appendData(new DataPoint(x,y), true, 750);
            y = Math.sqrt(x);
            soundSeries.appendData(new DataPoint(x,y), true, 750);
        }

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view,
                               int pos, long id) {
        //graph.removeAllSeries();

       /* if(pos == 1){
            graph.addSeries(tempSeries);
        }
        if(pos == 2){
            graph.addSeries(humSeries);
        }
        if(pos == 3){
            graph.addSeries(soundSeries);
        }
       */
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
        // Another interface callback
    }



}