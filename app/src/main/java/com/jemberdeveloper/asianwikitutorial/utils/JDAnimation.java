package com.jemberdeveloper.asianwikitutorial.utils;

import android.content.Context;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.jemberdeveloper.asianwikitutorial.R;

public class JDAnimation {

    public static void blink (Context c, View v){
        Animation a = AnimationUtils.loadAnimation(c, R.anim.blink);
        v.startAnimation(a);
    }

}
