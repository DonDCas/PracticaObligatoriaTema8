import Communications.Email;
import models.*;
import persistencia.Persistencia;
import utils.Utils;
import views.Menu;
import views.PintaConsola;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Scanner;

public class Main {
    public static final Scanner S = new Scanner(System.in);
    public static void main(String[] args) {
        Controlador controlador = new Controlador();

        do{
            Menu.logo();
            Object user = menuInicio(controlador);
            if(user != null) menuUsuario(controlador,user);
        }while (true);
    }

    //Metodo para pedir datos
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


    //Menu inicial de la aplicación.
    private static Object menuInicio(Controlador controlador) {
        int op = 0;
        boolean modoInvitado = controlador.modoInvitado();
        do{
            Menu.menuInicio(modoInvitado);
            try {
                op = Integer.parseInt(pedirPorTeclado("\nMarque su opción: "));
            } catch (NumberFormatException e){
                System.out.println("No se introdujo un numero");
            }
            if (op == 3 && !modoInvitado) op = 0;
            if (op == 0){
                System.out.println("Opción no valida");
                Utils.pulsaEnter();
            }
        }while (op == 0);
        Object user = null;
        switch(op){
            case 1 -> registrarUser(controlador);
            case 2 ->  user = login(controlador);
            case 3 -> menuMostrarCatalogo(controlador);
            default -> Utils.pulsaEnter("Opción no valida.(Pulsa Enter para continuar).");
        }
        return user;
    }

    // Funciones para pintar Catalogo

    // Menu para que el cliente elija como desea que se le muestren los productos
    private static void menuMostrarCatalogo(Controlador controlador) {
        int op = -1;
        do{
            op = -1;
            Utils.limpiarPantalla();
            Menu.menuSeleccionDeCatalogos();
            try {
                op = Integer.parseInt(pedirPorTeclado("Marque su opción: "));
            } catch (NumberFormatException e){
                System.out.println("Eso no es una opción valida");
                Utils.pulsaEnter();
            }
            switch (op){
                case 1 -> mostrarCatalogoCompleto(controlador);
                case 2 -> mostrarCatalogoPorDescripcion(controlador);
                case 3 -> mostrarCatalogoPorMarca(controlador);
                case 4 -> mostrarCatalogoPorModelo(controlador);
                case 5 -> mostrarCatalogoPorPrecios(controlador);
                case 6 -> mostrarCatalogoPorTermino(controlador);
            }
        }while (op != 0);
    }

    //Metodo que muestra todos los productos del catalogo segun el modelo introducido
    private static void mostrarCatalogoPorModelo(Controlador controlador) {
        Utils.limpiarPantalla();
        String modelo = pedirPorTeclado("\nIntroduce que el modelo que buscas: ");
        ArrayList<Producto> productosByModelo = controlador.buscaProductosByModelo(modelo);
        if (productosByModelo.isEmpty()) Utils.pulsaEnter("No se encontró ningún producto de ese modelo. (Pulsa Enter para continuar).");
        else {
            int cont = 0;
            Utils.limpiarPantalla();
            for (Producto p : productosByModelo){
                PintaConsola.pintaProductoCatalogo(p);
                if (cont == 4 || p == productosByModelo.getLast()){
                    Utils.pulsaEnter("Producto " + (productosByModelo.indexOf(p) + 1)+ " de " +
                            productosByModelo.size());
                    Utils.limpiarPantalla();
                    cont = -1;
                }
                cont++;
            }
        }
    }

    //Metodo que muestra todos los productos del catálogo
    private static void mostrarCatalogoCompleto(Controlador controlador) {
        int cont = 0;
        Utils.limpiarPantalla();
        ArrayList<Producto> catalogo = controlador.getCatalogo();
        for (Producto p : catalogo){
            PintaConsola.pintaProductoCatalogo(p);
            if (cont == 4 || p == catalogo.getLast()){
                Utils.pulsaEnter("Producto " + (catalogo.indexOf(p) + 1)+ " de " + catalogo.size());
                Utils.limpiarPantalla();
                cont = -1;
            }
            cont++;

        }
    }

    //Metodo que muestra por pantalla el catálogo dependiendo de que
    // la descripción del producto contenga lo que escriba el usuario
    private static void mostrarCatalogoPorDescripcion(Controlador controlador) {
        Utils.limpiarPantalla();
        String descripcion = pedirPorTeclado("\nIntroduce que es lo que buscas: ");
        ArrayList<Producto> productosByDescripcion = controlador.buscaProductosByDescripcion(descripcion);
        if (productosByDescripcion.isEmpty()) Utils.pulsaEnter("No se encontró ningún producto con esa descripción. (Pulsa Enter para continuar).");
        else {
            int cont = 0;
            Utils.limpiarPantalla();
            for (Producto p : productosByDescripcion){
                PintaConsola.pintaProductoCatalogo(p);
                if (cont == 4 || p == productosByDescripcion.getLast()){
                    Utils.pulsaEnter("Producto " + (productosByDescripcion.indexOf(p) + 1)+ " de " +
                            productosByDescripcion.size());
                    Utils.limpiarPantalla();
                    cont = -1;
                }
                cont++;
            }
        }

    }

    //Metodo que muestra un catálogo según las márcas que se le introduzcan
    private static void mostrarCatalogoPorMarca(Controlador controlador) {
        Utils.limpiarPantalla();
        Menu.menuMarcasProductos(controlador);
        String marca = pedirPorTeclado("\nIntroduce la marca por la que te interese buscar: ");
        marca = Utils.limpiaEspacios(marca);
        ArrayList<Producto> productosByMarca = controlador.buscaProductosByMarca(marca);
        if (productosByMarca.isEmpty()) Utils.pulsaEnter("Marca no encontrada. (Pulsa Enter para continuar).");
        else {
            int cont = 0;
            //Utils.limpiarPantalla();
            for (Producto p : productosByMarca){
                PintaConsola.pintaProductoCatalogo(p);
                if (cont == 4|| p == productosByMarca.getLast()){
                    Utils.pulsaEnter("Producto " + (productosByMarca.indexOf(p) + 1)+ " de " +
                            productosByMarca.size());
                    Utils.limpiarPantalla();
                    cont = -1;
                }
                cont++;
            }
        }

    }

    //Metodo que muestra un catálogo según un rango de precios
    private static void mostrarCatalogoPorPrecios(Controlador controlador) {
        Utils.limpiarPantalla();
        float minPrecio = -1;
        float maxPrecio = 0;
        PintaConsola.pintaAdvertenciaBuscaPrecios();
        String min, max;
        do{
            min = pedirPorTeclado("\nIntroduzca un precio mínimo: ");
            max = pedirPorTeclado("\nIntroduzca un precio maximo: ");
            min = min.trim();
            max = max.trim();
            if (min.isEmpty() && max.isEmpty()){
                Utils.veteADormir("Saliendo");
                minPrecio = 0;
                maxPrecio = 1;
            } else{
                try {
                    if (min.isEmpty()) minPrecio = 0;
                    else minPrecio = Float.parseFloat(min);
                    if (max.isEmpty()) maxPrecio = Float.MAX_VALUE;
                    else maxPrecio = Float.parseFloat(max);
                } catch (NumberFormatException e) {
                    Utils.pulsaEnter("No se introdujo un numero de forma correcta.");
                }
            }
        } while (minPrecio<=-1 || maxPrecio <=0);
        if (!min.isEmpty() || !max.isEmpty()){
            if (maxPrecio < minPrecio) {
                Utils.pulsaEnter("Se introdujo un precio maximo menor que el minimo," +
                        "\n se procede a intercambiar los valores.");
                float aux = maxPrecio;
                maxPrecio = minPrecio;
                minPrecio = aux;
            }
            ArrayList<Producto> productosByPrecios = controlador.buscaProductosByPrecio(minPrecio, maxPrecio);
            if (productosByPrecios.isEmpty())
                Utils.pulsaEnter("No se encontró ningún producto en el rango de precios. (Pulsa Enter para continuar).");
            else {
                int cont = 0;
                Utils.limpiarPantalla();
                for (Producto p : productosByPrecios) {
                    PintaConsola.pintaProductoCatalogo(p);
                    if (cont == 4 || p == productosByPrecios.getLast()) {
                        Utils.pulsaEnter("Producto " + (productosByPrecios.indexOf(p) + 1) + " de " +
                                productosByPrecios.size());
                        Utils.limpiarPantalla();
                        cont = -1;
                    }
                    cont++;
                }
            }
        }
    }

    //Metodo que muestra un catalogo según un termino pasado por el usuario
    private static void mostrarCatalogoPorTermino(Controlador controlador) {
        Utils.limpiarPantalla();
        String termino = pedirPorTeclado("\nIntroduce que es lo que buscas: ");
        ArrayList<Producto> productosByTermino = controlador.buscaProductosByTermino(termino);
        if (productosByTermino.isEmpty()) Utils.pulsaEnter("No se encontró ningún producto relacionado. (Pulsa Enter para continuar).");
        else {
            int cont = 0;
            Utils.limpiarPantalla();
            for (Producto p : productosByTermino){
                PintaConsola.pintaProductoCatalogo(p);
                if (cont == 4 || p == productosByTermino.getLast()){
                    Utils.pulsaEnter("Producto " + (productosByTermino.indexOf(p) + 1)+ " de " +
                            productosByTermino.size());
                    Utils.limpiarPantalla();
                    cont = -1;
                }
                cont++;
            }
        }
    }

