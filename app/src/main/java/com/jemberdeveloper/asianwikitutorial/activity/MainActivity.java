package com.jemberdeveloper.asianwikitutorial.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AbsListView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.google.android.material.tabs.TabLayout;
import com.jemberdeveloper.asianwikitutorial.R;
import com.jemberdeveloper.asianwikitutorial.adapter.SliderAdapter;
import com.jemberdeveloper.asianwikitutorial.adapter.UpcomingDramaAdapter;
import com.jemberdeveloper.asianwikitutorial.adapter.UpcomingMoviesAdapter;
import com.jemberdeveloper.asianwikitutorial.app.Config;
import com.jemberdeveloper.asianwikitutorial.model.SliderModel;
import com.jemberdeveloper.asianwikitutorial.model.UpcomingModel;
import com.jemberdeveloper.asianwikitutorial.network.MySingleton;
import com.jemberdeveloper.asianwikitutorial.utils.JDAnimation;
import com.jemberdeveloper.asianwikitutorial.utils.SliderTime;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.Timer;

public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";
    private ViewPager vpSlider;
    private TabLayout tabIndicator;
    private List<SliderModel> itemSlider = new ArrayList<>();
    private SliderAdapter sliderAdapter;
    private List<UpcomingModel> moviesModel = new ArrayList<>();
    private List<UpcomingModel> dramaModel = new ArrayList<>();
    private UpcomingMoviesAdapter moviesAdapter;
    private UpcomingDramaAdapter dramaAdapter;
    private ProgressBar pbMovies,pbDrama;
    private boolean isMovieScroll = false, isDramaScroll = false;
    private int currentMovies, totalMovies, scrolloutMovies, currentDrama, totalDrama, scrollOutDrama;
    private LinearLayout shimmerSlider;
    private ConstraintLayout shimmerMovie,shimmerDrama;
    private RecyclerView rvMovies,rvDramas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Toolbar toolbar = findViewById(R.id.toolbar);
        toolbar.setTitle(R.string.app_name);
        toolbar.setTitleTextColor(getResources().getColor(R.color.colorPrimary));
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.logo_asianwiki));
        setSupportActionBar(toolbar);
        toolbar.setElevation(2);

        shimmerSlider = findViewById(R.id.slider_shimmer);
        shimmerMovie = findViewById(R.id.movies_shimmer);
        shimmerDrama = findViewById(R.id.drama_shimmer);

        JDAnimation.blink(this,shimmerSlider);
        JDAnimation.blink(this,shimmerMovie);
        JDAnimation.blink(this,shimmerDrama);

        pbDrama = findViewById(R.id.pb_drama);
        pbMovies = findViewById(R.id.pb_movies);
        vpSlider = findViewById(R.id.vp_slider);
        tabIndicator = findViewById(R.id.indicator_slider);

        sliderAdapter = new SliderAdapter(this,itemSlider);
        vpSlider.setAdapter(sliderAdapter);
        tabIndicator.setupWithViewPager(vpSlider);
        rvMovies = findViewById(R.id.rv_movies);
        rvDramas = findViewById(R.id.rv_drama);
        dramaAdapter = new UpcomingDramaAdapter(this,dramaModel);
        final LinearLayoutManager dramaManager = new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false);
        moviesAdapter = new UpcomingMoviesAdapter(this,moviesModel);
        final LinearLayoutManager moviesManager = new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false);
        rvMovies.setAdapter(moviesAdapter);
        rvMovies.setLayoutManager(moviesManager);
        rvDramas.setAdapter(dramaAdapter);
        rvDramas.setLayoutManager(dramaManager);

        rvMovies.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isMovieScroll = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isMovieScroll && (currentMovies + scrolloutMovies < totalMovies)){
                    pbMovies.setVisibility(View.GONE);
                }
                currentMovies = moviesManager.getChildCount();
                totalMovies = moviesManager.getItemCount();
                scrolloutMovies = moviesManager.findFirstVisibleItemPosition();
                if (isMovieScroll && (currentMovies + scrolloutMovies == totalMovies)){
                    isMovieScroll = false;
                    pbMovies.setVisibility(View.VISIBLE);
                    if ((moviesAdapter.number * 10) < moviesModel.size()){
                        moviesAdapter.number = moviesAdapter.number + 1;
                    } else {
                        pbMovies.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, "All movies has been loaded", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        rvDramas.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL){
                    isDramaScroll = true;
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isDramaScroll && (currentDrama + scrollOutDrama < totalDrama)){
                    pbDrama.setVisibility(View.GONE);
                }
                currentDrama = dramaManager.getChildCount();
                totalDrama = dramaManager.getItemCount();
                scrollOutDrama = dramaManager.findFirstVisibleItemPosition();
                if (isDramaScroll && (currentDrama + scrollOutDrama == totalDrama)){
                    isDramaScroll = false;
                    pbDrama.setVisibility(View.VISIBLE);
                    if ((dramaAdapter.number * 10) < dramaModel.size()){
                        dramaAdapter.number = dramaAdapter.number + 1;
                    } else {
                        pbDrama.setVisibility(View.GONE);
                        Toast.makeText(MainActivity.this, "All dramas has been loaded", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        getData();

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new SliderTime.time(this,vpSlider,itemSlider), 10 * 1000, 20 * 1000);

        checkItem();

    }

    private void checkItem() {
        if (itemSlider.size() != 0){
            vpSlider.bringToFront();
            tabIndicator.bringToFront();
        }
        if (moviesModel.size() != 0){
            rvMovies.bringToFront();
        }
        if (dramaModel.size() != 0){
            rvDramas.bringToFront();
        }
    }

    private void getData() {
        shimmerSlider.setVisibility(View.VISIBLE);
        shimmerMovie.setVisibility(View.VISIBLE);
        shimmerDrama.setVisibility(View.VISIBLE);
        String url = Config.BASE_URL + "index.php";
        MySingleton.getInstance(this).getRequestQueue();
        StringRequest sr = new StringRequest(Request.Method.GET, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Document doc = Jsoup.parse(response);
                Elements el = doc.select("ul.amazingslider-slides li");
                for (int i = 0; i < el.size(); i++) {
                    Element element = el.get(i);
                    String link = element.select("a").attr("href");
                    String image = element.select("a img").attr("src");
                    String title = element.select("a img").attr("alt");

                    itemSlider.add(new SliderModel(link,image,title));
                    sliderAdapter.notifyDataSetChanged();
                }

                // Get upcoming movies
                Elements upMovies = doc.select("#slidorion #slide1");
                for (int i = 0; i < upMovies.size(); i++) {
                    Document docSlide1 = Jsoup.parse(upMovies.get(i).toString());
                    Elements elSlide1 = docSlide1.select("div");
                    for (int j = 0; j < elSlide1.size(); j++) {
                        Document docDiv = Jsoup.parse(elSlide1.get(j).toString());
                        Elements elDiv = docDiv.select("ul");
                        for (int k = 0; k < elDiv.size(); k++) {
                            Document docUl = Jsoup.parse(elDiv.get(k).toString());
                            Elements elUl = docUl.select("a");
                            for (int l = 0; l < elUl.size(); l++) {
                                Element elAtag = elUl.get(l);
                                String link = elAtag.attr("href");
                                String title = elAtag.text();

                                // Get thumbnail
                                if (!isMovieLinkExist(link) &&
                                        link.length() > 0 &&
                                        title.length() > 0){
                                    getThumbnail("movie",link,title);
                                }
                            }
                        }
                    }
                }
                // end upcoming movies

                // Get upcoming Drama
                Elements upDrama = doc.select("#slidorion2 .slide2");
                for (int i = 0; i < upMovies.size(); i++) {
                    Document docSlide1 = Jsoup.parse(upDrama.get(i).toString());
                    Elements elSlide1 = docSlide1.select("div");
                    for (int j = 0; j < elSlide1.size(); j++) {
                        Document docDiv = Jsoup.parse(elSlide1.get(j).toString());
                        Elements elDiv = docDiv.select("ul");
                        for (int k = 0; k < elDiv.size(); k++) {
                            Document docUl = Jsoup.parse(elDiv.get(k).toString());
                            Elements elUl = docUl.select("a");
                            for (int l = 0; l < elUl.size(); l++) {
                                Element elAtag = elUl.get(l);
                                String link = elAtag.attr("href");
                                String title = elAtag.text();

                                // Get drama thumbnail
                                if (!isDramaLinkExist(link) &&
                                        link.length() > 0 &&
                                        title.length() > 0){
                                    getThumbnail("drama",link,title);
                                }
                            }
                        }
                    }
                }
                // end upcoming drama




            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: ", error);
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(sr);
    }

    private void getThumbnail(final String type, final String link, final String title) {
        MySingleton.getInstance(this).getRequestQueue();
        StringRequest sr = new StringRequest(Request.Method.GET, link, new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                Document doc = Jsoup.parse(response);
                String urlThumbnail = doc.select(".thumbinner img").attr("src");
                urlThumbnail = Config.BASE_URL + urlThumbnail;
                // We have duplicate content
                // Try to check before adding to list
                if (!isMovieLinkExist(link) && type.equals("movie")){
                    moviesModel.add(new UpcomingModel(link,title,urlThumbnail));
                    moviesAdapter.notifyDataSetChanged();
                }
                if (!isDramaLinkExist(link) && type.equals("drama")){
                    dramaModel.add(new UpcomingModel(link,title,urlThumbnail));
                    dramaAdapter.notifyDataSetChanged();
                }

                checkItem();

            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e(TAG, "onErrorResponse: ", error);
            }
        });
        MySingleton.getInstance(this).addToRequestQueue(sr);
    }

    private boolean isMovieLinkExist (String link){
        for (int i = 0; i < moviesModel.size(); i++) {
            if (link.equals(moviesModel.get(i).getLink())){
                return true;
            }
        }
        return false;
    }

    private boolean isDramaLinkExist (String link){
        for (int i = 0; i < dramaModel.size(); i++) {
            if (link.equals(dramaModel.get(i).getLink())){
                return true;
            }
        }
        return false;
    }

}
