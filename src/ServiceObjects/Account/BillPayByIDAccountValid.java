package ServiceObjects.Account;

import Mainchannel.messages.BillPaymentValidation;

/**
 * Created by Administrator on 6/14/2015.
 */
public class BillPayByIDAccountValid extends BaseAccountRequest {

    private String SourceAccount=null;
    public  void   setSourceAccount(String sourceAccount){
        SourceAccount=sourceAccount;
    }
    public  String getSourceAccount(){
        return SourceAccount;
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

    private BillPaymentValidation ResultFromChannel=null;
    public  void   setResultFromChannel(BillPaymentValidation resultFromChannel){
        ResultFromChannel=resultFromChannel;
    }
    public  BillPaymentValidation getResultFromChannel(){
        return ResultFromChannel;
    }
}
