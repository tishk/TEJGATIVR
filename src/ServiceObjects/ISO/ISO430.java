package ServiceObjects.ISO;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

public class ISO430  implements Serializable {
    private static final long serialVersionUID = 7933179759833788700L;

    //-----------Security---------------------------------

    private class DesEncrypter {
        Cipher ecipher;

        Cipher dcipher;

        DesEncrypter(SecretKey key) throws Exception {
            ecipher = Cipher.getInstance("DES","ECB");
            dcipher = Cipher.getInstance("DES","ECB");
            ecipher.init(Cipher.ENCRYPT_MODE, key);
            dcipher.init(Cipher.DECRYPT_MODE, key);
        }

        public String encrypt(String str) throws Exception {
            // Encode the string into bytes using utf-8
            byte[] utf8 = str.getBytes("UTF8");

            // Encrypt
            byte[] enc = ecipher.doFinal(utf8);

            // Encode bytes to base64 to get a string
            return new sun.misc.BASE64Encoder().encode(enc);
        }

        public String decrypt(String str) throws Exception {
            // Decode base64 to get bytes
            byte[] dec = new sun.misc.BASE64Decoder().decodeBuffer(str);

            byte[] utf8 = dcipher.doFinal(dec);

            // Decode using utf-8
            return new String(utf8, "UTF8");
        }
    }

    private void  Sample() throws Exception {
        SecretKey key = KeyGenerator.getInstance("DES").generateKey();
        DesEncrypter encrypter = new DesEncrypter(key);
        String encrypted = encrypter.encrypt("Don't tell anybody!");
        String decrypted = encrypter.decrypt(encrypted);
    }
    public String Encrypt(String TextToEncrypt,String Key) throws Exception {
        SecretKey key = KeyGenerator.getInstance("DES").generateKey();
        DesEncrypter encrypter = new DesEncrypter(key);
        String encrypted = encrypter.encrypt(TextToEncrypt);
        return encrypted;
    }
    public String Decrypt(String TextToDecrypt,String Key) throws Exception {
        SecretKey key = KeyGenerator.getInstance("DES").generateKey();
        DesEncrypter encrypter = new DesEncrypter(key);
        String decrypted = encrypter.decrypt(TextToDecrypt);
        return decrypted;
    }

