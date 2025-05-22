package DAO;

import models.Producto;

import java.util.ArrayList;

public interface DaoProductos {

    public ArrayList<Producto> readAll (DAOManager dao);

    public ArrayList<Producto> readByDescripcion (DAOManager dao, String descripcipcion);

    public ArrayList<Producto> readByMarcas (DAOManager dao, String marca);

    public ArrayList<Producto> readByModelo (DAOManager dao, String modelo);

    public ArrayList<Producto> readByRangoPrecios (DAOManager dao, float precioMin, float precioMax);

    public Producto readById(DAOManager dao, int id);

    public boolean updateProducto(DAOManager dao, int id ,Producto producto);

    ArrayList<Producto> recuperaProductosCarrito(DAOManager dao, String idCliente);

    ArrayList<Producto> recuperaCarritoCliente(DAOManager dao, String idCliente);
}
