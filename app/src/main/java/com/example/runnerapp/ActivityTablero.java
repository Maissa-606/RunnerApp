package com.example.runnerapp;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.runnerapp.Modelo.RestApiMethods;

public class ActivityTablero extends AppCompatActivity {
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

    TextView btnNuevaCarrera,btnVerProgreso, btnEstadistica;
    ImageView btnMostrarAmigos, clasificacion, btnCerrarSesion;


    public static final String tablero_correo = RestApiMethods.correo;
    public static final String tablero_codigo_usuario = RestApiMethods.codigo_usuario;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tablero);

        ImageView perfil = findViewById(R.id.perfil);
        btnNuevaCarrera = findViewById(R.id.btnNuevaCarrera);
        btnVerProgreso = findViewById(R.id.tablerbtnVerProgreso);
        btnMostrarAmigos = findViewById(R.id.tablerobtnAmigos);
        btnEstadistica = findViewById(R.id.tablerobtnEstadistica);
        clasificacion = findViewById(R.id.ranking);
        btnCerrarSesion = findViewById(R.id.tabCerrarSesion);

        btnCerrarSesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences mSharedPrefs = getSharedPreferences("credenciales",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = mSharedPrefs.edit();
                    editor.putString("usuario","");
                    editor.putString("password","");
                    editor.commit();
                Intent intent = new Intent(getApplicationContext(), ActivityLogin.class);
                startActivity(intent);
                finish();

            }
        });

        perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Activity_Perfil.class);
                startActivity(intent);
            }
        });
        btnNuevaCarrera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), ActivityNuevaCarrera.class);
                startActivity(intent);
            }
        });
        btnVerProgreso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),Progreso_ListadoActividades.class);
                startActivity(intent);
            }
        });
        btnMostrarAmigos.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ActivitiAmigos.class);
                startActivity(intent);
            }
        });
        btnEstadistica.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ActivityEstadisticas.class);
                startActivity(intent);
            }
        });
        clasificacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ActivityClasificacion.class);
                startActivity(intent);
            }
        });
    }
}