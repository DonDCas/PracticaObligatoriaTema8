package models;

import java.io.Serializable;

public class Producto implements Serializable {
    private int id;
    private String marca;
    private String modelo;
    private String descripcion;
    private float precio;
    private int relevancia;

    //Constructor
    public Producto(int id, String marca, String modelo, String descripcion, float precio, int relevancia) {
        this.id = id;
        this.marca = marca;
        this.modelo = modelo;
        this.descripcion = descripcion;
        this.precio = precio;
        this.relevancia = relevancia;
    }

    //Constructor Copia
    public Producto(Producto producto){
        id = producto.id;
        marca = producto.marca;
        modelo = producto.modelo;
        descripcion = producto.descripcion;
        precio = producto.precio;
        relevancia = producto.relevancia;
    }

    // Getter y Setter


    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getMarca() {
        return marca;
    }

    public void setMarca(String marca) {
        this.marca = marca;
    }

    public String getModelo() {
        return modelo;
    }

    public void setModelo(String modelo) {
        this.modelo = modelo;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public float getPrecio() {
        return precio;
    }

    public void setPrecio(float precio) {
        this.precio = precio;
    }

    public int getRelevancia() {
        return relevancia;
    }

    public void setRelevancia(int relevancia) {
        this.relevancia = relevancia;
    }

    //Otros metodos
    private void calculaRelevancia(){}

    @Override
    public String toString() {
        return "Producto{" +
                "id=" + id +
                ", marca='" + marca + '\'' +
                ", modelo='" + modelo + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", precio=" + precio +
                ", relevancia=" + relevancia +
                '}';
    }

}
