import ServiceObjects.Other.SMSAlarmTransaction;
import org.asteriskjava.fastagi.AgiException;

import java.io.IOException;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.Set;

/**
 * Created by Administrator on 11/25/2015.
 */
public class Service_Account_SMS {
    public  static Util util = new Util();
    private Call call=new Call();
    private int  MainMenuCount=0;
    private String newMob="";

    private Set SMSMenu = new HashSet();
    public Service_Account_SMS(Call c) throws Exception {
        this.call=c;
        createMainMenu();
        sayMainMenu();
    }

    private  void createMainMenu(){
        /*
        1:taghir shomare mobile
        2:gheyre faal kardan
        3:bazgasht
         */
        SMSMenu.add("1");
        SMSMenu.add("2");
        SMSMenu.add("3");

    }
    private  void sayMainMenu() throws Exception {

        String Choice=null;

        boolean EntranceIsOK=false;
        while ((MainMenuCount<2)) {
            Choice = call.getParentStart().Say.sayMenu(SMSMenu,"Sms_");
           // Util.ShowMessage(Choice);
            if (!Choice.equals("-1")){

                SelectSubMenu(Choice);
            }
            else call.getParentStart().Say.namafhomAst();
            MainMenuCount++;
        }
        call.getParentStart().byAndHangUp();
    }
    private  void SelectSubMenu(String Choice) throws Exception {
        switch (Choice){
            case "1":changeMobile();
                break;
            case "2":inactiveSMS();
                break;
            case "3":returnToMainMenu();
                break;
            default:
                call.getParentStart().Say.namafhomAst();
                break;

        }
    }

    private  boolean getMobileIsOK() throws AgiException, IOException {
        int getMobCount=0;
        boolean getMobisok=false;
        String ifCorrect="";
        boolean mobIsNumber=false;

        while (!getMobisok && getMobCount<2){
            newMob=call.getParentStart().Say.enterNewMob();
            if (!newMob.equals("")){
                try{
                  new BigInteger(newMob);
                  mobIsNumber=true;
                }catch (NumberFormatException e){
                  mobIsNumber=false;
                }
               if ((newMob.length()==11) && (newMob.substring(0,2).equals("09"))&&(mobIsNumber)){
                   call.getParentStart().Say.shomareMobVaredShode();
                   call.getParentStart().Say.SayPersianDigitsSeparate(newMob);
                   ifCorrect=call.getParentStart().Say.ifMobCorrect5();
                   if (ifCorrect.equals("5")){
                       getMobisok=true;
                   }else{
                       call.getParentStart().Say.reEnterMob();
                       getMobCount++;
                   }
               }else{
                   call.getParentStart().Say.mobNotOK();
                   call.getParentStart().Say.reEnterMob();
                   getMobCount++;
               }
            }else{
                call.getParentStart().Say.reEnterMob();
                getMobCount++;
            }
        }
        return getMobisok;
    }

    private  void changeMobile() throws Exception {
        if (getMobileIsOK()){
            SMSAlarmTransaction smsAlarmTransaction=new SMSAlarmTransaction();
            smsAlarmTransaction.setAccountNumber(call.getAccount());
            smsAlarmTransaction.setMobileNumber(newMob);
            smsAlarmTransaction.setIsSetMobileNumber(true);
           // smsAlarmTransaction.setMAC(call.getMACAddress());
            smsAlarmTransaction.setCallUniqID(call.getCallUniqID());
            smsAlarmTransaction=(SMSAlarmTransaction)call.submitRequestToGateway(smsAlarmTransaction);
            if (smsAlarmTransaction.getActionCode().equals("0000")){
                call.getParentStart().Say.activatedSMSService();
            }else{
                call.getParentStart().Say.notActivatedSMSService();
            }
        }
    }

    private  void inactiveSMS() throws Exception {
        if (getMobileIsOK()){
            SMSAlarmTransaction smsAlarmTransaction=new SMSAlarmTransaction();
            smsAlarmTransaction.setAccountNumber(call.getAccount());
            smsAlarmTransaction.setMobileNumber(newMob);
            smsAlarmTransaction.setIsSetMobileNumber(false);
           // smsAlarmTransaction.setMAC(call.getMACAddress());
            smsAlarmTransaction.setCallUniqID(call.getCallUniqID());
            smsAlarmTransaction=(SMSAlarmTransaction)call.submitRequestToGateway(smsAlarmTransaction);
            if (smsAlarmTransaction.getActionCode().equals("0000")){
                call.getParentStart().Say.inActivatedSMSService();
            }else{
                call.getParentStart().Say.baArzePozesh();
            }
        }
    }

    private  void returnToMainMenu() throws Exception {
     call.getparentAccount().SayMainMenu();
    }
}
