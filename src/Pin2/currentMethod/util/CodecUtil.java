package Pin2.currentMethod.util;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.File;
import java.io.FileInputStream;
import java.io.UnsupportedEncodingException;
import java.security.*;

public class CodecUtil {
//    private static Logger log = Logger.getLogger(CodecUtil.class);

//    private static String deviceId = "7#";
//    private static String KEY_URL = "C:/KeyGeneration/MB7/storeKey";

    public static String decrypt(byte[] message, String KEY_URL) throws Exception {
        String decryptedM = (decryptWithRSA(message, loadKeyFromFile(KEY_URL).getPrivate()));
//        System.out.println("new String(fullMessage) = " + decryptedM);
        return decryptedM;
    }

    public static byte[] encrypt(String message, String deviceId , String KEY_URL) throws Exception {
        byte[] encryptedM = (encryptWithRSA(message, loadKeyFromFile(KEY_URL).getPublic()));
        byte[] start = deviceId.getBytes();
        byte[] fullMessage = new byte[start.length + encryptedM.length];
        System.arraycopy(start, 0, fullMessage, 0, 2);
        System.arraycopy(encryptedM, 0, fullMessage, 2, encryptedM.length);
//        System.out.println("new String(fullMessage) = " + new String(fullMessage));
        return fullMessage;
    }



    private static String decryptWithRSA(byte[] encrypted, Key key) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException {
        Cipher rsa = Cipher.getInstance("RSA");
//        log.info("rsa is " + rsa);
        rsa.init(Cipher.DECRYPT_MODE, key);
//        log.info("rsa is initialized");
        byte[] cipherText = rsa.doFinal(encrypted);
        String text = new String(cipherText, "CP1256");
        return text;
    }

    private static byte[] encryptWithRSA(String message, Key key) throws InvalidKeyException, IllegalBlockSizeException, NoSuchAlgorithmException, NoSuchPaddingException, BadPaddingException {
        Cipher rsa = Cipher.getInstance("RSA");
        rsa.init(Cipher.ENCRYPT_MODE, key);
        return rsa.doFinal(message.getBytes());
    }

    static KeyPair loadKeyFromFile(String url) throws Exception {
        String passwordStr = "123456";
        char[] passwordChars = new char[passwordStr.length()];
        passwordStr.getChars(0, passwordStr.length(), passwordChars, 0);

        KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
        FileInputStream fin = new FileInputStream(new File(url));
        keyStore.load(fin, passwordChars);
//        System.out.println("Key loaded from [" + url + "]");
        Key privateKey = keyStore.getKey("alias", passwordChars);

        java.security.cert.Certificate certificate = keyStore.getCertificate("alias");
        PublicKey publicKey = certificate.getPublicKey();
        return new KeyPair(publicKey, (PrivateKey) privateKey);
    }

    public static void main(String[] args) {
        try {
            String text = "Salam PIN1!!";

            System.out.println("text = " + text);

            byte[] encryptedText = Pin1.currentMethod.util.CodecUtil.encrypt(text, "2#", "C:\\storeKey");
            System.out.println("encryptedText = " + new String(encryptedText));

            byte[]encryptedText2 = new byte[128];

            System.arraycopy(encryptedText,2, encryptedText2, 0, 128);
            String decryptedText = Pin1.currentMethod.util.CodecUtil.decrypt(encryptedText2, "C:\\storeKey");
            System.out.println("decryptedText2 = " + decryptedText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
