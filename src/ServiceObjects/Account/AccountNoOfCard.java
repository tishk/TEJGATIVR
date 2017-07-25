package ServiceObjects.Account;

import Mainchannel.messages.CardAccountMessage;

import java.io.Serializable;

/**
 * Created by Administrator on 28/05/2015.
 */
public class AccountNoOfCard extends BaseAccountRequest {

    private String CardNo=null;
    public  void   setCardNo(String accountNumber){
        CardNo=accountNumber;
    }
    public  String getCardNo(){
        return CardNo;
    }



    private CardAccountMessage ResultFromChannel=null;
    public  void   setResultFromChannel(CardAccountMessage resultFromChannel){
        ResultFromChannel=resultFromChannel;
    }
    public  CardAccountMessage getResultFromChannel(){
        return ResultFromChannel;
    }

}
