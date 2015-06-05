/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.myintegrit;

import java.io.File;
import java.io.IOException;
import static java.lang.String.format;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ForkJoinPool;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author xubuntu
 */
public class MyIntegrit {
    ArrayList<String> ficheiros;
    HashMap<File, String> data;

    MyIntegrit(ArrayList<String> config) {
        ficheiros = config;
        try {
            init();
        } catch (IOException ex) {
            Logger.getLogger(MyIntegrit.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void resetConfig(ArrayList<String> al) throws IOException{
        ficheiros = al;
        init();
    }
    
    public boolean compareIter() throws IOException {
        HashMap<File, String> newIter = iter();
        boolean res = true;
        for(File f : data.keySet()){
            if(newIter.containsKey(f) && newIter.get(f).equals(data.get(f))){
                //NADA
            }
            else{
                res = false;
                Logger.getLogger(MyIntegrit.class.getName()).log(Level.SEVERE, "Foi encontrada uma diferença no ficheiro" + f.getAbsolutePath());
            }
        }
        return res;
    }
    
    public void init() throws IOException{
        data = iter();
    }
    
    public HashMap<File, String> iter() throws IOException{
        HashMap<File, String> res = new HashMap<File, String>();
        for(String s : ficheiros){
            ArrayList<File> fs = getAllFiles(s);
            for(File f : fs){
                res.put(f, getAttribsFile(f.getAbsolutePath()));
            }
        }
        return res;
    }
        
    
    public ArrayList<File> getAllFiles(String root){
        File file = new  File(root);
        File[] fs = file.listFiles();
        ArrayList<File> res = new ArrayList<File>();
        
        
        for(File f : fs){
            if(f.isDirectory()){
                res.addAll(getAllFiles(f.getAbsolutePath()));
            }
        }
        
        for(int i = 0; i < fs.length; i++){
            res.add(fs[i]);
        }
        
        return res;
    }
    
    public String getAttribsFile(String filepath) throws IOException{
        Path file =  Paths.get(filepath);
        PosixFileAttributes attrs = Files.getFileAttributeView(file, PosixFileAttributeView.class)
           .readAttributes();
//        System.out.format("%s %s %s%n", "hello",
//           attrs.owner().getName(),
//           PosixFilePermissions.toString(attrs.permissions()));
        return format("%s %s%n", 
           attrs.owner().getName(),
           PosixFilePermissions.toString(attrs.permissions()));
    }
}
