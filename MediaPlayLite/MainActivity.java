package com.csin.myapplication;


import androidx.appcompat.app.AppCompatActivity;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class MainActivity extends AppCompatActivity {
    ListView mListview;
    MediaPlayer mediaPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initData();
        addListener();
    }
    private void initData() {mListview = (ListView) findViewById(R.id.listview);}
    private void addListener(){
        mListview.setOnItemClickListener(((parent, view, position, id) -> {
            switch (position){
                case 0:
                    play1();
                    break;
                //  case 1:
                //    play2();
                //    break;
                // case 2:
                //   play3();
                //   break;
            }
        }));

    }
    //play for "raw"的初始化
    private void play1 (){
        mediaPlayer = MediaPlayer.create(this,R.raw.qifengle);
        if (!mediaPlayer.isPlaying()){
            mediaPlayer.start();
        }

    }
    public void release (View view){
        if(mediaPlayer != null){
            mediaPlayer.release();
            mediaPlayer = null;
        }
    }
}