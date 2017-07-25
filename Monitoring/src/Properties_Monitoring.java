import ServiceObjects.Other.loggerToFile;

import java.io.File;
import java.io.FileInputStream;
import java.sql.SQLException;

/**
 * Created by root on 4/27/16.
 */
public class Properties_Monitoring {

    public Properties_Monitoring(){
        try {
            readConfig();
            readTelBanksList();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  void readConfig() throws Exception {

        final File FileOfSettings=new File(Properties_Monitoring.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        String Path1 =FileOfSettings.toString();
        int i="Monitoring.jar".length();


        java.util.Properties props = new java.util.Properties();
        try{
            props.load(new FileInputStream(Path1+"/monitoring.properties"));
        }catch (Exception e){
            try{
                Path =Path1.substring(0,Path1.length()-i);
                props.load(new FileInputStream(Path+"/monitoring.properties"));//if jar file in running
            }catch (Exception ee){
                return;
            }//if running class
        }
        try
        {

            setMonitoring_listener_IP(props.getProperty("monitoring_Listener_IP"));
            setMonitoring_listener_Port(props.getProperty("monitoring_Listener_Port"));
            setMonitoring_WebListener_IP(props.getProperty("monitoring_WebListener_IP"));
            setMonitoring_WebListener_Port(props.getProperty("monitoring_WebListener_Port"));

            setGateway_IP(props.getProperty("gateway_IP"));
            setGateway_Port(props.getProperty("gateway_Port"));

            setDBMonitoring_listener_IP(props.getProperty("DBmonitoring_Listener_IP"));
            setDBMonitoring_listener_Port(props.getProperty("DBmonitoring_Listener_Port"));


            setOracle_DB_URL(props.getProperty("Oracle_DB_URL"));
            setOracle_DB_UserName(props.getProperty("Oracle_DB_UserName"));
            setOracle_DB_Password(props.getProperty("Oracle_DB_Password"));

            setSqlServer_DB_DRIVER(props.getProperty("SqlServer_DB_DRIVER"));
            setSqlServer_DB_URL(props.getProperty("SqlServer_DB_URL"));
            setSqlServer_DB_USER(props.getProperty("SqlServer_DB_USER"));
            setSqlServer_DB_PASSWORD(props.getProperty("SqlServer_DB_PASSWORD"));
            setSqlServer_DB_Name(props.getProperty("SqlServer_DB_Name"));

            setTelbankMonitoringPort(props.getProperty("telbankMonitoringPort"));


        }
        catch(Exception e)
        {

        }


    }
    public  void readTelBanksList() throws SQLException, ClassNotFoundException {
        DBUtils dbUtils=new DBUtils(true);
        setTelbankList(dbUtils.getTelBankList());
    }

    private String Path=null;
    public  void   setPath(String path){
        Path=path;
    }
    public  String getPath(){
        return Path;
    }

    private static String monitoring_listener_IP=null;
    public  void   setMonitoring_listener_IP(String Monitoring_listener_IP){
        monitoring_listener_IP=Monitoring_listener_IP;
    }
    public  static String getMonitoring_listener_IP(){
        return monitoring_listener_IP;
    }

    private static String Monitoring_listener_Port=null;
    public  void   setMonitoring_listener_Port(String monitoring_listener_Port){
        Monitoring_listener_Port=monitoring_listener_Port;
    }
    public  static String getMonitoring_listener_Port(){
        return Monitoring_listener_Port;
    }

    private static String DBMonitoring_listener_IP=null;
    public  void   setDBMonitoring_listener_IP(String DBmonitoring_listener_IP){
        DBMonitoring_listener_IP=DBmonitoring_listener_IP;
    }
    public  static String getDBMonitoring_listener_IP(){
        return DBMonitoring_listener_IP;
    }

    private static String DBMonitoring_listener_Port=null;
    public  void   setDBMonitoring_listener_Port(String DBmonitoring_listener_Port){
        DBMonitoring_listener_Port=DBmonitoring_listener_Port;
    }
    public  static String getDBMonitoring_listener_Port(){
        return DBMonitoring_listener_Port;
    }


    private static String monitoring_WebListener_IP=null;
    public  void   setMonitoring_WebListener_IP(String Monitoring_WebListener_IP){
        monitoring_WebListener_IP=Monitoring_WebListener_IP;
    }
    public  static String getMonitoring_WebListener_IP(){
        return monitoring_WebListener_IP;
    }

    private static String Monitoring_WebListener_Port=null;
    public  void   setMonitoring_WebListener_Port(String monitoring_WebListener_Port){
        Monitoring_WebListener_Port=monitoring_WebListener_Port;
    }
    public  static String getMonitoring_WebListener_Port(){
        return Monitoring_WebListener_Port;
    }


    private static String Gateway_IP=null;
    public  void   setGateway_IP(String gateway_IP){
        Gateway_IP=gateway_IP;
    }
    public  static String getGateway_IP(){
        return Gateway_IP;
    }

    private static String Gateway_Port=null;
    public  void   setGateway_Port(String gateway_Port){
        Gateway_Port=gateway_Port;
    }
    public  static String getGateway_Port(){
        return Gateway_Port;
    }

    private static String Oracle_DB_URL=null;
    public  void   setOracle_DB_URL(String oracle_DB_URL){
        Oracle_DB_URL=oracle_DB_URL;
    }
    public   static String getOracle_DB_URL(){
        return Oracle_DB_URL;
    }

    private static String Oracle_DB_UserName=null;
    public  void   setOracle_DB_UserName(String oracle_DB_UserName){
        Oracle_DB_UserName=oracle_DB_UserName;
    }
    public  static String getOracle_DB_UserName(){
        return Oracle_DB_UserName;
    }

    private static String Oracle_DB_Password=null;
    public  void   setOracle_DB_Password(String oracle_DB_Password){
        Oracle_DB_Password=oracle_DB_Password;
    }
    public  static String getOracle_DB_Password(){
        return Oracle_DB_Password;
    }

    private static String SqlServer_DB_DRIVER=null;
    public  void   setSqlServer_DB_DRIVER(String SqlServer_DB_DRIVER){
        this.SqlServer_DB_DRIVER=SqlServer_DB_DRIVER;
    }
    public   static String getSqlServer_DB_DRIVER(){
        return SqlServer_DB_DRIVER;
    }

    private static String SqlServer_DB_URL=null;
    public  void   setSqlServer_DB_URL(String SqlServer_DB_URL){
        this.SqlServer_DB_URL=SqlServer_DB_URL;
    }
    public  static String getSqlServer_DB_URL(){
        return SqlServer_DB_URL;
    }

    private static String SqlServer_DB_USER=null;
    public  void   setSqlServer_DB_USER(String sqlServer_DB_USER){
        SqlServer_DB_USER=sqlServer_DB_USER;
    }
    public  static String getSqlServer_DB_USER(){
        return SqlServer_DB_USER;
    }

    private static String SqlServer_DB_PASSWORD=null;
    public  void   setSqlServer_DB_PASSWORD(String sqlServer_DB_PASSWORD){
        SqlServer_DB_PASSWORD=sqlServer_DB_PASSWORD;
    }
    public  static String getSqlServer_DB_PASSWORD(){
        return SqlServer_DB_PASSWORD;
    }

    private static String SqlServer_DB_Name=null;
    public  void   setSqlServer_DB_Name(String sqlServer_DB_Name){
        SqlServer_DB_Name=sqlServer_DB_Name;
    }
    public  static String getSqlServer_DB_Name(){
        return SqlServer_DB_Name;
    }

    private static String TelbankList[]=null;
    public  void   setTelbankList(String telbankList[]){
        TelbankList=telbankList;
    }
    public  static String[] getTelbankList(){
        return TelbankList;
    }

    private static String TelbankMonitoringPort;
    public  void   setTelbankMonitoringPort(String telbankMonitoringPort){
        TelbankMonitoringPort=telbankMonitoringPort;
    }
    public  static String getTelbankMonitoringPort(){
        return TelbankMonitoringPort;
    }



}
