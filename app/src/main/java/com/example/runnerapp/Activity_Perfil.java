package com.example.runnerapp;

import static android.app.PendingIntent.getActivity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.runnerapp.Modelo.RestApiMethods;
import com.example.runnerapp.Modelo.Usuario;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

public class Activity_Perfil extends AppCompatActivity {
    //ocultar menu del telefono
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

EditText txtnombre,txtpais,txtpeso,txtaltu, txtfechaNacimiento, txtcorreo, txttelefono;
TextView btnEditar,btnAtras;   //OJOOOOOOOOOOOOOOO SE LOS CAMBIE A TEXTVIEW
ImageView perfilfoto;
Usuario usuario;
ArrayList<String> arrayUsuario;
String email;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        txtnombre = (EditText) findViewById(R.id.fhtxtnombre);
        txtfechaNacimiento = (EditText) findViewById(R.id.fhfecha);
        txtcorreo = (EditText) findViewById(R.id.fhcorreo);
        txtpais = (EditText) findViewById(R.id.fhtxtPais);
        txtpeso = (EditText) findViewById(R.id.fhpeso);
        txtaltu= (EditText) findViewById(R.id.fhaltura);
        txttelefono= (EditText) findViewById(R.id.fhtelefono);
        perfilfoto = (ImageView) findViewById(R.id.fhImage);
        btnEditar = (TextView) findViewById(R.id.perfilbtnActualizar); ////OJOOOOOOOO SE LOS CAMBIE A TEXTVIEW
        btnAtras = (TextView) findViewById(R.id.perbtnAtras);

        SharedPreferences mSharedPrefs = getSharedPreferences("credencialesPublicas",Context.MODE_PRIVATE);
        String correo = mSharedPrefs.getString("correo","");
        email = correo;

        listarUsuarios(email);
        
        btnEditar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editar();
                finish();
            }
        });


        btnAtras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ActivityTablero.class);
                startActivity(intent);

            }
        });

    }

    private void listarUsuarios(String correo) {
        RequestQueue queue = Volley.newRequestQueue(this);
        HashMap<String, String> parametros = new HashMap<>();
        parametros.put("email", correo);

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, RestApiMethods.EndPointBuscarCorreo,
                new JSONObject(parametros), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray usuarioArray = response.getJSONArray( "usuario");

                    arrayUsuario = new ArrayList<>();
                    for (int i=0; i<usuarioArray.length(); i++)
                    {
                        JSONObject RowUsuario = usuarioArray.getJSONObject(i);
                        usuario = new Usuario(  RowUsuario.getInt("codigo_usuario"),
                                RowUsuario.getString("nombres"),
                                RowUsuario.getString("apellidos"),
                                RowUsuario.getString("fecha_nac"),
                                RowUsuario.getString("pais"),
                                RowUsuario.getInt("codigo_pais"),
                                RowUsuario.getString("email"),
                                RowUsuario.getString("peso"),
                                RowUsuario.getString("altura"),
                                RowUsuario.getString("foto"),
                                RowUsuario.getString("telefono")
                        );

                        txtnombre.setText(usuario.getNombres()+" "+usuario.getApellidos());
                        txtfechaNacimiento.setText(usuario.getFechaNac());
                        txtpeso.setText(usuario.getPeso());
                        txtaltu.setText(usuario.getAltura());
                        txtpais.setText(usuario.getPais());
                        txtcorreo.setText(usuario.getCorreo());
                        txttelefono.setText(usuario.getTelefono());

                        mostrarFoto(usuario.getFoto().toString());

                    }


                }catch (JSONException ex){
                    Toast.makeText(getApplicationContext(), "mensaje"+ex, Toast.LENGTH_SHORT).show();
                }
            }


        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error "+error.toString(), Toast.LENGTH_SHORT).show();
            }
        });

        queue.add(jsonObjectRequest);
    }

    private void editar() {
        Intent intent = new Intent(getApplicationContext(),Activity_Actualizar_Perfil.class);
        intent.putExtra("email", email);
        intent.putExtra("nombres", usuario.getNombres()+"");
        intent.putExtra("apellidos", usuario.getApellidos()+"");
        intent.putExtra("fechanac", usuario.getFechaNac()+"");
        intent.putExtra("codigo_pais", usuario.getCodigo_pais()+"");
        intent.putExtra("telefono", usuario.getTelefono()+"");
        intent.putExtra("peso", usuario.getPeso()+"");
        intent.putExtra("altura", usuario.getAltura()+"");
        intent.putExtra("foto", usuario.getFoto()+"").toString();
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK );
        startActivity(intent);
        finish();
    }



    public void mostrarFoto(String foto) {
        try {
            String base64String = "data:image/png;base64,"+foto;
            String base64Image = base64String.split(",")[1];
            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            perfilfoto.setImageBitmap(decodedByte);//setea la imagen al imageView
        }catch (Exception ex){
            ex.toString();
        }
    }

}