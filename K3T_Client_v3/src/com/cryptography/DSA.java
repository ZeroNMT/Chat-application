/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.cryptography;

import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
import java.security.Signature;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;
import java.util.Base64;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.crypto.Cipher;

/**
 *
 * @author ZERO.NMT
 */
public class DSA {
    public static KeyPair createKeyDSA() {
        KeyPair kp =null;
        try {
            SecureRandom sr = new SecureRandom();
            KeyPairGenerator kpg = KeyPairGenerator.getInstance("RSA");
             kp = kpg.genKeyPair();          
        } catch (Exception ex) {
            Logger.getLogger(RSA.class.getName()).log(Level.SEVERE, null, ex);
        }
       
        return kp;
    }
    
    public static byte[] EncryptionDSA(byte[] message, byte[] privateKeyByte) {
        byte[] bSignature = null;
        try{
            PKCS8EncodedKeySpec  spec = new PKCS8EncodedKeySpec(privateKeyByte);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = kf.generatePrivate(spec);
            
            Signature signature = Signature.getInstance("SHA512withRSA");
            signature.initSign(privateKey);         
            signature.update(message);            
            bSignature = signature.sign();            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return bSignature;                   
    } 

    public static boolean DecryptionDSA_verifySign(byte [] strEncrypt, byte[] sign , byte[] punlicKeyBytes)  {
        boolean result = true;
        try{
            Signature signature = Signature.getInstance("SHA512withRSA");
            
            X509EncodedKeySpec spec =new X509EncodedKeySpec(punlicKeyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PublicKey publicKey  = kf.generatePublic(spec);
            
            signature.initVerify(publicKey);
            signature.update(strEncrypt);            
            result = signature.verify(sign);         
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }     
}
