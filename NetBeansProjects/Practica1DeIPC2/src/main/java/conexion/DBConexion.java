/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package conexion;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author fernan
 */
public class DBConexion {
    private static final String IP = "localhost";
    private static final int PUERTO = 3306;
    private static final String USER_NAME = "root";
    private static final String SHEMA = "Pizzeria";
    private static final String PASSWORD = "Fernan16@2026";
    // ruta para acceder a la base de datos
    private static final String URL = "jdbc:mysql://"+ IP + ":"+ PUERTO + "/" + SHEMA;
    
    private Connection connection;
    
    public void connect(){
        System.out.println("url conexion "+URL);
        try {
            //conexion
            connection = DriverManager.getConnection(URL, USER_NAME, PASSWORD);
            System.out.println("Esquema: "+ connection.getSchema());
            System.out.println("Catalogo: "+ connection.getCatalog());
        } catch (Exception e) {
            System.out.println("Error al conectarse ");
            e.printStackTrace();
        }
    }
    
    public static Connection getConnection() throws SQLException{
        return DriverManager.getConnection(URL, USER_NAME, PASSWORD);
    }
    
}
