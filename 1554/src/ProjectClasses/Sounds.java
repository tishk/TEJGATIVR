//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package ProjectClasses;

import ProjectClasses.Global;
import ProjectClasses.Global.Command;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.BaseAgiScript;

public class Sounds extends BaseAgiScript {
     Global.Command command=new Global().new Command();
    int Zeroresul;

    public Sounds() {
        Global var10003 = new Global();
        var10003.getClass();
        this.command = new Global().new Command();
    }

    public void SaykindOfShenase(String Shenase) throws AgiException {
        int Len = Shenase.length();
        String Type = Shenase.substring(Len - 2, Len - 1);
        byte var5 = -1;
        switch(Type.hashCode()) {
            case 49:
                if(Type.equals("1")) {
                    var5 = 0;
                }
                break;
            case 50:
                if(Type.equals("2")) {
                    var5 = 1;
                }
                break;
            case 51:
                if(Type.equals("3")) {
                    var5 = 2;
                }
                break;
            case 52:
                if(Type.equals("4")) {
                    var5 = 3;
                }
                break;
            case 53:
                if(Type.equals("5")) {
                    var5 = 4;
                }
                break;
            case 54:
                if(Type.equals("6")) {
                    var5 = 5;
                }
                break;
            case 55:
                if(Type.equals("7")) {
                    var5 = 6;
                }
                break;
            case 56:
                if(Type.equals("8")) {
                    var5 = 7;
                }
                break;
            case 57:
                if(Type.equals("9")) {
                    var5 = 8;
                }
        }

        switch(var5) {
            case 0:
                this.G_AB();
                break;
            case 1:
                this.G_Bargh();
                break;
            case 2:
                this.G_Gaz();
                break;
            case 3:
                this.G_TeleSabet();
                break;
            case 4:
                this.G_TeleHamrah();
                break;
            case 5:
                this.G_ShahrDari();
                break;
            case 6:
                this.G_ShahrDari();
            case 7:
            case 8:
        }

    }

    public void SayPriceGhabz(String ShenasePardakht) throws AgiException {
        String Price = ShenasePardakht.substring(0, ShenasePardakht.length() - 5) + "000";
        this.SayPersianDigit(Price);
    }

    private boolean HaveZero(String SayNow) {
        this.Zeroresul = 0;
        if("0".equals(SayNow.substring(2, 3))) {
            this.Zeroresul = 1;
        }

        if("0".equals(SayNow.substring(1, 2))) {
            this.Zeroresul += 10;
        }

        if("0".equals(SayNow.substring(0, 1))) {
            this.Zeroresul += 100;
        }

        return this.Zeroresul != 0;
    }

    public boolean SayPersianDigitsSeperate(String Digit) throws AgiException {
        try {
            int ex = Digit.length();
            int LeftIndex = ex % 3;
            if(ex > 2) {
                String SayNOW = Digit.substring(0, 3);
                Digit = Digit.substring(3);
                this.Say3DigitSeperate(SayNOW);
                this.SayPersianDigitsSeperate(Digit);
            } else {
                if(ex == 0) {
                    return true;
                }

                if(ex >= 1) {
                    this.Say3DigitSeperate(Digit);
                    return true;
                }
            }

            return false;
        } catch (NumberFormatException var5) {
            return true;
        }
    }

    private boolean Say3DigitSeperate(String SayNow) throws AgiException {
        boolean IsZero = false;
        if(SayNow.length() == 1 && Integer.parseInt(SayNow) != 0) {
            this.streamFile("persian/NUM/" + SayNow);
            return true;
        } else {
            if(SayNow.length() == 1) {
                IsZero = true;
                SayNow = "00" + SayNow;
            }

            if(SayNow.length() == 2) {
                SayNow = "0" + SayNow;
            }

            if(this.HaveZero(SayNow)) {
                if(this.Zeroresul == 1) {
                    this.streamFile("persian/NUM/" + SayNow.substring(0, 1) + "00o");
                    this.streamFile("persian/NUM/" + SayNow.substring(1, 2) + "0");
                } else if(this.Zeroresul == 10) {
                    this.streamFile("persian/NUM/" + SayNow.substring(0, 1) + "00o");
                    this.streamFile("persian/NUM/" + SayNow.substring(2, 3));
                } else if(this.Zeroresul == 11) {
                    this.streamFile("persian/NUM/" + SayNow.substring(0, 1) + "00");
                } else if(this.Zeroresul == 100) {
                    if(Integer.parseInt(SayNow.substring(1, 3)) < 20) {
                        this.streamFile("persian/NUM/0");
                        this.streamFile("persian/NUM/" + SayNow.substring(1, 3));
                    } else {
                        this.streamFile("persian/NUM/0");
                        this.streamFile("persian/NUM/" + SayNow.substring(1, 2) + "0o");
                        this.streamFile("persian/NUM/" + SayNow.substring(2, 3));
                    }
                } else if(this.Zeroresul == 101) {
                    this.streamFile("persian/NUM/0");
                    this.streamFile("persian/NUM/" + SayNow.substring(1, 2) + "0");
                } else if(this.Zeroresul == 110) {
                    this.streamFile("persian/NUM/0");
                    this.streamFile("persian/NUM/0");
                    this.streamFile("persian/NUM/" + SayNow.substring(2, 3));
                } else if(this.Zeroresul == 111) {
                    if(IsZero) {
                        this.streamFile("persian/NUM/0");
                    } else {
                        this.streamFile("persian/NUM/0");
                        this.streamFile("persian/NUM/0");
                        this.streamFile("persian/NUM/0");
                    }
                }
            } else if(Integer.parseInt(SayNow.substring(1, 3)) < 20) {
                this.streamFile("persian/NUM/" + SayNow.substring(0, 1) + "00o");
                this.streamFile("persian/NUM/" + SayNow.substring(1, 3));
            } else {
                this.streamFile("persian/NUM/" + SayNow.substring(0, 1) + "00o");
                this.streamFile("persian/NUM/" + SayNow.substring(1, 2) + "0o");
                this.streamFile("persian/NUM/" + SayNow.substring(2, 3));
            }

            return true;
        }
    }

