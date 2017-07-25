package ServiceObjects.Account;

import Mainchannel.messages.RegistrationMessage;

/**
 * Created by Administrator on 6/14/2015.
 */
public class RegisterAccount extends BaseAccountRequest {
    private String AccountNumber=null;
    public  void   setAccountNumber(String accountNumber){
        AccountNumber=accountNumber;
    }
    public  String getAccountNumber(){
        return AccountNumber;
    }

    private String ServicesType=null;
    public  void   setServicesType(String servicesType){
        ServicesType=servicesType;
    }
    public  String getServicesType(){
        return ServicesType;
    }

    private RegistrationMessage ResultFromChannel=null;
    public  void   setResultFromChannel(RegistrationMessage resultFromChannel){
        ResultFromChannel=resultFromChannel;
    }
    public  RegistrationMessage getResultFromChannel(){
        return ResultFromChannel;
    }
}
