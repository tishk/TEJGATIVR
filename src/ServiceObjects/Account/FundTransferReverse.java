package ServiceObjects.Account;


import Mainchannel.messages.FundTransferMessageReverse;

/**
 * Created by Administrator on 8/23/2015.
 */
public class FundTransferReverse  extends BaseAccountRequest{
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

    private String TransactionDescription=null;
    public  void   setTransactionDescription(String transactionDescription){
        TransactionDescription=transactionDescription;
    }
    public  String getTransactionDescription(){
        return TransactionDescription;
    }

    private FundTransferMessageReverse ResultFromChannel=null;
    public  void   setResultFromChannel(FundTransferMessageReverse resultFromChannel){
        ResultFromChannel=resultFromChannel;
    }
    public  FundTransferMessageReverse getResultFromChannel(){
        return ResultFromChannel;
    }



}

