package com.nodeads.test_task.github_client.core.controllers;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v4.util.Pair;
import android.util.Log;
import android.view.View;
import android.view.ViewAnimationUtils;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

/**
 * Created by Igor on 03.08.2016.
 */
public class AnimationController {

    private Context mContext;

    public AnimationController(Context c){
        this.mContext = c;
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public void circularReveal(final View circularView, final View backgroundView, final int backgroundColor) {
        int cx = (circularView.getLeft() + circularView.getRight()) / 2;
        int cy = (circularView.getTop() + circularView.getBottom()) / 2;
        int finalRadius = circularView.getWidth();
        Animator anim = ViewAnimationUtils.createCircularReveal(circularView, cx, cy, 0, finalRadius);
        anim.addListener(new AnimatorListenerAdapter() {

            @Override
            public void onAnimationStart(Animator animation) {
                super.onAnimationStart(animation);
                circularView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                super.onAnimationEnd(animation);
                circularView.setVisibility(View.GONE);
                backgroundView.setBackgroundColor(mContext.getResources().getColor(backgroundColor));
            }
        });
        anim.setDuration(1000);
        anim.start();
    }

    @TargetApi(Build.VERSION_CODES.JELLY_BEAN)
    public void transition(View targetView, String transitionName, Intent intent){
        ActivityOptionsCompat options;
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
            options = ActivityOptionsCompat.makeSceneTransitionAnimation((Activity)mContext, targetView, transitionName);
        } else{
            options = ActivityOptionsCompat.makeScaleUpAnimation(targetView, (int)targetView.getX(), (int)targetView.getY(), targetView.getWidth(), targetView.getHeight());
        }
        mContext.startActivity(intent, options.toBundle());
    }

    public void show(View view){
        Animation anim = getAnimation(android.R.anim.fade_in);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {
                view.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(anim);
    }

    public void hide(View view){
        Animation anim = getAnimation(android.R.anim.fade_in);
        anim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                view.setVisibility(View.GONE);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        view.startAnimation(anim);
    }

    public Animation getAnimation(int animRes){
        return AnimationUtils.loadAnimation(mContext, animRes);
    }

}
