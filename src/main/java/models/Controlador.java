package models;

import Communications.Email;
import Communications.Telegram;
import DAO.DAOManager;
import DAO.DaoProductosSQL;
import data.DataAdmin;
import data.DataClientes;
import data.DataProductos;
import data.DataTrabajadores;
import persistencia.Persistencia;
import utils.Utils;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;

public class Controlador implements Serializable {
    private DAOManager dao;
    private DaoProductosSQL daoProductos;
    ArrayList<Cliente> clientes;
    ArrayList<Trabajador> trabajadores;
    ArrayList<Admin> admins;
    HashMap<String,Cliente> pedidoCliente;

    //Constructor
    public Controlador() {
        dao = DAOManager.getSinglentonInstance();
        daoProductos = new DaoProductosSQL();
        this.clientes = new ArrayList<>();
        this.trabajadores = new ArrayList<>();
        this.admins = new ArrayList<>();
        pedidoCliente = new HashMap<>();
    }

    //Metodos relacionados a la Persistencia de datos
    public void cargarDatos() {
        Persistencia.existeCarpetaContenedora();
        if(Persistencia.existenDatosMapa()){
            pedidoCliente = Persistencia.cargaDatosMapa();
        }
        if(Persistencia.existenDatosAdmin()){
            admins = Persistencia.cargaDatosAdmin();
        }else{
            mockAdmin();
            //Cargar mock admins
        }
        if(Persistencia.existenDatosTrabajador()){
            trabajadores = Persistencia.cargaDatosTrabajador();
        }else{
            mockTrabajadores();
            //Cargar mock trabajadores
        }
        if(Persistencia.existenDatosCliente()){
            clientes = Persistencia.cargaDatosCliente();
        }else{
            mockCliente();
            mockPedidos();
            //Cargar mock Clientes y pedidos
        }

    }

    private void mockTrabajadores() {
        trabajadores.addAll(DataTrabajadores.getTrabajadoresMock());
        Persistencia.guardaDatosTrabajadores(this);
    }

    //Generador de datos clientes , trabajadores y admins
    public void mockCliente() {
        clientes.addAll(DataClientes.getClientesMock());
        Persistencia.guardaDatosClientes(this);
    }

    public void mockAdmin(){
        admins.addAll(DataAdmin.getAdminMock());
        Persistencia.guardaAdmins(this);
    }

    public void mockPedidos() {
        clientes.get(0).addProductoCarro(buscaProductoById(14566));
        clientes.get(0).addProductoCarro(buscaProductoById(84635));
        confirmaPedidoCliente(clientes.get(0).getId());
        clientes.get(0).addProductoCarro(buscaProductoById(93639));
        clientes.get(0).addProductoCarro(buscaProductoById(83263));
        confirmaPedidoCliente(clientes.get(0).getId());
        /*clientes.get(0).addProductoCarro(buscaProductoById(67233));
        clientes.get(0).addProductoCarro(buscaProductoById(456333));
        confirmaPedidoCliente(clientes.get(0).getId());
        clientes.get(0).addProductoCarro(buscaProductoById(93639));
        clientes.get(0).addProductoCarro(buscaProductoById(83263));
        confirmaPedidoCliente(clientes.get(0).getId());
        clientes.get(0).addProductoCarro(buscaProductoById(93639));
        clientes.get(0).addProductoCarro(buscaProductoById(83263));
        confirmaPedidoCliente(clientes.get(0).getId());
        clientes.get(0).addProductoCarro(buscaProductoById(93639));
        clientes.get(0).addProductoCarro(buscaProductoById(83263));
        confirmaPedidoCliente(clientes.get(0).getId());
        clientes.get(0).addProductoCarro(buscaProductoById(93639));
        clientes.get(0).addProductoCarro(buscaProductoById(83263));
        confirmaPedidoCliente(clientes.get(0).getId());
        clientes.get(0).addProductoCarro(buscaProductoById(84635));
        clientes.get(0).addProductoCarro(buscaProductoById(83263));*/
        asignaPedido(clientes.get(0).getPedidos().get(0).getId(),trabajadores.get(0).getId());
        asignaPedido(clientes.get(0).getPedidos().get(1).getId(),trabajadores.get(0).getId());
        clientes.get(0).getPedidos().get(1).setFechaPedido(LocalDate.parse("2023-01-01"));
        //Segundo pedido del cliente a fecha mucho más vieja para comprobar que se ordenan bien por fechas
        //clientes.get(0).getPedidos().get(3).setFechaPedido(LocalDate.parse("2024-02-02"));
        //clientes.get(0).getPedidos().get(2).setFechaPedido(LocalDate.parse("2025-04-04"));
    }

