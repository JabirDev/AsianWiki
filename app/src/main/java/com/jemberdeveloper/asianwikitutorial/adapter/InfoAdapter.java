package com.jemberdeveloper.asianwikitutorial.adapter;

import android.content.Context;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.jemberdeveloper.asianwikitutorial.R;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class InfoAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private List<String> items;

    public InfoAdapter(Context context, List<String> items) {
        this.context = context;
        this.items = items;
    }

    class InfoHolder extends RecyclerView.ViewHolder{
        TextView title;
        InfoHolder(@NonNull View itemView) {
            super(itemView);
            title = itemView.findViewById(R.id.tv_info);
        }

        void setData(String s, int position) {
            title.setText(Html.fromHtml(s));
            if (position % 2 == 0){
                title.setBackground(context.getResources().getDrawable(R.drawable.bg_text_info));
            }
        }

    }

    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_info, parent, false);
        return new InfoHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        InfoHolder uh = (InfoHolder) holder;
        uh.setData(items.get(position),position);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}

