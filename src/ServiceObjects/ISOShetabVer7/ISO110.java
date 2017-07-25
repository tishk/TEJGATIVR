package ServiceObjects.ISOShetabVer7;

import ServiceObjects.ISO.BitMap;

import javax.crypto.Cipher;
import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

public class ISO110 implements Serializable {


    //-----------Security---------------------------------
    private static final long serialVersionUID = 7933179759822748200L;
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
    public  String  Shamsi_Date() {

        Calendar cal = Calendar.getInstance();
        int Day = cal.get(Calendar.DAY_OF_MONTH);
        int Month = cal.get(Calendar.MONTH)+1;
        int Year = cal.get(Calendar.YEAR);

        if(Month < 3 && Day < 21)
            Year -= 622;
        else
            Year -= 621;

        switch (Month) {
            case 1:
                if(Day < 21)
                {
                    Month=10;
                    Day+=10;
                }
                else
                {
                    Month=11;
                    Day-=20;
                }
                break;

            case 2:
                if(Day < 20)
                {
                    Month=11;
                    Day+=11;
                }
                else
                {
                    Month=12;
                    Day-=19;
                }
                break;

            case 3:
                if(Day < 21)
                {
                    Month=12;
                    Day+=9;
                }
                else
                {
                    Month=1;
                    Day-=20;
                }
                break;

            case 4:
                if(Day < 21)
                {
                    Month=1;
                    Day+=11;
                }
                else
                {
                    Month=2;
                    Day-=20;
                }
                break;

            case 5:
            case 6:
                if(Day < 22)
                {
                    Month-=3;
                    Day+=10;
                }
                else
                {
                    Month-=2;
                    Day-=21;
                }
                break;

            case 7:
            case 8:
            case 9:
                if(Day < 23)
                {
                    Month-=3;
                    Day+=9;
                }
                else
                {
                    Month-=2;
                    Day-=22;
                }
                break;

            case 10 :
                if(Day < 23)
                {
                    Month=7;
                    Day+=8;
                }
                else
                {
                    Month=8;
                    Day-=22;
                }
                break;

            case 11:
            case 12:
                if(Day < 22)
                {
                    Month-=3;
                    Day+=9;
                }
                else
                {
                    Month-=2;
                    Day-=21;
                }
                break;
        }
        String M=null;
        String D=null;
        if (Month<10) M="0"; else M="";
        if (Day<10) D="0"; else D="";
        return String.valueOf(Year) +  M+String.valueOf(Month) + D+String.valueOf(Day);

    }
    public  String  getRequestTime(){
        Date  Time=new Date();
        SimpleDateFormat DateFormat =
                //new SimpleDateFormat ("hh:mm:ss a");
                new SimpleDateFormat ("HHmmss");
        String Now=DateFormat.format(Time);
        return Now;
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
    private String GetReferenceCode(){
        return FixLengthWithZero(String.valueOf(randInt(0, 999999)), 6)+
                FixLengthWithZero(String.valueOf(randInt(0, 999999)), 6) ;
    }
    private String GetTraceID(){

        return FixLengthWithZero(String.valueOf(randInt(0, 999999)), 6);
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

            SendDateTimeToSwitch=receiveString.substring(Index+6,Index+16);

            TraceCode=receiveString.substring(Index+16, Index+22);

            RegisterTime=receiveString.substring(Index+22,Index+28);

            RegisterDate=receiveString.substring(Index+28,Index+32);

            CheckoutDate=receiveString.substring(Index+32, Index+36);

            Index=Index+36;
            Index2=Index+2+Integer.valueOf(receiveString.substring(Index,Index+2));
            IDOfAcceptor=receiveString.substring(Index+2,Index2);

            Index=Index2;
            Index2=Index+2+Integer.valueOf(receiveString.substring(Index,Index+2));
            IDOfIssuer=receiveString.substring(Index+2,Index2);
            Index=Index2;

            ReferenceCode=receiveString.substring(Index,Index+12);

            ResponseAndLicenseTransaction=receiveString.substring(Index+12,Index+18);

            ResponseTransactionCode=receiveString.substring(Index+18,Index+20);
            try{
                if (Integer.valueOf(ResponseTransactionCode)!=0){
                    ActionCode="50"+ResponseTransactionCode;
                }else ActionCode="00"+ResponseTransactionCode;
            }catch (Exception e){

            }


            IDofCardAcceptorTerminal=receiveString.substring(Index+20,Index+28);


            Index=Index+28;
            Index2=Index+2+Integer.valueOf(receiveString.substring(Index,Index+2));

            AdditionalData=receiveString.substring(Index+2,Index2);

            TelNo=String.valueOf(Long.valueOf(AdditionalData.substring(0, 13)));
            BillID=String.valueOf(Long.valueOf(AdditionalData.substring(14, 27)));
            PayID=String.valueOf(Long.valueOf(AdditionalData.substring(28, 41)));
            Amount=String.valueOf(Long.valueOf(AdditionalData.substring(42, 52)));
            try{DeadLine=String.valueOf(Long.valueOf(AdditionalData.substring(53, 61)));}catch (Exception e){}
            MAC=receiveString.substring(Index2,Index2+16);

            PersianDate=Shamsi_Date();
            PersianTime=getRequestTime();

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

    private String SendDateTimeToSwitch="";
    public  String getSendDateTimeToSwitch(){
        return SendDateTimeToSwitch;
    }

    private String  PersianDate="";
    public  String  getPersianDate(){
        return PersianDate;
    }

    private String  PersianTime="";
    public  String  getPersianTime(){
        return PersianTime;
    }

    private String ActionCode="";
    public  String getActionCode(){
        return ActionCode;
    }

    private String CallUniqID=null;
    public  void   setCallUniqID(String CallUniqID){
        this.CallUniqID=CallUniqID;
    }
    public  String getCallUniqID(){
        return CallUniqID;
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

    private String CheckoutDate="";
    public  String getCheckoutDate(){
        return CheckoutDate;
    }

    private String IDOfAcceptor="";
    public  String getIDOfAcceptor(){
        return IDOfAcceptor;
    }

    private String IDOfIssuer="";
    public  String getIDOfIssuer(){
        return IDOfIssuer;
    }

    private String ReferenceCode="";
    public  String getReferenceCode(){
        return ReferenceCode;
    }

    private String ResponseAndLicenseTransaction="";
    public  String getResponseAndLicenseTransaction(){
        return ResponseAndLicenseTransaction;
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

    private String IDOfAcceptorBank="";
    public  String getIDOfAcceptorBank(){
        return IDOfAcceptorBank;
    }

    private String PreferredAccountData="";
    public  String getPreferredAccountData(){
        return PreferredAccountData;
    }

    private String  MAC="";
    public  String getMAC(){
        return MAC;
    }

    private String  ReceivedString="";
    public  void setReceivedString(String receivedString){
        ReceivedString=receivedString;
    }
    public  String getReceivedString(){
        return  ReceivedString;
    }


    private String BillID="";
    public  void    setBillID(String billID){
        BillID=billID;
    }
    public  String getBillID(){
        return BillID;
    }

    private String PayID="";
    public  void    setPayID(String payID){
        PayID=payID;
    }
    public  String getPayID(){
        return PayID;
    }

    private String  Amount="";
    public  void    setAmount(String amount){
        Amount=amount;
    }
    public  String getAmount(){
        return Amount ;
    }

    private String TelNo="";
    public  void    setTelNo(String telNo){
        TelNo=telNo;
    }
    public  String getTelNo(){
        return TelNo;
    }

    private String DeadLine="";
    public  void   setDeadLine(String deadLine){
        DeadLine=deadLine;
    }
    public  String getDeadLine(){
        return DeadLine;
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
