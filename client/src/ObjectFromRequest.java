import ServiceObjects.Account.*;
import ServiceObjects.Other.BillInfoByTelNumber;
import ServiceObjects.Other.BillPaySayByTelNumber;
import ServiceObjects.Other.MonitoringStatus;
import ServiceObjects.Other.SMSAlarmTransaction;
import ServiceObjects.Pan.BalanceForCard;
import ServiceObjects.Pan.BillPayByBillIDPan;
import ServiceObjects.Pan.BlockCard;
import ServiceObjects.Pin.AuthenticatePin1;
import ServiceObjects.Pin.AuthenticatePin2;

import javax.xml.crypto.Data;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.Date;

/**
 * Created by root on 5/16/16.
 */
public class ObjectFromRequest {
    private Socket socket=null;
    Socket socketToGateway;
    BufferedReader in = null;
    String input = null;
    String inputForGateway = null;
    InputStreamReader ioe = null;
    BufferedReader bufferedreader = null;
    PrintWriter printwriter=null;


    private strUtils strutils=new strUtils();
    private PersianDateTimeFromat persianDateTimeFromat=new PersianDateTimeFromat();

    private Object requestObject=null;
    private void setRequestObject(Object object){
        this.requestObject=object;
    }


    private String request="";

    private String cid="";
    private String msgSequence="";
    private String channelNO="";
    private String macAddress="";
    private String deviceCode="";
    private String pcNO="0000";
    private String kind="!";
    private String subKind="!";
    private String tempRequest="";
    private String callUniqID="";
    public ObjectFromRequest(String request){
        String temp[]=request.split(";");
        this.request=temp[1];
        this.tempRequest=temp[1];
        callUniqID=temp[0];

        processRequest();
    }
    private void setKindOfRequest(){


        try{

            msgSequence=strutils.leftString(tempRequest,15);
            channelNO=strutils.midString(tempRequest,4,3);
            tempRequest=strutils.rightString(tempRequest,tempRequest.length()-15);
            cid=strutils.leftString(tempRequest,18);
            deviceCode=strutils.midString(tempRequest,19,2);
            tempRequest=strutils.rightString(tempRequest,tempRequest.length()-20);
            macAddress=strutils.leftString(tempRequest,17);
            tempRequest=strutils.rightString(tempRequest,tempRequest.length()-17);
            kind=String.valueOf(tempRequest.charAt(0)).toLowerCase();
            subKind=String.valueOf(tempRequest.charAt(1)).toLowerCase();
        }catch (Exception e){

        }
    }

    private void processRequest(){
      setKindOfRequest();
        switch (kind){
          case "x":panOperation();
              break;
          case "m":telOperation();
              break;
          case "j":accountBlock();
              break;
          case "h":accountPanOfAccount();
              break;
          case "z":panAccountOfPant();
              break;
          case "b":accountBalance();
              break;
          case "d":accountGetTransactionName();
              break;
          case "r":accountChangePin();
              break;
          case "i":accountCalculateIBAN();
              break;
          case "t":accountTransaction();
              break;
          case "k":accountChequeStatus();
              break;
          case "s":accountGetPrivilageInfo();
              break;
          case "v":accountAvacasAuth();
              break;
          case "o":accountORM();
              break;
          case "e":accountFundTransfer();
              break;
          case "c":accountInstallment();
              break;
          case "f":accountFundTransferIdent();
              break;
          case "p":accountFollowUp();
              break;
          case "g":accountBillPayment();
              break;
          case "q":telSwitchGetInfo();
              break;
          case "u":telSwitchSendPayInfo();
              break;
          case "l":saveLog();
              break;
          case "n":accountGetService();
              break;
          case "w":monitoringRequest();
              break;
          default:failRequest();
      }

    }

    private void monitoringRequest() {

        MonitoringStatus monitoringStatus=new MonitoringStatus();
        monitoringStatus.setReportTime(new Timestamp((new Date()).getTime()));
        setRequestObject(monitoringStatus);

    }


    private void accountGetService(){
      setRequestObject((Object) "@"+msgSequence+"48");
    }
    private void saveLog(){
        setRequestObject((Object) "#"+msgSequence+"0000"+msgSequence);
    }
    private void accountGetPrivilageInfo(){
       String ans="1111000000000000       00000000000000000000000000000000000000000000000000111";
       setRequestObject((Object) "*"+msgSequence+"0000"+ans);
    }
    private void failRequest(){
        setRequestObject((Object) "!"+msgSequence+"9999");
    }

