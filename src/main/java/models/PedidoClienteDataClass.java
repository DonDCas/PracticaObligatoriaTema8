package models;

import java.io.Serializable;

public class PedidoClienteDataClass implements Serializable {
    private String idPedido;
    private String idCliente;
    private String nombreCliente;
    private String emailCliente;
    private String direccionCliente;
    private String localidadCliente;
    private String provinciaCliente;
    private int movilCliente;

    public PedidoClienteDataClass(String idPedido, Cliente cliente) {
        this.idPedido = idPedido;
        idCliente = cliente.getId();
        nombreCliente = cliente.getNombre();
        emailCliente = cliente.getCorreo();
        direccionCliente = cliente.getDireccion();
        localidadCliente = cliente.getLocalidad();
        provinciaCliente = cliente.getProvincia();
        movilCliente = cliente.getMovil();
    }

    public String getIdPedido() {
        return idPedido;
    }

    public String getEmailCliente() {
        return emailCliente;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public String getNombreCliente() {
        return nombreCliente;
    }

    public String getLocalidadCliente() {
        return localidadCliente;
    }

    public String getProvinciaCliente() {
        return provinciaCliente;
    }

    public String getDireccionCliente() {
        return direccionCliente;
    }

    public int getMovilCliente() {
        return movilCliente;
    }
}
