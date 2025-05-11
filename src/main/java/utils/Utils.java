package utils;

import models.Pedido;
import persistencia.Persistencia;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.DateTimeException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Scanner;

public class Utils {

    public static final Scanner S = new Scanner(System.in);
    public static final String CARACTERESESP = "-_!@/&.:,;·#$€%¬/()=?¿¡";

    public static void limpiarPantalla(){
        for (int i = 0; i < 1000; i++) {
            System.out.println();
        }
    }

    public static void pulsaEnter(){
        System.out.print("Pulsa enter para continuar ...");
        S.nextLine();
    }

    public static void pulsaEnter(String s){
        System.out.print(s);
        S.nextLine();
    }

    public static boolean esDigito(String comprobar){
        if(comprobar.isEmpty()) return false;
        try {
            Integer.parseInt(comprobar);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }

    public static int potencia(int x, int y) {
        int resultado = 1;
        for (int i = 0; i < y; i++) {
            resultado *= x;
        }

        return resultado;
    }

    public static int digitos(int num){
        int digitos = 0;
        do{
            num /= 10;
            digitos++;
        }while (num!=0);
        return digitos;
    }

    public static int numAleatorio100(int min, int max) {
        int num;
        do{
            num=(int) (Math.random()*potencia(10, digitos(max)));
        }while (num<min || num>max);
        return num;
    }


    public static float redondearDosDecimales(float decimal) {
        int pasoEntero;
        pasoEntero = (int) (decimal * 100);
        return (float) (pasoEntero) / 100;
    }

    public static String fechaAString(LocalDate fechaFormato) {
        String fecha = "";
        String fechaRecibida = String.valueOf(fechaFormato);
        for (int i = 0; i < fechaRecibida.length(); i++) {
            if((fechaRecibida.charAt(i)=='-') || (i==fechaRecibida.length()-1)){
                if (i==9) i++;
                for (int j = i-1; j > -1; j--) {
                    if (fechaRecibida.charAt(j) != '-') fecha = fechaRecibida.charAt(j) + fecha;
                    else{
                        j = -1;
                    }
                }
                if (i!=10 && fechaRecibida.charAt(i)=='-') fecha = '/' + fecha;
            }

        }
        return fecha;
    }

    public static LocalDate stringAFecha(String fecha) throws Exception {
        String fechaFormato = "";
        for (int i = 0; i < fecha.length(); i++) {
            if((fecha.charAt(i)=='/') || (i==fecha.length()-1)){
                if (i==9) i++;
                for (int j = i-1; j > -1; j--) {
                    if (fecha.charAt(j) != '/') fechaFormato = fecha.charAt(j) + fechaFormato;
                    else{
                        j = -1;
                    }
                }
                if (i!=10 && fecha.charAt(i)=='/') fechaFormato = '-' + fechaFormato;
            }

        }
        LocalDate localDate;
        try{
            localDate = LocalDate.parse(fechaFormato);
        }catch (DateTimeException e){
            throw new Exception("Fecha mal introducida");
        }
        return localDate;
    }

    public static boolean validaTelefono(int introTelefono) {
        int cont = 1;
        int aux = introTelefono;
        while (introTelefono>10){
            introTelefono /= 10;
            cont++;
        }
        if (cont != 9) return false;
        introTelefono = aux;
        introTelefono /= potencia(10,cont-1);
        if (introTelefono != 6 && introTelefono != 7) return false;
        return true;
    }

    public static boolean validaCorreo(String introCorreo) {
        if (introCorreo.isEmpty()) return false;
        if (introCorreo == null) return false;
        String patron = "^[\\w._%+-]+@[\\w.-]+\\.[a-zA-Z]{2,6}$";
        return introCorreo.matches(patron);
        //return (introCorreo.endsWith("@gmail.com")) || (introCorreo.endsWith("@hotmail.com"));
    }
    // Esta función genera un token para enviar por correo para la validación
    public static String generarToken() {
        String token = "";
        String nums = "1234567890";
        String letras ="abcdefghijklmnopqrstuvwxyz";
        for (int i = 0; i < 10; i++) {
            switch ((int)(Math.random()*3)){
                case 0-> token += nums.charAt(Utils.numAleatorio100(0,nums.length()-1));
                case 1-> token += letras.charAt(Utils.numAleatorio100(0,letras.length()-1));
                case 2-> token += letras.toUpperCase().charAt(Utils.numAleatorio100(0,letras.length()-1));
            };
        }
        return token;
    }

    //Limpia espacios por delante y por detras de un string en caso de necesitarlo.
    public static String limpiaEspacios(String s) {
        if (s.isEmpty()) return s;
        if (s.charAt(0)== ' ') s = s.substring(1, s.length());
        if (s.charAt(s.length()-1) == ' ') s = s.substring(0, s.length()-1);
        return s;
    }

    //Utils que devuelve un String sin acentos
    public static String quitaAcentos(String s) {
        for (int i = 0; i < s.length(); i++) {
            switch (s.toUpperCase().charAt(i)){
                case 'Á','Â', 'Ä', 'À', 'Ã' -> s = s.substring(0,i) + 'A' + s.substring(i+1,s.length());
                case 'É', 'Ê', 'Ë', 'È' -> s = s.substring(0,i) + 'E' + s.substring(i+1,s.length());
                case 'Í', 'Î', 'Ï', 'Ì' -> s = s.substring(0,i) + 'I' + s.substring(i+1,s.length());
                case 'Ó', 'Ô', 'Ö', 'Ò', 'Õ' -> s = s.substring(0,i) + 'O' + s.substring(i+1,s.length());
                case 'Ú', 'Û', 'Ü', 'Ù' -> s = s.substring(0,i) + 'U' + s.substring(i+1,s.length());
                case 'Ý', 'Ŷ', 'Ÿ' -> s = s.substring(0,i) + 'Y' + s.substring(i+1,s.length());
            };
        }
        return s;
    }

    public static boolean validaClave(String clave) {
        if (clave.length()<8) return false;
        boolean valido = false;
        for (int i = 0; i < CARACTERESESP.length(); i++) {
            if (clave.contains(String.valueOf(CARACTERESESP.charAt(i)))) valido = true;
        }
        if (!valido) return false;
        valido = false;
        for (int i = 0; i < clave.length(); i++) if (Character.isDigit(clave.charAt(i))) valido=true;
        if (!valido) return false;
        valido = false;
        for (int i = 0; i < clave.length(); i++) if (Character.isUpperCase(clave.charAt(i))) valido=true;
        if (!valido) return false;
        valido = false;
        for (int i = 0; i < clave.length(); i++) if (Character.isLowerCase(clave.charAt(i))) valido=true;
        if (!valido) return false;
        return true;
    }

    public static boolean validaPrecio(String nuevoDato) {
        if (nuevoDato == null || nuevoDato.isEmpty()) return false;
        try {
            Float.parseFloat(nuevoDato);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public static void veteADormir(){
        try {
            Thread.sleep(750);
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public static void veteADormir(String s){
        try {
            System.out.println(s);
            for (int i = 0; i < 3; i++) {
                System.out.print(". ");
                Thread.sleep(750);
            }
        }catch (InterruptedException e){
            e.printStackTrace();
        }
    }

    public static <T> void posicionLista(String s, Object obj, ArrayList<T> lista) {
        System.out.println(s + (lista.indexOf(obj)+1) + " de "+ lista.size());
    }

    public static boolean validaRuta(String nuevaRuta) {
        String rutaBase = String.valueOf(Paths.get(Persistencia.RUTA_CONFIG).toAbsolutePath());

        String unidadExistente = rutaBase.substring(0, 2); // e.g., "C:"
        return nuevaRuta.matches("^" + unidadExistente + "\\\\.*");
    }
}