    private void accountBalance(){

        String accountNO=strutils.rightString(tempRequest,tempRequest.length()-1);
        accountNO=strutils.fixLengthWithZero(accountNO,10);
        BalanceForAccount balanceForAccount=new BalanceForAccount();
        balanceForAccount.setAccountNumber(accountNO);
        balanceForAccount.setCallUniqID(callUniqID);
        setRequestObject(balanceForAccount);

    }
    private void accountChangePin(){

        //3260130736740470000000021889015330700-0B-CD-AF-0F-8CR343553108200001360136500001360136010808
        //3260100733725070000000021888969900700-0B-CD-AF-0F-8CR343553108200000000922100000000922200404

        String temp=strutils.rightString(tempRequest,tempRequest.length()-1);
        char changePinType=(strutils.rightString(temp,5)).charAt(0);
        boolean isPin1=false;
        if (changePinType=='0') isPin1=true;

        String accountNo=strutils.leftString(temp,10);
        int newPassLen= Integer.valueOf(strutils.midString(temp,temp.length()-1,2));
        int oldPassLen= Integer.valueOf(strutils.midString(temp,temp.length()-3,2));

        String oldPass=strutils.fixLengthWithZero(String.valueOf(Integer.valueOf(strutils.midString(temp,11,12))),oldPassLen);
//        String oldPass=String.valueOf(Integer.valueOf(strutils.midString(temp,11,12)));
        String newPass=strutils.fixLengthWithZero(String.valueOf(Integer.valueOf(strutils.midString(temp,23,12))),newPassLen);
//        String newPass=String.valueOf(Integer.valueOf(strutils.midString(temp,23,12)));
        if (isPin1){
            AuthenticatePin1 authenticatePin1=new AuthenticatePin1();
            authenticatePin1.setAccountNumber(accountNo);
            authenticatePin1.setPin(oldPass);
            authenticatePin1.setPin_New(newPass);
            authenticatePin1.setCallerID(cid);
            authenticatePin1.setDoChangePin(true);
            authenticatePin1.setCallUniqID(callUniqID);
            setRequestObject(authenticatePin1);
        }
        else{
            AuthenticatePin2 authenticatePin2=new AuthenticatePin2();
            authenticatePin2.setAccountNumber(accountNo);
            authenticatePin2.setPin(oldPass);
            authenticatePin2.setPin_New(newPass);
            authenticatePin2.setCallerID(cid);
            authenticatePin2.setDoChangePin(true);
            authenticatePin2.setCallUniqID(callUniqID);
            setRequestObject(authenticatePin2);
        }


    }
    private void accountTransaction(){

        String temp=strutils.rightString(tempRequest,tempRequest.length()-1);
        String transCount=strutils.leftString(temp,3);
        String accountNo=strutils.midString(temp,4,10);
        String fromDate="13"+strutils.midString(temp,14,6);
        String toDate="13"+strutils.midString(temp,20,6);
        String statementType=strutils.midString(temp,26,1);
        String maxMount="";
        String minMount="";
        String faxType="";
        if (temp.length()>27){
            maxMount=strutils.midString(temp,27,15);
            minMount=strutils.midString(temp,42,15);
            faxType=strutils.midString(temp,58,1);
        }else{
            maxMount="";
            minMount="";
            faxType="";
        }
        if (Long.valueOf(maxMount)==0) maxMount="";
        else maxMount=String.valueOf(Long.valueOf(maxMount));
        if (Long.valueOf(minMount)==0) minMount="";
        else minMount=String.valueOf(Long.valueOf(minMount));
        if (Long.valueOf(faxType)==0) faxType="";
        else faxType=String.valueOf(Long.valueOf(faxType));

        if (!maxMount.equals("")) maxMount=strutils.fixLengthWithZero(maxMount,18);
        if (!minMount.equals("")) minMount=strutils.fixLengthWithZero(minMount,18);

        if (Integer.valueOf(fromDate)>Integer.valueOf(persianDateTimeFromat.getRequestDate()))
            fromDate=persianDateTimeFromat.getRequestDate();

        if (Integer.valueOf(toDate)<Integer.valueOf(persianDateTimeFromat.getRequestDate()))
            toDate=persianDateTimeFromat.getRequestDate();

        if (Integer.valueOf(fromDate)<13800000)
            fromDate=persianDateTimeFromat.getRequestDate(365);
        if (Integer.valueOf(toDate)<13800000)
            toDate=persianDateTimeFromat.getRequestDate();

        Transaction transaction=new Transaction();
        transaction.setTransactionCount(transCount);
        transaction.setAccountNumber(accountNo);
        transaction.setStartDate(fromDate);
        transaction.setEndDate(toDate);
        transaction.setTransactionMaxAmount(maxMount);
        transaction.setTransactionMinAmount(minMount);
        transaction.setStatementType(statementType);
        transaction.setCallUniqID(callUniqID);

        setRequestObject(transaction);

    }
    private void accountORM(){
        switch (subKind){
            case "a":accountPin1Auth();
                break;
            case "s":accountAvacasSaveClearPrint();
                break;
            default:failRequest();
        }
    }
    private void accountPin1Auth(){

        String temp=strutils.rightString(tempRequest,tempRequest.length()-2);
        String accountNO=strutils.leftString(temp,10);
        String pin=strutils.rightString(temp,10);
        String len=strutils.leftString(pin,2);
        pin=strutils.rightString(pin,Integer.valueOf(len));
        AuthenticatePin1 authenticatePin1=new AuthenticatePin1();
        authenticatePin1.setAccountNumber(accountNO);
        authenticatePin1.setPin(pin);
        authenticatePin1.setDoChangePin(false);
        authenticatePin1.setCallerID(strutils.rightString(cid,11));
        authenticatePin1.setCallUniqID(callUniqID);
        setRequestObject(authenticatePin1);
    }

