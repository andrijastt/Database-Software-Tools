/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.etf.sab.student;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
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
        int articleCount = 0;        
        String queryUI = "";
        
        String selectQuery = "Select * from Item where IdOrder = ? and IdArticle = ?";
        try(PreparedStatement psSelect = conn.prepareStatement(selectQuery);) {
            
            psSelect.setInt(1, idOrder);
            psSelect.setInt(2, idArticle);
            
            try(ResultSet rsSelect = psSelect.executeQuery();){                                                
                if(rsSelect.next()){
                    queryUI = "UPDATE Item SET Count = Count + ? where IdOrder = ? and IdArticle = ?";
                    articleCount = rsSelect.getInt(2);
                } else {
                    queryUI = "INSERT INTO Item(Count, IdOrder, IdArticle) values (?, ?, ?)";
                }                 
            }             
        } catch (SQLException ex) {
            Logger.getLogger(sa190222_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
                        
        String query = "Select * from Article where Count > ? + ? and IdArticle = ?";
        try(PreparedStatement ps = conn.prepareStatement(query)) {            
            ps.setInt(1, count);
            ps.setInt(2, articleCount);
            ps.setInt(3, idArticle);
            
            try(ResultSet rs = ps.executeQuery()){
                         
                if(rs.next()){          // has the amount
                  
                    try(PreparedStatement psUI = conn.prepareStatement(queryUI, Statement.RETURN_GENERATED_KEYS);){                        
                        psUI.setInt(1, count);
                        psUI.setInt(2, idOrder);
                        psUI.setInt(3, idArticle);                        
                        psUI.executeUpdate();                        
                        ResultSet rsUI = psUI.getGeneratedKeys();
                        if(rsUI.next()){
                            return rsUI.getInt(1);      // TODO za update
                        }                        
                    }                    
                }                 
            } 
                    
        } catch (SQLException ex) {
            Logger.getLogger(sa190222_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return -1;        
    }

    @Override
    public int removeArticle(int idOrder, int idArticle) {
        
        Connection conn = DB.getInstance().getConnection();
        
        String query = "Delete from Item where IdOrder = ? and IdArticle = ?";
        try(PreparedStatement ps = conn.prepareStatement(query);) {
            
            ps.setInt(1, idOrder);
            ps.setInt(2, idArticle);
            
            int ret = ps.executeUpdate();            
            if(ret == 0) return -1;
            return ret;
            
        } catch (SQLException ex) {
            Logger.getLogger(sa190222_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return -1;
    }

    @Override
    public List<Integer> getItems(int idOrder) {
        
        ArrayList<Integer> ret = new ArrayList<>();
        
        Connection conn = DB.getInstance().getConnection();
        
        String query = "Select IdItem from Item where IdOrder = ?";        
        try(PreparedStatement ps = conn.prepareStatement(query);) {
            
            ps.setInt(1, idOrder);            
            try(ResultSet rs = ps.executeQuery();){
                
                while(rs.next()){
                    ret.add(rs.getInt(1));
                }                
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(sa190222_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return ret;
        
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
    public Calendar getSentTime(int idOrder) {
        Connection conn = DB.getInstance().getConnection();
        
        String query = "Select SentTime from [Order] where IdOrder = ?";
        try(PreparedStatement ps = conn.prepareStatement(query);) {            
            ps.setInt(1, idOrder);
            try(ResultSet rs = ps.executeQuery();){
                
                if(rs.next()){                                        
                    java.util.Date date = rs.getDate(1);                    
                    Calendar ret = Calendar.getInstance();
                    ret.setTime(date);
                    return ret;
                }
                
            }            
        } catch (SQLException ex) {
            Logger.getLogger(sa190222_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
        }        
        
        return null;
    }

    @Override
    public Calendar getRecievedTime(int idOrder) {
        
        Connection conn = DB.getInstance().getConnection();
        
        String query = "Select ReceivedTime from [Order] where IdOrder = ?";
        try(PreparedStatement ps = conn.prepareStatement(query);) {            
            ps.setInt(1, idOrder);
            try(ResultSet rs = ps.executeQuery();){
                
                if(rs.next()){                                        
                    java.util.Date date = rs.getDate(1);                    
                    Calendar ret = Calendar.getInstance();
                    ret.setTime(date);
                    return ret;
                }
                
            }            
        } catch (SQLException ex) {
            Logger.getLogger(sa190222_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
        }        
        
        return null;
    }

    @Override
    public int getBuyer(int idOrder) {
        
        Connection conn = DB.getInstance().getConnection();        
        String query = "Select IdBuyer from [Order] where IdOrder = ?";
        try(PreparedStatement ps = conn.prepareStatement(query);) {
            
            ps.setInt(1, idOrder);            
            try(ResultSet rs = ps.executeQuery();){                
                if(rs.next()){
                    return rs.getInt(1);
                }                
            }            
        } catch (SQLException ex) {
            Logger.getLogger(sa190222_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return -1;
    }

    @Override
    public int getLocation(int idOrder) {
        
        Connection conn = DB.getInstance().getConnection();
        
        String query = "Select Status, CurrentCity from [Order] where IdOrder = ?";
         try(PreparedStatement ps = conn.prepareStatement(query);) {
            
            ps.setInt(1, idOrder);            
            try(ResultSet rs = ps.executeQuery();){                
                if(rs.next()){
                    String temp = rs.getString(1);
                    if(rs.getString(1).equals("created")) return -1;                    
                    return rs.getInt(2);
                }                
            }            
        } catch (SQLException ex) {
            Logger.getLogger(sa190222_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return -1;
    }
    
    public static void main(String[] args) {
        
        String ret = new sa190222_OrderOperations().getState(1);
        System.out.println(ret);
        
        int retInt = new sa190222_OrderOperations().getLocation(1);  
//        retInt = new sa190222_OrderOperations().removeArticle(1, 2); 

        List<Integer> listRet = new sa190222_OrderOperations().getItems(1);
        System.out.println(retInt);
        
        Calendar cal = new sa190222_OrderOperations().getSentTime(1);
//        System.out.println(cal);
    }
    
}
