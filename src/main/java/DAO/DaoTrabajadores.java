package DAO;

public interface DaoTrabajadores {

    String buscaTrabajadorParaAsignar(DAOManager dao);

    boolean asignarPedido(DAOManager dao, String idTrabajador, String idPedido);
}
