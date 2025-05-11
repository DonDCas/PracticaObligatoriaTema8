package DAO;

import models.Producto;

import java.util.ArrayList;

public interface DaoProductos {
    public ArrayList<Producto> readAll (DAOManager dao);
}
