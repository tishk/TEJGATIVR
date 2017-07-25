package ServiceObjects.Account;

import Mainchannel.messages.AccountMessage;

/**
 * Created by Administrator on 6/17/2015.
 */
public class BlockAccount extends BaseAccountRequest {

    private String AccountNumber=null;
    public  void   setAccountNumber(String sourceAccount){
        AccountNumber=sourceAccount;
    }
    public  String getAccountNumber(){
        return AccountNumber;
    }

    private String BlockMode=null;
    public  void   setBlockMode(String blockMode){
        BlockMode=blockMode;
    }
    public  String getBlockMode(){
        return BlockMode;
    }

    private AccountMessage ResultFromChannel=null;
    public  void   setResultFromChannel(AccountMessage resultFromChannel){
        ResultFromChannel=resultFromChannel;
    }
    public  AccountMessage getResultFromChannel(){
        return ResultFromChannel;
    }

}
