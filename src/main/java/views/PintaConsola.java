package views;

import data.DataIVA;
import models.*;
import utils.Utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

public class PintaConsola {
    public final static Scanner S = new Scanner(System.in);

    public static String estadoPedido(int estado){
        return switch (estado){
            case 0-> "Creado";
            case 1-> "En Preparación";
            case 2-> "Enviado";
            case 3-> "Cancelado";
            //case 4-> "Entregado";
            default -> "ERROR";
        };
    }

    public static String pintaPedidosAsignados(ArrayList<Pedido> pedidosTrabajador,
                                               ArrayList<PedidoClienteDataClass> pedidosDatosClientes) {
        String estado = "";
        Collections.sort(pedidosTrabajador);
        String idPedido = "";
        int cont = 0;
        int op = -1;
        int cantidadProductos = 0;
        for(Pedido p : pedidosTrabajador){
            if (cont == 0){
                System.out.printf("""
                ╔═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗
                ║                                                            LISTADO DE PEDIDOS                                                         ║
                ╠═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╣
                """);
                cont++;
            }
            cantidadProductos = 0;
            for (Producto producto : p.getProductos()) cantidadProductos += p.getCantidadProductos().get(producto.getId());
            PedidoClienteDataClass datosCliente = null;
            for (PedidoClienteDataClass datos : pedidosDatosClientes)
                if (p.getId().equals(datos.getIdPedido())) datosCliente = datos;
            estado = estadoPedido(p.getEstado());
            if (p.getEstado()<3){
                System.out.printf("║ %3d. -- Estado: %-15s Fecha de Pedido: %s - NºProductos: %3d - Precio(SinIVA): %10.2f e - ID:Cliente: %8s  ║\n",
                        cont, estado, Utils.fechaAString(p.getFechaPedido()), cantidadProductos, p.calculaTotalPedidoConIVA(DataIVA.IVA), datosCliente.getIdCliente());
                cont++;
            }

            if (cont == 5 || p == pedidosTrabajador.getLast()){
                System.out.printf("╚═══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝\n");
                Utils.posicionLista("Pedidos: ", p, pedidosTrabajador);
                if (pedirPorTecladoSN("Has encontrado el pedido que quieres ver expandido? (S/N): ").equalsIgnoreCase("s")){
                    try{
                        op = Integer.parseInt(pedirPorTeclado("Indica cual el pedido que quieres ver en extendido(Introduce 0 para salir): "));
                    }catch (NumberFormatException e){
                        System.out.println("No se introdujo un numero. Seguiremos mostranto pedidos");
                    }
                    if (op > 0 && op <= cont){
                        return pedidosTrabajador.get((pedidosTrabajador.indexOf(p)) - (cont - op -1)).getId();
                    }
                }
                cont = 0;
            }
        }
        if (idPedido.isEmpty()) System.out.println("Hemos llegado al final de la lista de pedidos.");
        return "";
    }