    //Metodo sobrecargado para buscar productos para el carrtio de un cliente
    private static int mostrarCatalogoPorTermino(Controlador controlador, String termino) {
        Utils.limpiarPantalla();
        ArrayList<Producto> productosByTermino = controlador.buscaProductosByTermino(termino);
        if (productosByTermino.isEmpty()) Utils.pulsaEnter("No se encontró ningún producto relacionado. (Pulsa Enter para continuar).");
        else {
            //TODO estp va raro
            int cont = -1;
            int op = -1;
            Utils.limpiarPantalla();
            for (Producto p : productosByTermino){
                cont++;
                if (cont == 0){
                    PintaConsola.cabeceraCatalogoReducido();
                }
                PintaConsola.pintaProductoCatalogoReducido(p, (cont)+1);
                if (cont == 4 || p == productosByTermino.getLast()){
                    PintaConsola.pieCatalogoReducido();
                    Utils.posicionLista("Producto: ", p, productosByTermino);
                    if (pedirPorTecladoSN("¿Encontraste lo que buscas?(S/N): ").equalsIgnoreCase("S")){
                        try{
                            op = Integer.parseInt(pedirPorTeclado("Introduce el numero asociado al producto elegido: "));
                        }catch (NumberFormatException e){
                            Utils.pulsaEnter("No se introdujo un numero");
                        }
                        if (op < 1 || op > cont+1) Utils.pulsaEnter("Opción introducida erronea.");
                        else {
                            int idProducto = productosByTermino.get(productosByTermino.indexOf(p) - (cont - op)-1).getId();
                            Producto producto = controlador.buscaProductoById(idProducto);
                            PintaConsola.pintaProductoCatalogo(producto);
                            if (pedirPorTecladoSN("¿Es este el producto deseado?(S/N): ").equalsIgnoreCase("s"))
                                return idProducto;
                        }
                    }
                    Utils.limpiarPantalla();
                    cont = -1;
                }
            }
        }
        return -1;
    }


    // Funciones relacionadas con el registro de usuarios

    // Funcion que pide datos al usuario para realizar el registro y
    // termina enviando un correo con el token para ingresar a la app
    private static void registrarUser(Controlador controlador) {
        Utils.limpiarPantalla();
        //TODO SE PODRIA PONER UN PINTA INTRO DE DATOS
        System.out.printf("""
                ____________________________________________________________________________________________
                Bienvenido al sistema para registrarte como nuevo usuario.
                
                Puedes abandonar este sistema en cualquier momento,
                solo debes dejar vacío el formulario que estes rellenando en ese momento.
                ____________________________________________________________________________________________
                """);
        String correo = "";
        String clave = "";
        String nombre = "";
        String introMovil = "";
        String localidad = "";
        String provincia = "";
        String direccion = "";
        int movil = -1;
        boolean correoExiste;
        do{
            correoExiste = false;
            boolean pedirCorreo;
            do{
                pedirCorreo = true;
                correo = pedirPorTeclado("Introduce un email: ").toLowerCase();
                if (!correo.isEmpty() && !Utils.validaCorreo(correo)){
                    System.out.println("Correo no valido. Introduzca un correo con dominio correcto");
                    if (pedirPorTecladoSN("Volver a introducir (S/N): ").equalsIgnoreCase("n")){
                        pedirCorreo = false;
                        correo = "";
                    }
                }else pedirCorreo = false;
                if (correo.isEmpty()) pedirCorreo = false;
            }while (pedirCorreo);
            if (!correo.isEmpty()){
                if (controlador.existeCorreo(correo)){
                    Utils.pulsaEnter("El correo ya existe en el sistema.");
                    correoExiste = true;
                }
            }
        } while(correoExiste);
        if(!correo.isEmpty()){
            boolean pedirClave;
            do {
                pedirClave = true;
                clave = pedirPorTeclado("Introduce una contraseña segura: ");
                if (!clave.isEmpty()){
                    if (!Utils.validaClave(clave)){
                        System.out.println("""
                        Esta contraseña no es valida.
                        - La clave debee de tener una longitud de 8 caracteres
                        - La clave debe de contener mayusculas
                        - La clave debe de contener minusculas
                        - La clave debe de contener numeros
                        - La clave debe de contener un caracter especial (-_!@/&.:,;·#$€%¬/()=?¿¡)""");
                        if (pedirPorTecladoSN("¿Volver a intentar introducir una clave valida?(S/N): ").equalsIgnoreCase("n")){
                            pedirClave = false;
                            clave = "";
                        }
                    }else pedirClave = false;
                }
                if (clave.isEmpty()) pedirClave = false;
            }while (pedirClave);
            if (!clave.isEmpty()){
                nombre = pedirPorTeclado("Introduzca su nombre: ");
            }
            if (!nombre.isEmpty()) {
                direccion = pedirPorTeclado("Introduzca tu dirección: ");
            }
            if (!direccion.isEmpty()){
                localidad = pedirPorTeclado("Introduzca tu localidad: ");
            }
            if (!localidad.isEmpty()){
                provincia = pedirPorTeclado("Introduzca tu provincia: ");
            }
            if (!provincia.isEmpty()){
                boolean movilExiste;
                do{
                    movilExiste = false;
                    boolean pedirMovil;
                    movil = -1;
                    do{
                        pedirMovil = true;
                        introMovil = pedirPorTeclado("Introduzca un móvil de contacto: ");
                        if (!introMovil.isEmpty()){
                            if (Utils.esDigito(introMovil)){
                                movil = Integer.parseInt(introMovil);
                                if (!Utils.validaTelefono(movil)){
                                    System.out.println("Numero mal introducido.");
                                    if (pedirPorTecladoSN("¿Quieres volver a intentar introducir un numero?(S/N): ").equalsIgnoreCase("n"))
                                        introMovil = "";
                                    pedirMovil = false;
                                }else pedirMovil = false;
                            }else{
                                Utils.pulsaEnter("Se introdujeron letras.");
                            }
                        }else pedirMovil = false;
                    }while (pedirMovil);
                    if (!introMovil.isEmpty()){
                        int result = controlador.existemovil(movil);
                        if (result == -1){
                            Utils.pulsaEnter("Numero mal introducido");
                            movilExiste = true;
                        }
                        if (result == -2){
                            Utils.pulsaEnter("Numero ya existente en el sistema.");
                            movilExiste = true;
                        }
                        if (result == 1){
                            movilExiste = false;
                        }
                    }else movilExiste = false;
                }while (movilExiste);
            }
            if (!introMovil.isEmpty()){
                if (controlador.nuevoCliente(correo, clave, nombre, direccion, localidad, provincia, movil)){
                    Utils.pulsaEnter("Existo en el registro, revise su email para recibir el token");
                    if(pedirPorTecladoSN("¿Quieres iniciar sesión directamente? (S/N): ").equalsIgnoreCase("s")){
                        Cliente cliente = (Cliente) controlador.login(correo, clave);
                        menuCliente(controlador, cliente);
                    }
                }else{
                    Utils.pulsaEnter("Ha debido haber algún error ");
                }
            }
        }
    }


    //Funciones para el inicio de sesión

    //Función que pide correo y contraseña y devuelve el usuario a los que estan asignados
    private static Object login(Controlador controlador) {
        int oportunidades = 0;
        Object user = null;
        String correo = "";
        String clave = "";
        boolean loginVacio = false;
        System.out.println("""
                ____________________________________________________________
                Bienvenido a Inicio de sesión.
                Si quieres abandonar el inicio en algún momento,
                solo pulsa enter en un formulario vacio
                ____________________________________________________________
                """);
        do{
            boolean pedirCorreo;
            do{
                pedirCorreo = true;
                correo = pedirPorTeclado("Introduce un email: ").toLowerCase();
                if (correo.isEmpty()) pedirCorreo = false;
                else{
                    if (!Utils.validaCorreo(correo)) System.out.println("Correo no valido. Introduzca su gmail o hotmail");
                    else pedirCorreo = false;
                }
            }while (pedirCorreo);
            if(!correo.isEmpty()){
                boolean pedirClave;
                do {
                    pedirClave = true;
                    clave = pedirPorTeclado("Introduce una contraseña segura: ");
                    if (clave.isEmpty()) pedirClave = false;
                    else{
                        if (!Utils.validaClave(clave)){
                            System.out.println("Esta contraseña no es valida");
                            pedirClave = true;
                        }
                        else pedirClave = false;
                    }
                }while (pedirClave);
            }
            if (!correo.isEmpty() && !clave.isEmpty()){
                user = controlador.login(correo, clave);
                if ((user instanceof Trabajador) && ((Trabajador) user).isBaja()) user = null;
                if (user == null){
                    oportunidades++;
                    Utils.pulsaEnter("Error al autentificar. Quedan " + (4-oportunidades) + " oportunidades.");
                }
            } else{
                loginVacio = true;
            }
        }while (!loginVacio && user == null && oportunidades<4);
        if (user == null && !loginVacio) System.out.println("Demasiados intentos de autentificación. Se le regresara al menu inicial.");
        return user;
    }

