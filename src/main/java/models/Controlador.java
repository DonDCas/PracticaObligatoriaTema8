package models;

import Communications.Email;
import Communications.Telegram;
import DAO.*;
import persistencia.Persistencia;
import utils.Utils;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Controlador implements Serializable {
    public DAOManager dao;
    public DaoUsuariosSQL daoUsuarios;
    private DaoProductosSQL daoProductos;
    private DaoAdminsSQL daoAdmin;
    private DaoClientesSQL daoClientes;
    private DaoTrabajadorSQL daoTrabajador;
    private DaoPedidosSQL daoPedidos;
    HashMap<String,Cliente> pedidoCliente;

    //Constructor
    public Controlador() {
        dao = DAOManager.getSinglentonInstance();
        daoAdmin = new DaoAdminsSQL();
        daoPedidos = new DaoPedidosSQL();
        daoClientes = new DaoClientesSQL();
        daoUsuarios = new DaoUsuariosSQL();
        daoProductos = new DaoProductosSQL();
        daoTrabajador = new DaoTrabajadorSQL();
    }

    //Getters y Setters
    public ArrayList<Cliente> getClientes() {
        return daoUsuarios.readAllClientes(dao);
    }

    public ArrayList<Trabajador> getTrabajadores() {
        return daoUsuarios.readAllTrabajadores(dao);
    }

    public ArrayList<Admin> getAdmins() {
        return daoUsuarios.readAllAdmins(dao);
    }

    public ArrayList<Producto> getCatalogo() {
        return daoProductos.readAll(dao);
    }

    // Otros metodos

    //Metodo que sirve para encontrar el usuario que esta intentando iniciar sesión.
    //Si no lo encuentra devuelve Null
    public Object login(String email, String clave) {return daoUsuarios.readUser(dao,email,clave);}

    //Metodo que comprueba que devuelve añade un producto al carro del cliente
    // en caso de que se encuentre la ID del producto
    //Añadimos un producto al carro y en caso de que el producto ya exista en el devuelve false
    public boolean addProductoCarrito(Cliente cliente, int idProducto, int cantidad) {
        if (cliente.existeProductoCarro(daoClientes, dao, idProducto)) return daoClientes.actualizarProductoCarrito(dao, cliente.getId(), idProducto, cantidad);
        return daoClientes.addProductoCarrito(dao, cliente.getId(), idProducto,cantidad);
    }

    //Busca un producto por la ID y devuelve Null en caso de que no exista
    public Producto buscaProductoById(int id) {
        return daoProductos.readById(dao, id);
    }

    // Metodo que crea un pedido, lo añade a la lista de pedidos del cliente e inicia la asignación a un trabajador
    public boolean confirmaPedidoCliente(Cliente cliente, ArrayList<Producto> carro,
                                         HashMap<Integer, Integer> cantidadProductos) {
        if (cliente == null) return false;
        if (carro.isEmpty()) return false;
        String idPedido = generaIdPedido();
        if (daoPedidos.clienteRealizaPedido(dao, cliente.getId(), idPedido)){
            if(daoPedidos.insertarLineasPedidos(dao, idPedido, carro, cantidadProductos)){
                Pedido pedidoNuevo = daoPedidos.readById(dao, idPedido);
                Persistencia.registraLogNuevoPedido(cliente, pedidoNuevo);
                Persistencia.creaFacturaPDF(cliente, pedidoNuevo, cantidadProductos);
                cliente.vaciaCarro(dao, daoClientes);
                Trabajador trabajadorCandidato = buscaTrabajadorCandidatoParaAsignar();
                if (trabajadorCandidato != null) asignaPedido(idPedido, trabajadorCandidato.getId());
                return true;
            }
        }
        daoPedidos.borrarPedidoById(dao, idPedido);
        return false;
    }

    public Trabajador buscaTrabajadorCandidatoParaAsignar() {
        return  (Trabajador) daoUsuarios.readUserById(dao, daoTrabajador.buscaTrabajadorParaAsignar(dao));
    }

    public Cliente buscaClienteById(String idCliente) {
        return (Cliente) daoUsuarios.readUserById(dao, idCliente);
    }

    //Este devuelve un Arraylist de productos que pertenezan a la marca que indica el cliente
    public ArrayList<Producto> buscaProductosByMarca(String marca) {
        return daoProductos.readByMarcas(dao, marca);
    }

    //Este devuelve un Arraylist de productos que pertenezan a la modelo que indica el cliente
    public ArrayList<Producto> buscaProductosByModelo(String modelo) {
        return daoProductos.readByModelo(dao,modelo);
    }

    //Este metodo devuelve un Arraylist de productos que cuya descripción contenga lo indicado por el cliente
    public ArrayList<Producto> buscaProductosByDescripcion(String descripcion) {
        return daoProductos.readByDescripcion(dao,descripcion);
    }

    //Este metodo devuelve un array list de productos entre un rango de precios indicado por el cliente
    public ArrayList<Producto> buscaProductosByPrecio(float precioMin, float precioMax) {
        return daoProductos.readByRangoPrecios(dao,precioMin,precioMax);
    }

    //Metodo que devuelve un arraylist con todos los productos que incluyan el string que recibe
    //Este String se busca en la descripcion, marca y modelo
    public ArrayList<Producto> buscaProductosByTermino(String termino) {
        ArrayList<Producto> productosByTermino = new ArrayList<>();
        ArrayList<Producto> productosByDescripcion = buscaProductosByDescripcion(termino);
        ArrayList<Producto> productosByMarca = buscaProductosByMarca(termino);
        ArrayList<Producto> productosByModelo = buscaProductosByModelo(termino);
        for (Producto p : productosByDescripcion)
            if (!existeProductoEnLista(productosByTermino, p)) productosByTermino.add(p);
        for (Producto p : productosByMarca)
            if (!existeProductoEnLista(productosByTermino, p)) productosByTermino.add(p);
        for (Producto p : productosByModelo)
            if (!existeProductoEnLista(productosByTermino, p)) productosByTermino.add(p);
        return productosByTermino = new ArrayList<>(productosByTermino);
    }

    public boolean editarProducto(Producto p, int op,String nuevoDato) {
        switch (op){
            case 1:
                p.setMarca(nuevoDato);
                return true;
            case 2:
                p.setModelo(nuevoDato);
                return true;
            case 3:
                p.setDescripcion(nuevoDato);
                return true;
            case 4:
                if (!Utils.validaPrecio(nuevoDato)) return false;
                p.setPrecio(Float.parseFloat(nuevoDato));
                return true;
            case 5:
                if (!Utils.esDigito(nuevoDato)) return false;
                p.setRelevancia(Integer.parseInt(nuevoDato));
                return true;
            default:
                return false;
        }
    }

    //Metodo que devuelve un Arraylist contodos los pedidos realizados
    public ArrayList<Pedido> getTodosPedidos() {
        return daoPedidos.readAllPedidos(dao);
    }

    //Metodo que cuenta todos los pedidos que se han realizado en la app
    public int numPedidosTotales() {
        return getTodosPedidos().size();
    }

    public Pedido buscaPedidoById(String idPedido) {
        return daoPedidos.readById(dao, idPedido);
    }

    public boolean cambiaEstadoPedido(String idPedido, int nuevoEstado) {
        if (daoPedidos.updateEstadoPedido(dao, idPedido, nuevoEstado)){
            Persistencia.registraLogPedidoModificado(idPedido, String.valueOf(nuevoEstado), "Estado");
            Trabajador trabajadorAsignado = buscaTrabajadorAsignadoAPedido(idPedido);
            if (trabajadorAsignado != null){
                String mensaje = "Pedido: "+idPedido+" ha modificado su estado";
                Telegram.modificaPedidoMensajeTelegram(mensaje,String.valueOf(trabajadorAsignado.getIdTelegram()));
            }
            return true;
        }
        return false;
    }

    public boolean nuevoTrabajador(String email, String clave, String nombre, int movil, int idTelegram) {
        if (!Utils.validaCorreo(email)) return false;
        if (!Utils.validaClave(clave)) return false;
        if (!Utils.validaTelefono(movil)) return false;
        String id = generaIdTrabajador();
        //if (!Utils.validarIdTelegram(idTelegram)) return false;
        if (daoUsuarios.insertaTrabajador(dao,id,email,clave,nombre,
                movil, idTelegram)){
            Trabajador nuevoTrabajador = new Trabajador(id, email, clave, nombre, movil, idTelegram, false);
            Email.generarCorreoBienvenida(nuevoTrabajador);
            return true;
        }
        return false;
    }

    public Trabajador buscaTrabajadorAsignadoAPedido(String idPedido) {
        return daoUsuarios.readTrabajadorByIdPedido(dao, idPedido);
    }

    public ArrayList<Pedido> pedidosSinTrabajador() {
        ArrayList<Pedido> pedidosSinTrabajador = new ArrayList<>();
        ArrayList<Pedido> pedidosTotales = daoPedidos.readAllPedidos(dao);
        for (Pedido pedido : pedidosTotales){
            Trabajador trabajadorAsignado = daoUsuarios.readTrabajadorByIdPedido(dao, pedido.getId());
            if (trabajadorAsignado == null) pedidosSinTrabajador.add(pedido);
        }
        return pedidosSinTrabajador;
    }

    public int numPedidosSinTrabajador() {
        return pedidosSinTrabajador().size();
    }

    public boolean asignaPedido(String idPedido, String idTrabajador) {
        Pedido pedido = daoPedidos.readById(dao, idPedido);
        Cliente cliente = daoUsuarios.buscaClienteByIdPedido(dao, idPedido);
        if (pedido == null) return false;
        Trabajador trabajador = (Trabajador) daoUsuarios.readUserById(dao, idTrabajador);
        if (trabajador == null) return false;
        if (daoTrabajador.asignarPedido(dao, idTrabajador, idPedido)){
            Persistencia.registraLogAsignaPedido(trabajador,pedido);
            Telegram.asignaPedidoMensajeTelegram(pedido, trabajador);
            Email.generarCorreoAsignacionPedido(trabajador, pedido,cliente);
            return true;
        }
        return false;
    }

    public ArrayList<PedidoClienteDataClass> getPedidosAsignadosTrabajador(ArrayList<Pedido> pedidosAsignadosTrabajador){
        ArrayList<PedidoClienteDataClass> resultado = new ArrayList<>();
        for(Pedido p : pedidosAsignadosTrabajador) {
            if (p.getEstado()<3) resultado.add(new PedidoClienteDataClass(p.getId(), daoUsuarios.buscaClienteByIdPedido(dao, p.getId())));
        }
        return resultado;
    }

    //Metodo que devuelve un trabajador buscado mediante su id
    public Trabajador buscaTrabajadorByID(String idTrabajador) {
        return daoUsuarios.readTrabajadorById(dao, idTrabajador);
    }

    public ArrayList<PedidoClienteDataClass> getPedidosCompletadosTrabajador(String idTrabajador){
        Trabajador trabajador = buscaTrabajadorByID(idTrabajador);
        if (trabajador == null) return null;
        ArrayList<PedidoClienteDataClass> resultado = new ArrayList<>();
        for(Pedido p : daoPedidos.readByidTrabajadorAsignado(dao, idTrabajador)) {
            if (p.getEstado()>2) resultado.add(new PedidoClienteDataClass(p.getId(), daoUsuarios.buscaClienteByIdPedido(dao, p.getId())));
        }
        return resultado;
    }

    public ArrayList<PedidoClienteDataClass> getPedidosAsignadosYCompletados(ArrayList<Pedido> pedidosTrabajador){
        ArrayList<PedidoClienteDataClass> resultado = new ArrayList<>();
        for(Pedido p : pedidosTrabajador) {
            resultado.add(new PedidoClienteDataClass(p.getId(), daoUsuarios.buscaClienteByIdPedido(dao, p.getId())));
        }
        return resultado;
    }

    // Genera un id Aleatorio para los clientes y comprueba que no se haya usado antes.
    public String generaIdCliente() {
        boolean existeID = false;
        int id = -1;
        String idCliente = "";
        do {
            existeID = false;
            //El numero aleatorio es entre 1 y 100.000 + la cantidad de usuarios ya registrados.
            id = Utils.numAleatorio100(1, 100000 + daoUsuarios.readAllClientes(dao).size());
            idCliente = "C"+id;
            existeID = daoClientes.compruebaIdCliente(dao,idCliente);
        }while (existeID);
        return idCliente;
    }

    //Genera un id para los productos
    public int generaIdProducto() {
        int id = -1;
        boolean existeID = false;
        do{
            existeID = false;
            ArrayList<Producto> catalogo = daoProductos.readAll(dao);
            //Generamos un numero aleatorio entre 1 y 100000 + la cantidad de productos registrados.
            id = Utils.numAleatorio100(1,100000+catalogo.size());
            // comprobamos que no exista la id. En caso de que exista el metodo devolvera -1
            for (Producto p : catalogo) if(id == p.getId()) existeID = true;
        }while (existeID);
        return id;
    }

    //Genera un id para los pedidos
    public String generaIdPedido() {
        boolean existeID = false;
        String id = "";
        Pedido pedidoBuscado = null;
        do{
            existeID = false;
            //Generamos un numero de ID de forma aleatoria entre 1 y 100000 + la cantidad de pedidos totales
            id = "P" + Utils.numAleatorio100(1,100000+numPedidosTotales());
            //Comprobamos que la ID no existe buscandola entre todos los clientes y todos los pedidos de cada cliente
            pedidoBuscado = daoPedidos.readById(dao, id);
            if (pedidoBuscado != null) existeID = true;
            else return id;
        }while (existeID);
        return id;
    }

    public int generaIdAdmin() {
        return -1;
    }

    public String generaIdTrabajador() {
        String id;
        ArrayList<Trabajador> trabajadores = daoUsuarios.readAllTrabajadores(dao);
        boolean existeID;
        do{
            existeID = false;
            id = "T" + String.valueOf(Utils.numAleatorio100(1,(10000)+trabajadores.size()));
            for(Trabajador t : trabajadores) if (t.getId().equalsIgnoreCase(id)) existeID = true;
        }while (existeID);
        return id;
    }

    //METODOS AÑADIDOS A PARTE DEL UML

    // Comprueba que exista un producto en la lsita de terminos comprobando su ID con todos
    private boolean existeProductoEnLista(ArrayList<Producto> productosByTermino, Producto p) {
        for(Producto producto :productosByTermino) if (p.getId() == producto.getId()) return true;
        return false;
    }

    // Este metodo devuelve un ArrayLists de todas las marcas que hay disponibles para elegir.
    public ArrayList<String> devuelveListaMarcas() {
        ArrayList<String> marcas = new ArrayList<>();
        for (Producto p : daoProductos.readAll(dao)) if (!marcas.contains(p.getMarca())) marcas.add(p.getMarca());
        Collections.sort(marcas);
        return marcas;
    }


    // Metodo que comprueba si existe un correo entre los usuarios.
    public boolean existeCorreo(String correo) {
        return daoUsuarios.compruebaEmail(dao, correo);
    }

    // Metodo que genera un token, un id
    // para el cliente y luego antes de crear el usuario le envia un correo con el token
    public boolean nuevoCliente(String correo, String clave, String nombre,
                                String direccion, String localidad, String provincia, int movil) {
        String token = Utils.generarToken();
        String id = generaIdCliente();
        return daoUsuarios.insertaCliente(dao,id,correo,clave,nombre,movil ,direccion,localidad,provincia, token);
    }

    //Metodo que comprueba si el cliente esta validado
    public boolean compruebaClienteValidado(Cliente user) {
        return daoClientes.compruebaCorreoCliente(dao, user.getId());
    }

    //Metodo que valida un cliente si el token que da el usuario es correcto
    public boolean validarCliente(Cliente user, String token) {
        if (user.getToken().equals(token)){
            user.setCorreoValidado(true);
            daoClientes.updateCorreoValido(dao,user.isCorreoValidado(),user);
        }
        return user.isCorreoValidado();
    }

    //Metodo que devuelve la cantidad de pedidos en que no estan ni cancelados ni completados de un cliente.
    public int cuentaPedidosPendientesCliente(Cliente cliente) {
        return cliente.cuentaPedidosPendientes(dao, daoPedidos);
    }

    //Metodo que dependiendo del numero de op cambiara un dato de Cliente
    public boolean modificarDatosCliente(Cliente temp, String nuevoDato, int op) {
        switch (op){
            case 1:
                temp.setNombre(nuevoDato);
                return true;
            case 2:
                temp.setDireccion(nuevoDato);
                return true;
            case 3:
                temp.setLocalidad(nuevoDato);
                return true;
            case 4:
                temp.setProvincia(nuevoDato);
                return true;
            case 5:
                if (Utils.esDigito(nuevoDato) && Utils.validaTelefono((Integer.parseInt(nuevoDato)))){
                    temp.setMovil(Integer.parseInt(nuevoDato));
                    return true;
                }
                return false;
            case 6:
                if (Utils.validaCorreo(nuevoDato)) {
                    if (!existeCorreo(nuevoDato)) {
                        String token = Utils.generarToken();
                        temp.setToken(token);
                        temp.setCorreoValidado(false);
                        temp.setCorreo(nuevoDato);
                        return true;
                    }
                }
                return false;
            case 7:
                if (Utils.validaClave(nuevoDato)){
                    temp.setPass(nuevoDato);
                    return true;
                }
        }
        return false;
   }

   //Metodo que clona los datos de un cliente copia en uno ya almacenado
    public boolean clonarClienteCopia(Cliente cliente, Cliente clienteCopia) {
        cliente.setId(clienteCopia.getId());
        cliente.setNombre(clienteCopia.getNombre());
        cliente.setPass(clienteCopia.getPass());
        cliente.setCorreo(clienteCopia.getCorreo());
        cliente.setMovil(clienteCopia.getMovil());
        cliente.setDireccion(clienteCopia.getDireccion());
        cliente.setLocalidad(clienteCopia.getLocalidad());
        cliente.setProvincia(clienteCopia.getProvincia());
        cliente.setToken(clienteCopia.getToken());
        cliente.setCorreoValidado(clienteCopia.isCorreoValidado());
        cliente.setPedidos(clienteCopia.getPedidos());
        return daoUsuarios.updateUsuario(dao, clienteCopia);
    }

    //Metodo que borra el carrito entero del cliente
    public boolean borrarCarritoCliente(Cliente cliente) {
        return cliente.vaciaCarro(dao, daoClientes);
    }

    //Metodo que devuelve si se ha podido borrar o no un producto del carrito de un cliente
    public boolean borrarProductoCarrito(Cliente cliente, Producto productoBuscado,
                                         int cantidadAEliminar, int cantidadActual) {
        return cliente.quitaProductoCarro(dao,daoClientes,productoBuscado.getId(), cantidadAEliminar,cantidadActual);

    }


    //Metodo que dependiendo del op que modifica un atributo del trabajador
    public boolean modificarDatosTrabajador(Trabajador temp, String nuevoDato, int op) {
            switch (op) {
                case 1:
                    temp.setNombre(nuevoDato);
                    return true;
                case 2:
                    if (Utils.validaCorreo(nuevoDato)) {
                        if (!existeCorreo(nuevoDato)) {
                                temp.setCorreo(nuevoDato.toLowerCase());
                                return true;
                        }
                    }
                    return false;
                case 3:
                    if (Utils.validaClave(nuevoDato)) {
                        temp.setPass(nuevoDato);
                        return true;
                    }return false;
                case 4:
                    if (Utils.esDigito(nuevoDato) && Utils.validaTelefono((Integer.parseInt(nuevoDato)))) {
                        temp.setMovil(Integer.parseInt(nuevoDato));
                        return true;
                    } return false;
            }
            return false;
    }

    //Metodo que guarda los cambios de un trabajador copia en el original
    public boolean clonarTrabajadorCopia(Trabajador trabajador, Trabajador trabajadorTemp) {
        if (daoUsuarios.updateUsuario(dao, trabajadorTemp)){
            trabajador.setNombre(trabajadorTemp.getNombre());
            trabajador.setCorreo(trabajadorTemp.getCorreo());
            trabajador.setPass(trabajadorTemp.getPass());
            trabajador.setMovil(trabajadorTemp.getMovil());
            trabajador.setIdTelegram(trabajadorTemp.getIdTelegram());
            trabajador.setBaja(trabajadorTemp.isBaja());
            return true;
        }
        return false;
    }

    public boolean ModificaProduto(Producto productoTemp) {
        Producto original = buscaProductoById(productoTemp.getId());
        return daoProductos.updateProducto(dao, original.getId(), productoTemp);
    }

    public boolean addComentario(String idPedido, String comentario) {
        if (daoPedidos.updateComentarioPedido(dao, idPedido, comentario)){
            Persistencia.registraLogPedidoModificado(idPedido, comentario, "AddComentario");
            Trabajador trabajadorAsignado = buscaTrabajadorAsignadoAPedido(idPedido);
            if (trabajadorAsignado != null){
                String mensaje = "Se ha añadido un nuevo comentario al pedido "+idPedido;
                Telegram.modificaPedidoMensajeTelegram(mensaje,String.valueOf(trabajadorAsignado.getIdTelegram()));
            }
            return true;
        }
        return false;
    }

    public ArrayList<PedidoClienteDataClass> getTodosPedidosConCliente() {ArrayList<PedidoClienteDataClass> resultado = new ArrayList<>();
        for(Pedido p : getTodosPedidos()) {
            resultado.add(new PedidoClienteDataClass(p.getId(), daoUsuarios.buscaClienteByIdPedido(dao, p.getId())));
        }
        return resultado;
    }

    public PedidoClienteDataClass getPedidoClienteUnico(String idPedido) {
        return new PedidoClienteDataClass(idPedido, daoUsuarios.buscaClienteByIdPedido(dao, idPedido));
    }

    //Metodo que devuelve la cantidad de pedidos en estado de enviado o cancelado
    public int numPedidosCompletadosYCancelados() {
        int cont = 0;
        for(Pedido p : getTodosPedidos()) if (p.getEstado()>1) cont ++;
        return cont;
    }

    public ArrayList<Trabajador> getTrabajadoresDeAlta(){
        ArrayList<Trabajador> resultado = new ArrayList<>();
        for(Trabajador t : daoUsuarios.readAllTrabajadores(dao)) if (!t.isBaja()) resultado.add(new Trabajador(t));
        return resultado;
    }

    //Da de baja a un trabajador dejando sus datos guardados por si volvieramos a darlo de alta.
    public boolean darBajaTrabajador(Trabajador trabajadorElegido) {
        return daoTrabajador.updateBaja(dao, trabajadorElegido.getId(),true);
    }

    //Metodo que quita los pedidos pendientes que tenga un trabajador
    public boolean quitarPedidosAsignados(Trabajador trabajador, ArrayList<Pedido> pedidosAsignados) {
        if (daoPedidos.quitarPedidosTrabajador(dao, trabajador.getId(), pedidosAsignados)) return true;
        return false;
    }

    //Metodo para buscar un trabajador mediante su correo
    public Trabajador buscaTrabajadorByEmail(String correo) {
        return daoUsuarios.readTrabajadorByCorreo(dao, correo);
    }

    public boolean darAltaTrabajador(Trabajador trabajadorDeBaja) {
        return daoTrabajador.updateBaja(dao, trabajadorDeBaja.getId(),false);
    }


    public boolean existeCambios(Object objetoModificado, Object user) {
        if ((objetoModificado instanceof Cliente) && (user instanceof Cliente) ){
            Cliente cliente = (Cliente) user;
            Cliente temp = (Cliente) objetoModificado;
            if (!cliente.getNombre().equals(temp.getNombre())) return true;
            if (!cliente.getCorreo().equals(temp.getCorreo())) return true;
            if (!cliente.getPass().equals(temp.getPass())) return true;
            if (!cliente.getDireccion().equals(temp.getDireccion())) return true;
            if (!cliente.getLocalidad().equals(temp.getLocalidad())) return true;
            if (!cliente.getProvincia().equals(temp.getProvincia())) return true;
            if (!(cliente.getMovil() == temp.getMovil())) return true;
        }
        return false;
    }

    // Metodo para comprobar si existe un telefono movil ya en registro.
    public int existemovil(int movil) {
        if (!Utils.validaTelefono(movil)) return -1;
        if (daoUsuarios.compruebaMovil(dao,movil)) return -2;
        return 1;
    }

    public boolean cambiaFechaPedido(String idPedido, LocalDate nuevaFecha) {
        if (daoPedidos.updateFechaEntrega(dao, idPedido, nuevaFecha)){
            Persistencia.registraLogPedidoModificado(idPedido, String.valueOf(nuevaFecha), "Cambio Fecha");
            Trabajador trabajadorAsignado = buscaTrabajadorAsignadoAPedido(idPedido);
            if (trabajadorAsignado != null){
                String mensaje = "Pedido: "+idPedido+" ha sido actualizado la Fecha de entrega a "+ Utils.fechaAString(nuevaFecha);
                Telegram.modificaPedidoMensajeTelegram(mensaje,String.valueOf(trabajadorAsignado.getIdTelegram()));
            }
            return true;
        }
        return false;
    }

    public static boolean registraInicioSesion(Object user) {
        return Persistencia.registraLogInicioSesion(user);
    }

    public static boolean registraCierreSesion(Object user) {
        return Persistencia.registraLogCierreSesion(user);
    }


    public void cambiaEstadoPedidoCancelado(String idPedido) {
        if (daoPedidos.updateFechaEntrega(dao, idPedido, null)){
            Persistencia.registraLogPedidoModificado(idPedido, "Cancelacion Pedido", "Cancelacion de Pedido");
            Trabajador trabajadorAsignado = buscaTrabajadorAsignadoAPedido(idPedido);
            if (trabajadorAsignado != null){
                String mensaje = "Pedido: "+idPedido+" ha sido actualizado a CANCELADO";
                Telegram.modificaPedidoMensajeTelegram(mensaje,String.valueOf(trabajadorAsignado.getIdTelegram()));
            }
        }
    }

    public String recuperarInicioSesion(Object user) {
        String ultimoInicio = Persistencia.recuperaInicioSesionUsuario(user);
        if (ultimoInicio.isEmpty()) return "Es tu primer inicio de sesión.";
        ultimoInicio = Utils.fechaAString(LocalDate.parse(ultimoInicio));
        return "Iniciaste sesión por ultima vez: "+ultimoInicio;
    }

    public void guardarNuevoInicioSesion(Object user) {
        Persistencia.guardarInicioSesion(user);
    }

    public boolean modoInvitado() {
        return Persistencia.compruebaInivitadoActivado();
    }

    public Object ultimoInicioSesionParaAdmin(Object user) {
        String ultimoInicio = Persistencia.recuperaInicioSesionUsuario(user);
        if (ultimoInicio.isEmpty()) return "Este usuario aun no ha iniciado sesión";
        ultimoInicio = Utils.fechaAString(LocalDate.parse(ultimoInicio));
        return "inició sesión por última vez- "+ultimoInicio;
    }

    public ArrayList<Pedido> getPedidosCompletados() {
        ArrayList<Pedido> pedidosCompletados = new ArrayList<>();
        for (Cliente cli : daoUsuarios.readAllClientes(dao)){
            for(Pedido pedidoCliente : cli.getPedidos()){
                if(pedidoCliente.getEstado() > 1) pedidosCompletados.add(pedidoCliente);
            }
        }
        return pedidosCompletados;
    }

    public ArrayList<Pedido> getPedidosSinCompletar() {
        ArrayList<Pedido> pedidosNoCompletados = new ArrayList<>();
        for (Pedido p : daoPedidos.readAllPedidos(dao)){
                if(p.getEstado() < 2) pedidosNoCompletados.add(p);
        }
        return pedidosNoCompletados;
    }

    public void exportarAExcelPedidos(ArrayList<Pedido> pedidos, String nombreArchivo, String email) {
        ArrayList<PedidoClienteDataClass> datosCliente = getTodosPedidosConCliente();
        ArrayList<Trabajador> trabajadores = daoUsuarios.readAllTrabajadores(dao);
        Persistencia.exportarAExcelPedidos(pedidos, datosCliente, trabajadores, nombreArchivo, email, dao, daoPedidos);
    }

    public String recuperaRuta(String rutaSolicitada) {
        return Persistencia.recuperaRuta(rutaSolicitada);
    }

    public void modificaModoInvitado(String nuevoModoInvitado) {
        Persistencia.modificaModoInvitado(nuevoModoInvitado);
    }

    public boolean existeProductoCarroCliente(Cliente cliente, int id) {
        return cliente.existeProductoCarro(daoClientes, dao, id);
    }

    public ArrayList<Producto> actualizarCarritoCliente(Cliente user) {
        return daoProductos.recuperaProductosCarrito(dao, user.getId());
    }

    public Integer devuelveCantidadProductoCarrito(Cliente user, int id) {
        return daoClientes.devuelveCantidadProductoCarrito(dao, user.getId(), id);
    }

    public ArrayList<Pedido> buscaPedidoByCliente(Cliente user) {
        return daoPedidos.readByidCliente(dao, user.getId());
    }

    public ArrayList<Pedido> recuperaPedidosPendientesTrabajador(Trabajador trabajador) {
        return daoPedidos.readByidTrabajadorAsignadoSinCompletar(dao, trabajador.getId());

    }

    public ArrayList<Pedido> recuperaPedidosAsignadosTrabajador(Trabajador trabajador) {
        return daoPedidos.readByidTrabajadorAsignado(dao, trabajador.getId());
    }

    public boolean exportaCopiaDeSegridad(String ruta) {
        return Persistencia.exportaCopiaDeSeguridad(this, ruta);
    }

    public Controlador importarCopiaDeSeguridad(String rutaArchivo) {
        return Persistencia.importaCopiaDeSeguridad(rutaArchivo);
    }
}