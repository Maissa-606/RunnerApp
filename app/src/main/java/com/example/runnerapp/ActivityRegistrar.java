package com.example.runnerapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.InputType;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MotionEvent;
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
import java.util.Properties;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

public class ActivityRegistrar extends AppCompatActivity {


    private static final String TAG = "MainActivity";
    private TextView mDisplayDate, fechaNac;
    private DatePickerDialog.OnDateSetListener mDateSetListener;



    Session session = null;
    ProgressDialog pdialog = null;
    String para, asunto, mensaje;

    EditText nombres, apellidos, telefono, correo, contrasenia1, contrasenia2 ;
    TextView peso, altura,txtMostrarContra;
    Spinner cmbpais;
    TextView btnguardar, btnatras;
    LinearLayout btnTomaFoto,btnGaleria;
    ImageView btnMostrarContra;
    String contrasenia;
    ImageView Foto;

    Pais pais;
    List<Pais> paisList;
    ArrayList<String> arrayPaises;
    ArrayAdapter adp;
    int codigoPaisSeleccionado;

    int codigo;


    Intent intent;

    static final int RESULT_GALLERY_IMG = 200;
    static final int PETICION_ACCESO_CAM = 100;
    static final int TAKE_PIC_REQUEST = 101;
    Bitmap imagen;
    final Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registrar);
        Foto = (ImageView) findViewById(R.id.imageView);
        nombres = (EditText) findViewById(R.id.rtxtnombres);
        apellidos = (EditText) findViewById(R.id.rtxtapellidos);
        telefono = (EditText) findViewById(R.id.rtxtTelefono);
        correo = (EditText) findViewById(R.id.rtxtcorreo);
        contrasenia1 = (EditText) findViewById(R.id.rtxtcontraseña1);
        contrasenia2 = (EditText) findViewById(R.id.rtxtcontraseña2);
        fechaNac = (TextView) findViewById(R.id.rtxtFechaNacimiento);
        peso = (TextView) findViewById(R.id.rtxtPeso);
        altura = (TextView) findViewById(R.id.rtxtAltura);
        cmbpais = (Spinner) findViewById(R.id.rcmbPais);
        btnguardar = findViewById(R.id.rbtnGuardar);
        btnTomaFoto = findViewById(R.id.rbtnTomarFoto);
        btnGaleria = findViewById(R.id.rbtngaleria);
        btnMostrarContra = findViewById(R.id.arbtnMostrarContra);
        btnatras = findViewById(R.id.regbtnAtras);

        btnatras.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        intent = new Intent(getApplicationContext(), ActivityRegistrar.class);//para obtener el contacto seleccionado mas adelante

        btnMostrarContra.setOnTouchListener(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {

                switch ( event.getAction() ) {
                    case MotionEvent.ACTION_DOWN:
                        contrasenia1.setInputType(InputType.TYPE_CLASS_TEXT);
                        contrasenia2.setInputType(InputType.TYPE_CLASS_TEXT);
                        break;
                    case MotionEvent.ACTION_UP:
                        contrasenia1.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        contrasenia2.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_VARIATION_PASSWORD);
                        break;
                }
                return true;
            }
        });



        Random random = new Random();
        codigo = random.nextInt(8999) + 1000;
//        Toast.makeText(getApplicationContext(),"codigo: "+codigo,Toast.LENGTH_SHORT).show();
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

        btnguardar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                validarDatos();