    //Getters y Setters
    public ArrayList<Cliente> getClientes() {
        return clientes;
    }

    public void setClientes(ArrayList<Cliente> clientes) {
        this.clientes = clientes;
    }

    public ArrayList<Trabajador> getTrabajadores() {
        return trabajadores;
    }

    public void setTrabajadores(ArrayList<Trabajador> trabajadores) {
        this.trabajadores = trabajadores;
    }

    public ArrayList<Admin> getAdmins() {
        return admins;
    }

    public void setAdmins(ArrayList<Admin> admins) {
        this.admins = admins;
    }

    public ArrayList<Producto> getCatalogo() {
        return daoProductos.readAll(dao);
    }

    // Otros metodos

    //Metodo que sirve para encontrar el usuario que esta intentando iniciar sesión.
    //Si no lo encuentra devuelve Null
    public Object login(String email, String clave) {
        for(Admin admin :admins)
            if (admin.login(email,clave))
                return admin;
        for(Trabajador trab : trabajadores)
            if (trab.login(email, clave))
                return trab;
        for(Cliente clie :clientes)
            if (clie.login(email,clave))
                return clie;
        return null;
    }

    //Metodo que comprueba que devuelve añade un producto al carro del cliente
    // en caso de que se encuentre la ID del producto
    public boolean addProductoCarrito(Cliente cliente, int idProducto) {
        Producto p = buscaProductoById(idProducto);
        if (cliente == null) return false;
        if (p == null) return false;
        if(cliente.addProductoCarro(p)){
            Persistencia.guardaDatosCliente(cliente);
            return true;
        }
        return false;
    }

    //Busca un producto por la ID y devuelve Null en caso de que no exista
    public Producto buscaProductoById(int id) {
        return daoProductos.readById(dao, id);
    }

    // Metodo que crea un pedido, lo añade a la lista de pedidos del cliente e inicia la asignación a un trabajador
    public boolean confirmaPedidoCliente(String idCliente) {
        Cliente cliente = buscaClienteById(idCliente);
        if (cliente == null) return false;
        if (cliente.getCarro().isEmpty()) return false;
        ArrayList<Producto> productos = new ArrayList<>();
        for (Producto producto : cliente.getCarro()){
            productos.add(new Producto(producto));
        }
        Pedido nuevoPedido = new Pedido(generaIdPedido(), productos);
        cliente.addPedido(nuevoPedido);
        Persistencia.guardaDatosCliente(cliente);
        Persistencia.creaFacturaPDF(cliente,nuevoPedido);
        pedidoCliente.put(nuevoPedido.getId(),cliente);
        Persistencia.guardaDatosMapas(pedidoCliente);
        Persistencia.registraLogNuevoPedido(cliente, nuevoPedido);
        Trabajador trabajadorCandidato = buscaTrabajadorCandidatoParaAsignar();
        if (trabajadorCandidato != null) return asignaPedido(nuevoPedido.getId(), trabajadorCandidato.getId());
        return true;
    }

    public Trabajador buscaTrabajadorCandidatoParaAsignar() {
        Trabajador trabajadorCandidato = null;
        for(Trabajador trabajador : trabajadores){
            if (!trabajador.isBaja()){
                if (trabajadorCandidato == null) trabajadorCandidato = trabajador;
                else{
                    if (trabajadorCandidato.numPedidosPendientes() > trabajador.numPedidosPendientes())
                        trabajadorCandidato = trabajador;
                }
            }
        }
        if (hayEmpateTrabajadoresCandidatos(trabajadorCandidato)) return null;
        return trabajadorCandidato;
    }

    public boolean hayEmpateTrabajadoresCandidatos(Trabajador candidato) {
        for(Trabajador trabajador : trabajadores){
            if (!trabajador.isBaja())
                if (trabajador != candidato && trabajador.numPedidosPendientes() == candidato.numPedidosPendientes())
                    return true;
        }
        return false;
    }

