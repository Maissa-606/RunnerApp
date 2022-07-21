package com.example.runnerapp;

import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.runnerapp.Modelo.RestApiMethods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ActivityLogin extends AppCompatActivity {
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
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
        );
    }
    //Button btnIngresar, btnRegistrarse; elimine el casteo y lo puse TextView
    TextView btnIngresar, btnRegistrarse;

    EditText txtcorreo, txtcontrasenia;
    TextView btnRecuperarClave;
    CheckBox Recordar;
    final Context context = this;

    SharedPreferences mSharedPrefs,mSharedPrefsPublico;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        btnIngresar = findViewById(R.id.albtnIngresar);
        btnRegistrarse = findViewById(R.id.albtnRegistrar);
        txtcorreo = (EditText) findViewById(R.id.altxtUser);
        txtcontrasenia = (EditText) findViewById(R.id.altxtPass);
        Recordar = (CheckBox) findViewById(R.id.alckRecordar);
        btnRecuperarClave = (TextView) findViewById(R.id.olvidastepass);

        btnRecuperarClave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),recuperar.class);
                startActivity(intent);
            }
        });

        btnIngresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String usuario = txtcorreo.getText().toString();
                String contrasenia = txtcontrasenia.getText().toString();
                loginUsuario(usuario,contrasenia);

                RestApiMethods.correo = txtcorreo.getText().toString();

            }
        });

        btnRegistrarse.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(),ActivityRegistrar.class);
                startActivity(intent);
            }
        });

//======================PERSISTENCIA DE DATOS=========================================//
        mSharedPrefs = getSharedPreferences("credenciales",Context.MODE_PRIVATE);
        String user = mSharedPrefs.getString("usuario","");
        String pass = mSharedPrefs.getString("password","");
        txtcorreo.setText(user);
        txtcontrasenia.setText(pass);
//========CREDENCIALES PUBLICAS PARA LLAMAR EN LAS ACTIVIDADES Y EVITAR QUE LA INFORMACION SE PIERDA====///
        mSharedPrefsPublico = getSharedPreferences("credencialesPublicas",Context.MODE_PRIVATE);
        String userPublic = mSharedPrefsPublico.getString("usuarioPublic","");

        if (txtcorreo.getText().length() == 0 || txtcontrasenia.getText().length() ==0){
            //Toast.makeText(getApplicationContext(), "Bienvenido ", Toast.LENGTH_SHORT).show();
        }else {
            Recordar.setChecked(true);
            loginUsuario(user, pass);
        }



    }

    private void loginUsuario(String correo, String clave) {
       RequestQueue queue = Volley.newRequestQueue(this);
        HashMap<String, String> parametros = new HashMap<>();
        parametros.put("email", correo);
        parametros.put("clave", clave);
            JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, RestApiMethods.EndPointValidarLogin,
                    new JSONObject(parametros), new Response.Listener<JSONObject>() {
                @Override
                public void onResponse(JSONObject response) {
                    try {

                        JSONArray mensajeArray = response.getJSONArray("respuesta");
                        String mensaje="";
                        String codigo="";
                        for (int i = 0; i < mensajeArray.length(); i++) {
                            JSONObject RowMensaje = mensajeArray.getJSONObject(i);
                            mensaje = RowMensaje.getString("mensaje");
                            codigo = RowMensaje.getString("codigo_usuario");
                            RestApiMethods.correo = txtcorreo.getText().toString();
                            RestApiMethods.codigo_usuario = codigo;
                        }

                        if (mensaje.equals("login exitoso")){
                            mSharedPrefs = getSharedPreferences("credenciales",Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = mSharedPrefs.edit();
                            if (Recordar.isChecked()){
                                String user = txtcorreo.getText().toString();
                                String pass = txtcontrasenia.getText().toString();
                                editor.putString("usuario",user);
                                editor.putString("password",pass);
                                editor.commit();
                            }else{
                                editor.putString("usuario","");
                                editor.putString("password","");
                                editor.commit();
                            }

                            //==============USAR CEDENCIALES PUBLICAMENTE==============///
                            mSharedPrefsPublico = getSharedPreferences("credencialesPublicas",Context.MODE_PRIVATE);
                            SharedPreferences.Editor editorPublico = mSharedPrefsPublico.edit();
                            String userPublic = txtcorreo.getText().toString();
                            editorPublico.putString("correo",txtcorreo.getText().toString());
                            editorPublico.putString("idusuario",codigo );
                            editorPublico.commit();


                            //Toast.makeText(getApplicationContext(), "Response " + mensaje, Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent(getApplicationContext(),ActivityTablero.class);
                            intent.putExtra("codigo_usuario", codigo);
                            startActivity(intent);
                            finish();
                        }else{
                            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
                            alertDialogBuilder.setTitle("Datos invalidos");
                            alertDialogBuilder
                                    .setMessage("Verifique su correo o contraseña")
                                    .setCancelable(false)
                                    .setPositiveButton("OK",new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int id) {

                                            dialog.cancel();
                                        }
                                    });
                            // create alert dialog
                            AlertDialog alertDialog = alertDialogBuilder.create();
                            alertDialog.show();
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
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

}