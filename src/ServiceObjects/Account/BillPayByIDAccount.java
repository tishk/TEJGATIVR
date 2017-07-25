package ServiceObjects.Account;

import Mainchannel.messages.BillPaymentMessage;

/**
 * Created by Administrator on 28/05/2015.
 */
public class BillPayByIDAccount extends BaseAccountRequest {


    private String SourceAccount=null;
    public  void   setSourceAccount(String sourceAccount){
        SourceAccount=sourceAccount;
    }
    public  String getSourceAccount(){
        return SourceAccount;
    }

    private String MACAddress="";
    public  void    setMACAddress(String macAddress){
        MACAddress=macAddress;
    }
    public  String getMACAddress(){
        return MACAddress;
    }

    private String Amount=null;
    public  void   setAmount(String amount){
        Amount=amount;
    }
    public  String getAmount(){
        return Amount;
    }

    private String BillID=null;
    public  void   setBillID(String billID){
        BillID=billID;
    }
    public  String getBillID(){
        return BillID;
    }

    private String PaymentID=null;
    public  void   setPaymentID(String paymentID){
        PaymentID=paymentID;
    }
    public  String getPaymentID(){
        return PaymentID;
    }

    private String TraceCode=null;
    public  void   setTraceCode(String traceCode){
        TraceCode=traceCode;
    }
    public  String getTraceCode(){
        return TraceCode;
    }

    private String ReferenceCode=null;
    public  void   setReferenceCode(String referenceCode){
        ReferenceCode=referenceCode;
    }
    public  String getReferenceCode(){
        return ReferenceCode;
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

    private boolean isRegistered=false;
    public  void    setIsRegistered(boolean isregistered){
        isRegistered=isregistered;
    }
    public  boolean getIsRegistered(){
        return isRegistered;
    }

    private boolean DataFromDbChashed=false;
    public  void    setDataFromDbChashed(boolean dataFromDbChashed){
        DataFromDbChashed=dataFromDbChashed;
    }
    public  boolean getDataFromDbChashed(){
        return DataFromDbChashed;
    }

    private boolean hasAlreadyBeenPaid=false;
    public  void    sethasAlreadyBeenPaid(boolean HasAlreadyBeenPaid){
        hasAlreadyBeenPaid=HasAlreadyBeenPaid;
    }
    public  boolean gethasAlreadyBeenPaid(){
        return hasAlreadyBeenPaid;
    }

    private boolean SubServiceNotDefined=false;
    public  void    setSubServiceNotDefined(boolean subServiceNotDefined){
        SubServiceNotDefined=subServiceNotDefined;
    }
    public  boolean getSubServiceNotDefined(){
        return SubServiceNotDefined;
    }





    private BillPaymentMessage ResultFromChannel=null;
    public  void   setResultFromChannel(BillPaymentMessage resultFromChannel){
        ResultFromChannel=resultFromChannel;
    }
    public  BillPaymentMessage getResultFromChannel(){
        return ResultFromChannel;
    }

}
