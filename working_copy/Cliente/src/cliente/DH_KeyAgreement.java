/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import static cliente.Signatures.*;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.Key;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.spec.InvalidKeySpecException;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.KeyAgreement;
import javax.crypto.Mac;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.SecretKey;
import javax.crypto.spec.DHParameterSpec;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author tiago
 */
public class DH_KeyAgreement {
    private PrivateKey selfPrivateKey;
    private PublicKey otherPublicKey;
    private BigInteger P = new BigInteger("99494096650139337106186933977618513974146274831566768179581759037259788798151499814653951492724365471316253651463342255785311748602922458795201382445323499931625451272600173180136123245441204133515800495917242011863558721723303661523372572477211620144038809673692512025566673746993593384600667047373692203583");
    private BigInteger G = new BigInteger("44157404837960328768872680677686802650999163226766694797650810379076416463147265401084491113667624054557335394761604876882446924929840681990106974314935015501571333024773172440352475358750668213444607353872754650805031912866692119819377041901642732455911509867728218394542745330014071040326856846990119719675");

    public DH_KeyAgreement(byte[] selfPrivateKey, byte[] otherPublicKey) throws Exception {
        this.selfPrivateKey = (PrivateKey) byteToKey("RSA",selfPrivateKey,"private");
        this.otherPublicKey = (PublicKey)  byteToKey("RSA",otherPublicKey,"public");
    }
    
    
   // keyType = "public" | "private"    [Default "private" mesmo que string keyType esteja errada]
    public Key byteToKey(String algorithm, byte[] data , String keyType) throws InvalidKeySpecException, NoSuchAlgorithmException{
        KeyFactory kf = KeyFactory.getInstance(algorithm);

        Key res = null;
        if(keyType.equals("public")){
            X509EncodedKeySpec x509Spec = new X509EncodedKeySpec(data);
            res = kf.generatePublic(x509Spec);
        }
        else{
            PKCS8EncodedKeySpec pkcs8Spec = new PKCS8EncodedKeySpec(data);
            res = kf.generatePrivate(pkcs8Spec);
        }
        
        return res;
    }
     
    
    
    
    //É necessário estabelecer um protocolo em comum, o nosso prgrama tem de deixar de aceitar chaves e muitos tipos, por mim faziamos apenas o aes e chega
    
    //Protocolo:    Cliente: pubCliente
    //              Server : pubServer , Ek(mac(pubCliente, pubServer)
    //              Cliente: Ek(mac(pubCliente, pubServer)
    //Este método realiza a terceira linha
    
    
    
