package com.manoj.phonyhub.test;

import android.app.Service;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.IBinder;
import android.provider.Settings;

import androidx.annotation.Nullable;

public class TestService extends Service {

    // declaring object of MediaPlayer
    private MediaPlayer player;

    // execution of service will start
    // on calling this method
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        // creating a media player which, will play the Default ringtone audio in android device
        player = MediaPlayer.create(this, Settings.System.DEFAULT_RINGTONE_URI);

        // playing the audio on loop
        player.setLooping(true);

        // starting the process
        player.start();

        // returns the status of the program
        return START_STICKY;
    }

    // execution of the service will
    // stop on calling this method
    @Override
    public void onDestroy() {
        super.onDestroy();

        // stoping the process
        player.stop();

    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }
}
