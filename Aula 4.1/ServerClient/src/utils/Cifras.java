/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.security.InvalidKeyException;
import java.security.Key;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;

/**
 *
 * @author tiago
 */
public class Cifras {
    Cipher out;
    Cipher in;
//    public static byte[] cipherAES( Cipher e , byte[] data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
//        Cipher e = Cipher.getInstance("AES");
//        e.init(Cipher.ENCRYPT_MODE,chaveSessao);
//        
//        return e.doFinal(data);
//    }
//    
//    public static byte[] decipherAES( Key chaveSessao , byte[] data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
//        Cipher e = Cipher.getInstance("AES");
//        e.init(Cipher.DECRYPT_MODE,chaveSessao);
//        
//        return e.doFinal(data);
//    }
    
//    public static Cipher createCipher(Key chaveSessao) throws InvalidKeyException, NoSuchAlgorithmException, NoSuchPaddingException{
//        e = Cipher.getInstance("AES");
//        e.init(Cipher.ENCRYPT_MODE,chaveSessao);
//        
//        return e;
//    }
    
    Cifras(Key chaveSessao) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException{
        this.out = Cipher.getInstance("AES");
        this.out.init(Cipher.ENCRYPT_MODE,chaveSessao);
        
        this.in = Cipher.getInstance("AES");
        this.in.init(Cipher.DECRYPT_MODE,chaveSessao);
    }
    
    public byte[] cipherAES(byte[] data) throws IllegalBlockSizeException, BadPaddingException{
        return out.doFinal(data);
    }
    
    public byte[] decipherAES(byte[] data) throws IllegalBlockSizeException, BadPaddingException{
        return in.doFinal(data);
    }
}
