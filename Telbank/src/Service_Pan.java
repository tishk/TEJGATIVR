import Mainchannel.Exceptions.InvalidParameterException;
import Mainchannel.Exceptions.ResponseParsingException;
import Mainchannel.sender.SenderException;
import ServiceObjects.Account.AccountNoOfCard;
import ServiceObjects.Account.InternalFollowUp;
import ServiceObjects.Pan.BalanceForCard;
import ServiceObjects.Pan.BlockCard;
import org.asteriskjava.fastagi.AgiException;

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

public class Service_Pan {

    private Call call=new Call();
    private int  PanMenuCount=0;
    private String pin="";
    private String followCode="";
    private boolean accessGrant=false;
    private boolean startOperationGranted=false;

    private Set PanMenu = new HashSet();
    public Service_Pan(Call c) throws Exception {
        this.call=c;

       /* if (call.getisTejaratPan()) accessGrant=authenticate();
        else accessGrant=true;
        if (accessGrant){
            createMainMenu();
            sayMainMenu();
        }*/

       // Util.printMessage("aut res:"+String.valueOf(accessGrant),false);
        if (authenticate()){
            createMainMenu();
            sayMainMenu();
        }

    }
    private  boolean authenticate() throws Exception {
        int getPassCount=0;
        boolean getPassIsOK=false;
        while ((!getPassIsOK) && (getPassCount<2)){

            pin=call.getParentStart().Say.enterPanPass().trim();

            if (isNumber(pin)&&(pin.length()!=0)) {
                if (call.getisTejaratPan()){
                    if (checkPinIsOK()) return true;
                    else getPassCount++;
                }else return true;
            }else{
                call.getParentStart().Say.noNotValid();
                getPassCount++;
            }
        }
        return false;
    }
    private  boolean checkPinIsOK() throws Exception {


            BalanceForCard balanceForCard=new BalanceForCard();
            balanceForCard.setPan(call.getPan());
            balanceForCard.setPin(pin);
            balanceForCard.setCallUniqID(call.getCallUniqID());
            balanceForCard=(BalanceForCard)call.submitRequestToGateway(balanceForCard);
            call.setResult(balanceForCard);
            if (balanceForCard.getActionCode().equals("0000")) return true;
            else {
                call.getParentStart().Say.errorCode(balanceForCard.getActionCode());
                return false;
            }

    }
    private  boolean getPinIsOK() throws AgiException, IOException, ResponseParsingException, SenderException, InvalidParameterException {
        int getPassCount=0;
        boolean getPassIsOK=false;
        while ((!getPassIsOK) && (getPassCount<2)){
            pin=call.getParentStart().Say.enterPanPass().trim();
            if (isNumber(pin)&&(pin.length()!=0)) {
                getPassIsOK = true;
            }else{
                call.getParentStart().Say.noNotValid();
                getPassCount++;
            }
        }
        return getPassIsOK;
    }
    private  boolean isNumber(String number){
        try{
            BigInteger n=new BigInteger(number);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }

    private  void createMainMenu(){
        /*
        1:Say Last Balance Of Pan
        2:Bill PaymentOBJ
        3:Follow Bill PaymentOBJ
        4:Block Pan
        7:get Account No of Pan
        9:bazgasht
         */
        if (call.getisTejaratPan()) PanMenu.add("1");
        PanMenu.add("2");
        PanMenu.add("3");
        if (call.getisTejaratPan()) PanMenu.add("4");
        if (call.getisTejaratPan()) PanMenu.add("7");
        PanMenu.add("9");

    }
    public   void sayMainMenu() throws Exception {

        String Choice=null;

        boolean EntranceIsOK=false;
        while ((PanMenuCount<3)) {
            Choice = call.getParentStart().Say.sayMenu(PanMenu,"Sh115_");
            if (!Choice.equals("-1"))  SelectSubMenu(Choice);
            else call.getParentStart().Say.namafhomAst();
            PanMenuCount++;
        }
        call.getParentStart().byAndHangUp();
    }
    private  void SelectSubMenu(String Choice) throws Exception {
        switch (Choice){
            case "1":sayLastTransaction();
                break;
            case "2":billPayment();
                break;
            case "3":followBillPayment();
                break;
            case "4":blockPan();
                break;
            case "7":getAccountNoOfPan();
                break;
            case "9":call.getParentStart().byAndHangUp();
                break;
            default:
                call.getParentStart().Say.namafhomAst();
                break;

        }
    }

    private  void sayLastTransaction() throws Exception {

        if (!call.getisTejaratPan()) startOperationGranted=getPinIsOK();else startOperationGranted=true;
        if (startOperationGranted){
            BalanceForCard balanceForCard=new BalanceForCard();
            balanceForCard.setPan(call.getPan());
            balanceForCard.setPin(pin);
            balanceForCard.setCallUniqID(call.getCallUniqID());
            balanceForCard=(BalanceForCard)call.submitRequestToGateway(balanceForCard);
            call.setResult(balanceForCard);
            String balance="";
            if (balanceForCard.getActionCode().equals("0000")){
                balance=balanceForCard.getResultFromServer().getActualBalance();
                call.setBalance(balance);
                call.getParentStart().Say.mojodieHesabeShoma();
                call.getParentStart().Say.SayPersianDigit(balance);
                call.getParentStart().Say.rial();
                call.getParentStart().Say.mibashad();
            }
            else call.getParentStart().Say.errorCode(balanceForCard.getActionCode());
        }
    }

    private  void billPayment() throws Exception {
        //if (!call.getisTejaratPan()) startOperationGranted=getPinIsOK();else startOperationGranted=true;
        startOperationGranted=true;
        if (startOperationGranted) {
            call.setPin(pin);
             call.setparentPan(this);
            Service_BillPayment service_billPayment= new Service_BillPayment(call, true);
            service_billPayment=null;
        }
    }

    private  void    followBillPayment() throws Exception {
        //if (!call.getisTejaratPan()) startOperationGranted=getPinIsOK();else startOperationGranted=true;
        startOperationGranted=true;
        if (startOperationGranted) followUP();
    }
    private  void    followUP() throws Exception {
        if (getFollowCodeIsOK())
            doFollowUP();
    }
    private  void    doFollowUP() throws Exception {
        InternalFollowUp followUpTransaction=new InternalFollowUp();
        followUpTransaction.setIsInternalFollowCode(true);
        followUpTransaction.setSourceAccount(call.getPan());
        followUpTransaction.setFollowUpCode(followCode);
        followUpTransaction.setCallUniqID(call.getCallUniqID());
        followUpTransaction.setIsPanPayment(true);
        followUpTransaction=(InternalFollowUp)call.submitRequestToGateway(followUpTransaction);
        afterFollowUp(followUpTransaction);
    }
    private  void    afterFollowUp(InternalFollowUp followUpTransaction) throws AgiException, IOException {
        int status=-1;
        if (!followUpTransaction.getActionCode().equals("0000")){
            if (followUpTransaction.getisValidFollowCode()){
                try{status=Integer.valueOf(followUpTransaction.getdoneFlag());}catch (Exception e){status=-1;}
                call.getParentStart().Say.sayFollowStatus(status);
            }else ; //code pegiri vared shode dar system mojod nemibashad;
        }else call.getParentStart().Say.baArzePozesh();
    }
    private  boolean getFollowCodeIsOK() throws AgiException, IOException {
        int getFollowCodeCount=0;
        boolean getFollowCodeIsOK=false;
        String confirm="";
        while ((!getFollowCodeIsOK) && (getFollowCodeCount<2)){
            followCode=call.getParentStart().Say.enterFollowCode().trim();
            if (isNumber(followCode)&&(followCode.length()!=0)&&(followCode.length()==6)){
                call.getParentStart().Say.shomareVaredShode();
                call.getParentStart().Say.SayPersianDigitsSeparate(followCode);
                confirm=call.getParentStart().Say.ifMobCorrect5().trim();
                if (confirm.equals("5")){
                    getFollowCodeIsOK=true;
                }else {

                    //call.getParentStart().Say.namafhomAst();
                    getFollowCodeCount++;
                }
            }else{
                call.getParentStart().Say.noNotValid();
                getFollowCodeCount++;
            }
        }
        return getFollowCodeIsOK;
    }

    private  void blockPan() throws Exception{

            String confirm=call.getParentStart().Say.cardBlockedIfAreYouSurePress5().trim();
            if (confirm.equals("5")){
                BlockCard blockCard=new BlockCard();
                blockCard.setPan(call.getPan());
                blockCard.setPin(pin);
                blockCard.setCallUniqID(call.getCallUniqID());
                blockCard=(BlockCard)call.submitRequestToGateway(blockCard);
                Util.printMessage("action code of card block is:"+blockCard.getActionCode(),false );
                if (blockCard.getActionCode().equals("0000")) call.getParentStart().Say.panBlocked();
                else call.getParentStart().Say.errorCode(blockCard.getActionCode());
            }


    }

    private  void getAccountNoOfPan() throws Exception {
        AccountNoOfCard accountNoOfCard=new AccountNoOfCard();
        accountNoOfCard.setCardNo(call.getPan());
        accountNoOfCard=(AccountNoOfCard) call.submitRequestToGateway(accountNoOfCard);
        String actionCode=accountNoOfCard.getResultFromChannel().getAction_code();

        Util.printMessage("actionCode is account of pan is:"+actionCode,false);
        if (actionCode.equals("0000")){
            try{
                long temp=Long.valueOf(accountNoOfCard.getResultFromChannel().getCfsAccNo());
                accountNoOfCard.getResultFromChannel().setCfsAccNo(String.valueOf(temp));
            }catch (Exception e){}
            call.getParentStart().Say.accountOfPanIs();
            call.getParentStart().Say.SayPersianDigitsSeparate(accountNoOfCard.getResultFromChannel().getCfsAccNo());
        }else call.getParentStart().Say.errorCode(actionCode);
    }
}
