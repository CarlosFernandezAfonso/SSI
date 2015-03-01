/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package ssi_rc4;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author tiago
 */
public class SSI_RC4 {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        
        String arg0 = args[0];
        String path_out;
        
        if(args.length != 4){
            System.out.println("Argumentos insuficientes");
            return;
        }
        
        switch(arg0){
            case "-int":
                System.out.println("YO");
                int[] file_key = file_to_intArray(args[1]);
                int[] file_in = file_to_intArray(args[2]);
                path_out = args[3];
                RC4_int_array temp = new RC4_int_array(file_key);
        
                intArray_to_file( path_out , temp.decrypt(file_in));
                break;
                
            case "-byte":
                Path pathK = Paths.get(args[1]);
                byte[] bkey = Files.readAllBytes(pathK);
                
                Path path_in = Paths.get(args[2]);
                byte[] bFileIn = Files.readAllBytes(path_in);
                
                path_out = args[3];
                
                Rc4 tempByte = new Rc4(bFileIn);
        
                byte[] out = null;
        
                out = tempByte.encrypt(bFileIn);

                FileOutputStream fos = new FileOutputStream(path_out);
                fos.write(out);
                fos.close();
                
                break;
            default:
                break;
        }
        
        
        // TESTES a seco...
//        System.out.println("rc4_ints_version");
//        rc4_ints_version();
//        
//        System.out.println("rc4_byte_version");
//        rc4_byte_version();
    }
    
    public void rc4_byte_version(){
        String chave = "123456";
        String texto_limpo = "textolimpoaqui";
        String rec , cif;
        
        Rc4 temp = new Rc4(chave.getBytes());
        
        cif = new String( temp.encrypt(texto_limpo.getBytes()) );
        
        temp = new Rc4(chave.getBytes());
        
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
        
        System.out.println( print_Teste(texto) +"-----" + print_Teste(cif) +"-----"+ print_Teste(rec) +"-----");
    }
    
    public static String print_Teste(int [] a){
        StringBuilder res = new StringBuilder();
        for(int i : a){
            res.append("# " + (char) i + "(" + i+")");
        }
        return res.toString();
    }
    
    public static String ArrayToString(int [] a){
        StringBuilder res = new StringBuilder();
        for(int i : a){
            res.append((char) i );
        }
        
        return res.toString();
    }
    
    public static void intArray_to_file(String path,int[] out) throws FileNotFoundException, UnsupportedEncodingException{
        PrintWriter writer = new PrintWriter(path);
        
        for(int i : out)
        writer.print((char) i);
        writer.close();
    }
    

    public static int[] file_to_intArray(String path) throws FileNotFoundException{
        BufferedReader br = new BufferedReader(new FileReader(path));
        ArrayList<Integer> res = new ArrayList<Integer>();
        
        try {
            int character = br.read();

            while (character != -1) {
                res.add(character);
                character = br.read();
            }
        } catch (IOException ex) {
            Logger.getLogger(SSI_RC4.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            try {
                br.close();
            } catch (IOException ex) {
                Logger.getLogger(SSI_RC4.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        
        return  toIntArray(res);
    }
    
    
    public static int[] toIntArray(List<Integer> integerList) {
        int[] intArray = new int[integerList.size()];
        for (int i = 0; i < integerList.size(); i++) {
            intArray[i] = integerList.get(i);
        }
        return intArray;
    }

}
