import java.io.File;
import java.io.FileInputStream;

/**
 * Created by root on 4/27/16.
 */


public class Properties_Client {


    private static String ClientID=null;
    private static String OldGateway_listener_IP=null;
    private static boolean UseOldGateway;
    private static boolean UseOldAuthenticate;
    private static boolean UseOldTelSwitch;
    private static boolean UseOldPayment;
    private static String Gateway_listener_Port=null;
    private static String OldGateway_listener_Port=null;
    private static String Path=null;
    private static String Local_listener_IP=null;
    private static String Local_listener_Port=null;
    private static String Gateway_listener_IP=null;


    public Properties_Client(){
        try {
            readConfig();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public  void readConfig() throws Exception {

        final File FileOfSettings=new File(Properties_Client.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        String Path1 =FileOfSettings.toString();
        int i="client.jar".length();


        java.util.Properties props = new java.util.Properties();
        try{
            props.load(new FileInputStream(Path1+"//client.properties"));

        }catch (Exception e){
            try{
                Path =Path1.substring(0,Path1.length()-i);
                props.load(new FileInputStream(Path+"//client.properties"));//if jar file in running
            }catch (Exception ee){
             /*   try{
                    props.load(new FileInputStream(Path1+"\\client.properties"));
                }catch (Exception e1){
                    Path =Path1.substring(0,Path1.length()-i);
                    props.load(new FileInputStream(Path+"//client.properties"));//if jar file in running
                }*/
                return;
            }//if running class
        }
        try
        {          setLocal_listener_IP(props.getProperty("Local_listener_IP"));
            setLocal_listener_Port(props.getProperty("Local_listener_Port"));
            setGateway_listener_IP(props.getProperty("Gateway_listener_IP"));
            setGateway_listener_Port(props.getProperty("Gateway_listener_Port"));
            setClientID(props.getProperty("clientID"));
            setUseOldGateway(Boolean.parseBoolean(props.getProperty("UseOldGateway")));
            setUseOldTelSwitch(Boolean.parseBoolean(props.getProperty("UseOldTelSwitch")));
            setUseOldPayment(Boolean.parseBoolean(props.getProperty("UseOldPayment")));
            setUseOldAuthenticate(Boolean.parseBoolean(props.getProperty("UseOldAuthenticate")));
            setOldGateway_listener_IP(props.getProperty("OldGateway_listener_IP"));
            setOldGateway_listener_Port(props.getProperty("OldGateway_listener_Port"));

        }
        catch(Exception e)
        {

        }


    }

    public static String getClientID() {
        return ClientID;
    }

    public static void setClientID(String clientID) {
        ClientID = clientID;
    }

    public static String getOldGateway_listener_IP() {
        return OldGateway_listener_IP;
    }

    public static void setOldGateway_listener_IP(String oldGateway_listener_IP) {
        OldGateway_listener_IP = oldGateway_listener_IP;
    }



    public static String getGateway_listener_Port() {
        return Gateway_listener_Port;
    }

    public static void setGateway_listener_Port(String gateway_listener_Port) {
        Gateway_listener_Port = gateway_listener_Port;
    }

    public static String getOldGateway_listener_Port() {
        return OldGateway_listener_Port;
    }

    public static void setOldGateway_listener_Port(String oldGateway_listener_Port) {
        OldGateway_listener_Port = oldGateway_listener_Port;
    }

    public static boolean isUseOldGateway() {
        return UseOldGateway;
    }

    public static void setUseOldGateway(boolean useOldGateway) {
        UseOldGateway = useOldGateway;
    }

    public static boolean isUseOldAuthenticate() {
        return UseOldAuthenticate;
    }

    public static void setUseOldAuthenticate(boolean useOldAuthenticate) {
        UseOldAuthenticate = useOldAuthenticate;
    }

    public static boolean isUseOldTelSwitch() {
        return UseOldTelSwitch;
    }

    public static void setUseOldTelSwitch(boolean useOldTelSwitch) {
        UseOldTelSwitch = useOldTelSwitch;
    }

    public static boolean isUseOldPayment() {
        return UseOldPayment;
    }

    public static void setUseOldPayment(boolean useOldPayment) {
        UseOldPayment = useOldPayment;
    }

    public static String getPath() {
        return Path;
    }

    public static void setPath(String path) {
        Path = path;
    }

    public static String getLocal_listener_IP() {
        return Local_listener_IP;
    }

    public static void setLocal_listener_IP(String local_listener_IP) {
        Local_listener_IP = local_listener_IP;
    }

    public static String getLocal_listener_Port() {
        return Local_listener_Port;
    }

    public static void setLocal_listener_Port(String local_listener_Port) {
        Local_listener_Port = local_listener_Port;
    }

    public static String getGateway_listener_IP() {
        return Gateway_listener_IP;
    }

    public static void setGateway_listener_IP(String gateway_listener_IP) {
        Gateway_listener_IP = gateway_listener_IP;
    }
}
