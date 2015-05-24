/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import java.security.NoSuchProviderException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author tiago
 */
public class Cifras {
    Cipher out;
    Cipher in;
    
    Cifras(byte [] iv, String tipo) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, InvalidAlgorithmParameterException, UnsupportedEncodingException, IOException, NoSuchProviderException{
        switch(tipo){
            case "RC4":
                System.out.println("RC4");
                this.out = Cipher.getInstance( "RC4" );

                KeyGenerator kg = KeyGenerator.getInstance("RC4");
                kg.init(128);

                String key_path = "rc4_key.txt";
                byte[] bkey = Files.readAllBytes(Paths.get(key_path));

                SecretKey key = new SecretKeySpec(bkey,"RC4");

                this.out.init(Cipher.ENCRYPT_MODE,key);
                
                this.in = Cipher.getInstance( "RC4" );

                KeyGenerator kgin = KeyGenerator.getInstance("RC4");
                kgin.init(128);

                String key_pathin = "rc4_key.txt";
                byte[] bkeyin = Files.readAllBytes(Paths.get(key_pathin));

                SecretKey keyin = new SecretKeySpec(bkeyin,"RC4");

                this.in.init(Cipher.DECRYPT_MODE,keyin);
                
                break;
                
                
            default:
                
                System.out.println("AES_CBC_NoPadding_key");
                String key_path_2 = "AES_CBC_NoPadding_key.txt";
                byte[] bkey_2 = Files.readAllBytes(Paths.get(key_path_2));

                SecretKeySpec key_2 = new SecretKeySpec(bkey_2, "AES");
                
                this.out = Cipher.getInstance(tipo, "SunJCE");
                this.out.init(Cipher.ENCRYPT_MODE,key_2,new IvParameterSpec(iv));

                this.in = Cipher.getInstance(tipo);
                this.in.init(Cipher.DECRYPT_MODE,key_2,new IvParameterSpec(iv));
                break;
                
        }
        
        
    }
    
    public byte[] cipherAES(byte[] data) throws IllegalBlockSizeException, BadPaddingException{
        return out.doFinal(data);
    }
    
    public byte[] decipherAES(byte[] data) throws IllegalBlockSizeException, BadPaddingException{
        return in.doFinal(data);
    }
}
