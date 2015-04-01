/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.crypto.Mac;

/**
 *
 * @author tiago
 */
public class File {

    public static byte[] readFile(String stringPath) throws IOException{
        Path path = Paths.get(stringPath);
        return Files.readAllBytes(path);    
    }
    
    public boolean writeFile(String stringPath , byte[] data){
        try {
            FileOutputStream out1 = new FileOutputStream(stringPath);
            out1.write(data);
            out1.flush();
            out1.close();
        } catch (IOException ex) {
            return false;
        }
        return true;
    }
}