    //Metodo que dependiendo del tipo de usuario muestra un diferentes menus
    public static void menuUsuario(Controlador controlador, Object user) {
        if(Controlador.registraInicioSesion(user)){
            if (user instanceof Cliente){
                if (!controlador.compruebaClienteValidado((Cliente) user)) validarCorreo(controlador,user);
                else menuCliente(controlador, (Cliente) user);
            }
            if (user instanceof Trabajador) menuTrabajador(controlador, (Trabajador) user);
            if (user instanceof Admin) menuAdmin(controlador, (Admin) user);
            Controlador.registraCierreSesion(user);
        }
    }

    //Función para validar el correo en caso de que no lo estuviera
    private static void validarCorreo(Controlador controlador, Object user) {
        System.out.println("Hemos observado que no tienes validado el correo.\n" +
                "Visita tu correo y busca el mail que te hemos envidado con el token.");
        String token;
        boolean validado = false;
        int oportunidades = 0;
        do{
            oportunidades ++;
            token = pedirPorTeclado("Introduce el token que hayas recibido: ");
            if(!controlador.validarCliente((Cliente) user, token)){
                System.out.print("Token erroneo.");
                System.out.println((oportunidades!=4)
                        ? "Quedan " + (4-oportunidades) + " oportunidades"
                        : "No quedan más oportunidades");
            }else{
                System.out.println("Token correcto. Iniciando sesión.");
                validado = true;
            }
        }while (!validado && oportunidades!=4);
        if (controlador.compruebaClienteValidado((Cliente) user)) menuCliente(controlador,(Cliente) user);
    }

    //Menu Principal de los Admins
    private static void menuAdmin(Controlador controlador, Admin user) {
        String ultimoInicioSesion = controlador.recuperarInicioSesion(user);
        controlador.guardarNuevoInicioSesion(user);
        int op = -1;
        do{
            op = -1;
            Utils.limpiarPantalla();
            Menu.menuAdmin(controlador, user, ultimoInicioSesion);
            try{
                op = Integer.parseInt(pedirPorTeclado("Introduce la opción deseada: "));
            }catch (NumberFormatException e){
                System.out.println("No se introdujo un numero correcto.");
            }
            if(op<0 && op>11) Utils.pulsaEnter("Opción no disponible");
            switch (op){
                case 1-> menuMostrarCatalogo(controlador);
                case 2-> menuModificarProducto(controlador);
                case 3-> menuMostrarResumenClientes(controlador);
                case 4-> menuMostrarResumenPedidos(controlador);
                case 5-> menuMostrarResumenTrabajadores(controlador);
                case 6-> menuMostrarEstadisticasAPP(controlador);
                case 7-> menuModificarEstadoAdmin(controlador);
                case 8-> darDeAltaTrabajador(controlador);
                case 9-> darDeBajaTrabajador(controlador);
                case 10-> menuAsignarPedidoATrabajador(controlador);
                case 11-> mostrarUltimosIniciosSesion(controlador);
                case 12-> exportarpedidosAExcel(controlador, user);
                case 13-> exportarCopiaDeSeguridad(controlador, user);
                case 14 -> administrarConfiguracionApp(controlador);
                case 15 -> modificarModoInvitado(controlador);
            }
        }while (op != 0);
    }

    private static void modificarModoInvitado(Controlador controlador) {
        Utils.limpiarPantalla();
        Menu.modificarModoInvitado(controlador);
        if (pedirPorTecladoSN("¿Deseas modificar este modo? (S/N): ").equalsIgnoreCase("s"))
            controlador.modificaModoInvitado((controlador.modoInvitado() ? "false" : "true"));
        Utils.veteADormir("Modificando");
        Utils.pulsaEnter("Se modifico con exito.");
    }

    private static void administrarConfiguracionApp(Controlador controlador) {
        Utils.limpiarPantalla();
        Menu.rutasConfiguracion(controlador);

        //if (op == 6)

        Utils.pulsaEnter();


    }

    private static void exportarCopiaDeSeguridad(Controlador controlador, Admin user) {
        String salida = "";
        int op = -1;
        do {
            op = -1;
            Menu.menuSeguridad();
            try {
                op = Integer.parseInt(pedirPorTeclado("Introduce la opción deseada: "));
            } catch (NumberFormatException e) {
                Utils.pulsaEnter("Se introdujo la opción de forma incorrecta.");
            }
            switch (op) {
                case 1 -> realizarCopiaSeguridad(controlador, user);
                case 2 -> cargarCopiaSeguridad(controlador, user);
                default -> Utils.pulsaEnter("Opción incorrecta.");
            }
            if (op == 1 || op==2){
                Utils.veteADormir("Volviendo al menu principal");
                op = 0;
            }
        } while (op != 0);
    }

    private static void cargarCopiaSeguridad(Controlador controlador, Admin user) {
        String rutaArchivo = "";
        do{
            Utils.limpiarPantalla();
            PintaConsola.pintaAyudaArchivos();
            rutaArchivo = pedirPorTeclado("Introduce la ruta en la que se encuentra el archivo de backup: ");
            if (!rutaArchivo.isEmpty()){
                Utils.pulsaEnter(controlador.importarCopiaDeSeguridad(rutaArchivo)
                        ? "Se ha cargado la copia de seguiradad."
                        : "Hubo algun error al importar la copia de seguridad.");

            }else Utils.pulsaEnter("Debes de introducir una ruta al archivo que sea correcta. ");

        } while (rutaArchivo.isEmpty());
    }

    private static void realizarCopiaSeguridad(Controlador controlador, Admin user) {
        if (pedirPorTecladoSN("¿Quieres exportarlo en la ruta por defecto? (S/N): ").equalsIgnoreCase("s")) {
                Utils.pulsaEnter(controlador.exportaCopiaDeSegridad("")
                        ? "Se ha realizado la copia de seguridad correctamente."
                        : "No se ha podido realizar la copia de seguridad.");
        } else {
            PintaConsola.pintaAyudaRutas();
            String rutaArchivo = pedirPorTeclado("Introduce la ruta donde quieres ubicar los archivos: ");
            Utils.pulsaEnter(controlador.exportaCopiaDeSegridad(rutaArchivo)
                    ? "Se ha realizado la copia de seguridad correctamente."
                    : "No se ha podido realizar la copia de seguridad.");
        }
        Utils.pulsaEnter();
    }

    private static void exportarpedidosAExcel(Controlador controlador, Admin admin) {
        int op = -1;
        do{
            Utils.limpiarPantalla();
            Menu.menuExportar(controlador);
            try{
                op = Integer.parseInt(pedirPorTeclado("Introduce la opción deseada: "));
            }catch (NumberFormatException e){
                System.out.println("Se introdujo la opcion de forma incorrecta.");
                op = -1;
            }
            if (op < 0 || op > 4){
                Utils.pulsaEnter("Opción no disponible.");
                if (pedirPorTecladoSN("¿Volver a intentarlo? (S/N): ").equalsIgnoreCase("n")) op = 0;
            }else{

                switch (op){
                    case 1 -> controlador.exportarAExcelPedidos(controlador.getTodosPedidos(), "TodosLosPedidos", admin.getCorreo());
                    case 2 -> controlador.exportarAExcelPedidos(controlador.pedidosSinTrabajador(), "PedidosSinAsignar", admin.getCorreo());
                    case 3 -> controlador.exportarAExcelPedidos(controlador.getPedidosCompletados(), "Pedidos_Completados", admin.getCorreo());
                    case 4 -> controlador.exportarAExcelPedidos(controlador.getPedidosSinCompletar(), "Pedidos_Sin_Completar", admin.getCorreo());
                }
                if(op>0 && pedirPorTecladoSN("¿Desea realizar otra operación?(S/N): ").equalsIgnoreCase("n")) op = 0;
            }

        } while (op != 0);

    }

    private static void mostrarUltimosIniciosSesion(Controlador controlador) {
        Utils.limpiarPantalla();
        PintaConsola.ultimasSesiones(controlador);
        Utils.pulsaEnter("Estos son todos los inicios de sesión hasta el momento");
    }

    //Menu Que muestra un resumen de todos los clientes registrados. El Admin tiene la posibilidad de ver
    // los datos de un cliente elegido de forma extendida.
    private static void menuMostrarResumenClientes(Controlador controlador) {
        String idCliente;
        ArrayList<Cliente> todoslosClientes = controlador.getClientes();
        Cliente clienteExtendido = null;
        do{
            idCliente = "";
            Utils.limpiarPantalla();
            idCliente = PintaConsola.pintaResumenClientes(todoslosClientes);
            if (!idCliente.isEmpty()){
                clienteExtendido = controlador.buscaClienteById(idCliente);
                if (clienteExtendido == null) Utils.pulsaEnter("Id no encontrada.");
                if (clienteExtendido != null){
                    Utils.limpiarPantalla();
                    PintaConsola.pintaDatosCliente(controlador, clienteExtendido);
                    Utils.pulsaEnter("Aqui tienes los datos extendidos del cliente");
                }
                if(pedirPorTecladoSN("¿Quieres volver a ver la lista de clientes?(S/N): ").equalsIgnoreCase("s")) idCliente = "a";
                else idCliente = "";
            }
        }while (!idCliente.isEmpty());
        Utils.veteADormir("Volviendo al menu Principal.");
    }

