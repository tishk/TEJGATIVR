package ServiceObjects.Account;

import Mainchannel.messages.CardAccountMessage;

/**
 * Created by Administrator on 28/05/2015.
 */
public class CardNoOfAccount extends BaseAccountRequest {
    private String AccountNumber=null;
    public  void   setAccountNumber(String accountNumber){
        AccountNumber=accountNumber;
    }
    public  String getAccountNumber(){
        return AccountNumber;
    }



    private CardAccountMessage ResultFromChannel=null;
    public  void   setResultFromChannel(CardAccountMessage resultFromChannel){
        ResultFromChannel=resultFromChannel;
    }
    public  CardAccountMessage getResultFromChannel(){
        return ResultFromChannel;
    }
}
