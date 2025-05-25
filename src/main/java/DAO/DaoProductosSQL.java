package DAO;

import models.Producto;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Properties;

public class DaoProductosSQL implements DaoProductos, Serializable {

    public ArrayList<Producto> readAll (DAOManager dao){
        String sentencia = "Select * from Productos";
        return resultadoSelectArrayList(dao, sentencia);
    }

    @Override
    public ArrayList<Producto> readByDescripcion(DAOManager dao, String descripcipcion) {
        String sentencia = "Select * from Productos where Descripcion like (\"%"+descripcipcion+"%\")";
        return resultadoSelectArrayList(dao, sentencia);
    }

    @Override
    public ArrayList<Producto> readByMarcas(DAOManager dao, String marca) {
        String sentencia = "Select * from Productos where Marca like (\"%"+marca+"%\")";
        return resultadoSelectArrayList(dao, sentencia);
    }

    @Override
    public ArrayList<Producto> readByModelo(DAOManager dao, String modelo) {
        String sentencia = "Select * from Productos where Modelo like (\"%"+modelo+"%\")";
        return resultadoSelectArrayList(dao, sentencia);
    }


    @Override
    public ArrayList<Producto> readByRangoPrecios(DAOManager dao, float precioMin, float precioMax) {
        String sentencia = "Select * from Productos where Precio>= "+precioMin+" and Precio<="+precioMax;
        return resultadoSelectArrayList(dao, sentencia);
    }

    @Override
    public Producto readById(DAOManager dao, int id) {
        Producto producto = null;
        String sentencia = "Select * from Productos where id_producto = "+id;
        try{
            dao.open();
            PreparedStatement ps = dao.getConn().prepareStatement(sentencia);
            try (ResultSet rs = ps.executeQuery()){
                if(rs.next()) producto = filaSelect(rs);
                dao.close();
                return producto;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean updateProducto(DAOManager dao, int id, Producto producto) {
        String precio = String.valueOf(producto.getPrecio());
        String sentencia = String.format("Update Productos SET" +
                " Marca = '%s'," +
                " Modelo = '%s'," +
                " Descripcion = '%s'," +
                " Precio = %s," +
                " Relevancia = %d" +
                " WHERE id_Producto = %d;",
                producto.getMarca(), producto.getModelo(), producto.getDescripcion(),
                precio, producto.getRelevancia(), id);
        try{
            dao.open();
            try(Statement stmt = dao.getConn().createStatement()){
                stmt.executeUpdate(sentencia);
                return true;
            }
        }catch (Exception ex) {
            return false;
        }
    }

    @Override
    public ArrayList<Producto> recuperaProductosCarrito(DAOManager dao, String idCliente) {
        String sentencia=  "Select c.*, p.* " +
                "from carritos c " +
                "inner join Productos p " +
                "on c.idProducto = p.id_producto " +
                "where idCliente = '"+idCliente+"';";
        return resultadoSelectArrayList(dao, sentencia);
    }

    @Override
    public ArrayList<Producto> recuperaCarritoCliente(DAOManager dao, String idCliente) {
        String sentencia = "select c.*, p.* " +
                "from carritos c " +
                "inner join Productos p " +
                "on idProducto = id_producto " +
                "where c.idcliente = '"+idCliente+"';";
        return resultadoSelectArrayList(dao, sentencia);
    }

    private Producto filaSelect(ResultSet rs) throws SQLException {
                return new Producto(
                rs.getInt("id_Producto"),
                rs.getString("Marca"),
                rs.getString("Modelo"),
                rs.getString("Descripcion"),
                rs.getFloat("Precio"),
                rs.getInt("Relevancia")
        );
    }

    private ArrayList<Producto> resultadoSelectArrayList(DAOManager dao, String sentencia) {
        ArrayList<Producto> todosLosProductos = new ArrayList<>();
        Producto producto;
        try{
            dao.open();
            PreparedStatement ps = dao.getConn().prepareStatement(sentencia);
            try (ResultSet rs = ps.executeQuery()){
                while (rs.next()){
                    producto = filaSelect(rs);
                    todosLosProductos.add(producto);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return todosLosProductos;
    }

}
