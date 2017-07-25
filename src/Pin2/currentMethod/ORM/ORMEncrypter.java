package Pin2.currentMethod.ORM;


import Pin2.currentMethod.util.CodecUtil;
import Pin2.currentMethod.util.PropertiesUtil;
import com.neda.DateUtil;
import com.sabapardazesh.orm.messageing.MessageConvertor;
import com.sabapardazesh.orm.messageing.core.bootstrap.sax.MessagingInitializer;
import com.sabapardazesh.orm.messageing.domain.*;
import com.sabapardazesh.orm.messageing.impl.MessageConvertorImpl;
import org.apache.commons.lang.StringUtils;

import java.sql.*;


public class ORMEncrypter extends Thread {


    byte msgBytes[];

    static Connection connection;



    public ORMEncrypter()throws SQLException, ClassNotFoundException {
        new MessagingInitializer();

        propertyInitialization();
    }

    private void propertyInitialization() {
        try {
            Class.forName(PropertiesUtil.getDbdriver());
        } catch (ClassNotFoundException e) {

            System.out.println((new StringBuilder()).append("Initialization failed :: ").append(e).toString());

            e.printStackTrace();
        }
        try {
            connection = DriverManager.getConnection(PropertiesUtil.getDburl(), PropertiesUtil.getDbusername(), PropertiesUtil.getDbUserPassword());
        } catch (SQLException e) {

            System.out.println((new StringBuilder()).append("Initialization failed :: ").append(e).toString());

            e.printStackTrace();
        }
    }

