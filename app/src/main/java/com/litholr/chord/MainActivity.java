package com.litholr.chord;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.media.MediaPlayer;
import android.media.MediaRecorder;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import java.io.File;
import java.io.IOException;

public class MainActivity extends AppCompatActivity {

    Button btnStartRecord, btnStopRecord, btnStartPlay, btnStopPlay, btnLogin, btnSeeList;
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

        btnStartPlay = (Button) findViewById(R.id.btnStartPlay);
        btnStartRecord = (Button) findViewById(R.id.btnStartRecord);
        btnStopPlay = (Button) findViewById(R.id.btnStopPlay);
        btnStopRecord = (Button) findViewById(R.id.btnStopRecord);


        btnLogin = (Button) findViewById(R.id.btnLogin);


        btnSeeList = (Button) findViewById(R.id.btnSeeList);

        btnStartRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                pathSave = getFilesDir() + "/"
//                        + UUID.randomUUID().toString() + "_audio_record.mp3";
                pathSave = Environment.getExternalStorageDirectory()
                        .getAbsolutePath()
                        + "/Documents/Chord/Audio/audio_record.mp3";
                setupMediaRecorder();
                try {
                    mediaRecorder.prepare();
                    mediaRecorder.start();
                } catch (IOException e) {
                    e.printStackTrace();
                }

                btnStartPlay.setEnabled(false);
                btnStopPlay.setEnabled(false);
                btnStopRecord.setEnabled(true);

                Toast.makeText(MainActivity.this, "Recording...", Toast.LENGTH_SHORT).show();
            }
        });

        btnStopRecord.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    mediaRecorder.stop();
                } catch (IllegalStateException ise){
                    ise.printStackTrace();
                }
                mediaRecorder.release();
                mediaRecorder = null;
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
                Toast.makeText(MainActivity.this, "Playing...", Toast.LENGTH_SHORT).show();
            }
        });

        btnStopPlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnStopRecord.setEnabled(false);
                btnStartRecord.setEnabled(true);
                btnStopPlay.setEnabled(false);
                btnStartPlay.setEnabled(true);

                if (mediaPlayer != null) {
                    mediaPlayer.stop();
                    mediaPlayer.release();
                    setupMediaRecorder();
                }
            }
        });

        btnSeeList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, ListActivity.class);
                startActivity(intent);
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivityForResult(intent,0);
            }
        });
    }

    private void setupMediaRecorder() {
        mediaRecorder = new MediaRecorder();
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC);
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.MPEG_4);
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB);
        File dir = new File(Environment.getExternalStorageDirectory().getAbsolutePath()
                + "/Documents/Chord/Audio");
        if(!dir.exists()){
            dir.mkdirs();
        }
        mediaRecorder.setOutputFile(pathSave);
    }


    private boolean checkPermissionFromDevice() {
        int write_external_storage_result = ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE);
        int record_audio_result = ContextCompat.checkSelfPermission(this, Manifest.permission.RECORD_AUDIO);

        return write_external_storage_result == PackageManager.PERMISSION_GRANTED &&
                record_audio_result == PackageManager.PERMISSION_GRANTED;
    }


    private void requestPermission() {
        ActivityCompat.requestPermissions(this, new String[]{
                Manifest.permission.WRITE_EXTERNAL_STORAGE,
                Manifest.permission.RECORD_AUDIO,
                Manifest.permission.INTERNET,
                Manifest.permission.READ_EXTERNAL_STORAGE

        }, REQUEST_PERMISSION_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case REQUEST_PERMISSION_CODE: {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
                else
                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
            }
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Toast.makeText(this, data.getStringExtra("nickname"), Toast.LENGTH_SHORT).show();
    }
}

