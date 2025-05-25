package Communications;

import data.DataIVA;
import jakarta.activation.DataHandler;
import jakarta.activation.FileDataSource;
import jakarta.mail.*;
import jakarta.mail.internet.InternetAddress;
import jakarta.mail.internet.MimeBodyPart;
import jakarta.mail.internet.MimeMessage;
import jakarta.mail.internet.MimeMultipart;
import models.Cliente;
import models.Pedido;
import models.Producto;
import models.Trabajador;
import persistencia.Persistencia;
import utils.Utils;

import java.time.LocalDate;
import java.util.Properties;

import static jakarta.mail.Transport.send;

public class Email {

    private static Properties pro = Persistencia.iniciaProperties();

    public static final String HOST = pro.getProperty("hostCorreo");

    private static final String USER = pro.getProperty("correoEmpresa");

    private static final String PASS = pro.getProperty("passCorreo");

    //Prepara un correo para los nuevos usuarios
    public static boolean enviarCorreoVerificacionRegistro(String token, String nombre, String email){
        String correo="<div style=\"width: 65%; margin: auto;\">\n" +
                "    <div style=\"background-color: #9B76C5; padding: 20px; text-align: center; color: #ffffff; font-family: Arial, sans-serif;\">\n" +
                "        <h1 style=\"margin: 0;\">¡Bienvenido a FernanShop!</h1>\n" +
                "        <p style=\"font-size: 18px; margin: 5px 0;\">Todo lo que necesitas para tu creatividad y oficina</p>\n" +
                "    </div>\n" +
                "\n" +
                "    <div style=\"font-family: Arial, sans-serif; line-height: 1.6;background-color:#cccccc; color: #333333; padding: 20px;\">\n" +
                "        <h2 style=\"color: #9B76C5;\">Bienvenido a la familia de Fernanshop "+ nombre +"</h2>\n" +
                "        <p>¡Estas a un paso de acceder a FernanShop!<br><br>\n" +
                "\n" +
                "            Solo tienes que introducir este token: <b>"+ token +"</b> cuando intentes acceder a tu cuenta.<br>\n" +
                "            <br>\n" +
                "            Accede a nuestra pagina en el boton que tienes a continuacion y disfruta de nuestro contenido.\n" +
                "        </p>\n" +
                "        <a href=\"https://acortar.link/CBacV7\" ><div style=\"background-color: #00cc00; color: #ffffff; text-align: center; padding: 2px; margin: 20px 0; border-radius: 5px;\">\n" +
                "            <p style==\"color: #ffffff; text-decoration: none; font-size: 16px; font-weight: bold;\">Comprar ahora</p>\n" +
                "        </div></a>\n" +
                "        <h3 style=\"color: #9B76C5;\">Nuestras categorías más populares:</h3>\n" +
                "        <ul>\n" +
                "            <li>Material escolar</li>\n" +
                "            <li>Artículos de oficina</li>\n" +
                "            <li>Arte y manualidades</li>\n" +
                "            <li>Accesorios tecnológicos</li>\n" +
                "        </ul>\n" +
                "        <p>No te pierdas nuestras ofertas y descuentos semanales. ¡Suscríbete ahora a nuestro boletín para estar al día!</p>\n" +
                "    </div>\n" +
                "\n" +
                "\n" +
                "    <div style=\"background-color: #9B76C5; padding: 10px; text-align: center; color: #ffffff; font-size: 14px; font-family: Arial, sans-serif;\">\n" +
                "        <p style=\"margin: 0;\">Fernanshop © 2025. Todos los derechos reservados.</p>\n" +
                "        <p style=\"margin: 0;\">¿Tienes alguna pregunta? Escríbenos a <a href=\"mailto:fernanshop559@gmail.com\" style=\"color: #00cc00; text-decoration: none;\">fernanshop559@gmail.com</a></p>\n" +
                "    </div>\n" +
                "</div>";


        return enviaMail(email,
                "Bienvenido a Fernanshop, aqui esta tu correo de verificación", correo);
    }