    public void run() {

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

            case 200: {
                MessageProcess200(rcMsg,MsC);
                break;
            }

            case 222: {
                MessageProcess222(rcMsg, MsC);
                break;
            }

            case 223: {
                MessageProcess223(rcMsg, MsC);
                break;
            }

            case 231: {
                MessageProcess231(rcMsg, MsC);
                break;
            }

            case 500: {
                MessageProcess500(rcMsg, MsC);
                break;
            }

            case 600: {
                MessageProcess600(rcMsg,MsC);
                break;
            }
        }
    }

    private static void MessageProcess200(Message rcMsg,MessageConvertor MsC) throws Exception {
        Msg200 msg200 = (Msg200) rcMsg;
        String loginId = msg200.getAccountNumber();
        String dateAndTime = ((Msg200) rcMsg).getDateAndTime();
        int res = registerCustomer(msg200);

        Msg210 Ms210 = new Msg210();
        Ms210.setOriginalMsgSeq(msg200.getMsgSeq());
        Ms210.setDateAndTime(DateUtil.msgDateTime());
        Ms210.setResultCode(Integer.valueOf(res));
        try {
            String st = MsC.generateStringMessage(Ms210);
            byte resBytes[] = CodecUtil.encrypt(st, PropertiesUtil.getOrmDeviceId(), PropertiesUtil.getKeyOrm());


            String resStr = (new StringBuilder()).append(new String(resBytes)).toString();

            resBytes = resStr.getBytes();
            ORM.sendBytes(resBytes);

        } catch (Exception e) {
            saveLogDb(msg200.getAccountNumber(), "200", msg200.getDateAndTime(), 999, msg200.getMsgSeq().toString());

            e.printStackTrace();
        }
    }
    private static int  registerCustomer(Msg200 msg200) throws Exception {
        int Result;
        String Accno;
        String Sql;
        Result = 10;
        String service = msg200.getServiceTypes();
        String isFreeTrans = service.substring(0, 1);
        Accno = StringUtils.leftPad(msg200.getAccountNumber(), 10, "0");
        service = (new StringBuilder()).append(service.substring(2, 3)).append(service.substring(1, 2)).append("11000000").toString();
        Statement stmnt1 = null;
        try {
            int step = 0;
            String[] da = {"0000000000000000", "0000000000000000", "0000000000000000", "0000000000000000", "0000000000000000"};
            try {
                da[step] = msg200.getDestAccounts()[step];
                step = 1;
                da[step] = msg200.getDestAccounts()[step];
                step = 2;
                da[step] = msg200.getDestAccounts()[step];
                step = 3;
                da[step] = msg200.getDestAccounts()[step];
                step = 4;
                da[step] = msg200.getDestAccounts()[step];
            } catch (Exception e) {
                step = step + 1;
                for (int i = step; step < 5; step++)
                    da[i] = "0000000000000000";
                    msg200.setDestAccounts(da);

            }
            int res = 0;
            if (ConnectedToOracleDB()){
                Sql = (new StringBuilder())
                        .append(" Delete from PrivilegeRegistration " +
                        "where fldmainAccountno='")
                        .append(Accno).append("'")
                        .append(" INSERT INTO [PrivilegeRegistration]" +
                        "([fldRegisterationDate], [fldCenterCode], ")
                        .append(" [fldBranchCode]," +
                              " [fldMainAccountNo], " +
                              " [fldNationalCode]," +
                              " [fldPersianName]," +
                              " [fldServiceType], ")
                        .append(" [fldFirstAccountNo]," +
                              " [fldSecondAccountNo]," +
                              " [fldThirdAccountNo], " +
                              " [fldFourthAccountNo], " +
                              " [fldFifthAccountNo], ")
                        .append(" [fldAccountGroup], " +
                              " [fldPassword]," +
                              " [fldPrintFlag], " +
                              " [IsFreeTransfer]," +
                              " [LastChangePin]," +
                              " [RegisteredInCM], ")
                        .append(" [Priority], " +
                              " [IsActive]," +
                              " [OnlineState]," +
                              " [OrmMsgID]," +
                              " [MyPriority]," +
                              " [AvaCasActive]) ")
                        .append(" VALUES('")
                        .append(msg200.getDateAndTime().substring(0, 8))
                        .append("',0,0, '")
                        .append(Accno)
                        .append("' , '")
                        .append(msg200.getCustomerNationalCode())
                        .append("', '")
                        .append(msg200.getFirstNameLatin())
                        .append(" ")
                        .append(msg200.getLastNameLatin())
                        .append("','")
                        .append(service)
                        .append("', '")
                        .append(msg200.getDestAccounts()[0].substring(6, 16))
                        .append("', '")
                        .append(msg200.getDestAccounts()[1].substring(6, 16))
                        .append("', '")
                        .append(msg200.getDestAccounts()[2].substring(6, 16))
                        .append("', '")
                        .append(msg200.getDestAccounts()[3].substring(6, 16))
                        .append("', '")
                        .append(msg200.getDestAccounts()[4].substring(6, 16))
                        .append("', '")
                        .append(msg200.getAccountGroup())
                        .append("', '00000000',")
                        .append("1,'")
                        .append(isFreeTrans)
                        .append("','")
                        .append(msg200.getDateAndTime().substring(0, 8))
                        .append("',0,5,1,1,'")
                        .append(msg200.getMsgSeq())
                        .append("','00000','1' )").toString();

                stmnt1 = null;
                try{
                stmnt1 = connection.createStatement();
                res = stmnt1.executeUpdate(Sql);
                } catch (SQLException e) {

                if (stmnt1 != null) {
                    stmnt1.close();
                }
                System.out.println("\nRegister Customer Fail.");

            }
            }
            if (res > 0)  Result = 0;
            if (res < 0)  Result = 10;
            if (res == 0) Result = 82;
        } catch (Exception e) {

            Result = 10;
        }

        saveLogDb(Accno, "200", msg200.getDateAndTime(), Result, msg200.getMsgSeq().toString());
        return Result;

    }

    private static void MessageProcess222(Message rcMsg,MessageConvertor MsC) throws Exception {
        Msg222 msg222 = (Msg222) rcMsg;
        String loginId = msg222.getAccountNumber();
        String dateAndTime = ((Msg200) rcMsg).getDateAndTime();
        int res = registerLegalCustomer(msg222);

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
            saveLogDb(msg222.getAccountNumber(), "222", msg222.getDateAndTime(), 999, msg222.getMsgSeq().toString());
            e.printStackTrace();
        }

    }
    private static int  registerLegalCustomer(Msg222 msg222) throws Exception {
        int Result;
        String Accno;
        String Sql;
        Result = 10;
        Accno = StringUtils.leftPad(msg222.getAccountNumber(), 10, "0");

        Statement stmnt1 = null;

        Sql = new StringBuilder().append(" Delete from "+PropertiesUtil.getTbName()+" where AccNo='").
                append(Accno).append("'").
                append(" INSERT INTO "+PropertiesUtil.getTbName()+" (OrmMsgID,AccNo,HesabGroup,BranchNo,Name,RegDate,AvaCasActive,SendToAvaCas)").
                append(" VALUES('").append(msg222.getMsgSeq()).append("', '").
                append(Accno).append("' , '").
                append(msg222.getAccountGroup()).append("', '").
                append(msg222.getBranchCode()).append("', '").
                append(msg222.getCompanyName()).append("', '").
                append(msg222.getDateAndTime().substring(0, 8)).append("', '").
                append("1,1)").toString();

        int res = 0;
        stmnt1 = null;
        try {
            if (ConnectedToOracleDB()){
                stmnt1 = connection.createStatement();
                res = stmnt1.executeUpdate(Sql);

            }

            if (stmnt1 != null) {
                stmnt1.close();
            }

            if (res > 0) Result = 0;
            if (res < 0) Result = 10;
            if (res == 0)Result = 82;

            System.out.println("\nRegister Legal Customer Successfull.");

        } catch (SQLException e) {
            if (stmnt1 != null) {
                stmnt1.close();
            }

            System.out.println("\nRegister Legal Customer Fail.");

        } catch (Exception e) {

            if (stmnt1 != null) {
                stmnt1.close();
            }

            System.out.println("\nRegister Legal Customer Fail , Message False.");

            Result = 10;
        }



        saveLogDb(Accno, "200", msg222.getDateAndTime(), Result, msg222.getMsgSeq().toString());
        return Result;
    }

    private static void MessageProcess223(Message rcMsg,MessageConvertor MsC) throws Exception {
        Msg223 msg223 = (Msg223) rcMsg;
        String loginId = msg223.getAccountNumber();
        String dateAndTime = ((Msg200) rcMsg).getDateAndTime();
        int res = registerJointAccount(msg223);

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
            saveLogDb(msg223.getAccountNumber(), "223", msg223.getDateAndTime(), 999, msg223.getMsgSeq().toString());
             e.printStackTrace();
        }

    }
    private static int  registerJointAccount(Msg223 msg223) throws Exception {
        int Result;
        String Accno;
        String Sql;
        Result = 10;
        Accno = StringUtils.leftPad(msg223.getAccountNumber(), 10, "0");

        Statement stmnt1 = null;

        Sql = new StringBuilder().append(" Delete from "+PropertiesUtil.getTbName()+"  where AccNo='").
                append(Accno).append("'").
                append(" INSERT INTO "+PropertiesUtil.getTbName()+" (OrmMsgID,AccNo,HesabGroup,BranchNo,Name,RegDate,AvaCasActive,SendToAvaCas)").
                append(" VALUES('").append(msg223.getMsgSeq()).append("', '").
                append(Accno).append("' , '").
                append(msg223.getAccountGroup()).append("', '").
                append(msg223.getBranchCode()).append("', '").
                append(msg223.getRequesterName()).append("', '").
                append(msg223.getDateAndTime().substring(0, 8)).append("', '").
                append("1,1)").toString();

        int res = 0;
        stmnt1 = null;
        try {
            stmnt1 = connection.createStatement();
            res = stmnt1.executeUpdate(Sql);

            if (stmnt1 != null) {
                stmnt1.close();
            }

            if (connection != null) {
                connection.close();
            }


            if (res > 0)
                Result = 0;
            if (res < 0)
                Result = 10;
            if (res == 0)
                Result = 82;

            System.out.println("\nRegister Legal Customer Successfull.");

        } catch (SQLException e) {
            if (stmnt1 != null) {
                stmnt1.close();
            }

            if (connection != null) {
                connection.close();
            }

            System.out.println("\nRegister Legal Customer Fail.");

        } catch (Exception e) {
            if (stmnt1 != null) {
                stmnt1.close();
            }

            if (connection != null) {
                connection.close();
            }

            System.out.println("\nRegister Legal Customer Fail , Message False.");

            Result = 10;
        }



        saveLogDb(Accno, "200", msg223.getDateAndTime(), Result, msg223.getMsgSeq().toString());
        return Result;
    }

    private static void MessageProcess231(Message rcMsg,MessageConvertor MsC) throws Exception {
        Msg231 msg231 = (Msg231) rcMsg;
        String OriginalMsgSeq = Long.toString(msg231.getOriginalMsgSeq());

        String dateAndTime = ((Msg231) rcMsg).getDateAndTime();

        int res = msg231.getResultCode();

        String AccNo = StringUtils.leftPad(ORM.migratedRecord.get(Long.valueOf(OriginalMsgSeq)), 10, "0");
        UpdateMigrateCustomer(AccNo, OriginalMsgSeq);
        System.out.println("Result code for MsgSeq = " + OriginalMsgSeq + " is " + res);
    }
    private static int  UpdateMigrateCustomer(String Accno, String MsgSeq)throws Exception {
        int result;
        String Sql;
        Statement stmnt1;
        result = 10;
        Sql = (new StringBuilder()).append(" Update " + PropertiesUtil.getTbName() + "  Set AvaCasActive=1, SendToAvacas=1, OrmMsgID= ").append(MsgSeq).append(" Where  AccNo='").append(Accno).append("'").toString();
        int res = 0;
        if (ConnectedToOracleDB()){
            stmnt1 = connection.createStatement();
            result = stmnt1.executeUpdate(Sql);

            try {
                stmnt1.close();
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        return result;
    }


    private static void MessageProcess500(Message rcMsg,MessageConvertor MsC) throws Exception {
        Msg500 msg500 = (Msg500) rcMsg;
        String loginId = msg500.getLoginId();
        String dateAndTime = ((Msg500) rcMsg).getDateAndTime();
        int res = updateCustomer(msg500);

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
            saveLogDb(msg500.getLoginId(), "500", msg500.getDateAndTime(), 999, msg500.getMsgSeq().toString());
        }

    }
    private static int  updateCustomer(Msg500 msg500) throws Exception {
        int Result;
        String Accno;
        String Sql;
        Result = 10;
        String service = msg500.getServiceTypes();
        String isFreeTrans = service.substring(0, 1);
        Accno = StringUtils.leftPad(msg500.getLoginId(), 10, "0");
        service = (new StringBuilder()).append(service.substring(2, 3)).append(service.substring(1, 2)).append("11000000").toString();
        Statement stmnt1 = null;
        try {
            int step = 0;
            String[] da = {"0000000000000000", "0000000000000000", "0000000000000000", "0000000000000000", "0000000000000000"};
            try {

                System.out.println("msg500.getDestAccounts()[0] = " + msg500.getDestAccounts()[0] + " > substring(6, 16)");
                da[step] = msg500.getDestAccounts()[step];
                System.out.println("msg500.getDestAccounts()[1] = " + msg500.getDestAccounts()[1] + " > substring(6, 16)");
                step = 1;
                da[step] = msg500.getDestAccounts()[step];
                System.out.println("msg500.getDestAccounts()[2] = " + msg500.getDestAccounts()[2] + " > substring(6, 16)");
                step = 2;
                da[step] = msg500.getDestAccounts()[step];
                System.out.println("msg500.getDestAccounts()[3] = " + msg500.getDestAccounts()[3] + " > substring(6, 16)");
                step = 3;
                da[step] = msg500.getDestAccounts()[step];
                System.out.println("msg500.getDestAccounts()[4] = " + msg500.getDestAccounts()[4] + " > substring(6, 16)");
                step = 4;
                da[step] = msg500.getDestAccounts()[step];
            } catch (Exception e) {
                step = step + 1;
                for (int i = step; step < 5; step++)
                    da[i] = "0000000000000000";

                msg500.setDestAccounts(da);
                System.out.println("msg500 by Morteza.");
                System.out.println("msg500.getDestAccounts()[0] = " + msg500.getDestAccounts()[0] + " > substring(6, 16)");
                System.out.println("msg500.getDestAccounts()[1] = " + msg500.getDestAccounts()[1] + " > substring(6, 16)");
                System.out.println("msg500.getDestAccounts()[2] = " + msg500.getDestAccounts()[2] + " > substring(6, 16)");
                System.out.println("msg500.getDestAccounts()[3] = " + msg500.getDestAccounts()[3] + " > substring(6, 16)");
                System.out.println("msg500.getDestAccounts()[4] = " + msg500.getDestAccounts()[4] + " > substring(6, 16)");
            }

            Sql = (new StringBuilder()).append(" Update [PrivilegeRegistration] Set [fldRegisterationDate]='").append(msg500.getDateAndTime().substring(0, 8)).append("',").append(" [fldFirstAccountNo]='").append(msg500.getDestAccounts()[0].substring(6, 16)).append("',").append(" [fldSecondAccountNo]='").append(msg500.getDestAccounts()[1].substring(6, 16)).append("',").append(" [fldThirdAccountNo]='").append(msg500.getDestAccounts()[2].substring(6, 16)).append("',").append(" [fldFourthAccountNo]='").append(msg500.getDestAccounts()[3].substring(6, 16)).append("',").append(" [fldFifthAccountNo]='").append(msg500.getDestAccounts()[4].substring(6, 16)).append("',").append(" [LastChangePin]='").append(msg500.getDateAndTime().substring(0, 8)).append("',").append(" [IsFreeTransfer]='").append(isFreeTrans).append("',").append(" [fldServiceType]='").append(service).append("',").append(" [IsActive]=1,").append(" [OnlineState]=1,").append(" [AvaCasActive]=1").append(" Where fldMainAccountNo='").append(Accno).append("'").toString();
            int res;
            res = 0;

            try {
                stmnt1 = connection.createStatement();
                res = stmnt1.executeUpdate(Sql);

                if (stmnt1 != null) {
                    stmnt1.close();
                }

                if (connection != null) {
                    connection.close();
                }

                System.out.println("\nUpdate Customer Successfull.");
                //MainForm.sendString("Update Customer Successfull.");
            } catch (SQLException e) {

                if (stmnt1 != null) {
                    stmnt1.close();
                }

                if (connection != null) {
                    connection.close();
                }


                System.out.println("\nUpdate Customer Fail.");
                //MainForm.sendString("Update Customer Fail.");
                //getInstance(LoggingType.DBExceptionLog).log(e);
                //e.printStackTrace();

            }

            if (res > 0)
                Result = 0;
            if (res < 0)
                Result = 10;
            if (res == 0)
                Result = 82;
        } catch (Exception e) {
            System.out.println("\nUpdate Customer Fail , Message False.");
            //MainForm.sendString("Update Customer Fail , Message False.");

        }

        saveLogDb(Accno, "500", msg500.getDateAndTime(), Result, msg500.getMsgSeq().toString());
        return Result;
    }

    private static void MessageProcess600(Message rcMsg,MessageConvertor MsC) throws Exception {
        Msg600 msg600 = (Msg600) rcMsg;
        String loginId = msg600.getLoginId();
        String dateAndTime = ((Msg600) rcMsg).getDateAndTime();
        int res = disableAcc(msg600, loginId);

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

            saveLogDb(msg600.getLoginId(), "600", msg600.getDateAndTime(), 999, msg600.getMsgSeq().toString());
            e.printStackTrace();
        }

    }
    private static int  disableAcc(Msg600 msg600, String Loginid) throws Exception {
        int Result;
        String Sql;
        int res;
        Result = 10;
        Loginid = StringUtils.leftPad(Loginid, 10, "0");
        Sql = (new StringBuilder()).append(" update dbo.PrivilegeRegistration set isactive=0 where fldmainaccountno='").append(Loginid).append("'").toString();
        res = 0;
        Statement stmnt1 = null;
        try {
            stmnt1 = connection.createStatement();
            res = stmnt1.executeUpdate(Sql);
            if (stmnt1 != null) {
                stmnt1.close();
            }
            if (connection != null) {
                connection.close();
            }
            System.out.println("\nDisable Customer Account Successfull.");
        } catch (SQLException e) {
            if (stmnt1 != null) {
                stmnt1.close();
            }
            if (connection != null) {
                connection.close();
            }
            System.out.println("\nDisable Customer Account Fail.");
            //getInstance(LoggingType.DBExceptionLog).log(e);
        }
        if (res > 0)
            Result = 0;
        if (res < 0)
            Result = 10;
        if (res == 0)
            Result = 82;

        saveLogDb(Loginid, "600", msg600.getDateAndTime(), Result, msg600.getMsgSeq().toString());
        return Result;
    }


    private static boolean ConnectedToOracleDB(){
        try {
            String driver="oracle.jdbc.OracleDriver";
            String url="jdbc:oracle:thin:@//localhost:1521/orcl";
            String userName="SYSTEM";
            String password="12345";


            if (connection==null) {
                Class.forName(driver);
                connection= DriverManager.getConnection(url, userName, password);
                return true;
            }
            else if (connection.isClosed()){
                Class.forName(driver);
                connection= DriverManager.getConnection(url, userName, password);
                return true;
            }
            else
            {
                connection.close();
                Class.forName(driver);
                connection= DriverManager.getConnection(url, userName, password);
                return true;

            }

        } catch (Exception e) {

            return false;
        }
    }
    private static void    saveLogDb(String Accno, String MsgType, String DateTime, int ResultCode, String MsgId) throws Exception {
        String Sql;
        Sql = (new StringBuilder()).append(" INSERT INTO [OrmLog]([Accno], " +
                " [MsgType]," +
                " [DateTime]," +
                " [ResultCode]," +
                " [MsgId])" +
                " VALUES('")
                .append(Accno).append("'").append(",'")
                .append(MsgType).append("'").append(",'")
                .append(DateTime).append("'").append(",")
                .append(ResultCode).append(",'")
                .append(MsgId).append("')").toString();

        Statement stmnt1 = null;
        try {
            if (ConnectedToOracleDB()){
                stmnt1 = connection.createStatement();
                stmnt1.executeUpdate(Sql);

            }


        } catch (SQLException e) {


        }
    }

}