    /*Menu que muestra una lista resumida de todos los pedidos realizados. Si el admin lo desea se le otorga los datos
    de un pedido de forma extendida*/
    private static void menuMostrarResumenPedidos(Controlador controlador) {
        String idPedido;
        ArrayList<Pedido> todoslosPedidos = controlador.getTodosPedidos();
        ArrayList<PedidoClienteDataClass> datosCliente = controlador.getTodosPedidosConCliente();
        Pedido pedidoExtendido = null;
        do{
            idPedido = "";
            Utils.limpiarPantalla();
            idPedido = PintaConsola.pintaResumenPedidos(todoslosPedidos, datosCliente);
            if (!idPedido.isEmpty()){
                pedidoExtendido = controlador.buscaPedidoById(idPedido);
                if (pedidoExtendido == null) Utils.pulsaEnter("Id no encontrada.");
                else{
                    PedidoClienteDataClass datos = controlador.getPedidoClienteUnico(idPedido);
                    Utils.limpiarPantalla();
                    Trabajador trabajadorDelPedido = controlador.buscaTrabajadorAsignadoAPedido(idPedido);
                    PintaConsola.pintaDatosPedidoExtendido(pedidoExtendido, datos, trabajadorDelPedido);
                    Utils.pulsaEnter("Aqui tienes los datos extendidos del pedido");
                }
                if(pedirPorTecladoSN("¿Quieres volver al principio de la lista de pedidos?(S/N): ").equalsIgnoreCase("s")) idPedido = "a";
                else idPedido = "";
            }
        }while (!idPedido.isEmpty());
        Utils.veteADormir("Volviendo al menu principal.");
    }

    /*Menu que muestra un resumen de todos los trabajadores registrados. El admin tiene la posibilidad
    de ver los datos más extendidos del trabajador elegido.*/
    private static void menuMostrarResumenTrabajadores(Controlador controlador) {
        String idTrabajador;
        ArrayList<Trabajador> todoslosTrabajadores = controlador.getTrabajadoresDeAlta();
        Trabajador trabajadorExtendido = null;
        do{
            idTrabajador = "";
            Utils.limpiarPantalla();
            idTrabajador = PintaConsola.pintaResumenTrabajadores(controlador, todoslosTrabajadores);
            if (!idTrabajador.isEmpty()){
                trabajadorExtendido = controlador.buscaTrabajadorByID(idTrabajador);
                if (trabajadorExtendido == null) Utils.pulsaEnter("Id no encontrada.");
                if (trabajadorExtendido != null){
                    Utils.limpiarPantalla();
                    PintaConsola.pintaDatosTrabajador(controlador, trabajadorExtendido);
                    Utils.pulsaEnter("Aqui tienes los datos extendidos del trabajador");
                }
                if(pedirPorTecladoSN("¿Quieres volver a ver la lista de trabajadores?(S/N): ").equalsIgnoreCase("s")) idTrabajador = "a";
                else idTrabajador = "";
            }
        }while (!idTrabajador.isEmpty());
        Utils.veteADormir("Volviendo al menu principal.");
    }

    //Metodo que muestra las estadisticas de la aplicación.
    private static void menuMostrarEstadisticasAPP(Controlador controlador) {
        Utils.limpiarPantalla();
        PintaConsola.pintaEstadisticas(controlador);
        Utils.pulsaEnter();
    }

    //Menu para que el Admin elija que pedido quiere modificarle el estado o añadirle un comentario.
    private static void menuModificarEstadoAdmin(Controlador controlador) {
        String idPedido;
        ArrayList<Pedido> todoslosPedidos = new ArrayList<>();
        ArrayList<PedidoClienteDataClass> datosCliente = new ArrayList<>();
        Pedido pedidoExtendido = null;
        do{
            todoslosPedidos = controlador.getTodosPedidos();
            datosCliente = controlador.getTodosPedidosConCliente();
            idPedido = "";
            Utils.limpiarPantalla();
            idPedido = PintaConsola.pintaResumenPedidos(todoslosPedidos, datosCliente);
            pedidoExtendido = controlador.buscaPedidoById(idPedido);
            if (!idPedido.isEmpty()){
                pedidoExtendido = controlador.buscaPedidoById(idPedido);
                if (pedidoExtendido == null) Utils.pulsaEnter("Id no encontrada.");
                else{
                    PedidoClienteDataClass datos = controlador.getPedidoClienteUnico(idPedido);
                    Trabajador trabajadorDePedido = controlador.buscaTrabajadorAsignadoAPedido(idPedido);
                    PintaConsola.pintaDatosPedidoExtendido(pedidoExtendido, datos, trabajadorDePedido);
                    if (pedirPorTecladoSN("¿Es este el Pedido que deseas modificar? (S/N)").equalsIgnoreCase("S")) {
                        modificarEstadoOAddComentario(controlador, trabajadorDePedido, pedidoExtendido);
                    }
                    Utils.limpiarPantalla();
                }
                if(pedirPorTecladoSN("¿Quieres volver a ver la lista de pedidos?(S/N): ").equalsIgnoreCase("s")) idPedido = "a";
                else idPedido = "";
            }
        }while (!idPedido.isEmpty());

    }


    // Metodo para dar alta a un trabajador nuevo y en caso de que ya existiera en el sistema  se le vuelve a dar de alta.
    private static void darDeAltaTrabajador(Controlador controlador) {
        Utils.limpiarPantalla();
        //TODO SE PODRIA PONER UN PINTA INTRO DE DATOS
        System.out.printf("""
                ____________________________________________________________________________________________
                Bienvenido al sistema para la dación el alta de un trabajador.
                
                Puedes abandonar este sistema en cualquier momento,
                solo debes dejar vacío el formulario que estes rellenando en ese momento.
                ____________________________________________________________________________________________
                """);
        String correo = "";
        String clave = "";
        String nombre = "";
        String introMovil = "";
        String introTelegram = "";
        int idTelegram = -1;
        int movil = -1;
        boolean correoExiste;
        boolean trabajadorYaRegistrado = false;
        do{
            correoExiste = false;
            boolean pedirCorreo;
            do{
                pedirCorreo = true;
                correo = pedirPorTeclado("Introduce un email: ").toLowerCase();
                if (!correo.isEmpty() && !Utils.validaCorreo(correo)){
                    System.out.println("Correo no valido. Introduzca su gmail o hotmail");
                    if (pedirPorTecladoSN("Volver a introducir (S/N): ").equalsIgnoreCase("n")){
                        pedirCorreo = false;
                        correo = "";
                    }
                }else pedirCorreo = false;
                if (correo.isEmpty()) pedirCorreo = false;
            }while (pedirCorreo);
            if (!correo.isEmpty()){
                if (controlador.existeCorreo(correo)){
                    Trabajador trabajadorDeBaja = controlador.buscaTrabajadorByEmail(correo);
                    if (trabajadorDeBaja != null && trabajadorDeBaja.isBaja()) {
                        PintaConsola.pintaDatosPersonalesTrabajador(trabajadorDeBaja);
                        if (pedirPorTecladoSN("Ya hay un trabajador registrado en la base de datos con el correo introducido que esta dado de baja\n" +
                                "¿Quieres volver a darle de alta? (S/N): ").equalsIgnoreCase("S")){
                            if (controlador.darAltaTrabajador(trabajadorDeBaja)){
                                Utils.pulsaEnter("Trabajador dado de Alta");
                                trabajadorYaRegistrado = true;
                            }else Utils.pulsaEnter("No se ha podido dar de alta al nuevo trabajador.");
                        }else{
                            correoExiste = true;
                        }
                    }else{
                        Utils.pulsaEnter("El correo ya existe en el sistema.");
                        correoExiste = true;
                    }
                }
            }
        } while(correoExiste);
        if(!correo.isEmpty()){
            if (!trabajadorYaRegistrado){
                boolean pedirClave;
                do {
                    pedirClave = true;
                    clave = pedirPorTeclado("Introduce una contraseña segura: ");
                    if (!clave.isEmpty()){
                        if (!Utils.validaClave(clave)){
                            System.out.println("""
                            Esta contraseña no es valida.
                            - La clave tiene que contener mayusculas
                            - La clave tiene que contener minusculas
                            - La clave tiene que contener numeros
                            - La clave tiene que contener un caracter especial (-_!@/&.:,;·#$€%¬/()=?¿¡)""");
                            if (pedirPorTecladoSN("¿Volver a intentar introducir una clave valida?(S/N): ").equalsIgnoreCase("n")){
                                pedirClave = false;
                                clave = "";
                            }
                        }else pedirClave = false;
                    }
                    if (clave.isEmpty()) pedirClave = false;
                }while (pedirClave);
                if (!clave.isEmpty()){
                    nombre = pedirPorTeclado("Introduce el nombre del trabajador: ");
                }
                if (!nombre.isEmpty()){
                    boolean movilExiste;
                    do{
                        movilExiste = false;
                        boolean pedirMovil;
                        movil = -1;
                            do{
                                pedirMovil = true;
                                introMovil = pedirPorTeclado("Introduce un móvil de contacto: ");
                                if (!introMovil.isEmpty()){
                                    if (Utils.esDigito(introMovil)){
                                        movil = Integer.parseInt(introMovil);
                                        if (!Utils.validaTelefono(movil)){
                                            System.out.println("Numero mal introducido.");
                                            if (pedirPorTecladoSN("¿Quieres volver a intentar introducir un numero?(S/N): ").equalsIgnoreCase("n"))
                                                introMovil = "";
                                                pedirMovil = false;
                                        }else pedirMovil = false;
                                    }else{
                                        Utils.pulsaEnter("Se introdujeron letras.");
                                    }
                                }else pedirMovil = false;
                            }while (pedirMovil);
                            if (!introMovil.isEmpty()){
                                int result = controlador.existemovil(movil);
                                if (result == -1){
                                    Utils.pulsaEnter("Numero mal introducido");
                                    movilExiste = true;
                                }
                                if (result == -2){
                                    Utils.pulsaEnter("Numero ya existente en el sistema.");
                                    movilExiste = true;
                                }
                                if (result == 1){
                                    movilExiste = false;
                                }
                            }else movilExiste = false;
                    }while (movilExiste);
                }
                if (!introMovil.isEmpty()){
                    idTelegram = -1;
                    boolean pedirTelegram;
                    do{
                        pedirTelegram = true;
                        introTelegram = pedirPorTeclado("Introduce la ID de telegram para este trabajador: ");
                        if(!introTelegram.isEmpty()){
                            if(Utils.esDigito(introTelegram)){
                                idTelegram = Integer.parseInt(introTelegram);
                                pedirTelegram = false;
                            }else{
                                if (pedirPorTecladoSN("No introdujiste bien el ID, ¿Quieres volver a intentarlo?: ").equalsIgnoreCase("n")){
                                    pedirTelegram = false;
                                    introTelegram = "";
                                }
                            }
                        }else pedirTelegram = false;
                    }while (pedirTelegram);
                }
                if (!introTelegram.isEmpty()){
                    Utils.pulsaEnter(controlador.nuevoTrabajador(correo, clave, nombre, movil, idTelegram)
                            ? "Trabajador dado de alta."
                            : "No se ha podido dar de alta al nuevo trabajador. ");
                }
            }
        }
        Utils.veteADormir("Volviendo al menu principal.");
    }

