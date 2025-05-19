package models;

import java.io.Serial;
import java.io.Serializable;

public abstract class Usuario implements Serializable {
    protected String id;
    protected String nombre;
    protected String pass;
    protected String correo;
    protected int movil;

    public Usuario(String id, String nombre, String pass, String correo,int movil) {
        this.id = id;
        this.nombre = nombre;
        this.pass = pass;
        this.correo = correo;
        this.movil = movil;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getPass() {
        return pass;
    }

    public void setPass(String pass) {
        this.pass = pass;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public int getMovil() {
        return movil;
    }

    public void setMovil(int movil) {
        this.movil = movil;
    }

    //Metodo que sirve para loguearse en la app
    /*public boolean login(String correo, String pass) {
        if (correo.equals(this.correo) && (pass.equals(this.pass))) return true;
        return false;
    }*/
}
