//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package ProjectClasses;

import ProjectClasses.CallObject;
import ProjectClasses.Connection;
import ProjectClasses.Global;
import ProjectClasses.Message;
import ProjectClasses.Sounds;
import ProjectClasses.Connection.SocketConnection;
import ProjectClasses.Global.CardIdentityBankObJ;
import ProjectClasses.Global.Command;
import ProjectClasses.Global.SettingsOBJ;
import ProjectClasses.Global.Variables;
import ProjectClasses.Message.SocketMessage;
import ProjectClasses.Message.SocketMessage.Billpayment_Follew;
import ProjectClasses.Message.SocketMessage.Billpayment_Pay;
import ProjectClasses.Message.SocketMessage.Card;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;

import com.sun.jndi.cosnaming.CNCtx;
import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;

public class Operations {
    Operations.LOG Log = new Operations.LOG();
    Variables Var;
    Command Command;
    SettingsOBJ SettingOBJ;
    Sounds Say;
    SocketConnection ConnectToServer;
    SocketMessage RequestToServer;
    public boolean isTejarat=false;
    public String GetPinCardExceptTejarat() throws AgiException, IOException {
        //this.log("Get Pin Code");
        String PinCard = Operations.this.Say.GetPinCard();
        return PinCard;
    }

    public Operations() {
        this.Var = new Global().new Variables();
        this.Command = new Global().new Command();
        this.SettingOBJ = new Global().new SettingsOBJ();
        this.Say = new Sounds();
        this.ConnectToServer = new Connection().new SocketConnection();
        this.RequestToServer = new Message().new SocketMessage();
    }

    public void service(AgiRequest request, AgiChannel channel) throws AgiException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public class BlockCard {
        CallObject Call = new CallObject();

        public BlockCard() {
        }

        private void log(String S) throws FileNotFoundException, IOException {
            Operations.this.Log.Log(this.Call, S);
        }

        private void SendReqToServer() throws FileNotFoundException, IOException, UnsupportedEncodingException, InterruptedException, AgiException {

            Message.SocketMessage.BlockCard C = new Message().new SocketMessage().new BlockCard();
            this.Call = C.SubmitMSGForCard(this.Call);
            this.Call = C.GetMSGFromServer(this.Call);
            this.Call = C.MSGReceiveProcess(this.Call);
            this.log("request for block card sent to server");
        }

        private void proccessActionCode(CallObject Calll) throws AgiException, FileNotFoundException, IOException {
            if(Calll.Action_ConnectedToGateway) {
                File file = new File("//var//lib//asterisk//sounds//persian//ActionCode//" + Calll.Action_Code + ".wav");
                if(!file.exists()) {
                    Operations.this.Say.PlayFile("persian/ActionCode/9900");
                } else {
                    Operations.this.Say.PlayFile("persian/ActionCode/" + Calll.Action_Code);
                    if("8888".equals(Calll.Action_Code)) {
                        Operations.this.Say.SayBy();
                    }

                    if("5078".equals(Calll.Action_Code)) {
                        Operations.this.Say.SayBy();
                    }

                    if("0000".equals(Calll.Action_Code)) {
                        Operations.this.Say.SayBy();
                    }
                }
            } else {
                Operations.this.Say.Answer_NotAnswer();
            }

        }

        public CallObject Satrt(CallObject C) throws AgiException, IOException, InterruptedException {
            this.Call = C;
            this.log("Start of blocking Card Proccess");
            String Confrime = Operations.this.Say.BlockCard_Alarm();
            if(Integer.parseInt(Confrime) == 5) {
                this.SendReqToServer();
                this.log("Card ");
            }

            this.proccessActionCode(this.Call);
            return this.Call;
        }
    }

    public class PlayCash {
        CallObject Call = new CallObject();

        public PlayCash() {
        }

        private void log(String S) throws FileNotFoundException, IOException {
            Operations.this.Log.Log(this.Call, S);
        }

        public void playcach(CallObject C) throws UnsupportedEncodingException, AgiException, IOException, FileNotFoundException, InterruptedException {
            this.Call = C;
            this.log("System play cash");
            String Cash = Integer.toString(Integer.parseInt(this.Call.Cash_reachable));
            Operations.this.Say.Mojodie();
            Operations.this.Say.SayPersianDigit(Cash);
            Operations.this.Say.Rial();
            Operations.this.Say.Mibashad();
        }
    }

