package com.example.runnerapp;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Properties;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import cz.msebera.android.httpclient.Header;

public class recuperar extends Activity {


    Session session = null;
    ProgressDialog pdialog = null;
    EditText destinatario;
    TextView enviar;
    String para, asunto, mensaje;
    String usuario=null, password=null, nombrecompleto=null;
    private static final String URL_CONTRASENIA = "/API/recuperar_clave.php";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar);

        enviar = findViewById(R.id.btnenviar);
        destinatario = findViewById(R.id.txtrecuperar);


        enviar.setOnClickListener((v) -> {
            if(isEmailValid(destinatario.getText().toString())){
                usuario = null;

                obtener_datos_usuarios();
            }else {
                destinatario.setError("Correo Invalido");

            }
        });
    }

    private boolean isEmailValid(String email) {
        boolean isValid = false;
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        CharSequence inputStr = email;

        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(inputStr);

        if (matcher.matches()) {
            isValid = true;

        }
        return isValid;

    }
    private void obtener_datos_usuarios (){
        final ProgressDialog progressDialog = new ProgressDialog(recuperar.this);
        progressDialog.setMessage("Cargado datos");
        progressDialog.setCancelable(false);
        progressDialog.show();


        AsyncHttpClient client = new AsyncHttpClient();

        RequestParams params = new RequestParams();
        params.put("correo", destinatario.getText().toString());
        client.post(URL_CONTRASENIA, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200){
                    progressDialog.dismiss();
                    try{
                        final JSONArray jsonArray = new JSONArray(new String(responseBody));
                        for (int i = 0; i < jsonArray.length(); i++){
                            JSONObject jsonObjectInside = jsonArray.getJSONObject(i);
                            usuario = jsonObjectInside.getString("email");
                            password = jsonObjectInside.getString("clave");
                            nombrecompleto = jsonObjectInside.getString("nombrecompleto");
                        }
                        if (usuario!=null){
                            configurar_envio();

                        }else {
                            Toast.makeText(getApplicationContext(),"El correo no esta en base de datos",Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(getApplicationContext(), "Error en Datos..", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, byte[] responseBody, Throwable error) {
                Toast.makeText(getApplicationContext(), "Error en el servidor", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();

            }
        });

    }
    public void configurar_envio(){
        para = destinatario.getText().toString();
        asunto = "Recuperacion de contraseña - RUNNING HN";
        mensaje = "Hola "+nombrecompleto+", \n"+"Su usuario es: "+usuario+"\n"+ "Su contraseña es: "+password;

         Properties properties = new Properties ();


        properties.put("mail.smtp.host", "smtp.gmail.com");
        properties.put("mail.smtp.starttls.enable", "true");
        properties.put("mail.smtp.port", "25");
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


        enviar_correo task = new enviar_correo();
        task.execute();


    }
    class enviar_correo extends AsyncTask<String, Void, String>{


        @Override
        protected String doInBackground(String... strings) {
            try {
                MimeMessage message = new MimeMessage(session);
                message.setFrom(new InternetAddress("RunnighnHondu@gmail.com"));
                message.addRecipient(Message.RecipientType.TO,
                        new InternetAddress(para));
                message.setSubject(asunto);
                message.setText(mensaje);

                Transport t = session.getTransport("smtp");
                t.connect("RunnighnHondu@gmail.com","Hondras12.");
                t.sendMessage(message, message.getAllRecipients());

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

}