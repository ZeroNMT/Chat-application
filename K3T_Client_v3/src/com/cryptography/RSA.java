/*
* To change this license header, choose License Headers in Project Properties.
* To change this template file, choose Tools | Templates
* and open the template in the editor.
*/
package com.cryptography;

import java.io.FileInputStream;
import java.security.KeyFactory;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
import java.security.NoSuchAlgorithmException;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.SecureRandom;
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
public class RSA {
    public static KeyPair createKeyRSA(int num) {
        KeyPair kp =null;
        try {
            SecureRandom sr = new SecureRandom();
            KeyPairGenerator kpg;
            kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(num, sr);
            kp = kpg.genKeyPair();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return kp;
    }
    
    public static byte[] getKeyFileRSA(String filename)  {
        byte[] keyBytes = null;
        try{
            FileInputStream fis = new FileInputStream(filename);
            keyBytes = new byte[fis.available()];
            fis.read(keyBytes);
            fis.close();

        } catch (Exception e) {
            e.printStackTrace();
        }
        return keyBytes;
    }
    
    
        public static String EncryptionRSA2(String message, byte[] keyBytes) {
        String cipherText = "";
        try{
            X509EncodedKeySpec spec =new X509EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PublicKey publicKey  = kf.generatePublic(spec);
            
            Cipher c = Cipher.getInstance("RSA");
            c.init(Cipher.ENCRYPT_MODE, publicKey);
            String msg = "helloworld";
            byte encryptOut[] = c.doFinal(message.getBytes());
            cipherText =  Base64.getEncoder().encodeToString(encryptOut);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cipherText;
    }
    public static String DecryptionRSA2(String strEncrypt, byte[] keyBytes ) {
        String plainText = "";
        try{
            PKCS8EncodedKeySpec  spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = kf.generatePrivate(spec);
            
            Cipher c = Cipher.getInstance("RSA");
            c.init(Cipher.DECRYPT_MODE, privateKey);
            byte decryptOut[] = c.doFinal(Base64.getDecoder().decode(strEncrypt));
            plainText = new String(decryptOut);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return plainText;
    }        
    public static byte[] EncryptionRSA(byte[] message, byte[] keyBytes) {
        byte[] cipherText = null;
        try{
            X509EncodedKeySpec spec =new X509EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PublicKey publicKey  = kf.generatePublic(spec);
            
            Cipher c = Cipher.getInstance("RSA");
            c.init(Cipher.ENCRYPT_MODE, publicKey);
            String msg = "helloworld";
            cipherText = c.doFinal(message);
            //cipherText =  Base64.getEncoder().encodeToString(encryptOut);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cipherText;
    }
    public static byte[] DecryptionRSA(byte[] strEncrypt, byte[] keyBytes ) {
        byte[] plainText = null;
        try{
            PKCS8EncodedKeySpec  spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            PrivateKey privateKey = kf.generatePrivate(spec);
            
            Cipher c = Cipher.getInstance("RSA");
            c.init(Cipher.DECRYPT_MODE, privateKey);
            //plainText = c.doFinal(Base64.getDecoder().decode(strEncrypt));
            plainText = c.doFinal(strEncrypt);            
            //plainText = new String(decryptOut);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return plainText;
    }
}
