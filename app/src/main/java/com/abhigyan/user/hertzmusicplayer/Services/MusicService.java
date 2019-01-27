package com.abhigyan.user.hertzmusicplayer.Services;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.abhigyan.user.hertzmusicplayer.Activities.NavigationMusicActivity;

import java.io.IOException;
import java.util.ArrayList;

/**
 * This class is a service class that will play the audio in the background and also bind the UI of the player class
 * with the with the background play
 */

public class MusicService extends Service implements MediaPlayer.OnCompletionListener, MediaPlayer.OnPreparedListener, MediaPlayer.OnErrorListener {

    private MediaPlayer mediaPlayer;
    private IBinder iBinder = new MusicBinder();
    private ArrayList<String> songData;
    private int songPosition =0;
    private int audioSessionID;



    public MusicService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        return iBinder;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mediaPlayer = new MediaPlayer();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //get an intent from player class which will play the song
        songData = intent.getStringArrayListExtra("songlist");
        songPosition = intent.getIntExtra("songPos", 0);
        initalizeMediaPlayer(songPosition);
        return START_STICKY;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mediaPlayer.stop();
        mediaPlayer.release();
    }
//##########################MEDIA PLAYER LISTENERS#############################################
    @Override
    public void onCompletion(MediaPlayer mediaPlayer) {

        playNextMedia();

    }

    @Override
    public boolean onError(MediaPlayer mediaPlayer, int i, int i1) {
        return false;
    }

    @Override
    public void onPrepared(MediaPlayer mediaPlayer) {

        mediaPlayer.start();

    }
//#############################################################################################

    //########################BROADCASTS############################
    private void sendPositionBroadcast(int position)
    {
        Intent positionIntent = new Intent("abc");
        positionIntent.putExtra("position", position);
        positionIntent.putExtra("audioSession", mediaPlayer.getAudioSessionId());
        Log.i("pos#####1", String.valueOf(position));
        sendBroadcast(positionIntent);
    }

    private void sendDurationBroadcast(int duration)
    {
        Intent lengthIntent = new Intent("xyz");
        lengthIntent.putExtra("length", duration);
        Log.i("dur#####1", String.valueOf(duration));
        sendBroadcast(lengthIntent);
    }
//#########################MEDIA PLAYER METHODS#################################################

    private void initalizeMediaPlayer(int position)
    {
        //reset previous media player
        mediaPlayer.reset();
        try {//set a data source
            mediaPlayer.setDataSource(songData.get(position));
        } catch (IOException e) {
            e.printStackTrace();
            }
            //prepare media player asynchronously so that mp doesnt block up main thread
        mediaPlayer.prepareAsync();
        mediaPlayer.setOnPreparedListener(this);
        mediaPlayer.setOnCompletionListener(this);
        mediaPlayer.setOnErrorListener(this);
        sendPositionBroadcast(position);
        setAudioSessionID(mediaPlayer.getAudioSessionId());

    }

    public int getAudioSessionID()
    {
        if(mediaPlayer.isPlaying()) {
            return mediaPlayer.getAudioSessionId();
        }
        return -1;
    }

    public void setAudioSessionID(int audioSessionID) {
        this.audioSessionID = audioSessionID;
    }

    public void playMedia()
    {
        mediaPlayer.start();
    }

    public void pauseMedia()
    {
        mediaPlayer.pause();
    }

    public void stopAudioPlayback()
    {
        mediaPlayer.stop();
    }

    public void playNextMedia()
    {
        mediaPlayer.stop();
        mediaPlayer.reset();
        if(songPosition<=songData.size()) {
            songPosition++;
        }
        else
        {
            songPosition = 0;
        }
        initalizeMediaPlayer(songPosition);
    }

    public void loopSong()
    {
        if(mediaPlayer.isLooping() ==  false)
        {
            mediaPlayer.setLooping(true);
        }
        else
        {
            mediaPlayer.setLooping(false);
        }
    }

    public void playPreviousMedia()
    {
        mediaPlayer.stop();

        if(songPosition>=0) {
            songPosition--;
        }
        else
        {
            songPosition = songData.size();
        }
        initalizeMediaPlayer(songPosition);
    }

    public boolean songIsPlaying()
    {
        if(mediaPlayer.isPlaying() ==  true)
        {
            return true;
        }
        return false;
    }

    public boolean songIsLooping()
    {
        if(mediaPlayer.isLooping() == true)
        {
            return true;
        }
        return false;
    }

    public class MusicBinder extends Binder
    {
        public MusicService getMusicService()
        {
            return MusicService.this;
        }
    }
}
