package DAO;

import models.Pedido;
import models.Producto;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;

public class DaoPedidosSQL implements DaoPedidos {

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
                ResultSet rs3 = rs2;
                pedido.setProductos(leerResultadosArrayListProductos(rs2));
                pedido.setCantidadProductos(leerResultadosMapaCantidadProductos(rs3));
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
                ResultSet rs2 = stmt.executeQuery();;
                ResultSet rs3 = rs2;
                pedido.setProductos(leerResultadosArrayListProductos(rs2));
                pedido.setCantidadProductos(leerResultadosMapaCantidadProductos(rs3));
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
            ResultSet rs3 = rs2;
            pedido.setProductos(leerResultadosArrayListProductos(rs2));
            pedido.setCantidadProductos(leerResultadosMapaCantidadProductos(rs3));
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

    private HashMap<Integer, Integer> leerResultadosMapaCantidadProductos(ResultSet rs3) {
        HashMap<Integer, Integer> cantidadProductos = new HashMap<>();
        try{
            while (rs3.next()){
                cantidadProductos.put(rs3.getInt("id_producto"),rs3.getInt("cantidadProducto"));
            }
            return cantidadProductos;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    private ArrayList<Producto> leerResultadosArrayListProductos(ResultSet rs2) {
        ArrayList<Producto> productosPedido = new ArrayList<>();
        Producto producto = null;
        try{
            while (rs2.next()){
                producto = leerLineaConsultaProductos(rs2);
                productosPedido.add(producto);
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
                rs.getDate("fechaEntrega").toLocalDate(),
                rs.getInt("estado"),
                rs.getString("comentario")
        );
    }
}