    /*public static void pedidoExtendido(Trabajador trabajador, String idPedido, ArrayList<PedidoClienteDataClass> pedidosClientes){
        String estado = "";
        Pedido pedidoElegido = null;
        Utils.limpiarPantalla();
        for(Pedido p :trabajador.getPedidosAsignados()) if (p.getId().equals(idPedido)) pedidoElegido = p;
        if (pedidoElegido == null ) Utils.pulsaEnter("Pedido no encontrado.");
        else{
            PedidoClienteDataClass datosCliente = null;
            for (PedidoClienteDataClass datos : pedidosClientes)
                if (pedidoElegido.getId().equals(datos.getIdPedido())) datosCliente = datos;
            estado = estadoPedido(pedidoElegido.getEstado());
            if (pedidoElegido.getEstado()<2){ //Cuando el estado del pedido es "Creado", "En Preparación" o "Enviado"
                System.out.printf("""
                    ====================== ID Pedido: %s ===============================
                    Estado: %s
                    Nombre del cliente: %s
                    Direccion: %s
                    Localidad: %s(%s)
                    Movil de contacto: %d
                    Fecha de pedido: %s
                    Fecha de entrega estimada: %s
                    Comentarios: %s
                    Detalles del pedido: %s
                    Precio sin IVA: \t%5.2f
                    IVA a incluir:  \t%5.2f
                    Precio completo:\t%5.2f
                    """
                        ,pedidoElegido.getId(),estado,datosCliente.getNombreCliente(),datosCliente.getDireccionCliente(),
                        datosCliente.getLocalidadCliente(),datosCliente.getProvinciaCliente(), datosCliente.getMovilCliente(),
                        Utils.fechaAString(pedidoElegido.getFechaPedido()), Utils.fechaAString(pedidoElegido.getFechaEntregaEstimada()),
                        pedidoElegido.getComentario(),pintaProductosPedido(pedidoElegido), pedidoElegido.calculaTotalPedidoSinIVA(),
                        pedidoElegido.calculaIVAPedido(DataIVA.IVA), pedidoElegido.calculaTotalPedidoConIVA(DataIVA.IVA));
            }
            *//*if (pedidoElegido.getEstado()==3){ // Cuando el estado es "Entregado". Eliminado del programa
                System.out.printf("""
                    ====================== ID Pedido: %s ===============================
                    Estado: %s
                    Nombre del cliente: %s
                    Direccion: %s
                    Localidad: %s(%s)
                    Movil de contacto: %d
                    Fecha de pedido: %s
                    Fecha de entrega: %s
                    Comentarios: %s
                    Detalles del pedido: %s
                    Precio sin IVA: \t%5.2f
                    IVA a incluir:  \t%5.2f
                    Precio completo:\t%5.2f
                    """
                        ,pedidoElegido.getId(),estado,datosCliente.getNombreCliente(),datosCliente.getDireccionCliente(),
                        datosCliente.getLocalidadCliente(),datosCliente.getProvinciaCliente(), datosCliente.getMovilCliente(),
                        Utils.fechaAString(pedidoElegido.getFechaPedido()), Utils.fechaAString(pedidoElegido.getDeliveryDate()),
                        pedidoElegido.getComentario(),pintaProductosPedido(pedidoElegido), pedidoElegido.calculaTotalPedidoSinIVA(),
                        pedidoElegido.calculaIVAPedido(DataIVA.IVA), pedidoElegido.calculaTotalPedidoConIVA(DataIVA.IVA));
            }*//*
            if (pedidoElegido.getEstado()==3){ //Cuando el estado es "Canceldo"
                System.out.printf("""
                    ====================== ID Pedido: %s ===============================
                    Estado: %s
                    Nombre del cliente: %s
                    Direccion: %s
                    Localidad: %s(%s)
                    Movil de contacto: %d
                    Fecha de pedido: %s
                    Fecha de entrega: CANCELADO
                    Comentarios: %s
                    Detalles del pedido: %s
                    Precio sin IVA: \t%5.2f
                    IVA a incluir:  \t%5.2f
                    Precio completo:\t%5.2f
                    """,pedidoElegido.getId(),estado,datosCliente.getNombreCliente(),datosCliente.getDireccionCliente(),
                        datosCliente.getLocalidadCliente(),datosCliente.getProvinciaCliente(), datosCliente.getMovilCliente(),
                        Utils.fechaAString(pedidoElegido.getFechaPedido()), pedidoElegido.getComentario(),
                        pintaProductosPedido(pedidoElegido), pedidoElegido.calculaTotalPedidoSinIVA(),
                        pedidoElegido.calculaIVAPedido(DataIVA.IVA), pedidoElegido.calculaTotalPedidoConIVA(DataIVA.IVA));
            }
        }
    }*/

    private static String pintaProductosPedido(Pedido pedidoElegido) {
        String resultado = "";
        for(Producto p : pedidoElegido.getProductos()){
            resultado += String.format("║      - %-10s - %-30s x%3d - %7.2fe                                  ║",
                    p.getMarca(), p.getModelo(), pedidoElegido.getCantidadProductos().get(p.getId()),p.getPrecio());
            if (!(pedidoElegido.getProductos().getLast() == p)) resultado += "\n";
        }
        return resultado;
    }

    public static String pintaHistoricoPedidosDeTrabajador(Trabajador trabajador, ArrayList<PedidoClienteDataClass> pedidosDatosClientes,
                                                           ArrayList<Pedido> pedidosTrabajador) {
        String estado = "";
        int cont = 1;
        int op = -1;
        Collections.sort(pedidosTrabajador);
        for(Pedido p : pedidosTrabajador){
            int cantidadProductos = 0;
            for (Producto producto : p.getProductos()) cantidadProductos += p.getCantidadProductos().get(producto.getId());
            if (cont == 1)
                System.out.printf("""
                ╔═════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗
                ║                                                         HISTORICO DE PEDIDOS                                                        ║
                ╠═════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╣
                """);
            for (PedidoClienteDataClass datos : pedidosDatosClientes){
                estado = estadoPedido(p.getEstado());
                if (datos.getIdPedido().equals(p.getId()))
                    System.out.printf("║ %2d -- Estado: %-15s Fecha de Pedido: %s - NºProductos: x%3d - Precio(SinIVA): %10.2f e - ID:Cliente: %8s ║\n",
                            cont, estado,Utils.fechaAString(p.getFechaPedido()),
                            cantidadProductos,p.calculaTotalPedidoSinIVA() ,datos.getIdCliente());
            }
            if (cont == 10 || p == pedidosTrabajador.getLast()){
                System.out.printf("╚═════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝\n");
                Utils.posicionLista("Pedido: ", p , pedidosTrabajador);
                if (pedirPorTecladoSN("¿Quieres ver alguno de estos pedidos de forma extendida?(S/N): ").equalsIgnoreCase("s")){
                    do{
                        try{
                            op = Integer.parseInt(pedirPorTeclado("Indica que producto es (0 para salir): "));
                        }catch (NumberFormatException e){
                            System.out.println("Numero mal introducido.");
                            if (pedirPorTecladoSN("¿Quieres volver a probar?").equalsIgnoreCase("n")) op = 0;
                        }
                    } while (op==-1);
                    return pedidosTrabajador.get(pedidosTrabajador.indexOf(p) - (cont-op)).getId();
                }
                cont = 0;
            }
            cont++;
        }
    return "";
    }

