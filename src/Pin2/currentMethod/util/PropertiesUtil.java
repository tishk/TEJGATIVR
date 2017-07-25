// Decompiled by DJ v3.10.10.93 Copyright 2007 Atanas Neshkov  Date: 2012-04-08 23:56:38
// Home Page: http://members.fortunecity.com/neshkov/dj.html  http://www.neshkov.com/dj.html - Check often for new version!
// Decompiler options: packimports(3) 
// Source File Name:   PropertiesUtil.java

package Pin2.currentMethod.util;



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
            props.load(new FileInputStream(Path+"/pin2.properties")); //if jar file in running
        }catch (Exception e){
            try{
                Path =Path1+"/Pin2/currentMethod/util/";
                props.load(new FileInputStream(Path+"pin2.properties"));// if class in running
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
            keyPairOrm = CodecUtil.loadKeyFromFile(keyOrm);
            keyAvaCas = Path+props.getProperty("keyAvaCas");
            AvaCasHostName = props.getProperty("AvaCasHostName");
            AvaCasQueueManagerName = props.getProperty("AvaCasQueueManagerName");
            AvaCasHostPort = Integer.valueOf(props.getProperty("AvaCasHostPort")).intValue();
            AvaCasMQusername = props.getProperty("AvaCasMQusername");
            AvaCasMQuserPass = props.getProperty("AvaCasMQuserPass");
            AvaCasSendQueueName = props.getProperty("AvaCasSendQueueName");
            AvaCasReceiveQueueName = props.getProperty("AvaCasReceiveQueueName");
            AvaCasTcpPort = Integer.valueOf(props.getProperty("AvaCasTcpPort")).intValue();
            keyPairAvaCas = Pin2.currentMethod.util.CodecUtil.loadKeyFromFile(keyAvaCas);
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
        return Pin2.currentMethod.util.PropertiesUtil.AvaCasHostName;
    }

    public static void setAvaCasHostName(String AvaCasHostName)
    {
        Pin2.currentMethod.util.PropertiesUtil.AvaCasHostName = AvaCasHostName;
    }

    public static int getAvaCasHostPort()
    {
        return Pin2.currentMethod.util.PropertiesUtil.AvaCasHostPort;
    }

    public static void setAvaCasHostPort(int AvaCasHostPort)
    {
        Pin2.currentMethod.util.PropertiesUtil.AvaCasHostPort = AvaCasHostPort;
    }

    public static String getAvaCasMQuserPass()
    {
        return Pin2.currentMethod.util.PropertiesUtil.AvaCasMQuserPass;
    }

    public static void setAvaCasMQuserPass(String AvaCasMQuserPass)
    {
        Pin2.currentMethod.util.PropertiesUtil.AvaCasMQuserPass = AvaCasMQuserPass;
    }

    public static String getAvaCasMQusername()
    {
        return Pin2.currentMethod.util.PropertiesUtil.AvaCasMQusername;
    }

    public static void setAvaCasMQusername(String AvaCasMQusername)
    {
        Pin2.currentMethod.util.PropertiesUtil.AvaCasMQusername = AvaCasMQusername;
    }

    public static String getAvaCasQueueManagerName()
    {
        return Pin2.currentMethod.util.PropertiesUtil.AvaCasQueueManagerName;
    }

    public static void setAvaCasQueueManagerName(String AvaCasQueueManagerName)
    {
        Pin2.currentMethod.util.PropertiesUtil.AvaCasQueueManagerName = AvaCasQueueManagerName;
    }

    public static String getAvaCasReceiveQueueName()
    {
        return Pin2.currentMethod.util.PropertiesUtil.AvaCasReceiveQueueName;
    }

    public static void setAvaCasReceiveQueueName(String AvaCasReceiveQueueName)
    {
        Pin2.currentMethod.util.PropertiesUtil.AvaCasReceiveQueueName = AvaCasReceiveQueueName;
    }

    public static String getAvaCasSendQueueName()
    {
        return Pin2.currentMethod.util.PropertiesUtil.AvaCasSendQueueName;
    }

    public static void setAvaCasSendQueueName(String AvaCasSendQueueName)
    {
        Pin2.currentMethod.util.PropertiesUtil.AvaCasSendQueueName = AvaCasSendQueueName;
    }

    public static int getAvaCasTcpPort()
    {
        return Pin2.currentMethod.util.PropertiesUtil.AvaCasTcpPort;
    }

    public static void setAvaCasTcpPort(int AvaCasTcpPort)
    {
        Pin2.currentMethod.util.PropertiesUtil.AvaCasTcpPort = AvaCasTcpPort;
    }

    public static String getDbUserPassword()
    {
        return Pin2.currentMethod.util.PropertiesUtil.dbUserPassword;
    }

    public static void setDbUserPassword(String dbUserPassword)
    {
        Pin2.currentMethod.util.PropertiesUtil.dbUserPassword = dbUserPassword;
    }

    public static String getDbdriver()
    {
        return Pin2.currentMethod.util.PropertiesUtil.dbdriver;
    }

    public static void setDbdriver(String dbdriver)
    {
        Pin2.currentMethod.util.PropertiesUtil.dbdriver = dbdriver;
    }

    public static String getDburl()
    {
        return Pin2.currentMethod.util.PropertiesUtil.dburl;
    }

    public static void setDburl(String dburl)
    {
        Pin2.currentMethod.util.PropertiesUtil.dburl = dburl;
    }

    public static String getDbusername()
    {
        return Pin2.currentMethod.util.PropertiesUtil.dbusername;
    }

    public static void setDbusername(String dbusername)
    {
        Pin2.currentMethod.util.PropertiesUtil.dbusername = dbusername;
    }

    public static String getKeyAvaCas()
    {
        return Pin2.currentMethod.util.PropertiesUtil.keyAvaCas;
    }

    public static void setKeyAvaCas(String keyAvaCas)
    {
        Pin2.currentMethod.util.PropertiesUtil.keyAvaCas = keyAvaCas;
    }

    public static String getKeyOrm()
    {
        return Pin2.currentMethod.util.PropertiesUtil.keyOrm;
    }

    public static void setKeyOrm(String keyOrm)
    {
        Pin2.currentMethod.util.PropertiesUtil.keyOrm = keyOrm;
    }

    public static String getOrmHostName()
    {
        return Pin2.currentMethod.util.PropertiesUtil.ormHostName;
    }

    public static void setOrmHostName(String ormHostName)
    {
        Pin2.currentMethod.util.PropertiesUtil.ormHostName = ormHostName;
    }

    public static int getOrmHostPort()
    {
        return Pin2.currentMethod.util.PropertiesUtil.ormHostPort;
    }

    public static void setOrmHostPort(int ormHostPort)
    {
        Pin2.currentMethod.util.PropertiesUtil.ormHostPort = ormHostPort;
    }

    public static String getOrmMQuserPass()
    {
        return Pin2.currentMethod.util.PropertiesUtil.ormMQuserPass;
    }

    public static void setOrmMQuserPass(String ormMQuserPass)
    {
        Pin2.currentMethod.util.PropertiesUtil.ormMQuserPass = ormMQuserPass;
    }

    public static String getOrmMQusername()
    {
        return Pin2.currentMethod.util.PropertiesUtil.ormMQusername;
    }

    public static void setOrmMQusername(String ormMQusername)
    {
        Pin2.currentMethod.util.PropertiesUtil.ormMQusername = ormMQusername;
    }

    public static String getOrmQueueManagerName()
    {
        return Pin2.currentMethod.util.PropertiesUtil.ormQueueManagerName;
    }

    public static void setOrmQueueManagerName(String ormQueueManagerName)
    {
        Pin2.currentMethod.util.PropertiesUtil.ormQueueManagerName = ormQueueManagerName;
    }

    public static String getOrmReceiveQueueName()
    {
        return Pin2.currentMethod.util.PropertiesUtil.ormReceiveQueueName;
    }

    public static void setOrmReceiveQueueName(String ormReceiveQueueName)
    {
        Pin2.currentMethod.util.PropertiesUtil.ormReceiveQueueName = ormReceiveQueueName;
    }

    public static String getOrmSendQueueName()
    {
        return Pin2.currentMethod.util.PropertiesUtil.ormSendQueueName;
    }

    public static void setOrmSendQueueName(String ormSendQueueName)
    {
        Pin2.currentMethod.util.PropertiesUtil.ormSendQueueName = ormSendQueueName;
    }

    public static KeyPair getKeyPairAvaCas()
    {
        return Pin2.currentMethod.util.PropertiesUtil.keyPairAvaCas;
    }

    public static void setKeyPairAvaCas(KeyPair keyPairAvaCas)
    {
        Pin2.currentMethod.util.PropertiesUtil.keyPairAvaCas = keyPairAvaCas;
    }

    public static KeyPair getKeyPairOrm()
    {
        return Pin2.currentMethod.util.PropertiesUtil.keyPairOrm;
    }

    public static void setKeyPairOrm(KeyPair keyPairOrm)
    {
        Pin2.currentMethod.util.PropertiesUtil.keyPairOrm = keyPairOrm;
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
        Pin2.currentMethod.util.PropertiesUtil.branchNo = branchNo;
    }

    public static String getOrmDeviceId() {
        return ormDeviceId;
    }

    public static void setOrmDeviceId(String ormDeviceId) {
        Pin2.currentMethod.util.PropertiesUtil.ormDeviceId = ormDeviceId;
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
        Pin2.currentMethod.util.PropertiesUtil.tbName = tbName;
    }

    private static String ormDeviceId;
    private static String AvaCasDeviceId;
}