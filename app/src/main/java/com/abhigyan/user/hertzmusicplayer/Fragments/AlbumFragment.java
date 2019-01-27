package com.abhigyan.user.hertzmusicplayer.Fragments;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.CardView;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.GridLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.abhigyan.user.hertzmusicplayer.R;
import com.abhigyan.user.hertzmusicplayer.Utility.MemoryAccess;
import com.abhigyan.user.hertzmusicplayer.Utility.ProcessorTool;
import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;

import java.util.ArrayList;


public class AlbumFragment extends Fragment {

    GridLayout albumGridLayout;
    private ArrayList<String> albumNameAL = new ArrayList<>(),
                                artistNameAL = new ArrayList<>();

  //  private ArrayList<Bitmap> coverPicsAL =  new ArrayList<>();
    private ArrayList<Long> albumIDAL = new ArrayList<>();
    private int isCalled =0;
    private MemoryAccess memoryAccess;
    private ProcessorTool processorTool = new ProcessorTool(getContext());

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View myView = inflater.inflate(R.layout.fragment_album, container, false);
        albumGridLayout = myView.findViewById(R.id.albumGridLayout);
        return myView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        if(isCalled == 0) {
            memoryAccess = new MemoryAccess(getContext());
            memoryAccess.accessMemoryForSongs();
            isCalled = 1;
        }

        albumNameAL = memoryAccess.getAlbumNameAL();
        artistNameAL =memoryAccess.getSingerNameAL();
        albumIDAL = memoryAccess.getAlbumIDAL();

        for(int i = 0;i<albumNameAL.size();i++)
        {
            populateLayout(albumNameAL.get(i),albumIDAL.get(i));
        }
    }

    private void populateLayout(final String albumNm, final long albumID )
    {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        ((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        int width = displayMetrics.widthPixels;

        CardView cardView = new CardView(getContext());
        cardView.setRadius(20f);
        cardView.setCardElevation(20f);
        cardView.setBackground(getResources().getDrawable(android.R.drawable.screen_background_dark_transparent));
        LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(width/2,height/3);
        params.setMargins(5, 15, 5, 15);
        cardView.setLayoutParams(params);

        LayoutInflater layoutInflater = (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View myView = layoutInflater.inflate(R.layout.album_content,cardView, true);
        ImageView imageView = myView.findViewById(R.id.albumIMG);
        TextView textView = myView.findViewById(R.id.albumTEXT);
        textView.setText(processorTool.reformatTrackName(albumNm));

        final Uri sArtworkUri = Uri.parse("content://media/external/audio/albumart");
        Uri uri = ContentUris.withAppendedId(sArtworkUri, albumID);
        Glide.with(getContext())
                .asBitmap()
                .load(uri)
                .apply(RequestOptions.placeholderOf(R.drawable.defaultalbumpic).error(R.drawable.defaultalbumpic))
                .into(imageView);

        albumGridLayout.addView(cardView);

        cardView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


            }
        });
    }
}
