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
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Carlos
 */
class ThreadCliente implements Runnable{
    private PrintWriter out;
    private BufferedReader in;
    private Socket clientSocket;
    private int numero;
    private String type;
   

    ThreadCliente(Socket clientSocket, int num, String type) throws Exception {
        this.clientSocket = clientSocket;
        this.type = type;  
        this.numero = num;
    }

    @Override
    public void run() {
        
        
        try {
            Stream_setups(this.type);
        } catch (NoSuchPaddingException ex) {
            Logger.getLogger(ThreadCliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(ThreadCliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ThreadCliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidKeyException ex) {
            Logger.getLogger(ThreadCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        

        String entrada;
        System.out.println("Entrou um novo cliente numero: " + numero);
        try {
            while((entrada = in.readLine()) != null){
                out.println(numero + " - " + entrada ); 
                out.flush();
            }
        } catch (Exception ex) {
            //Logger.getLogger(ThreadCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Saiu o cliente numero: " + numero);
    }

    
    private void Stream_setups(String type) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, InvalidKeyException {
        
        switch(type){
            case "RC4":
                this.out = Stream_Ciphers.rc4_printWriter(clientSocket.getOutputStream());
                this.in = Stream_Ciphers.rc4_bufferedReader(clientSocket.getInputStream());
                break;
            default:
                this.out =  new PrintWriter(clientSocket.getOutputStream(), true);
                this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                break;
        }

    }
    
    
}
