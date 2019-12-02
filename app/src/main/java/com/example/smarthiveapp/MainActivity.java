package com.example.smarthiveapp;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.media.CamcorderProfile;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.EditText;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FileDownloadTask;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnPausedListener;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageException;
import com.google.firebase.storage.StorageMetadata;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Button;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;
import com.jjoe64.graphview.series.PointsGraphSeries;

import org.w3c.dom.Text;

import java.io.IOException;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;


public class MainActivity extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    // Global variables
    private static final String TAG = "My Activity";
    private Button videoButton;
    private Button audioButton;
    ArrayList<Double> finalTemp = new ArrayList<>();
    ArrayList<Double> finalSound = new ArrayList<>();




    @Override
    public void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Populate current readings
        updateCurrentData();

        // Go to VideoActivity on button click
        videoButton = (Button) findViewById(R.id.videoStreamButton);
        videoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                openDisplayVideo();
            }
        });

        // Get reference for audio file
        FirebaseStorage storage = FirebaseStorage.getInstance();
        StorageReference storageRef = storage.getReference();

        // Download audio file and update player with source
        final MediaPlayer audioPlayer = new MediaPlayer();
        storageRef.child("all.wav").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                try {
                    audioPlayer.setDataSource(uri.toString());
                    audioPlayer.prepare();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });

        // Play audio on click
        audioButton = (Button) findViewById(R.id.audioStreamButton);
        audioButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (audioPlayer.isPlaying()){
                    audioPlayer.pause();
                }
                else {
                    audioPlayer.start();
                }
            }
        });

        // Initialize dropdown menu
        Spinner optionsSpinner = (Spinner) findViewById(R.id.options_spinner);
        optionsSpinner.setOnItemSelectedListener(this);
        // populate dropdown menu from string array in resource
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.spinner_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        optionsSpinner.setAdapter(adapter);


    }

    // Dropdown menu item selection
    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int pos, long id) {
        // Initialize graph and data
        GraphView graph = (GraphView) findViewById(R.id.graph);
        PointsGraphSeries<DataPoint> tempSeries = new PointsGraphSeries<>(generateTempData());
        PointsGraphSeries<DataPoint> soundSeries = new PointsGraphSeries<DataPoint>(generateSoundData());



        // enable scaling and scrolling
        graph.getViewport().setScalable(true);
        graph.getViewport().setScalableY(true);

        // if Temp is selected
        if(pos == 1){
            // clear graph
            graph.removeAllSeries();
            // update data
            tempSeries.resetData(generateTempData());
            // set data specific view window
            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setMinY(60);
            graph.getViewport().setMaxY(120);

            graph.getViewport().setXAxisBoundsManual(true);
            graph.getViewport().setMinX(0);
            graph.getViewport().setMaxX(10);
            // add temp data to graph
            graph.addSeries(tempSeries);
        }

        if(pos == 2){
            // clear graph
            graph.removeAllSeries();
            // update data
            soundSeries.resetData(generateSoundData());
            // set data specific view window
            graph.getViewport().setYAxisBoundsManual(true);
            graph.getViewport().setMinY(100);
            graph.getViewport().setMaxY(600);

            graph.getViewport().setXAxisBoundsManual(true);
            graph.getViewport().setMinX(0);
            graph.getViewport().setMaxX(16);
            // add sound data to graph
            graph.addSeries(soundSeries);
        }

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }

    // Go to VideoActivity
    public void openDisplayVideo() {
        Intent intent = new Intent(this, VideoActivity.class);
        startActivity(intent);
    }

    private DataPoint[] generateTempData() {
        // get values reference from firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference valuesRef = db.collection("data").document("values");
        valuesRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                // Pull temperature data as an array
                ArrayList<Double> tempGroup = (ArrayList<Double>) documentSnapshot.get("Temperature");
                finalTemp = tempGroup;
            }
        });

        //Iterate through array to output a DataPoint array for graphing
        DataPoint[] values = new DataPoint[finalTemp.size()];
        for (int i = 0; i < finalTemp.size(); i++) {
            double x = i;
            double y = finalTemp.get(i);
            DataPoint v = new DataPoint(x, y);
            values[i] = v;
        }
        return values;
    }


    private DataPoint[] generateSoundData() {
        // get values reference from firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference valuesRef = db.collection("data").document("values");
        valuesRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                // pull sound data as an array
                ArrayList<Double> soundGroup = (ArrayList<Double>) documentSnapshot.get("avgPitch");
                finalSound = soundGroup;
            }
        });

        // Iterate through arraylist to output a datapoint array for graphing
        DataPoint[] values = new DataPoint[finalSound.size()];
        for (int i = 0; i < finalSound.size(); i++) {
            double x = i;
            double y = finalSound.get(i);
            DataPoint v = new DataPoint(x, y);
            values[i] = v;
        }
        return values;
    }

    public void updateCurrentData(){
        // Get values reference from firestore
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        DocumentReference valuesRef = db.collection("data").document("values");
        valuesRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                // Get Temp data
                List<Double> tempGroup = (List<Double>) documentSnapshot.get("Temperature");
                // Add latest data point to the string for display
                String newStr = "Temp: " + tempGroup.get(tempGroup.size() - 1).toString() + " F";
                TextView tempText = (TextView) findViewById(R.id.tempText);
                tempText.setText(newStr);
                // Get sound data
                List<Double> soundGroup = (List<Double>) documentSnapshot.get("avgPitch");
                // Add latest data point to the string for display
                newStr = "Avg Pitch: " + soundGroup.get(soundGroup.size() - 1).toString() + " Hz";
                TextView soundText = (TextView) findViewById(R.id.soundText);
                soundText.setText(newStr);

            }
        });
    }

}