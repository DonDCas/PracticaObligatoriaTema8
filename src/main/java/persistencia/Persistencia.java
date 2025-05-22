package persistencia;

import Communications.Email;
import DAO.DAOManager;
import DAO.DaoPedidosSQL;
import com.openhtmltopdf.pdfboxout.PdfRendererBuilder;
import data.DataIVA;
import models.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import utils.Utils;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;

public class Persistencia {
    public static final String RUTA_CONFIG = ".\\config\\config.properties";

    public static Properties iniciaProperties(){
        Properties pro = new Properties();
        try (FileReader fr = new FileReader(RUTA_CONFIG)) {
            pro.load(fr);
        } catch (IOException e) {

            throw new RuntimeException(e);
        }
        return pro;
    }

    //Comprobamos si existe la carpeta contenedora de los archivos
    public static void existeCarpetaContenedora() {
        Properties pro = iniciaProperties();
        File directorioRaiz = new File(pro.getProperty("rutaContenedor"));
        if (!directorioRaiz.exists()) directorioRaiz.mkdir();
    }

    public static void guardaDatosTrabajador(Trabajador trab) {
        Properties pro = iniciaProperties();
        File directorioTrabajadores = new File(pro.getProperty("rutaTrabajadores"));
        String nombreArchivo = trab.getId()+".trab";
        try (FileOutputStream fos = new FileOutputStream(directorioTrabajadores+"\\"+nombreArchivo);
             ObjectOutputStream oos = new ObjectOutputStream(fos)){
            oos.writeObject(trab);
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public static void guardaDatosCliente(Cliente cliente) {
        Properties pro = iniciaProperties();
        String nombreDirectorio = pro.getProperty("rutaClientes");
        File directorioCliente = new File(nombreDirectorio);
        String nombreArchivo = cliente.getId()+".cliente";
        try(FileOutputStream fos = new FileOutputStream(directorioCliente+"\\"+nombreArchivo);
        ObjectOutputStream oos = new ObjectOutputStream(fos)){
            oos.writeObject(cliente);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<Cliente> cargaDatosCliente() {
        Properties pro = iniciaProperties();
        ArrayList<Cliente> resultados = new ArrayList<>();
        String nombreDirectorio = pro.getProperty("rutaClientes");
        File directorioCliente = new File(nombreDirectorio);
        String[] archivos = directorioCliente.list();
        for (int i = 0; i < archivos.length; i++) {
            try(FileInputStream fis = new FileInputStream(directorioCliente+"\\"+archivos[i]);
            ObjectInputStream ois = new ObjectInputStream(fis)){
                Cliente clie = (Cliente) ois.readObject();
                resultados.add(clie);
            }catch (IOException | ClassNotFoundException e){
                throw new RuntimeException(e);
            }
        }

        return resultados;
    }

    public static boolean existenDatosProducto() {
        Properties pro = iniciaProperties();
        File directorioProductos = new File(pro.getProperty("rutaProductos"));
        if (!directorioProductos.exists()){
            directorioProductos.mkdirs();
            return false;
        }
        return true;
    }

    public static void guardaDatosProductos(ArrayList<Producto> catalogo) {
        for (Producto p : catalogo){
            guardaDatosProducto(p);
        }
    }

    public static void guardaDatosProducto(Producto p) {
        Properties pro = iniciaProperties();
        String nombreDirectorio = pro.getProperty("rutaProductos");
        File directorioProductos = new File(nombreDirectorio);
        if (!directorioProductos.exists()) directorioProductos.mkdirs();
        String nombreArchivo = p.getId() + ".producto";
        try (FileOutputStream fos = new FileOutputStream(nombreDirectorio+"\\"+nombreArchivo);
             ObjectOutputStream oos = new ObjectOutputStream(fos)){
            oos.writeObject(p);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
    }

    public static ArrayList<Producto> cargaDatosProducto() {
        Properties pro = iniciaProperties();
        ArrayList<Producto> resultado = new ArrayList<>();
        String nombreDirectorio = pro.getProperty("rutaProductos");
        File directorioProductos = new File(nombreDirectorio);
        if (!directorioProductos.exists()) return null;
        String[] archivos = directorioProductos.list();
        for (int i = 0; i < archivos.length; i++) {
            try(FileInputStream fis = new FileInputStream(nombreDirectorio+"\\"+archivos[i]);
                ObjectInputStream ois = new ObjectInputStream(fis)){
                Producto temp = (Producto) ois.readObject();
                resultado.add(temp);
            } catch (IOException | ClassNotFoundException e) {
                throw new RuntimeException(e);
            }
        }
        return resultado;
    }

    public static boolean registraLogInicioSesion(Object user) {
        Properties pro = iniciaProperties();
        String nombreDirectorio = pro.getProperty("rutaLogs");
        File directorioLogs = new File(nombreDirectorio);
        if (!directorioLogs.exists()) directorioLogs.mkdir();
        String tipoUser = "", nombreUser = "", correoUser = "";
        String fechaActual = String.valueOf(LocalDateTime.now());
        if (user != null){
            if (user instanceof Admin){
                nombreUser = ((Admin) user).getNombre();
                correoUser = ((Admin) user).getCorreo();
                tipoUser = "Admin";
            }
            if (user instanceof Trabajador){
                nombreUser = ((Trabajador) user).getNombre();
                correoUser = ((Trabajador) user).getCorreo();
                tipoUser = "Trabajador";
            }
            if (user instanceof Cliente){
                nombreUser = ((Cliente) user).getNombre();
                correoUser = ((Cliente) user).getCorreo();
                tipoUser = "Cliente";
            }
        } else{
            String lineaNueva = String.format("[ERROR INICIO SESION]: usuario no encontrado %s\n",fechaActual);
            try(BufferedWriter bw = new BufferedWriter(new FileWriter(directorioLogs+"\\.log.txt",true))){
                bw.write(lineaNueva);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return false;
        }
        String lineaNueva = String.format("[Inicio de sesion]: %s;%s;%s;%s\n",
                nombreUser,correoUser,tipoUser,fechaActual);
        try(BufferedWriter bw = new BufferedWriter(
                new FileWriter(directorioLogs+"\\log.txt",true))){
           bw.write(lineaNueva);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        return true;
    }

    public static boolean registraLogCierreSesion(Object user) {
        Properties pro = iniciaProperties();
        String nombreDirectorio = pro.getProperty("rutaLogs");
        File directorioLogs = new File(nombreDirectorio);
        if (!directorioLogs.exists()) directorioLogs.mkdirs();
        String nombreUser = "", correoUser = "", tipoUser = "";
        String fechaActual = String.valueOf(LocalDateTime.now());
        if(user == null){
            String linea = String.format("[ERROR AL REGISTRAR CIERRE SESION]: %s", fechaActual);
            try(BufferedWriter bw = new BufferedWriter(new FileWriter(directorioLogs+"\\log.txt",true))){
                bw.write(linea);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
            return false;
        }
        if (user instanceof Admin){
            nombreUser = ((Admin) user).getNombre();
            correoUser = ((Admin) user).getCorreo();
            tipoUser = "Admin";
        }
        if (user instanceof Trabajador){
            nombreUser = ((Trabajador) user).getNombre();
            correoUser = ((Trabajador) user).getCorreo();
            tipoUser = "Trabajador";
        }
        if (user instanceof Cliente){
            nombreUser = ((Cliente) user).getNombre();
            correoUser = ((Cliente) user).getCorreo();
            tipoUser = "Cliente";
        }
        String lineaNueva = String.format("[Cierre de sesion]: %s;%s;%s;%s\n",nombreUser,correoUser,tipoUser,fechaActual);
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(directorioLogs+"\\log.txt",true))){
            bw.write(lineaNueva);
        }catch (IOException e){
            throw new RuntimeException(e);
        }
        return true;
    }

    public static void registraLogPedidoModificado(String idPedido, String nuevoDatoModificado, String tipoModificacion) {
        Properties pro = iniciaProperties();
        String nombreDirectorio = pro.getProperty("rutaLogs");
        File directorio = new File(nombreDirectorio);
        if (!directorio.exists()) directorio.mkdirs();
        String fechaActual = String.valueOf(LocalDateTime.now());
        String lineaNueva = String.format("[Modificacion Pedido]: %s;%s;%s;%s\n",
                idPedido,tipoModificacion,nuevoDatoModificado,fechaActual);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(nombreDirectorio+"\\log.txt",true))){
            bw.write(lineaNueva);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    public static void registraLogAsignaPedido(Trabajador trabajador, Pedido pedido) {
        Properties pro = iniciaProperties();
        String nombreDirectorio = pro.getProperty("rutaLogs");
        File directorio = new File(nombreDirectorio);
        if (directorio.exists()) directorio.mkdirs();
        String fechaActual = String.valueOf(LocalDateTime.now());
        String linea = String.format("[Asignacion Pedido]: %s;%s;%s;%s\n",pedido.getId(),trabajador.getNombre(),trabajador.getCorreo(), fechaActual);
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(nombreDirectorio+"\\log.txt",true))){
            bw.write(linea);
        }catch (IOException e){
            throw new RuntimeException(e);
        }

    }

    public static void registraLogNuevoPedido(Cliente cliente, Pedido nuevoPedido) {
        Properties pro = iniciaProperties();
        String nombreDirectorio = pro.getProperty("rutaLogs");
        File directorio = new File(nombreDirectorio);
        if (!directorio.exists()) directorio.mkdirs();
        String fechaActual = String.valueOf(LocalDateTime.now());
        String lineaNueva = String.format("[Nuevo Pedido]: %s;%s;%s;%s\n",nuevoPedido.getId(), cliente.getNombre(),
                cliente.getCorreo(),fechaActual);
        try(BufferedWriter bw = new BufferedWriter(new FileWriter(nombreDirectorio+"\\log.txt",true))){
            bw.write(lineaNueva);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String recuperaInicioSesionUsuario(Object user) {
        Properties pro = iniciaProperties();
        String inicioSesion = "";
        if (user instanceof Cliente) inicioSesion = pro.getProperty(((Cliente) user).getId());
        if (user instanceof Trabajador) inicioSesion = pro.getProperty(((Trabajador) user).getId());
        if (user instanceof Admin) inicioSesion = pro.getProperty(((Admin) user).getId());
        if (inicioSesion == null) return "";
        return inicioSesion;
    }

    public static void guardarInicioSesion(Object user) {
        Properties pro = iniciaProperties();
        if (user instanceof Cliente) pro.setProperty(((Cliente) user).getId(),String.valueOf(LocalDate.now()));
        if (user instanceof Trabajador) pro.setProperty(((Trabajador) user).getId(),String.valueOf(LocalDate.now()));
        if (user instanceof Admin) pro.setProperty(((Admin) user).getId(),String.valueOf(LocalDate.now()));
        try (FileOutputStream out = new FileOutputStream(RUTA_CONFIG)) {
            pro.store(out, "Archivo de configuración");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static boolean compruebaInivitadoActivado() {
        Properties pro = iniciaProperties();
        return Boolean.parseBoolean(pro.getProperty("modoInvitado"));
    }

    public static void exportarAExcelPedidos(ArrayList<Pedido> pedidos, ArrayList<PedidoClienteDataClass> datosClientes,
                                             ArrayList<Trabajador> trabajadores, String parteNombreArchivo, String emailAdmin,
                                             DAOManager dao, DaoPedidosSQL daoPedidos) {
        Properties pro = iniciaProperties();
        Workbook libroExcel = new XSSFWorkbook();
        Trabajador trabajadorPedido = null;
        PedidoClienteDataClass datosCliente = null;
        for (Pedido pedido : pedidos){
            trabajadorPedido = null;
            for (Trabajador trabajador : trabajadores){
                for (Pedido pedidoTrabajador : daoPedidos.readByidTrabajadorAsignado(dao, trabajador.getId())){
                    if (pedidoTrabajador.getId().equals(pedido.getId())) trabajadorPedido = trabajador;
                }
            }
            for (PedidoClienteDataClass cliente : datosClientes){
                if (cliente.getIdPedido().equals(pedido.getId())) datosCliente = cliente;
            }
            //Nombramos cada hoja del excel con el id del pedido que vayamos a trabajar
            String nombreHoja = pedido.getId();
            // Creamos Arraylist de String por cada columna de la hoja de excel
            ArrayList<String> titulos = new ArrayList<>();
            ArrayList<String> datosCol1 = new ArrayList<>();
            ArrayList<String> datosCol2 = new ArrayList<>();
            ArrayList<String> datosCol3 = new ArrayList<>();
            Sheet hoja = libroExcel.createSheet(nombreHoja);

            String estado = (pedido.getEstado() == 0 ?
                    "Creado"
                    : (pedido.getEstado() == 1) ? "En Preparación"
                    : (pedido.getEstado() == 2) ? "Enviado"
                    : "Cancelado");

            //Cabecera de datos
            titulos.addAll(Arrays.asList("ID PEDIDO","Nombre Cliente","Dirección","Localidad","Provincia","Correo contacto",
                    "Telefono contacto","Fecha Realización Pedido","Fecha Entrega Estimada","Estado",
                    "Nombre Trabajador Asignado", "Correo Trabajador", "Telefono Trabajador", "Comentarios del pedido",
                    "PRODUCTOS"));
            for (int i = 0; i<pedido.getProductos().size(); i++){
                titulos.add("Producto "+ i);
            }
            titulos.addAll(Arrays.asList("Precio Total Sin IVA", "Precio Total Con IVA"));

            //Datos columna 1
            datosCol1.addAll(Arrays.asList(pedido.getId(), datosCliente.getNombreCliente(), datosCliente.getDireccionCliente(),
                    datosCliente.getLocalidadCliente(), datosCliente.getProvinciaCliente(),datosCliente.getEmailCliente(),
                    String.valueOf(datosCliente.getMovilCliente()), Utils.fechaAString(pedido.getFechaPedido())));
            datosCol1.add(pedido.getEstado() == 4 ? "CANCELADO": Utils.fechaAString(pedido.getFechaEntregaEstimada()));
            datosCol1.add(estado);
            datosCol1.add((trabajadorPedido == null ? "Sin trabajador" : trabajadorPedido.getNombre()));
            datosCol1.add((trabajadorPedido == null ? "Sin trabajador" : trabajadorPedido.getCorreo()));
            datosCol1.add((trabajadorPedido == null ? "Sin trabajador" : String.valueOf(trabajadorPedido.getMovil())));
            datosCol1.add((pedido.getComentario()));
            datosCol1.add("");
            String datosProducto = "";
            for (int i = 0; i<pedido.getProductos().size(); i++){
                datosProducto = String.format("%s - %s", pedido.getProductos().get(i).getMarca(), pedido.getProductos().get(i).getModelo());
                datosCol1.add(datosProducto);
            }
            datosCol1.addAll(Arrays.asList(String.format("%7.2f",pedido.calculaTotalPedidoSinIVA()),
                    String.format("%7.2f", pedido.calculaTotalPedidoConIVA(DataIVA.IVA))));

            //Datos columna 2
            for (int i = 0; i < 15; i++) {
                datosCol2.add("-");
            }
            for (int i = 0; i<pedido.getProductos().size(); i++){
                datosCol2.add("Precio: ");
            }
            datosCol2.add("-");
            datosCol2.add("-");

            //Datos columna 3
            for (int i = 0; i < 15; i++) {
                datosCol3.add("-");
            }
            for (int i = 0; i<pedido.getProductos().size(); i++){
                datosCol3.add(String.format("%7.2f",pedido.getProductos().get(i).getPrecio()));
            }
            datosCol3.add("-");
            datosCol3.add("-");

            for (int i = 0; i < titulos.size(); i++) {
                Row fila = hoja.createRow(i);
                fila.createCell(0).setCellValue(titulos.get(i));
                fila.createCell(1).setCellValue(datosCol1.get(i));
                fila.createCell(2).setCellValue(datosCol2.get(i).equals("-") ? "" : datosCol2.get(i));
                fila.createCell(3).setCellValue(datosCol3.get(i).equals("-") ? "" : datosCol3.get(i));
            }

        }

        //Generamos el archivo de forma "fisica" para enviarlo
        String nombreArchivo = "Pedidos_Excel_"+parteNombreArchivo+".xlsx";
        String rutaArchivoExcel = "data/"+nombreArchivo;
        try (FileOutputStream fos = new FileOutputStream(rutaArchivoExcel)){
            libroExcel.write(fos);
            libroExcel.close();
        }catch (IOException e){
            System.out.println("No se ha podido crear el excel");
        }

        //Enviamos el archivo creado
        Email.enviarExcelProductos(emailAdmin, rutaArchivoExcel, nombreArchivo, parteNombreArchivo);

        File archivoABorrar = new File(rutaArchivoExcel);

        archivoABorrar.delete();

    }

    public static boolean exportaCopiaDeSeguridad(Controlador controlador) {
        Properties pro = iniciaProperties();
        String nombreDirectorio = pro.getProperty("rutaBackUp");
        File directorioBackUp = new File(nombreDirectorio);
        if (!directorioBackUp.exists()) directorioBackUp.mkdirs();
        String nombreArchivo = nombreDirectorio + "\\" + String.valueOf(LocalDate.now()) + ".backup";
        try (FileOutputStream fos = new FileOutputStream(nombreArchivo);
             ObjectOutputStream oos = new ObjectOutputStream(fos)){
            oos.writeObject(controlador);
        }catch (IOException e){
            return false;
        }
        return true;
    }

    public static boolean exportaCopiaDeSeguridad(Controlador controlador, String rutaArchivo) {
        File directorioBackUp = new File(rutaArchivo);
        if (!directorioBackUp.exists()) directorioBackUp.mkdirs();
        String nombreArchivo = rutaArchivo + "\\" + String.valueOf(LocalDate.now()) + ".backup";
        try (FileOutputStream fos = new FileOutputStream(nombreArchivo);
             ObjectOutputStream oos = new ObjectOutputStream(fos)){
            oos.writeObject(controlador);
        }catch (IOException e){
            return false;
        }
        return true;
    }

    public static void creaFacturaPDF(Cliente cliente, Pedido nuevoPedido, HashMap<Integer, Integer> cantidadProductos) {
        Properties pro = iniciaProperties();

        String plantillaFactura = "<html lang=\"es\" xmlns=\"http://www.w3.org/1999/xhtml\">\n" +
                "<head>\n" +
                "    <meta charset=\"UTF-8\" />\n" +
                "    <title>Factura - FernanShop</title>\n" +
                "    <style>\n" +
                "        body {\n" +
                "            font-family: Arial, sans-serif;\n" +
                "            margin: 40px;\n" +
                "            color: #333;\n" +
                "        }\n" +
                "        h1, h2 {\n" +
                "            color: #9B76C5;\n" +
                "        }\n" +
                "        .resumen {\n" +
                "            margin-top: 30px;\n" +
                "        }\n" +
                "        table {\n" +
                "            width: 100%;\n" +
                "            border-collapse: collapse;\n" +
                "            margin-top: 15px;\n" +
                "        }\n" +
                "        table, th, td {\n" +
                "            border: 1px solid #ccc;\n" +
                "        }\n" +
                "        th {\n" +
                "            background-color: #f4f4f4;\n" +
                "        }\n" +
                "        th, td {\n" +
                "            padding: 10px;\n" +
                "            text-align: left;\n" +
                "        }\n" +
                "        .total {\n" +
                "            font-weight: bold;\n" +
                "            background-color: #eee;\n" +
                "        }\n" +
                "    </style>\n" +
                "</head>\n" +
                "<body>\n" +
                "\n" +
                "    <h1>FernanShop - Factura</h1>\n" +
                "    <p><strong>Cliente:</strong> {{cliente_nombre}}</p>\n" +
                "    <p><strong>Email:</strong> {{cliente_email}}</p>\n" +
                "    <p><strong>Número de Pedido:</strong> {{pedido_id}}</p>\n" +
                "    <p><strong>Fecha del Pedido:</strong> {{fecha_pedido}}</p>\n" +
                "    <p><strong>Fecha Estimada de Entrega:</strong> {{fecha_entrega}}</p>\n" +
                "\n" +
                "    <h2>Productos</h2>\n" +
                "    <table>\n" +
                "        <thead>\n" +
                "            <tr>\n" +
                "                <th>Marca</th>\n" +
                "                <th>Modelo</th>\n" +
                "                <th>Precio (€)</th>\n" +
                "            </tr>\n" +
                "        </thead>\n" +
                "        <tbody>\n" +
                "            {{filas_productos}}\n" +
                "        </tbody>\n" +
                "    </table>\n" +
                "\n" +
                "    <div class=\"resumen\">\n" +
                "        <h2>Resumen del Pedido</h2>\n" +
                "        <table>\n" +
                "            <tr>\n" +
                "                <td>Total sin IVA</td>\n" +
                "                <td>{{total_sin_iva}} €</td>\n" +
                "            </tr>\n" +
                "            <tr>\n" +
                "                <td>IVA ({{iva}}%)</td>\n" +
                "                <td>{{total_iva}} €</td>\n" +
                "            </tr>\n" +
                "            <tr class=\"total\">\n" +
                "                <td>Total con IVA</td>\n" +
                "                <td>{{total_con_iva}} €</td>\n" +
                "            </tr>\n" +
                "        </table>\n" +
                "    </div>\n" +
                "\n" +
                "    <p style=\"margin-top: 40px;\">Gracias por confiar en FernanShop. ¡Esperamos verte pronto de nuevo!</p>\n" +
                "\n" +
                "</body>\n" +
                "</html>";

        String html = plantillaFactura.replace("{{cliente_nombre}}", cliente.getNombre())
                .replace("{{cliente_email}}", cliente.getCorreo())
                .replace("{{pedido_id}}", String.valueOf(nuevoPedido.getId()))
                .replace("{{fecha_pedido}}", Utils.fechaAString(nuevoPedido.getFechaPedido()))
                .replace("{{fecha_entrega}}", Utils.fechaAString(nuevoPedido.getFechaEntregaEstimada()))
                .replace("{{filas_productos}}", generaFilasHTML(nuevoPedido.getProductos(), cantidadProductos))
                .replace("{{total_sin_iva}}", String.format("%.2f", nuevoPedido.calculaTotalPedidoSinIVA()))
                .replace("{{total_iva}}", String.format("%.2f", nuevoPedido.calculaIVAPedido(DataIVA.IVA)))
                .replace("{{total_con_iva}}", String.format("%.2f", nuevoPedido.calculaTotalPedidoConIVA(DataIVA.IVA)))
                .replace("{{iva}}", String.valueOf(DataIVA.IVA));

        String nombreArchivo = "factura_Pedido_" + nuevoPedido.getId() + ".pdf";
        Path rutaPDF = Paths.get("data", nombreArchivo);

        try {
            // Crea el directorio si no existe
            Files.createDirectories(rutaPDF.getParent());

            try (OutputStream os = Files.newOutputStream(rutaPDF)) {
                PdfRendererBuilder builder = new PdfRendererBuilder();
                builder.useFastMode();
                builder.withHtmlContent(html, null);  // asegúrate de que html tiene contenido válido
                builder.toStream(os);
                builder.run();
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("Error generando el PDF: " + e.getMessage(), e);
        }

        Email.pedidoNuevo(cliente, nuevoPedido, String.valueOf(rutaPDF), nombreArchivo);

        File archivo = new File(String.valueOf(rutaPDF));
        archivo.delete();

    }

    private static String generaFilasHTML(List<Producto> productos, HashMap<Integer, Integer> cantidadProductos) {
        StringBuilder sb = new StringBuilder();
        for (Producto p : productos) {
            sb.append("<tr>")
                    .append("<td>").append(p.getMarca()).append("</td>")
                    .append("<td>").append(p.getModelo()).append("</td>")
                    .append("<td>").append(String.format("%.2f", p.getPrecio())).append(" €</td>")
                    .append("<td>").append(String.format("%3d", cantidadProductos.get(p.getId()))).append(" €</td>")
                    .append("</tr>");
        }
        return sb.toString();
    }

    public static String recuperaRuta(String rutaSolicitada) {
        Properties pro = iniciaProperties();
        File ruta = new File(pro.getProperty(rutaSolicitada));
        return ruta.getAbsolutePath();
    }

    public static void modificaModoInvitado(String nuevoModoInvitado) {
        Properties pro = iniciaProperties();
        pro.setProperty("modoInvitado", nuevoModoInvitado);
        try (FileOutputStream out = new FileOutputStream(RUTA_CONFIG)){
            pro.store(out , "ModoInvitado modificado "+LocalDate.now());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    /* No funciona bien

       ___
        .'/,-Y"     "~-.
        l.Y             ^.
        /\               _\_
       i            ___/"   "\
       |          /"   "\   o !
       l         ]     o !__./
        \ _  _    \.___./    "~\
         X \/ \            ___./
        ( \ ___.   _..--~~"   ~`-.
         ` Z,--   /               \
           \__.  (   /       ______)
             \   l  /-----~~" /
              Y   \          /
              |    "x______.^
              |           \
              |            \
             /              \_


    public static void modificaRuta(String rutaAModificadar, String nuevaRuta) {
        Properties pro = iniciaProperties();
        String nombreCarpeta = pro.getProperty(rutaAModificadar);
        String[] partesNombreCarpeta = nombreCarpeta.split("\\\\");
        nombreCarpeta = partesNombreCarpeta[partesNombreCarpeta.length-1];
        File rutaAntigua = new File(pro.getProperty(rutaAModificadar));
        File rutaNueva = new File(nuevaRuta+"\\"+nombreCarpeta);

        if (!rutaNueva.exists()) {
            rutaNueva.mkdirs();
        }
        File[] archivos = rutaAntigua.listFiles();
        if (archivos != null) {
            for (File archivo : archivos) {
                File destino = new File(rutaNueva, archivo.getName());
                try {
                    Files.copy(archivo.toPath(), destino.toPath(), StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException e) {
                    throw new RuntimeException("Error copiando el archivo: " + archivo.getName(), e);
                }
            }
        }

        // Convertir nueva ruta a relativa antes de guardarla en el properties
        String rutaBase = new File(pro.getProperty("rutaContenedor")).getAbsolutePath();  // Directorio actual del proyecto
        Path pathBase = Paths.get(pro.getProperty("rutaContenedor")).toAbsolutePath();
        Path pathNueva = rutaNueva.toPath();
        Path rutaRelativa = pathBase.relativize(pathNueva);

        // Actualizar la propiedad con la nueva ruta
        pro.setProperty(rutaAModificadar, String.valueOf(rutaRelativa));

        // Guardar los cambios en el archivo de configuración
        try (FileOutputStream out = new FileOutputStream(RUTA_CONFIG)) {
            pro.store(out, "Modificada la ruta " + rutaAModificadar + " " + LocalDate.now());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        // (Opcional) Eliminar el directorio antiguo si lo deseas:
        for (File archivo : archivos) archivo.delete();
        rutaAntigua.delete();
    }*/
}