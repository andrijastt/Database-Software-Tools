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
        
        Connection conn = DB.getInstance().getConnection();
        
        String query = "Delete from SimulationTime; Insert into SimulationTime(Id, Time) values(1, ?)";        
        java.util.Date utilDate =clndr.getTime();
        java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
        try(PreparedStatement ps = conn.prepareStatement(query);) {     
            
            ps.setDate(1,sqlDate);            
            ps.execute();            
        } catch (SQLException ex) {
            Logger.getLogger(sa190222_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }

    @Override
    public Calendar time(int days) {
        
        Connection conn = DB.getInstance().getConnection();
        String query = "Update SimulationTime set Time = Time + ? where Id = 1";
        
        try(PreparedStatement ps = conn.prepareStatement(query);) {                        
            
            ps.setInt(1, days);            
            ps.executeUpdate();            
            String query1 = "Select TOP 1Time from SimulationTime";
            try(PreparedStatement ps1 = conn.prepareStatement(query1);){                                    
                try(ResultSet rs = ps1.executeQuery();){                
                    if(rs.next()){
                        if(rs.getDate(1) == null) return null;
                        java.util.Date date = rs.getDate(1);                    
                        Calendar ret = Calendar.getInstance();
                        ret.setTime(date);
                        return ret;
                    }
                }            
            } catch (SQLException ex) {
                Logger.getLogger(sa190222_TransactionOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(sa190222_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Calendar getCurrentTime() {
        Connection conn = DB.getInstance().getConnection();
        
        String query = "Select TOP 1 Time from SimulationTime";
        try(PreparedStatement ps = conn.prepareStatement(query);){                                    
            try(ResultSet rs = ps.executeQuery();){                
                if(rs.next()){
                    if(rs.getDate(1) == null) return null;
                    java.util.Date date = rs.getDate(1);                    
                    Calendar ret = Calendar.getInstance();
                    ret.setTime(date);
                    return ret;
                }
            }            
        } catch (SQLException ex) {
            Logger.getLogger(sa190222_TransactionOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return null;
    }

    @Override
    public void eraseAll() {            // porveriti da li mora manualno da se radi?
        Connection conn = DB.getInstance().getConnection();
        String query = "exec sp_MSForEachTable 'DISABLE TRIGGER ALL ON ?'\n" +
        "exec sp_MSForEachTable 'ALTER TABLE ? NOCHECK CONSTRAINT ALL'\n" +
        "exec sp_MSForEachTable 'SET QUOTED_IDENTIFIER ON; DELETE FROM ?'\n" +
        "exec sp_MSForEachTable 'ALTER TABLE ? WITH CHECK CHECK CONSTRAINT ALL'\n" +
        "exec sp_MSForEachTable 'ENABLE TRIGGER ALL ON ?'";
        try (PreparedStatement ps = conn.prepareStatement(query);) {
            ps.execute();
        } catch (SQLException ex) {
            Logger.getLogger(sa190222_GeneralOperations.class.getName()).log(Level.SEVERE, null, ex);
        } 
    }
    
    public static void main(String[] args) {
        Calendar cal = Calendar.getInstance();        
        cal.set(2023, 4, 15);
        new sa190222_GeneralOperations().setInitialTime(cal);
    }
    
}
