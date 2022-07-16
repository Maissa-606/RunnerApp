package com.example.runner_app;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.runner_app.ClasesJava.Usuarios;
import com.example.runner_app.ConexionPHP.Database.Database;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class ActivityRegistro extends AppCompatActivity
{

    EditText inpNom,inpApel,inpEmail,inpTel,inppais,inpClave;
    Button btnRegistrar;


    @Override
    protected void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registro);

        //DECLARACION DE INPUTS
        inpNom = (EditText) findViewById(R.id.inpNombre);
        inpApel = (EditText) findViewById(R.id.inpApellido);
        inpEmail = (EditText) findViewById(R.id.inpCorreo);
        inpTel = (EditText) findViewById(R.id.inpTelefono);
        inppais = (EditText) findViewById(R.id.inpPais);
        inpClave=  (EditText) findViewById(R.id.inpContraseña);


        //DECLARACION BOTONES
        btnRegistrar= (Button) findViewById(R.id.btnRegistro);




        //---------EVENTOS DE BOTONES
        btnRegistrar.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v)
            {
                RegistrarUsuario();

            }
        });

    }



    /*--------------METODOS SQL GUARDAR,ACTUALIZAR,BORRAR DATOS-------------------
    *
    *
    *
    * */
    public void RegistrarUsuario()
    {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        HashMap<String, String> datos = new HashMap<>();
        datos.put("nombres", inpNom.getText().toString());
        datos.put("apellidos", inpApel.getText().toString());
        datos.put("telefono", inpTel.getText().toString());
        datos.put("email", inpEmail.getText().toString());
        datos.put("contrasena", inpClave.getText().toString());


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST,
                Database.EndpointCREATEUsuarios,
                new JSONObject(datos), new Response.Listener<JSONObject>()
        {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Toast.makeText(getApplicationContext(), "String Response " + response.getString("mensaje").toString(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),ActivityRegistro.class);
                    startActivity(intent);
                    finish();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
        requestQueue.add(jsonObjectRequest);


    }
}



