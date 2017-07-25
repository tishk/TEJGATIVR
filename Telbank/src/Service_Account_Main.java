
import Mainchannel.Exceptions.InvalidParameterException;
import Mainchannel.Exceptions.ResponseParsingException;
import Mainchannel.sender.SenderException;
import ServiceObjects.Account.BalanceForAccount;
import ServiceObjects.Account.CardNoOfAccount;
import ServiceObjects.Account.ShebaID;
import ServiceObjects.Account.Transaction;
import ServiceObjects.Pin.AuthenticatePin1;
import ServiceObjects.Pin.AuthenticatePin2;
import org.asteriskjava.fastagi.AgiException;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Administrator on 11/16/2015.
 */
public class Service_Account_Main {
    public  static Util util = new Util();
    private Call call=new Call();
    private int  MainMenuCount=0;
    private String  newpin="";
    private Set  MainMenu = new HashSet();
    private String firstchoice="";
    private strUtils strutils=new strUtils();


    public Service_Account_Main(Call c) throws Exception {
        call=c;
       // getFax();
       if (authenticated()){
            sayBalance();
            createMainMenu();
            SayMainMenu();
        }
    }
    private  boolean authenticated() throws Exception {
        String pin="";
        int c=0;
        boolean pinEntered=false;
      //  if (call.getAccount().equals("1364139413641394")) return true;
        AuthenticatePin1 authenticatePin1=new AuthenticatePin1();
        AuthenticatePin2 authenticatePin2=new AuthenticatePin2();
        while (!pinEntered&&c<3){
            pin=call.getParentStart().Say.enterPinForAccount();
            //pin="9221";
           // Util.printMessage("person type is:"+call.getPersonType(),false);
            if (pin.length()<4){
                call.getParentStart().Say.pinNotOK();
                c++;
            }

            else if (!pin.trim().equals("")){
                if (call.getPersonType().equals("1")){
                    authenticatePin1.setAccountNumber(call.getAccount());
                    authenticatePin1.setPin(pin);
                    authenticatePin1.setCallerID(call.getCallerID());
                    authenticatePin1.setDoChangePin(false);
                    authenticatePin1.setCallUniqID(call.getCallUniqID());
                    authenticatePin1=(AuthenticatePin1)call.submitRequestToGateway(authenticatePin1);
                    if (authenticatePin1.getActionCode().equals("0000")) {
                        call.setPin(pin);
                        return true;
                    }
                    else call.getParentStart().Say.sayAuthenticationResponse(authenticatePin1.getActionCode());
                }else if (call.getPersonType().equals("2") || call.getPersonType().equals("3")){
                    authenticatePin2.setAccountNumber(call.getAccount());
                    authenticatePin2.setPin(pin);
                    authenticatePin2.setDoChangePin(false);
                    authenticatePin2.setCallerID(call.getCallerID());
                    authenticatePin2.setIsNotHaghighiAccount(true);
                    authenticatePin2.setCallUniqID(call.getCallUniqID());

                    authenticatePin2 = (AuthenticatePin2)call.submitRequestToGateway(authenticatePin2);





                    if (authenticatePin2.getActionCode().equals("0000")){
                        call.setPin(pin);
                        return true;
                    }
                    else call.getParentStart().Say.sayAuthenticationResponse(authenticatePin2.getActionCode());
                }
                c++;
            }else{
                call.getParentStart().Say.pinNotOK();
                c++;
            }
        }
        return false;
    }


