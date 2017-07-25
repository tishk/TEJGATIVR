
import Mainchannel.Exceptions.InvalidParameterException;
import Mainchannel.Exceptions.ResponseParsingException;
import Mainchannel.sender.SenderException;
import org.asteriskjava.fastagi.AgiException;

import java.io.IOException;
import java.net.*;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.sql.SQLException;

public  class Call  {

    private Bank    gateway;
    public  void    setgateway(Bank Gateway){
        gateway=Gateway;
    }
    public  Bank    getGateway() throws Exception {
        getServer();
        return gateway;
    }
    public  void    freeGateway() {
        gateway=null;
    }
    public  boolean connectedToGateway=false;
    public  void getServer() throws Exception {
        int c=0;
        while (c<2) {
            ConnectToServer();
            if (!connectedToGateway) c++;
            else break;
            Thread.sleep(1000);
        }
    }
    private int  ConnectToServer() {
        try {
            Util.printMessage("in Connection to gateway ...",false);
            setgateway((Bank) Naming.lookup("rmi://" + Util.GatewayIP + ":" + Util.GatewayPort + "/Gateway"));
            Util.printMessage("Connected To Gateway.",false);
            connectedToGateway=true;
            return 0;
        } catch (NotBoundException var2) {
            connectedToGateway=false;
            return -1;
        } catch (MalformedURLException var3) {
            connectedToGateway=false;
            return -2;
        } catch (RemoteException var4) {
            connectedToGateway=false;
            return -3;
        } catch (Exception var5) {
            connectedToGateway=false;
            return -4;
        }
    }

    public  String getMACAddressOfClient() throws SocketException {
        InetAddress ip;
        try {

            ip = InetAddress.getLocalHost();
            NetworkInterface network = NetworkInterface.getByInetAddress(ip);
            byte[] mac = network.getHardwareAddress();
            StringBuilder sb = new StringBuilder();
            for (int i = 0; i < mac.length; i++) {
                sb.append(String.format("%02X%s", mac[i], (i < mac.length - 1) ? "-" : ""));
            }
        return sb.toString();
        } catch (UnknownHostException e) {

            e.printStackTrace();
            return "!";

        } catch (SocketException e){

            e.printStackTrace();
            return "!";

        }
    }


    private boolean IsPan;
    public  void   setIsPan(boolean isPan){
        IsPan=isPan;
    }
    public  boolean getIsPan() {
        return IsPan;
    }

    private void  baArzePozesh() throws AgiException {
        getParentStart().Say.baArzePozesh();
    }

    public  Object submitRequestToGateway(Object object) throws IOException, AgiException, ServerNotActiveException{
        Bank b=null;
       // BaseAccountRequest baseAccountRequest=new BaseAccountRequest();

        try {
           // Util.printMessage("******************in request:",false);
            b=(Bank) Naming.lookup("rmi://" + Util.GatewayIP + ":" + Util.GatewayPort + "/Gateway");
            object=b.submitRequest(object);


        } catch (NotBoundException e) {
            Util.printMessage("1:"+e.toString(),false);
            baArzePozesh();

        } catch (MalformedURLException e) {
            Util.printMessage("2:"+e.toString(),false);
            baArzePozesh();

        } catch (RemoteException e) {
            Util.printMessage("3:"+e.toString(),false);
            baArzePozesh();

        } catch (InvalidParameterException e) {
            Util.printMessage("4:"+e.toString(),false);
            baArzePozesh();

        } catch (ResponseParsingException e) {
            Util.printMessage("5:"+e.toString(),false);
            baArzePozesh();

        } catch (SenderException e) {
            Util.printMessage("6:"+e.toString(),false);
            baArzePozesh();

        } catch (SQLException e) {
            Util.printMessage("7:"+e.toString(),false);
            baArzePozesh();
        } catch (Exception e){
            Util.printMessage("8:"+e.toString(),false);
        }


        return object;
    }

    private int StatusConnectionToServer;
    public  void   setStatusConnectionToServer(int statusConnectionToServer){
        StatusConnectionToServer=statusConnectionToServer;
    }
    public  int getStatusConnectionToServer() {
        return StatusConnectionToServer;
    }

    public strUtils strutils=new strUtils();

    private String CallerID;
    public  void   setCallerID(String callerID){
        CallerID=callerID;
    }
    public  String getCallerID() {
        return CallerID;
    }

    private String UniQID;
    public  void   setUniQID(String uniQID){
        UniQID=uniQID;
    }
    public  String getUniQID(){
        return UniQID;
    }

    private String ChannelName;
    public  void   setChannelName(String channelName){
        ChannelName=channelName;
    }
    public  String getChannelName(){
        return ChannelName;
    }

    private String DateOfCall;
    public  void   setDateOfCall(String dateOfCall){
        DateOfCall=dateOfCall;
    }
    public  String getDateOfCall(){
        return DateOfCall;
    }

