package Mainchannel;

import Mainchannel.Exceptions.ConfigurationException;
import Mainchannel.Exceptions.InvalidParameterException;
import Mainchannel.Exceptions.ResponseParsingException;
import Mainchannel.Exceptions.TimeoutException;
import Mainchannel.messages.*;
import Mainchannel.sender.SenderConfig;
import Mainchannel.sender.SenderException;
import Mainchannel.util.JMSConnectionPool.MQSession;
import Mainchannel.util.JMSConnectionPool.MQSessionPool;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.log4j.PropertyConfigurator;

import javax.jms.IllegalStateException;
import javax.jms.JMSException;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Properties;

/**
 * Created by root on 4/18/16.
 */
public class MainMQ_Synchronized {

    private static Log log = LogFactory.getLog(MainMQ.class);
    private static MQSessionPool sessionPool;
    private static XML2MessageConvertor xml2MessageConvertor;
    private static Message2XMLConvertor message2XMLConvertor;
    private static int MESSAGES_PRIORITY = 5;
    public static int STATE_IO_ERROR = 0;
    public static int STATE_MQ_DWON = 1;
    public static int STATE_MQ_UP_CHANNEL_DOWN = 2;
    public static int STATE_MQ_UP_CHANNEL_UP = 3;
    public static int STATE_EBANKING_NOT_CONNECTED = 4;
    private static SenderConfig senderConfig;
    static Integer responseCount = Integer.valueOf(1);

    public MainMQ_Synchronized() {
    }

    public static synchronized void Init() throws Exception {
        Properties properties = new Properties();
        final File FileOfSettings=new File(MainMQ.class.getProtectionDomain().getCodeSource().getLocation().getPath());
        String Path1 =FileOfSettings.toString();
        int i="Gateway.jar".length();
        String Path =Path1.substring(0,Path1.length()-i);
        Properties props = new Properties();
        try{
            properties.load(new FileInputStream(Path+"/MainMQ.properties")); //if jar file in running
        }catch (Exception e){
            try{
                Path =Path1+"/Mainchannel/MainMQ.properties";
                properties.load(new FileInputStream(Path));// if class in running
            }catch (Exception ee){}
        }

        PropertyConfigurator.configureAndWatch("utils/log4j.properties", 5000L);

        try {
            xml2MessageConvertor = new XML2MessageConvertor();
            message2XMLConvertor = new Message2XMLConvertor();
            String t = properties.getProperty("Host_Address");
            String queue_manager_name = properties.getProperty("Queue_Manager_Name");
            String client_ID = properties.getProperty("Client_ID");
            String reciveQueueName = properties.getProperty("Receive_Queue");
            String sendQueueName = properties.getProperty("Send_Queue");
            String hostPort = properties.getProperty("Host_Port");
            String channel_name = properties.getProperty("Channel_Name");
            String MQusername = properties.getProperty("User_Name");
            String MQuserPass = properties.getProperty("User_Password");
            String transportType = properties.getProperty("Transport_Type");
            String numberOfConnections = properties.getProperty("Number_Of_Connections");
            String numberOfSessionsPerConnection = properties.getProperty("Number_Of_Sessions_Per_Connection");
            String timeout = properties.getProperty("Send_Receive_Timeout");
            if(t == null) {
                throw new ConfigurationException("HostAddress not set in host.properties");
            } else if(queue_manager_name == null) {
                throw new ConfigurationException("HostAddress not set in host.properties");
            } else if(client_ID == null) {
                throw new ConfigurationException("Client_ID not set in host.properties");
            } else if(client_ID.length() > 10) {
                throw new ConfigurationException("Length of Client_ID in host.properties must be less than 10 charachters");
            } else if(reciveQueueName == null) {
                throw new ConfigurationException("Receive_Queue not set in host.properties");
            } else if(sendQueueName == null) {
                throw new ConfigurationException("Send_Queue not set in host.properties");
            } else if(hostPort == null) {
                throw new ConfigurationException("Host_Port not set in host.properties");
            } else if(channel_name == null) {
                throw new ConfigurationException("Channel_Name not set in host.properties");
            } else if(MQusername == null) {
                throw new ConfigurationException("User_Name not set in host.properties");
            } else if(MQuserPass == null) {
                throw new ConfigurationException("User_Name not set in host.properties");
            } else if(transportType == null) {
                throw new ConfigurationException("Transport_Type not set in host.properties");
            } else if(numberOfConnections == null) {
                throw new ConfigurationException("Number_Of_Connections not set in host.properties");
            } else if(numberOfSessionsPerConnection == null) {
                throw new ConfigurationException("Number_Of_Sessions_Per_Connection not set in host.properties");
            } else if(timeout == null) {
                throw new ConfigurationException("Timeout not set in host.properties");
            } else {
                senderConfig = new SenderConfig();
                senderConfig.setChannel_name(channel_name);
                senderConfig.setClient_ID(client_ID);
                senderConfig.setHostName(t);
                senderConfig.setHostPort(Integer.parseInt(hostPort));
                senderConfig.setMQusername(MQusername);
                senderConfig.setMQuserPass(MQuserPass);
                senderConfig.setQueue_manager_name(queue_manager_name);
                senderConfig.setReciveQueueName(reciveQueueName);
                senderConfig.setSendQueueName(sendQueueName);
                senderConfig.setTransportType(transportType);
                senderConfig.setNumberOfConnections(Integer.valueOf(numberOfConnections).intValue());
                senderConfig.setNumberOfSessionsPerConnection(Integer.valueOf(numberOfSessionsPerConnection).intValue());
                senderConfig.setTimeout((long)(Integer.valueOf(timeout).intValue() * 1000));
                sessionPool = new MQSessionPool(senderConfig, MESSAGES_PRIORITY);
                Runtime.getRuntime().addShutdownHook(new Thread() {
                    public void run() {
                        MainMQ_Synchronized.sessionPool.stop();
                    }
                });
            }
        } catch (Exception var14) {
            log.error(var14.getMessage());
            throw var14;
        } catch (Throwable var17) {
            log.error(var17);
            throw new Exception(var17);
        }
    }