    public boolean SayPersianDigit(String Digit) throws AgiException {
        try {
            int ex = Digit.length();
            int LeftIndex = ex % 3;
            if(LeftIndex == 0 && ex >= 3) {
                LeftIndex = 3;
            }

            if(ex > 2) {
                String SayNOW = Digit.substring(0, LeftIndex);
                this.Say3Digits(SayNOW);
                String DigitTemp = Digit.substring(LeftIndex);
                if(0L == Long.parseLong(DigitTemp)) {
                    this.PlayFile(this.SetConnectionFile(Digit.length(), 0));
                } else {
                    this.PlayFile(this.SetConnectionFile(Digit.length(), 1));
                }

                Digit = Digit.substring(LeftIndex);
                this.SayPersianDigit(Digit);
            } else {
                if(ex == 0) {
                    return true;
                }

                if(ex >= 1) {
                    this.Say3Digits(Digit);
                    return true;
                }
            }

            return false;
        } catch (NumberFormatException var6) {
            return true;
        }
    }

    private boolean Say3Digits(String SayNow) throws AgiException {
        if(SayNow.length() == 1) {
            SayNow = "00" + SayNow;
        }

        if(SayNow.length() == 2) {
            SayNow = "0" + SayNow;
        }

        if(this.HaveZero(SayNow)) {
            if(this.Zeroresul == 1) {
                this.streamFile("persian/NUM/" + SayNow.substring(0, 1) + "00o");
                this.streamFile("persian/NUM/" + SayNow.substring(1, 2) + "0");
            } else if(this.Zeroresul == 10) {
                this.streamFile("persian/NUM/" + SayNow.substring(0, 1) + "00o");
                this.streamFile("persian/NUM/" + SayNow.substring(2, 3));
            } else if(this.Zeroresul == 11) {
                this.streamFile("persian/NUM/" + SayNow.substring(0, 1) + "00");
            } else if(this.Zeroresul == 100) {
                if(Integer.parseInt(SayNow) <= 20) {
                    this.streamFile("persian/NUM/" + SayNow.substring(1, 3));
                } else {
                    this.streamFile("persian/NUM/" + SayNow.substring(1, 2) + "0o");
                    this.streamFile("persian/NUM/" + SayNow.substring(2, 3));
                }
            } else if(this.Zeroresul == 101) {
                this.streamFile("persian/NUM/" + SayNow.substring(1, 2) + "0");
            } else if(this.Zeroresul == 110) {
                this.streamFile("persian/NUM/" + SayNow.substring(2, 3));
            } else if(this.Zeroresul == 111) {
                ;
            }
        } else if(Integer.parseInt(SayNow.substring(1, 3)) <= 20) {
            this.streamFile("persian/NUM/" + SayNow.substring(0, 1) + "00o");
            this.streamFile("persian/NUM/" + SayNow.substring(1, 3));
        } else {
            this.streamFile("persian/NUM/" + SayNow.substring(0, 1) + "00o");
            this.streamFile("persian/NUM/" + SayNow.substring(1, 2) + "0o");
            this.streamFile("persian/NUM/" + SayNow.substring(2, 3));
        }

        return true;
    }

