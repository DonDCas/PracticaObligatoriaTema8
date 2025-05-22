package DAO;

import models.Cliente;

public interface DaoClientes {

    boolean updateCorreoValido(DAOManager dao, boolean nuevoValor, Cliente cliente);

    boolean updateToken(DAOManager dao, String token);

    boolean updateAll(DAOManager dao, Cliente cliente);

    boolean compruebaCorreoCliente(DAOManager dao, String id);

    boolean compruebaIdCliente(DAOManager dao, String id);

    boolean addProductoCarrito(DAOManager dao, String idCliente, int idProducto, int cantidad);

    boolean actualizarProductoCarrito(DAOManager dao, String idCliente, int idProducto, int cantidad);

    boolean buscaProductoCarrito(DAOManager dao, String id, int idProducto);

    Integer devuelveCantidadProductoCarrito(DAOManager dao, String idCliente, int idProducto);

    boolean quitarProductosCarrito(DAOManager dao, String id, int id1, int cantidadAEliminar, int cantidadActual);

    boolean vaciaCarro(DAOManager dao, String idCliente);
}
