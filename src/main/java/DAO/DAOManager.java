package DAO;

import persistencia.Persistencia;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DAOManager {

    private  Connection conn;
    private final String URL;
    private final String USER;
    private final String PASS;
    private static DAOManager singlenton; //Atributo estatico que guarda una referencia al DAO

    private DAOManager() { //Constructor privado para que no se pueda llamar las veces que se quiera
        Properties pro = Persistencia.iniciaProperties();
        this.conn = null;
        this.URL = pro.getProperty("URLDB"); //Enlazo la dirección del servidor y de la base de datos a usar
        this.USER = pro.getProperty("UserDB"); //Usuario de la BBDD
        this.PASS = pro.getProperty("PassDB"); //Clave de la BBDD
    }

    public static DAOManager getSinglentonInstance(){ //Metodo que devuelve el DAO, si el atributo estatico ya ha sido
                                                      // inicializado no devuelve nada
        if (singlenton == null) singlenton = new DAOManager();

        return singlenton;
    }

    public Connection getConn() {
        return conn;
    }

    public void open() throws Exception {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver"); //Cargo el driver de conexión jdbc
            conn = DriverManager.getConnection(URL, USER, PASS); //Uso la clase DriverManager para crear la conexión
        } catch (Exception e) {
            throw e;
        }
    }

    public void close() throws SQLException {
        try
        {
            if(this.conn!=null)
                this.conn.close();
        }
        catch(Exception e) { throw e; }
    }

}


