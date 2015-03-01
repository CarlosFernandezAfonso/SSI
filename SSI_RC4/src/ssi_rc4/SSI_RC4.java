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
        
//                System.out.println(yo(file_key) + "---" + yo(file_in) + "----"+ yo(temp.encrypt(file_in)));
                intArray_to_file( path_out , temp.decrypt(file_in));
                break;
            case "-byte":
                Path pathK = Paths.get(args[1]);
                byte[] bkey = Files.readAllBytes(pathK);
                
                Path path_in = Paths.get(args[2]);
                byte[] bFileIn = Files.readAllBytes(path_in);
                
                path_out = args[3];
                
                rc4 tempByte = new rc4(bFileIn);
        
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
            res.append("# " + (char) i + "(" + i+")");
        }
        
        for(int i : a){
            printChar(i);
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
    
    
    
    public static final char[] EXTENDED = { 0x00C7, 0x00FC, 0x00E9, 0x00E2,
            0x00E4, 0x00E0, 0x00E5, 0x00E7, 0x00EA, 0x00EB, 0x00E8, 0x00EF,
            0x00EE, 0x00EC, 0x00C4, 0x00C5, 0x00C9, 0x00E6, 0x00C6, 0x00F4,
            0x00F6, 0x00F2, 0x00FB, 0x00F9, 0x00FF, 0x00D6, 0x00DC, 0x00A2,
            0x00A3, 0x00A5, 0x20A7, 0x0192, 0x00E1, 0x00ED, 0x00F3, 0x00FA,
            0x00F1, 0x00D1, 0x00AA, 0x00BA, 0x00BF, 0x2310, 0x00AC, 0x00BD,
            0x00BC, 0x00A1, 0x00AB, 0x00BB, 0x2591, 0x2592, 0x2593, 0x2502,
            0x2524, 0x2561, 0x2562, 0x2556, 0x2555, 0x2563, 0x2551, 0x2557,
            0x255D, 0x255C, 0x255B, 0x2510, 0x2514, 0x2534, 0x252C, 0x251C,
            0x2500, 0x253C, 0x255E, 0x255F, 0x255A, 0x2554, 0x2569, 0x2566,
            0x2560, 0x2550, 0x256C, 0x2567, 0x2568, 0x2564, 0x2565, 0x2559,
            0x2558, 0x2552, 0x2553, 0x256B, 0x256A, 0x2518, 0x250C, 0x2588,
            0x2584, 0x258C, 0x2590, 0x2580, 0x03B1, 0x00DF, 0x0393, 0x03C0,
            0x03A3, 0x03C3, 0x00B5, 0x03C4, 0x03A6, 0x0398, 0x03A9, 0x03B4,
            0x221E, 0x03C6, 0x03B5, 0x2229, 0x2261, 0x00B1, 0x2265, 0x2264,
            0x2320, 0x2321, 0x00F7, 0x2248, 0x00B0, 0x2219, 0x00B7, 0x221A,
            0x207F, 0x00B2, 0x25A0, 0x00A0 };

    public static final char getAscii(int code) {
        if (code >= 0x80 && code <= 0xFF) {
            return EXTENDED[code - 0x7F];
        }
        return (char) code;
    }

    public static final void printChar(int code) {
        System.out.printf("%c%n", getAscii(code));
        
    }
}
