package ServiceObjects.Pan;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;

/**
 * Created by Administrator on 26/06/2015.
 */
public class BaseCardRequest  implements Serializable {

    public BaseCardRequest()  {
        processMAC();
    }

    private String CallUniqID=null;
    public  void   setCallUniqID(String CallUniqID){
        this.CallUniqID=CallUniqID;
    }
    public  String getCallUniqID(){
        return CallUniqID;
    }

    private static final long serialVersionUID = 7955579759822788700L;
    private String  Pan=null;
    public  void    setPan(String cardNo){
        this.Pan=cardNo;
    }
    public  String  getPan(){
        return this.Pan;
    }

    private String  Pin=null;
    public  void    setPin(String pin){
        this.Pin=pin;
    }
    public  String  getPin(){
        return this.Pin;
    }

    private String  PinLength=null;
    private void    setPinLength(String pinLength){
        this.PinLength=pinLength;
    }
    public  String  getPinLength(){
        return this.PinLength;
    }

    private String  ClientNo=null;
    public  void    setClientNo(String clientNo){
        this.ClientNo=clientNo;
    }
    public  String  getClientNo(){
        return this.ClientNo;
    }

    private String  ChannelNo=null;
    public  void    setChannelNo(String channelNo){
        this.ChannelNo=channelNo;
    }
    public  String  getChannelNo(){
        return this.ChannelNo;
    }

    private String  RandomNumber10Char=null;
    public  void    setRandomNumber10Char(String randomNumber10Char){
        this.RandomNumber10Char=randomNumber10Char;
    }
    public  String  getRandomNumber10Char(){
        return this.RandomNumber10Char;
    }

    private String  CallerID=null;
    public  void    setCallerID(String callerID){
        this.CallerID=callerID;
    }
    public  String  getCallerID(){
        return this.CallerID;
    }

    private String  DeviceCode=null;
    public  void    setDeviceCode(String deviceCode){
        this.DeviceCode=deviceCode;
    }
    public  String  getDeviceCode(){
        return this.DeviceCode;
    }

    private String  MacAddress=null;
    public  void    setMacAddress(String macAddress){
        this.MacAddress=macAddress;
    }
    public  String  getMacAddress(){
        return this.MacAddress;
    }
    private void    processMAC()  {
        setMacAddress(getMAC());
    }
    public  String  getMAC() {
        InetAddress ip;
        try {

            String ipp=InetAddress.getLocalHost().getCanonicalHostName();
            ip = InetAddress.getLocalHost();

            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            byte[] mac = network.getHardwareAddress();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
            return sb.toString();
        } catch (UnknownHostException e) {

         //   e.printStackTrace();
            return "!";

        } catch (SocketException e){

       //     e.printStackTrace();
            return "!";

        }
    }


}
