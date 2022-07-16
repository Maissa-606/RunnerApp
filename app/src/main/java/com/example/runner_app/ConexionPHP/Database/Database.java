package com.example.runner_app.ConexionPHP.Database;

public class Database
{

    private static final String SHttp = "http://";
    private static final String ipaddress= "192.168.20.22/"; //FQDN
    private static final String WebApi = "Runner_App/";
    private static final String GETUsuarios="ListaUsuario.php"; //BASE DE DATOS GENERAL
    private static final String CREATEUsuarios="CrearUsuario.php"; //BASE DE DATOS GENERAL




    public static final String EndpointGETUsuarios = SHttp + ipaddress + WebApi + GETUsuarios;
    public static final String EndpointCREATEUsuarios = SHttp + ipaddress + WebApi + CREATEUsuarios;



}