    //Prepara un correo para cuando el cliente cambia de correo
    public static boolean enviaCorreoVerificacionCambioCorreo(String token, String nombre, String email){
        String correo="<div style=\"width: 65%; margin: auto;\">\n" +
                "    <div style=\"background-color: #9B76C5; padding: 20px; text-align: center; color: #ffffff; font-family: Arial, sans-serif;\">\n" +
                "        <h1 style=\"margin: 0;\">¡Bienvenido a FernanShop!</h1>\n" +
                "        <p style=\"font-size: 18px; margin: 5px 0;\">Todo lo que necesitas para tu creatividad y oficina</p>\n" +
                "    </div>\n" +
                "\n" +
                "    <div style=\"font-family: Arial, sans-serif; line-height: 1.6;background-color:#cccccc; color: #333333; padding: 20px;\">\n" +
                "        <h2 style=\"color: #9B76C5;\">Hemos recibido tu solicitud de cambio de correo "+ nombre +"</h2>\n" +
                "        <p> Solo tienes que introducir este token: <b>"+ token +"</b> cuando intentes acceder a tu cuenta.<br>\n" +
                "            <br>\n" +
                "            Accede a nuestra pagina en el boton que tienes a continuacion y disfruta de nuestro contenido.\n" +
                "        </p>\n" +
                "        <a href=\"https://acortar.link/CBacV7\" ><div style=\"background-color: #00cc00; color: #ffffff; text-align: center; padding: 2px; margin: 20px 0; border-radius: 5px;\">\n" +
                "            <p style==\"color: #ffffff; text-decoration: none; font-size: 16px; font-weight: bold;\">Comprar ahora</p>\n" +
                "        </div></a>\n" +
                "        <h3 style=\"color: #9B76C5;\">Nuestras categorías más populares:</h3>\n" +
                "        <ul>\n" +
                "            <li>Material escolar</li>\n" +
                "            <li>Artículos de oficina</li>\n" +
                "            <li>Arte y manualidades</li>\n" +
                "            <li>Accesorios tecnológicos</li>\n" +
                "        </ul>\n" +
                "        <p>No te pierdas nuestras ofertas y descuentos semanales. ¡Suscríbete ahora a nuestro boletín para estar al día!</p>\n" +
                "    </div>\n" +
                "\n" +
                "\n" +
                "    <div style=\"background-color: #9B76C5; padding: 10px; text-align: center; color: #ffffff; font-size: 14px; font-family: Arial, sans-serif;\">\n" +
                "        <p style=\"margin: 0;\">Fernanshop © 2025. Todos los derechos reservados.</p>\n" +
                "        <p style=\"margin: 0;\">¿Tienes alguna pregunta? Escríbenos a <a href=\"mailto:fernanshop559@gmail.com\" style=\"color: #00cc00; text-decoration: none;\">fernanshop559@gmail.com</a></p>\n" +
                "    </div>\n" +
                "</div>";


        return Email.enviaMail(email,
                "Verifica el cambio de correo", correo);
    }

    public static boolean generarCorreoAsignacionPedido(Trabajador trabajador, Pedido pedido, Cliente cliente) {
        // Construcción de la lista de productos
        StringBuilder productosHTML = new StringBuilder();

        for (Producto producto : pedido.getProductos()) {
            productosHTML.append("<li>")
                    .append(producto.getMarca()).append(" ")
                    .append(producto.getModelo()).append(" ")
                    .append("x"+pedido.getCantidadProductos().get(producto.getId())).append(" - ")
                    .append(String.format("%.2f", producto.getPrecio())).append(" €")
                    .append("</li>");
        }

        // Plantilla HTML con los datos dinámicos
        String correoHTML = "<div style=\"width: 65%; margin: auto;\">"
                + "<div style=\"background-color: #0056b3; padding: 20px; text-align: center; color: #ffffff; font-family: Arial, sans-serif;\">"
                + "<h1 style=\"margin: 0;\">¡Nuevo pedido asignado, <b>" + trabajador.getNombre() + "</b>!</h1>"
                + "<p style=\"font-size: 18px; margin: 5px 0;\">Se te ha asignado un nuevo pedido para gestionar.</p>"
                + "</div>"

                + "<div style=\"font-family: Arial, sans-serif; line-height: 1.6; background-color: #cccccc; color: #333333; padding: 20px;\">"
                + "<h2 style=\"color: #0056b3;\">Detalles del pedido</h2>"
                + "<p><b>Número de pedido:</b> " + pedido.getId() + "</p>"
                + "<p><b>Fecha del pedido:</b> " + pedido.getFechaPedido() + "</p>"
                + "<p><b>Fecha estimada de entrega:</b> " + pedido.getFechaEntregaEstimada() + "</p>"

                + "<h3 style=\"color: #0056b3;\">Datos del cliente:</h3>"
                + "<p><b>Nombre:</b> " + cliente.getNombre() + "</p>"
                + "<p><b>Dirección:</b> " + cliente.getDireccion() + ", " + cliente.getLocalidad() + ", " + cliente.getProvincia() + "</p>"
                + "<p><b>Correo:</b> " + cliente.getCorreo() + "</p>"

                + "<h3 style=\"color: #0056b3;\">Productos del pedido:</h3>"
                + "<ul>" + productosHTML.toString() + "</ul>"

                + "<p>Para cualquier consulta sobre este pedido, contacta con el cliente o el equipo de soporte.</p>"

                + "<p><b>Tu número de contacto:</b> " + trabajador.getMovil() + "</p>"

                + "<div style=\"background-color: #0056b3; padding: 10px; text-align: center; color: #ffffff; font-size: 14px; font-family: Arial, sans-serif;\">"
                + "<p style=\"margin: 0;\">FernanShop © 2025. Todos los derechos reservados.</p>"
                + "<p style=\"margin: 0;\">¿Tienes dudas? Contacta con soporte en "
                + "<a href=\"mailto:fernanshop559@gmail.com\" style=\"color: #00cc00; text-decoration: none;\">fernanshop559@gmail.com</a></p>"
                + "</div>"
                + "</div>";

        return enviaMail(trabajador.getCorreo(), "Nuevo Pedido Asignado",correoHTML);
    }

