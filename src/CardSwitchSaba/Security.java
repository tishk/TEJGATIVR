package CardSwitchSaba;



import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.security.spec.KeySpec;

/**
 * Created by Administrator on 6/24/2015.
 */
public class Security {

    private  String GetAnsiPinBlock(String pan, String pin) throws UnsupportedEncodingException {

        String sPANPart = "0000" + pan.substring(pan.length()-13, 15);
        String sPINPart = ("0" + String.valueOf(pin.length()) + pin + "FFFFFFFFFFFFFFFF").substring(0, 16);
        return xorHex(sPANPart, sPINPart);
    }
    private  String xorHex(String a, String b) {
        // TODO: Validation
        char[] chars = new char[a.length()];
        for (int i = 0; i < chars.length; i++) {
            chars[i] = toHex(fromHex(a.charAt(i)) ^ fromHex(b.charAt(i)));
        }
        return new String(chars);
    }
    private  int    fromHex(char c) {
        if (c >= '0' && c <= '9') {
            return c - '0';
        }
        if (c >= 'A' && c <= 'F') {
            return c - 'A' + 10;
        }
        if (c >= 'a' && c <= 'f') {
            return c - 'a' + 10;
        }
        throw new IllegalArgumentException();
    }
    private  char   toHex(int nybble) {
        if (nybble < 0 || nybble > 15) {
            throw new IllegalArgumentException();
        }
        return "0123456789ABCDEF".charAt(nybble);
    }

    private  static byte[] StringToByteArray(String hex) {

        int length = hex.length();
        byte[] buffer = new byte[length / 2];
        for (int i = 0; i < length; i += 2) {
            String S=hex.substring(i, i + 2);
            int j=Integer.valueOf(S, 0x10);
            buffer[i / 2] =(byte)j ;
            //System.out.println(buffer[i / 2]);
        }
        return buffer;
    }
    private  String encrypt(String inputText,String KeyValue) throws UnsupportedEncodingException {
        byte[] StrToCode =  StringToByteArray(inputText);
        byte[] keyValue =  StringToByteArray(KeyValue);
        byte[] Res ;
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        try {
            KeySpec keySpec = new DESKeySpec(keyValue);
            SecretKey key = SecretKeyFactory.getInstance("DES").generateSecret(keySpec);
            IvParameterSpec iv = new IvParameterSpec("00000000".getBytes("UTF-8"));
            Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
            //cipher.init(Cipher.ENCRYPT_MODE,key,iv);
            cipher.init(Cipher.ENCRYPT_MODE,key);
            bout.write(cipher.doFinal(StrToCode));
            Res=bout.toByteArray();
        } catch(Exception e) {
            System.out.println("Exception .. "+ e.getMessage());
            Res=null;
        }
        StringBuilder result=new StringBuilder();
        for (byte b : Res) result.append(String.format("%02X", b & 0xFF));
        return result.toString();

    }
    private  String decrypt(String inputText,String KeyValue) throws Exception {
        byte[] StrToCode =  StringToByteArray(inputText);
        byte[] keyValue =  StringToByteArray(KeyValue);
        byte[] Res ;
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        try {
            KeySpec keySpec = new DESKeySpec(keyValue);
            SecretKey key = SecretKeyFactory.getInstance("DES").generateSecret(keySpec);
            IvParameterSpec iv = new IvParameterSpec("00000000".getBytes("UTF-8"));
            Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
            //cipher.init(Cipher.ENCRYPT_MODE,key,iv);
            cipher.init(Cipher.DECRYPT_MODE,key);
            bout.write(cipher.doFinal(StrToCode));
            Res=bout.toByteArray();
        } catch(Exception e) {
            System.out.println("Exception .. "+ e.getMessage());
            Res=null;
        }
        StringBuilder result=new StringBuilder();
        for (byte b : Res) result.append(String.format("%02X", b & 0xFF));
        return result.toString();

    }
    private  byte[] encryptByte(byte[] inputByteArray, String keyStr) throws UnsupportedEncodingException {
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        byte[] keyValue =  StringToByteArray(keyStr);

        try {
            KeySpec keySpec = new DESKeySpec(keyValue);
            SecretKey key = SecretKeyFactory.getInstance("DES").generateSecret(keySpec);
            IvParameterSpec iv = new IvParameterSpec("00000000".getBytes("UTF-8"));
            Cipher cipher = Cipher.getInstance("DES/ECB/NoPadding");
            //cipher.init(Cipher.ENCRYPT_MODE,key,iv);
            cipher.init(Cipher.ENCRYPT_MODE,key);
            bout.write(cipher.doFinal(inputByteArray));
            return  bout.toByteArray();
        } catch(Exception e) {
            System.out.println("Exception .. "+ e.getMessage());
            return "0000000000000000".getBytes("UTF-8");
        }
    }
    public   String CodeString(String str, String strKey) {
        try {
            String s = str;
            byte[] inputByteArray = new byte[8];
            byte[] bytes = new byte[0];
            try {
                bytes = s.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < bytes.length; i += 8) {
                for (int j = 0; j < 8; j++) {
                    if ((i + j) >= bytes.length) {
                        inputByteArray[j] = inputByteArray[j];
                    } else {
                        inputByteArray[j] = (byte) (inputByteArray[j] ^ bytes[i + j]);
                    }
                }

                try {
                    //String T = new String(inputByteArray, "UTF-8");
                    try {
                        inputByteArray = encryptByte(inputByteArray, strKey);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }


            StringBuilder result=new StringBuilder();
            for (byte b : inputByteArray) result.append(String.format("%02X", b & 0xFF));
            return result.toString();



        } catch (Exception e) {
            return "0000000000000000";
        }
    }
    public   String PinBlockDesEncryption(String pan, String pin,String key) {
        try {
          return encrypt(GetAnsiPinBlock(pan,pin),key);
        } catch (UnsupportedEncodingException e) {
          return "0000000000000000";
        }
    }




}
