package ResponseServices;

import CardSwitchSaba.SendToSabaSwitch;
import Mainchannel.Exceptions.InvalidParameterException;
import Mainchannel.Exceptions.ResponseParsingException;
import Mainchannel.MainMQ;
import Mainchannel.messages.*;
import Mainchannel.sender.SenderException;
import ServiceObjects.Account.*;
import ServiceObjects.ISO.ISO100;
import ServiceObjects.ISO.ISO200;
import ServiceObjects.Other.*;
import ServiceObjects.Pan.BalanceForCard;
import ServiceObjects.Pan.BillPayByBillIDPan;
import ServiceObjects.Pan.BlockCard;
import ServiceObjects.Pin.AuthenticatePin1;
import ServiceObjects.Pin.AuthenticatePin2;
import ServiceObjects.Pin.PreMethodAuthentication;
import SystemStatus.Status_All;
import TelSwitchPKG.SendToTelSwitch;
import utils.PersianDateTime;
import utils.PropertiesUtils;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class GatewayServices implements java.io.Serializable {
    private static final long serialVersionUID = 7933179759822777700L;
    PersianDateTime persianDateTime=new PersianDateTime();
    private static  String lastMsgSeq=null;
    public static synchronized String getMsgSequence() {
        String seq;
        String sseq;
        do {
            seq = String.valueOf(System.nanoTime()).substring(0,12);//144525438988
             sseq= String.valueOf(System.currentTimeMillis());
           // System.out.println("seq is:" + seq);
        } while(seq.equals(lastMsgSeq));

        lastMsgSeq = seq;
        return seq;
    }
    public static void logTodb(final Object object){
        //

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future future = executorService.submit(new Runnable() {
            public void run() {
                try {
                   new LoggerToDB(object);
                 //   loToDB=loggerToDB;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }



    public AuthenticatePin1        accountPin1authenticate(AuthenticatePin1 authenticatePin1) throws SQLException, InterruptedException, ClassNotFoundException, ResponseParsingException, SenderException, InvalidParameterException {
        if (authenticatePin1.getAccountNumber().equals("0353056352")||authenticatePin1.getAccountNumber().equals("353056352")){
            System.out.println("test");
        }
        if (PropertiesUtils.getUseOldAuthenticatePin1()){
            return  accountPin1authenticatePreMethod(authenticatePin1) ;
        }
        else{
            return Pin1.currentMethod.Avacas.AvaCas.getAuthenticatePin1(authenticatePin1);
        }
    }
    public AuthenticatePin1        accountPin1authenticatePreMethod(AuthenticatePin1 authenticatePin1) throws SQLException, ClassNotFoundException, SenderException, ResponseParsingException, InvalidParameterException {
        //System.out.println("in old aut pin1");
        PreMethodAuthentication preMethodAuthentication= null;
        try {
            preMethodAuthentication = new PreMethodAuthentication(authenticatePin1.getAccountNumber(),
                                                                  authenticatePin1.getPin(),
                                                                  authenticatePin1.getCallerID(),
                                                                  true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        authenticatePin1.setActionCode(preMethodAuthentication.getActionCode());
        authenticatePin1.setMsgSeq(getMsgSequence());
        preMethodAuthentication=null;
        LoggerToDB loggerToDB =new LoggerToDB(authenticatePin1);
       /* if (!loggerToDB.getResultOfLog()) {

            authenticatePin1.setActionCode("6666");
        }*/
        return authenticatePin1;
    }
    public AuthenticatePin1        accountPin1Change(AuthenticatePin1 authenticatePin1) throws SQLException, ClassNotFoundException, ResponseParsingException, SenderException, InvalidParameterException {
        if (PropertiesUtils.getUseOldAuthenticatePin1()) return accountPin1ChangePreMethod(authenticatePin1);
        else return Pin1.currentMethod.Avacas.AvaCas.getAuthenticatePin1(authenticatePin1);
    }
    public AuthenticatePin1        accountPin1ChangePreMethod(AuthenticatePin1 authenticatePin1) throws SQLException, ClassNotFoundException, SenderException, ResponseParsingException, InvalidParameterException {
        PreMethodAuthentication preMethodAuthentication= null;
        try {
            preMethodAuthentication = new PreMethodAuthentication(authenticatePin1.getAccountNumber(),
                                                                  authenticatePin1.getPin(),
                                                                  authenticatePin1.getPin_New(),
                                                                  authenticatePin1.getCallerID(),
                                                                  true);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        authenticatePin1.setActionCode(preMethodAuthentication.getActionCode());
        authenticatePin1.setMsgSeq(getMsgSequence());
        preMethodAuthentication=null;
        LoggerToDB loggerToDB =new LoggerToDB(authenticatePin1);
        if (!loggerToDB.getResultOfLog()) authenticatePin1.setActionCode("6666");
        return authenticatePin1;
    }

    public AuthenticatePin2        accountPin1authenticateNOtHaghighi(AuthenticatePin2 authenticatePin2) throws SQLException, InterruptedException {


            return  accountPin1authenticatePreMethodIsNotHaghighi(authenticatePin2) ;

    }
    public AuthenticatePin2        accountPin1authenticatePreMethodIsNotHaghighi(AuthenticatePin2 authenticatePin2) throws SQLException {
        PreMethodAuthentication preMethodAuthentication= null;
        preMethodAuthentication = new PreMethodAuthentication(authenticatePin2.getAccountNumber(),
                authenticatePin2.getPin(),
                authenticatePin2.getCallerID(),
                true,true);
        authenticatePin2.setActionCode(preMethodAuthentication.getActionCode());
        authenticatePin2.setMsgSeq(getMsgSequence());
        preMethodAuthentication=null;
        LoggerToDB loggerToDB =new LoggerToDB(authenticatePin2);
        if (!loggerToDB.getResultOfLog()) authenticatePin2.setActionCode("6666");
        return authenticatePin2;
    }

    public AuthenticatePin2        accountPin2authenticate(AuthenticatePin2 authenticatePin2) throws SQLException, InterruptedException, ClassNotFoundException, ResponseParsingException, SenderException, InvalidParameterException {
        if (PropertiesUtils.getUseOldAuthenticatePin2()) {
            if (authenticatePin2.getIsNotHaghighiAccount())
                   return accountPin1authenticateNOtHaghighi(authenticatePin2);
            else   return  accountPin2authenticatePreMethod(authenticatePin2) ;
        }else{
            return Pin2.currentMethod.Avacas.AvaCas.getAuthenticatePin2(authenticatePin2);
        }
    }
    public AuthenticatePin2        accountPin2authenticatePreMethod(AuthenticatePin2 authenticatePin2) throws SQLException, ClassNotFoundException, SenderException, ResponseParsingException, InvalidParameterException {
        PreMethodAuthentication preMethodAuthentication= null;
        try {
            preMethodAuthentication = new PreMethodAuthentication(authenticatePin2.getAccountNumber(),
                                                                  authenticatePin2.getPin(),
                                                                  authenticatePin2.getCallerID(),
                                                                  false);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        authenticatePin2.setActionCode(preMethodAuthentication.getActionCode());
        authenticatePin2.setMsgSeq(getMsgSequence());
        preMethodAuthentication=null;
        LoggerToDB loggerToDB =new LoggerToDB(authenticatePin2);
        if (!loggerToDB.getResultOfLog()) authenticatePin2.setActionCode("6666");
        return authenticatePin2;
    }
    public AuthenticatePin2        accountPin2Change(AuthenticatePin2 authenticatePin2) throws SQLException, ClassNotFoundException, ResponseParsingException, SenderException, InvalidParameterException {
        if (PropertiesUtils.getUseOldAuthenticatePin2()) return accountPin2ChangePreMethod(authenticatePin2);
        else return Pin2.currentMethod.Avacas.AvaCas.ChangePin2(authenticatePin2);
    }
    public AuthenticatePin2        accountPin2ChangePreMethod(AuthenticatePin2 authenticatePin2) throws SQLException, ClassNotFoundException, SenderException, ResponseParsingException, InvalidParameterException {
        PreMethodAuthentication preMethodAuthentication= null;
        try {
            preMethodAuthentication = new PreMethodAuthentication(authenticatePin2.getAccountNumber(),
                                                                  authenticatePin2.getPin(),
                                                                  authenticatePin2.getPin_New(),
                                                                  authenticatePin2.getCallerID(),
                                                                    false);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        authenticatePin2.setActionCode(preMethodAuthentication.getActionCode());
        authenticatePin2.setMsgSeq(getMsgSequence());
        preMethodAuthentication=null;
        LoggerToDB loggerToDB =new LoggerToDB(authenticatePin2);
        if (!loggerToDB.getResultOfLog()) authenticatePin2.setActionCode("6666");
        return authenticatePin2;
    }

    public SMSAlarmTransaction     accountGetMobileOfAccount(SMSAlarmTransaction smsAlarmTransaction) throws SQLException {
        smsAlarmTransaction.GetMobileNumberFromWebService();
        LoggerToDB loggerToDB =new LoggerToDB(smsAlarmTransaction);
        if (!loggerToDB.getResultOfLog()) smsAlarmTransaction.setActionCode("6666");
        loggerToDB =null;
        return smsAlarmTransaction;
    }
    public SMSAlarmTransaction     accountSetMobileOfAccount(SMSAlarmTransaction smsAlarmTransaction) throws SQLException {
        if (smsAlarmTransaction.getAccountNumber().equals("3435531082")){
            System.out.println("test");
        }
        smsAlarmTransaction.SetMobileNumberInWebService();
        LoggerToDB loggerToDB =new LoggerToDB(smsAlarmTransaction);
        if (!loggerToDB.
                getResultOfLog()) smsAlarmTransaction.setActionCode("6666");
        loggerToDB =null;
        return smsAlarmTransaction;
    }
    public SMSAlarmTransaction     accountdeleteMobileOfAccount(SMSAlarmTransaction smsAlarmTransaction) throws SQLException {

        if (smsAlarmTransaction.getAccountNumber().equals("3435531082")){
            System.out.println("test");
        }
        smsAlarmTransaction.deleteMobileNumberInWebService();
        LoggerToDB loggerToDB =new LoggerToDB(smsAlarmTransaction);
        if (!loggerToDB.getResultOfLog()) smsAlarmTransaction.setActionCode("6666");
        loggerToDB =null;
        return smsAlarmTransaction;
    }

    public AccountInformation      accountInformation(AccountInformation accountInformation) throws SenderException, InvalidParameterException, ResponseParsingException, SQLException, ClassNotFoundException {
        int SendCounter=0;

        //accountInformation.setAccountNumber("155971991");

       // System.out.println("MAC is :"+accountInformation.getMAC());
        accountInformation.setAccountNumber(accountInformation.getAccountNumber());
        accountInformation.setPin("1234");
        accountInformation.setRequestDateTime(persianDateTime.getRequestDateTime());
        accountInformation.setChannelType(PropertiesUtils.getBaseRequestMessage_Channeltype());
        accountInformation.setChannelPass(PropertiesUtils.getBaseRequestMessage_ChannelPass());
        accountInformation.setChannelID(PropertiesUtils.getBaseRequestMessage_ChannelID());
        accountInformation.setMsgSeq(getMsgSequence());
        accountInformation.setEncAlgorytm(PropertiesUtils.getBaseRequestMessage_EncAlgorytm());
        accountInformation.setMAC(PropertiesUtils.getBaseRequestMessage_MAC());
        AccountListMessage Result=new AccountListMessage();
        Result.setAction_code("3333");
        AccountListMessage ResultTemp=new AccountListMessage();
        try{
           // while (!(Result.getAction_code().equals("1806"))&&(SendCounter<10)){
              //  if (!Result.getAction_code().equals("1806")) ResultTemp=Result;
              //  if (ResultTemp.getAction_code().equals("9126")) break;
                Result= MainMQ.getAccountList(
                        accountInformation.getAccountNumber(),
                        accountInformation.getPin(),
                        accountInformation.getChannelType(),
                        accountInformation.getChannelPass(),
                        accountInformation.getChannelID(),
                        accountInformation.getMsgSeq(),
                        accountInformation.getRequestDateTime(),
                        accountInformation.getEncAlgorytm(),
                        accountInformation.getMAC()
                );
            ResultTemp=Result;
              //  SendCounter++;
                //System.out.println("result from server : " + Result.getResponseXml());

          //  }
        }catch (Exception e){

        }
        accountInformation.setBranchName(new DBUtils().getBranchName(ResultTemp.getAccountBranchID()));
        accountInformation.setResultFromChannel(ResultTemp);
       logTodb(accountInformation);
        return accountInformation;

    }
    public BalanceForAccount       accountBalance(BalanceForAccount balanceForAccount) throws SenderException, InvalidParameterException, ResponseParsingException, SQLException{
        int SendCounter=0;
        String AcCode=null;
        //balanceForAccount.setAccountNumber("155971991");
        balanceForAccount.setPin("1234");
        balanceForAccount.setRequestDateTime(persianDateTime.getRequestDateTime());
        balanceForAccount.setChannelType(PropertiesUtils.getBaseRequestMessage_Channeltype());
        balanceForAccount.setChannelPass(PropertiesUtils.getBaseRequestMessage_ChannelPass());
        balanceForAccount.setChannelID(PropertiesUtils.getBaseRequestMessage_ChannelID());
        balanceForAccount.setMsgSeq(getMsgSequence());
        balanceForAccount.setEncAlgorytm(PropertiesUtils.getBaseRequestMessage_EncAlgorytm());
        balanceForAccount.setMAC(PropertiesUtils.getBaseRequestMessage_MAC());
        BalanceMessage Result=new BalanceMessage();
        Result.setAction_code("3333");
        BalanceMessage ResultTemp=new BalanceMessage();
        try{
      //      while (!(Result.getAction_code().equals("1806"))&&(SendCounter++<10)){
         //       if (!Result.getAction_code().equals("1806")) ResultTemp=Result;
           //     if (ResultTemp.getAction_code().equals("9126")) break;
             //   if (ResultTemp.getAction_code().equals("0000")) break;
                Result= MainMQ.getBalance(
                        balanceForAccount.getAccountNumber(),
                        balanceForAccount.getPin(),
                        balanceForAccount.getRequestDateTime(),
                        balanceForAccount.getChannelType(),
                        balanceForAccount.getChannelPass(),
                        balanceForAccount.getChannelID(),
                        balanceForAccount.getMsgSeq(),
                        balanceForAccount.getEncAlgorytm(),
                        balanceForAccount.getMAC()
                );

            ResultTemp=Result;
                //System.out.println(Result.getResponseXml());
          //  }
        }catch (Exception e) {
            balanceForAccount.setGatewayMessage("Gateway!!!"+Result.getAction_desc());
        }

        balanceForAccount.setResultFromChannel(ResultTemp);
        ResultTemp=null;
        logTodb(balanceForAccount);
//        balanceForAccount=deletePointSignOfBalance(balanceForAccount);
        return balanceForAccount;
    }


    public Transaction             accountTransaction(Transaction transaction) throws SenderException, InvalidParameterException, ResponseParsingException, SQLException {

        int SendCounter=0;
        transaction.setPin("1234");
        transaction.setRequestDateTime(persianDateTime.getRequestDateTime());
        transaction.setChannelType(PropertiesUtils.getBaseRequestMessage_Channeltype());
        transaction.setChannelPass(PropertiesUtils.getBaseRequestMessage_ChannelPass());
        transaction.setChannelID(PropertiesUtils.getBaseRequestMessage_ChannelID());
        transaction.setMsgSeq(getMsgSequence());
        transaction.setEncAlgorytm(PropertiesUtils.getBaseRequestMessage_EncAlgorytm());
        transaction.setMAC(PropertiesUtils.getBaseRequestMessage_MAC());
        transaction.setStartTime("000000");
        transaction.setEndTime("235959");
        StatementListMessage Result=new StatementListMessage();
        //Result.setAction_code("3333");
        StatementListMessage ResultTemp=new StatementListMessage();
       try {
           // while (!(Result.getAction_code().equals("1806")) && (SendCounter ++< 10)) {
              //  if (!Result.getAction_code().equals("1806")) ResultTemp=Result;
              //  if (ResultTemp.getAction_code().equals("9126")) break;
              //  if (ResultTemp.getAction_code().equals("0000")) break;
                Result = MainMQ.getTransactionList(
                        transaction.getAccountNumber(),
                        transaction.getPin(),
                        transaction.getChannelType(),
                        transaction.getChannelPass(),
                        transaction.getChannelID(),
                        transaction.getStartDate(),
                        transaction.getEndDate(),
                        transaction.getStartTime(),
                        transaction.getEndTime(),
                        transaction.getTransactionCount(),
                        transaction.getCreditOrDebit(),//D bestankar C bedehkar
                        transaction.getTransactionMinAmount(),
                        transaction.getTransactionMaxAmount(),
                        transaction.getTransactionDocNO(),
                        transaction.getTransactionOperationCode(),
                        transaction.getTransactionDocDescription(),
                        transaction.getBranchCode(),
                        transaction.getMsgSeq(),
                        transaction.getStatementType(),// 8 for 30 trans , 9 for other
                        transaction.getrrn(),
                        transaction.getRequestDateTime(),
                        transaction.getEncAlgorytm(),
                        transaction.getMAC()
                );
           ResultTemp=Result;
            //}
        }catch (Exception e){

        }

        transaction.setResultFromCM(ResultTemp);
       logTodb(transaction);
        return transaction;

    }
    public CardNoOfAccount         accountGetPan(CardNoOfAccount PanNoOfAccount) throws SenderException, InvalidParameterException, ResponseParsingException, SQLException {
        int SendCounter=0;

        PanNoOfAccount.setPin("1234");
        PanNoOfAccount.setRequestDateTime(persianDateTime.getRequestDateTime());
        PanNoOfAccount.setChannelType(PropertiesUtils.getBaseRequestMessage_Channeltype());
        PanNoOfAccount.setChannelPass(PropertiesUtils.getBaseRequestMessage_ChannelPass());
        PanNoOfAccount.setChannelID(PropertiesUtils.getBaseRequestMessage_ChannelID());
        PanNoOfAccount.setMsgSeq(getMsgSequence());
        PanNoOfAccount.setEncAlgorytm(PropertiesUtils.getBaseRequestMessage_EncAlgorytm());
        PanNoOfAccount.setMAC(PropertiesUtils.getBaseRequestMessage_MAC());
        CardAccountMessage Result=new CardAccountMessage();
        Result.setAction_code("3333");
        CardAccountMessage ResultTemp=new CardAccountMessage();
        try{
          //  while (!(Result.getAction_code().equals("1806"))&&(SendCounter<10)){
            //    if (!Result.getAction_code().equals("1806")) ResultTemp=Result;
             //   if (ResultTemp.getAction_code().equals("9126")) break;
                Result= MainMQ.getPan(
                        PanNoOfAccount.getMsgSeq(),
                        PanNoOfAccount.getChannelType(),
                        PanNoOfAccount.getChannelID(),
                        PanNoOfAccount.getChannelPass(),
                        PanNoOfAccount.getAccountNumber(),
                        PanNoOfAccount.getPin(),
                        PanNoOfAccount.getRequestDateTime(),
                        PanNoOfAccount.getEncAlgorytm(),
                        PanNoOfAccount.getMAC()
                );
            ResultTemp=Result;
                SendCounter++;

          //  }
        }catch (Exception e) {
            PanNoOfAccount.setGatewayMessage("Gateway!!!"+Result.getAction_desc());
        }
        PanNoOfAccount.setResultFromChannel(ResultTemp);
        logTodb(PanNoOfAccount);
        return PanNoOfAccount;
    }
    public AccountNoOfCard         accountGetAccountOfPan(AccountNoOfCard accountNoOfPan) throws SQLException {
        int SendCounter=0;

        accountNoOfPan.setPin("1234");
        accountNoOfPan.setRequestDateTime(persianDateTime.getRequestDateTime());
        accountNoOfPan.setChannelType(PropertiesUtils.getBaseRequestMessage_Channeltype());
        accountNoOfPan.setChannelPass(PropertiesUtils.getBaseRequestMessage_ChannelPass());
        accountNoOfPan.setChannelID(PropertiesUtils.getBaseRequestMessage_ChannelID());
        accountNoOfPan.setMsgSeq(getMsgSequence());
        accountNoOfPan.setEncAlgorytm(PropertiesUtils.getBaseRequestMessage_EncAlgorytm());
        accountNoOfPan.setMAC(PropertiesUtils.getBaseRequestMessage_MAC());
        CardAccountMessage Result=new CardAccountMessage();
        Result.setAction_code("3333");
        CardAccountMessage ResultTemp=new CardAccountMessage();
        try {
          //  while (!(Result.getAction_code().equals("1806")) && (SendCounter < 10)) {
             //   if (!Result.getAction_code().equals("1806")) ResultTemp = Result;
             //   if (ResultTemp.getAction_code().equals("9126")) break;
                Result= MainMQ.getCardAccNo(
                        accountNoOfPan.getMsgSeq(),
                        accountNoOfPan.getChannelType(),
                        accountNoOfPan.getChannelID(),
                        accountNoOfPan.getChannelPass(),
                        accountNoOfPan.getCardNo(),
                        accountNoOfPan.getRequestDateTime(),
                        accountNoOfPan.getEncAlgorytm(),
                        accountNoOfPan.getMAC()
                );

                SendCounter++;
            ResultTemp=Result;
           // }
        }catch (Exception e) {
            accountNoOfPan.setGatewayMessage("Gateway!!!"+Result.getAction_desc());
        }


        accountNoOfPan.setResultFromChannel(ResultTemp);
        logTodb(accountNoOfPan);

        return accountNoOfPan;
    }
    public RegisterAccount         accountRegister(RegisterAccount registerAccount) throws SenderException, InvalidParameterException, ResponseParsingException, SQLException {
        int SendCounter=0;

        registerAccount.setPin("1234");
        registerAccount.setRequestDateTime(persianDateTime.getRequestDateTime());
        registerAccount.setRequestDateTime(persianDateTime.getRequestDateTime());
        registerAccount.setChannelType(PropertiesUtils.getBaseRequestMessage_Channeltype());
        registerAccount.setChannelPass(PropertiesUtils.getBaseRequestMessage_ChannelPass());
        registerAccount.setChannelID(PropertiesUtils.getBaseRequestMessage_ChannelID());
        registerAccount.setMsgSeq(getMsgSequence());
        registerAccount.setEncAlgorytm(PropertiesUtils.getBaseRequestMessage_EncAlgorytm());
        registerAccount.setMAC(PropertiesUtils.getBaseRequestMessage_MAC());
        RegistrationMessage Result=new RegistrationMessage();
        Result.setAction_code("3333");
        RegistrationMessage ResultTemp=new RegistrationMessage();
        try {
           // while (!(Result.getAction_code().equals("1806")) && (SendCounter < 10)) {
            //    if (!Result.getAction_code().equals("1806")) ResultTemp = Result;
             //   if (ResultTemp.getAction_code().equals("9126")) break;
                Result= MainMQ.getRegistration(
                        registerAccount.getAccountNumber(),
                        registerAccount.getPin(),
                        registerAccount.getServicesType(),
                        registerAccount.getRequestDateTime(),
                        registerAccount.getChannelType(),
                        registerAccount.getChannelPass(),
                        registerAccount.getChannelID(),
                        registerAccount.getMsgSeq(),
                        registerAccount.getEncAlgorytm(),
                        registerAccount.getMAC()
                );

               // SendCounter++;
            ResultTemp=Result;
           // }
        }catch (Exception e) {
            registerAccount.setGatewayMessage("Gateway!!!"+Result.getAction_desc());
        }

        registerAccount.setResultFromChannel(ResultTemp);
        logTodb(registerAccount);
        return registerAccount;
    }
    public ChequeStatus            accountChequeStatus(ChequeStatus chequeStatus) throws SenderException, InvalidParameterException, ResponseParsingException, SQLException {
        int SendCounter=0;
        chequeStatus.setPin("1234");
        chequeStatus.setRequestDateTime(persianDateTime.getRequestDateTime());
        chequeStatus.setChannelType(PropertiesUtils.getBaseRequestMessage_Channeltype());
        chequeStatus.setChannelPass(PropertiesUtils.getBaseRequestMessage_ChannelPass());
        chequeStatus.setChannelID(PropertiesUtils.getBaseRequestMessage_ChannelID());
        chequeStatus.setMsgSeq(getMsgSequence());
        chequeStatus.setEncAlgorytm(PropertiesUtils.getBaseRequestMessage_EncAlgorytm());
        chequeStatus.setMAC(PropertiesUtils.getBaseRequestMessage_MAC());
        chequeStatus.setChequeAmount("");
        chequeStatus.setChequeOperateDate("");
        ChequeStatusMessage Result=new ChequeStatusMessage();
        Result.setAction_code("3333");
        ChequeStatusMessage ResultTemp=new ChequeStatusMessage();
        try {
          //  while (!(Result.getAction_code().equals("1806")) && (SendCounter < 10)) {
              //  if (!Result.getAction_code().equals("1806")) ResultTemp = Result;
               // if (ResultTemp.getAction_code().equals("9126")) break;
                Result= MainMQ.getChequeStatus(
                        chequeStatus.getAccountNumber(),
                        chequeStatus.getPin(),
                        chequeStatus.getChannelType(),
                        chequeStatus.getChannelPass(),
                        chequeStatus.getChannelID(),
                        chequeStatus.getChequeNO(),
                        chequeStatus.getChequeAmount(),
                        chequeStatus.getChequeOperateDate(),
                        chequeStatus.getRequestDateTime(),
                        chequeStatus.getMsgSeq(),
                        chequeStatus.getEncAlgorytm(),
                        chequeStatus.getMAC()
                );

               // SendCounter++;
            ResultTemp=Result;
           // }
        }catch (Exception e) {
            chequeStatus.setGatewayMessage("Gateway!!!"+Result.getAction_desc());
        }

        chequeStatus.setResultFromChannel(ResultTemp);
        logTodb(chequeStatus);
        return chequeStatus;
    }
    public ShebaID                 accountGetShebaID(ShebaID shebaID) throws SQLException, SenderException, InvalidParameterException, ResponseParsingException {
        BalanceForAccount balanceForAccount=new BalanceForAccount();
        balanceForAccount.setAccountNumber(shebaID.getAccountNumber());
        balanceForAccount=accountBalance(balanceForAccount);
        if (balanceForAccount.getResultFromChannel().getAction_code().equals("0000")){
            shebaID.setMsgSeq(getMsgSequence());
            shebaID.calculateShebaID();
            logTodb(shebaID);
        }else{
            shebaID.setActionCode(balanceForAccount.getResultFromChannel().getAction_code());
        }
        return shebaID;
    }
    public BlockAccount            accountBlock(BlockAccount blockAccount) throws SenderException, InvalidParameterException, ResponseParsingException, SQLException {
        int SendCounter=0;
        blockAccount.setPin("1234");
        blockAccount.setRequestDateTime(persianDateTime.getRequestDateTime());
        blockAccount.setChannelType(PropertiesUtils.getBaseRequestMessage_Channeltype());
        blockAccount.setChannelPass(PropertiesUtils.getBaseRequestMessage_ChannelPass());
        blockAccount.setChannelID(PropertiesUtils.getBaseRequestMessage_ChannelID());
        blockAccount.setMsgSeq(getMsgSequence());
        blockAccount.setEncAlgorytm(PropertiesUtils.getBaseRequestMessage_EncAlgorytm());
        blockAccount.setMAC(PropertiesUtils.getBaseRequestMessage_MAC());
        AccountMessage Result=new AccountMessage();
        Result.setAction_code("3333");
        AccountMessage ResultTemp=new AccountMessage();
        try {
          //  while (!(Result.getAction_code().equals("1806")) && (SendCounter < 10)) {
              //  if (!Result.getAction_code().equals("1806")) ResultTemp = Result;
              //  if (ResultTemp.getAction_code().equals("9126")) break;
                Result= MainMQ.blockAccount(
                        blockAccount.getMsgSeq(),
                        blockAccount.getChannelType(),
                        blockAccount.getChannelID(),
                        blockAccount.getChannelPass(),
                        blockAccount.getAccountNumber(),
                        blockAccount.getPin(),
                        blockAccount.getBlockMode(),
                        blockAccount.getRequestDateTime(),
                        blockAccount.getEncAlgorytm(),
                        blockAccount.getMAC()
                );

               // SendCounter++;
            ResultTemp=Result;
          //  }
        }catch (Exception e) {
            blockAccount.setGatewayMessage("Gateway!!!"+Result.getAction_desc());
        }

        blockAccount.setResultFromChannel(ResultTemp);
        logTodb(blockAccount);
        return blockAccount;
    }
    public FollowUpTransactionACH  accountACHFollowFundTransfer(FollowUpTransactionACH followUpTransactionACH) throws SQLException {
        int SendCounter=0;
        followUpTransactionACH.setPin("1234");
        followUpTransactionACH.setRequestDateTime(persianDateTime.getRequestDateTime());
        followUpTransactionACH.setChannelType(PropertiesUtils.getBaseRequestMessage_Channeltype());
        followUpTransactionACH.setChannelPass(PropertiesUtils.getBaseRequestMessage_ChannelPass());
        followUpTransactionACH.setChannelID(PropertiesUtils.getBaseRequestMessage_ChannelID());
        followUpTransactionACH.setMsgSeq(getMsgSequence());
        followUpTransactionACH.setEncAlgorytm(PropertiesUtils.getBaseRequestMessage_EncAlgorytm());
        followUpTransactionACH.setMAC(PropertiesUtils.getBaseRequestMessage_MAC());
        ACHFundTransferMessage Result=new ACHFundTransferMessage();
        Result.setAction_code("3333");
        ACHFundTransferMessage ResultTemp=new ACHFundTransferMessage();
        try {
          //  while (!(Result.getAction_code().equals("1806")) && (SendCounter < 10)) {
              //  if (!Result.getAction_code().equals("1806")) ResultTemp = Result;
              //  if (ResultTemp.getAction_code().equals("9126")) break;
                Result= MainMQ.achFollowupByTrackId(
                        followUpTransactionACH.getMsgSeq(),
                        followUpTransactionACH.getChannelType(),
                        followUpTransactionACH.getChannelPass(),
                        followUpTransactionACH.getChannelID(),
                        followUpTransactionACH.getFollowUpCode(),
                        followUpTransactionACH.getRequestDateTime(),
                        followUpTransactionACH.getEncAlgorytm(),
                        followUpTransactionACH.getMAC()
                );

               // SendCounter++;
            ResultTemp=Result;
          //  }
        }catch (Exception e) {
            followUpTransactionACH.setGatewayMessage("Gateway!!!"+Result.getAction_desc());
        }

        followUpTransactionACH.setResultFromChannel(ResultTemp);
        logTodb(followUpTransactionACH);
        return followUpTransactionACH;
    }


    public BalanceForCard          cardBalance(BalanceForCard balanceForCard) throws UnsupportedEncodingException, SQLException, InterruptedException {

        SendToSabaSwitch sendToSabaSwitch =new SendToSabaSwitch(balanceForCard.getPan(),balanceForCard.getPin());

        if (!sendToSabaSwitch.getSendResult()) balanceForCard.setActionCode("3333");
        else{
            ISO200 ISO200 = sendToSabaSwitch.getCard200();

            CashResponse cashResponse=new CashResponse();
            cashResponse.setCard200(ISO200);

            if (cashResponse.resultOfSentMessageIsOK()){

                if (cashResponse.getResultOfSentMessageIsOK()){
                   // System.out.println("in cashResponse");
                    balanceForCard.setActionCode(cashResponse.getCard210().getActionCode());

                    balanceForCard.setResultFromServer(cashResponse.getCard210());
                    //balanceForCard.getResultFromServer().getA
                }else{
                    balanceForCard.setActionCode("9126");
                }
            }else{
                balanceForCard.setActionCode("9126");
            }
        }
        return balanceForCard;
    }
    public BlockCard               cardBlockCard(BlockCard blockCard) throws UnsupportedEncodingException, SQLException, InterruptedException {
        SendToSabaSwitch sendToSabaSwitch =new SendToSabaSwitch("hotcard",blockCard.getPan(),blockCard.getPin());
        if (!sendToSabaSwitch.getSendResult()) blockCard.setActionCode("3333");
        else{
            ISO200 ISO200 = sendToSabaSwitch.getCard200();

            CashResponse cashResponse=new CashResponse();
            cashResponse.setCard200(ISO200);
            if (cashResponse.resultOfSentMessageIsOK()){
                if (cashResponse.getResultOfSentMessageIsOK()){
                    blockCard.setActionCode(cashResponse.getCard210().getActionCode());

                    blockCard.setResultFromServer(cashResponse.getCard210());
                }else{
                    blockCard.setActionCode("9126");
                }
            }
        }
        return blockCard;
    }

    public BillInfoByTelNumber     telSwitchGetBillData(BillInfoByTelNumber billInfoByTelNumber) throws UnsupportedEncodingException, SQLException, InterruptedException {
        SendToTelSwitch sendToTelSwitch =new SendToTelSwitch(billInfoByTelNumber.getPan(),
                                                             billInfoByTelNumber.getIsMobile(),
                                                             billInfoByTelNumber.getTelNo());

        if (!sendToTelSwitch.getSendResult()) billInfoByTelNumber.setActionCode("3333");
        else{
            ISO100 ISO100 = sendToTelSwitch.getCard100();

            CashResponse cashResponse=new CashResponse();
            cashResponse.setCard100(ISO100);
            if (cashResponse.resultOfTelSwitchSentMessageIsOK()){
                if (cashResponse.getResultOfTelSwitchSentMessageIsOK()){

                    billInfoByTelNumber.setResultFromServer(cashResponse.getCard110());
                     System.out.println("gott code :" + billInfoByTelNumber.getResultFromServer().getResponseTransactionCode());
                    if (billInfoByTelNumber.getResultFromServer().getResponseTransactionCode().equals("00")){
                        billInfoByTelNumber.setActionCode("0000");
                    }else{
                        billInfoByTelNumber.setActionCode("70"+billInfoByTelNumber.getResultFromServer().getResponseTransactionCode());
                    }
                }else{
                    billInfoByTelNumber.setActionCode("9126");
                }
            }else{
                billInfoByTelNumber.setActionCode("9126");
            }
        }
        return billInfoByTelNumber;
    }
    public BillPaySayByTelNumber   telSwitchSetBillPaymentData(BillPaySayByTelNumber billPaySayByTelNumber) throws UnsupportedEncodingException, SQLException, InterruptedException {

        SendToTelSwitch sendToTelSwitch =new SendToTelSwitch(
                                                            billPaySayByTelNumber.getPan(),
                                                            billPaySayByTelNumber.getTraceCode(),
                                                            billPaySayByTelNumber.getReferenceCode(),
                                                            billPaySayByTelNumber.getBillID(),
                                                            billPaySayByTelNumber.getPaymentID(),
                                                            billPaySayByTelNumber.getPayDate(),
                                                            billPaySayByTelNumber.getTelNo(),
                billPaySayByTelNumber.getIsMobile());

        System.out.println("in say bill pay:"+billPaySayByTelNumber.getPan());
        if (!sendToTelSwitch.getSendResult()) billPaySayByTelNumber.setActionCode("3333");
        else{
            ISO100 ISO100 = sendToTelSwitch.getCard100();

            CashResponse cashResponse=new CashResponse();
            cashResponse.setCard100(ISO100);
            if (cashResponse.getResultOfTelSwitchSentMessageIsOK()){

                billPaySayByTelNumber.setResultFromServer(cashResponse.getCard110());
                if (billPaySayByTelNumber.getResultFromServer().getResponseTransactionCode().equals("00")){
                    billPaySayByTelNumber.setActionCode("0000");
                }else{
                    billPaySayByTelNumber.setActionCode("70"+billPaySayByTelNumber.getResultFromServer().getResponseTransactionCode());
                }
            }else{
                billPaySayByTelNumber.setActionCode("6666");
            }
        }
        return billPaySayByTelNumber;
    }

    public Status_All systemStatus(Status_All status) throws InterruptedException {

        try {
            status.setIsOSOfGateway(true);
            status.setIsDataBaseServer(false);
            status.setIsTelBankOSStatus(false);
            status.setIsTelBankStatus(false);
            status.processSystemStatus();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return status;

    }
    public MonitoringStatus createMonitoringReport(MonitoringStatus monitoringStatus) throws InterruptedException {

        DBUtils dbUtils=new DBUtils();
        monitoringStatus = dbUtils.createMonitoringReport();

        return monitoringStatus;
    }


    //Payments

    public FundTransfer            accountFundTransfer(FundTransfer fundTransfer) throws SenderException, InvalidParameterException, ResponseParsingException, SQLException {
        int SendCounter=0;
        String messageSequence=getMsgSequence();
        String traceCode=messageSequence.substring(6,12);
        fundTransfer.setPin("1234");
        fundTransfer.setCurrency("364");
        fundTransfer.setRequestDateTime(persianDateTime.getRequestDateTime());
        fundTransfer.setRequestDateTime(persianDateTime.getRequestDateTime());
        fundTransfer.setChannelType(PropertiesUtils.getBaseRequestMessage_Channeltype());
        fundTransfer.setChannelPass(PropertiesUtils.getBaseRequestMessage_ChannelPass());
        fundTransfer.setChannelID(PropertiesUtils.getBaseRequestMessage_ChannelID());
        fundTransfer.setMsgSeq(messageSequence);
        fundTransfer.setPayCode1(fundTransfer.getTransferID());
        fundTransfer.setEncAlgorytm(PropertiesUtils.getBaseRequestMessage_EncAlgorytm());
        fundTransfer.setMAC(PropertiesUtils.getBaseRequestMessage_MAC());
        FundTransferMessage Result=new FundTransferMessage();
        Result.setAction_code("3333");
        FundTransferMessage ResultTemp=new FundTransferMessage();
        try{
          //  while (!(Result.getAction_code().equals("1806"))&&(SendCounter<10)){
          //      if (!Result.getAction_code().equals("1806")) ResultTemp=Result;
         //       if (ResultTemp.getAction_code().equals("9126")) break;
                Result= MainMQ.FundTransfer(  //dpi  mq connection
                        fundTransfer.getSourceAccount(),
                        fundTransfer.getDestinationAccount(),
                        fundTransfer.getTransactionAmount(),
                        fundTransfer.getCurrency(),
                        fundTransfer.getTransactionDescription(),
                        fundTransfer.getPin(),
                        fundTransfer.getChannelType(),
                        fundTransfer.getChannelPass(),
                        fundTransfer.getChannelID(),
                        fundTransfer.getOperationCode(),
                        fundTransfer.getPayCode1(),
                        fundTransfer.getPayCode2(),
                        fundTransfer.getExtraInfo(),
                        fundTransfer.getRequestDateTime(),
                        fundTransfer.getEncAlgorytm(),
                        fundTransfer.getMAC(),
                        fundTransfer.getMsgSeq()
                );

             //   SendCounter++;
            ResultTemp=Result;
           // }
        }catch (Exception e) {
            fundTransfer.getResultFromChannel().setAction_code("3000");
            fundTransfer.setGatewayMessage("Gateway!!!"+Result.getAction_desc());
        }

        fundTransfer.setResultFromChannel(ResultTemp);
        fundTransfer.setTraceID(traceCode);
       // logTodb(fundTransfer);

        return fundTransfer;
    }
    public FundTransfer            accountFundTransferNoCost(FundTransfer fundTransfer) throws SenderException, InvalidParameterException, ResponseParsingException, SQLException {
        int SendCounter=0;
        String followUpCode="";
        String fType="0";
        fundTransfer.setPin("1234");
        fundTransfer.setCurrency("364");
        fundTransfer.setRequestDateTime(persianDateTime.getRequestDateTime());
        fundTransfer.setRequestDateTime(persianDateTime.getRequestDateTime());
        fundTransfer.setChannelType(PropertiesUtils.getBaseRequestMessage_Channeltype());
        fundTransfer.setChannelPass(PropertiesUtils.getBaseRequestMessage_ChannelPass());
        fundTransfer.setChannelID(PropertiesUtils.getBaseRequestMessage_ChannelID());
        fundTransfer.setMsgSeq(getMsgSequence());
        fundTransfer.setPayCode1("");
        fundTransfer.setPayCode2("");
        fundTransfer.setOperationCode("");
        fundTransfer.setEncAlgorytm(PropertiesUtils.getBaseRequestMessage_EncAlgorytm());
        fundTransfer.setMAC(PropertiesUtils.getBaseRequestMessage_MAC());
        fundTransfer.setTraceID(fundTransfer.getMsgSeq().substring(6, 12));
        FundTransferNoCostMessage Result=new FundTransferNoCostMessage();

        Result.setAction_code("3333");
        FundTransferNoCostMessage ResultTemp=new FundTransferNoCostMessage();
        try{
          //  while (!(Result.getAction_code().equals("1806"))&&(SendCounter<10)){
          //      if (!Result.getAction_code().equals("1806")) ResultTemp=Result;
         //       if (ResultTemp.getAction_code().equals("9126")) break;
                Result= MainMQ.FundTransferNoCost(  //dpi  mq connection
                        fundTransfer.getSourceAccount(),
                        fundTransfer.getDestinationAccount(),
                        fundTransfer.getTransactionAmount(),
                        fundTransfer.getCurrency(),
                        fundTransfer.getTransactionDescription(),
                        fundTransfer.getPin(),
                        fundTransfer.getChannelType(),
                        fundTransfer.getChannelPass(),
                        fundTransfer.getChannelID(),
                        fundTransfer.getOperationCode(),
                        fundTransfer.getPayCode1(),
                        fundTransfer.getPayCode2(),
                        followUpCode,
                        fundTransfer.getExtraInfo(),
                        fundTransfer.getRequestDateTime(),
                        fundTransfer.getEncAlgorytm(),
                        fundTransfer.getMAC(),
                        fundTransfer.getMsgSeq(),
                        fType
                );

             //   SendCounter++;
            ResultTemp=Result;
           // }
        }catch (Exception e) {
            fundTransfer.getResultFromChannel().setAction_code("3000");
            fundTransfer.setGatewayMessage("Gateway!!!"+Result.getAction_desc());
        }

        fundTransfer.setResultFromChannel(getFundTransferMessageObject(ResultTemp));
       // logTodb(fundTransfer);

        return fundTransfer;
    }
    public FundTransferMessage     getFundTransferMessageObject(FundTransferNoCostMessage fundTransferNoCostMessage){
        FundTransferMessage fundTransfer=new FundTransferMessage();
        fundTransfer.setAction_code(fundTransferNoCostMessage.getAction_code());
        fundTransfer.setAccountPass(fundTransferNoCostMessage.getAccountPass());
        fundTransfer.setBlckNumber(fundTransferNoCostMessage.getBlckNumber());
        fundTransfer.setCurrency(fundTransferNoCostMessage.getCurrency());
        fundTransfer.setDstAccountNumber(fundTransferNoCostMessage.getDstAccountNumber());
        fundTransfer.setFPayCode(fundTransferNoCostMessage.getFPayCode());
        fundTransfer.setOpCode(fundTransferNoCostMessage.getOpCode());
        fundTransfer.setRefNo(fundTransferNoCostMessage.getRefCode());
        fundTransfer.setReqsecuence(fundTransferNoCostMessage.getReqsecuence());
        fundTransfer.setSrcAccountNumber(fundTransferNoCostMessage.getSrcAccountNumber());
        fundTransfer.setSPayCode(fundTransferNoCostMessage.getSPayCode());
        fundTransfer.setTransAmount(fundTransferNoCostMessage.getTransAmount());
        fundTransfer.setTransDesc(fundTransferNoCostMessage.getFtDesc());
        fundTransfer.setAction_desc(fundTransferNoCostMessage.getAction_desc());
        fundTransfer.setChannelId(fundTransferNoCostMessage.getChannelId());
        fundTransfer.setChannelType(fundTransferNoCostMessage.getChannelType());
        fundTransfer.setEncAlgorytm(fundTransferNoCostMessage.getEncAlgorytm());
        fundTransfer.setMAC(fundTransferNoCostMessage.getMAC());
        fundTransfer.setMsgSequence(fundTransferNoCostMessage.getMsgSequence());
        fundTransfer.setReqDateTime(fundTransferNoCostMessage.getReqDateTime());
        fundTransfer.setResponseXml(fundTransferNoCostMessage.getResponseXml());
        fundTransfer.setTxDate(fundTransferNoCostMessage.getTxDate());
        fundTransfer.setTxTime(fundTransferNoCostMessage.getTxTime());

        return fundTransfer;
    }
    public FollowUpTransaction     accountFollowUpTransaction(FollowUpTransaction followUpTransaction) throws SenderException, InvalidParameterException, ResponseParsingException, SQLException {
        int SendCounter=0;
        followUpTransaction.setPin("1234");
        followUpTransaction.setRequestDateTime(persianDateTime.getRequestDateTime());
        followUpTransaction.setChannelType(PropertiesUtils.getBaseRequestMessage_Channeltype());
        followUpTransaction.setChannelPass(PropertiesUtils.getBaseRequestMessage_ChannelPass());
        followUpTransaction.setChannelID(PropertiesUtils.getBaseRequestMessage_ChannelID());
        followUpTransaction.setMsgSeq(getMsgSequence());
        followUpTransaction.setEncAlgorytm(PropertiesUtils.getBaseRequestMessage_EncAlgorytm());
        followUpTransaction.setMAC(PropertiesUtils.getBaseRequestMessage_MAC());
        FollowUpMessage Result=new FollowUpMessage();
        Result.setAction_code("3333");
        FollowUpMessage ResultTemp=new FollowUpMessage();
        try {
         //   while (!(Result.getAction_code().equals("1806")) && (SendCounter < 10)) {
          //      if (!Result.getAction_code().equals("1806")) ResultTemp = Result;
              //  if (ResultTemp.getAction_code().equals("9126")) break;
                Result= MainMQ.FollowUp(
                        followUpTransaction.getSourceAccount(),
                        followUpTransaction.getDestinationAccount(),
                        followUpTransaction.getFollowUpCode(),
                        followUpTransaction.getPin(),
                        followUpTransaction.getTransactionDateTime(),
                        followUpTransaction.getRequestDateTime(),
                        followUpTransaction.getChannelType(),
                        followUpTransaction.getChannelPass(),
                        followUpTransaction.getChannelID(),
                        followUpTransaction.getMsgSeq(),
                        followUpTransaction.getEncAlgorytm(),
                        followUpTransaction.getMAC()
                );

              //  SendCounter++;
            ResultTemp=Result;
          //  }
        }catch (Exception e) {
            followUpTransaction.setGatewayMessage("Gateway!!!"+Result.getAction_desc());
        }

        followUpTransaction.setResultFromChannel(ResultTemp);
       logTodb(followUpTransaction);
        return followUpTransaction;
    }
    public BillPayByIDAccount      accountBillPaymentByBillID(BillPayByIDAccount billPayByIDAccount) throws SenderException, InvalidParameterException, ResponseParsingException, SQLException {
        int SendCounter=0;
        String messageSequence=getMsgSequence();
        String traceCode=messageSequence.substring(6,12);
        billPayByIDAccount.setPin("1234");

        billPayByIDAccount.setRequestDateTime(persianDateTime.getRequestDateTime());
        billPayByIDAccount.setChannelType(PropertiesUtils.getBaseRequestMessage_Channeltype());
        billPayByIDAccount.setChannelPass(PropertiesUtils.getBaseRequestMessage_ChannelPass());
        billPayByIDAccount.setChannelID(PropertiesUtils.getBaseRequestMessage_ChannelID());
        billPayByIDAccount.setMsgSeq(messageSequence);

        billPayByIDAccount.setEncAlgorytm(PropertiesUtils.getBaseRequestMessage_EncAlgorytm());
        billPayByIDAccount.setMAC(PropertiesUtils.getBaseRequestMessage_MAC());
        BillPaymentMessage Result=new BillPaymentMessage();
        Result.setAction_code("3333");
        BillPaymentMessage ResultTemp=new BillPaymentMessage();
        try {
        //    while (!(Result.getAction_code().equals("1806")) && (SendCounter < 10)) {
          //      if (!Result.getAction_code().equals("1806")) ResultTemp = Result;
          //      if (ResultTemp.getAction_code().equals("9126")) break;
                Result= MainMQ.BillPayment(

                        billPayByIDAccount.getMsgSeq(),
                        billPayByIDAccount.getSourceAccount(),
                        billPayByIDAccount.getPin(),
                        billPayByIDAccount.getRequestDateTime(),
                        billPayByIDAccount.getChannelType(),
                        billPayByIDAccount.getChannelPass(),
                        billPayByIDAccount.getChannelID(),
                        billPayByIDAccount.getAmount(),
                        billPayByIDAccount.getBillID(),
                        billPayByIDAccount.getPaymentID(),
                        billPayByIDAccount.getEncAlgorytm(),
                        billPayByIDAccount.getMAC()
                );

            //    SendCounter++;
            ResultTemp=Result;
          //  }
        }catch (Exception e) {
            billPayByIDAccount.setGatewayMessage("Gateway!!!"+Result.getAction_desc());
        }

        billPayByIDAccount.setResultFromChannel(ResultTemp);
        billPayByIDAccount.setTraceCode(traceCode);
//        logTodb(billPayByIDAccount);
        return billPayByIDAccount;
    }
    public BillPayByIDAccountValid accountBillPayByIDValidation(BillPayByIDAccountValid billPayByIDAccountValid) throws SenderException, InvalidParameterException, ResponseParsingException, SQLException {
        int SendCounter=0;
        billPayByIDAccountValid.setPin("1234");
        billPayByIDAccountValid.setRequestDateTime(persianDateTime.getRequestDateTime());
        billPayByIDAccountValid.setChannelType(PropertiesUtils.getBaseRequestMessage_Channeltype());
        billPayByIDAccountValid.setChannelPass(PropertiesUtils.getBaseRequestMessage_ChannelPass());
        billPayByIDAccountValid.setChannelID(PropertiesUtils.getBaseRequestMessage_ChannelID());
        billPayByIDAccountValid.setMsgSeq(getMsgSequence());
        billPayByIDAccountValid.setEncAlgorytm(PropertiesUtils.getBaseRequestMessage_EncAlgorytm());
        billPayByIDAccountValid.setMAC(PropertiesUtils.getBaseRequestMessage_MAC());
        BillPaymentValidation Result=new BillPaymentValidation();
        Result.setAction_code("3333");
        BillPaymentValidation ResultTemp=new BillPaymentValidation();
        try {
        //    while (!(Result.getAction_code().equals("1806")) && (SendCounter < 10)) {
          //      if (!Result.getAction_code().equals("1806")) ResultTemp = Result;
            //    if (ResultTemp.getAction_code().equals("9126")) break;
                Result= MainMQ.BillPaymentValidation(
                        billPayByIDAccountValid.getMsgSeq(),
                        billPayByIDAccountValid.getSourceAccount(),
                        billPayByIDAccountValid.getPin(),
                        billPayByIDAccountValid.getRequestDateTime(),
                        billPayByIDAccountValid.getChannelType(),
                        billPayByIDAccountValid.getChannelPass(),
                        billPayByIDAccountValid.getChannelID(),
                        billPayByIDAccountValid.getAmount(),
                        billPayByIDAccountValid.getBillID(),
                        billPayByIDAccountValid.getPaymentID(),
                        billPayByIDAccountValid.getEncAlgorytm(),
                        billPayByIDAccountValid.getMAC()
                );

             //   SendCounter++;
            ResultTemp=Result;
           // }
        }catch (Exception e) {
            billPayByIDAccountValid.setGatewayMessage("Gateway!!!"+Result.getAction_desc());
        }

        billPayByIDAccountValid.setResultFromChannel(ResultTemp);
       logTodb(billPayByIDAccountValid);
        return billPayByIDAccountValid;
    }
    public BillPayByBillIDPan      cardBillPayByBillIDPan(BillPayByBillIDPan billPayByBillIDPan) throws UnsupportedEncodingException, SQLException, InterruptedException {
        ISO200 ISO200 =null;
        billPayByBillIDPan.setMsgSeq(getMsgSequence()) ;
        SendToSabaSwitch sendToSabaSwitch =new SendToSabaSwitch( billPayByBillIDPan.getPan(),
                billPayByBillIDPan.getPin(),
                billPayByBillIDPan.getBillID(),
                billPayByBillIDPan.getPaymentID(),
                billPayByBillIDPan.getAmount());

        if (!sendToSabaSwitch.getSendResult()) billPayByBillIDPan.setActionCode("3333");
        else{
            ISO200 = sendToSabaSwitch.getCard200();
            //test

            CashResponse cashResponse=new CashResponse();
            cashResponse.setCard200(ISO200);

            if (cashResponse.resultOfSentMessageIsOK()){
//                System.out.println("in card bill pay send ok");
                if (cashResponse.getResultOfSentMessageIsOK()){
//                    System.out.println("in card bill pay result ok");
                    billPayByBillIDPan.setActionCode(cashResponse.getCard210().getActionCode());
                    billPayByBillIDPan.setTraceCode(cashResponse.getCard210().getTraceCode());
                    billPayByBillIDPan.setResultFromServer(cashResponse.getCard210());

                    System.out.println("***** "+cashResponse.getCard210().getTraceCode());

                }else{
                    billPayByBillIDPan.setActionCode("9126");
                    sendToSabaSwitch =new SendToSabaSwitch(ISO200);
                }
            }
        }
//        logTodb(billPayByBillIDPan);
        return billPayByBillIDPan;
    }


    public InternalFollowUp        FollowUpTransactionInternal(InternalFollowUp followUpTransaction) throws SenderException, InvalidParameterException, ResponseParsingException, SQLException {
        DBUtils dbUtils = new DBUtils();
        if (dbUtils.connected) followUpTransaction = dbUtils.internalFollowUpTransaction(followUpTransaction);
        else followUpTransaction.setActionCode("6666");
        return followUpTransaction;
    }

    public ClientJarSettings       getSettingForClientJarFile(ClientJarSettings clientJarSettings){

        clientJarSettings.setUseOldAuthenticate(PropertiesUtils.getClientJarUseOldAuthenticate());
        clientJarSettings.setUseOldGateway(PropertiesUtils.getClientJarUseOldGateway());
        clientJarSettings.setUseOldPayment(PropertiesUtils.getClientJarUseOldPayment());
        clientJarSettings.setUseOldTelSwitch(PropertiesUtils.getClientJarUseOldTelSwithch());
        clientJarSettings.setActionCode("0000");

        return clientJarSettings;

    }

}