    public static boolean generarCorreoBienvenida(Trabajador trabajador) {
        // Plantilla HTML con los datos dinámicos
        String correoHTML = "<div style=\"width: 65%; margin: auto;\">"
                + "<div style=\"background-color: #0073e6; padding: 20px; text-align: center; color: #ffffff; font-family: Arial, sans-serif;\">"
                + "<h1 style=\"margin: 0;\">¡Bienvenido a FernanShop, <b>" + trabajador.getNombre() + "</b>!</h1>"
                + "<p style=\"font-size: 18px; margin: 5px 0;\">Estamos emocionados de que formes parte de nuestro equipo.</p>"
                + "</div>"

                + "<div style=\"font-family: Arial, sans-serif; line-height: 1.6; background-color: #f4f4f4; color: #333333; padding: 20px;\">"
                + "<h2 style=\"color: #0073e6;\">Tus credenciales de acceso</h2>"
                + "<p><b>Correo electrónico:</b> " + trabajador.getCorreo() + "</p>"
                + "<p><b>Contraseña temporal:</b> " + trabajador.getPass() + "</p>"
                + "<p>Por favor, cambia tu contraseña tras el primer inicio de sesión.</p>"

                + "<h3 style=\"color: #0073e6;\">Tu información de contacto</h3>"
                + "<p><b>Nombre:</b> " + trabajador.getNombre() + "</p>"
                + "<p><b>Número de contacto:</b> " + trabajador.getMovil() + "</p>"

                + "<h3 style=\"color: #0073e6;\">¿Qué debes hacer ahora?</h3>"
                + "<ul>"
                + "<li>Accede a tu cuenta con las credenciales proporcionadas.</li>"
                + "<li>Familiarízate con nuestro sistema y procesos.</li>"
                + "<li>No dudes en contactar a tu supervisor si tienes preguntas.</li>"
                + "</ul>"

                + "<a href=\"https://acortar.link/CBacV7\">"
                + "<div style=\"background-color: #00cc00; color: #ffffff; text-align: center; padding: 10px; margin: 20px 0; border-radius: 5px;\">"
                + "<p style=\"color: #ffffff; text-decoration: none; font-size: 16px; font-weight: bold;\">Acceder a la plataforma</p>"
                + "</div>"
                + "</a>"

                + "<p>¡Bienvenido a bordo! Estamos seguros de que harás un gran trabajo.</p>"

                + "<div style=\"background-color: #0073e6; padding: 10px; text-align: center; color: #ffffff; font-size: 14px; font-family: Arial, sans-serif;\">"
                + "<p style=\"margin: 0;\">FernanShop © 2025. Todos los derechos reservados.</p>"
                + "<p style=\"margin: 0;\">Si tienes preguntas, contacta con recursos humanos en "
                + "<a href=\"mailto:fernanshop559@gmail.com\" style=\"color: #00cc00; text-decoration: none;\">fernanshop559@gmail.com</a></p>"
                + "</div>"
                + "</div>";

        return enviaMail(trabajador.getCorreo(), "Bienvenido a la familia Fernanshop", correoHTML);
    }

