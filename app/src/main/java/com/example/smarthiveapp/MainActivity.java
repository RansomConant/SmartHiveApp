package com.example.smarthiveapp;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Button;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    private Button videoButton;
    private Button audioButton;
    PointsGraphSeries<DataPoint> tempSeries;
    PointsGraphSeries<DataPoint> humSeries;
    PointsGraphSeries<DataPoint> soundSeries;
    double x = 1, y;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        videoButton = (Button) findViewById(R.id.videoStreamButton);
        videoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openDisplayVideo();
            }
        });
        audioButton = (Button) findViewById(R.id.audioStreamButton);
        final MediaPlayer audioPlayer = MediaPlayer.create(this, R.raw.bumblebee);
        audioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (audioPlayer.isPlaying()) {
                    audioPlayer.pause();
                }
                else {
                    audioPlayer.start();
                }
            }
        });


        Spinner optionsSpinner = (Spinner) findViewById(R.id.options_spinner);
        optionsSpinner.setOnItemSelectedListener(this);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        optionsSpinner.setAdapter(adapter);


    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        GraphView graph = (GraphView) findViewById(R.id.graph);
        PointsGraphSeries<DataPoint> tempSeries = new PointsGraphSeries<DataPoint>();
        PointsGraphSeries<DataPoint> humSeries = new PointsGraphSeries<DataPoint>();
        PointsGraphSeries<DataPoint> soundSeries = new PointsGraphSeries<DataPoint>();

        tempSeries.resetData(generateTempData());
        humSeries.resetData(generateHumData());
        soundSeries.resetData(generateSoundData());

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

    public void openDisplayVideo() {
        Intent intent = new Intent(this, VideoActivity.class);
        startActivity(intent);
    }

    private DataPoint[] generateTempData() {
        int count = 40;
        DataPoint[] values = new DataPoint[count];
        for (int i = 0; i < count; i++) {
            double x = i;
            double y = (0.5) * Math.sin(x) + 1;
            DataPoint v = new DataPoint(x, y);
            values[i] = v;
        }
        return values;
    }
    private DataPoint[] generateHumData() {
        int count = 40;
        DataPoint[] values = new DataPoint[count];
        for (int i = 0; i < count; i++) {
            double x = i;
            double y = Math.sqrt(x);
            DataPoint v = new DataPoint(x, y);
            values[i] = v;
        }
        return values;
    }

    private DataPoint[] generateSoundData() {
        int count = 40;
        DataPoint[] values = new DataPoint[count];
        for (int i = 0; i < count; i++) {
            double x = i;
            double y = 2;
            DataPoint v = new DataPoint(x, y);
            values[i] = v;
        }
        return values;
    }

}