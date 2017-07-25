//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package Mainchannel.util.JMSConnectionPool;

import Mainchannel.MainMQ;
import Mainchannel.Exceptions.NoIdleSessionException;
import Mainchannel.Exceptions.TimeoutException;
import Mainchannel.messages.BaseMessage;
import Mainchannel.sender.SenderConfig;
import com.ibm.mq.jms.MQQueueConnectionFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Timer;
import javax.jms.*;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;

public class MQSessionPool {
    private static Log log = LogFactory.getLog(MQSessionPool.class);
    List<QueueConnection> connections = Collections.synchronizedList(new ArrayList());
    List<MQSession> idleMQSessions = Collections.synchronizedList(new ArrayList());
    List<MQSession> workingMQSessions = Collections.synchronizedList(new ArrayList());
    SenderConfig config;
    Timer timer;
    int priority;
    boolean isStarted = false;
    static int seq;
    static int responseCounter = 0;

    public MQSessionPool(SenderConfig config, int priority) throws JMSException {
        this.config = config;
        this.priority = priority;
        this.start();
    }

    private synchronized void start_() throws JMSException {
        MQQueueConnectionFactory connectionFactory = new MQQueueConnectionFactory();
        connectionFactory.setUseConnectionPooling(true);
        this.timer = new Timer();
        List e = this.workingMQSessions;
        synchronized(this.workingMQSessions) {
            List var4 = this.idleMQSessions;
            synchronized(this.idleMQSessions) {
                List var5 = this.connections;
                synchronized(this.connections) {
                    try {
                        if(this.config.getTransportType().equalsIgnoreCase(SenderConfig.REMOTE_TRANSPORT)) {
                            connectionFactory.setTransportType(SenderConfig.REMOTE_TRANSPORT_ID);
                            connectionFactory.setHostName(this.config.getHostName());
                            connectionFactory.setTransportType(1);
                        }

                        connectionFactory.setPort(this.config.getHostPort());
                        connectionFactory.setChannel(this.config.getChannel_name());
                        //connectionFactory.setChannel("CM.TO.TB");
                        connectionFactory.setQueueManager(this.config.getQueue_manager_name());
                       // connectionFactory.se
                        String username=this.config.getMQusername();
                        String password=this.config.getMQuserPass();
                       log.info("Start of creating QueueConnections and QueueSessions.");
                        long e1 = System.currentTimeMillis();

                        for(int connectionsIterator = 0; connectionsIterator < this.config.getNumberOfConnections(); ++connectionsIterator) {
                            if(log.isInfoEnabled()) {
                                e1 = System.currentTimeMillis();
                            }


                            log.info("Creating QueueConnection #" + (connectionsIterator + 1) + "  ...");
                            QueueConnection connection = connectionFactory.createQueueConnection(username, password);
                            log.info("Creating QueueConnection #" + (connectionsIterator + 1) + " lasted " + (System.currentTimeMillis() - e1) + " ms");
                            if(log.isInfoEnabled()) {
                                e1 = System.currentTimeMillis();
                            }

                           log.info("Starting QueueConnection #" + (connectionsIterator + 1) + " ...");
                            connection.start();
                           log.info("Starting QueueConnection #" + (connectionsIterator + 1) + " lasted " + (System.currentTimeMillis() - e1) + " ms");
                            this.connections.add(connection);
                        }

                        Iterator var20 = this.connections.iterator();

                        for(int connNo = 1; var20.hasNext(); ++connNo) {
                            QueueConnection conn = (QueueConnection)var20.next();
                            log.info("Start of creating QueueSessions of QueueConnection #" + connNo);

                            for(int j = 0; j < this.config.getNumberOfSessionsPerConnection(); ++j) {
                                if(log.isInfoEnabled()) {
                                    e1 = System.currentTimeMillis();
                                }

                                this.addIdleMQSession(new MQSession(conn, this.config.getSendQueueName(), this.config.getReciveQueueName(), this.config.getClient_ID(), this.config.getTimeout(), this.priority));
                                log.info("Creating QueueSession #" + (j + 1) + " of QueueConnection #" + connNo + " lasted " + (System.currentTimeMillis() - e1) + " ms");
                            }
                        }
                    } catch (JMSException var16) {
                        if(var16.getLinkedException() != null) {
                            log.error(var16.getLinkedException());
                        }

                        log.error(var16);
                        throw var16;
                    }
                }
            }
        }

        this.isStarted = true;

        try {
            this.getMQSession().setListener(new MQSessionPool.MyListener());
        } catch (Exception var15) {
            log.error(var15);
        }

    }
    private synchronized void start() throws JMSException {
        PropertyConfigurator.configureAndWatch("utils/log4j.properties", 5000L);
        MQQueueConnectionFactory connectionFactory = new MQQueueConnectionFactory();
        connectionFactory.setUseConnectionPooling(true);
        this.timer = new Timer();
        List e = this.workingMQSessions;
        synchronized(this.workingMQSessions) {
            List var4 = this.idleMQSessions;
            synchronized(this.idleMQSessions) {
                List var5 = this.connections;
                synchronized(this.connections) {
                    try {
                        if(this.config.getTransportType().equalsIgnoreCase(SenderConfig.REMOTE_TRANSPORT)) {
                           // connectionFactory.setTransportType(SenderConfig.REMOTE_TRANSPORT_ID);
                            connectionFactory.setHostName(this.config.getHostName());
                            connectionFactory.setTransportType(1);
                        }

                        connectionFactory.setPort(this.config.getHostPort());
                       // connectionFactory.setChannel(this.config.getChannel_name());
                        connectionFactory.setQueueManager(this.config.getQueue_manager_name());
                        log.info("Start of creating QueueConnections and QueueSessions.");
                        long e1 = System.currentTimeMillis();

                        for(int connectionsIterator = 0; connectionsIterator < this.config.getNumberOfConnections(); ++connectionsIterator) {
                            if(log.isInfoEnabled()) {
                                e1 = System.currentTimeMillis();
                            }

                            log.info("Creating QueueConnection #" + (connectionsIterator + 1) + "  ...");
                            QueueConnection connection = connectionFactory.createQueueConnection(this.config.getMQusername(), this.config.getMQuserPass());
                            log.info("Creating QueueConnection #" + (connectionsIterator + 1) + " lasted " + (System.currentTimeMillis() - e1) + " ms");
                            if(log.isInfoEnabled()) {
                                e1 = System.currentTimeMillis();
                            }

                            log.info("Starting QueueConnection #" + (connectionsIterator + 1) + " ...");
                            connection.start();
                            log.info("Starting QueueConnection #" + (connectionsIterator + 1) + " lasted " + (System.currentTimeMillis() - e1) + " ms");
                            this.connections.add(connection);
                        }

                        Iterator var20 = this.connections.iterator();

                        for(int connNo = 1; var20.hasNext(); ++connNo) {
                            QueueConnection conn = (QueueConnection)var20.next();
                            log.info("Start of creating QueueSessions of QueueConnection #" + connNo);

                            for(int j = 0; j < this.config.getNumberOfSessionsPerConnection(); ++j) {
                                if(log.isInfoEnabled()) {
                                    e1 = System.currentTimeMillis();
                                }

                                this.addIdleMQSession(new MQSession(conn, this.config.getSendQueueName(), this.config.getReciveQueueName(), this.config.getClient_ID(), this.config.getTimeout(), this.priority));
                                log.info("Creating QueueSession #" + (j + 1) + " of QueueConnection #" + connNo + " lasted " + (System.currentTimeMillis() - e1) + " ms");
                            }
                        }
                    } catch (JMSException var16) {
                        if(var16.getLinkedException() != null) {
                            log.error(var16.getLinkedException());
                        }

                        log.error(var16);
                        throw var16;
                    }
                }
            }
        }

        this.isStarted = true;

        try {
            this.getMQSession().setListener(new MQSessionPool.MyListener());
        } catch (Exception var15) {
            log.error(var15);
        }

    }
    private void addIdleMQSession(MQSession session) {
        List var2 = this.idleMQSessions;
        synchronized(this.idleMQSessions) {
            this.idleMQSessions.add(session);
        }
    }

