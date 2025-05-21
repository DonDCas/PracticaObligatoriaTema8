package models;

import DAO.DAOManager;
import DAO.DaoClientesSQL;
import DAO.DaoPedidosSQL;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

public class Cliente extends Usuario implements Serializable {

    private String localidad;
    private String provincia;
    private String direccion;
    private String token;
    private boolean correoValidado;
    private ArrayList<Pedido> pedidos;

    // Constructor
    public Cliente(String id, String email, String clave, String nombre, String localidad,
                   String provincia, String direccion, int movil, String token, boolean correoValidado) {
        super(id, nombre, clave, email, movil);
        this.localidad = localidad;
        this.provincia = provincia;
        this.direccion = direccion;
        this.token = token;
        this.correoValidado = correoValidado;
        pedidos = new ArrayList<>();
    }

    public Cliente(Cliente cliente) {
        super(cliente.id,cliente.nombre,cliente.pass, cliente.correo, cliente.movil);
        localidad = cliente.getLocalidad();
        provincia = cliente.getProvincia();
        direccion = cliente.getDireccion();
        token = cliente.getToken();
        correoValidado = cliente.isCorreoValidado();
        pedidos = new ArrayList<>(cliente.getPedidos());
    }

    // Getter y Setter

    public String getLocalidad() {
        return localidad;
    }

    public void setLocalidad(String localidad) {
        this.localidad = localidad;
    }

    public String getProvincia() {
        return provincia;
    }

    public void setProvincia(String provincia) {
        this.provincia = provincia;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public boolean isCorreoValidado() {
        return correoValidado;
    }

    public void setCorreoValidado(boolean correoValidado) {
        this.correoValidado = correoValidado;
    }

    public ArrayList<Pedido> getPedidos() {
        return pedidos;
    }

    public void setPedidos(ArrayList<Pedido> pedidos) {
        this.pedidos = pedidos;
    }

    //Otros metodos

    //Metodo que busca en el carro un producto mediante su ID y si existe lo retira de la lista
    public boolean quitaProductoCarro(DAOManager dao, DaoClientesSQL daoClientes,
                                      int idProducto, int cantidadAEliminar, int cantidadActual) {
        return daoClientes.quitarProductosCarrito(dao, id, idProducto, cantidadAEliminar, cantidadActual);
    }

    public boolean vaciaCarro(DAOManager dao, DaoClientesSQL daoClientes) {
        return daoClientes.vaciaCarro(dao, id);
    }

    public void addPedido(Pedido pedido){
        pedidos.add(pedido);
        //vaciaCarro();
    }

    public float precioCarroSinIva(ArrayList<Producto> carro, HashMap<Integer, Integer> productosCantidad){
        float precioFinal = 0;
        for(Producto p : carro){
            precioFinal += (p.getPrecio() * productosCantidad.get(p.getId()));
        }
        return precioFinal;
    }

    public float precioIVACarro(ArrayList<Producto> carro, HashMap<Integer, Integer> productosCantidad, int IVA){
        return precioCarroSinIva(carro, productosCantidad) * (IVA/100f);}

    public float precioCarroConIVA(ArrayList<Producto> carro, HashMap<Integer, Integer> productosCantidad, int IVA){
        return precioCarroSinIva(carro, productosCantidad) + precioIVACarro(carro, productosCantidad, IVA);}

    public boolean existeProductoCarro(DaoClientesSQL daoCliente, DAOManager dao, int idProducto){
        return daoCliente.buscaProductoCarrito(dao, id, idProducto);
    }

    public int cuentaPedidosPendientes(DAOManager dao, DaoPedidosSQL daoPedidos) {
        int cont = 0;
        for(Pedido p : daoPedidos.readByidCliente(dao, id)){
            if (p.getEstado() <= 2) cont++;
        }
        return cont;
    }
}