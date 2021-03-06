
package com.cryptography;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
/**
 *
 * @author ZERO.NMT
 */

public class HMAC {
    
    public static String hmacDigest(String msg, byte[] keyString) {
        String digest = null;
        try {
            SecretKeySpec key = new SecretKeySpec(keyString, "HmacSHA512");
            Mac mac = Mac.getInstance("HmacSHA512");
            mac.init(key);
            
            byte[] bytes = mac.doFinal(msg.getBytes("UTF-8"));
            
            StringBuffer hash = new StringBuffer();
            for (int i = 0; i < bytes.length; i++) {
                String hex = Integer.toHexString(0xFF & bytes[i]);
                if (hex.length() == 1) {
                    hash.append('0');
                }
                hash.append(hex);
            }
            digest = hash.toString();
        } catch (UnsupportedEncodingException e) {
        } catch (InvalidKeyException e) {
        } catch (NoSuchAlgorithmException e) {
        }
        return digest;
    }
}