    public static synchronized boolean ValidateBaseParameters(String channelType, String channelPass, String channelID) throws InvalidParameterException {
        Thread.currentThread().setContextClassLoader(MainMQ.class.getClassLoader());
        String errors = "";
        if(channelType == null || channelType.equals("")) {
            errors = "channelType == null || channelType.equals(\"\")";
        }

        if(channelPass == null || channelPass.equals("")) {
            errors = errors + " \nchannelPass == null || channelPass.equals(\"\")";
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

    private  static synchronized Object callService(String request, String msgSeq, String channelId) throws SenderException {
        MQSession session = null;
        boolean sessionWasClosedUbnormaly = false;
        long t2 = 0L;

        Object responseStr;
        try {
            long t1 = System.currentTimeMillis();
            session = sessionPool.getMQSession();
            // log.info("Request  = " + request);
            //System.out.println("===========================================================");
            //System.out.println("request is : " + request);
            // System.out.println("msgSeq is : "+msgSeq);
            //System.out.println("channelId is : "+channelId);
            // System.out.println("-------------------------------------------------------------");
            t2 = System.currentTimeMillis();
            if(t2 - t1 > 50L) {
                System.out.println("connection failed :reason TimeOut" + (t2 - t1));
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
                log.error("sendAndReceivetime out :Time is : " + (t3 - t2));
            }

            if(sessionWasClosedUbnormaly) {
                sessionPool.remove(session);
            } else {
                sessionPool.freeMQSession(session);
            }

        }

        //  System.out.println("response is : " + responseStr);
        // System.out.println("msgSeq is : "+msgSeq);
        //System.out.println("channelId is : "+channelId);
        // System.out.println("===========================================================");
        //log.info("Response  = " + responseStr);
        return responseStr;
    }

    public static synchronized AccountListMessage getAccountList(String accountNumber, String accountPass, String channelType, String channelPass, String channelID, String msgSeq, String requestDateTime, String encAlgorytm, String MAC) throws SenderException, InvalidParameterException, ResponseParsingException {
        ValidateBaseParameters(channelType, channelPass, channelID);
        String xmlRequest = message2XMLConvertor.GenerateAccountListRequest(accountNumber, accountPass, channelType, channelPass, channelID, msgSeq, requestDateTime, encAlgorytm, MAC);
        Object response = callService(xmlRequest, msgSeq, channelID);
        AccountListMessage responseMessage;
        if(response instanceof AccountListMessage) {
            responseMessage = (AccountListMessage)response;
        } else if(response instanceof BaseMessage) {
            responseMessage = xml2MessageConvertor.getResponseAccountList(((BaseMessage)response).getResponseXml());
            responseMessage.setResponseXml(((BaseMessage)response).getResponseXml());
        } else {
            responseMessage = xml2MessageConvertor.getResponseAccountList(response.toString());
            responseMessage.setResponseXml(response.toString());
        }
        responseMessage.setRequestXml(xmlRequest);


        log.info("Response =" + responseMessage.getResponseXml());
        return responseMessage;
    }
    //get account information
    public static synchronized String getAccountListStr(String accountNumber, String accountPass, String channelType, String channelPass, String channelID, String msgSeq, String requestDateTime, String encAlgorytm, String MAC) {
        try {
            AccountListMessage e = getAccountList(accountNumber, accountPass, channelType, channelPass, channelID, msgSeq, requestDateTime, encAlgorytm, MAC);
            return e == null?"NULL":e.getResponseXml();
        } catch (Exception var10) {
            log.error(var10);
            return var10.getMessage();
        }
    }

    public static synchronized BalanceMessage getBalance(String accountNumber, String accountPass, String requestDateTime, String channelType, String channelPass, String channelID, String msgSeq, String encAlgorytm, String MAC)  {
        String temp="";
        try {
            if(requestDateTime.length() < 14) {
                new InvalidParameterException("getBalance: requestDateTime.length() < 14");
            }

            ValidateBaseParameters(channelType, channelPass, channelID);
            // message2XMLConvertor = new Message2XMLConvertor();
            String xmlRequest = message2XMLConvertor.GenerateBalanceRequest(accountNumber, accountPass, requestDateTime, channelType,
                    channelPass, channelID, msgSeq, encAlgorytm, MAC);
            temp=xmlRequest;
            Object response = callService(xmlRequest, msgSeq, channelID);
            BalanceMessage responseMessage;
            if(response instanceof BalanceMessage) {
                responseMessage = (BalanceMessage)response;
            } else if(response instanceof BaseMessage) {
                responseMessage = xml2MessageConvertor.getResponseBalance(((BaseMessage)response).getResponseXml());
                responseMessage.setResponseXml(((BaseMessage)response).getResponseXml());
            } else {
                responseMessage = xml2MessageConvertor.getResponseBalance(response.toString());
                responseMessage.setResponseXml(response.toString());
            }
            responseMessage.setRequestXml(xmlRequest);
            log.info("Response =" + responseMessage.getResponseXml());
            return responseMessage;
        }catch (Exception e){
            BalanceMessage responseMessage=new BalanceMessage();
            responseMessage.setRequestXml(temp);
            responseMessage.setAction_desc(e.toString());
            return responseMessage;
        }
    }
    //get balance of account
    public static synchronized String getBalanceStr(String accountNumber, String accountPass, String requestDateTime, String channelType, String channelPass, String channelID, String msgSequence, String encAlgorytm, String MAC) throws Exception {
        BalanceMessage response = getBalance(accountNumber, accountPass, requestDateTime, channelType, channelPass, channelID, msgSequence, encAlgorytm, MAC);
        return response.getResponseXml();
    }

    public static synchronized StatementListMessage getTransactionList(String accountNumber, String accountPass, String channelType, String channelPass, String channelID, String fromDate, String toDate, String fromTime, String toTime, String transCount, String creditDebit, String transMinAmount, String transMaxAmount, String transDocNo, String transOprationCode, String transDesc, String BranchCode, String msgSeq, String stmType, String rrn, String requestDateTime, String encAlgorytm, String MAC) throws SenderException, InvalidParameterException, ResponseParsingException {

        ValidateBaseParameters(channelType, channelPass, channelID);
        String xmlRequest = message2XMLConvertor.GenerateStatementListRequest(accountNumber, accountPass, channelType, channelPass, channelID, fromDate, toDate, fromTime, toTime, transCount, creditDebit, transMinAmount, transMaxAmount, transDocNo, transOprationCode, transDesc, BranchCode,rrn, stmType,msgSeq,requestDateTime, encAlgorytm, MAC);
        Object response = callService(xmlRequest, msgSeq, channelID);
        StatementListMessage responseMessage;
        if(response instanceof StatementListMessage) {
            responseMessage = (StatementListMessage)response;
        } else if(response instanceof BaseMessage) {
            responseMessage = xml2MessageConvertor.getResponseStatementList(((BaseMessage)response).getResponseXml());
            responseMessage.setResponseXml(((BaseMessage)response).getResponseXml());
        } else {
            responseMessage = xml2MessageConvertor.getResponseStatementList(response.toString());
            responseMessage.setResponseXml(response.toString());
        }
        responseMessage.setRequestXml(xmlRequest);


        return responseMessage;
    }
    public static synchronized StatementListMessage getTransactionList_(String accountNumber, String accountPass, String channelType, String channelPass, String channelID, String fromDate, String toDate, String fromTime, String toTime, String transCount, String creditDebit, String transMinAmount, String transMaxAmount, String transDocNo, String transOprationCode, String transDesc, String BranchCode, String msgSeq, String stmType, String rrn, String requestDateTime, String encAlgorytm, String MAC) throws SenderException, InvalidParameterException, ResponseParsingException {

        ValidateBaseParameters(channelType, channelPass, channelID);
        String xmlRequest = message2XMLConvertor.GenerateStatementListRequest(accountNumber, accountPass, channelType, channelPass, channelID, fromDate, toDate, fromTime, toTime, transCount, creditDebit, transMinAmount, transMaxAmount, transDocNo, transOprationCode, transDesc, BranchCode,rrn, stmType,msgSeq,requestDateTime, encAlgorytm, MAC);
      /* Object response = callService(xmlRequest, msgSeq, channelID);
        StatementListMessage responseMessage;
        if(response instanceof StatementListMessage) {
            responseMessage = (StatementListMessage)response;
        } else if(response instanceof BaseMessage) {
            responseMessage = xml2MessageConvertor.getResponseStatementList(((BaseMessage)response).getResponseXml());
            responseMessage.setResponseXml(((BaseMessage)response).getResponseXml());
        } else {
            responseMessage = xml2MessageConvertor.getResponseStatementList(response.toString());
            responseMessage.setResponseXml(response.toString());
        }*/
        //------------------------------
        StatementListMessage responseMessage;
        String res="<?xml version=\"1.0\" encoding=\"UTF-8\"?>\n" +
                "<!-- Generated: Wed Jan 13 10:36:06 GMT 2016-->\n" +
                "<RESPONSE><ACTIONCODE>0000</ACTIONCODE><DESC /><RESPTYPE>STATEMENTLIST</RESPTYPE><STATEMENTLIST><MSGSEQ>316301361832</MSGSEQ><CHNTYPE>NIVR</CHNTYPE><CHNID>38</CHNID><ACCNO>0004150645501</ACCNO><FDATE>13900101</FDATE><TDATE>13941023</TDATE><TRANSCOUNT>030</TRANSCOUNT><BALANCE>+00000000000363603</BALANCE><HOSTID>1</HOSTID><TRANSACTIONS><TRANSACTION><OPCODE>628</OPCODE><AMNT>000000000000005000</AMNT><CRDB>C</CRDB><DT>13941021</DT><TM>075515</TM><EFDT>13941021</EFDT><DOCNO>0757329</DOCNO><DESC>0000155971983</DESC><BRCODE>099991</BRCODE><ROW>000</ROW><LAMNT>000000000001919260</LAMNT><PAYIDS></PAYIDS><TRCKID>283802757329</TRCKID><TRML>34</TRML><EXTRAINFO>00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000انتقال آني ازحساب 0000155971983 به حساب 0004150645501 ازدرگاه تلفن</EXTRAINFO><RRN>000000000000</RRN></TRANSACTION><TRANSACTION><OPCODE>628</OPCODE><AMNT>000000000000005000</AMNT><CRDB>C</CRDB><DT>13941021</DT><TM>075515</TM><EFDT>13941021</EFDT><DOCNO>0757329</DOCNO><DESC>0000155971983</DESC><BRCODE>099991</BRCODE><ROW>001</ROW><LAMNT>000000000001919260</LAMNT><PAYIDS></PAYIDS><TRCKID>283802757329</TRCKID><TRML>34</TRML><EXTRAINFO>00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000انتقال آني ازحساب 0000155971983 به حساب 0004150645501 ازدرگاه تلفن</EXTRAINFO><RRN>000000000000</RRN></TRANSACTION><TRANSACTION><OPCODE>628</OPCODE><AMNT>000000000000005000</AMNT><CRDB>C</CRDB><DT>13941021</DT><TM>075515</TM><EFDT>13941021</EFDT><DOCNO>0757329</DOCNO><DESC>0000155971983</DESC><BRCODE>099991</BRCODE><ROW>002</ROW><LAMNT>000000000001919260</LAMNT><PAYIDS></PAYIDS><TRCKID>283802757329</TRCKID><TRML>34</TRML><EXTRAINFO>00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000انتقال آني ازحساب 0000155971983 به حساب 0004150645501 ازدرگاه تلفن</EXTRAINFO><RRN>000000000000</RRN></TRANSACTION><TRANSACTION><OPCODE>628</OPCODE><AMNT>000000000000005000</AMNT><CRDB>C</CRDB><DT>13941021</DT><TM>075515</TM><EFDT>13941021</EFDT><DOCNO>0757329</DOCNO><DESC>0000155971983</DESC><BRCODE>099991</BRCODE><ROW>003</ROW><LAMNT>000000000001919260</LAMNT><PAYIDS></PAYIDS><TRCKID>283802757329</TRCKID><TRML>34</TRML><EXTRAINFO>00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000انتقال آني ازحساب 0000155971983 به حساب 0004150645501 ازدرگاه تلفن</EXTRAINFO><RRN>000000000000</RRN></TRANSACTION><TRANSACTION><OPCODE>628</OPCODE><AMNT>000000000000005000</AMNT><CRDB>C</CRDB><DT>13941021</DT><TM>075515</TM><EFDT>13941021</EFDT><DOCNO>0757329</DOCNO><DESC>0000155971983</DESC><BRCODE>099991</BRCODE><ROW>004</ROW><LAMNT>000000000001919260</LAMNT><PAYIDS></PAYIDS><TRCKID>283802757329</TRCKID><TRML>34</TRML><EXTRAINFO>00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000انتقال آني ازحساب 0000155971983 به حساب 0004150645501 ازدرگاه تلفن</EXTRAINFO><RRN>000000000000</RRN></TRANSACTION><TRANSACTION><OPCODE>628</OPCODE><AMNT>000000000000005000</AMNT><CRDB>C</CRDB><DT>13941021</DT><TM>075515</TM><EFDT>13941021</EFDT><DOCNO>0757329</DOCNO><DESC>0000155971983</DESC><BRCODE>099991</BRCODE><ROW>005</ROW><LAMNT>000000000001919260</LAMNT><PAYIDS></PAYIDS><TRCKID>283802757329</TRCKID><TRML>34</TRML><EXTRAINFO>00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000انتقال آني ازحساب 0000155971983 به حساب 0004150645501 ازدرگاه تلفن</EXTRAINFO><RRN>000000000000</RRN></TRANSACTION><TRANSACTION><OPCODE>628</OPCODE><AMNT>000000000000005000</AMNT><CRDB>C</CRDB><DT>13941021</DT><TM>075515</TM><EFDT>13941021</EFDT><DOCNO>0757329</DOCNO><DESC>0000155971983</DESC><BRCODE>099991</BRCODE><ROW>006</ROW><LAMNT>000000000001919260</LAMNT><PAYIDS></PAYIDS><TRCKID>283802757329</TRCKID><TRML>34</TRML><EXTRAINFO>00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000انتقال آني ازحساب 0000155971983 به حساب 0004150645501 ازدرگاه تلفن</EXTRAINFO><RRN>000000000000</RRN></TRANSACTION><TRANSACTION><OPCODE>628</OPCODE><AMNT>000000000000005000</AMNT><CRDB>C</CRDB><DT>13941021</DT><TM>075515</TM><EFDT>13941021</EFDT><DOCNO>0757329</DOCNO><DESC>0000155971983</DESC><BRCODE>099991</BRCODE><ROW>007</ROW><LAMNT>000000000001919260</LAMNT><PAYIDS></PAYIDS><TRCKID>283802757329</TRCKID><TRML>34</TRML><EXTRAINFO>00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000انتقال آني ازحساب 0000155971983 به حساب 0004150645501 ازدرگاه تلفن</EXTRAINFO><RRN>000000000000</RRN></TRANSACTION><TRANSACTION><OPCODE>628</OPCODE><AMNT>000000000000005000</AMNT><CRDB>C</CRDB><DT>13941021</DT><TM>075515</TM><EFDT>13941021</EFDT><DOCNO>0757329</DOCNO><DESC>0000155971983</DESC><BRCODE>099991</BRCODE><ROW>008</ROW><LAMNT>000000000001919260</LAMNT><PAYIDS></PAYIDS><TRCKID>283802757329</TRCKID><TRML>34</TRML><EXTRAINFO>00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000انتقال آني ازحساب 0000155971983 به حساب 0004150645501 ازدرگاه تلفن</EXTRAINFO><RRN>000000000000</RRN></TRANSACTION><TRANSACTION><OPCODE>628</OPCODE><AMNT>000000000000005000</AMNT><CRDB>C</CRDB><DT>13941021</DT><TM>075515</TM><EFDT>13941021</EFDT><DOCNO>0757329</DOCNO><DESC>0000155971983</DESC><BRCODE>099991</BRCODE><ROW>009</ROW><LAMNT>000000000001919260</LAMNT><PAYIDS></PAYIDS><TRCKID>283802757329</TRCKID><TRML>34</TRML><EXTRAINFO>00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000انتقال آني ازحساب 0000155971983 به حساب 0004150645501 ازدرگاه تلفن</EXTRAINFO><RRN>000000000000</RRN></TRANSACTION><TRANSACTION><OPCODE>628</OPCODE><AMNT>000000000000005000</AMNT><CRDB>C</CRDB><DT>13941021</DT><TM>075515</TM><EFDT>13941021</EFDT><DOCNO>0757329</DOCNO><DESC>0000155971983</DESC><BRCODE>099991</BRCODE><ROW>010</ROW><LAMNT>000000000001919260</LAMNT><PAYIDS></PAYIDS><TRCKID>283802757329</TRCKID><TRML>34</TRML><EXTRAINFO>00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000انتقال آني ازحساب 0000155971983 به حساب 0004150645501 ازدرگاه تلفن</EXTRAINFO><RRN>000000000000</RRN></TRANSACTION><TRANSACTION><OPCODE>628</OPCODE><AMNT>000000000000005000</AMNT><CRDB>C</CRDB><DT>13941021</DT><TM>075515</TM><EFDT>13941021</EFDT><DOCNO>0757329</DOCNO><DESC>0000155971983</DESC><BRCODE>099991</BRCODE><ROW>011</ROW><LAMNT>000000000001919260</LAMNT><PAYIDS></PAYIDS><TRCKID>283802757329</TRCKID><TRML>34</TRML><EXTRAINFO>00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000انتقال آني ازحساب 0000155971983 به حساب 0004150645501 ازدرگاه تلفن</EXTRAINFO><RRN>000000000000</RRN></TRANSACTION><TRANSACTION><OPCODE>628</OPCODE><AMNT>000000000000005000</AMNT><CRDB>C</CRDB><DT>13941021</DT><TM>075515</TM><EFDT>13941021</EFDT><DOCNO>0757329</DOCNO><DESC>0000155971983</DESC><BRCODE>099991</BRCODE><ROW>012</ROW><LAMNT>000000000001919260</LAMNT><PAYIDS></PAYIDS><TRCKID>283802757329</TRCKID><TRML>34</TRML><EXTRAINFO>00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000انتقال آني ازحساب 0000155971983 به حساب 0004150645501 ازدرگاه تلفن</EXTRAINFO><RRN>000000000000</RRN></TRANSACTION><TRANSACTION><OPCODE>628</OPCODE><AMNT>000000000000005000</AMNT><CRDB>C</CRDB><DT>13941021</DT><TM>075515</TM><EFDT>13941021</EFDT><DOCNO>0757329</DOCNO><DESC>0000155971983</DESC><BRCODE>099991</BRCODE><ROW>013</ROW><LAMNT>000000000001919260</LAMNT><PAYIDS></PAYIDS><TRCKID>283802757329</TRCKID><TRML>34</TRML><EXTRAINFO>00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000انتقال آني ازحساب 0000155971983 به حساب 0004150645501 ازدرگاه تلفن</EXTRAINFO><RRN>000000000000</RRN></TRANSACTION><TRANSACTION><OPCODE>628</OPCODE><AMNT>000000000000005000</AMNT><CRDB>C</CRDB><DT>13941021</DT><TM>075515</TM><EFDT>13941021</EFDT><DOCNO>0757329</DOCNO><DESC>0000155971983</DESC><BRCODE>099991</BRCODE><ROW>014</ROW><LAMNT>000000000001919260</LAMNT><PAYIDS></PAYIDS><TRCKID>283802757329</TRCKID><TRML>34</TRML><EXTRAINFO>00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000انتقال آني ازحساب 0000155971983 به حساب 0004150645501 ازدرگاه تلفن</EXTRAINFO><RRN>000000000000</RRN></TRANSACTION><TRANSACTION><OPCODE>628</OPCODE><AMNT>000000000000005000</AMNT><CRDB>C</CRDB><DT>13941021</DT><TM>075515</TM><EFDT>13941021</EFDT><DOCNO>0757329</DOCNO><DESC>0000155971983</DESC><BRCODE>099991</BRCODE><ROW>015</ROW><LAMNT>000000000001919260</LAMNT><PAYIDS></PAYIDS><TRCKID>283802757329</TRCKID><TRML>34</TRML><EXTRAINFO>00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000انتقال آني ازحساب 0000155971983 به حساب 0004150645501 ازدرگاه تلفن</EXTRAINFO><RRN>000000000000</RRN></TRANSACTION><TRANSACTION><OPCODE>628</OPCODE><AMNT>000000000000005000</AMNT><CRDB>C</CRDB><DT>13941021</DT><TM>075515</TM><EFDT>13941021</EFDT><DOCNO>0757329</DOCNO><DESC>0000155971983</DESC><BRCODE>099991</BRCODE><ROW>016</ROW><LAMNT>000000000001919260</LAMNT><PAYIDS></PAYIDS><TRCKID>283802757329</TRCKID><TRML>34</TRML><EXTRAINFO>00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000انتقال آني ازحساب 0000155971983 به حساب 0004150645501 ازدرگاه تلفن</EXTRAINFO><RRN>000000000000</RRN></TRANSACTION><TRANSACTION><OPCODE>628</OPCODE><AMNT>000000000000005000</AMNT><CRDB>C</CRDB><DT>13941021</DT><TM>075515</TM><EFDT>13941021</EFDT><DOCNO>0757329</DOCNO><DESC>0000155971983</DESC><BRCODE>099991</BRCODE><ROW>017</ROW><LAMNT>000000000001919260</LAMNT><PAYIDS></PAYIDS><TRCKID>283802757329</TRCKID><TRML>34</TRML><EXTRAINFO>00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000انتقال آني ازحساب 0000155971983 به حساب 0004150645501 ازدرگاه تلفن</EXTRAINFO><RRN>000000000000</RRN></TRANSACTION><TRANSACTION><OPCODE>628</OPCODE><AMNT>000000000000005000</AMNT><CRDB>C</CRDB><DT>13941021</DT><TM>075515</TM><EFDT>13941021</EFDT><DOCNO>0757329</DOCNO><DESC>0000155971983</DESC><BRCODE>099991</BRCODE><ROW>018</ROW><LAMNT>000000000001919260</LAMNT><PAYIDS></PAYIDS><TRCKID>283802757329</TRCKID><TRML>34</TRML><EXTRAINFO>00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000انتقال آني ازحساب 0000155971983 به حساب 0004150645501 ازدرگاه تلفن</EXTRAINFO><RRN>000000000000</RRN></TRANSACTION><TRANSACTION><OPCODE>628</OPCODE><AMNT>000000000000005000</AMNT><CRDB>C</CRDB><DT>13941021</DT><TM>075515</TM><EFDT>13941021</EFDT><DOCNO>0757329</DOCNO><DESC>0000155971983</DESC><BRCODE>099991</BRCODE><ROW>019</ROW><LAMNT>000000000001919260</LAMNT><PAYIDS></PAYIDS><TRCKID>283802757329</TRCKID><TRML>34</TRML><EXTRAINFO>00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000انتقال آني ازحساب 0000155971983 به حساب 0004150645501 ازدرگاه تلفن</EXTRAINFO><RRN>000000000000</RRN></TRANSACTION><TRANSACTION><OPCODE>628</OPCODE><AMNT>000000000000005000</AMNT><CRDB>C</CRDB><DT>13941021</DT><TM>075515</TM><EFDT>13941021</EFDT><DOCNO>0757329</DOCNO><DESC>0000155971983</DESC><BRCODE>099991</BRCODE><ROW>020</ROW><LAMNT>000000000001919260</LAMNT><PAYIDS></PAYIDS><TRCKID>283802757329</TRCKID><TRML>34</TRML><EXTRAINFO>00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000انتقال آني ازحساب 0000155971983 به حساب 0004150645501 ازدرگاه تلفن</EXTRAINFO><RRN>000000000000</RRN></TRANSACTION><TRANSACTION><OPCODE>628</OPCODE><AMNT>000000000000005000</AMNT><CRDB>C</CRDB><DT>13941021</DT><TM>075515</TM><EFDT>13941021</EFDT><DOCNO>0757329</DOCNO><DESC>0000155971983</DESC><BRCODE>099991</BRCODE><ROW>021</ROW><LAMNT>000000000001919260</LAMNT><PAYIDS></PAYIDS><TRCKID>283802757329</TRCKID><TRML>34</TRML><EXTRAINFO>00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000انتقال آني ازحساب 0000155971983 به حساب 0004150645501 ازدرگاه تلفن</EXTRAINFO><RRN>000000000000</RRN></TRANSACTION><TRANSACTION><OPCODE>628</OPCODE><AMNT>000000000000005000</AMNT><CRDB>C</CRDB><DT>13941021</DT><TM>075515</TM><EFDT>13941021</EFDT><DOCNO>0757329</DOCNO><DESC>0000155971983</DESC><BRCODE>099991</BRCODE><ROW>022</ROW><LAMNT>000000000001919260</LAMNT><PAYIDS></PAYIDS><TRCKID>283802757329</TRCKID><TRML>34</TRML><EXTRAINFO>00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000انتقال آني ازحساب 0000155971983 به حساب 0004150645501 ازدرگاه تلفن</EXTRAINFO><RRN>000000000000</RRN></TRANSACTION><TRANSACTION><OPCODE>628</OPCODE><AMNT>000000000000005000</AMNT><CRDB>C</CRDB><DT>13941021</DT><TM>075515</TM><EFDT>13941021</EFDT><DOCNO>0757329</DOCNO><DESC>0000155971983</DESC><BRCODE>099991</BRCODE><ROW>023</ROW><LAMNT>000000000001919260</LAMNT><PAYIDS></PAYIDS><TRCKID>283802757329</TRCKID><TRML>34</TRML><EXTRAINFO>00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000انتقال آني ازحساب 0000155971983 به حساب 0004150645501 ازدرگاه تلفن</EXTRAINFO><RRN>000000000000</RRN></TRANSACTION><TRANSACTION><OPCODE>628</OPCODE><AMNT>000000000000005000</AMNT><CRDB>C</CRDB><DT>13941021</DT><TM>075515</TM><EFDT>13941021</EFDT><DOCNO>0757329</DOCNO><DESC>0000155971983</DESC><BRCODE>099991</BRCODE><ROW>024</ROW><LAMNT>000000000001919260</LAMNT><PAYIDS></PAYIDS><TRCKID>283802757329</TRCKID><TRML>34</TRML><EXTRAINFO>00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000انتقال آني ازحساب 0000155971983 به حساب 0004150645501 ازدرگاه تلفن</EXTRAINFO><RRN>000000000000</RRN></TRANSACTION><TRANSACTION><OPCODE>628</OPCODE><AMNT>000000000000005000</AMNT><CRDB>C</CRDB><DT>13941021</DT><TM>075515</TM><EFDT>13941021</EFDT><DOCNO>0757329</DOCNO><DESC>0000155971983</DESC><BRCODE>099991</BRCODE><ROW>025</ROW><LAMNT>000000000001919260</LAMNT><PAYIDS></PAYIDS><TRCKID>283802757329</TRCKID><TRML>34</TRML><EXTRAINFO>00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000انتقال آني ازحساب 0000155971983 به حساب 0004150645501 ازدرگاه تلفن</EXTRAINFO><RRN>000000000000</RRN></TRANSACTION><TRANSACTION><OPCODE>628</OPCODE><AMNT>000000000000005000</AMNT><CRDB>C</CRDB><DT>13941021</DT><TM>075515</TM><EFDT>13941021</EFDT><DOCNO>0757329</DOCNO><DESC>0000155971983</DESC><BRCODE>099991</BRCODE><ROW>026</ROW><LAMNT>000000000001919260</LAMNT><PAYIDS></PAYIDS><TRCKID>283802757329</TRCKID><TRML>34</TRML><EXTRAINFO>00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000انتقال آني ازحساب 0000155971983 به حساب 0004150645501 ازدرگاه تلفن</EXTRAINFO><RRN>000000000000</RRN></TRANSACTION><TRANSACTION><OPCODE>628</OPCODE><AMNT>000000000000005000</AMNT><CRDB>C</CRDB><DT>13941021</DT><TM>075515</TM><EFDT>13941021</EFDT><DOCNO>0757329</DOCNO><DESC>0000155971983</DESC><BRCODE>099991</BRCODE><ROW>027</ROW><LAMNT>000000000001919260</LAMNT><PAYIDS></PAYIDS><TRCKID>283802757329</TRCKID><TRML>34</TRML><EXTRAINFO>00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000انتقال آني ازحساب 0000155971983 به حساب 0004150645501 ازدرگاه تلفن</EXTRAINFO><RRN>000000000000</RRN></TRANSACTION><TRANSACTION><OPCODE>628</OPCODE><AMNT>000000000000005000</AMNT><CRDB>C</CRDB><DT>13941021</DT><TM>075515</TM><EFDT>13941021</EFDT><DOCNO>0757329</DOCNO><DESC>0000155971983</DESC><BRCODE>099991</BRCODE><ROW>028</ROW><LAMNT>000000000001919260</LAMNT><PAYIDS></PAYIDS><TRCKID>283802757329</TRCKID><TRML>34</TRML><EXTRAINFO>00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000انتقال آني ازحساب 0000155971983 به حساب 0004150645501 ازدرگاه تلفن</EXTRAINFO><RRN>000000000000</RRN></TRANSACTION><TRANSACTION><OPCODE>628</OPCODE><AMNT>000000000000005000</AMNT><CRDB>C</CRDB><DT>13941021</DT><TM>075515</TM><EFDT>13941021</EFDT><DOCNO>0757329</DOCNO><DESC>0000155971983</DESC><BRCODE>099991</BRCODE><ROW>029</ROW><LAMNT>000000000001919260</LAMNT><PAYIDS></PAYIDS><TRCKID>283802757329</TRCKID><TRML>34</TRML><EXTRAINFO>00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000انتقال آني ازحساب 0000155971983 به حساب 0004150645501 ازدرگاه تلفن</EXTRAINFO><RRN>000000000000</RRN></TRANSACTION><TRANSACTION><OPCODE>628</OPCODE><AMNT>000000000000005000</AMNT><CRDB>C</CRDB><DT>13941021</DT><TM>075515</TM><EFDT>13941021</EFDT><DOCNO>0757329</DOCNO><DESC>0000155971983</DESC><BRCODE>099991</BRCODE><ROW>030</ROW><LAMNT>000000000001919260</LAMNT><PAYIDS></PAYIDS><TRCKID>283802757329</TRCKID><TRML>34</TRML><EXTRAINFO>00000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000000انتقال آني ازحساب 0000155971983 به حساب 0004150645501 ازدرگاه تلفن</EXTRAINFO><RRN>000000000000</RRN></TRANSACTION></TRANSACTIONS><RESPDATETIME>13941023102517</RESPDATETIME><MAC>1</MAC></STATEMENTLIST></RESPONSE>\n" +
                "\n";
        responseMessage = xml2MessageConvertor.getResponseStatementList(res);
        responseMessage.setResponseXml(res);
        responseMessage.setRequestXml(res);
        //------------------------------

        return responseMessage;
    }
    //get transaction list
    public static synchronized String getTransactionListStr(String accountNumber, String accountPass, String channelType, String channelPass, String channelID, String fromDate, String toDate, String fromTime, String toTime, String transCount, String creditDebit, String transMinAmount, String transMaxAmount, String transDocNo, String transOprationCode, String transDesc, String BranchCode, String msgSequence, String stmType, String rrn, String requestDateTime, String encAlgorytm, String MAC) {
        try {
            StatementListMessage e = getTransactionList(accountNumber, accountPass, channelType, channelPass, channelID, fromDate, toDate, fromTime, toTime, transCount, creditDebit, transMinAmount, transMaxAmount, transDocNo, transOprationCode, transDesc, BranchCode, msgSequence, stmType, rrn, requestDateTime, encAlgorytm, MAC);
            return e == null?"NULL":e.getResponseXml();
        } catch (Exception var24) {
            log.error(var24);
            return var24.getMessage();
        }
    }

    public static synchronized String getPanStr(String msgSeq, String chnType, String chnId, String chnPass, String accNo, String accPass, String reqDateTime, String encAlgrthm, String mac) {
        try {
            CardAccountMessage e = getPan(msgSeq, chnType, chnId, chnPass, accNo, accPass, reqDateTime, encAlgrthm, mac);
            return e.getResponseXml();
        } catch (Exception var10) {
            log.error(var10);
            return var10.getMessage();
        }
    }
    //get card no from accno
    public static synchronized CardAccountMessage getPan(String msgSeq, String chnType, String chnId, String chnPass, String accNo, String accPass, String reqDateTime, String encAlgrthm, String mac) throws InvalidParameterException, ResponseParsingException, SenderException {
        try{
            ValidateBaseParameters(chnType, chnPass, chnId);
            String xmlRequest = message2XMLConvertor.geterateGetPanRequest(msgSeq, chnType, chnId, chnPass, accNo, accPass, reqDateTime, encAlgrthm, mac);
            Object response = callService(xmlRequest, msgSeq, chnId);
            CardAccountMessage responseMessage;
            if(response instanceof CardAccountMessage) {
                responseMessage = xml2MessageConvertor.getPanGetterResponse(((BaseMessage)response).getResponseXml());
                responseMessage.setResponseXml(((BaseMessage)response).getResponseXml());
            } else {
                responseMessage = xml2MessageConvertor.getPanGetterResponse(response.toString());
                responseMessage.setResponseXml(response.toString());
            }
            responseMessage.setRequestXml(xmlRequest);
            // log.info("Response = " + responseMessage.getResponseXml());
            return responseMessage;
        }catch (Exception e){
            CardAccountMessage responseMessage=new CardAccountMessage();


            responseMessage.setAction_desc(e.toString());
            return responseMessage;
        }

    }

    public static synchronized String getCardAccNoStr(String msgSeq, String chnType, String chnId, String chnPass, String pan, String reqDateTime, String encAlgrthm, String mac) {
        try {
            CardAccountMessage e = getCardAccNo(msgSeq, chnType, chnId, chnPass, pan, reqDateTime, encAlgrthm, mac);
            return e.getResponseXml();
        } catch (Exception var9) {
            log.error(var9);
            return var9.getMessage();
        }
    }
    //get acc no from card no
    public static synchronized CardAccountMessage getCardAccNo(String msgSeq, String chnType, String chnId, String chnPass, String pan, String reqDateTime, String encAlgrthm, String mac) throws InvalidParameterException, ResponseParsingException, SenderException {
        try{
            ValidateBaseParameters(chnType, chnPass, chnId);
            String xmlRequest = message2XMLConvertor.geterateGetCardAccNoRequest(msgSeq, chnType, chnId, chnPass, pan, reqDateTime, encAlgrthm, mac);
            Object response = callService(xmlRequest, msgSeq, chnId);
            CardAccountMessage responseMessage;
            if(response instanceof CardAccountMessage) {
                responseMessage = xml2MessageConvertor.getCardAccNoResponse(((BaseMessage)response).getResponseXml());
                responseMessage.setResponseXml(((BaseMessage)response).getResponseXml());
            } else {
                responseMessage = xml2MessageConvertor.getCardAccNoResponse(response.toString());
                responseMessage.setResponseXml(response.toString());
            }
            responseMessage.setRequestXml(xmlRequest);
            log.info("Response = " + responseMessage.getResponseXml());
            return responseMessage;
        }catch (Exception e){
            CardAccountMessage responseMessage=new CardAccountMessage();
            responseMessage.setAction_desc(e.toString());
            return responseMessage;
        }


    }

    public static synchronized RegistrationMessage getRegistration(String accountNumber, String accountPass, String serviceType, String requestDateTime, String channelType, String channelPass, String channelID, String msgSequence, String encAlgorytm, String MAC) throws SenderException, InvalidParameterException, ResponseParsingException {

        try{
            if(requestDateTime.length() < 14) {
                new InvalidParameterException("getRegistration: requestDateTime.length() < 14");
            }

            ValidateBaseParameters(channelType, channelPass, channelID);
            String xmlRequest = message2XMLConvertor.GenerateRegistrationRequest(accountNumber, accountPass, serviceType, requestDateTime, channelType, channelPass, channelID, msgSequence, encAlgorytm, MAC);
            Object response = callService(xmlRequest, msgSequence, channelID);
            RegistrationMessage responseMessage;
            if(response instanceof RegistrationMessage) {
                responseMessage = (RegistrationMessage)response;
            } else if(response instanceof BaseMessage) {
                responseMessage = xml2MessageConvertor.getResponseRegistration(((BaseMessage)response).getResponseXml());
                responseMessage.setResponseXml(((BaseMessage)response).getResponseXml());
            } else {
                responseMessage = xml2MessageConvertor.getResponseRegistration(response.toString());
                responseMessage.setResponseXml(response.toString());
            }
            responseMessage.setRequestXml(xmlRequest);
            log.info("Response =" + responseMessage.getResponseXml());
            return responseMessage;
        }catch (Exception e){
            RegistrationMessage responseMessage=new RegistrationMessage();
            responseMessage.setAction_desc(e.toString());
            return responseMessage;
        }


    }
    //register account
    public static synchronized String getRegistrationStr(String accountNumber, String accountPass, String serviceType, String requestDateTime, String channelType, String channelPass, String channelID, String msgSequence, String encAlgorytm, String MAC) {
        try {
            RegistrationMessage e = getRegistration(accountNumber, accountPass, serviceType, requestDateTime, channelType, channelPass, channelID, msgSequence, encAlgorytm, MAC);
            return e.getResponseXml();
        } catch (Exception var11) {
            log.error(var11);
            return var11.getMessage();
        }
    }

    public static synchronized FollowUpMessage FollowUp(String srcAccountNumber, String dstAccountNumber, String followUpCode, String accountPass, String origTransDateTime, String requestDateTime, String channelType, String channelPass, String channelID, String msgSequence, String encAlgorytm, String MAC) throws SenderException, InvalidParameterException, ResponseParsingException {
        try{
            ValidateBaseParameters(channelType, channelPass, channelID);
            String xmlRequest = message2XMLConvertor.GenerateFollowUpRequest(srcAccountNumber, dstAccountNumber, followUpCode, accountPass, origTransDateTime, requestDateTime, channelType, channelPass, channelID, msgSequence, encAlgorytm, MAC);
            Object response = callService(xmlRequest, msgSequence, channelID);
            FollowUpMessage responseMessage;
            if(response instanceof FollowUpMessage) {
                responseMessage = (FollowUpMessage)response;
            } else if(response instanceof BaseMessage) {
                responseMessage = xml2MessageConvertor.getResponseFollowUp(((BaseMessage)response).getResponseXml());
                responseMessage.setResponseXml(((BaseMessage)response).getResponseXml());
            } else {
                responseMessage = xml2MessageConvertor.getResponseFollowUp(response.toString());
                responseMessage.setResponseXml(response.toString());
            }
            responseMessage.setRequestXml(xmlRequest);
            log.info("Response =" + responseMessage.getResponseXml());
            return responseMessage;
        }catch (Exception e){
            FollowUpMessage responseMessage=new FollowUpMessage();
            responseMessage.setAction_desc(e.toString());
            return responseMessage;
        }


    }
    //Followupp Transaction
    public static synchronized String FollowUpStr(String srcAccountNumber, String dstAccountNumber, String followUpCode, String accountPass, String origTransDateTime, String requestDateTime, String channelType, String channelPass, String channelID, String msgSequence, String encAlgorytm, String MAC) {
        try {
            FollowUpMessage e = FollowUp(srcAccountNumber, dstAccountNumber, followUpCode, accountPass, origTransDateTime, requestDateTime, channelType, channelPass, channelID, msgSequence, encAlgorytm, MAC);
            return e.getResponseXml();
        } catch (Exception var13) {
            log.error(var13);
            return var13.getMessage();
        }
    }

    public static synchronized String RTGSFollowUpStr(String msgSeq, String chnType, String chnId, String chnPass, String srcBnkId, String srcAccNo, String dstBnkId, String dstAccNo, String followUpCode, String reqDateTime, String encAlgorithm, String MAC) {
        try {
            RTGSFollowUpMessage e = RTGSFollowUp(msgSeq, chnType, chnId, chnPass, srcBnkId, srcAccNo, dstBnkId, dstAccNo, followUpCode, reqDateTime, encAlgorithm, MAC);
            return e.getResponseXml();
        } catch (Exception var13) {
            log.error(var13);
            return var13.getMessage();
        }
    }
    //Followupp Transaction Paya || RTG
    public static synchronized RTGSFollowUpMessage RTGSFollowUp(String msgSeq, String chnType, String chnId, String chnPass, String srcBnkId, String srcAccNo, String dstBnkId, String dstAccNo, String followUpCode, String reqDateTime, String encAlgorithm, String MAC) throws SenderException, InvalidParameterException, ResponseParsingException {
        try{
            ValidateBaseParameters(chnType, chnPass, chnId);
            String xmlRequest = message2XMLConvertor.generateRTGSFollowUpRequest(msgSeq, chnType, chnId, chnPass, srcBnkId, srcAccNo, dstBnkId, dstAccNo, followUpCode, reqDateTime, encAlgorithm, MAC);
            Object response = callService(xmlRequest, msgSeq, chnId);
            RTGSFollowUpMessage responseMessage;
            if(response instanceof RTGSFollowUpMessage) {
                responseMessage = (RTGSFollowUpMessage)response;
            } else if(response instanceof BaseMessage) {
                responseMessage = xml2MessageConvertor.getRTGSFollowUpResponse(((BaseMessage)response).getResponseXml());
                responseMessage.setResponseXml(((BaseMessage)response).getResponseXml());
            } else {
                responseMessage = xml2MessageConvertor.getRTGSFollowUpResponse(response.toString());
                responseMessage.setResponseXml(response.toString());
            }
            responseMessage.setRequestXml(xmlRequest);
            log.info("Response =" + responseMessage.getResponseXml());
            return responseMessage;
        }catch (Exception e){
            RTGSFollowUpMessage responseMessage=new RTGSFollowUpMessage();
            responseMessage.setAction_desc(e.toString());
            return responseMessage;
        }

    }

    public static synchronized BillPaymentMessage BillPayment(String msgSequence, String srcAccount, String accountPass, String requestDateTime, String channelType, String channelPass, String channelID, String amount, String billID, String paymentID, String encAlgorytm, String MAC) throws SenderException, InvalidParameterException, ResponseParsingException {
        try{
            ValidateBaseParameters(channelType, channelPass, channelID);
            String xmlRequest = message2XMLConvertor.GenerateBillPaymentRequest(msgSequence, srcAccount, accountPass, requestDateTime, channelType, channelPass, channelID, amount, billID, paymentID, encAlgorytm, MAC);
            Object response = callService(xmlRequest, msgSequence, channelID);
            BillPaymentMessage responseMessage;
            if(response instanceof BillPaymentMessage) {
                responseMessage = (BillPaymentMessage)response;
            } else if(response instanceof BaseMessage) {
                responseMessage = xml2MessageConvertor.getResponseBillPayment(((BaseMessage)response).getResponseXml());
                responseMessage.setResponseXml(((BaseMessage)response).getResponseXml());
            } else {
                responseMessage = xml2MessageConvertor.getResponseBillPayment(response.toString());
                responseMessage.setResponseXml(response.toString());
            }
            responseMessage.setRequestXml(xmlRequest);
            log.info("Response =" + responseMessage.getResponseXml());
            return responseMessage;
        }catch (Exception e){
            BillPaymentMessage responseMessage=new BillPaymentMessage();
            responseMessage.setAction_desc(e.toString());
            return responseMessage;
        }


    }
    //Bill PaymentOBJ
    public static synchronized String BillPaymentStr(String msgSequence, String srcAccount, String accountPass, String requestDateTime, String channelType, String channelPass, String channelID, String amount, String billID, String paymentID, String encAlgorytm, String MAC) {
        try {
            BillPaymentMessage e = BillPayment(msgSequence, srcAccount, accountPass, requestDateTime, channelType, channelPass, channelID, amount, billID, paymentID, encAlgorytm, MAC);
            return e.getResponseXml();
        } catch (Exception var13) {
            log.error(var13);
            return var13.getMessage();
        }
    }

    public static synchronized BillPaymentValidation BillPaymentValidation(String msgSequence, String srcAccount, String accountPass, String requestDateTime, String channelType, String channelPass, String channelID, String amount, String billID, String paymentID, String encAlgorytm, String MAC) throws SenderException, InvalidParameterException, ResponseParsingException {

        try{
            ValidateBaseParameters(channelType, channelPass, channelID);
            String xmlRequest = message2XMLConvertor.GenerateBillPaymentValidationRequest(msgSequence, srcAccount, accountPass, requestDateTime, channelType, channelPass, channelID, amount, billID, paymentID, encAlgorytm, MAC);
            Object response = callService(xmlRequest, msgSequence, channelID);
            BillPaymentValidation responseMessage;
            if(response instanceof BillPaymentValidation) {
                responseMessage = (BillPaymentValidation)response;
            } else if(response instanceof BaseMessage) {
                responseMessage = xml2MessageConvertor.getResponseBillPaymentValidation(((BaseMessage)response).getResponseXml());
                responseMessage.setResponseXml(((BaseMessage)response).getResponseXml());
            } else {
                responseMessage = xml2MessageConvertor.getResponseBillPaymentValidation(response.toString());
                responseMessage.setResponseXml(response.toString());
            }
            responseMessage.setRequestXml(xmlRequest);
            log.info("Response =" + responseMessage.getResponseXml());
            return responseMessage;
        }catch (Exception e){
            BillPaymentValidation responseMessage=new BillPaymentValidation();
            responseMessage.setAction_desc(e.toString());
            return responseMessage;
        }


    }
    //bill PaymentOBJ Validation
    public static synchronized String BillPaymentValidationStr(String msgSequence, String srcAccount, String accountPass, String requestDateTime, String channelType, String channelPass, String channelID, String amount, String billID, String paymentID, String encAlgorytm, String MAC) {
        try {
            BillPaymentValidation e = BillPaymentValidation(msgSequence, srcAccount, accountPass, requestDateTime, channelType, channelPass, channelID, amount, billID, paymentID, encAlgorytm, MAC);
            return e.getResponseXml();
        } catch (Exception var13) {
            log.error(var13);
            return var13.getMessage();
        }
    }

    public static synchronized ChequeStatusMessage getChequeStatus(String accountNumber, String accountPass, String channelType, String channelPass, String channelID, String chequeNo, String chequeAmnt, String chequeOpDate, String requestDateTime, String msgSeq, String encAlgorytm, String MAC) throws SenderException, InvalidParameterException, ResponseParsingException {
        try{
            ValidateBaseParameters(channelType, channelPass, channelID);
            String xmlRequest = message2XMLConvertor.GenerateChequeStatusRequest(accountNumber, accountPass, channelType, channelPass, channelID, chequeNo, chequeAmnt, chequeOpDate, requestDateTime, msgSeq, encAlgorytm, MAC);
            Object response = callService(xmlRequest, msgSeq, channelID);
            ChequeStatusMessage responseMessage;
            if(response instanceof ChequeStatusMessage) {
                responseMessage = (ChequeStatusMessage)response;
            } else if(response instanceof BaseMessage) {
                responseMessage = xml2MessageConvertor.getResponseChequeStatus(((BaseMessage) response).getResponseXml());
                responseMessage.setResponseXml(((BaseMessage)response).getResponseXml());
            } else {
                responseMessage = xml2MessageConvertor.getResponseChequeStatus(response.toString());
                responseMessage.setResponseXml(response.toString());
            }
            responseMessage.setRequestXml(xmlRequest);
            log.info("Response =" + responseMessage.getResponseXml());
            return responseMessage;
        }catch (Exception e){
            ChequeStatusMessage responseMessage=new ChequeStatusMessage();
            responseMessage.setAction_desc(e.toString());
            return responseMessage;
        }



    }
    //get Cheque Status
    public static synchronized String getChequeStatusStr(String accountNumber, String accountPass, String channelType, String channelPass, String channelID, String chequeNo, String chequeAmnt, String chequeOpDate, String requestDateTime, String msgSequence, String encAlgorytm, String MAC) {
        try {
            ChequeStatusMessage e = getChequeStatus(accountNumber, accountPass, channelType, channelPass, channelID, chequeNo, chequeAmnt, chequeOpDate, requestDateTime, msgSequence, encAlgorytm, MAC);
            return e.getResponseXml();
        } catch (Exception var13) {
            log.error(var13);
            return var13.getMessage();
        }
    }

    public static synchronized String achFollowupByTrackIdStr(String msgSequence, String chnType, String chnPass, String ChnId, String trackId, String reqDateTime, String encAlgrthm, String mac) {
        try {
            ACHFundTransferMessage e = achFollowupByTrackId(msgSequence, chnType, chnPass, ChnId, trackId, reqDateTime, encAlgrthm, mac);
            return e.getResponseXml();
        } catch (Exception var9) {
            log.error(var9);
            return var9.getMessage();
        }
    }
    //peygiri Tarakinesh paya
    public static synchronized ACHFundTransferMessage achFollowupByTrackId(String msgSequence, String chnType, String chnPass, String ChnId, String trackId, String reqDateTime, String encAlgrthm, String mac) throws InvalidParameterException, ResponseParsingException, SenderException {
        ValidateBaseParameters(chnType, chnPass, ChnId);
        String xmlRequest = message2XMLConvertor.generateACHFlwupByTrackIdRequest(msgSequence, chnType, chnPass, ChnId, trackId, reqDateTime, encAlgrthm, mac);
        Object response = callService(xmlRequest, msgSequence, ChnId);
        ACHFundTransferMessage responseMessage;
        if(response instanceof ACHFundTransferMessage) {
            responseMessage = (ACHFundTransferMessage)response;
        } else if(response instanceof BaseMessage) {
            responseMessage = xml2MessageConvertor.getACHFlwupByTrackIdResponse(((BaseMessage)response).getResponseXml());
            responseMessage.setResponseXml(response.toString());
        } else {
            responseMessage = xml2MessageConvertor.getACHFlwupByTrackIdResponse(response.toString());
            responseMessage.setResponseXml(response.toString());
        }
        responseMessage.setRequestXml(xmlRequest);
        log.info("Response = " + responseMessage.getResponseXml());
        return responseMessage;
    }

    public static synchronized FundTransferMessage FundTransfer(String srcAccountNumber, String dstAccountNumber, String transAmount, String currency, String transDesc, String accountPass, String channelType, String channelPass, String channelID, String opCode, String fPayCode, String sPayCode, String shpInf, String requestDateTime, String encAlgorytm, String MAC, String msgSeq) throws SenderException, InvalidParameterException, ResponseParsingException {
        ValidateBaseParameters(channelType, channelPass, channelID);
        String xmlRequest = message2XMLConvertor.GenerateFundTransferRequest(srcAccountNumber, dstAccountNumber, transAmount, currency, transDesc, accountPass, channelType, channelPass, channelID, opCode, fPayCode, sPayCode, shpInf, requestDateTime, encAlgorytm, MAC, msgSeq);
        Object response = callService(xmlRequest, msgSeq, channelID);
        FundTransferMessage responseMessage;
        if(response instanceof FundTransferMessage) {
            responseMessage = (FundTransferMessage)response;
        } else if(response instanceof BaseMessage) {
            responseMessage = xml2MessageConvertor.getResponseFundTransfer(((BaseMessage)response).getResponseXml());
            responseMessage.setResponseXml(((BaseMessage)response).getResponseXml());
        } else {
            responseMessage = xml2MessageConvertor.getResponseFundTransfer(response.toString());
            responseMessage.setResponseXml(response.toString());
        }
        responseMessage.setRequestXml(xmlRequest);
        log.info("Response =" + responseMessage.getResponseXml());
        return responseMessage;
    }
    //found transfer
    public static synchronized String FundTransferStr(String srcAccountNumber, String dstAccountNumber, String transAmount, String currency, String transDesc, String accountPass, String channelType, String channelPass, String channelID, String opCode, String fPayCode, String sPayCode, String shpInf, String requestDateTime, String encAlgorytm, String MAC, String msgSeq) {
        try {
            FundTransferMessage e = FundTransfer(srcAccountNumber, dstAccountNumber, transAmount, currency, transDesc, accountPass, channelType, channelPass, channelID, opCode, fPayCode, sPayCode, shpInf, requestDateTime, encAlgorytm, MAC, msgSeq);
            return e.getResponseXml();
        } catch (Exception var18) {
            log.error(var18);
            return var18.getMessage();
        }
    }


    public static synchronized String blockAccountStr(String msgSeq, String chnType, String chnId, String chnPass, String accNo, String accPass, String blckMode, String reqDateTime, String encAlgthm, String mac) {
        try {
            AccountMessage e = blockAccount(msgSeq, chnType, chnId, chnPass, accNo, accPass, blckMode, reqDateTime, encAlgthm, mac);
            return e.getResponseXml();
        } catch (Exception var11) {
            log.error(var11);
            return var11.getMessage();
        }
    }
    //Block Account
    public  static synchronized AccountMessage blockAccount(String msgSeq, String chnType, String chnId, String chnPass, String accNo, String accPass, String blckMode, String reqDateTime, String encAlgthm, String mac) throws InvalidParameterException, ResponseParsingException, SenderException {
        ValidateBaseParameters(chnType, chnPass, chnId);
        String xmlRequest = message2XMLConvertor.generateBlockAccountRequest(msgSeq, chnType, chnId, chnPass, accNo, accPass, blckMode, reqDateTime, encAlgthm, mac);
        Object response = callService(xmlRequest, msgSeq, chnId);
        AccountMessage responseMessage;
        if(response instanceof AccountMessage) {
            responseMessage = xml2MessageConvertor.getBlockAccountResponse(((BaseMessage)response).getResponseXml());
            responseMessage.setResponseXml(((BaseMessage)response).getResponseXml());
        } else {
            responseMessage = xml2MessageConvertor.getBlockAccountResponse(response.toString());
            responseMessage.setResponseXml(response.toString());
        }
        responseMessage.setRequestXml(xmlRequest);
        log.info("Response = " + responseMessage.getResponseXml());
        return responseMessage;
    }


    public static synchronized String ACHFollowupStr(String msgSequence, String chnType, String chnPass, String chnId, String srcIBaAN, String accPass, String dstIBAN, String followupCode, String reqDateTime, String encAlgthm, String mac) {
        try {
            ACHFundTransferMessage e = ACHFollowup(msgSequence, chnType, chnPass, chnId, srcIBaAN, accPass, dstIBAN, followupCode, reqDateTime, encAlgthm, mac);
            return e.getResponseXml();
        } catch (Exception var12) {
            log.error(var12);
            return var12.getMessage();
        }
    }
    //peygiri tatakonesh paya
    public static synchronized ACHFundTransferMessage ACHFollowup(String msgSequence, String chnType, String chnPass, String chnId, String srcIBaAN, String accPass, String dstIBAN, String followupCode, String reqDateTime, String encAlgrthm, String mac) throws InvalidParameterException, ResponseParsingException, SenderException {
        ValidateBaseParameters(chnType, chnPass, chnId);
        String xmlRequset = message2XMLConvertor.generateACHFollowupRequest(msgSequence, chnType, chnPass, chnId, srcIBaAN, accPass, dstIBAN, followupCode, reqDateTime, encAlgrthm, mac);
        Object response = callService(xmlRequset, msgSequence, chnId);
        ACHFundTransferMessage responseMessage;
        if(response instanceof ACHFundTransferMessage) {
            responseMessage = (ACHFundTransferMessage)response;
        } else if(response instanceof BaseMessage) {
            responseMessage = xml2MessageConvertor.getACHFollowupResponse(((BaseMessage)response).getResponseXml());
            responseMessage.setResponseXml(response.toString());
        } else {
            responseMessage = xml2MessageConvertor.getACHFollowupResponse(response.toString());
            responseMessage.setResponseXml(response.toString());
        }

        log.info("Response = " + responseMessage.getResponseXml());
        return responseMessage;
    }



    private static synchronized boolean isServerReachable() {
        try {
            return InetAddress.getByName(senderConfig.getHostName()).isReachable(30000);
        } catch (UnknownHostException var1) {
            return false;
        } catch (IOException var2) {
            return false;
        }
    }

    public static synchronized void reconnect() throws JMSException {
        sessionPool.reconnect();
    }

    public static synchronized int getChannelState() {
        return !isServerReachable()?STATE_IO_ERROR:sessionPool.getChannelState();
    }


}
