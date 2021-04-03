package com.litholr.chord;

import android.Manifest;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {

    Button btnStartRecord, btnStopRecord, btnStartPlay, btnStopPlay;
    String pathSave = "";

    MediaRecorder mediaRecorder;
    MediaPlayer mediaPlayer;

    final int REQUEST_PERMISSION_CODE = 1000;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (!checkPermissionFromDevice())
            requestPermission();

        btnStartPlay = (Button)findViewById(R.id.btnStartPlay);
        btnStartRecord = (Button)findViewById(R.id.btnStartRecord);
        btnStopPlay = (Button)findViewById(R.id.btnStopPlay);
        btnStopRecord = (Button)findViewById(R.id.btnStopRecord);



        btnStartRecord.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view){
                    pathSave = getFilesDir()+"/"
                            + UUID.randomUUID().toString()+"_audio_record.3gp";
                    setupMediaRecorder();
                    try{
                        mediaRecorder.prepare();
                        mediaRecorder.start();
                    }catch (IOException e) {
                        e.printStackTrace();
                    }

                    btnStartPlay.setEnabled(false);
                    btnStopPlay.setEnabled(false);
                    btnStopRecord.setEnabled(true);

                    Toast.makeText(MainActivity.this,"Recording...", Toast.LENGTH_SHORT).show();
            }
        });

        btnStopRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mediaRecorder.stop();
                btnStopRecord.setEnabled(false);
                btnStartPlay.setEnabled(true);
                btnStartRecord.setEnabled(true);
                btnStopPlay.setEnabled(false);
            }
        });

        btnStartPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnStopPlay.setEnabled(true);
                btnStartRecord.setEnabled(false);
                btnStopRecord.setEnabled(false);
                btnStartPlay.setEnabled(false);

                mediaPlayer = new MediaPlayer();
                try {
                    mediaPlayer.setDataSource(pathSave);
                    mediaPlayer.prepare();

                } catch (IOException e) {
                    e.printStackTrace();
                }

                mediaPlayer.start();
                Toast.makeText(MainActivity.this,"Playing...",Toast.LENGTH_SHORT).show();
            }
        });

        btnStopPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnStopRecord.setEnabled(false);
                btnStartRecord.setEnabled(true);
                btnStopPlay.setEnabled(false);
                btnStartPlay.setEnabled(true);

                if (mediaPlayer != null){
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    setupMediaRecorder();
                }
            }
        });

    }

    private void setupMediaRecorder() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        mediaRecorder.setOutputFile(pathSave);
    }


    private boolean checkPermissionFromDevice(){
        int write_external_storage_result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);
        int read_external_storage_result = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);

        return write_external_storage_result == PackageManager.PERMISSION_GRANTED &&
                read_external_storage_result == PackageManager.PERMISSION_GRANTED &&
                record_audio_result == PackageManager.PERMISSION_GRANTED;
    }


    private void requestPermission() {
        ActivityCompat.requestPermissions(this,new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.READ_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO

        },REQUEST_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case REQUEST_PERMISSION_CODE:
            {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this,"Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
}