    public class LOG {
        public LOG() {
        }

        public void Log(CallObject Call, String LogStr) throws UnsupportedEncodingException, FileNotFoundException, IOException {
            new Operations.LOG.LogThread(Call, LogStr);
        }

        public class LogThread extends Thread {
            CallObject COBJect = new CallObject();
            String StringToLog = "";
            private String CurrentLogFile;

            LogThread(CallObject Call, String LogString) throws FileNotFoundException, IOException {
                this.COBJect = Call;
                this.StringToLog = LogString;
                this.Log_Main();
            }

            private boolean Log_AddedToLogfile(String S) {
                try {
                    PrintWriter i = new PrintWriter(new BufferedWriter(new FileWriter(this.CurrentLogFile, true)));
                    Throwable SS = null;

                    try {
                        i.println(this.COBJect.DateTimeOfCall + " :" + S);
                    } catch (Throwable var13) {
                        SS = var13;
                        throw var13;
                    } finally {
                        if(i != null) {
                            if(SS != null) {
                                try {
                                    i.close();
                                } catch (Throwable var12) {
                                    SS.addSuppressed(var12);
                                }
                            } else {
                                i.close();
                            }
                        }

                    }
                } catch (IOException var15) {
                    ;
                }

                int var16 = S.length();
                var16 = 20 - var16;
                String var17 = "";

                for(int j = 0; j <= var16; ++j) {
                    var17 = var17 + " ";
                }

                System.out.println("-->" + this.COBJect.DateTimeOfCall + " :" + S + var17 + " CID:" + this.COBJect.CallerID + "&" + "ID:" + this.COBJect.UniqueID);
                return true;
            }

            private boolean Log_ExsistFile() throws FileNotFoundException, UnsupportedEncodingException {
                File file = new File(this.CurrentLogFile);
                return !file.exists()?true:true;
            }

            public void Log_CreateLogFile() throws FileNotFoundException, UnsupportedEncodingException {
                File file = new File(this.COBJect.LogPath + this.COBJect.DateOfCall + "//" + this.COBJect.TimeOfCall + "_" + "_" + this.COBJect.CallerID + "_" + this.COBJect.UniqueID);
                if(!file.exists()) {
                    try {
                        PrintWriter e = new PrintWriter(this.COBJect.LogPath + this.COBJect.DateOfCall + "//" + this.COBJect.TimeOfCall + "_" + "_" + this.COBJect.CallerID + "_" + this.COBJect.UniqueID, "UTF-8");
                        Object var3 = null;
                        if(e != null) {
                            if(var3 != null) {
                                try {
                                    e.close();
                                } catch (Throwable var7) {
                                    ((Throwable)var3).addSuppressed(var7);
                                }
                            } else {
                                e.close();
                            }
                        }
                    } catch (IOException var8) {
                        ;
                    }
                }

                this.CurrentLogFile = file.toString();
            }

            private boolean Log_ExsistFolder() {
                File file = new File(this.COBJect.LogPath + this.COBJect.DateOfCall);
                return !file.exists()?file.mkdir():true;
            }

            private void Log_Failed(String S) {
                System.out.println("-->" + this.COBJect.TimeOfCall + " : " + S + " : I Can not Log the Calls Please Help me!");
            }

            public boolean Log_Main() throws UnsupportedEncodingException, FileNotFoundException, IOException {
                this.Log_CreateLogFile();
                if(this.Log_ExsistFolder()) {
                    if(this.Log_ExsistFile()) {
                        if(this.Log_AddedToLogfile(this.StringToLog)) {
                            return true;
                        } else {
                            this.Log_Failed("Can not ADD to file ");
                            return false;
                        }
                    } else {
                        this.Log_Failed(" LogFile not Exist ");
                        return false;
                    }
                } else {
                    this.Log_Failed(" LogFolder not Exist ");
                    return false;
                }
            }
        }
    }

    public class Login {
        CallObject Call = new CallObject();
        public boolean isTejaratCard=false;
        public Login() {
        }

        private void log(String S) throws FileNotFoundException, IOException {
            Operations.this.Log.Log(this.Call, S);
        }

