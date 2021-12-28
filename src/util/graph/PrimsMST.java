package util.graph;

import java.util.*;

/**
 * Prim's minimum spanning tree algorithm
 * @author John Mansfield
 * Reference - CLRS, Tushar Roy https://github.com/mission-peace
 * @version 1.0
 */
public class PrimsMST implements GraphTransformation {

    /**
     * @see util.graph.GraphTransformation#transform(util.graph.Graph)
     */
    public Graph transform(Graph g) {

        BinaryMinHeap<Node> minHeap = new BinaryMinHeap<>();
        Map<Node, WeightedEdge> vertexToEdge = new HashMap<>();
        List<Edge> result = new ArrayList<>();
        Graph MST = new Graph();

        //init key for each node to max
        for (int i = 0; i < g.getNodeCount(); i++) {
            minHeap.add(Double.MAX_VALUE, g.getNode(i));
            g.getNode(i);
        }

        //select initial node randomly and init key to 0
        Node startNode = (Node) g.getNodes().iterator().next();
        minHeap.decrease(startNode, 0.0);

        //run until minHeap is empty
        while(!minHeap.empty()){
            //extract min node from heap and add edge (if present) to MST
            Node current =  minHeap.extractMin();
            Edge spanningTreeEdge = vertexToEdge.get(current);
            if(spanningTreeEdge != null){
                result.add(spanningTreeEdge);
            }

            //for each adjacent Node
            for(int i = 0; i < current.getEdgeCount(); i++){
                WeightedEdge edge= (WeightedEdge) current.getEdge(i);
                Node adjacent =  edge.getOther(current);  //getVertexForEdge(current,current.getEdge(i));
                //if adjacent node is in minHeap and existing weight > edge weight
                //decrease value of edge in minHeap and add to HashMap
                if (minHeap.containsData(adjacent) && minHeap.getWeight(adjacent) > edge.getWeight()){
                    minHeap.decrease(adjacent, edge.getWeight());
                    vertexToEdge.put(adjacent, edge);
                }
            }
        }
        //remove edges from g and return MST
        Set<Edge> edgeSet= g.getEdges();
        List<Edge> edgeList = new ArrayList(edgeSet);
        for (int i = 0; i < edgeList.size(); i++) {
            Edge edge = edgeList.get(i);
            Node A = edge.getA();
            Node B = edge.getB();
            if (!result.contains(edgeList.get(i))) {
                A.removeEdge(edge);
                B.removeEdge(edge);
            }
        }
        return g;
    }
}