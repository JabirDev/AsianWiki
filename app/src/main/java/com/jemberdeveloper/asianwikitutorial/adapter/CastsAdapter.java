package com.jemberdeveloper.asianwikitutorial.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jemberdeveloper.asianwikitutorial.R;
import com.jemberdeveloper.asianwikitutorial.model.CastModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import static androidx.constraintlayout.widget.Constraints.TAG;

public class CastsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<CastModel> items;

    public CastsAdapter(Context context, List<CastModel> items) {
        this.context = context;
        this.items = items;
    }

    class CastHolder extends RecyclerView.ViewHolder{
        ImageView image;
        TextView realName,name;
        CastHolder(@NonNull View itemView) {
            super(itemView);
            realName = itemView.findViewById(R.id.tv_realname);
            name = itemView.findViewById(R.id.tv_name);
            image = itemView.findViewById(R.id.iv_profile);
        }

        void setData(CastModel s) {
            name.setText(s.getName());
            realName.setText(s.getRealName());
            Picasso.get()
                    .load(s.getImage())
                    .placeholder(R.drawable.placeholder_asianwiki)
                    .fit()
                    .centerCrop()
                    .into(image, new Callback() {
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
        View view = inflater.inflate(R.layout.item_casts, parent, false);
        return new CastHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        CastHolder uh = (CastHolder) holder;
        uh.setData(items.get(position));
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}
