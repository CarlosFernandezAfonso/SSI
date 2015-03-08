/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cliente;


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
public class Cliente {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        try {
            Socket s = new Socket("localhost", 4567);
            
            PrintWriter out = Stream_Ciphers.rc4_printWriter(s.getOutputStream());
            BufferedReader in = Stream_Ciphers.rc4_bufferedReader(s.getInputStream());
            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
            
           
    
            String entrada;
            String linha;
            while(true){
                linha = br.readLine();
            
                out.println(linha + "\n");
                out.flush();
                
                entrada = in.readLine();
                System.out.print(entrada + "\n");
            }
        } catch (Exception ex) {
            Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
    }
    
   
}