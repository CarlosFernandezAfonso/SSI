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
public class RC4_int_array {
    private int [] S;
    private int [] K;

    public RC4_int_array ( final int [] chave ){
        K = new int[256];
        S = new int[256];
        int t_chave = chave.length;
        
        for(int i = 0 ; i < 256; i++){
            S[i] =  i;
            K[i] = chave[i % t_chave];
        }
        
        int j = 0;
        
        for(int i = 0 ; i < 256; i++){
            
            j = (j + S[i] + K[i]) % 256;
           
            int temp = S[i];
            S[i] = S[j];
            S[j] = temp;

        }
    }
    
    
    public int[] encrypt(int[] texto_limpo) {

        int [] texto_cifrado = new int[texto_limpo.length];
        int i = 0;
        int j = 0;
        int k, t;
        for (int counter = 0; counter < texto_limpo.length; counter++) {
          i = (i + 1) % 256;
          j = (j + S[i]) % 256;

          int temp = S[i];
          S[i] = S[j];
          S[j] = temp;


          t = (S[i] + S[j]) % 256;
          k = S[t];
          texto_cifrado[counter] = (texto_limpo[counter] ^ k);
        }
        return texto_cifrado;
    }
    
    public int[] decrypt(int[] texto_limpo){
        return encrypt(texto_limpo);
    }
    
}
