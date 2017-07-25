package ServiceObjects.Account;

import Mainchannel.messages.BalanceMessage;

import java.io.Serializable;

/**
 * Created by Administrator on 28/05/2015.
 */
public class BalanceForAccount extends BaseAccountRequest  {

    private String AccountNumber=null;
    public  void   setAccountNumber(String accountNumber){
        AccountNumber=accountNumber;
    }
    public  String getAccountNumber(){
        return AccountNumber;
    }



    private BalanceMessage ResultFromChannel=null;
    public  void   setResultFromChannel(BalanceMessage resultFromChannel){
        ResultFromChannel=resultFromChannel;
    }
    public  BalanceMessage getResultFromChannel(){
        return ResultFromChannel;
    }


}