    public synchronized MQSession getMQSession() throws NoIdleSessionException, JMSException {
        if(!this.isStarted()) {
            throw new NoIdleSessionException("MQSessionPool is not started");
        } else {
            List var1 = this.idleMQSessions;
            synchronized(this.idleMQSessions) {
                List var2 = this.connections;
                MQSession var10000;
                synchronized(this.connections) {
                    MQSession session;
                    List var4;
                    if(this.idleMQSessions.size() <= 0) {
                        if(this.connections.size() > 0) {
                            String CID=this.config.getClient_ID();
                            session = new MQSession((QueueConnection)this.connections.get(0), this.config.getSendQueueName(), this.config.getReciveQueueName(), this.config.getClient_ID(), this.config.getTimeout(), this.priority);
                          //  log.info("New Session created out of the pool !");
                            var4 = this.workingMQSessions;
                            synchronized(this.workingMQSessions) {
                                this.workingMQSessions.add(session);
                            }

                            session.setRequestTime(System.currentTimeMillis());
                            var10000 = session;
                            return var10000;
                        }

                        throw new NoIdleSessionException("No more free MQ Session is available in SessionPool.");
                    }

                 //   log.debug("Free MQ Sessions NO = " + this.idleMQSessions.size());
                    session = (MQSession)this.idleMQSessions.remove(0);
                    var4 = this.workingMQSessions;
                    synchronized(this.workingMQSessions) {
                        this.workingMQSessions.add(session);
                    }

                    session.setRequestTime(System.currentTimeMillis());
                    var10000 = session;
                }

                return var10000;
            }
        }
    }