    //Metodo que pide al Admin un id de Trabajador para darle de Alta
    private static void darDeBajaTrabajador(Controlador controlador) {
        String idTrabajador;
        Trabajador trabajadorElegido;
        ArrayList<Trabajador> todoslosTrabajadores;
        do{
            todoslosTrabajadores = controlador.getTrabajadoresDeAlta();
            Utils.limpiarPantalla();
            idTrabajador = "";
            idTrabajador = PintaConsola.pintaResumenTrabajadores(controlador, todoslosTrabajadores);
             if(!idTrabajador.isEmpty()){
                trabajadorElegido = controlador.buscaTrabajadorByID(idTrabajador);
                if (trabajadorElegido == null) Utils.pulsaEnter("Trabajador no encontrado.");
                else{
                    ArrayList<Pedido> pedidosTrabajador = controlador.recuperaPedidosPendientesTrabajador(trabajadorElegido);
                    PintaConsola.pintaDatosTrabajador(controlador, trabajadorElegido);
                    if (pedirPorTecladoSN("Es este el trabajador que has elegido? (S/N): ").equalsIgnoreCase("S")){
                        if (pedidosTrabajador.size()>0){
                            System.out.println("NO se puede dar de baja al trabajador elegido porque tiene pedidos asignados.");
                            if (pedirPorTecladoSN("¿Deseas que se quitarle los pedidos asignados y seguir con el proceso? (S/N): ").
                                    equalsIgnoreCase("s")){
                                if (controlador.quitarPedidosAsignados(trabajadorElegido, pedidosTrabajador))
                                    controlador.darBajaTrabajador(trabajadorElegido);
                            }
                        }
                        else controlador.darBajaTrabajador(trabajadorElegido);
                    }
                }
            }
        }while (!idTrabajador.isEmpty());
    }


    //Menu que muestra todos los pedidos sin asignación y elije a que trabajador asignarlo
    private static void menuAsignarPedidoATrabajador(Controlador controlador) {
        ArrayList<Pedido> pedidosSinAsignacion = controlador.pedidosSinTrabajador();
        String idPedido;
        Pedido pedidoBuscado = null;
        boolean pedidoElegido = false;
        ArrayList<PedidoClienteDataClass> datosPedidoClientes = controlador.getTodosPedidosConCliente();
        PedidoClienteDataClass datosPedidoCliente;
        do{
            Utils.limpiarPantalla();
            pedidoElegido = false;
            idPedido = "";
            idPedido = PintaConsola.pintaResumenPedidos(pedidosSinAsignacion, datosPedidoClientes);
            if(!idPedido.isEmpty()){
                pedidoBuscado = controlador.buscaPedidoById(idPedido);
                if (pedidoBuscado == null) Utils.pulsaEnter("No se encontro un pedido con esa ID.");
                else{
                    datosPedidoCliente = controlador.getPedidoClienteUnico(idPedido);
                    PintaConsola.pintaDatosPedidoExtendido(pedidoBuscado, datosPedidoCliente, null);
                    if(pedirPorTecladoSN("¿Quieres elegir este pedido para Asignar? (S/N): ").equalsIgnoreCase("S")){
                        pedidoElegido = true;
                        Utils.pulsaEnter("Pedido Elegido");
                    }
                }
            }
        }while (!idPedido.isEmpty() && !pedidoElegido);
        boolean trabajadorElegido;
        if(pedidoElegido){
            ArrayList<Trabajador> trabajadores = controlador.getTrabajadoresDeAlta();
            String idTrabajador;
            Trabajador trabajadorBuscado = null;
            do{
                trabajadorElegido = false;
                idTrabajador = "";
                idTrabajador = PintaConsola.pintaResumenTrabajadores(controlador, trabajadores);
                if(!idTrabajador.isEmpty()){
                    trabajadorBuscado = controlador.buscaTrabajadorByID(idTrabajador);
                    if (trabajadorBuscado != null){
                        PintaConsola.pintaDatosTrabajador(controlador, trabajadorBuscado);
                        if (pedirPorTecladoSN("Este es el trabajador al que quieres asignarle el pedido? (S/N): ").equalsIgnoreCase("S")){
                            trabajadorElegido = true;
                        }
                    }
                }
            }while (!idTrabajador.isEmpty() && !trabajadorElegido);
            if (trabajadorElegido)
                Utils.pulsaEnter(controlador.asignaPedido(idPedido, idTrabajador)
                        ? "Pedido Asignado con exito a " + trabajadorBuscado.getNombre()
                        : "Error al asignar el pedido");
        }
    }

    //Metodos del Trabajador

    //Menu principal de los trabajadores. Se pide opción y manda al trabajador al metodo correspondiente
    private static void menuTrabajador(Controlador controlador, Trabajador user) {
        String ultimoInicioSesion = controlador.recuperarInicioSesion(user);
        controlador.guardarNuevoInicioSesion(user);
        int op = -1;
        do{
            op = -1;
            Utils.limpiarPantalla();
            Menu.menuTrabajador(controlador, user, ultimoInicioSesion);
            try{
                op = Integer.parseInt(pedirPorTeclado("Introduce la opción deseada: "));
            }catch (NumberFormatException e){
                System.out.println("No se introdujo un numero correcto.");
            }
            switch (op){
                case 1-> menuConsultaPedidosAsignados(controlador, user);
                case 2-> menuModificarEstadoPedido(controlador, user);
                case 3-> menuMostrarCatalogo(controlador);
                case 4-> menuModificarProducto(controlador);
                case 5-> menuMostrarHistorialPedidos(controlador, user);
                case 6-> pintaDatosTrabajador(controlador, user);
                case 7-> menuModificarDatosTrabajador(controlador, user);
            }
        }while (op != 8);
    }

    //Metodo que muestra el listado de pedidos asignados al trabajador
    private static void menuConsultaPedidosAsignados(Controlador controlador, Trabajador user) {
        String idPedido = "";
        ArrayList<Pedido> pedidosAsignados = controlador.recuperaPedidosAsignadosTrabajador(user);
        if (pedidosAsignados.size()<1) Utils.pulsaEnter("No tienes pedidos asignados.");
        else{
            do{
                Utils.limpiarPantalla();
                idPedido = PintaConsola.pintaPedidosAsignados(pedidosAsignados, controlador.getPedidosAsignadosTrabajador(pedidosAsignados));
                if(!idPedido.isEmpty()){
                    PedidoClienteDataClass datosPedido = controlador.getPedidoClienteUnico(idPedido);
                    PintaConsola.pintaDatosPedidoExtendido(controlador.buscaPedidoById(idPedido),datosPedido, user);
                    Utils.pulsaEnter("\nAqui tienes el pedido extendido.");
                }
                if (pedirPorTecladoSN("¿Quieres volver al menu de pedidos? (S/N): ").equalsIgnoreCase("n"))
                    idPedido="";
            }while (!idPedido.isEmpty());
        }
    }

