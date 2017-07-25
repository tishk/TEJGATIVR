package ResponseServices;

import Mainchannel.Exceptions.InvalidParameterException;
import Mainchannel.Exceptions.ResponseParsingException;
import Mainchannel.sender.SenderException;
import ServiceObjects.Account.BillPayByIDAccount;
import ServiceObjects.Account.FundTransfer;
import ServiceObjects.Account.ShebaID;
import ServiceObjects.Other.DBUtils;
import ServiceObjects.Other.DBUtilsSQLServer;
import ServiceObjects.Other.LoggerToDB;
import ServiceObjects.Pan.BillPayByBillIDPan;
import utils.PropertiesUtils;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;

/**
 * Created by Administrator on 1/16/2016.
 */
public class PaymentNewMethod {

    private GatewayServices gatewayServices=new GatewayServices();

    public PaymentNewMethod(Object ObjectForPayment) throws InvalidParameterException, SQLException, ResponseParsingException, SenderException, UnsupportedEncodingException, InterruptedException, ClassNotFoundException {

        if (ObjectForPayment instanceof FundTransfer){
            accountFundTransfer((FundTransfer)ObjectForPayment);
        }else if (ObjectForPayment instanceof BillPayByIDAccount){
            accountBillPayment((BillPayByIDAccount)ObjectForPayment);
        }else if (ObjectForPayment instanceof BillPayByBillIDPan){
            panBillPayment((BillPayByBillIDPan)ObjectForPayment);
        }
    }
    public PaymentNewMethod(String params[]) throws InvalidParameterException, SQLException, ResponseParsingException, SenderException, ClassNotFoundException {
        setRequestString(params);
        switch(Integer.valueOf(params[2])){
            case     8:accountFundTransfer();
                break;
            case     10:accountBillPayment();
                break;
            case    202:panBillPayment();
                break;
            //test
        }
    }


