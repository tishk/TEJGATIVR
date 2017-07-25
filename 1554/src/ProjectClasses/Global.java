//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package ProjectClasses;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import org.w3c.dom.DOMException;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

public class Global {
    public Global() {
    }

    public class SettingsOBJ {
        public String IP;
        public String Port;
        public String ClientNo;
        public String DeviceCode;
        public String MacAddress;
        public String ChannelNo;

        public SettingsOBJ() {
        }
    }

    public class CardIdentityBankObJ {
        public String Sarmayeh;
        public String Sepah;
        public String Refah;
        public String Sarderat;
        public String Melli;
        public String Keshavarzi;
        public String Mehr;
        public String Mellat;
        public String Saman;
        public String Parsian1;
        public String Parsian2;
        public String Tejarat;
        public String Tejarat2;

        public String EghtesadNovin;
        public String TosehSaderat;
        public String Karafarin;
        public String PostBank;
        public String SanatoMadan;
        public String Maskan;
        public String EtebariToseh;
        public String Sina;
        public String Ayandeh1;
        public String Ayandeh2;
        public String TrueCardNumber;

        public CardIdentityBankObJ() {
        }
    }

    public class Variables {
        public String ProjectPath;
        public String SettingPath;
        public String Line = "_______________________________________________________________________________";
        public String CallerID;
        public String UniqueID;
        public String ChanName;
        public String Path;
        public String CallTime;

        public Variables() {
        }
    }

    public class Command {
        Global.Variables V = Global.this.new Variables();

        public Command() {
        }

        public void test(String S) {
            System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
            System.out.println(S);
            System.out.println("$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$$");
        }

        public Global.SettingsOBJ LoadFromXML(String ParamName, Global.Variables V) {
            Global.SettingsOBJ OBJ = Global.this.new SettingsOBJ();

            try {
                File e = new File(V.Path + "//Settings//Settings.xml");
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(e);
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName(ParamName);

                for(int temp = 0; temp < nList.getLength(); ++temp) {
                    Node nNode = nList.item(temp);
                    if(nNode.getNodeType() == 1) {
                        Element eElement = (Element)nNode;
                        byte var13 = -1;
                        switch(ParamName.hashCode()) {
                            case -1571332381:
                                if(ParamName.equals("GateweySettings")) {
                                    var13 = 0;
                                }
                                break;
                            case 1697672505:
                                if(ParamName.equals("PCInformation")) {
                                    var13 = 1;
                                }
                        }

                        switch(var13) {
                            case 0:
                                OBJ.IP = eElement.getElementsByTagName("IP").item(0).getTextContent();
                                OBJ.Port = eElement.getElementsByTagName("Port").item(0).getTextContent();
                                break;
                            case 1:
                                OBJ.ChannelNo = eElement.getElementsByTagName("ChannelNo").item(0).getTextContent();
                                OBJ.ClientNo = eElement.getElementsByTagName("ClientNo").item(0).getTextContent();
                                OBJ.DeviceCode = eElement.getElementsByTagName("DeviceCode").item(0).getTextContent();
                                OBJ.MacAddress = eElement.getElementsByTagName("MacAddress").item(0).getTextContent();
                        }
                    }
                }
            } catch (Exception var14) {
                var14.printStackTrace();
            }

            return OBJ;
        }

