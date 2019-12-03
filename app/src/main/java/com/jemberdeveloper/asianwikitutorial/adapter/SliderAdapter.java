package com.jemberdeveloper.asianwikitutorial.adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.jemberdeveloper.asianwikitutorial.R;
import com.jemberdeveloper.asianwikitutorial.model.SliderModel;
import com.squareup.picasso.Callback;
import com.squareup.picasso.Picasso;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import static android.content.ContentValues.TAG;

public class SliderAdapter extends PagerAdapter {

    private Context context;
    private List<SliderModel> items;

    public SliderAdapter(Context context, List<SliderModel> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View view = inflater.inflate(R.layout.item_slider, null);
        ImageView image = view.findViewById(R.id.iv_image);
        TextView title = view.findViewById(R.id.tv_title);
        title.setText(items.get(position).getTitle());
        Picasso.get()
                .load(items.get(position).getImage())
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

        container.addView(view);
        return view;
    }

    @Override
    public int getCount() {
        return items.size();
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
