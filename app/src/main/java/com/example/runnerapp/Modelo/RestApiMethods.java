package com.example.runnerapp.Modelo;

public class RestApiMethods {
    private static final String ipaddress = "juancarlossanchez.000webhostapp.com/";
    public static final String StringHttp = "http://";
    //EndPoint Urls
    private static final String GetAmigosAgregados = "/API/listaamigos.php";
    private static final String GetBuscar = "/API/validarLogin.php";
    private static final String getBuscarCorreo = "/API/listasingleusuario.php";
    private static final String setUpdate = "/API/actualizarusuario.php";
    private static final String CreateUsuario = "/API/crearusuario.php";
    private static final String listaPaises = "/API/listapaises.php";
    private static final String listaUsuarioPaise = "/API/listasingleusuariopais.php";
    private static final String ListaAgregarAmigo = "/API/listaagregaramigos.php";
    private static final String agregarAmigo = "/API/crearamigo.php";
    private static final String guardarActividad = "/API/crearactividad.php";
    private static final String detallesguardarActividad = "/API/creardetalleactividad.php";
    private static final String EliminarAmigos = "/API/eliminaramigo.php";
    private static final String listaActividades = "/API/listaactividades.php";
    private static final String mostrarEstadistica = "/API/estadisticas.php";
    private static final String listatop = "/API/listaranking.php";


    //metodo get
    //public static final String EndPointGetContact = StringHttp + ipaddress + GetEmple;
    public static final String EndPointValidarLogin = StringHttp + ipaddress + GetBuscar;
    public static final String EndPointBuscarCorreo = StringHttp + ipaddress + getBuscarCorreo;

    public static final String EndPointSetUpdateUser= StringHttp + ipaddress + setUpdate;
    public static final String EndPointCreateUsuario = StringHttp + ipaddress + CreateUsuario;
    public static final String EndPointListarPaises = StringHttp + ipaddress + listaPaises;
    public static final String EndPointListarUsuarioPaise = StringHttp + ipaddress + listaUsuarioPaise;
    public static final String EndPointAgregarAmigo = StringHttp + ipaddress + agregarAmigo;
    public static final String EndPointListarAmigo = StringHttp + ipaddress + ListaAgregarAmigo;
    public static final String GuardarActidad = StringHttp + ipaddress + guardarActividad;
    public static final String DetallesGuardarActidad = StringHttp + ipaddress + detallesguardarActividad;
    public static final String EndPointListaAmigosAdd = StringHttp + ipaddress + GetAmigosAgregados;
    public static final String EndPointEliminarAmigosAdd = StringHttp + ipaddress + EliminarAmigos;
    public static final String ListarActividades = StringHttp + ipaddress + listaActividades;
    public static final String Estadistico = StringHttp + ipaddress + mostrarEstadistica;
    public static final String listaClasifiacion = StringHttp + ipaddress + listatop;


    public static String EndPointBuscarUsuario;

    public static  String correo = "";
    public static  String codigo_usuario = "";

    String prueba = "http://juancarlossanchez.000webhostapp.com/API/listasingleusuariopais.php";
}