    public static int pintaProductosResumidos(ArrayList<Producto> catalogo) {
        int cont =0 ;
        int op = -1;
        for(Producto p : catalogo){
            if (cont == 0){
                System.out.printf("""
                    ╔════════════════════════════════════════════════════════════════════════════════════════════════════╗
                    ║                                                                                                    ║
                    ║                                        LISTADO DE PRODUCTOS                                        ║
                    ║                                                                                                    ║
                    ╠════════════════════════════════════════════════════════════════════════════════════════════════════╣
                    """);
            }
            cont++;
            System.out.printf("║ - %-2d -- Marca: %-10s - Modelo: %-26s - Precio: %8.2f - Relevancia %2d  ║\n",cont, p.getMarca(),
            p.getModelo(), p.getPrecio(), p.getRelevancia());
            if (cont == 10 || p == catalogo.getLast()){
                System.out.printf("╚════════════════════════════════════════════════════════════════════════════════════════════════════╝\n");
                Utils.posicionLista("Productos: ", p , catalogo);
                if (pedirPorTecladoSN("¿Encontraste el producto que deseabas? (S/N): ").equalsIgnoreCase("S")){
                    try {
                        op = Integer.parseInt(pedirPorTeclado("Indica el producto que deseas modificar: "));
                    } catch (NumberFormatException e) {
                        throw new NumberFormatException();
                    }
                    if (op>0 && op <11){
                        return catalogo.get(catalogo.indexOf(p) - (cont - op)).getId();
                    }else{
                        if (op != -1) Utils.pulsaEnter("Opción no disponible.");
                    }
                }
                Utils.limpiarPantalla();
                cont = 0;
            }

        }
        return -1;
    }

    public static String pintaResumenClientes(ArrayList<Cliente> todoslosClientes) {
        int cont = 1;
        int op;
        do{
            for(Cliente c : todoslosClientes){
                op = -1;
                if (cont == 1)
                    System.out.print("""
                ╔═════════════════════════════════════════════════════════════════════════════════════════════════════════╗
                ║                                                                                                         ║
                ║                                          LISTADO DE CLIENTES                                            ║
                ║                                                                                                         ║
                ╠═════════════════════════════════════════════════════════════════════════════════════════════════════════╣
                """);
                System.out.printf("║ %2d -- Nombre: %-15s - Localidad: %15s(%-15s) - NºPedidos realizados: %2d ║\n",
                        cont, c.getNombre(), c.getLocalidad(), c.getProvincia(), c.getPedidos().size());
                if(cont == 5 || c == todoslosClientes.getLast()){
                    System.out.printf("╚═════════════════════════════════════════════════════════════════════════════════════════════════════════╝\n");
                    Utils.posicionLista("Cliente ", c, todoslosClientes);
                    if (pedirPorTecladoSN("¿Quieres ver alguno de estos clientes de forma extendida?(S/N): ").equalsIgnoreCase("s")){
                        boolean reintentar;
                        do{
                            reintentar = false;
                            try{
                                op = Integer.parseInt(pedirPorTeclado("Indica cual es el cliente deseado: "));
                            }catch (NumberFormatException e){
                                System.out.println("Numero mal introducido.");
                                if (pedirPorTecladoSN("¿Quieres volver a intentarlo?").equalsIgnoreCase("s")) reintentar = true;
                            }
                        }while (reintentar);
                        if (op>0 && op<=cont) return todoslosClientes.get(todoslosClientes.indexOf(c)-(cont-op)).getId();
                        else Utils.pulsaEnter("Opción no valida");
                    }
                    cont = 0;
                }
                cont++;
            }
            System.out.println("Has llegado al final de la lista.");
        }while (pedirPorTecladoSN("¿Volver al inicio de la lista? (S/N): ").equalsIgnoreCase("s"));
        return "";
    }

    public static void pintaDatosPersonalesCliente(Cliente cliente) {
        String localidadProvincia = String.format("%s - %s", cliente.getLocalidad(), cliente.getProvincia());
        System.out.printf("""
        ╔══════════════════════════════════════════════════════════╗
        ║                     DATOS PERSONALES                     ║
        ╚══════════════════════════════════════════════════════════╝
                                                                  
          Nombre del cliente: %-10s                           
          Email del cliente:  %-10s                    
          Movil del cliente:  %-9d                             
          Dirección:          %-15s                               
          Localidad:          %-42s        
                                                                  
        ╔══════════════════════════════════════════════════════════╗
        """, cliente.getNombre(),cliente.getCorreo(), cliente.getMovil(), cliente.getDireccion(),
        localidadProvincia);
    }

