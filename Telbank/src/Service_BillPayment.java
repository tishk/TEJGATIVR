import ServiceObjects.Account.BillPayByIDAccount;
import ServiceObjects.Other.BillInfoByTelNumber;
import ServiceObjects.Other.BillPaySayByTelNumber;
import ServiceObjects.Pan.BillPayByBillIDPan;
import org.asteriskjava.fastagi.AgiException;

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Administrator on 11/17/2015.
 */
public class Service_BillPayment {

    private Call  call=new Call();
    private int    billPayMenuCount=0;
    private BillInfoByTelNumber   billInfoByTelNumber=new BillInfoByTelNumber();
    private BillPaySayByTelNumber billPaySayByTelNumber=new BillPaySayByTelNumber();
    private BillPayByIDAccount billPayByIDAccount=new BillPayByIDAccount();
    private BillPayByBillIDPan billPayByIDPan=new BillPayByBillIDPan();
    private Set    billPayMenu=new HashSet();
    private boolean isPan=false;
    private boolean isTrueTellNumber(String number){
        try{

            BigInteger n=new BigInteger(number);
            if ((telNumber.length()<10)||(telNumber.length()>11)) return false;

            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }
    private boolean isNumber(String number){
        try{
            //&&(telNumber.length()>10)
            BigInteger n=new BigInteger(number);
            return true;
        }catch (NumberFormatException e){
            return false;
        }
    }
    private boolean isMobile(){
        try{
            String prefix=telNumber.substring(0,2);
            if (prefix.equals("09")) return true;
            else return false;

        }catch (Exception e){
            return false;
        }
    }
    private strUtils strutils=new strUtils();


    private String billID="";
    private String payID="";
    private String amount="";

    private String telNumber="";
    private String transferAmount="";
    private String actionCode="";


    public Service_BillPayment(Call c,boolean isPan) throws Exception {
        this.isPan=isPan;
        call=c;
       // billPayForAccount_();
        createBillPayMenu();
        SayBillPayMenu();
    }

    private  void createBillPayMenu(){
        /*
        badaz mojodi
        1:ba shenase ghabz
        2:ba shomare telephone
        9:bazgasht
         */
        billPayMenu.add("1");
        billPayMenu.add("2");
        billPayMenu.add("9");
    }
    private  void SayBillPayMenu() throws Exception {

        String Choice=null;
        billPayMenuCount=0;
        boolean EntranceIsOK=false;
        while ((billPayMenuCount<3)) {
            Choice = call.getParentStart().Say.sayMenu(billPayMenu,"GhabzMenu_");
            if (!Choice.equals("-1")){

                SelectSubMenu(Choice);
               // break;
            }
            else call.getParentStart().Say.namafhomAst();
            billPayMenuCount++;
        }
        if (!isPan) call.getSpecialService().SayMainMenu();
        else call.getparentPan().sayMainMenu();
    }
    private  void SelectSubMenu(String Choice) throws Exception {
        switch (Choice){
            case "1":billPayByID();
                billPayMenuCount=4;
                break;
            case "2":billPayByTelNumber();
                billPayMenuCount=4;
                break;
            case "9"://nothing yet
                //billPayMenuCount=4;
                break;
            case "-1":call.getParentStart().Say.namafhomAst();
                break;
            default:
                call.getParentStart().Say.namafhomAst();
                break;
        }
    }

    private  void    billPayByTelNumber() throws Exception {

        if (getTelNumberIsOK())
           if (getBillInfoByTelNumberIsOK())
              if (confirmTelDataIsOK())
                  if (checkDigitOfBillUsingTelNumberIsOK())
                      if (balanceIsEnough())
                          doBillPay();
    }
    private  void    billPayByID() throws Exception {
        if (getBillIDIsOK())
            if (getPaymentIDIsOK())
                if (confirmBillDataIsOK())
                    if (checkDigitIsOK())
                        if (balanceIsEnough())
                            doBillPay();
    }
    private  void    doBillPay() throws Exception {
        if (isPan){
            Util.printMessage("in do bill pay",false);
            billPayForPan();
            afterBillPayforPan();
        }
        else {
            billPayForAccount();
            afterBillPayforAccount();
        }


    }
    private  void    billPayForAccount_() throws Exception {

        billPayByIDAccount.setSourceAccount(call.getAccount());
        billPayByIDAccount.setBillID("9488346204120");
        billPayByIDAccount.setPaymentID("15540267");
        billPayByIDAccount.setAmount("155000");
        billPayByIDAccount.setCallUniqID(call.getCallUniqID());
        billPayByIDAccount= (BillPayByIDAccount)call.submitRequestToGateway(billPayByIDAccount);
        actionCode=billPayByIDAccount.getActionCode();
        Util.printMessage("bill pay ac code:"+billPayByIDAccount.getActionCode(),false);
        afterBillPayforAccount();
    }
    private  void    billPayForAccount() throws Exception {

        billPayByIDAccount.setSourceAccount(call.getAccount());
        billPayByIDAccount.setBillID(billID);
        billPayByIDAccount.setPaymentID(payID);
        billPayByIDAccount.setAmount(amount);
        billPayByIDAccount.setCallUniqID(call.getCallUniqID());
        billPayByIDAccount= (BillPayByIDAccount)call.submitRequestToGateway(billPayByIDAccount);
        actionCode=billPayByIDAccount.getActionCode();

    }
    private  void    billPayForPan() throws Exception {
        billPayByIDPan.setPan(call.getPan());
        billPayByIDPan.setPin(call.getPin());
        billPayByIDPan.setBillID(billID);
        billPayByIDPan.setPaymentID(payID);
        billPayByIDPan.setAmount(amount);
        billPayByIDPan.setCallUniqID(call.getCallUniqID());
        billPayByIDPan= (BillPayByBillIDPan)call.submitRequestToGateway(billPayByIDPan);
        actionCode=billPayByIDPan.getActionCode();

    }

    private  void    doSuccessOperationForAccount() throws Exception {
        int intActionCode=Integer.valueOf(actionCode);
        switch (intActionCode){
            case 0:
                nowBillPayHappened();
                break;
            case 1000:
                nowBillPayHappened();
                break;
            case 2000:
                earlyBillPayWillHappen();
                break;
            default:
                break;
        }
    }
    private  void    doSuccessOperationForPan() throws Exception {
        nowBillPayHappenedForPAN(); ;
    }
    private  void    nowBillPayHappened() throws Exception {
        int i=0;
        call.getParentStart().Say.paySuccess();
        call.getParentStart().Say.shomarePeygireiShma();
        call.getParentStart().Say.SayPersianDigitsSeparate(billPayByIDAccount.getTraceCode());
        call.getParentStart().Say.mibashad();
       /* billPaySayByTelNumber.setPan(call.getAccount());
        billPaySayByTelNumber.setTraceCode(billPayByIDAccount.getActionCode());
        billPaySayByTelNumber.setReferenceCode(billPayByIDAccount.getReferenceCode());
        billPaySayByTelNumber.setBillID(billID);
        billPaySayByTelNumber.setPaymentID(payID);
        billPaySayByTelNumber.setIsMobile(isMobile());
        billPaySayByTelNumber.setPayDate(billPayByIDAccount.getResultFromCM().getTxDate());
        billPaySayByTelNumber.setTelNo(telNumber);
        while (i++<3){
            billPaySayByTelNumber=(BillPaySayByTelNumber)call.submitRequestToGateway(billPaySayByTelNumber);
            if (billPaySayByTelNumber.getActionCode().equals("0000")){
               // call.getParentStart().Say.paySuccess();
               // call.getParentStart().Say.shomarePeygireiShma();
               // call.getParentStart().Say.SayPersianDigitsSeparate(billPayByIDAccount.getTraceCode());
              //  call.getParentStart().Say.mibashad();
                break;
            }
        }*/
    }
    private  void    nowBillPayHappenedForPAN() throws Exception {
        int i=0;
        call.getParentStart().Say.paySuccess();
        call.getParentStart().Say.shomarePeygireiShma();
        call.getParentStart().Say.SayPersianDigitsSeparate(billPayByIDPan.getTraceCode());
        call.getParentStart().Say.mibashad();
       /* billPaySayByTelNumber.setPan(call.getAccount());
        billPaySayByTelNumber.setTraceCode(billPayByIDAccount.getActionCode());
        billPaySayByTelNumber.setReferenceCode(billPayByIDAccount.getReferenceCode());
        billPaySayByTelNumber.setBillID(billID);
        billPaySayByTelNumber.setPaymentID(payID);
        billPaySayByTelNumber.setIsMobile(isMobile());
        billPaySayByTelNumber.setPayDate(billPayByIDAccount.getResultFromCM().getTxDate());
        billPaySayByTelNumber.setTelNo(telNumber);
        while (i++<3){
            billPaySayByTelNumber=(BillPaySayByTelNumber)call.submitRequestToGateway(billPaySayByTelNumber);
            if (billPaySayByTelNumber.getActionCode().equals("0000")){
               // call.getParentStart().Say.paySuccess();
               // call.getParentStart().Say.shomarePeygireiShma();
               // call.getParentStart().Say.SayPersianDigitsSeparate(billPayByIDAccount.getTraceCode());
              //  call.getParentStart().Say.mibashad();
                break;
            }
        }*/
    }
    private  void    earlyBillPayWillHappen() throws AgiException, IOException {
        call.getParentStart().Say.bardashVaVarizKhahadShode();
        call.getParentStart().Say.shomarePeygireiShma();
        call.getParentStart().Say.SayPersianDigitsSeparate(billPayByIDAccount.getTraceCode());
        call.getParentStart().Say.mibashad();
    }
    private  boolean afterBillPayforAccount() throws Exception {
        int accIntCode=-1;
        Util.printMessage("action code is:"+actionCode,false);
        try{accIntCode=Integer.valueOf(actionCode);}catch (Exception e){}
        switch (accIntCode){
            case   -1:call.getParentStart().Say.baArzePozesh();
            case 0000:
                doSuccessOperationForAccount();
                return true;
            case 1000:
                doSuccessOperationForAccount();
                return true;
            case 2000:
                doSuccessOperationForAccount();
                return true;
            case 3004:
                call.getParentStart().Say.sorryDoNotPay();
                break;
            case 9010:
            case 9011:
                call.getParentStart().Say.shenaseMotabarNist();
                break;
            case 9012:
                call.getParentStart().Say.shenaseMotabarNist();
                break;
            case 9013:
                call.getParentStart().Say.shenaseMotabarNist();
                break;
            case 9016:
                call.getParentStart().Say.accountNotRegistered();
                break;
            case 9014:
                call.getParentStart().Say.billPayedEarly();
                break;
            case 9001:
                call.getParentStart().Say.accountNotValid();
                break;
            case 9009:
                call.getParentStart().Say.accountNotValid();
                break;
            case 9500:
                call.getParentStart().Say.saghfPorAst();
                break;
            case 9300:
                call.getParentStart().Say.accountNotRegistered();
                break;
            case 9700:
                call.getParentStart().Say.baArzePozesh();// Baz kardan hesab bestankar faal nist
                break;
            case 9999:
                call.getParentStart().Say.baArzePozesh();//Conection to server Fail
                break;
            case 7777:
                call.getParentStart().Say.baArzePozesh();//Conection to server Fail
                break;
            case 9126:
                call.getParentStart().Say.baArzePozesh();
                break;
            default:
                call.getParentStart().Say.errorCode(actionCode);
                break;
        }
        return false;
    }
    private  boolean afterBillPayforPan() throws Exception {
        int accIntCode=-1;
        try{accIntCode=Integer.valueOf(actionCode);}catch (Exception e){accIntCode=-1;}
        Util.printMessage("actionCode:"+String.valueOf(accIntCode),false);
        switch (accIntCode){
            case   -1:call.getParentStart().Say.baArzePozesh();
            case 0:
                doSuccessOperationForPan();
                return true;
            case 9010:
            case 9011:
                call.getParentStart().Say.shenaseMotabarNist();
                break;
            case 9012:
                call.getParentStart().Say.shenaseMotabarNist();
                break;
            case 9013:
                call.getParentStart().Say.shenaseMotabarNist();
                break;
            case 9016:
                call.getParentStart().Say.accountNotRegistered();
                break;
            case 9014:
                call.getParentStart().Say.billPayedEarly();
                break;
            case 9001:
                call.getParentStart().Say.accountNotValid();
                break;
            case 9009:
                call.getParentStart().Say.accountNotValid();
                break;
            case 9500:
                call.getParentStart().Say.saghfPorAst();
                break;
            case 9300:
                call.getParentStart().Say.accountNotRegistered();
                break;
            case 9700:
                call.getParentStart().Say.baArzePozesh();// Baz kardan hesab bestankar faal nist
                break;
            case 9999:
                call.getParentStart().Say.baArzePozesh();//Conection to server Fail
                break;
            case 7777:
                call.getParentStart().Say.baArzePozesh();//Conection to server Fail
                break;
            case 9126:
                call.getParentStart().Say.baArzePozesh();
                break;
            default:
                call.getParentStart().Say.errorCode(actionCode);
                break;
        }
        return false;
    }
    private  boolean getTelNumberIsOK() throws AgiException, IOException {
        int getTelCount=0;
        boolean getTelIsOK=false;
        String confirm="";
        while ((!getTelIsOK) && (getTelCount<2)){
            telNumber=call.getParentStart().Say.enterTelNumber().trim();
            if (isTrueTellNumber(telNumber)){
                call.getParentStart().Say.shomareTelephoneVaredShode();
                if (isMobile()){
                    call.getParentStart().Say.SayMobileNo(telNumber);
                }else{
                    call.getParentStart().Say.SayPersianDigitsSeparate(telNumber);
                }

                confirm=call.getParentStart().Say.ifMobCorrect5().trim();
                if (confirm.equals("5")){
                    getTelIsOK=true;
                }else {
                   // call.getParentStart().Say.namafhomAst();
                    getTelCount++;
                }
            }else{
                call.getParentStart().Say.noNotValid();
                getTelCount++;
            }
        }
        return getTelIsOK;
    }
    private  boolean getBillInfoByTelNumberIsOK() throws Exception {

        if (isPan) billInfoByTelNumber.setPan(call.getPan());
        else billInfoByTelNumber.setPan(call.getAccount());
        billInfoByTelNumber.setIsMobile(isMobile());
        billInfoByTelNumber.setTelNo(telNumber);
        call.getParentStart().Say.pleaseWait();
        billInfoByTelNumber.setCallUniqID(call.getCallUniqID());
        billInfoByTelNumber= (BillInfoByTelNumber) call.submitRequestToGateway(billInfoByTelNumber);
        if (billInfoByTelNumber.getActionCode().equals("0000")){
             return true;
        }else{
            call.getParentStart().Say.errorCode(billInfoByTelNumber.getActionCode());
            return false;
        }
    }
    private  boolean confirmTelDataIsOK() throws AgiException, IOException {
        String confirmation="";
        call.getParentStart().Say.mablaghe();
        call.getParentStart().Say.SayPersianDigit(billInfoByTelNumber.getResultFromServer().getAmount());
       // call.getParentStart().Say.rial();
        call.getParentStart().Say.rialBabateGhabze();
        call.getParentStart().Say.telephone();
        call.getParentStart().Say.bardashtKhahadshod();
        confirmation=call.getParentStart().Say.ifCorrectPress5();
        if (confirmation.trim().equals("5")) return true;
        else return false;
    }
    private  boolean balanceIsEnough() throws AgiException, IOException {
        if (isPan) return true;
        if (Long.valueOf(call.getBalance())>Long.valueOf(amount)+Long.valueOf("50000")){
            return true;
        }
        else {
            call.getParentStart().Say.balanceNotEnaugh();
            return false;
        }
    }
    private  boolean checkDigitOfBillUsingTelNumberIsOK() throws AgiException {
        boolean Result = false;
        billID=billInfoByTelNumber.getResultFromServer().getBillID();
        payID =billInfoByTelNumber.getResultFromServer().getPayID();
        amount =billInfoByTelNumber.getResultFromServer().getAmount();
        String  tempAmount=String.valueOf(Long.valueOf(payID.substring(0, payID.length() - 5) + "000"));
        byte Result1;
        if(isTrueNumber(billID.substring(billID.length() - 2, billID.length() - 1))) {
            if(checkDigit(billID.length(), billID, 0)) {
                if(checkDigit(payID.length(), payID, 1)) {
                    String Temp = billID+payID;
                    if(checkDigit(Temp.length(), Temp, 0)) {
                        if(amount.equals("0")||amount.equals(tempAmount)) {
                            actionCode = "0000";//OK
                            Result=true;
                        } else {
                            actionCode = "9013";//Money Error
                        }
                    } else {
                        actionCode = "9012";//PaymentId And BillId Not Matched Erorr
                    }
                } else {
                    actionCode = "9011"; //PaymentId Error
                }
            } else {
                actionCode = "9010";//BillId Error
            }
        } else {
            actionCode = "9015";//Service Not Found
        }
        if (!actionCode.equals("0000")) call.getParentStart().Say.errorCode(actionCode);
        return Result;
    }
    private  boolean isTrueNumber(String s) {
        int i=-1;
        try{i=Integer.valueOf(s);}catch (NumberFormatException e){ return false;}
        if (i==0) return false;
        else return true;
    }
    private  boolean checkDigit(int Len, String D, int Type) {
        int Sum = 0;
        int j = 2;

        try {
            int First = Integer.parseInt(D.substring(Len - Type - 1, Len - Type));

            for(int i = Len - 1 - Type; i >= 1; --i) {
                String N = D.substring(i - 1, i);
                int S = Integer.parseInt(N) * j;
                Sum += S;
                ++j;
                if(j == 8) {
                    j = 2;
                }
            }

            int R = Sum % 11;
            if(R != 0 && R != 1) {
                int var15 = 11 - R;
                if(var15 == First) {
                    return true;
                } else {
                    return false;
                }
            } else {
                byte Digit = 0;
                if(Digit == First) {
                    return true;
                } else {
                    return false;
                }
            }
        } catch (StringIndexOutOfBoundsException var14) {
            return false;
        }catch (NumberFormatException e){
            return false;
        }
    }
    private  boolean getBillIDIsOK() throws AgiException, IOException {
        int getBillIDCount=0;
        boolean getBillIDIsOK=false;
        String confirm="";
        while ((!getBillIDIsOK) && (getBillIDCount<2)){
            billID=call.getParentStart().Say.enterBillID().trim();
            if (isNumber(billID)&&(billID.length()!=0)&&(billID.length()==13)){

                if (checkDigit(billID.length(),billID,0)){
                    Util.printMessage("check digit passed",false);
                    getBillIDIsOK=true;
                }else{
                    call.getParentStart().Say.noNotValid();
                    getBillIDCount++;
                }
            }else{
                call.getParentStart().Say.noNotValid();
                getBillIDCount++;
            }
        }
        return getBillIDIsOK;
    }
    private  boolean getPaymentIDIsOK() throws AgiException, IOException {
        int getPaymentIDCount=0;
        boolean getPaymentIDIsOK=false;
        String confirm="";
        while ((!getPaymentIDIsOK) && (getPaymentIDCount<2)){
            payID=call.getParentStart().Say.enterPaymentID().trim();
            if (isNumber(payID)&&(payID.length()!=0)){
                if (checkDigit(payID.length(),payID,1)){
                    try{ amount=String.valueOf(Long.valueOf(payID.substring(0, payID.length() - 5) + "000"));}catch (Exception e){amount="0";}
                    getPaymentIDIsOK=true;
                }else{
                    call.getParentStart().Say.noNotValid();
                    getPaymentIDCount++;
                }
            }else{
                call.getParentStart().Say.noNotValid();
                getPaymentIDCount++;
            }
        }
        return getPaymentIDIsOK;
    }
    private  boolean confirmBillDataIsOK() throws AgiException, IOException {
        String confirmation="";

        call.getParentStart().Say.mablaghe();
        call.getParentStart().Say.SayPersianDigit(amount);
       // call.getParentStart().Say.rial();
        call.getParentStart().Say.rialBabateGhabze();
        call.getParentStart().Say.sayBillKind(getBillType());
        call.getParentStart().Say.vaShenaseGhabze();
        call.getParentStart().Say.SayPersianDigitsSeparate(billID);
        call.getParentStart().Say.bardashtKhahadshod();
        confirmation=call.getParentStart().Say.ifCorrectPress5();
        if (confirmation.trim().equals("5")) return true;
        else return false;
    }
    private  boolean checkDigitIsOK() throws AgiException, IOException {
        boolean Result = false;
        byte Result1;
        if(isTrueNumber(billID.substring(billID.length() - 2, billID.length() - 1))) {
            if(checkDigit(billID.length(), billID, 0)) {
                if(checkDigit(payID.length(), payID, 1)) {

                    String Temp = billID+payID;
                    if(checkDigit(Temp.length(), Temp, 0)) {
                            actionCode = "0000";//OK
                            Result=true;

                    } else {
                        actionCode = "9012";//PaymentId And BillId Not Matched Erorr
                    }
                } else {
                    actionCode = "9011"; //PaymentId Error
                }
            } else {
                actionCode = "9010";//BillId Error
            }
        } else {
            actionCode = "9015";//Service Not Found
        }
        if (!actionCode.equals("0000")) call.getParentStart().Say.errorCode(actionCode);
        Util.printMessage("final check digit is : "+actionCode,false);
        return Result;
    }
    private  int     getBillType(){
        int res=-1;
        String t= strutils.midString(billID,billID.length()-1,1);
        try{res=Integer.valueOf(t);}catch (Exception e){res=-1;}
        return res;
    }

}
