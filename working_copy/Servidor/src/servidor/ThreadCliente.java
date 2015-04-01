/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package servidor;

import java.io.BufferedReader;
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
   

    ThreadCliente(Socket clientSocket, int num) throws Exception {
        this.clientSocket = clientSocket;
        this.type = "AES/CBC/NoPadding";  
        this.numero = num;
    }

    @Override
    public void run() {
        try { 
            DH_KeyAgreement_Servidor dh_instance;
            dh_instance = new DH_KeyAgreement_Servidor(readFile("RSA_priv_self.txt"),readFile("RSA_pub_other.txt"));

            ArrayList<SecretKey> resp;
            resp = dh_instance.DH_key_exchange_Server(this.clientSocket.getOutputStream(), this.clientSocket.getInputStream());
        
            writeFile("AES_CBC_NoPadding_key.txt", resp.get(0).getEncoded());
            writeFile("AES_SignatureKey.txt", resp.get(1).getEncoded());
        } catch (Exception ex) {
                    Logger.getLogger(ThreadCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        
        //Exit para debug
        System.exit(0);
        
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
            String signature_path = "AES_SignatureKey.txt";
            byte[] sigKey = Files.readAllBytes(Paths.get(signature_path));

            SecretKeySpec signatureKey = new SecretKeySpec(sigKey, "HmacMD5");

            Mac mac = Mac.getInstance(signatureKey.getAlgorithm());
            mac.init(signatureKey);

            String entrada,incoming_mac;
            System.out.println("Entrou um novo cliente numero: " + numero);
            try {
                while((entrada = in.readUTF()) != null){
                    incoming_mac = in.readUTF();
                    
                    Mac_Verification(entrada,mac,incoming_mac);
                     
                    //String msg = numero + " - " + entrada;
                    String msg = entrada;
                    System.out.println(msg);
                    out.writeUTF(msg ); 
                    
                    out.writeUTF( new String(mac.doFinal(msg.getBytes())) );
                     
                    out.flush();
                     
                }
            } catch (Exception ex) {
                Logger.getLogger(ThreadCliente.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(0);
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
                this.out =  new DataOutputStream(clientSocket.getOutputStream());
                this.in = new DataInputStream(clientSocket.getInputStream());
                break;
        }
 
    }
    
    
    public boolean Mac_Verification(String msg, Mac mac, String incoming_mac){
        String msg_mac = new String(mac.doFinal(msg.getBytes()));
        
        System.out.println("Recebido->\n" + msg_mac + "\nCalculado->\n" + incoming_mac );
        System.out.println(incoming_mac.equals(msg_mac));
        return (incoming_mac.equals(msg_mac));
    }
    
    
    public static byte[] readFile(String stringPath) throws IOException{
        Path path = Paths.get(stringPath);
        return Files.readAllBytes(path);    
    }
    
    public boolean writeFile(String stringPath , byte[] data){
        try {
            FileOutputStream out1 = new FileOutputStream(stringPath);
            out1.write(data);
            out1.flush();
            out1.close();
        } catch (IOException ex) {
            return false;
        }
        return true;
    }
}