    public static void pintaDatosCliente(Controlador controlador, Cliente clienteExtendido) {
        String localidadProvincia = String.format("%s - %s", clienteExtendido.getLocalidad(), clienteExtendido.getProvincia());
        ArrayList<Pedido> pedidosCliente = controlador.buscaPedidoByCliente(clienteExtendido);
        System.out.printf("""
        ╔════════════════════════════════════════════════════════════════╗
        ║                      ID CLIENTE: %8s                      ║
        ╠════════════════════════════════════════════════════════════════╣
        ║                                                                ║
        ║ Nombre del cliente: %-10s                                 ║
        ║ Email del cliente:  %-40s   ║
        ║ Movil del cliente:  %-9d                                  ║
        ║ Dirección:          %-15s                            ║
        ║ Localidad:          %-42s ║
        ║                                                                ║
        ╠════════════════════════════════════════════════════════════════╣
        ║                                                                ║
        ║ NUMERO DE PEDIDOS (TOTALES):    %3d                            ║
        ║ NUMERO DE PEDIDOS (EN PROCESO): %3d                            ║
        ║ NUMERO DE PEDIDOS (CANCELADOS): %3d                            ║
        ║                                                                ║
        ╚════════════════════════════════════════════════════════════════╝

        """, clienteExtendido.getId(),clienteExtendido.getNombre(),clienteExtendido.getCorreo(), clienteExtendido.getMovil(),
                clienteExtendido.getDireccion(), localidadProvincia,
                pedidosCliente.size(), controlador.cuentaPedidosPendientesCliente(clienteExtendido),
                (pedidosCliente.size() - controlador.cuentaPedidosPendientesCliente(clienteExtendido)));
    }

    public static String pintaResumenPedidos(ArrayList<Pedido> todoslosPedidos, ArrayList<PedidoClienteDataClass> datosClientes) {
        todoslosPedidos = new ArrayList<>(todoslosPedidos);
        Collections.sort(todoslosPedidos);
        String estado;
        int op = -1;
        int cont = 1;
        do{
            for(Pedido p : todoslosPedidos){
                if (cont == 1){
                    Utils.limpiarPantalla();
                    System.out.printf("""
                    ╔═════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗
                    ║                                                          LISTADO DE PEDIDOS                                                         ║
                    ╠═════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╣
                    """);
                }
                int cantidadTotalProductos = 0;
                for (Producto producto : p.getProductos()){
                    cantidadTotalProductos += p.getCantidadProductos().get(producto.getId());
                }
                for (PedidoClienteDataClass datos : datosClientes){
                    if(datos.getIdPedido().equals(p.getId())){
                        estado = estadoPedido(p.getEstado());
                        System.out.printf("║ %3d -- Estado: %-15s Fecha de Pedido: %s - NºProductos: %3d - Precio(SinIVA): %10.2f e - ID:Cliente: %8s ║\n",
                                cont, estado, Utils.fechaAString(p.getFechaPedido()),
                                cantidadTotalProductos,p.calculaTotalPedidoSinIVA() ,datos.getIdCliente());
                    }
                }
                if (cont == 10 || p == todoslosPedidos.getLast()){
                    System.out.printf("╚═════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝\n");
                    Utils.posicionLista("Predido: ",p,todoslosPedidos);
                    if (pedirPorTecladoSN("¿Quieres elegir alguno de estos pedidos?(S/N): ").equalsIgnoreCase("s")){
                        boolean reintentar;
                        do{
                            reintentar = false;
                            try{
                                op = Integer.parseInt(pedirPorTeclado("Indica cual es el pedido deseado: "));
                            }catch (NumberFormatException e){
                                System.out.println("Numero mal introducido.");
                                if (pedirPorTecladoSN("¿Quieres volver a intentarlo? (S/N): ").equalsIgnoreCase("s")) reintentar = true;
                            }
                        }while (reintentar);
                        if (op>0 && op<=cont) return todoslosPedidos.get(todoslosPedidos.indexOf(p)-(cont-op)).getId();
                        else Utils.pulsaEnter("Opción no valida");
                    }
                    cont = 0;
                }
                cont++;
            }
            System.out.println("Has llegado al final de la lista.");
        }while (pedirPorTecladoSN("¿Volver al inicio de la lista? (S/N): ").equalsIgnoreCase("s"));
        return "";
    }

   /* public static void pintaDatosPedidoExtendido(Pedido pedidoExtendido, PedidoClienteDataClass datosCliente) {
        String estado = estadoPedido(pedidoExtendido.getEstado());
        String productos = pintaProductosPedido(pedidoExtendido);
        System.out.printf("""
        ╔═════════════════════════════════════════════════════════════════════════════════════════════════════╗
        ║                                                                                                     ║
        ║                                          ID PEDIDO: %8s                                        ║
        ║                                                                                                     ║
        ╠═════════════════════════════════════════════════════════════════════════════════════════════════════╣
        ║                                                                                                     ║
        ║ Estado del Pedido:         %-20s                                                     ║
        ║ Fecha de Pedido:           %-11s                                                              ║
        ║ Fecha estimada de entrega: %-11s                                                              ║
        ║ Pedido Realizado por:      %-10s                                                               ║
        ║ Email del cliente:         %-20s                                                     ║
        ║ Movil del cliente:         %-10d                                                               ║
        ║ Dirección:                 %-15s                                                          ║
        ║ Localidad:                 %-15s(%-15s)                                   ║
        ║                                                                                                     ║
        ║ PRODUCTOS:                                                                                          ║
        ╠-----------------------------------------------------------------------------------------------------╣
        %s
        ╠-----------------------------------------------------------------------------------------------------╣
        ║ PRECIO TOTAL SIN IVA:      %-7.2f e                                                                ║
        ║ IVA A IMPONER:             %-7.2f e                                                                ║
        ║ PRECIO TOTAL CON IVA:      %-7.2f e                                                                ║
        ╚═════════════════════════════════════════════════════════════════════════════════════════════════════╝
        """, pedidoExtendido.getId(), estado, Utils.fechaAString(pedidoExtendido.getFechaPedido()),
                Utils.fechaAString(pedidoExtendido.getFechaEntregaEstimada()), datosCliente.getNombreCliente(),
                datosCliente.getEmailCliente(), datosCliente.getMovilCliente(),datosCliente.getDireccionCliente(),
                datosCliente.getLocalidadCliente(),datosCliente.getProvinciaCliente(), productos,
                pedidoExtendido.calculaTotalPedidoSinIVA(), pedidoExtendido.calculaIVAPedido(DataIVA.IVA),
                pedidoExtendido.calculaTotalPedidoConIVA(DataIVA.IVA));
    }*/