        public void SendReqToServer() throws FileNotFoundException, IOException, UnsupportedEncodingException, InterruptedException, AgiException {
            Card C = new Message().new SocketMessage().new Card();
            this.Call = C.SubmitMSGForCard(this.Call);
            this.log(this.Call.SendString);
            this.Call = C.GetMSGFromServer(this.Call);
            this.log(this.Call.ReceiveString);
            this.Call = C.MSGReceiveProcess(this.Call);
        }

        public String GetPinCard() throws AgiException, IOException {
            this.log("Get Pin Code");
            String PinCard = Operations.this.Say.GetPinCard();
            return PinCard;
        }

        public String FirstGetCardNumber() throws AgiException, IOException {
            this.log("Get Card number");
            String CardNumber = Operations.this.Say.FirstGetCardNumber();
            this.log("Entered Number is : " + CardNumber);
            return CardNumber;
        }

        public String GetCardNumber() throws AgiException, IOException {
            this.log("Get Card number");
            String CardNumber = Operations.this.Say.GetCardNumber();
            this.log("Entered Number is : " + CardNumber);
            return CardNumber;
        }

        public boolean CardNumberOK_(String CNumber) {
            if(CNumber.length() >= 16 && CNumber.length() <= 19) {
                String firstDigits = CNumber.substring(0, 6);

                CardIdentityBankObJ CardNCheck = Command.LoadSetting();
                if(CardNCheck.Ayandeh1.equals(firstDigits)) {
                    CardNCheck.TrueCardNumber = "true";
                } else if(CardNCheck.Ayandeh2.equals(firstDigits)) {
                    CardNCheck.TrueCardNumber = "true";
                } else if(CardNCheck.EghtesadNovin.equals(firstDigits)) {
                    CardNCheck.TrueCardNumber = "true";
                } else if(CardNCheck.EtebariToseh.equals(firstDigits)) {
                    CardNCheck.TrueCardNumber = "true";
                } else if(CardNCheck.Karafarin.equals(firstDigits)) {
                    CardNCheck.TrueCardNumber = "true";
                } else if(CardNCheck.Keshavarzi.equals(firstDigits)) {
                    CardNCheck.TrueCardNumber = "true";
                } else if(CardNCheck.Maskan.equals(firstDigits)) {
                    CardNCheck.TrueCardNumber = "true";
                } else if(CardNCheck.Mehr.equals(firstDigits)) {
                    CardNCheck.TrueCardNumber = "true";
                } else if(CardNCheck.Mellat.equals(firstDigits)) {
                    CardNCheck.TrueCardNumber = "true";
                } else if(CardNCheck.Melli.equals(firstDigits)) {
                    CardNCheck.TrueCardNumber = "true";
                } else if(CardNCheck.Parsian1.equals(firstDigits)) {
                    CardNCheck.TrueCardNumber = "true";
                } else if(CardNCheck.Parsian2.equals(firstDigits)) {
                    CardNCheck.TrueCardNumber = "true";
                } else if(CardNCheck.PostBank.equals(firstDigits)) {
                    CardNCheck.TrueCardNumber = "true";
                } else if(CardNCheck.Refah.equals(firstDigits)) {
                    CardNCheck.TrueCardNumber = "true";
                } else if(CardNCheck.Saman.equals(firstDigits)) {
                    CardNCheck.TrueCardNumber = "true";
                } else if(CardNCheck.SanatoMadan.equals(firstDigits)) {
                    CardNCheck.TrueCardNumber = "true";
                } else if(CardNCheck.Sarderat.equals(firstDigits)) {
                    CardNCheck.TrueCardNumber = "true";
                } else if(CardNCheck.Sarmayeh.equals(firstDigits)) {
                    CardNCheck.TrueCardNumber = "true";
                } else if(CardNCheck.Sepah.equals(firstDigits)) {
                    CardNCheck.TrueCardNumber = "true";
                } else if(CardNCheck.Sina.equals(firstDigits)) {
                    CardNCheck.TrueCardNumber = "true";
                } else if(CardNCheck.Tejarat.equals(firstDigits)) {
                    CardNCheck.TrueCardNumber = "true";
                } else if(CardNCheck.Tejarat2.equals(firstDigits)) {
                    CardNCheck.TrueCardNumber = "true";
                } else if(CardNCheck.TosehSaderat.equals(firstDigits)) {
                    CardNCheck.TrueCardNumber = "true";
                }
                isTejarat=false;
                isTejaratCard=false;
                System.out.println("########################## :"+firstDigits);
                if("627353".equals(firstDigits.trim())) {
                    isTejarat=true;
                    isTejaratCard=true;
                    CardNCheck.TrueCardNumber = "true";
                }
                if("585983".equals(firstDigits.trim())) {
                    isTejarat=true;
                    isTejaratCard=true;
                    CardNCheck.TrueCardNumber = "true";
                }

                return Boolean.valueOf(CardNCheck.TrueCardNumber).booleanValue();
            } else {
                return false;
            }
        }
        private boolean isTejaratPan(String pan) {
            if (pan.substring(0,6).equals("585983")||pan.substring(0,6).equals("627353")) return true;
            else return false;
        }
        public boolean CardNumberOK(String CNumber) {
            if (checkDigitPanIsOK(CNumber)){
               if (isTejaratPan(CNumber)){
                   isTejaratCard=true;
               }else{
                   isTejaratCard=false;
               }
                return true;
            }else{
                return false;
            }

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


        public void CardNumberNotOK() throws AgiException, IOException {
            this.log("Card Number is invalid !");
            Operations.this.Say.CardNumberNotOK();
        }

        public boolean PinCardOK(String CardNumber, String PinNumber) throws IOException, FileNotFoundException, InterruptedException, AgiException {

            this.log("Card number is : " + CardNumber + "  pincardnumber is : " + PinNumber);
            if (PinNumber.length()<4) return false;
            try{
                Long.valueOf(PinNumber);

            }catch (Exception e){
                return false;
            }
            this.Call.CardPin = PinNumber;
                if (isTejaratCard){
                    try {
                    this.SendReqToServer();
                    int ex = Integer.parseInt(this.Call.Action_Code);
                    if(ex == 0) {
                        this.log("PinCard is OK !");
                    } else {
                        this.proccessActionCode(this.Call);
                    }

                    return ex == 0;
                } catch (NumberFormatException var4) {
                    return false;
                }
                }else return true;
        }

        public void PinCardNotOK() throws AgiException, IOException {
            this.log("Pin Card is invalid !");
            Operations.this.Say.PinCardNotOK();
        }

        public CallObject login(CallObject CallOBJ) throws AgiException, IOException, FileNotFoundException, UnsupportedEncodingException, InterruptedException {
            this.Call = CallOBJ;
            int C = 0;

            for(int J = 0; C <= 2; ++C) {
                String CardNumber;
                if(C == 0) {
                    CardNumber = this.FirstGetCardNumber();
                } else {
                    CardNumber = this.GetCardNumber();
                }

                if(!this.CardNumberOK(CardNumber)) {
                    this.CardNumberNotOK();
                } else {
                    if (isTejaratCard){
                        for(this.Call.CardNumber = CardNumber; J <= 2; ++J) {
                            String CardPin = this.GetPinCard();
                            if(this.PinCardOK(CardNumber, CardPin)) {
                                this.Call.Action_Login = true;
                                Call.isTejaratCard=true;
                                return this.Call;
                            }

                            this.PinCardNotOK();

                        }


                    }else{
                        String CardPin = this.GetPinCard();
                        if(this.PinCardOK(CardNumber, CardPin)) {

                            Call.isTejaratCard=false;
                            this.Call.Action_Login = true;
                            return this.Call;
                        }
                    }

                    if(J > 2) {
                        this.Call.Action_Login = false;
                        return this.Call;
                    }
                }
            }

            this.Call.Action_Login = false;
            return this.Call;
        }

        private void proccessActionCode(CallObject Calll) throws AgiException, FileNotFoundException, IOException {
            if(Calll.Action_ConnectedToGateway) {
                this.log("Action code from Server : " + Calll.Action_Code);
                File file = new File("//var//lib//asterisk//sounds//persian//ActionCode//" + Calll.Action_Code + ".wav");
                if(!file.exists()) {
                    Operations.this.Say.PlayFile("persian/ActionCode/9900");
                } else {
                    Operations.this.Say.PlayFile("persian/ActionCode/" + Calll.Action_Code);
                    if("8888".equals(Calll.Action_Code)) {
                        Operations.this.Say.SayBy();
                    }

                    if("5078".equals(Calll.Action_Code)) {
                        Operations.this.Say.SayBy();
                    }

                    if("5057".equals(Calll.Action_Code)) {
                        Operations.this.Say.SayBy();
                    }
                }
            } else {
                Operations.this.Say.Answer_NotAnswer();
            }

        }
    }

