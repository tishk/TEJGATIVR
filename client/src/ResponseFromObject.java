import Mainchannel.Exceptions.InvalidParameterException;
import Mainchannel.Exceptions.ResponseParsingException;
import Mainchannel.messages.StatementListMessage;
import Mainchannel.messages.StatementMessage;
import Mainchannel.sender.SenderException;
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
import oracle.sql.CHAR;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.sql.SQLException;
import java.util.Map;

/**
 * Created by root on 5/16/16.
 */
public class ResponseFromObject {

    Object responseObject=null;
    Object requestObject=null;
    String responseString=null;
    String requestString=null;
    private strUtils strutils=new strUtils();
    private PersianDateTimeFromat persianDateTimeFromat=new PersianDateTimeFromat();
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
    private String accountNO="!";
    private String accountpass="";
    private String callUniqID="";

    public ResponseFromObject(Object object,String req) throws IOException, ServerNotActiveException {

        String temp[]=req.split(";");
        this.requestString=temp[1];
        callUniqID=temp[0];
        setKindOfRequest();
        this.responseObject=submitRequestToGateway(object);
        processRequest();


    }
    public  Object submitRequestToGateway(Object object) throws IOException,ServerNotActiveException {
        Bank b=null;

        try {
            b=(Bank) Naming.lookup("rmi://" + Properties_Client.getGateway_listener_IP()+ ":" + Properties_Client.getGateway_listener_Port()+ "/Gateway");

            object=b.submitRequest(object);


        } catch (NotBoundException e) {

        } catch (MalformedURLException e) {

        } catch (RemoteException e) {

        } catch (InvalidParameterException e) {

        } catch (ResponseParsingException e) {

        } catch (SenderException e) {

        } catch (SQLException e) {

        } catch (Exception e){

        }


        return object;
    }