    public static void pintaDatosPedidoExtendido(Pedido pedidoExtendido, PedidoClienteDataClass datosCliente, Trabajador trabajadorAsignado) {
        String estado = estadoPedido(pedidoExtendido.getEstado());
        String productos = pintaProductosPedido(pedidoExtendido);
        String comentario = String.format(pedidoExtendido.getComentario().isEmpty()
                ? "║                                                                                                     ║"
                : """
                        ║ Comentarios: %-17s                                                                      ║""",pedidoExtendido.getComentario());

        String trabajador = ((trabajadorAsignado != null) ? trabajadorAsignado.getNombre() : "Sin trabajador asignado");
        System.out.printf("""
        ╔═════════════════════════════════════════════════════════════════════════════════════════════════════╗
        ║                                                                                                     ║
        ║                                          ID PEDIDO: %8s                                        ║
        ║                                                                                                     ║
        ╠═════════════════════════════════════════════════════════════════════════════════════════════════════╣
        ║                                                                                                     ║
        ║ Estado del Pedido:         %-20s                                                     ║
        ║ Fecha de Pedido:           %-11s                                                              ║
        ║ Fecha estimada de entrega: %-11s                                                              ║
        ║ Pedido Realizado por:      %-10s                                                               ║
        ║ Email del cliente:         %-40s                                 ║
        ║ Movil del cliente:         %-10d                                                               ║
        ║ Dirección:                 %-15s                                                          ║
        ║ Localidad:                 %-23s(%-15s)                                 ║
        ║ Trabajador Asignado:       %-25s                                                ║
        %s
        ║ PRODUCTOS:                                                                                          ║
        ╠-----------------------------------------------------------------------------------------------------╣
        %s
        ╠-----------------------------------------------------------------------------------------------------╣
        ║ PRECIO TOTAL SIN IVA:      %-10.2f e                                                             ║
        ║ IVA A IMPONER:             %-10.2f e                                                             ║
        ║ PRECIO TOTAL CON IVA:      %-10.2f e                                                             ║
        ╚═════════════════════════════════════════════════════════════════════════════════════════════════════╝
        """, pedidoExtendido.getId(), estado, Utils.fechaAString(pedidoExtendido.getFechaPedido()),
                Utils.fechaAString(pedidoExtendido.getFechaEntregaEstimada()), datosCliente.getNombreCliente(),
                datosCliente.getEmailCliente(), datosCliente.getMovilCliente(),datosCliente.getDireccionCliente(),
                datosCliente.getLocalidadCliente(),datosCliente.getProvinciaCliente(), trabajador, comentario,productos,
                pedidoExtendido.calculaTotalPedidoSinIVA(), pedidoExtendido.calculaIVAPedido(DataIVA.IVA),
                pedidoExtendido.calculaTotalPedidoConIVA(DataIVA.IVA));
    }

    public static String pintaResumenTrabajadores(Controlador controlador, ArrayList<Trabajador> todoslosTrabajadores) {
        int cont = 1;
        int op = -1;
        do {
            for (Trabajador t : todoslosTrabajadores) {
                ArrayList<Pedido> pedidosAsignado = controlador.recuperaPedidosAsignadosTrabajador(t);
                if(cont == 1)
                    System.out.print("""
                        ╔════════════════════════════════════════════════════════════════════════════════════════════╗
                        ║                                                                                            ║
                        ║                                   LISTADO DE TRABAJADORES                                  ║
                        ║                                                                                            ║
                        ╠════════════════════════════════════════════════════════════════════════════════════════════╣
                        """);
                System.out.printf("║ %3s -- Nombre: %15s - NºPedidos pendientes: %2d - NºPedidos Asignados: %2d        ║\n",
                        cont, t.getNombre(), t.getPedidosPendientes(pedidosAsignado).size(), pedidosAsignado.size());
                if (cont == 10 || t == todoslosTrabajadores.getLast()){
                    System.out.printf("╚════════════════════════════════════════════════════════════════════════════════════════════╝\n");
                    Utils.posicionLista("Trabajador: ", t, todoslosTrabajadores);
                    if (pedirPorTecladoSN("¿Encontraste al trabajador que deseabas?(S/N): ").equalsIgnoreCase("s")){
                        boolean reintentar;
                        do{
                            reintentar = false;
                            try{
                                op = Integer.parseInt(pedirPorTeclado("Indica cual es el trabajador deseado: "));
                            }catch (NumberFormatException e){
                                System.out.println("Numero mal introducido.");
                                if (pedirPorTecladoSN("¿Quieres volver a intentarlo?").equalsIgnoreCase("s")) reintentar = true;
                            }
                        }while (reintentar);
                        if (op > 0 && op<=cont) return todoslosTrabajadores.get(todoslosTrabajadores.indexOf(t)-(cont-op)).getId();
                        else Utils.pulsaEnter("Opción no valida");
                    }
                    cont = 0;
                }
                cont++;
            }
            System.out.println("Has llegado al final de la lista.");
        }while (pedirPorTecladoSN("¿Volver al inicio de la lista? (S/N): ").equalsIgnoreCase("s"));
        return "";
    }

