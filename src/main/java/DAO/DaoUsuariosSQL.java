package DAO;

import models.Admin;
import models.Cliente;
import models.Trabajador;

import java.sql.*;
import java.util.ArrayList;

public class DaoUsuariosSQL implements DaoUsuarios {

    @Override
    public boolean compruebaEmail(DAOManager dao, String email) {
        String sentencia = "Select * from Usuarios where Correo='"+email+"';";
        return resultadoBolean(dao, sentencia);
    }

    @Override
    public boolean compruebaMovil(DAOManager dao, int movil) {
        String sentencia = "Select * from Usuarios WHERE Movil="+movil+";";
        return resultadoBolean(dao, sentencia);
    }

    @Override
    public boolean updateUsuario(DAOManager dao, Object object) {
        String sentencia = "";
        try{
            dao.open();
            CallableStatement stmt = null;
            if (object instanceof Cliente cliente){
                sentencia = "{CALL ModificaCliente(?,?,?,?,?,?,?,?,?,?)}";
                stmt = dao.getConn().prepareCall(sentencia);
                stmt.setString(1, cliente.getId());
                stmt.setString(2, cliente.getCorreo());
                stmt.setString(3, cliente.getPass());
                stmt.setString(4, cliente.getNombre());
                stmt.setInt(5, cliente.getMovil());
                stmt.setString(6, cliente.getDireccion());
                stmt.setString(7, cliente.getLocalidad());
                stmt.setString(8, cliente.getProvincia());
                stmt.setString(9, cliente.getToken());
                stmt.setBoolean(10, cliente.isCorreoValidado());
            }
            if (object instanceof Trabajador trabajador){
                sentencia = "{CALL ModificaTrabajador(?,?,?,?,?,?)}";
                stmt = dao.getConn().prepareCall(sentencia);
                stmt.setString(1, trabajador.getId());
                stmt.setString(2, trabajador.getCorreo());
                stmt.setString(3, trabajador.getPass());
                stmt.setString(4, trabajador.getNombre());
                stmt.setInt(5, trabajador.getMovil());
                stmt.setInt(6, trabajador.getIdTelegram());
            }
            if (object instanceof Admin)
                sentencia = "UPDATE Usuario SET " +
                        "Nombre='"+((Admin) object).getNombre()+"'" +
                        "Correo='"+((Admin) object).getCorreo()+"'" +
                        "Pass='"+((Admin) object).getPass()+"' " +
                        "WHERE Id='"+((Admin) object).getId()+"';";
            stmt.execute();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean insertaCliente(DAOManager dao, String id, String correo, String pass, String nombre, int movil,
                                   String direccion, String localidad, String provincia, String token) {
        String sentencia = "{Call InsertarClientes (?,?,?,?,?,?,?,?,?)}";
        try{
            dao.open();
            CallableStatement stmt = dao.getConn().prepareCall(sentencia);
            stmt.setString(1,id);
            stmt.setString(2,correo);
            stmt.setString(3, pass);
            stmt.setString(4,nombre);
            stmt.setInt(5,movil);
            stmt.setString(6,direccion);
            stmt.setString(7,localidad);
            stmt.setString(8,provincia);
            stmt.setString(9,token);
            stmt.execute();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean insertaTrabajador(DAOManager dao, String id, String correo, String pass, String nombre, int movil, int idTelegram) {
        String sentencia = "{Call InsertarTrabajador (?,?,?,?,?,?)}";
        try{
            dao.open();
            CallableStatement stmt = dao.getConn().prepareCall(sentencia);
            stmt.setString(1,id);
            stmt.setString(2,correo);
            stmt.setString(3, pass);
            stmt.setString(4,nombre);
            stmt.setInt(5,movil);
            stmt.setInt(6,idTelegram);
            stmt.execute();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public boolean insertaAdmin(DAOManager dao, String id, String correo, String pass, String nombre, int movil) {
        String sentencia = "{Call InsertarClientes ( '"
                +id+"','"
                +correo+"','"
                +pass+"','"
                +nombre+"','"
                +movil+");" +
                "}";
        try{
            dao.open();
            Statement stmt = dao.getConn().prepareCall(sentencia);
            stmt.execute(sentencia);
            dao.close();
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public Object readUser(DAOManager dao, String email, String pass) {
        String sentencia = "Select u.*, c.*, t.*, a.* from Usuarios u " +
                "LEFT JOIN Clientes c ON u.id=c.Id_Cliente " +
                "LEFT JOIN trabajadores t on u.id=t.Id_Trabajador " +
                "LEFT JOIN Admins a on u.id=a.Id_Admin " +
                "WHERE u.correo = '"+email+"' AND u.pass='"+pass+"';";
        return realizarConsultaTodosUsers(dao, sentencia);
    }

    public Object readUserById(DAOManager dao, String id) {
        String sentencia = "Select u.*, c.*, t.*, a.* from Usuarios u " +
                "LEFT JOIN Clientes c ON u.id=c.Id_Cliente " +
                "LEFT JOIN trabajadores t on u.id=t.Id_Trabajador " +
                "LEFT JOIN Admins a on u.id=a.Id_Admin " +
                "WHERE u.id = '"+id+"';";
        return realizarConsultaTodosUsers(dao, sentencia);
    }

    @Override
    public ArrayList<Admin> readAllAdmins(DAOManager dao) {
        String sentencia = "Select u.*, a.* from Usuarios u " +
                "LEFT JOIN Admins a on u.id=a.Id_Admin " +
                "WHERE u.Id Like ('A%')";
        return consultaArrayListAdmin(dao, sentencia);
    }

    public ArrayList<Cliente> readAllClientes(DAOManager dao){
        String sentencia = "Select u.*, c.* from Usuarios u " +
                "LEFT JOIN Clientes c on u.id=c.Id_Cliente " +
                "WHERE u.Id Like ('C%')";
        return consultaArrayListClientes(dao, sentencia);
    }

    @Override
    public ArrayList<Trabajador> readAllTrabajadores(DAOManager dao) {
        String sentencia = "Select u.*, t.* from Usuarios u " +
                "LEFT JOIN trabajadores t on u.id=t.Id_Trabajador " +
                "WHERE u.Id Like ('T%')";
        return consultaArrayListTrabajadores(dao, sentencia);
    }

    @Override
    public Cliente buscaClienteByIdPedido(DAOManager dao, String idPedido) {
        String sentencia = "Select u.*, c.* from Usuarios u " +
                "LEFT JOIN Clientes c on u.id=c.Id_Cliente " +
                "Inner Join Pedidos p " +
                "on c.Id_Cliente = p.id_Cliente " +
                "WHERE p.id_Pedido= ?;";
        try {
            dao.open();
            PreparedStatement stmt = dao.getConn().prepareStatement(sentencia);
            stmt.setString(1,idPedido);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return leeFilaCliente(rs);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Trabajador readTrabajadorByIdPedido(DAOManager dao, String idPedido) {
        String sentencia = "Select u.*, t.* " +
                "from Usuarios u " +
                "inner join trabajadores t " +
                "on u.id=t.id_trabajador " +
                "inner join Pedido_asignado_trabajador pat " +
                "on t.id_trabajador=pat.id_trabajadorAsignado " +
                "where id_Pedido= ?;";
        try {
            dao.open();
            PreparedStatement stmt = dao.getConn().prepareStatement(sentencia);
            stmt.setString(1,idPedido);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return leerFilaTrabajadores(rs);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Trabajador readTrabajadorById(DAOManager dao, String idTrabajador) {
        String sentencia = "Select u.*, t.* " +
                "from Usuarios u " +
                "inner join trabajadores t " +
                "on u.id=t.id_trabajador " +
                "where id= ?;";
        try {
            dao.open();
            PreparedStatement stmt = dao.getConn().prepareStatement(sentencia);
            stmt.setString(1,idTrabajador);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return leerFilaTrabajadores(rs);
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Trabajador readTrabajadorByCorreo(DAOManager dao, String correo) {
        String sentencia = "Select u.*, t.* " +
                "from Usuarios u " +
                "inner join trabajadores t " +
                "on u.id=t.id_trabajador " +
                "where correo= ?;";
        try {
            dao.open();
            PreparedStatement stmt = dao.getConn().prepareStatement(sentencia);
            stmt.setString(1,correo);
            ResultSet rs = stmt.executeQuery();
            rs.next();
            return leerFilaTrabajadores(rs);
        } catch (Exception e) {
            return null;
        }
    }

    private ArrayList<Trabajador> consultaArrayListTrabajadores(DAOManager dao, String sentencia) {
        ArrayList<Trabajador> todosLosTrabajadores = new ArrayList<>();
        Trabajador trabajador;
        try {
            dao.open();
            PreparedStatement ps = dao.getConn().prepareStatement(sentencia);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                trabajador = leerFilaTrabajadores(rs);
                todosLosTrabajadores.add(trabajador);
            }
            return todosLosTrabajadores;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private ArrayList<Cliente> consultaArrayListClientes(DAOManager dao, String sentencia) {
        ArrayList<Cliente> todosLosClientes = new ArrayList<>();
        Cliente cliente;
        try {
            dao.open();
            PreparedStatement ps = dao.getConn().prepareStatement(sentencia);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                cliente = leeFilaCliente(rs);
                todosLosClientes.add(cliente);
            }
            return todosLosClientes;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Cliente leeFilaCliente(ResultSet rs) throws SQLException {
        return new Cliente(
                rs.getString("id"),
                rs.getString("Correo"),
                rs.getString("Pass"),
                rs.getString("Nombre"),
                rs.getString("Localidad"),
                rs.getString("Provincia"),
                rs.getString("Direccion"),
                rs.getInt("Movil"),
                rs.getString("token"),
                rs.getBoolean("correoValidado")
        );
    }

    private ArrayList<Admin> consultaArrayListAdmin(DAOManager dao, String sentencia) {
        ArrayList<Admin> todosLosAdmins = new ArrayList<>();
        Admin admin;
        try {
            dao.open();
            PreparedStatement ps = dao.getConn().prepareStatement(sentencia);
            ResultSet rs = ps.executeQuery();
            while (rs.next()){
                admin = leeFilaAdmins(rs);
                todosLosAdmins.add(admin);
            }
            return todosLosAdmins;
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Object realizarConsultaTodosUsers(DAOManager dao, String sentencia) {
        try {
            dao.open();
            PreparedStatement ps = dao.getConn().prepareStatement(sentencia);
            try (ResultSet rs = ps.executeQuery()){
                if(rs.next()){
                    if(rs.getString("Id").charAt(0)=='A') return leeFilaAdmins(rs);
                    if(rs.getString("id").charAt(0)=='T') return leerFilaTrabajadores(rs);
                    if(rs.getString("id").charAt(0)=='C') return leeFilaCliente(rs);
                    return null;
                }
                dao.close();
                return null;
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    private Trabajador leerFilaTrabajadores(ResultSet rs) throws SQLException {
        return new Trabajador(
                rs.getString("id"),
                rs.getString("Correo"),
                rs.getString("Pass"),
                rs.getString("Nombre"),
                rs.getInt("Movil"),
                rs.getInt("idTelegram"),
                rs.getBoolean("baja")
        );
    }

    private Admin leeFilaAdmins(ResultSet rs) throws SQLException {
        return new Admin(
                rs.getString("Id"),
                rs.getString("Nombre"),
                rs.getString("Pass"),
                rs.getString("Correo"),
                rs.getInt("Movil")
        );
    }

    private boolean resultadoBolean(DAOManager dao, String sentencia) {
        try{
            dao.open();
            PreparedStatement ps = dao.getConn().prepareStatement(sentencia);
            try(ResultSet rs = ps.executeQuery(sentencia)){
                if (rs.next()){
                    dao.close();
                    return true;
                }
                dao.close();
                return false;
            }

        } catch (Exception e) {
            return false;
        }

    }
}
