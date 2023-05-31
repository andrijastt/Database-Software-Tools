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
import rs.etf.sab.operations.TransactionOperations;

/**
 *
 * @author stoja
 */
public class sa190222_TransactionOperations implements TransactionOperations {

    @Override
    public BigDecimal getBuyerTransactionsAmmount(int idBuyer) {
        
        Connection conn = DB.getInstance().getConnection();        
        String query = "Select Sum(AmountPaid) from [Transaction] where IdBuyer = ?";
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
        String query = "Select Sum(AmountPaid) from [Transaction] where IdShop = ?";
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
    public List<Integer> getTransationsForBuyer(int idBuyer) {
        
        ArrayList<Integer> ret = new ArrayList<>();
        
        Connection conn = DB.getInstance().getConnection();        
        String query = "Select IdTransaction from [Transaction] where IdBuyer = ?";
        try(PreparedStatement ps = conn.prepareStatement(query);) {
            
            ps.setInt(1, idBuyer);
            try(ResultSet rs = ps.executeQuery();){                
                while(rs.next()){                    
                    ret.add(rs.getInt(1));
                }
                return ret;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(sa190222_TransactionOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }

    @Override
    public int getTransactionForBuyersOrder(int idOrder) {
        
        Connection conn = DB.getInstance().getConnection();        
        String query = "Select IdTransaction from [Transaction] where IdOrder = ?";
        try(PreparedStatement ps = conn.prepareStatement(query);) {
            
            ps.setInt(1, idOrder);
            try(ResultSet rs = ps.executeQuery();){                
                if(rs.next()){                    
                    return rs.getInt(1);                    
                }                
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(sa190222_TransactionOperations.class.getName()).log(Level.SEVERE, null, ex);
        }                
        return -1;
    }

    @Override
    public int getTransactionForShopAndOrder(int idOrder, int idShop) {
        Connection conn = DB.getInstance().getConnection();        
        String query = "Select IdTransaction from [Transaction] where IdOrder = ? and IdShop = ?";
        try(PreparedStatement ps = conn.prepareStatement(query);) {
            
            ps.setInt(1, idOrder);
            ps.setInt(2, idShop);
            try(ResultSet rs = ps.executeQuery();){                
                if(rs.next()){                    
                    return rs.getInt(1);                    
                }                
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(sa190222_TransactionOperations.class.getName()).log(Level.SEVERE, null, ex);
        }                
        return -1;
    }

    @Override
    public List<Integer> getTransationsForShop(int idShop) {
        ArrayList<Integer> ret = new ArrayList<>();
        
        Connection conn = DB.getInstance().getConnection();        
        String query = "Select IdTransaction from [Transaction] where IdShop = ?";
        try(PreparedStatement ps = conn.prepareStatement(query);) {
            
            ps.setInt(1, idShop);
            try(ResultSet rs = ps.executeQuery();){                
                while(rs.next()){                    
                    ret.add(rs.getInt(1));
                }
                return ret;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(sa190222_TransactionOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }

    @Override
    public Calendar getTimeOfExecution(int idTransacction) {
        
        Connection conn = DB.getInstance().getConnection();
        
        String query = "Select ExecutionTime from [Transaction] where IdTransaction  = ?";
        try(PreparedStatement ps = conn.prepareStatement(query);){                        
            ps.setInt(1, idTransacction);
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
    public BigDecimal getAmmountThatBuyerPayedForOrder(int idOrder) {
        
        Connection conn = DB.getInstance().getConnection();        
        String query = "Select AmountPaid from [Transaction] where IdOrder = ?";
        try(PreparedStatement ps = conn.prepareStatement(query);){                        
            ps.setInt(1, idOrder);
            try(ResultSet rs = ps.executeQuery();){                
                if(rs.next()) return rs.getBigDecimal(1);                
            }            
        } catch (SQLException ex) {
            Logger.getLogger(sa190222_TransactionOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return new BigDecimal(-1);
    }

    @Override
    public BigDecimal getAmmountThatShopRecievedForOrder(int idShop, int idOrder) {
        
        Connection conn = DB.getInstance().getConnection();        
        String query = "Select (AmountPaid * (100 - SystemCut) / 100) from [Transaction] where IdShop = ? and IdOrder = ?";
        try(PreparedStatement ps = conn.prepareStatement(query);){
            
            ps.setInt(1, idShop);
            ps.setInt(2, idOrder);
            try(ResultSet rs = ps.executeQuery();){                
                if(rs.next()) return rs.getBigDecimal(1);                
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(sa190222_TransactionOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
                
        return new BigDecimal(-1);
    }

    @Override
    public BigDecimal getTransactionAmount(int idTransaction) {
        
        Connection conn = DB.getInstance().getConnection();        
        String query = "Select AmountPaid from [Transaction] where IdTransaction = ?";
        
        try(PreparedStatement ps = conn.prepareStatement(query);){
            
            ps.setInt(1, idTransaction);
            try(ResultSet rs = ps.executeQuery();){                
                if(rs.next()) return rs.getBigDecimal(1);                
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(sa190222_TransactionOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return new BigDecimal(-1);
    }

    @Override
    public BigDecimal getSystemProfit() {
        
        Connection conn = DB.getInstance().getConnection();        
        String query = "Select SUM(AmountPaid * SystemCut / 100) from [Transaction]";
        
        try(PreparedStatement ps = conn.prepareStatement(query);
                ResultSet rs = ps.executeQuery();) {            
            if(rs.next()) return rs.getBigDecimal(1);            
        } catch (SQLException ex) {
            Logger.getLogger(sa190222_TransactionOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return new BigDecimal(0);
    }
    
}