    public class BillPayment_follow {
        CallObject Call = new CallObject();

        public BillPayment_follow() {
        }

        private void log(String S) throws FileNotFoundException, IOException {
            Operations.this.Log.Log(this.Call, S);
        }

        public CallObject Start(CallObject C) throws AgiException, IOException, FileNotFoundException, InterruptedException {
            this.Call = C;
            this.log("Follow Bill Payment is started");

                String BIDF = this.GetBillFolowCode();
                if(!"-1".equals(BIDF)) {
                    boolean pinEntered=false;
                    if (!Call.isTejaratCard) {
                       /* for (int cc = 0; cc <= 2; ++cc) {
                            String CardPin = GetPinCardExceptTejarat();
                            if (!CardPin.trim().equals("")) {
                                Call.CardPin = CardPin;
                                pinEntered = true;
                                break;
                            } else {
                                Operations.this.Say.InvalidEntry();
                            }

                        }*/
                        pinEntered=true;

                    }else{
                        pinEntered=true;
                    }
                    if (pinEntered) {
                    this.Call.Followcode = BIDF;
                    this.SendReqToServer();
                    this.BillPayFollowProccessActionCode(this.Call);
                    }
            }


            return this.Call;
        }

        public String GetBillFolowCode() throws AgiException, IOException {
            String BFollow = "-1";
            int c = 0;

            while(c <= 2) {
                BFollow = Operations.this.Say.G_VorodeShomarePeygiri();
                this.log("Entered Follow Code ID : " + BFollow);
                if(this.CheckBillFollowCode(BFollow)) {
                    Operations.this.Say.SayPersianDigitsSeperate(BFollow);
                    if("5".equals(Operations.this.Say.IfCorrect())) {
                        return BFollow;
                    }
                } else {
                    Operations.this.Say.CardNumberNotOK();
                    this.log("Entered Follow Code ID : " + BFollow);
                    ++c;
                }
            }

            return BFollow;
        }

