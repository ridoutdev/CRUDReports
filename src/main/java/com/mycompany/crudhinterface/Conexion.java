package com.mycompany.crudhinterface;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author RidO
 */
public class Conexion {
    
    private static Connection conexion;
    
    static{
        String url="jdbc:mysql://localhost:3306/acceso";
        String user="root";
        String pass="";
        
        try {
            conexion = DriverManager.getConnection(url, user, pass);
            System.out.println("Conexión realizada con exito!!!");
        } catch (SQLException ex) {
            System.out.println(ex);
        }
        
    }

    public static Connection getConexion() {
        return conexion;
    }
}
