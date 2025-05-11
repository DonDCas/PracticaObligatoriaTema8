package models;

import Communications.Telegram;

import java.io.Serializable;
import java.util.ArrayList;

public class Trabajador implements Serializable {

    private String id;
    private String email;
    private String clave;
    private String nombre;
    private int movil;
    private boolean baja;
    private int idTelegram;
    private ArrayList<Pedido> pedidosAsignados;

    //Constructor
    public Trabajador(String id, String email, String clave, String nombre, int movil,int idTelegram) {
        this.id = id;
        this.email = email;
        this.clave = clave;
        this.nombre = nombre;
        this.movil = movil;
        this.idTelegram = idTelegram;
        baja = false;
        pedidosAsignados = new ArrayList<>();
    }

    public Trabajador(Trabajador trabajador) {
        id = trabajador.id;
        email = trabajador.email;
        clave = trabajador.clave;
        nombre = trabajador.nombre;
        movil = trabajador.movil;
        idTelegram = trabajador.idTelegram;
        baja = trabajador.baja;
        pedidosAsignados = trabajador.pedidosAsignados;
    }

    //Getter y Setter
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

    public int getMovil() {
        return movil;
    }

    public void setMovil(int movil) {
        this.movil = movil;
    }

    public int getIdTelegram() {
        return idTelegram;
    }

    public void setIdTelegram(int idTelegram) {
        this.idTelegram = idTelegram;
    }

    public ArrayList<Pedido> getPedidosAsignados() {
        return pedidosAsignados;
    }

    public void setPedidosAsignados(ArrayList<Pedido> pedidosAsignados) {
        this.pedidosAsignados = pedidosAsignados;
    }

    public boolean isBaja() {
        return baja;
    }

    public void setBaja(boolean baja) {
        this.baja = baja;
    }

    // Otros Metodos

    public boolean login(String email, String clave){
        return email.equals(this.email) && clave.equals(this.clave) && !baja;
    }

    public Pedido buscaPedidoAsignadoPendiente(String idPedido){
        for (Pedido p : getPedidosPendientes()) if (p.getId().equals(idPedido)) return p;
        return null;
    }

    public Pedido buscaPedidoAsignadoCompletado(String idPedido){
        for (Pedido p : getPedidosCompletados()) if (p.getId().equals(idPedido)) return p;
        return null;
    }

    public boolean asignaPedido(Pedido p){
        if (p.getProductos().isEmpty()) return false;
        pedidosAsignados.add(p);
        return true;
    }

    public ArrayList<Pedido> getPedidosPendientes(){
        ArrayList<Pedido> pedidosPendientes = new ArrayList<>();
        for(Pedido p : pedidosAsignados)
            if (p.getEstado()<2) pedidosPendientes.add(new Pedido(p));
        return pedidosPendientes;
    }

    public ArrayList<Pedido> getPedidosCompletados(){
        ArrayList<Pedido> pedidosCompletados = new ArrayList<>();
        for(Pedido p : pedidosAsignados)
            if (p.getEstado()>1) pedidosCompletados.add(new Pedido(p));
        return pedidosCompletados;
    }

    public int numPedidosPendientes(){
        int cont = 0;
        for (Pedido p : pedidosAsignados) if (p.getEstado()<2) cont++;
        return cont;
    }

    public void quitaPedidosPendientes() {
        ArrayList<Pedido> pedidosParaQuitar = new ArrayList<>();
        for (Pedido p : pedidosAsignados) if (p.getEstado()<2) pedidosParaQuitar.add(p);
        pedidosAsignados.removeAll(pedidosParaQuitar);
    }
}
