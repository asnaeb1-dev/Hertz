package com.abhigyan.user.hertzmusicplayer.Activities;

import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.abhigyan.user.hertzmusicplayer.DailogBoxes.MoodSelectorDailog;
import com.abhigyan.user.hertzmusicplayer.Fragments.AlbumFragment;
import com.abhigyan.user.hertzmusicplayer.Fragments.ArtistFragment;
import com.abhigyan.user.hertzmusicplayer.Fragments.FavouritesFragment;
import com.abhigyan.user.hertzmusicplayer.Fragments.OnlineFragment;
import com.abhigyan.user.hertzmusicplayer.Fragments.PlaylistFragment;
import com.abhigyan.user.hertzmusicplayer.Fragments.SongListFragment;
import com.abhigyan.user.hertzmusicplayer.R;
import com.abhigyan.user.hertzmusicplayer.Services.MusicService;
import com.abhigyan.user.hertzmusicplayer.Utility.MemoryAccess;
import com.abhigyan.user.hertzmusicplayer.ViewPager.DepthTransformation;
import com.gauravk.audiovisualizer.visualizer.BarVisualizer;
import com.valdesekamdem.library.mdtoast.MDToast;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private SectionsPagerAdapter mSectionsPagerAdapter;
    private DrawerLayout drawer;
    private NavigationView navigationView;
    private ViewPager centralViewPager;
    private TabLayout tabLayout;
    private Toolbar toolbar;
    private int presentMood;
    private int audioSession;
    private boolean audioIsPlaying = false;

    private ServiceConnection serviceConnection;
    private BroadcastReceiver broadcastReceiver1;
    private MusicService musicService;
    private boolean doubleBackToExitPressedOnce = false;
    private BarVisualizer barVisualizer1;
    private boolean serviceBounded = false;
    private MemoryAccess memoryAccess = new MemoryAccess(this);

    private void bindUIwithService()
    {
        Intent binderIntent = new Intent(getApplicationContext(), MusicService.class);
        bindService(binderIntent,serviceConnection,Context.BIND_AUTO_CREATE);
        startService(binderIntent);
        audioIsPlaying = true;
    }

    private void getAllBroadcasts()
    {
        broadcastReceiver1 = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                audioSession = intent.getIntExtra("audioSession", -1);
                if(audioSession!=-1)
                {
                    barVisualizer1.setAudioSessionId(audioSession);
                }
                barVisualizer1.setAudioSessionId(audioSession);
            }
        };

        registerReceiver(broadcastReceiver1,new IntentFilter("abc"));
    }

    private void initializeServiceConnection()
    {
        serviceConnection = new ServiceConnection() {
            @Override
            public void onServiceConnected(ComponentName componentName, IBinder iBinder) {

                MusicService.MusicBinder binder = (MusicService.MusicBinder) iBinder;
                musicService = binder.getMusicService();
                audioIsPlaying = musicService.songIsPlaying();
                serviceBounded = true;
            }

            @Override
            public void onServiceDisconnected(ComponentName componentName) {
                serviceBounded = false;
            }
        };

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        findAllUIS();
        setSupportActionBar(toolbar);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        if(audioIsPlaying == false) {
            initializeServiceConnection();
            bindUIwithService();
            getAllBroadcasts();

        }
        navigationView.setNavigationItemSelectedListener(this);

        DepthTransformation depthTransformation = new DepthTransformation();
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());
        centralViewPager.setAdapter(mSectionsPagerAdapter);
        centralViewPager.setPageTransformer(true, depthTransformation);
        centralViewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.addOnTabSelectedListener(new TabLayout.ViewPagerOnTabSelectedListener(centralViewPager));
        tabLayout.setTabMode(TabLayout.MODE_SCROLLABLE);
    }

    private void findAllUIS() {
        toolbar = findViewById(R.id.mainToolbar);
        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        centralViewPager = findViewById(R.id.containerVP);
        tabLayout = findViewById(R.id.tabs);
        barVisualizer1 = findViewById(R.id.barVisualizer1);
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            String message= "Please click BACK again to exit";
            if (doubleBackToExitPressedOnce) {
                super.onBackPressed();
                return;
            }

            this.doubleBackToExitPressedOnce = true;
            MDToast mdToast = MDToast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT, MDToast.TYPE_INFO);
            mdToast.show();
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce=false;
                }
            }, 2000);
        }

    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.favs) {
            Toast.makeText(this, "favourites", Toast.LENGTH_SHORT).show();
            centralViewPager.setCurrentItem(3, true);

        } else if (id == R.id.playlists) {
            Toast.makeText(this, "playlists", Toast.LENGTH_SHORT).show();
            centralViewPager.setCurrentItem(4, true);
        } else if (id == R.id.equalizer) {
            Toast.makeText(this, "equalizer", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.moodSelector) {

            MoodSelectorDailog moodSelectorDailog = new MoodSelectorDailog(this);
            presentMood = moodSelectorDailog.generateMoodSelectorDailog();
            Log.i("present mood", String.valueOf(presentMood));
        } else if (id == R.id.moodSetter) {

            Intent intent = new Intent(this, MoodEngineActivity.class);
            startActivity(intent);

        } else if (id == R.id.login) {
            Toast.makeText(this, "login", Toast.LENGTH_SHORT).show();

        }else if (id == R.id.signup) {
            Toast.makeText(this, "signup", Toast.LENGTH_SHORT).show();

        }else if (id == R.id.settings) {
            Toast.makeText(this, "settings", Toast.LENGTH_SHORT).show();

        }else if (id == R.id.about) {
            Toast.makeText(this, "about", Toast.LENGTH_SHORT).show();

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {

            switch (position)
            {
                case 0:
                    //this case will take the user to the songs fragment
                    return new SongListFragment();

                case 1:
                    return new AlbumFragment();

                    case 2:
                        return new ArtistFragment();

                case 3:
                    return new FavouritesFragment();

                case 4:
                    return new PlaylistFragment();

                case 5:
                    return new OnlineFragment();

                default:
                    return null;
            }
        }

        @Override
        public int getCount() {
            // Show 5 total pages.
            return 6;
        }

        @Nullable
        @Override
        public CharSequence getPageTitle(int position) {

            switch (position) {
                case 0:
                    return "SONGS";
            }

            return super.getPageTitle(position);
        }
    }
}
