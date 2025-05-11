package Communications;

import models.Pedido;
import models.Producto;
import models.Trabajador;
import persistencia.Persistencia;
import utils.Utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class Telegram {
    static Properties pro = Persistencia.iniciaProperties();
    private static final String TOKEN = pro.getProperty("tokenTelegram");

    public static boolean enviarMensajeTelegram(String mensaje, String idTelegram){
        String direccion;
        String fijo="https://api.telegram.org/bot" + TOKEN + "/sendMessage?chat_id="+idTelegram+"&text=";
        boolean dev = false;
        try{
            String mensajeCodificado = URLEncoder.encode(mensaje, StandardCharsets.UTF_8);
            direccion=fijo+mensajeCodificado;
            URL url = new URL(direccion);
            URLConnection con = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            dev=true;
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        return dev;
    }

    public static boolean asignaPedidoMensajeTelegram(Pedido p, Trabajador trabajador){
        String direccion;
        String mensaje = String.format("""
                ❗Buenas %s. Tienes un nuevo pedido asignado:❗
                ---------------------------------------------------
                
                ID Pedido: %s
                Fecha del Pedido: %s
                Nº de Productos: %d
                
                ---------------------------------------------------
                
                📧 Revisa tu correo 📧
                """,trabajador.getNombre(), p.getId(), Utils.fechaAString(p.getFechaPedido()),p.getProductos().size());

        return enviarMensajeTelegram(mensaje, String.valueOf(trabajador.getIdTelegram()));
    }

    public static boolean modificaPedidoMensajeTelegram(String mensaje, String idTrabajadorAsignado){
        String direccion;
        String fijo="https://api.telegram.org/bot" + TOKEN + "/sendMessage?chat_id=" + idTrabajadorAsignado + "&text=";
        direccion=fijo+mensaje;
        URL url;
        boolean dev = false;
        try{
            url = new URL(direccion);
            URLConnection con = url.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
            dev=true;
        }catch (IOException e){
            System.out.println(e.getMessage());
        }
        return dev;
    }
}


