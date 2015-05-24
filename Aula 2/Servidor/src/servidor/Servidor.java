/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Carlos
 */
public class Servidor {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args1) {
        // TODO code application logic here
        //args[0] é o modo de cifragem usado, exemplo "rc4"
        
        String[] args ={"RC4","AES/CBC/PKCS5Padding","AES/CFB8/PKCS5Padding","AES/CFB8/NoPadding","AES/CFB/NoPadding","AES/CBC/NoPadding"};
        
        try{
            ServerSocket serverSocket = new ServerSocket(4567);
            int n = 0;
            while(true){
                System.out.println("Server is up!");
                Socket clientSocket = serverSocket.accept();
                
                ThreadCliente tc = new ThreadCliente(clientSocket, n, args[0]);
                n = n +1;
                Thread t = new Thread(tc);
                t.start();
            }
            
  
            
            
        } catch (IOException ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        } catch (Exception ex) {
            Logger.getLogger(Servidor.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        
    }
    
}
