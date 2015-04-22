/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import javax.crypto.Mac;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author tiago
 */
public class Mac_utils {
    public static boolean Mac_Verification(byte [] msg, javax.crypto.Mac mac, byte [] incoming_mac){
        byte[] msg_mac = mac.doFinal(msg);
       
        //System.out.println("Recebido-> " + new String( msg_mac ) + "\nCalculado-> " + new String(incoming_mac) );
        System.out.println("(" + Arrays.equals(incoming_mac,msg_mac) + ")");
        return (Arrays.equals(incoming_mac,msg_mac));
    }

    
    
    
    public static Mac mac_Creation(SecretKey macKey) throws InvalidKeyException, NoSuchAlgorithmException{
        SecretKeySpec macKeySpec = new SecretKeySpec(macKey.getEncoded(), "HmacMD5");

        javax.crypto.Mac mac = javax.crypto.Mac.getInstance(macKeySpec.getAlgorithm());
        mac.init(macKeySpec);
        
        return mac;
    }



}