    public Cliente buscaClienteById(String idCliente) {
        for (Cliente c : clientes) if (c.getId().equals(idCliente)) return c;
        return null;
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
        ArrayList<Pedido> pedidosTotales = new ArrayList<>();
        for(Cliente c : clientes){
            for (Pedido pedidoCliente : c.getPedidos()){
                if (!pedidosTotales.contains(pedidoCliente)) pedidosTotales.add(pedidoCliente);
            }
        }
        return pedidosTotales;
    }

    //Metodo que cuenta todos los pedidos que se han realizado en la app
    public int numPedidosTotales() {
        return getTodosPedidos().size();
    }

    public Pedido buscaPedidoById(String idPedido) {
        for (Cliente c : clientes)for(Pedido p : c.getPedidos()) if (p.getId().equalsIgnoreCase(idPedido)) return p;
        return null;
    }

    public boolean cambiaEstadoPedido(String idPedido, int nuevoEstado) {
        Cliente cliente = pedidoCliente.get(idPedido);
        Trabajador trabajador = buscaTrabajadorAsignadoAPedido(idPedido);
        for (Pedido pedidoBuscado : cliente.getPedidos()){
            if (pedidoBuscado.getId().equals(idPedido)){
                if (pedidoBuscado.cambiaEstado(nuevoEstado)){
                    if (trabajador != null){
                        for (Pedido pedidoAsignado : trabajador.getPedidosAsignados()){
                            if (pedidoAsignado.getId().equalsIgnoreCase(idPedido)) pedidoAsignado.cambiaEstado(nuevoEstado);
                            Persistencia.guardaDatosTrabajador(trabajador);
                        }
                    }
                    Persistencia.guardaDatosCliente(cliente);
                    Persistencia.registraLogPedidoModificado(buscaPedidoById(idPedido), String.valueOf(nuevoEstado), "Estado");
                    return true;
                }
            }
        }
        return false;
    }

    public boolean nuevoTrabajador(String email, String clave, String nombre, int movil, int idTelegram) {
        if (!Utils.validaCorreo(email)) return false;
        if (!Utils.validaClave(clave)) return false;
        if (!Utils.validaTelefono(movil)) return false;
        //if (!Utils.validarIdTelegram(idTelegram)) return false;
        Trabajador nuevoTrabajador = new Trabajador(generaIdTrabajador(), email, clave, nombre, movil, idTelegram);
        trabajadores.add(nuevoTrabajador);
        Email.generarCorreoBienvenida(nuevoTrabajador);
        Persistencia.guardaDatosTrabajador(nuevoTrabajador);
        return true;
    }

    public Trabajador buscaTrabajadorAsignadoAPedido(String idPedido) {
        for(Trabajador t : trabajadores)
            for (Pedido p : t.getPedidosAsignados())
                if (p.getId().equals(idPedido)) return t;
        return null;
    }

    public ArrayList<Pedido> pedidosSinTrabajador() {
        ArrayList<Pedido> pedidosSinTrabajador = new ArrayList<>();
        ArrayList<Pedido> pedidosDeClientes = new ArrayList<>();
        boolean conTrabajador = false;
        for(Cliente cliente : clientes) pedidosDeClientes.addAll(cliente.getPedidos());

        for (Pedido pedidoDelTotal : pedidosDeClientes){
            conTrabajador = false;
            for(Trabajador trabajador : trabajadores){
                for(Pedido pedidoDeTrabajador : trabajador.getPedidosAsignados()){
                    if (pedidoDeTrabajador.getId().equalsIgnoreCase(pedidoDelTotal.getId())) conTrabajador = true;
                }
            }
            if (!conTrabajador) pedidosSinTrabajador.add(pedidoDelTotal);
        }
        return pedidosSinTrabajador;
    }

    public int numPedidosSinTrabajador() {
        return pedidosSinTrabajador().size();
    }

