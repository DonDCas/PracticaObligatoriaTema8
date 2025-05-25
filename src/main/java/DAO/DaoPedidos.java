package DAO;

import models.Pedido;
import models.Producto;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public interface DaoPedidos {
    ArrayList<Pedido> readAllPedidos(DAOManager dao);

    ArrayList<Pedido> readByidCliente(DAOManager dao, String idCliente);

    Pedido readById(DAOManager dao, String idPedido);

    boolean clienteRealizaPedido(DAOManager dao, String idCliente, String idPedido);

    boolean insertarLineasPedidos(DAOManager dao, String idPedido, ArrayList<Producto> carro, HashMap<Integer, Integer> cantidadProductos);

    boolean borrarPedidoById(DAOManager dao, String idPedido);

    ArrayList<Pedido> readByidTrabajadorAsignado(DAOManager dao, String id);

    ArrayList<Pedido> readByidTrabajadorAsignadoSinCompletar(DAOManager dao, String id);

    boolean updateEstadoPedido(DAOManager dao, String idPedido, int nuevoEstado);

    boolean updateComentarioPedido(DAOManager dao, String idPedido, String comentario);

    boolean updateFechaEntrega(DAOManager dao, String idPedido, LocalDate nuevaFecha);

    boolean quitarPedidosTrabajador(DAOManager dao, String idTrabajador, ArrayList<Pedido> pedidosAsignados);

}