    private void setKindOfRequest(){

        tempRequest=requestString;

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

    private void processRequest() throws IOException, ServerNotActiveException {

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
        monitoringStatus=(MonitoringStatus)responseObject;
        if (monitoringStatus!=null){
            responseString=monitoringStatus.getLastFundTransferTime()+"#"+monitoringStatus.getLastBillPaymentTime()+"#";
            for (Map.Entry<String,String> entry :monitoringStatus.getBranchesStatus().entrySet()){
                responseString=requestString+entry.getKey()+"@"+entry.getValue()+"*";
            }
        }else{
            responseString=msgSequence+"9900";
        }
    }

    private void telSwitchGetInfo(){
        switch (subKind){
            case "m" :telSwitchGetMobileBillInfo();
                break;
            default:telSwitchGetTelBillInfo();
        }
    }
    private void telSwitchGetTelBillInfo(){

        BillInfoByTelNumber billInfoByTelNumber=new BillInfoByTelNumber();
        billInfoByTelNumber=(BillInfoByTelNumber) responseObject;

        String actionCode=billInfoByTelNumber.getResultFromServer().getActionCode();
        String tel=strutils.fixLengthWithZero(billInfoByTelNumber.getTelNo(),13);
        String billID=strutils.fixLengthWithZero(billInfoByTelNumber.getResultFromServer().getBillID(), 13);
        String payID = strutils.fixLengthWithZero(billInfoByTelNumber.getPaymentID(),13);
        String amount = strutils.fixLengthWithZero(billInfoByTelNumber.getAmount(),10);


        responseString=msgSequence+actionCode+tel+"="+billID+"="+payID+"="+amount+"="+"00000000";
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

        BalanceForCard balanceForCard=new BalanceForCard();
        balanceForCard=(BalanceForCard) responseObject;
        String actionCode=balanceForCard.getResultFromServer().getActionCode();
        String traceID=balanceForCard.getResultFromServer().getTraceCode();
        String balance=strutils.fixLengthWithZero(balanceForCard.getResultFromServer().getActualBalance(),18);
        String availableBalance=strutils.fixLengthWithZero(balanceForCard.getResultFromServer().getAvilableBalance(),18);

        responseString=msgSequence+actionCode+traceID+balance+availableBalance;

    }

    private void panBillPayment(){


        BillPayByBillIDPan billPayByBillIDPan=new BillPayByBillIDPan();
        billPayByBillIDPan=(BillPayByBillIDPan) responseObject;
        String actionCode=billPayByBillIDPan.getActionCode();
        String traceID=billPayByBillIDPan.getTraceCode();
        String balance=strutils.fixLengthWithZero(billPayByBillIDPan.getResultFromServer().getAvilableBalance(),18);
//        String balance=strutils.fixLengthWithZero("1364",18);


        if (actionCode.equals("0000")){
            responseString=msgSequence+actionCode+traceID+balance;
        }else responseString=msgSequence+actionCode;


    }
    private void saveLog(){
        responseString=msgSequence+"0000"+msgSequence;
    }
    private void panFollowup(){

        InternalFollowUp internalFollowUp=new InternalFollowUp();
        internalFollowUp=(InternalFollowUp) responseObject;
        if (internalFollowUp.getActionCode().equals("0000"))
            responseString=msgSequence+internalFollowUp.getActionCode()+internalFollowUp.getdoneFlag();
        else responseString=msgSequence+internalFollowUp.getActionCode()+"9000";

    }
    private void panBlock(){


        BlockCard blockCard=new BlockCard();
        blockCard=(BlockCard) responseObject;
        responseString=msgSequence+blockCard.getActionCode();

    }
    private void panAccountOfPant(){
        AccountNoOfCard accountNoOfCard=new AccountNoOfCard();
        accountNoOfCard=(AccountNoOfCard)responseObject;
        String actionCode=accountNoOfCard.getResultFromChannel().getAction_code();
        if (actionCode.equals("0000"))
            responseString=msgSequence+
                    actionCode+
                    strutils.fixLengthWithZero(accountNoOfCard.getResultFromChannel().getCfsAccNo(),13);
        else responseString=msgSequence+actionCode+"0000000000000";
    }

    private void accountBalance() throws IOException, ServerNotActiveException {

        String accountNO=strutils.rightString(tempRequest,tempRequest.length()-1);

        accountNO=strutils.fixLengthWithZero(accountNO,10);

        AccountInformation accountInformation=new AccountInformation();
        accountInformation.setAccountNumber(accountNO);
        accountInformation=(AccountInformation)submitRequestToGateway(accountInformation);

        if (accountInformation.getResultFromChannel()!=null){
            if (accountInformation.getResultFromChannel().getAction_code().equals("0000")){
                BalanceForAccount balanceForAccount=new BalanceForAccount();
                balanceForAccount.setAccountNumber(accountNO);
                balanceForAccount=(BalanceForAccount)submitRequestToGateway(balanceForAccount);

                String actionCode=balanceForAccount.getResultFromChannel().getAction_code();
                String pass="00000";
                String nameAndFamily=strutils.fixLengthWithSpace(accountInformation.getResultFromChannel().getFarsiName(),40);
                String lastTransDate=strutils.fixLengthWithZero(balanceForAccount.getResultFromChannel().getLastTransactionDate(), 6);
                if (lastTransDate.equals("000000")) lastTransDate=persianDateTimeFromat.getIranianDateForBalance();
                if (lastTransDate.length()==8){
                    lastTransDate=lastTransDate.substring(2);
                }
                String branchCode=strutils.fixLengthWithSpace(accountInformation.getResultFromChannel().getAccountBranchID(),5);
                String suffix=strutils.fixLengthWithZero(balanceForAccount.getResultFromChannel().getCurrency(),3);
                String ip="";
        /*try{
            if (Integer.valueOf(suffix)==0){

                 ip=strutils.fixLengthWithSpace(Properties_Client.getLocal_listener_IP(),15);

            }else{

                 ip=strutils.fixLengthWithSpace(Properties_Client.getLocal_listener_IP(),13);
            }
        }catch (Exception e){

        }*/
                ip=strutils.fixLengthWithSpace(Properties_Client.getLocal_listener_IP(),15);

                String avBalance=balanceForAccount.getResultFromChannel().getActualBalance();
                String rightAvBAlance=strutils.rightString(avBalance,15);
                String cash=strutils.fixLengthWithZero(rightAvBAlance,15);
                String branchName=strutils.fixLengthWithSpace(accountInformation.getBranchName(),30);
                String isOnlineBranch="1";
                String hostID=accountInformation.getResultFromChannel().getAccountHost();
                String personType="1";
                responseString=actionCode+
                        accountNO+
                        pass+
                        nameAndFamily+
                        lastTransDate+
                        branchCode+
                        ip+
                        suffix+
                        cash+
                        lastTransDate+
                        branchName+
                        isOnlineBranch+
                        hostID+
                        personType;
                responseString=msgSequence+responseString;

            }else{
                responseString=msgSequence+accountInformation.getResultFromChannel().getAction_code();
            }
        }else{
            responseString=msgSequence+"9999";
        }


    }
    private void accountBalance__() throws IOException, ServerNotActiveException {

        String accountNO=strutils.rightString(tempRequest,tempRequest.length()-1);
        accountNO=strutils.fixLengthWithZero(accountNO,10);

        AccountInformation accountInformation=new AccountInformation();
        accountInformation.setAccountNumber(accountNO);
        accountInformation=(AccountInformation)submitRequestToGateway(accountInformation);


            BalanceForAccount balanceForAccount=new BalanceForAccount();
                balanceForAccount.setAccountNumber(accountNO);
                balanceForAccount=(BalanceForAccount)submitRequestToGateway(balanceForAccount);

                String actionCode=balanceForAccount.getResultFromChannel().getAction_code();
                String pass="00000";
                String nameAndFamily=strutils.fixLengthWithSpace(accountInformation.getResultFromChannel().getFarsiName(),40);
                String lastTransDate=strutils.fixLengthWithZero(balanceForAccount.getResultFromChannel().getLastTransactionDate(), 6);
                if (lastTransDate.equals("000000")) lastTransDate=persianDateTimeFromat.getIranianDateForBalance();
                if (lastTransDate.length()==8){
                    lastTransDate=lastTransDate.substring(2);
                }
                String branchCode=strutils.fixLengthWithSpace(accountInformation.getResultFromChannel().getAccountBranchID(),5);
                String suffix=strutils.fixLengthWithZero(balanceForAccount.getResultFromChannel().getCurrency(),3);
                String ip="";
        /*try{
            if (Integer.valueOf(suffix)==0){

                 ip=strutils.fixLengthWithSpace(Properties_Client.getLocal_listener_IP(),15);

            }else{

                 ip=strutils.fixLengthWithSpace(Properties_Client.getLocal_listener_IP(),13);
            }
        }catch (Exception e){

        }*/
                ip=strutils.fixLengthWithSpace(Properties_Client.getLocal_listener_IP(),15);

                String avBalance=balanceForAccount.getResultFromChannel().getActualBalance();
                String rightAvBAlance=strutils.rightString(avBalance,15);
                String cash=strutils.fixLengthWithZero(rightAvBAlance,15);
                String branchName=strutils.fixLengthWithSpace(accountInformation.getBranchName(),30);
                String isOnlineBranch="1";
                String hostID=accountInformation.getResultFromChannel().getAccountHost();
                String personType="1";
                responseString=actionCode+
                        accountNO+
                        pass+
                        nameAndFamily+
                        lastTransDate+
                        branchCode+
                        ip+
                        suffix+
                        cash+
                        lastTransDate+
                        branchName+
                        isOnlineBranch+
                        hostID+
                        personType;
                responseString=msgSequence+responseString;


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
    private void telSetMob(){

        SMSAlarmTransaction smsAlarmTransaction=new SMSAlarmTransaction();
        smsAlarmTransaction=(SMSAlarmTransaction) responseObject;
        responseString=msgSequence+smsAlarmTransaction.getActionCode();

    }
    private void telSetTell(){

    }
    private void telGetMob(){

    }
    private void accountGetService(){
        responseString=msgSequence+"48";
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


        AuthenticatePin1 authenticatePin1=new AuthenticatePin1();
        authenticatePin1=(AuthenticatePin1)responseObject;
        responseString=msgSequence+authenticatePin1.getActionCode();
    }
    private void accountCalculateIBAN(){


        ShebaID shebaID=new ShebaID();
        shebaID=(ShebaID)responseObject;
        responseString=msgSequence+shebaID.getActionCode()+shebaID.getShebaID();

    }
    private void accountPanOfAccount(){

        CardNoOfAccount cardNoOfAccount=new CardNoOfAccount();
        cardNoOfAccount=(CardNoOfAccount)responseObject;
        responseString=msgSequence+
                cardNoOfAccount.getResultFromChannel().getAction_code()+
                cardNoOfAccount.getResultFromChannel().getPan();
    }
    private void accountGetPrivilageInfo(){
        String ans="1111000000000000       00000000000000000000000000000000000000000000000000111";
        responseString=msgSequence+"0000"+ans;
    }
    private void accountFollowUp(){

        InternalFollowUp internalFollowUp=new InternalFollowUp();
        internalFollowUp=(InternalFollowUp)responseObject;
        responseString=msgSequence+internalFollowUp.getActionCode()+internalFollowUp.getdoneFlag();
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


        AuthenticatePin2 authenticatePin2=new AuthenticatePin2();
        authenticatePin2=(AuthenticatePin2) responseObject;
        responseString=msgSequence+authenticatePin2.getActionCode();

    }
    private void accountFundTransfer(){


        FundTransfer fundTransfer=new FundTransfer();
        fundTransfer=(FundTransfer)responseObject;
        responseString=msgSequence+fundTransfer.getActionCode()+fundTransfer.getTraceID();
    }
    private void accountBillPayment(){

        BillPayByIDAccount billPayByIDAccount=new BillPayByIDAccount();
        billPayByIDAccount=(BillPayByIDAccount)responseObject;
        responseString=msgSequence+billPayByIDAccount.getActionCode()+billPayByIDAccount.getTraceCode();
    }
    private void accountChangePin(){

        String temp=strutils.rightString(tempRequest,tempRequest.length()-1);
        char changePinType=(strutils.rightString(temp,5)).charAt(0);
        boolean isPin1=false;
        if (changePinType=='0') isPin1=true;
        if (isPin1){
            AuthenticatePin1 authenticatePin1=new AuthenticatePin1();
            authenticatePin1=(AuthenticatePin1) responseObject;
            responseString=msgSequence+authenticatePin1.getActionCode();
        }
        else{
            AuthenticatePin2 authenticatePin2=new AuthenticatePin2();
            authenticatePin2=(AuthenticatePin2) responseObject;
            responseString=msgSequence+authenticatePin2.getActionCode();
        }



    }
    private void accountFundTransferIdent(){


        FundTransfer fundTransfer=new FundTransfer();
        fundTransfer=(FundTransfer)responseObject;
        responseString=msgSequence+fundTransfer.getActionCode()+fundTransfer.getTraceID();
    }
    private void accountTransaction() throws IOException, ServerNotActiveException {

        Transaction transaction=new Transaction();
        transaction=(Transaction) responseObject;
        String tempTrans="";
        int transCount=Integer.valueOf(transaction.getResultFromCM().getTransCount());
        boolean isUsualStatementType=transaction.getStatementType().equals("8")||transaction.getStatementType().equals("9");
        StatementListMessage stlMessage=transaction.getResultFromCM();
        for(int i=0;i<transCount;i++){
            StatementMessage stMessage=stlMessage.getStatementMessage(i);
            String amountString="";
            if (isUsualStatementType){
                try{
                    amountString=deleteZeroPrefix(stMessage.getAmount());

                    String temp="";
                    if (stMessage.getCreditDebit().equals("D")){
                        for (int k=amountString.length();k<14;k++){
                            temp=temp+"0";
                        }
                        temp=temp+"-";
                    }
                    else{
                        for (int k=amountString.length();k<15;k++){
                            temp=temp+"0";
                        }
                    }

                    amountString=temp+amountString;
                }catch (Exception e){

                }
                tempTrans= tempTrans+
                        strutils.rightString(stMessage.getTransDate(),6) +
                        "$"+amountString+
                        "$"+strutils.fixLengthWithZero(deleteZeroPrefix(stMessage.getTransDocNo()), 8)+
                        "$"+strutils.fixLengthWithZero(deleteZeroPrefix(stMessage.getTransOprationCode()),3)+
                        "$"+strutils.fixLengthWithZero(deleteZeroPrefix(stMessage.getLastAmount()),15)+
                        "$"+strutils.fixLengthWithZero(deleteZeroPrefix(stMessage.getBranchCode()),6)+
                        "$"+stMessage.getShpInf()+
                        "$"+stMessage.getBranchName()+"#";//if need set Branch Name here
            }
            else{
                tempTrans= tempTrans+
                        "$"+strutils.rightString(stMessage.getTransDate(),6)+
                        "$"+strutils.fixLengthWithZero(stMessage.getAmount(), 15)+
                        "$"+strutils.fixLengthWithZero(stMessage.getTransDocNo(), 8)+
                        "$"+strutils.fixLengthWithZero(stMessage.getTransOprationCode(),3)+
                        "$"+strutils.fixLengthWithZero(stMessage.getLastAmount(),15)+
                        "$"+strutils.fixLengthWithZero(stMessage.getBranchCode(),6);
            }
        }
        tempTrans=strutils.fixLengthWithZero(String.valueOf(transCount),3)+"#"+tempTrans;
        responseString=msgSequence+transaction.getResultFromCM().getAction_code()+tempTrans;

    }
    private void accountInstallment(){
        FundTransfer fundTransfer=new FundTransfer();
        fundTransfer=(FundTransfer)responseObject;
        responseString=msgSequence+fundTransfer.getActionCode()+fundTransfer.getTraceID();

    }
    private void accountChequeStatus(){

        ChequeStatus chequeStatus=new ChequeStatus();
        chequeStatus=(ChequeStatus)responseObject;
        if (!chequeStatus.getResultFromChannel().getAction_code().equals("0000")){
            responseString=msgSequence+chequeStatus.getResultFromChannel().getAction_code();
        }else{
            responseString=msgSequence+strutils.fixLengthWithZero(chequeStatus.getResultFromChannel().getChequeStatus(),4);
        }
    }
    private void accountBlock(){

        BlockAccount blockAccount=new BlockAccount();
        blockAccount=(BlockAccount)responseObject;
        if (!blockAccount.getResultFromChannel().getAction_code().equals("0000")){
            responseString=msgSequence+blockAccount.getResultFromChannel().getAction_code();
        }else{
            responseString=msgSequence+strutils.fixLengthWithZero(blockAccount.getResultFromChannel().getAction_code(),4);
        }

    }

    private String deleteZeroPrefix(String str){
        try{
            Long amount=Long.valueOf(str);
            return String.valueOf(amount);
        }catch (Exception e){
            return "!";
        }
    }











    private void failRequest(){
        setRequestObject((Object) "!"+msgSequence+"9999");
    }


    private void accountAvacasSaveClearPrint(){

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

        setRequestObject(billPaySayByTelNumber);

    }


    private void accountGetTransactionName(){

    }
    public Object getCreatedObject(){
        return this.requestObject;
    }
    private void setRequestObject(Object object){
        this.requestObject=object;
    }

    public void   processResponse(Object object){

    }
    public String getResponse(){
        return this.responseString;
    }

}
