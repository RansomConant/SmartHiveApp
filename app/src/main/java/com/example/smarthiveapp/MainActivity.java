package com.example.smarthiveapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    PointsGraphSeries<DataPoint> tempSeries;
    PointsGraphSeries<DataPoint> humSeries;
    PointsGraphSeries<DataPoint> soundSeries;
    double x = 1, y;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        Spinner optionsSpinner = (Spinner) findViewById(R.id.options_spinner);
        optionsSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        optionsSpinner.setAdapter(adapter);



       /* series = new LineGraphSeries<DataPoint>();
        for(int i = 0; i < 750; i++){
            x = x + 0.1;
            y = Math.sin(x / 5) + 1;
            series.appendData(new DataPoint(x,y), true, 750);
        }
        graph.addSeries(series);
        */
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        GraphView graph = (GraphView) findViewById(R.id.graph);
        PointsGraphSeries<DataPoint> tempSeries = new PointsGraphSeries<DataPoint>();
        PointsGraphSeries<DataPoint> humSeries = new PointsGraphSeries<DataPoint>();
        PointsGraphSeries<DataPoint> soundSeries = new PointsGraphSeries<DataPoint>();
        if(pos == 0) {
            for (int i = 0; i < 40; i++) {
                x = x + 0.5;
                y = Math.sin(x / 5) + 1;
                tempSeries.appendData(new DataPoint(x, y), true, 40);
                y = Math.sqrt(x);
                humSeries.appendData(new DataPoint(x, y), true, 40);
                y = Math.cos(x / 5 + 1);
                soundSeries.appendData(new DataPoint(x, y), true, 40);
            }
        }

        tempSeries.setShape(PointsGraphSeries.Shape.TRIANGLE);
        humSeries.setShape(PointsGraphSeries.Shape.RECTANGLE);
        soundSeries.setShape(PointsGraphSeries.Shape.POINT);

        if(pos == 1){
            graph.removeAllSeries();
            graph.addSeries(tempSeries);
        }
        if(pos == 2){
            graph.removeAllSeries();
            graph.addSeries(humSeries);
        }
        if(pos == 3){
            graph.removeAllSeries();
            graph.addSeries(soundSeries);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}