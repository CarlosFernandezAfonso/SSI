/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package servidor;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author Carlos
 */
public class Servidor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        // TODO code application logic here
        //args[0] é o modo de cifragem usado, exemplo "rc4"
        try{
            ServerSocket serverSocket = new ServerSocket(4567);
            int n = 0;
            while(true){
                Socket clientSocket = serverSocket.accept();
                
                ThreadCliente tc = new ThreadCliente(clientSocket, n, args[0]);
                n = n +1;
                Thread t = new Thread(tc);
                t.start();
            }
        }
        catch(Exception e){
            
        }
        
    }
    
}
