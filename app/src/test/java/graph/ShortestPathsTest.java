package graph;

import static org.junit.Assert.*;
import org.junit.FixMethodOrder;

import org.junit.Test;
import org.junit.runners.MethodSorters;

import java.net.URL;
import java.io.FileNotFoundException;

import java.util.LinkedList;
// Shayla Neitzel, 7/27/25, Testing ShortestPaths

@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class ShortestPathsTest {

    /* Performs the necessary gradle-related incantation to get the
       filename of a graph text file in the src/test/resources directory at
       test time.*/
    private String getGraphResource(String fileName) {
        ClassLoader classLoader = getClass().getClassLoader();
        URL resource = classLoader.getResource(fileName);
        return resource.getPath();
    }

    /* Returns the Graph loaded from the file with filename fn located in
     * src/test/resources at test time. */
    private Graph loadBasicGraph(String fn) {
        Graph result = null;
        String filePath = getGraphResource(fn);
        try {
          result = ShortestPaths.parseGraph("basic", filePath);
        } catch (FileNotFoundException e) {
          fail("Could not find graph " + fn);
        }
        return result;
    }

    /** Dummy test case demonstrating syntax to create a graph from scratch.
     * Write your own tests below. */
    @Test
    public void test00Nothing() {
        Graph g = new Graph();
        Node a = g.getNode("A");
        Node b = g.getNode("B");
        g.addEdge(a, b, 1);

        // sample assertion statements:
        assertTrue(true);
        assertEquals(2+2, 4);
    }

    /** Minimal test case to check the path from A to B in Simple0.txt */
    @Test
    public void test01Simple0() {
        Graph g = loadBasicGraph("Simple0.txt");
        g.report();
        ShortestPaths sp = new ShortestPaths();
        Node a = g.getNode("A");
        sp.compute(a);
        Node b = g.getNode("B");
        LinkedList<Node> abPath = sp.shortestPath(b);
        assertEquals(abPath.size(), 2);
        assertEquals(abPath.getFirst(), a);
        assertEquals(abPath.getLast(),  b);
        assertEquals(sp.shortestPathLength(b), 1.0, 1e-6);
    }

    @Test
    public void test02Simple1() {
        Graph g = loadBasicGraph("Simple1.txt");
        g.report();
        ShortestPaths sp = new ShortestPaths();
        Node a = g.getNode("A");
        sp.compute(a);
        Node b = g.getNode("B");
        LinkedList<Node> abPath = sp.shortestPath(b);
        assertEquals(abPath.size(), 2);
        assertEquals(abPath.getFirst(), a);
        assertEquals(abPath.getLast(), b);
        assertEquals(sp.shortestPathLength(b), 1, 1e-6);
    }

    //Testing compute function when given only one node
    @Test
    public void computeWithOneNode(){
        Graph g = new Graph();
        ShortestPaths sp = new ShortestPaths();
        Node a = g.getNode("A");
        g.addEdge(a, a, 0);
        sp.compute(a);
        g.report();
        LinkedList<Node> abPath = sp.shortestPath(a);
        assertEquals(abPath.size(), 1);
        assertEquals(abPath.getFirst(), a);
        assertEquals(abPath.getLast(), a);
        assertEquals(sp.shortestPathLength(a), 0, 1e-6);
    }

    //Testing compute function when given several nodes
    @Test
    public void computeWithFewNode(){
        Graph g = new Graph();
        ShortestPaths sp = new ShortestPaths();
        Node a = g.getNode("A");
        Node b = g.getNode("B");
        Node c = g.getNode("C");
        g.addEdge(a, b, 1);
        g.addEdge(a, c, 4);
        g.addEdge(c, b, 2);
        sp.compute(a);
        g.report();
        LinkedList<Node> abPath = sp.shortestPath(c);
        assertEquals(abPath.size(), 2);
        assertEquals(abPath.getFirst(), a);
        assertEquals(abPath.getLast(), c);
        assertEquals(sp.shortestPathLength(c), 4, 1e-6);
    }

    //Testing compute for when a node has multiple paths
    @Test
    public void nodeHasMultPaths(){
        Graph g = new Graph();
        ShortestPaths sp = new ShortestPaths();
        Node a = g.getNode("A");
        Node b = g.getNode("B");
        Node c = g.getNode("C");
        Node d = g.getNode("D");
        Node e = g.getNode("E");
        g.addEdge(a, b, 1);
        g.addEdge(b, c, 2);
        g.addEdge(c, d, 2);
        g.addEdge(d, e, 1);
        g.addEdge(e, a, 1);
        g.addEdge(b, e, 1);
        sp.compute(a);
        g.report();
        LinkedList<Node> abPath = sp.shortestPath(e);
        assertEquals(abPath.size(), 3);
        assertEquals(abPath.getFirst(), a);
        assertEquals(abPath.getLast(), e);
        assertEquals(sp.shortestPathLength(e), 2, 1e-6);
    }

    // Testing when origin node is not a
    @Test
    public void nodeHasMult(){
        Graph g = new Graph();
        ShortestPaths sp = new ShortestPaths();
        Node a = g.getNode("A");
        Node b = g.getNode("B");
        Node c = g.getNode("C");
        Node d = g.getNode("D");
        Node e = g.getNode("E");
        g.addEdge(a, b, 1);
        g.addEdge(b, c, 2);
        g.addEdge(c, d, 2);
        g.addEdge(d, a, 3);
        g.addEdge(e, a, 1);
        g.addEdge(b, e, 1);
        g.addEdge(d,e,1);
        sp.compute(b);
        g.report();
        LinkedList<Node> abPath = sp.shortestPath(a);
        assertEquals(abPath.size(), 3);
        assertEquals(abPath.getFirst(), b);
        assertEquals(abPath.getLast(), a);
        assertEquals(2, sp.shortestPathLength(a),0);
        System.out.println(abPath);
    }

    // Testing shortestpathlength if desired path doesn't exists
    @Test
    public void pathLengthNotPossible(){
        Graph g = new Graph();
        ShortestPaths sp = new ShortestPaths();
        Node a = g.getNode("A");
        Node b = g.getNode("B");
        Node c = g.getNode("C");
        g.addEdge(a, b, 1);
        g.addEdge(c, b, 2);
        sp.compute(a);
        g.report();
        LinkedList<Node> abPath = sp.shortestPath(c);
        assertEquals(sp.shortestPathLength(c), Double.POSITIVE_INFINITY, 1e-6);
        System.out.println("No such path:" + sp.shortestPathLength(c));
    }

    // testing shortestpathlength if desired path does exist
    @Test
    public void shortPathLengthPossible(){
        Graph g = new Graph();
        ShortestPaths sp = new ShortestPaths();
        Node a = g.getNode("A");
        Node b = g.getNode("B");
        Node c = g.getNode("C");
        g.addEdge(a, b, 1);
        g.addEdge(c, b, 2);
        g.addEdge(a, c, 2);
        sp.compute(a);
        g.report();
        LinkedList<Node> abPath = sp.shortestPath(c);
        assertEquals(abPath.size(), 2);
        assertEquals(abPath.getFirst(), a);
        assertEquals(abPath.getLast(), c);
        assertEquals(sp.shortestPathLength(c), 2, 1e-6);
        System.out.println("shortest path length: " + sp.shortestPathLength(c));
    }

    // testing shortestpath if no path exists bewteen origin and destination nodes
    @Test
    public void noPathPossible(){
        Graph g = new Graph();
        ShortestPaths sp = new ShortestPaths();
        Node a = g.getNode("A");
        Node b = g.getNode("B");
        Node c = g.getNode("C");
        g.addEdge(a, b, 1);
        g.addEdge(c, b, 2);
        sp.compute(a);
        g.report();
        LinkedList<Node> abPath = sp.shortestPath(c);
        assertEquals(null, abPath);
        System.out.println("No path: " + abPath);
    }

    //Testing if origin and destination node are same 
    @Test
    public void nodeInOnlyOnce(){
        Graph g = new Graph();
        ShortestPaths sp = new ShortestPaths();
        Node a = g.getNode("A");
        Node b = g.getNode("B");
        Node c = g.getNode("C");
        g.addEdge(a, b, 1);
        g.addEdge(c, b, 2);
        sp.compute(a);
        g.report();
        LinkedList<Node> abPath = sp.shortestPath(a);
        assertEquals(abPath.size(), 1);
        assertEquals(abPath.getFirst(), a);
        assertEquals(abPath.getLast(), a);
        assertEquals(sp.shortestPathLength(a), 0, 1e-6);
        System.out.println("shortest path length: " + sp.shortestPathLength(a));
        System.out.println("Origin & Destination are the same: " + abPath);
    }

    //Testing that that the shortest path is in the correct order (origin to destination)
    @Test
    public void correctOrderPath(){
        Graph g = new Graph();
        ShortestPaths sp = new ShortestPaths();
        Node a = g.getNode("A");
        Node b = g.getNode("B");
        Node c = g.getNode("C");
        Node d = g.getNode("D");
        Node e = g.getNode("E");
        g.addEdge(a, b, 1);
        g.addEdge(b, c, 2);
        g.addEdge(c, d, 2);
        g.addEdge(d, a, 3);
        g.addEdge(e, a, 1);
        g.addEdge(b, e, 1);
        g.addEdge(d, e, 1);
        sp.compute(a);
        g.report();
        LinkedList<Node> abPath = sp.shortestPath(e);
        assertEquals(abPath.size(), 3);
        assertEquals(abPath.getFirst(), a);
        assertEquals(abPath.getLast(), e);
        assertEquals(sp.shortestPathLength(e), 2, 1e-6);
        System.out.println("shortest path length: " + sp.shortestPathLength(e));
        System.out.println("Origin: " + abPath.getFirst());
        System.out.println("Destination: " + abPath.getLast());
        System.out.println("Path from Origin to destination " + abPath);
    }

    /* Pro tip: unless you include @Test on the line above your method header,
     * gradle test will not run it! This gets me every time. */
}
