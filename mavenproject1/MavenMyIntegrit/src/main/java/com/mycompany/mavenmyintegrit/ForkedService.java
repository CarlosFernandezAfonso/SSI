/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.myintegrit;

import java.io.IOException;
import static java.lang.System.exit;
import java.lang.management.ManagementFactory;
import java.util.ArrayList;
import java.util.concurrent.RecursiveAction;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.misc.Signal;
import sun.misc.SignalHandler;

/**
 *
 * @author xubuntu
 */
public class ForkedService extends Thread{
    //HashMap<String, OwnerData> data;

    public ForkedService(String root) {
        
        
    }

    public ForkedService() {
        
    }

    @Override
    public void run() {
                
        //System.out.println("Hello i'm (my)Integrit : pid = " + ManagementFactory.getRuntimeMXBean().getName());
        
        //Criação de um arraylist
        ArrayList<String> config = new ArrayList<String>();
        config.add("target");
        
        final MyIntegrit myit = new MyIntegrit(config);
        
        Signal.handle(new Signal("HUP"), new SignalHandler() {
            public void handle(Signal signal)
            {
                try {
                    myit.init();
                } catch (IOException ex) {
                    Logger.getLogger(ForkedService.class.getName()).log(Level.SEVERE, null, ex);
                }
                
                System.out.println("Ai");
            }
        });
        
        while(true){

            try {
                Thread.sleep(5000);
                boolean res = myit.compareIter();
                System.out.println("Resultado foi " + res);
            } catch (InterruptedException ex) {
                System.out.println("Exit");
                exit(0);
            } catch (IOException ex) {
                Logger.getLogger(ForkedService.class.getName()).log(Level.SEVERE, null, ex);
            }
          
            
        }
    }

    
    
    
}