    public boolean asignaPedido(String idPedido, String idTrabajador) {
        Pedido pedido = buscaPedidoById(idPedido);
        Cliente cliente = pedidoCliente.get(idPedido);
        if (pedido == null) return false;
        Trabajador trabajador = buscaTrabajadorByID(idTrabajador);
        if (trabajador == null) return false;
        if (trabajador.asignaPedido(pedido)){
            Persistencia.guardaDatosTrabajador(trabajador);
            Persistencia.guardaDatosCliente(cliente);
            Persistencia.registraLogAsignaPedido(trabajador, pedido);
            Telegram.asignaPedidoMensajeTelegram(pedido, trabajador);
            Email.generarCorreoAsignacionPedido(trabajador, pedido,cliente);
            return true;
        }
        return false;
    }

    public ArrayList<PedidoClienteDataClass> getPedidosAsignadosTrabajador(String idTrabajador){
        Trabajador trabajador = buscaTrabajadorByID(idTrabajador);
        if (trabajador == null) return null;
        ArrayList<PedidoClienteDataClass> resultado = new ArrayList<>();
        for(Pedido p : trabajador.getPedidosAsignados()) {
            if (p.getEstado()<3) resultado.add(new PedidoClienteDataClass(p.getId(), pedidoCliente.get(p.getId())));
        }
        return resultado;
    }

    //Metodo que devuelve un trabajador buscado mediante su id
    public Trabajador buscaTrabajadorByID(String idTrabajador) {
        for (Trabajador t : trabajadores) if(t.getId().equals(idTrabajador)) return t;
        return null;
    }

    //Metodo que devuelve un pedido supuestamente asignado a un trabajador
    public Pedido buscaPedidoAsignadoTrabajador(String idTrabajador, String idPedido) {
        Trabajador trabajador = buscaTrabajadorByID(idTrabajador);
        if (trabajador != null)
            for (Pedido p : trabajador.getPedidosAsignados()) if (p.getId().equals(idPedido)) return p;
        return null;
    }

    public ArrayList<PedidoClienteDataClass> getPedidosCompletadosTrabajador(String idTrabajador){
        Trabajador trabajador = buscaTrabajadorByID(idTrabajador);
        if (trabajador == null) return null;
        ArrayList<PedidoClienteDataClass> resultado = new ArrayList<>();
        for(Pedido p : trabajador.getPedidosAsignados()) {
            if (p.getEstado()>2) resultado.add(new PedidoClienteDataClass(p.getId(), pedidoCliente.get(p.getId())));
        }
        return resultado;
    }

    public ArrayList<PedidoClienteDataClass> getPedidosAsignadosYCompletados(String idTrabajador){
        Trabajador trabajador = buscaTrabajadorByID(idTrabajador);
        if (trabajador == null) return null;
        ArrayList<PedidoClienteDataClass> resultado = new ArrayList<>();
        for(Pedido p : trabajador.getPedidosAsignados()) {
            resultado.add(new PedidoClienteDataClass(p.getId(), pedidoCliente.get(p.getId())));
        }
        return resultado;
    }

    // Genera un id Aleatorio para los clientes y comprueba que no se haya usado antes.
    public String generaIdCliente() {
        boolean existeID = false;
        int id = -1;
        do {
            existeID = false;
            //El numero aleatorio es entre 1 y 100.000 + la cantidad de usuarios ya registrados.
            id = Utils.numAleatorio100(1, 100000 + clientes.size());
            for (Cliente c : clientes) {
                int idRegistrada = Integer.parseInt(c.getId().substring(1));
                if (idRegistrada == id) existeID = true;
            }
        }while (existeID);
        return "C".concat(String.valueOf(id));
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
        int id = -1;
        do{
            existeID = false;
            //Generamos un numero de ID de forma aleatoria entre 1 y 100000 + la cantidad de pedidos totales
            id = Utils.numAleatorio100(1,100000+numPedidosTotales());
            //Comprobamos que la ID no existe buscandola entre todos los clientes y todos los pedidos de cada cliente
            for(Cliente c : clientes){
                for(Pedido p : c.getPedidos()){
                    int idRegistrada = Integer.parseInt(p.getId().substring(1));
                    // Si encontramos coincidencia levantamos bandera y tendremos que volver a empezar el proceso
                    if (idRegistrada == id) existeID = true;
                }
            }
        }while (existeID);
        return "P"+String.valueOf(id);
    }

