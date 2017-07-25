package Mainchannel.util.JMSConnectionPool;

import Mainchannel.sender.SenderConfig;
import Pin1.currentMethod.Avacas.AvaCasListener;
import Pin1.currentMethod.Avacas.AvaCasThread;
import ServiceObjects.Pin.AuthenticatePin1;
import com.ibm.mq.jms.MQQueueConnectionFactory;

import javax.jms.*;
import java.security.KeyPair;
import java.util.HashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by Administrator on 8/17/2015.
 */
public class MQServerToServer {
    public  static boolean ResultOfRunning=false;
    private static SenderConfig senderConfig=null;
    public  MQServerToServer(SenderConfig senderCfg) throws Exception {
        senderConfig=senderCfg;
        InitializeAvaCas();
    }

    private static QueueSession sessionSender=null;
    private static Queue sendQueue=null;
    private static QueueSender sender=null;

    private static QueueSession  sessionReceive=null;
    private static Queue         receiveQueue=null;
    private static QueueReceiver receiver=null;


    private static MQQueueConnectionFactory connectionFactory =null;
    private static QueueConnection connection =null;

    public static int numLeft = 0;



    private static int transportType;

    public static HashMap idHolderHashMap = new HashMap();
    public static AvaCasThread getAvaCasThread(String key) {
        AvaCasThread avaCasThread = null;
        if (idHolderHashMap.containsKey(key))
            avaCasThread = (AvaCasThread) idHolderHashMap.get(key);
        return avaCasThread;
    }
    public static synchronized void sendBytes(byte byteMessage[])throws JMSException {
        BytesMessage message = sessionSender.createBytesMessage();
        message.writeBytes(byteMessage);
        long timeout = 60000L;
        message.setJMSExpiration(timeout);
        long timeToLive = 0x186a0L;
        sender.send(message, 1, 1, timeToLive);
        if (sessionSender.getTransacted())
            sessionSender.commit();
    }


    private static KeyPair KEY_PAIR;
    private static String  KEY_PAIR_PATH = null;
    private static void    getAvaCasKeys(){
        setKEY_PAIR(Pin1.currentMethod.util.PropertiesUtil.getKeyPairAvaCas());
        setKEY_PAIR_PATH(Pin1.currentMethod.util.PropertiesUtil.getKeyAvaCas());
    }
    private static void    setKEY_PAIR(KeyPair key_pair){
        KEY_PAIR=key_pair;

    }
    private static void    setKEY_PAIR_PATH(String key_pair_path){
        KEY_PAIR_PATH = key_pair_path;
    }
    public static KeyPair  getKEY_PAIR(){
        return KEY_PAIR;

    }
    public static String   getKEY_PAIR_PATH(){
        return KEY_PAIR_PATH;
    }

    private static void    InitializeAvaCas() throws Exception {

        transportType = 1;
        if (CommunicatedWithQueue())
            if (CreatedMainMQReceiver())
                if (CreatedMainMQSender()){
                    ResultOfRunning=true;
                    return;
                }
        ResultOfRunning=false;

    }
    private static boolean CommunicatedWithQueue(){
        try{
            connectionFactory = new MQQueueConnectionFactory();
            connectionFactory.setUseConnectionPooling(true);
            connectionFactory.setTransportType(transportType);
            connectionFactory.setHostName(senderConfig.getHostName());
            connectionFactory.setPort(senderConfig.getHostPort());
            connectionFactory.setQueueManager(senderConfig.getQueue_manager_name());
            connection = connectionFactory.createQueueConnection(senderConfig.getMQusername(), senderConfig.getMQuserPass());
            connection.start();
            return true;
        }catch (Exception e){
            return false;
        }
    }
    private static boolean CreatedMainMQReceiver() throws JMSException {
        try{
            sessionReceive = connection.createQueueSession(false, 1);

            receiveQueue = sessionReceive.createQueue(senderConfig.getReciveQueueName());
            receiver = sessionReceive.createReceiver(receiveQueue);
            receiver.setMessageListener(new AvaCasListener());
            return true;
        }catch (Exception e){
            return false;
        }



    }
    private static boolean CreatedMainMQSender() throws JMSException {
        try{
            sessionSender = connection.createQueueSession(false, 1);
            sendQueue = sessionSender.createQueue(senderConfig.getReciveQueueName());
            sender = sessionSender.createSender(sendQueue);
            sender.setDeliveryMode(1);
            return true;
        }catch (Exception e){
            return false;
        }



    }

    public static AuthenticatePin1 getAuthenticatePin1(AuthenticatePin1 AutPin1){
        ExecutorService runClient = Executors.newFixedThreadPool(1);
        AvaCasThread av = new AvaCasThread(AutPin1);
        runClient.execute(av);

        return av.getResult();

    }
    public static AuthenticatePin1 ChangePin1(AuthenticatePin1 AutPin1){
        ExecutorService runClient = Executors.newFixedThreadPool(1);
        AvaCasThread av = new AvaCasThread(AutPin1);
        runClient.execute(av);

        return av.getResult();

    }

}

