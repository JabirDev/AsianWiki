package com.jemberdeveloper.asianwikitutorial.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jemberdeveloper.asianwikitutorial.R;
import com.jemberdeveloper.asianwikitutorial.model.UpcomingModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static android.content.ContentValues.TAG;

public class UpcomingMoviesAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<UpcomingModel> items;
    public int number = 1;

    public UpcomingMoviesAdapter(Context context, List<UpcomingModel> items) {
        this.context = context;
        this.items = items;
    }

    class MovieHolder extends RecyclerView.ViewHolder {
        ImageView thumbnail;
        TextView title;
        MovieHolder(@NonNull View itemView) {
            super(itemView);
            thumbnail = itemView.findViewById(R.id.thumbnail);
            title = itemView.findViewById(R.id.tv_title);
        }

        void setData(UpcomingModel data) {
            title.setText(data.getTitle());
            Picasso.get()
                    .load(data.getThumbnail())
                    .placeholder(R.drawable.placeholder_asianwiki)
                    .fit()
                    .centerCrop()
                    .into(thumbnail, new Callback() {
                        @Override
                        public void onSuccess() {
                            Log.d(TAG, "onSuccess: image loaded");
                        }

                        @Override
                        public void onError(Exception e) {
                            Log.e(TAG, "onError: ", e);
                        }
                    });
        }
    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_upcoming, parent, false);
        return new MovieHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        MovieHolder mh = (MovieHolder) holder;
        mh.setData(items.get(position));
    }

    @Override
    public int getItemCount() {
        if (number * 10 > items.size()){
            return items.size();
        } else {
            return number * 10;
        }
    }
}
