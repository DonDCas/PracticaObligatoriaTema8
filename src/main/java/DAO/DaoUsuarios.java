package DAO;

import models.Admin;
import models.Cliente;
import models.Trabajador;

import java.util.ArrayList;

public interface DaoUsuarios {
    boolean compruebaEmail(DAOManager dao, String email);

    boolean compruebaMovil(DAOManager dao, int movil);

    boolean updateUsuario(DAOManager dao, Object object);

    boolean insertaCliente(DAOManager dao, String id, String correo, String pass, String nombre, int movil,
                            String direccion, String localidad, String provincia, String token);

    boolean insertaTrabajador(DAOManager dao, String id, String correo, String pass, String nombre, int movil,
                            int idTelegram);

    boolean insertaAdmin(DAOManager dao, String id, String correo, String pass, String nombre, int movil);


    Object readUser(DAOManager dao, String email, String pass);

    Object readUserById(DAOManager dao, String id);

    ArrayList<Admin> readAllAdmins(DAOManager dao);

    ArrayList<Cliente> readAllClientes(DAOManager dao);

    ArrayList<Trabajador> readAllTrabajadores(DAOManager dao);

    Cliente buscaClienteByIdPedido(DAOManager dao, String idPedido);

    Trabajador readTrabajadorByIdPedido(DAOManager dao, String idPedido);

    Trabajador readTrabajadorById(DAOManager dao, String idTrabajador);

    Trabajador readTrabajadorByCorreo(DAOManager dao, String correo);
}
