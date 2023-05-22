/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.etf.sab.student;

import java.sql.*;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.CityOperations;

/**
 *
 * @author stoja
 */
public class sa190222_CityOperations implements CityOperations {

    /**
     * 
     * @param string
     * @return 
     */
    @Override
    public int createCity(String string) {
                
        Connection conn = DB.getInstance().getConnection();
        int ret = -1;
        String query = "Select * from City where Name = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(query);){         
            ps.setString(1, string);            
            try (ResultSet rs = ps.executeQuery();){
                
                if(!rs.next()){
                    
                    String insertSQL = "insert into City(Name) values(?)";
                    try (PreparedStatement psInsert = conn.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);){
                        psInsert.setString(1, string);            
                        psInsert.executeUpdate();
                        
                        try (ResultSet rsInsert = psInsert.getGeneratedKeys()){
                             if(rsInsert.next()){
                                 ret = rsInsert.getInt(1);
                             }  
                            
                        } catch (SQLException ex) {
                            Logger.getLogger(sa190222_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                    } catch (SQLException ex) {
                        Logger.getLogger(sa190222_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
                    }
                    
                }
                
            } catch (SQLException ex) {
                Logger.getLogger(sa190222_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
            
            
        } catch (SQLException ex) {
            Logger.getLogger(sa190222_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        return ret;        
    }

    @Override
    public List<Integer> getCities() {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public int connectCities(int i, int i1, int i2) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Integer> getConnectedCities(int i) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }

    @Override
    public List<Integer> getShops(int i) {
        throw new UnsupportedOperationException("Not supported yet."); // Generated from nbfs://nbhost/SystemFileSystem/Templates/Classes/Code/GeneratedMethodBody
    }
    
    public static void main(String[] args) {
        int ret = new sa190222_CityOperations().createCity("Beograd");
        System.out.println(ret);
    }
    
}
