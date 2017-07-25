package ServiceObjects.Account;

import Mainchannel.messages.ACHFundTransferMessage;

/**
 * Created by Administrator on 6/14/2015.
 */
public class FollowUpTransactionACH extends BaseAccountRequest {

    private String SourceBank=null;
    public  void   setSourceBank(String sourceBank){
        SourceBank=sourceBank;
    }
    public  String getSourceBank(){
        return SourceBank;
    }

    private String SourceAccount=null;
    public  void   setSourceAccount(String sourceAccount){
        SourceAccount=sourceAccount;
    }
    public  String getSourceAccount(){
        return SourceAccount;
    }

    private String DestinationBank=null;
    public  void   setDestinationBank(String destinationBank){
        DestinationBank=destinationBank;
    }
    public  String getDestinationBank(){
        return DestinationBank;
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

    private ACHFundTransferMessage ResultFromChannel=null;
    public  void   setResultFromChannel(ACHFundTransferMessage resultFromChannel){
        ResultFromChannel=resultFromChannel;
    }
    public  ACHFundTransferMessage getResultFromChannel(){
        return ResultFromChannel;
    }

}
