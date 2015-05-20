package com.mycompany.sdc_graphs;

import java.awt.List;
import java.awt.Point;
import java.util.ArrayList;
import java.util.Random;
import org.jgrapht.UndirectedGraph;
import org.jgrapht.alg.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleGraph;

/**
 * Hello world!
 *
 */
public class App 
{
    public static void main( String[] args )
    {
        
        
        
        UndirectedGraph<Integer, DefaultEdge> g =
        new SimpleGraph<Integer, DefaultEdge>(DefaultEdge.class);

        Integer i = 0;
        
        for(i = 0; i < 1000 ; i++){
            g.addVertex(i);
        }
        
        ArrayList<Point> pares = new ArrayList<Point>(); 
        
        Integer a = 0;
        Integer b = 0;
//        for(a = 0; a<1000; a++){
//            for(b=0; b<1000; b++){
//                if(a != b){
//                   pares.add(new Point(a, b)); 
//                }             
//            }
//        }
        int counter = 0;
        Random r = new Random();
        boolean flag = false;
        while(!flag){
            a = r.nextInt(1000);
            b = r.nextInt(1000);
            if(!g.containsEdge(a, b) && !g.containsEdge(b,a) && a != b){
                g.addEdge(a, b);
                counter++;
            }
            ConnectivityInspector cn = new ConnectivityInspector(g);
            if(cn.isGraphConnected()){
                flag = true;
            }
        }
        
        System.out.println("O numero de tentativas foi: " + counter);
        
//        
//        //Criar um  ConnectivityInspector
//        
//        //Usar isGraphConnected() 
//        Point p = new Point(i, i);
//
//        // add the vertices
//        g.addVertex(v1);
//        g.addVertex(v2);
//        g.addVertex(v3);
//        g.addVertex(v4);
//
//        // add edges to create a circuit
//        g.addEdge(v1, v2);
//        g.addEdge(v2, v3);
//        g.addEdge(v3, v4);
//        g.addEdge(v4, v1);
//        
//        System.out.println(g.toString());
    }
}