    private Object RequestObject=null;
    public  void   setRequestObject(Object requestObject){
        RequestObject=requestObject;
    }
    public  Object getRequestObject(){
        return RequestObject;
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

    private Object ResultObject="";
    public  void   setResultObject(Object resultObject){
        ResultObject=resultObject;
    }
    public  Object getResultObject(){
        return ResultObject;
    }


    private void  accountFundTransfer() throws SenderException, InvalidParameterException, ResponseParsingException, SQLException, ClassNotFoundException {
        if (!(this.getRequestString().length==6))
            this.setResultString( "$6508*Description :error on parameters count:parameters count most be 6 *$");
        else {

            FundTransfer fundTransfer=new FundTransfer();
            fundTransfer.setSourceAccount(this.getRequestString()[3]);
            fundTransfer.setDestinationAccount(this.getRequestString()[4]);
            fundTransfer.setTransactionAmount(this.getRequestString()[5]);
            fundTransfer=accountFundTransferGetRegisterData(fundTransfer);
            if (fundTransfer.getActionCode().equals("8888")){
                fundTransfer=gatewayServices.accountFundTransfer(fundTransfer);
                if (fundTransfer.getResultFromChannel().getAction_code() =="") fundTransfer.setActionCode("6500");
                else fundTransfer.setActionCode(fundTransfer.getResultFromChannel().getAction_code());
            }
            LoggerToDB loggerToDB =new LoggerToDB(fundTransfer);
            accountFundTransferCompleteResponse(fundTransfer);
        }
    }
    private void  accountFundTransferCompleteResponse(FundTransfer fundTransfer){
        this.setResultString(
                "$"+
                        fundTransfer.getActionCode()+"*"+
                        fundTransfer.getResultFromChannel().getMsgSequence()+"*"+
                        fundTransfer.getResultFromChannel().getChannelType()+"*"+
                        fundTransfer.getResultFromChannel().getChannelId()+"*"+
                        fundTransfer.getResultFromChannel().getSrcAccountNumber()+"*"+
                        fundTransfer.getResultFromChannel().getDstAccountNumber()+"*"+
                        fundTransfer.getResultFromChannel().getTransAmount()+"*"+
                        fundTransfer.getResultFromChannel().getOpCode()+"*"+
                        fundTransfer.getResultFromChannel().getTransDesc()+"*"+
                        fundTransfer.getResultFromChannel().getRefNo()+"*"+
                        fundTransfer.getResultFromChannel().getRespDateTime()+"*"+
                        fundTransfer.getResultFromChannel().getMAC()+"*"+
                        "$");
    }
    private void  accountFundTransfer(FundTransfer fundTransfer) throws SenderException, InvalidParameterException, ResponseParsingException, SQLException, ClassNotFoundException {
        fundTransfer=accountFundTransferGetRegisterData(fundTransfer);
        if (fundTransfer.getActionCode().equals("8888")){
            fundTransfer=gatewayServices.accountFundTransfer(fundTransfer);
            fundTransfer.setActionCode(fundTransfer.getResultFromChannel().getAction_code());
        }
        LoggerToDB loggerToDB =new LoggerToDB(fundTransfer);
        this.setResultObject(fundTransfer);
    }

    private boolean destinationAccountIsValid(FundTransfer fundTransfer){

        if (fundTransfer.getDestinationAccount().equals(fundTransfer.getDefinedAccount1())) return true;
        if (fundTransfer.getDestinationAccount().equals(fundTransfer.getDefinedAccount2())) return true;
        if (fundTransfer.getDestinationAccount().equals(fundTransfer.getDefinedAccount3())) return true;
        if (fundTransfer.getDestinationAccount().equals(fundTransfer.getDefinedAccount4())) return true;
        if (fundTransfer.getDestinationAccount().equals(fundTransfer.getDefinedAccount5())) return true;
        return false;
    }
    private FundTransfer  accountFundTransferGetRegisterData(FundTransfer fundTransfer) throws SenderException, InvalidParameterException, ResponseParsingException, SQLException, ClassNotFoundException {

        if (PropertiesUtils.getUseOldPaymentSystem()){
            if (fundTransfer.getIsFundTransfer()) fundTransfer= cashRegisterDataForFundTransferFromSQLServer(fundTransfer);
            else if (fundTransfer.getIsInstallmentPay()) fundTransfer= cashRegisterDataForInstallmentPayFromSQLServer(fundTransfer);
            else if (fundTransfer.getIsIdentFundTranfer()) fundTransfer= cashRegisterDataForIdentFundTransferFromSQLServer(fundTransfer);

            if (fundTransfer.getActionCode().equals("6666")||
                    fundTransfer.getActionCode().equals("6667")||
                    // fundTransfer.getActionCode().equals("9300")||
                    fundTransfer.getActionCode().equals("9001"))
            {
                if (fundTransfer.getIsFundTransfer()) fundTransfer= cashRegisterDataForFundTransfer(fundTransfer);
                else if (fundTransfer.getIsInstallmentPay()) fundTransfer= cashRegisterDataForInstallmentPay(fundTransfer);
                else if (fundTransfer.getIsIdentFundTranfer()) fundTransfer= cashRegisterDataForIdentFundTransfer(fundTransfer);
            }
        }else{
            if (fundTransfer.getIsFundTransfer()) fundTransfer= cashRegisterDataForFundTransfer(fundTransfer);
            else if (fundTransfer.getIsInstallmentPay()) fundTransfer= cashRegisterDataForInstallmentPay(fundTransfer);
            else if (fundTransfer.getIsIdentFundTranfer()) fundTransfer= cashRegisterDataForIdentFundTransfer(fundTransfer);
        }

        return fundTransfer;
    }

    private FundTransfer  cashRegisterDataForFundTransfer(FundTransfer fundTransfer) throws SenderException, InvalidParameterException, ResponseParsingException, SQLException {
        DBUtils dbUtils =new DBUtils();
        fundTransfer= dbUtils.accountFundTransferCashData(fundTransfer);
        if (dbUtils.connected){
            if (fundTransfer.getDataFromDbChashed()){
                if (fundTransfer.getIsRegistered()){
                    if (String.valueOf(fundTransfer.getServiceTypes().charAt(2)).equals("1")) {
                        if (fundTransfer.getIsFreeTransfer()){
                            if (destinationAccountIsValid(fundTransfer)){
                                fundTransfer.setActionCode("8888");
                            }else fundTransfer.setActionCode("9700");
                        }else fundTransfer.setActionCode("9700");
                    }else fundTransfer.setActionCode("9800");
                }else fundTransfer.setActionCode("9300");
            }else fundTransfer.setActionCode("6667");
        }else fundTransfer.setActionCode("6666");
        return fundTransfer;
    }
    private FundTransfer  cashRegisterDataForFundTransferFromSQLServer(FundTransfer fundTransfer) throws SenderException, InvalidParameterException, ResponseParsingException, SQLException, ClassNotFoundException {
        DBUtilsSQLServer dbUtilsSQLServer =new DBUtilsSQLServer("MasterDatabase");
        fundTransfer= dbUtilsSQLServer.accountFundTransferCashData(fundTransfer);
        if (dbUtilsSQLServer.connected){
            if (fundTransfer.getDataFromDbChashed()){
                if (fundTransfer.getIsRegistered()){
                    if (String.valueOf(fundTransfer.getServiceTypes().charAt(2)).equals("1")) {
                        if (fundTransfer.getIsFreeTransfer()){
                            if (destinationAccountIsValid(fundTransfer)){
                                fundTransfer.setActionCode("8888");
                            }else fundTransfer.setActionCode("9700");
                        }else fundTransfer.setActionCode("9700");
                    }else fundTransfer.setActionCode("9800");
                }else fundTransfer.setActionCode("9300");
            }else fundTransfer.setActionCode("6667");
        }else fundTransfer.setActionCode("6666");
        return fundTransfer;
    }

    private FundTransfer  cashRegisterDataForInstallmentPay(FundTransfer fundTransfer) throws SenderException, InvalidParameterException, ResponseParsingException, SQLException {
        DBUtils dbUtils =new DBUtils();
        fundTransfer= dbUtils.accountFundTransferCashData(fundTransfer);
        if (dbUtils.connected){
            if (fundTransfer.getDataFromDbChashed()) {
                if (fundTransfer.getIsValidAccount()){
                    if (fundTransfer.getAccountGroupAllowed()){
                        if (fundTransfer.getIsRegistered()) {
                            if (String.valueOf(fundTransfer.getServiceTypes().charAt(2)).equals("1")) {
                                if (fundTransfer.getIsFreeTransfer()) {
                                    if (destinationAccountIsValid(fundTransfer)) {
                                        fundTransfer.setActionCode("8888");
                                    } else fundTransfer.setActionCode("9700");
                                } else fundTransfer.setActionCode("9700");
                            } else fundTransfer.setActionCode("9007");
                        } else fundTransfer.setActionCode("9300");
                    }else fundTransfer.setActionCode("9008");
                }else fundTransfer.setActionCode("9001");
            }else fundTransfer.setActionCode("6667");
        }else fundTransfer.setActionCode("6666");
        return fundTransfer;
    }
    private FundTransfer  cashRegisterDataForInstallmentPayFromSQLServer(FundTransfer fundTransfer) throws SenderException, InvalidParameterException, ResponseParsingException, SQLException, ClassNotFoundException {
        DBUtilsSQLServer dbUtilsSQLServer =new DBUtilsSQLServer("MasterDatabase");
        fundTransfer= dbUtilsSQLServer.accountFundTransferCashData(fundTransfer);
        if (dbUtilsSQLServer.connected){
            if (fundTransfer.getDataFromDbChashed()) {
                if (fundTransfer.getIsValidAccount()){
                    if (fundTransfer.getAccountGroupAllowed()){
                        if (fundTransfer.getIsRegistered()) {
                            if (String.valueOf(fundTransfer.getServiceTypes().charAt(2)).equals("1")) {
                                if (fundTransfer.getIsFreeTransfer()) {
                                    if (destinationAccountIsValid(fundTransfer)) {
                                        fundTransfer.setActionCode("8888");
                                    } else fundTransfer.setActionCode("9700");
                                } else fundTransfer.setActionCode("9700");
                            } else fundTransfer.setActionCode("9007");
                        } else fundTransfer.setActionCode("9300");
                    }else fundTransfer.setActionCode("9008");
                }else fundTransfer.setActionCode("9001");
            }else fundTransfer.setActionCode("6667");
        }else fundTransfer.setActionCode("6666");
        return fundTransfer;
    }

    private FundTransfer  cashRegisterDataForIdentFundTransfer(FundTransfer fundTransfer) throws SenderException, InvalidParameterException, ResponseParsingException, SQLException {
        DBUtils dbUtils =new DBUtils();
        fundTransfer= dbUtils.accountFundTransferCashData(fundTransfer);
        if (dbUtils.connected){
            if (fundTransfer.getDataFromDbChashed()){
                if (fundTransfer.getIsRegistered()){
                    if (String.valueOf(fundTransfer.getServiceTypes().charAt(2)).equals("1")) {
                        if (fundTransfer.getIsFreeTransfer()){
                            if (destinationAccountIsValid(fundTransfer)){
                                fundTransfer.setActionCode("8888");
                            }else fundTransfer.setActionCode("9700");
                        }else fundTransfer.setActionCode("9700");
                    }else fundTransfer.setActionCode("9800");
                }else fundTransfer.setActionCode("9300");
            }else fundTransfer.setActionCode("6667");
        }else fundTransfer.setActionCode("6666");
        return fundTransfer;
    }
    private FundTransfer  cashRegisterDataForIdentFundTransferFromSQLServer(FundTransfer fundTransfer) throws SenderException, InvalidParameterException, ResponseParsingException, SQLException, ClassNotFoundException {
        DBUtilsSQLServer dbUtilsSQLServer =new DBUtilsSQLServer("MasterDatabase");
        fundTransfer= dbUtilsSQLServer.accountFundTransferCashData(fundTransfer);
        if (dbUtilsSQLServer.connected){
            if (fundTransfer.getDataFromDbChashed()){
                if (fundTransfer.getIsRegistered()){
                    if (String.valueOf(fundTransfer.getServiceTypes().charAt(2)).equals("1")) {
                        if (fundTransfer.getIsFreeTransfer()){
                            if (destinationAccountIsValid(fundTransfer)){
                                fundTransfer.setActionCode("8888");
                            }else fundTransfer.setActionCode("9700");
                        }else fundTransfer.setActionCode("9700");
                    }else fundTransfer.setActionCode("9800");
                }else fundTransfer.setActionCode("9300");
            }else fundTransfer.setActionCode("6667");
        }else fundTransfer.setActionCode("6666");
        return fundTransfer;
    }


    private void  accountBillPayment() throws SenderException, InvalidParameterException, ResponseParsingException, SQLException {
        if (!(this.getRequestString().length==7))
            this.setResultString( "$6508*Description :error on parameters count:parameters count most be 7 *$");
        else {
            BillPayByIDAccount billPayByIDAccount=new BillPayByIDAccount();
            billPayByIDAccount.setSourceAccount(this.getRequestString()[3]);
            billPayByIDAccount.setAmount(this.getRequestString()[4]);
            billPayByIDAccount.setBillID(this.getRequestString()[5]);
            billPayByIDAccount.setPaymentID(this.getRequestString()[6]);
            billPayByIDAccount=accountBillPaymentGetData(billPayByIDAccount);
            if (billPayByIDAccount.getActionCode().equals("8888")){
                billPayByIDAccount=gatewayServices.accountBillPaymentByBillID(billPayByIDAccount);
                if (billPayByIDAccount.getResultFromChannel().getAction_code() =="")
                    billPayByIDAccount.getResultFromChannel().setAction_code("6500");
                else {
                    if (billPayByIDAccount.getResultFromChannel().getAction_code().equals("3333")){
                        billPayByIDAccount.getResultFromChannel().setAction_code("3000");
                    }
                    if (billPayByIDAccount.getResultFromChannel().getAction_code().equals("1931")){
                        billPayByIDAccount.getResultFromChannel().setAction_code("9014");
                    }
                    // if (billPayByIDAccount.getResultFromCM().getAction_code().equals("9126")){
                    //   billPayByIDAccount.getResultFromCM().setAction_code("2000");
                    // }
                    if (billPayByIDAccount.getResultFromChannel().getAction_code().equals("1912")){
                        changePriority(billPayByIDAccount.getSourceAccount());
                    }
                    billPayByIDAccount.setActionCode(billPayByIDAccount.getResultFromChannel().getAction_code());
                }
            }
            accountBillPaymentByBillIDCompleteResponse(billPayByIDAccount);

            LoggerToDB loggerToDB =new LoggerToDB(billPayByIDAccount);
            // if (!loggerToDB.getResultOfLog()) billPayByIDAccount.setActionCode("6666");
        }
    }
    private void  accountBillPaymentByBillIDCompleteResponse(BillPayByIDAccount billPayByIDAccount){
        this.setResultString(
                "$"+
                        billPayByIDAccount.getActionCode()+"*"+
                        billPayByIDAccount.getResultFromChannel().getMsgSequence()+"*"+
                        billPayByIDAccount.getResultFromChannel().getChannelType()+"*"+
                        billPayByIDAccount.getResultFromChannel().getChannelId()+"*"+
                        billPayByIDAccount.getResultFromChannel().getSrcAccount()+"*"+
                        billPayByIDAccount.getResultFromChannel().getAmount()+"*"+
                        billPayByIDAccount.getResultFromChannel().getBillID()+"*"+
                        billPayByIDAccount.getResultFromChannel().getPaymentID()+"*"+
                        billPayByIDAccount.getResultFromChannel().getServiceCode()+"*"+
                        billPayByIDAccount.getResultFromChannel().getCompany()+"*"+
                        billPayByIDAccount.getResultFromChannel().getRefNo()+"*"+
                        billPayByIDAccount.getResultFromChannel().getRespDateTime()+"*"+
                        billPayByIDAccount.getResultFromChannel().getMAC()+"*"+
                        "$");
    }
    private void  accountBillPayment(BillPayByIDAccount billPayByIDAccount) throws SenderException, InvalidParameterException, ResponseParsingException, SQLException {
        billPayByIDAccount=accountBillPaymentGetData(billPayByIDAccount);
        if (billPayByIDAccount.getActionCode().equals("8888")){
            billPayByIDAccount=gatewayServices.accountBillPaymentByBillID(billPayByIDAccount);
            if (billPayByIDAccount.getResultFromChannel().getAction_code() =="")
                billPayByIDAccount.getResultFromChannel().setAction_code("6500");
            else {
                if (billPayByIDAccount.getResultFromChannel().getAction_code().equals("3333")){
                    billPayByIDAccount.getResultFromChannel().setAction_code("3000");
                }
                if (billPayByIDAccount.getResultFromChannel().getAction_code().equals("1931")){
                    billPayByIDAccount.getResultFromChannel().setAction_code("9014");
                }
                // if (billPayByIDAccount.getResultFromCM().getAction_code().equals("9126")){
                //     billPayByIDAccount.getResultFromCM().setAction_code("2000");
                // }
                if (billPayByIDAccount.getResultFromChannel().getAction_code().equals("1912")){
                    changePriority(billPayByIDAccount.getSourceAccount());
                }
                billPayByIDAccount.setActionCode(billPayByIDAccount.getResultFromChannel().getAction_code());
            }

        }
        LoggerToDB loggerToDB =new LoggerToDB(billPayByIDAccount);
        this.setResultObject(billPayByIDAccount);
    }

    private void  changePriority(String acc) throws SQLException {
        DBUtils dbUtils =new DBUtils();
        if (dbUtils.connected){
            dbUtils.changePriority(acc);
        }
    }
    private BillPayByIDAccount  accountBillPaymentGetData(BillPayByIDAccount billPayByIDAccount) throws SenderException, InvalidParameterException, ResponseParsingException, SQLException {
        DBUtils dbUtils =new DBUtils();
        if (dbUtils.connected){
            billPayByIDAccount= dbUtils.accountBillPayByIDAccountCashData(billPayByIDAccount);
            if (billPayByIDAccount.getDataFromDbChashed()) {
                if (billPayByIDAccount.getIsRegistered()) {
                    if (String.valueOf(billPayByIDAccount.getServiceTypes().charAt(3)).equals("1")) {
                        if (!billPayByIDAccount.gethasAlreadyBeenPaid()) {
                            if (!billPayByIDAccount.getSubServiceNotDefined()) {
                                billPayByIDAccount.setActionCode("8888");
                            } else billPayByIDAccount.setActionCode("9016");
                        } else billPayByIDAccount.setActionCode("9014");
                    } else billPayByIDAccount.setActionCode("9009");
                } else billPayByIDAccount.setActionCode("9300");
            }else billPayByIDAccount.setActionCode("6666");
        }else billPayByIDAccount.setActionCode("6666");
        return billPayByIDAccount;
    }

    private void  panBillPayment() throws SenderException, InvalidParameterException, ResponseParsingException, SQLException {
        if (!(this.getRequestString().length==4))
            this.setResultString( "$6508*Description :error on parameters count:parameters count most be 4 *$");
        else {
            ShebaID shebaID=new ShebaID();
            shebaID.setAccountNumber(this.getRequestString()[3]);
            shebaID=gatewayServices.accountGetShebaID(shebaID);
            //accountGetShebaIDCompleteResponse(shebaID);
        }
    }
    private void  panBillPayCompleteResponse(BillPayByBillIDPan billPayByBillIDPan){
        this.setResultString(
                "$"+
                        billPayByBillIDPan.getActionCode()+"*"+
                        billPayByBillIDPan.getResultFromServer()+"*"+
                        "$");
    }
    private void  panBillPayment(BillPayByBillIDPan billPayByBillIDPan) throws SenderException, InvalidParameterException, ResponseParsingException, SQLException, UnsupportedEncodingException, InterruptedException {
        System.out.println("in bill paymenttt");
        this.setResultObject(gatewayServices.cardBillPayByBillIDPan(billPayByBillIDPan));
    }

}
