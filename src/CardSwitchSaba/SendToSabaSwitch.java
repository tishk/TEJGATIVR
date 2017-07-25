package CardSwitchSaba;

import ServiceObjects.ISO.ISO200;
import ServiceObjects.ISO.ISO210;
import ServiceObjects.ISO.ISO400;
import ServiceObjects.Other.LoggerToDB;
import utils.PropertiesUtils;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

/**
 * Created by Administrator on 6/21/2015.
 */
public class SendToSabaSwitch {
    private String ISOMessage=null;
    private short messageType=-1;

    private ISO200 card200=null;
    private void setCard200(ISO200 Card200){
        card200=Card200;
    }
    public  ISO200 getCard200(){
        return card200;
    }

    private ISO400 card400=null;
    private void setCard400(ISO400 Card400){
        card400=Card400;
    }
    public  ISO400 getCard400(){
        return card400;
    }


    private ISO210 card210=null;
    private void setCard210(ISO210 Card210){
        card210=Card210;
    }
    public  ISO210 getCard210(){
        return card210;
    }

    private String Pann="";
    private void setPann(String pan){
        Pann=pan;
    }
    public  String getPann(){
        return Pann;
    }

    private String Pinn="";
    private void setPinn(String pin){
        Pinn=pin;
    }
    public  String getPinn(){
        return Pinn;
    }

    private String BillIDD="";
    private void setBillIDD(String billID){
        BillIDD=billID;
    }
    public  String getBillIDD(){
        return BillIDD;
    }

    private String PaymentIDD="";
    private void setPaymentIDD(String paymentID){
        PaymentIDD=paymentID;
    }
    public  String getPaymentIDD(){
        return PaymentIDD;
    }

    private String Amountt="";
    private void setAmountt(String amountt){
        Amountt=amountt;
    }
    public  String getAmountt(){
        return Amountt;
    }

    private boolean SendResult=false;
    public  void    setSendResult(boolean sendResult){
        SendResult=sendResult;
    }
    public  boolean getSendResult(){
        return SendResult;
    }

    public SendToSabaSwitch(String pan, String pin) throws UnsupportedEncodingException, SQLException, InterruptedException {
        messageType=0;
        setPann(pan);
        setPinn(pin);
        send();
    }
    public SendToSabaSwitch(String hotCard, String pan, String pin) throws UnsupportedEncodingException, SQLException, InterruptedException {
        messageType=1;
        setPann(pan);
        setPinn(pin);
        send();
    }
    public SendToSabaSwitch(String Pan, String Pin, String BillID, String PaymentID,String Amount) throws SQLException, UnsupportedEncodingException, InterruptedException {
        messageType=2;
        setPann(Pan);
        setPinn(Pin);
        setBillIDD(BillID);
        setPaymentIDD(PaymentID);
        setAmountt(Amount);
        send();
    }
    public SendToSabaSwitch(ISO210 ISO210) throws UnsupportedEncodingException, SQLException, InterruptedException {
        messageType=3;
        setCard210(ISO210);
        send();
    }
    public SendToSabaSwitch(ISO200 ISO200) throws UnsupportedEncodingException, SQLException, InterruptedException {
        messageType=4;
        //ISOMessageBalance("6273531403004496000","17311");
        //ISO200=card200;
        setCard200(ISO200);
        send();
    }
    public SendToSabaSwitch(String pan, String pin, String BillID, String PaymentID, int i) throws UnsupportedEncodingException, SQLException, InterruptedException {
        messageType=5;
        ISOMessageBillPayment(pan, pin, BillID, PaymentID,getAmountt());
        send();
    }

