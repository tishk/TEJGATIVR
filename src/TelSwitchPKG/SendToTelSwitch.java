package TelSwitchPKG;

import ServiceObjects.ISO.ISO100;
import ServiceObjects.Other.LoggerToDB;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

/**
 * Created by Administrator on 6/21/2015.
 */
public class SendToTelSwitch {
    private String ISOMessage=null;
    private short messageType=-1;

    private ISO100 card100=null;
    private void   setCard100(ISO100 Card100){
        card100=Card100;
    }
    public  ISO100 getCard100(){
        return card100;
    }

    private boolean IsGetInfoMessage=false;
    private void    setIsGetInfoMessage(boolean isGetInfoMessage){
        IsGetInfoMessage=isGetInfoMessage;
    }
    private boolean getIsGetInfoMessage(){
        return IsGetInfoMessage;
    }

    private boolean IsMobileInfo=false;
    private void    setIsMobileInfo(boolean isMobileInfo){
        IsMobileInfo=isMobileInfo;
    }
    private boolean getIsMobileInfo(){
        return IsMobileInfo;
    }


    private String Pann="";
    private void setPann(String pan){
        Pann=pan;
    }
    public String getPann(){
        return Pann;
    }

    private String Pinn="";
    private void setPinn(String pin){
        Pinn=pin;
    }
    public String getPinn(){
        return Pinn;
    }

    private String BillIDd="";
    private void setBillIDd(String billID){
        BillIDd=billID;
    }
    public String getBillIDd(){
        return BillIDd;
    }

    private String PaymentIDd="";
    private void setPaymentIDd(String paymentID){
        PaymentIDd=paymentID;
    }
    public String getPaymentIDd(){
        return PaymentIDd;
    }


    private String TelKind="";
    private void setTelKindd(String telKind){
        TelKind=telKind;
    }
    public String getTelKindd(){
        return TelKind;
    }

    private String PayIDd="";
    private void setPayIDd(String payID){
        PayIDd=payID;
    }
    public String getPayIDd(){
        return PayIDd;
    }

    private String TraceCode="";
    private void setTraceCode(String traceCode){
        TraceCode=traceCode;
    }
    public String getTraceCode(){
        return TraceCode;
    }

    private String ReferenceCodee="";
    private void setReferenceCodee(String referenceCodee){
        ReferenceCodee=referenceCodee;
    }
    public String getReferenceCodee(){
        return ReferenceCodee;
    }

    private String PayDatee="";
    private void setPayDatee(String payDate){
        PayDatee=payDate;
    }
    public String getPayDatee(){
        return PayDatee;
    }

    private String TelNo="";
    private void setTelNoo(String telNo){
        TelNo=telNo;
    }
    public String getTelNoo(){
        return TelNo;
    }

    private boolean SendResult=false;
    public  void    setSendResult(boolean sendResult){
        SendResult=sendResult;
    }
    public  boolean getSendResult(){
        return SendResult;
    }


    public SendToTelSwitch(String pan,
                           boolean isMobile,
                           String telNo) throws UnsupportedEncodingException, SQLException, InterruptedException {
        messageType=0;
        setIsGetInfoMessage(true);
        setPann(pan);
        if (isMobile) setTelKindd("MC");else setTelKindd("TC");
        setIsMobileInfo(isMobile);
        setTelNoo(telNo);
        send();
    }
    public SendToTelSwitch(String pan,
                           String traceCode,
                           String referenceCode,
                           String billID,
                           String payID,
                           String payDate,
                           String telNo,
                           boolean isMobile ) throws UnsupportedEncodingException, SQLException, InterruptedException {
        messageType=1;
        setIsGetInfoMessage(false);
        setPann(pan);
        setTraceCode(traceCode);
        setReferenceCodee(referenceCode);
        if (isMobile) setTelKindd("MC");else setTelKindd("TC");
        setBillIDd(billID);
        setPayIDd(payID);
        setPayDatee(payDate);
        setTelNoo(telNo);
        send();
    }

    private void prepareMessage() throws UnsupportedEncodingException, SQLException {
        switch (messageType){
            case 0:ISOMessage = ISOMessageGetNOData();
                break;
            case 1:ISOMessage = ISOMessagePaymentResult();
                break;
            default:
                break;
        }
    }
    private void send()   {
        try{
            prepareMessage();
            setSendResult(false);
            int sendCount=1;
            while ((!getSendResult())&&(sendCount++<=3)){
                sendOnSocket();
                Thread.sleep(300);
            }
            if (getSendResult()) saveSentRequestInDB();
            else ;
        }catch (Exception e){
            System.out.println("here : " + e.toString());
        }


    }
    private void sendOnSocket() throws InterruptedException {
     this.setSendResult(ConnectionToTelSwitch.sendOnSocket(ISOMessage));
    }
    private void saveSentRequestInDB() throws SQLException {
        new LoggerToDB(getCard100());
    }

    private String ISOMessagePaymentResult() throws UnsupportedEncodingException, SQLException {

        card100=new ISO100(){{
            setIsGetInfo(getIsGetInfoMessage());
            setPan(getPann());
            setPanWithLen(getPann());
            seMessageType("0100");
            seBitmapEarly("E238808188C10000");
            seBitmapSecondary("0000000010000001");
            setTransactionISOCode("980000");
            setSendDateTimeToSwitch();
            setTraceCode();
            setRegisterTime();
            setRegisterDate();
            setProcessDateTime();
            setDeviceCode("07");
            setIDOfAcceptor();
            setIDOfIssuer();
            setReferenceCode(getReferenceCodee());
            setIDofCardAcceptorTerminal("00000000000A000");
            setCodeOfCardAcceptor("V000A000");
            setTelKind(getTelKindd());
            setBillID(getBillIDd());
            setPayID(getPayIDd());
            setPayDate(getPayDatee());
            setTelNo(getTelNoo());
            setValidationCode();
            setEncryptedRequest();
         }};
        return card100.getEncryptedRequest();
    }
    private String ISOMessageGetNOData() throws UnsupportedEncodingException, SQLException {

        card100=new ISO100(){{
            setIsMobile(getIsMobileInfo());
            setIsGetInfo(getIsGetInfoMessage());
            setPan(getPann());
            setPanWithLen(getPann());
            seMessageType("0100");
            seBitmapEarly("E238808188C10000");
            seBitmapSecondary("0000000010000001");
            setTransactionISOCode("930000");
            setSendDateTimeToSwitch();
            setTraceCode();
            setRegisterTime();
            setRegisterDate();
            setProcessDateTime();
            setDeviceCode("07");
            setIDOfAcceptor();
            setIDOfIssuer();
            //setReferenceCode(getReferenceCode());
            setReferenceCode(getReferenceCode());
            setIDofCardAcceptorTerminal("00000000000A000");
            setCodeOfCardAcceptor("V000A000");
            setTelKind(getTelKindd());
            setTelNo(getTelNoo());
            setValidationCode();
            setEncryptedRequest();
        }};

        return card100.getEncryptedRequest();
    }


}
