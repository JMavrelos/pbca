package gr.blackswamp.core;

import android.util.Base64;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;

public class Crypto {


    public static String encryptIt(String value, String salt) {
        try {
            DESKeySpec keySpec = new DESKeySpec(salt.getBytes("UTF8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(keySpec);

            byte[] clearText = value.getBytes("UTF8");
            // Cipher is not thread safe
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            return Base64.encodeToString(cipher.doFinal(clearText), Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

    public static String decryptIt(String value, String salt) {
        try {
            DESKeySpec keySpec = new DESKeySpec(salt.getBytes("UTF8"));
            SecretKeyFactory keyFactory = SecretKeyFactory.getInstance("DES");
            SecretKey key = keyFactory.generateSecret(keySpec);

            byte[] encrypedPwdBytes = Base64.decode(value, Base64.DEFAULT);
            // cipher is not thread safe
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, key);
            byte[] decrypedValueBytes = (cipher.doFinal(encrypedPwdBytes));
            return new String(decrypedValueBytes);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return value;
    }

}
