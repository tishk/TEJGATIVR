package ServiceObjects.Pan;

import ServiceObjects.ISO.ISO210;
import ServiceObjects.ISO.ISO430;

/**
 * Created by Administrator on 28/05/2015.
 */
public class BillPayByBillIDPan extends BaseCardRequest   {

    private String  BillID=null;
    public  void    setBillID(String billID){
        this.BillID=billID;
    }
    public  String  getBillID(){
        return this.BillID;
    }

    private String  PaymentID=null;
    public  void    setPaymentID(String paymentID){
        this.PaymentID=paymentID;
    }
    public  String  getPaymentID(){
        return this.PaymentID;
    }

    private String  Amount=null;
    public  void    setAmount(String amount){
        this.Amount=amount;
    }
    public  String  getAmount(){
        return this.Amount;
    }

    private String TraceCode=null;
    public  void   setTraceCode(String traceCode){
        TraceCode=traceCode;
    }
    public  String getTraceCode(){
        return TraceCode;
    }

    private String MsgSeq=null;
    public  void   setMsgSeq(String msgSeq){
        MsgSeq=msgSeq;
    }
    public  String getMsgSeq(){
        return MsgSeq;
    }



    private String ActionCode="";
    public  void    setActionCode(String actionCode){
        ActionCode=actionCode;
    }
    public  String getActionCode(){
        return ActionCode;
    }

    private ISO210 ResultFromServer=new ISO210();
    public  void setResultFromServer(ISO210 resultFromServer){
        this.ResultFromServer=resultFromServer;
    }
    public ISO210 getResultFromServer(){
        return this.ResultFromServer;
    }

    private ISO430 ResultOfReverse=new ISO430();
    public  void setResultOfReverse(ISO430 resultOfReverse){
        this.ResultOfReverse=resultOfReverse;
    }
    public ISO430 getResultOfReverse(){
        return this.ResultOfReverse;
    }
}
