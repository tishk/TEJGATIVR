package ServiceObjects.Other;

import ServiceObjects.ISO.ISO110;
import ServiceObjects.Pan.BaseCardRequest;

/**
 * Created by Administrator on 12/5/2015.
 */
public class BillPaySayByTelNumber extends BaseCardRequest {

    private String ActionCode="";
    public  void   setActionCode(String actionCode){
        ActionCode=actionCode;
    }
    public  String getActionCode(){
        return ActionCode;
    }

    private String TraceCode="";
    public  void   setTraceCode(String traceCode){
        TraceCode=traceCode;
    }
    public  String getTraceCode(){
        return TraceCode;
    }

    private String ReferenceCode="";
    public  void   setReferenceCode(String referenceCode){
        ReferenceCode=referenceCode;
    }
    public  String getReferenceCode(){
        return ReferenceCode;
    }

    private String PayDate="";
    public  void   setPayDate(String payDate){
        PayDate=payDate;
    }
    public  String getPayDate(){
        return PayDate;
    }

    private String TelKind="";
    public  void   setTelKind(String telKind){
        TelKind=telKind;
    }
    public  String getTelKind(){
        return TelKind;
    }


    private String TelNo="";
    public  void   setTelNo(String telNo){
        TelNo=telNo;
    }
    public  String getTelNo(){
        return TelNo;
    }

    private String BillID="";
    public  void   setBillID(String billID){
        BillID=billID;
    }
    public  String getBillID(){
        return BillID;
    }

    private String PaymentID="";
    public  void   setPaymentID(String paymentID){
        PaymentID=paymentID;
    }
    public  String getPaymentID(){
        return PaymentID;
    }

    private String Amount="";
    public  void   setAmount(String amount){
        Amount=amount;
    }
    public  String getAmount(){
        return Amount;
    }


    private boolean IsMobile=false;
    public  void   setIsMobile(boolean isMobile){
        IsMobile=isMobile;
    }
    public  boolean getIsMobile(){
        return IsMobile;
    }


    private ISO110 ResultFromServer=null;
    public  void   setResultFromServer(ISO110 resultFromServer){
        this.ResultFromServer=resultFromServer;
    }
    public  ISO110 getResultFromServer(){
        return this.ResultFromServer;
    }


}
