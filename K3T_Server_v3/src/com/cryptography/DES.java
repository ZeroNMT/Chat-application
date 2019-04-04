
package com.cryptography;

import java.io.FileInputStream;
import java.security.KeyPair;
import java.security.KeyPairGenerator;
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
public class DES {
    public static SecretKey createKeyDES() {
        SecretKey secretKey = null;
        try{
            KeyGenerator kg = KeyGenerator.getInstance("DES");
            secretKey = kg.generateKey();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return secretKey;
    }
    public static byte[] getSecretKeyAES(String filename)  {
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
    public static String EncryptionDES(String message, byte[] secretKeyByte) {
        String cipherText = "";
        try{
            SecretKey secretKey = new SecretKeySpec(secretKeyByte,"DES");            
            
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5PADDING");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);
            byte[] byteEncrypted = cipher.doFinal(message.getBytes());
            cipherText =  Base64.getEncoder().encodeToString(byteEncrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return cipherText;
    }
    public static String DecryptionDES(String strEncrypt, byte[] secretKeyByte) {
        String plainText = "";
        try{
            SecretKey secretKey = new SecretKeySpec(secretKeyByte,"DES");                        
            Cipher cipher = Cipher.getInstance("DES/ECB/PKCS5PADDING");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);
            byte[] byteDecrypted = cipher.doFinal(Base64.getDecoder().decode(strEncrypt));
            plainText = new String(byteDecrypted);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return plainText;
    }
}
