import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.security.spec.KeySpec;
import java.util.Base64;

/**
 * Created by Administrator on 2/2/2017.
 */
public class Main {

    static String key=null;
    static String textToEncrypt=null;
    static String parameters=null;
    public static void main(String[] args) throws Exception {

        if (args.length==2){
            textToEncrypt=args[0];
            key=args[1];
            if (key.length()!=16) {
                System.out.println("babak 10 bar goftam kilido 16 tai bezan");
                return;
            }
            parameters="AES";
        }else if (args.length==3){
            textToEncrypt=args[0];
            key=args[1];
            if (key.length()!=16) {
                System.out.println("babak 10 bar goftam kilido 16 tai bezan");
                return;
            }
            parameters=args[2];

        }else{
            System.out.println("babak parametraro dorost vared kon !!!:java -jar encryptionTest.jar test 1234567812345678");
        }

        start();

    }
    public static  void start() throws IOException {
        Encryptor encryptor=new Encryptor();
        System.out.println("Text to encrypt:"+textToEncrypt);

        byte[] encryptbyte=encryptor.encrypt(key, parameters, textToEncrypt);
        FileOutputStream fileOutputStream=new FileOutputStream("EncryptedFile");
        fileOutputStream.write(encryptbyte);
        fileOutputStream.close();

        Path path= Paths.get("EncryptedFile");
        byte[] fileload= Files.readAllBytes(path);

        String decryptTest=encryptor.decrypt(key, parameters,fileload);
        Base64.Encoder encoder=Base64.getEncoder() ;
        String s=encoder.encodeToString(encryptbyte);
        System.out.println("decrypted Text:"+decryptTest);
    }

    private static void test1() throws IOException {

        String key = "1234567812345678"; // 128 bit key
        String initVector = "RandomInitVector"; // 16 bytes IV
        Encryptor encryptor=new Encryptor();
        byte[] encryptbyte=encryptor.encrypt(key, initVector, "test");
        FileOutputStream fileOutputStream=new FileOutputStream("EncryptedFile");
        fileOutputStream.write(encryptbyte);
        fileOutputStream.close();

       Path path= Paths.get("EncryptedFile");
        byte[] fileload= Files.readAllBytes(path);

        String decryptTest=encryptor.decrypt(key, initVector,fileload);
        Base64.Encoder encoder=Base64.getEncoder() ;
        String s=encoder.encodeToString(encryptbyte);
        System.out.println(decryptTest);
    }
    private static void test2() throws Exception {
        SecurityClient securityClient=new SecurityClient("12345678");
        String s=securityClient.encrypt("hamid");
        String s1=securityClient.decrypt(s);

        System.out.println(s1);
    }


}
