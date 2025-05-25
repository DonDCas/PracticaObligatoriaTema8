package views;

import data.DataIVA;
import models.*;
import utils.Utils;

import java.util.ArrayList;
import java.util.HashMap;


public class Menu {

    public static void logo(){
        Utils.limpiarPantalla();
        System.out.print("""
                                                                                                                \s
                                                      ...             ..                                        \s
                                                  .';col'   .,;;;,.   ,ll:,..                                   \s
                                               .'coddxd;   ,odddddl'  .:ddddl:'.                                \s
                                             .;odxxxxxo'  .cxdddddd:   ,dddddddl,.                              \s
                                           .;oxxxxxxxxl.  'oxdddddxl.  .lddddddddl,.                            \s
                                          'lxxxxxxdlcc,   'ccccccccc.   ;ccldddddddc.                           \s
                                         ;dxxxxxxxc.  ....           .'..  .ldddddddo,                          \s
                                        ;dxxxxxxxd,  .lxdl.         .lddc.  ;ddddddddo,                         \s
                                       ;dxxxxxxxxl.  .;ll;.         .:ll,   'oxdddddddo'                        \s
                                      .oxxxxxxxxx:                          .cdddddddddl.                       \s
                                     .:xxxxxxxxxd:...........................:dddddddddd,                       \s
                                     .lxxxxxxxxxxdddddddddddddddddddddddddddddddddddddddc.                      \s
                                     'oxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxdddddddxl.                      \s
                                     'dxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxdddddxo.                      \s
                                     .oxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxl.                      \s
                                     .lxxxxxxxdc,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,,.                       \s
                                      ;xxxxxxxl.                                                                \s
                                      .lxxxxxx:                                 '::::::'                        \s
                                       'oxxxxo.                                 ,dxxxxl.                        \s
                                        ,dxxxo;''''''''''''''''''''''''''''''''';oxxxo'                         \s
                                         'oxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxl.                          \s
                                          .cdxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxd:.                           \s
                                            'ldxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxxdc.                             \s
                                              'cdxxxxxxxxxxxxxxxxxxxxxxxxxxxxxo:.                               \s
                                                .;cdxxxxxxxxxxxxxxxxxxxxxxxoc,.                                 \s
                                                   .';coddxxxxxxxxxxxddlc;'.                                    \s
                                                        ..',,,;;;,,''..                                         \s
                                                                                                                \s
                                                                                                                \s
                                                                                                                \s
                .cc::'  .;l:c;.  'ccc:,    ;l, .c,    .;l;     .,l;..:;   .;ccc'   ,c.  .c'   .;ccc:.   .:cc:,. \s
                'dl;,.  .ld:;'   ;d:'cd'  .cxd:,o:    ;doo,    .:ddc'lc   'ol;,.   :d;..:d,  .ll'..:d,  .ol,:d; \s
                'do::.  .ldc:'   ;dlloc.   co;cdd:   .oocoo.   .:d:cddc.   ';:c;.  :dl::ld,  'd:   'd:  .oocc:. \s
                ,d;     .lo:;'   ;d,'ll.   co..cx:  .co:;:oc.  .:o..:xc.  .:::ol.  :d'  ,d,  .;oc;:lc.  .oc.    \s
                .'.      .'''.   ..  .'.   ..   '.   ..   ..    ..   ..    .,,'.   ..   .'.    .',,.     ..     \s
                
                
                
                """);
    }

    public static void menuInicio(boolean modoInvitado) {
            System.out.print("""
                ╔══════════════════════════════════════════════════════╗
                ║                                                      ║
                ║               Bienvenidos a FernanShop               ║
                ║                                                      ║
                ╠══════════════════════════════════════════════════════╣
                ║                    MENU DE INICIO                    ║
                ╠══════════════════════════════════════════════════════╣
                ║                                                      ║
                ║ 1. Registrarse                                       ║
                ║ 2. Iniciar sesión                                    ║""");
            if(modoInvitado)
                System.out.print("""
                
                ║ 3. Ver el catalogo                                   ║""");
            System.out.print("""
                
                ║                                                      ║
                ╚══════════════════════════════════════════════════════╝
                """);
    }

