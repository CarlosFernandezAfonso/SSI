/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package servidor;

import java.io.BufferedReader;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
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
        
        DH_KeyAgreement aux = new DH_KeyAgreement();
        byte dataToWrite[];

//        try {
//            dataToWrite = aux.DH_key_exchange(this.clientSocket.getOutputStream(), this.clientSocket.getInputStream()).getEncoded();
//        
//                FileOutputStream out1 = new FileOutputStream("AES_CBC_NoPadding_key.txt");
//                out1.write(dataToWrite);
//                out1.close();
//        } catch (Exception ex) {
//                    Logger.getLogger(ThreadCliente.class.getName()).log(Level.SEVERE, null, ex);
//        }
        
        
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
        } catch (NoSuchProviderException ex) {
            Logger.getLogger(ThreadCliente.class.getName()).log(Level.SEVERE, null, ex);
        } catch (InvalidAlgorithmParameterException ex) {
            Logger.getLogger(ThreadCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        
        
        //Max initiation
        try{
            String key_path = "AES_CBC_NoPadding_key.txt";
            byte[] bkey = Files.readAllBytes(Paths.get(key_path));

            SecretKeySpec key = new SecretKeySpec(bkey, "HmacMD5");

            Mac mac = Mac.getInstance(key.getAlgorithm());
            
            mac.init(key);

//            String message = "This is a confidential message";
//            // get the string as UTF-8 bytes
//            byte[] b = message.getBytes("UTF-8");             
//            // create a digest from the byte array
//            byte[] digest = mac.doFinal(b);
        
        
        
         int teste_i = 0; //  

            String entrada,incoming_mac;
            System.out.println("Entrou um novo cliente numero: " + numero);
            try {
                while((incoming_mac = in.readLine()) != null){
                    entrada = in.readLine();
                     
                    Mac_Verification(entrada,mac,incoming_mac);
                     
                    String msg = numero + " - " + entrada;
                     
                    out.println( new String(mac.doFinal(msg.getBytes())) );
                     
                    
                    System.out.println("a mandar : " + msg);
                    out.println( msg ); 
                     
                    out.flush();
                     
                }
            } catch (Exception ex) {
                //Logger.getLogger(ThreadCliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        
        
        }
        catch(Exception e){}
            System.out.println("Saiu o cliente numero: " + numero);
        }

    
    private void Stream_setups(String type) throws NoSuchPaddingException, NoSuchAlgorithmException, IOException, InvalidKeyException, NoSuchProviderException, InvalidAlgorithmParameterException {
        String key_path = "IV.txt";
        byte[] iv = Files.readAllBytes(Paths.get(key_path));
            
        switch(type){
            case "RC4":
                this.out = Stream_Ciphers.rc4_printWriter(clientSocket.getOutputStream());
                this.in = Stream_Ciphers.rc4_bufferedReader(clientSocket.getInputStream());
                break;
            case "AES/CBC/NoPadding":
                this.out = Stream_Ciphers.AES_CBC_NoPadding_printWriter(clientSocket.getOutputStream(),iv);
                this.in = Stream_Ciphers.AES_CBC_NoPadding_bufferedReader(clientSocket.getInputStream(), iv);
                    break;
            default:
                this.out =  new PrintWriter(clientSocket.getOutputStream(), true);
                this.in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
                break;
        }
 
    }
    
    
    public boolean Mac_Verification(String msg, Mac mac, String incoming_mac){
        String msg_mac = new String(mac.doFinal(msg.getBytes()));
        
        System.out.println("("+ (incoming_mac.matches(msg_mac)) + ")" + msg );
        return (incoming_mac.matches(msg_mac));
    }
    
}
