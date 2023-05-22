/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package jdbc_vezbe;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author stoja
 */
public class DB {
    
    private static final String username = "sa";
    private static final String password = "Andrija`123";
    private static final String database = "ProdavnicaArtikala";
    private static final int port  = 1433;
    private static final String server = "localhost";
    
    private static final String ConnectionUrl = "jdbc:sqlserver://"+server+":"+port+";databaseName="+database;
    
    private static DB db = null;
    
    private Connection connection;
    
    private DB(){
        try {
            connection = DriverManager.getConnection(ConnectionUrl, username, password);
        } catch (SQLException ex) {
            Logger.getLogger(DB.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public static DB getInstance(){
        if(db == null) db = new DB();
        return db;
    }

    public Connection getConnection() {
        return connection;
    }
    
    
}


