package com.search.wiki.common.view;

import android.content.Intent;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.search.wiki.R;
import com.search.wiki.home.view.HomeActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        loadProgress();
    }

    private void loadProgress() {
        final ImageView imageView = (ImageView) findViewById(R.id.image_logo);
        final Animation rotateAnimation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.rotate);
        final Animation fadeOutAnimation = AnimationUtils.loadAnimation(getBaseContext(), R.anim.abc_fade_out);

        imageView.startAnimation(rotateAnimation);

        fadeOutAnimation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                openHomePage();
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        final Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                imageView.startAnimation(fadeOutAnimation);
            }
        }, 3000);
    }

    public void openHomePage() {
        finish();
        startActivity(new Intent(this, HomeActivity.class));
    }
}
