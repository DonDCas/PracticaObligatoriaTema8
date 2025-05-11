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

    @Override
    public ArrayList<Producto> readByDescripcion(DAOManager dao, String descripcipcion) {
        ArrayList<Producto> todosLosProductos = new ArrayList<>();
        Producto producto;
        String sentencia = "Select * from Productos where descripcion like (\"%"+descripcipcion+"%\")";
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

    @Override
    public ArrayList<Producto> readByMarcas(DAOManager dao, String marca) {
        ArrayList<Producto> todosLosProductos = new ArrayList<>();
        Producto producto;
        String sentencia = "Select * from Productos where marca like (\"%"+marca+"%\")";
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

    @Override
    public ArrayList<Producto> readByModelo(DAOManager dao, String modelo) {
        ArrayList<Producto> todosLosProductos = new ArrayList<>();
        Producto producto;
        String sentencia = "Select * from Productos where modelo like (\"%"+modelo+"%\")";
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

    @Override
    public ArrayList<Producto> readByRangoPrecios(DAOManager dao, float precioMin, float precioMax) {
        ArrayList<Producto> todosLosProductos = new ArrayList<>();
        Producto producto;
        String sentencia = "Select * from Productos where precio>= "+precioMin+" and precio<="+precioMax;
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

    @Override
    public Producto readById(DAOManager dao, int id) {
        Producto producto;
        String sentencia = "Select * from Productos where id = "+id;
        try{
            dao.open();
            PreparedStatement ps = dao.getConn().prepareStatement(sentencia);
            try (ResultSet rs = ps.executeQuery()){
                    producto = new Producto(
                            rs.getInt("id"),
                            rs.getString("Marca"),
                            rs.getString("Modelo"),
                            rs.getString("Descripcion"),
                            rs.getFloat("Precio"),
                            rs.getInt("Relevancia"));
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return producto;
    }
}
