/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cliente;


import utils.DH_KeyAgreement;
import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import utils.Cifras;
import static utils.File_Utils.readFile;
import static utils.Mac_utils.Mac_Verification;
import static utils.Mac_utils.mac_Creation;
import static utils.strem_tools.readBYTES;


/**
 *
 * @author Carlos
 */
public class Cliente {

    private SecretKey macKey;
    private Cifras myCipher;

    public Cliente() {
    }
    
    
    public void main(String[] args) throws Exception {
        try {
            Socket s = new Socket("localhost", 4567);
            
            DH_KeyAgreement dh_instance = new DH_KeyAgreement(readFile("RSA_priv_client.txt"),readFile("RSA_pub_server.txt"));
            ArrayList<Object> secretsDH;

            secretsDH = dh_instance.DH_key_exchange_Cliente(s.getOutputStream(), s.getInputStream());

            myCipher = (Cifras) secretsDH.get(0);
            macKey = (SecretKey) secretsDH.get(1);

            
            String key_path = "IV.txt";
            byte[] iv = Files.readAllBytes(Paths.get(key_path));
            
            DataOutputStream out = new DataOutputStream(s.getOutputStream());
            DataInputStream in = new DataInputStream(s.getInputStream());
            
            
            //Mac initiation
            try{
                Mac mac = mac_Creation(this.macKey);       

                BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

                byte[] entrada;
                String linha;
                byte[] incoming_mac;
                int teste_i = 0; 
                while(true){
                    linha = br.readLine();
                    
                    
                    byte[] out_cipharray = this.myCipher.cipherAES(linha.getBytes());
                    out.writeInt( out_cipharray.length);
                    out.write(out_cipharray);
                    out.flush();
                    
                    byte[] macBytes = mac.doFinal(out_cipharray) ;
                    out.writeInt( macBytes.length);
                    out.write(macBytes);
                    out.flush();          
                    
                    entrada = readBYTES(in);
                    incoming_mac = readBYTES(in);
                    
                    Mac_Verification(entrada,mac, incoming_mac);
                    System.out.println(new String(this.myCipher.decipherAES( entrada)));
                }

            } catch (Exception ex) {
                Logger.getLogger(Cliente.class.getName()).log(Level.SEVERE, null, ex);
                System.exit(0);
            }
        }
        catch(Exception e){
        System.exit(0);
        }
            
    }
        
    
    
    
  
}
