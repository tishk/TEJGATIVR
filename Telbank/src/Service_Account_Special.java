import Mainchannel.Exceptions.InvalidParameterException;
import Mainchannel.Exceptions.ResponseParsingException;
import Mainchannel.sender.SenderException;
import ServiceObjects.Account.ChequeStatus;
import ServiceObjects.Account.FundTransfer;
import ServiceObjects.Account.InternalFollowUp;
import ServiceObjects.Pin.AuthenticatePin2;
import org.asteriskjava.fastagi.AgiException;

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Administrator on 11/17/2015.
 */
public class Service_Account_Special {

    private Call call=new Call();

    private int    MainMenuCount=0;
    private String newpin="";
    private String identCode="";
    private String identTransferAmount="";
    private String chequeSerial="";
    private String identTransferAcc="";
    private String destinationAccount="";
    private String transferAmount="";
    private String installmentAccount="";
    private String installmentAmount="";
    private String followCode="";

    private Set SpecialServicesMenu=new HashSet();
    public Service_Account_Special(Call c) throws Exception {
        call=c;
     //  if (authenticated()){
            createMainMenu();
            SayMainMenu();
      //  }
    }

    private  boolean authenticated() throws Exception {
        String pin="";
        int c=0;
        boolean pinEntered=false;
        AuthenticatePin2 authenticatePin2=new AuthenticatePin2();
        while (!pinEntered&&c<3){
            pin=call.getParentStart().Say.enterPin2();
           // Util.printMessage("entered pin2 is:"+pin,false);
            if (pin.length()<5){
                call.getParentStart().Say.pinNotOK();
                c++;
            }else if (!pin.trim().equals("")){
                authenticatePin2.setAccountNumber(call.getAccount());
                authenticatePin2.setCallerID(call.getCallerID());
                authenticatePin2.setPin(pin);
                authenticatePin2.setDoChangePin(false);
                authenticatePin2.setCallUniqID(call.getCallUniqID());
                authenticatePin2=(AuthenticatePin2)call.submitRequestToGateway(authenticatePin2);
                if (authenticatePin2.getActionCode().equals("0000")) {
                    call.setPin2(pin);
                    return true;
                }else call.getParentStart().Say.sayAuthenticationResponse(authenticatePin2.getActionCode());
                c++;
            }else{
                call.getParentStart().Say.pinNotOK();
                c++;
            }
        }
      //  Util.printMessage("pin2 acc code:"+authenticatePin2.getActionCode(),false);
        return false;
    }

    private  void    createMainMenu(){
        /*
        badaz mojodi
        1:Enteghale vajh
        2:Pardakhte Ghoboz
        3:Pardakhte Aghsat
        4:Peygiri Pardahkte Gheyre Hozori
        5:Change Pin2
        6:Entetghale vajhe shenase dar
        7:peygirieh cheque
        8:gheyre faal kardane hesab
        9:exit
         */
        SpecialServicesMenu.add("1");
        SpecialServicesMenu.add("2");
        SpecialServicesMenu.add("3");
        SpecialServicesMenu.add("4");
        SpecialServicesMenu.add("5");
        SpecialServicesMenu.add("6");
        SpecialServicesMenu.add("7");
        SpecialServicesMenu.add("8");
        SpecialServicesMenu.add("9");

    }
    public   void    SayMainMenu() throws Exception {

        String Choice=null;

        boolean EntranceIsOK=false;
        while ((MainMenuCount<2)) {
            Choice = call.getParentStart().Say.sayMenu(SpecialServicesMenu,"Sh18_");
            //Util.ShowMessage(Choice);
            if (!Choice.equals("-1")){

                if (authenticated()) SelectSubMenu(Choice);

            }
            else call.getParentStart().Say.namafhomAst();
            MainMenuCount++;
        }
        call.getParentStart().byAndHangUp();
    }
    private  void    SelectSubMenu(String Choice) throws Exception {
        switch (Choice){
            case "1":fundTransfer();
                break;
            case "2":billPayment();
                break;
            case "3":installmentPayment();
                break;
            case "4":followUP();
                break;
            case "5":changePin2();
                break;
            case "6":transferMoneyWithID();
                break;
            case "7":followCheque();
                break;
            case "8":inactiveAccount();
                break;
            case "9":exit();
                break;
            case "-1":call.getParentStart().Say.namafhomAst();
                break;
            default:
                call.getParentStart().Say.namafhomAst();
                break;

        }
    }