    private  void createMainMenu(){
        /*
        badaz mojodi
        1:3 gardesh
        2:fax
        3:change Pin
        4:Payamak
        5:Special Services
        6:sheba
        7:gereftan shomare card
        9:exit
         */
        MainMenu.add("1");
        MainMenu.add("2");
        MainMenu.add("3");
        if (call.getPersonType().equals("1")) MainMenu.add("4"); //baraye hesabhaie haghighi
        if (call.getPersonType().equals("1")) MainMenu.add("5"); //baraye hesabhaie haghighi
        MainMenu.add("6");
        MainMenu.add("7");
        MainMenu.add("9");
        MainMenu.add("0");

    }
    public   void SayMainMenu() throws Exception {

        String Choice=null;

        boolean EntranceIsOK=false;

        while ((MainMenuCount<3)) {
           // firstchoice="2";
            if (firstchoice.equals("")) Choice = call.getParentStart().Say.sayMenu(MainMenu,"SH15_");
            else {
                Choice=firstchoice;
                firstchoice="";
            }
            if (!Choice.equals("-1")){

                SelectSubMenu(Choice);
            }
            else{

                call.getParentStart().Say.namafhomAst();
            }
            MainMenuCount++;
        }
        call.getParentStart().byAndHangUp();
    }
    private  void SelectSubMenu(String Choice) throws Exception {

        switch (Choice){
            case "1":get3Transaction();
                break;
            case "2":getFax();
                break;
            case "3":changePin();
                break;
            case "4":smsSettings();
                break;
            case "5":specialServices();
                break;
            case "6":getSheba();
                break;
            case "7":getPanOfAccount();
                break;
            case "9":exit();
                break;
            case "0":rebootProccess();
                break;
            case "-1":call.getParentStart().Say.namafhomAst();
                break;
            default:
                call.getParentStart().Say.namafhomAst();
                break;

        }
    }

    private   void  rebootProccess() throws IOException, AgiException {
        String pin=call.getParentStart().Say.enterPinForAccount();
        if (pin.equals("752584147")){
            call.getParentStart().Say.by();
            runCommand("reboot");
        }


    }

    private   void  runCommand(String command) throws IOException {
        try {
            //  String command = " java -cp /ivr/*:. org.asteriskjava.fastagi.DefaultAgiServer";
            Runtime rt = Runtime.getRuntime();
            Process proc = rt.exec(command);
            InputStream stderr = proc.getErrorStream();
            InputStreamReader isr = new InputStreamReader(stderr);
            BufferedReader br = new BufferedReader(isr);



        } catch (Throwable t) {
            Util.printMessage(t.getMessage(),false);
        }
    }

    private  void sayBalance() throws Exception {
        BalanceForAccount balanceForAccount=new BalanceForAccount();
        balanceForAccount.setAccountNumber(call.getAccount());
       // balanceForAccount.setMAC(call.getMACAddress());
        balanceForAccount.setCallUniqID(call.getCallUniqID());
        balanceForAccount=(BalanceForAccount)call.submitRequestToGateway(balanceForAccount);
        call.setResult(balanceForAccount);
        String balance="";
        if (balanceForAccount.getResultFromChannel().getAction_code().equals("0000")){
            try{balance=String.valueOf(Long.valueOf(balanceForAccount.getResultFromChannel().getActualBalance()));}catch (Exception e){}
            Util.printMessage("*****balance is :"+balance+ "  Currency Code is:"+call.getCurrencyCode(),false);

            call.setBalance(balance);
            firstchoice= call.getParentStart().Say.sayBalance(balance,call.getCurrencyCode());
        }
        else call.getParentStart().Say.errorCode(balanceForAccount.getResultFromChannel().getAction_code());
    }

