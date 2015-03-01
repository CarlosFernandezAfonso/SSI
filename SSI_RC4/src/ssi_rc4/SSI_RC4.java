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
public class SSI_RC4 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        String chave = "123456";
        String texto_limpo = "textolimpoaqui";
        String rec , cif;
        
        rc4 temp = new rc4(chave.getBytes());
        
        cif = new String( temp.encrypt(texto_limpo.getBytes()) );
        
        temp = new rc4(chave.getBytes());
        
        rec = new String( temp.decrypt(cif.getBytes()) );
        
        System.out.println( texto_limpo +"\n" + cif +"\n" + rec );
     
    }
    
    public void rc4_ints_version(){
        int [] texto = {9,9,9};
        int [] key = {1,2,3};
        int [] rec , cif ;
        
        RC4_int_array temp = new RC4_int_array(key);
        
        
        
        cif = temp.encrypt(texto);
        
        temp = new RC4_int_array(key);
        rec = temp.decrypt(cif);
        
        System.out.println( yo(texto) +"-----" + yo(cif) +"-----"+ yo(rec) +"-----");
    }
    
    public static String yo(int [] a){
        StringBuilder res = new StringBuilder();
        for(int i : a){
            res.append("# " + i);
        }
        return res.toString();
    }
    
}
