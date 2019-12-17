package com.jemberdeveloper.asianwikitutorial.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import com.jemberdeveloper.asianwikitutorial.R;
import com.jemberdeveloper.asianwikitutorial.app.Config;

public class BrowserableActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_browserable);
        getData();
    }

    private void getData() {
        String url = getIntent().getData().toString();
        if (url.equals(Config.BASE_URL)){
            startActivity(new Intent(this,MainActivity.class));
            finish();
        } else {
            Intent i = new Intent(this,DetailActivity.class);
            i.putExtra("url", url);
            startActivity(i);
            finish();
        }
        TextView tv = findViewById(R.id.tv_data);
        tv.setText(url);
    }
}
