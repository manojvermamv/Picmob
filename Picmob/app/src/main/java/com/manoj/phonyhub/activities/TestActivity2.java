package com.manoj.phonyhub.activities;

import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.view.View;
import android.widget.Button;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.manoj.phonyhub.R;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Timer;
import java.util.TimerTask;

public class TestActivity2 extends AppCompatActivity implements View.OnClickListener {

    SeekBar seekBar;
    MediaPlayer player;
    TextView music_position, music_duration;
    Button btn_start, btn_stop;
    private volatile boolean stopThread = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_activity2);

        seekBar = findViewById(R.id.test_seekbar);
        btn_start = findViewById(R.id.btn_start_thread);
        btn_stop = findViewById(R.id.btn_stop_thread);
        music_position = findViewById(R.id.test_music_playing);
        music_duration = findViewById(R.id.test_music_duration);

        player = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI);
        player.setLooping(true);

        btn_start.setOnClickListener(this);
        btn_stop.setOnClickListener(this);

        Handler handler = new Handler();

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                if (player.isPlaying()) {
                    int currentPosition = player.getCurrentPosition() / 1000;
                    seekBar.setMax(player.getDuration() / 1000);
                    seekBar.setProgress(player.getCurrentPosition());
                    music_duration.setText(convertMusicDuration(player.getDuration()));
                    music_position.setText(convertMusicDuration(player.getCurrentPosition()));
                }
                //handler.postDelayed(this, 1000);
            }
        }, 0, 1000);

    }

    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (view == btn_start) {
            //startThread();
            player.start();
            //startService(new Intent(this, TestService.class));
        } else if (view == btn_stop) {
            //stopThread();
            stopMusic();
            //stopService(new Intent(this, TestService.class));
        }
    }

    private void startThread() {
        stopThread = false;
        //MyRunnable runnable = new MyRunnable();
        //new Thread(runnable).start();
        new MyThread(10).start();
    }

    private void stopThread() {
        stopThread = true;
    }

    String convertMusicDuration(int duration) {
        int hour = (duration / 3600000);
        int min = (duration / 60000) % 60000;
        int sec = duration % 60000;

        NumberFormat formatter = new DecimalFormat("00");
        String mintues = formatter.format(min);
        String seconds = formatter.format(sec);
        return mintues + ":" + sec;
    }

    public void stopMusic() {
        if (player.isPlaying()) {
            player.stop();
        }
        seekBar.setProgress(0);
        music_position.setText(R.string.music_reset_position);
        music_duration.setText(R.string.music_reset_position);
    }

    class MyThread extends Thread {
        int seconds;

        MyThread(int seconds) {
            this.seconds = seconds;
        }

        @Override
        public void run() {
            if (stopThread) {
                seekBar.setProgress(0);
                return;
            }
            for (int i = 0; i < seconds; i++) {
                int finalI = i;
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        int percentage = (finalI * 100) / seconds;
                        seekBar.setProgress(percentage);
                    }
                });
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    class MyRunnable implements Runnable {
        @Override
        public void run() {
        }
    }

}