    //Desta maneira, para obter elegantemente a resposta proponho
    public ArrayList<SecretKey> DH_key_exchange_Cliente(OutputStream  out_s,InputStream  in_s) throws Exception{
        // Criacao par chave publica
        System.out.println("Gen DH par chave");
        KeyPairGenerator kpg = KeyPairGenerator.getInstance("DiffieHellman");
        DHParameterSpec param = new DHParameterSpec(P, G);
        kpg.initialize(param);
        KeyPair keyPair = kpg.genKeyPair();
        
        // Criar Canal de comunicacao server
        DataOutputStream out = new DataOutputStream(out_s);
        
        // Enviar nossa chave publica
        byte[] myPublickeyBytes = keyPair.getPublic().getEncoded();
        out.writeInt(myPublickeyBytes.length);
        out.write(myPublickeyBytes);
        
        
        // Receber chave publica de outros
        DataInputStream in = new DataInputStream(in_s);
        int aux_tamanho_chave = in.readInt();
        byte[] otherPublickeyBytes = new byte[aux_tamanho_chave];
        in.readFully(otherPublickeyBytes);
        int aux_tamanho_sts = in.readInt();
        byte[] sts_Authentication = new byte[aux_tamanho_sts];
        in.readFully(sts_Authentication);
            
        
        
        //Usa as duas chaves publicas para criar a chave de sessao
        KeyFactory kf = KeyFactory.getInstance("DiffieHellman");
        X509EncodedKeySpec x509Spec = new X509EncodedKeySpec(otherPublickeyBytes);
        PublicKey ChavePubCliente = kf.generatePublic(x509Spec);
        
        // Acordo de Chaves
        KeyAgreement ka = KeyAgreement.getInstance("DiffieHellman");
        ka.init(keyPair.getPrivate());
        ka.doPhase(ChavePubCliente,true);
        
        SecretKey chaveDH = ka.generateSecret("AES");
        SecretKey chaveSessao = getSimetricKey(chaveDH);           
        

        //Cria a parmutação pubCliente + pubServer
        byte[] pubClienteServer = (new String(keyPair.getPublic().getEncoded()) + new String(otherPublickeyBytes)).getBytes();
        byte[] signature = Signatures.generatingSignature(selfPrivateKey, pubClienteServer);
        byte[] sts_publicKeys = cipherAES( chaveSessao , signature);
        
        //Verifica se a permutação é igual a que recebeu
//        if(!Arrays.equals(sts_publicKeys, sts_Authentication)){
//            System.out.println("Fase Station-to-Station falhou.\nVerifique as chaves publicas.");
//            //Rejeita a conexão.
//        }
        
        //Verifica se a permutação é igual a que recebeu
        if(!descypherThenUnsign(sts_Authentication, chaveSessao, otherPublicKey, pubClienteServer)){
            System.out.println("Fase Station-to-Station falhou.\nVerifique as chaves publicas.");
            //Rejeita a conexão.
        }
        else{
            System.out.println("DH Key Agreement bem sucedido");
        }
        
        //Por fim envia a informação
        
        out.writeInt(sts_publicKeys.length);
        out.write(sts_publicKeys);
        out.flush();
        
        
        ArrayList<SecretKey> resp = new ArrayList<SecretKey>();
        resp.add(chaveSessao);
        resp.add(getSignKey(chaveDH));
        return resp;
    }
    
    
   
    
    SecretKey getSimetricKey(SecretKey dhKey){
        byte[] simetricArray = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
        
            byte[] digestedKey = md.digest(dhKey.getEncoded());
            simetricArray = new byte[digestedKey.length/2];
            System.arraycopy(digestedKey, 0, simetricArray, 0, simetricArray.length);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(DH_KeyAgreement.class.getName()).log(Level.SEVERE, null, ex);
        }
           
        SecretKeySpec cifraSimetrica = new SecretKeySpec(simetricArray, "AES");
        return cifraSimetrica;
    }
    
    SecretKey getSignKey(SecretKey dhKey){
        byte[] signatureArray = null;
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
        
            byte[] digestedKey = md.digest(dhKey.getEncoded());
            signatureArray = new byte[digestedKey.length/2];
            System.arraycopy(digestedKey, signatureArray.length, signatureArray, 0, signatureArray.length);
        } catch (NoSuchAlgorithmException ex) {
            Logger.getLogger(DH_KeyAgreement.class.getName()).log(Level.SEVERE, null, ex);
        }
           
        SecretKeySpec cifraAssinatura = new SecretKeySpec(signatureArray, "AES");
        return cifraAssinatura;
    }
    
   
    boolean descypherThenUnsign(byte[] assinaturaCifrada, SecretKey chaveSessao, PublicKey pubKey, byte[] data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher e = Cipher.getInstance("AES");
        e.init(Cipher.DECRYPT_MODE,chaveSessao);
        
        byte[] assinatura = null;
        assinatura = e.doFinal(assinaturaCifrada);
        
        return verifySignature(pubKey, data , assinatura);
    }
    
    byte[] cipherAES( Key chaveSessao , byte[] data) throws NoSuchAlgorithmException, NoSuchPaddingException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException{
        Cipher e = Cipher.getInstance("AES");
        e.init(Cipher.ENCRYPT_MODE,chaveSessao);
        
        return e.doFinal(data);
    }
    
    
    
    
}
