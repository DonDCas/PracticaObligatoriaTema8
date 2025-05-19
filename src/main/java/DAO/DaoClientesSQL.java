package DAO;

import models.Cliente;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class DaoClientesSQL implements DaoClientes{
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

}
