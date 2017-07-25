package ServiceObjects.Pin;

import ServiceObjects.Account.BaseAccountRequest;

/**
 * Created by Administrator on 28/05/2015.
 */
public class AuthenticatePin2 extends BaseAccountRequest {

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

    private String Pin_New=null;
    public  void   setPin_New(String pin_new){
        Pin_New=pin_new;
    }
    public  String getPin_New(){
        return Pin_New;
    }

    private boolean DoChangePin=false;
    public  void   setDoChangePin(boolean doChangePin){
        DoChangePin=doChangePin;
    }
    public  boolean getDoChangePin(){
        return DoChangePin;
    }

    private String CallerID=null;
    public  void   setCallerID(String callerID){
        CallerID=callerID;
    }
    public  String getCallerID(){
        return CallerID;
    }

    private boolean IsNotHaghighiAccount=false;
    public  void    setIsNotHaghighiAccount(boolean IsNotHaghighiAccount){
        this.IsNotHaghighiAccount=IsNotHaghighiAccount;
    }
    public  boolean getIsNotHaghighiAccount(){
        return IsNotHaghighiAccount;
    }


}