    private String TimeOfCall;
    public  void   setTimeOfCall(String timeOfCall){
        TimeOfCall=timeOfCall;
    }
    public  String getTimeOfCall(){
        return TimeOfCall;
    }

    private String Account;
    public  void   setAccount(String account){
        Account=account;
    }
    public  String getAccount(){
        return Account;
    }

    private String Pan;
    public  void   setPan(String pan){
        Pan=pan;
    }
    public  String getPan(){
        return Pan;
    }

    private String Pin2;
    public  void   setPin2(String pin2){
        Pin2=pin2;
    }
    public  String getPin2(){
        return Pin2;
    }

    private String Pin;
    public  void   setPin(String pin){
        Pin=pin;
    }
    public  String getPin(){
        return Pin;
    }

    private String UserEntrance;
    public  void   setUserEntrance(String userEntrance){
        UserEntrance=userEntrance;
    }
    public  String getUserEntrance(){
        return UserEntrance;
    }

    private int KindOfRequest;
    public  void   setKindOfRequest(int kindOfRequest){
        KindOfRequest=kindOfRequest;
    }
    public  int getKindOfRequest(){
        return KindOfRequest;
    }


    private CallStatus   Status=new CallStatus();
    public  void         setStatus(String SetStatus){

    }
    public  CallStatus   getStatus(){
      return Status;
    }
    public  void         logStatus(CallStatus callStatus){

    }

    private Service_Start parentStart;
    public  void setParentStart(Service_Start Father){
        parentStart =Father;
    }
    public  Service_Start getParentStart(){
        return parentStart;
    }

    private Service_Account_Main parentAccount;
    public  void setparentAccount(Service_Account_Main ParentAccount){
        parentAccount =ParentAccount;
    }
    public  Service_Account_Main getparentAccount(){
        return parentAccount;
    }

    private Service_Pan parentPan;
    public  void setparentPan(Service_Pan ParentPan){
        parentPan =ParentPan;
    }
    public  Service_Pan getparentPan(){
        return parentPan;
    }

    private Service_Account_Special parentSpecialService;
    public  void setSpecialService(Service_Account_Special specialService){
        parentSpecialService =specialService;
    }
    public  Service_Account_Special getSpecialService(){
        return parentSpecialService;
    }



    private Object  Result=null;
    public  void    setResult(Object result){
        Result=result;
    }
    public Object   getResult(){
        return Result;
    }

    private String PersonType;
    public  void   setPersonType(String personType){
        PersonType=personType;
    }
    public  String getPersonType(){
        return PersonType;
    }

    private String Balance;
    public  void   setBalance(String balance){
        Balance=balance;
    }
    public  String getBalance(){
        return Balance;
    }

    private boolean isTejaratPan=false;
    public  void   setisTejaratPan(boolean IsTejaratPan){
        isTejaratPan=IsTejaratPan;
    }
    public  boolean getisTejaratPan(){
        return isTejaratPan;
    }

    private String CurrencyCode;
    public  void   setCurrencyCode(String currencyCode){
        CurrencyCode=currencyCode;
    }
    public  String getCurrencyCode(){
        return CurrencyCode;
    }

    private String ServiceType;
    public  void   setServiceType(String serviceType){
        ServiceType=serviceType;
    }
    public  String getServiceType(){
        return ServiceType;
    }

    private boolean CanBillPay=false;
    public  void    setCanBillPay(boolean canBillPay){
        CanBillPay=canBillPay;
    }
    public  boolean getCanBillPay(){
        return CanBillPay;
    }

    private String NameAndFamily;
    public  void   setNameAndFamily(String nameAndFamily){
        NameAndFamily=nameAndFamily;
    }
    public  String getNameAndFamily(){
        return NameAndFamily;
    }

    private String Branch;
    public  void   setBranch(String branch){
        Branch=branch;
    }
    public  String getBranch(){
        return Branch;
    }

    private String getReferenceCode(){

        return String.valueOf(System.nanoTime()).substring(0,12);

    }

    private String CallUniqID="";
    public  void   setCallUniqID(){
        CallUniqID=Util.ClientNo+getReferenceCode();
    }
    public  String getCallUniqID(){

        return this.CallUniqID;

    }


    public  class CallStatus{

        private boolean CallConnectionStatus;
        public  void    setCallConnectionStatus(boolean callConnectionStatus){
            CallConnectionStatus=callConnectionStatus;
        }
        public  boolean getCallConnectionStatus(){
            return CallConnectionStatus;
        }

        private int     Position;
        public  void    setPosition(int position){
            Position=position;
            setPositionDescription(position);
        }
        public  int     getPosition(){
            return  Position;
        }

        private String  PositionDescription;
        private void    setPositionDescription(int position){
            PositionDescription=createPositionString(position);
        }
        public  String  getPositionDescription(){
          return  PositionDescription;
        }
        private String  createPositionString(int position) {
             return null;
        }

        private String  Action;
        public  void    setActionOfPosition(String action){
           Action=action;
        }
        public  String  getActionOfPosition(){
            return  Action;
        }


    }
}
