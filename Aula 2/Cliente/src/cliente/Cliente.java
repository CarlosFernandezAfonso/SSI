/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package cliente;


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

    static private Cifras cifra;
    
    public static void main(String[] args) throws Exception{
        Cliente c = new Cliente();
        
        c.main_aux(args);
        
    }
    private DataOutputStream out;
    private DataInputStream in;
    
    
    /**
     * @param args the command line arguments
     */
    public void main_aux(String[] args1) throws Exception {

        String[] args = {"RC4","AES/CBC/PKCS5Padding","AES/CFB8/PKCS5Padding","AES/CFB8/NoPadding","AES/CFB/NoPadding","AES/CBC/NoPadding"};

        Socket s = new Socket("localhost", 4567);


         out = new DataOutputStream(s.getOutputStream());
         in = new DataInputStream(s.getInputStream());
         
        Stream_setups(args[0]);


        BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

        byte[] entrada;
        String linha;
        while(true){
            linha = br.readLine();

            byte[] linha_cifrada = cifra.cipherAES(linha.getBytes());

            out.writeInt(linha_cifrada.length);
            out.write(linha_cifrada);

            entrada = readBYTES(in);
            entrada = this.cifra.decipherAES(entrada); 
            System.out.println(new String(entrada));


        }


    }
    
   
    
    
    
    public void Stream_setups(String type) throws Exception{
        
        byte[] iv = Files.readAllBytes(Paths.get("IV.txt"));

        out.writeInt(iv.length);
        out.write(iv);
        
        
        cifra = new Cifras( iv, type);
      
    }
    
    
    
    
    public byte[] readBYTES(DataInputStream in) throws IOException{
        int tamanho = in.readInt();
        byte[] byte_res = new byte[tamanho];
        in.readFully(byte_res);
        
        return byte_res;
    }
    
   
   
}