    public static void menuSeleccionDeCatalogos() {
        System.out.println("""
                ╔══════════════════════════════════════════════════════╗
                ║                                                      ║
                ║              ¿Qué catalogo quieres ver?              ║
                ║                                                      ║
                ╠══════════════════════════════════════════════════════╣
                ║                                                      ║
                ║  1 - Mostrar todos los productos.                    ║
                ║  2 - Mostrar productos por descripción               ║
                ║  3 - Mostrar productos por marcas                    ║
                ║  4 - Mostrar productos por modelos                   ║
                ║  5 - Mostrar productos por rango de precios          ║
                ║  6 - Mostrar productos por termino                   ║
                ║                                                      ║
                ║  0 - Para salir.                                     ║
                ╚══════════════════════════════════════════════════════╝
                """);
    }

    public static void menuAdmin(Controlador controlador, Admin user, String ultimoInicioSesion) {
            Utils.limpiarPantalla();
        System.out.printf("""
                ╔══════════════════════════════════════════════════════╗
                ║                                                      ║
                ║  Bienvenido Admin. Tienes %2d pedidos por asignar     ║
                ║  %-45s       ║
                ║                                                      ║
                ╠══════════════════════════════════════════════════════╣
                ║                                                      ║
                ║  1  - Ver todo el catalogo.                          ║
                ║  2  - Editar un producto.                            ║
                ║  3  - Ver un resumen de todos los Clientes.          ║
                ║  4  - Ver un resumen de todos los Pedidos.           ║
                ║  5  - Ver un resumen de todos los Trabajadores.      ║
                ║  6  - Ver las estadísticas de la aplicación.         ║
                ║  7  - Cambiar el estado de un Pedido.                ║
                ║  8  - Dar de alta a un Trabajador.                   ║
                ║  9  - Dar de baja a un Trabajador.                   ║
                ║  10 - Asignar un Pedido a un Trabajador.             ║
                ║  11 - Últimos inicios de sesión de usuarios.         ║
                ║  12 - Exportar pedidos en Excel.                     ║
                ║  13 - Realizar copia de seguridad de la app.         ║
                ║  14 - Consultar rutas de configuración de la app.    ║
                ║  15 - Modificar modo Invitado.                       ║
                ║                                                      ║
                ║  0 - SALIR.                                          ║
                ╚══════════════════════════════════════════════════════╝
                """, controlador.numPedidosSinTrabajador(), ultimoInicioSesion);
    }

    //Menu cliente
    public static void menuCliente(Controlador controlador, Cliente cliente, String ultimoInicioSesion) {
        String notificacion = String.format("%-45s",((controlador.cuentaPedidosPendientesCliente(cliente)>0)
                        ? "Tienes " + controlador.cuentaPedidosPendientesCliente(cliente)+" pedido(s) pendiente(s) de entrega"
                        : "No tienes pedidos pendientes de entrega"));
        /*String notificacion = ((controlador.cuentaPedidosPendientesCliente(cliente)>0)
                ? "Tienes " + controlador.cuentaPedidosPendientesCliente(cliente)+" pedido(s) pendiente(s) de entrega"
                : "No tienes pedidos pendientes de entrega");*/
        System.out.printf("""
                ╔══════════════════════════════════════════════════════╗
                ║                                                      ║
                ║  Bienvenido a FERNANSHOP, %-20s       ║
                ║  %-45s       ║
                ║  %-45s       ║
                ║                                                      ║
                ╠══════════════════════════════════════════════════════╣
                ║                                                      ║
                ║ 1.- Consultar catalogo de productos                  ║
                ║ 2.- Realizar un pedido                               ║
                ║ 3.- Ver mis pedidos realizados                       ║
                ║ 4.- Ver mis datos personales                         ║
                ║ 5.- Modificar mis datos personales                   ║
                ║                                                      ║
                ║ 6.- Cerrar sesión                                    ║
                ║                                                      ║
                ╚══════════════════════════════════════════════════════╝
                """, cliente.getNombre(), ultimoInicioSesion,notificacion);
    }

