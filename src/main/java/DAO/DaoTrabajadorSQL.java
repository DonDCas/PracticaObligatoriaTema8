package DAO;

import models.Pedido;
import models.Trabajador;

import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;

public class DaoTrabajadorSQL implements DaoTrabajadores, Serializable {


    @Override
    public String buscaTrabajadorParaAsignar(DAOManager dao) {
        String sentencia = "SELECT\n" +
                "    CASE\n" +
                "        WHEN COUNT(*) = 1 THEN MIN(id_trabajadorElegido)\n" +
                "        ELSE NULL\n" +
                "    END AS trabajadorElegido\n" +
                "FROM (\n" +
                "    SELECT \n" +
                "        t.Id_Trabajador AS id_trabajadorElegido,\n" +
                "        COUNT(CASE WHEN p.estado <= 1 THEN 1 END) AS num_pedidos\n" +
                "    FROM trabajadores t\n" +
                "    LEFT JOIN Pedido_asignado_trabajador pat \n" +
                "        ON pat.id_trabajadorAsignado = t.Id_Trabajador\n" +
                "    LEFT JOIN Pedidos p \n" +
                "        ON pat.id_Pedido = p.id_Pedido \n" +
                "           AND p.estado <= 1\n" +
                "    WHERE t.baja = false\n" +
                "    GROUP BY t.Id_Trabajador\n" +
                ") AS subconsulta\n" +
                "WHERE num_pedidos = (\n" +
                "    SELECT MIN(num_pedidos)\n" +
                "    FROM (\n" +
                "        SELECT \n" +
                "            COUNT(CASE WHEN p.estado <= 1 THEN 1 END) AS num_pedidos\n" +
                "        FROM trabajadores t2\n" +
                "        LEFT JOIN Pedido_asignado_trabajador pat2 \n" +
                "            ON pat2.id_trabajadorAsignado = t2.Id_Trabajador\n" +
                "        LEFT JOIN Pedidos p \n" +
                "            ON pat2.id_Pedido = p.id_Pedido \n" +
                "               AND p.estado <= 1\n" +
                "        WHERE t2.baja = false\n" +
                "        GROUP BY t2.Id_Trabajador\n" +
                "    ) AS sub2\n" +
                ");\n";
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

    @Override
    public boolean updateBaja(DAOManager dao, String idTrabajador, boolean nuevoDato) {
        String sentencia = "UPDATE trabajadores SET baja = ? WHERE id_trabajador=?";
        try{
            dao.open();
            PreparedStatement stmt = dao.getConn().prepareStatement(sentencia);
            stmt.setBoolean(1, nuevoDato);
            stmt.setString(2, idTrabajador);
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
