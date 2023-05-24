/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.etf.sab.student;

import java.math.BigDecimal;
import java.sql.*;
import java.util.Calendar;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.OrderOperations;

/**
 *
 * @author stoja
 */
public class sa190222_OrderOperations implements OrderOperations {

    @Override
    public int addArticle(int idOrder, int idArticle, int count) {
        
        Connection conn = DB.getInstance().getConnection();
        
        String query = "Select * from Article where Count > ? and IdArticle = ?";
        try(PreparedStatement ps = conn.prepareStatement(query)) {
            
            ps.setInt(1, count);
            ps.setInt(2, idArticle);
            
            try(ResultSet rs = ps.executeQuery()){
         
                
                
            } catch (SQLException ex) {
                Logger.getLogger(sa190222_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
                    
        } catch (SQLException ex) {
            Logger.getLogger(sa190222_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return -1;        
    }

    @Override
    public int removeArticle(int i, int i1) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Integer> getItems(int i) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int completeOrder(int i) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public BigDecimal getFinalPrice(int i) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public BigDecimal getDiscountSum(int i) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public String getState(int orderId) {
        
        Connection conn = DB.getInstance().getConnection();
        
        String query = "Select Status from [Order] where IdOrder = ?";        
        try(PreparedStatement ps = conn.prepareStatement(query);) {
            
            ps.setInt(1, orderId);
            try(ResultSet rs = ps.executeQuery();){
                
                if(rs.next()){
                    return rs.getString(1);
                }
                
            } catch (SQLException ex) {
                Logger.getLogger(sa190222_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(sa190222_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return "";
    }

    @Override
    public Calendar getSentTime(int i) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Calendar getRecievedTime(int i) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int getBuyer(int i) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int getLocation(int i) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    public static void main(String[] args) {
        
        String ret = new sa190222_OrderOperations().getState(1);
        System.out.println(ret);
    }
    
}