    //Metodo que muestra al trabajador los pedidos y cuando elija un by ID podra modificarle el estado, cometarios y fechas de entrega
    private static void menuModificarEstadoPedido(Controlador controlador, Trabajador user) {
        String idPedido = "";
        ArrayList<Pedido> pedidosTrabajador = controlador.recuperaPedidosPendientesTrabajador(user);
        int op = -1;
        if (pedidosTrabajador.size()<1){
            Utils.pulsaEnter("No tienes pedidos asignados pendientes.");
        }
        else{
            do{
                pedidosTrabajador = controlador.recuperaPedidosPendientesTrabajador(user);
                Utils.limpiarPantalla();
                op = -1;
                idPedido = "";
                String respuestaSN = "";
                if (pedidosTrabajador.size()>0){
                    idPedido = PintaConsola.pintaPedidosAsignados(pedidosTrabajador, controlador.getPedidosAsignadosTrabajador(pedidosTrabajador));
                    if (!idPedido.isEmpty()) {
                        Pedido pedido = controlador.buscaPedidoById(idPedido);
                        if (pedido != null){
                            PedidoClienteDataClass datosPedido = controlador.getPedidoClienteUnico(idPedido);
                            PintaConsola.pintaDatosPedidoExtendido(pedido, datosPedido, user);
                            respuestaSN = pedirPorTecladoSN("Es este el pedido que quieres modificar (S/N): ");
                            if (respuestaSN.equalsIgnoreCase("s")) {
                                modificarEstadoOAddComentario(controlador, user, pedido);
                            }
                            pedidosTrabajador = controlador.recuperaPedidosPendientesTrabajador(user);
                        }
                        else{
                            Utils.pulsaEnter("No se encontro el pedido solicitado.");
                        }
                    }
                    if (pedirPorTecladoSN("¿Quieres volver al menu de pedidos? (S/N): ").equalsIgnoreCase("n")){
                        pedidosTrabajador = controlador.recuperaPedidosPendientesTrabajador(user);
                        idPedido="";
                    }
                }
                else Utils.pulsaEnter("No tienes pedidos asignados.");
            }while (!idPedido.isEmpty());
        }
    }

    //En este metodo al usuario se le da la opción de modificar el estado del pedido o solo añadir un nuevo comentario.
    private static void modificarEstadoOAddComentario(Controlador controlador, Trabajador trabajador, Pedido pedido) {
        int op = -1;
        do{
            Menu.menuEstadoOComentario();
            try{
                op = Integer.parseInt(pedirPorTeclado("Introduce la opción deseada: "));
            }catch (NumberFormatException e){
                Utils.pulsaEnter("No se introdujo una respuesta aceptable. Introduzca un número por favor.");
            }
            if (op>0 || op<-1){
                String idPedido = pedido.getId();
                switch (op){
                    case 1:
                        do {
                            op = -1;
                            Menu.menuEstadosPedido();
                            try {
                                op = Integer.parseInt(pedirPorTeclado("Introduce el numero correspondiente estado al que quieres cambiar el pedido: "));
                            } catch (NumberFormatException e) {
                                Utils.pulsaEnter("No se introdujo un numero como corresponde");
                            }
                            if (op != -1){ //Con este if comprobamos que haya pasado el try-catch
                                if (op>0 && op<4){ //Con este if comprobamos que la opción elegida sea valida
                                    if (!controlador.cambiaEstadoPedido(idPedido, op)) { //En este if comprobamos si se pudo modificar el estado y en caso erroneo nos avisa
                                        op = -1;
                                        Utils.pulsaEnter("Opción elegida incorrecta.");
                                    }
                                    else { //En caso de haber podido moficar el estado del pedido seguimos con la logica de la modificación
                                        System.out.println("Estado modificado.");
                                        if (pedirPorTecladoSN("¿Quieres añadir tambien un comentario al Pedido? (S/N): ").equalsIgnoreCase("S"))
                                            addComentarioPedido(controlador, idPedido);
                                        cambiarFechasTrasEstado(controlador, pedido, op);
                                    }
                                }else{
                                    Utils.pulsaEnter("Este numero no corresponde a ningún estado.");
                                    op = -1;
                                }
                            }
                        } while (op == -1);
                        break;
                    case 2:
                        addComentarioPedido(controlador, idPedido);
                        break;
                    default:
                        op = -1;
                        Utils.pulsaEnter("Opción introducida no valida");
                        break;
                };
            }
        }while (op==-1);
    }

    //Metodo que cambia la fecha de entrega dependiendo de si el trabajador lo ve oportuno.
    private static void cambiarFechasTrasEstado(Controlador controlador, Pedido pedido, int op) {
        LocalDate nuevaFecha = null;
        if (op<2){//En caso de que el estado sea "En Preparación" o "Enviado" se da al trabajador la opción de modificar la fecha de entrega
            String respuesta = pedirPorTecladoSN("Deseas modificar la fecha de Entrega estimada(S/N): ");
            if (respuesta.equalsIgnoreCase("s")){
                do{
                    try{
                        nuevaFecha = Utils.stringAFecha(pedirPorTeclado("Introduce la nueva fecha en formato (dd/mm/yyyy): "));
                    } catch (Exception e) {
                        Utils.pulsaEnter("Fecha introducida en un formato incorrecto");
                    }
                    if (controlador.cambiaFechaPedido(pedido.getId(), nuevaFecha))Utils.pulsaEnter("Modificada la fecha de entrega.");
                    else {
                        Utils.pulsaEnter("No se pudo modificar la fecha de entrega.");
                        nuevaFecha = null;
                    }
                }while (nuevaFecha==null);
            }
        }
        if (op == 3) { //Si el estado cambia a "Cancelado" se cambian las fechas de entrega a NULL
            controlador.cambiaEstadoPedidoCancelado(pedido.getId());
            Utils.pulsaEnter("Se cancelo la fecha de entrega.");

            /*//Si el estado pasa a "entregado" se pide la fecha en la que se entrego.
            String respuestaSN = pedirPorTecladoSN("¿Se ha entregado hoy el paquete? (S/N): ");
            if (respuestaSN.equalsIgnoreCase("S"))
                pedido.cambiaFechaDelivery(LocalDate.now());
            else {
                do {
                    try {
                        nuevaFecha = Utils.stringAFecha(pedirPorTeclado("Introduce la fecha de llegada: "));
                    } catch (Exception e) {
                        Utils.pulsaEnter("Fecha introducida en un formato incorrecto");
                    }
                    if (!pedido.cambiaFechaDelivery(nuevaFecha)){
                        Utils.pulsaEnter("Error al modificar la fecha.");
                        nuevaFecha = null;
                    }
                    else Utils.pulsaEnter("Modificada la fecha de entrega.");
                } while (nuevaFecha == null);
            }*/
        }
       /* if (op == 4) {
            pedido.cambiaFechaEntregaCandelado();
            Utils.pulsaEnter("Se cancelo la fecha de entrega.");
        }*/
    }

    //Metodo se añade un comentario al pedido en caso de que el trabajador lo vea oportuno
    private static void addComentarioPedido(Controlador controlador, String idPedido) {
        String comentario = "";
            comentario = pedirPorTeclado("Si quieres añadir un comentario escribelo a continación(sino pulsa enter): ");
            if (!comentario.isEmpty()) //Si el usuario no introduce nada y solo pulsa enter no se añadira un comentario al pedido
                Utils.pulsaEnter(controlador.addComentario(idPedido, comentario)
                        ? "Comentario añadido correctamente."
                        : "No se pudo añadir el comentario.");
            else Utils.pulsaEnter("No añadiste un comentario. ");
    }

    //Metodo para mostrar un listado de productos y tras elegir uno modificar sus datos
    private static void menuModificarProducto(Controlador controlador) {
        int id = -1;
        do{
           id = -1;
           Utils.limpiarPantalla();
           //TODO añadir un menu para mostrar los productos en caso de que el Trabajador lo necesite.
           try{
               id = PintaConsola.pintaProductosResumidos(controlador.getCatalogo());
           } catch (NumberFormatException e) {
               Utils.pulsaEnter("No se introdujo un numero.");
               if (pedirPorTecladoSN("¿Desea volver a intentarlo? (S/N): ").equalsIgnoreCase("n")) id = 0;
           }
           if (id == -1 && pedirPorTecladoSN("No se elegido un producto. ¿Volver a ver la lista? (S/N): ").equalsIgnoreCase("N")) id = 0;
           Producto producto = controlador.buscaProductoById(id);
           if(producto != null){
               Producto productoElegido = new Producto(producto);
               PintaConsola.pintaProductoCatalogo(productoElegido);
               boolean modificadoDato = false;
               if (pedirPorTecladoSN("Es este el producto que deseas modificar (S/N): ").equalsIgnoreCase("S")){
                   int op;
                   do{
                       Utils.limpiarPantalla();
                       op = -1;
                       PintaConsola.pintaProductoCatalogo(productoElegido);
                       Menu.menuModificarProductos();
                       try{
                           op = Integer.parseInt(pedirPorTeclado("Introduce la opción deseada: "));
                       } catch (NumberFormatException e) {
                           Utils.pulsaEnter("Opción introducida no valida");
                       }
                       if (op>0 && op <6){
                           String nuevoDato = pedirPorTeclado("Introduce el dato nuevo: ");
                           if (controlador.editarProducto(productoElegido, op, nuevoDato)){
                               Utils.pulsaEnter("Se modifico el producto de forma correcta.");
                               modificadoDato = true;
                           }else{
                               if ( op != 0) Utils.pulsaEnter("Hubo un error a la hora de modificar el producto.");
                           }
                       }else{
                           if (op != 0) Utils.pulsaEnter("Opción no disponible");
                       }
                   } while (op != 0);
                   if (modificadoDato)
                       if (pedirPorTecladoSN("¿Quieres guardar los datos modificados en el nuevo? (S/N): ").equalsIgnoreCase("s")){
                           if (controlador.ModificaProduto(productoElegido)) Utils.pulsaEnter("Cambios guardados.");
                           else Utils.pulsaEnter("Fallo al guardar los cambios.");
                       }
               }
           } else if(id!=0) Utils.veteADormir("Producto no encontrado.\nVolviendo a mostrar la lista.");
        }while (id!=0);
    }

