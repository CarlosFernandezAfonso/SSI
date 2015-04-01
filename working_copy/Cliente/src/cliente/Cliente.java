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
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
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
public class Cliente {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        try {
            Socket s = new Socket("localhost", 4567);
            
            DH_KeyAgreement dh_instance = new DH_KeyAgreement(readFile("RSA_priv_self.txt"),readFile("RSA_pub_other.txt"));
            ArrayList<SecretKey> secretsDH;
            try {
                secretsDH = dh_instance.DH_key_exchange_Cliente(s.getOutputStream(), s.getInputStream());
                FileOutputStream out = new FileOutputStream("AES_CBC_NoPadding_key.txt");
                out.write(secretsDH.get(0).getEncoded());
                FileOutputStream out2 = new FileOutputStream("AES_SignatureKey.txt");
                out2.write(secretsDH.get(1).getEncoded());
                out2.close();
            } catch (Exception ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(0);
            }
            System.exit(0);
            
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
                String signature_path = "AES_SignatureKey.txt";
                byte[] sigKey = readFile(signature_path);

                SecretKeySpec signatureKey = new SecretKeySpec(sigKey, "HmacMD5");

                Mac mac = Mac.getInstance(signatureKey.getAlgorithm());
                mac.init(signatureKey);

       

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
    
    
    public static byte[] readFile(String stringPath) throws IOException{
        Path path = Paths.get(stringPath);
        return Files.readAllBytes(path);     
    }
}