    public static boolean pedidoNuevo(Cliente cliente, Pedido pedido, String rutaPDF, String nombreArchivo) {
        String email = cliente.getCorreo();
        StringBuilder productosHTML = new StringBuilder();

        for (Producto producto : pedido.getProductos()) {
            productosHTML.append("<li>")
                    .append(producto.getMarca()).append(" ")
                    .append(producto.getModelo()).append(" ")
                    .append("x"+pedido.getCantidadProductos().get(producto.getId())).append(" - ")
                    .append(String.format("%.2f", (producto.getPrecio() * pedido.getCantidadProductos().get(producto.getId())))).append(" €")
                    .append("</li>");
        }

        int iva = DataIVA.IVA;

        // Calcular totales con IVA
        float totalSinIVA = pedido.calculaTotalPedidoSinIVA();
        float totalIVA = pedido.calculaIVAPedido(DataIVA.IVA);
        float totalConIVA = pedido.calculaTotalPedidoConIVA(DataIVA.IVA);

        // Plantilla HTML con los datos dinámicos
        String correo = "<div style=\"width: 65%; margin: auto;\">"
                + "<div style=\"background-color: #9B76C5; padding: 20px; text-align: center; color: #ffffff; font-family: Arial, sans-serif;\">"
                + "<h1 style=\"margin: 0;\">¡Gracias por tu compra, <b>" + cliente.getNombre() + "</b>!</h1>"
                + "<p style=\"font-size: 18px; margin: 5px 0;\">Tu pedido ha sido confirmado con éxito.</p>"
                + "</div>"

                + "<div style=\"font-family: Arial, sans-serif; line-height: 1.6; background-color: #cccccc; color: #333333; padding: 20px;\">"
                + "<h2 style=\"color: #9B76C5;\">Detalles de tu pedido</h2>"
                + "<p><b>Número de pedido:</b> " + pedido.getId() + "</p>"
                + "<p><b>Fecha del pedido:</b> " + Utils.fechaAString(pedido.getFechaPedido()) + "</p>"
                + "<p><b>Fecha estimada de entrega:</b> " + Utils.fechaAString(pedido.getFechaEntregaEstimada()) + "</p>"

                + "<h3 style=\"color: #9B76C5;\">Productos adquiridos:</h3>"
                + "<ul>" + productosHTML.toString() + "</ul>"

                + "<h3 style=\"color: #9B76C5;\">Resumen del pago:</h3>"
                + "<p><b>Total sin IVA:</b> " + String.format("%.2f", totalSinIVA) + " €</p>"
                + "<p><b>IVA (" + iva + "%):</b> " + String.format("%.2f", totalIVA) + " €</p>"
                + "<p><b>Total con IVA:</b> <span style=\"color: #d32f2f; font-size: 18px;\">" + String.format("%.2f", totalConIVA) + " €</span></p>"

                + "<p>Pronto recibirás otro correo con la actualización del envío. ¡Gracias por confiar en FernanShop!</p>"

                + "<a href=\"https://acortar.link/CBacV7\">"
                + "<div style=\"background-color: #00cc00; color: #ffffff; text-align: center; padding: 10px; margin: 20px 0; border-radius: 5px;\">"
                + "<p style=\"color: #ffffff; text-decoration: none; font-size: 16px; font-weight: bold;\">Ver mi pedido</p>"
                + "</div>"
                + "</a>"
                + "</div>"

                + "<div style=\"background-color: #9B76C5; padding: 10px; text-align: center; color: #ffffff; font-size: 14px; font-family: Arial, sans-serif;\">"
                + "<p style=\"margin: 0;\">FernanShop © 2025. Todos los derechos reservados.</p>"
                + "<p style=\"margin: 0;\">¿Tienes alguna pregunta? Escríbenos a "
                + "<a href=\"mailto:fernanshop559@gmail.com\" style=\"color: #00cc00; text-decoration: none;\">fjrueda92@gmail.com</a>"
                + "</p>"
                + "</div>"
                + "</div>";

        return enviarCorreoConArchivoAdjunto(email, rutaPDF, nombreArchivo,"Nuevo Pedido Confirmado", correo);
    }


