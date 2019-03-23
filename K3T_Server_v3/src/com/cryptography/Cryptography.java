/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.cryptography;

import java.io.Serializable;
import java.security.KeyPair;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;


public class Cryptography implements Serializable {
    private String nameAlgorithm;
    private ArrayList<byte[]> listKey ;    
    // no 0: public key RSA
    // no 1: public key DSA
    // no 2: private key RSA
    // no 3: pricate key DSA
    // no 4,5: key of nameAlgorithm
    public Cryptography(){
        this.nameAlgorithm="";
        this.listKey = new ArrayList<byte[]>();                
    }    
    
    public Cryptography(String nameAlgorithm,ArrayList<byte[]> listKey){
        this.nameAlgorithm=nameAlgorithm;
        this.listKey = listKey;                
    }
    //Name Algorithm
    public String getNameAlgorithm() {
        return nameAlgorithm;
    }
    public void setNameAlgorithm(String nameAlgorithm) {
        this.nameAlgorithm = nameAlgorithm;
    }
    
    //List Key
    public ArrayList<byte[]> getListKey() {
        return listKey;
    }
    public void setListKey(ArrayList<byte[]> listKey) {
        this.listKey = listKey;
    }

    
}
