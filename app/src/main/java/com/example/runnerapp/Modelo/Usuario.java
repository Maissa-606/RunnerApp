package com.example.runnerapp.Modelo;

public class Usuario
{
    public int id;
    public String nombres;
    public String apellidos;
    public String fechaNac;
    public String pais;
    public int codigo_pais;
    public String correo;
    public String contraseña;
    public String peso;
    public String altura;
    public String Latitud;
    public String Longitud;
    public String foto;



    public String telefono;

    public Usuario(int codigo_usuario, String nombres, String apellidos,String foto) {
        this.nombres = nombres;
        this.id = codigo_usuario;
        this.apellidos = apellidos;
        this.foto = foto;

    }

    public Usuario(int id, String nombres, String apellidos, String fechaNac, String pais, int codigo_pais, String correo, String peso, String altura, String foto, String telefono) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.fechaNac = fechaNac;
        this.pais = pais;
        this.codigo_pais = codigo_pais;
        this.correo = correo;
        this.peso = peso;
        this.altura = altura;
        this.foto = foto;
        this.telefono = telefono;
    }

    public Usuario(int id, String nombres, String apellidos, String correo,String contraseña, String peso, String altura, String latitud, String longitud, String foto) {
        this.id = id;
        this.nombres = nombres;
        this.apellidos = apellidos;
//        this.telefono = telefono;
        this.correo = correo;
        this.contraseña = contraseña;
        this.peso = peso;
        this.altura = altura;
        Latitud = latitud;
        Longitud = longitud;
        this.foto = foto;
    }

    public Usuario(String correo, String contraseña) {
        this.correo = correo;
        this.contraseña = contraseña;
    }


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }



    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContraseña() {
        return contraseña;
    }

    public void setContraseña(String contraseña) {
        this.contraseña = contraseña;
    }

    public String getPeso() {
        return peso;
    }

    public void setPeso(String peso) {
        this.peso = peso;
    }

    public String getAltura() {
        return altura;
    }

    public void setAltura(String altura) {
        this.altura = altura;
    }

    public String getLatitud() {
        return Latitud;
    }

    public void setLatitud(String latitud) {
        Latitud = latitud;
    }

    public String getLongitud() {
        return Longitud;
    }

    public void setLongitud(String longitud) {
        Longitud = longitud;
    }

    public String getFoto() {
        return foto;
    }

    public void setFoto(String foto) {
        this.foto = foto;
    }

    public String getFechaNac() {
        return fechaNac;
    }

    public void setFechaNac(String fechaNac) {
        this.fechaNac = fechaNac;
    }

    public String getPais() {
        return pais;
    }

    public void setPais(String pais) {
        this.pais = pais;
    }

    public int getCodigo_pais() {
        return codigo_pais;
    }

    public void setCodigo_pais(int codigo_pais) {
        this.codigo_pais = codigo_pais;
    }
    public String getTelefono() {
        return telefono;
    }

    public void setTelefono(String telefono) {
        this.telefono = telefono;
    }
}