        public void SendReqToServer() throws FileNotFoundException, IOException, UnsupportedEncodingException, InterruptedException {

            Billpayment_Follew C = new Message().new SocketMessage().new Billpayment_Follew();
            this.Call = C.SubmitMSGBillpayment(this.Call);
            this.Call = C.GetMSGFromServer(this.Call);
            this.Call = C.MSGReceiveProcess(this.Call);
        }

        public boolean IsNumber(String S) {
            try {
                for(int ex = 0; ex < S.length(); ++ex) {
                    int D = Integer.parseInt(Character.toString(S.charAt(ex)));
                }

                return true;
            } catch (NumberFormatException var4) {
                return false;
            }
        }

        public boolean CheckBillFollowCode(String FollowCode) {
            return !this.IsNumber(FollowCode)?false:FollowCode.length() == 6;
        }

        public boolean IN(String S) {
            byte var3 = -1;
            switch(S.hashCode()) {
                case 49:
                    if(S.equals("1")) {
                        var3 = 0;
                    }
                    break;
                case 50:
                    if(S.equals("2")) {
                        var3 = 1;
                    }
                    break;
                case 51:
                    if(S.equals("3")) {
                        var3 = 2;
                    }
                    break;
                case 52:
                    if(S.equals("4")) {
                        var3 = 3;
                    }
                    break;
                case 53:
                    if(S.equals("5")) {
                        var3 = 4;
                    }
                    break;
                case 54:
                    if(S.equals("6")) {
                        var3 = 5;
                    }
            }

            switch(var3) {
                case 0:
                    return true;
                case 1:
                    return true;
                case 2:
                    return true;
                case 3:
                    return true;
                case 4:
                    return true;
                case 5:
                    return true;
                default:
                    return false;
            }
        }

        public void BillPayFollowProccessActionCode(CallObject Calll) throws AgiException {
            if(Calll.Action_ConnectedToGateway) {
                int Status = Integer.parseInt(Calll.FollowStatus);
                switch(Status) {
                    case 0:
                        Operations.this.Say.PlayFile("persian/ActionCode/33");
                        break;
                    case 1:
                        Operations.this.Say.PlayFile("persian/ActionCode/32");
                        break;
                    case 2:
                        Operations.this.Say.PlayFile("persian/ActionCode/34");
                        break;
                    case 3:
                        Operations.this.Say.PlayFile("persian/ActionCode/64");
                        break;
                    case 4:
                        Operations.this.Say.PlayFile("persian/ActionCode/58");
                        break;
                    case 5:
                        Operations.this.Say.PlayFile("persian/ActionCode/59");
                }
            } else {
                Operations.this.Say.Answer_NotAnswer();
            }

        }
    }