//                configurar_envio();


            }
        });

        comboboxPaises();
        cmbpais.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                //setComboboxSeleccionado();//obtengo el usuario seleccionado de la lista
                String cadena = adapterView.getSelectedItem().toString();

                //Quitar los caracteres del combobox para obtener solo el codigo del pais
                codigoPaisSeleccionado = Integer.valueOf(extraerNumeros(cadena).toString().replace("]", "").replace("[", ""));

                //Toast.makeText(getApplicationContext(),"usuario id: "+codigoPaisSeleccionado, Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btnGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        btnTomaFoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                permisos();
            }
        });

        btnGaleria.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                GaleriaImagenes();
            }
        });

        //Inicio de codigo Fecha-------------------------------------------------------------
        mDisplayDate = (TextView) findViewById(R.id.rtxtFechaNacimiento);

        //--------------------------------------------------------
        mDisplayDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        ActivityRegistrar.this,
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
        //---------------------Fin de codigo de la fecha-------------------------------------------

    }



    private void validarDatos(){
        if (Foto.getDrawable() == null){
            Toast.makeText(getApplicationContext(), "Debe agregar una Fotografia" ,Toast.LENGTH_LONG).show();
        }else if(nombres.getText().toString().equals("")){
            Toast.makeText(getApplicationContext(), "Debe de escribir un nombre" ,Toast.LENGTH_LONG).show();
        }else if(apellidos.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Debe de escribir un apellido", Toast.LENGTH_LONG).show();
        }else if(fechaNac.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Selecione una fecha de nacimiento", Toast.LENGTH_LONG).show();
        }else if(arrayPaises.size()==0) {
            Toast.makeText(getApplicationContext(), "Debe selecionar un pais", Toast.LENGTH_LONG).show();
        }else if(telefono.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Debe de escribir un telefono", Toast.LENGTH_LONG).show();
        }else if(correo.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Debe de escribir un Correo", Toast.LENGTH_LONG).show();
        }else if(contrasenia1.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Debe de escribir un contraseña", Toast.LENGTH_LONG).show();
        }else if(contrasenia2.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Reescriba su contraseña", Toast.LENGTH_LONG).show();
        }else if(peso.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Debe de seleccionar su peso", Toast.LENGTH_LONG).show();
        }else if(altura.getText().toString().equals("")) {
            Toast.makeText(getApplicationContext(), "Debe de seleccionar su altura", Toast.LENGTH_LONG).show();
        }else{
            validarContrasenia();
        }
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

    //**Entrar a la carpeta de fotos del telefono**//
    private void GaleriaImagenes() {
        Intent intent = new Intent(Intent.ACTION_PICK,MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.setType("image/");
        startActivityForResult(intent.createChooser(intent,"Seleccione la aplicacion"),RESULT_GALLERY_IMG);
    }

    //***Metodo para convertir imagen***//
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PETICION_ACCESO_CAM)
        {
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED)
            {
                tomarFoto();
            }
        }else{
            Toast.makeText(getApplicationContext(),"Se necesitan permisos",Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Uri imageUri;
        //obtener la iamgen por el almacenamiento interno
        if(resultCode==RESULT_OK && requestCode==RESULT_GALLERY_IMG)
        {

            imageUri = data.getData();
            Foto.setImageURI(imageUri);
            try {
                imagen=MediaStore.Images.Media.getBitmap(this.getContentResolver(),imageUri);

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

    List<Integer> extraerNumeros(String cadena) {
        List<Integer> todosLosNumeros = new ArrayList<Integer>();
        Matcher encuentrador = Pattern.compile("\\d+").matcher(cadena);
        while (encuentrador.find()) {
            todosLosNumeros.add(Integer.parseInt(encuentrador.group()));
        }
        return todosLosNumeros;
    }

    private String validarContrasenia() {
        if (contrasenia1.getText().toString().equals(contrasenia2.getText().toString())){
            contrasenia = contrasenia1.getText().toString();
            configurar_envio();
            final EditText taskEditText = new EditText(context);
            AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(context);
            alertDialogBuilder.setTitle("Verifique su correo");
            alertDialogBuilder
                    .setMessage("hemos enviado un correo con su codigo de verificación")
                    .setView(taskEditText)
                    .setCancelable(true)
                    .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
                                    int task = Integer.valueOf(taskEditText.getText().toString());
                                    if (codigo == task) {
                                        RegistrarUsuario();
                                        Toast.makeText(getApplicationContext(), "codigo valido", Toast.LENGTH_SHORT).show();
                                    } else {
                                        Toast.makeText(getApplicationContext(), "codigo invalido", Toast.LENGTH_SHORT).show();
                                    }


                                }
                            }

                    );

            // create alert dialog
            AlertDialog alertDialog = alertDialogBuilder.create();
            alertDialog.show();

        }
        else {
            Toast.makeText(getApplicationContext(), "Verificacion de contraseña incorrecta.", Toast.LENGTH_SHORT).show();

        }
        return contrasenia;
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
//                            arrayPaises.clear();//limpiar la lista de usuario antes de comenzar a listar
                            for (int i=0; i<contactoArray.length(); i++)
                            {
                                JSONObject RowPais = contactoArray.getJSONObject(i);
                                pais = new Pais(  RowPais.getInt("codigo_pais"),
                                        RowPais.getString("nombre")
                                );

                                arrayPaises.add(pais.getNombre() + " ["+pais.getId()+"]");
                            }

                            ArrayAdapter<String> adapter = new ArrayAdapter<String>(ActivityRegistrar.this, android.R.layout.simple_spinner_dropdown_item, arrayPaises);
                            cmbpais.setAdapter(adapter);

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

    private void RegistrarUsuario() {
        RequestQueue requestQueue = Volley.newRequestQueue(getApplicationContext());
        HashMap<String, String> parametros = new HashMap<>();

        String fotoString = GetStringImage(imagen);

        parametros.put("nombres", nombres.getText().toString());
        parametros.put("apellidos", apellidos.getText().toString());
        parametros.put("telefono", telefono.getText().toString());
        parametros.put("email", correo.getText().toString());
        parametros.put("clave", contrasenia);
        parametros.put("fecha_nac", fechaNac.getText().toString());
        parametros.put("peso", peso.getText().toString());
        parametros.put("altura", altura.getText().toString());
        parametros.put("codigo_pais", codigoPaisSeleccionado+"");
        parametros.put("foto",  fotoString);
        parametros.put("estado","1");

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, RestApiMethods.EndPointCreateUsuario,
                new JSONObject(parametros), new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    Toast.makeText(getApplicationContext(), "String Response " + response.getString("mensaje").toString(), Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),ActivityLogin.class);
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
        limpiar();
    }

    private void limpiar(){
        nombres.setText("");
        apellidos.setText("");
        telefono.setText("");
        correo.setText("");
        fechaNac.setText("");
        peso.setText("");
        altura.setText("");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    public void configurar_envio(){

        para = correo.getText().toString();
        asunto = "Verificación de correo - RUNNING HN";
        mensaje = "Hola "+nombres.getText().toString()+" "+apellidos.getText().toString()+", \n"+"Su codigo de verificacion es: "+codigo;


        Properties properties = new Properties ();


        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.port", "25");//puerto
        properties.put("mail.smtp.user", "RunnighnHondu@gmail.com");
        properties.put("mail.smtp.auth", "true");

        session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("RunnighnHondu@gmail.com", "Hondras12.");
            }
        });
        session.setDebug(true);

        pdialog = ProgressDialog.show(this, "","Enviando correo", true);


        ActivityRegistrar.enviar_correo task = new ActivityRegistrar.enviar_correo(); //llamamos a la clase enviar correo
        task.execute();


    }
    class enviar_correo extends AsyncTask<String, Void, String> {


        @Override
        protected String doInBackground(String... strings) {
            try {
                //Creando objeto MimeMessage
                MimeMessage message = new MimeMessage(session);
                //Configuracion de la direccion del remitente
                message.setFrom(new InternetAddress("RunnighnHondu@gmail.com"));
                //Anadimos el receptor
                message.addRecipient(Message.RecipientType.TO,
                        new InternetAddress(para));
                message.setSubject(asunto);
                message.setText(mensaje);

                //lo enviamos
                Transport t = session.getTransport("smtp");
                t.connect("RunnighnHondu@gmail.com","Hondras12.");
                t.sendMessage(message, message.getAllRecipients());

                //cierre
                t.close();


            } catch (Exception e) {
                e.printStackTrace();
            }
            return null;
        }
        @Override
        protected void onPostExecute(String result){
            pdialog.dismiss();
            Toast.makeText(getApplicationContext(),"Mensaje enviado", Toast.LENGTH_LONG).show();
        }
    }

    //------USO DE NUMBERPICKER DE SELECCION DE DATOS. ----------------------------
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