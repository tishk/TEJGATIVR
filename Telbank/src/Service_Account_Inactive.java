import ServiceObjects.Account.BlockAccount;
import org.asteriskjava.fastagi.AgiException;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Hamid on 17/12/2015.
 */
public class Service_Account_Inactive {

    private Call call=new Call();

    private int  InactiveMenuCount=0;
    private Set  InactiveMenu=new HashSet();

    public Service_Account_Inactive(Call c) throws Exception {
        this.call=c;
        createMainMenu();
        SayMainMenu();
    }

    private  void    createMainMenu(){
        /*
        badaz mojodi
        1:Masdod nemodan Hesab telBank/mobileBank/internetBank
        2:Masdod nemodan All services
        3:return Main Menu
        9:exit
         */
        InactiveMenu.add("1");
        InactiveMenu.add("2");
        InactiveMenu.add("3");
        InactiveMenu.add("9");

    }
    public   void    SayMainMenu() throws Exception {

        String Choice=null;

        boolean EntranceIsOK=false;
        while ((InactiveMenuCount<2)) {
            Choice = call.getParentStart().Say.sayMenu(InactiveMenu,"BlockMenu_");
            Util.printMessage(Choice, false);
            if (!Choice.equals("-1")){
               // EntranceIsOK=true;
                SelectSubMenu(Choice);
            }
            else call.getParentStart().Say.namafhomAst();
            InactiveMenuCount++;
        }
        call.getParentStart().byAndHangUp();
    }
    private  void    SelectSubMenu(String Choice) throws Exception {
        switch (Choice){
            case "1":block_TB_MB_IB();
                break;
            case "2":block_All();
                break;
            case "3":returnMainMenu();
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

    private  void    block_TB_MB_IB() throws Exception {
      String choice=call.getParentStart().Say.inactive_TB_MB_IB_AreYouSure();
        if (choice.equals("5")){
            BlockAccount blockAccount=new BlockAccount();
            blockAccount.setAccountNumber(call.getAccount());
            blockAccount.setBlockMode("1");
         //   blockAccount.setMAC(call.getMACAddress());
            blockAccount.setCallUniqID(call.getCallUniqID());
            blockAccount=(BlockAccount)call.submitRequestToGateway(blockAccount);
            String actionCode=blockAccount.getResultFromChannel().getAction_code().trim();
            Util.printMessage("action code of block account is :"+actionCode,false);
            if (actionCode.equals("0000")){
                call.getParentStart().Say.accountHasBeenblock();
                call.getParentStart().Say.by();

            }

            else call.getParentStart().Say.errorCode(actionCode);
        }else{
            call.getParentStart().Say.namafhomAst();
        }
    }
    private  void    block_All() throws Exception {
        String choice=call.getParentStart().Say.inactive_All_AreYouSure();
        if (choice.equals("5")){
            BlockAccount blockAccount=new BlockAccount();
            blockAccount.setAccountNumber(call.getAccount());
            blockAccount.setBlockMode("2");
           // blockAccount.setMAC(call.getMACAddress());
            blockAccount.setCallUniqID(call.getCallUniqID());
            blockAccount=(BlockAccount)call.submitRequestToGateway(blockAccount);
            String actionCode=blockAccount.getResultFromChannel().getAction_code().trim();
            Util.printMessage("action code of block account is :"+actionCode,false);
            if (actionCode.equals("0000")){
                call.getParentStart().Say.accountHasBeenblock();
                call.getParentStart().Say.by();
            }

            else call.getParentStart().Say.errorCode(actionCode);
        }else{
            call.getParentStart().Say.namafhomAst();
        }
    }
    private  void    returnMainMenu() throws Exception {
      call.getparentAccount().SayMainMenu();
    }

    private  void    exit() throws AgiException {
        call.getParentStart().byAndHangUp();
    }

}
