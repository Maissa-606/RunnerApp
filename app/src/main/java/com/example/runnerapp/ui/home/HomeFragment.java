package com.example.runnerapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.runnerapp.R;
import com.example.runnerapp.databinding.ActivityMain2Binding;
import com.example.runnerapp.databinding.FragmentHomeBinding;

public class HomeFragment extends Fragment {
    ImageView foto;
    EditText nombre,peso,altura,pais,edad,fechaNac,correo;

    private FragmentHomeBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        HomeViewModel homeViewModel = new ViewModelProvider(this).get(HomeViewModel.class);


        binding = FragmentHomeBinding.inflate(inflater, container, false);
        View root = binding.getRoot();

        nombre = (EditText) binding.fhtxtnombre;
        correo = (EditText) binding.fhcorreo;
        pais = (EditText) binding.fhtxtPais;
        //edad = (EditText) binding.fh
        peso = (EditText) binding.fhpeso;
        altura = (EditText) binding.fhaltura;
        fechaNac = (EditText) binding.fhfecha;
        foto = (ImageView) binding.fhImage;
        nombre.setText("prueba");

        //final TextView textView = binding.textHome;
        //homeViewModel.getText().observe(getViewLifecycleOwner(), textView::setText);
        return root;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }



}