package com.example.smarthiveapp;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import android.widget.VideoView;
import android.net.Uri;
import android.widget.MediaController;
import android.app.ProgressDialog;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;

public class VideoActivity extends AppCompatActivity {

    VideoView simpleVideoView;
    MediaController mediaControls;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_video);

        simpleVideoView = (VideoView) findViewById(R.id.simpleVideoView);

        if (mediaControls == null) {
            // create an object of media controller class
            mediaControls = new MediaController(VideoActivity.this);
            mediaControls.setAnchorView(simpleVideoView);
        }
        // set the media controller for video view
        simpleVideoView.setMediaController(mediaControls);

        FirebaseStorage storage = FirebaseStorage.getInstance();

        StorageReference storageRef = storage.getReference();



        storageRef.child("all.mp4").getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                simpleVideoView.setVideoURI(uri);
            }
        });

        // start a video
        simpleVideoView.start();

        // implement on completion listener on video view
        simpleVideoView.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                Toast.makeText(getApplicationContext(), "Thank You...!!!", Toast.LENGTH_LONG).show(); // display a toast when an video is completed
            }
        });
        simpleVideoView.setOnErrorListener(new MediaPlayer.OnErrorListener() {
            @Override
            public boolean onError(MediaPlayer mp, int what, int extra) {
                Toast.makeText(getApplicationContext(), "Oops An Error Occur While Playing Video...!!!", Toast.LENGTH_LONG).show(); // display a toast when an error is occured while playing an video
                return false;
            }
        });
    }
}
