package rs.etf.sab.student;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.Map;
import java.util.Set;

// Press Shift twice to open the Search Everywhere dialog and type `show whitespaces`,
// then press Enter. You can now see whitespace characters in your code.
public class Dijkstra {

    public static Graph calculateShortestPathFromSource(Graph graph, Node source) {
        source.setDistance(0);

        Set<Node> settledNodes = new HashSet<>();
        Set<Node> unsettledNodes = new HashSet<>();

        unsettledNodes.add(source);

        while (unsettledNodes.size() != 0) {
            Node currentNode = getLowestDistanceNode(unsettledNodes);
            unsettledNodes.remove(currentNode);
            for (Map.Entry< Node, Integer> adjacencyPair:
                    currentNode.getAdjacentNodes().entrySet()) {
                Node adjacentNode = adjacencyPair.getKey();
                Integer edgeWeight = adjacencyPair.getValue();
                if (!settledNodes.contains(adjacentNode)) {
                    calculateMinimumDistance(adjacentNode, edgeWeight, currentNode);
                    unsettledNodes.add(adjacentNode);
                }
            }
            settledNodes.add(currentNode);
        }
        return graph;
    }

    private static Node getLowestDistanceNode(Set < Node > unsettledNodes) {
        Node lowestDistanceNode = null;
        int lowestDistance = Integer.MAX_VALUE;
        for (Node node: unsettledNodes) {
            int nodeDistance = node.getDistance();
            if (nodeDistance < lowestDistance) {
                lowestDistance = nodeDistance;
                lowestDistanceNode = node;
            }
        }
        return lowestDistanceNode;
    }

    private static void calculateMinimumDistance(Node evaluationNode,
                                                 Integer edgeWeigh, Node sourceNode) {
        Integer sourceDistance = sourceNode.getDistance();
        if (sourceDistance + edgeWeigh < evaluationNode.getDistance()) {
            evaluationNode.setDistance(sourceDistance + edgeWeigh);
            LinkedList<Node> shortestPath = new LinkedList<>(sourceNode.getShortestPath());
            shortestPath.add(sourceNode);
            evaluationNode.setShortestPath(shortestPath);
        }
    }
    
    public static void main(String[] args) {
        
        Node nodeA = new Node(1);
        Node nodeB = new Node(2);
        Node nodeC1 = new Node(3);
        Node nodeC2 = new Node(4); 
        Node nodeC3 = new Node(5);
        Node nodeC4 = new Node(6);
        Node nodeC5 = new Node(7);

        Node.connectNodes(nodeA, nodeC1, 10);
        Node.connectNodes(nodeA, nodeC5, 15);
        Node.connectNodes(nodeA, nodeC2, 3);
        Node.connectNodes(nodeA, nodeC4, 3);
        Node.connectNodes(nodeB, nodeC1, 8);
        Node.connectNodes(nodeB, nodeC5, 2);
        Node.connectNodes(nodeC3, nodeC2, 1);
        Node.connectNodes(nodeC3, nodeC4, 1);
        

        Graph graph = new Graph();

        graph.addNode(nodeA);
        graph.addNode(nodeB);
        graph.addNode(nodeC1);
        graph.addNode(nodeC2);
        graph.addNode(nodeC3);
        graph.addNode(nodeC4);
        graph.addNode(nodeC5);

        graph = Dijkstra.calculateShortestPathFromSource(graph, nodeA);

        System.out.println("rs.etf.sab.student.Dijkstra.main()");
    }
    
}