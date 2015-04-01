/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package servidor;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Security;
import java.security.Signature;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

/**
 *
 * @author tiago
 */
public class Signatures {
    
    
    static public boolean verifySignature(PublicKey pubKey, byte[] data, byte[] signature){
        boolean res = false;
        try {
            Security.addProvider(new BouncyCastleProvider());
            Signature sig = Signature.getInstance("SHA1withRSA","BC");
            sig.initVerify(pubKey);
            sig.update(data);
            
            res = sig.verify(signature);
        } catch (Exception ex) {
            Logger.getLogger(DH_KeyAgreement_Servidor.class.getName()).log(Level.SEVERE, null, ex);
        } 
        
        return res;
    }
    
    static public byte[] generatingSignature(PrivateKey priv, byte[] data){
        byte[] realSig = null;
        
        try {
            Security.addProvider(new BouncyCastleProvider());
            Signature dsa = Signature.getInstance("SHA1withRSA", "BC"); 
            dsa.initSign(priv);
                 
            dsa.update(data);
            
            realSig = dsa.sign();

        } catch (Exception e) {
            System.err.println("Caught exception " + e.toString());
        }
        
        return realSig;    
    }
    
}
