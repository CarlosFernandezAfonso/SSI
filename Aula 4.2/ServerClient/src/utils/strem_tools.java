/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import java.io.DataInputStream;
import java.io.IOException;

/**
 *
 * @author tiago
 */
public class strem_tools {
    public static byte[] readBYTES(DataInputStream in) throws IOException{
        int tamanho = in.readInt();
        byte[] byte_res = new byte[tamanho];
        in.readFully(byte_res);
        
        return byte_res;
    }
}
