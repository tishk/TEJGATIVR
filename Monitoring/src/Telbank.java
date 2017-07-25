import SystemStatus.Status_All;

import java.io.*;
import java.net.*;
import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by root on 4/20/16.
 */
public class Telbank extends Status {
    private Socket socket =null;
    private String ip;

    private byte[] buf =null;
    private String RCVString ="";



    public Telbank(String[] params,boolean isOSStatus) throws SQLException, ClassNotFoundException, IOException {
        setIsTelBankStatus(!isOSStatus);
        initializeAccountParameters(params[2]);
        setTBClientID(params[3]);
        ip=getTelBankIP();
        if (isNetworkConnected()) telBankStatus();
    }

    private String  TBClientID="";
    public  void    setTBClientID(String tBClientID){
        TBClientID=tBClientID;
    }
    public  String  getTBClientID(){
        return TBClientID;
    }

    private boolean IsTelBankStatus=false;
    public  void    setIsTelBankStatus(boolean isTelBankStatus){
        IsTelBankStatus=isTelBankStatus;
    }
    public  boolean getIsTelBankStatus(){
        return IsTelBankStatus;
    }

    private String  TBGatewayConneted="";
    public  void    setTBGatewayConneted(String tBGatewayConneted){
        TBGatewayConneted=tBGatewayConneted;
    }
    public  String  getTBGatewayConneted(){
        return TBGatewayConneted;
    }

    private String  TBGatewayConnetedStatusDateTime="";
    public  void    setTBGatewayConnetedStatusDateTime(String tBGatewayConnetedStatusDateTime){
        TBGatewayConnetedStatusDateTime=tBGatewayConnetedStatusDateTime;
    }
    public  String  getTBGatewayConnetedStatusDateTime(){
        return TBGatewayConnetedStatusDateTime;
    }

    private String  TBGatewayConnetedStatusActionCode="";
    public  void    setTBGatewayConnetedStatusActionCode(String tBGatewayConnetedStatusActionCode){
        TBGatewayConnetedStatusActionCode=tBGatewayConnetedStatusActionCode;
    }
    public  String  getTBGatewayConnetedStatusActionCode(){
        return TBGatewayConnetedStatusActionCode;
    }

    private String  TBGatewayConnetedStatusDesc="";
    public  void    setTBGatewayConnetedStatusDesc(String tBGatewayConnetedStatusDesc){
        TBGatewayConnetedStatusDesc=tBGatewayConnetedStatusDesc;
    }
    public  String  getTBGatewayConnetedStatusDesc(){
        return TBGatewayConnetedStatusDesc;
    }

    public  String  getTelBankIP() throws SQLException, ClassNotFoundException {
        DBUtils dbUtils=new DBUtils(true);
       return dbUtils.getTelBankIP(getTBClientID());
    }
    public  void    telBankStatus() throws SQLException, ClassNotFoundException, IOException {

        String req="";
        if (getIsTelBankStatus()) req="giveMeTelBankStatus";
        else req="giveMeOSStatus";

        int port=Integer.valueOf(Properties_Monitoring.getTelbankMonitoringPort());

        try
        {
            Socket client = new Socket(ip, port);
            OutputStream outToServer = client.getOutputStream();
            DataOutputStream out = new DataOutputStream(outToServer);
            out.writeBytes(req);
          //  InputStream inFromServer = client.getInputStream();
         //   ObjectInputStream ois=new ObjectInputStream(inFromServer);

           // saveToDB((Status_All)ois.readObject());
            client.close();
        }catch(IOException e)
        {
            e.printStackTrace();
        }
    }
    public  boolean isNetworkConnected(){
        try {
            String ipAddress = ip;
            InetAddress inet = InetAddress.getByName(ipAddress);
            if (inet.isReachable(2000)) {
                setPingStatus("1");
                setPingStatusActionCode("0000");
                setPingStatusDesc("");
                setPingStatusDateTime(getNowDateTime());
                return true;
            }
            else {
                setPingStatus("0");
                setPingStatusActionCode("3333");
                setPingStatusDesc("");
                setPingStatusDateTime(getNowDateTime());
                return false;
            }

            // new DBUtils(this);
        } catch (Exception e) {
            return false;

        }
    }

    public  void    telBankStatus_() {

        String req="";
        if (getIsTelBankStatus()) req="giveMeTelBankStatus";
        else req="giveMeOSStatus";
        String ip= null;
        try {
            ip = getTelBankIP();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        int port=Integer.valueOf(Properties_Monitoring.getTelbankMonitoringPort());


        Socket s = null;
        try {
            s = new Socket(ip,port);
            OutputStream os = s.getOutputStream();
            ObjectOutputStream oos = new ObjectOutputStream(os);
            Status_All to = new Status_All();
            oos.writeObject(to);

            oos.close();
            os.close();
            s.close();

        } catch (IOException e) {
            e.printStackTrace();
        }
            }

    private void processGottResponse(String response){
         if (getIsTelBankStatus()) {
             processTelBankStatus();
         }
         else{
             processOSStatus();
         }
    }

    private void processNotGotResponse() throws SQLException, ClassNotFoundException {
        setPingStatus("0");
        setPingStatusActionCode("9126");
        setPingStatusDateTime(getNowDateTime());
        new DBUtils(this);
    }

    private void freeResources(){
        socket =null;

        buf =null;
        RCVString ="";
        System.gc();
    }

    private void processTelBankStatus(){
        try{
            String[] telStatus=RCVString.split("$");
            setTBClientID(telStatus[0]);

            setPingStatus(telStatus[1]);
            setPingStatusDateTime(telStatus[4]);
            setPingStatusDesc("---");
            setPingStatusActionCode("0000");

            setAppRunning(telStatus[2]);
            setAppRunningDateTIme(telStatus[4]);
            setAppRunningDesc("---");
            setAppRunningActionCode("0000");


            setTBGatewayConneted(telStatus[3]);
            setTBGatewayConnetedStatusDateTime(telStatus[4]);
            setTBGatewayConnetedStatusDesc("---");
            setTBGatewayConnetedStatusActionCode("0000");

           // new DBUtils(this);
        }catch (Exception e){
            System.out.println("Error on pharse Message from client ID:"+getTBClientID());
        }





    }
    private void processOSStatus(){

    }

    private void saveToDB(final Status_All status_all){
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future future = executorService.submit(new Runnable() {
            public void run() {
                try {

                    new DBUtils(status_all);
                } catch (Exception e) {
                    e.printStackTrace();
                    executorService.shutdownNow();
                }finally {
                    executorService.shutdownNow();
                }
            }
        });
    }


}