    public String codeString(String str, String strKey){
        try
        {
            String s = str;
            byte[] inputByteArray = new byte[8];
            byte[] bytes = new byte[0];
            try {
                bytes = s.getBytes("UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            for (int i = 0; i < bytes.length; i += 8)
            {
                for (int j = 0; j < 8; j++)
                {
                    if ((i + j) >= bytes.length)
                    {
                        inputByteArray[j] = inputByteArray[j];
                    }
                    else
                    {
                        inputByteArray[j] = (byte)(inputByteArray[j] ^ bytes[i + j]);
                    }
                }

                try {
                    String T = new String(inputByteArray, "UTF-8");
                    try {
                        inputByteArray = this.Encrypt(T, strKey).getBytes("UTF-8");
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }

            }
            StringBuilder builder = new StringBuilder();
            for (byte num3 : inputByteArray) {
                //builder.append(String.format("{0:X2}", num3));
                builder.append(String.format("\\u{0:X2}", num3));

            }

            return builder.toString();
        }
        catch(Exception e)
        {
            return "0000000000000000";
        }
    }

    //----------Security-------------------------------------
    private   String GetAnsiPinBlock(String pan, String pin) throws UnsupportedEncodingException {

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
    private int    fromHex(char c) {
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
    private char   toHex(int nybble) {
        if (nybble < 0 || nybble > 15) {
            throw new IllegalArgumentException();
        }
        return "0123456789ABCDEF".charAt(nybble);
    }
    private String GetPinBlockEncryption(String Pan,String Pin,String Key){
        String PINEncrypted= null;
        try {
            PINEncrypted = GetAnsiPinBlock(Pan, Pin);
        } catch (UnsupportedEncodingException e) {

        }
        PINEncrypted=codeString(PINEncrypted,Key);
        return PINEncrypted;
    }


    private String FixLengthWithZero(String Str,int length){
        String Temp=Str;
        for (int i=0;i<length-Str.length();i++) Temp="0"+Temp;
        return Temp;
    }
    private String FixLengthWithSpace(String Str,int length){
        String Temp=Str;
        for (int i=0;i<length-Str.length();i++) Temp=" "+Temp;
        return Temp;
    }
    private String GetGMTNowTime(){
        Date currentTime = new Date();

        SimpleDateFormat DataFormat =
                new SimpleDateFormat("MMddHHmmss");

        DataFormat.setTimeZone(TimeZone.getTimeZone("GMT"));

        return  DataFormat.format(currentTime);
    }
    private String GetRegisterTime(){
        Date currentTime = new Date();

        SimpleDateFormat DataFormat =
                new SimpleDateFormat("HHmmss");
        return  DataFormat.format(currentTime);
    }
    private String GetRegisterDate(){
        Date currentTime = new Date();

        SimpleDateFormat DataFormat =
                new SimpleDateFormat("MMdd");
        return  DataFormat.format(currentTime);
    }
    private String GetThisBankID(String PAN){
        try {
            if (PAN.substring(0,6).equals("585983")) return "585983";
            else return "627353";
        }catch (Exception e){
            return "627353";
        }

    }
    private String GetSourceBankID(String PAN){
        try {
            return PAN.substring(0,6);

        }catch (Exception e){
            return "-1-1-1";
        }

    }
    private int    randInt(int min, int max) {

        Random rand = new Random();

        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }

    public  void      ProcessReceiveString(String receiveString){
        int Index=0;
        int Index2=0;

        try {
            setReceivedString(receiveString);
            MessageLen=receiveString.substring(0,4);
            MessageType= receiveString.substring(4,8);
            BitmapEarly=receiveString.substring(8,24);
            BitmapSecondary=receiveString.substring(24,40);
            Index=Integer.valueOf(receiveString.substring(40,42))+42;
            Pan=receiveString.substring(42,Index);
            TransactionISOCode=receiveString.substring(Index,Index+6);
            AmountCurrencyAcceptor=receiveString.substring(Index+6,Index+18);
            AmountCurrencyCardIssuer=receiveString.substring(Index+18,Index+30);
            SendDateTimeToSwitch=receiveString.substring(Index+30,Index+40);
            TraceCode=receiveString.substring(Index+40, Index+46);
            RegisterTime=receiveString.substring(Index+46,Index+52);
            RegisterDate=receiveString.substring(Index+52,Index+56);
            ProcessDateTimeInDestination=receiveString.substring(Index+56, Index+60);
            Index=Index+60;
            Index2=Index+2+Integer.valueOf(receiveString.substring(Index,Index+2));
            IDOfPayer=receiveString.substring(Index+2,Index2);
            Index=Index2;
            Index2=Index+2+Integer.valueOf(receiveString.substring(Index,Index+2));
            IDOfIssuer=receiveString.substring(Index+2,Index2);
            Index=Index2;
            ReferenceCode=receiveString.substring(Index,Index+12);
            ResponseTransactionCode=receiveString.substring(Index+12,Index+14);
            IDofCardAcceptorTerminal=receiveString.substring(Index+14,Index+29);
            Index=Index+29;
            int len=Integer.valueOf(receiveString.substring(Index,Index+3));
            Index2=Index+3+len;
            AdditionalData=receiveString.substring(Index+3,Index2);
            Index=Index2;
            CodeOfCurrencyTransactionAccepter=receiveString.substring(Index,Index+3);
            Index=Index+3;
            int lenn=Integer.valueOf(receiveString.substring(Index,Index+3));
            Index2=Index+3+lenn;
            ExistInformation=receiveString.substring(Index+3,Index2);
            Index=Index2;
            int len3=Integer.valueOf(receiveString.substring(Index, Index + 2));
            Index2=Index+len3+2;
            IDOfAcceptorBank=receiveString.substring(Index+2,Index2);
            ProcessReceiveStringIsSuccess=true;
        } catch (Exception e) {
            ProcessReceiveStringIsSuccess=false;
        }
    }

    //------------------Fields--------------------------------------------------

    private BitMap bitMap=null;
    public  void   setBitMap(BitMap bitMap){

    }
    public  BitMap getBitMap(){
        return this.bitMap;
    }

    private String MessageLen="";
    public  String getMessageLen(){
        return MessageLen;
    }

    private String MessageType="";
    public  String getMessageType(){
        return MessageType;
    }

    private String BitmapEarly="";
    public  String getBitmapEarly(){
        return BitmapEarly;
    }

    private String BitmapSecondary="";
    public  String getBitmapSecondary(){
        return BitmapSecondary;
    }

    private String Pan="";
    public  String getPan(){
        return Pan;
    }

    private String TransactionISOCode="";
    public  String getTransactionISOCode(){
        return TransactionISOCode;
    }

    private String AmountCurrencyAcceptor="";
    public  String getAmountCurrencyAcceptor(){
        return AmountCurrencyAcceptor;
    }

    private String AmountCurrencyCardIssuer="";
    public  String getAmountCurrencyCardIssuer(){
        return AmountCurrencyCardIssuer;
    }

    private String CallUniqID=null;
    public  void   setCallUniqID(String CallUniqID){
        this.CallUniqID=CallUniqID;
    }
    public  String getCallUniqID(){
        return CallUniqID;
    }

    private String SendDateTimeToSwitch="";
    public  String getSendDateTimeToSwitch(){
        return SendDateTimeToSwitch;
    }

    private String TraceCode="";
    public  String getTraceCode(){
        return TraceCode;
    }


    private String RegisterTime="";
    public  String getRegisterTime(){
        return RegisterTime;
    }


    private String RegisterDate="";
    public  String getRegisterDate(){
        return RegisterDate;
    }

    private String ProcessDateTimeInDestination="";
    public  String getProcessDateTimeInDestination(){
        return ProcessDateTimeInDestination;
    }

    private String IDOfPayer="";
    public  String getIDOfPayer(){
        return IDOfPayer;
    }

    private String IDOfIssuer="";
    public  String getIDOfIssuer(){
        return IDOfIssuer;
    }

    private String DataOfMagneticTapeTrack2="";
    public  String getDataOfMagneticTapeTrack2(){
        return DataOfMagneticTapeTrack2;
    }

    private String ReferenceCode="";
    public  String getReferenceCode(){
        return ReferenceCode;
    }


    private String ResponseTransactionCode="";
    public  String getResponseTransactionCode(){
        return ResponseTransactionCode;
    }

    private String IDofCardAcceptorTerminal="";
    public  String getIDofCardAcceptorTerminal(){
        return IDofCardAcceptorTerminal;
    }


    private String AdditionalData="";
    public  String getAdditionalData(){
        return AdditionalData;
    }

    private String CodeOfCurrencyTransactionAccepter="";
    public  String getCodeOfCurrencyTransactionAccepter(){
        return CodeOfCurrencyTransactionAccepter;
    }

    private String ExistInformation="";
    public  String getExistInformation(){
        return ExistInformation;
    }

    private String IDOfAcceptorBank="";
    public  String getIDOfAcceptorBank(){
        return IDOfAcceptorBank;
    }

    private String  ReceivedString="";
    public  void setReceivedString(String receivedString){
        ReceivedString=receivedString;
    }
    public  String getReceivedString(){
        return  ReceivedString;
    }



    //----------Other field--------------------------

    private String Pin="";
    public  void    setPin(String pin){
        Pan=pin;
    }
    public  String getPin(){
        return Pin;
    }
    public  boolean   ProcessReceiveStringIsSuccess=false;

}