    private void prepareMessage() throws UnsupportedEncodingException, SQLException {
        switch (messageType){
            case 0:ISOMessage = ISOMessageBalance(getPann(), getPinn());
                break;
            case 1:ISOMessage = ISOMessageHotCard(getPann(), getPinn());
                break;
            case 2:ISOMessage = ISOMessageBillPayment(getPann(), getPinn(),getBillIDD(),getPaymentIDD(),getAmountt());
                break;
            case 3:ISOMessage = ISOMessageReverseFor210(getCard210());
                break;
            case 4:ISOMessage = ISOMessageReverseFor200(getCard200());
                break;
            default:
                break;
        }
    }
    private void send() throws UnsupportedEncodingException, SQLException, InterruptedException {
        prepareMessage();
        setSendResult(false);
        int sendCount=1;
        while ((!getSendResult())&&(sendCount++<=3)){
            sendOnSocket();
            Thread.sleep(300);
        }
        if (getSendResult()) saveSentRequestInDB();
        else ;
    }

    private void sendOnSocket() throws InterruptedException {
           this.setSendResult(ConnectionToSaba.sendOnSocket(ISOMessage));
    }
    private void saveSentRequestInDB() throws SQLException {

        switch (messageType){
            case 0:new LoggerToDB(getCard200());
                break;
            case 1:new LoggerToDB(getCard200());
                break;
            case 2:new LoggerToDB(getCard200());
                break;
            case 3:new LoggerToDB(getCard400());
                break;
            case 4:new LoggerToDB(getCard400());
                break;
            default:
                break;
        }

    }

