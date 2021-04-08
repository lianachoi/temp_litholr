package com.litholr.chord;

import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;

public class ListActivity extends AppCompatActivity {

    ArrayList<String> arrayList;
    ListView listView;
    ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);


//        ContentResolver contentResolver = getContentResolver();
//        String[] mediaStoreAudio = {
//                MediaStore.Audio.Media.IS_MUSIC,
//                MediaStore.Audio.Media.IS_ALARM,
//                MediaStore.Audio.Media.TITLE,
//                MediaStore.Audio.Media.ARTIST,
//                MediaStore.Audio.Media.DURATION,
//                MediaStore.Audio.Media._ID,
//        };
//
//        Cursor cursor = contentResolver.query(
//                MediaStore.Audio.Media.INTERNAL_CONTENT_URI,mediaStoreAudio,
//                null,null,
//                MediaStore.Audio.Media.TITLE+" ASC"
//        );
        doStuff();
    }

    public void getMusic(){
        ContentResolver contentResolver = getContentResolver();
        Uri musicUri = MediaStore.Audio.Media.EXTERNAL_CONTENT_URI;
//        //Uri uri = MediaStore.Audio.Media.getContentUriForPath(getFilesDir() + "/");
//        String filePath = getFilesDir()+"/";
//
//        Toast.makeText(ListActivity.this,"getFileDir :: "+ filePath , Toast.LENGTH_SHORT).show();
//
//        Cursor pathCursor = getContentResolver().query( MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,
//                null, "_data = '" + filePath + "'",
//                null, null );
//
//        int id = pathCursor.getInt(pathCursor.getColumnIndex("_id"));

//        Uri uri = ContentUris.withAppendedId(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI, id);
        Cursor cursor = contentResolver.query(
                musicUri,null,
                null,null,
                MediaStore.Audio.Media.TITLE+" ASC"
        );

        if (cursor != null && cursor.moveToFirst()){
            int songTitle = cursor.getColumnIndex(MediaStore.Audio.Media.TITLE);
            int songArtist = cursor.getColumnIndex(MediaStore.Audio.Media.ARTIST);
            int songLocation = cursor.getColumnIndex(MediaStore.Audio.Media.DATA);
            
            do {
                String currentTitle = cursor.getString(songTitle);
                String currentArtist = cursor.getString(songArtist);
                String currentLocation = cursor.getString(songLocation);
                arrayList.add(currentTitle
                        + "\n" + currentArtist
                        + "\n" + currentLocation);
            } while (cursor.moveToNext());
        }
    }

    public void doStuff() {
        listView = (ListView) findViewById(R.id.listView);
        arrayList = new ArrayList<>();
        getMusic();
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, arrayList);
        listView.setAdapter(adapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

            }
        });
    }
}