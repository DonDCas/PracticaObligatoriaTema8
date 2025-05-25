package models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;

public class Pedido implements Comparable<Pedido>, Serializable {
    private String id;
    private LocalDate fechaPedido;
    private LocalDate fechaEntregaEstimada;
    private int estado;
    private String comentario;
    private ArrayList<Producto> productos;
    private HashMap<Integer,Integer> cantidadProductos;

    
    // Constructores


    public Pedido(String id, LocalDate fechaPedido, LocalDate fechaEntregaEstimada, int estado,
                  String comentario) {
        this.id = id;
        this.fechaPedido = fechaPedido;
        this.fechaEntregaEstimada = fechaEntregaEstimada;
        this.estado = estado;
        this.comentario = comentario;
        productos = new ArrayList<>();
        cantidadProductos = new HashMap<>();
    }

    public Pedido(String id, ArrayList<Producto> productos, HashMap<Integer, Integer> cantidadProductos) {
        this.id = id;
        fechaPedido = LocalDate.now();
        fechaEntregaEstimada = LocalDate.now().plusDays(7);
        estado = 0;
        comentario = "Sin comentarios";
        this.productos = productos;
        this.cantidadProductos = cantidadProductos;
    }

    public Pedido(Pedido pedido) {
        id = pedido.getId();
        fechaPedido = pedido.getFechaPedido();
        fechaEntregaEstimada = pedido.getFechaEntregaEstimada();
        estado = pedido.getEstado();
        comentario = pedido.getComentario();
        productos = pedido.getProductos();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDate getFechaPedido() {
        return fechaPedido;
    }

    public void setFechaPedido(LocalDate fechaPedido) {
        this.fechaPedido = fechaPedido;
    }

    public LocalDate getFechaEntregaEstimada() {
        return fechaEntregaEstimada;
    }

    public void setFechaEntregaEstimada(LocalDate fechaEntregaEstimada) {
        this.fechaEntregaEstimada = fechaEntregaEstimada;
    }

    public int getEstado() {
        return estado;
    }

    public void setEstado(int estado) {
        this.estado = estado;
    }

    public String getComentario() {
        return comentario;
    }

    public ArrayList<Producto> getProductos() {
         return productos;
    }

    public void setProductos(ArrayList<Producto> productos) {
        this.productos = productos;
    }

    public void setCantidadProductos(HashMap<Integer, Integer> cantidadProductos) {
        this.cantidadProductos = cantidadProductos;
    }

    public HashMap<Integer, Integer> getCantidadProductos() {
        return cantidadProductos;
    }

    //Otros Metodos
    
    public boolean cambiaEstado(int nuevoEstado){
        if (nuevoEstado>0 && nuevoEstado<4){
            estado = nuevoEstado;
            return true;
        }
        return false;}
    
    public boolean cambiaFechaEntrega(LocalDate nuevaFecha){
        if (nuevaFecha == null) return false;
        if (nuevaFecha.isEqual(fechaEntregaEstimada)) return false;
        if (nuevaFecha.isEqual(fechaPedido)) return false;
        if (nuevaFecha.isBefore(fechaPedido)) return false;
        fechaEntregaEstimada = nuevaFecha;
        return true;
    }

    public float calculaTotalPedidoSinIVA(){
        float precioSinIva = 0;
        for (Producto p : productos) precioSinIva += (p.getPrecio()*cantidadProductos.get(p.getId()));
        return precioSinIva;
    }

    public float calculaIVAPedido (int IVA){return calculaTotalPedidoSinIVA() * (IVA/100f);}
    
    public float calculaTotalPedidoConIVA (int IVA){return calculaTotalPedidoSinIVA() + calculaIVAPedido(IVA);}
    
    public int numArticulos(){return productos.size();}


    //public void addProducto(Producto producto){}


    @Override
    public int compareTo(Pedido p) {
        return (this.fechaPedido).compareTo(p.fechaPedido);
    }

}