    private String SetConnectionFile(int Len, int Kind) {
        String Part1 = "persian/NUM/";
        if(Kind == 0) {
            switch(Len) {
                case 1:
                    return "";
                case 2:
                    return "";
                case 3:
                    return "";
                case 4:
                    return Part1 + "1000";
                case 5:
                    return Part1 + "1000";
                case 6:
                    return Part1 + "1000";
                case 7:
                    return Part1 + "1000000";
                case 8:
                    return Part1 + "1000000";
                case 9:
                    return Part1 + "1000000";
                case 10:
                    return Part1 + "1000000000";
                case 11:
                    return Part1 + "1000000000";
                case 12:
                    return Part1 + "1000000000";
                case 13:
                    return Part1 + "1000000000000";
                case 14:
                    return Part1 + "1000000000000";
                case 15:
                    return Part1 + "1000000000000";
                default:
                    return "";
            }
        } else {
            switch(Len) {
                case 1:
                    return "";
                case 2:
                    return "";
                case 3:
                    return "";
                case 4:
                    return Part1 + "1000o";
                case 5:
                    return Part1 + "1000o";
                case 6:
                    return Part1 + "1000o";
                case 7:
                    return Part1 + "1000000o";
                case 8:
                    return Part1 + "1000000o";
                case 9:
                    return Part1 + "1000000o";
                case 10:
                    return Part1 + "1000000000o";
                case 11:
                    return Part1 + "1000000000o";
                case 12:
                    return Part1 + "1000000000o";
                case 13:
                    return Part1 + "1000000000000o";
                case 14:
                    return Part1 + "1000000000000o";
                case 15:
                    return Part1 + "1000000000000o";
                default:
                    return "";
            }
        }
    }

    public boolean SayDate(String D) throws AgiException {
        D = D.trim();
        String Year = D.substring(0, 2);
        String Month = D.substring(2, 4);
        String Day = D.substring(4, 6);
        this.streamFile("persian/DATE/A" + Day);
        this.streamFile("persian/DATE/B" + Month);
        this.streamFile("persian/DATE/C" + Year);
        return false;
    }

    public String GetStringFromAsterisk(String File, int TimeOut) throws AgiException {
        boolean digit = true;
        String Result = "";
        char digit1 = this.streamFile(File, "0123456789*#");
        Result = Result + String.valueOf(digit1);
        if(digit1 != 42 && digit1 != 35) {
            while((digit1 = this.waitForDigit(TimeOut)) != 0 && digit1 != 35 && digit1 != 42) {
                Result = Result + String.valueOf(digit1);
            }
        }

        return Result;
    }

    public String GetStringFormASK(String File, int timeOUT) throws AgiException {
        boolean digit = true;
        String Result = "";
        char digit1 = this.streamFile(File, "0123456789*#");
        Result = Result + String.valueOf(digit1);
        if(digit1 != 42 && digit1 != 35) {
            while(digit1 != 42 && digit1 != 35) {
                digit1 = this.streamFile("persian/null/" + Integer.toString(timeOUT), "0123456789*#");
                if(digit1 == 35 || digit1 == 42 || digit1 == 0) {
                    break;
                }

                Result = Result + String.valueOf(digit1);
            }
        }

        Result = Result.trim();
        return Result;
    }

    public String MainMenu() throws AgiException {
        String entrance="";
        try{
            entrance=this.getData("persian/1554Menu", 3000L, 1);
        }catch (Exception e){

        }

        return  entrance;
    }

    public String MainMenuIsNotTejaratCard() throws AgiException {

        String entrance = "";
        try {
            entrance = this.getData("persian/1554MenuIsNotTejarat", 3000L, 1);
        } catch (Exception e) {

        }

        return entrance;
    }

    public void test() throws AgiException, IOException {
        String S = this.GetStringFormASK("persian/msg/M01", 5);
        this.SayPersianDigitsSeperate(S);
    }

    public String GetPinCard() throws AgiException, IOException {
        return this.GetStringFormASK("persian/msg/M02", 5);
    }

    public String FirstGetCardNumber() throws AgiException, IOException {
        return this.GetStringFormASK("persian/msg/FirstM01", 5);
    }

    public String GetCardNumber() throws AgiException, IOException {
        return this.GetStringFormASK("persian/msg/M01", 5);
    }

    public String IfCorrect() throws AgiException {
        return this.CheckStar(this.getData("persian/msg/L05", 3000L, 1));
    }

    public String G_VorodeSheneseGhabz() throws AgiException {
        return this.GetStringFormASK("persian/msg/M03", 5);
    }

    public String G_VorodeShenasePardakht() throws AgiException {
        return this.GetStringFormASK("persian/msg/M04", 5);
    }

    public String G_VorodeShomarePeygiri() throws AgiException {
        return this.GetStringFormASK("persian/msg/M05", 5);
    }

    private String RepairDigit(String S) {
        String Temp = "";

        for(int i = 0; i < S.length(); ++i) {
            Temp = Temp + S.charAt(i);
            ++i;
        }

        return Temp;
    }

