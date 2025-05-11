package models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;

public class Pedido implements Comparable<Pedido>, Serializable {
    private String id;
    private LocalDate fechaPedido;
    private LocalDate fechaEntregaEstimada;
    private LocalDate deliveryDate;
    private int estado;
    private String comentario;
    private ArrayList<Producto> productos;

    
    // Constructores
    public Pedido(String id, ArrayList<Producto> productos) {
        this.id = id;
        fechaPedido = LocalDate.now();
        fechaEntregaEstimada = LocalDate.now().plusDays(7);
        estado = 0;
        comentario = "Sin comentarios";
        this.productos = productos;
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

    public LocalDate getDeliveryDate() {
        return deliveryDate;
    }

    public void setDeliveryDate(LocalDate deliveryDate) {
        this.deliveryDate = deliveryDate;
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

    public void setComentario(String comentario) {
        this.comentario = comentario;
    }

    public ArrayList<Producto> getProductos() {
         return productos;
    }

    public void setProductos(ArrayList<Producto> productos) {
        this.productos = productos;
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
        for (Producto p : productos) precioSinIva += p.getPrecio();
        return precioSinIva;
    }

    public float calculaIVAPedido (int IVA){return calculaTotalPedidoSinIVA() * (IVA/100f);}
    
    public float calculaTotalPedidoConIVA (int IVA){return calculaTotalPedidoSinIVA() + calculaIVAPedido(IVA);}
    
    public int numArticulos(){return productos.size();}
    
    public Producto buscaProducto(int idProducto){
        for(Producto p : productos) if (p.getId() == idProducto) return p;
        return null;}

    //public void addProducto(Producto producto){}


    @Override
    public int compareTo(Pedido p) {
        return (this.fechaPedido).compareTo(p.fechaPedido);
    }

    public boolean addComentario(String comentario) {
        if (comentario.isEmpty()) return false;
        if (this.comentario.equals("Sin comentarios")) this.comentario = "";
        this.comentario += "\n - " + comentario;
        return true;
    }

    public boolean cambiaFechaDelivery(LocalDate nuevaFecha) {
        if (nuevaFecha == null) return false;
        if (nuevaFecha.isEqual(fechaPedido)) return false;
        if (nuevaFecha.isBefore(fechaPedido)) return false;
        deliveryDate = nuevaFecha;
        return true;
    }

    public boolean cambiaFechaEntregaCandelado() {
        fechaEntregaEstimada = null;
        deliveryDate = null;
        return true;
    }
}
