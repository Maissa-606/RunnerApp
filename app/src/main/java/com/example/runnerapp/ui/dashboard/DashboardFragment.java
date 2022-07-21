package com.example.runnerapp.ui.dashboard;

import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.runnerapp.ActivityNuevaCarrera;
import com.example.runnerapp.R;
import com.example.runnerapp.databinding.ActivityMapsBinding;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;

import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class DashboardFragment extends Fragment implements OnMapReadyCallback {



    private GoogleMap mMap;
    private ActivityMapsBinding binding;



    LatLng pinicial;
    public static Double km =0.0;
    public static List<Double> recorridoMapLatitud = new ArrayList<>();
    public static List<Double> recorridoMapLongitud = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // Retrieve the content view that renders the map.

        binding = ActivityMapsBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);




        return root;
    }


    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;




        mMap.getUiSettings().setZoomControlsEnabled(true);

        ejecutar();

    }

    public double CalcularDistanciaenKM(LatLng StartP, LatLng EndP) {
        int Radius=6371;//radius of earth in Km
        double lat1 = StartP.latitude;
        double lat2 = EndP.latitude;
        double lon1 = StartP.longitude;
        double lon2 = EndP.longitude;
        double dLat = Math.toRadians(lat2-lat1);
        double dLon = Math.toRadians(lon2-lon1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.asin(Math.sqrt(a));
        double valueResult= Radius*c;
        double km=valueResult/1;
        DecimalFormat newFormat = new DecimalFormat("####");
        int kmInDec =  Integer.valueOf(newFormat.format(km));
        double meter=valueResult%1000;
        int  meterInDec= Integer.valueOf(newFormat.format(meter));
        Log.i("Radius Value",""+valueResult+"   KM  "+kmInDec+" Meter   "+meterInDec);
        return Radius * c;
    }

    private void ejecutar(){
        final Handler handler= new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                metodoEjecutar();//llamamos nuestro metodo
                handler.postDelayed(this,5000);//se ejecutara cada 5 segundos
            }
        },2000);//empezara a ejecutarse después de 5 milisegundos
    }
    private void metodoEjecutar() {
        //valida si tiene los permisos de ser asi manda a llamar el metodo locationStart()

        Double latitud = 0.0 ;
        latitud= ActivityNuevaCarrera.getlatitud();

        Double longitud = 0.0;
        longitud= ActivityNuevaCarrera.getLongitud();
         LatLng pfinal = new LatLng(latitud, longitud);

        mMap.addMarker(new MarkerOptions().position(pfinal).title("Punto Inicial").icon(BitmapDescriptorFactory.defaultMarker()));


        if (ActivityNuevaCarrera.btnComenzar.getText().equals("Comenzar")){
            pinicial= new LatLng(latitud, longitud);
//            mMap.addMarker(new MarkerOptions().position(pinicial).title("Punto Inicial").icon(BitmapDescriptorFactory.defaultMarker()));
//            Toast.makeText(getContext(), "Punto Inicial"+pinicial, Toast.LENGTH_SHORT).show();
        }

        if(ActivityNuevaCarrera.btnComenzar.getText().equals("DETENER")) {
            if (pinicial != null) {
                Double distancia = 0.0;
                pfinal = pinicial;//actualizamos el punto inicial con el final
                pinicial = new LatLng(latitud, longitud);
                mMap.addPolyline(new PolylineOptions().add(pinicial, pfinal).width(7).color(Color.RED).geodesic(true));
                DecimalFormat df = new DecimalFormat("#.00");
                distancia = Double.valueOf(df.format(CalcularDistanciaenKM(pinicial, pfinal)));
                km = km + distancia;
                mMap.addMarker(new MarkerOptions().position(pinicial).title("Punto Inicial").icon(BitmapDescriptorFactory.fromResource(R.drawable.corredor)));
                recorridoMapLongitud.add(longitud);
                recorridoMapLatitud.add(latitud);

            }
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(pfinal, 17));

    }


}