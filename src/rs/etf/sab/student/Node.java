package rs.etf.sab.student; 

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Node {

    private int name;
    private List<Node> shortestPath = new LinkedList<>();
    private Integer distance = Integer.MAX_VALUE;
    Map<Node, Integer> adjacentNodes = new HashMap<>();

    public static Map<Integer, Node> allNodes = new HashMap<>();
    
    public void addDestination(Node destination, int distance) {
        adjacentNodes.put(destination, distance);
    }

    public Node(int name) {
        this.name = name;
        allNodes.put(name, this);
    }

    public int getName() {
        return name;
    }

    public void setName(int name) {
        this.name = name;
    }

    public List<Node> getShortestPath() {
        return shortestPath;
    }

    public void setShortestPath(List<Node> shortestPath) {
        this.shortestPath = shortestPath;
    }

    public Integer getDistance() {
        return distance;
    }

    public void setDistance(Integer distance) {
        this.distance = distance;
    }

    public Map<Node, Integer> getAdjacentNodes() {
        return adjacentNodes;
    }

    public void setAdjacentNodes(Map<Node, Integer> adjacentNodes) {
        this.adjacentNodes = adjacentNodes;
    }
    
    public static void connectNodes(Node node1, Node node2, int distance){        
        node1.addDestination(node2, distance);
        node2.addDestination(node1, distance);        
    }
}