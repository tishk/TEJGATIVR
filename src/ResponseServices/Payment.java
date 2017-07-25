package ResponseServices;

import Mainchannel.Exceptions.InvalidParameterException;
import Mainchannel.Exceptions.ResponseParsingException;
import Mainchannel.sender.SenderException;
import ServiceObjects.Account.*;
import ServiceObjects.Other.DBUtils;
import ServiceObjects.Other.DBUtilsSQLServer;
import ServiceObjects.Other.LoggerToDB;
import ServiceObjects.Pan.BillPayByBillIDPan;
import ServiceObjects.Pan.PanPayment;
import com.ibm.disthub2.impl.util.StringUtil;
import utils.strUtils;

import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Administrator on 1/7/2016.
 */
public class Payment {
    private String desAccount1="";
    private String desAccount2="";
    private String desAccount3="";
    private String desAccount4="";
    private String desAccount5="";
    private String fldServiceType="";
    private String isFreeTransfer="";
    private boolean registered=false;
    private Long MAX_TRANSACTION_IN_DAY_FOR_DECLARATED_ACCOUNT=150000000L;
    private Long MAX_TRANSACTION_IN_DAY_FOR_UNDECLARATED_ACCOUNT=30000000L;
    private Long transactionOfToday=0L;

    private boolean USE_SQLSERVER_REGISTARTION_TABLE=true;
    private String OLD_REGISTRATION_DATABAS="MasterDataBase";
    private FundTransfer fundTransfer=null;
    private BillPayByIDAccount billPayByIDAccount=null;
    private BillPayByBillIDPan billPayByBillIDPan=null;
    private Long maxValue=0L;
    private short donFlag;
    private strUtils strUtils =new strUtils();
    private String accountGroup=null;
    public   String generateTraceCode() {

      return String.valueOf(System.nanoTime()).substring(0,12).substring(6,12);//144525438988

    }
    private GatewayServices gatewayServices=new GatewayServices();

    private Object ResultObject="";
    public  void   setResultObject(Object resultObject){
        ResultObject=resultObject;
    }
    public  Object getResultObject(){
        return ResultObject;
    }

    public Payment(Object ObjectForPayment) throws InvalidParameterException, SQLException, ResponseParsingException, SenderException, UnsupportedEncodingException, InterruptedException, ClassNotFoundException {

        if (ObjectForPayment instanceof FundTransfer){
            fundTransfer=(FundTransfer)ObjectForPayment;
            if (fundTransfer.getIsFundTransfer()){
                accountFundTransfer();
            }else if (fundTransfer.getIsIdentFundTranfer()){
                accountFundTransfer();
            }else if (fundTransfer.getIsInstallmentPay()){
                accountFundTransferInstallmentPayment();
            }else ; //nothing yet
        }else if (ObjectForPayment instanceof BillPayByIDAccount){
            billPayByIDAccount=(BillPayByIDAccount)ObjectForPayment;
            accountBillPayment();
        }else if (ObjectForPayment instanceof BillPayByBillIDPan){
            billPayByBillIDPan=(BillPayByBillIDPan)ObjectForPayment;
            panBillPayment();
        }else if (ObjectForPayment instanceof InternalFollowUp){
            getFollowupPayment((InternalFollowUp) ObjectForPayment);
        }
    }

    private void    changePriority(String acc) throws SQLException {
        DBUtilsSQLServer dbUtilsSQLServer =new DBUtilsSQLServer("MasterDataBase");
        if (dbUtilsSQLServer.connected){
            dbUtilsSQLServer.changePriority(acc);
        }
    }

