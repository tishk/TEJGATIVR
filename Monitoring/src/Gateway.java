import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by root on 4/20/16.
 */
public class Gateway extends Status {

    private boolean IsPingStatus=false;
    public  void    setIsPingStatus(boolean IsPingStatus){
        this.IsPingStatus=IsPingStatus;
    }
    public  boolean getIsPingStatus(){
        return this.IsPingStatus;
    }

    public boolean  IsDBStatus=false;
    public  void    setIsDBStatus(boolean IsDBStatus){
        this.IsDBStatus=IsDBStatus;
    }
    public  boolean getIsDBStatus(){
        return this.IsDBStatus;
    }

    public boolean  IsRunningStatus=false;
    public  void    setIsRunningStatus(boolean IsRunningStatus){
        this.IsRunningStatus=IsRunningStatus;
    }
    public  boolean getIsRunningStatus(){
        return this.IsRunningStatus;
    }

    public boolean  IsAllStatus=false;
    public  void    setIsAllStatus(boolean IsAllStatus){
        this.IsAllStatus=IsAllStatus;
    }
    public  boolean getIsAllStatus(){
        return this.IsAllStatus;
    }



    public Gateway(String [] requestParams,String ServiceChoice){
        switch (ServiceChoice){
            case "01":PingStatus();
            case "02":RunningStatus();
            case "08":DBStatus();
            default:
        }
    }

    public Gateway (String [] requestParams){
       AllStatus();
    }

    private String  OperationCode="";
    public  void    setOperationCode(String operationCode){
        OperationCode=operationCode;
    }
    public  String  getOperationCode(){
        return OperationCode;
    }


