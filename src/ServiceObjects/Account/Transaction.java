package ServiceObjects.Account;

import Mainchannel.messages.StatementListMessage;

/**
 * Created by Administrator on 28/05/2015.
 */
public class Transaction extends BaseAccountRequest {


    public  Transaction(){

    }

    private BaseAccountRequest ResultFromGateway=new BaseAccountRequest();
    public  void       setResultResultFromGateway(Boolean Success,String Result,String ActionCode,String GatewayMessage){
        ResultFromGateway.setIsSuccessfulConnectToGateway(Success);
        ResultFromGateway.setResult(Result);
        ResultFromGateway.setActionCode(ActionCode);
        ResultFromGateway.setGatewayMessage(GatewayMessage);
    }
    public BaseAccountRequest getResultFromGateway(){
        return this.ResultFromGateway;
    }

    private String AccountNumber=null;
    public  void   setAccountNumber(String accountNumber){
        this.AccountNumber=accountNumber;
    }
    public  String getAccountNumber(){
        return AccountNumber;
    }

    private String StatementType=null;
    public  void   setStatementType(String statementType){
        this.StatementType=statementType;
    }
    public  String getStatementType(){
        return StatementType;
    }

    private String StartDate=null;
    public  void   setStartDate(String startDate){
        this.StartDate=startDate;
    }
    public  String getStartDate(){
        return StartDate;
    }

    private String EndDate=null;
    public  void   setEndDate(String endDate){
        this.EndDate=endDate;
    }
    public  String getEndDate(){
        return EndDate;
    }

    private String StartTime=null;
    public  void   setStartTime(String startTime){
        this.StartTime=startTime;
    }
    public  String getStartTime(){
        return StartTime;
    }

    private String EndTime=null;
    public  void   setEndTime(String endTime){
        this.EndTime=endTime;
    }
    public  String getEndTime(){
        return EndTime;
    }

    private String TransactionCount=null;
    public  void   setTransactionCount(String transactionCount){
        this.TransactionCount=transactionCount;
    }
    public  String getTransactionCount(){
        return TransactionCount;
    }

    private String CreditOrDebit=null;
    public  void   setCreditOrDebit(String creditOrDebit){
        this.CreditOrDebit=creditOrDebit;
    }
    public  String getCreditOrDebit(){
        return CreditOrDebit;
    }

    private String TransactionMinAmount=null;
    public  void   setTransactionMinAmount(String transactionMinAmount){
        this.TransactionMinAmount=transactionMinAmount;
    }
    public  String getTransactionMinAmount(){
        return TransactionMinAmount;
    }

    private String TransactionMaxAmount=null;
    public  void   setTransactionMaxAmount(String transactionMaxAmount){
        this.TransactionMaxAmount=transactionMaxAmount;
    }
    public  String getTransactionMaxAmount(){
        return TransactionMaxAmount;
    }

    private String TransactionDocDescription=null;
    public  void   setTransactionDocDescription(String transactionDocDescription){
        this.TransactionDocDescription=transactionDocDescription;
    }
    public  String getTransactionDocDescription(){
        return TransactionDocDescription;
    }

    private String TransactionDocNO=null;
    public  void   setTransactionDocNO(String transactionDocNO){
        this.TransactionDocNO=transactionDocNO;
    }
    public  String getTransactionDocNO(){
        return TransactionDocNO;
    }

    private String TransactionOperationCode=null;
    public  void   setTransactionOperationCode(String transactionOperationCode){
        this.TransactionOperationCode=transactionOperationCode;
    }
    public  String getTransactionOperationCode(){
        return TransactionOperationCode;
    }

    private String BranchCode=null;
    public  void   setBranchCode(String branchCode){
        this.BranchCode=branchCode;
    }
    public  String getBranchCode(){
        return BranchCode;
    }

    private String rrn=null;
    public  void   setrrn(String Rrn){
        this.rrn=Rrn;
    }
    public  String getrrn(){
        return rrn;
    }


    private StatementListMessage ResultFromCM =null;
    public  void setResultFromCM(StatementListMessage statementlistMessage){
        this.ResultFromCM =statementlistMessage;
    }
    public  StatementListMessage getResultFromCM(){
        return ResultFromCM;
    }

}
