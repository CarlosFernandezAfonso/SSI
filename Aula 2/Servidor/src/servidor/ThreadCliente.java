/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package servidor;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;


/**
 *
 * @author Carlos
 */
class ThreadCliente implements Runnable{
    private DataOutputStream out;
    private DataInputStream in;
    private Socket clientSocket;
    private int numero;
    private String type;
    private Cifras cifra;
   

    ThreadCliente(Socket clientSocket, int num, String type) throws Exception {
        this.clientSocket = clientSocket;
        this.type = type;  
        this.numero = num;
        this.out = new DataOutputStream(clientSocket.getOutputStream());
        this.in = new DataInputStream(clientSocket.getInputStream());
            
    }

    @Override
    public void run() {
        
        
        try {
            Stream_setups(this.type);
        } catch (Exception ex) {
            Logger.getLogger(ThreadCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
       
     
        System.out.println("Entrou um novo cliente numero: " + numero);
        try {
            
                      
            byte[] entrada;
            entrada = readBYTES(this.in);
            while(entrada.length != 0){

                entrada = this.cifra.decipherAES(entrada); 
                System.out.println(new String(entrada));

                String aux = /*this.numero + " - " +*/ new String(entrada);
                
                entrada = this.cifra.cipherAES(aux.getBytes());   
                out.writeInt(entrada.length);
                out.write(entrada);

                entrada = readBYTES(this.in);
            }
            
            
        } catch (Exception ex) {
            //Logger.getLogger(ThreadCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Saiu o cliente numero: " + numero);
    }

    
    private void Stream_setups(String type) throws Exception{
        
        
        byte[] iv = readBYTES(this.in);
        
        this.cifra = new Cifras( iv, type);
       

    }
    
    
    
    
    public static byte[] readBYTES(DataInputStream in) throws IOException{
        int tamanho = in.readInt();
        byte[] byte_res = new byte[tamanho];
        in.readFully(byte_res);
        
        return byte_res;
    }

}
