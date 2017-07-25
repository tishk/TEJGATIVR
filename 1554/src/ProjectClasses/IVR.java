//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package ProjectClasses;

import ProjectClasses.CallObject;
import ProjectClasses.Global;
import ProjectClasses.Operations;
import ProjectClasses.Sounds;
import ProjectClasses.Global.Command;
import ProjectClasses.Global.DateClass;
import ProjectClasses.Operations.BillPayment;
import ProjectClasses.Operations.BillPayment_follow;
import ProjectClasses.Operations.BlockCard;
import ProjectClasses.Operations.LOG;
import ProjectClasses.Operations.Login;
import ProjectClasses.Operations.PlayCash;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.parsers.ParserConfigurationException;
import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.BaseAgiScript;
import org.xml.sax.SAXException;

public class IVR extends BaseAgiScript {
    public IVR() {
    }

    public   String getCallID() {
        String seq;

            seq = String.valueOf(System.nanoTime()).substring(0,12);//144525438988
            seq="1554"+seq.substring(1);

        return seq;
    }

    public void service(AgiRequest request, AgiChannel channel) throws AgiException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public String SettProjectPath() {
        File f = new File(Global.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        return f.toString();
    }

    public void AsteriskStart() throws AgiException, UnsupportedEncodingException, FileNotFoundException, IOException, ParserConfigurationException, SAXException, InterruptedException {

        DateClass D = new Global().new DateClass();

        CallObject CaLL = new CallObject();
        this.answer();
        CaLL.UniqueID = this.getUniqueId();
        CaLL.ChanName = this.getName();
        CaLL.CallerID = this.getFullVariable("${CALLERID(num)}", CaLL.ChanName);
        CaLL.DateOfCall = D.Shamsi_Date();
        CaLL.TimeOfCall = D.GetNowTime();
        CaLL.DateTimeOfCall = CaLL.DateOfCall + " " + CaLL.TimeOfCall;
        CaLL.ProjectPath = this.SettProjectPath();
        CaLL.LogPath = CaLL.ProjectPath + "//Log//";
        CaLL.SettingPath = CaLL.ProjectPath + "//Settings//Settings.xml";
        CaLL.callUniqID=getCallID();
        new IVR.IVRTHread(CaLL);
    }

    public class IVRTHread extends Thread {
        LOG LogOBJ;
        Sounds Say;
        CallObject Call;
        Command C;
        boolean isTejarat=false;

        IVRTHread(CallObject C) throws ParserConfigurationException, SAXException, InterruptedException, IOException, UnsupportedEncodingException, AgiException {

            this.LogOBJ = new Operations().new LOG();
            this.Say = new Sounds();
            this.Call = new CallObject();
            this.C = new Global().new Command();
            this.Call = C;

            try {
                if(this.login().Action_Login) {

                    this.log("Logined in System");
                    this.GoMainMenu();
                    this.HaltIVRLine();
                } else {
                    this.proccessActionCode(this.Call.Action_Code);
                }
            } catch (UnsupportedEncodingException var4) {
                Logger.getLogger(IVR.class.getName()).log(Level.SEVERE, (String)null, var4);
            } catch (FileNotFoundException var5) {
                Logger.getLogger(IVR.class.getName()).log(Level.SEVERE, (String)null, var5);
            } catch (AgiException | IOException var6) {
                Logger.getLogger(IVR.class.getName()).log(Level.SEVERE, (String)null, var6);
            }

        }

        public IVRTHread() {
            this.LogOBJ = new Operations().new LOG();
            this.Say = new Sounds();
            this.Call = new CallObject();
            this.C = new Global().new Command();
            throw new UnsupportedOperationException("Not supported yet.");
        }

        public void run() {
        }

        private void log(String S) throws FileNotFoundException, IOException {
            this.LogOBJ.Log(this.Call, S);
        }

        private int GetUserMenuSelection() throws UnsupportedEncodingException, FileNotFoundException, IOException, AgiException, InterruptedException {
            boolean IntSelectedMenu = true;
            String SelectedMenu = "";
            this.log("Paly MainMenu");
            if (isTejarat) SelectedMenu = this.Say.MainMenu();
            else SelectedMenu = this.Say.MainMenuIsNotTejaratCard();

            int IntSelectedMenu1;
            try {
                SelectedMenu = SelectedMenu.substring(0, 1);
                IntSelectedMenu1 = Integer.parseInt(SelectedMenu);
                this.log("Selection Menu is: " + SelectedMenu);
            } catch (StringIndexOutOfBoundsException | NumberFormatException var4) {
                IntSelectedMenu1 = -1;
                this.log("Not Selected Menu");
            }

            return IntSelectedMenu1;
        }

        private void SendFax(String UQID) throws AgiException, UnsupportedEncodingException, FileNotFoundException, IOException {
            this.Say.SatrtFax();
            this.log("System tries FAX");
            IVR.this.exec("sendfax", "/Fax/" + UQID + ".tif");
            this.log("FAX Sent");
        }

        private void By() throws UnsupportedEncodingException, FileNotFoundException, IOException, AgiException {
            this.log("User exited");
            this.Say.SayBy();
        }

        private void InvalidEntry() throws AgiException, UnsupportedEncodingException, FileNotFoundException, IOException {
            this.Say.InvalidEntry();
            this.log("Invalid Entry !");
        }

        private void HaltIVRLine() throws UnsupportedEncodingException, FileNotFoundException, IOException, AgiException {
            this.log("The interaction ended");
            this.log("END");
            IVR.this.hangup();
        }

        private void GoMainMenu() throws UnsupportedEncodingException, FileNotFoundException, AgiException, IOException, InterruptedException {
            int MainMenuCounter = 0;

            while(true) {
                label29:
                while(MainMenuCounter <= 1) {
                    switch(this.GetUserMenuSelection()) {
                        case -1:
                            ++MainMenuCounter;
                            break;
                        case 0:
                        case 5:
                        case 6:
                        case 8:
                        default:
                            this.InvalidEntry();
                            ++MainMenuCounter;
                            break;
                        case 1:
                            if (isTejarat) this.playcash();
                            else InvalidEntry();
                            break;
                        case 2:
                            this.BillPayment_Pay();
                            break;
                        case 3:
                            this.BillPayment_Follow();
                            break;
                        case 4:
                            if (isTejarat) this.BlockCard();
                            else InvalidEntry();
                            break;
                        case 7:
                            int ii = 0;

                            while(true) {
                                if(ii >= 20) {
                                    continue label29;
                                }

                                this.Say.test();
                                ++ii;
                            }
                        case 9:
                            this.By();
                            MainMenuCounter = 3;
                    }
                }

                this.HaltIVRLine();
                return;
            }
        }

        private void playcash() throws AgiException, IOException, InterruptedException {

            PlayCash PLYC = new Operations().new PlayCash();
            PLYC.playcach(this.Call);
        }

        private void BillPayment_Pay() throws AgiException, InterruptedException, FileNotFoundException, IOException {

            BillPayment BillPayment = new Operations().new BillPayment();
            this.Call = BillPayment.Start(this.Call);
        }

        private void BillPayment_Follow() throws AgiException, IOException, FileNotFoundException, InterruptedException {

            BillPayment_follow BillPayment_F = new Operations().new BillPayment_follow();
            this.Call = BillPayment_F.Start(this.Call);
        }

        private CallObject login() throws AgiException, IOException, FileNotFoundException, UnsupportedEncodingException, InterruptedException {

            Login LGN = new Operations().new Login();

            CallObject callObject=LGN.login(this.Call);
            isTejarat=LGN.isTejaratCard;
            return callObject;
        }

        private void BlockCard() throws InterruptedException, AgiException, IOException {

            BlockCard BCard = new Operations().new BlockCard();
            this.Call = BCard.Satrt(this.Call);
        }

        private void proccessActionCode(String ActionCode) throws AgiException {
            File file = new File("//var//lib//asterisk//sounds//persian//ActionCode//" + ActionCode);
            this.Say.PlayFile("persian/ActionCode/" + ActionCode);
            if(!file.exists()) {
                this.Say.PlayFile("persian/ActionCode/9900");
            } else {
                this.Say.PlayFile("persian/ActionCode/" + ActionCode);
            }

        }
    }
}
