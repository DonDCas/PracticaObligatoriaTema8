package DAO;

//import com.mysql.cj.xdevapi.PreparableStatement;
import models.Cliente;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

public class DaoClientesSQL implements DaoClientes, Serializable {
    @Override
    public boolean updateCorreoValido(DAOManager dao, boolean nuevoValor, Cliente cliente) {
        String sentencia = "UPDATE Clientes SET " +
                "correoValidado="+nuevoValor+" " +
                "WHERE Id_Cliente='"+cliente.getId()+"';";
        try {
            dao.open();
            Statement stmt = dao.getConn().createStatement();
            stmt.executeUpdate(sentencia);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateToken(DAOManager dao, String token) {
        return false;
    }

    @Override
    public boolean updateAll(DAOManager dao, Cliente cliente) {
        return false;
    }

    @Override
    public boolean compruebaCorreoCliente(DAOManager dao, String id) {
        String sentencia = "Select * from Clientes where Id_Cliente='"+id+"';";
        try{
            dao.open();
            PreparedStatement ps = dao.getConn().prepareStatement(sentencia);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) return rs.getBoolean("correoValidado");
            return false;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean compruebaIdCliente(DAOManager dao, String id) {
        String sentencia = "Select * from Clientes WHERE id = '"+id+"';";
        try{
            dao.open();
            PreparedStatement ps = dao.getConn().prepareStatement(sentencia);
            try(ResultSet rs = ps.executeQuery(sentencia)){
                if (rs.next()){
                    dao.close();
                    return true;
                }
                dao.close();
                return false;
            }

        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean addProductoCarrito(DAOManager dao, String idCliente, int idProducto, int cantidad) {
        String sentencia = "INSERT INTO carritos VALUES (?,?,?);";
        try{
            dao.open();
            PreparedStatement ps = dao.getConn().prepareStatement(sentencia);
            ps.setString(1, idCliente);
            ps.setInt(2, idProducto);
            ps.setInt(3, cantidad);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    @Override
    public boolean actualizarProductoCarrito(DAOManager dao, String idCliente, int idProducto, int cantidad) {
        String sentencia = "UPDATE carritos " +
                "SET cantidad = (cantidad+?) " +
                "WHERE idCliente = ? AND idProducto=?;";
        try{
            dao.open();
            PreparedStatement ps = dao.getConn().prepareStatement(sentencia);
            ps.setInt(1,cantidad);
            ps.setString(2,idCliente);
            ps.setInt(3,idProducto);
            ps.executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean buscaProductoCarrito(DAOManager dao, String id, int idProducto) {
        String sentencia = "SELECT * FROM carritos WHERE idCliente = ? AND idProducto = ?";
        try{
            dao.open();
            PreparedStatement ps = dao.getConn().prepareStatement(sentencia);
            ps.setString(1,id);
            ps.setInt(2,idProducto);
            ResultSet rs = ps.executeQuery();
            return rs.next();
        } catch (Exception e) {
            return false;
        }

    }

    @Override
    public Integer devuelveCantidadProductoCarrito(DAOManager dao, String idCliente, int idProducto) {
        String sentencia = "SELECT cantidad " +
                "FROM carritos " +
                "WHERE idCliente = ? AND idProducto = ?;";
        try{
            dao.open();
            PreparedStatement ps = dao.getConn().prepareStatement(sentencia);
            ps.setString(1,idCliente);
            ps.setInt(2,idProducto);
            ResultSet rs = ps.executeQuery();
            rs.next();
            return rs.getInt("cantidad");
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public boolean quitarProductosCarrito(DAOManager dao, String idCliente,
                                          int idProducto, int cantidadAEliminar,int cantidadActual) {
        String sentencia = "";
        try{
            dao.open();
            PreparedStatement stmt;
            if (cantidadAEliminar == 0 || cantidadActual == cantidadAEliminar){
                sentencia = "DELETE FROM carritos WHERE idCliente = ? AND idProducto = ?";
                stmt = dao.getConn().prepareStatement(sentencia);
                stmt.setString(1,idCliente);
                stmt.setInt(2,idProducto);
            }else {
                sentencia = "UPDATE carritos " +
                        "set cantidad = (cantidad - ?) " +
                        "WHERE idCliente = ? AND idProducto = ? ";
                stmt = dao.getConn().prepareStatement(sentencia);
                stmt.setInt(1,cantidadAEliminar);
                stmt.setString(2,idCliente);
                stmt.setInt(3,idProducto);
            }
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }


    }

    @Override
    public boolean vaciaCarro(DAOManager dao, String idCliente) {
        String sentencia = "DELETE FROM carritos " +
                "WHERE idCliente = ?;";

        try {
                dao.open();
                PreparedStatement stmt = dao.getConn().prepareStatement(sentencia);
                stmt.setString(1,idCliente);
                stmt.executeUpdate();
                return true;
        } catch (Exception e){
            return false;
        }
    }

}
