package ServiceObjects.Account;

import Mainchannel.messages.ChequeStatusMessage;

/**
 * Created by Administrator on 6/14/2015.
 */
public class ChequeStatus extends BaseAccountRequest {
    private String AccountNumber=null;
    public  void   setAccountNumber(String sourceAccount){
        AccountNumber=sourceAccount;
    }
    public  String getAccountNumber(){
        return AccountNumber;
    }

    private String ChequeNO=null;
    public  void   setChequeNO(String chequeNO){
        ChequeNO=chequeNO;
    }
    public  String getChequeNO(){
        return ChequeNO;
    }

    private String ChequeAmount=null;
    public  void   setChequeAmount(String chequeAmount){
        ChequeAmount=chequeAmount;
    }
    public  String getChequeAmount(){
        return ChequeAmount;
    }

    private String ChequeOperateDate=null;
    public  void   setChequeOperateDate(String chequeOperateDate){
        ChequeOperateDate=chequeOperateDate;
    }
    public  String getChequeOperateDate(){
        return ChequeOperateDate;
    }

    private ChequeStatusMessage ResultFromChannel=null;
    public  void   setResultFromChannel(ChequeStatusMessage resultFromChannel){
        ResultFromChannel=resultFromChannel;
    }
    public  ChequeStatusMessage getResultFromChannel(){
        return ResultFromChannel;
    }
}
