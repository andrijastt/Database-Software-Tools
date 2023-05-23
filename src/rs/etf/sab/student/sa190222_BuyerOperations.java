/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.etf.sab.student;

import java.math.BigDecimal;
import java.sql.*;
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
    public int setCity(int i, int i1) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int getCity(int i) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public BigDecimal increaseCredit(int i, BigDecimal bd) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int createOrder(int i) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Integer> getOrders(int i) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public BigDecimal getCredit(int i) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    public static void main(String[] args) {
        
        int ret = new sa190222_BuyerOperations().createBuyer("Milos Cvetanovic", 2);
        System.out.println("rs.etf.sab.student.sa190222_BuyerOperations.main() " + ret);
    }
    
}
