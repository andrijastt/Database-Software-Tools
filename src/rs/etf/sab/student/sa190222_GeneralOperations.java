/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.etf.sab.student;

import java.sql.*;
import java.util.Calendar;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.GeneralOperations;

/**
 *
 * @author stoja
 */
public class sa190222_GeneralOperations implements GeneralOperations {

    @Override
    public void setInitialTime(Calendar clndr) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Calendar time(int i) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Calendar getCurrentTime() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public void eraseAll() {            // porveriti da li mora manualno da se radi?
        Connection conn = DB.getInstance().getConnection();
        String query = "EXEC sys.sp_msforeachtable 'ALTER TABLE ? NOCHECK CONSTRAINT ALL' \n"
                + "EXEC sys.sp_msforeachtable 'DELETE FROM ?'\n"
                + "EXEC sys.sp_MSForEachTable 'ALTER TABLE ? CHECK CONSTRAINT ALL' ";
        try (PreparedStatement ps = conn.prepareStatement(query);) {
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(sa190222_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    public static void main(String[] args) {
        new sa190222_GeneralOperations().eraseAll();
    }
    
}
