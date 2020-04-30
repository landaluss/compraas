package com.chema.myapplication;

public class Entidad {

    private String id;
    private String fecha;
    private String nombreCompra;
    private String compra;

    public Entidad(String id , String fecha , String nombreCompra , String compra){
        this.id = id;
        this.fecha = fecha;
        this.nombreCompra = nombreCompra;
        this.compra = compra;
    }

    public String getid(){ return id; }
    public String getFecha(){ return fecha; }
    public String getNombreCompra(){ return nombreCompra; }
    public String getCompra(){ return compra; }

}
