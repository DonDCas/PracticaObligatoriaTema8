package DAO;

import models.Pedido;
import models.Trabajador;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DaoTrabajadorSQL implements DaoTrabajadores {


    @Override
    public String buscaTrabajadorParaAsignar(DAOManager dao) {
        String sentencia = "SELECT t.Id_Trabajador AS trabajadorElegido, \n" +
                "       COUNT(pat.id_Pedido) AS num_pedidos\n" +
                "FROM trabajadores t\n" +
                "LEFT JOIN Pedido_asignado_trabajador pat ON t.Id_Trabajador = pat.id_trabajadorAsignado\n" +
                "LEFT JOIN Pedidos p ON pat.id_Pedido = p.id_Pedido AND p.estado <= 1\n" +
                "WHERE t.baja = false\n" +
                "GROUP BY t.Id_Trabajador\n" +
                "ORDER BY num_pedidos ASC\n" +
                "LIMIT 1;";
        try{
            dao.open();
            PreparedStatement stmt = dao.getConn().prepareStatement(sentencia);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return rs.getString("trabajadorElegido");
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean asignarPedido(DAOManager dao, String idTrabajador, String idPedido) {
        String sentencia = "INSERT INTO Pedido_asignado_trabajador VALUES (?,?);";
        try{
            dao.open();
            PreparedStatement stmt = dao.getConn().prepareStatement(sentencia);
            stmt.setString(1, idPedido);
            stmt.setString(2, idTrabajador);
            stmt.executeUpdate();
            return true;
        }catch (Exception e){
            return false;
        }
    }

}
