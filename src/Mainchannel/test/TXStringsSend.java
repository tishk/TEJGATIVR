//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Mainchannel.test;

import Mainchannel.Exceptions.TimeoutException;
import Mainchannel.messages.XML2MessageConvertor;
import Mainchannel.messages.Message2XMLConvertor;
import Mainchannel.sender.SenderConfig;
import Mainchannel.sender.SenderException;
import Mainchannel.util.JMSConnectionPool.MQSession;
import Mainchannel.util.JMSConnectionPool.MQSessionPool;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.PrintStream;
import javax.jms.IllegalStateException;
import javax.jms.JMSException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

public class TXStringsSend {
    private static Log log = LogFactory.getLog(TXStringsSend.class);
    private static XML2MessageConvertor xml2MessageConvertor;
    private static Message2XMLConvertor message2XMLConvertor;
    private static SenderConfig senderConfig;
    private static MQSessionPool sessionPool;
    private static int MESSAGES_PRIORITY = 5;

    public TXStringsSend() {
    }

    public static void main(String[] args) {
        readFile();
    }

    public static void readFile() {
        String txString = "";
        String channel_type = "";
        String messageSeq = "";
        String channel_id = "";

        try {
            BufferedReader e = new BufferedReader(new FileReader("ivr.txt"));
            boolean index = false;
            init("IVR");
            int i = 0;

            while(e.ready()) {
                PrintStream var10000 = System.out;
                StringBuilder var10001 = (new StringBuilder()).append("");
                ++i;
                var10000.println(var10001.append(i).toString());
                String[] ivrReq = e.readLine().split(",");
                txString = ivrReq[0].trim();
                channel_type = ivrReq[1].trim();
                messageSeq = ivrReq[3].trim();
                callService(txString, messageSeq, channel_id);
            }

            e.close();
        } catch (Exception var9) {
            var9.getMessage();
        }

    }

    public static void init(String channelType) throws Exception {
        SenderConfig senderConfig = new SenderConfig();
        senderConfig.setChannel_name("SERVER001");
        senderConfig.setHostName("10.0.0.25");
        senderConfig.setHostPort(Integer.parseInt("1717"));
        senderConfig.setMQusername("mquser");
        senderConfig.setMQuserPass("");
        senderConfig.setQueue_manager_name("CSQ4");
        senderConfig.setTransportType("");
        senderConfig.setNumberOfConnections(Integer.valueOf("1").intValue());
        senderConfig.setNumberOfSessionsPerConnection(Integer.valueOf("1").intValue());
        senderConfig.setTimeout(20000L);
        senderConfig.setClient_ID(channelType + "001");
        senderConfig.setReciveQueueName("CM2" + channelType + "_ONLINE");
        senderConfig.setSendQueueName(channelType + "2CM_ONLINE");
        senderConfig.setTransportType("REMOTE");
        sessionPool = new MQSessionPool(senderConfig, MESSAGES_PRIORITY);
        Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
                TXStringsSend.sessionPool.stop();
            }
        });
    }

    public static Object callService(String request, String msgSeq, String channelId) throws SenderException {
        MQSession session = null;
        boolean sessionWasClosedUbnormaly = false;
        long t2 = 0L;

        Object responseStr;
        try {
            long t1 = System.currentTimeMillis();
            session = sessionPool.getMQSession();
            log.info("Request  = " + request);
            t2 = System.currentTimeMillis();
            if(t2 - t1 > 50L) {
                log.error("WOOOOOOOOOOOOOOOOOW!!!!! getMQSession() is too long.   msgSeq::" + msgSeq + "Time>>>>>" + (t2 - t1));
            }

            responseStr = session.sendAndReceive(request, msgSeq, channelId);
        } catch (IllegalStateException var19) {
            sessionWasClosedUbnormaly = true;
            responseStr = xml2MessageConvertor.GenerateErrorXML("9126", "Timeout occured when receiving response from MQ.");
        } catch (TimeoutException var20) {
            responseStr = xml2MessageConvertor.GenerateErrorXML("9126", "Timeout occured when receiving response from MQ.");
        } catch (JMSException var21) {
            responseStr = xml2MessageConvertor.GenerateErrorXML("1839", var21.getMessage());
        } catch (Exception var22) {
            responseStr = xml2MessageConvertor.GenerateErrorXML("1839", var22.getMessage());
        } finally {
            long t3 = System.currentTimeMillis();
            if(t2 != 0L && t3 - t2 > 7000L) {
                log.error("WOW!!!!!!!!!!!!!!!! sendAndReceive() is too long. msgSeq::" + msgSeq + "Time>>>>>" + (t3 - t2));
            }

            if(sessionWasClosedUbnormaly) {
                sessionPool.remove(session);
            } else {
                sessionPool.freeMQSession(session);
            }

        }

        log.info("Response  = " + responseStr);
        return responseStr;
    }
}
