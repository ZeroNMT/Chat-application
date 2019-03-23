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
    private Algorithm nameAlgorithm;
    private ArrayList<byte[]> listKey ;
    
    // no 1: public key RSA
    // no 2: public key DSA
    // no 3: private key RSA
    // no 4: pricate key DSA
    // no 5,6: key of nameAlgorithm
    
    //Name Algorithm
    public Algorithm getNameAlgorithm() {
        return nameAlgorithm;
    }
    public void setNameAlgorithm(Algorithm nameAlgorithm) {
        this.nameAlgorithm = nameAlgorithm;
    }
    
    //List Key
    public ArrayList<byte[]> getListKey() {
        return listKey;
    }
    public void setListKey(ArrayList<byte[]> listKey) {
        this.listKey = listKey;
    }
    
    public void createSendKeyStart() {
        
        KeyPair keyRSA = RSA.createKeyRSA();
        KeyPair KeyDSA = DSA.createKeyDSA();
        listKey.add(keyRSA.getPublic().getEncoded());
        listKey.add(KeyDSA.getPublic().getEncoded());
    }
    public void createKeyStart() {
        try{
            KeyPair keyRSA = RSA.createKeyRSA();
            KeyPair KeyDSA = DSA.createKeyDSA();
            listKey.add(keyRSA.getPublic().getEncoded());
            listKey.add(KeyDSA.getPublic().getEncoded());
            listKey.add(keyRSA.getPrivate().getEncoded());
            listKey.add(KeyDSA.getPrivate().getEncoded());
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
}
