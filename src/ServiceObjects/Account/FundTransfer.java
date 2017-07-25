package ServiceObjects.Account;

import Mainchannel.messages.FundTransferMessage;

/**
 * Created by Administrator on 6/14/2015.
 */
public class FundTransfer extends BaseAccountRequest {


    private String SourceAccount=null;
    public  void   setSourceAccount(String sourceAccount){
        SourceAccount=sourceAccount;
    }
    public  String getSourceAccount(){
        return SourceAccount;
    }

    private String DestinationAccount=null;
    public  void   setDestinationAccount(String destinationAccount){
        DestinationAccount=destinationAccount;
    }
    public  String getDestinationAccount(){
        return DestinationAccount;
    }

    private String TransactionAmount=null;
    public  void   setTransactionAmount(String transactionAmount){
        TransactionAmount=transactionAmount;
    }
    public  String getTransactionAmount(){
        return TransactionAmount;
    }

    private String definedAccount1=null;
    public  void   setDefinedAccount1(String DefinedAccount1){
        definedAccount1=DefinedAccount1;
    }
    public  String getDefinedAccount1(){
        return definedAccount1;

    }
    private String definedAccount2=null;
    public  void   setDefinedAccount2(String DefinedAccount2){
        definedAccount2=DefinedAccount2;
    }
    public  String getDefinedAccount2(){
        return definedAccount2;
    }

    private String definedAccount3=null;
    public  void   setDefinedAccount3(String DefinedAccount3){
        definedAccount3=DefinedAccount3;
    }
    public  String getDefinedAccount3(){
        return definedAccount3;
    }

    private String definedAccount4=null;
    public  void   setDefinedAccount4(String DefinedAccount4){
        definedAccount4=DefinedAccount4;
    }
    public  String getDefinedAccount4(){
        return definedAccount4;
    }

    private String definedAccount5=null;
    public  void   setDefinedAccount5(String DefinedAccount5){
        definedAccount5=DefinedAccount5;
    }
    public  String getDefinedAccount5(){
        return definedAccount5;
    }

    private String NationalCode=null;
    public  void   setNationalCode(String NationalCode){
        NationalCode=NationalCode;
    }
    public  String getNationalCode(){
        return NationalCode;
    }

    private String ServiceTypes=null;
    public  void   setServiceTypes(String ServiceTypes){
        ServiceTypes=ServiceTypes;
    }
    public  String getServiceTypes(){
        return ServiceTypes;
    }

    private String MaxTransferAmount=null;
    public  void   setMaxTransferAmount(String maxTransferAmount){
        MaxTransferAmount=MaxTransferAmount;
    }
    public  String getMaxTransferAmount(){
        return MaxTransferAmount;
    }


    private String Currency=null;
    public  void   setCurrency(String currency){
        Currency=currency;
    }
    public  String getCurrency(){
        return Currency;
    }

    private String OperationCode=null;
    public  void   setOperationCode(String operationCode){
        OperationCode=operationCode;
    }
    public  String getOperationCode(){
        return OperationCode;
    }

    private String PayCode1=null;
    public  void   setPayCode1(String payCode1){
        PayCode1=payCode1;
    }
    public  String getPayCode1(){
        return PayCode1;
    }

    private String PayCode2=null;
    public  void   setPayCode2(String payCode2){
        PayCode2=payCode2;
    }
    public  String getPayCode2(){
        return PayCode2;
    }

    private String ExtraInfo=null;
    public  void   setExtraInfo(String extraInfo){
        ExtraInfo=extraInfo;
    }
    public  String getExtraInfo(){
        return ExtraInfo;
    }

    private boolean isRegistered=false;
    public  void    setIsRegistered(boolean isregistered){
        isRegistered=isregistered;
    }
    public  boolean getIsRegistered(){
        return isRegistered;
    }

    private boolean IsFreeTransfer=false;
    public  void    setIsFreeTransfer(boolean isFreeTransfer){
        IsFreeTransfer=isFreeTransfer;
    }
    public  boolean getIsFreeTransfer(){
        return IsFreeTransfer;
    }


    private boolean DataFromDbChashed=false;
    public  void    setDataFromDbChashed(boolean dataFromDbChashed){
        DataFromDbChashed=dataFromDbChashed;
    }
    public  boolean getDataFromDbChashed(){
        return DataFromDbChashed;
    }

    private boolean IsFundTransfer=false;
    public  void    setIsFundTransfer(boolean isFundTransfer){
        IsFundTransfer=isFundTransfer;
    }
    public  boolean getIsFundTransfer(){
        return IsFundTransfer;
    }

    private boolean IsInstallmentPay=false;
    public  void    setIsInstallmentPay(boolean isInstallmentPay){
        IsInstallmentPay=isInstallmentPay;
    }
    public  boolean getIsInstallmentPay(){
        return IsInstallmentPay;
    }

    private boolean IsIdentFundTranfer=false;
    public  void    setIsIdentFundTranfer(boolean isIdentFundTranfer){
        IsIdentFundTranfer=isIdentFundTranfer;
    }
    public  boolean getIsIdentFundTranfer(){
        return IsIdentFundTranfer;
    }

    private boolean AccountGroupAllowed=false;
    public  void    setAccountGroupAllowed(boolean accountGroupAllowed){
        AccountGroupAllowed=accountGroupAllowed;
    }
    public  boolean getAccountGroupAllowed(){
        return AccountGroupAllowed;
    }

    private boolean IsValidAccount=false;
    public  void    setIsValidAccount(boolean isValidAccount){
        IsValidAccount=isValidAccount;
    }
    public  boolean getIsValidAccount(){
        return IsValidAccount;
    }

    private String TransactionDescription=null;
    public  void   setTransactionDescription(String transactionDescription){
        TransactionDescription=transactionDescription;
    }
    public  String getTransactionDescription(){
        return TransactionDescription;
    }

    private String TraceID="";
    public  void   setTraceID(String traceID){
        TraceID=traceID;
    }
    public  String getTraceID(){
        return TraceID;
    }

    private String TransferID="";
    public  void   setTransferID(String transferID){
        TransferID=transferID;
    }
    public  String getTransferID(){
        return TransferID;
    }

    private FundTransferMessage ResultFromChannel=null;
    public  void   setResultFromChannel(FundTransferMessage resultFromChannel){
        ResultFromChannel=resultFromChannel;
    }
    public  FundTransferMessage getResultFromChannel(){
        return ResultFromChannel;
    }



}
