package ResponseServices;

import Mainchannel.Exceptions.InvalidParameterException;
import Mainchannel.Exceptions.ResponseParsingException;
import Mainchannel.messages.StatementMessage;
import Mainchannel.sender.SenderException;
import ServiceObjects.Account.*;
import ServiceObjects.Other.*;
import ServiceObjects.Pan.BalanceForCard;
import ServiceObjects.Pan.BillPayByBillIDPan;
import ServiceObjects.Pan.BlockCard;
import ServiceObjects.Pin.AuthenticatePin1;
import ServiceObjects.Pin.AuthenticatePin2;
import SystemStatus.Status_All;
import com.sun.corba.se.spi.ior.ObjectKey;
import utils.PersianDateTime;
import utils.PropertiesUtils;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.rmi.RemoteException;
import java.rmi.server.RemoteServer;
import java.rmi.server.ServerNotActiveException;
import java.sql.SQLException;

public class resultFromBank implements java.io.Serializable {
    private static final long serialVersionUID = 7933179759822777200L;
    GatewayServices gatewayServices=new GatewayServices();
    Payment payment=null;
    PaymentPreMethodS paymentPreMethodS=null;
    private  PersianDateTime PDT = new PersianDateTime();
    public static synchronized String getMsgSequence() {
        return String.valueOf(System.nanoTime()).substring(0,12);
    }
    ObjectCompare objectCompare=new ObjectCompare();
    int lenOfStringRequest=-1;
    long tempVar=-1;
    String clientIP;

    public resultFromBank(String params[]){
        setRequestString(params);
        if (!helpRequested(params)) sendAndReceiveStringRequest();

    }
    public resultFromBank(Object request) throws RemoteException, SQLException {
        setRequestObject(request);
        try {
            clientIP= RemoteServer.getClientHost();
        } catch (ServerNotActiveException e) {
            e.printStackTrace();
        }
        sendAndReceiveObjectRequest();
    }

    private int    CommandCode=-1;
    public  void   setCommandCode(int commandCode){
        CommandCode=commandCode;
    }
    public  int    getCommandCode(){
        return CommandCode;
    }

    private int    IndexOfInvalidParameter=-1;
    public  void   setIndexOfInvalidParameter(int indexOfInvalidParameter){
        IndexOfInvalidParameter=indexOfInvalidParameter;
    }
    public  int    getIndexOfInvalidParameter(){
        return IndexOfInvalidParameter;
    }

    private String RequestString[]=null;
    public  void     setRequestString(String[] requestString){
        RequestString=requestString;
    }
    public  String[] getRequestString(){
        return RequestString;
    }

    private String ResultString="";
    public  void     setResultString(String resultString){
        ResultString=resultString;
    }
    public  String   getResultString(){
        return ResultString;
    }

    private String ActionCode="";
    public  void     setActionCode(String actionCode){
        ActionCode=actionCode;
    }
    public  String   getActionCode(){
        return ActionCode;
    }


    private Object RequestObject=null;
    public  void   setRequestObject(Object requestObject){
        RequestObject=requestObject;
    }
    public  Object getRequestObject(){
        return RequestObject;
    }

    private Object ResultObject="";
    public  void   setResultObject(Object resultObject){
        ResultObject=resultObject;
    }
    public  Object getResultObject(){
        return ResultObject;
    }


    public  String  printToScreen(String S) {
        S = PDT.getShamsiDate() + " " + PDT.GetNowTime() + " --> " + S;

        int i;
        for(i = 0; i < S.length(); ++i) {
            System.out.print("_");
        }

        System.out.println("_");

        for(i = 0; i < S.length() + 1; ++i) {
            System.out.print(" ");
        }

        System.out.println("|");
        System.out.println(S + " |");

        for(i = 0; i < S.length(); ++i) {
            System.out.print("_");
        }

        System.out.println("_|");
        return S;
    }
    private boolean parametersNumericalIsOK(){
        lenOfStringRequest=this.getRequestString().length;
        String temp="";
        for(int i=2;i<lenOfStringRequest;i++){
            try{
                temp=this.getRequestString()[i];
                tempVar=Long.valueOf(temp);
            }catch (NumberFormatException var1){
                this.setIndexOfInvalidParameter(i);
                return false;
            }
        }
        return true;
    }

    private java.sql.Timestamp getCurrentTimeStamp() {

        java.util.Date today = new java.util.Date();
        long t = today.getTime();
        return new java.sql.Timestamp(t);

    }

