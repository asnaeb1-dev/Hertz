package com.abhigyan.user.hertzmusicplayer.Activities;

import android.database.Cursor;
import android.database.DatabaseUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.abhigyan.user.hertzmusicplayer.Databases.Mood1DB;
import com.abhigyan.user.hertzmusicplayer.Databases.Mood2DB;
import com.abhigyan.user.hertzmusicplayer.Databases.Mood3DB;
import com.abhigyan.user.hertzmusicplayer.Databases.Mood4DB;
import com.abhigyan.user.hertzmusicplayer.Databases.Mood5DB;
import com.abhigyan.user.hertzmusicplayer.R;
import com.abhigyan.user.hertzmusicplayer.RecyclerViewAdapters.MoodEngineRVAdapter;
import com.abhigyan.user.hertzmusicplayer.Utility.MemoryAccess;
import com.davidecirillo.multichoicerecyclerview.MultiChoiceAdapter;
import com.github.clans.fab.FloatingActionButton;
import com.github.clans.fab.FloatingActionMenu;

import org.json.JSONObject;

import java.util.ArrayList;

public class MoodEngineActivity extends AppCompatActivity {

    private RecyclerView moodEngineRV;
    private MoodEngineRVAdapter moodEngineRVAdapter;
    private MemoryAccess memoryAccess = new MemoryAccess(this);
    private Toolbar moodEngineToolBar;
    private Mood1DB mood1DB;
    private Mood2DB mood2DB;
    private Mood3DB mood3DB;
    private Mood4DB mood4DB;
    private Mood5DB mood5DB;

    private ArrayList<String> trackNameAL = new ArrayList<>(),
                                pathAL = new ArrayList<>(),
                                albumNameAl = new ArrayList<>(),
                                artistNameAL = new ArrayList<>(),
                                composerNameAL = new ArrayList<>(),
                                durationAL = new ArrayList<>(),
                                sizeAL = new ArrayList<>();