    private  void    fundTransfer() throws Exception {
        if (getDestinationAccountIsOK()){
            if (getAmountIsOK()){
                if (ConfirmFundTransferIsOK()){
                    doTransferMoney();
                }
            }
        }
    }
    private  void    doTransferMoney() throws Exception {
        FundTransfer fundTransfer=new FundTransfer();
        fundTransfer.setSourceAccount(call.getAccount());

        destinationAccount=call.getParentStart().strutils.fixLengthWithZero(destinationAccount,10);

        fundTransfer.setDestinationAccount(destinationAccount);
        fundTransfer.setTransactionAmount(transferAmount);
        fundTransfer.setIsFundTransfer(true);
        fundTransfer.setCallUniqID(call.getCallUniqID());
        try{
            fundTransfer=(FundTransfer)call.submitRequestToGateway(fundTransfer);
        }catch (Exception e){
        }
        doTransferMoneyResultReaction(fundTransfer);
    }
    private  void    doTransferMoneyResultReaction(FundTransfer fundTransfer) throws AgiException, IOException {
        int intActionCode=-1;
        String actionCode=fundTransfer.getActionCode();
        String codePeygiri=fundTransfer.getTraceID();
        try{intActionCode=Integer.valueOf(actionCode);}catch (NumberFormatException e){intActionCode=-1;}
        switch (intActionCode){
            case -1:
                call.getParentStart().Say.baArzePozesh();
                break;
            case 0:
                call.getParentStart().Say.transferOK();
                call.getParentStart().Say.shomarePeygireiShma();
                call.getParentStart().Say.SayPersianDigitsSeparate(codePeygiri);
                call.getParentStart().Say.mibashad();
                break;
            case 1000:
                call.getParentStart().Say.mablaghBardashMishavad();
                call.getParentStart().Say.shomarePeygireiShma();
                call.getParentStart().Say.SayPersianDigitsSeparate(codePeygiri);
                call.getParentStart().Say.mibashad();
                break;
            case 2000:
                call.getParentStart().Say.enteghalAnjamKhahadShod();
                call.getParentStart().Say.shomarePeygireiShma();
                call.getParentStart().Say.SayPersianDigitsSeparate(codePeygiri);
                call.getParentStart().Say.mibashad();
                break;
            case 3004:
                call.getParentStart().Say.sorryDoNotPay();
                break;
            case 9001:
                call.getParentStart().Say.accountNotValid();
                break;
            case 9008:
                call.getParentStart().Say.accountNotValid();
                break;
            case 9400:
                call.getParentStart().Say.accountNotValid();
                break;
            case 9500:
                call.getParentStart().Say.saghfPorAst();
                break;
            case 9600:
                call.getParentStart().Say.accountNotRegistered();
                break;
            //case 9700:
            //    break;
            //case 9800:
            //    break;
            case 9999:
                call.getParentStart().Say.baArzePozesh();
                break;
            case 1016:
                call.getParentStart().Say.mojodiKafiNist();
                break;
            case 1912:
                call.getParentStart().Say.noeHesabBaNoeDarkhastYeksanNist();
                break;
            default:
                call.getParentStart().Say.FundTransferError(actionCode);
                break;
        }
    }
    private  boolean getDestinationAccountIsOK() throws AgiException, IOException {
        int countOfGetAcc=0;
        boolean accEntred=false;
        while (!accEntred && countOfGetAcc<2){
            destinationAccount=call.getParentStart().Say.enterDestinationAccount();
            if (destinationAccount.length()==0){
                call.getParentStart().Say.farmaniDaryaftNashod();

                countOfGetAcc++;
            }else{

                accEntred=true;
            }
        }
        return accEntred;
    }
    private  boolean getAmountIsOK() throws AgiException, IOException {
        int countOfGetAmount=0;
        boolean amountEntred=false;
        while (!amountEntred && countOfGetAmount<2){
            transferAmount=call.getParentStart().Say.lotfanMablaghRaVarehNamaeid();
            if (transferAmount.trim().length()==0){
                call.getParentStart().Say.farmaniDaryaftNashod();
                countOfGetAmount++;
            }else{
                BigInteger t=new BigInteger("0");
                try{t=new BigInteger(transferAmount);}catch (Exception e){t=new BigInteger("0");}
                if (t!=new BigInteger("0")){
                   amountEntred=true;
                }else{
                    call.getParentStart().Say.noNotValid();
                    countOfGetAmount++;
                }
            }
        }
        return amountEntred;
    }
    private  boolean ConfirmFundTransferIsOK() throws AgiException, IOException {
        String confirmation="";
        call.getParentStart().Say.mablaghe();
        call.getParentStart().Say.SayPersianDigit(transferAmount);
        call.getParentStart().Say.rial();
        call.getParentStart().Say.bardashVaBeHesabe();
        call.getParentStart().Say.SayPersianDigitsSeparate(destinationAccount);
        call.getParentStart().Say.varizKhahadShod();
        confirmation=call.getParentStart().Say.ifCorrectPress5();

        if (confirmation.trim().equals("5")) return true;
        else return false;
    }

