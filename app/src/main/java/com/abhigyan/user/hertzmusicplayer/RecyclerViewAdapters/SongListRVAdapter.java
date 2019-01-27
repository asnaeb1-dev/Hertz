package com.abhigyan.user.hertzmusicplayer.RecyclerViewAdapters;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.abhigyan.user.hertzmusicplayer.Activities.NavigationMusicActivity;
import com.abhigyan.user.hertzmusicplayer.R;
import com.abhigyan.user.hertzmusicplayer.Utility.ProcessorTool;
import com.bumptech.glide.Glide;

import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

public class SongListRVAdapter extends RecyclerView.Adapter<SongListRVAdapter.Viewholder>
{
    private Context context;
    private ArrayList<String>  trackNameAL,
                               albumNameAL;

    private ArrayList<Long> albumIDAL;

    // private ArrayList<Bitmap> coverPicsAL;

    public SongListRVAdapter(Context context, ArrayList<String> trackNameAL, ArrayList<String> albumNameAL, ArrayList<Long>albumIDAL/*ArrayList<Bitmap> coverPicsAL*/) {
        this.context = context;
        this.trackNameAL = trackNameAL;
        this.albumNameAL = albumNameAL;
        this.albumIDAL = albumIDAL;
        //this.coverPicsAL = coverPicsAL;
    }

    @NonNull
    @Override
    public SongListRVAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        //responsible for inflating the view
        View view = LayoutInflater.from(context).inflate(R.layout.song_list_unit_ui, parent, false);
        //create the object of the Viewholder class down below
        SongListRVAdapter.Viewholder viewholder = new SongListRVAdapter.Viewholder(view);
        return viewholder;
    }

    @Override
    public void onBindViewHolder(@NonNull final SongListRVAdapter.Viewholder holder, final int position) {
        //changes wrt to what the layout are and add a new item
        //takes the content and shows it on the imageView

        ProcessorTool processorTool = new ProcessorTool(context);

        if(trackNameAL.get(position)!=null)
        {
            holder.trackNameTV.setText(processorTool.reformatTrackName(trackNameAL.get(position)));
        }
        else
        {
            holder.trackNameTV.setText("Unknown");
        }
        //------------------------------------------------------------------------------------------
        if(albumNameAL.get(position)!=null)
        {
            holder.albumNameTV.setText("Album- "+processorTool.reformatTrackName(albumNameAL.get(position)));
        }
        else
        {
            holder.albumNameTV.setText("Album- Unknown");
        }
        //------------------------------------------------------------------------------------------


        try {
            final Uri sArtworkUri = Uri
                    .parse("content://media/external/audio/albumart");

            Uri uri = ContentUris.withAppendedId(sArtworkUri, albumIDAL.get(position));
            Glide.with(context).asBitmap().load(uri).into(holder.coverPics);
        }
        catch (Exception e)
        {
            Glide.with(context).asBitmap().load(context.getResources().getDrawable(R.drawable.defaultalbumpic)).into(holder.coverPics);
            e.printStackTrace();
        }

        //------------------------------------------------------------------------------------------

        holder.linearLayoutSongList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Activity activity = (Activity) context;
                Intent intent = new Intent(context, NavigationMusicActivity.class);
                intent.putExtra("positionOfPointerOnList", position);
                intent.putExtra("callSource", 1);
                context.startActivity(intent);
                activity.overridePendingTransition(R.anim.slide_out_left,R.anim.slide_in_right);
            }
        });
    }

    @Override
    public int getItemCount() {
        //this sets the size of the recycler view
        //without this the recycler view will show 0 items
        return trackNameAL.size();
    }


    public class Viewholder extends RecyclerView.ViewHolder
    {
        TextView trackNameTV, albumNameTV;
        CircleImageView coverPics, optionCIV;
        LinearLayout linearLayoutSongList;

        public Viewholder(View itemView) {
            super(itemView);

            linearLayoutSongList = itemView.findViewById(R.id.songListLL);
            trackNameTV = itemView.findViewById(R.id.songNameTV);
            albumNameTV = itemView.findViewById(R.id.albumNameTV);

            coverPics = itemView.findViewById(R.id.songListCIV);
            optionCIV = itemView.findViewById(R.id.optionsCIV);
        }
    }
}
