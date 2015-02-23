/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package servidor;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author Carlos
 */
class ThreadCliente implements Runnable{
    private PrintWriter out;
    private BufferedReader in;
    private int numero;
   

    ThreadCliente(PrintWriter out, BufferedReader in, int num) {
       this.out = out;
       this.in = in;
       this.numero = num;
    }

    @Override
    public void run() {
        String entrada;
        System.out.println("Entrou um novo cliente numero: " + numero);
        try {
            while((entrada = in.readLine()) != null){
                out.println(numero + " - " + entrada ); 
                out.flush();
            }
        } catch (Exception ex) {
            //Logger.getLogger(ThreadCliente.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("Saiu o cliente numero: " + numero);
    }
    
}
