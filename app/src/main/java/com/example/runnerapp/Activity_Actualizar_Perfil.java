package com.example.runnerapp;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.runnerapp.Modelo.Pais;
import com.example.runnerapp.Modelo.RestApiMethods;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Activity_Actualizar_Perfil extends AppCompatActivity {

    @Override public void onBackPressed() { }


    private static final String TAG = "MainActivity";
    private TextView mDisplayDate;
    private DatePickerDialog.OnDateSetListener mDateSetListener;


    EditText txtNombre,txtApellido,txtTelefono;
    TextView peso, altura,txtFechaNac;
    Spinner SpiPais;
    TextView btnActualizar;
    LinearLayout btnTomarFoto,btnSelectGaleria;
    ImageView Foto;
    String fotoString,email;
    Bitmap imagen;
    static final int RESULT_GALLERY_IMG = 200;
    static final int PETICION_ACCESO_CAM = 100;
    static final int TAKE_PIC_REQUEST = 101;
    Pais pais;
    ArrayList<String> arrayPaises;

    int codigoPaisSeleccionado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_actualizar_perfil);
        peso =  (TextView) findViewById(R.id.actuPeso);
        altura =  (TextView) findViewById(R.id.actuAltura);

        peso.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seleccionarPeso();
            }
        });

        altura.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                seleccionarAltura();
            }
        });

        mDisplayDate = (TextView) findViewById(R.id.actuFechaNac);


        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        Activity_Actualizar_Perfil.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        mDateSetListener,
                        year,month,day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                month = month + 1;
                Log.d(TAG, "onDateSet: mm/dd/yyy: " + day  + "/" + month + "/" + year);

                String date =  day  + "/" + month + "/" + year;
                mDisplayDate.setText(date);
            }
        };

        txtNombre = (EditText) findViewById(R.id.actutxtnombre);
        txtApellido =  (EditText) findViewById(R.id.actutxtapellidos);
        txtFechaNac =  (TextView) findViewById(R.id.actuFechaNac);
        txtTelefono =(EditText) findViewById(R.id.actxtTelefono);
        SpiPais =  (Spinner)  findViewById(R.id.actucmbPais);


        btnActualizar = findViewById(R.id.actbtnActualizar);
        btnTomarFoto = findViewById(R.id.actuTomarFoto);
        btnSelectGaleria = findViewById(R.id.actugaleria);

        Foto =(ImageView) findViewById(R.id.actuImgView);

        email = getIntent().getStringExtra("email");
        String nombres =getIntent().getStringExtra("nombres");
        String apellidos =getIntent().getStringExtra("apellidos");
        String fechaNac =getIntent().getStringExtra("fechanac");
        String telefono = getIntent().getStringExtra("telefono");
        String peso =getIntent().getStringExtra("peso");
        String altura =getIntent().getStringExtra("altura");
        fotoString =getIntent().getStringExtra("foto");

        txtNombre.setText(nombres);
        txtApellido.setText(apellidos);
        txtFechaNac.setText(fechaNac);
        txtTelefono.setText(telefono);
        this.peso.setText(peso);
        this.altura.setText(altura);
        mostrarFoto(fotoString);

        comboboxPaises();



        SpiPais.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String cadena = adapterView.getSelectedItem().toString();

                codigoPaisSeleccionado = Integer.valueOf(extraerNumeros(cadena).toString().replace("]","").replace("[",""));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnActualizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences mSharedPrefs = getSharedPreferences("credencialesPublicas", Context.MODE_PRIVATE);
                String correo = mSharedPrefs.getString("correo","");
                ActualizarDatos(correo);
            }
        });

        btnTomarFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permisos();
            }
        });

        btnSelectGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GaleriaImagenes();
            }
        });

    }


    public void mostrarFoto(String foto) {
        try {
            String base64String = "data:image/png;base64,"+foto;
            String base64Image = base64String.split(",")[1];
            byte[] decodedString = Base64.decode(base64Image, Base64.DEFAULT);
            Bitmap decodedByte = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
            Foto.setImageBitmap(decodedByte);//setea la imagen al imageView
        }catch (Exception ex){
            ex.toString();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri imageUri;
        //obtener la iamgen por el almacenamiento interno
        if(resultCode==RESULT_OK && requestCode==RESULT_GALLERY_IMG) {

            imageUri = data.getData();
            Foto.setImageURI(imageUri);
            try {
                imagen= MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageUri);

            }catch (Exception e)
            {
                Toast.makeText(getApplicationContext(),"Error al seleccionar imagen", Toast.LENGTH_SHORT).show();
            }
        }
        //obtener la iamgen por la camara
        if(requestCode == TAKE_PIC_REQUEST && resultCode == RESULT_OK)
        {
            Bundle extras = data.getExtras();
            imagen = (Bitmap) extras.get("data");
            Foto.setImageBitmap(imagen);
        }

    }

    private void ActualizarDatos(String correo) {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        HashMap<String, String> parametros = new HashMap<>();
        String fotoString2 = GetStringImage(imagen);
        if (fotoString2.equals("")||fotoString2.isEmpty()||fotoString2.equals(null)){
            fotoString2 = fotoString;
        }

        parametros.put("nombres", txtNombre.getText().toString());
        parametros.put("apellidos", txtApellido.getText().toString());
        parametros.put("fecha_nac", txtFechaNac.getText().toString());
        parametros.put("telefono",txtTelefono.getText().toString());
        parametros.put("peso", peso.getText().toString());
        parametros.put("altura", altura.getText().toString());
        parametros.put("email", correo);
        parametros.put("codigo_pais", codigoPaisSeleccionado+"");
        parametros.put("foto", fotoString2);


        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, RestApiMethods.EndPointSetUpdateUser,
                new JSONObject(parametros), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Intent intent = new Intent(getApplicationContext(), Activity_Perfil.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP );
                    startActivity(intent);
                    Toast.makeText(getApplicationContext(), response.getString("mensaje").toString(), Toast.LENGTH_SHORT).show();

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

        Intent intent = new Intent(getApplicationContext(),Activity_Perfil.class);
        startActivity(intent);
    }


    private String GetStringImage(Bitmap photo) {

        try {
            ByteArrayOutputStream ba = new ByteArrayOutputStream();
            photo.compress(Bitmap.CompressFormat.JPEG, 70, ba);
            byte[] imagebyte = ba.toByteArray();
            String encode = Base64.encodeToString(imagebyte, Base64.DEFAULT);

            return encode;
        }catch (Exception ex)
        {
            ex.toString();
        }
        return "";
    }

    private void permisos() {

        if(ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED)
        {
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.CAMERA},PETICION_ACCESO_CAM);
        }else{
            tomarFoto();
        }
    }

    private void tomarFoto() {
        Intent takepic = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if(takepic.resolveActivity(getPackageManager()) != null)
        {
            startActivityForResult(takepic,TAKE_PIC_REQUEST);
        }
    }

    private void GaleriaImagenes() {

        Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent,"Seleccione la aplicacion"),RESULT_GALLERY_IMG);

    }

    List<Integer> extraerNumeros(String cadena) {
        List<Integer> todosLosNumeros = new ArrayList<Integer>();
        Matcher encuentrador = Pattern.compile("\\d+").matcher(cadena);
        while (encuentrador.find()) {
            todosLosNumeros.add(Integer.parseInt(encuentrador.group()));
        }
        return todosLosNumeros;
    }

    private void comboboxPaises(){
        RequestQueue queue = Volley.newRequestQueue(this);

        StringRequest stringRequest = new StringRequest(Request.Method.GET, RestApiMethods.EndPointListarPaises,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            JSONArray contactoArray = jsonObject.getJSONArray( "pais");

                            arrayPaises = new ArrayList<>();
                            for (int i=0; i<contactoArray.length(); i++)
                            {
                                JSONObject RowPais = contactoArray.getJSONObject(i);
                                pais = new Pais(  RowPais.getInt("codigo_pais"),
                                        RowPais.getString("nombre")
                                );

                                arrayPaises.add(pais.getNombre() + " ["+pais.getId()+"]");
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, arrayPaises);
                            SpiPais.setAdapter(adapter);

                        }catch (JSONException ex){
                            Toast.makeText(getApplicationContext(), "mensaje "+ex, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new Response.ErrorListener(){
            @Override
            public void onErrorResponse(VolleyError error){
                Toast.makeText(getApplicationContext(), "mensaje "+error, Toast.LENGTH_SHORT).show();
            }
        });
        queue.add(stringRequest);
    }

    private void seleccionarPeso(){
        LayoutInflater inflater = this.getLayoutInflater();
        View item = inflater.inflate(R.layout.pickerpeso, null);
        NumberPicker picker1= (NumberPicker) item.findViewById(R.id.number1);
        NumberPicker picker2= (NumberPicker) item.findViewById(R.id.number2);

        picker1.setMaxValue(999);
        picker1.setMinValue(0);
        picker2.setMaxValue(9);
        picker2.setMinValue(0);
        picker1.setValue(100);

        NumberPicker.OnValueChangeListener changeListener = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker1, int oldVal, int newVal) {
                peso.setText(picker1.getValue()+" . "+picker2.getValue());

            }
        };

        NumberPicker.OnValueChangeListener change = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker2, int oldVal, int newVal) {
                peso.setText(picker1.getValue()+" . "+picker2.getValue());
            }
        };

        picker1.setOnValueChangedListener(changeListener);
        picker2.setOnValueChangedListener(change);



        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setView(item);
        builder.setTitle("Peso");
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }
    private void seleccionarAltura(){
        LayoutInflater inflater = this.getLayoutInflater();
        View item = inflater.inflate(R.layout.pickeraltura, null);
        NumberPicker picker1= (NumberPicker) item.findViewById(R.id.numberpickerAltura);
        picker1.setMinValue(0);
        picker1.setMaxValue(300);
        picker1.setValue(100);

        NumberPicker.OnValueChangeListener changeListener = new NumberPicker.OnValueChangeListener() {
            @Override
            public void onValueChange(NumberPicker picker1, int oldVal, int newVal) {
                altura.setText(picker1.getValue()+"");

            }
        };

        picker1.setOnValueChangedListener(changeListener);
        androidx.appcompat.app.AlertDialog.Builder builder = new androidx.appcompat.app.AlertDialog.Builder(this);
        builder.setView(item);
        builder.setTitle("Seleccione su altura");
        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {

            }
        });
        builder.show();
    }


}