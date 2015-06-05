/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.mycompany.myintegrit;

/**
 *
 * @author xubuntu
 */
public class OwnerData {
    public String file;
    public String owner;
    public String permissions;

    public OwnerData(String file, String owner, String permissions) {
        this.file = file;
        this.owner = owner;
        this.permissions = permissions;
    }

    
}