    private ArrayList<Long> albumIDAL = new ArrayList<>();
    private FloatingActionMenu fabMenu;
    private int itemsel = 0;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mood_engine);

        findAllIDs();

        fabMenu.animate().translationX(1000f);
        moodEngineToolBar.setTitle(getResources().getString(R.string.mood_select_title));
        moodEngineToolBar.setTitleTextColor(getResources().getColor(android.R.color.white));
        memoryAccess.accessMemoryForSongs();
        MoodEngineRVAdapter moodEngineRVAdapter = new MoodEngineRVAdapter(this,memoryAccess.getTrackNameAL(), memoryAccess.getAlbumNameAL(), memoryAccess.getAlbumIDAL());
        moodEngineRV.setAdapter(moodEngineRVAdapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        moodEngineRV.setLayoutManager(linearLayoutManager);

        moodEngineRVAdapter.setMultiChoiceSelectionListener(new MultiChoiceAdapter.Listener() {
            @Override
            public void OnItemSelected(int selectedPosition, int itemSelectedCount, int allItemCount) {

                if(itemsel == 0)
                {
                    fabMenu.animate().translationXBy(-1000f).setDuration(500);
                    itemsel = 1;
                }
                albumIDAL.add(memoryAccess.getAlbumIDAL().get(selectedPosition));
                trackNameAL.add(memoryAccess.getTrackNameAL().get(selectedPosition));
                pathAL.add(memoryAccess.getSongDataAL().get(selectedPosition));
                albumNameAl.add(memoryAccess.getAlbumNameAL().get(selectedPosition));
                artistNameAL.add(memoryAccess.getSingerNameAL().get(selectedPosition));
                composerNameAL.add(memoryAccess.getComposerNameAL().get(selectedPosition));
                durationAL.add(memoryAccess.getTimeListAL().get(selectedPosition));
                sizeAL.add(memoryAccess.getSizeOfSongListAL().get(selectedPosition));
            }

            @Override
            public void OnItemDeselected(int deselectedPosition, int itemSelectedCount, int allItemCount) {

                if(itemSelectedCount == 0)
                {
                    itemsel = 0;
                    fabMenu.animate().translationXBy(1000f).setDuration(500);
                }
            }

            @Override
            public void OnSelectAll(int itemSelectedCount, int allItemCount) {

            }

            @Override
            public void OnDeselectAll(int itemSelectedCount, int allItemCount) {

            }
        });
    }

    private void findAllIDs()
    {
        moodEngineRV = findViewById(R.id.moodEngineRecyclerView);
        moodEngineToolBar = findViewById(R.id.moodEngineToolBar);
        fabMenu = findViewById(R.id.menuFAB);
    }
    public void addSongsToCry(View view)
    {
        mood1DB = new Mood1DB(this);
        for(int i = 0;i<trackNameAL.size();i++)
        {
            mood1DB.insertData(String.valueOf(albumIDAL.get(i)),
                    trackNameAL.get(i),
                    pathAL.get(i),
                    albumNameAl.get(i),
                    artistNameAL.get(i),
                    composerNameAL.get(i),
                    durationAL.get(i),
                    sizeAL.get(i));
        }
        clearAllArrayLists();
        moodEngineRVAdapter.deselectAll();
    }
    public void addSongsToSad(View view)
    {
        //add audio song to sad database
        mood2DB = new Mood2DB(this);
        for(int i = 0;i<trackNameAL.size();i++)
        {
            mood2DB.insertData(String.valueOf(albumIDAL.get(i)),
                    trackNameAL.get(i),
                    pathAL.get(i),
                    albumNameAl.get(i),
                    artistNameAL.get(i),
                    composerNameAL.get(i),
                    durationAL.get(i),
                    sizeAL.get(i));
        }
        clearAllArrayLists();
        moodEngineRVAdapter.deselectAll();
    }
    public void addSongsToMeh(View view)
    {
        //add audio song to meh database
        mood3DB = new Mood3DB(this);
        for(int i = 0;i<trackNameAL.size();i++)
        {
            mood3DB.insertData(String.valueOf(albumIDAL.get(i)),
                    trackNameAL.get(i),
                    pathAL.get(i),
                    albumNameAl.get(i),
                    artistNameAL.get(i),
                    composerNameAL.get(i),
                    durationAL.get(i),
                    sizeAL.get(i));
        }
        clearAllArrayLists();
        moodEngineRVAdapter.deselectAll();
    }
    public void addSongsToSmile(View view)
    {
        //add audio song to smile database
        mood4DB = new Mood4DB(this);
        for(int i = 0;i<trackNameAL.size();i++)
        {
            mood4DB.insertData(String.valueOf(albumIDAL.get(i)),
                    trackNameAL.get(i),
                    pathAL.get(i),
                    albumNameAl.get(i),
                    artistNameAL.get(i),
                    composerNameAL.get(i),
                    durationAL.get(i),
                    sizeAL.get(i));
        }
        clearAllArrayLists();
        moodEngineRVAdapter.deselectAll();

    }
    public void addSongsToHappy(View view)
    {
        //add audio song to happy database
        mood5DB = new Mood5DB(this);
        for(int i = 0;i<trackNameAL.size();i++)
        {
            mood5DB.insertData(String.valueOf(albumIDAL.get(i)),
                    trackNameAL.get(i),
                    pathAL.get(i),
                    albumNameAl.get(i),
                    artistNameAL.get(i),
                    composerNameAL.get(i),
                    durationAL.get(i),
                    sizeAL.get(i));
        }
        clearAllArrayLists();
        moodEngineRVAdapter.deselectAll();
    }

    private void clearAllArrayLists()
    {
        albumIDAL.clear();
        trackNameAL.clear();
        pathAL.clear();
        albumNameAl.clear();
        artistNameAL.clear();
        composerNameAL.clear();
        durationAL.clear();
        sizeAL.clear();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        System.gc();
        finish();
    }
}

/*

 private void seeData() {

        Cursor cur = mood1DB.getAllData();
        if(cur!=null)
        {
            if(cur.moveToFirst())
            {
                do{
                    Log.i("INFO***CHECK*****",
                            cur.getString(1)+" "
                                    +cur.getString(2)+" "
                                    +cur.getString(3)+" "
                                    +cur.getString(4)+" "
                                    +cur.getString(5)+" "
                                    +cur.getString(6)+" "
                                    +cur.getString(7)+" "
                                    +cur.getString(8)
                    );
                }while(cur.moveToNext());
            }
        }


 */