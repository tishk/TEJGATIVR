import Mainchannel.Exceptions.InvalidParameterException;
import Mainchannel.Exceptions.ResponseParsingException;
import Mainchannel.sender.SenderException;
import ServiceObjects.Account.BaseAccountRequest;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by root on 4/20/16.
 */
public class Status {

    public Status(){

    }


    public  Object submitRequestToGateway(Object object) throws IOException,  ServerNotActiveException {
        Bank b=null;
        try {
            b=(Bank) Naming.lookup("rmi://" + Properties_Monitoring.getGateway_IP()+":" +Properties_Monitoring.getGateway_Port()+ "/Gateway");
            object=b.submitRequest(object);

        } catch (NotBoundException e) {
            System.out.println("1"+e.toString());

        } catch (MalformedURLException e) {
            System.out.println("2"+e.toString());
        } catch (RemoteException e) {
            System.out.println("3"+e.toString());
        } catch (InvalidParameterException e) {
            System.out.println("4"+e.toString());
        } catch (ResponseParsingException e) {
            System.out.println("5"+e.toString());
        } catch (SenderException e) {
            System.out.println("6"+e.toString());
        } catch (SQLException e) {
            System.out.println("7"+e.toString());
        }

        return object;
    }

    private String  AccountNumber="";
    public  void    setAccountNumber(String accountNumber){
        AccountNumber=accountNumber;
    }
    public  String  getAccountNumber(){
        return AccountNumber;
    }

    private String  Pin1="";
    public  void    setPin1(String pin1){
        Pin1=pin1;
    }
    public  String  getPin1(){
        return Pin1;
    }

    private String  Pin2="";
    public  void    setPin2(String pin2){
        Pin2=pin2;
    }
    public  String  getPin2(){
        return Pin2;
    }

    private String  Pan="";
    public  void    setPan(String pan){
        Pan=pan;
    }
    public  String  getPan(){
        return Pan;
    }

    private String  PinOfPan="";
    public  void    setPinOfPan(String pinOfPan){
        PinOfPan=pinOfPan;
    }
    public  String  getPinOfPan(){
        return PinOfPan;
    }

    private String  PhoneNumber="";
    public  void    setPhoneNumber(String phoneNumber){
        PhoneNumber=phoneNumber;
    }
    public  String  getPhoneNumber(){
        return PhoneNumber;
    }

    public  void    initializeAccountParameters(String accParams){
        try{
            String accparams[]=accParams.split("@");
            setAccountNumber(accparams[0]);
            setPin1(accparams[1]);
            setPin2(accparams[2]);
            setPan(accparams[3]);
            setPinOfPan(accparams[4]);
            setPhoneNumber(accparams[5]);

        }catch (Exception e){

        }
    }

    private String  Response=null;
    public  void    setResponse(String response){
        Response=response;
    }
    public  String  getResponse(){
        return Response; //uniqueID+"#service#"+serviceCode+"#"+
    }

    public  String  getNowDateTime(){

        Date Time=new Date();
        SimpleDateFormat DateFormat =
                //new SimpleDateFormat ("hh:mm:ss a");
                new SimpleDateFormat ("yyyy-MM-dd HH:mm:ss");
        String Now=DateFormat.format(Time);
        return Now;

    }

    private String  MAC="";
    public  void    setMAC(String mac){
        MAC=mac;
    }
    public  String  getMAC(){
        return MAC;
    }

    private String  IP="";
    public  void    setIP(String IP){
        IP=IP;
    }
    public  String  getIP(){
        return IP;
    }

    private String  PingStatus="";
    public  void    setPingStatus(String pingStatus){
        PingStatus=pingStatus;
    }
    public  String  getPingStatus(){
        return PingStatus;
    }

    private String  PingStatusDateTime="";
    public  void    setPingStatusDateTime(String pingStatusDateTime){
        PingStatusDateTime=pingStatusDateTime;
    }
    public  String  getPingStatusDateTime(){
        return PingStatusDateTime;
    }

    private String  PingStatusActionCode="";
    public  void    setPingStatusActionCode(String pingStatusActionCode){
        PingStatusActionCode=pingStatusActionCode;
    }
    public  String  getPingStatusActionCode(){
        return PingStatusActionCode;
    }

    private String  PingStatusDesc="";
    public  void    setPingStatusDesc(String pingStatusDesc){
        PingStatusDesc=pingStatusDesc;
    }
    public  String  getPingStatusDesc(){
        return PingStatusDesc;
    }

    private String  AppRunning="";
    public  void    setAppRunning(String appRunning){
        this.AppRunning=appRunning;
    }
    public  String  getAppRunning(){
        return AppRunning;
    }

    private String  AppRunningDateTIme="";
    public  void    setAppRunningDateTIme(String appRunningDateTIme){
        AppRunningDateTIme=appRunningDateTIme;
    }
    public  String  getAppRunningDateTIme(){
        return AppRunningDateTIme;
    }

    private String  AppRunningActionCode="";
    public  void    setAppRunningActionCode(String appRunningActionCode){
        AppRunningActionCode=appRunningActionCode;
    }
    public  String  getAppRunningActionCode(){
        return AppRunningActionCode;
    }

    private String  AppRunningDesc="";
    public  void    setAppRunningDesc(String appRunningDesc){
        AppRunningDesc=appRunningDesc;
    }
    public  String  getAppRunningDesc(){
        return AppRunningDesc;
    }


}
