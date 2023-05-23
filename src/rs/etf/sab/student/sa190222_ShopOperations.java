/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.etf.sab.student;

import java.sql.*;
import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.ShopOperations;

/**
 *
 * @author stoja
 */
public class sa190222_ShopOperations implements ShopOperations {

    @Override
    public int createShop(String name, String cityName) {
        int ret = -1;
        int cityId = 0;
        Connection conn = DB.getInstance().getConnection();
        
        String selectCityNameQuery = "Select * from City where Name = ?";
        try(PreparedStatement psCityName = conn.prepareStatement(selectCityNameQuery);) {
            psCityName.setString(1, cityName);
            
            try(ResultSet rsCityName = psCityName.executeQuery();){
                
                if(!rsCityName.next()){     // No city Found
                    return ret;
                } else {
                    cityId = rsCityName.getInt(1);
                }
                
            } catch (SQLException ex) {
                Logger.getLogger(sa190222_ShopOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(sa190222_ShopOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String ShopNameQuery = "Select * from Shop where Name = ?";
        try(PreparedStatement psShopName = conn.prepareStatement(ShopNameQuery);) {
            psShopName.setString(1, name);
            
            try(ResultSet rsShopName = psShopName.executeQuery();){
                
                if(rsShopName.next()){     // Shop with same name found
                    return ret;
                } 
                
            } catch (SQLException ex) {
                Logger.getLogger(sa190222_ShopOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(sa190222_ShopOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        String insertShopQuery = "Insert into SHOP(Name, Discount, IdCity) values (?, 0, ?)";
        try(PreparedStatement psInsert = conn.prepareStatement(insertShopQuery, Statement.RETURN_GENERATED_KEYS);) {
            
            psInsert.setString(1, name);
            psInsert.setInt(2, cityId);
            
            psInsert.executeUpdate();
            ResultSet rsInsert = psInsert.getGeneratedKeys();
            
            if(rsInsert.next()){
                ret = rsInsert.getInt(1);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(sa190222_ShopOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
                
        return ret;
    }

    @Override
    public int setCity(int i, String string) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int getCity(int i) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int setDiscount(int i, int i1) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int increaseArticleCount(int i, int i1) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int getArticleCount(int i) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Integer> getArticles(int i) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int getDiscount(int i) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    public static void main(String[] args) {
        int ret = new sa190222_ShopOperations().createShop("Rival", "Aleksinac");
        System.out.println(ret);
    }
}
