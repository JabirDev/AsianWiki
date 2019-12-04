package com.jemberdeveloper.asianwikitutorial.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.jemberdeveloper.asianwikitutorial.R;
import com.jemberdeveloper.asianwikitutorial.adapter.CastsAdapter;
import com.jemberdeveloper.asianwikitutorial.adapter.InfoAdapter;
import com.jemberdeveloper.asianwikitutorial.app.Config;
import com.jemberdeveloper.asianwikitutorial.model.CastModel;
import com.jemberdeveloper.asianwikitutorial.network.Koneksi;
import com.squareup.picasso.Picasso;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class DetailActivity extends AppCompatActivity {

    private static final String TAG = "DetailActivity";
    private List<String> listInfo = new ArrayList<>();
    private List<String> imgProfile = new ArrayList<>();
    private List<String> realName = new ArrayList<>();
    private List<String> urlProfile = new ArrayList<>();
    private List<String> castName = new ArrayList<>();
    private List<String> notes = new ArrayList<>();
    private ArrayList<String> trailerVidUrl = new ArrayList<>();
    private ArrayList<String> trailerThumUrl = new ArrayList<>();
    private List<CastModel> castModels = new ArrayList<>();
    private String trailerUrl, synopsis;
    private boolean castWithImage = false;
    private ImageView thumbnail,imageTrailer;
    private TextView title,tvSysnopsis;
    private InfoAdapter infoAdapter,noteAdapter;
    private CastsAdapter castsAdapter;
    private FloatingActionButton fabPlayTrailer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        init();
        getData();
        setData();

    }

    private void setData() {
        Intent data = getIntent();
        Picasso.get()
                .load(data.getStringExtra("thumbnail"))
                .placeholder(R.drawable.placeholder_asianwiki)
                .fit()
                .centerCrop()
                .into(thumbnail, new com.squareup.picasso.Callback() {
                    @Override
                    public void onSuccess() {
                        Log.d(TAG, "onSuccess: image loaded");
                    }

                    @Override
                    public void onError(Exception e) {
                        Log.e(TAG, "onError: ", e);
                    }
                });
        title.setText(data.getStringExtra("title"));
        Picasso.get()
                .load(R.drawable.placeholder_asianwiki)
                .into(imageTrailer);
    }

    private void init() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        toolbar.getNavigationIcon().setTint(getResources().getColor(R.color.colorPrimary));
        fabPlayTrailer = findViewById(R.id.fab_trailer);
        thumbnail = findViewById(R.id.iv_thumbnail);
        imageTrailer = findViewById(R.id.iv_trailer);
        title = findViewById(R.id.tv_title);
        tvSysnopsis = findViewById(R.id.tv_synopsis);
        RecyclerView rvInfo = findViewById(R.id.rv_info);
        RecyclerView rvNote = findViewById(R.id.rv_notes);
        RecyclerView rvCast = findViewById(R.id.rv_casts);
        infoAdapter = new InfoAdapter(this,listInfo);
        noteAdapter = new InfoAdapter(this,notes);
        castsAdapter = new CastsAdapter(this,castModels);
        LinearLayoutManager infoManager = new LinearLayoutManager(this);
        LinearLayoutManager noteManager = new LinearLayoutManager(this);
        GridLayoutManager castManager = new GridLayoutManager(this,2);
        rvInfo.setAdapter(infoAdapter);
        rvInfo.setLayoutManager(infoManager);
        rvNote.setAdapter(noteAdapter);
        rvNote.setLayoutManager(noteManager);
        rvCast.setAdapter(castsAdapter);
        rvCast.setLayoutManager(castManager);
        rvInfo.setNestedScrollingEnabled(false);
        rvNote.setNestedScrollingEnabled(false);
        rvCast.setNestedScrollingEnabled(false);
    }

    private void getData() {
        String url = getIntent().getStringExtra("url");
        Call<ResponseBody> aw = Koneksi.getAsianWiki().getData(url);
        aw.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Document doc = null;
                try {
                    doc = Jsoup.parse(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Elements elUl = null;
                if (doc != null) {
                    elUl = doc.select("#mw-content-text ul");
                }
                if (elUl != null) {
                    for (int i = 0; i < elUl.size(); i++) {
                        if (elUl.get(i).text().contains("Release Date:")) {
                            Document docLi = Jsoup.parse(elUl.get(i).toString());
                            Elements elLi = docLi.select("li");
                            for (int j = 0; j < elLi.size(); j++) {
                                listInfo.add(elLi.get(j).text());
                                infoAdapter.notifyDataSetChanged();
                            }
                        }
                    }
                    if (elUl.size() > 2) {
                        if (elUl.get(2).toString().contains("href")) {
                            Document docLi = Jsoup.parse(elUl.get(2).toString());
                            Elements elLi = docLi.select("li");
                            if (elLi.size() > 1) {
                                for (int i = 0; i < elLi.size(); i++) {
                                    castWithImage = false;
                                    Element el = elLi.get(i);
                                    String realN = el.select("a").text();
                                    String urlPro = Config.BASE_URL + el.select("a").attr("href").substring(1);
                                    String cName = el.text();
                                    cName = cName.substring(cName.indexOf("- ") + 2);
                                    realName.add(realN);
                                    urlProfile.add(urlPro);
                                    castName.add(cName);
                                    imgProfile.add("na");
                                }
                            } else {
                                castWithImage = true;
                            }
                        }
                    } else {
                        castWithImage = true;
                    }
                }
                Elements elTable = doc.select("#mw-content-text table");
                if (castWithImage) {
                    for (int i = 0; i < elTable.size(); i++) {
                        if (elTable.get(i).attr("style").equals("text-align:center")) {
                            Document docTable = Jsoup.parse(elTable.get(i).toString());
                            Elements elTr = docTable.select("tr");
                            for (int j = 0; j < elTr.size(); j++) {
                                if (j > 0 && j != 2 && j != 3) {
                                    Document docTr = Jsoup.parse(elTr.get(j).toString());
                                    Elements elAtag = docTr.select("a");
                                    for (int k = 0; k < elAtag.size(); k++) {
                                        Element el = elAtag.get(k);
                                        String realN = el.attr("title");
                                        String urlPro = Config.BASE_URL + el.attr("href").substring(1);
                                        String imgPro = Config.BASE_URL + el.select("img").attr("src").substring(1);

                                        realName.add(realN);
                                        urlProfile.add(urlPro);
                                        imgProfile.add(imgPro);
                                    }
                                }
                                if (j == 3) {
                                    String tdText = elTr.get(j).toString();
                                    String[] tagTd = tdText.split("<td>");
                                    for (String aTagTd : tagTd) {
                                        aTagTd = aTagTd.replace("</td>", "")
                                                .replace("<tr>", "")
                                                .replace("</tr>", "")
                                                .trim();
                                        if (aTagTd.length() > 0) {
                                            castName.add(aTagTd);
                                        }
                                    }
                                }

                            }
                        }
                    }
                }

                for (int i = 0; i < urlProfile.size(); i++) {
                    castModels.add(new CastModel(imgProfile.get(i), urlProfile.get(i), realName.get(i), castName.get(i)));
                    castsAdapter.notifyDataSetChanged();
                }

                Elements elTrailer = doc.select("#mw-content-text script");
                for (int i = 0; i < elTrailer.size(); i++) {
                    String elTrai = elTrailer.get(i).toString();
                    if (elTrai.contains("mediaplayer")) {
                        int inFile = elTrai.indexOf("file\': \'");
                        elTrai = elTrai.substring(inFile + 8);
                        elTrai = elTrai.substring(0, elTrai.indexOf("xml") + 3);
                        trailerUrl = elTrai;
                    } else {
                        trailerUrl = "na";
                    }
                }

                Elements elPtag = doc.select("#mw-content-text p");
                synopsis = elPtag.get(5).text();
                tvSysnopsis.setText(synopsis);

                Elements elOl = doc.select("#mw-content-text ol");
                if (elOl.size() == 0){
                    notes.add("Not available");
                    noteAdapter.notifyDataSetChanged();
                } else {
                    for (int i = 0; i < elOl.size(); i++) {
                        Document docLi = Jsoup.parse(elOl.get(i).toString());
                        Elements elLi = docLi.select("li");
                        for (int j = 0; j < elLi.size(); j++) {
                            notes.add(elLi.get(j).text());
                            noteAdapter.notifyDataSetChanged();
                        }
                    }
                }

                if (!TextUtils.isEmpty(trailerUrl) && !trailerUrl.equals("na")){
                    getTrailer();
                }

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    private void getTrailer() {
        Call<ResponseBody> getTrailers = Koneksi.getAsianWiki().getData(trailerUrl);
        getTrailers.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                Document doc = null;
                try {
                    doc = Jsoup.parse(response.body().string());
                } catch (IOException e) {
                    e.printStackTrace();
                }
                Elements items = doc.select("item");
                for (int i = 0; i < items.size(); i++) {
                    Element element = items.get(i);
                    String urlVideo = element.select("media|content").attr("url");
                    String urlThum = element.select("media|thumbnail").attr("url");
                    trailerVidUrl.add(urlVideo);
                    trailerThumUrl.add(urlThum);
                }

                Picasso.get()
                        .load(trailerThumUrl.get(0))
                        .placeholder(R.drawable.placeholder_asianwiki)
                        .fit()
                        .centerCrop()
                        .into(imageTrailer, new com.squareup.picasso.Callback() {
                            @Override
                            public void onSuccess() {
                                Log.d(TAG, "onSuccess: ");
                            }

                            @Override
                            public void onError(Exception e) {
                                Log.e(TAG, "onError: ", e);
                            }
                        });

                fabPlayTrailer.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (trailerVidUrl.size() != 0){
                            Intent i = new Intent(DetailActivity.this,TrailerActivity.class);
                            i.putStringArrayListExtra("urlVideo", trailerVidUrl);
                            i.putStringArrayListExtra("urlThumb", trailerThumUrl);
                            startActivity(i);
                        } else {
                            Toast.makeText(DetailActivity.this, "Trailer not available", Toast.LENGTH_SHORT).show();
                        }
                    }
                });

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                Log.e(TAG, "onFailure: ", t);
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home){
            finish();
        }
        return super.onOptionsItemSelected(item);
    }
}
