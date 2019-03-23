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
    public static KeyPair createKeyRSA() {
        KeyPair kp =null;
        try {
            SecureRandom sr = new SecureRandom();
            KeyPairGenerator kpg;
            kpg = KeyPairGenerator.getInstance("RSA");
            kpg.initialize(2048, sr);
            kp = kpg.genKeyPair();
        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return kp;
    }
    
    public static PublicKey getPublicKeyRSA(String filename) throws Exception {
        PublicKey pk = null;
        try{
            FileInputStream fis = new FileInputStream(filename);
            byte[] keyBytes = new byte[fis.available()];
            fis.read(keyBytes);
            fis.close();
            X509EncodedKeySpec spec =new X509EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            pk=kf.generatePublic(spec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pk;
    }
    public static PrivateKey getPrivateKeyRSA(String filename) throws Exception {
        PrivateKey pk = null;        
        try{
            FileInputStream fis = new FileInputStream(filename);
            byte[] keyBytes = new byte[fis.available()];
            fis.read(keyBytes);
            fis.close();
            PKCS8EncodedKeySpec  spec = new PKCS8EncodedKeySpec(keyBytes);
            KeyFactory kf = KeyFactory.getInstance("RSA");
            pk=kf.generatePrivate(spec);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pk;
    }
    
    
    
    public static String EncryptionRSA(String message, PublicKey publicKey) {
        String cipherText = "";
        try{
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
    public static String DecryptionRSA(String strEncrypt, PrivateKey privateKey) {
        String plainText = "";
        try{
            Cipher c = Cipher.getInstance("RSA");
            c.init(Cipher.DECRYPT_MODE, privateKey);
            byte decryptOut[] = c.doFinal(Base64.getDecoder().decode(strEncrypt));
            plainText = new String(decryptOut);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return plainText;
    }
}
