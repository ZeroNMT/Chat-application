package com.cryptography;

import java.io.FileInputStream;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;

/**
 *
 * @author ZERO.NMT
 */
public class AES {
    public static SecretKey createKeyAES(){
        SecretKey secretKey = null;
        
        try{
            SecureRandom sr = new SecureRandom();
            KeyGenerator kg = KeyGenerator.getInstance("AES");
            kg.init(256, sr);
            secretKey = kg.generateKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return secretKey;
    }
    public static byte[] getSecretKeyAES(String filename) {
        byte[] secretKeyByte = null;
        try{
            FileInputStream fis = new FileInputStream(filename);
            secretKeyByte = new byte[fis.available()];
            fis.read(secretKeyByte);
            fis.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return secretKeyByte;
    }
    
    public static String EncryptionAES(String message, byte[] secretKeyByte) {
        String cipherText = "";
        try{
            SecretKey secretKey = new SecretKeySpec(secretKeyByte,"AES");            
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] byteEncrypted = cipher.doFinal(message.getBytes());
            cipherText =  Base64.getEncoder().encodeToString(byteEncrypted);
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cipherText;
    }
    public static String DecryptionAES(String strEncrypt, byte[] secretKeyByte) {
        String plainText = "";
        try{
            SecretKey secretKey = new SecretKeySpec(secretKeyByte,"AES");                        
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] byteDecrypted = cipher.doFinal(Base64.getDecoder().decode(strEncrypt));
            plainText = new String(byteDecrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return plainText;
    }
}
