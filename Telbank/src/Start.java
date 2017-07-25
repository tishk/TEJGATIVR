import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.BaseAgiScript;

import java.io.IOException;


public class Start extends BaseAgiScript {

    public Start(){
        try {
          // Util.ShowMessage("in start telbank");
           //this.statusConnectionToServer=status;
           StartTelBank();
        } catch (Exception e){

        }
    }
    private int statusConnectionToServer=-5;
    public static   class   IVRThread extends Thread {
        private Call call=new Call();;
        private  String  inputByUser=null;
        boolean isCard=false;
        boolean isAccount=false;
        boolean isNotEnterAny=false;
        int counterOfGetMenu=0;
        Util.PersianDateTime persianDateTime= new Util.PersianDateTime();
        public Voices Say=new  Voices();
        IVRThread(Call C) throws Exception {

       //  startTest(C);
         startThis(C);
          //  Util.printMessage(Util.ivrPath+Util.FaxFile,false);
        //  startThisTest(C);

        }
        private  void    greeting() throws AgiException, InterruptedException, IOException {
            inputByUser= Say.vaghtBekheyrAndEnter(getTimeInDay());
            //inputByUser="3435531082";
            if (inputByUser.length()==0){
                isCard=false;
                isAccount=false;
                isNotEnterAny=true;
            }
            else if (inputByUser.equals("123456")){

                    Util.printMessage("before test",false);
                    new testClass();
                    return;


            }
            else if (inputByUser.length()==16){

                call.setUserEntrance(inputByUser);
                isCard=true;
                isAccount=false;
                isNotEnterAny=false;
            }
            else if (inputByUser.length()<=13){
                isCard=false;
                //Util.printMessage("Entered acc:"+inputByUser,false);
                call.setUserEntrance(inputByUser);
                isAccount=true;
                isNotEnterAny=false;
            }
            else{
              //  Util.printMessage("Entered acc:"+inputByUser,false);
                isCard=false;
                isAccount=false;
                isNotEnterAny=false;
            }


        }
        private  void    getAccountOrCard(boolean noEnterPrev) throws AgiException, IOException {
           inputByUser="";
            if (noEnterPrev) inputByUser=Say.enterAccountNumber();
            else   inputByUser=Say.accountNotValidreenter();


            if (inputByUser.length()==0){
                isCard=false;
                isAccount=false;
                isNotEnterAny=true;
            }
            else if (inputByUser.length()==16){
                call.setUserEntrance(inputByUser);
                isCard=true;
                isAccount=false;
                isNotEnterAny=false;
            }
            else if (inputByUser.length()<=13){
                isCard=false;
                call.setUserEntrance(inputByUser);
                isAccount=true;
                isNotEnterAny=false;
            }
            else{
                isCard=false;
                isAccount=false;
                isNotEnterAny=false;
            }

        }
        private  int     getTimeInDay(){
            int hourInDate=persianDateTime.GetTimeInDate();
            int monthInYear=persianDateTime.GetMonthInYear();
            if (monthInYear<7){
                if (hourInDate<12) return 0;
                else if (hourInDate<16) return 1;
                else if (hourInDate<20) return 2;
                else if (hourInDate<24) return 3;
                else return 0;
            }else{
                if (hourInDate<12) return 0;
                else if (hourInDate<15) return 1;
                else if (hourInDate<19) return 2;
                else if (hourInDate<24) return 3;
                else return 0;
            }
        }
        private  void    byAndHangUp() throws AgiException{
            System.gc();
            Say.by();
        }
        private  void    processInvalidEntry() throws AgiException, IOException {

             getAccountOrCard(false);

        }
        private  void    processNoEntry() throws AgiException, IOException {
                getAccountOrCard(true);
        }
        private  void    processThisTelBank() throws Exception {

            //greeting();
            do{
               // Util.printMessage("EnteredUser:"+inputByUser,false);
                //counterOfGetMenu++;
                if (isAccount||isCard){
                    call.setUserEntrance(inputByUser);
                    new Service_Start(call);
                    break;
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
               // getAccountOrCard();
            }while (counterOfGetMenu++<3);

            byAndHangUp();
        }
        private  void    sorry() throws AgiException {
            Say.baArzePozesh();
            byAndHangUp();
        }
        private  void    startThis(Call C) throws Exception {
            call = C;
            call.getGateway();
            inputByUser = "";
            Util.printMessage("Call start for Caller ID :" + call.getCallerID(), false);


            if (call.connectedToGateway){
               greeting();
               processThisTelBank();
            }else sorry();
        }
        private  void    startThisTest(Call C) throws Exception {
            call=C;
            call.getGateway();
            Util.printMessage("Call start for Caller ID :" + call.getCallerID(), false);
            if (call.connectedToGateway){
                //inputByUser="4150645501";
               // Say.SayPersianDigitsSeparate("17");
             //   inputByUser="3435531082";
               // inputByUser="3512512541";
             //   isAccount=true;
             //   processThisTelBank();
                new Service_Account_Fax(call);
            }else sorry();
        }
        private  void    startTest(Call C ) {
            call = C;
            try {
                call.getGateway();
                inputByUser = "";
                Util.printMessage("Call start for Caller ID :" + call.getCallerID(), false);
                call.setAccount("4150645501");
                testClass testclass=new testClass(call);

                testclass.faxMenu();
            } catch (Exception e) {
                e.printStackTrace();
            }


        }

    }

    public   void    StartTelBank() throws Exception {
        answer();


        new IVRThread(InitializeCall());


    }
    private  Call    InitializeCall() throws Exception {
        return new Call(){{
                setUniQID(getUniqueId());
                setChannelName(getName());
                setCallerID(getFullVariable("${CALLERID(num)}", getUniqueId()));
                setDateOfCall(Util.persianDateTime.GetNowDate());
                setTimeOfCall(Util.persianDateTime.GetNowTime());
                setCallUniqID();
        }};
    }
    @Override
    public   void    service(AgiRequest agiRequest, AgiChannel agiChannel) throws AgiException {

    }

}