    private String ISOMessageBalance(final String Pan, final String Pin) throws UnsupportedEncodingException, SQLException {

        card200=new ISO200(){{
            setIsBalance(true);
            setPan(Pan);
            setPanWithLen(Pan);
            setPin(Pin);
            seMessageType("0200");
            seBitmapEarly("F67C8081A8E1B000");
            seBitmapSecondary("0000000010000001");
            setTransactionISOCode("310000");
            setAmountCurrencyAcceptor("0");
            setAmountCurrencyCardIssuer("0");
            setSendDateTimeToSwitch();
            setAmountCurrencyConvertRate("1");
            setTraceCode();
            setRegisterTime();
            setRegisterDate();
            setExpireDateOfCard("00","00");
            setProcessDateTime();
            setDeviceCode("07");
            setIDOfPayer();
            setIDOfAcceptor();
            setIDOfPayer();
            setDataOfMagneticTapeTrack2("00");
            setReferenceCode();
            setIDofCardAcceptorTerminal(PropertiesUtils.getMerchandID());
            setCodeOfCardAcceptorTerminal(PropertiesUtils.getMerchandCode());
            setNameAndLocationOfAcceptor("Telephone Bank        TEHRAN       THRIR");
            setAdditionalDataForTransaction("003");
            setCodeOfCurrencyTransactionAccepter("364");
            setCodeOfCurrencyTransactionIssuer("364");
            setIDOfPinCardOwner();
            setValidationCode();
            setEncryptedRequest();
         }};
        return card200.getEncryptedRequest();
    }
    private String ISOMessageHotCard(final String Pan, final String Pin) throws UnsupportedEncodingException, SQLException {

        card200=new ISO200(){{
            setIsBalance(false);
            setPan(Pan);
            setPanWithLen(Pan);
            setPin(Pin);
            seMessageType("0200");
            seBitmapEarly("F67C8081A8E1B000");
            seBitmapSecondary("0000000010000001");
            setTransactionISOCode("940000");
            setAmountCurrencyAcceptor("0");
            setAmountCurrencyCardIssuer("0");
            setSendDateTimeToSwitch();
            setAmountCurrencyConvertRate("1");
            setTraceCode();
            setRegisterTime();
            setRegisterDate();
            setExpireDateOfCard("00","00");
            setProcessDateTime();
            setDeviceCode("07");
            setIDOfPayer();
            setIDOfAcceptor();
            setIDOfPayer();
            setDataOfMagneticTapeTrack2("00");
            setReferenceCode();
            setIDofCardAcceptorTerminal(PropertiesUtils.getMerchandID());
            setCodeOfCardAcceptorTerminal(PropertiesUtils.getMerchandCode());
            setNameAndLocationOfAcceptor("Telephone Bank        TEHRAN       THRIR");
            setAdditionalDataForHotCard();
            setCodeOfCurrencyTransactionAccepter("364");
            setCodeOfCurrencyTransactionIssuer("364");
            setIDOfPinCardOwner();
            setValidationCode();
            setEncryptedRequest();
        }};
        return card200.getEncryptedRequest();
    }
    private String ISOMessageBillPayment(final String Pan,final String Pin,final String billID,final String paymentID,final String amnt) throws UnsupportedEncodingException, SQLException {

        card200=new ISO200(){{
            setIsBalance(false);
            setPan(Pan);
            setPanWithLen(Pan);
            setPin(Pin);
            seMessageType("0200");
            seBitmapEarly("F67C8081A8E1B000");
            seBitmapSecondary("0000000010000001");
            setTransactionISOCode("170000");
            setAmountCurrencyAcceptor(amnt);
            setAmountCurrencyCardIssuer(amnt);
            setSendDateTimeToSwitch();
            setAmountCurrencyConvertRate("1");
            setTraceCode();
            setRegisterTime();
            setRegisterDate();
            setExpireDateOfCard("00","00");
            setProcessDateTime();
            setDeviceCode("07");
            setIDOfPayer();
            setIDOfAcceptor();
            setIDOfPayer();
            setDataOfMagneticTapeTrack2("00");
            setReferenceCode();
            setIDofCardAcceptorTerminal(PropertiesUtils.getMerchandID());
            setCodeOfCardAcceptorTerminal(PropertiesUtils.getMerchandCode());
            setNameAndLocationOfAcceptor("Telephone Bank        TEHRAN       THRIR");
            setAdditionalDataForBillPayment("045", billID,paymentID);
            setCodeOfCurrencyTransactionAccepter("364");
            setCodeOfCurrencyTransactionIssuer("364");
            setIDOfPinCardOwner();
            setValidationCode();
            setEncryptedRequest();
        }};
        return card200.getEncryptedRequest();

    }
    private String ISOMessageReverseFor210(final ISO210 request) throws SQLException {

        card400=new ISO400(){{
            setPan(request.getPan());
            //setPanWithLen(Pan);
            setPin(request.getPin());
            seMessageType("0420");
            seBitmapEarly("F6788001AEE1A000");
            seBitmapSecondary("0000004210000001");
            setTransactionISOCode(request.getTransactionISOCode());
            setAmountCurrencyAcceptor(request.getAmountCurrencyAcceptor());
            setAmountCurrencyCardIssuer(request.getAmountCurrencyCardIssuer());
            setSendDateTimeToSwitch();
            setAmountCurrencyConvertRate(request.getAmountCurrencyConvertRate());
            setTraceCode(request.getTraceCode());
            setRegisterTime(request.getRegisterTime());
            setRegisterDate(request.getRegisterDate());
            setProcessDateTime(request.getProcessDateTimeInDestination());
            setIDOfPayer(request.getIDOfPayer());
            setIDOfAcceptor(request.getIDOfIssuer());
            setDataOfMagneticTapeTrack2(request.getDataOfMagneticTapeTrack2());
            setReferenceCode(request.getReferenceCode());
            setResponseForRecognizedRequest("");
            setResponseCode("");
            setIDofCardAcceptorTerminal(request.getIDofCardAcceptorTerminal());
            setCodeOfCardAcceptorTerminal(request.getIDofCardAcceptorTerminal());
            setNameAndLocationOfAcceptor(request.getNameAndLocationOfAcceptor());
            setAdditionalData(request.getAdditionalData());
            setCodeOfCurrencyTransactionAccepter(request.getCodeOfCurrencyTransactionAccepter());
            setCodeOfCurrencyTransactionIssuer(request.getCodeOfCurrencyTransactionIssuer());
            setPreliminaryDataElements(request.getPreliminaryDataElements());
            setEncryptedRequest();
        }};
        card400=new ISO400(){{
            setPan(request.getPan());
           // setPanWithLen(request.getPanWithLen());
            setPin(request.getPin());
            seMessageType("0420");
            seBitmapEarly("F6788001AEE1A000");
            seBitmapSecondary("0000004210000001");
            setTransactionISOCode("170000");
            setAmountCurrencyAcceptor(request.getAmountCurrencyAcceptor());
            setAmountCurrencyCardIssuer(request.getAmountCurrencyCardIssuer());
            setSendDateTimeToSwitch();
            setAmountCurrencyConvertRate(request.getAmountCurrencyConvertRate());
            setTraceCode(request.getTraceCode());
            setRegisterTime(request.getRegisterTime());
            setRegisterDate(request.getRegisterDate());
            setProcessDateTime(request.getProcessDateTimeInDestination());
            setIDOfPayer(request.getIDOfPayer());
            setIDOfAcceptor(request.getIDOfIssuer());
            setDataOfMagneticTapeTrack2(request.getDataOfMagneticTapeTrack2());
            setReferenceCode(request.getReferenceCode());
            setResponseForRecognizedRequest("123456");
            setResponseCode("12");
            setIDofCardAcceptorTerminal(request.getIDofCardAcceptorTerminal());
          //  setCodeOfCardAcceptorTerminal(request.getCodeOfCardAcceptorTerminal());
            setNameAndLocationOfAcceptor(request.getNameAndLocationOfAcceptor());
            setAdditionalData(request.getAdditionalData());
            setCodeOfCurrencyTransactionAccepter(request.getCodeOfCurrencyTransactionAccepter());
            setCodeOfCurrencyTransactionIssuer(request.getCodeOfCurrencyTransactionIssuer());
            setPreliminaryDataElements("0200"+getTraceCode()+
                    getRegisterDate()+
                    getRegisterTime()+
                    FixLengthWithZero(getIDOfPayer().substring(2),11)+
                    FixLengthWithZero(getIDOfAcceptor().substring(2),11));
            setNewAmount("000000000000000000000000C00000000C00000000");

            setEncryptedRequest();
        }};
        //return card400.getEncryptedRequest();
        return card400.getEncryptedRequest();
    }
    private String ISOMessageReverseFor200(final ISO200 request) throws SQLException {

        card400=new ISO400(){{
            setPan(request.getPan());
            setPanWithLen(request.getPanWithLen());
            setPin(request.getPin());
            seMessageType("0420");
            seBitmapEarly("F6788001AEE1A000");
            seBitmapSecondary("0000004210000001");
            setTransactionISOCode("170000");
            setAmountCurrencyAcceptor(request.getAmountCurrencyAcceptor());
            setAmountCurrencyCardIssuer(request.getAmountCurrencyCardIssuer());
            setSendDateTimeToSwitch();
            setAmountCurrencyConvertRate(request.getAmountCurrencyConvertRate());
            setTraceCode(request.getTraceCode());
            setRegisterTime(request.getRegisterTime());
            setRegisterDate(request.getRegisterDate());
            setProcessDateTime(request.getProcessDateTime());
            setIDOfPayer(request.getIDOfPayer());
            setIDOfAcceptor(request.getIDOfAcceptor());
            setDataOfMagneticTapeTrack2(request.getDataOfMagneticTapeTrack2());
            setReferenceCode(request.getReferenceCode());
            setResponseForRecognizedRequest("123456");
            setResponseCode("12");
            setIDofCardAcceptorTerminal(request.getIDofCardAcceptorTerminal());
            setCodeOfCardAcceptorTerminal(request.getCodeOfCardAcceptorTerminal());
            setNameAndLocationOfAcceptor(request.getNameAndLocationOfAcceptor());
            setAdditionalData(request.getAdditionalData());
            setCodeOfCurrencyTransactionAccepter(request.getCodeOfCurrencyTransactionAccepter());
            setCodeOfCurrencyTransactionIssuer(request.getCodeOfCurrencyTransactionIssuer());
            setPreliminaryDataElements("0200"+getTraceCode()+
                                       getRegisterDate()+
                                       getRegisterTime()+
                                       FixLengthWithZero(getIDOfPayer().substring(2),11)+
                                       FixLengthWithZero(getIDOfAcceptor().substring(2),11));
            setNewAmount("000000000000000000000000C00000000C00000000");

            setEncryptedRequest();
        }};
        return card400.getEncryptedRequest();
    }
}