    //Menu que devuelve las Marcas disponibles en nuestra empresa
    public static void menuMarcasProductos(Controlador controlador) {
        ArrayList<String> marcas = controlador.devuelveListaMarcas();
        if (marcas.isEmpty()) System.out.println("No hay productos disponibles");
        else{
            System.out.printf("""
                    ╔═══════════════════════════════════════════════════════════╗
                    ║                                                           ║
                    ║                     Listado de Marcas                     ║
                    ║                                                           ║
                    ╠═══════════════════════════════════════════════════════════╣
                    """);
            for (String s :marcas) System.out.printf("║ - %-10s                                              ║\n", s);
            System.out.println("╚═══════════════════════════════════════════════════════════╝");
        }
    }




    //Menus internos
    public static void menuModificarPerfilCliente(Cliente cliente) {
        PintaConsola.pintaDatosPersonalesCliente(cliente);
        System.out.print("""
        ║                                                          ║
        ║ 1. Nombre                                                ║
        ║ 2. Dirección                                             ║
        ║ 3. Localidad                                             ║
        ║ 4. Provincia                                             ║
        ║ 5. Teléfono                                              ║
        ║ 6. Correo                                                ║
        ║ 7. Contraseña                                            ║
        ║                                                          ║
        ║ 8. Salir                                                 ║
        ╚══════════════════════════════════════════════════════════╝
        """);
    }

    public static void realizarPedido(ArrayList<Producto> carro, HashMap<Integer, Integer> cantidadProductos) {
        int totalProductos = 0;
        for(Producto p : carro) totalProductos += cantidadProductos.get(p.getId());
        System.out.printf("""
                ╔══════════════════════════════════════════════════════╗
                ║  Realizando pedido.                                  ║
                ║  Nº de productos en el carrito: %-2d                   ║
                ╠══════════════════════════════════════════════════════╣
                ║ 1- Buscar un producto.                               ║
                ║ 2- Ver el carrito actual.                            ║
                ║ 3- Añadir un producto al carro.                      ║
                ║ 4- Borrar un producto del carro                      ║
                ║ 5- Confirmar Pedido.                                 ║
                ║ 6- Cancelar Pedido.                                  ║
                ║                                                      ║
                ║ 7- Salir (Se guardará el carrito hasta que vuelvas). ║
                ╚══════════════════════════════════════════════════════╝
                
                """, totalProductos);
    }


    public static void pintaPedido(Pedido pedidoBuscado) {
        System.out.println("================== ID: "+pedidoBuscado.getId()+" =======================\n");
        System.out.println("- Fecha de pedido: " + Utils.fechaAString(pedidoBuscado.getFechaPedido()));
        System.out.println("- Fecha estimada de entrega: " + Utils.fechaAString(pedidoBuscado.getFechaEntregaEstimada()));
        System.out.println("- Comentarios: " + pedidoBuscado.getComentario());
        switch (pedidoBuscado.getEstado()){
            case 0:
                System.out.println("- Estado: Creado.");
                break;
            case 1:
                System.out.println("- Estado: En Preparación.");
                break;
            case 2:
                System.out.println("- Estado: Enviado.");
                break;
            /*case 3:
                System.out.println("- Estado: Entregado.");
                System.out.println("- Fecha de entrega: " + Utils.fechaAString(pedidoBuscado.getDeliveryDate()));
                break;*/
            case 3:
                System.out.println("- Estado: CANCELADO");
                break;
        }
        System.out.println("*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*");
        System.out.println("Productos: ");
        for (Producto p :pedidoBuscado.getProductos()){
            //PintaConsola.pintaProductoResumen(p);
        }
        System.out.println("\n*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*+*");
        System.out.printf("\nEl Precio total (sin IVA) es: \t \t \t\t\t\t\t\t %5.2f e\n", pedidoBuscado.calculaTotalPedidoSinIVA());
        System.out.printf("Se le sumaran la siguiente cantidad en concepto de iva: \t %5.2f e\n", pedidoBuscado.calculaIVAPedido(DataIVA.IVA));
        System.out.printf("El total del Pedido con IVA sera de: \t\t\t\t\t\t %5.2f e\n", pedidoBuscado.calculaTotalPedidoConIVA(DataIVA.IVA));
    }