    private  void    billPayment() throws Exception {
        call.setSpecialService(this);
        call.setCanBillPay(true);
        new Service_BillPayment(call,false);
    }

    private  void    installmentPayment() throws Exception {

        if (getInstallmentAccountIsOK()){
            if (getAmountOfIsInstallmentOK()){
                if (ConfirmInstallmentIsOK()){
                    doInstallmentPayment();
                }
            }
        }
    }
    private  void    doInstallmentPayment() throws Exception {

        FundTransfer fundTransfer=new FundTransfer();
        fundTransfer.setSourceAccount(call.getAccount());
        fundTransfer.setDestinationAccount(installmentAccount);
        fundTransfer.setTransactionAmount(installmentAmount);
        fundTransfer.setIsInstallmentPay(true);
        fundTransfer.setCallUniqID(call.getCallUniqID());
        fundTransfer=(FundTransfer)call.submitRequestToGateway(fundTransfer);
        Util.printMessage("installment action code:"+fundTransfer.getActionCode(),false);
        doTransferMoneyResultReaction(fundTransfer);

    }
    private  boolean getInstallmentAccountIsOK() throws AgiException, IOException {
        int countOfGetInstallmentAcc=0;
        boolean InstallmentAccEntred=false;
        while (!InstallmentAccEntred && countOfGetInstallmentAcc<2){
            installmentAccount=call.getParentStart().Say.enterAccountOfinstallmentPayment();
            if (installmentAccount.trim().length()==0){
                call.getParentStart().Say.farmaniDaryaftNashod();
                countOfGetInstallmentAcc++;
            }else{
                BigInteger t=new BigInteger("0");
                try{t=new BigInteger(installmentAccount);}catch (Exception e){t=new BigInteger("0");}
                if (t!=new BigInteger("0")){
                    InstallmentAccEntred=true;
                }else{
                    call.getParentStart().Say.noNotValid();
                    countOfGetInstallmentAcc++;
                }
            }
        }
        return InstallmentAccEntred;
    }
    private  boolean getAmountOfIsInstallmentOK() throws AgiException, IOException {
        int countOfGetAmount=0;
        boolean amountEntred=false;
        while (!amountEntred && countOfGetAmount<2){
            installmentAmount=call.getParentStart().Say.lotfanMablaghRaVarehNamaeid();
            if (installmentAmount.trim().length()==0){
                call.getParentStart().Say.farmaniDaryaftNashod();
                countOfGetAmount++;
            }else{
                BigInteger t=new BigInteger("0");
                try{t=new BigInteger(installmentAmount);}catch (Exception e){t=new BigInteger("0");}
                if (t!=new BigInteger("0")){
                    amountEntred=true;
                }else{
                    call.getParentStart().Say.noNotValid();
                    countOfGetAmount++;
                }
            }
        }
        return amountEntred;
    }
    private  boolean ConfirmInstallmentIsOK() throws AgiException, IOException {
        String confirmation="";
        call.getParentStart().Say.mablaghe();
        call.getParentStart().Say.SayPersianDigit(installmentAmount);
        call.getParentStart().Say.rial();
        call.getParentStart().Say.bardashVaBeHesabe();
        call.getParentStart().Say.SayPersianDigitsSeparate(installmentAccount);
        call.getParentStart().Say.varizKhahadShod();
        confirmation=call.getParentStart().Say.ifCorrectPress5();
        Util.printMessage("in do transfer***************:Confirmation is :"+confirmation,false);
        if (confirmation.trim().equals("5")) return true;
        else return false;
    }

