/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package rs.etf.sab.student;

import java.math.BigDecimal;
import java.sql.*;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
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
    public int completeOrder(int idOrder) {
        
        Connection conn = DB.getInstance().getConnection();        
        BigDecimal price = getFinalPrice(idOrder);

        String query = "Select * from Buyer where Wallet > ?"; 

        try(PreparedStatement ps = conn.prepareStatement(query);) {
            ps.setBigDecimal(1, price);                                   
            try(ResultSet rs = ps.executeQuery();){               
                if(!rs.next()){
                    return  -1;
                }                                
                
                int idBuyer = rs.getInt(1);                
                int idBuyerCity = rs.getInt(4);
                String updateBuyerQuery = "UPDATE Buyer SET Wallet = Wallet - ? where IdBuyer = ?";
                
                try(PreparedStatement ps1 = conn.prepareStatement(updateBuyerQuery);){                    
                    ps1.setBigDecimal(1, price);
                    ps1.setInt(2, idBuyer);                    
                    ps1.executeUpdate();                    
                }
                
                // DIJKSTRA OVDE ili koji vec algoritam budem koristio
                
                String cityQuery = "Select IdCity from City";
                PreparedStatement psCity = conn.prepareStatement(cityQuery);                
                ResultSet rsCity = psCity.executeQuery();
                
                Graph graph = new Graph();                
                while(rsCity.next()){                                        
                    graph.addNode(new Node(rsCity.getInt(1)));
                }
                                                
                String linesQuery = "Select IdCity1, IdCity2, Distance from Line";
                PreparedStatement psLines = conn.prepareStatement(linesQuery);
                
                ResultSet rsLines = psLines.executeQuery();
                
                while(rsLines.next()){                    
                    Node.connectNodes(Node.allNodes.get(rsLines.getInt(1)), Node.allNodes.get(rsLines.getInt(2)), rsLines.getInt(3));                    
                }
                
                graph = Dijkstra.calculateShortestPathFromSource(graph, Node.allNodes.get(idBuyerCity));
                
                HashSet<Node> nodes = (HashSet<Node>) graph.getNodes();
               
                String queryIdCityOrder = "Select IdCity\n" +
                "from Shop S join Article A on S.IdShop = A.IdShop join Item I on I.IdArticle = A.IdArticle\n" +
                "where IdOrder = ?";
                
                PreparedStatement psIdCityOrder = conn.prepareStatement(queryIdCityOrder);
                psIdCityOrder.setInt(1, idOrder);
                
                ResultSet rsqueryIdCityOrder = psIdCityOrder.executeQuery();
                
                int distance = Integer.MAX_VALUE;
                int cityIdNearestToBuyer = -1;
                
                while(rsqueryIdCityOrder.next()){                    
                    int temp = rsqueryIdCityOrder.getInt(1);                    
                    Node tempNode = Node.allNodes.get(temp);                    
                    if(tempNode.getDistance() < distance){                        
                        distance = tempNode.getDistance();
                        cityIdNearestToBuyer = tempNode.getName();
                    }
                }

                Node neareastCityNode = Node.allNodes.get(cityIdNearestToBuyer);                    
                
                Node.allNodes.clear();
                graph = new Graph();
                rsCity = psCity.executeQuery();
                                   
                while(rsCity.next()){                                        
                    graph.addNode(new Node(rsCity.getInt(1)));
                }
                
                rsLines = psLines.executeQuery();                                    
                while(rsLines.next()){                    
                    Node.connectNodes(Node.allNodes.get(rsLines.getInt(1)), Node.allNodes.get(rsLines.getInt(2)), rsLines.getInt(3));                    
                }
                
                
                 graph = Dijkstra.calculateShortestPathFromSource(graph, Node.allNodes.get(cityIdNearestToBuyer));
                
                queryIdCityOrder = "Select IdCity\n" +
                "from Shop S join Article A on S.IdShop = A.IdShop join Item I on I.IdArticle = A.IdArticle\n" +
                "where IdOrder = ? and S.IdShop != ?";
                
                psIdCityOrder = conn.prepareStatement(queryIdCityOrder);
                psIdCityOrder.setInt(1, idOrder);
                psIdCityOrder.setInt(2, cityIdNearestToBuyer);
                
                rsqueryIdCityOrder = psIdCityOrder.executeQuery();
                
                int distance1 = -1;
                
                while(rsqueryIdCityOrder.next()){                    
                    int temp = rsqueryIdCityOrder.getInt(1);                    
                    Node tempNode = Node.allNodes.get(temp);                    
                    if(tempNode.getDistance() > distance1){                        
                        distance1 = tempNode.getDistance();                        
                    }
                }
                
                Calendar cal = new sa190222_GeneralOperations().getCurrentTime();                                                
                java.util.Date utilDate = cal.getTime();
                java.sql.Date sqlDate = new java.sql.Date(utilDate.getTime());
                
                int startDate = distance + distance1; 
                
                for(Node node: neareastCityNode.getShortestPath()){
                    
                    String insertTrackingQuery = "Insert into Tracking(IdOrder, StartDate, IdCity) values (?, Dateadd(DAY, ?, ?), ?)";
                    
                    PreparedStatement psTracking = conn.prepareStatement(insertTrackingQuery);
                    psTracking.setInt(1, idOrder);
                    psTracking.setInt(2,startDate);
                    psTracking.setDate(3, sqlDate);
                    psTracking.setInt(4,node.getName());
                    psTracking.executeUpdate();
                    
                    startDate -= node.getDistance();
                }
                
                String updateOrderQuery = "UPDATE [Order] set status = 'sent', TravelTime = ?, SentTime = ?, CurrentCity = ? where IdOrder = ?";
                try(PreparedStatement ps2 = conn.prepareStatement(updateOrderQuery);){                                                            
                    ps2.setInt(1, distance + distance1);
                    ps2.setDate(2, sqlDate);
                    ps2.setInt(3, cityIdNearestToBuyer);
                    ps2.setInt(4, idOrder);                    
                    ps2.executeUpdate();                    
                }                
                
                return 1;
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(sa190222_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        return -1;
    }

    @Override
    public BigDecimal getFinalPrice(int idOrder) {
        Connection conn = DB.getInstance().getConnection();
        
        String query = "EXEC dbo.SP_FINAL_PRICE ?";        
        try(PreparedStatement ps = conn.prepareCall(query);) {
            
            ps.setInt(1, idOrder);
            try(ResultSet rs =ps.executeQuery();){
                if(rs.next()) return rs.getBigDecimal(1);                
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(sa190222_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return new BigDecimal(-1);
    }

    @Override
    public BigDecimal getDiscountSum(int idOrder) {
        
        Connection conn = DB.getInstance().getConnection();
        
        String query = "Select Sum(I.[Count] * A.Price) - Sum(I.[Count] * A.Price * (100 - S.Discount) / 100)\n" +
        "from [Order] O join Item I on O.IdOrder = I.IdOrder join Article A on A.IdArticle = I.IdArticle join Shop S on S.IdShop = A.IdShop\n" +
        "where O.IdOrder = ?";
        
        try(PreparedStatement ps = conn.prepareStatement(query);) {            
            
            ps.setInt(1, idOrder);                        
            try(ResultSet rs = ps.executeQuery();){
                if(rs.next()) return rs.getBigDecimal(1);            
            }
            
        } catch (SQLException ex) {
            Logger.getLogger(sa190222_OrderOperations.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        return new BigDecimal(-1);
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
                    if(rs.getDate(1) == null) return null;
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
                    if(rs.getDate(1) == null) return null;
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
        
//        int ret = new sa190222_OrderOperations().addArticle(1, 1, 10);
//        ret = new sa190222_OrderOperations().addArticle(1, 2, 20);
//        System.out.println(ret);
//        
//        int retInt = new sa190222_OrderOperations().getLocation(1);  
////        retInt = new sa190222_OrderOperations().removeArticle(1, 2); 
//
//        List<Integer> listRet = new sa190222_OrderOperations().getItems(1);
//        System.out.println(retInt);
//        
//        Calendar cal = new sa190222_OrderOperations().getSentTime(1);
//        System.out.println(cal);
        
        int completeOrder = new sa190222_OrderOperations().completeOrder(1);
        System.out.println(completeOrder);
        
        
    }
    
}