    public static void pintaDatosTrabajador(Controlador controlador, Trabajador trabajador) {
        ArrayList<Pedido> pedidosAsignados = controlador.recuperaPedidosAsignadosTrabajador(trabajador);
        System.out.printf("""
        ╔══════════════════════════════════════════════════════════╗
        ║                 ID TRABAJADOR: %8s                  ║
        ╠══════════════════════════════════════════════════════════╣
        ║                                                          ║
        ║ Nombre del Trabajador: %-15s                   ║
        ║ Email del Trabajador:  %-33s ║
        ║ Movil del Trabajador:  %-9d                         ║
        ║ ID Telegram:           %-13d                     ║
        ║                                                          ║
        ╠══════════════════════════════════════════════════════════╣
        ║                                                          ║
        ║ Nº PEDIDOS ASIGNADOS:    %3d                             ║
        ║ Nº PEDIDOS PENDIENTES:   %3d                             ║
        ║ Nº PEDIDOS COMPLETADOS:  %3d                             ║
        ║                                                          ║
        ╚══════════════════════════════════════════════════════════╝
       """, trabajador.getId(),trabajador.getNombre(),trabajador.getCorreo(), trabajador.getMovil(), trabajador.getIdTelegram(),
                pedidosAsignados.size(), trabajador.getPedidosPendientes(pedidosAsignados).size(),
                trabajador.getPedidosCompletados(pedidosAsignados).size());
    }


    public static void pintaEstadisticas(Controlador controlador) {
        System.out.printf("""
                ╔══════════════════════════════════════════════════════════╗
                ║                 ESTADISTICAS DE LA APP                   ║
                ╠══════════════════════════════════════════════════════════╣
                ║                                                          ║
                ║ Número de Clientes:                         %3d          ║
                ║ Número de Trabajadores:                     %3d          ║
                ║ Número de Pedidos:                          %3d          ║
                ║ Número de Pedidos pendientes:               %3d          ║
                ║ Número de Pedidos Completados o Cancelados: %3d          ║
                ║ Número de Pedidos sin asignar:              %3d          ║
                ║                                                          ║
                ╚══════════════════════════════════════════════════════════╝
                """, controlador.getClientes().size(), controlador.getTrabajadoresDeAlta().size(),
                controlador.numPedidosTotales(), (controlador.numPedidosTotales() - controlador.numPedidosCompletadosYCancelados()),
                controlador.numPedidosCompletadosYCancelados(), controlador.numPedidosSinTrabajador());
    }

    public static void pintaDatosPersonalesTrabajador(Trabajador trabajador) {
        System.out.printf("""
                ╔══════════════════════DATOS PERSONALES════════════════════╗
                ║                                                          ║
                ║ ID del trabajador:     %-15s                   ║
                ║ Nombre del trabajador: %-15s                   ║
                ║ Email del trabajador:  %-15s             ║
                ║ Movil del trabajador:  %-9d                         ║
                ║ ID Telegram:           %-10d                        ║
                ║                                                          ║
                ╚══════════════════════════════════════════════════════════╝
                """, trabajador.getId(), trabajador.getNombre(), trabajador.getCorreo(), trabajador.getMovil(),
                trabajador.getIdTelegram());
    }

    public static void pintaProductoCatalogo(Producto producto) {
        String salida = "";
        if(producto.getRelevancia()>9){
            System.out.printf("""
                    x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x
                    X                                          PROMOCION                                          X
                    x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x
                    X                                      ID: %10s                                         X
                    x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x
                    X    MARCA: %10s                        X      MODELO: %-25s        X
                    x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x
                    X   %65s                         X
                    x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x
                    X  PRECIO: %7.2f e                                                                          X
                    x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x*x
                    
                    """
                    , producto.getId(), producto.getMarca(), producto.getModelo(),producto.getDescripcion(),producto.getPrecio());
        }else{
            System.out.printf("""
                    ╔═════════════════════════════════════════════════════════════════════════════════════════════╗
                    ║                                       ID: %10s                                        ║
                    ╠═══════════════════════════════════════════════╦═════════════════════════════════════════════╣
                    ║    MARCA: %10s                          ║      MODELO: %-25s      ║
                    ╠═══════════════════════════════════════════════╩═════════════════════════════════════════════╣
                    ║   %65s                         ║
                    ╠═════════════════════════════════════════════════════════════════════════════════════════════╣
                    ║  PRECIO: %7.2f e                                                                          ║
                    ╚═════════════════════════════════════════════════════════════════════════════════════════════╝
                    
                    """, producto.getId(), producto.getMarca(), producto.getModelo(),
                    producto.getDescripcion(),producto.getPrecio());



        }
    }

