package models;

import java.util.ArrayList;

public class Trabajador extends Usuario {

    private boolean baja;
    private int idTelegram;

    //Constructor
    public Trabajador(String id, String email, String clave, String nombre, int movil,int idTelegram, boolean baja) {
        super(id,nombre,clave,email,movil);
        this.idTelegram = idTelegram;
        this.baja = baja;
    }

    public Trabajador(Trabajador trabajador) {
        super(trabajador.id, trabajador.nombre, trabajador.pass, trabajador.correo, trabajador.movil);
        idTelegram = trabajador.idTelegram;
        baja = trabajador.baja;
    }

    //Getter y Setter
    public int getIdTelegram() {
        return idTelegram;
    }

    public void setIdTelegram(int idTelegram) {
        this.idTelegram = idTelegram;
    }

    public boolean isBaja() {
        return baja;
    }

    public void setBaja(boolean baja) {
        this.baja = baja;
    }

    // Otros Metodos

    public ArrayList<Pedido> getPedidosPendientes(ArrayList<Pedido> pedidosAsignados){
        ArrayList<Pedido> pedidosPendientes = new ArrayList<>();
        for(Pedido p : pedidosAsignados)
            if (p.getEstado()<2) pedidosPendientes.add(new Pedido(p));
        return pedidosPendientes;
    }

    public ArrayList<Pedido> getPedidosCompletados(ArrayList<Pedido> pedidosAsignados){
        ArrayList<Pedido> pedidosCompletados = new ArrayList<>();
        for(Pedido p : pedidosAsignados)
            if (p.getEstado()>1) pedidosCompletados.add(new Pedido(p));
        return pedidosCompletados;
    }
}
