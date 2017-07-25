// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 2012-04-08 23:56:38
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   PropertiesUtil.java

package Pin1.currentMethod.util;



import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.security.KeyPair;
import java.util.Properties;

// Referenced classes of package com.neda.util:
//            CodecUtil

public class PropertiesUtil
{

    public PropertiesUtil()
    {
    }

    public static void readConfig() throws Exception {


        final File FileOfSettings=new File(PropertiesUtil.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        String Path1 =FileOfSettings.toString();
        int i="Gateway.jar".length();
        String Path =Path1.substring(0,Path1.length()-i);
        Properties props = new Properties();
        try{
            props.load(new FileInputStream(Path+"/orm1.properties")); //if jar file in running
        }catch (Exception e){
            try{
                Path =Path1+"/Pin1/currentMethod/util/";
                props.load(new FileInputStream(Path+"orm1.properties"));// if class in running
            }catch (Exception ee){}
        }

        try{
            keyOrm =Path+ props.getProperty("keyOrm");
            ormHostName = props.getProperty("ormHostName");
            ormQueueManagerName = props.getProperty("ormQueueManagerName");
            ormHostPort = Integer.valueOf(props.getProperty("ormHostPort")).intValue();
            ormMQusername = props.getProperty("ormMQusername");
            ormMQuserPass = props.getProperty("ormMQuserPass");
            ormSendQueueName = props.getProperty("ormSendQueueName");
            ormReceiveQueueName = props.getProperty("ormReceiveQueueName");
            //ormDeviceId = props.getProperty("ormReceiveQueueName");
            keyPairOrm = Pin1.currentMethod.util.CodecUtil.loadKeyFromFile(keyOrm);
            keyAvaCas = Path+props.getProperty("keyAvaCas");
            AvaCasHostName = props.getProperty("AvaCasHostName");
            AvaCasQueueManagerName = props.getProperty("AvaCasQueueManagerName");
            AvaCasHostPort = Integer.valueOf(props.getProperty("AvaCasHostPort")).intValue();
            AvaCasMQusername = props.getProperty("AvaCasMQusername");
            AvaCasMQuserPass = props.getProperty("AvaCasMQuserPass");
            AvaCasSendQueueName = props.getProperty("AvaCasSendQueueName");
            AvaCasReceiveQueueName = props.getProperty("AvaCasReceiveQueueName");
            AvaCasTcpPort = Integer.valueOf(props.getProperty("AvaCasTcpPort")).intValue();
            keyPairAvaCas = Pin1.currentMethod.util.CodecUtil.loadKeyFromFile(keyAvaCas);
            dbdriver = props.getProperty("dbdriver");
            dburl = props.getProperty("dburl");
            dbusername = props.getProperty("dbusername");
            dbUserPassword = props.getProperty("dbuserpassword");
            ormDeviceId = props.getProperty("ormDeviceId");
            AvaCasDeviceId = props.getProperty("AvaCasDeviceId");
            tbName = props.getProperty("tbName");
            branchNo = props.getProperty("branchNo");
        }catch (Exception e){

        }

    }

    public static void writeConfig()
    {
    }

    public static String getAvaCasHostName()
    {
        return PropertiesUtil.AvaCasHostName;
    }

    public static void setAvaCasHostName(String AvaCasHostName)
    {
        PropertiesUtil.AvaCasHostName = AvaCasHostName;
    }

    public static int getAvaCasHostPort()
    {
        return PropertiesUtil.AvaCasHostPort;
    }

    public static void setAvaCasHostPort(int AvaCasHostPort)
    {
        PropertiesUtil.AvaCasHostPort = AvaCasHostPort;
    }

    public static String getAvaCasMQuserPass()
    {
        return PropertiesUtil.AvaCasMQuserPass;
    }

    public static void setAvaCasMQuserPass(String AvaCasMQuserPass)
    {
        PropertiesUtil.AvaCasMQuserPass = AvaCasMQuserPass;
    }

    public static String getAvaCasMQusername()
    {
        return PropertiesUtil.AvaCasMQusername;
    }

    public static void setAvaCasMQusername(String AvaCasMQusername)
    {
        PropertiesUtil.AvaCasMQusername = AvaCasMQusername;
    }

    public static String getAvaCasQueueManagerName()
    {
        return PropertiesUtil.AvaCasQueueManagerName;
    }

    public static void setAvaCasQueueManagerName(String AvaCasQueueManagerName)
    {
        PropertiesUtil.AvaCasQueueManagerName = AvaCasQueueManagerName;
    }

    public static String getAvaCasReceiveQueueName()
    {
        return PropertiesUtil.AvaCasReceiveQueueName;
    }

    public static void setAvaCasReceiveQueueName(String AvaCasReceiveQueueName)
    {
        PropertiesUtil.AvaCasReceiveQueueName = AvaCasReceiveQueueName;
    }

    public static String getAvaCasSendQueueName()
    {
        return PropertiesUtil.AvaCasSendQueueName;
    }

    public static void setAvaCasSendQueueName(String AvaCasSendQueueName)
    {
        PropertiesUtil.AvaCasSendQueueName = AvaCasSendQueueName;
    }

    public static int getAvaCasTcpPort()
    {
        return PropertiesUtil.AvaCasTcpPort;
    }

    public static void setAvaCasTcpPort(int AvaCasTcpPort)
    {
        PropertiesUtil.AvaCasTcpPort = AvaCasTcpPort;
    }

    public static String getDbUserPassword()
    {
        return PropertiesUtil.dbUserPassword;
    }

    public static void setDbUserPassword(String dbUserPassword)
    {
        PropertiesUtil.dbUserPassword = dbUserPassword;
    }

    public static String getDbdriver()
    {
        return PropertiesUtil.dbdriver;
    }

    public static void setDbdriver(String dbdriver)
    {
        PropertiesUtil.dbdriver = dbdriver;
    }

    public static String getDburl()
    {
        return PropertiesUtil.dburl;
    }

    public static void setDburl(String dburl)
    {
        PropertiesUtil.dburl = dburl;
    }

    public static String getDbusername()
    {
        return PropertiesUtil.dbusername;
    }

    public static void setDbusername(String dbusername)
    {
        PropertiesUtil.dbusername = dbusername;
    }

    public static String getKeyAvaCas()
    {
        return PropertiesUtil.keyAvaCas;
    }

    public static void setKeyAvaCas(String keyAvaCas)
    {
        PropertiesUtil.keyAvaCas = keyAvaCas;
    }

    public static String getKeyOrm()
    {
        return PropertiesUtil.keyOrm;
    }

    public static void setKeyOrm(String keyOrm)
    {
        PropertiesUtil.keyOrm = keyOrm;
    }

    public static String getOrmHostName()
    {
        return PropertiesUtil.ormHostName;
    }

    public static void setOrmHostName(String ormHostName)
    {
        PropertiesUtil.ormHostName = ormHostName;
    }

    public static int getOrmHostPort()
    {
        return PropertiesUtil.ormHostPort;
    }

    public static void setOrmHostPort(int ormHostPort)
    {
        PropertiesUtil.ormHostPort = ormHostPort;
    }

    public static String getOrmMQuserPass()
    {
        return PropertiesUtil.ormMQuserPass;
    }

    public static void setOrmMQuserPass(String ormMQuserPass)
    {
        PropertiesUtil.ormMQuserPass = ormMQuserPass;
    }

    public static String getOrmMQusername()
    {
        return PropertiesUtil.ormMQusername;
    }

    public static void setOrmMQusername(String ormMQusername)
    {
        PropertiesUtil.ormMQusername = ormMQusername;
    }

    public static String getOrmQueueManagerName()
    {
        return PropertiesUtil.ormQueueManagerName;
    }

    public static void setOrmQueueManagerName(String ormQueueManagerName)
    {
        PropertiesUtil.ormQueueManagerName = ormQueueManagerName;
    }

    public static String getOrmReceiveQueueName()
    {
        return PropertiesUtil.ormReceiveQueueName;
    }

    public static void setOrmReceiveQueueName(String ormReceiveQueueName)
    {
        PropertiesUtil.ormReceiveQueueName = ormReceiveQueueName;
    }

    public static String getOrmSendQueueName()
    {
        return PropertiesUtil.ormSendQueueName;
    }

    public static void setOrmSendQueueName(String ormSendQueueName)
    {
        PropertiesUtil.ormSendQueueName = ormSendQueueName;
    }

    public static KeyPair getKeyPairAvaCas()
    {
        return PropertiesUtil.keyPairAvaCas;
    }

    public static void setKeyPairAvaCas(KeyPair keyPairAvaCas)
    {
        PropertiesUtil.keyPairAvaCas = keyPairAvaCas;
    }

    public static KeyPair getKeyPairOrm()
    {
        return PropertiesUtil.keyPairOrm;
    }

    public static void setKeyPairOrm(KeyPair keyPairOrm)
    {
        PropertiesUtil.keyPairOrm = keyPairOrm;
    }

    private static KeyPair keyPairOrm;
    private static String keyOrm;
    private static String ormHostName;
    private static String ormQueueManagerName;
    private static int ormHostPort;
    private static String ormMQusername;
    private static String ormMQuserPass;
    private static String ormSendQueueName;
    private static String ormReceiveQueueName;
    private static KeyPair keyPairAvaCas;
    private static String keyAvaCas;
    private static String AvaCasHostName;
    private static String AvaCasQueueManagerName;
    private static int AvaCasHostPort;
    private static String AvaCasMQusername;
    private static String AvaCasMQuserPass;
    private static String AvaCasSendQueueName;
    private static String AvaCasReceiveQueueName;
    private static int AvaCasTcpPort;
    private static String dbdriver;
    private static String dburl;
    private static String dbusername;
    private static String dbUserPassword;
    private static String tbName;
    private static String branchNo;

    public static String getBranchNo() {
        return branchNo;
    }

    public static void setBranchNo(String branchNo) {
        PropertiesUtil.branchNo = branchNo;
    }

    public static String getOrmDeviceId() {
        return ormDeviceId;
    }

    public static void setOrmDeviceId(String ormDeviceId) {
        PropertiesUtil.ormDeviceId = ormDeviceId;
    }

    public static String getAvaCasDeviceId() {
        return AvaCasDeviceId;
    }

    public static void setAvaCasDeviceId(String avaCasDeviceId) {
        AvaCasDeviceId = avaCasDeviceId;
    }

    public static String getTbName() {
        return tbName;
    }

    public static void setTbName(String tbName) {
        PropertiesUtil.tbName = tbName;
    }

    private static String ormDeviceId;
    private static String AvaCasDeviceId;
}