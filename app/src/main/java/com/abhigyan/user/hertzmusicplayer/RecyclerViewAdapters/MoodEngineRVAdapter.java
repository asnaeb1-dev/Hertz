package com.abhigyan.user.hertzmusicplayer.RecyclerViewAdapters;

import android.content.ContentUris;
import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.abhigyan.user.hertzmusicplayer.R;
import com.abhigyan.user.hertzmusicplayer.Utility.ProcessorTool;
import com.bumptech.glide.Glide;
import com.davidecirillo.multichoicerecyclerview.MultiChoiceAdapter;
import java.util.ArrayList;
import de.hdodenhof.circleimageview.CircleImageView;

public class MoodEngineRVAdapter extends MultiChoiceAdapter<MoodEngineRVAdapter.Viewholder> {

    private Context context;
    private ArrayList<String> trackNameAL,
                            albumNameAL;

    private ArrayList<Long> albumidAL;

    public MoodEngineRVAdapter(Context context, ArrayList<String> trackNameAL, ArrayList<String> albumNameAL, ArrayList<Long> albumidAL) {
        this.context = context;
        this.trackNameAL = trackNameAL;
        this.albumNameAL = albumNameAL;
        this.albumidAL = albumidAL;
    }

    @Override
    public void onBindViewHolder(Viewholder holder, int position) {
        super.onBindViewHolder(holder, position);

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

            Uri uri = ContentUris.withAppendedId(sArtworkUri, albumidAL.get(position));
            Glide.with(context).asBitmap().load(uri).into(holder.coverPics);
        }
        catch (Exception e)
        {
            e.printStackTrace();
            holder.coverPics.setImageResource(R.drawable.albumpic);
        }

    }

    @Override
    protected View.OnClickListener defaultItemViewClickListener(Viewholder holder, int position) {


        return super.defaultItemViewClickListener(holder, position);
    }

    @NonNull
    @Override
    public MoodEngineRVAdapter.Viewholder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

        View view = LayoutInflater.from(context).inflate(R.layout.song_list_unit_ui, viewGroup, false);
        //create the object of the Viewholder class down below
        MoodEngineRVAdapter.Viewholder viewholder = new MoodEngineRVAdapter.Viewholder(view);
        return viewholder;
    }

    @Override
    public int getItemCount() {
        return trackNameAL.size();
    }



    public class Viewholder extends RecyclerView.ViewHolder
    {
        CircleImageView optionCIV, coverPics;
        TextView trackNameTV, albumNameTV;
        LinearLayout linearLayoutSongList;

        public Viewholder(@NonNull View itemView) {
            super(itemView);

            linearLayoutSongList = itemView.findViewById(R.id.songListLL);
            trackNameTV = itemView.findViewById(R.id.songNameTV);
            albumNameTV = itemView.findViewById(R.id.albumNameTV);

            coverPics = itemView.findViewById(R.id.songListCIV);
            optionCIV = itemView.findViewById(R.id.optionsCIV);
        }
    }
}