    private void   AllStatus(){
        setIsAllStatus(true);
       // final ExecutorService executorService = Executors.newSingleThreadExecutor();
     //  Future future = executorService.submit(new Runnable() {
        // public void run() {
                try {
                    String ipAddress = Properties_Monitoring.getGateway_IP();
                    InetAddress inet = InetAddress.getByName(ipAddress);
                    if (inet.isReachable(2000)) {
                        setPingStatus("1");
                        setPingStatusActionCode("0000");
                        setPingStatusDesc("---");
                    }
                    else {
                        setPingStatus("0");
                        setPingStatusActionCode("3333");
                        setPingStatusDesc("---");
                    }
                    setPingStatusDateTime(getNowDateTime());
                    DBUtils dbUtils=new DBUtils(true);
                    if (dbUtils.connected){
                        setgDBConnected("1");
                        setgDBConnectedisOKActionCode("0000");
                        setgDBConnectedisOKDesc("---");
                    }
                    else {
                        setgDBConnected("0");
                        setgDBConnectedisOKActionCode("3333");
                        setgDBConnectedisOKDesc("---");
                    }
                    setgDBConnectedisOKDateTime(getNowDateTime());

                        try (Socket socket = new Socket()) {
                            socket.connect(new InetSocketAddress(
                                    Properties_Monitoring.getGateway_IP(),
                                    Integer.valueOf(Properties_Monitoring.getGateway_Port())),3000);
                            setAppRunning("1");
                            setAppRunningActionCode("0000");
                            setAppRunningDesc("---");
                            socket.close();

                        } catch (IOException e) {
                            setAppRunningActionCode("9126");
                            setAppRunningDesc("---");
                            setAppRunning("0");
                        }
                           setAppRunningDateTIme(getNowDateTime());
                  //  new DBUtils(this);
                } catch (Exception e) {
                    e.printStackTrace();
                   // executorService.shutdownNow();
                }finally {
                   //executorService.shutdownNow();
                }
          //  }
      //  });
   //     PingStatus();
     //   DBStatus();
      //  RunningStatus();
    }
    private void   PingStatus(){
        setIsPingStatus(true);
       // final ExecutorService executorService = Executors.newSingleThreadExecutor();
       // Future future = executorService.submit(new Runnable() {
          //  public void run() {
                try {
                    String ipAddress = Properties_Monitoring.getGateway_IP();
                    InetAddress inet = InetAddress.getByName(ipAddress);
                     if (inet.isReachable(2000)) {
                         setPingStatus("1");
                         setPingStatusActionCode("0000");
                         setPingStatusDesc("---");
                     }
                    else {
                         setPingStatus("0");
                         setPingStatusActionCode("9126");
                         setPingStatusDesc("---");
                     }
                    setPingStatusDateTime(getNowDateTime());
                   // new DBUtils(this);
                } catch (Exception e) {
                    e.printStackTrace();
               //     executorService.shutdownNow();
                }finally {
                //    executorService.shutdownNow();
                }
           // }
       // });
    }
    private void   DBStatus(){
        setIsDBStatus(true);
       // final ExecutorService executorService = Executors.newSingleThreadExecutor();
       // Future future = executorService.submit(new Runnable() {
        ///    public void run() {
                try {
                   DBUtils dbUtils=new DBUtils(true);
                    if (dbUtils.connected){
                        setgDBConnected("1");
                        setgDBConnectedisOKActionCode("0000");
                        setgDBConnectedisOKDesc("");
                    }
                    else {
                        setgDBConnected("0");
                        setgDBConnectedisOKActionCode("3333");
                        setgDBConnectedisOKDesc("");
                    }
                    setgDBConnectedisOKDateTime(getNowDateTime());

                 //  new DBUtils(this);
                } catch (Exception e) {
                    e.printStackTrace();
                   // executorService.shutdownNow();
                }finally {
                   // executorService.shutdownNow();
                }
         //   }
     //   });
    }
    private void   RunningStatus(){
        setIsRunningStatus(true);
       // final ExecutorService executorService = Executors.newSingleThreadExecutor();
      //  Future future = executorService.submit(new Runnable() {
         //   public void run() {
                try {
                    try (Socket socket = new Socket()) {
                        socket.connect(new InetSocketAddress(
                                       Properties_Monitoring.getGateway_IP(),
                                       Integer.valueOf(Properties_Monitoring.getGateway_Port())),3000);
                        setAppRunning("1");
                        setAppRunningActionCode("0000");
                        setAppRunningDesc("---");
                        socket.close();

                    } catch (IOException e) {
                       setAppRunning("0");
                        setAppRunningActionCode("9126");
                        setAppRunningDesc("---");

                    }
                  setAppRunningDateTIme(getNowDateTime());
                    //new DBUtils(this);
                } catch (Exception e) {
                    setAppRunning("0");
                    e.printStackTrace();
                //    executorService.shutdownNow();
                }finally {
                 //   executorService.shutdownNow();
                }
          //  }
       // });
    }



    private String  gDBConnected="";
    public  void    setgDBConnected(String DBConnected){
        gDBConnected=DBConnected;
    }
    public  String  getgDBConnected(){
        return gDBConnected;
    }

    private String  gDBConnectedisOKDateTime="";
    public  void    setgDBConnectedisOKDateTime(String DBConnectedisOKDateTime){
        gDBConnectedisOKDateTime=DBConnectedisOKDateTime;
    }
    public  String  getgDBConnectedisOKDateTime(){
        return gDBConnectedisOKDateTime;
    }

    private String  gDBConnectedisOKActionCode="";
    public  void    setgDBConnectedisOKActionCode(String DBConnectedisOKActionCode){
        gDBConnectedisOKActionCode=DBConnectedisOKActionCode;
    }
    public  String  getgDBConnectedisOKActionCode(){
        return gDBConnectedisOKActionCode;
    }

    private String  gDBConnectedisOKDesc="";
    public  void    setgDBConnectedisOKDesc(String DBConnectedisOKDesc){
        gDBConnectedisOKDesc=DBConnectedisOKDesc;
    }
    public  String  getgDBConnectedisOKDesc(){
        return gDBConnectedisOKDesc;
    }


}