    private static String pedirPorTeclado(String s) {
        System.out.print(s);
        return S.nextLine();
    }

    private static String pedirPorTecladoSN(String s) {
        String respuestaSN;
        do{
            respuestaSN = pedirPorTeclado(s);
            if (!respuestaSN.equalsIgnoreCase("s") && !respuestaSN.equalsIgnoreCase("n"))
                System.out.println("Introduce S o N");
        }while(!respuestaSN.equalsIgnoreCase("s") && !respuestaSN.equalsIgnoreCase("n"));
        return respuestaSN;
    }
    public static void pintaProductoResumen(Producto producto, int cantidad) {
        System.out.printf("║ ID: %-6d - %-14s - %-25s - Precio: %-7.2f x%-2d ║\n",producto.getId(),producto.getMarca(),
                producto.getModelo(),producto.getPrecio(),cantidad);
    }

    public static void pintaPedidoParaCliente(Pedido pedidoBuscado, Cliente cliente) {
        String estado = estadoPedido(pedidoBuscado.getEstado());
        String productos = pintaProductosPedido(pedidoBuscado);
        String comentario = String.format(pedidoBuscado.getComentario().isEmpty()
                ? "║                                                                                                     ║"
                : """
                        ║-----------------------------------------------------------------------------------------------------║
                          Comentarios: %s
                        ║-----------------------------------------------------------------------------------------------------║""",pedidoBuscado.getComentario());

        System.out.printf("""
        ╔═════════════════════════════════════════════════════════════════════════════════════════════════════╗
        ║                                          ID PEDIDO: %8s                                        ║
        ╠═════════════════════════════════════════════════════════════════════════════════════════════════════╣
        ║ Estado del Pedido:         %-20s                                                     ║
        ║ Fecha de Pedido:           %-11s                                                              ║
        ║ Fecha estimada de entrega: %-11s                                                              ║
        ║ Dirección:                 %-15s                                                          ║
        ║ Localidad:                 %-15s(%-15s)                                   ║
        ║                                                                                                     ║
        %s
        ║                                                                                                     ║
        ║ PRODUCTOS:                                                                                          ║
        ╠-----------------------------------------------------------------------------------------------------╣
        %s
        ╠-----------------------------------------------------------------------------------------------------╣
        ║ PRECIO TOTAL SIN IVA:      %-7.2f e                                                                ║
        ║ IVA A IMPONER:             %-7.2f e                                                                ║
        ║ PRECIO TOTAL CON IVA:      %-7.2f e                                                                ║
        ╚═════════════════════════════════════════════════════════════════════════════════════════════════════╝
        """,pedidoBuscado.getId(),estado,Utils.fechaAString(pedidoBuscado.getFechaPedido()),
                Utils.fechaAString(pedidoBuscado.getFechaEntregaEstimada()), cliente.getDireccion(),cliente.getLocalidad(),
                cliente.getProvincia(),comentario,productos, pedidoBuscado.calculaTotalPedidoSinIVA(), pedidoBuscado.calculaIVAPedido(DataIVA.IVA),
                pedidoBuscado.calculaTotalPedidoConIVA(DataIVA.IVA));

    }

    public static void cabeceraCatalogoReducido() {
        System.out.printf("""
                    ╔═════════════════════════════════════════════════════════════════════════════════════════════════════════╗
                    ║                                           LISTADO DE PRODUCTOS                                          ║
                    ╠═════════════════════════════════════════════════════════════════════════════════════════════════════════╣
                    """);
    }

    public static void pintaProductoCatalogoReducido(Producto p, int cont) {
        String relevancia = "Sin Promoción";
        if(p.getRelevancia()>9) relevancia = "***EN PROMOCIÓN***";
        System.out.printf("║ - %d -- Marca: %-10s - Modelo: %-26s - Precio: %8.2f - %-19s  ║\n",cont, p.getMarca(),
                p.getModelo(), p.getPrecio(), relevancia);
    }

    public static void pieCatalogoReducido() {
        System.out.printf("╚═════════════════════════════════════════════════════════════════════════════════════════════════════════╝\n");
    }

    public static void pintaCarroClienteResumido(ArrayList<Producto> carro,
                                         HashMap<Integer, Integer> productosCantidad) {
        int cont = 1;
        System.out.printf("""
                ╔═══════════════════════════════════════════════════════════════════════════════════════╗
                ║                                  Productos del Carrito                                ║
                ╠═══════════════════════════════════════════════════════════════════════════════════════╣
                """);
        for(Producto producto : carro){
            System.out.printf("""
                    ║ %3d .- %13s - %-30s - Precio(Sin IVA): %6.2f x%-4d ║
                    """, cont, producto.getMarca(),
                    producto.getModelo(),producto.getPrecio(),
                    productosCantidad.get(producto.getId()));
            cont++;
        }
        System.out.printf("╚══════════════════════════════════════════════════════════════════════════════════════╝\n");

    }

