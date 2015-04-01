/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package paramgenerator;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.security.*;
/**
 *
 * @author tiago
 */
public class RSA_GEN {
    public static void main(String[] args) throws NoSuchAlgorithmException, NoSuchProviderException, FileNotFoundException, IOException {
        KeyPairGenerator keyGen = KeyPairGenerator.getInstance("RSA");
        keyGen.initialize(1024);
        KeyPair keypair = keyGen.genKeyPair();
        
        byte[] publicKey = keypair.getPublic().getEncoded();
        byte[] privateKey = keypair.getPrivate().getEncoded();
        
        FileOutputStream out = new FileOutputStream("RSA_pub.txt");
        out.write(publicKey);
        out.close();
        
        out = new FileOutputStream("RSA_priv.txt");
        out.write(privateKey);
        out.close();
     
    }
}