    public synchronized void freeMQSession(MQSession session) {
        List var2 = this.workingMQSessions;
        synchronized(this.workingMQSessions) {
            List var3 = this.idleMQSessions;
            synchronized(this.idleMQSessions) {
                log.debug("MQSessions NO: Free = " + this.idleMQSessions.size() + "  Allocated = " + this.workingMQSessions.size());
                if(this.workingMQSessions.contains(session)) {
                    this.workingMQSessions.remove(session);
                }

                this.addIdleMQSession(session);
            }

        }
    }

    public synchronized void stop() {
        this.isStarted = false;
        List var1 = this.workingMQSessions;
        synchronized(this.workingMQSessions) {
            List var2 = this.idleMQSessions;
            synchronized(this.idleMQSessions) {
                List var3 = this.connections;
                synchronized(this.connections) {
                    boolean var20 = false;

                    Iterator e;
                    QueueConnection connection2;
                    label239: {
                        try {
                            var20 = true;
                            e = this.idleMQSessions.iterator();

                            MQSession connection;
                            while(e.hasNext()) {
                                connection = (MQSession)e.next();
                                if(connection.getSession().getTransacted()) {
                                    connection.getSession().rollback();
                                }

                                connection.getSender().close();
                                connection.getSession().close();
                            }

                            e = this.workingMQSessions.iterator();

                            while(e.hasNext()) {
                                connection = (MQSession)e.next();
                                if(connection.getSession().getTransacted()) {
                                    connection.getSession().rollback();
                                }

                                connection.getSender().close();
                                connection.getSession().close();
                            }

                            var20 = false;
                            break label239;
                        } catch (JMSException var24) {
                            var20 = false;
                        } finally {
                            if(var20) {
                                try {
                                    Iterator e1 = this.connections.iterator();

                                    while(e1.hasNext()) {
                                        QueueConnection connection1 = (QueueConnection)e1.next();
                                        connection1.stop();
                                        connection1.close();
                                    }
                                } catch (Exception var21) {
                                    ;
                                }

                                this.connections.clear();
                                this.idleMQSessions.clear();
                                this.workingMQSessions.clear();
                            }
                        }

                        try {
                            e = this.connections.iterator();

                            while(e.hasNext()) {
                                connection2 = (QueueConnection)e.next();
                                connection2.stop();
                                connection2.close();
                            }
                        } catch (Exception var22) {
                            ;
                        }

                        this.connections.clear();
                        this.idleMQSessions.clear();
                        this.workingMQSessions.clear();
                        return;
                    }

                    try {
                        e = this.connections.iterator();

                        while(e.hasNext()) {
                            connection2 = (QueueConnection)e.next();
                            connection2.stop();
                            connection2.close();
                        }
                    } catch (Exception var23) {
                        ;
                    }

                    this.connections.clear();
                    this.idleMQSessions.clear();
                    this.workingMQSessions.clear();
                }
            }

        }
    }

