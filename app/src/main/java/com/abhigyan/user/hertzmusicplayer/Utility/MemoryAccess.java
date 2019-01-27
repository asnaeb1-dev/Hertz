package com.abhigyan.user.hertzmusicplayer.Utility;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.provider.MediaStore;

import java.util.ArrayList;

public class MemoryAccess {

    private String[] projection ={"*"};
    private Context context;
    private int audioSession;

    public int getAudioSession() {
        return audioSession;
    }

    public void setAudioSession(int audioSession) {
        this.audioSession = audioSession;
    }
    //  private ArrayList<Bitmap> coverPics = new ArrayList<>();

    private ArrayList<String>   trackNameAL = new ArrayList<>(),
                                albumNameAL = new ArrayList<>(),
                                singerNameAL = new ArrayList<>(),
                                composerNameAL = new ArrayList<>(),
                                sizeOfSongListAL = new ArrayList<>(),
                                timeListAL = new ArrayList<>(),
                                songDataAL = new ArrayList<>();//main

    private ArrayList<Long> albumIDAL = new ArrayList<>();

    public MemoryAccess(Context context)
    {
        this.context = context;
    }

    //accesses the main memory for songs and all its details
    public void accessMemoryForSongs()
    {
       // ProcessorTool processorTool = new ProcessorTool(context);
        Cursor detailsCursor = context.getContentResolver().query(MediaStore.Audio.Media.EXTERNAL_CONTENT_URI,projection,null,null,null);
        {
            if(detailsCursor!=null)
            {
                if(detailsCursor.moveToFirst())
                {
                    do{
                        long albumId = detailsCursor.getLong(detailsCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM_ID));

                        String trackName = detailsCursor.getString(detailsCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DISPLAY_NAME));
                        String albumName = detailsCursor.getString(detailsCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ALBUM));
                        String artistName = detailsCursor.getString(detailsCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.ARTIST));
                        String composerName = detailsCursor.getString(detailsCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.COMPOSER));

                        String songLocation = detailsCursor.getString(detailsCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DATA));
                        String duration = detailsCursor.getString(detailsCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.DURATION));
                        String size  = detailsCursor.getString(detailsCursor.getColumnIndexOrThrow(MediaStore.Audio.Media.SIZE));//main

                        trackNameAL.add(trackName);
                        albumNameAL.add(albumName);
                        singerNameAL.add(artistName);
                        composerNameAL.add(composerName);
                        songDataAL.add(songLocation);
                        timeListAL.add(duration);
                        sizeOfSongListAL.add(size);
                        albumIDAL.add(albumId);

                       // coverPics.add(processorTool.getImage(albumId));
                    }while(detailsCursor.moveToNext());
                }
            }
        }

       // setCoverPics(coverPics);
        setAlbumNameAL(albumNameAL);
        setAlbumIDAL(albumIDAL);
        setComposerNameAL(composerNameAL);
        setSizeOfSongListAL(sizeOfSongListAL);
        setTrackNameAL(trackNameAL);
        setSingerNameAL(singerNameAL);
        setTimeListAL(timeListAL);
        setSongDataAL(songDataAL);
    }


   // public ArrayList<Bitmap> getCoverPics() {
  //      return coverPics;
  //  }

  //  private void setCoverPics(ArrayList<Bitmap> coverPics) {
     //   this.coverPics = coverPics;
   // }

    public ArrayList<String> getTrackNameAL() {
        return trackNameAL;
    }

    private void setTrackNameAL(ArrayList<String> trackNameAL) {
        this.trackNameAL = trackNameAL;
    }

    public ArrayList<String> getAlbumNameAL() {
        return albumNameAL;
    }

    private void setAlbumNameAL(ArrayList<String> albumNameAL) {
        this.albumNameAL = albumNameAL;
    }

    public ArrayList<String> getSingerNameAL() {
        return singerNameAL;
    }

    private void setSingerNameAL(ArrayList<String> singerNameAL) {
        this.singerNameAL = singerNameAL;
    }

    public ArrayList<String> getComposerNameAL() {
        return composerNameAL;
    }

    private void setComposerNameAL(ArrayList<String> composerNameAL) {
        this.composerNameAL = composerNameAL;
    }

    public ArrayList<String> getSizeOfSongListAL() {
        return sizeOfSongListAL;
    }

    private void setSizeOfSongListAL(ArrayList<String> sizeOfSongListAL) {
        this.sizeOfSongListAL = sizeOfSongListAL;
    }

    public ArrayList<String> getTimeListAL() {
        return timeListAL;
    }

    private void setTimeListAL(ArrayList<String> timeListAL) {
        this.timeListAL = timeListAL;
    }

    public ArrayList<String> getSongDataAL() {
        return songDataAL;
    }

    private void setSongDataAL(ArrayList<String> songDataAL) {
        this.songDataAL = songDataAL;
    }

    public ArrayList<Long> getAlbumIDAL() {
        return albumIDAL;
    }

    private void setAlbumIDAL(ArrayList<Long> albumIDAL) {
        this.albumIDAL = albumIDAL;
    }

}
