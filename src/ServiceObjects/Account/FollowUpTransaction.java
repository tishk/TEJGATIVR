package ServiceObjects.Account;

import Mainchannel.messages.FollowUpMessage;

/**
 * Created by Administrator on 6/14/2015.
 */
public class FollowUpTransaction extends BaseAccountRequest {

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

    private String status="";
    public  void    setstatus(String Status){
        status=Status;
    }
    public  String getstatus(){
        return status;
    }

    private boolean isValidFollowCode=false;
    public  void    setisValidFollowCode(boolean IsValidFollowCode){
        isValidFollowCode=IsValidFollowCode;
    }
    public  boolean getisValidFollowCode(){
        return isValidFollowCode;
    }



    private FollowUpMessage ResultFromChannel=null;
    public  void   setResultFromChannel(FollowUpMessage resultFromChannel){
        ResultFromChannel=resultFromChannel;
    }
    public  FollowUpMessage getResultFromChannel(){
        return ResultFromChannel;
    }



}