        public Global.SettingsOBJ LoadSetting(String Kind) {
            File f = new File(Global.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            this.V.Path = f.toString();
            return this.LoadFromXML(Kind, this.V);
        }

        public Global.CardIdentityBankObJ LoadSetting() {
            File f = new File(Global.class.getProtectionDomain().getCodeSource().getLocation().getPath());
            this.V.Path = f.toString();
            return this.loadFromXML(this.V);
        }

        public Global.CardIdentityBankObJ loadFromXML(Global.Variables V) {
            Global.CardIdentityBankObJ OBJ = Global.this.new CardIdentityBankObJ();

            try {
                File e = new File(V.Path + "//Settings//Settings.xml");
                DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
                DocumentBuilder dBuilder = dbFactory.newDocumentBuilder();
                Document doc = dBuilder.parse(e);
                doc.getDocumentElement().normalize();
                NodeList nList = doc.getElementsByTagName("CardNumberCheck");

                for(int temp = 0; temp < nList.getLength(); ++temp) {
                    Node nNode = nList.item(temp);
                    if(nNode.getNodeType() == 1) {
                        Element eElement = (Element)nNode;
                        OBJ.Ayandeh1 = eElement.getElementsByTagName("Ayandeh1").item(0).getTextContent();
                        OBJ.Ayandeh2 = eElement.getElementsByTagName("Ayandeh2").item(0).getTextContent();
                        OBJ.EghtesadNovin = eElement.getElementsByTagName("EghtesadNovin").item(0).getTextContent();
                        OBJ.EtebariToseh = eElement.getElementsByTagName("EtebariToseh").item(0).getTextContent();
                        OBJ.Karafarin = eElement.getElementsByTagName("Karafarin").item(0).getTextContent();
                        OBJ.Keshavarzi = eElement.getElementsByTagName("Keshavarzi").item(0).getTextContent();
                        OBJ.Maskan = eElement.getElementsByTagName("Maskan").item(0).getTextContent();
                        OBJ.Mehr = eElement.getElementsByTagName("Mehr").item(0).getTextContent();
                        OBJ.Mellat = eElement.getElementsByTagName("Mellat").item(0).getTextContent();
                        OBJ.Melli = eElement.getElementsByTagName("Melli").item(0).getTextContent();
                        OBJ.Parsian1 = eElement.getElementsByTagName("Parsian1").item(0).getTextContent();
                        OBJ.Parsian2 = eElement.getElementsByTagName("Parsian2").item(0).getTextContent();
                        OBJ.PostBank = eElement.getElementsByTagName("PostBank").item(0).getTextContent();
                        OBJ.Refah = eElement.getElementsByTagName("Refah").item(0).getTextContent();
                        OBJ.Saman = eElement.getElementsByTagName("Saman").item(0).getTextContent();
                        OBJ.SanatoMadan = eElement.getElementsByTagName("SanatoMadan").item(0).getTextContent();
                        OBJ.Sarderat = eElement.getElementsByTagName("Sarderat").item(0).getTextContent();
                        OBJ.Sarmayeh = eElement.getElementsByTagName("Sarmayeh").item(0).getTextContent();
                        OBJ.Sepah = eElement.getElementsByTagName("Sepah").item(0).getTextContent();
                        OBJ.Sina = eElement.getElementsByTagName("Sina").item(0).getTextContent();
                        OBJ.Tejarat = eElement.getElementsByTagName("Tejarat").item(0).getTextContent();
                        OBJ.Tejarat2 = eElement.getElementsByTagName("Tejarat2").item(0).getTextContent();
                        OBJ.TosehSaderat = eElement.getElementsByTagName("TosehSaderat").item(0).getTextContent();
                        OBJ.TrueCardNumber = eElement.getElementsByTagName("TrueCardNumber").item(0).getTextContent();
                    }
                }
            } catch (ParserConfigurationException | DOMException | SAXException | IOException var11) {
                ;
            }

            return OBJ;
        }

        public void Start() {
        }
    }

    public class DateClass {
        public DateClass() {
        }

        public String Shamsi_Date() {
            Calendar cal = Calendar.getInstance();
            int Day = cal.get(5);
            int Month = cal.get(2) + 1;
            int Year = cal.get(1);
            if(Month < 3 && Day < 21) {
                Year -= 622;
            } else {
                Year -= 621;
            }

            switch(Month) {
                case 1:
                    if(Day < 21) {
                        Month = 10;
                        Day += 10;
                    } else {
                        Month = 11;
                        Day -= 20;
                    }
                    break;
                case 2:
                    if(Day < 20) {
                        Month = 11;
                        Day += 11;
                    } else {
                        Month = 12;
                        Day -= 19;
                    }
                    break;
                case 3:
                    if(Day < 21) {
                        Month = 12;
                        Day += 9;
                    } else {
                        Month = 1;
                        Day -= 20;
                    }
                    break;
                case 4:
                    if(Day < 21) {
                        Month = 1;
                        Day += 11;
                    } else {
                        Month = 2;
                        Day -= 20;
                    }
                    break;
                case 5:
                case 6:
                    if(Day < 22) {
                        Month -= 3;
                        Day += 10;
                    } else {
                        Month -= 2;
                        Day -= 21;
                    }
                    break;
                case 7:
                case 8:
                case 9:
                    if(Day < 23) {
                        Month -= 3;
                        Day += 9;
                    } else {
                        Month -= 2;
                        Day -= 22;
                    }
                    break;
                case 10:
                    if(Day < 23) {
                        Month = 7;
                        Day += 8;
                    } else {
                        Month = 8;
                        Day -= 22;
                    }
                    break;
                case 11:
                case 12:
                    if(Day < 22) {
                        Month -= 3;
                        Day += 9;
                    } else {
                        Month -= 2;
                        Day -= 21;
                    }
            }

            return Year + "." + Month + "." + Day;
        }

        public String GetNowDateTime() {
            Date DateTime = new Date();
            SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy.MM.dd \'@\' hh:mm:ss a");
            String Now = DateFormat.format(DateTime);
            return this.Shamsi_Date() + "  " + Now.substring(13);
        }

        public String GetNowDate() {
            Date Date = new Date();
            SimpleDateFormat DateFormat = new SimpleDateFormat("yyyy.MM.dd");
            String Now = DateFormat.format(Date);
            return Now;
        }

        public String GetNowTime() {
            Date Time = new Date();
            SimpleDateFormat DateFormat = new SimpleDateFormat("HH:mm:ss");
            String Now = DateFormat.format(Time);
            return Now;
        }
    }
}
