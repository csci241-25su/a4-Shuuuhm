package graph;

import heap.Heap;
import java.util.Map;
import java.util.HashMap;
import java.util.LinkedList;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.HashSet;
import java.util.Stack;

//Name: Shayla Neitzel
//Date: 7/27/25
//Purpose: Implement Dijkstra's algorithm - A4


/** Provides an implementation of Dijkstra's single-source shortest paths
 * algorithm.
 * Sample usage:
 *   Graph g = // create your graph
 *   ShortestPaths sp = new ShortestPaths();
 *   Node a = g.getNode("A");
 *   sp.compute(a);
 *   Node b = g.getNode("B");
 *   LinkedList<Node> abPath = sp.getShortestPath(b);
 *   double abPathLength = sp.getShortestPathLength(b);
 *   */
public class ShortestPaths {
    // stores auxiliary data associated with each node for the shortest
    // paths computation:
    private HashMap<Node,PathData> paths;

    /** Compute the shortest path to all nodes from origin using Dijkstra's
     * algorithm. Fill in the paths field, which associates each Node with its
     * PathData record, storing total distance from the source, and the
     * backpointer to the previous node on the shortest path.
     * Precondition: origin is a node in the Graph.*/
    public void compute(Node origin) {
        paths = new HashMap<Node,PathData>();
        Heap<Node, Double> frontier = new Heap<Node, Double>();
        HashSet<Node> settledNodes = new HashSet<Node>(); 

        frontier.add(origin,0.0);
        paths.put(origin, new PathData(0, null)); //(resetting path to origin node, which is nothing) origin node is node with no parents
        while (frontier.size() > 0){
            Node closestNode = (Node)frontier.poll();
            settledNodes.add(closestNode);
        
        for (Map.Entry<Node, Double> neighbor : closestNode.getNeighbors().entrySet()){
            Node thisNeighbor = neighbor.getKey();
            Double distance = neighbor.getValue();
            if (settledNodes.contains(thisNeighbor)){
                continue;
            }
            PathData pathToParentNode = paths.get(closestNode);
            PathData currPathToNeighbor = paths.get(thisNeighbor);

            if(frontier.contains(thisNeighbor) == false){
                frontier.add(thisNeighbor, distance + pathToParentNode.distance);
                paths.put(thisNeighbor, new PathData(distance + pathToParentNode.distance, closestNode));
            }else{
                if (pathToParentNode.distance + distance < currPathToNeighbor.distance);
                frontier.changePriority(thisNeighbor, distance + pathToParentNode.distance);
                paths.put(thisNeighbor, new PathData(pathToParentNode.distance + distance, closestNode));
            }
        }

        }

        // TODO 1: implement Dijkstra's algorithm to fill paths with
        // shortest-path data for each Node reachable from origin.

    }

    /** Returns the length of the shortest path from the origin to destination.
     * If no path exists, return Double.POSITIVE_INFINITY.
     * Precondition: destination is a node in the graph, and compute(origin)
     * has been called. */
    public double shortestPathLength(Node destination) {
        if (paths.get(destination) == null){
            return Double.POSITIVE_INFINITY;
        }
        return paths.get(destination).distance;
        // TODO 2 - implement this method to fetch the shortest path length
        // from the paths data computed by Dijkstra's algorithm.
        // throw new UnsupportedOperationException();
    }

    /** Returns a LinkedList of the nodes along the shortest path from origin
     * to destination. This path includes the origin and destination. If origin
     * and destination are the same node, it is included only once.
     * If no path to it exists, return null.
     * Precondition: destination is a node in the graph, and compute(origin)
     * has been called. */
    public LinkedList<Node> shortestPath(Node destination) {
        // TODO 3 - implement this method to reconstruct sequence of Nodes
        // along the shortest path from the origin to destination using the
        // paths data computed by Dijkstra's algorithm.
        LinkedList<Node> shortestPathList = new LinkedList<Node>();
        Stack<Node> reverseOrder = new Stack<Node>();
        // add to stack then pop them off into list
        if (paths.get(destination) == null){
            return null;
        }
        Node placeHolder = destination;
        while (placeHolder != null){
            reverseOrder.add(placeHolder);
            placeHolder = paths.get(placeHolder).previous;
        }
        while (reverseOrder.size() > 0){
            shortestPathList.add(reverseOrder.pop());
        }
        return shortestPathList;
        // throw new UnsupportedOperationException();
    }


    /** Inner class representing data used by Dijkstra's algorithm in the
     * process of computing shortest paths from a given source node. */
    class PathData {
        double distance; // distance of the shortest path from source
        Node previous; // previous node in the path from the source

        /** constructor: initialize distance and previous node */
        public PathData(double dist, Node prev) {
            distance = dist;
            previous = prev;
        }
        @Override //instead of outputting memory location, outputs what it is
        public String toString(){
            return "Distance " + this.distance + " previousNode " + this.previous;
        }
    }


    /** Static helper method to open and parse a file containing graph
     * information. Can parse either a basic file or a DB1B CSV file with
     * flight data. See GraphParser, BasicParser, and DB1BParser for more.*/
    protected static Graph parseGraph(String fileType, String fileName) throws
        FileNotFoundException {
        // create an appropriate parser for the given file type
        GraphParser parser;
        if (fileType.equals("basic")) {
            parser = new BasicParser();
        } else if (fileType.equals("db1b")) {
            parser = new DB1BParser();
        } else {
            throw new IllegalArgumentException(
                    "Unsupported file type: " + fileType);
        }

        // open the given file
        parser.open(new File(fileName));

        // parse the file and return the graph
        return parser.parse();
    }

    public static void main(String[] args) {
      // read command line args
      String fileType = args[0];
      String fileName = args[1];
      String origCode = args[2];

      String destCode = null;
      if (args.length == 4) {
          destCode = args[3];
      }

      // parse a graph with the given type and filename
      Graph graph;
      try {
          graph = parseGraph(fileType, fileName);
      } catch (FileNotFoundException e) {
          System.out.println("Could not open file " + fileName);
          return;
      }
      graph.report();


      // TODO 4: create a ShortestPaths object, use it to compute shortest
      // paths data from the origin node given by origCode.

      // TODO 5:
      // If destCode was not given, print each reachable node followed by the
      // length of the shortest path to it from the origin.

      // TODO 6:
      // If destCode was given, print the nodes in the path from
      // origCode to destCode, followed by the total path length
      // If no path exists, print a message saying so.
      
      ShortestPaths sp = new ShortestPaths();
      sp.compute(graph.getNode(args[2]));
      System.out.println(sp.paths);
      if (args.length < 4){ 
        sp.shortestPath(null);
        System.out.println("ShortestPaths:" + sp.shortestPath(graph.getNode(args[2])));
        for (Map.Entry<Node, PathData> entry : sp.paths.entrySet()){
            System.out.println("Reachable Nodes: " + sp.shortestPath((entry.getKey())));
            System.out.println("Shortestpathlength: " + sp.shortestPathLength(entry.getKey()));
        }
    }else{
        sp.shortestPath(graph.getNode(args[3]));
        System.out.println("shortestPath: " + sp.shortestPath(graph.getNode(args[3])));
        System.out.println("ShortestPathLength: " + sp.shortestPathLength(graph.getNode(args[3])));
        if (sp.shortestPath(graph.getNode(args[3])) == null){
            System.out.println("There is no path");
        }
    }
    }
}