    public synchronized void reconnect() throws JMSException {
        synchronized(this) {
            this.stop();
            this.start();
        }
    }

    public int getChannelState() {
        if(!this.isStarted) {
            return MainMQ.STATE_EBANKING_NOT_CONNECTED;
        } else {
            MQSession session = null;

            int var3;
            try {
                session = this.getMQSession();
                session.sendAndReceive("PING_MESSAGE", "1", "1");
                return MainMQ.STATE_MQ_UP_CHANNEL_UP;
            } catch (JMSException var9) {
                var9.printStackTrace();
                if(var9.getLinkedException() != null) {
                    var9.getLinkedException().printStackTrace();
                }

                var3 = MainMQ.STATE_MQ_DWON;
            } catch (TimeoutException var10) {
                log.error(var10);
                var3 = MainMQ.STATE_MQ_UP_CHANNEL_DOWN;
                return var3;
            } catch (NoIdleSessionException var11) {
                var3 = MainMQ.STATE_MQ_DWON;
                return var3;
            } finally {
                if(session != null) {
                    this.freeMQSession(session);
                }

            }

            return var3;
        }
    }

    public boolean isStarted() {
        return this.isStarted;
    }

    public synchronized void remove(MQSession session) {
        if(this.idleMQSessions.contains(session)) {
            this.idleMQSessions.remove(session);
        }

        if(this.workingMQSessions.contains(session)) {
            this.workingMQSessions.remove(session);
        }

    }

    class MyListener implements MessageListener {
        MyListener() {
        }

        public void onMessage(Message message) {
          //  MQSessionPool.log.info("1. Response counter: " + ++MQSessionPool.responseCounter);
          //  MQSessionPool.log.info("Size of correlationId map == " + MQSession.threads.size());

            try {
                if(!MQSession.threads.containsKey(message.getJMSCorrelationID())) {
                 System.out.println("1. A response without request, MsgSeq: " + ((BaseMessage)((BaseMessage)((ObjectMessage)message).getObject())).getMsgSequence());
                   // MQSessionPool.log.error("1. A response without request, MsgSeq: " + ((BaseMessage)((BaseMessage)((ObjectMessage)message).getObject())).getMsgSequence());
                }
                //System.out.println("JMSCorrelationID is : " + message.getJMSCorrelationID());
                MQSession e = (MQSession)MQSession.threads.get(message.getJMSCorrelationID());

                if(e != null) {
                    if(System.currentTimeMillis() - e.getRequestTime() > MQSessionPool.this.config.getTimeout()) {
                       // MQSessionPool.log.error("*****The response is too late, MsgSequence: " + ((BaseMessage)((BaseMessage)((ObjectMessage)message).getObject())).getMsgSequence());
                    }

                    //System.out.println("message is : "+message);
                   // PrintWriter out = new PrintWriter("message.txt");
                   // out.write(message.toString());
                   // FileUtils.writeStringToFile(new File("message1.txt"), ((TextMessage) message).getText());
                    Object o=((TextMessage) message).getText();
                    //String msg=((TextMessage) message).getText();
                    //e.setResponse(((ObjectMessage)message).getObject());
                    e.setResponse(o);
                    o=null;
                    synchronized(e.getCurrentCorrelationId()) {
                        e.getCurrentCorrelationId().notify();
                    }
                } else {
                   // MQSessionPool.log.error("1. A response without request, MsgSequence: " + ((BaseMessage)((BaseMessage)((ObjectMessage)message).getObject())).getMsgSequence());
                }
            } catch (Exception var6) {
                var6.printStackTrace();

                // MQSessionPool.log.error(var6);
            }

        }
    }
}
