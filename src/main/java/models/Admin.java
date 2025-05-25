package models;

import java.io.Serializable;

public class Admin extends Usuario {

    // Constructor
    public Admin(String id, String nombre, String clave, String email, int movil) {
        super(id, nombre, clave, email, movil);
    }
}