    //Mostrar historico de todos los pedidos que ha tenido el trabajador asignados
    private static void menuMostrarHistorialPedidos(Controlador controlador, Trabajador user) {
        String idPedido = "";
        ArrayList<Pedido> pedidosTrabajador = controlador.recuperaPedidosAsignadosTrabajador(user);
        if (pedidosTrabajador.size()<1) Utils.pulsaEnter("Todavía no se te ha asignado ningún pedido");
        else{
            do{
                Utils.limpiarPantalla();
                idPedido = "";
                ArrayList<PedidoClienteDataClass> datosPedidosCliente = controlador.getPedidosAsignadosYCompletados(pedidosTrabajador);
                idPedido = PintaConsola.pintaHistoricoPedidosDeTrabajador(user, datosPedidosCliente, pedidosTrabajador);
                if(!idPedido.isEmpty()){
                    Pedido pedido = controlador.buscaPedidoById(idPedido);
                    if (pedido == null) Utils.pulsaEnter("Pedido no encontrado");
                    else{
                        PedidoClienteDataClass datosPedidoCliente = controlador.getPedidoClienteUnico(idPedido);
                        PintaConsola.pintaDatosPedidoExtendido(pedido,datosPedidoCliente, user);
                        Utils.pulsaEnter("\nAqui tienes el pedido extendido.");
                    }
                }
            }while (!idPedido.isEmpty());
        }
    }

    //Metodo para pintar los datos de un trabajador
    private static void pintaDatosTrabajador(Controlador controlador, Trabajador trabajador) {
        Utils.limpiarPantalla();
        PintaConsola.pintaDatosPersonalesTrabajador(trabajador);
        Utils.pulsaEnter("Aqui tienes tus datos personales.");
    }

    //Metodo que nos permite editar los datos de un trabajador
    private static void menuModificarDatosTrabajador(Controlador controlador, Trabajador user) {
        int op = -1;
        Trabajador temp = new Trabajador(user);
        boolean existeCambio = false;
        do{
            op=-1;
            Utils.limpiarPantalla();
            Menu.menuModificarPerfilTrabajador(controlador, temp);
            try{
                op = Integer.parseInt(pedirPorTeclado("Introduce la opción deseada: "));
            }catch (NumberFormatException e){
                System.out.println("Por favor, introduzca un numero.");
            }
            if (op != -1 && (op >= 1 && op <= 4)){
                String nuevoDato = pedirPorTeclado("Introduce el nuevo dato: ");
                if (controlador.modificarDatosTrabajador(temp,nuevoDato,op)){
                    Utils.pulsaEnter("Dato modificado correctamente.");
                    existeCambio = true;
                }else Utils.pulsaEnter("Hubo un error al modificar los datos.");
                Utils.pulsaEnter();
            }
        }while (op!=0);
        if (existeCambio && pedirPorTecladoSN("¿Quieres guardar los datos? (S/N): ").equalsIgnoreCase("s")){
            Utils.pulsaEnter(controlador.clonarTrabajadorCopia(user, temp) ? "Datos modificados." : "Hubo un fallo al Modificar los datos");
        }
    }

    // Menu dedicado a los clientes
    public static void menuCliente(Controlador controlador, Cliente user){
        String ultimoInicioSesion = controlador.recuperarInicioSesion(user);
        controlador.guardarNuevoInicioSesion(user);
        int op = -1;
        do{
            Utils.limpiarPantalla();
            Menu.menuCliente(controlador, user, ultimoInicioSesion);
            try{
                op = Integer.parseInt(pedirPorTeclado("Introduce la opción deseada: "));
            }catch (NumberFormatException e){
                System.out.println("No se introdujo un numero correcto.");
            }
            switch (op){
                case 1-> menuMostrarCatalogo(controlador);
                case 2-> menuRealizarPedido(controlador, user);
                case 3-> menuMostrarPedidos(controlador, user);
                case 4-> pintaDatosCliente(controlador, user);
                case 5-> menuModificaDatosCliente(controlador, user);
            }
        }while (op != 6);
    }

    //Menu del cliente para realizar un pedido
    private static void menuRealizarPedido(Controlador controlador, Cliente user) {
        int opMenuPedido = -1;
        do{
            ArrayList<Producto> carro = controlador.actualizarCarritoCliente(user);
            HashMap<Integer, Integer> cantidadProductos = new HashMap<>();
            for(Producto p : carro) cantidadProductos.put(p.getId(), controlador.devuelveCantidadProductoCarrito(user,p.getId()));
            opMenuPedido = -1;
            Utils.limpiarPantalla();
            Menu.realizarPedido(carro, cantidadProductos);
            try{
                opMenuPedido = Integer.parseInt(pedirPorTeclado("Introduce la opción deseada: "));
            } catch (NumberFormatException e){
                Utils.pulsaEnter("No se introdujo un numero de forma correcta.");
            }
            switch (opMenuPedido){
                case 1-> menuMostrarCatalogo(controlador); // Esta opción le da al cliente la opción de ver los productos
                case 2-> mostrarCarrito(user, carro, cantidadProductos); //Esta opción le da al cliente la opción de ver su carrito
                case 3-> addProductoCarro(controlador, user);//Esta opción añadira un producto mediante la ID que introduzca el cliente
                case 4-> deleteProductoCarro(controlador,user,carro, cantidadProductos); //Esta opción le pide un id de un producto al cliente y si esta en el carro lo elimina
                case 5-> confirmaPedido(controlador, user, carro, cantidadProductos); //Esta opción finaliza el pedido
                case 6-> cancelarPedido(controlador,user, carro, cantidadProductos); //Esta opción borra el carrito si el cliente lo decide
            }
        }while (opMenuPedido <= 4 || opMenuPedido >7);
    }

    //Función para mostrar el carrito de un cliente
    private static void mostrarCarrito(Cliente user, ArrayList<Producto> carro, HashMap<Integer, Integer> cantidadProductos) {
        Utils.limpiarPantalla();
        if (carro.isEmpty()) Utils.pulsaEnter("Todavia no hay productos en el carro");
        else{
            System.out.println("Aquí esta tu carrito. ");
            PintaConsola.pintaCarroCliente(user, carro, cantidadProductos);
            Utils.pulsaEnter();
        }
    }

    //Funcion para pedir al usuario un ID de un producto y añadirlo al carrito
    private static void addProductoCarro(Controlador controlador, Cliente user) {
        int idProducto = -1;
        int cantidad = -1;
        String termino = pedirPorTeclado("¿Qué deseas añadir al carrito?: ");
        do{
            if(idProducto==-2 && pedirPorTecladoSN("¿Añadir un nuevo termino? (S/N): ").equalsIgnoreCase("s"))
                termino = pedirPorTeclado("¿Qué deseas añadir al carrito?: ");
            else idProducto = -1;
            idProducto = mostrarCatalogoPorTermino(controlador, termino);
            if (idProducto == -1) idProducto = -2;
        }while (idProducto==-2 && pedirPorTecladoSN("¿Volver a buscar? (S/N): ").equalsIgnoreCase("s"));
        if(idProducto != -2){
            Producto productoBuscado = controlador.buscaProductoById(idProducto);
            if (controlador.existeProductoCarroCliente(user, productoBuscado.getId())){
                if (pedirPorTecladoSN("El producto ya existe en el carrito. ¿Quieres pedir una mayor cantidad? (S/N): ").
                        equalsIgnoreCase("s")){
                    do{
                        try{
                            cantidad = Integer.parseInt(pedirPorTeclado("Introduce la cantidad deseada(mínimo=1): "));
                        } catch (NumberFormatException e) {
                            Utils.pulsaEnter("Se introdujo mal la cantidad deseada.");
                        }
                    } while (cantidad<1);
                }else productoBuscado=null;
            }else{
                do{
                    try{
                        cantidad = Integer.parseInt(pedirPorTeclado("Introduce la cantidad deseada(mínimo=1): "));
                    } catch (NumberFormatException e) {
                        Utils.pulsaEnter("Se introdujo mal la cantidad deseada.");
                    }
                } while (cantidad<1);
            }
            if(productoBuscado == null) Utils.pulsaEnter("Producto no añadido");
            else{
                Utils.pulsaEnter(controlador.addProductoCarrito(user, idProducto, cantidad)
                        ? "El Producto se añadido al carro"
                        : "Hubo un problema para añadir el producto al carro");
            }
        }
    }