    public static void pintaCarroCliente(Cliente user, ArrayList<Producto> carro,
                                         HashMap<Integer, Integer> productosCantidad) {
        int cantidadTotalProductos = 0;
        for(Producto p : carro) cantidadTotalProductos+=productosCantidad.get(p.getId());
        System.out.printf("""
                ╔═══════════════════════════════════════════════════════════════════════════════╗
                ║                             Productos del Carrito                             ║
                ╠═══════════════════════════════════════════════════════════════════════════════╣
                ║ Tu carrito contiene %-2d                                                        ║
                ║ Productos:                                                                    ║
                ║———————————————————————————————————————————————————————————————————————————————║
                """,cantidadTotalProductos);
            for(Producto p : carro) PintaConsola.pintaProductoResumen(p,productosCantidad.get(p.getId()));
        System.out.printf("""
                ║———————————————————————————————————————————————————————————————————————————————║
                ║ El precio total del pedido sera de:   %7.2f e                               ║
                ║ Se le añadira un IVA de:              %7.2f e                               ║
                ║ El precio total con IVA es de:        %7.2f e                               ║
                ╚═══════════════════════════════════════════════════════════════════════════════╝
                """, user.precioCarroSinIva(carro, productosCantidad),
                user.precioIVACarro(carro, productosCantidad, DataIVA.IVA),
                user.precioCarroConIVA(carro, productosCantidad, DataIVA.IVA));
    }

    public static void ultimasSesiones(Controlador controlador) {
        ArrayList<String> lineas = new ArrayList<>();
        for (Admin admin : controlador.getAdmins()){
            lineas.add(String.format("║ %-10s - Tipo Usuario: %-13s - %-37s - %-45s ║\n",admin.getId(), "Administrador" ,admin.getCorreo(), controlador.ultimoInicioSesionParaAdmin(admin)));
        }
        for (Trabajador trabajador : controlador.getTrabajadores()){
            lineas.add(String.format("║ %-10s - Tipo Usuario: %-13s - %-37s - %-45s ║\n",trabajador.getId(), "Trabajador" ,trabajador.getCorreo(), controlador.ultimoInicioSesionParaAdmin(trabajador)));
        }
        for (Cliente cliente : controlador.getClientes()){
            lineas.add(String.format("║ %-10s - Tipo Usuario: %-13s - %-37s - %-45s ║\n",cliente.getId(), "Cliente" ,cliente.getCorreo(), controlador.ultimoInicioSesionParaAdmin(cliente)));
        }
        System.out.printf("""
                ╔══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╗
                ║                                                     Últimos inicios de sesión                                                    ║
                ╠══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╣
                """);
        for(String linea : lineas){
            System.out.printf("%-115s",linea);
        }
        System.out.printf("╚══════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════════╝\n");
    }


    public static void pintaAyudaRutas() {
        System.out.println("""
                                    -------------------------------------------------------------------------
                                    
                                    Las Rutas deben ser ruta absoluta.
                                    Esto quiere decir que debes indicar la ruta de la siguiente manera:
                                    C:\\carpeta\\carpeta2
                                    
                                    No hace falta indicar la carpeta que quieras ultima ruta contenedora.
                                    Por ejemplo:
                                    C:\\Carpeta\\backup
                                    
                                    Solo indica la carpeta en la que quieres guardar los datos.
                                    
                                    Si quieres asegurarte de escribir bien la ruta, copia la ruta
                                    de la ventana del explorador de windows y copiala directamente en 
                                    esta app.
                                    
                                    No puedes guardar directamente en la carpeta raiz del disco duro.
                                    
                                    Solo puedes introducir una dirección que no sea en la que se encuentra
                                    la aplicación.
                                    
                                    -------------------------------------------------------------------------
                                    """);
        Utils.pulsaEnter();
    }

    public static void pintaAyudaArchivos() {
        System.out.println("""
                                    -------------------------------------------------------------------------
                                    
                                    Las Rutas deben ser ruta absoluta.
                                    Esto quiere debes indicar la ruta de la siguiente manera:
                                    C:\\carpeta\\carpeta2\\archivo.ext
                                                                        
                                    Es importante que indiques el nombre del archivo
                                    
                                    Si quieres asegurarte de escribir bien la ruta dirijete al archivo
                                    que es la copia de backUp pulsa sobre el con el boton derecho
                                    y en el menu contextual haz clic sobre la opción 
                                    "Copiar como ruta de acceso".
                                                                       
                                    -------------------------------------------------------------------------
                                    """);
        Utils.pulsaEnter();
    }

    public static void consultaIniciosSesion(int op, Controlador controlador) {

    }

    public static void pintaAdvertenciaBuscaPrecios() {
        System.out.println("""
                                    -------------------------------------------------------------------------
                                    
                                    A continuación se le pedirá que ingrese un rango de precios.
                                    
                                    Si no quiere introducir un rango mínimo deje el formulario vacío.
                                    
                                    Si por el contrario no quieres introducir un rango maximo tras
                                    introducir un rango mínimo deje el formulario vacío.
                                    
                                    Por último si desea salir sin consultar el rango de precios
                                    deje los dos formularios vacíos.
                                    
                                    -------------------------------------------------------------------------
                                    """);
    }
}

/*
    ╔
    ═
    ╗

    ║

    ╚
    ╝
    ╠
    ╣
    ╦
    ╩
    ╬
     */