    private String CheckStar(String S) {
        int Len = S.length();
        if(Len != 0 && "*".equals(S.substring(Len - 1))) {
            S = S.substring(0, Len - 1);
        }

        return S;
    }

    public String BlockCard_Alarm() throws AgiException {
        return this.getData("persian/msg/M07", 3000L, 1);
    }

    public void Mablaghe() throws AgiException {
        this.streamFile("persian/msg/L01");
    }

    public void Mojodie() throws AgiException {
        this.streamFile("persian/msg/M06");
    }

    public void Rial() throws AgiException {
        this.streamFile("persian/msg/Rls");
    }

    public void Mibashad() throws AgiException {
        this.streamFile("persian/msg/L07");
    }

    public void SatrtFax() throws AgiException {
        this.streamFile("persian/faxstart");
    }

    public void SayBy() throws UnsupportedEncodingException, FileNotFoundException, IOException, AgiException {
        this.streamFile("persian/Bye");
        this.hangup();
    }

    public void InvalidEntry() throws AgiException, UnsupportedEncodingException, FileNotFoundException, IOException {
        this.streamFile("persian/error");
    }

    public void CardNumberNotOK() throws AgiException, IOException {
        this.streamFile("persian/msg/E02");
    }

    public void PinCardNotOK() throws AgiException, IOException {
        this.streamFile("persian/msg/R05");
    }

    public void UnclearCommand() throws AgiException {
        this.streamFile("persian/msg/E00");
    }

    public void InternalError() throws AgiException {
        this.streamFile("persian/msg/E01");
    }

    public void Sorry() throws AgiException {
        this.streamFile("persian/msg/E03");
    }

    public void Jahate() throws AgiException {
        this.streamFile("persian/msg/L08");
    }

    public void G_RialBabatewGhabze() throws AgiException {
        this.streamFile("persian/msg/L02");
    }

    public void G_VaBaShenase() throws AgiException {
        this.streamFile("persian/msg/L03");
    }

    public void G_VaBAShomarePeygiri() throws AgiException {
        this.streamFile("persian/msg/L10");
    }

    public void G_BardashtKHahadshod() throws AgiException {
        this.streamFile("persian/msg/L04");
    }

    public void G_PardakhtShode() throws AgiException {
        this.streamFile("persian/msg/L11");
    }

    public void G_ShomarePeygiri() throws AgiException {
        this.streamFile("persian/msg/L06");
    }

    public void G_InGhabzDarTarikhe() throws AgiException {
        this.streamFile("persian/msg/L09");
    }

    public void G_AB() throws AgiException {
        this.streamFile("persian/msg/N01");
    }

    public void G_Bargh() throws AgiException {
        this.streamFile("persian/msg/N02");
    }

    public void G_Gaz() throws AgiException {
        this.streamFile("persian/msg/N03");
    }

    public void G_TeleSabet() throws AgiException {
        this.streamFile("persian/msg/N04");
    }

    public void G_TeleHamrah() throws AgiException {
        this.streamFile("persian/msg/N05");
    }

    public void G_ShahrDari() throws AgiException {
        this.streamFile("persian/msg/N06");
    }

    public void G_ShenasePardakhtNotOK() throws AgiException {
        this.streamFile("persian/msg/R07");
    }

    public void G_ShenaseGhabzNotOK() throws AgiException {
        this.streamFile("persian/msg/E02");
    }

    public void G_NotPayed() throws AgiException {
        this.streamFile("persian/msg/R10");
    }

    public void G_PayOK() throws AgiException {
        this.streamFile("persian/msg/R11");
    }

    public void G_PayPendding() throws AgiException {
        this.streamFile("persian/msg/R12");
    }

    public void Answer_NotAnswer() throws AgiException {
        this.streamFile("persian/msg/R00");
    }

    public void Answer_PardakhtOK() throws AgiException {
        this.streamFile("persian/msg/R01");
    }

    public void Answer_MablaghOver() throws AgiException {
        this.streamFile("persian/msg/R02");
    }

    public void Answer_CardBloked() throws AgiException {
        this.streamFile("persian/msg/R03");
    }

    public void Answer_OperationsOK() throws AgiException {
        this.streamFile("persian/msg/R04");
    }

    public void Answer_CashNotEnough() throws AgiException {
        this.streamFile("persian/msg/R06");
    }

    public void Answer_AccountNotExist() throws AgiException {
        this.streamFile("persian/msg/R08");
    }

    public void Answer_PayLimit() throws AgiException {
        this.streamFile("persian/msg/R09");
    }

    public void Beep() throws AgiException {
        this.streamFile("persian/Beep");
    }

    public void PlayFile(String S) throws AgiException {
        this.streamFile(S);
    }

    public void service(AgiRequest request, AgiChannel channel) throws AgiException {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
