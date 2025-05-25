package DAO;

import models.Pedido;
import models.Producto;

import java.io.Serializable;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class DaoPedidosSQL implements DaoPedidos, Serializable {

    @Override
    public ArrayList<Pedido> readAllPedidos(DAOManager dao) {
        ArrayList<Pedido> pedidos = new ArrayList<>();
        String sentencia = "Select p.*" +
                "From Pedidos p";
        String sentenciaProductos = "SELECT * " +
                "FROM lineasPedido " +
                "WHERE id_pedido = ?";
        try{
            dao.open();
            PreparedStatement stmt = dao.getConn().prepareStatement(sentencia);
            ResultSet rs = stmt.executeQuery();
            pedidos = leerResultadosArrayList(rs);
            for(Pedido pedido : pedidos){
                stmt = dao.getConn().prepareStatement(sentenciaProductos);
                stmt.setString(1,pedido.getId());
                ResultSet rs2 = stmt.executeQuery();;
                HashMap<Integer,Integer> cantidadProductos = new HashMap<>();
                pedido.setProductos(leerResultadosArrayListProductos(rs2, cantidadProductos));
                pedido.setCantidadProductos(cantidadProductos);
            }
            return pedidos;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public ArrayList<Pedido> readByidCliente(DAOManager dao, String idCliente) {
        ArrayList<Pedido> pedidos = new ArrayList<>();
        String sentencia = "Select p.* " +
                "From Pedidos p " +
                "WHERE id_Cliente = ?;";
        String sentenciaProductos = "SELECT * " +
                "FROM lineasPedido " +
                "WHERE id_pedido = ?;";
        try{
            dao.open();
            PreparedStatement stmt = dao.getConn().prepareStatement(sentencia);
            stmt.setString(1,idCliente);
            ResultSet rs = stmt.executeQuery();
            pedidos = leerResultadosArrayList(rs);
            for(Pedido pedido : pedidos){
                stmt = dao.getConn().prepareStatement(sentenciaProductos);
                stmt.setString(1,pedido.getId());
                ResultSet rs2 = stmt.executeQuery();
                HashMap<Integer,Integer> cantidadProductos = new HashMap<>();
                pedido.setProductos(leerResultadosArrayListProductos(rs2, cantidadProductos));
                pedido.setCantidadProductos(cantidadProductos);
            }
            return pedidos;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Pedido readById(DAOManager dao, String idPedido) {
        Pedido pedido = null;
        String sentencia = "SELECT * from Pedidos WHERE id_Pedido = ?;";
        String sentenciaProductos = "SELECT * " +
                "FROM lineasPedido " +
                "WHERE id_Pedido = ?";
        try{
            dao.open();
            PreparedStatement stmt = dao.getConn().prepareStatement(sentencia);
            stmt.setString(1, idPedido);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            pedido = leerLineaConsulta(rs);
            stmt = dao.getConn().prepareStatement(sentenciaProductos);
            stmt.setString(1,pedido.getId());
            ResultSet rs2 = stmt.executeQuery();;
            HashMap<Integer,Integer> cantidadProductos = new HashMap<>();
            pedido.setProductos(leerResultadosArrayListProductos(rs2, cantidadProductos));
            pedido.setCantidadProductos(cantidadProductos);
            return pedido;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean clienteRealizaPedido(DAOManager dao, String idCliente, String idPedido) {
        String sentencia = "INSERT INTO Pedidos (id_Pedido, id_Cliente) VALUES (?, ?)";
        try{
            dao.open();
            PreparedStatement stmt = dao.getConn().prepareStatement(sentencia);
            stmt.setString(1,idPedido);
            stmt.setString(2,idCliente);
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean insertarLineasPedidos(DAOManager dao, String idPedido,
                                         ArrayList<Producto> carro, HashMap<Integer, Integer> cantidadProductos) {
        String sentencia = "INSERT INTO lineasPedido (id_pedido, id_producto, Marca, Modelo, Descripcion, " +
                "Precio, Relevancia, cantidadProducto) " +
                "VALUES (?,?, ?, ?, ?, ?,?,?);";
        for (Producto p : carro){
            try{
                dao.open();
                PreparedStatement stmt = dao.getConn().prepareStatement(sentencia);
                stmt.setString(1,idPedido);
                stmt.setInt(2,p.getId());
                stmt.setString(3,p.getMarca());
                stmt.setString(4,p.getModelo());
                stmt.setString(5,p.getDescripcion());
                stmt.setFloat(6,p.getPrecio());
                stmt.setInt(7,p.getRelevancia());
                stmt.setInt(8,cantidadProductos.get(p.getId()));
                stmt.executeUpdate();
            } catch (Exception e) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean borrarPedidoById(DAOManager dao, String idPedido) {
        String sentencia = "DELETE FROM Pedidos WHERE id_Pedido = '"+idPedido+"';";
        try {
            dao.open();
            PreparedStatement stmt = dao.getConn().prepareStatement(sentencia);
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }

    }

    @Override
    public ArrayList<Pedido> readByidTrabajadorAsignado(DAOManager dao, String idTrabajador) {
        ArrayList<Pedido> pedidos = new ArrayList<>();
        String sentencia = "Select pat.*, p.* " +
                "From Pedido_asignado_trabajador pat " +
                "INNER Join Pedidos p " +
                "ON pat.id_Pedido = p.id_pedido " +
                "WHERE pat.id_trabajadorAsignado = ?;";
        String sentenciaProductos = "SELECT * " +
                "FROM lineasPedido " +
                "WHERE id_pedido = ?;";
        try{
            dao.open();
            PreparedStatement stmt = dao.getConn().prepareStatement(sentencia);
            stmt.setString(1,idTrabajador);
            ResultSet rs = stmt.executeQuery();
            pedidos = leerResultadosArrayList(rs);
            for(Pedido pedido : pedidos){
                stmt = dao.getConn().prepareStatement(sentenciaProductos);
                stmt.setString(1,pedido.getId());
                ResultSet rs2 = stmt.executeQuery();
                HashMap<Integer,Integer> cantidadProductos = new HashMap<>();
                pedido.setProductos(leerResultadosArrayListProductos(rs2, cantidadProductos));
                pedido.setCantidadProductos(cantidadProductos);
            }
            return pedidos;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public ArrayList<Pedido> readByidTrabajadorAsignadoSinCompletar(DAOManager dao, String idTrabajador) {
        ArrayList<Pedido> pedidos = new ArrayList<>();
        String sentencia = "Select pat.*, p.* " +
                "From Pedido_asignado_trabajador pat " +
                "INNER Join Pedidos p " +
                "ON pat.id_Pedido = p.id_pedido " +
                "WHERE pat.id_trabajadorAsignado = ? AND p.estado <= 1;";
        String sentenciaProductos = "SELECT * " +
                "FROM lineasPedido " +
                "WHERE id_pedido = ?;";
        try{
            dao.open();
            PreparedStatement stmt = dao.getConn().prepareStatement(sentencia);
            stmt.setString(1,idTrabajador);
            ResultSet rs = stmt.executeQuery();
            pedidos = leerResultadosArrayList(rs);
            for(Pedido pedido : pedidos){
                stmt = dao.getConn().prepareStatement(sentenciaProductos);
                stmt.setString(1,pedido.getId());
                ResultSet rs2 = stmt.executeQuery();
                HashMap<Integer,Integer> cantidadProductos = new HashMap<>();
                pedido.setProductos(leerResultadosArrayListProductos(rs2, cantidadProductos));
                pedido.setCantidadProductos(cantidadProductos);
            }
            return pedidos;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public boolean updateEstadoPedido(DAOManager dao, String idPedido, int nuevoEstado) {
        String sentencia = "UPDATE Pedidos " +
                "SET estado = ? " +
                "WHERE id_Pedido = ?;";
        try{
            dao.open();
            PreparedStatement stmt = dao.getConn().prepareStatement(sentencia);
            stmt.setInt(1, nuevoEstado);
            stmt.setString(2, idPedido);
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateComentarioPedido(DAOManager dao, String idPedido, String comentario) {
        String sentencia = "UPDATE Pedidos " +
                "SET comentario = ? " +
                "WHERE id_Pedido = ?;";
        try{
            dao.open();
            PreparedStatement stmt = dao.getConn().prepareStatement(sentencia);
            stmt.setString(1, comentario);
            stmt.setString(2, idPedido);
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean updateFechaEntrega(DAOManager dao, String idPedido, LocalDate nuevaFecha) {
        String sentencia = "UPDATE Pedidos " +
                "SET fechaEntrega = ? " +
                "WHERE id_Pedido = ?;";
        try{
            dao.open();
            PreparedStatement stmt = dao.getConn().prepareStatement(sentencia);
            stmt.setDate(1, Date.valueOf(nuevaFecha));
            stmt.setString(2, idPedido);
            stmt.executeUpdate();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean quitarPedidosTrabajador(DAOManager dao, String idTrabajador, ArrayList<Pedido> pedidosAsignados) {
        String sentencia = "DELETE FROM Pedido_asignado_trabajador where id_trabajadorAsignado = ? AND id_Pedido = ?";
        for (Pedido pedido : pedidosAsignados){
            try{
                dao.open();
                PreparedStatement stmt = dao.getConn().prepareStatement(sentencia);
                stmt.setString(1, idTrabajador);
                stmt.setString(2, pedido.getId());
                stmt.executeUpdate();
            }catch (Exception e){
                return false;
            }
        }
        return true;
    }

    private ArrayList<Producto> leerResultadosArrayListProductos(ResultSet rs2, HashMap<Integer, Integer> cantidadProductos) {
        ArrayList<Producto> productosPedido = new ArrayList<>();
        Producto producto = null;
        try{
            while (rs2.next()){
                producto = leerLineaConsultaProductos(rs2);
                productosPedido.add(producto);
                cantidadProductos.put(producto.getId(),rs2.getInt("cantidadProducto"));
            }
            return productosPedido;
        } catch (SQLException e) {
            return productosPedido;
        }
    }

    private Producto leerLineaConsultaProductos(ResultSet rs2) throws SQLException {
        return new Producto(
                rs2.getInt("id_Producto"),
                rs2.getString("Marca"),
                rs2.getString("Modelo"),
                rs2.getString("Descripcion"),
                rs2.getFloat("Precio"),
                rs2.getInt("Relevancia")
        );
    }

    private ArrayList<Pedido> leerResultadosArrayList(ResultSet rs) {
        ArrayList<Pedido> todosLosPedidos = new ArrayList<>();
        Pedido pedido = null;
        try{
            while (rs.next()){
                pedido = leerLineaConsulta(rs);
                todosLosPedidos.add(pedido);
            }
            return todosLosPedidos;

        } catch (SQLException e) {
            return null;
        }
    }

    private Pedido leerLineaConsulta(ResultSet rs) throws SQLException {
        return new Pedido(
                rs.getString("id_Pedido"),
                rs.getDate("fechaPedido").toLocalDate(),
                rs.getDate("fechaEntrega") != null ? rs.getDate("fechaEntrega").toLocalDate() : null,
                rs.getInt("estado"),
                rs.getString("comentario")
        );
    }
}