    public int generaIdAdmin() {
        return -1;
    }

    public String generaIdTrabajador() {
        String id;
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
        for (Cliente c : clientes) if(c.getEmail().toLowerCase().equals(correo.toLowerCase())) return true;
        for (Trabajador t : trabajadores) if(t.getEmail().toLowerCase().equals(correo.toLowerCase())) return true;
        for (Admin a : admins) if(a.getEmail().toLowerCase().equals(correo.toLowerCase())) return true;
        return false;
    }

    // Metodo que genera un token, un id
    // para el cliente y luego antes de crear el usuario le envia un correo con el token
    public boolean nuevoCliente(String correo, String clave, String nombre,
                                String direccion, String localidad, String provincia, int movil) {
        String token = Utils.generarToken();
        if (Email.enviarCorreoVerificacionRegistro(token,nombre, correo)){
            Cliente clienteNuevo = new Cliente (generaIdCliente(), correo,
                    clave, nombre, localidad, provincia, direccion, movil, token);
            if (clienteNuevo != null){
                Persistencia.guardaDatosCliente(clienteNuevo);
                clientes.add(clienteNuevo);
            }

        }
        return false;
    }

    //Metodo que comprueba si el cliente esta validado
    public boolean compruebaClienteValidado(Cliente user) {
        return user.isCorreoValidado();
    }

    //Metodo que valida un cliente si el token que da el usuario es correcto
    public boolean validarCliente(Cliente user, String token) {
        if (user.getToken().equals(token)){
            user.setCorreoValidado(true);
            Persistencia.guardaDatosCliente(user);
        }
        return user.isCorreoValidado();
    }
/*
    //Metodo que comprueba si el trabajador tiene el correo validado
    public boolean compruebaTrabajadorValidado(Trabajador user) { return user.isCorreoValidado();}

    //Metodo para validar al trabajador si el token que da el usuario es correcto
    public boolean validarTrabajador(Trabajador user, String token) {
        if (user.getToken().equals(token)){
            user.setCorreoValidado(true);
        }
        return user.isCorreoValidado();
    }*/