    //Pide al usuario un id de un producto y en caso de que exista en el carrito lo elimina de este
    private static void deleteProductoCarro(Controlador controlador, Cliente user,
                                            ArrayList<Producto> carro,
                                            HashMap<Integer, Integer> cantidadProductos) {
        int op = -1;
        if (carro.size()<1)
            Utils.pulsaEnter("No hay productos en el carrito");
        else {
            System.out.println("Por favor, introduzca el numero que indica el producto que desea eliminar.");
            do{
                PintaConsola.pintaCarroClienteResumido(carro, cantidadProductos);
                try{
                    op = Integer.parseInt(pedirPorTeclado("Indica el producto que deseas eliminar (introduce 0 para salir): "));
                } catch (NumberFormatException e) {
                    System.out.println("Por favor, introduzca un número.");
                }

            }while (op == -1);
            if (op != 0){
                int id = carro.get(op-1).getId();
                Producto productoBuscado = controlador.buscaProductoById(id);
                if(productoBuscado == null) Utils.pulsaEnter("Producto no encontrado");
                else{
                    PintaConsola.pintaProductoCatalogo(productoBuscado);
                    if(pedirPorTecladoSN("¿Este es el producto que deseas eliminar del carrito? (S/N): ").equalsIgnoreCase("s")){
                        int cantidadAEliminar = -1;
                        System.out.printf("De este producto hay %d cantidad.\n", cantidadProductos.get(productoBuscado.getId()));
                        do{
                            try{
                                cantidadAEliminar = Integer.parseInt(pedirPorTeclado("¿Cuantos quieres eliminar? (Introduce 0 para eliminarlo completamente)"));
                            } catch (NumberFormatException e) {
                                System.out.println("No se introdujo un número.");
                            }
                            if(cantidadAEliminar>cantidadProductos.get(productoBuscado.getId())){
                                Utils.pulsaEnter("Se ha introducido una cantidad mayor a la existente.");
                                cantidadAEliminar=-1;
                            }
                            if (cantidadAEliminar<0) Utils.pulsaEnter("Cantidad incorrecta a introducir.");
                        }while (cantidadAEliminar<0);
                        if (controlador.borrarProductoCarrito(user, productoBuscado,
                                cantidadAEliminar, cantidadProductos.get(productoBuscado.getId()))){
                            if (cantidadAEliminar != 0 || cantidadAEliminar == cantidadProductos.get(productoBuscado.getId())){
                                cantidadProductos.remove(productoBuscado.getId());
                                System.out.println("Producto eliminado con existo.");
                            }else{
                                cantidadProductos.replace(productoBuscado.getId(),
                                        (cantidadProductos.get(productoBuscado.getId()-cantidadAEliminar)));
                                System.out.println("Producto del carrito actualizado.");
                            }
                        }else System.out.println("No se encontro el producto en su carrito");
                        Utils.pulsaEnter();
                    }
                }
            }
        }
    }

    //Metodo que le pregunta al cliente si desea finalizar su pedido
    private static void confirmaPedido(Controlador controlador, Cliente user, ArrayList<Producto> carro,
                                       HashMap<Integer, Integer> cantidadProductos) {
        if (carro.size()>0){
            System.out.println("Este es su carrito actual:");
            PintaConsola.pintaCarroCliente(user, carro, cantidadProductos);
            if (pedirPorTecladoSN("¿Si desea confirmar el pedido? (S/N): ").equalsIgnoreCase("s")){
                Utils.pulsaEnter(controlador.confirmaPedidoCliente(user, carro, cantidadProductos)
                        ? "El pedido esta en proceso"
                        : "El pedido no se ha podido realizar");
            }else Utils.pulsaEnter("Entonces se guardara el carrito hasta que usted decida modificarlo a su gusto.");
        }else{
            Utils.pulsaEnter("Tu carrito esta vacio. No se puede procesar el pedido. ");
        }

    }

    //Metodo que le pregunta al cliente si desea cancelar el pedido y en caso afirmativo borra el carrito
    private static void cancelarPedido(Controlador controlador, Cliente user, ArrayList<Producto> carro,
                                       HashMap<Integer, Integer> cantidadProductos) {
        if (carro.size()>0){
            System.out.println("Este es su carrito actual:");
            PintaConsola.pintaCarroCliente(user, carro, cantidadProductos);
            if (pedirPorTecladoSN("¿Desea borrar el pedido? (S/N)").equalsIgnoreCase("s")){
                Utils.pulsaEnter(controlador.borrarCarritoCliente(user)
                        ? "Carrito borrado con exito"
                        : "No se pudo borrar el pedido");
            }else System.out.println("Entonces se guardara el carrito hasta que usted decida modificarlo a su gusto.");
        }else Utils.pulsaEnter("No se pudo cancelar el pedido porque el carrito esta vacio. ");
    }

    // Este menu muestra los pedidos resumidos y ordenados por fecha y de 5 en 5. Cuando introduzca el id del pedido
    // Se mostrará el pedido completo
    private static void menuMostrarPedidos(Controlador controlador, Cliente user) {
        ArrayList<Pedido> pedidosCliente = controlador.buscaPedidoByCliente(user);
        if (pedidosCliente.isEmpty()) Utils.pulsaEnter("No existen pedidos para mostrar");
        else{
            System.out.println("Aqui tienes un listado con tus productos.");
            Collections.sort(pedidosCliente); // Ordenamos los pedidos por fecha de pedido.
            int cont;
            String opElegida;
            String idPedidoElegido;
            do{
                opElegida = "";
                idPedidoElegido = "";
                cont = 0;
                for(Pedido p: pedidosCliente){
                    if (idPedidoElegido.isEmpty()){
                        if (cont == 0){
                            Utils.limpiarPantalla();
                            Menu.encabezadoListaPedidos();
                        }
                        Menu.pintaPedidoResumido((cont+1), p);
                        if (cont == 4 || pedidosCliente.getLast() == p){
                            Menu.pieListadoPedidos();
                            Utils.posicionLista("Pedido: ", p, pedidosCliente);
                            if (pedirPorTecladoSN("¿Quieres ver más extendendido alguno de estos pedidos? (S/N): ").
                                    equalsIgnoreCase("s")){
                                opElegida = pedirPorTeclado("Elige el numero asignado al pedido deseado: ");
                                if (!opElegida.isEmpty()){
                                    if (!Utils.esDigito(opElegida)) Utils.pulsaEnter("No se introdujo un numero.");
                                    else{
                                        int op = Integer.parseInt(opElegida);
                                        if (op<1||op>(cont+1)) Utils.pulsaEnter("Opción no disponible");
                                        else{
                                            idPedidoElegido = pedidosCliente.get(pedidosCliente.indexOf(p)-(cont-(op-1))).getId();
                                        }
                                    }
                                }
                            } else{
                                Utils.veteADormir("Cargando más pedidos");
                                System.out.println();
                                cont = -1;
                            }
                        }
                        cont++;
                    }
                }
                if(!idPedidoElegido.isEmpty()){
                    Pedido pedidoBuscado = controlador.buscaPedidoById(idPedidoElegido);
                    Utils.limpiarPantalla();
                    if (pedidoBuscado != null){
                        PintaConsola.pintaPedidoParaCliente(pedidoBuscado, user);
                        Utils.pulsaEnter("Aqui tienes el pedido expandido.");
                    }else Utils.pulsaEnter("ID mal introducido, no existe el Pedido.");
                }else Utils.pulsaEnter("No hay más pedidos");
            }while (pedirPorTecladoSN("¿Quieres volver al inicio de la lista? (S/N): ").equalsIgnoreCase("s"));

        }
    }

    //Pinta los datos del cliente
    private static void pintaDatosCliente(Controlador controlador, Cliente user) {
        Utils.limpiarPantalla();
        PintaConsola.pintaDatosCliente(controlador, user);
        Utils.pulsaEnter("\nAqui tienes tus datos personales pulsa enter para volver al menu anterior.");
    }

    //Menu que tras mostrar los datos del Cliente pregunta cual es el que desea modificar
    private static void menuModificaDatosCliente(Controlador controlador, Cliente user) {
        int op = -1;
        Cliente temp = new Cliente(user);
        Boolean correoNuevo = false;
        do{
            op=-1;
            Utils.limpiarPantalla();
            Menu.menuModificarPerfilCliente(temp);
            try{
                op = Integer.parseInt(pedirPorTeclado("Introduce la opción deseada: "));
            }catch (NumberFormatException e){
                System.out.println("Por favor, introduzca un numero.");
            }
            if (op != -1 && (op>0 && op <8)){
                String nuevoDato = pedirPorTeclado("Introduce el nuevo dato: ");
                if(controlador.modificarDatosCliente(temp,nuevoDato,op)){
                    Utils.pulsaEnter("Dato modificado");
                    if (op == 6) correoNuevo = true;
                } else Utils.pulsaEnter("Hubo un error al modificar los datos.");
            } else{
                if (op!=8) Utils.pulsaEnter("Opción no valida.");
            }
        }while (op!=8);
        if(controlador.existeCambios(temp, user)){
            if (pedirPorTeclado("¿Quieres guardar los datos? (S/N): ").equalsIgnoreCase("s")){
                if (controlador.clonarClienteCopia(user, temp)){
                    if (correoNuevo){
                        Email.enviaCorreoVerificacionCambioCorreo(temp.getToken(),temp.getNombre(), temp.getCorreo());
                    }
                    Utils.pulsaEnter("Datos modificados.");
                }else Utils.pulsaEnter("Hubo algún error al modificar los datos.");
            }else Utils.pulsaEnter("No se modificó ningún dato.");
        }else Utils.pulsaEnter("No se modifico ningún dato.");
    }
}