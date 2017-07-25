package ServiceObjects.Account;

import java.io.Serializable;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.net.UnknownHostException;


/**
 * Created by Administrator on 28/05/2015.
 */
public class BaseAccountRequest  implements Serializable {
    private static final long serialVersionUID = 7933179759822788700L;
    public BaseAccountRequest()  {
        processMAC();
    }

    private boolean IsSuccessfulConnectToGateway=false;
    public  void    setIsSuccessfulConnectToGateway(boolean success){
        this.IsSuccessfulConnectToGateway=success;
    }
    public  boolean  getIsSuccessfulConnectToGateway(){
        return this.IsSuccessfulConnectToGateway;
    }

    private String  Result=null;
    public  void    setResult(String result){
        this.Result=result;
    }
    public  String  getResult(){
        return this.Result;
    }

    private String  ActionCode=null;
    public  void    setActionCode(String actionCode){
        this.ActionCode=actionCode;
    }
    public  String  getActionCode(){
        return this.ActionCode;
    }

    private String Pin=null;
    public  void   setPin(String pin){
        Pin=pin;
    }
    public  String getPin(){
        return Pin;
    }

    private String  GatewayMessage=null;
    public  void    setGatewayMessage(String gatewayMessage){
        this.GatewayMessage=gatewayMessage;
    }
    public  String  getGatewayMessage(){
        return this.GatewayMessage;
    }

    private String RequestDateTime=null;
    public  void   setRequestDateTime(String requestDateTime){
        RequestDateTime=requestDateTime;
    }
    public  String getRequestDateTime(){
        return RequestDateTime;
    }

    private String ChannelType=null;
    public  void   setChannelType(String channelType){
        ChannelType=channelType;
    }
    public  String getChannelType(){
        return ChannelType;
    }

    private String ChannelPass=null;
    public  void   setChannelPass(String channelPass){
        ChannelPass=channelPass;
    }
    public  String getChannelPass(){
        return ChannelPass;
    }

    private String ChannelID=null;
    public  void   setChannelID(String channelID){
        ChannelID=channelID;
    }
    public  String getChannelID(){
        return ChannelID;
    }

    private String MsgSeq=null;
    public  void   setMsgSeq(String msgSeq){
        MsgSeq=msgSeq;
    }
    public  String getMsgSeq(){
        return MsgSeq;
    }

    private String CallUniqID=null;
    public  void   setCallUniqID(String CallUniqID){
        this.CallUniqID=CallUniqID;
    }
    public  String getCallUniqID(){
        return this.CallUniqID;
    }

    private String EncAlgorytm=null;
    public  void   setEncAlgorytm(String encAlgorytm){
        EncAlgorytm=encAlgorytm;
    }
    public  String getEncAlgorytm(){
        return EncAlgorytm;
    }

    private String MAC=null;
    public  void   setMAC(String mac){
        MAC=mac;
    }
    public  String getMAC(){
        return MAC;
    }
    private void   processMAC()  {
        setMAC(getMACAddress());
    }
    public  String getMACAddress() {
        InetAddress ip;
        try {

            //ip = InetAddress.getByName(Telbank.Util.IP);

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

          //  e.printStackTrace();
            return "!";

        } catch (SocketException e){

         //   e.printStackTrace();
            return "!";

        }
    }



}
