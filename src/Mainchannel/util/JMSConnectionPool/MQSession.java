//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Mainchannel.util.JMSConnectionPool;

import Mainchannel.Exceptions.TimeoutException;
import Mainchannel.messages.XML2MessageConvertor;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import javax.jms.*;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class MQSession {
    private static Log log = LogFactory.getLog(MQSession.class);
    private static String clientID = "clientId";
    private static String lastCorrelationID;
    private static boolean NON_TRANSACTED = false;
    static short correlationIdCounter;
    private QueueSession session;
    private Queue receiveQueue;
    private Queue sendQueue;
    private QueueSender sender;
    private String currentCorrelationId;
    private long timeout;
    int priority;
    Object response;
    long requestTime;
    public static Map<String, MQSession> threads = Collections.synchronizedMap(new HashMap());
    public static Map<String, MQSession> temp = Collections.synchronizedMap(new HashMap());
    static int requestCounter = 0;


    public boolean CreatedMainMQ() throws JMSException {
        try{
          /*  this.session = connection.createQueueSession(false, 1);
            this.receiveQueue = session.createQueue(this.config.getReciveQueueName());
            this.sendQueue = session.createQueue(senderConfig.getSendQueueName());
            this.sender = session.createSender(sendQueue);
            this.sender.setDeliveryMode(1);*/
            return true;
        }catch (Exception e){
            return false;
        }



    }




    //--------------------------------------------------------------------------------------



    public void setResponse(Object response) {
        this.response = response;
    }

    public QueueSession getSession() {
        return this.session;
    }

    public QueueSender getSender() {
        return this.sender;
    }

    public MQSession(QueueConnection connection, String sendQueueName, String receiveQueueName, String clientID, long timeout, int priority) throws JMSException {
        clientID = clientID;
        this.timeout = timeout;

        try {
            this.session = connection.createQueueSession(NON_TRANSACTED, 1);
            //this.session = connection.createQueueSession(false, 1);
            this.sendQueue = this.session.createQueue(sendQueueName);
            this.receiveQueue = this.session.createQueue(receiveQueueName);
            this.sender = this.session.createSender(this.sendQueue);
            this.sender.setDeliveryMode(1);
            this.sender.setPriority(priority);
            this.priority = priority;
        } catch (JMSException var9) {
            log.error(var9.getLinkedException());
            throw var9;
        }
    }

    public MQSession(QueueConnection connection, String sendQueueName, String receiveQueueName, String clientID, long timeout, int priority,String ForTest) throws JMSException {
        clientID = clientID;
        this.timeout = timeout;

        try {
            this.session = connection.createQueueSession(NON_TRANSACTED, 1);
            this.sendQueue = this.session.createQueue(sendQueueName);
            this.receiveQueue = this.session.createQueue(receiveQueueName);
            this.sender = this.session.createSender(this.sendQueue);
            this.sender.setDeliveryMode(1);
            this.sender.setPriority(priority);
            this.priority = priority;
        } catch (JMSException var9) {
            log.error(var9);
            throw var9;
        }
    }

    public static synchronized String getCorrelationID() {
        StringBuffer correlationID;
        do {
            correlationID = new StringBuffer(clientID);
            correlationID.append(String.valueOf(correlationIdCounter++));
        } while(correlationID.toString().equals(lastCorrelationID));

        lastCorrelationID = correlationID.toString();
        return correlationID.toString();
    }

    public void setListener(MessageListener listener) throws JMSException {
        QueueReceiver receiver = this.session.createReceiver(this.receiveQueue);
        receiver.setMessageListener(listener);
    }

    public void sendRequest(String messageStr, String msgSeq, String channelId) throws JMSException {
        TextMessage request = this.session.createTextMessage(messageStr);
        request.setJMSCorrelationID(this.currentCorrelationId);
        request.setStringProperty("ClientVersion", "V3.0");
        request.setStringProperty("MSGSEQ", msgSeq);
        request.setStringProperty("CHANNELID", channelId);
        log.debug("Before sending to queue. Message = " + messageStr);
        this.sender.send(request, 1, this.priority, this.timeout);
        log.debug("After sending to queue.");
        if(this.session.getTransacted()) {
            this.session.commit();
        }

    }

    public Object sendAndReceive(String msg, String msgSeq, String channelId) throws JMSException, TimeoutException {
        this.response = null;
        this.currentCorrelationId = getCorrelationID();
        log.info("1. Request nubmer: " + ++requestCounter);

        try {
            this.sendRequest(msg, msgSeq, channelId);
        } catch (Exception var12) {
            log.error(var12);
           // log.error("WOW! exception in sending request, MsgSeq = " + msgSeq);
        }

        Map e = threads;
        synchronized(threads) {
            String var5 = this.currentCorrelationId;
            synchronized(this.currentCorrelationId) {
                if(threads.containsKey(this.currentCorrelationId)) {
                    //log.error("WOW !, correlationID is not unique.");
                }
            }
        }

        try {
            threads.put(this.currentCorrelationId, this);
        } catch (Exception var11) {
            log.error(var11);
        }

        try {
            String var15 = this.currentCorrelationId;
            synchronized(this.currentCorrelationId) {
                this.currentCorrelationId.wait(this.timeout);
            }

            threads.remove(this.currentCorrelationId);
        } catch (Exception var10) {
            log.error(var10);
            var10.printStackTrace();
        }

        if(this.response == null) {
            this.response = (new XML2MessageConvertor()).GenerateErrorXML("9126", "مشکل در ارتباط با کانال");
        }

        return this.response;
    }

    public String getCurrentCorrelationId() {
        return this.currentCorrelationId;
    }

    public long getRequestTime() {
        return this.requestTime;
    }

    public void setRequestTime(long requestTime) {
        this.requestTime = requestTime;
    }
}
