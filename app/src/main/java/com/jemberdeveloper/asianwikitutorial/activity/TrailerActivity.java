package com.jemberdeveloper.asianwikitutorial.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.AccelerateDecelerateInterpolator;

import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.PlaybackParameters;
import com.google.android.exoplayer2.Player;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.source.ConcatenatingMediaSource;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.jemberdeveloper.asianwikitutorial.R;
import com.jemberdeveloper.asianwikitutorial.adapter.TrailerAdapter;

import java.util.ArrayList;
import java.util.List;

public class TrailerActivity extends AppCompatActivity implements TrailerAdapter.ChooseVideListener {

    private PlayerView playerView;
    private SimpleExoPlayer exoPlayer;
    private ConcatenatingMediaSource cMediaSource;
    private List<String> videoList = new ArrayList<>();
    private List<String> thumbList = new ArrayList<>();
    private List<String> titleList = new ArrayList<>();
    private TrailerAdapter adapter;
    private LinearLayoutManager manager;
    private RecyclerView recyclerView;
    private AnimatorSet animatorSet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setFullScreen();
        setContentView(R.layout.activity_trailer);

        animatorSet = new AnimatorSet();
        videoList = getIntent().getStringArrayListExtra("urlVideo");
        thumbList = getIntent().getStringArrayListExtra("urlThumb");
        titleList = getIntent().getStringArrayListExtra("title");

        recyclerView = findViewById(R.id.recyclerview);
        adapter = new TrailerAdapter(this,thumbList,videoList,titleList);
        manager = new LinearLayoutManager(this,RecyclerView.HORIZONTAL,false);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);
        adapter.notifyDataSetChanged();

        playerView = findViewById(R.id.playerView);

    }

    @Override
    protected void onStart() {
        super.onStart();
        playVideo();
    }

    @Override
    protected void onStop() {
        super.onStop();
        playerView.setPlayer(null);
        exoPlayer.release();
        exoPlayer = null;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (exoPlayer != null){
            exoPlayer.setPlayWhenReady(false);
            exoPlayer.stop();
            exoPlayer.seekTo(0);
        }
    }

    private void setFullScreen() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
    }

    @Override
    public void onItemClick(String url) {
        for (int i = 0; i < videoList.size(); i++) {
            if (videoList.get(i).equals(url)){
                int currentPlay = exoPlayer.getCurrentWindowIndex();
                if (currentPlay == i){
                    exoPlayer.setPlayWhenReady(true);
                } else {
                    exoPlayer.seekToDefaultPosition(i);
                    exoPlayer.setPlayWhenReady(true);
                }
                break;
            }
        }
    }

    private void playVideo() {
        exoPlayer = ExoPlayerFactory.newSimpleInstance(this,new DefaultTrackSelector());
        playerView.setPlayer(exoPlayer);
        DefaultDataSourceFactory dataSource = new DefaultDataSourceFactory(this, Util.getUserAgent(this, getString(R.string.app_name)));
        cMediaSource = new ConcatenatingMediaSource();
        for (int i = 0; i < videoList.size(); i++) {
            MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSource)
                    .createMediaSource(Uri.parse(videoList.get(i)));
            cMediaSource.addMediaSource(mediaSource);
        }
        exoPlayer.prepare(cMediaSource);
        exoPlayer.setPlayWhenReady(true);

        exoPlayer.addListener(new Player.EventListener() {
            @Override
            public void onTimelineChanged(Timeline timeline, Object manifest, int reason) {

            }

            @Override
            public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

            }

            @Override
            public void onLoadingChanged(boolean isLoading) {

            }

            @Override
            public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                if (playbackState == Player.STATE_ENDED){
                    recyclerView.setVisibility(View.VISIBLE);
                    animation();
                } else if (playWhenReady && playbackState == Player.STATE_READY){
                    recyclerView.setVisibility(View.GONE);
                } else if (playWhenReady){
                    recyclerView.setVisibility(View.VISIBLE);
                    animation();
                } else {
                    recyclerView.setVisibility(View.VISIBLE);
                    animation();
                }
            }

            @Override
            public void onRepeatModeChanged(int repeatMode) {

            }

            @Override
            public void onShuffleModeEnabledChanged(boolean shuffleModeEnabled) {

            }

            @Override
            public void onPlayerError(ExoPlaybackException error) {

            }

            @Override
            public void onPositionDiscontinuity(int reason) {

            }

            @Override
            public void onPlaybackParametersChanged(PlaybackParameters playbackParameters) {

            }

            @Override
            public void onSeekProcessed() {

            }
        });

    }

    private void animation() {
        PropertyValuesHolder oY = PropertyValuesHolder.ofFloat(View.TRANSLATION_Y,-10f,0);
        PropertyValuesHolder a = PropertyValuesHolder.ofFloat(View.ALPHA,0,1);
        Animator flyDown = ObjectAnimator.ofPropertyValuesHolder(recyclerView,oY);
        Animator fadeIn = ObjectAnimator.ofPropertyValuesHolder(recyclerView,a);
        flyDown.setDuration(1000);
        fadeIn.setDuration(3000);
        flyDown.setInterpolator(new AccelerateDecelerateInterpolator());
        fadeIn.setInterpolator(new AccelerateDecelerateInterpolator());
        animatorSet.play(fadeIn).with(flyDown);
        animatorSet.start();
    }

}
