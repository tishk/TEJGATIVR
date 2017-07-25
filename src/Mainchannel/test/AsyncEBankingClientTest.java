//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Mainchannel.test;

import Mainchannel.MainMQ;
import Mainchannel.Exceptions.InvalidParameterException;
import Mainchannel.messages.XML2MessageConvertor;
import com.ibm.mq.jms.MQQueueConnectionFactory;
import Mainchannel.messages.Message2XMLConvertor;
import Mainchannel.sender.SenderConfig;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.jms.JMSException;
import javax.jms.Message;
import javax.jms.MessageListener;
import javax.jms.Queue;
import javax.jms.QueueConnection;
import javax.jms.QueueReceiver;
import javax.jms.QueueSender;
import javax.jms.QueueSession;
import javax.jms.TextMessage;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class AsyncEBankingClientTest {
    private static Log log = LogFactory.getLog(AsyncEBankingClientTest.class);
    static boolean NON_TRANSACTED = false;
    private static XML2MessageConvertor xml2MessageConvertor = new XML2MessageConvertor();
    private static Message2XMLConvertor message2XMLConvertor = new Message2XMLConvertor();
    private static int MESSAGES_PRIORITY = 1;
    private static long SEND_RECEIVE_TIMEOUT = 3600000L;
    Map<String, Long> messages;
    private QueueSender sender;
    private QueueReceiver receiver;
    private Queue sendQueue;
    private Queue receiveQueue;
    QueueSession receiveSession;
    QueueSession sendSession;
    List<String> correlationIDs = new ArrayList();
    int seqSender;

    public AsyncEBankingClientTest() {
    }

    String getCorrelationID() {
        String ciTemplate;
        do {
            int r = ++this.seqSender % 1000;
            String seq;
            if(r < 10) {
                seq = "0" + String.valueOf(r);
            } else {
                seq = String.valueOf(r);
            }

            ciTemplate = "WMQ" + seq + "SampleCorrelationID";
        } while(this.correlationIDs.contains(ciTemplate));

        this.correlationIDs.add(ciTemplate);
        return ciTemplate;
    }

    public void Init() throws Exception {
        try {
            SenderConfig e = new SenderConfig();
            e.setChannel_name("");
            e.setClient_ID("client_ID");
            e.setHostName("127.0.0.1");
            e.setHostPort(Integer.parseInt("1414"));
            e.setMQusername("administrator");
            e.setMQuserPass("");
            e.setQueue_manager_name("CSQ2");
            e.setReciveQueueName("CM2EBNK_ONLINE");
            e.setSendQueueName("EBNK2CM_ONLINE");
            e.setTransportType("LOCAL");
            e.setNumberOfConnections(Integer.valueOf(5).intValue());
            e.setNumberOfSessionsPerConnection(Integer.valueOf(5).intValue());
            MQQueueConnectionFactory connectionFactory = new MQQueueConnectionFactory();
            connectionFactory.setUseConnectionPooling(true);
            if(e.getTransportType().equalsIgnoreCase(SenderConfig.REMOTE_TRANSPORT)) {
                connectionFactory.setTransportType(SenderConfig.REMOTE_TRANSPORT_ID);
                connectionFactory.setHostName(e.getHostName());
            }

            connectionFactory.setPort(e.getHostPort());
            connectionFactory.setQueueManager(e.getQueue_manager_name());
            QueueConnection connection = connectionFactory.createQueueConnection();
            connection.start();
            this.receiveSession = connection.createQueueSession(NON_TRANSACTED, 1);
            this.sendSession = connection.createQueueSession(NON_TRANSACTED, 1);
            this.sendQueue = this.sendSession.createQueue(e.getSendQueueName());
            this.receiveQueue = this.receiveSession.createQueue(e.getReciveQueueName());
            this.sender = this.sendSession.createSender(this.sendQueue);
            this.sender.setDeliveryMode(1);
            this.sender.setPriority(1);
            this.receiveQueue = this.receiveSession.createQueue(e.getReciveQueueName());
            this.receiveSession.createReceiver(this.receiveQueue).setMessageListener(new AsyncEBankingClientTest.EBankingListener());
            this.messages = new HashMap();
        } catch (Exception var4) {
            log.error(var4.getMessage());
            throw var4;
        }
    }

    public static boolean ValidateBaseParameters(String channelType, String channelPass, String channelID) throws InvalidParameterException {
        String errors = "";
        if(channelType == null || channelType.equals("")) {
            errors = "channelType == null || channelType.equals(\"\")";
        }

        if(channelPass == null || channelPass.equals("")) {
            errors = errors + " \nchannelType == null || channelType.equals(\"\")";
        }

        if(channelID == null || channelID.equals("") || channelID.length() > 2) {
            errors = errors + " \nchannelID == null || channelID.equals(\"\") || channelID.length() > 2";
        }

        if(errors.length() > 0) {
            throw new InvalidParameterException(errors);
        } else {
            return true;
        }
    }

    private void callService(String request) throws JMSException {
        try {
            TextMessage e = this.sendSession.createTextMessage(request);
            e.setJMSExpiration(3600000L);
            e.setJMSCorrelationID(this.getCorrelationID());
            this.messages.put(e.getJMSCorrelationID(), Long.valueOf(System.currentTimeMillis()));
            this.sender.send(e);
            log.debug("Request = " + e.getText());
            log.debug("Messages size = " + this.messages.size());
        } catch (JMSException var3) {
            log.error(var3);
            throw var3;
        }
    }

    static String getMsgSequence() {
        String t = String.valueOf(System.currentTimeMillis());
        return t.substring(t.length() - 12);
    }

    public static void main(String[] args) {
        AsyncEBankingClientTest client = new AsyncEBankingClientTest();
        String srcaccount_no = "3201500000";

        try {
            client.Init();

            while(true) {
                client.getBalance(srcaccount_no, "1234", "13860504212233", "SMS", "1234", "1", getMsgSequence(), "1", "1");
                Thread.sleep(70L);
            }
        } catch (Exception var4) {
            var4.printStackTrace();
        }
    }

    public void getBalance(String accountNumber, String accountPass, String requestDateTime, String channelType, String channelPass, String channelID, String msgSequence, String encAlgorytm, String mac) throws InvalidParameterException, Exception {
        Thread.currentThread().setContextClassLoader(MainMQ.class.getClassLoader());
        if(requestDateTime.length() < 14) {
            new InvalidParameterException("getBalance: requestDateTime.length() < 14");
        }

        ValidateBaseParameters(channelType, channelPass, channelID);
        String xmlRequest = message2XMLConvertor.GenerateBalanceRequest(accountNumber, accountPass, requestDateTime, channelType, channelPass, channelID, msgSequence, encAlgorytm, mac);
        this.callService(xmlRequest);
    }

    class EBankingListener implements MessageListener {
        EBankingListener() {
        }

        public void onMessage(Message message) {
            try {
                String e = message.getJMSCorrelationID();
                Long sendTime = (Long) AsyncEBankingClientTest.this.messages.get(e);
                if(sendTime != null) {
                    AsyncEBankingClientTest.log.debug("Duration  = " + (System.currentTimeMillis() - sendTime.longValue()) + "  " + ((TextMessage)message).getText());
                }

                AsyncEBankingClientTest.this.messages.remove(e);
                AsyncEBankingClientTest.log.info("A message received: " + ((TextMessage)message).getText());
                AsyncEBankingClientTest.log.debug("Messages length =" + AsyncEBankingClientTest.this.messages.size());
            } catch (Exception var4) {
                var4.printStackTrace();
            }

        }
    }
}
