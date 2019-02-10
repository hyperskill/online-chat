package chat;

import com.sun.org.apache.xerces.internal.impl.dv.util.HexBin;

import javax.crypto.Cipher;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;

public class AesCryptoAlgorithm {

    private static final String SECRET_KEY = "JustSecretString";

    public String encryption(String message, String key) {
        String encryptedMessage = "";
        try {
            if (!isValidKey(key)) {
                System.out.println("Error: For the aes algorithm, key length must be 16, 24 or 32 symbols(bytes)");
            }

            IvParameterSpec ivParameter = new IvParameterSpec(SECRET_KEY.getBytes("UTF-8"));
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey, ivParameter);

            byte[] bytes = cipher.doFinal(message.getBytes("UTF-8"));
            encryptedMessage = HexBin.encode(bytes);
        } catch (Throwable cause) {
            System.out.println("Error: " + cause.getMessage());
        }
        return encryptedMessage;
    }

    public String decryption(String encryptedMessage, String key) {
        String decryptedMessage = "";
        try {
            if (!isValidKey(key)) {
                System.out.println("Error: For the aes algorithm, key length must be 16, 24 or 32 symbols(bytes)");
            }

            IvParameterSpec ivParameter = new IvParameterSpec(SECRET_KEY.getBytes("UTF-8"));
            SecretKeySpec secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "AES");

            Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
            cipher.init(Cipher.DECRYPT_MODE, secretKey, ivParameter);

            byte[] bytes = cipher.doFinal(HexBin.decode(encryptedMessage));
            decryptedMessage = new String(bytes);
        } catch (Throwable cause) {
            System.out.println("Error: " + cause.getMessage());
            cause.printStackTrace();
        }
        return decryptedMessage;
    }

    private static boolean isValidKey(String key) {
        return key.length() == 16 || key.length() == 24 || key.length() == 32;
    }
}