    //Menus del Trabajador
    //Menu principal del trabajador
    public static void menuTrabajador(Controlador controlador, Trabajador trabajador, String ultimoInicioSesion) {
        ArrayList<Pedido> pedidosPendientesTrabajador = controlador.recuperaPedidosPendientesTrabajador(trabajador);
        String mensaje = String.format((pedidosPendientesTrabajador.size()>0)
                ? "Tienes %-2d pedido(s) pendiente(s)."
                : "No tienes pedidos asignados.", pedidosPendientesTrabajador.size());
        System.out.printf("""
                ╔═════════════════════════════════════════════════════════════╗
                ║                                                             ║
                ║  Bienvenido %-12s %-34s ║
                ║  %-45s              ║
                ║                                                             ║
                ╠═════════════════════════════════════════════════════════════╣
                """,trabajador.getNombre(),mensaje,ultimoInicioSesion);
        System.out.printf("""
                ║                                                             ║
                ║ 1.- Consultar los pedidos que tengo asignados               ║
                ║ 2.- Modificar el estado de un pedido                        ║
                ║ 3.- Consultar el catálogo de productos                      ║
                ║ 4.- Modificar un producto del catalogo                      ║
                ║ 5.- Ver el histórico de mis pedidos terminados              ║
                ║ 6.- Ver mi perfil                                           ║
                ║ 7.- Modificar mis datos personales                          ║
                ║ 8.- Cerrar sesión                                           ║
                ║                                                             ║
                ╚═════════════════════════════════════════════════════════════╝
                """);
    }

    //Menu de modificar datos del trabajador
    public static void menuModificarPerfilTrabajador(Controlador controlador, Trabajador trabajador) {
        PintaConsola.pintaDatosPersonalesTrabajador(trabajador);
        System.out.print("""
                ------------------------------------------------------------
                | Elige el parámetro que deseas modificar                  |
                ------------------------------------------------------------
                | 1.- Nombre                                               |
                | 2.- Email                                                |
                | 3.- Clave                                                |
                | 4.- Móvil                                                |
                |                                                          |
                | 0.- Salir                                                |
                ------------------------------------------------------------
                ¿Que datos quieres modificar?\s""");
    }

    public static void menuEstadosPedido() {
        System.out.print("""
                ╔═══════════════════════════════════════════════════╗
                ║           Modificar estado de un pedido           ║
                ╠═══════════════════════════════════════════════════╣
                ║                                                   ║
                ║1. En Preparación                                  ║
                ║2. Enviado                                         ║
                ║3. Cancelado                                       ║
                ║                                                   ║
                ║0. Salir                                           ║
                ║                                                   ║
                ╚═══════════════════════════════════════════════════╝
                
                """);
    }

    public static void menuModificarProductos() {
        System.out.print("""
                   ------------------------------------------------------------------------------------------------
                   | Elige el parámetro que deseas modificar                                                      |
                   ------------------------------------------------------------------------------------------------
                   | 1.- Marca                                                                                    |
                   | 2.- Modelo                                                                                   |
                   | 3.- Descripción                                                                              |
                   | 4.- Precio                                                                                   |
                   | 5.- Relevancia                                                                               |
                   |                                                                                              |
                   | 0.- Salir                                                                                    |
                   ------------------------------------------------------------------------------------------------
                   """);
    }

    public static void menuEstadoOComentario() {
        System.out.print("""
                
                ╔═══════════════════════════════════════════════════╗
                ║                 ¿Que deseas hacer                 ║
                ╠═══════════════════════════════════════════════════╣
                ║                                                   ║
                ║1. Modificar el Estado del Pedido                  ║
                ║2. Añadir un comentario                            ║
                ║                                                   ║
                ║0. Salir                                           ║
                ║                                                   ║
                ╚═══════════════════════════════════════════════════╝

                """);
    }

    public static void encabezadoListaPedidos() {
        System.out.print("""
                 ╔══════════════════════════════════════════════════════════════╗
                 ║                       Listado de Pedidos                     ║
                 ╠══════════════════════════════════════════════════════════════╣
                 """);
    }

