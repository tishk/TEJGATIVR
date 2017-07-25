package ServiceObjects.ISO;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESKeySpec;
import javax.crypto.spec.IvParameterSpec;
import java.io.ByteArrayOutputStream;
import java.io.Serializable;
import java.io.UnsupportedEncodingException;
import java.security.spec.KeySpec;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.TimeZone;

public class ISO400  implements Serializable {
    private static final long serialVersionUID = 6527733398267757690L;
    public ISO400(){

    }

    //-----------Security---------------------------------
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
    private String GetBillTypeID(String BillID){
        String Result = "00";
        int BillIDType=0;
        try{
            int Len=BillID.length();
            BillIDType=Integer.valueOf(BillID.substring(Len-2,Len-1));
        }catch (Exception e){

        }

        switch (BillIDType){
            case 1:Result ="WA";
                break;
            case 2:Result ="EL";
                break;
            case 3:Result ="GA";
                break;
            case 4:Result ="TC";
                break;
            case 5:Result ="MC";
                break;
            case 6:Result ="MN";
                break;
            case 7:Result ="MN";
                break;
            default:Result="00";
        }

        return Result;
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

    private   String CodeString(String str, String strKey) {
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
    private   String PinBlockDesEncryption(String pan, String pin,String key) {
        try {
            return encrypt(GetAnsiPinBlock(pan,pin),key);
        } catch (UnsupportedEncodingException e) {
            return "0000000000000000";
        }
    }
    //----------Security-------------------------------------
    public  String FixLengthWithZero(String Str,int length){
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
            if (PAN.substring(0,6).equals("585983")) return "06"+"585983";
            else return "06"+"585983";
        }catch (Exception e){
            return "06"+"585983";
        }

    }
    private String GetSourceBankID(String PAN){
        try {
            return "06"+PAN.substring(0,6);

        }catch (Exception e){
            return "06-1-1-1";
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
    //------------------Fields--------------------------------------------------

    private BitMap bitMap=null;
    public  void   setBitMap(BitMap bitMap){

    }
    public  BitMap getBitMap(){
        return this.bitMap;
    }

    private String MessageType="";
    public  void    seMessageType(String messageType){
        MessageType=messageType;
    }
    public  String getMessageType(){
        return MessageType;
    }
    public  int    getMessageTypeLength(){return 4;}

    private String BitmapEarly="";
    public  void    seBitmapEarly(String bitmapEarly){
        BitmapEarly=bitmapEarly;
    }
    public  String getBitmapEarly(){
        return BitmapEarly;
    }
    public  int    getBitmapEarlyLength(){return 16;}

    private String BitmapSecondary="";
    public  void    seBitmapSecondary(String bitmapSecondary){
        BitmapSecondary=bitmapSecondary;
    }
    public  String getBitmapSecondary(){
        return BitmapSecondary;
    }
    public  int    getBitmapSecondaryLength(){return 16;}


    private String PanWithLen="";
    public  void    setPanWithLen(String pan){
        PanWithLen=pan;
    }

    private String CallUniqID=null;
    public  void   setCallUniqID(String CallUniqID){
        this.CallUniqID=CallUniqID;
    }
    public  String getCallUniqID(){
        return CallUniqID;
    }

    private String Pan="";
    public  void    setPan(String pan){
        Pan=pan;
    }
    public  String getPan(){
        return Pan;
    }
    public  String getPanWithLen(){
        return String.valueOf(Pan.length())+Pan;
    }

    private String TransactionISOCode="";
    public  void    setTransactionISOCode(String transactionISOCode){
        TransactionISOCode=transactionISOCode;
    }
    public  String getTransactionISOCode(){
        return TransactionISOCode;
    }
    public  int    getTransactionISOCoderLength(){return 6;}

    private String AmountCurrencyAcceptor="";
    public  void    setAmountCurrencyAcceptor(String amountCurrencyAcceptor){
        AmountCurrencyAcceptor=FixLengthWithZero(amountCurrencyAcceptor,12);
    }
    public  String getAmountCurrencyAcceptor(){
        return AmountCurrencyAcceptor;
    }
    public  int    getAmountCurrencyAcceptorLength(){return 12;}

    private String AmountCurrencyCardIssuer="";
    public  void    setAmountCurrencyCardIssuer(String amountCurrencyCardIssuer){
        AmountCurrencyCardIssuer=FixLengthWithZero(amountCurrencyCardIssuer,12);
    }
    public  String getAmountCurrencyCardIssuer(){
        return AmountCurrencyCardIssuer;
    }
    public  int    getAmountCurrencyCardIssuerLength(){return 12;}


    private String SendDateTimeToSwitch="";
    public  void    setSendDateTimeToSwitch(){
        SendDateTimeToSwitch=GetGMTNowTime();
    }
    public  String getSendDateTimeToSwitch(){
        return SendDateTimeToSwitch;
    }

    private String AmountCurrencyConvertRate="";
    public  void    setAmountCurrencyConvertRate(String amountCurrencyConvertRate){
        AmountCurrencyConvertRate=FixLengthWithZero(amountCurrencyConvertRate,8);
    }
    public  String getAmountCurrencyConvertRate(){
        return AmountCurrencyConvertRate;
    }
    public  int    getAmountCurrencyConvertRateLength(){return 8;}

    private String TraceCode="";
    public  void    setTraceCode(String traceCode ){
        TraceCode=traceCode;
    }
    public  String getTraceCode(){
        return TraceCode;
    }


    private String RegisterTime="";
    public  void    setRegisterTime(String registerTime){
        RegisterTime=registerTime;
    }
    public  String getRegisterTime(){
        return RegisterTime;
    }


    private String RegisterDate="";
    public  void    setRegisterDate(String registerDate){
        RegisterDate=registerDate;
    }
    public  String getRegisterDate(){
        return RegisterDate;
    }

    private String ProcessDateTime="";
    public  void    setProcessDateTime(String processDateTime){
        ProcessDateTime=processDateTime;
    }
    public  String getProcessDateTime(){
        return ProcessDateTime;
    }

    private String IDOfPayer="";
    public  void    setIDOfPayer(String idOfPayer){
        IDOfPayer=idOfPayer;
    }
    public  String getIDOfPayer(){
        return IDOfPayer;
    }

    private String IDOfAcceptor="";
    public  void    setIDOfAcceptor(String idOfAcceptor){
        IDOfAcceptor=idOfAcceptor;
    }
    public  String getIDOfAcceptor(){
        return IDOfAcceptor;
    }

    private String DataOfMagneticTapeTrack2="";
    public  void    setDataOfMagneticTapeTrack2(String dataOfMagneticTapeTrack2){
        DataOfMagneticTapeTrack2=dataOfMagneticTapeTrack2;
    }
    public  String getDataOfMagneticTapeTrack2(){
        return DataOfMagneticTapeTrack2;
    }

    private String ReferenceCode="";
    public  void    setReferenceCode(String referenceCode){
        ReferenceCode=referenceCode;
    }
    public  String getReferenceCode(){
        return ReferenceCode;
    }

    private String ResponseForRecognizedRequest="";
    public  void    setResponseForRecognizedRequest(String responseForRecognizedRequest){
        ResponseForRecognizedRequest=responseForRecognizedRequest;
    }
    public  String getResponseForRecognizedRequest(){
        return ResponseForRecognizedRequest;
    }

    private String ResponseCode="";
    public  void    setResponseCode(String responseCode){
        ResponseCode=responseCode;
    }
    public  String getResponseCode(){
        return ResponseCode;
    }

    private String IDofCardAcceptorTerminal="";
    public  void    setIDofCardAcceptorTerminal(String idOfCardAcceptorTerminal){
        IDofCardAcceptorTerminal=idOfCardAcceptorTerminal;
    }
    public  String getIDofCardAcceptorTerminal(){
        return IDofCardAcceptorTerminal;
    }

    private String CodeOfCardAcceptorTerminal="";
    public  void    setCodeOfCardAcceptorTerminal(String codeOfCardAcceptorTerminal){
        CodeOfCardAcceptorTerminal=codeOfCardAcceptorTerminal;
    }
    public  String getCodeOfCardAcceptorTerminal(){
        return CodeOfCardAcceptorTerminal;
    }


    private String NameAndLocationOfAcceptor="";
    public  void    setNameAndLocationOfAcceptor(String nameAndLocationOfAcceptor){
        NameAndLocationOfAcceptor=FixLengthWithSpace(nameAndLocationOfAcceptor,40);
    }
    public  String getNameAndLocationOfAcceptor(){
        return NameAndLocationOfAcceptor;
    }

    private String AdditionalData="";
    public  void    setAdditionalData(String additionalData){
        AdditionalData=additionalData;
    }
    public  String getAdditionalData(){
        return AdditionalData;
    }

    private String CodeOfCurrencyTransactionAccepter="";
    public  void    setCodeOfCurrencyTransactionAccepter(String codeOfCurrencyTransactionAccepter){
        CodeOfCurrencyTransactionAccepter=codeOfCurrencyTransactionAccepter;
    }
    public  String getCodeOfCurrencyTransactionAccepter(){
        return CodeOfCurrencyTransactionAccepter;
    }

    private String CodeOfCurrencyTransactionIssuer="";
    public  void    setCodeOfCurrencyTransactionIssuer(String codeOfCurrencyTransactionIssuer){
        CodeOfCurrencyTransactionIssuer=codeOfCurrencyTransactionIssuer;
    }
    public  String getCodeOfCurrencyTransactionIssuer(){
        return CodeOfCurrencyTransactionIssuer;
    }

    private String PreliminaryDataElements="";
    public  void    setPreliminaryDataElements(String preliminaryDataElements){
        PreliminaryDataElements=preliminaryDataElements;
    }
    public  String getPreliminaryDataElements(){
        return PreliminaryDataElements;
    }

    private String  NewAmount="";
    public  void    setNewAmount(String newAmount){
        NewAmount=newAmount;
    }
    public  String getNewAmount(){
        return NewAmount;
    }

    private String  SentString="";
    public  void    setSentString(String sentString){
        SentString=sentString;
    }
    public  String getSentString(){
        return SentString;
    }

    //----------Other field--------------------------

    private String Pin="";
    public  void    setPin(String pin){
        Pin=pin;
    }
    public  String getPin(){
        return Pin;
    }

    private String  RequestString="";
    public  void    setRequestString(){
        String Request=null;
        try {

            Request=getMessageType()+//ISO
                    getBitmapEarly()+//ISO
                    getBitmapSecondary()+//P1
                    getPanWithLen()+//P2
                    getTransactionISOCode()+//P3
                    getAmountCurrencyAcceptor()+//P4
                    getAmountCurrencyCardIssuer()+//P6
                    getSendDateTimeToSwitch()+//P7
                    getAmountCurrencyConvertRate()+//P10
                    getTraceCode()+//P11
                    getRegisterTime()+//P12
                    getRegisterDate()+//P13
                    getProcessDateTime()+//P17
                    getIDOfPayer()+//P32
                    getIDOfAcceptor()+//P33
                    getDataOfMagneticTapeTrack2()+//P35
                    getReferenceCode()+//P37
                    getResponseForRecognizedRequest()+
                    getResponseCode()+
                    getIDofCardAcceptorTerminal()+//P41
                    getCodeOfCardAcceptorTerminal()+
                    getNameAndLocationOfAcceptor()+//P43
                    getAdditionalData()+//P48
                    getCodeOfCurrencyTransactionAccepter()+//P49
                    getCodeOfCurrencyTransactionIssuer()+//P51
                    getPreliminaryDataElements()+//S90
                    getNewAmount()+//S95
                    getIDOfAcceptor();//S100
            RequestString=Request;

        } catch (Exception e) {
            EncryptedRequest="!";
        }
    }
    public  String getRequestString(){
        return RequestString ;
    }


    private String  EncryptedRequest="";
    public  void    setEncryptedRequest(){
        String RequestEncrypted=null;

        try {
            setRequestString();

            String Request=getRequestString();
            //System.out.println("new part 1 : "+Request);
            RequestEncrypted=CodeString(Request, "1C1C1C1C1C1C1C1C");
            Request= Request+RequestEncrypted;
            Request= FixLengthWithZero(String.valueOf(Request.length()),4)+Request;
            // System.out.println("new part 2 : "+Request);
            EncryptedRequest=Request;
        } catch (Exception e) {
            EncryptedRequest="!";
        }
    }
    public  String getEncryptedRequest(){
        return EncryptedRequest;
    }

    private String  AdditionalDataForBillPayment="";
    public  void    setAdditionalDataForBillPayment(String Len,String BillID,String PaymentID){
        AdditionalDataForBillPayment=Len+FixLengthWithSpace(GetBillTypeID(BillID)+BillID+"="+PaymentID,Integer.valueOf(Len));
    }
    public  String getAdditionalDataForBillPayment(){
        return AdditionalDataForBillPayment;
    }

    private String  AdditionalDataForTransaction="";
    public  void    setAdditionalDataForTransaction(String Len){
        AdditionalDataForTransaction=Len+FixLengthWithZero("",Integer.valueOf(Len));
    }
    public  String getAdditionalDataForTransaction(){
        return AdditionalDataForTransaction;
    }



}
