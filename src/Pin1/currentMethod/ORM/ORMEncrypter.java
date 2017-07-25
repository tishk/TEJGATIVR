package Pin1.currentMethod.ORM;


import Pin1.currentMethod.util.CodecUtil;
import Pin1.currentMethod.util.PropertiesUtil;
import ServiceObjects.Other.LoggerToDB;
import com.neda.DateUtil;
import com.sabapardazesh.orm.messageing.MessageConvertor;
import com.sabapardazesh.orm.messageing.core.bootstrap.sax.MessagingInitializer;
import com.sabapardazesh.orm.messageing.domain.*;
import com.sabapardazesh.orm.messageing.impl.MessageConvertorImpl;
import org.apache.commons.lang.StringUtils;

import java.sql.*;


public class ORMEncrypter extends Thread {

    byte msgBytes[];
    static LoggerToDB loggerToDB=null;
    public ORMEncrypter(byte[] temp)throws SQLException, ClassNotFoundException {
        new MessagingInitializer();
        this.setMsgBytes(temp);
        //propertyInitialization();
        run();
    }
    public void run() {

        String St = new String(msgBytes);
        String decMsg = null;
        try {
            decMsg = CodecUtil.decrypt(msgBytes, PropertiesUtil.getKeyOrm());
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            MessageProcess(decMsg);
        } catch (SQLException e) {

            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    public void setMsgBytes(byte msgBytes[]) {
        this.msgBytes = msgBytes;
    }
    private static void MessageProcess(String msgStr)throws Exception {
        MessageConvertor MsC = new MessageConvertorImpl();
        Message rcMsg = null;
        try {
            rcMsg = MsC.getMessageInstance(msgStr);
        } catch (Exception e) {}
        Integer msgId = Integer.valueOf(rcMsg.getMessageId());

        switch (msgId.intValue()) {
            default:
                break;

            case 200: { // sabte nami haghighi
                MessageProcess200(rcMsg,MsC);
                break;
            }

            case 222: { // sabte name hoghoghi
                MessageProcess222(rcMsg, MsC);
                break;
            }

            case 223: { // sabte name moshtarak
                MessageProcess223(rcMsg, MsC);
                break;
            }

            case 231: { // javabe migrate
                MessageProcess231(rcMsg, MsC);
                break;
            }

            case 500: { // update
                MessageProcess500(rcMsg, MsC);
                break;
            }

            case 600: { // ghere faal sazi
                MessageProcess600(rcMsg,MsC);
                break;
            }
        }
    }
    private static void MessageProcess200(Message rcMsg,MessageConvertor MsC) throws Exception {
        Msg200 msg200 = (Msg200) rcMsg;
        String loginId = msg200.getAccountNumber();
        String dateAndTime = ((Msg200) rcMsg).getDateAndTime();
        loggerToDB=new LoggerToDB(msg200);
        int res = loggerToDB.getOrmResult();
        Msg210 Ms210 = new Msg210();
        Ms210.setOriginalMsgSeq(msg200.getMsgSeq());
        Ms210.setDateAndTime(DateUtil.msgDateTime());
        Ms210.setResultCode(Integer.valueOf(res));
        try {
            String st = MsC.generateStringMessage(Ms210);
            byte resBytes[] = CodecUtil.encrypt(st, PropertiesUtil.getOrmDeviceId(), PropertiesUtil.getKeyOrm());
            ORM.sendBytes(resBytes);
        } catch (Exception e) {
            loggerToDB.orm_saveLogDb(msg200.getAccountNumber(), "200", msg200.getDateAndTime(), 999, msg200.getMsgSeq().toString());
            e.printStackTrace();
        }
    }
    private static void MessageProcess222(Message rcMsg,MessageConvertor MsC) throws Exception {
        Msg222 msg222 = (Msg222) rcMsg;
        String loginId = msg222.getAccountNumber();
        String dateAndTime = ((Msg200) rcMsg).getDateAndTime();
        loggerToDB=new LoggerToDB(msg222);
        int res = loggerToDB.getOrmResult();


        Msg232 ms232 = new Msg232();
        ms232.setOriginalMsgSeq(ms232.getMsgSeq());
        Timestamp gTimestamp = new Timestamp(System.currentTimeMillis());
        ms232.setDateAndTime(DateUtil.msgDateTime());
        ms232.setResultCode(Integer.valueOf(res));
        try {
            String st = MsC.generateStringMessage(ms232);
            byte resBytes[] = CodecUtil.encrypt(st, PropertiesUtil.getOrmDeviceId(), PropertiesUtil.getKeyOrm());
            String resStr = (new StringBuilder()).append(new String(resBytes)).toString();
            resBytes = resStr.getBytes();
            ORM.sendBytes(resBytes);

        } catch (Exception e) {
            loggerToDB.orm_saveLogDb(msg222.getAccountNumber(), "222", msg222.getDateAndTime(), 999, msg222.getMsgSeq().toString());
            e.printStackTrace();
        }

    }
    private static void MessageProcess223(Message rcMsg,MessageConvertor MsC) throws Exception {
        Msg223 msg223 = (Msg223) rcMsg;
        String loginId = msg223.getAccountNumber();
        String dateAndTime = ((Msg200) rcMsg).getDateAndTime();
        loggerToDB=new LoggerToDB(msg223);
        int res = loggerToDB.getOrmResult();

        Msg233 ms233 = new Msg233();
        ms233.setOriginalMsgSeq(ms233.getMsgSeq());
        Timestamp gTimestamp = new Timestamp(System.currentTimeMillis());
        ms233.setDateAndTime(DateUtil.msgDateTime());
        ms233.setResultCode(Integer.valueOf(res));
        try {
            String st = MsC.generateStringMessage(ms233);
            byte resBytes[] = CodecUtil.encrypt(st, PropertiesUtil.getOrmDeviceId(), PropertiesUtil.getKeyOrm());
            String resStr = (new StringBuilder()).append(new String(resBytes)).toString();
            resBytes = resStr.getBytes();
            ORM.sendBytes(resBytes);

        } catch (Exception e) {
            loggerToDB.orm_saveLogDb(msg223.getAccountNumber(), "223", msg223.getDateAndTime(), 999, msg223.getMsgSeq().toString());
             e.printStackTrace();
        }

    }
    private static void MessageProcess231(Message rcMsg,MessageConvertor MsC) throws Exception {
        Msg231 msg231 = (Msg231) rcMsg;
        String OriginalMsgSeq = Long.toString(msg231.getOriginalMsgSeq());

        String dateAndTime = ((Msg231) rcMsg).getDateAndTime();

        int res = msg231.getResultCode();

        String AccNo = StringUtils.leftPad(ORM.migratedRecord.get(Long.valueOf(OriginalMsgSeq)), 10, "0");
        loggerToDB=new LoggerToDB(msg231);
        System.out.println("Result code for MsgSeq = " + OriginalMsgSeq + " is " + res);
    }
    private static void MessageProcess500(Message rcMsg,MessageConvertor MsC) throws Exception {
        Msg500 msg500 = (Msg500) rcMsg;
        String loginId = msg500.getLoginId();
        String dateAndTime = ((Msg500) rcMsg).getDateAndTime();
        loggerToDB=new LoggerToDB(msg500);
        int res = loggerToDB.getOrmResult();

        Msg510 Ms510 = new Msg510();
        Ms510.setOriginalMsgSeq(msg500.getMsgSeq());
        Ms510.setDateAndTime(DateUtil.msgDateTime());
        Ms510.setResultCode(Integer.valueOf(res));
        try {
            String st = MsC.generateStringMessage(Ms510);
            byte resBytes[] = CodecUtil.encrypt(st, PropertiesUtil.getOrmDeviceId(), PropertiesUtil.getKeyOrm());
            String resStr = (new StringBuilder()).append(new String(resBytes)).toString();
            resBytes = resStr.getBytes();
            ORM.sendBytes(resBytes);

        } catch (Exception e) {

            e.printStackTrace();
            loggerToDB.orm_saveLogDb(msg500.getLoginId(), "500", msg500.getDateAndTime(), 999, msg500.getMsgSeq().toString());
        }

    }
    private static void MessageProcess600(Message rcMsg,MessageConvertor MsC) throws Exception {
        Msg600 msg600 = (Msg600) rcMsg;
        String loginId = msg600.getLoginId();
        String dateAndTime = ((Msg600) rcMsg).getDateAndTime();
        loggerToDB=new LoggerToDB(msg600);
        int res = loggerToDB.getOrmResult();

        Msg610 Ms610 = new Msg610();
        Ms610.setOriginalMsgSeq(msg600.getMsgSeq());
        Ms610.setDateAndTime(DateUtil.msgDateTime());
        Ms610.setResultCode(Integer.valueOf(res));
        try {
            String st = MsC.generateStringMessage(Ms610);
            byte resBytes[] = CodecUtil.encrypt(st, PropertiesUtil.getOrmDeviceId(), PropertiesUtil.getKeyOrm());
            String resStr = (new StringBuilder()).append(new String(resBytes)).toString();

            resBytes = resStr.getBytes();
            ORM.sendBytes(resBytes);

        } catch (Exception e) {

            loggerToDB.orm_saveLogDb(msg600.getLoginId(), "600", msg600.getDateAndTime(), 999, msg600.getMsgSeq().toString());
            e.printStackTrace();
        }

    }



}