    private boolean helpRequested(String parameters[]){
        if (parameters[2].equals("help")) {
            getHelpOfCommand(parameters);
            return true;
        }else return false;
    }
    private void    getHelpOfCommand(String parameters[]) {
        String fileName=getFileName(parameters[3]);
        if (fileName.equals("-2")){
            this.setResultString( "$6505*Description : " + "Command with code : "+parameters[3]+" not exist!*$");
            return;
        }else if (fileName.equals("-1")){
            this.setResultString( "$6506*Description : " + "code : "+parameters[3]+" is not valid numeric code!*$");
            return;
        }else{
            try {
                fileName="\\Result\\HelpFiles\\"+fileName;
                BufferedReader input = new BufferedReader(
                        new InputStreamReader(
                                this.getClass().getClassLoader().getResourceAsStream(fileName)));

                try (BufferedReader br = input) {
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        //System.out.println(line);
                        this.setResultString(this.getResultString() + line+"\n");
                    }
                } catch (Exception var1) {
                }
            } catch (Exception var2) {
                printToScreen(var2.getMessage());
            }
        }


    }
    private static  String getFileName(String C) {
        int kind = -1;
        try {
            kind = Integer.valueOf(C);
        } catch (NumberFormatException var1) {
            if (C.equals("code")) return "Main";
        }
        switch (kind) {
            case -1:
                return "-1";
            case 301:
                return "301_Account_Authenticate_Pin1";
            case 302:
                return "302_Account_ChangePin_Pin1";
            case 303:
                return "303_Account_Authenticate_Pin2";
            case 304:
                return "304_Account_ChangePin_Pin2";
            case 0:
                return "000_Account_Information";
            case 1:
                return "001_Account_Balance";
            case 2:
                return "002_Account_GetMobileOfAccount";
            case 3:
                return "003_Account_SetMobileOfAccount";
            case 4:
                return "004_Account_StatementList";
            case 5:
                return "005_Account_GetPan";
            case 6:
                return "006_Account_GetAccountOfPan";
            case 7:
                return "007_Account_Register";
            case 8:
                return "008_Account_FundTransfer";
            case 9:
                return "009_Account_FollowUpFundTransfer";
            case 10:
                return "010_Account_BillPaymentByBillID";
            case 11:
                return "011_Account_BillPaymentValidation";
            case 12:
                return "012_Account_ChequeStatus";
            case 13:
                return "013_Account_GetShebaID";
            case 14:
                return "012_Account_GetPanOfAccount";
            case 15:
                return "015_Account_Block";
            case 16:
                return "016_Account_ACHFollowUP";
            case 401:
                return "401_Account_GetBillData";
            case 402:
                return "402_Account_SetBillPamentData";
            case 501:
                return "501_internalFollowUp";
            default:
                return "-2";
        }
    }

    private void  sendAndReceiveStringRequest(){
        if (parametersNumericalIsOK()) {
            try{

                this.setCommandCode(Integer.valueOf(getRequestString()[2]));
                switch (this.getCommandCode()){
                    case  301:accountPin1authenticate();
                        break;
                    case  302:accountPin1ChangePin();
                        break;
                    case  303:accountPin2authenticate();
                        break;
                    case  304:accountPin2ChangePin();
                        break;
                    case    0:accountInformation();
                        break;
                    case    1:accountBalance();
                        break;
                    case    2:accountGetMobileOfAccount();
                        break;
                    case    3:accountSetMobileOfAccount();
                        break;
                    case    4:accountTransactions();
                        break;
                    case    5:accountGetPan();
                        break;
                    case    6:accountGetAccountOfPan();
                        break;
                    case    7:accountRegister();
                        break;
                    case    8:accountFundTransfer();
                        break;
                    case    9:accountFollowUpFundTransfer();
                        break;
                    case   10:accountBillPaymentByBillID();
                        break;
                    case   11:accountBillPaymentValidation();
                        break;
                    case   12:accountChequeStatus();
                        break;
                    case   13:accountGetShebaID();
                        break;
                    case   14:accountGetAccountOfPan();
                        break;
                    case   15:accountBlock();
                        break;
                    case   16:accountACHFollowUp();
                        break;
                    case   202:panBillPay();
                        break;
                    case   401:telSwitchGetBillData();
                        break;
                    case   402:telSwitchSayBillPayment();
                        break;
                    case   501:internalFollowUpFundTransfer();
                        break;
                }
            }catch (InvalidParameterException e) {
                e.printStackTrace();
            } catch (ResponseParsingException e) {
                e.printStackTrace();
            } catch (SenderException e) {
                e.printStackTrace();
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }else{
            this.setResultString( "-9: Description :parameter number :"+this.getRequestString()[getIndexOfInvalidParameter()]+" is not numeric");
        }

    }
    private void  sendAndReceiveObjectRequest() throws RemoteException, SQLException {

        try{
            if (this.getRequestObject() instanceof BalanceForAccount)
              accountBalance((BalanceForAccount) this.getRequestObject());
            else if (this.getRequestObject() instanceof AuthenticatePin1)
                accountPin1authenticate((AuthenticatePin1) this.getRequestObject());
            else if (this.getRequestObject() instanceof AuthenticatePin2)
                accountPin2authenticate((AuthenticatePin2) this.getRequestObject());
            else if (this.getRequestObject() instanceof AccountInformation)
                accountInformation((AccountInformation) this.getRequestObject());
            else if (this.getRequestObject() instanceof SMSAlarmTransaction){
              SMSAlarmTransaction smsAlarmTransaction=(SMSAlarmTransaction) this.getRequestObject();
                accountSetMobileOfAccount(smsAlarmTransaction);
                // if (smsAlarmTransaction.getIsSetMobileNumber()) accountSetMobileOfAccount(smsAlarmTransaction);
            //  else accountGetMobileOfAccount((SMSAlarmTransaction) this.getRequestObject());
              smsAlarmTransaction=null;}
            else if (this.getRequestObject() instanceof Transaction)
                accountTransactions((Transaction) this.getRequestObject());
            else if (this.getRequestObject() instanceof CardNoOfAccount)
                accountGetPan((CardNoOfAccount) this.getRequestObject());
            else if (this.getRequestObject() instanceof AccountNoOfCard)
                accountGetAccountOfPan((AccountNoOfCard) this.getRequestObject());
            else if (this.getRequestObject() instanceof RegisterAccount)
                accountRegister((RegisterAccount) this.getRequestObject());
            else if (this.getRequestObject() instanceof FundTransfer)  //payment
                accountFundTransfer((FundTransfer) this.getRequestObject());
            else if (this.getRequestObject() instanceof FollowUpTransaction)//payment
                accountFollowUpFundTransfer((FollowUpTransaction) this.getRequestObject());
            else if (this.getRequestObject() instanceof InternalFollowUp){

                internalFollowUpFundTransfer((InternalFollowUp) this.getRequestObject());
            }
            else if (this.getRequestObject() instanceof BillPayByIDAccount)//payment
                accountBillPaymentByBillID((BillPayByIDAccount) this.getRequestObject());
            else if (this.getRequestObject() instanceof BillPayByIDAccountValid)
                accountBillPaymentValidation((BillPayByIDAccountValid) this.getRequestObject());
            else if (this.getRequestObject() instanceof ChequeStatus)
                accountChequeStatus((ChequeStatus) this.getRequestObject());
            else if (this.getRequestObject() instanceof BlockAccount)
                accountBlock((BlockAccount) this.getRequestObject());
            else if (this.getRequestObject() instanceof FollowUpTransactionACH)
                accountACHFollowUp((FollowUpTransactionACH) this.getRequestObject());
            else if (this.getRequestObject() instanceof ShebaID)
                accountGetShebaID((ShebaID) this.getRequestObject());
            else if (this.getRequestObject() instanceof BillInfoByTelNumber)
                telSwitchGetBillData((BillInfoByTelNumber) this.getRequestObject());
            else if (this.getRequestObject() instanceof BillPaySayByTelNumber)
                telSwitchSayBillPayment((BillPaySayByTelNumber) this.getRequestObject());
            else if (this.getRequestObject() instanceof BalanceForCard)
                panGetBalance((BalanceForCard) this.getRequestObject());
            else if (this.getRequestObject() instanceof BillPayByBillIDPan)//payment
                panBillPay((BillPayByBillIDPan) this.getRequestObject());
            else if (this.getRequestObject() instanceof BlockCard)
                panBlock((BlockCard) this.getRequestObject());
            else if (this.getRequestObject() instanceof BillPaySayByTelNumber)
                telSwitchSayBillPayment((BillPaySayByTelNumber) this.getRequestObject());
            else if (this.getRequestObject() instanceof Status_All)
                systemstatus((Status_All) this.getRequestObject());
            else if (this.getRequestObject() instanceof ClientJarSettings)
                settingForClient((ClientJarSettings) this.getRequestObject());
            else if (this.getRequestObject() instanceof MonitoringStatus)
                createMonitoringReport((MonitoringStatus) this.getRequestObject());


        } catch (InvalidParameterException e) {
            e.printStackTrace();
        } catch (ResponseParsingException e) {
            e.printStackTrace();
        } catch (SenderException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

    }

    private void createMonitoringReport(MonitoringStatus monitoringStatus) throws InterruptedException {

        this.setResultObject(gatewayServices.createMonitoringReport(monitoringStatus));
    }

    public  void  getActionCodeFromObject(Object requestObject) throws RemoteException, SQLException {

        if (requestObject == null) {
            setActionCode("4444");
            return;
        }
        try{
            if (requestObject instanceof BalanceForAccount)
                setActionCode(((BalanceForAccount) requestObject).getResultFromChannel().getAction_code());
            else if (requestObject instanceof AuthenticatePin1)
                setActionCode(((AuthenticatePin1) requestObject).getActionCode());
            else if (requestObject instanceof AuthenticatePin2)
                setActionCode(((AuthenticatePin2) requestObject).getActionCode());
            else if (requestObject instanceof AccountInformation)
                setActionCode(((AccountInformation) requestObject).getResultFromChannel().getAction_code());
            else if (requestObject instanceof SMSAlarmTransaction){
                setActionCode(((SMSAlarmTransaction) requestObject).getActionCode());
            }
              /*  SMSAlarmTransaction smsAlarmTransaction=(SMSAlarmTransaction) this.getRequestObject();
                accountSetMobileOfAccount(smsAlarmTransaction);*/
                // if (smsAlarmTransaction.getIsSetMobileNumber()) accountSetMobileOfAccount(smsAlarmTransaction);
                //  else accountGetMobileOfAccount((SMSAlarmTransaction) this.getRequestObject());
//                smsAlarmTransaction=null;}
            else if (requestObject instanceof Transaction)
                setActionCode(((Transaction) requestObject).getResultFromCM().getAction_code());
            else if (requestObject instanceof CardNoOfAccount)
                setActionCode(((CardNoOfAccount) requestObject).getResultFromChannel().getAction_code());
            else if (requestObject instanceof AccountNoOfCard)
                setActionCode(((AccountNoOfCard) requestObject).getResultFromChannel().getAction_code());
            else if (requestObject instanceof RegisterAccount)
                setActionCode(((RegisterAccount) requestObject).getResultFromChannel().getAction_code());
            else if (requestObject instanceof FundTransfer)
                setActionCode(((FundTransfer) requestObject).getResultFromChannel().getAction_code());
            else if (requestObject instanceof FollowUpTransaction)
                setActionCode(((FollowUpTransaction) requestObject).getResultFromChannel().getAction_code());
            else if (requestObject instanceof InternalFollowUp)
                setActionCode(((InternalFollowUp) requestObject).getActionCode());
            else if (requestObject instanceof BillPayByIDAccount)
                setActionCode(((BillPayByIDAccount) requestObject).getResultFromChannel().getAction_code());
            else if (requestObject instanceof BillPayByIDAccountValid)
                setActionCode(((BillPayByIDAccountValid) requestObject).getResultFromChannel().getAction_code());
            else if (requestObject instanceof ChequeStatus)
                setActionCode(((ChequeStatus) requestObject).getResultFromChannel().getAction_code());
            else if (requestObject instanceof BlockAccount)
                setActionCode(((BlockAccount) requestObject).getResultFromChannel().getAction_code());
            else if (requestObject instanceof FollowUpTransactionACH)
                setActionCode(((FollowUpTransactionACH) requestObject).getResultFromChannel().getAction_code());
            else if (requestObject instanceof ShebaID)
                setActionCode(((ShebaID) requestObject).getActionCode());
            else if (requestObject instanceof BillInfoByTelNumber)
                setActionCode(((BillInfoByTelNumber) requestObject).getActionCode());
            else if (requestObject instanceof BillPaySayByTelNumber)
                setActionCode(((BillPaySayByTelNumber) requestObject).getActionCode());
            else if (requestObject instanceof BalanceForCard)
                setActionCode(((BalanceForCard) requestObject).getActionCode());
            else if (requestObject instanceof BillPayByBillIDPan)
                setActionCode(((BillPayByBillIDPan) requestObject).getActionCode());
            else if (requestObject instanceof BlockCard)
                setActionCode(((BlockCard) requestObject).getActionCode());
            else if (requestObject instanceof BillPaySayByTelNumber)
                setActionCode(((BillPaySayByTelNumber) requestObject).getActionCode());
            else setActionCode("4444");


        } catch (Exception e ){
//            e.printStackTrace();
        }

    }



    private void  accountGetMobileOfAccount() throws SenderException, InvalidParameterException, ResponseParsingException, SQLException {
        if (!(this.getRequestString().length==4))
            this.setResultString( "$6508*Description :error on parameters count:parameters count most be 4*$");
        else {
            SMSAlarmTransaction smsAlarmTransaction=new SMSAlarmTransaction();
            smsAlarmTransaction.setAccountNumber(this.getRequestString()[3]);
            smsAlarmTransaction=gatewayServices.accountGetMobileOfAccount(smsAlarmTransaction);
            this.setResultString((smsAlarmTransaction.getMobileNumber()));
        }
    }
    private void  accountGetMobileOfAccount(SMSAlarmTransaction smsAlarmTransaction) throws SenderException, InvalidParameterException, ResponseParsingException, SQLException {
        this.setResultObject(gatewayServices.accountGetMobileOfAccount(smsAlarmTransaction));
    }
    private void  accountInactiveMobileOfAccount(SMSAlarmTransaction smsAlarmTransaction) throws SenderException, InvalidParameterException, ResponseParsingException, SQLException {
        this.setResultObject(gatewayServices.accountGetMobileOfAccount(smsAlarmTransaction));
    }

    private void  accountSetMobileOfAccount() throws InvalidParameterException, ResponseParsingException, SQLException {
        if (!(this.getRequestString().length==5))
            this.setResultString( "$6508*Description :error on parameters count:parameters count most be 5*$");
        else {
            SMSAlarmTransaction smsAlarmTransaction=new SMSAlarmTransaction();
            smsAlarmTransaction.setAccountNumber(this.getRequestString()[3]);
            smsAlarmTransaction.setMobileNumber(this.getRequestString()[4]);
            smsAlarmTransaction=gatewayServices.accountSetMobileOfAccount(smsAlarmTransaction);
            if (smsAlarmTransaction.getMobileChanged()) this.setResultString("0000");
            else this.setResultString("6511");
        }
    }
    private void  accountSetMobileOfAccount(SMSAlarmTransaction smsAlarmTransaction) throws SenderException, InvalidParameterException, ResponseParsingException, SQLException {
        this.setResultObject(gatewayServices.accountSetMobileOfAccount(smsAlarmTransaction));

    }

    private void  accountPin1authenticate() throws SenderException, InvalidParameterException, ResponseParsingException, SQLException, InterruptedException, ClassNotFoundException {
        if (!(this.getRequestString().length==5))
            this.setResultString( "$6508*Description :error on parameters count:parameters count most 5 *$");
        else {
            AuthenticatePin1 authenticatePin1=new AuthenticatePin1();
            authenticatePin1.setAccountNumber(this.getRequestString()[3]);
            authenticatePin1.setPin(this.getRequestString()[4]);
            authenticatePin1.setDoChangePin(false);
            authenticatePin1.setMsgSeq(getMsgSequence());
            authenticatePin1=gatewayServices.accountPin1authenticate(authenticatePin1);
            this.setResultString("$" + authenticatePin1.getActionCode()+"*"+"$");
        }
    }
    private void  accountPin1authenticate(AuthenticatePin1 authenticatePin1) throws SenderException, InvalidParameterException, ResponseParsingException, SQLException, InterruptedException, ClassNotFoundException {
        if (authenticatePin1.getAccountNumber().equals("0353056352")||authenticatePin1.getAccountNumber().equals("353056352")){
            System.out.println("test");
        }
        authenticatePin1.setIPOfClient(clientIP);
        if (authenticatePin1.getDoChangePin()) this.setResultObject(gatewayServices.accountPin1Change(authenticatePin1));
        else this.setResultObject(gatewayServices.accountPin1authenticate(authenticatePin1));
    }
    private void  accountPin1ChangePin() throws SenderException, InvalidParameterException, ResponseParsingException, SQLException, ClassNotFoundException {
        if (!(this.getRequestString().length==6))
            this.setResultString( "$6508*Description :error on parameters count:parameters count most 6 *$");

        else {
            AuthenticatePin1 authenticatePin1=new AuthenticatePin1();
            authenticatePin1.setAccountNumber(this.getRequestString()[3]);
            authenticatePin1.setPin(this.getRequestString()[4]);
            authenticatePin1.setPin_New(this.getRequestString()[5]);
            authenticatePin1.setDoChangePin(true);
            authenticatePin1.setMsgSeq(getMsgSequence());
            authenticatePin1=gatewayServices.accountPin1Change(authenticatePin1);
            this.setResultString("$" + authenticatePin1.getActionCode()+"*"+"$");
        }
    }

    private void  accountPin2authenticate() throws SenderException, InvalidParameterException, ResponseParsingException, SQLException, InterruptedException, ClassNotFoundException {
        if (!(this.getRequestString().length==5))
            this.setResultString( "$6508*Description :error on parameters count:parameters count most 5 *$");
        else {
            AuthenticatePin2 authenticatePin2=new AuthenticatePin2();
            authenticatePin2.setAccountNumber(this.getRequestString()[3]);
            authenticatePin2.setPin(this.getRequestString()[4]);
            authenticatePin2.setDoChangePin(false);
            authenticatePin2.setMsgSeq(getMsgSequence());
            authenticatePin2=gatewayServices.accountPin2authenticate(authenticatePin2);
            this.setResultString("$" + authenticatePin2.getActionCode()+"*"+"$");
        }
    }
    private void  accountPin2authenticate(AuthenticatePin2 authenticatePin2) throws SenderException, InvalidParameterException, ResponseParsingException, SQLException, InterruptedException, ClassNotFoundException {
        System.out.println("in pin 2");
        if (authenticatePin2.getAccountNumber().equals("0353056352")||authenticatePin2.getAccountNumber().equals("353056352")){
            System.out.println("test");
        }

        if (authenticatePin2.getDoChangePin()){
            this.setResultObject(gatewayServices.accountPin2Change(authenticatePin2));
        }
        else {
            this.setResultObject(gatewayServices.accountPin2authenticate(authenticatePin2));
        }
    }
    private void  accountPin2ChangePin() throws SenderException, InvalidParameterException, ResponseParsingException, SQLException, ClassNotFoundException {
        if (!(this.getRequestString().length==6))
            this.setResultString( "$6508*Description :error on parameters count:parameters count most 6 *$");

        else {
            AuthenticatePin2 authenticatePin2=new AuthenticatePin2();
            authenticatePin2.setAccountNumber(this.getRequestString()[3]);
            authenticatePin2.setPin(this.getRequestString()[4]);
            authenticatePin2.setPin_New(this.getRequestString()[5]);
            authenticatePin2.setDoChangePin(true);
            authenticatePin2.setMsgSeq(getMsgSequence());
            authenticatePin2=gatewayServices.accountPin2Change(authenticatePin2);
            this.setResultString("$" + authenticatePin2.getActionCode()+"*"+"$");
        }
    }

    private void  accountBalance() throws SenderException, InvalidParameterException, ResponseParsingException, SQLException {
        if ((this.getRequestString().length<4) || (this.getRequestString().length>5))
            this.setResultString( "$6508*Description :error on parameters count:parameters count most be 4 or 5*$");
        else {
            BalanceForAccount balanceForAccount=new BalanceForAccount();
            balanceForAccount.setAccountNumber(this.getRequestString()[3]);
            balanceForAccount=gatewayServices.accountBalance(balanceForAccount);
            if (balanceForAccount.getResultFromChannel().getAction_code() =="")
                balanceForAccount.getResultFromChannel().setAction_code("6500");
            if (this.getRequestString().length==5)
                accountBalanceGetSpecified(balanceForAccount, Integer.valueOf(this.getRequestString()[5]));
            else
                accountBalanceGetCompleteResponse(balanceForAccount);
        }
    }
    private void  accountBalanceGetSpecified(BalanceForAccount balanceForAccount,int index){
        switch (index) {
            case 0:
                this.setResultString(balanceForAccount.getResultFromChannel().getAction_code());
                break;
            case 1:
                this.setResultString(balanceForAccount.getResultFromChannel().getMsgSequence());
                break;
            case 2:
                this.setResultString(balanceForAccount.getResultFromChannel().getChannelType());
                break;
            case 3:
                this.setResultString(balanceForAccount.getResultFromChannel().getChannelId());
                break;
            case 4:
                this.setResultString(balanceForAccount.getResultFromChannel().getAccountNumber());
                break;
            case 5:
                this.setResultString(balanceForAccount.getResultFromChannel().getAccountGroup());
                break;
            case 6:
                this.setResultString(balanceForAccount.getResultFromChannel().getAvailableBalance());
                break;
            case 7:
                this.setResultString(balanceForAccount.getResultFromChannel().getActualBalance());
                break;
            case 8:
                this.setResultString(balanceForAccount.getResultFromChannel().getCreditDebit());
                break;
            case 9:
                this.setResultString(balanceForAccount.getResultFromChannel().getCurrency());
                break;
            case 10:
                this.setResultString(balanceForAccount.getResultFromChannel().getLastTransactionDate());
                break;
            case 11:
                this.setResultString(balanceForAccount.getResultFromChannel().getHostId());
                break;
            case 12:
                this.setResultString(balanceForAccount.getResultFromChannel().getBranchId());
                break;
            case 13:
                this.setResultString(balanceForAccount.getResultFromChannel().getRespDateTime());
                break;
            case 14:
                this.setResultString(balanceForAccount.getResultFromChannel().getMAC());
                break;
            default:this.setResultString("$6010*: index number of requested field not correct*$");

        }
    }
    private void  accountBalanceGetCompleteResponse(BalanceForAccount balanceForAccount){
        this.setResultString(
                "$"+
                        balanceForAccount.getResultFromChannel().getAction_code()+"*"+
                        balanceForAccount.getResultFromChannel().getMsgSequence()+"*"+
                        balanceForAccount.getResultFromChannel().getChannelType()+"*"+
                        balanceForAccount.getResultFromChannel().getChannelId()+"*"+
                        balanceForAccount.getResultFromChannel().getAccountNumber()+"*"+
                        balanceForAccount.getResultFromChannel().getAccountGroup()+"*"+
                        balanceForAccount.getResultFromChannel().getAvailableBalance()+"*"+
                        balanceForAccount.getResultFromChannel().getActualBalance()+"*"+
                        balanceForAccount.getResultFromChannel().getCreditDebit()+"*"+
                        balanceForAccount.getResultFromChannel().getCurrency()+"*"+
                        balanceForAccount.getResultFromChannel().getLastTransactionDate()+"*"+
                        balanceForAccount.getResultFromChannel().getHostId()+"*"+
                        balanceForAccount.getResultFromChannel().getBranchId()+"*"+
                        balanceForAccount.getResultFromChannel().getRespDateTime()+"*"+
                        balanceForAccount.getResultFromChannel().getMAC()+"*"+
                        "$");
    }
    private void  accountBalance(BalanceForAccount balanceForAccount) throws SenderException, InvalidParameterException, ResponseParsingException, SQLException {
        this.setResultObject(gatewayServices.accountBalance(balanceForAccount));
    }

    private void  accountInformation() throws SenderException, InvalidParameterException, ResponseParsingException, SQLException, ClassNotFoundException {
        if ((this.getRequestString().length<4) || (this.getRequestString().length>5))
            this.setResultString( "$6508*Description :error on parameters count:parameters count most be 4 or 5*$");
        else {
            AccountInformation accountInformation=new AccountInformation();
            accountInformation.setAccountNumber(this.getRequestString()[3]);
            accountInformation=gatewayServices.accountInformation(accountInformation);
            if (accountInformation.getResultFromChannel().getAction_code() =="")
                accountInformation.getResultFromChannel().setAction_code("6500");
            if (this.getRequestString().length==5)
                accountInformationGetSpecified(accountInformation, Integer.valueOf(this.getRequestString()[5]));
            else
                accountInformationGetCompleteResponse(accountInformation);
        }
    }
    private void  accountInformationGetSpecified(AccountInformation  accountInformation,int index){
        switch (index) {
            case 0:
                this.setResultString(accountInformation.getResultFromChannel().getAction_code());
                break;
            case 1:
                this.setResultString(accountInformation.getResultFromChannel().getMsgSequence());
                break;
            case 2:
                this.setResultString(accountInformation.getResultFromChannel().getChannelType());
                break;
            case 3:
                this.setResultString(accountInformation.getResultFromChannel().getChannelId());
                break;
            case 4:
                this.setResultString(accountInformation.getResultFromChannel().getAccountNumber());
                break;
            case 5:
                this.setResultString(accountInformation.getResultFromChannel().getAccountType());
                break;
            case 6:
                this.setResultString(accountInformation.getResultFromChannel().getAccountcurrency());
                break;
            case 7:
                this.setResultString(accountInformation.getResultFromChannel().getAccountStatus());
                break;
            case 8:
                this.setResultString(accountInformation.getResultFromChannel().getAccountHost());
                break;
            case 9:
                this.setResultString(accountInformation.getResultFromChannel().getAccountBranchID());
                break;
            case 10:
                this.setResultString(accountInformation.getResultFromChannel().getPrsnType());
                break;
            case 11:
                this.setResultString(accountInformation.getResultFromChannel().getLatinName());
                break;
            case 12:
                this.setResultString(accountInformation.getResultFromChannel().getLatinFamily());
                break;
            case 13:
                this.setResultString(accountInformation.getResultFromChannel().getFarsiName());
                break;
            case 14:
                this.setResultString(accountInformation.getResultFromChannel().getFarsiFamily());
                break;
            case 15:
                this.setResultString(accountInformation.getResultFromChannel().getFatherName());
                break;
            case 16:
                this.setResultString(accountInformation.getResultFromChannel().getNationalCode());
                break;
            case 17:
                this.setResultString(accountInformation.getResultFromChannel().getBirthDate());
                break;
            case 18:
                this.setResultString(accountInformation.getResultFromChannel().getBirthPlace());
                break;
            case 19:
                this.setResultString(accountInformation.getResultFromChannel().getAddress1());
                break;
            case 20:
                this.setResultString(accountInformation.getResultFromChannel().getAddress2());
                break;
            case 21:
                this.setResultString(accountInformation.getResultFromChannel().getHomePhone());
                break;
            case 22:
                this.setResultString(accountInformation.getResultFromChannel().getOfficePhone());
                break;
            case 23:
                this.setResultString(accountInformation.getResultFromChannel().getCelPhone());
                break;
            case 24:
                this.setResultString(accountInformation.getResultFromChannel().getCreationDateTime());
                break;
            case 25:
                this.setResultString(accountInformation.getResultFromChannel().getRespDateTime());
                break;
            case 26:
                this.setResultString(accountInformation.getResultFromChannel().getMAC());
                break;
            default:this.setResultString("$6010*: index number of requested field not correct*$");
        }
    }
    private void  accountInformationGetCompleteResponse(AccountInformation accountInformation){
        this.setResultString(
                "$"+
                        accountInformation.getResultFromChannel().getAction_code()+"*"+
                        accountInformation.getResultFromChannel().getMsgSequence()+"*"+
                        accountInformation.getResultFromChannel().getChannelType()+"*"+
                        accountInformation.getResultFromChannel().getChannelId()+"*"+
                        accountInformation.getResultFromChannel().getAccountNumber()+"*"+
                        accountInformation.getResultFromChannel().getAccountType()+"*"+
                        accountInformation.getResultFromChannel().getAccountcurrency()+"*"+
                        accountInformation.getResultFromChannel().getAccountStatus()+"*"+
                        accountInformation.getResultFromChannel().getAccountHost()+"*"+
                        accountInformation.getResultFromChannel().getAccountBranchID()+"*"+
                        accountInformation.getResultFromChannel().getPrsnType()+"*"+
                        accountInformation.getResultFromChannel().getLatinName()+"*"+
                        accountInformation.getResultFromChannel().getLatinFamily()+"*"+
                        accountInformation.getResultFromChannel().getFarsiName()+"*"+
                        accountInformation.getResultFromChannel().getFarsiFamily()+"*"+
                        accountInformation.getResultFromChannel().getFatherName()+"*"+
                        accountInformation.getResultFromChannel().getNationalCode()+"*"+
                        accountInformation.getResultFromChannel().getBirthDate()+"*"+
                        accountInformation.getResultFromChannel().getBirthPlace()+"*"+
                        accountInformation.getResultFromChannel().getAddress1()+"*"+
                        accountInformation.getResultFromChannel().getAddress2()+"*"+
                        accountInformation.getResultFromChannel().getHomePhone()+"*"+
                        accountInformation.getResultFromChannel().getOfficePhone()+"*"+
                        accountInformation.getResultFromChannel().getCelPhone()+"*"+
                        accountInformation.getResultFromChannel().getCreationDateTime()+"*"+
                        accountInformation.getResultFromChannel().getReqDateTime()+"*"+
                        accountInformation.getResultFromChannel().getMAC()+"*"+
                        "$");
    }
    private void  accountInformation(AccountInformation accountInformation) throws SenderException, InvalidParameterException, ResponseParsingException, SQLException, ClassNotFoundException {
        this.setResultObject(gatewayServices.accountInformation(accountInformation));
    }

    private void  accountTransactions() throws SenderException, InvalidParameterException, ResponseParsingException, SQLException {
        if (!(this.getRequestString().length==7))
            this.setResultString( "$6508*Description :error on parameters count:parameters count most be 9*$");
        else {
            Transaction transaction=new Transaction();
            transaction.setAccountNumber(this.getRequestString()[3]);
            transaction.setStartDate(this.getRequestString()[4]);
            transaction.setEndDate(this.getRequestString()[5]);
            transaction.setTransactionCount(this.getRequestString()[6]);
            transaction=gatewayServices.accountTransaction(transaction);
            if (transaction.getResultFromCM().getAction_code() =="")
                     transaction.getResultFromCM().setAction_code("6500");
            accountTransactionsCompleteResponse(transaction);
        }
    }
    private void  accountTransactionsCompleteResponse(Transaction transaction){
        String transactions="";
        int transCount=0;
        try{
             transCount=Integer.valueOf(transaction.getResultFromCM().getTransCount());
        }catch (NumberFormatException var1){
             transactions="#!transaction Count not valid numerical value#";
        }finally {
            StatementMessage trans=transaction.getResultFromCM().getStatementMessage(transCount);
            for (int i=0;i<transCount;i++){
                 transactions=transactions+
                             "#"+
                             trans.getTransOprationCode()+"@"+
                             trans.getAmount()+"@"+
                             trans.getCreditDebit()+"@"+
                             trans.getTransDate()+"@"+
                             trans.getTransTime()+"@"+
                             trans.getTransEffectiveDate()+"@"+
                             trans.getTransDocNo()+"@"+
                             trans.getTransDesc()+"@"+
                             trans.getBranchCode()+"@"+
                             trans.getTransNo()+"@"+
                             trans.getLastAmount()+"@"+
                             trans.getPayId1()+"@"+
                             "#";
            }
        }
        String headerResult=
                "$"+
                transaction.getResultFromCM().getAction_code()+"*"+
                transaction.getResultFromCM().getMsgSequence()+"*"+
                transaction.getResultFromCM().getChannelType()+"*"+
                transaction.getResultFromCM().getChannelId()+"*"+
                transaction.getResultFromCM().getAccountNumber()+"*"+
                transaction.getResultFromCM().getFromDate()+"*"+
                transaction.getResultFromCM().getToDate()+"*"+
                transaction.getResultFromCM().getTransCount()+"*"+
                transaction.getResultFromCM().getBalance()+"*"+
                transaction.getResultFromCM().getHostId()+"*"+
                transaction.getResultFromCM().getRespDateTime()+"*"+
                transaction.getResultFromCM().getMAC()+"*"+
                transactions+"*"+
                "$";
        this.setResultString(headerResult);
    }
    private void  accountTransactions(Transaction transaction) throws SenderException, InvalidParameterException, ResponseParsingException, SQLException {

        this.setResultObject(gatewayServices.accountTransaction(transaction));
    }

    private void  accountGetPan() throws SenderException, InvalidParameterException, ResponseParsingException, SQLException {
        if (!(this.getRequestString().length==4))
            this.setResultString( "$6508*Description :error on parameters count:parameters count most be 4 *$");
        else {
            CardNoOfAccount cardNoOfAccount=new CardNoOfAccount();
            cardNoOfAccount.setAccountNumber(this.getRequestString()[3]);
            cardNoOfAccount=gatewayServices.accountGetPan(cardNoOfAccount);
            if (cardNoOfAccount.getResultFromChannel().getAction_code() =="")
                cardNoOfAccount.getResultFromChannel().setAction_code("6500");
            accountGetPanCompleteResponse(cardNoOfAccount);
        }
    }
    private void  accountGetPanCompleteResponse(CardNoOfAccount cardNoOfAccount){
        this.setResultString(
                "$"+
                cardNoOfAccount.getResultFromChannel().getAction_code()+"*"+
                cardNoOfAccount.getResultFromChannel().getMsgSequence()+"*"+
                cardNoOfAccount.getResultFromChannel().getChannelType()+"*"+
                cardNoOfAccount.getResultFromChannel().getChannelId()+"*"+
                cardNoOfAccount.getResultFromChannel().getPan()+"*"+
                cardNoOfAccount.getResultFromChannel().getRespDateTime()+"*"+
                cardNoOfAccount.getResultFromChannel().getMAC()+"*"+
                "$");
    }
    private void  accountGetPan(CardNoOfAccount cardNoOfAccount) throws SenderException, InvalidParameterException, ResponseParsingException, SQLException {
        this.setResultObject(gatewayServices.accountGetPan(cardNoOfAccount));
    }

    private void  accountGetAccountOfPan() throws SenderException, InvalidParameterException, ResponseParsingException, SQLException {
        if (!(this.getRequestString().length==4))
            this.setResultString( "$6508*Description :error on parameters count:parameters count most be 4 *$");
        else {
            AccountNoOfCard accountNoOfCard=new AccountNoOfCard();
            accountNoOfCard.setCardNo(this.getRequestString()[3]);
            accountNoOfCard=gatewayServices.accountGetAccountOfPan(accountNoOfCard);
            if (accountNoOfCard.getResultFromChannel().getAction_code() =="")
                accountNoOfCard.getResultFromChannel().setAction_code("6500");
            accountGetAccountOfPanCompleteResponse(accountNoOfCard);
        }
    }
    private void  accountGetAccountOfPanCompleteResponse(AccountNoOfCard accountNoOfCard){
        this.setResultString(
                "$"+
                accountNoOfCard.getResultFromChannel().getAction_code()+"*"+
                accountNoOfCard.getResultFromChannel().getMsgSequence()+"*"+
                accountNoOfCard.getResultFromChannel().getChannelType()+"*"+
                accountNoOfCard.getResultFromChannel().getChannelId()+"*"+
                accountNoOfCard.getResultFromChannel().getCfsAccNo()+"*"+
                accountNoOfCard.getResultFromChannel().getFarAccNo()+"*"+
                accountNoOfCard.getResultFromChannel().getRespDateTime()+"*"+
                accountNoOfCard.getResultFromChannel().getMAC()+"*"+
                "$");
    }
    private void  accountGetAccountOfPan(AccountNoOfCard accountNoOfCard) throws SenderException, InvalidParameterException, ResponseParsingException, SQLException {
        this.setResultObject(gatewayServices.accountGetAccountOfPan(accountNoOfCard));
    }

    private void  accountRegister() throws SenderException, InvalidParameterException, ResponseParsingException, SQLException {
        if (!(this.getRequestString().length==5))
            this.setResultString( "$6508*Description :error on parameters count:parameters count most be 5 *$");
        else {
            RegisterAccount registerAccount=new RegisterAccount();
            registerAccount.setAccountNumber(this.getRequestString()[3]);
            registerAccount.setServicesType(this.getRequestString()[4]);
            registerAccount=gatewayServices.accountRegister(registerAccount);
            if (registerAccount.getResultFromChannel().getAction_code() =="")
                registerAccount.getResultFromChannel().setAction_code("6500");
            accountRegisterCompleteResponse(registerAccount);
        }
    }
    private void  accountRegisterCompleteResponse(RegisterAccount registerAccount){
        this.setResultString(
                "$"+
                        registerAccount.getResultFromChannel().getAction_code()+"*"+
                        registerAccount.getResultFromChannel().getMsgSequence()+"*"+
                        registerAccount.getResultFromChannel().getChannelType()+"*"+
                        registerAccount.getResultFromChannel().getChannelId()+"*"+
                        registerAccount.getResultFromChannel().getAccountNumber()+"*"+
                        registerAccount.getResultFromChannel().getRespDateTime()+"*"+
                        registerAccount.getResultFromChannel().getMAC()+"*"+
                "$");
}
    private void  accountRegister(RegisterAccount registerAccount) throws SenderException, InvalidParameterException, ResponseParsingException, SQLException {
        this.setResultObject(gatewayServices.accountRegister(registerAccount));
    }


    private void  accountChequeStatus() throws SenderException, InvalidParameterException, ResponseParsingException, SQLException {
        if (!(this.getRequestString().length==7))
            this.setResultString( "$6508*Description :error on parameters count:parameters count most be 7 *$");
        else {
            ChequeStatus chequeStatus=new ChequeStatus();
            chequeStatus.setAccountNumber(this.getRequestString()[3]);
            chequeStatus.setChequeNO(this.getRequestString()[4]);
            chequeStatus.setChequeAmount(this.getRequestString()[5]);
            chequeStatus.setChequeOperateDate(this.getRequestString()[6]);
            chequeStatus=gatewayServices.accountChequeStatus(chequeStatus);
            if (chequeStatus.getResultFromChannel().getAction_code() =="")
                chequeStatus.getResultFromChannel().setAction_code("6500");
            accountChequeStatusCompleteResponse(chequeStatus);
        }
    }
    private void  accountChequeStatusCompleteResponse(ChequeStatus chequeStatus){
        this.setResultString(
                "$"+
                        chequeStatus.getResultFromChannel().getAction_code()+"*"+
                        chequeStatus.getResultFromChannel().getMsgSequence()+"*"+
                        chequeStatus.getResultFromChannel().getChannelType()+"*"+
                        chequeStatus.getResultFromChannel().getChannelId()+"*"+
                        chequeStatus.getResultFromChannel().getAccountNumber()+"*"+
                        chequeStatus.getResultFromChannel().getChequeNumber()+"*"+
                        chequeStatus.getResultFromChannel().getChequeAmount()+"*"+
                        chequeStatus.getResultFromChannel().getChequeOpDate()+"*"+
                        chequeStatus.getResultFromChannel().getChequeStatus()+"*"+
                        chequeStatus.getResultFromChannel().getChequeStatusDesc()+"*"+
                        chequeStatus.getResultFromChannel().getRespDateTime()+"*"+
                        chequeStatus.getResultFromChannel().getMAC()+"*"+
                        "$");
    }
    private void  accountChequeStatus(ChequeStatus chequeStatus) throws SenderException, InvalidParameterException, ResponseParsingException, SQLException {
        this.setResultObject(gatewayServices.accountChequeStatus(chequeStatus));
    }

    private void  accountBlock() throws SenderException, InvalidParameterException, ResponseParsingException, SQLException {
        if (!(this.getRequestString().length==5))
            this.setResultString( "$6508*Description :error on parameters count:parameters count most be 5 *$");
        else {
            BlockAccount blockAccount=new BlockAccount();
            blockAccount.setAccountNumber(this.getRequestString()[3]);
            blockAccount.setBlockMode(this.getRequestString()[4]);
            blockAccount=gatewayServices.accountBlock(blockAccount);
            if (blockAccount.getResultFromChannel().getAction_code() =="")
                blockAccount.getResultFromChannel().setAction_code("6500");
            accountBlockCompleteResponse(blockAccount);
        }
    }
    private void  accountBlockCompleteResponse(BlockAccount blockAccount){
        this.setResultString(
                "$"+
                    blockAccount.getResultFromChannel().getAction_code()+"*"+
                 "$");
    }
    private void  accountBlock(BlockAccount blockAccount) throws SenderException, InvalidParameterException, ResponseParsingException, SQLException {
        this.setResultObject(gatewayServices.accountBlock(blockAccount));
    }

    private void  accountACHFollowUp() throws SenderException, InvalidParameterException, ResponseParsingException, SQLException {
        if (!(this.getRequestString().length==4))
            this.setResultString( "$6508*Description :error on parameters count:parameters count most be 4 *$");
        else {
            FollowUpTransactionACH followUpTransactionACH=new FollowUpTransactionACH();
            followUpTransactionACH.setFollowUpCode(this.getRequestString()[3]);
            followUpTransactionACH=gatewayServices.accountACHFollowFundTransfer(followUpTransactionACH);
            if (followUpTransactionACH.getResultFromChannel().getAction_code() =="")
                followUpTransactionACH.getResultFromChannel().setAction_code("6500");
            accountACHFollowUpCompleteResponse(followUpTransactionACH);
        }
    }
    private void  accountACHFollowUpCompleteResponse(FollowUpTransactionACH followUpTransactionACH){
        this.setResultString(
                "$"+
                         followUpTransactionACH.getResultFromChannel().getAction_code()+"*"+
                         followUpTransactionACH.getResultFromChannel().getTransStatus()+"*"+
                "$");
    }
    private void  accountACHFollowUp(FollowUpTransactionACH FollowUpTransactionACH) throws SenderException, InvalidParameterException, ResponseParsingException, SQLException {
        this.setResultObject(gatewayServices.accountACHFollowFundTransfer(FollowUpTransactionACH));
    }

    private void  accountGetShebaID() throws SenderException, InvalidParameterException, ResponseParsingException, SQLException {
        if (!(this.getRequestString().length==4))
            this.setResultString( "$6508*Description :error on parameters count:parameters count most be 4 *$");
        else {
            ShebaID shebaID=new ShebaID();
            shebaID.setAccountNumber(this.getRequestString()[3]);
            shebaID=gatewayServices.accountGetShebaID(shebaID);
            accountGetShebaIDCompleteResponse(shebaID);
        }
    }
    private void  accountGetShebaIDCompleteResponse(ShebaID shebaID){
        this.setResultString(
                "$"+
                        shebaID.getActionCode()+"*"+
                        shebaID.getMsgSeq()+"*"+
                        shebaID.getAccountNumber()+"*"+
                        shebaID.getShebaID()+"*"+
                        "$");
    }
    private void  accountGetShebaID(ShebaID shebaID) throws SenderException, InvalidParameterException, ResponseParsingException, SQLException {
        this.setResultObject(gatewayServices.accountGetShebaID(shebaID));
    }

    private void  panGetBalance() throws SenderException, InvalidParameterException, ResponseParsingException, SQLException {
        if (!(this.getRequestString().length==4))
            this.setResultString( "$6508*Description :error on parameters count:parameters count most be 4 *$");
        else {
            ShebaID shebaID=new ShebaID();
            shebaID.setAccountNumber(this.getRequestString()[3]);
            shebaID=gatewayServices.accountGetShebaID(shebaID);
            accountGetShebaIDCompleteResponse(shebaID);
        }
    }
    private void  panGetBalanceCompleteResponse(BalanceForCard balanceForCard){
        this.setResultString(
                "$"+
                        balanceForCard.getActionCode()+"*"+
                        balanceForCard.getResultFromServer().getActualBalance()+"*"+
                "$");
    }
    private void  panGetBalance(BalanceForCard balanceForCard) throws SenderException, InvalidParameterException, ResponseParsingException, SQLException, UnsupportedEncodingException, InterruptedException {
        this.setResultObject(gatewayServices.cardBalance(balanceForCard));
    }

    private void  panBlock() throws SenderException, InvalidParameterException, ResponseParsingException, SQLException {
        //
    }
    private void  panBlockCompleteResponse(BalanceForCard balanceForCard){
        this.setResultString(
                "$"+
                        balanceForCard.getActionCode()+"*"+
                        balanceForCard.getResultFromServer().getActualBalance()+"*"+
                        "$");
    }
    private void  panBlock(BlockCard blockCard) throws SenderException, InvalidParameterException, ResponseParsingException, SQLException, UnsupportedEncodingException, InterruptedException {
        this.setResultObject(gatewayServices.cardBlockCard(blockCard));
    }


    private void  panGetAccountNoOfPan() throws SenderException, InvalidParameterException, ResponseParsingException, SQLException {
        if (!(this.getRequestString().length==4))
            this.setResultString( "$6508*Description :error on parameters count:parameters count most be 4 *$");
        else {
            ShebaID shebaID=new ShebaID();
            shebaID.setAccountNumber(this.getRequestString()[3]);
            shebaID=gatewayServices.accountGetShebaID(shebaID);
            accountGetShebaIDCompleteResponse(shebaID);
        }
    }
    private void  panGetAccountNoOfPanCompleteResponse(AccountNoOfCard accountNoOfCard){
        this.setResultString(
                "$"+
                        accountNoOfCard.getActionCode()+"*"+
                        accountNoOfCard.getResultFromChannel().getCfsAccNo()+"*"+
                "$");
    }
    private void  panGetAccountNoOfPan(AccountNoOfCard accountNoOfCard) throws SenderException, InvalidParameterException, ResponseParsingException, SQLException {
        this.setResultObject(gatewayServices.accountGetAccountOfPan(accountNoOfCard));
    }

    private void  telSwitchGetBillData() throws SenderException, InvalidParameterException, ResponseParsingException, SQLException, UnsupportedEncodingException, InterruptedException {
        if (!(this.getRequestString().length==6))
            this.setResultString( "$6508*Description :error on parameters count:parameters count most be 6 *$");
        else {
            BillInfoByTelNumber billInfoByTelNumber=new BillInfoByTelNumber();
            billInfoByTelNumber.setPan(this.getRequestString()[3]);
            if (this.getRequestString()[4].equals("1")) billInfoByTelNumber.setIsMobile(true);
            else billInfoByTelNumber.setIsMobile(false);
            billInfoByTelNumber.setTelNo(this.getRequestString()[5]);
            billInfoByTelNumber=gatewayServices.telSwitchGetBillData(billInfoByTelNumber);
            telSwitchGetBillDataCompleteResponse(billInfoByTelNumber);
        }
    }
    private void  telSwitchGetBillDataCompleteResponse(BillInfoByTelNumber billInfoByTelNumber){
        this.setResultString(
                 "$"+
                        billInfoByTelNumber.getActionCode()+"*"+
                        billInfoByTelNumber.getBillID()+"*"+
                        billInfoByTelNumber.getPaymentID()+"*"+
                        billInfoByTelNumber.getAmount()+"*"+
                 "$");
    }
    private void  telSwitchGetBillData(BillInfoByTelNumber billInfoByTelNumber) throws SenderException, InvalidParameterException, ResponseParsingException, SQLException, UnsupportedEncodingException, InterruptedException {
        this.setResultObject(gatewayServices.telSwitchGetBillData(billInfoByTelNumber));
    }

    private void  telSwitchSayBillPayment() throws SenderException, InvalidParameterException, ResponseParsingException, SQLException, UnsupportedEncodingException, InterruptedException {
        if (!(this.getRequestString().length==11))
            this.setResultString( "$6508*Description :error on parameters count:parameters count most be 11 *$");
        else {
            BillPaySayByTelNumber billPaySayByTelNumber=new BillPaySayByTelNumber();
            billPaySayByTelNumber.setPan(this.getRequestString()[3]);
            billPaySayByTelNumber.setTraceCode(this.getRequestString()[4]);
            billPaySayByTelNumber.setReferenceCode(this.getRequestString()[5]);
            if (this.getRequestString()[6].equals("1")) billPaySayByTelNumber.setIsMobile(true);
            else billPaySayByTelNumber.setIsMobile(false);
            billPaySayByTelNumber.setBillID(this.getRequestString()[7]);
            billPaySayByTelNumber.setPaymentID(this.getRequestString()[8]);
            billPaySayByTelNumber.setPayDate(this.getRequestString()[9]);
            billPaySayByTelNumber.setTelNo(this.getRequestString()[10]);
            billPaySayByTelNumber=gatewayServices.telSwitchSetBillPaymentData(billPaySayByTelNumber);
            telSwitchSayBillPaymentCompleteResponse(billPaySayByTelNumber);
        }
    }
    private void  telSwitchSayBillPaymentCompleteResponse(BillPaySayByTelNumber billPaySayByTelNumber){
        this.setResultString(
                        "$"+
                        billPaySayByTelNumber.getActionCode()+"*"+
                        "$");
    }
    private void  telSwitchSayBillPayment(BillPaySayByTelNumber billPaySayByTelNumber) throws SenderException, InvalidParameterException, ResponseParsingException, SQLException, UnsupportedEncodingException, InterruptedException {
        this.setResultObject(gatewayServices.telSwitchSetBillPaymentData(billPaySayByTelNumber));
    }

    private void  settingForClient(ClientJarSettings clientJarSettings) throws SenderException, InvalidParameterException, ResponseParsingException, SQLException, UnsupportedEncodingException, InterruptedException {
        this.setResultObject(gatewayServices.getSettingForClientJarFile(clientJarSettings));
    }


    //Payments
    private void  doPaymentByStringRequest() throws SQLException, ResponseParsingException, InvalidParameterException, SenderException, ClassNotFoundException {
      //  payment=new Payment(getRequestString());
       // setResultString(payment.getResultString());
    }
    private void  doPaymentByObject(Object object) throws SQLException, InterruptedException, UnsupportedEncodingException, InvalidParameterException, SenderException, ResponseParsingException, ClassNotFoundException {
    /*  if (PropertiesUtils.getUseOldPaymentSystem()){
            paymentPreMethodS=new PaymentPreMethodS(object);
            setResultObject(paymentPreMethodS.getResultObject());
      }
      else{*/
          payment=new Payment(object);
          setResultObject(payment.getResultObject());
//      }
       /* String callID="";
        if (object instanceof FundTransfer){
        }else if (object instanceof BillPayByIDAccount){
        }else if (object instanceof BillPayByBillIDPan){
        }else if (object instanceof FollowUpTransaction){
        }*/
    }

    private void  accountFundTransfer() throws SenderException, InvalidParameterException, ResponseParsingException, SQLException, ClassNotFoundException {
        doPaymentByStringRequest();
    }
    private void  accountFundTransfer(FundTransfer fundTransfer) throws SenderException, InvalidParameterException, ResponseParsingException, SQLException, UnsupportedEncodingException, InterruptedException, ClassNotFoundException {
        doPaymentByObject(fundTransfer);
    }

    private void  accountBillPaymentByBillID() throws SenderException, InvalidParameterException, ResponseParsingException, SQLException, ClassNotFoundException {
       doPaymentByStringRequest();
    }
    private void  accountBillPaymentByBillID(BillPayByIDAccount billPayByIDAccount) throws SenderException, InvalidParameterException, ResponseParsingException, SQLException, UnsupportedEncodingException, InterruptedException, ClassNotFoundException {
            doPaymentByObject(billPayByIDAccount);
    }

    private void  panBillPay() throws SenderException, InvalidParameterException, ResponseParsingException, SQLException, ClassNotFoundException {
       doPaymentByStringRequest();
    }
    private void  panBillPay(BillPayByBillIDPan billPayByBillIDPan) throws SenderException, InvalidParameterException, ResponseParsingException, SQLException, UnsupportedEncodingException, InterruptedException, ClassNotFoundException {
        doPaymentByObject(billPayByBillIDPan);
    }

    private void  internalFollowUpFundTransfer() throws SenderException, InvalidParameterException, ResponseParsingException, SQLException, ClassNotFoundException {
        doPaymentByStringRequest();
    }
    private void  internalFollowUpFundTransfer(InternalFollowUp followUpTransaction) throws SenderException, InvalidParameterException, ResponseParsingException, SQLException, InterruptedException, UnsupportedEncodingException, ClassNotFoundException {
        doPaymentByObject(followUpTransaction);
    }


    private void  accountFollowUpFundTransfer() throws SenderException, InvalidParameterException, ResponseParsingException, SQLException {
        if (!(this.getRequestString().length==7))
            this.setResultString( "$6508*Description :error on parameters count:parameters count most be 7 *$");
        else {
            FollowUpTransaction followUpTransaction=new FollowUpTransaction();
            followUpTransaction.setSourceAccount(this.getRequestString()[3]);
            followUpTransaction.setDestinationAccount(this.getRequestString()[4]);
            followUpTransaction.setFollowUpCode(this.getRequestString()[5]);
            followUpTransaction.setTransactionDateTime(this.getRequestString()[6]);
            followUpTransaction=gatewayServices.accountFollowUpTransaction(followUpTransaction);
            if (followUpTransaction.getResultFromChannel().getAction_code() =="")
                followUpTransaction.getResultFromChannel().setAction_code("6500");
            accountFollowUpFundTransferCompleteResponse(followUpTransaction);
        }
    }
    private void  accountFollowUpFundTransferCompleteResponse(FollowUpTransaction followUpTransaction){
        this.setResultString(
                "$"+
                        followUpTransaction.getResultFromChannel().getAction_code()+"*"+
                        followUpTransaction.getResultFromChannel().getMsgSequence()+"*"+
                        followUpTransaction.getResultFromChannel().getChannelType()+"*"+
                        followUpTransaction.getResultFromChannel().getChannelId()+"*"+
                        followUpTransaction.getResultFromChannel().getSrcAccountNumber()+"*"+
                        followUpTransaction.getResultFromChannel().getDstAccountNumber()+"*"+
                        followUpTransaction.getResultFromChannel().getFollowUpCode()+"*"+
                        followUpTransaction.getResultFromChannel().getTransAmount()+"*"+
                        followUpTransaction.getResultFromChannel().getTransDate()+"*"+
                        followUpTransaction.getResultFromChannel().getTransTime()+"*"+
                        followUpTransaction.getResultFromChannel().getTransStatus()+"*"+
                        followUpTransaction.getResultFromChannel().getRespDateTime()+"*"+
                        followUpTransaction.getResultFromChannel().getMAC()+"*"+
                        "$");
    }
    private void  accountFollowUpFundTransfer(FollowUpTransaction followUpTransaction) throws SenderException, InvalidParameterException, ResponseParsingException, SQLException, InterruptedException, UnsupportedEncodingException, ClassNotFoundException {
        doPaymentByObject(followUpTransaction);
//        this.setResultObject(gatewayServices.accountFollowUpTransaction(followUpTransaction));
    }

    private void  internalFollowUpFundTransfer_() throws SenderException, InvalidParameterException, ResponseParsingException, SQLException {
        if (!(this.getRequestString().length==7))
            this.setResultString( "$6508*Description :error on parameters count:parameters count most be 7 *$");
        else {
            InternalFollowUp followUpTransaction=new InternalFollowUp();
            followUpTransaction.setSourceAccount(this.getRequestString()[3]);
            followUpTransaction.setDestinationAccount(this.getRequestString()[4]);
            followUpTransaction.setFollowUpCode(this.getRequestString()[5]);
            followUpTransaction.setTransactionDateTime(this.getRequestString()[6]);
            followUpTransaction=gatewayServices.FollowUpTransactionInternal(followUpTransaction);
            internalFollowUpFundTransferCompleteResponse_(followUpTransaction);
        }
    }
    private void  internalFollowUpFundTransferCompleteResponse_(InternalFollowUp followUpTransaction){
        this.setResultString(
                "$"+
                        followUpTransaction.getstatus()+
                        "$");
    }
    private void  internalFollowUpFundTransfer_(InternalFollowUp followUpTransaction) throws SenderException, InvalidParameterException, ResponseParsingException, SQLException {
        this.setResultObject(gatewayServices.FollowUpTransactionInternal(followUpTransaction));
    }

    private void  accountBillPaymentValidation() throws SenderException, InvalidParameterException, ResponseParsingException, SQLException {
        if (!(this.getRequestString().length==7))
            this.setResultString( "$6508*Description :error on parameters count:parameters count most be 7 *$");
        else {
            BillPayByIDAccountValid billPayByIDAccountValid =new BillPayByIDAccountValid();
            billPayByIDAccountValid.setSourceAccount(this.getRequestString()[3]);
            billPayByIDAccountValid.setAmount(this.getRequestString()[4]);
            billPayByIDAccountValid.setBillID(this.getRequestString()[5]);
            billPayByIDAccountValid.setPaymentID(this.getRequestString()[6]);
            billPayByIDAccountValid =gatewayServices.accountBillPayByIDValidation(billPayByIDAccountValid);
            if (billPayByIDAccountValid.getResultFromChannel().getAction_code() =="")
                billPayByIDAccountValid.getResultFromChannel().setAction_code("6500");
            accountBillPaymentValidationCompleteResponse(billPayByIDAccountValid);
        }
    }
    private void  accountBillPaymentValidationCompleteResponse(BillPayByIDAccountValid billPayByIDAccountValid){
        this.setResultString(
                "$"+
                        billPayByIDAccountValid.getResultFromChannel().getAction_code()+"*"+
                        billPayByIDAccountValid.getResultFromChannel().getMsgSequence()+"*"+
                        billPayByIDAccountValid.getResultFromChannel().getChannelType()+"*"+
                        billPayByIDAccountValid.getResultFromChannel().getChannelId()+"*"+
                        billPayByIDAccountValid.getResultFromChannel().getSrcAccount()+"*"+
                        billPayByIDAccountValid.getResultFromChannel().getAmount()+"*"+
                        billPayByIDAccountValid.getResultFromChannel().getBillID()+"*"+
                        billPayByIDAccountValid.getResultFromChannel().getPaymentID()+"*"+
                        billPayByIDAccountValid.getResultFromChannel().getServiceCode()+"*"+
                        billPayByIDAccountValid.getResultFromChannel().getCompany()+"*"+
                        billPayByIDAccountValid.getResultFromChannel().getRespDateTime()+"*"+
                        billPayByIDAccountValid.getResultFromChannel().getMAC()+"*"+
                        "$");
    }
    private void  accountBillPaymentValidation(BillPayByIDAccountValid billPayByIDAccountValid) throws SenderException, InvalidParameterException, ResponseParsingException, SQLException {
        this.setResultObject(gatewayServices.accountBillPayByIDValidation(billPayByIDAccountValid));
    }

    private void  systemstatus(Status_All statusAll) throws InterruptedException {
        this.setResultObject(gatewayServices.systemStatus(statusAll));
    }


}