    private  void    followUP() throws Exception {
        if (getFollowCodeIsOK())
            doFollowUP();
    }
    private  void    doFollowUP() throws Exception {
        InternalFollowUp followUpTransaction=new InternalFollowUp();
        followUpTransaction.setIsInternalFollowCode(true);
        followUpTransaction.setFollowUpCode(followCode);
        followUpTransaction.setSourceAccount(call.getAccount());
        followUpTransaction.setCallUniqID(call.getCallUniqID());
        followUpTransaction.setIsPanPayment(false);
        followUpTransaction=(InternalFollowUp)call.submitRequestToGateway(followUpTransaction);
        afterFollowUp(followUpTransaction);
    }
    private  void    afterFollowUp(InternalFollowUp followUpTransaction) throws AgiException, IOException {
        int status=-1;
        int accCode=-1;
        try{accCode=Integer.valueOf(followUpTransaction.getActionCode());}catch (Exception e){accCode=-1;}
        Util.printMessage("ActionCode followUP:"+followUpTransaction.getActionCode(),false);
       // Util.printMessage("doneflag followUP:"+followUpTransaction.getdoneFlag(),false);
        if (accCode==0){
            //if (followUpTransaction.getisValidFollowCode()){
                try{status=Integer.valueOf(followUpTransaction.getdoneFlag());}catch (Exception e){status=-1;}

                call.getParentStart().Say.sayFollowStatus(status);
           // }else ; //code pegiri vared shode dar system mojod nemibashad;
        }else if (followUpTransaction.getActionCode().equals("9006")){
            call.getParentStart().Say.noNotValid();
        }
        else call.getParentStart().Say.errorCode(followUpTransaction.getActionCode());  //call.getParentStart().Say.baArzePozesh();
    }
    private  boolean isNumber(String number){
        try{
            BigInteger n=new BigInteger(number);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }
    private  boolean getFollowCodeIsOK() throws AgiException, IOException {
        int getFollowCodeCount=0;
        boolean getFollowCodeIsOK=false;
        String confirm="";
        while ((!getFollowCodeIsOK) && (getFollowCodeCount<2)){
            followCode=call.getParentStart().Say.enterFollowCode().trim();
            if (isNumber(followCode)&&(followCode.length()!=0)&&(followCode.length()==6)){
               /* call.getParentStart().Say.shomareVaredShode();
                call.getParentStart().Say.SayPersianDigitsSeparate(followCode);
                confirm=call.getParentStart().Say.ifMobCorrect5().trim();
                if (confirm.equals("5")){
                    getFollowCodeIsOK=true;
                }else {
                    call.getParentStart().Say.namafhomAst();
                    getFollowCodeCount++;
                }*/
                getFollowCodeIsOK=true;
            }else{
                call.getParentStart().Say.noNotValid();
                getFollowCodeCount++;
            }
        }
        return getFollowCodeIsOK;
    }

    private  void    changePin2() throws Exception {
      if (getPinIsOk())
          doChangePin2();
    }
    private  void    doChangePin2() throws Exception {
        AuthenticatePin2 authenticatePin2 = new AuthenticatePin2();
        authenticatePin2.setAccountNumber(call.getAccount());
        authenticatePin2.setCallerID(call.getCallerID());
        authenticatePin2.setPin(call.getPin2());
        authenticatePin2.setPin_New(newpin);
        authenticatePin2.setDoChangePin(true);
        authenticatePin2.setCallUniqID(call.getCallUniqID());
        authenticatePin2 = (AuthenticatePin2) call.submitRequestToGateway(authenticatePin2);
        //Util.printMessage("Action Code of Changed Pin is"+authenticatePin2.getActionCode(),false);
        if (authenticatePin2.getActionCode().equals("0000")) {
            call.getParentStart().Say.pinChanged();
        } else {
            call.getParentStart().Say.errorCode(authenticatePin2.getActionCode());
            call.getParentStart().Say.pinNotChanged();
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

    private  void    transferMoneyWithID() throws Exception {

        if (getIdentityCodeIsOK()){
            if (getAccTransMoneyByIDIsOK()){
                if (getAmountTransMoneyByIDIsOK()){
                    if (ConfirmtransferMoneyWithIDIsOK()){
                        doTransferMoneyWithID();
                    }
                }
            }
        }
    }
    private  void    doTransferMoneyWithID() throws Exception {

        FundTransfer fundTransfer=new FundTransfer();
        fundTransfer.setSourceAccount(call.getAccount());
        fundTransfer.setTraceID(identCode);
        fundTransfer.setDestinationAccount(identTransferAcc);
        fundTransfer.setTransactionAmount(identTransferAmount);
        fundTransfer.setIsIdentFundTranfer(true);
        fundTransfer.setCallUniqID(call.getCallUniqID());
        fundTransfer=(FundTransfer)call.submitRequestToGateway(fundTransfer);
        doTransferMoneyResultReaction(fundTransfer);

    }
    private  boolean getIdentityCodeIsOK() throws AgiException, IOException {
        int getIdentityCodeCount=0;
        boolean getIdentityCodeIsOK=false;
        String confirm="";
        while ((!getIdentityCodeIsOK) && (getIdentityCodeCount<2)){
            identCode=call.getParentStart().Say.enterIdentCode().trim();
            if (isNumber(identCode)&&(identCode.length()!=0)){
                call.getParentStart().Say.shomareVaredShode();
                call.getParentStart().Say.SayPersianDigitsSeparate(identCode);
                confirm=call.getParentStart().Say.ifMobCorrect5().trim();
                if (confirm.equals("5")){
                    getIdentityCodeIsOK=true;
                }else {
                    //call.getParentStart().Say.namafhomAst();
                    getIdentityCodeCount++;
                }
            }else{
                call.getParentStart().Say.namafhomAst();
                getIdentityCodeCount++;
            }
        }
        return getIdentityCodeIsOK;
    }
    private  boolean getAccTransMoneyByIDIsOK() throws AgiException, IOException {
        int countOfGetAccTransMoneyByID=0;
        boolean AccTransMoneyByIDEntred=false;
        while (!AccTransMoneyByIDEntred && countOfGetAccTransMoneyByID<2){
            identTransferAcc=call.getParentStart().Say.enterDestinationAccount();
            if (identTransferAcc.trim().length()==0){
                call.getParentStart().Say.farmaniDaryaftNashod();
                countOfGetAccTransMoneyByID++;
            }else{
                BigInteger t;
                try{t=new BigInteger(identTransferAcc);}catch (Exception e){t=new BigInteger("0");}
                if (t!=new BigInteger("0")){
                    AccTransMoneyByIDEntred=true;
                }else{
                    call.getParentStart().Say.noNotValid();
                    countOfGetAccTransMoneyByID++;
                }
            }
        }
        return AccTransMoneyByIDEntred;
    }
    private  boolean getAmountTransMoneyByIDIsOK() throws AgiException, IOException {
        int countOfGetAmountTransMoneyByID=0;
        boolean amountEntred=false;
        while (!amountEntred && countOfGetAmountTransMoneyByID<2){
            identTransferAmount=call.getParentStart().Say.lotfanMablaghRaVarehNamaeid();
            if (identTransferAmount.length()==0){
                call.getParentStart().Say.farmaniDaryaftNashod();
                countOfGetAmountTransMoneyByID++;
            }else{
                BigInteger t;
                try{t=new BigInteger(identTransferAmount);}catch (Exception e){t=new BigInteger("-1");}
                if (t!=new BigInteger("-1")){
                    //Util.printMessage("entered amount:"+identTransferAcc,false);
                    amountEntred=true;
                }else{
                   // Util.printMessage("entered amount invalid:"+identTransferAcc,false);
                    call.getParentStart().Say.noNotValid();
                    countOfGetAmountTransMoneyByID++;
                }
            }
        }
        return amountEntred;
    }
    private  boolean ConfirmtransferMoneyWithIDIsOK() throws AgiException, IOException {
        String confirmation="";
        call.getParentStart().Say.mablaghe();
        call.getParentStart().Say.SayPersianDigit( identTransferAmount);
        call.getParentStart().Say.rial();
        call.getParentStart().Say.bardashVaBeHesabe();
        call.getParentStart().Say.SayPersianDigitsSeparate(identTransferAcc);
        call.getParentStart().Say.varizKhahadShod();
        confirmation=call.getParentStart().Say.ifCorrectPress5();
        if (confirmation.trim().equals("5")) return true;
        else return false;
    }

    private  void    followCheque() throws Exception {
        if (getChequeSerialIsOK())
            doFollowCheque();

    }
    private  void    doFollowCheque() throws Exception {
        ChequeStatus chequeStatus=new ChequeStatus();
        chequeStatus.setAccountNumber(call.getAccount());
        chequeStatus.setChequeNO(chequeSerial);
        chequeStatus.setCallUniqID(call.getCallUniqID());
        chequeStatus=(ChequeStatus)call.submitRequestToGateway(chequeStatus);
        afterFollowCheque(chequeStatus);

    }
    private  boolean getChequeSerialIsOK() throws AgiException, IOException {
        int getChequeSerialCount=0;
        boolean getChequeSerialIsOK=false;
        String confirm="";
        while ((!getChequeSerialIsOK) && (getChequeSerialCount<2)){
            chequeSerial=call.getParentStart().Say.enterSerialOfChequq().trim();
            Util.printMessage("cheq is:"+chequeSerial,false);
            if (isNumber(chequeSerial)&&(chequeSerial.length()!=0)){
                try{
                    chequeSerial=chequeSerial.substring(0,6);
                    getChequeSerialIsOK=true;
                }catch (Exception e)
                {
                    call.getParentStart().Say.noNotValid();
                    getChequeSerialIsOK=false;
                }

            }else{
                if (chequeSerial.length()==0){
                    call.getParentStart().Say.farmaniDaryaftNashod();
                }else  call.getParentStart().Say.noNotValid();

                getChequeSerialCount++;
            }
        }
        return getChequeSerialIsOK;
    }
    private  void    afterFollowCheque(ChequeStatus chequeStatus) throws AgiException {
        int actionCode=-1;

        String acCode=chequeStatus.getResultFromChannel().getAction_code();
        try {
            Util.printMessage("cheq action is :"+acCode,false);
        } catch (IOException e) {
            e.printStackTrace();
        }
        try{actionCode=Integer.valueOf(acCode);}catch (Exception e){actionCode=-1;}
        call.getParentStart().Say.sayChequeStatus(actionCode);
    }


    private  void inactiveAccount() throws Exception {
         this.call.setSpecialService(this);
        new Service_Account_Inactive(this.call);
    }

    private  void exit() throws AgiException {
      call.getParentStart().byAndHangUp();
    }

}
