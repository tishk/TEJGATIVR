package Pin1.currentMethod.util;

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
//        String decryptedM = (decryptWithRSA(message, loadKeyFromFile(KEY_URL).getPrivate()));
          String decryptedM = (decryptWithRSASaba(message, loadKeyFromFile(KEY_URL).getPrivate()));
//  System.out.println("new String(fullMessage) = " + decryptedM);
        return decryptedM;
    }
    public static String decryptORMMessage(byte[] message, String KEY_URL) throws Exception {
        String decryptedM = (decryptWithRSA(message, loadKeyFromFile(KEY_URL).getPrivate()));
//        System.out.println("new String(fullMessage) = " + decryptedM);
        return decryptedM;
    }

    public static byte[] encrypt(String message, String deviceId , String KEY_URL) throws Exception {
        byte[] encryptedM = (encryptWithRSA(message, loadKeyFromFile(KEY_URL).getPublic()));
        byte[] start = deviceId.getBytes();
        int lll=start.length;
        int lenn=encryptedM.length;
        byte[] fullMessage = new byte[start.length +lenn ];
        System.arraycopy(start, 0, fullMessage, 0, 2);
        System.arraycopy(encryptedM, 0, fullMessage, 2, encryptedM.length);
//        System.out.println("new String(fullMessage) = " + new String(fullMessage));
        int lllll=fullMessage.length;
        return fullMessage;
    }

    private static String decryptWithRSASaba(byte[] encrypted, Key key) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, BadPaddingException, IllegalBlockSizeException {
        Cipher rsa;
        rsa = Cipher.getInstance("RSA");
        byte[] ciphertext = null;
        rsa.init(Cipher.DECRYPT_MODE, key);
        byte[] temp = new byte[128];
        int counter = 0;
        byte[] result = new byte[encrypted.length];
        for (int i = 0; i < encrypted.length / 128; i++) {
            System.arraycopy(encrypted, (i * 128), temp, 0, 128);
            ciphertext = rsa.doFinal(temp);
            try {
                System.arraycopy(ciphertext, 0, result, counter, ciphertext.length);
            }
            catch (Exception e) {
                e.printStackTrace();
            }
            counter = counter + ciphertext.length;
        }
        byte y[] = new byte[counter];
        System.arraycopy(result, 0, y, 0, counter);
        String testMessage = null;
        try {
            testMessage = new String(y, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        return testMessage;
    }

    private static String decryptWithRSA(byte[] encrypted, Key key) throws InvalidKeyException, IllegalBlockSizeException, BadPaddingException, NoSuchAlgorithmException, NoSuchPaddingException, UnsupportedEncodingException {
        Cipher rsa = Cipher.getInstance("RSA");
        String text ="";
        byte[] cipherText =new byte[128];
        byte[] temp =new byte[128];
        int c=0;
//        log.info("rsa is " + rsa);
        rsa.init(Cipher.DECRYPT_MODE, key);
//        log.info("rsa is initialized");
        int ll=encrypted.length;
        int countOfMessagePart=ll/128;
        try{
            for (int j=0;j<countOfMessagePart;j++){
                c=0;
                int endOfByteArray=(j+1)*128-1;
                int startOfByteArray=j*128;
                temp =new byte[128];
                for (int k=startOfByteArray;k<endOfByteArray;k++){
                    temp[c]=encrypted[k];
                    c++;
                }
                try{
                    cipherText= rsa.doFinal(temp);
                    text =text+ new String(cipherText, "CP1256");
                }catch (Exception e){
                    System.out.println(e.getMessage());
                }

            }
        }catch (Exception e){
         System.out.println(e.getMessage());
        }

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

            byte[] encryptedText = CodecUtil.encrypt(text, "2#", "C:\\storeKey");
            System.out.println("encryptedText = " + new String(encryptedText));

            byte[]encryptedText2 = new byte[128];

            System.arraycopy(encryptedText,2, encryptedText2, 0, 128);
            String decryptedText = CodecUtil.decrypt(encryptedText2, "C:\\storeKey");
            System.out.println("decryptedText2 = " + decryptedText);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
