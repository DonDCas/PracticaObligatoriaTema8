package DAO;

import models.Admin;
import models.Producto;

import java.util.ArrayList;

public interface DaoAdmins {


    public ArrayList<Admin> readAll (DAOManager dao);

    public Admin readById(DAOManager dao, String id);

}
