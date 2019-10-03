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

    //LineGraphSeries<DataPoint> series;
    PointsGraphSeries<DataPoint> pointSeries;
    double x = 1;

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
        PointsGraphSeries<DataPoint> pointSeries = new PointsGraphSeries<>(new DataPoint[] {
                new DataPoint(1, 1)
        });
        graph.addSeries(pointSeries);
        pointSeries.setShape(PointsGraphSeries.Shape.TRIANGLE);

        if(pos != 0){
            pointSeries.appendData(new DataPoint(x+=1,x),true, 4);
        }
        // set manual X bounds
        graph.getViewport().setXAxisBoundsManual(true);
        graph.getViewport().setMinX(0);
        graph.getViewport().setMaxX(10);

        // set manual Y bounds
        graph.getViewport().setYAxisBoundsManual(true);
        graph.getViewport().setMinY(0);
        graph.getViewport().setMaxY(10);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

}