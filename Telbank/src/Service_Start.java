import ServiceObjects.Account.AccountInformation;
import ServiceObjects.Account.BalanceForAccount;
import org.asteriskjava.fastagi.AgiException;

import java.io.IOException;

/**
 * Created by Administrator on 11/22/2015.
 */
public class Service_Start {

    private Call call=new Call();;
    boolean maybeisPan=false;
    boolean maybeisAccount=false;
    boolean isNotEnterAny=false;
    int counterOfGetMenu=0;
    public Voices Say=new  Voices();
    public strUtils strutils=new strUtils();
    public Service_Start(Call c) throws Exception {
        this.call=c;
       // Util.printMessage("start calling from telnumber:"+call.getCallerID(),false);
        processFirstInput();
    }
    public  void    getAccountOrCard(boolean isnull) throws AgiException, IOException {
        if (!isnull) call.setUserEntrance(Say.accountNotValidreenter());
        else call.setUserEntrance(Say.enterAccountNumber());
       setKindOfEntrance();

    }
    private void    setKindOfEntrance() throws IOException {

        if (call.getUserEntrance().length()==0){
            maybeisPan=false;
            maybeisAccount=false;
            isNotEnterAny=true;
        }
        else if (call.getUserEntrance().equals("1364139413641394")){
            maybeisPan=false;
            maybeisAccount=true;
            isNotEnterAny=false;
        }
        else if (call.getUserEntrance().length()==16||call.getUserEntrance().length()==19){
            maybeisPan=true;
            maybeisAccount=false;
            isNotEnterAny=false;
        }
        else if (call.getUserEntrance().length()<=13){
            maybeisPan=false;
            maybeisAccount=true;
            isNotEnterAny=false;
        }
        else{
            maybeisPan=false;
            maybeisAccount=false;
            isNotEnterAny=false;
        }

    }
    public  void    byAndHangUp() throws AgiException{
        System.gc();
        Say.by();
    }
    public  void    processInvalidEntry() throws AgiException, IOException {
        String s=Say.accountNotValidreenter();
        Util.printMessage("receive from invalid",false);

            getAccountOrCard(false);

    }
    public  void    processIncorrectAccount() throws AgiException, IOException {
        getAccountOrCard(true);
    }
    public  void    processNoEntry() throws AgiException, IOException {

            getAccountOrCard(true);

    }
    private void    processnoNotValid() throws AgiException, IOException {

            getAccountOrCard(false);

    }
    private void    processFirstInput() throws Exception {

        setKindOfEntrance();
        do{
            if (maybeisAccount){
                if (isAccount()){

                    call.setAccount(call.getUserEntrance());
                    call.setParentStart(this);

                    Service_Account_Main service_account_main= new Service_Account_Main(call);
                    service_account_main=null;
                    break;
                }else{
                    counterOfGetMenu++;
                    processIncorrectAccount();
                }

            }
            else if (maybeisPan){

                if (isPan()){
                    call.setPan(call.getUserEntrance());
                    call.setParentStart(this);
                    Service_Pan service_pan= new Service_Pan(call);
                    service_pan=null;
                    break;
                }else{
                    counterOfGetMenu++;
                   // Util.printMessage("h11",false);
                    processnoNotValid();
                }

            }
            else if(isNotEnterAny)
            {
                counterOfGetMenu++;
                processNoEntry();
            }
            else {
                counterOfGetMenu++;

                processInvalidEntry();
            }
        }while (counterOfGetMenu++<3);
        byAndHangUp();
    }
    private void    processChannelActionCode(String actionCode) throws AgiException {

       if (actionCode.equals("3000")||actionCode.equals("3002")||actionCode.equals("3003")){
           Say.goBranch();
          // byAndHangUp();
       }else if (actionCode.equals("9000")){
           //call.getParentStart().Say.accountNotValid();
       }else if (actionCode.equals("7777")){
           Say.baArzePozesh();
         //  byAndHangUp();
       }else if (actionCode.equals("9000")){
           Say.baArzePozesh();
           //byAndHangUp();
       }else if (actionCode.equals("6667")){
           Say.baArzePozesh();
           //byAndHangUp();
       }else if (actionCode.equals("1940")){
           Say.codeMelliNadarad();
          // byAndHangUp();
       }
       else {
           Say.errorCode(actionCode);
           //byAndHangUp();
       }
    }
    private boolean isAccount() throws Exception {
        //call.getServer();
      //  if (call.getUserEntrance().equals("1364139413641394")) return true;
        BalanceForAccount balanceForAccount=new BalanceForAccount();
        balanceForAccount.setAccountNumber(call.getUserEntrance());

        if (call.connectedToGateway){
            balanceForAccount.setCallUniqID(call.getCallUniqID());
            balanceForAccount=(BalanceForAccount)call.submitRequestToGateway(balanceForAccount);
            call.setResult(balanceForAccount);
            Util.printMessage("action code of balance:"+balanceForAccount.getResultFromChannel().getAction_code(),false);

        }else {
            balanceForAccount.setActionCode("6667");
        }

        if (balanceForAccount.getResultFromChannel().getAction_code().equals("0000")){

            call.setBalance(balanceForAccount.getResultFromChannel().getActualBalance());
            AccountInformation accountInformation=new AccountInformation();
            accountInformation.setAccountNumber(call.getUserEntrance());
            accountInformation.setCallUniqID(call.getCallUniqID());
            accountInformation=(AccountInformation)call.submitRequestToGateway(accountInformation);
            if (accountInformation.getResultFromChannel().getAction_code().equals("0000")){
                Util.printMessage("action code of account information:"+accountInformation.getResultFromChannel().getAction_code(),false);
               // Util.printMessage("CurrencyCode of account information:"+accountInformation.getResultFromCM().getAccountcurrency(),false);
               // Util.printMessage("NameAndFamily of account information:"+accountInformation.getResultFromCM().getFarsiName(),false);
                Util.printMessage("PersonType of account information:"+accountInformation.getResultFromChannel().getPrsnType(), false);
                call.setPersonType(accountInformation.getResultFromChannel().getPrsnType());
                call.setCurrencyCode(accountInformation.getResultFromChannel().getAccountcurrency());
                call.setNameAndFamily(accountInformation.getResultFromChannel().getFarsiName());
                call.setBranch(accountInformation.getBranchName());
            }else {
                return false;
            }


           // call.setServicesType(accountInformation.getResultFromCM().get);
           // call.setBranch(accountInformation.getResultFromCM().get);
           // Util.printMessage("Autenticated balance is "+balanceForAccount.getResultFromCM().getActualBalance(),false);
            return true;
        }
        else processChannelActionCode(balanceForAccount.getResultFromChannel().getAction_code());
        return false;
    }
    private boolean isAccount__() throws Exception {

        return true;
    }
    private boolean isPan() throws AgiException, IOException {
       if (checkDigitPanIsOK(call.getUserEntrance())){
           if (isTejaratPan(call.getUserEntrance())){

               call.setisTejaratPan(true);
              return true;
           }else return true;
       }else return false;
    }
    private boolean checkDigitPanIsOK_(String pan){
        boolean isNumber;
        if (pan.substring(0,6).equals("627353") || (pan.length()==19)) return true;
        try{
            long t=Long.valueOf(pan)/10000000000L;
            isNumber=true;
        }catch (NumberFormatException var1){
            isNumber=false;
        }
        if (isNumber){
            int l=pan.length();
            int sum=0,Ptemp=0,temp=0;
            for(int i=1;i<=l;i++){
                Ptemp=Integer.valueOf(pan.substring(i-1,i));

                if (i%2==0){
                    temp=Ptemp*1;
                }else{
                    temp=Ptemp*2;
                }
                if (temp>9) temp-=temp-9;

                sum+=temp;
            }
            return (sum % 10==0);
//            return true;
//            else return false;
        }else return false;
    }
    private boolean checkDigitPanIsOK(String pan){
        boolean isNumber=false;
        if (pan.substring(0,6).equals("627353") || (pan.length()==19)) return true;
        try{
            long t=Long.valueOf(pan)/10000000000L;
            isNumber=true;
        }catch (NumberFormatException var1){
            isNumber=false;
        }
        if (isNumber){
            int l=pan.length();
            int sum=0,Ptemp=0,temp=0;
            for(int i=1;i<=l;i++){
                if (i%2==0){
                    Ptemp=Integer.valueOf(pan.substring(i-1,i));
                    temp=Ptemp*1;
                    if (temp>9) temp=temp-9;
                }else{
                    Ptemp=Integer.valueOf(pan.substring(i - 1, i));
                    temp=Ptemp*2;
                    if (temp>9) temp=temp-9;
                }
                sum=sum+temp;
            }
            if (sum % 10==0) return true;
            else return false;
        }else return false;
    }
    private boolean isTejaratPan(String pan) {
      if (pan.substring(0,6).equals("585983")||pan.substring(0,6).equals("627353")) return true;
        else return false;
    }

}
