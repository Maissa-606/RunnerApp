package com.example.runner_app;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;

public class ActivityLogin extends AppCompatActivity
{


    //Declaración de Variables
    TextView btnIngresar, btnRegistrarse;
    EditText txtcorreo, txtcontrasenia;
    TextView btnRecuperarClave;
    CheckBox Recordar;



    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}