    public static void pintaPedidoResumido(int cont, Pedido p) {
        System.out.printf("║ %d. - Nº de productos: %-3d - Fecha de realización: %-10s ║\n",cont, p.numArticulos(),
                Utils.fechaAString(p.getFechaPedido()));
    }

    public static void pieListadoPedidos() {
        System.out.println("╚══════════════════════════════════════════════════════════════╝");
    }

    public static void menuExportar(Controlador controlador) {
        int pedidosSinCompletar = controlador.getTodosPedidos().size() - controlador.numPedidosSinTrabajador();
        System.out.printf("""
                 ╔══════════════════════════════════════════════════════════════╗
                 ║                       Listado de Pedidos                     ║
                 ╠══════════════════════════════════════════════════════════════╣
                 ║                                                              ║
                 ║ 1. Exportar todos los pedidos %-4d                           ║
                 ║ 2. Exportar pedidos sin asignación %-4d                      ║
                 ║ 3. Exportar pedidos completados %-4d                         ║
                 ║ 4. Exportar pedidos sin completar %-4d                       ║
                 ║                                                              ║
                 ║ 0. Salir                                                     ║
                 ╚══════════════════════════════════════════════════════════════╝
                 """, controlador.getTodosPedidos().size(), controlador.pedidosSinTrabajador().size(),
                controlador.numPedidosCompletadosYCancelados(), pedidosSinCompletar);
    }

    public static void rutasConfiguracion(Controlador controlador) {
        String rutaClientes = controlador.recuperaRuta("rutaClientes");
        String rutaTrabajadores = controlador.recuperaRuta("rutaTrabajadores");
        String rutaAdministradores = controlador.recuperaRuta("rutaAdmin");
        String rutaProductos = controlador.recuperaRuta("rutaProductos");
        String rutaLog = controlador.recuperaRuta("rutaLogs");
        System.out.printf("""
                 ╔════════════════════════════════════════════════════════════════════════╗
                 ║                     Configuración de la Aplicación                     ║
                 ╠════════════════════════════════════════════════════════════════════════╣
                 ║                                                                        ║
                 ║  A continuación se mostrarán las rutas que usa la                      ║
                 ║  aplicación para guardar los datos de la misma.                        ║
                 ║                                                                        ║
                 ╚════════════════════════════════════════════════════════════════════════╝
                 
                   Ruta registro de los clientes:
                   %-40s
                                                                                
                   Ruta registro de los trabajadores:                        
                   %-40s   
                                                                                
                   Ruta registro de los administradores:                     
                   %-40s   
                                                                                
                   Ruta registro de los productos:                           
                   %-40s   
                                                                                
                   Ruta registro de los logs:                                
                   %-40s   
                                                                                
                 """,rutaClientes, rutaTrabajadores, rutaAdministradores,rutaProductos,rutaLog);



    }

    public static void modificarModoInvitado(Controlador controlador) {
        String modoInvitado = (controlador.modoInvitado() ? "Activado" : "Desactivado");
        System.out.printf("""
                 ╔════════════════════════════════════════════════════════════════════════╗
                 ║                     Configuración de la Aplicación                     ║
                 ╠════════════════════════════════════════════════════════════════════════╣
                 ║                                                                        ║
                 ║  Actualmente la aplicación se tiene el modo Invitado:                  ║
                 ║  configurado como: %-12s                                        ║
                 ║                                                                        ║
                 ╚════════════════════════════════════════════════════════════════════════╝
                 """,modoInvitado);
    }

    public static void menuSeguridad() {
        System.out.printf("""
                 ╔════════════════════════════════════════════════════════════════════════╗
                 ║                     Configuración de la Aplicación                     ║
                 ╠════════════════════════════════════════════════════════════════════════╣
                 ║                                                                        ║
                 ║  1- Realizar una copia de seguridad.                                   ║
                 ║  2- Cargar copia de seguridad.                                         ║
                 ║                                                                        ║
                 ║  0- Volver al menu Principal                                           ║
                 ╚════════════════════════════════════════════════════════════════════════╝
                 """);
    }
}
