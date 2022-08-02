package com.example.runnerapp;

import androidx.fragment.app.FragmentActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.example.runnerapp.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.model.PolylineOptions;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback  {

    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    List<Double> pilatitud = new ArrayList<>();
    List<Double> pilongitud = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());


        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }


    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        obtenerRecorrido(getIntent().getStringExtra("codigo_actividad"));
        ejecutar();
        mMap.getUiSettings().setZoomControlsEnabled(true);


    }



    private void obtenerRecorrido(String codigo_actividad) {

        RequestQueue queue = Volley.newRequestQueue(this);
        HashMap<String, String> parametros = new HashMap<>();
        parametros.put("codigo_actividad", codigo_actividad);
        String url = "http://transportweb2.online/API/listadetalleactividad.php";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                new JSONObject(parametros), new Response.Listener<JSONObject>() {

            @Override
            public void onResponse(JSONObject response) {
                try {

                    JSONArray usuarioArray = response.getJSONArray("detalle");
                    for (int i = 0; i < usuarioArray.length(); i++) {
                        JSONObject RowDetalle = usuarioArray.getJSONObject(i);
                        pilatitud.add(RowDetalle.getDouble("Latitud"));
                        pilongitud.add(RowDetalle.getDouble("Longitud"));
                    }


                } catch (JSONException e) {
                    Toast.makeText(getApplicationContext(), "Error "+e, Toast.LENGTH_SHORT).show();
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


    private void ejecutar(){
        final Handler handler= new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {

                metodoEjecutar();

            }
        },2000);
    }
    private void metodoEjecutar() {
        LatLng pfinal = null;
        LatLng pinicial =null;

        pinicial = new LatLng(pilatitud.get(0),pilongitud.get(0));
        pfinal = new LatLng(pilatitud.get(1),pilongitud.get(1));

        pfinal = new LatLng(pilatitud.get(0),pilongitud.get(0));

        for(int i = 0;i<pilatitud.size() ;i++)
        {
            pfinal = pinicial;
            pinicial = new LatLng(pilatitud.get(i),pilongitud.get(i));
            mMap.addMarker(new MarkerOptions().position(pinicial).icon(BitmapDescriptorFactory.fromResource(R.drawable.corredor)));
            mMap.addPolyline(new PolylineOptions().add(pinicial,pfinal ).width(7).color(Color.RED).geodesic(true));
        }
        mMap.addMarker(new MarkerOptions().position(pfinal).icon(BitmapDescriptorFactory.fromResource(R.drawable.descansando)));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pfinal, 15));


    }
}