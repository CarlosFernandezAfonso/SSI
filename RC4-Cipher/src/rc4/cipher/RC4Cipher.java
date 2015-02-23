/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package rc4.cipher;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyGenerator;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author Carlos
 */
public class RC4Cipher {

    /**
     * // Cria instância da cifra
        Cipher e = Cipher.getInstance("RC4");
        // Cria instância do gerador de chaves
        KeyGenerator kg = KeyGenerator.getInstance("RC4");
        // Inicializa gerador de chaves (128bit) e gera chave
        kg.init(128);
        SecretKey key = kg.generateKey();
        //Alternativa...
        //byte[] key_bytes = { 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01 };
        //SecretKey key = new SecretKeySpec(key_bytes,"RC4");
        // Inicializa cifra
        e.init(Cipher.ENCRYPT_MODE,key);
        // Utiliza a cifra (in e out são arrays de bytes
        // ou streams)
        byte[] in = null;
        byte[] out = null;
        
        out = e.doFinal(in);
     */
    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException, IOException {
        String arg0 = args[0];
        
        switch(arg0){
            case "-genkey":
                String genKeyFile = args[1];
                criarKey(genKeyFile);
                break;
            case "-enc":
                String encKeyFile = args[1];
                String encInFile = args[2];
                String encOutFile = args[3];
                encode(encKeyFile, encInFile, encOutFile);
                break;
            case "-dec":
                String decKeyFile = args[1];
                String decInFile = args[2];
                String decOutFile = args[3];
                encode(decKeyFile, decInFile, decOutFile);    
                break;
        }
     }
    
    public static void  criarKey(String path) throws NoSuchAlgorithmException, FileNotFoundException, IOException{
        KeyGenerator kg = KeyGenerator.getInstance("RC4");
        // Inicializa gerador de chaves (128bit) e gera chave
        kg.init(128);
        SecretKey key = kg.generateKey();
        
        byte[] chave = key.getEncoded();
        
        FileOutputStream fos = new FileOutputStream(path);
        fos.write(chave);
        fos.close();
    }
    
    public static void encode(String pathKey, String pathIn, String pathOut) throws FileNotFoundException, IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
        Path pathK = Paths.get(pathKey);
        byte[] bkey = Files.readAllBytes(pathK);
        
        Path pathI = Paths.get(pathIn);
        byte[] dataIn = Files.readAllBytes(pathI);
        
        // Cria instância da cifra
        Cipher e = Cipher.getInstance("RC4");
        // Cria instância do gerador de chaves
        KeyGenerator kg = KeyGenerator.getInstance("RC4");
        // Inicializa gerador de chaves (128bit) e gera chave
        kg.init(128);
        //SecretKey key = kg.generateKey();
        //Alternativa...
        //byte[] key_bytes = { 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01 };
        SecretKey key = new SecretKeySpec(bkey,"RC4");
        // Inicializa cifra
        e.init(Cipher.ENCRYPT_MODE,key);
        // Utiliza a cifra (in e out são arrays de bytes
        // ou streams)
  
        byte[] out = null;
        
        out = e.doFinal(dataIn);
        
        FileOutputStream fos = new FileOutputStream(pathOut);
        fos.write(out);
        fos.close();
    }
    
    public static void decode(String pathKey, String pathIn, String pathOut) throws FileNotFoundException, IOException, NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
        Path pathK = Paths.get(pathKey);
        byte[] bkey = Files.readAllBytes(pathK);
        
        Path pathI = Paths.get(pathIn);
        byte[] dataIn = Files.readAllBytes(pathI);
        
        // Cria instância da cifra
        Cipher e = Cipher.getInstance("RC4");
        // Cria instância do gerador de chaves
        KeyGenerator kg = KeyGenerator.getInstance("RC4");
        // Inicializa gerador de chaves (128bit) e gera chave
        kg.init(128);
        //SecretKey key = kg.generateKey();
        //Alternativa...
        //byte[] key_bytes = { 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01, 0x01 };
        SecretKey key = new SecretKeySpec(bkey,"RC4");
        // Inicializa cifra
        e.init(Cipher.ENCRYPT_MODE,key);
        // Utiliza a cifra (in e out são arrays de bytes
        // ou streams)
  
        byte[] out = null;
        
        out = e.doFinal(dataIn);
        
        FileOutputStream fos = new FileOutputStream(pathOut);
        fos.write(out);
        fos.close();
    }
    
}
