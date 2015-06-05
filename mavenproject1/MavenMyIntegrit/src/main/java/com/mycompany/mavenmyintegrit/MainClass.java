/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.myintegrit;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

/**
 *
 * @author xubuntu
 */
public class MainClass {
    public static void main(String[] args) throws IOException{
        String pidRaw = java.lang.management.ManagementFactory.getRuntimeMXBean().getName();
        String pid = pidRaw.split("@", -1)[0];
        System.out.println(pid);
        FileWriter path = new FileWriter("/var/run/myintegrit/myintegrit.pid", false);
        path.write(pid);
        path.close();
        
        ForkedService fdb = new ForkedService();
        
        fdb.setDaemon(false);
        fdb.start();
        
        
        // TESTING
        
        //primObject.getAttribsFile("hello.txt");
        
        //primObject.getAllFiles("target");
         //while(true);
    }
}