    public static boolean enviaMail(String destino, String asunto, String mensaje){
        // Creamos nuestra variable de propiedades con los datos de nuestro servidor de correo
        /*Properties props = new Properties();
        props.put("mail.smtp.auth","true");
        props.put("mail.smtp.starttls.enable","true");
        props.put("mail.smtp.HOST", HOST);
        props.put("mail.smtp.socketFactory.port","465");
        props.put("mail.smtp.socketFactor.class","javax.net.ssl.SSLSocketFactory");
*/
        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // TLS
        props.put("mail.smtp.host", HOST);
        props.put("mail.smtp.port", "587"); // Puerto correcto para TLS

        // Obtenemos la sesión en nuestro servidor de correo
        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USER, PASS);
            }
        });


        try{
            //Creamos un mensaje de correo por defecto
            Message message = new MimeMessage(session);

            // En el mensaje, establecemos el receptor
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(destino));

            //Establecemos el Asunto
            message.setSubject(asunto);

            // Añadimos el contenido del mensaje
            message.setContent(mensaje, "text/html; charset=utf-8");

            //Intentamos mandar el mensaje
            Transport.send(message);
        }catch (Exception e) { //Si entra aqui hemos tenido fallo
            throw new RuntimeException(e);
        }
        return true;
    }

    public static void enviarExcelProductos(String emailAdmin, String rutaArchivoExcel, String nombreArchivo, String parteNombreArchivo) {

        String mensaje ="<div style=\"width: 65%; margin: auto;\">\n" +
                "  <div style=\"background-color: #0056b3; padding: 20px; text-align: center; color: #ffffff; font-family: Arial, sans-serif;\">\n" +
                "    <h1 style=\"margin: 0;\">¡Informe de pedidos generado!</h1>\n" +
                "    <p style=\"font-size: 18px; margin: 5px 0;\">Se ha generado un archivo con los pedidos recientes.</p>\n" +
                "  </div>\n" +
                "\n" +
                "  <div style=\"font-family: Arial, sans-serif; line-height: 1.6; background-color: #cccccc; color: #333333; padding: 20px;\">\n" +
                "    <h2 style=\"color: #0056b3;\">Resumen del informe</h2>\n" +
                "    <p><b>Fecha de generación:</b> <i>"+Utils.fechaAString(LocalDate.now())+"</i></p>\n" +
                "    <p><b>Responsable:</b> Sistema automático de FernanShop</p>\n" +
                "\n" +
                "    <h3 style=\"color: #0056b3;\">Contenido del archivo Excel:</h3>\n" +
                "    <ul>\n" +
                "      <li>Listado de todos los pedidos registrados</li>\n" +
                "      <li>Información detallada del cliente por pedido</li>\n" +
                "      <li>Estado actual y fechas clave de cada pedido</li>\n" +
                "      <li>Productos incluidos en cada pedido</li>\n" +
                "    </ul>\n" +
                "\n" +
                "    <p>Puedes encontrar el archivo adjunto a este correo con el nombre: <b>"+nombreArchivo+"</b></p>\n" +
                "\n" +
                "    <p>Si tienes cualquier problema para visualizar el archivo, no dudes en contactar con soporte.</p>\n" +
                "  </div>\n" +
                "\n" +
                "  <div style=\"background-color: #0056b3; padding: 10px; text-align: center; color: #ffffff; font-size: 14px; font-family: Arial, sans-serif;\">\n" +
                "    <p style=\"margin: 0;\">FernanShop © 2025. Todos los derechos reservados.</p>\n" +
                "    <p style=\"margin: 0;\">¿Tienes dudas? Contacta con soporte en \n" +
                "      <a href=\"mailto:fernanshop559@gmail.com\" style=\"color: #00cc00; text-decoration: none;\">fernanshop559@gmail.com</a>\n" +
                "    </p>\n" +
                "  </div>\n" +
                "</div>";

        String asunto = "Resumen excel de " + parteNombreArchivo.replaceAll("[_]", " ");

        enviarCorreoConArchivoAdjunto(emailAdmin, rutaArchivoExcel, nombreArchivo, asunto ,mensaje);

    }

    private static boolean enviarCorreoConArchivoAdjunto(String emailDestino, String rutaArchivo, String nombreArchivo, String asunto , String mensaje){

        Properties props = new Properties();
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true"); // TLS
        props.put("mail.smtp.host", HOST);
        props.put("mail.smtp.port", "587");

        Session session = Session.getInstance(props, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(USER, PASS);
            }
        });

        try{
            Message mensajeEmail = new MimeMessage(session);
            mensajeEmail.setFrom(new InternetAddress(USER));
            mensajeEmail.setRecipients(Message.RecipientType.TO, InternetAddress.parse(emailDestino));
            mensajeEmail.setSubject(asunto);

            //Parte del texto
            MimeBodyPart cuerpoTexto = new MimeBodyPart();
            cuerpoTexto.setContent(mensaje, "text/html; charset=utf-8");

            // Parte del adjunto
            MimeBodyPart adjunto = new MimeBodyPart();
            FileDataSource source = new FileDataSource(rutaArchivo);
            adjunto.setDataHandler(new DataHandler(source));
            adjunto.setFileName(nombreArchivo);

            // Combinar partes
            Multipart multipart = new MimeMultipart();
            multipart.addBodyPart(cuerpoTexto);
            multipart.addBodyPart(adjunto);

            //Enviar
            mensajeEmail.setContent(multipart);
            Transport.send(mensajeEmail);
        } catch (MessagingException e) {
            System.out.println("No se ha podido enviar el correo con el archivo adjunto");
        }
        return true;
    }
}
