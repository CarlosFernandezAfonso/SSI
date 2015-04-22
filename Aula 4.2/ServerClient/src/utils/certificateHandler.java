/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package utils;

import java.io.ByteArrayInputStream;
import java.io.FileNotFoundException;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyFactory;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509Certificate;
import java.security.interfaces.RSAPrivateKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import static utils.CertValidator.validarCertificados;

/**
 *
 * @author Carlos
 */
public class certificateHandler {
   
    
   static public PublicKey convertByteToPuKey(byte[] certificado) throws CertificateException, FileNotFoundException{
       ByteArrayInputStream  fin = new ByteArrayInputStream(certificado);
       CertificateFactory f = CertificateFactory.getInstance("X.509");
       X509Certificate certificate = (X509Certificate)f.generateCertificate(fin);
       PublicKey pk = certificate.getPublicKey();
       
       return pk;
   }
   
   static public X509Certificate convertByteToCert(byte[] certificado) throws CertificateException, FileNotFoundException{
       ByteArrayInputStream  fin = new ByteArrayInputStream(certificado);
       CertificateFactory f = CertificateFactory.getInstance("X.509");
       X509Certificate certificate = (X509Certificate)f.generateCertificate(fin);
      
       return certificate;
   }
   
   static public PrivateKey converterByteToPrivKey(byte[] pk8) throws NoSuchAlgorithmException, InvalidKeySpecException{
        PKCS8EncodedKeySpec keySpec = new PKCS8EncodedKeySpec(pk8);
        KeyFactory keyFactory = KeyFactory.getInstance("RSA");
        PrivateKey privKey = (PrivateKey)keyFactory.generatePrivate(keySpec);
        
        return privKey;
   }
   
   static public PublicKey verifyThenConvert(byte[] ca, byte[] certificado) throws InvalidAlgorithmParameterException, Exception{
       
       if(validarCertificados(ca, certificado)){
           System.out.println("Certificado Validado.");
           return convertByteToPuKey(certificado);
       }
       return null;
   }
            
            
}
