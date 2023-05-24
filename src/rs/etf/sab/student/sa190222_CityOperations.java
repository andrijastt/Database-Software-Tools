/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.etf.sab.student;

import java.sql.*;
import java.util.ArrayList;
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
     * @param name  - the name of the city. Name of the cities must be unique.
     * @return city id, or -1 on failure
     */
    @Override
    public int createCity(String name) {
                
        Connection conn = DB.getInstance().getConnection();
        int ret = -1;
        String query = "Select * from City where Name = ?";
        
        try (PreparedStatement ps = conn.prepareStatement(query);){         
            ps.setString(1, name);            
            try (ResultSet rs = ps.executeQuery();){
                
                if(!rs.next()){
                    
                    String insertSQL = "insert into City(Name) values(?)";
                    try (PreparedStatement psInsert = conn.prepareStatement(insertSQL, Statement.RETURN_GENERATED_KEYS);){
                        psInsert.setString(1, name);            
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
        
        ArrayList<Integer> ret = new ArrayList<>();
        Connection conn = DB.getInstance().getConnection();
        
        String query = "Select IdCity from City";
        try {
            Statement stmt = conn.createStatement();
            stmt.execute(query);
            
            ResultSet rs = stmt.getResultSet();
            while(rs.next()){
                ret.add(rs.getInt(1));
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(sa190222_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return ret;        
    }

    @Override
    public int connectCities(int cityId1, int cityId2, int distance) {
        Connection conn = DB.getInstance().getConnection();
        int ret = -1;
        int count = 0;        
        
        String selectQuery = "Select * from City where IdCity =  ? or IdCity = ?";
        try (PreparedStatement psSelect = conn.prepareStatement(selectQuery);){
            
            psSelect.setInt(1, cityId1);
            psSelect.setInt(2, cityId2);
            
            try(ResultSet rsSelect = psSelect.executeQuery()){
                
                while(rsSelect.next()){
                    count++;
                }                
            } catch (SQLException ex) {
                Logger.getLogger(sa190222_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
                return ret;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(sa190222_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            return ret;
        }
        
        if(count == 2){

            String query = "Select * from Line where (IdCity1 = ? and IdCity2 = ?) or (IdCity2 = ? and IdCity1 = ?)";                       
            try (PreparedStatement ps = conn.prepareStatement(query);){

                ps.setInt(1, cityId1);
                ps.setInt(2, cityId2);
                ps.setInt(3, cityId1);
                ps.setInt(4, cityId2);
                try(ResultSet rs = ps.executeQuery()){

                    if(!rs.next()){
                        String insertQuery = "Insert into Line(IdCity1, IdCity2, Distance) values(?, ?, ?)";
                        try (PreparedStatement psInsert = conn.prepareStatement(insertQuery, Statement.RETURN_GENERATED_KEYS);){
                            
                            psInsert.setInt(1, cityId1);
                            psInsert.setInt(2, cityId2);
                            psInsert.setInt(3, distance);                            
                            psInsert.executeUpdate();
                             try(ResultSet rsInsert = psInsert.getGeneratedKeys()){
                                 
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
        }
        
        return ret;
    }

    @Override
    public List<Integer> getConnectedCities(int i) {
        ArrayList<Integer> ret = new ArrayList<>();
        Connection conn = DB.getInstance().getConnection();
        
        String query = "Select IdLine from Line where IdCity1 = ? or IdCity2 = ?";
        try(PreparedStatement ps = conn.prepareStatement(query, Statement.RETURN_GENERATED_KEYS)) {
            
            ps.setInt(1, i);
            ps.setInt(2, i);
            
            try(ResultSet rs = ps.executeQuery()){
                while(rs.next()){
                    ret.add(rs.getInt(1));
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
    public List<Integer> getShops(int idCity) {
                
        Connection conn = DB.getInstance().getConnection();
        
        String query = "Select IdShop from Shop where IdCity = ?";
        try(PreparedStatement ps = conn.prepareStatement(query);) {
            
            ps.setInt(1, idCity);
            try(ResultSet rs = ps.executeQuery();){
                ArrayList<Integer> ret = new ArrayList<>();
                while(rs.next()){
                    ret.add(rs.getInt(1));
                }
                return ret;
            } catch (SQLException ex) {
                Logger.getLogger(sa190222_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(sa190222_CityOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return null;
    }
    
    public static void main(String[] args) {
        
        int ret = new sa190222_CityOperations().createCity("Aleksinac");  
        ret = new sa190222_CityOperations().createCity("Beograd");  
        ret = new sa190222_CityOperations().createCity("Novi Sad");  
        ret = new sa190222_CityOperations().connectCities(1, 2, 4);
        System.out.println(ret);                
        
    }
    
}
