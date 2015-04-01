/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package serverclient;

import cliente.Cliente;

/**
 *
 * @author tiago
 */
public class ServerClient {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws Exception {
        Cliente c = new Cliente();
        
        c.main(null);
    }
    
}