    private boolean destinationAccountIsValid(FundTransfer fundTransfer){

        if (fundTransfer.getDestinationAccount().equals(fundTransfer.getDefinedAccount1())) return true;
        if (fundTransfer.getDestinationAccount().equals(fundTransfer.getDefinedAccount2())) return true;
        if (fundTransfer.getDestinationAccount().equals(fundTransfer.getDefinedAccount3())) return true;
        if (fundTransfer.getDestinationAccount().equals(fundTransfer.getDefinedAccount4())) return true;
        if (fundTransfer.getDestinationAccount().equals(fundTransfer.getDefinedAccount5())) return true;
        return false;
    }

    private void  accountFundTransfer_(FundTransfer fundTransfer) throws SenderException, InvalidParameterException, ResponseParsingException, SQLException, ClassNotFoundException {
        fundTransfer=accountFundTransferGetRegisterData(fundTransfer);
        if (fundTransfer.getActionCode().equals("8888")){//say ok
            fundTransfer=gatewayServices.accountFundTransfer(fundTransfer);
            fundTransfer.setActionCode(fundTransfer.getResultFromChannel().getAction_code());
        }
        LoggerToDB loggerToDB =new LoggerToDB(fundTransfer);
        this.setResultObject(fundTransfer);
    }
    private void  getFollowupPayment(InternalFollowUp internalFollowUp) throws SQLException {
       if (internalFollowUp.getSourceAccount().equals("3435531082")){
           System.out.println("test");
        }
       DBUtils dbUtils=new DBUtils();
        if (dbUtils.connected){
           doInternalFollowUp(internalFollowUp, dbUtils);

            System.out.println(internalFollowUp.getActionCode());
        }else internalFollowUp.setActionCode("9900");

        this.setResultObject(internalFollowUp);
    }

    private InternalFollowUp doInternalFollowUp(InternalFollowUp internalFollowUp, DBUtils dbUtils) throws SQLException {
        /*InternalFollowUp internalFollowUp=new InternalFollowUp();
        internalFollowUp.setFollowUpCode(followUpTransaction.getFollowUpCode());
        internalFollowUp.setIsInternalFollowCode(true);
        internalFollowUp.setSourceAccount(followUpTransaction.getSourceAccount());
        internalFollowUp.setCallUniqID(followUpTransaction.getCallUniqID());
        internalFollowUp.setIsPanPayment(false);*/
        internalFollowUp=dbUtils.internalFollowUpTransaction(internalFollowUp);
        /*internalFollowUp.setActionCode(internalFollowUp.getActionCode());
        internalFollowUp.setisValidFollowCode(internalFollowUp.getisValidFollowCode());
        internalFollowUp.setstatus(internalFollowUp.getstatus());*/
        return internalFollowUp;
    }

    private void  getFollowupTransactionPaymen(InternalFollowUp internalFollowUp) throws SQLException {
        DBUtils dbUtils=new DBUtils();
        if (dbUtils.connected){
            dbUtils.internalFollowUpTransaction(internalFollowUp);
            System.out.println(internalFollowUp.getActionCode());
        }
    }
    private void  accountBillPayment_() throws SenderException, InvalidParameterException, ResponseParsingException, SQLException {
//        billPayByIDAccount=accountBillPaymentGetData(billPayByIDAccount);
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
    private void  doPanBillPayment() throws SenderException, InvalidParameterException, ResponseParsingException, SQLException, UnsupportedEncodingException, InterruptedException {
        billPayByBillIDPan=gatewayServices.cardBillPayByBillIDPan(billPayByBillIDPan);
       /* if (billPayByBillIDPan.getBillID().equals("9413325304125") && (billPayByBillIDPan.getPaymentID().equals("21060192"))){
            billPayByBillIDPan.setActionCode("0000");
            billPayByBillIDPan.setTraceCode("123456");
        }*/
        this.setResultObject(billPayByIDAccount);
    }
    //test



    private FundTransfer  accountFundTransferGetRegisterData(FundTransfer fundTransfer) throws SenderException, InvalidParameterException, ResponseParsingException, SQLException, ClassNotFoundException {


            if (fundTransfer.getIsFundTransfer()) fundTransfer= cashRegisterDataForFundTransfer(fundTransfer);
            else if (fundTransfer.getIsInstallmentPay()) fundTransfer= cashRegisterDataForInstallmentPay(fundTransfer);
            else if (fundTransfer.getIsIdentFundTranfer()) fundTransfer= cashRegisterDataForIdentFundTransfer(fundTransfer);

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
            }else fundTransfer.setActionCode("9900");
        }else fundTransfer.setActionCode("9900");
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
                                        fundTransfer.setActionCode("8888");
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

