package Pin1.currentMethod.ORM;

import Pin1.currentMethod.util.PropertiesUtil;
import com.ibm.mq.jms.MQQueueConnectionFactory;

import javax.jms.*;
import java.util.HashMap;

/**
 * Created by Administrator on 28/05/2015.
 */
public class ORM {
    public  static boolean ResultOfRunning=false;
    public  ORM() throws Exception {
     InitializeORM();
    }
    private static QueueSession sessionSender=null;
    private static Queue sendQueue=null;
    private static QueueSender sender=null;

    private static QueueSession  sessionReceive=null;
    private static Queue         receiveQueue=null;
    private static QueueReceiver receiver=null;


    private static MQQueueConnectionFactory connectionFactory =null;
    private static QueueConnection connection =null;

    private static int transportType;
    public  static HashMap<Long,String> migratedRecord = new HashMap<Long,String>();

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

    private static boolean CommunicatedWithQueue(){
        try{
            connectionFactory = new MQQueueConnectionFactory();
            connectionFactory.setUseConnectionPooling(true);
            connectionFactory.setTransportType(transportType);
            connectionFactory.setHostName(PropertiesUtil.getOrmHostName());
            connectionFactory.setPort(PropertiesUtil.getOrmHostPort());
            connectionFactory.setQueueManager(PropertiesUtil.getOrmQueueManagerName());
            connection = connectionFactory.createQueueConnection();
            connection.start();
            return true;
        }catch (Exception e){
            return false;
        }
    }
    private static boolean CreatedORMReceiver() throws JMSException {
        try{
            sessionReceive = connection.createQueueSession(false, 1);
            if ((PropertiesUtil.getOrmReceiveQueueName() != null )&&(!PropertiesUtil.getOrmReceiveQueueName().equalsIgnoreCase("")))
                receiveQueue = sessionReceive.createQueue(PropertiesUtil.getOrmReceiveQueueName());
            receiver = sessionReceive.createReceiver(receiveQueue);
            receiver.setMessageListener(new ORMListener());
            return true;
        }catch (Exception e){
            return false;
        }



    }
    private static boolean CreatedORMSender() throws JMSException {

        try{
            sessionSender = connection.createQueueSession(false, 1);
            sendQueue = sessionSender.createQueue(PropertiesUtil.getOrmSendQueueName());
            sender = sessionSender.createSender(sendQueue);
            sender.setDeliveryMode(1);
            return true;
        }catch (Exception e){
            return false;
        }

    }

    private static void    InitializeORM() throws Exception {
        PropertiesUtil.readConfig();
        transportType = 1;
        if (CommunicatedWithQueue())
            if (CreatedORMReceiver())
                if (CreatedORMSender()){
                    ResultOfRunning=true;
                    return;
                }
        ResultOfRunning=false;

    }

}
