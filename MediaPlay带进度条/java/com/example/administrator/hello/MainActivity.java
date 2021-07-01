package com.example.administrator.hello;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MainActivity extends AppCompatActivity {
    ImageView playIv,pauseIv;
    TextView singerTv,songTv;
    SeekBar seekBar;
    ListView listView;

    private int[] musicnum = {1,2,3,4,5};
    private String[] song = {"东京不太热","狂野想乡","七月上","撒野","唐人"};
    private  String[]  singer ={"艾辰","西瓜JUN","jam","凯瑟喵","五音JW"};
    private int[]  songsid;
    private MediaPlayer mediaPlayer;
    private int status = 0;
    private  double time = 0;
    private Thread thraed;
    private Boolean s = false;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);

            seekBar.setProgress(status);

        }
    };
    private void initView() {

        playIv = findViewById(R.id.play);
        pauseIv = findViewById(R.id.pause);

        singerTv = findViewById(R.id.local_music_bottom_tv_singer);
        songTv = findViewById(R.id.local_music_bottom_tv_song);
        seekBar = findViewById(R.id.sb);
        listView = findViewById(R.id.lv1);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mediaPlayer = new MediaPlayer();
        initView();
        mediaPlayer = MediaPlayer.create(getApplicationContext(),R.raw.saye);
        songsid = new int[]{R.raw.dongjingbutaire, R.raw.kuangyexiangxiang, R.raw.qiyueshang, R.raw.saye, R.raw.tangren};
        try {
            setListViewAdapter();
        } catch (Exception e) {
            Log.i("TAG", "读取歌曲失败");
        }

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                s=true;
                mediaPlayer.seekTo(seekBar.getProgress());
                status = seekBar.getProgress();
                s=false;

            }
        });

        //播放键
        playIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Boolean a;
                if(mediaPlayer.isPlaying()){
                    a=false;
                }else{
                    a = true;
                }
                if (mediaPlayer!=null){
                    try {
                        if(a){
                            mediaPlayer.prepare();
                            mediaPlayer.seekTo(status);
                            mediaPlayer.start();
                            s=false;}
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                }}
        });
        pauseIv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mediaPlayer.isPlaying()){
                    s=true;
                    mediaPlayer.stop();

                }else{
//                    if ()

                }
            }
        });
    }

    //设置列表项
    private void setListViewAdapter() {
        List<Map<String,Object>>  listitems = new ArrayList<>();
        for (int i = 0;i<song.length;i++){
            Map<String,Object>  listitem = new HashMap<>();
            listitem.put("musicnum",musicnum[i]);
            listitem.put("song",song[i]);
            listitem.put("singer",singer[i]);
            listitem.put("songid",songsid[i]);
            listitems.add(listitem);
        }
        SimpleAdapter simpleAdapter =new SimpleAdapter(this,listitems,
                R.layout.item_local_music,new String[]{"musicnum","singer","song"},
                new int[]{R.id.item_local_music_num,R.id.item_local_music_singer,R.id.item_local_music_song});
        listView.setAdapter(simpleAdapter);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                s=true;
                status = 0;
                if (mediaPlayer!=null){
                    mediaPlayer.reset();
                }
                mediaPlayer = MediaPlayer.create(getApplicationContext(), songsid[position]);
                mediaPlayer.start();
                songTv.setText(song[position]);
                singerTv.setText(singer[position]);

                //设置seekbar
                time = mediaPlayer.getDuration();
                int i = new Double(time).intValue();
                seekBar.setMax(i);
                seekBar.setProgress(0);
                thread();
                s=false;
            }
        });

    }

    public void dowork(){
        while (s);
        try{
            int currentPosition=getCurrentPosition();
            status = currentPosition;}
        catch (Exception e){

        }
    }

    public void thread(){
        thraed = new Thread(){
            public void run(){

                while(status<time){
                    dowork();
                    handler.sendEmptyMessage(0x111);

                }
            }
        };
        thraed.start();
    }

    private int getCurrentPosition(){
        if(mediaPlayer!=null){
            return mediaPlayer.getCurrentPosition();
        }        return 0;
    }
}