    public class BillPayment {
        CallObject Call = new CallObject();

        public BillPayment() {
        }

        private void log(String S) throws FileNotFoundException, IOException {
            Operations.this.Log.Log(this.Call, S);
        }

        public CallObject Start(CallObject C) throws AgiException, IOException, FileNotFoundException, InterruptedException {

            Billpayment_Pay BilPAY = new Message().new SocketMessage().new Billpayment_Pay();
            this.Call = C;
            this.log("Bill ID Opertion Started");
            int j = 0;

            while(j < 2) {


                    String ShG = this.GetShanaseGhabz();
                    if(Long.parseLong(ShG) != 0L) {
                        this.Call.ShenaseGhabz = ShG;
                        String ShP = this.GetShenasePardakht();
                        if(Long.parseLong(ShP) != 0L) {
                            this.Call.ShenasePardakht = ShP;
                            Operations.this.Say.Mablaghe();
                            Operations.this.Say.SayPriceGhabz(ShP);
                            Operations.this.Say.G_RialBabatewGhabze();
                            Operations.this.Say.SaykindOfShenase(ShG);
                            Operations.this.Say.G_VaBaShenase();
                            Operations.this.Say.SayPersianDigitsSeperate(ShG);
                            if("5".equals(Operations.this.Say.IfCorrect())) {
                                boolean pinEntered=false;
                                if (!Call.isTejaratCard) {
                                    /*for (int cc = 0; cc <= 2; ++cc) {
                                        String CardPin = GetPinCardExceptTejarat();
                                        if (!CardPin.trim().equals("")) {
                                            Call.CardPin = CardPin;
                                            pinEntered = true;
                                            break;
                                        } else {
                                            Operations.this.Say.InvalidEntry();
                                        }

                                    }*/
                                    pinEntered=true;

                                }else{
                                    pinEntered=true;
                                }
                                if (pinEntered) {
                                    this.Call = BilPAY.SubmitMSGBillpayment(this.Call);
                                    this.Call = BilPAY.GetMSGFromServer(this.Call);
                                    this.Call = BilPAY.MSGReceiveProcess(this.Call);
                                    this.BillPayProccessActionCode(this.Call);
                                    break;
                                }
                            }

                            ++j;
                        } else {
                            this.Call.ShenasePardakht = "-1";
                            ++j;
                        }
                    } else {
                        this.Call.ShenaseGhabz = "-1";
                        this.Call.ShenasePardakht = "-1";
                        ++j;
                    }

                }



            return this.Call;
        }

        private String GetShanaseGhabz() throws AgiException, IOException {
            int C = 0;

            String SHGH;
            for(SHGH = "0"; C < 1; ++C) {
                SHGH = Operations.this.Say.G_VorodeSheneseGhabz();
                this.log("Entered Bill ID : " + SHGH);
                if(this.CheckShenaseOK(SHGH)) {
                    return SHGH;
                }

                this.log("Entered Bill ID Not OK! : " + SHGH);
                Operations.this.Say.G_ShenaseGhabzNotOK();
                SHGH = "0";
            }

            return SHGH;
        }

        private String GetShenasePardakht() throws AgiException, IOException {
            int C = 0;

            String SHP;
            for(SHP = "0"; C < 1; ++C) {
                SHP = Operations.this.Say.G_VorodeShenasePardakht();
                if(this.CheckShenasePardakhtOK(SHP)) {
                    this.log("Entered Pay ID : " + SHP);
                    return SHP;
                }

                this.log("Entered Pay ID Not OK : " + SHP);
                Operations.this.Say.G_ShenasePardakhtNotOK();
                SHP = "0";
            }

            return SHP;
        }

        public boolean CheckDigit(int Len, String D, int Type) {
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
            } catch (StringIndexOutOfBoundsException | NumberFormatException var14) {
                return false;
            }
        }

