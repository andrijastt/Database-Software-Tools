/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.etf.sab.student;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.BuyerOperations;

/**
 *
 * @author stoja
 */
public class sa190222_BuyerOperations implements BuyerOperations {

    @Override
    public int createBuyer(String name, int idCity) {
        
        Connection conn = DB.getInstance().getConnection();
        
        String selectCityQuery = "Select * from City where IdCity = ?";
        try(PreparedStatement psCitySelect = conn.prepareStatement(selectCityQuery);) {
            
            psCitySelect.setInt(1, idCity);
            try(ResultSet rsCity = psCitySelect.executeQuery();){
                
                if(!rsCity.next()){     // city doesn't exist
                    return -1;
                }
                
            } catch (SQLException ex) {
                Logger.getLogger(sa190222_BuyerOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(sa190222_BuyerOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String query = "Insert into Buyer(Name, Wallet, IdCity) values (?, 0, ?)";
        
        try(PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);) {
            
            ps.setString(1, name);
            ps.setInt(2, idCity);
            
            ps.executeUpdate();
            try(ResultSet rs = ps.getGeneratedKeys()){
                
                if(rs.next()){
                    return rs.getInt(1);
                }
                
            } catch (SQLException ex) {
                Logger.getLogger(sa190222_BuyerOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(sa190222_BuyerOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return -1;
    }

    @Override
    public int setCity(int idBuyer, int idCity) {
        
        Connection conn = DB.getInstance().getConnection();
        
        String selectCityQuery = "Select * from City where IdCity = ?";
        try(PreparedStatement psCitySelect = conn.prepareStatement(selectCityQuery);) {
            
            psCitySelect.setInt(1, idCity);
            try(ResultSet rsCity = psCitySelect.executeQuery();){
                
                if(!rsCity.next()){     // city doesn't exist
                    return -1;
                }
                
            } catch (SQLException ex) {
                Logger.getLogger(sa190222_BuyerOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(sa190222_BuyerOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String updateQuery = "Update Buyer set IdCity = ? where IdBuyer = ?";
        try(PreparedStatement psUpdate = conn.prepareStatement(updateQuery);) {
            
            psUpdate.setInt(1, idCity);
            psUpdate.setInt(2, idBuyer);
            
            if(psUpdate.executeUpdate() == 1) return 1;
            
        } catch (SQLException ex) {
            Logger.getLogger(sa190222_BuyerOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return -1;        
    }

    @Override
    public int getCity(int idBuyer) {
        
        Connection conn = DB.getInstance().getConnection();

        String query = "Select IdCity from Buyer where IdBuyer = ?";
        try(PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);) {
            
            ps.setInt(1, idBuyer);                        
            try(ResultSet rs =ps.executeQuery();){
         
                if(rs.next()){
                    return rs.getInt(1);
                }
                
            } catch (SQLException ex) {
                Logger.getLogger(sa190222_BuyerOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(sa190222_BuyerOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return -1;
        
    }

    @Override
    public BigDecimal increaseCredit(int buyerId, BigDecimal wallet) {
        
        Connection conn = DB.getInstance().getConnection();
        
        String query = "UPDATE Buyer SET Wallet = Wallet + ? where IdBuyer = ?";
        try(PreparedStatement ps = conn.prepareStatement(query);) {            
            
            ps.setBigDecimal(1, wallet);
            ps.setInt(2, buyerId);            
            
            if(ps.executeUpdate() == 1){                                
                
                String selectQuery = "SELECT Wallet from Buyer where IdBuyer = ?";                
                try(PreparedStatement psSelect = conn.prepareStatement(selectQuery);){         
                    
                    psSelect.setInt(1, buyerId);                                        
                    try(ResultSet rs = psSelect.executeQuery();){                                                
                        if(rs.next()){
                            return rs.getBigDecimal(1);
                        }                                                        
                    } catch (SQLException ex) {
                        Logger.getLogger(sa190222_BuyerOperations.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                } catch (SQLException ex) {
                    Logger.getLogger(sa190222_BuyerOperations.class.getName()).log(Level.SEVERE, null, ex);
                }                
            }            
        } catch (SQLException ex) {
            Logger.getLogger(sa190222_BuyerOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
                
        return new BigDecimal(0);
    }

    @Override
    public int createOrder(int IdBuyer) {
        
        Connection conn = DB.getInstance().getConnection();
        
        String selectQuery = "Select * from Buyer where IdBuyer = ?";
        try(PreparedStatement psSelect = conn.prepareStatement(selectQuery);) {            
            
            psSelect.setInt(1, IdBuyer);
            try(ResultSet rsSelect = psSelect.executeQuery();){                
                if(!rsSelect.next()){
                    return -1;
                }                
            } catch (SQLException ex) {
                Logger.getLogger(sa190222_BuyerOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(sa190222_BuyerOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String query = "Insert into [Order] (IdBuyer, Status) values (?, 'created')";
        try(PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS);) {
            
            ps.setInt(1, IdBuyer);
            ps.executeUpdate();
            
            try(ResultSet rs = ps.getGeneratedKeys()){
                if(rs.next()){
                    return rs.getInt(1);                    
                }
            }            
        } catch (SQLException ex) {
            Logger.getLogger(sa190222_BuyerOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return -1;
    }

    @Override
    public List<Integer> getOrders(int buyerId) {
        ArrayList<Integer> ret = new ArrayList<>();
        
        Connection conn = DB.getInstance().getConnection();
        
        String query = "Select IdOrder from [Order] where IdBuyer = ?";
        try(PreparedStatement ps = conn.prepareStatement(query);) {
            
            ps.setInt(1, buyerId);
            try(ResultSet rs = ps.executeQuery();){
                
                while(rs.next()){
                    ret.add(rs.getInt(1));
                }
                
            } catch (SQLException ex) {
                Logger.getLogger(sa190222_BuyerOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(sa190222_BuyerOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return ret;
    }

    @Override
    public BigDecimal getCredit(int buyerId) {
       Connection conn = DB.getInstance().getConnection();
       
       String selectQuery = "SELECT Wallet from Buyer where IdBuyer = ?";                
        try(PreparedStatement psSelect = conn.prepareStatement(selectQuery);){         

            psSelect.setInt(1, buyerId);                                        
            try(ResultSet rs = psSelect.executeQuery();){                                                
                if(rs.next()){
                    return rs.getBigDecimal(1);
                }                                                        
            } catch (SQLException ex) {
                Logger.getLogger(sa190222_BuyerOperations.class.getName()).log(Level.SEVERE, null, ex);
            }

        } catch (SQLException ex) {
            Logger.getLogger(sa190222_BuyerOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        return new BigDecimal(-1);
    }
    
    public static void main(String[] args) {
        
        int ret = new sa190222_BuyerOperations().createBuyer("Milos", 1);
        ret = new sa190222_BuyerOperations().createOrder(1);
        BigDecimal retB = new sa190222_BuyerOperations().increaseCredit(1, new BigDecimal(5000));
                    
            List<Integer> list = new sa190222_BuyerOperations().getOrders(1);
            
            System.out.println("rs.etf.sab.student.sa190222_BuyerOperations.main()" + list);
        
//        System.out.println("rs.etf.sab.student.sa190222_BuyerOperations.main() " + ret);
    }
    
}
