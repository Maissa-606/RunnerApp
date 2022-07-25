package com.example.runnerapp;

import android.animation.Animator;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GestureDetectorCompat;

import com.airbnb.lottie.LottieAnimationView;

public class ActivityAnuncio1 extends AppCompatActivity {
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);
        if (hasFocus) {
            hideSystemUI();
        }
    }

    private void hideSystemUI() {
        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        | View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_FULLSCREEN);
    }


    private GestureDetectorCompat mDetector;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);

        setContentView(R.layout.activity_anuncio1);
        mDetector = new GestureDetectorCompat(this, new MyGestureListener());

        TextView next1 = findViewById(R.id.tvnext);
        next1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openActivity2();
            }
        });

        // Lottie Animation
        LottieAnimationView animationViewRemote = findViewById(R.id.abtnnext2);
        animationViewRemote.setAnimation(R.raw.boton);
        animationViewRemote.loop(false);
        animationViewRemote.playAnimation();

        // handler
        Handler handler = new Handler();

        animationViewRemote.addAnimatorListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animator) {
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        animationViewRemote.pauseAnimation();
                    }
                },1000);
            }

            @Override
            public void onAnimationEnd(Animator animator) {
                //que no siga el loop animationViewRemote.playAnimation();
            }

            @Override
            public void onAnimationCancel(Animator animator) {
            }

            @Override
            public void onAnimationRepeat(Animator animator) {
            }
        });
    }

    public void openActivity2() {
        Intent intent = new Intent(this, ActivityAnuncio2.class);
        startActivity(intent);
        overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event){
        this.mDetector.onTouchEvent(event);
        return super.onTouchEvent(event);
    }


    class MyGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityx, float velocityY) {
            if (e1.getX()>e2.getX())
            {
                openActivity2();
            }
            return true;
        }
    }
}