    private BillPayByBillIDPan  panBillPaymentGetData(BillPayByBillIDPan billPayByIDAccount) throws SenderException, InvalidParameterException, ResponseParsingException, SQLException {
        DBUtils dbUtils =new DBUtils();
        if (dbUtils.connected) {
           /* billPayByIDAccount= dbUtils.accountBillPayByIDAccountCashData(billPayByIDAccount);
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
        }else billPayByIDAccount.setActionCode("6666");*/
        }
        return billPayByIDAccount;
    }




    private boolean getRegDataFromSQLISSuccess_fundTransfer(String accountNo){
        DBUtilsSQLServer dbUtilsSQLServer =new DBUtilsSQLServer(OLD_REGISTRATION_DATABAS);
        if (dbUtilsSQLServer.connected){
            String res[]=dbUtilsSQLServer.getRegistrationData(accountNo).split("@");
            if (res.length>2){
                registered=Boolean.valueOf(res[0]);
                fldServiceType=res[1];
                desAccount1=res[2];
                desAccount2=res[3];
                desAccount3=res[4];
                desAccount4=res[5];
                desAccount5=res[6];
                isFreeTransfer=res[7];;
                return true;
            }else{
                 return false;
            }
        }else {
            return false;
        }
    }
    private boolean getMaxPaymentOfTodayIsSuccess(String accountNo) throws SQLException {
        DBUtils dbUtils =new DBUtils();
        if (dbUtils.connected){
           transactionOfToday= dbUtils.getPaymentOfToday(accountNo);
            return true;
        }else {
            return false;
        }
    }
    private boolean isDeclaretedAccountDestination(String destAccountNo){
//        destAccountNo=strUtils.fixLengthWithZero(destAccountNo,10);
        destAccountNo=strUtils.fixLengthWithZero(destAccountNo,10);
        return (destAccountNo.equals(strUtils.fixLengthWithZero(desAccount1,10)))||
                (destAccountNo.equals(strUtils.fixLengthWithZero(desAccount2,10)))||
                (destAccountNo.equals(strUtils.fixLengthWithZero(desAccount3,10)))||
                (destAccountNo.equals(strUtils.fixLengthWithZero(desAccount4,10)))||
                (destAccountNo.equals(strUtils.fixLengthWithZero(desAccount5,10)));
    }
    private boolean isFreeTransferAccount(){
        return isFreeTransfer.equals("1");
    }
    private boolean maxPaymentNotFullForFundtransfer() {

        Long amount=Long.valueOf(fundTransfer.getTransactionAmount());
        if (isDeclaretedAccountDestination(fundTransfer.getDestinationAccount())){
            return (transactionOfToday+amount)<= MAX_TRANSACTION_IN_DAY_FOR_DECLARATED_ACCOUNT;
        }else{
            return (transactionOfToday+amount)<= MAX_TRANSACTION_IN_DAY_FOR_UNDECLARATED_ACCOUNT;
        }
    }
    private void    logFundTransfer() {

    }
    private void    logBillPayment() {

        LoggerToDB loggerToDB =new LoggerToDB(billPayByIDAccount);
        this.setResultObject(billPayByIDAccount);
    }
    private void    ifNeedChangePriority() throws SQLException {
        if  (fundTransfer.getResultFromChannel().getAction_code().equals("1912")){
            changePriority(fundTransfer.getSourceAccount());
        }
    }
    private void    setDonFlag() {

        int accIntCode=0;
        try{
            accIntCode=Integer.valueOf(fundTransfer.getResultFromChannel().getAction_code());
        }catch (Exception e){
            accIntCode=0;
        }
        switch (accIntCode){
            case 0:donFlag=1;
              break;
            case 1000:donFlag=2;
                break;
            case 2000:donFlag=3;
                break;
            default:donFlag=0;
        }
    }
    private void    setDonFlagForBillPayment() {

        int accIntCode=0;
        try{
            accIntCode=Integer.valueOf(billPayByIDAccount.getResultFromChannel().getAction_code());
        }catch (Exception e){
            accIntCode=0;
        }
        switch (accIntCode){
            case 0:donFlag=1;
              break;
            case 1000:donFlag=2;
                break;
            case 2000:donFlag=3;
                break;
            default:donFlag=0;
        }
    }
    private void    doFundTranser() throws SenderException, InvalidParameterException, ResponseParsingException, SQLException {


/*
        if (fundTransfer.getIsFundTransfer()) fundTransfer=gatewayServices.accountFundTransfer(fundTransfer);
        else fundTransfer=gatewayServices.accountFundTransferNoCost(fundTransfer);
*/
        fundTransfer=gatewayServices.accountFundTransfer(fundTransfer);

        if (fundTransfer.getResultFromChannel()!=null){
            fundTransfer.setActionCode(fundTransfer.getResultFromChannel().getAction_code());

        }else{
            fundTransfer.setActionCode("9126");
        }
        setDonFlag();
        fundTransfer.setTransactionDescription(String.valueOf(donFlag));
        ifNeedChangePriority();
    }
    private void    accountFundTransfer() throws SenderException, InvalidParameterException, ResponseParsingException, SQLException, ClassNotFoundException {
        if (fundTransfer.getSourceAccount().equals("3435531082")){
            System.out.println("test");
        }
        if (getRegDataFromSQLISSuccess_fundTransfer(fundTransfer.getSourceAccount())){
            if (getMaxPaymentOfTodayIsSuccess(fundTransfer.getSourceAccount())){
                if (isFreeTransferAccount()){
                    if  (maxPaymentNotFullForFundtransfer()){
                        doFundTranser();
                    }else {
                        fundTransfer.setActionCode("9500");
                    }
                }else {
                    fundTransfer.setActionCode("9800");
                }
            }else {
                fundTransfer.setActionCode("6600");
            }
        }else {
            fundTransfer.setActionCode("9900");
        }

        fundTransfer.setTransactionDescription(String.valueOf(donFlag));
        logToDB(fundTransfer);
        this.setResultObject(fundTransfer);
    }

