/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package servidor;



import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import utils.Cifras;
import utils.DH_KeyAgreement;
import static utils.File_Utils.*;
import static utils.Mac_utils.*;
import static utils.strem_tools.readBYTES;


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
    private SecretKey macKey;
    
    private Cifras myCipher;
    
   

    ThreadCliente(Socket clientSocket, int num) throws Exception {
        this.clientSocket = clientSocket;
        this.type = "AES/CBC/NoPadding";  
        this.numero = num;
    }

    @Override
    public void run() {
        try {
            
            DH_KeyAgreement dh_instance;
            dh_instance = new DH_KeyAgreement(readFile("RSA_priv_server.txt"),readFile("RSA_pub_client.txt"));

            ArrayList<Object> resp;
            resp = dh_instance.DH_key_exchange_Server(this.clientSocket.getOutputStream(), this.clientSocket.getInputStream());

            myCipher = (Cifras) resp.get(0);
            macKey = (SecretKey) resp.get(1);

            //System.out.println(new String(sessionKeyAES.getEncoded()));
            //System.out.println(new String(macKey.getEncoded()));
                
            
            out = new DataOutputStream(clientSocket.getOutputStream());
            in = new DataInputStream(clientSocket.getInputStream());
            
            
            Mac mac = mac_Creation(this.macKey);        
            
            byte[] entrada, incoming_mac;
            int aux_tamanho;
            System.out.println("Entrou um novo cliente numero: " + numero);

             
            
                entrada = readBYTES(this.in);
                while(entrada.length != 0){
                    incoming_mac = readBYTES(this.in);
                    
                    Mac_Verification(entrada,mac,incoming_mac);
                    
                    entrada = this.myCipher.decipherAES(entrada); 
                    System.out.println(new String(entrada));
                    
                    entrada = this.myCipher.cipherAES(entrada);   
                    out.writeInt(entrada.length);
                    out.write(entrada);
                    
                    byte[] macString = mac.doFinal(entrada);
                    out.writeInt( macString.length);
                    out.write(macString);                         
                    out.flush();

                    entrada = readBYTES(this.in);
                }
                
                
                
            
            System.out.println("Saiu o cliente numero: " + numero);
        } catch (Exception ex) {
            Logger.getLogger(ThreadCliente.class.getName()).log(Level.SEVERE, null, ex);
            System.exit(0);
        } 
    }

    
 
    

}