        private boolean IsNumber(String S) {
            Boolean Result = Boolean.valueOf(true);
            if(S == null) {
                Result = Boolean.valueOf(false);
            } else {
                try {
                    for(int ex = 0; ex < S.length(); ++ex) {
                        int D = Integer.parseInt(Character.toString(S.charAt(ex)));
                    }
                } catch (StringIndexOutOfBoundsException | NumberFormatException var5) {
                    Result = Boolean.valueOf(false);
                }
            }

            return Result.booleanValue();
        }

        private boolean CheckShenaseOK(String Shenase) {
            Boolean Result = Boolean.valueOf(false);
            if(!this.IsNumber(Shenase)) {
                Result = Boolean.valueOf(false);
            } else if(this.CheckDigit(Shenase.length(), Shenase, 0)) {
                Result = Boolean.valueOf(true);
            }

            return Result.booleanValue();
        }

        private boolean CheckShenasePardakhtOK(String shenaseP) {
            return this.IsNumber(shenaseP);
        }

        public int CheckDigitOK(String BillID, String PayID) {
            String Money = Integer.toString(Integer.parseInt(PayID.substring(0, PayID.length() - 5) + "000"));
            boolean Result = false;
            byte Result1;
            if(this.IN(BillID.substring(BillID.length() - 2, BillID.length() + 1))) {
                if(this.CheckDigit(BillID.length(), BillID, 0)) {
                    if(this.CheckDigit(PayID.length(), PayID, 1)) {
                        String Temp = Integer.toString(Integer.parseInt(BillID)) + Integer.toString(Integer.parseInt(PayID));
                        if(this.CheckDigit(Temp.length(), Temp, 0)) {
                            if("0".equals(Money)) {
                                Result1 = 0;
                            } else {
                                Result1 = -5;
                            }
                        } else {
                            Result1 = -4;
                        }
                    } else {
                        Result1 = -3;
                    }
                } else {
                    Result1 = -2;
                }
            } else {
                Result1 = -1;
            }

            return Result1;
        }

        private boolean IN(String S) {
            byte var3 = -1;
            switch(S.hashCode()) {
                case 49:
                    if(S.equals("1")) {
                        var3 = 0;
                    }
                    break;
                case 50:
                    if(S.equals("2")) {
                        var3 = 1;
                    }
                    break;
                case 51:
                    if(S.equals("3")) {
                        var3 = 2;
                    }
                    break;
                case 52:
                    if(S.equals("4")) {
                        var3 = 3;
                    }
                    break;
                case 53:
                    if(S.equals("5")) {
                        var3 = 4;
                    }
                    break;
                case 54:
                    if(S.equals("6")) {
                        var3 = 5;
                    }
            }

            switch(var3) {
                case 0:
                    return true;
                case 1:
                    return true;
                case 2:
                    return true;
                case 3:
                    return true;
                case 4:
                    return true;
                case 5:
                    return true;
                default:
                    return false;
            }
        }

        public void BillPayProccessActionCode(CallObject Calll) throws AgiException, IOException {
            if(Calll.Action_ConnectedToGateway) {
                File file = new File("//var//lib//asterisk//sounds//persian//ActionCode//" + Calll.Action_Code + ".wav");
                if("9014".equals(Calll.Action_Code.trim())) {
                    this.log("Bill ID Has Payed!");
                    Operations.this.Say.G_InGhabzDarTarikhe();
                    Operations.this.Say.SayDate(Calll.PayDate);
                    Operations.this.Say.G_VaBAShomarePeygiri();
                    Operations.this.Say.SayPersianDigitsSeperate(Calll.CodePeygiri);
                    Operations.this.Say.G_PardakhtShode();
                } else if("0000".equals(Calll.Action_Code.trim())) {
                    this.log("Bill ID Pay Done Successfull!");
                    Operations.this.Say.G_PayOK();
                    Operations.this.Say.G_ShomarePeygiri();
                    Operations.this.Say.SayPersianDigitsSeperate(Calll.CodePeygiri);
                    Operations.this.Say.Mibashad();
                } else if(!file.exists()) {
                    this.log("Unnown Error");
                    Operations.this.Say.PlayFile("persian/ActionCode/9900");
                } else {
                    this.log("Error with Action Code : " + Calll.Action_Code);
                    Operations.this.Say.PlayFile("persian/ActionCode/" + Calll.Action_Code);
                }
            } else {
                Operations.this.Say.Answer_NotAnswer();
            }

        }
    }
}