    private void setMaxPaymentIsFull() {

    }

    private void logToDB(final Object object) {
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

    private void    accountFundTransferInstallmentPayment() throws SenderException, InvalidParameterException, ResponseParsingException, SQLException, ClassNotFoundException {

        if (getRegDataFromSQLISSuccess_fundTransfer(fundTransfer.getSourceAccount())){
            setAccountGroup();
//            if (isDestinationAccountAllowed()){
                if (getMaxPaymentOfTodayIsSuccess(fundTransfer.getSourceAccount())){
                    if (isFreeTransferAccount()){
                        if  (maxPaymentNotFullForFundtransfer()){
                            doFundTranser();
                        }else {
                            fundTransfer.setActionCode("9500");

                        }
                    }else {
                        fundTransfer.setActionCode("9800");

                    }
                }else{
                    fundTransfer.setActionCode("6600");

                }
//            }else fundTransfer.getResultFromChannel().setAction_code("9008");
        }else {
            fundTransfer.setActionCode("9900");

        }

        logToDB(fundTransfer);
        this.setResultObject(fundTransfer);
    }

    private Boolean isDestinationAccountAllowed() throws SQLException {
        DBUtils dbUtils =new DBUtils();
        if (dbUtils.connected){
            if (dbUtils.isAccountCodeOK(accountGroup)) return true;
            else return false;
        }else return false;
    }
    private void setAccountGroup() throws InvalidParameterException, SQLException, ResponseParsingException, SenderException, ClassNotFoundException {
        AccountInformation accountInformation=new AccountInformation();
        accountInformation.setAccountNumber(fundTransfer.getDestinationAccount());
        accountInformation.setCallUniqID(fundTransfer.getCallUniqID());
        accountInformation=gatewayServices.accountInformation(accountInformation);
        if (accountInformation.getResultFromChannel().getAction_code().equals("0000")){
            accountGroup=accountInformation.getResultFromChannel().getAccountType();
//            accountGroup="032";
        }else{

        }

    }
    private void doBillPaymentAccount() throws SenderException, InvalidParameterException, ResponseParsingException, SQLException {
        billPayByIDAccount=gatewayServices.accountBillPaymentByBillID(billPayByIDAccount);
        if (billPayByIDAccount.getResultFromChannel()!=null){
            billPayByIDAccount.setActionCode(billPayByIDAccount.getResultFromChannel().getAction_code());

        }else {
            billPayByIDAccount.setActionCode("9126");
        }
    }
    private void accountBillPayment() throws SenderException, InvalidParameterException, ResponseParsingException, SQLException {

        if (getRegDataFromSQLISSuccess_fundTransfer(billPayByIDAccount.getSourceAccount())){
            DBUtils dbUtils =new DBUtils();
            if (dbUtils.connected){
                billPayByIDAccount= dbUtils.accountBillPayByIDAccountCashData(billPayByIDAccount);
                if (billPayByIDAccount.getDataFromDbChashed()) {
                    if (billPayByIDAccount.getIsRegistered()) {
                        if (fldServiceType.charAt(2)=='1'){
                            if (!billPayByIDAccount.gethasAlreadyBeenPaid()) {
//                                if (!billPayByIDAccount.getSubServiceNotDefined()) {
                                    doBillPaymentAccount();
//                                } else billPayByIDAccount.setActionCode("9016");
                            } else billPayByIDAccount.setActionCode("9014");
                        } else billPayByIDAccount.setActionCode("9009");
                    } else billPayByIDAccount.setActionCode("9300");
                }else billPayByIDAccount.setActionCode("6666");
            }else billPayByIDAccount.setActionCode("6666");
        }else billPayByIDAccount.getResultFromChannel().setAction_code("9900");
//        logBillPayment();
        setDonFlagForBillPayment();
        billPayByIDAccount.setGatewayMessage(String.valueOf(donFlag));
        logToDB(billPayByIDAccount);
        this.setResultObject(billPayByIDAccount);
    }
    private void panBillPayment() throws SenderException, InvalidParameterException, ResponseParsingException, SQLException, UnsupportedEncodingException, InterruptedException {

            DBUtils dbUtils =new DBUtils();
            if (dbUtils.connected){
                PanPayment panPayment=new PanPayment();
                panPayment= dbUtils.panBillPayByIDPanCashData(billPayByBillIDPan,panPayment);
                if (panPayment.isDataFromDbChashed()) {
                    if (panPayment.isRegistered()) {
                            if (!panPayment.isHasAlreadyBeenPaid()) {
//                                if (!panPayment.isSubServiceNotDefined()) {
                                    doPanBillPayment();
//                                } else billPayByBillIDPan.setActionCode("9016");
                            } else billPayByBillIDPan.setActionCode("9014");
                    } else billPayByBillIDPan.setActionCode("9300");
                }else billPayByBillIDPan.setActionCode("6666");
            }else billPayByBillIDPan.setActionCode("6666");
        this.setResultObject(billPayByBillIDPan);
    logToDB(billPayByBillIDPan);
    }





}