    //Metodo que devuelve la cantidad de pedidos en que no estan ni cancelados ni completados de un cliente.
    public int cuentaPedidosPendientesCliente(Cliente cliente) {
        return cliente.cuentaPedidosPendientes();
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
                        temp.setEmail(nuevoDato);
                        return true;
                    }
                }
                return false;
            case 7:
                if (Utils.validaClave(nuevoDato)){
                    temp.setClave(nuevoDato);
                    return true;
                }
        }
        return false;
   }

   //Metodo que clona los datos de un cliente copia en uno ya almacenado
    public void clonarClienteCopia(Cliente cliente, Cliente clienteCopia) {
        cliente.setId(clienteCopia.getId());
        cliente.setNombre(clienteCopia.getNombre());
        cliente.setClave(clienteCopia.getClave());
        cliente.setEmail(clienteCopia.getEmail());
        cliente.setMovil(clienteCopia.getMovil());
        cliente.setDireccion(clienteCopia.getDireccion());
        cliente.setLocalidad(clienteCopia.getLocalidad());
        cliente.setProvincia(clienteCopia.getProvincia());
        cliente.setToken(clienteCopia.getToken());
        cliente.setCorreoValidado(clienteCopia.isCorreoValidado());
        cliente.setCarro(clienteCopia.getCarro());
        cliente.setPedidos(clienteCopia.getPedidos());
        Persistencia.guardaDatosCliente(cliente);
    }

    //Metodo que borra el carrito entero del cliente
    public boolean borrarCarritoCliente(Cliente cliente) {
        if (!cliente.getCarro().isEmpty()){
            cliente.vaciaCarro();
            return true;
        }
        return false;
    }

    //Metodo que devuelve si se ha podido borrar o no un producto del carrito de un cliente
    public boolean borrarProductoCarrito(Cliente cliente, Producto productoBuscado) {
        if (cliente.quitaProductoCarro(productoBuscado.getId())){
           Persistencia.guardaDatosCliente(cliente);
            return true;
        }
    return false;
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
                                temp.setEmail(nuevoDato.toLowerCase());
                                return true;
                        }
                    }
                    return false;
                case 3:
                    if (Utils.validaClave(nuevoDato)) {
                        temp.setClave(nuevoDato);
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
    public void clonarTrabajadorCopia(Trabajador trabajador, Trabajador trabajadorTemp) {
        trabajador.setNombre(trabajadorTemp.getNombre());
        trabajador.setEmail(trabajadorTemp.getEmail());
        trabajador.setClave(trabajadorTemp.getClave());
        trabajador.setMovil(trabajadorTemp.getMovil());
        trabajador.setIdTelegram(trabajadorTemp.getIdTelegram());
        trabajador.setBaja(trabajadorTemp.isBaja());
        Persistencia.guardaDatosTrabajador(trabajador);
    }

    public void clonarProduto(Producto productoTemp) {
// TODO añadir update de la tabla producto para la base de datos
        Producto original = buscaProductoById(productoTemp.getId());
        original.setMarca(productoTemp.getMarca());
        original.setModelo(productoTemp.getModelo());
        original.setDescripcion(productoTemp.getDescripcion());
        original.setPrecio(productoTemp.getPrecio());
        original.setRelevancia(productoTemp.getRelevancia());
        Persistencia.guardaDatosProducto(original);
    }

    public boolean addComentario(String idPedido, String comentario) {
        Cliente cliente = pedidoCliente.get(idPedido);
        Trabajador trabajador = buscaTrabajadorAsignadoAPedido(idPedido);
        for (Pedido pedidoBuscado : cliente.getPedidos()){
            if (pedidoBuscado.getId().equals(idPedido)){
                if (pedidoBuscado.addComentario(comentario)){
                    if (trabajador != null){
                        //TODO Añadir envio de mensaje en Telegram
                        Persistencia.guardaDatosTrabajador(trabajador);
                    }
                    Persistencia.guardaDatosCliente(cliente);
                    Persistencia.registraLogPedidoModificado(buscaPedidoById(idPedido), comentario, "AddComentario");
                    return true;
                }
            }
        }
        return false;
    }

    public ArrayList<PedidoClienteDataClass> getTodosPedidosConCliente() {ArrayList<PedidoClienteDataClass> resultado = new ArrayList<>();
        for(Pedido p : getTodosPedidos()) {
            resultado.add(new PedidoClienteDataClass(p.getId(), pedidoCliente.get(p.getId())));
        }
        return resultado;
    }

    public PedidoClienteDataClass getPedidoClienteUnico(String idPedido) {
        for (Pedido p: getTodosPedidos()){
            if (p.getId().equals(idPedido)) return new PedidoClienteDataClass(idPedido, pedidoCliente.get(idPedido));
        }
        return null;
    }

    //Metodo que devuelve la cantidad de pedidos en estado de enviado o cancelado
    public int numPedidosCompletadosYCancelados() {
        int cont = 0;
        for(Pedido p : getTodosPedidos()) if (p.getEstado()>1) cont ++;
        return cont;
    }

    public ArrayList<Trabajador> getTrabajadoresDeAlta(){
        ArrayList<Trabajador> resultado = new ArrayList<>();
        for(Trabajador t : trabajadores) if (!t.isBaja()) resultado.add(new Trabajador(t));
        return resultado;
    }

    //Da de baja a un trabajador dejando sus datos guardados por si volvieramos a darlo de alta.
    public void darBajaTrabajador(Trabajador trabajadorElegido) {
        trabajadorElegido.setBaja(true);
        Persistencia.guardaDatosTrabajador(trabajadorElegido);
    }

    //Metodo que quita los pedidos pendientes que tenga un trabajador
    public void quitarPedidosAsignados(Trabajador trabajador) {
        trabajador.quitaPedidosPendientes();
        Persistencia.guardaDatosTrabajador(trabajador);
    }

    //Metodo para buscar un trabajador mediante su correo
    public Trabajador buscaTrabajadorByEmail(String correo) {
        for(Trabajador t : trabajadores) if(t.getEmail().equalsIgnoreCase(correo)) return t;
        return null;
    }

    public void darAltaTrabajador(Trabajador trabajadorDeBaja) {
        trabajadorDeBaja.setBaja(false);
        Persistencia.guardaDatosTrabajador(trabajadorDeBaja);
    }


    public boolean existeCambios(Object objetoModificado, Object user) {
        if ((objetoModificado instanceof Cliente) && (user instanceof Cliente) ){
            Cliente cliente = (Cliente) user;
            Cliente temp = (Cliente) objetoModificado;
            if (!cliente.getNombre().equals(temp.getNombre())) return true;
            if (!cliente.getEmail().equals(temp.getEmail())) return true;
            if (!cliente.getClave().equals(temp.getClave())) return true;
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
        for(Cliente c : clientes){
            if (c.getMovil() == movil) return -2;
        }
        for (Trabajador t : trabajadores)
            if (t.getMovil() == movil) return -2;
        return 1;
    }

    public boolean cambiaFechaPedido(String idPedido, LocalDate nuevaFecha) {
        Cliente cliente = pedidoCliente.get(idPedido);
        Trabajador trabajador = buscaTrabajadorAsignadoAPedido(idPedido);
        for (Pedido pedidoBuscado : cliente.getPedidos()){
            if (pedidoBuscado.getId().equals(idPedido)){
                if (pedidoBuscado.cambiaFechaEntrega(nuevaFecha)){
                    if (trabajador != null){
                        //TODO Añadir envio de mensaje en Telegram
                        for (Pedido pedidoAsignado : trabajador.getPedidosAsignados()){
                            if (pedidoAsignado.getId().equalsIgnoreCase(idPedido)) pedidoAsignado.cambiaFechaEntrega(nuevaFecha);
                            Persistencia.guardaDatosTrabajador(trabajador);
                        }
                    }
                    Persistencia.guardaDatosCliente(cliente);
                    Persistencia.registraLogPedidoModificado(pedidoBuscado, String.valueOf(nuevaFecha), "Cambio Fecha");
                    return true;
                }
            }
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
        Cliente cliente = pedidoCliente.get(idPedido);
        Trabajador trabajador = buscaTrabajadorAsignadoAPedido(idPedido);
        for (Pedido pedidoBuscado : cliente.getPedidos()){
            if (pedidoBuscado.getId().equals(idPedido)){
                if (pedidoBuscado.cambiaFechaEntregaCandelado()){
                    if (trabajador != null){
                        //TODO Añadir envio de mensaje en Telegram
                        for (Pedido pedidoAsignado : trabajador.getPedidosAsignados()){
                            if (pedidoAsignado.getId().equalsIgnoreCase(idPedido)) pedidoAsignado.cambiaFechaEntregaCandelado();
                            Persistencia.guardaDatosTrabajador(trabajador);
                        }
                    }
                    Persistencia.guardaDatosCliente(cliente);
                    Persistencia.registraLogPedidoModificado(pedidoBuscado, "Cancelacion Pedido", "Cancelacion de Pedido");
                }
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
        for (Cliente cli : clientes){
            for(Pedido pedidoCliente : cli.getPedidos()){
                if(pedidoCliente.getEstado() > 1) pedidosCompletados.add(pedidoCliente);
            }
        }
        return pedidosCompletados;
    }

    public ArrayList<Pedido> getPedidosSinCompletar() {
        ArrayList<Pedido> pedidosNoCompletados = new ArrayList<>();
        for (Cliente cli : clientes){
            for(Pedido pedidoCliente : cli.getPedidos()){
                if(pedidoCliente.getEstado() < 2) pedidosNoCompletados.add(pedidoCliente);
            }
        }
        return pedidosNoCompletados;
    }

    public void exportarAExcelPedidos(ArrayList<Pedido> pedidos, String nombreArchivo, String email) {
        ArrayList<PedidoClienteDataClass> datosCliente = getTodosPedidosConCliente();
        Persistencia.exportarAExcelPedidos(pedidos, datosCliente, trabajadores, nombreArchivo, email);
    }

    public String recuperaRuta(String rutaSolicitada) {
        return Persistencia.recuperaRuta(rutaSolicitada);
    }

    public void modificaModoInvitado(String nuevoModoInvitado) {
        Persistencia.modificaModoInvitado(nuevoModoInvitado);
    }

   /* public void modificaRuta(String rutaAModificadar, String nuevaRuta) {
        Persistencia.modificaRuta(rutaAModificadar, nuevaRuta);
    }*/

}