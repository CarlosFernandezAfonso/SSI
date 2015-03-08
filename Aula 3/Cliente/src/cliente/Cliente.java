/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cliente;


import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileOutputStream;
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
import javax.crypto.Mac;
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
            
//            DH_KeyAgreement aux = new DH_KeyAgreement();
//            byte dataToWrite[];
//            try {
//                dataToWrite = aux.DH_key_exchange(s.getOutputStream(), s.getInputStream()).getEncoded();
//                FileOutputStream out = new FileOutputStream("AES_CBC_NoPadding_key.txt");
//                out.write(dataToWrite);
//                out.close();
//            } catch (Exception ex) {
//                
//            }
            
            System.out.println("---" + args.length);
            
            String key_path = "IV.txt";
            byte[] iv = Files.readAllBytes(Paths.get(key_path));
            DataOutputStream out ;
            DataInputStream in;
            switch(args[0]){
                case "RC4" : 
                    out = Stream_Ciphers.rc4_printWriter(s.getOutputStream());
                    in = Stream_Ciphers.rc4_bufferedReader(s.getInputStream());
                    break;
                case "AES/CBC/NoPadding":
                    out = Stream_Ciphers.AES_CBC_NoPadding_printWriter(s.getOutputStream(),iv);
                    in = Stream_Ciphers.AES_CBC_NoPadding_bufferedReader(s.getInputStream(), iv);
                    break;
                default:
                    out =  new DataOutputStream(s.getOutputStream());
                    in = new DataInputStream(s.getInputStream());
                    break;
                    
            }
            
            
            //Mac initiation
            try{
                String key_path1 = "AES_CBC_NoPadding_key.txt";
                byte[] bkey = Files.readAllBytes(Paths.get(key_path1));

                SecretKeySpec key = new SecretKeySpec(bkey, "HmacMD5");

                Mac mac = Mac.getInstance(key.getAlgorithm());
                mac.init(key);

    //            String message = "This is a confidential message";
    //            // get the string as UTF-8 bytes
    //            byte[] b = message.getBytes("UTF-8");             
    //            // create a digest from the byte array
    //            byte[] digest = mac.doFinal(b);
        

                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

                String entrada;
                String linha;
                String incoming_mac;
                int teste_i = 0; // System.out.println("> " + teste_i++); // teste lineasd
                while(true){
                    linha = br.readLine();
                    String msg = linha;
                    
                    out.writeUTF(msg);
                    out.writeUTF(new String(mac.doFinal(msg.getBytes())) );
                            System.out.println(new String(mac.doFinal(msg.getBytes())));
                    out.flush();
                    
                    entrada = in.readUTF();
                    incoming_mac = in.readUTF();
                    
                    Mac_Verification(entrada,mac,incoming_mac);
                    System.out.print(entrada + "\n");
                }
            } catch (Exception ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        catch(Exception e){}
            
        }
        
    
    public static boolean Mac_Verification(String msg, Mac mac, String incoming_mac){
        String msg_mac = new String(mac.doFinal(msg.getBytes()));
        
        System.out.println("("+ (incoming_mac.equals(msg_mac)) + ")" + msg );
        return (incoming_mac.equals(msg_mac));
    }
}
