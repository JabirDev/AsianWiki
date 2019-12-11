package com.jemberdeveloper.asianwikitutorial.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jemberdeveloper.asianwikitutorial.R;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class TrailerAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<String> urlThumbnail;
    private List<String> urlVideo;
    private List<String> titles;
    private ChooseVideListener itemClick;

    public TrailerAdapter(Context context, List<String> urlThumbnail, List<String> urlVideo, List<String> titles) {
        this.context = context;
        this.urlThumbnail = urlThumbnail;
        this.urlVideo = urlVideo;
        this.titles = titles;
        try {
            itemClick = (ChooseVideListener) context;
        }
        catch (ClassCastException e){
            throw new ClassCastException(context.toString() +
                    " must implement ChooseVideoListener");
        }
    }

    class TrailerHolder extends RecyclerView.ViewHolder {

        ImageView thumbnails;
        TextView tvTitle;

        TrailerHolder(@NonNull View itemView) {
            super(itemView);
            thumbnails = itemView.findViewById(R.id.thumbnail);
            tvTitle = itemView.findViewById(R.id.tv_title);
        }

        void setData(String thumbnail, final String video, String title) {
            tvTitle.setText(title);
            Picasso.get()
                    .load(thumbnail)
                    .placeholder(R.drawable.placeholder_asianwiki)
                    .fit()
                    .centerCrop()
                    .into(thumbnails);
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    itemClick.onItemClick(video);
                }
            });
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_trailer, parent, false);
        return new TrailerHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        TrailerHolder th = (TrailerHolder) holder;
        th.setData(urlThumbnail.get(position),urlVideo.get(position), titles.get(position));
    }

    @Override
    public int getItemCount() {
        return urlVideo.size();
    }

    public interface ChooseVideListener {
        void onItemClick(String url);
    }

}
