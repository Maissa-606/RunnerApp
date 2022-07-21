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


    Session session = null; //Inicio de session para autenticar usuario y password
    ProgressDialog pdialog = null; //dialogo del proceso
    EditText destinatario; //Ingreso del correo a enviar
//    Button enviar; //Boton para enviar el correo
    TextView enviar; //Boton para enviar el correo
    String para, asunto, mensaje;//Para datos del mensaje
    String usuario=null, password=null, nombrecompleto=null;
    //Variable que contiene la URL
    private static final String URL_CONTRASENIA = "http://transportweb2.online/API/recuperar_clave.php";


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recuperar);

    //identificamos el boton de envio
        enviar = findViewById(R.id.btnenviar);
        destinatario = findViewById(R.id.txtrecuperar);


    //Le damos funcionalidad al boton de envio
        enviar.setOnClickListener((v) -> {
            if(isEmailValid(destinatario.getText().toString())){ //preguntamos si es un mail valido
                usuario = null;

                obtener_datos_usuarios();
            }else {
                destinatario.setError("Correo Invalido");//Avisamos que hay un error en la sintaxis

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
        //Iniciando barra de progreso
        final ProgressDialog progressDialog = new ProgressDialog(recuperar.this);
        progressDialog.setMessage("Cargado datos");
        progressDialog.setCancelable(false);
        progressDialog.show();

        //URLS de los archivos php, desde llaman conexion con base de datos mysql alojada en servidor

        AsyncHttpClient client = new AsyncHttpClient();

        //Instanciamos los parametros a enviar
        RequestParams params = new RequestParams();
        //Agregar los parametros de correo y destinatario
        params.put("correo", destinatario.getText().toString());
        client.post(URL_CONTRASENIA, params, new AsyncHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, byte[] responseBody) {
                if(statusCode == 200){
                    //Si hay respuesta de servidor cerramos el dialogo
                    progressDialog.dismiss();
                    try{
                        final JSONArray jsonArray = new JSONArray(new String(responseBody)); //decodificamos el Json que nos viene del servidor
                        for (int i = 0; i < jsonArray.length(); i++){//Recorremos los datos
                            JSONObject jsonObjectInside = jsonArray.getJSONObject(i);
                            usuario = jsonObjectInside.getString("email");
                            password = jsonObjectInside.getString("clave");
                            nombrecompleto = jsonObjectInside.getString("nombrecompleto");
                        }
                        if (usuario!=null){ //si el usuario es diferente de nulo osea contiene datos
                            configurar_envio();//Procedemos a configurar el envio para luego enviar el correo

                        }else {
                            //en caso contrario no hay datos de usuario consultado y damos mensaje por pantalla
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
                // Si falla la peticion al servidor enviamos mensaje por pantalla y cerramos el dialogo
                Toast.makeText(getApplicationContext(), "Error en el servidor", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();

            }
        });

    }
    public void configurar_envio(){
        //Almacenamos los datos obtenido en sus respectivas variables para el envio del correo
        para = destinatario.getText().toString();
        asunto = "Recuperacion de contraseña - RUNNING HN";
        mensaje = "Hola "+nombrecompleto+", \n"+"Su usuario es: "+usuario+"\n"+ "Su contraseña es: "+password;

        //creamos las propiedades
        Properties properties = new Properties ();

        //configurando propiedades para email
        //si vamos a utilizar otro servidor tnemos que cambiar los valores
        properties.put("mail.smtp.host", "smtp.gmail.com");//host
        properties.put("mail.smtp.starttls.enable", "true");//Habilitar starttlls de smtp de correo
        properties.put("mail.smtp.port", "25");//puerto
        properties.put("mail.smtp.user", "RunnighnHondu@gmail.com");//correo de emisor
        properties.put("mail.smtp.auth", "true");//Autorizacion de envio

        //STARTTLS es una extencion a los protocolos de comunicacion de texto plano,
        //que ofrese una forma de mejorar desde una conexion de exto plano a una conexion cifrada,
        //(TLS O SSL) en lugar de utilizar un puerto diferente para la comunicacion cifrada.

        //Creamos la nueva sesion
        session = Session.getDefaultInstance(properties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication("RunnighnHondu@gmail.com", "Hondras12.");
            }
        });
        session.setDebug(true);//Esto es para depurar una vez funcione bien lo podemos quitar

        pdialog = ProgressDialog.show(this, "","Enviando correo", true);


        enviar_correo task = new enviar_correo(); //llamamos a la clase enviar correo
        task.execute();


    }
    class enviar_correo extends AsyncTask<String, Void, String>{


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

}