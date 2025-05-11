package models;

import java.io.Serializable;
import java.util.ArrayList;

public class Cliente implements Serializable {

    private String id;
    private String email;
    private String clave;
    private String nombre;
    private String localidad;
    private String provincia;
    private String direccion;
    private int movil;
    private String token;
    private boolean correoValidado;
    private ArrayList<Pedido> pedidos;
    private ArrayList<Producto> carro;

    // Constructor
    public Cliente(String id, String email, String clave, String nombre, String localidad, String provincia, String direccion, int movil, String token) {
        this.id = id;
        this.email = email;
        this.clave = clave;
        this.nombre = nombre;
        this.localidad = localidad;
        this.provincia = provincia;
        this.direccion = direccion;
        this.movil = movil;
        this.token = token;
        correoValidado = false;
        pedidos = new ArrayList<>();
        carro = new ArrayList<>();
    }

    public Cliente(Cliente cliente) {
        id = cliente.getId();
        email = cliente.getEmail();
        clave = cliente.getClave();
        nombre = cliente.getNombre();
        localidad = cliente.getLocalidad();
        provincia = cliente.getProvincia();
        direccion = cliente.getDireccion();
        movil = cliente.getMovil();
        token = cliente.getToken();
        correoValidado = cliente.isCorreoValidado();
        pedidos = new ArrayList<>(cliente.getPedidos());
        carro = new ArrayList<>(cliente.getCarro());
    }

    // Getter y Setter
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getClave() {
        return clave;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

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

    public int getMovil() {
        return movil;
    }

    public void setMovil(int movil) {
        this.movil = movil;
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

    public ArrayList<Producto> getCarro() {
        return carro;
    }

    public void setCarro(ArrayList<Producto> carro) {
        this.carro = carro;
    }

    //Otros metodos

    public boolean login(String email, String pass) {
        if (email.equals(this.email) && (pass.equals(this.clave))) return true;
        return false;
    }

    //Añadimos un producto al carro y en caso de que el producto ya exista en el devuelve false
    public boolean addProductoCarro(Producto producto) {
        if (existeProductoCarro(producto.getId())) return false;
        carro.add(producto);
        return true;
    }

    //Metodo que busca en el carro un producto mediante su ID y si existe lo retira de la lista
    public boolean quitaProductoCarro(int idProducto) {
        for(Producto p :carro){
            if (p.getId() == idProducto){
                carro.remove(p);
                return true;
            }
        }
        return false;
    }

    //Metodo que devuelve el tamaño del carro del cliente.
    public int numProductosCarro() {
        return carro.size();
    }

    public void vaciaCarro() {
        carro.clear();
    }

    public void addPedido(Pedido pedido){
        pedidos.add(pedido);
        vaciaCarro();
    }

    public float precioCarroSinIva(){
        float precioFinal = 0;
        for(Producto p : carro){
            precioFinal += p.getPrecio();
        }
        return precioFinal;
    }

    public float precioIVACarro(int IVA){return precioCarroSinIva() * (IVA/100f);}

    public float precioCarroConIVA(int IVA){return precioCarroSinIva() + precioIVACarro(IVA);}

    public boolean existeProductoCarro(int idProducto){
        for(Producto p : carro) if (p.getId() == idProducto) return true;
        return false;
    }

    public int cuentaPedidosPendientes() {
        int cont = 0;
        for(Pedido p : pedidos){
            if (p.getEstado() <= 2) cont++;
        }
        return cont;
    }
}