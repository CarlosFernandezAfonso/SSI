/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssi_rc4;

/**
 *
 * @author tiago
 */
public class rc4 {
    private final byte[] S = new byte[256];
    private final byte[] T = new byte[256];
    

    public rc4(final byte[] chave) {

        int tamanho_chave = chave.length;
        for (int i = 0; i < 256; i++) {
            S[i] = (byte) i;
            T[i] = chave[i % tamanho_chave];
        }
        int j = 0;
        byte tmp;
        for (int i = 0; i < 256; i++) {
            j = (j + S[i] + T[i]) & 0xFF;
            tmp = S[j];
            S[j] = S[i];
            S[i] = tmp;
        }
    }
    

    public byte[] encrypt(final byte[] texto_limpo) {
        final byte[] texto_cifrado = new byte[texto_limpo.length];
        int i = 0;
        int j = 0;
        int  k, t;
        byte tmp;
        for (int counter = 0; counter < texto_limpo.length; counter++) {
            i = (i + 1) & 0xFF;
            j = (j + S[i]) & 0xFF;
            tmp = S[j];
            S[j] = S[i];
            S[i] = tmp;
            t = (S[i] + S[j]) & 0xFF;
            k = S[t];
            texto_cifrado[counter] = (byte) (texto_limpo[counter] ^ k);
        }
        return texto_cifrado;
    }

    public byte[] decrypt(final byte[] texto_cifrado) {
        return encrypt(texto_cifrado);
    }
}
