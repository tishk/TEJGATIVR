package ServiceObjects.Account;

import Mainchannel.messages.AccountListMessage;

/**
 * Created by Administrator on 09/06/2015.
 */
public class AccountInformation extends BaseAccountRequest {
    private String AccountNumber=null;
    public  void   setAccountNumber(String accountNumber){
        AccountNumber=accountNumber;
    }
    public  String getAccountNumber(){
        return AccountNumber;
    }

    private String Pin=null;
    public  void   setPin(String pin){
        Pin=pin;
    }
    public  String getPin(){
        return Pin;
    }

    private String BranchName=null;
    public  void   setBranchName(String BranchName){
        this.BranchName=BranchName;
    }
    public  String getBranchName(){
        return BranchName;
    }

    private AccountListMessage ResultFromChannel=null;
    public  void   setResultFromChannel(AccountListMessage resultFromChannel){
        ResultFromChannel=resultFromChannel;
    }
    public  AccountListMessage getResultFromChannel(){
        return ResultFromChannel;
    }

}
