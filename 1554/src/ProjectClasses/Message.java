//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package ProjectClasses;

import ProjectClasses.CallObject;
import ProjectClasses.Connection;
import ProjectClasses.Global;
import ProjectClasses.Sounds;
import ProjectClasses.Connection.SocketConnection;
import ProjectClasses.Global.Command;
import ProjectClasses.Global.SettingsOBJ;
import ProjectClasses.Global.Variables;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Random;
import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiException;
import org.asteriskjava.fastagi.AgiRequest;

public class Message {
    public Message() {
    }

    public void service(AgiRequest request, AgiChannel channel) throws AgiException {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public class SocketMessage {
        Command command;
        Sounds Say;

        public SocketMessage() {
            Global var10003 = new Global();
            var10003.getClass();
            this.command = new Global().new Command();
            this.Say = new Sounds();
        }

        public String RepairStringLenght(String S, int Len) {
            int L = S.length();
            if(S.length() <= Len) {
                for(int i = 0; i < Len - L; ++i) {
                    S = "0" + S;
                }
            }

            return S;
        }

        public class MSGFromServer {
            public MSGFromServer() {
            }
        }

        public class BlockCard {
            public BlockCard() {
            }

            public CallObject SubmitMSGForCard(CallObject Call) {
                Global var10002 = new Global();
                var10002.getClass();
               // new SettingsOBJ(var10002);
                Message.SocketMessage.BlockCard.BlockCardOBJ CashCardOBJ = new Message.SocketMessage.BlockCard.BlockCardOBJ();
                SettingsOBJ PCSetting = SocketMessage.this.command.LoadSetting("PCInformation");
                Random rand = new Random();
                String RandomCode = "0" + SocketMessage.this.RepairStringLenght(Integer.toString(rand.nextInt(99999999) + 1), 8);
                CashCardOBJ.CID = SocketMessage.this.RepairStringLenght(Call.CallerID, 18);
                CashCardOBJ.CardFlg = "XM";
                CashCardOBJ.CardNO = SocketMessage.this.RepairStringLenght(Call.CardNumber, 16);
//                CashCardOBJ.CardPin = SocketMessage.this.RepairStringLenght(Integer.toString(Integer.parseInt(Call.CardPin)), 12);
                CashCardOBJ.CardPin = SocketMessage.this.RepairStringLenght(Call.CardPin, 12);
//                CashCardOBJ.CardPinLenght = SocketMessage.this.RepairStringLenght(Integer.toString(Integer.toString(Integer.parseInt(CashCardOBJ.CardPin)).length()), 2);
                CashCardOBJ.CardPinLenght = SocketMessage.this.RepairStringLenght(String.valueOf(Call.CardPin.length()), 2);
                CashCardOBJ.ClientNO = SocketMessage.this.RepairStringLenght(PCSetting.ClientNo, 3);
                Call.SendString = CashCardOBJ.ClientNO + PCSetting.ChannelNo + RandomCode + CashCardOBJ.CID + PCSetting.DeviceCode + PCSetting.MacAddress + CashCardOBJ.CardFlg + CashCardOBJ.CardNO + CashCardOBJ.CardPinLenght + CashCardOBJ.CardPin;
                return Call;
            }

            public CallObject GetMSGFromServer(CallObject Call) throws FileNotFoundException, IOException, UnsupportedEncodingException, InterruptedException, AgiException {

                SocketConnection Con = new Connection().new SocketConnection();
                Call = Con.StartConnect(Call);
                Call = this.MSGReceiveProcess(Call);
                return Call;
            }

            public CallObject MSGReceiveProcess(CallObject Call) throws AgiException {
                String Mess = Call.ReceiveString;
                if(Call.Action_ConnectedToGateway) {
                    if(Mess.length() > 20) {
                        try {
                            Call.Action_Code = Mess.substring(16, 20);
                            Call.CodePeygiri = Mess.substring(20, 26);
                            Call.Cash_reachable = Mess.substring(26, 44);
                            Call.Cash_Real = Mess.substring(44, 62);
                        } catch (ExceptionInInitializerError var4) {
                            ;
                        }
                    } else if(Mess.length() > 3 && Mess.length() < 20) {
                        Call.Action_Code = "8888";
                        Call.CodePeygiri = "!";
                        Call.Cash_reachable = "!";
                        Call.Cash_Real = "!";
                    } else {
                        Call.Action_Code = Mess.substring(16, 20);
                        Call.CodePeygiri = "!";
                        Call.Cash_reachable = "!";
                        Call.Cash_Real = "!";
                    }
                } else {
                    SocketMessage.this.Say.Answer_NotAnswer();
                }

                return Call;
            }

            private class BlockCardOBJ {
                String CardFlg;
                String ClientNO;
                String CID;
                String RandomCode;
                String DeviceCode;
                String MacAddress;
                String ChannelNO;
                String CardNO;
                String CardPinLenght;
                String CardPin;

                private BlockCardOBJ() {
                    this.CardFlg = "XB";
                }
            }
        }

        public class Billpayment_Follew {
            public Billpayment_Follew() {
            }

            public CallObject SubmitMSGBillpayment(CallObject Call) {
                Global var10002 = new Global();
                var10002.getClass();
                //new SettingsOBJ(var10002);
                Message.SocketMessage.Billpayment_Follew.BillPayment_FollwOBJ BillPayFOBJ = SocketMessage.this.new Billpayment_Follew().new BillPayment_FollwOBJ();
                SettingsOBJ PCSetting = SocketMessage.this.command.LoadSetting("PCInformation");
                PCSetting.ClientNo = SocketMessage.this.RepairStringLenght(PCSetting.ClientNo, 3);
                Random rand = new Random();
                String RandomCode = "0" + SocketMessage.this.RepairStringLenght(Integer.toString(rand.nextInt(99999999) + 1), 8);
                BillPayFOBJ.CID = SocketMessage.this.RepairStringLenght(Call.CallerID, 18);
                BillPayFOBJ.BillPaymentFollwFlag = "XP";
                BillPayFOBJ.CardNO = SocketMessage.this.RepairStringLenght(Call.CardNumber, 16);
//                BillPayFOBJ.CardPinLenght = SocketMessage.this.RepairStringLenght(Integer.toString(Call.CardPin.length()), 2);
                BillPayFOBJ.CardPin = SocketMessage.this.RepairStringLenght(Call.CardPin, 12);
                BillPayFOBJ.CardPinLenght = SocketMessage.this.RepairStringLenght(String.valueOf(Call.CardPin.length()), 2);

                BillPayFOBJ.FollowCode = SocketMessage.this.RepairStringLenght(Call.Followcode, 10);
                Call.SendString = PCSetting.ClientNo + PCSetting.ChannelNo + RandomCode + BillPayFOBJ.CID + PCSetting.DeviceCode + PCSetting.MacAddress + BillPayFOBJ.BillPaymentFollwFlag + BillPayFOBJ.CardNO + BillPayFOBJ.FollowCode;
                return Call;
            }

            public CallObject GetMSGFromServer(CallObject Call) throws FileNotFoundException, IOException, UnsupportedEncodingException, InterruptedException {
                Connection var10002 = new Connection();
                var10002.getClass();
                SocketConnection Con = new Connection().new SocketConnection();
                new CallObject();
                CallObject B = Con.StartConnect(Call);
                return B;
            }

            public CallObject MSGReceiveProcess(CallObject Calll) {
                String Mess = Calll.ReceiveString;
                if(Mess.length() > 20) {
                    try {
                        Calll.Action_Code = Mess.substring(16, 20);
                        Calll.FollowStatus = Mess.substring(20, 21);
                    } catch (ExceptionInInitializerError var4) {
                        ;
                    }
                } else {
                    Calll.Action_Code = Mess.substring(16, 20);
                    Calll.FollowStatus = "!";
                }

                return Calll;
            }

            public class BillPayment_FollwOBJ {
                String BillPaymentFollwFlag = "G";
                String ClientNO;
                String RandomCode;
                String DeviceCode;
                String ShenaseGhabz;
                String ShenasePardakht;
                String MablagheGhabz;
                String ChannelNO;
                String MessageKind;
                String CID;
                String MacAddress;
                String CardNO;
                String CardPinLenght;
                String CardPin;
                String FollowCode;

                public BillPayment_FollwOBJ() {
                }
            }
        }

        public class Billpayment_Pay {
            Message.SocketMessage.Billpayment_Pay.BillPaymentMessage BillMSG = new Message.SocketMessage.Billpayment_Pay.BillPaymentMessage();

            public Billpayment_Pay() {
            }

            public CallObject SubmitMSGBillpayment(CallObject Call) {
                Global var10002 = new Global();
                var10002.getClass();
                //new SettingsOBJ(var10002);
                Message.SocketMessage.Billpayment_Pay.BillPaymentOBJ BillPayOBJ = new Message.SocketMessage.Billpayment_Pay.BillPaymentOBJ();
                SettingsOBJ PCSetting = SocketMessage.this.command.LoadSetting("PCInformation");
                PCSetting.ClientNo = SocketMessage.this.RepairStringLenght(PCSetting.ClientNo, 3);
                Random rand = new Random();
                String RandomCode = "0" + SocketMessage.this.RepairStringLenght(Integer.toString(rand.nextInt(99999999) + 1), 8);
                BillPayOBJ.CID = SocketMessage.this.RepairStringLenght(Call.CallerID, 18);
                BillPayOBJ.BillPaymentFlag = "XG";
                BillPayOBJ.CardNO = SocketMessage.this.RepairStringLenght(Call.CardNumber, 16);
                BillPayOBJ.CardPin = SocketMessage.this.RepairStringLenght(Call.CardPin, 12);
//                BillPayOBJ.CardPinLenght = SocketMessage.this.RepairStringLenght(Integer.toString(Call.CardPin.length()), 2);
                BillPayOBJ.CardPinLenght = SocketMessage.this.RepairStringLenght(String.valueOf(Call.CardPin.length()), 2);
                Call.MablagheGhabz = Call.ShenasePardakht.substring(0, Call.ShenasePardakht.length() - 5) + "000";
                BillPayOBJ.ShenaseGhabz = SocketMessage.this.RepairStringLenght(Call.ShenaseGhabz, 13);
                BillPayOBJ.ShenasePardakht = SocketMessage.this.RepairStringLenght(Call.ShenasePardakht, 13);
                BillPayOBJ.MablagheGhabz = SocketMessage.this.RepairStringLenght(Call.MablagheGhabz, 15);
                Call.SendString = PCSetting.ClientNo + PCSetting.ChannelNo + RandomCode + BillPayOBJ.CID + PCSetting.DeviceCode + PCSetting.MacAddress + BillPayOBJ.BillPaymentFlag + BillPayOBJ.CardNO + BillPayOBJ.CardPinLenght + BillPayOBJ.CardPin + BillPayOBJ.ShenaseGhabz + BillPayOBJ.ShenasePardakht + BillPayOBJ.MablagheGhabz;
                return Call;
            }

            public CallObject GetMSGFromServer(CallObject Call) throws FileNotFoundException, IOException, UnsupportedEncodingException, InterruptedException {
                Connection var10002 = new Connection();
                var10002.getClass();
                SocketConnection Con = new Connection().new SocketConnection();
                new CallObject();
                CallObject B = Con.StartConnect(Call);
                return B;
            }

            public CallObject MSGReceiveProcess(CallObject Call) {
                String Mess = Call.ReceiveString.trim().toString();
                int ACC = Integer.parseInt(Mess.substring(16, 20));
                if(ACC == 0) {
                    try {
                        Call.Action_Code = Mess.substring(16, 20);
                        Call.CodePeygiri = Mess.substring(20, 26);
                        Call.Cash_AfterBillPaymen = Mess.substring(26, 44);
                    } catch (ExceptionInInitializerError var5) {
                        ;
                    }
                } else if(ACC == 9014) {
                    Call.Action_Code = Mess.substring(16, 20);
                    Call.PayDate = Mess.substring(20, 26);
                    Call.CodePeygiri = Mess.substring(26, 32);
                } else {
                    Call.Action_Code = Mess.substring(16, 20);
                    Call.CodePeygiri = "!";
                }

                return Call;
            }

            public class BillPaymentMessage {
                String ClientNO = "!";
                String ChannelNO = "!";
                String RandomCode = "!";
                String ActionCode = "!";
                String CodePeygiri = "!";
                String MojodiAfter = "!";
                String PayDate;

                public BillPaymentMessage() {
                }
            }

            public class BillPaymentOBJ {
                String BillPaymentFlag = "G";
                String ClientNO;
                String RandomCode;
                String DeviceCode;
                String ShenaseGhabz;
                String ShenasePardakht;
                String ChannelNO;
                String MessageKind;
                String CID;
                String MacAddress;
                String CardNO;
                String CardPinLenght;
                String CardPin;
                String MablagheGhabz;

                public BillPaymentOBJ() {
                }
            }
        }

        public class Account {
            public Account() {
            }

            public String SubmitMessageCashFromAccount(Message.SocketMessage.Account.CashFromAccountOBJ MessageOBJ, Variables Var) {
                String Message = MessageOBJ.ClientNO + MessageOBJ.ChannelNO + MessageOBJ.RandomCode + MessageOBJ.CID + MessageOBJ.DeviceCode + MessageOBJ.MacAddress + MessageOBJ.AccountFlg + MessageOBJ.ACCNO;
                return Message;
            }

            public class CashFromAccountOBJ {
                String AccountFlg = "B";
                String ClientNO;
                String CID;
                String RandomCode;
                String DeviceCode;
                String MacAddress;
                String ACCNO;
                String ChannelNO;

                public CashFromAccountOBJ() {
                }
            }
        }

        public class Card {
            public Card() {
            }

            public CallObject SubmitMSGForCard(CallObject Call) {
                Global var10002 = new Global();
                var10002.getClass();
                //new SettingsOBJ(var10002);
                Message.SocketMessage.Card.CashFromCardOBJ CashCardOBJ = new Message.SocketMessage.Card.CashFromCardOBJ();
                SettingsOBJ PCSetting = SocketMessage.this.command.LoadSetting("PCInformation");
                Random rand = new Random();
                String RandomCode = "0" + SocketMessage.this.RepairStringLenght(Integer.toString(rand.nextInt(99999999) + 1), 8);
                CashCardOBJ.CID = SocketMessage.this.RepairStringLenght(Call.CallerID, 18);
                CashCardOBJ.CardFlg = "XB";
                CashCardOBJ.CardNO = SocketMessage.this.RepairStringLenght(Call.CardNumber, 16);
//                CashCardOBJ.CardPin = SocketMessage.this.RepairStringLenght(Integer.toString(Integer.parseInt(Call.CardPin)), 12);
                CashCardOBJ.CardPin = SocketMessage.this.RepairStringLenght(Call.CardPin, 12);
                CashCardOBJ.CardPinLenght = SocketMessage.this.RepairStringLenght(String.valueOf(Call.CardPin.length()), 2);
//                CashCardOBJ.CardPinLenght = SocketMessage.this.RepairStringLenght(String.valueOf(CashCardOBJ.CardPin.length()), 2);

                CashCardOBJ.ClientNO = SocketMessage.this.RepairStringLenght(PCSetting.ClientNo, 3);
                Call.SendString = CashCardOBJ.ClientNO + PCSetting.ChannelNo + RandomCode + CashCardOBJ.CID + PCSetting.DeviceCode + PCSetting.MacAddress + CashCardOBJ.CardFlg + CashCardOBJ.CardNO + CashCardOBJ.CardPinLenght + CashCardOBJ.CardPin;
                return Call;
            }

            public CallObject GetMSGFromServer(CallObject Call) throws FileNotFoundException, IOException, UnsupportedEncodingException, InterruptedException, AgiException {
                Connection var10002 = new Connection();
                var10002.getClass();
                SocketConnection Con = new Connection().new SocketConnection();
                Call = Con.StartConnect(Call);
                Call = this.MSGReceiveProcess(Call);
                return Call;
            }

            public CallObject MSGReceiveProcess(CallObject Call) throws AgiException {
                String Mess = Call.ReceiveString;
                if(Call.Action_ConnectedToGateway) {
                    if(Mess.length() > 20) {
                        try {
                            Call.Action_Code = Mess.substring(16, 20);
                            Call.CodePeygiri = Mess.substring(20, 26);
                            Call.Cash_reachable = Mess.substring(26, 44);
                            Call.Cash_Real = Mess.substring(44, 62);
                        } catch (ExceptionInInitializerError var4) {
                            ;
                        }
                    } else if(Mess.length() > 3 && Mess.length() < 20) {
                        Call.Action_Code = "8888";
                        Call.CodePeygiri = "!";
                        Call.Cash_reachable = "!";
                        Call.Cash_Real = "!";
                    } else {
                        Call.Action_Code = Mess.substring(16, 20);
                        Call.CodePeygiri = "!";
                        Call.Cash_reachable = "!";
                        Call.Cash_Real = "!";
                    }
                } else {
                    SocketMessage.this.Say.Answer_NotAnswer();
                }

                return Call;
            }

            private class CashFromCardOBJ {
                String CardFlg;
                String ClientNO;
                String CID;
                String RandomCode;
                String DeviceCode;
                String MacAddress;
                String ChannelNO;
                String CardNO;
                String CardPinLenght;
                String CardPin;

                private CashFromCardOBJ() {
                    this.CardFlg = "XB";
                }
            }
        }
    }
}