    private void freeResources(){
        try {socket.close();} catch (IOException e) {}
        try {in.close();} catch (IOException e) {}
        try {socketToGateway.close();} catch (IOException e) {}
        try {ioe.close();} catch (IOException e) {}
        try {bufferedreader.close();} catch (IOException e) {}
        try
        {
            printwriter.close();
            socket=null;socketToGateway=null;ioe=null;bufferedreader=null;in=null;printwriter=null;
        } catch (Exception e){}
        System.gc();

    }
    private void telOperation(){
        switch (subKind) {
            case "a":telSetTell();
                break;
            case "g":telGetMob();
                break;
            case "s":telSetMob();
                break;
            default:failRequest();
        }
    }
    private void telSetTell(){

    }
    private void telGetMob(){

    }
    private void telSetMob(){

        String temp=strutils.rightString(tempRequest,tempRequest.length()-2);
        String type=strutils.leftString(temp,1);
        String accountNO=strutils.midString(temp,2,10);
        String mobile=strutils.fixLengthWithZero(strutils.rightString(temp,10),11);

        SMSAlarmTransaction smsAlarmTransaction=new SMSAlarmTransaction();
        smsAlarmTransaction.setAccountNumber(accountNO);
        smsAlarmTransaction.setMobileNumber(mobile);

        if (type.toLowerCase().equals("d")) smsAlarmTransaction.setIsDeleteMobileNumber(true);
        if (type.toLowerCase().equals("i")) smsAlarmTransaction.setIsDeleteMobileNumber(false);
        smsAlarmTransaction.setCallUniqID(callUniqID);
        setRequestObject(smsAlarmTransaction);


    }
    private void accountCalculateIBAN(){

        String accountNO=strutils.rightString(tempRequest,tempRequest.length()-1);
        ShebaID shebaID=new ShebaID();
        shebaID.setAccountNumber(accountNO);
        shebaID.setCallUniqID(callUniqID);
        setRequestObject(shebaID);

    }
    private void accountPanOfAccount(){

        String accountNO=strutils.rightString(tempRequest,tempRequest.length()-1);
        CardNoOfAccount cardNoOfAccount=new CardNoOfAccount();
        cardNoOfAccount.setAccountNumber(accountNO);
        cardNoOfAccount.setCallUniqID(callUniqID);
        setRequestObject(cardNoOfAccount);

    }
    private void accountAvacasAuth(){
        switch (subKind){
            case "a":accountPin2Auth();
                break;
            case "s":accountAvacasSaveClearPrint();
                break;
            default:failRequest();
        }
    }
    private void accountPin2Auth(){

        String temp=strutils.rightString(tempRequest,tempRequest.length()-2);
        String tempAcc=strutils.leftString(temp, 10);
        String accountNO=String.valueOf(Long.valueOf(strutils.fixLengthWithZero(tempAcc,10)));
        String pin2=strutils.rightString(temp,temp.length()-10);
        String[] tempList=pin2.split("#");
        pin2=tempList[1];
        AuthenticatePin2 authenticatePin2=new AuthenticatePin2();
        authenticatePin2.setAccountNumber(accountNO);
        authenticatePin2.setPin(pin2);
        authenticatePin2.setCallerID(cid);
        authenticatePin2.setDoChangePin(false);
        authenticatePin2.setCallUniqID(callUniqID);
        setRequestObject(authenticatePin2);

    }
    private void accountAvacasSaveClearPrint(){

    }
    private void telSwitchGetInfo(){
        switch (subKind){
            case "m" :telSwitchGetMobileBillInfo();
                break;
            default:telSwitchGetTelBillInfo();
        }
    }
    private void telSwitchGetMobileBillInfo(){

        String temp=strutils.rightString(tempRequest,tempRequest.length()-2);
        String pan=strutils.rightString(temp,16);
        temp=strutils.rightString(temp,temp.length()-16);
        String mob=strutils.leftString(temp,13);

        BillInfoByTelNumber billInfoByTelNumber=new BillInfoByTelNumber();
        billInfoByTelNumber.setPan(pan);
        billInfoByTelNumber.setIsMobile(true);
        billInfoByTelNumber.setTelNo(mob);
        billInfoByTelNumber.setCallUniqID(callUniqID);

        setRequestObject(billInfoByTelNumber);
    }
    private void telSwitchGetTelBillInfo(){

        String temp=strutils.rightString(tempRequest,tempRequest.length()-1);
        String pan=strutils.leftString(temp, 16);
        temp=strutils.rightString(temp,temp.length()-16);
        String tel=strutils.leftString(temp,13);

        BillInfoByTelNumber billInfoByTelNumber=new BillInfoByTelNumber();
        billInfoByTelNumber.setPan(pan);
        billInfoByTelNumber.setIsMobile(false);
        billInfoByTelNumber.setTelNo(tel);
        billInfoByTelNumber.setCallUniqID(callUniqID);

        setRequestObject(billInfoByTelNumber);

    }
    private void telSwitchSendPayInfo(){
        BillPaySayByTelNumber billPaySayByTelNumber=new BillPaySayByTelNumber();
        String type=strutils.leftString(tempRequest, 1);

        if (type.equals("m")){
            tempRequest=strutils.rightString(tempRequest,tempRequest.length()-1);
            billPaySayByTelNumber.setIsMobile(true);
        }
        else {
            type="";
            billPaySayByTelNumber.setIsMobile(false);
        }

        String pan=strutils.rightString(tempRequest,16);
        tempRequest=strutils.rightString(tempRequest,tempRequest.length()-16);
        String billID=strutils.leftString(tempRequest,13);
        tempRequest=strutils.rightString(tempRequest,tempRequest.length()-13);
        String payID=strutils.leftString(tempRequest,13);
        tempRequest=strutils.rightString(tempRequest,tempRequest.length()-13);
        String traceID=strutils.leftString(tempRequest,6);
        tempRequest=strutils.rightString(tempRequest,tempRequest.length()-6);
        String switchMessageID=strutils.leftString(tempRequest,12);
        tempRequest=strutils.rightString(tempRequest,tempRequest.length()-12);
        String date=strutils.leftString(tempRequest,6);
        tempRequest=strutils.rightString(tempRequest,tempRequest.length()-6);
        String tel=strutils.rightString(tempRequest,13);

        billPaySayByTelNumber.setPan(pan);
        billPaySayByTelNumber.setTraceCode(traceID);
        billPaySayByTelNumber.setReferenceCode(switchMessageID);
        billPaySayByTelNumber.setBillID(billID);
        billPaySayByTelNumber.setPaymentID(payID);
        billPaySayByTelNumber.setPayDate(date);
        billPaySayByTelNumber.setTelNo(tel);
        billPaySayByTelNumber.setCallUniqID(callUniqID);
        setRequestObject(billPaySayByTelNumber);

    }
    private void accountBillPayment(){

        String temp=strutils.rightString(tempRequest,tempRequest.length()-1);
        String accountNo=strutils.leftString(temp,10);
        String billID=strutils.midString(temp,11,13);
        String payID=strutils.midString(temp,24,13);
        String amount=String.valueOf(Integer.valueOf(strutils.rightString(temp, 15)));

        BillPayByIDAccount billPayByIDAccount=new BillPayByIDAccount();
        billPayByIDAccount.setSourceAccount(accountNo);
        billPayByIDAccount.setBillID(billID);
        billPayByIDAccount.setPaymentID(payID);
        billPayByIDAccount.setAmount(amount);
        billPayByIDAccount.setCallUniqID(callUniqID);
        setRequestObject(billPayByIDAccount);

    }
    private void accountInstallment(){

        String temp=strutils.rightString(tempRequest,tempRequest.length()-1);
        String sourceAccountNO=strutils.leftString(temp,10);
        String destAccountNo=strutils.midString(temp, 11, 10);
        String amount=String.valueOf(Integer.valueOf(strutils.midString(temp,21,15)));
        FundTransfer fundTransfer=new FundTransfer();
        fundTransfer.setSourceAccount(sourceAccountNO);
        fundTransfer.setDestinationAccount(destAccountNo);
        fundTransfer.setTransactionAmount(amount);
        fundTransfer.setIsInstallmentPay(true);
        fundTransfer.setCallUniqID(callUniqID);
        setRequestObject(fundTransfer);

    }
    private void accountFundTransferIdent(){

        String codeTemp=strutils.rightString(tempRequest,22);
        String t=strutils.leftString(codeTemp,2);
        int lenOfCode=Integer.valueOf(t);
        String code=strutils.rightString(tempRequest,lenOfCode);

        String temp=strutils.leftString(tempRequest, tempRequest.length() - 22);
        temp=temp.substring(1);
        String sourceAccountNO=strutils.leftString(temp,10);
        String destAccountNo=strutils.midString(temp, 11, 10);
        String amount=String.valueOf(Integer.valueOf(strutils.rightString(temp,15)));
        FundTransfer fundTransfer=new FundTransfer();
        fundTransfer.setSourceAccount(sourceAccountNO);
        fundTransfer.setDestinationAccount(destAccountNo);
        fundTransfer.setTransactionAmount(amount);
        fundTransfer.setTransferID(code);
        fundTransfer.setIsIdentFundTranfer(true);
        fundTransfer.setCallUniqID(callUniqID);
        setRequestObject(fundTransfer);
    }
    private void accountFundTransfer(){

        if (tempRequest.length()>36) tempRequest=strutils.leftString(tempRequest,tempRequest.length()-22);
        String temp=strutils.rightString(tempRequest,tempRequest.length()-1);
        String sourceAccountNO=strutils.leftString(temp,10);
        String destAccountNo=strutils.midString(temp, 11, 10);
        String amount=String.valueOf(Integer.valueOf(strutils.rightString(temp,15)));
        FundTransfer fundTransfer=new FundTransfer();
        fundTransfer.setSourceAccount(sourceAccountNO);
        fundTransfer.setDestinationAccount(destAccountNo);
        fundTransfer.setTransactionAmount(amount);
        fundTransfer.setIsFundTransfer(true);
        fundTransfer.setCallUniqID(callUniqID);
        setRequestObject(fundTransfer);

    }
    private void accountChequeStatus(){

        if (tempRequest.length()>36) tempRequest=strutils.leftString(tempRequest,tempRequest.length()-22);
        String temp=strutils.rightString(tempRequest,tempRequest.length()-1);

        String accountNO=strutils.leftString(temp,10);
        String cheque=strutils.midString(temp,17,6);

        ChequeStatus chequeStatus=new ChequeStatus();
        chequeStatus.setAccountNumber(accountNO);
        chequeStatus.setChequeNO(cheque);
        chequeStatus.setCallUniqID(callUniqID);
        setRequestObject(chequeStatus);

    }
    private void accountBlock(){

        String temp=strutils.rightString(tempRequest,tempRequest.length()-1);

        String accountNO=strutils.leftString(temp,10);
        String blockMode=strutils.rightString(temp, 1);

        BlockAccount blockAccount=new BlockAccount();
        blockAccount.setAccountNumber(accountNO);
        blockAccount.setBlockMode(blockMode);
        blockAccount.setCallUniqID(callUniqID);
        setRequestObject(blockAccount);

    }
    private void accountFollowUp(){

        String temp=strutils.rightString(tempRequest,tempRequest.length()-1);

        String followCode=String.valueOf(Integer.valueOf(strutils.rightString(tempRequest,10)));
        String account="";
        account=strutils.leftString(temp,10);

        InternalFollowUp internalFollowUp=new InternalFollowUp();
        internalFollowUp.setSourceAccount(account);
        internalFollowUp.setFollowUpCode(followCode);
        internalFollowUp.setIsInternalFollowCode(true);
        internalFollowUp.setCallUniqID(callUniqID);
        setRequestObject(internalFollowUp);

    }
    private void panOperation(){
        switch (subKind) {
            case "b":panBalance();
                break;
            case "p":panFollowup();
                break;
            case "g":panBillPayment();
                break;
            case "m":panBlock();
                break;
            default:failRequest();
        }
    }
    private void panBalance(){

        tempRequest=strutils.rightString(tempRequest,tempRequest.length()-2);

        String temp=macAddress+tempRequest;

        String pan=strutils.midString(temp,18,16);
       /* if (pan.equals("5859831026664614")){
            System.out.println("test");
        }
        if (pan.equals("5859831002862901")){
            System.out.println("test");
        }
        if (pan.equals("6037991467916494")){
            System.out.println("test");
        }
*/
        int pinLength=Integer.valueOf(strutils.rightString(temp,temp.length()-33).substring(0,2));
        String pin=strutils.rightString(temp,pinLength);
//        String pin=(strutils.rightString(temp,temp.length()-35-pinLength));

        BalanceForCard balanceForCard=new BalanceForCard();
        balanceForCard.setPan(pan);
        balanceForCard.setPin(pin);
        balanceForCard.setCallUniqID(callUniqID);
        setRequestObject(balanceForCard);

    }
    private void panBillPayment(){

        String temp=pcNO+macAddress+strutils.rightString(tempRequest,tempRequest.length()-2);

        pcNO=strutils.leftString(temp,4);
        temp=strutils.rightString(temp,temp.length()-4);
        macAddress=strutils.leftString(temp, 17);
        temp=strutils.rightString(temp,temp.length()-17);
        String pan=strutils.leftString(temp,16);
        String pinLen=strutils.midString(temp, 17, 2);
        String pinAndLen=strutils.midString(temp, 17, 14);
        String pin=strutils.rightString(pinAndLen,Integer.valueOf(pinLen));
        String billID=strutils.midString(temp,31,13);
        String payID=strutils.midString(temp,44,13);
        String amount=String.valueOf(Integer.valueOf(strutils.rightString(temp, 15)));

        BillPayByBillIDPan billPayByBillIDPan=new BillPayByBillIDPan();
        billPayByBillIDPan.setPan(pan);
        billPayByBillIDPan.setPin(pin);
        billPayByBillIDPan.setBillID(billID);
        billPayByBillIDPan.setPaymentID(payID);
        billPayByBillIDPan.setAmount(amount);
        billPayByBillIDPan.setCallUniqID(callUniqID);
        setRequestObject(billPayByBillIDPan);

    }
    private void panAccountOfPant(){
        String temp=strutils.midString(tempRequest,2,16);
        String pan=temp;

        AccountNoOfCard accountNoOfCard=new AccountNoOfCard();
        accountNoOfCard.setCardNo(pan);
        accountNoOfCard.setCallUniqID(callUniqID);
        setRequestObject(accountNoOfCard);
    }
    private void panFollowup(){

        String temp=strutils.rightString(tempRequest,tempRequest.length()-2);

        String followCode=String.valueOf(Integer.valueOf(strutils.rightString(temp,10)));
        String pan="";
        pan=strutils.leftString(temp, 16);

        InternalFollowUp internalFollowUp=new InternalFollowUp();
        internalFollowUp.setSourceAccount(pan);
        internalFollowUp.setFollowUpCode(followCode);
        internalFollowUp.setIsInternalFollowCode(true);
        internalFollowUp.setIsPanPayment(true);
        internalFollowUp.setCallUniqID(callUniqID);
        setRequestObject(internalFollowUp);
    }
    private void panBlock(){
        String temp=strutils.rightString(tempRequest,tempRequest.length()-2);
        String pan=strutils.leftString(temp, 16);
        String pin=String.valueOf(Integer.valueOf(strutils.rightString(temp,12)));

        BlockCard blockCard=new BlockCard();
        blockCard.setPan(pan);
        blockCard.setPin(pin);
        blockCard.setCallUniqID(callUniqID);
        setRequestObject(blockCard);
    }

    private void accountGetTransactionName(){

    }
    public Object getCreatedObject(){
        return this.requestObject;
    }


}
