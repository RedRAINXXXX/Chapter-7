package com.bytedance.videoplayer;

import android.content.Intent;
import android.content.res.Configuration;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.VideoView;

public class MainActivity extends AppCompatActivity {
    private VideoView mvideo;
    private Button play;
    private Button pause;
    private Button chooseVideo;
    private String videoUrl = "";
    private SeekBar mseekbar;
    private Uri mVideoUri;
    private static final int PICK_VIDEO = 1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Uri uri = Uri.parse("android.resource://" + getPackageName()+'/'+R.raw.sss);
        mvideo = findViewById(R.id.videoView);
        mvideo.setVideoURI(uri);
        play = findViewById(R.id.play);
        pause = findViewById(R.id.pause);
        chooseVideo = findViewById(R.id.setvideo);
        mseekbar = findViewById(R.id.seekBar);

        play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mvideo.start();
                //MainActivity.this.onSaveInstanceState(new Bundle());
            }
        });
        pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mvideo.pause();
            }
        });
        mvideo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mediaPlayer) {
                mediaPlayer.start();
                mediaPlayer.setLooping(true);
            }
        });
        mseekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                if(b) mvideo.seekTo((int)((float)i/100*mvideo.getDuration()));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        chooseVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.setType("video/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent,"Select Video"),PICK_VIDEO);
            }
        });
        new Thread() {
            @Override
            public void run() {
                while (true) {
                    mseekbar.post(new Runnable() {
                        @Override
                        public void run() {
                            mseekbar.setProgress(mvideo.getCurrentPosition() * 100 / mvideo.getDuration());
                        }
                    });
                    try {
                        sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }.start();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK && null != data && requestCode == PICK_VIDEO) {
            mVideoUri = data.getData();
            mvideo.setVideoURI(mVideoUri);
        }
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

}
