package SystemStatus;

import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by root on 4/20/16.
 */
public class Status_All implements Serializable {

    public Status_All(){

    }

    private Status_TelBank status_telBank=new Status_TelBank();
    public  void  setStatus_telBank(Status_TelBank status_telBank){
        this.status_telBank=status_telBank;
    }
    public Status_TelBank  getStatus_TelBank(){
        return status_telBank;
    }

    private boolean  IsTelBankStatus=false;
    public  void    setIsTelBankStatus(boolean isTelBankStatus){
        IsTelBankStatus=isTelBankStatus;
    }
    public  boolean getIsTelBankStatus(){
        return IsTelBankStatus;
    }

    private boolean  IsTelBankOSStatus=false;
    public  void    setIsTelBankOSStatus(boolean isTelBankOSStatus){
        IsTelBankOSStatus=isTelBankOSStatus;
    }
    public  boolean getIsTelBankOSStatus(){
        return IsTelBankOSStatus;
    }

    private boolean  IsDataBaseServer=false;
    public  void    setIsDataBaseServer(boolean IsDataBaseServer){
        this.IsDataBaseServer=IsDataBaseServer;
    }
    public  boolean getIsDataBaseServer(){
        return IsDataBaseServer;
    }

    private boolean  IsOSOfGateway=false;
    public  void    setIsOSOfGateway(boolean isOSOfGateway){
        IsOSOfGateway=isOSOfGateway;
    }
    public  boolean getIsOSOfGateway(){
        return IsOSOfGateway;
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
            String accparams[]=accParams.split("$");
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
        AppRunning=appRunning;
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

    private String  CPUUsingTime="";
    public  void    setCPUUsingTime(String cPUUsingTime){
        CPUUsingTime=cPUUsingTime;
    }
    public  String  getCPUUsingTime(){
        return CPUUsingTime;
    }

    private String  CPUFreeTime="";
    public  void    setCPUFreeTime(String cPUFreeTime){
        CPUFreeTime=cPUFreeTime;
    }
    public  String  getCPUFreeTime(){
        return CPUFreeTime;
    }

    private String  CPUUsingPercent="";
    public  void    setCPUUsingPercent(String cPUUsingPercent){
        CPUUsingPercent=cPUUsingPercent;
    }
    public  String  getCPUUsingPercent(){
        return CPUUsingPercent;
    }

    private String  CPUFreePercent="";
    public  void    setCPUFreePercent(String cPUFreePercent){
        CPUFreePercent=cPUFreePercent;
    }
    public  String  getCPUFreePercent(){
        return CPUFreePercent;
    }

    private String  RAMAllSpace="";
    public  void    setRAMAllSpace(String rAMAllSpace){
        RAMAllSpace=rAMAllSpace;
    }
    public  String  getRAMAllSpace(){
        return RAMAllSpace;
    }

    private String  RAMUsingSpace="";
    public  void    setRAMUsingSpace(String rAMUsingSpace){
        RAMUsingSpace=rAMUsingSpace;
    }
    public  String  getRAMUsingSpace(){
        return RAMUsingSpace;
    }

    private String  RAMFreeSpace="";
    public  void    setRAMFreeSpace(String rAMFreeSpace){
        RAMFreeSpace=rAMFreeSpace;
    }
    public  String  getRAMFreeSpace(){
        return RAMFreeSpace;
    }

    private String  RAMUsingPercent="";
    public  void    setRAMUsingPercent(String rAMUsingPercent){
        RAMUsingPercent=rAMUsingPercent;
    }
    public  String  getRAMUsingPercent(){
        return RAMUsingPercent;
    }

    private String  RAMFreePercent="";
    public  void    setRAMFreePercent(String rAMFreePercent){
        RAMFreePercent=rAMFreePercent;
    }
    public  String  getRAMFreePercent(){
        return RAMFreePercent;
    }

    private String  NetDownloadRate="";
    public  void    setNetDownloadRate(String netDownloadRate){
        NetDownloadRate=netDownloadRate;
    }
    public  String  getNetDownloadRate(){
        return NetDownloadRate;
    }

    private String  NetUploadRate="";
    public  void    setNetUploadRate(String netUploadRate){
        NetUploadRate=netUploadRate;
    }
    public  String  getNetUploadRate(){
        return NetUploadRate;
    }

    private String  NetAdapterInfo="";
    public  void    setNetAdapterInfo(String netAdapterInfo){
        NetAdapterInfo=netAdapterInfo;
    }
    public  String  getNetAdapterInfo(){
        return NetAdapterInfo;
    }

    private String  SwapAllSpace="";
    public  void    setSwapAllSpace(String swapAllSpace){
        SwapAllSpace=swapAllSpace;
    }
    public  String  getSwapAllSpace(){
        return SwapAllSpace;
    }

    private String  SwapUsingSpace="";
    public  void    setSwapUsingSpace(String swapUsingSpace){
        SwapUsingSpace=swapUsingSpace;
    }
    public  String  getSwapUsingSpace(){
        return SwapUsingSpace;
    }

    private String  SwapFreeSpace="";
    public  void    setSwapFreeSpace(String swapFreeSpace){
        SwapFreeSpace=swapFreeSpace;
    }
    public  String  getSwapFreeSpace(){
        return SwapFreeSpace;
    }

    private String  SwapUsingPercent="";
    public  void    setSwapUsingPercent(String swapUsingPercent){
        SwapUsingPercent=swapUsingPercent;
    }
    public  String  getSwapUsingPercent(){
        return SwapUsingPercent;
    }

    private String  SwapFreePercent="";
    public  void    setSwapFreePercent(String swapFreePercent ){
        SwapFreePercent=swapFreePercent;
    }
    public  String  getSwapFreePercent(){
        return SwapFreePercent;
    }

    private String  DiskAllSpace="";
    public  void    setDiskAllSpace(String diskAllSpace){
        DiskAllSpace=diskAllSpace;
    }
    public  String  getDiskAllSpace(){
        return DiskAllSpace;
    }

    private String  DiskUsingSpace="";
    public  void    setDiskUsingSpacentv(String diskUsingSpace){
        DiskUsingSpace=diskUsingSpace;
    }
    public  String  getDiskUsingSpace(){
        return DiskUsingSpace;
    }

    private String  DiskFreeSpace="";
    public  void    setDiskFreeSpace(String diskFreeSpace ){
        DiskFreeSpace=diskFreeSpace;
    }
    public  String  getDiskFreeSpace(){
        return DiskFreeSpace;
    }

    private String  ClientID= null;
    public  void    setClientID(String clientID){
        ClientID=clientID;
    }
    public  String  getClientID(){
        return ClientID;
    }

    private String  ConnectionToGateway="";
    public  void    setConnectionToGateway(String ConnectionToGateway){
        this.ConnectionToGateway=ConnectionToGateway;
    }
    public  String  getConnectionToGateway(){
        return ConnectionToGateway;
    }

    private String  ConnectionToGatewayActionCode="";
    public  void    setConnectionToGatewayActionCode(String ConnectionToGatewayActionCode){
        this.ConnectionToGatewayActionCode=ConnectionToGatewayActionCode;
    }
    public  String  getConnectionToGatewayActionCode(){
        return this.ConnectionToGatewayActionCode;
    }

    private String  ConnectionToGatewayDateTime="";
    public  void    setConnectionToGatewayDateTime(String ConnectionToGatewayDateTime){
        this.ConnectionToGatewayDateTime=ConnectionToGatewayDateTime;
    }
    public  String  getConnectionToGatewayDateTime(){
        return this.ConnectionToGatewayDateTime;
    }

    private String  ConnectionToGatewayDesc="";
    public  void    setConnectionToGatewayDesc(String ConnectionToGatewayDesc){
        this.ConnectionToGatewayDesc=ConnectionToGatewayDesc;
    }
    public  String  getConnectionToGatewayDesc(){
        return this.ConnectionToGatewayDesc;
    }

    public void    processSystemStatus() throws  InterruptedException {
        getStatus_CPU();
        getStatus_DISK();
        getStatus_NETWORK();
        getStatus_RAM();
        getStatus_SWAP();
    }

    private void    getStatus_CPU(){
     //   final ExecutorService executorService = Executors.newSingleThreadExecutor();
      //  final Future future = executorService.submit(new Runnable() {
           // public void run() {
                try {
                    Status_CPU status_cpu=new Status_CPU();
                    setCPUFreePercent(status_cpu.getCPUIdleTimePercent());
                    setCPUFreeTime(status_cpu.getCpuIdleTime());
                    setCPUUsingPercent(status_cpu.getCPUWorkTimePercent());
                    setCPUUsingTime(status_cpu.getCpuWorkTime());
                    status_cpu=null;
                } catch (Exception e) {

                    e.printStackTrace();
                    //executorService.shutdownNow();
                }finally {
                   // executorService.shutdownNow();

                }
          //  }
      //  });

    }
    private void    getStatus_DISK(){
       // final ExecutorService executorService = Executors.newSingleThreadExecutor();
      //  final Future future = executorService.submit(new Runnable() {
         //   public void run() {
                try {
                    Status_Disk status_disk=new Status_Disk();
                    setDiskAllSpace(status_disk.getTotalSpace());
                    setDiskUsingSpacentv(status_disk.getUsedSpace());
                    setDiskFreeSpace(status_disk.getFreeSpace());
                    status_disk=null;
                } catch (Exception e) {
                    e.printStackTrace();
                  //  executorService.shutdownNow();
                }finally {
                //    executorService.shutdownNow();

                }
          //  }
       // });
    }
    private void    getStatus_RAM(){
       // final ExecutorService executorService = Executors.newSingleThreadExecutor();
      //  final Future future = executorService.submit(new Runnable() {
        //    public void run() {
                try {
                    Status_Memory status_memory=new Status_Memory();
                    setRAMAllSpace(status_memory.getTotal());
                    setRAMFreeSpace(status_memory.getActualFree());
                    setRAMUsingSpace(status_memory.getActualUsed());
                    setRAMFreePercent(status_memory.getFreePercent());
                    setRAMUsingPercent(status_memory.getUsedPercent());
                    status_memory=null;
                } catch (Exception e) {
                    e.printStackTrace();
                   // executorService.shutdownNow();
                }finally {
                 //   executorService.shutdownNow();

                }
           // }
      //  });
    }
    private void    getStatus_NETWORK(){
     //   final ExecutorService executorService = Executors.newSingleThreadExecutor();
      //  final Future future = executorService.submit(new Runnable() {
        //    public void run() {
                try {
                    Status_Network status_network=new Status_Network();
                    setNetDownloadRate(status_network.getDownloadRate());
                    setNetUploadRate(status_network.getUploadRate());
                    setNetAdapterInfo(status_network.getNetworkData());
                    status_network=null;
                } catch (Exception e) {
                    e.printStackTrace();
                   // executorService.shutdownNow();
                }finally {
                   // executorService.shutdownNow();

                }
          //  }
     //   });
    }
    private void    getStatus_SWAP(){
       // final ExecutorService executorService = Executors.newSingleThreadExecutor();
      //  final Future future = executorService.submit(new Runnable() {
         //   public void run() {
                try {
                    Status_Swap status_swap=new Status_Swap();
                    setSwapAllSpace(status_swap.getActualTotal());
                    setSwapFreeSpace(status_swap.getActualFree());
                    setSwapUsingSpace(status_swap.getActualUsed());
                    setSwapFreePercent(status_swap.getFreePercent());
                    setSwapUsingPercent(status_swap.getUsedPercent());
                } catch (Exception e) {
                    e.printStackTrace();
                 //   executorService.shutdownNow();
                }finally {
                  //  executorService.shutdownNow();

                }
           // }
      //  });
    }





}
