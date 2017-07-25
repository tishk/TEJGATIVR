package ServiceObjects.Account;

/**
 * Created by Administrator on 12/17/2015.
 */
public class InternalFollowUp extends BaseAccountRequest{

    private String MessageSequence=null;
    public  void   setMessageSequence(String messageSequence){
        MessageSequence=messageSequence;
    }
    public  String getMessageSequence(){
        return MessageSequence;
    }

    private boolean IsPanPayment=false;
    public  void    setIsPanPayment(boolean isPanPayment){
         this.IsPanPayment=isPanPayment;
    }
    public  boolean getIsPanPayment(){
        return IsPanPayment;
    }


    private String ActionCode=null;
    public  void   setActionCode(String actionCode){
        ActionCode=actionCode;
    }
    public  String getActionCode(){
        return ActionCode;
    }


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

    private String FollowUpCode=null;
    public  void   setFollowUpCode(String followUpCode){
        FollowUpCode=followUpCode;
    }
    public  String getFollowUpCode(){
        return FollowUpCode;
    }

    private String TransactionDateTime=null;
    public  void   setTransactionDateTime(String transactionDateTime){
        TransactionDateTime=transactionDateTime;
    }
    public  String getTransactionDateTime(){
        return TransactionDateTime;
    }

    private boolean IsInternalFollowCode=false;
    public  void   setIsInternalFollowCode(boolean isInternalFollowCode){
        IsInternalFollowCode=isInternalFollowCode;
    }
    public  boolean getIsInternalFollowCode(){
        return IsInternalFollowCode;
    }

    private String status="-2";
    public  void    setstatus(String Status){
        status=Status;
    }
    public  String getstatus(){
        return status;
    }

    private String DoneFlag="-2";
    public  void   setdoneFlag(String doneFlag){
        DoneFlag=doneFlag;
    }
    public  String getdoneFlag(){
        return DoneFlag;
    }

    private boolean isValidFollowCode=false;
    public  void    setisValidFollowCode(boolean IsValidFollowCode){
        isValidFollowCode=IsValidFollowCode;
    }
    public  boolean getisValidFollowCode(){
        return isValidFollowCode;
    }

}
