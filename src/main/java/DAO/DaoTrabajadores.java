package DAO;

import models.Pedido;
import models.Trabajador;

import java.util.ArrayList;

public interface DaoTrabajadores {

    String buscaTrabajadorParaAsignar(DAOManager dao);

    boolean asignarPedido(DAOManager dao, String idTrabajador, String idPedido);

}
