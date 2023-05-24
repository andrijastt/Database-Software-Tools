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
import rs.etf.sab.operations.TransactionOperations;

/**
 *
 * @author stoja
 */
public class sa190222_TransactionOperations implements TransactionOperations {

    @Override
    public BigDecimal getBuyerTransactionsAmmount(int idBuyer) {
        
        Connection conn = DB.getInstance().getConnection();        
        String query = "Select Sum(AmountPaid) from Transaction where IdBuyer = ?";
        try(PreparedStatement ps = conn.prepareStatement(query);) {
            
            ps.setInt(1, idBuyer);
            try(ResultSet rs = ps.executeQuery();){                
                if(rs.next()){                    
                    return rs.getBigDecimal(1);                    
                }
                return new BigDecimal(0);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(sa190222_TransactionOperations.class.getName()).log(Level.SEVERE, null, ex);
        }        
        
        return new BigDecimal(-1);
    }

    @Override
    public BigDecimal getShopTransactionsAmmount(int idShop) {
        
        Connection conn = DB.getInstance().getConnection();        
        String query = "Select Sum(AmountPaid) from Transaction where IdShop = ?";
        try(PreparedStatement ps = conn.prepareStatement(query);) {
            
            ps.setInt(1, idShop);
            try(ResultSet rs = ps.executeQuery();){                
                if(rs.next()){                    
                    return rs.getBigDecimal(1);                    
                }
                return new BigDecimal(0);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(sa190222_TransactionOperations.class.getName()).log(Level.SEVERE, null, ex);
        }        
        
        return new BigDecimal(-1);
    }

    @Override
    public List<Integer> getTransationsForBuyer(int i) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int getTransactionForBuyersOrder(int i) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int getTransactionForShopAndOrder(int i, int i1) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Integer> getTransationsForShop(int i) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public Calendar getTimeOfExecution(int i) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public BigDecimal getAmmountThatBuyerPayedForOrder(int i) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public BigDecimal getAmmountThatShopRecievedForOrder(int i, int i1) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public BigDecimal getTransactionAmount(int i) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public BigDecimal getSystemProfit() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
}