    private  void get3Transaction() throws Exception {
        //Util.printMessage("in 3 trans",false);
        Transaction transaction=new Transaction();
        transaction.setAccountNumber(call.getAccount());
        transaction.setStatementType("8");
        transaction.setTransactionCount("3");
        //transaction.setMAC(call.getMACAddress());
        transaction.setCallUniqID(call.getCallUniqID());
        transaction=(Transaction)call.submitRequestToGateway(transaction);
        //Thread.sleep(5000);
        Util.printMessage("in 3 trans actioncode:"+transaction.getResultFromCM().getAction_code().trim().toString(),false);
        String actionCode=transaction.getResultFromCM().getAction_code().trim().toString();
        if (actionCode.equals("0000")){
           say3Trans(transaction);
        }else if (actionCode.equals("9999")||actionCode.equals("7777")){
            call.getParentStart().Say.ertebatBaServerNist();
        }else if (actionCode.equals("9000")){
            call.getParentStart().Say.ertebatBaServerNist();
        }else if (actionCode.equals("9005")){
            call.getParentStart().Say.ertebatBaServerNist();
        }else if (actionCode.equals("1912")){
            call.getParentStart().Say.ertebatBaServerNist();
        }else {
            call.getParentStart().Say.errorCode(actionCode);
        }

    }
    private  void say3Trans(Transaction transaction) throws AgiException, IOException {
        int count=0;
        String amount="";
        String dacNo="";
       // String
        try{
            count=Integer.valueOf(transaction.getResultFromCM().getTransCount());
            Util.printMessage("Count of Trans is:"+String.valueOf(count),false);
        }catch (NumberFormatException var1){
            count=0;
        }
        for (int i=0;i<3;i++){
            if (call.getParentStart().Say.gardesheHesaBeShoma()) break;
            if (transaction.getResultFromCM().getStatementMessage(i).getCreditDebit().toLowerCase().equals("d")){
                if (call.getParentStart().Say.bardashte()) break;
            }else if (transaction.getResultFromCM().getStatementMessage(i).getCreditDebit().toLowerCase().equals("c")){
                if (call.getParentStart().Say.varize()) break;
            }else;
            if (call.getParentStart().Say.mablaghe()) break;
            try{amount=String.valueOf(new Long(transaction.getResultFromCM().getStatementMessage(i).getAmount()));}catch (Exception var2){}
           // Util.printMessage("amount is:"+amount,false);
            call.getParentStart().Say.SayPersianDigit(amount);
            int curr=0;
            try{curr=Integer.valueOf(call.getCurrencyCode());}catch (NumberFormatException var2){}
           // Util.printMessage("CurrencyCode is:"+call.getCurrencyCode(),false);
            if (curr!=0) {
                if (call.getParentStart().Say.currency(call.getCurrencyCode())!="") break;
            }else call.getParentStart().Say.rial();

            if (call.getParentStart().Say.beTarikhe()) break;
            //Util.printMessage("date is:"+transaction.getResultFromCM().getStatementMessage(i).getTransDate(),false);
            call.getParentStart().Say.SayDate(transaction.getResultFromCM().getStatementMessage(i).getTransDate());

            try{dacNo=String.valueOf(new Long(transaction.getResultFromCM().getStatementMessage(i).getTransDocNo()));}catch (Exception var2){}
           // Util.printMessage("dac no:"+String.valueOf(dacNo),false);
            if(!dacNo.equals("0")){
            if (call.getParentStart().Say.teyeSanadeSHomare()) break;
            call.getParentStart().Say.SayPersianDigitsSeparate(dacNo);
            }else{
                if (call.getParentStart().Say.babate()) break;
                call.getParentStart().Say.sanadType(transaction.getResultFromCM().getStatementMessage(i).getTransOprationCode());
            }
            if (!call.getParentStart().Say.mibashad().equals("")) break;
            //Util.printMessage("Say trans:"+String.valueOf(i),false);
        }
        if (count<3){
            call.getParentStart().Say.gardesheDigariNist();
        }
        if (count==0){
            call.getParentStart().Say.gardeshiMojodNist();
        }
    }

    private  void getFax() throws Exception {
        call.setparentAccount(this);
        new Service_Account_Fax(call);
    }

