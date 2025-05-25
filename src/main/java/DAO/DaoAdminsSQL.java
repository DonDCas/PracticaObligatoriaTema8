package DAO;

import models.Admin;
import models.Producto;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class DaoAdminsSQL implements DaoAdmins, Serializable {
    @Override
    public ArrayList<Admin> readAll(DAOManager dao) {
        String sentencia = "select U.* from Usuarios U inner Join Admins A on U.Id = A.Id_Admin";
        return resultadoSelectArrayList(dao, sentencia);
    }

    @Override
    public Admin readById(DAOManager dao, String id) {
        return null;
    }

    private Admin filaSelect(ResultSet rs) throws SQLException {
        return new Admin(
                rs.getString("Id"),
                rs.getString("Nombre"),
                rs.getString("Pass"),
                rs.getString("Correo"),
                rs.getInt("Movil")
        );
    }

    private ArrayList<Admin> resultadoSelectArrayList(DAOManager dao, String sentencia) {
        ArrayList<Admin> todosLosAdmins = new ArrayList<>();
        Admin admin;
        try{
            dao.open();
            PreparedStatement ps = dao.getConn().prepareStatement(sentencia);
            try (ResultSet rs = ps.executeQuery()){
                while (rs.next()){
                    admin = filaSelect(rs);
                    todosLosAdmins.add(admin);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return todosLosAdmins;
    }
}
