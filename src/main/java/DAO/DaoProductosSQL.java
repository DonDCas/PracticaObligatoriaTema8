package DAO;

import models.Producto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.Properties;

public class DaoProductosSQL implements DaoProductos {
    public ArrayList<Producto> readAll (DAOManager dao){
        ArrayList<Producto> todosLosProductos = new ArrayList<>();
        Producto producto;
        String sentencia = "Select * from Productos";
        try{
            dao.open();
            PreparedStatement ps = dao.getConn().prepareStatement(sentencia);
            try (ResultSet rs = ps.executeQuery()){
                while (rs.next()){
                    producto = new Producto(
                            rs.getInt("id"),
                            rs.getString("Marca"),
                            rs.getString("Modelo"),
                            rs.getString("Descripcion"),
                            rs.getFloat("Precio"),
                            rs.getInt("Relevancia")
                    );
                    todosLosProductos.add(producto);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return todosLosProductos;
    }
}