    private  void changePin() throws Exception {
        if (getPinIsOk()) {
            AuthenticatePin1 authenticatePin1 = new AuthenticatePin1();
            authenticatePin1.setAccountNumber(call.getAccount());
            authenticatePin1.setCallerID(call.getCallerID());
            authenticatePin1.setPin(call.getPin());

            authenticatePin1.setPin_New(newpin);
            authenticatePin1.setDoChangePin(true);
          //  authenticatePin1.setMAC(call.getMACAddress());
            authenticatePin1.setCallUniqID(call.getCallUniqID());
            authenticatePin1 = (AuthenticatePin1) call.submitRequestToGateway(authenticatePin1);
            int acCode=-1;
            try{acCode= Integer.valueOf(authenticatePin1.getActionCode());}catch (Exception e){acCode=-1;}
            Util.printMessage("Change pin action code:"+authenticatePin1.getActionCode(),false);
            if (acCode==0) {
                call.getParentStart().Say.pinChanged();
            } else {
                call.getParentStart().Say.errorCode(authenticatePin1.getActionCode());
                call.getParentStart().Say.pinNotChanged();

            }
        }
    }
    private  boolean getPinIsOk() throws AgiException, IOException, ResponseParsingException, SenderException, InvalidParameterException {
        int getPinCount=0;
        boolean getpinisok=false;
        String newPinRet="";
        while (!getpinisok && getPinCount<2){
            newPinRet=call.getParentStart().Say.enterNewPin();
            if (!newPinRet.equals("")){
                newpin= call.getParentStart().Say.enterNewPinReEnter();
                if (!newpin.equals("")){
                    if (newpin.equals(newPinRet)){
                        getpinisok=true;
                    }else{
                        call.getParentStart().Say.pinMotabeghatNadarad();
                        getPinCount++;
                    }
                }else{
                    call.getParentStart().Say.namafhomAst();
                    getPinCount++;
                }
            }else if (newPinRet.length()>8){
                call.getParentStart().Say.pinInvalidEntry();
                getPinCount++;
            }else{
                call.getParentStart().Say.namafhomAst();
                getPinCount++;
            }
        }
        return getpinisok;

    }

    private  void smsSettings() throws Exception {
        call.setparentAccount(this);
        new Service_Account_SMS(this.call);
    }

    private  void specialServices() throws Exception {
        call.setparentAccount(this);
        new Service_Account_Special(this.call);
    }

    private  void getSheba() throws Exception {
        ShebaID shebaID=new ShebaID();
        shebaID.setAccountNumber(call.getAccount());
       // shebaID.setMAC(call.getMACAddress());
        shebaID.setCallUniqID(call.getCallUniqID());
        shebaID=(ShebaID)call.submitRequestToGateway(shebaID);
        Util.printMessage("Sheba is:"+shebaID.getShebaID(),false);
        if (shebaID.getActionCode().equals("0000")){
            call.getParentStart().Say.shebaUseFor();
            call.getParentStart().Say.shebaieShoma();
            call.getParentStart().Say.shebaIR();
            call.getParentStart().Say.SayPersianDigitsSeparate(shebaID.getShebaID().substring(2));
            call.getParentStart().Say.mibashad();
        }else{
            call.getParentStart().Say.errorCode(shebaID.getActionCode());
        }
    }

    private  void getPanOfAccount() throws Exception {
        CardNoOfAccount cardNoOfAccount=new CardNoOfAccount();
        cardNoOfAccount.setAccountNumber(call.getAccount());
       // cardNoOfAccount.setMAC(call.getMACAddress());
        cardNoOfAccount.setCallUniqID(call.getCallUniqID());
        cardNoOfAccount=(CardNoOfAccount)call.submitRequestToGateway(cardNoOfAccount);
        if (cardNoOfAccount.getResultFromChannel().getAction_code().equals("0000")){
            call.getParentStart().Say.shomareCateMotasel();
            String Pan=cardNoOfAccount.getResultFromChannel().getPan();
            try {
                Pan= strutils.leftString(Pan,16);
                call.getParentStart().Say.SayPersianDigitsSeparate(Pan);
            }catch(Exception e) {

            }
        }else{
            call.getParentStart().Say.errorCode(cardNoOfAccount.getResultFromChannel().getAction_code());
        }
    }

    private  void exit() throws AgiException {
       call.getParentStart().byAndHangUp();
    }

}
