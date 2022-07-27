package com.example.runnerapp;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.location.LocationProvider;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.runnerapp.Modelo.RestApiMethods;
import com.example.runnerapp.databinding.ActivityMapsBinding;
import com.example.runnerapp.ui.dashboard.DashboardFragment;
import com.google.android.gms.maps.GoogleMap;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class ActivityNuevaCarrera extends AppCompatActivity{
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


    public static Button btnComenzar, btnDetener;
    EditText txtLat,txtLon, txtTiempo;

    public static String latitud = "";
    public static String longitud = "";
    GoogleMap mMap;
    private ActivityMapsBinding binding;
    final String[] codigo_actividad = new String[1];


    //-----tiempo
    private int segundos;
    private boolean running1;
    private boolean wasRunning;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_usuario);
        if(savedInstanceState != null){
            savedInstanceState.getInt("segundos");
            savedInstanceState.getInt("running1");
            savedInstanceState.getInt("wasRunning");


        }

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {

        }


        iniciarTiempo();

        btnComenzar = (Button) findViewById(R.id.btnComenzar);
        txtLat = (EditText) findViewById(R.id.txtLat);
        txtLon = (EditText) findViewById(R.id.txtLon);
        txtTiempo = (EditText) findViewById(R.id.nctiempo);


        btnComenzar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (btnComenzar.getText().equals("Comenzar")){
                    latitud = txtLat.getText().toString();
                    longitud = txtLon.getText().toString();
                    btnComenzar.setText("DETENER");

                    running1 =true;


                } else

                if (btnComenzar.getText().equals("DETENER")){
                    try {
                        String tiempo = txtTiempo.getText().toString();
                        SharedPreferences mSharedPrefs = getSharedPreferences("credencialesPublicas",Context.MODE_PRIVATE);
                        String idusuario = mSharedPrefs.getString("idusuario","");
                        guardarRecorrido(idusuario,DashboardFragment.km,tiempo);


                        new CountDownTimer(5000, 1000) {
                            public void onFinish() {
                                // When timer is finished
                                // Execute your code here
                                System.out.println("codigo actividad: "+codigo_actividad[0]);
                                System.out.println("Latitud: "+DashboardFragment.recorridoMapLatitud.get(0));
                                System.out.println("Longitud:"+DashboardFragment.recorridoMapLongitud.get(0));
                                if (DashboardFragment.recorridoMapLongitud !=null) {
                                    for (int indice = 0; indice < DashboardFragment.recorridoMapLongitud.size(); indice++) {
                                        guardarDetallesRecorrido(codigo_actividad[0], DashboardFragment.recorridoMapLatitud.get(indice), DashboardFragment.recorridoMapLongitud.get(indice));
                                    }
                                }

                            }

                            public void onTick(long millisUntilFinished) {
                                // millisUntilFinished    The amount of time until finished.
                            }
                        }.start();
                        new CountDownTimer(10001, 1000) {
                            public void onFinish() {
                                cerrarActividad();
                            }

                            public void onTick(long millisUntilFinished) {
                                // millisUntilFinished    The amount of time until finished.
                            }
                        }.start();


                        Toast.makeText(getApplicationContext(),"recorrido "+ DashboardFragment.recorridoMapLatitud+", "+DashboardFragment.recorridoMapLongitud,Toast.LENGTH_LONG).show();
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        });

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        } else {
            locationStart();
        }
        //mando a llamar el metodo ejecutar y cada 10 segundos se encargara de setear la nueva ubicacion


    }









    //-----------------------------LATITUD Y LONGITUD----------------------------------------
    private void locationStart() {
        LocationManager mlocManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        Localizacion Local = new Localizacion();
        Local.setMainActivity(this);
        final boolean gpsEnabled = mlocManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        if (!gpsEnabled) {
            //SE VA A LA CONFIGURACION DEL SISTEMA PARA QUE ACTIVE EL GPS UNA VEZ QUE INICIA LA APLICACION
            Intent settingsIntent = new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS);
            startActivity(settingsIntent);
        }
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION,}, 1000);
        }
        mlocManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 0, 0, (LocationListener) Local);
        mlocManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, (LocationListener) Local);


    }

    //-------------------------GUARDAR RECORRIDO--------------------------

    private void guardarRecorrido(String codigoUsuario, Double distancia,String tiempo) {

        RequestQueue queue = Volley.newRequestQueue(this);
        HashMap<String, String> parametros = new HashMap<>();
        parametros.put("codigo_usuario", codigoUsuario);
        parametros.put("distancia", distancia+"");
        parametros.put("tiempo",tiempo);
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, RestApiMethods.GuardarActidad,
                new JSONObject(parametros), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {

                    codigo_actividad[0] = String.valueOf(response.getString("mensaje"));
                    Toast.makeText(getApplicationContext(), "Actividad guardada exitosamente"+ codigo_actividad[0], Toast.LENGTH_SHORT).show();

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

    private void guardarDetallesRecorrido(String codigoactividad, Double latitud, Double longitud) {
        RequestQueue queue = Volley.newRequestQueue(this);
        HashMap<String, String> parametros = new HashMap<>();
        parametros.put("codigo_actividad", codigoactividad);
        parametros.put("Latitud", latitud+"");
        parametros.put("Longitud", longitud+"");
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, RestApiMethods.DetallesGuardarActidad,
                new JSONObject(parametros), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Toast.makeText(getApplicationContext(), response.getString("mensaje"), Toast.LENGTH_SHORT).show();
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

    private void cerrarActividad(){
        getSupportFragmentManager().beginTransaction().
                remove(getSupportFragmentManager().findFragmentById(R.id.fragmentContainerView)).commit();
        getSupportFragmentManager().beginTransaction().
                remove(getSupportFragmentManager().findFragmentById(R.id.navigation_dashboard));
        Intent intent = new Intent(getApplicationContext(), ActivityTablero.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
        startActivity(intent);
        finish();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);

        outState.putInt("segundos", segundos);
        outState.putBoolean("running", running1);
        outState.putBoolean("running", wasRunning);

    }

    private void iniciarTiempo() {
        TextView timeview = findViewById(R.id.cronometro);

        Handler handler = new Handler();
        handler.post(new Runnable() {
            @Override
            public void run() {
                int hours = segundos / 360;
                int minutos = (segundos % 360) / 60;
                int secs = segundos % 60;

                String time = String.format(Locale.getDefault(),
                        "%d:%02d:%02d",
                        hours,minutos,secs);
                txtTiempo.setText(time);
                if(running1){
                    segundos++;
                }
                handler.postDelayed(this,1000);
            }
        });
    }




    public class Localizacion implements LocationListener {
        ActivityNuevaCarrera mainActivity;

        public void setMainActivity(ActivityNuevaCarrera mainActivity) {
            this.mainActivity = mainActivity;
        }

        @Override
        public void onLocationChanged(Location loc) {
            // Este metodo se ejecuta cada vez que el GPS recibe nuevas coordenadas
            // debido a la deteccion de un cambio de ubicacion

            loc.getLatitude();
            loc.getLongitude();
            ActivityNuevaCarrera.setLatitud(loc.getLatitude()+"");
            ActivityNuevaCarrera.setLongitud(loc.getLongitude()+"");
            txtLat.setText(loc.getLatitude()+"");
            txtLon.setText(loc.getLongitude()+"");
            this.mainActivity.setLocation(loc);
        }


        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {
            switch (status) {
                case LocationProvider.AVAILABLE:
                    Log.d("debug", "LocationProvider.AVAILABLE");
                    break;
                case LocationProvider.OUT_OF_SERVICE:
                    Log.d("debug", "LocationProvider.OUT_OF_SERVICE");
                    break;
                case LocationProvider.TEMPORARILY_UNAVAILABLE:
                    Log.d("debug", "LocationProvider.TEMPORARILY_UNAVAILABLE");
                    break;
            }
        }
    }

    public void setLocation(Location loc) {
        //Obtener la direccion de la calle a partir de la latitud y la longitud
        if (loc.getLatitude() != 0.0 && loc.getLongitude() != 0.0) {
            try {
                Geocoder geocoder = new Geocoder(this, Locale.getDefault());
                List<Address> list = geocoder.getFromLocation(
                        loc.getLatitude(), loc.getLongitude(), 1);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static void setLatitud(String latitud) {
        ActivityNuevaCarrera.latitud = latitud;
    }
    public static void setLongitud(String longitud) {
        ActivityNuevaCarrera.longitud = longitud;
    }

    public static Double getlatitud(){
        return Double.valueOf(latitud);
    }
    public static Double getLongitud(){
        return Double.valueOf(longitud);
    }



}