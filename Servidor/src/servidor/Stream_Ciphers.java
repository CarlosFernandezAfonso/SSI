/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
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
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author tiago
 */
public class Stream_Ciphers {
    
    public static BufferedReader AES_CBC_NoPadding_bufferedReader(InputStream in, byte[] IV)  throws NoSuchAlgorithmException, NoSuchPaddingException, IOException, InvalidKeyException, InvalidAlgorithmParameterException {
        

        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        
        String key_path = "AES_CBC_NoPadding_key.txt";
        byte[] bkey = Files.readAllBytes(Paths.get(key_path));
        
        SecretKeySpec key = new SecretKeySpec(bkey, "AES");
        
        cipher.init(Cipher.ENCRYPT_MODE, key,new IvParameterSpec(IV));
            
        CipherInputStream cos = new CipherInputStream(in,cipher);

        return new BufferedReader(new InputStreamReader(cos));

    }
    
    public static PrintWriter AES_CBC_NoPadding_printWriter(OutputStream out , byte[] IV) throws NoSuchAlgorithmException, NoSuchProviderException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, IOException{
        
        Cipher cipher = Cipher.getInstance("AES/CBC/NoPadding");
        
        String key_path = "AES_CBC_NoPadding_key.txt";
        byte[] bkey = Files.readAllBytes(Paths.get(key_path));
        
        SecretKeySpec key = new SecretKeySpec(bkey, "AES");
        
        
        cipher.init(Cipher.ENCRYPT_MODE, key,new IvParameterSpec(IV));
        
        CipherOutputStream cos = new CipherOutputStream(out,cipher);

        return new PrintWriter(cos, true);
    }
    
      
    
    public static PrintWriter rc4_printWriter(OutputStream out) throws NoSuchAlgorithmException, NoSuchPaddingException, IOException, InvalidKeyException {
      

        Cipher cipher = Cipher.getInstance( "RC4" );

        KeyGenerator kg = KeyGenerator.getInstance("RC4");
        kg.init(128);

        String key_path = "rc4_key.txt";
        byte[] bkey = Files.readAllBytes(Paths.get(key_path));

        SecretKey key = new SecretKeySpec(bkey,"RC4");

        cipher.init(Cipher.ENCRYPT_MODE,key);

        CipherOutputStream cos = new CipherOutputStream(out,cipher);

        return new PrintWriter(cos, true);


    }
    
    public static BufferedReader rc4_bufferedReader(InputStream in) throws NoSuchAlgorithmException, NoSuchPaddingException, IOException, InvalidKeyException {
        

        Cipher cipher = Cipher.getInstance( "RC4" );

        KeyGenerator kg = KeyGenerator.getInstance("RC4");
        kg.init(128);

        String key_path = "rc4_key.txt";
        byte[] bkey = Files.readAllBytes(Paths.get(key_path));

        SecretKey key = new SecretKeySpec(bkey,"RC4");

        cipher.init(Cipher.DECRYPT_MODE,key);
       
        CipherInputStream cos = new CipherInputStream(in,cipher);

        return new BufferedReader(new InputStreamReader(cos));
    }
}
