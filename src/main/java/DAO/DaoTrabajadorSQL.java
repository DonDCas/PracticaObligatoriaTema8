package DAO;

import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class DaoTrabajadorSQL implements DaoTrabajadores {


    @Override
    public String buscaTrabajadorParaAsignar(DAOManager dao) {
        String sentencia = "SELECT\n" +
                "            CASE\n" +
                "    WHEN COUNT(*) = 1 THEN MIN(id_trabajadorAsignado)\n" +
                "    ELSE NULL\n" +
                "    END AS trabajadorElegido\n" +
                "    FROM (\n" +
                "       SELECT id_trabajadorAsignado, COUNT(*) AS num_pedidos\n" +
                "       FROM Pedido_asignado_trabajador pat\n" +
                "        JOIN Pedidos p ON pat.id_Pedido = p.id_Pedido\n" +
                "       WHERE p.estado <= 1" +
                "       AND t.\n" +
                "       GROUP BY id_trabajadorAsignado\n" +
                "   ) AS subconsulta\n" +
                "        WHERE num_pedidos = (\n" +
                "                SELECT MIN(num_pedidos)\n" +
                "       FROM (\n" +
                "                SELECT COUNT(*) AS num_pedidos\n" +
                "       FROM Pedido_asignado_trabajador pat\n" +
                "        JOIN Pedidos p ON pat.id_Pedido = p.id_Pedido\n" +
                "       WHERE p.estado <= 1\n" +
                "        GROUP BY id_trabajadorAsignado\n" +
                "       ) AS sub2\n" +
                ");";
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
