/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package cliente;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.math.BigInteger;
import java.net.Socket;
import java.security.InvalidAlgorithmParameterException;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.security.spec.X509EncodedKeySpec;
import javax.crypto.KeyAgreement;
import javax.crypto.SecretKey;
import javax.crypto.spec.DHParameterSpec;

/**
 *
 * @author tiago
 */
public class DH_KeyAgreement {
    private BigInteger P = new BigInteger("99494096650139337106186933977618513974146274831566768179581759037259788798151499814653951492724365471316253651463342255785311748602922458795201382445323499931625451272600173180136123245441204133515800495917242011863558721723303661523372572477211620144038809673692512025566673746993593384600667047373692203583");
    private BigInteger G = new BigInteger("44157404837960328768872680677686802650999163226766694797650810379076416463147265401084491113667624054557335394761604876882446924929840681990106974314935015501571333024773172440352475358750668213444607353872754650805031912866692119819377041901642732455911509867728218394542745330014071040326856846990119719675");

    public DH_KeyAgreement() {
    }
    
    
    public SecretKey DH_key_exchange(OutputStream  out_s,InputStream  in_s) throws Exception{
        
        
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
        
        // receber coneccao cliente
        //Socket socket = null;
        
        // Receber chave publica de outros
        DataInputStream in = new DataInputStream(in_s);
        int aux_tamanho = in.readInt();
        byte[] otherPublickeyBytes = new byte[aux_tamanho];
        in.readFully(otherPublickeyBytes);

        KeyFactory kf = KeyFactory.getInstance("DH");
        X509EncodedKeySpec x509Spec = new X509EncodedKeySpec(otherPublickeyBytes);
        PublicKey ChavePubCliente = kf.generatePublic(x509Spec);
        
        // Acordo de Chaves
        KeyAgreement ka = KeyAgreement.getInstance("DH");
        ka.init(keyPair.getPrivate());
        ka.doPhase(ChavePubCliente,true);
        
        return ka.generateSecret("AES");
    }
}
