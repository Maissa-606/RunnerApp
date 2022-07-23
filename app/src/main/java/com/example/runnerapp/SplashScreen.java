package com.example.runnerapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class SplashScreen extends AppCompatActivity {
    SharedPreferences mSharedPrefs;
    static final int LOCATION_GPS_REQUEST = 1000;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        View decorView = getWindow().getDecorView();
        int uiOptions = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        decorView.setSystemUiVisibility(uiOptions);
       // permisos();

}

/*
    private void entrarAnuncios() {
        mSharedPrefs = getSharedPreferences("anuncios", Context.MODE_PRIVATE);
        String olvidar = mSharedPrefs.getString("olvidar","");

        new Handler().postDelayed(new Runnable () {
            @Override
            public void run() {
                if(olvidar.equals( "1")){

                    startActivity(new Intent(SplashScreen.this, ActivityLogin.class));
                    finish();
                }else {
                    startActivity(new Intent(SplashScreen.this, ActivityAnuncio1.class));
                    finish();
                }
            }
        },2000);
    }

    private void permisos() {

        if(ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},LOCATION_GPS_REQUEST);
        }else{
            entrarAnuncios();
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == LOCATION_GPS_REQUEST)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {

                entrarAnuncios();
            }
        }else{
            Toast.makeText(getApplicationContext(),"Se necesitan permisos",Toast.LENGTH_LONG).show();
        }
    }*/
}