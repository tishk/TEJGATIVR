package utils;

import ServiceObjects.Other.loggerToFile;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

/**
 * Created by Administrator on 13/07/2015.
 */
public class PropertiesUtils {

    public PropertiesUtils(){
        try {
            readConfig();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  void readConfig() throws Exception {

        final File FileOfSettings=new File(PropertiesUtils.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        String Path1 =FileOfSettings.toString();
        int i="Gateway.jar".length();
        Path =Path1.substring(0,Path1.length()-i);
        setProjectPath(Path);
        Properties props = new Properties();
      //  System.out.print("path:"+Path+"gateway.properties");
        try{
            props.load(new FileInputStream(Path+"gateway.properties")); //if jar file in running
            setLogConfigurePath(Path+ "log4j.properties");
        }catch (Exception e){
            try{
                Path =Path1+"/utils";

                props.load(new FileInputStream(Path+"/gateway.properties"));// if class in running
                setLogConfigurePath(Path+ "/log4j.properties");
            }catch (Exception ee){}
        }


        try
        {
          //  props.load(new FileInputStream(Path+"gateway.properties"));
            setDataBaseURL(props.getProperty("DataBaseURL"));
            setDataBaseUserName(props.getProperty("DataBaseUserName"));
            setDataBasePassword(props.getProperty("DataBasePassword"));
            setSQL_DB_DRIVER(props.getProperty("SQL_DB_DRIVER"));
            setSQL_DB_URL(props.getProperty("SQL_DB_URL"));
            setSQL_DB_USER(props.getProperty("SQL_DB_USER"));
            setSQL_DB_PASSWORD(props.getProperty("SQL_DB_PASSWORD"));
            setMerchandID(props.getProperty("MerchandID"));
            setMerchandCode(props.getProperty("MerchandCode"));
            setBaseRequestMessage_ChannelID(props.getProperty("BaseRequestMessage_ChannelID"));
            setBaseRequestMessage_ChannelPass(props.getProperty("BaseRequestMessage_ChannelPass"));
            setBaseRequestMessage_EncAlgorytm(props.getProperty("BaseRequestMessage_EncAlgorytm"));
            setBaseRequestMessage_Channeltype(props.getProperty("BaseRequestMessage_Channeltype"));
            setBaseRequestMessage_MAC(props.getProperty("BaseRequestMessage_MAC"));
            setSabaHost(props.getProperty("sabaHost"));
            setSabaPort(props.getProperty("sabaPort"));
            setThisHostForSaba(props.getProperty("thisHostForSaba"));
            setThisPortForSaba(props.getProperty("thisPortForSaba"));
            setTelSwitchHost(props.getProperty("TelSwitchHost"));
            setTelSwitchPort(props.getProperty("TelSwitchPort"));
            setThisPortForTelSwitch(props.getProperty("ThisPortForTelSwitch"));
            setClientJarUseOldAuthenticate(props.getProperty("ClientJarUseOldAuthenticate"));
            setClientJarUseOldGateway(props.getProperty("ClientJarUseOldGateway"));
            setClientJarUseOldPayment(props.getProperty("ClientJarUseOldPayment"));
            setClientJarUseOldTelSwithch(props.getProperty("ClientJarUseOldTelSwithch"));

            if (props.getProperty("UseOldAuthenticatePin1").equals("1")) setUseOldAuthenticatePin1(true);
            else setUseOldAuthenticatePin1(false);

            if (props.getProperty("UseOldAuthenticatePin2").equals("1")) setUseOldAuthenticatePin2(true);
            else setUseOldAuthenticatePin2(false);

            if (props.getProperty("UseOldPaymentSystem").equals("1")) setUseOldPaymentSystem(true);
            else setUseOldPaymentSystem(false);

            if (props.getProperty("UseExtraInfo").equals("1"))  setUseExtraInfo(true);
            else setUseExtraInfo(false);

            if (props.getProperty("SaveXMLRequestAndResponseInDB").equals("1")) setSaveXMLRequestAndResponseInDB(true);
            else setSaveXMLRequestAndResponseInDB(false);

            setUseQueueForLog(props.getProperty("useQueueForLog"));
            setQueueLoggerIntervalTime(props.getProperty("queueLoggerIntervalTime"));

        }
        catch(Exception e)
        {
           loggerToFile.getInstance().logError(loggerToFile.getClassName(), loggerToFile.getLineNumber(),e.getMessage());
        }


    }

    private static String useQueueForLog;
    private static String queueLoggerIntervalTime;

    public static String getUseQueueForLog() {
        return useQueueForLog;
    }

    public void setUseQueueForLog(String useQueueForLog) {
        this.useQueueForLog = useQueueForLog;
    }

    public static String getQueueLoggerIntervalTime() {
        return queueLoggerIntervalTime;
    }

    public void setQueueLoggerIntervalTime(String queueLoggerIntervalTime) {
        this.queueLoggerIntervalTime = queueLoggerIntervalTime;
    }

    private String Path=null;
    public  void   setPath(String path){
        Path=path;
    }
    public  String getPath(){
        return Path;
    }

    private String OS=null;
    public  void   setOS(String oS){
        OS=oS;
    }
    public  synchronized String getOS(){
        return OS;
    }

    private static String LogConfigurePath=null;
    public  void   setLogConfigurePath(String logConfigurePath){
        LogConfigurePath=logConfigurePath;
    }
    public  synchronized static String getLogConfigurePath(){
        return LogConfigurePath;
    }

    private static String clientJarUseOldPayment="1";
    private static String clientJarUseOldAuthenticate="1";
    private static String clientJarUseOldTelSwithch="1";
    private static String clientJarUseOldGateway="1";

    public static boolean getClientJarUseOldPayment() {
        return clientJarUseOldPayment.equals("1");
    }

    public static void setClientJarUseOldPayment(String clientJarUseOldPayment) {
        PropertiesUtils.clientJarUseOldPayment = clientJarUseOldPayment;
    }

    public static boolean getClientJarUseOldAuthenticate() {
       return clientJarUseOldAuthenticate.equals("1");
    }

    public static void setClientJarUseOldAuthenticate(String clientJarUseOldAuthenticate) {
        PropertiesUtils.clientJarUseOldAuthenticate = clientJarUseOldAuthenticate;
    }

    public static boolean getClientJarUseOldTelSwithch() {
        return clientJarUseOldTelSwithch.equals("1");
    }

    public static void setClientJarUseOldTelSwithch(String clientJarUseOldTelSwithch) {
        PropertiesUtils.clientJarUseOldTelSwithch = clientJarUseOldTelSwithch;
    }

    public static boolean getClientJarUseOldGateway() {
        return clientJarUseOldGateway.equals("1");
    }

    public static void setClientJarUseOldGateway(String clientJarUseOldGateway) {
        PropertiesUtils.clientJarUseOldGateway = clientJarUseOldGateway;
    }

    private static Boolean SaveXMLRequestAndResponseInDB=null;
    public  void   setSaveXMLRequestAndResponseInDB(Boolean saveXMLRequestAndResponseInDB){
        SaveXMLRequestAndResponseInDB=saveXMLRequestAndResponseInDB;
    }
    public  synchronized static Boolean getSaveXMLRequestAndResponseInDB(){
        return SaveXMLRequestAndResponseInDB;
    }

    private static String DataBaseURL=null;
    public  void   setDataBaseURL(String dataBaseURL){
        DataBaseURL=dataBaseURL;
    }
    public  static String getDataBaseURL(){
        return DataBaseURL;
    }

    private static String DataBasePassword=null;
    public  void   setDataBasePassword(String dataBasePassword){
        DataBasePassword=dataBasePassword;
    }
    public  synchronized static String getDataBasePassword(){
        return DataBasePassword;
    }

    private static String DataBaseUserName=null;
    public  void   setDataBaseUserName(String dataBaseUserName){
        DataBaseUserName=dataBaseUserName;
    }
    public  synchronized static String getDataBaseUserName(){
        return DataBaseUserName;
    }

    private static String MerchandID=null;
    public  void   setMerchandID(String merchandID){
        MerchandID=merchandID;
    }
    public  synchronized static String getMerchandID(){
        return MerchandID;
    }

    private static String MerchandCode=null;
    public  void   setMerchandCode(String merchandCode){
        MerchandCode=merchandCode;
    }
    public  synchronized static String getMerchandCode(){
        return MerchandCode;
    }

    private static String BaseRequestMessage_Channeltype=null;
    public  void   setBaseRequestMessage_Channeltype(String baseRequestMessage_Channeltype){
        BaseRequestMessage_Channeltype=baseRequestMessage_Channeltype;
    }
    public  synchronized static String getBaseRequestMessage_Channeltype(){
        return BaseRequestMessage_Channeltype;
    }

    private static boolean UseExtraInfo=false;
    public  void   setUseExtraInfo(boolean useExtraInfo){
        UseExtraInfo=useExtraInfo;
    }
    public  synchronized static boolean getUseExtraInfo(){
        return UseExtraInfo;
    }

    private static String BaseRequestMessage_ChannelPass=null;
    public  void   setBaseRequestMessage_ChannelPass(String baseRequestMessage_ChannelPass){
        BaseRequestMessage_ChannelPass=baseRequestMessage_ChannelPass;
    }
    public  synchronized static String getBaseRequestMessage_ChannelPass(){
        return BaseRequestMessage_ChannelPass;
    }

    private static String BaseRequestMessage_ChannelID=null;
    public  void   setBaseRequestMessage_ChannelID(String baseRequestMessage_ChannelID){
        BaseRequestMessage_ChannelID=baseRequestMessage_ChannelID;
    }
    public  synchronized static String getBaseRequestMessage_ChannelID(){
        return BaseRequestMessage_ChannelID;
    }

    private static String BaseRequestMessage_EncAlgorytm=null;
    public  void   setBaseRequestMessage_EncAlgorytm(String baseRequestMessage_EncAlgorytm){
        BaseRequestMessage_EncAlgorytm=baseRequestMessage_EncAlgorytm;
    }
    public  synchronized static String getBaseRequestMessage_EncAlgorytm(){
        return BaseRequestMessage_EncAlgorytm;
    }

    private static String BaseRequestMessage_MAC=null;
    public  void   setBaseRequestMessage_MAC(String baseRequestMessage_MAC){
        BaseRequestMessage_MAC=baseRequestMessage_MAC;
    }
    public  synchronized static String getBaseRequestMessage_MAC(){
        return BaseRequestMessage_MAC;
    }

    private static String SQL_DB_DRIVER=null;
    public  void   setSQL_DB_DRIVER(String SQL_dB_DRIVER){
        SQL_DB_DRIVER=SQL_dB_DRIVER;
    }
    public  synchronized static String getSQL_DB_DRIVER(){
        return SQL_DB_DRIVER;
    }

    private static String SQL_DB_URL=null;
    public  void   setSQL_DB_URL(String sQL_DB_URL){
        SQL_DB_URL=sQL_DB_URL;
    }
    public  synchronized static String getSQL_DB_URL(){
        return SQL_DB_URL;
    }

    private static String SQL_DB_USER=null;
    public  void   setSQL_DB_USER(String SQL_dB_USER){
        SQL_DB_USER=SQL_dB_USER;
    }
    public  synchronized static String getSQL_DB_USER(){
        return SQL_DB_USER;
    }

    private static String SQL_DB_PASSWORD=null;
    public  void   setSQL_DB_PASSWORD(String SQL_dB_PASSWORD){
        SQL_DB_PASSWORD=SQL_dB_PASSWORD;
    }
    public  synchronized static String getSQL_DB_PASSWORD(){
        return SQL_DB_PASSWORD;
    }

    private static String SabaHost=null;
    public  void   setSabaHost(String sabaHost){
        SabaHost=sabaHost;
    }
    public  synchronized static String getSabaHost(){
        return SabaHost;
    }

    private static String SabaPort=null;
    public  synchronized static String getSabaPort(){
        return SabaPort;
    }
    public  void   setSabaPort(String sabaPort){
        SabaPort=sabaPort;
    }

    private static String ThisHostForSaba=null;
    public  void   setThisHostForSaba(String thisHostForSaba){
        ThisHostForSaba=thisHostForSaba;
    }
    public  synchronized static String getThisHostForSaba(){
        return ThisHostForSaba;
    }

    private static String ThisPortForSaba=null;
    public  void   setThisPortForSaba(String thisPortForSaba){
        ThisPortForSaba=thisPortForSaba;
    }
    public  synchronized static String getThisPortForSaba(){
        return ThisPortForSaba;
    }

    private static String TelSwitchHost=null;
    public  void   setTelSwitchHost(String telSwitchHost){
        TelSwitchHost=telSwitchHost;
    }
    public  synchronized static String getTelSwitchHost(){
        return TelSwitchHost;
    }

    private static String TelSwitchPort=null;
    public  void   setTelSwitchPort(String telSwitchPort){
        TelSwitchPort=telSwitchPort;
    }
    public  synchronized static String getTelSwitchPort(){
        return TelSwitchPort;
    }

    private static String ProjectPath=null;
    public  void   setProjectPath(String projectPath){
        ProjectPath=projectPath;
    }
    public  synchronized static String getProjectPath(){
        return ProjectPath;
    }

    private static String ThisPortForTelSwitch=null;
    public  void   setThisPortForTelSwitch(String thisPortForTelSwitch){
        ThisPortForTelSwitch=thisPortForTelSwitch;
    }
    public  synchronized static String getThisPortForTelSwitch(){
        return ThisPortForTelSwitch;
    }



    private static boolean UseOldAuthenticatePin1=false;
    public  void   setUseOldAuthenticatePin1(boolean useOldAuthenticatePin1){
        UseOldAuthenticatePin1=useOldAuthenticatePin1;
    }
    public  synchronized static boolean getUseOldAuthenticatePin1(){
        return UseOldAuthenticatePin1;
    }

    private static boolean UseOldAuthenticatePin2=false;
    public  void   setUseOldAuthenticatePin2(boolean useOldAuthenticatePin2){
        UseOldAuthenticatePin2=useOldAuthenticatePin2;
    }
    public  synchronized static boolean getUseOldAuthenticatePin2(){
        return UseOldAuthenticatePin2;
    }

    private static boolean UseOldPaymentSystem=false;
    public  void   setUseOldPaymentSystem(boolean useOldPaymentSystem){
        UseOldPaymentSystem=useOldPaymentSystem;
    }
    public  synchronized static boolean getUseOldPaymentSystem(){
        return UseOldPaymentSystem;
    }


    //UseOldPaymentSystem

}
