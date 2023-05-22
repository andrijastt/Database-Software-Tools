/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.etf.sab.student;

import java.sql.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import rs.etf.sab.operations.ArticleOperations;

/**
 *
 * @author stoja
 */
public class sa190222_ArticleOperations implements ArticleOperations {

    @Override
    public int createArticle(int shopId, String articleName, int articlePrice) {
        int ret = -1;
        Connection conn = DB.getInstance().getConnection();
        
        String queryShop = "Select * from Shop where IdShop = ?";
        try (PreparedStatement psStore = conn.prepareStatement(queryShop);){            
            psStore.setInt(1, shopId);
            try(ResultSet rsShop = psStore.executeQuery()){
                
                if(!rsShop.next()){
                    return ret;
                }                
            }            
        } catch (SQLException ex) {
            Logger.getLogger(sa190222_ArticleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        String query = "Select * from Article where IdShop = ? and Name = ?";               
        try (PreparedStatement ps = conn.prepareStatement(query);){
            
            ps.setInt(1, shopId);
            ps.setString(2, articleName);
            
            try(ResultSet rs = ps.executeQuery()){
                
                if(!rs.next()){
                    String queryInsert = "Insert into Article(Name, Price, Count, IdShop) values(?, ?, 0, ?)";                    
                    try (PreparedStatement psInsert = conn.prepareStatement(queryInsert, Statement.RETURN_GENERATED_KEYS);){
                        
                        psInsert.setString(1, articleName);
                        psInsert.setInt(2, articlePrice);
                        psInsert.setInt(3, shopId);
                        
                        try(ResultSet rsInsert = psInsert.executeQuery()){
                            
                            if(rsInsert.next()){
                                ret = rsInsert.getInt(1);
                            }
                            
                        } catch (SQLException ex) {
                            Logger.getLogger(sa190222_ArticleOperations.class.getName()).log(Level.SEVERE, null, ex);
                        }
                        
                    } catch (SQLException ex) {
                        Logger.getLogger(sa190222_ArticleOperations.class.getName()).log(Level.SEVERE, null, ex);
                    }
                }
                
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(sa190222_ArticleOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
       return ret;
    }
    
    public static void main(String[] args) {
        
        int ret = new sa190222_ArticleOperations().createArticle(1, "aaa", 100);
        System.out.println("rs.etf.sab.student.sa190222_ArticleOperations.main() " + ret);
        
    }
    
}
