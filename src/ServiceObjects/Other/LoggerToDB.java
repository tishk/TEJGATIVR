package ServiceObjects.Other;

import Pin1.currentMethod.ORM.ORM;
import Pin1.currentMethod.util.PropertiesUtil;
import ServiceObjects.Account.*;
import ServiceObjects.ISO.*;
import ServiceObjects.Pan.BillPayByBillIDPan;
import ServiceObjects.Pin.AuthenticatePin1;
import ServiceObjects.Pin.AuthenticatePin2;
import com.sabapardazesh.orm.messageing.domain.*;
import org.apache.commons.lang.StringUtils;
import utils.LoggerSettings;
import utils.PersianDateTime;
import utils.PropertiesUtils;
import utils.strUtils;

import java.sql.*;
import java.text.SimpleDateFormat;

public class LoggerToDB {
    private static final long serialVersionUID = 1286846752369758700L;
    Object objectToLog;
    public LoggerToDB(Object objectToLog)  {
        this.objectToLog=objectToLog;
        try {
            if (LoggerSettings.getUseQueueForLogToDataBase()){
                LoggerSettings.dataBaseLoggerQueue.enqueue(this);
                setResultOfLog(true);
            }else{
                doLog(objectToLog);
            }
        } catch (SQLException e) {
            setResultOfLog(false);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void doLog(Object objectToLog) throws Exception {
        if (connectedToDB()) setResultOfLog(LogToDataBase(objectToLog));
        else setResultOfLog(false);
    }
    public void doLogByQueue() throws Exception {
        if (connectedToDB()) setResultOfLog(LogToDataBase(objectToLog));
        else setResultOfLog(false);
    }

    strUtils strutils=new strUtils();

    private  Connection connection =null;
    public   boolean    connected=false;

    private  boolean  resultOfLog=false;
    private  void setResultOfLog(boolean ResultOfLog){
        this.resultOfLog=ResultOfLog;
    }
    public   boolean getResultOfLog(){
        return resultOfLog;
    }

    private  int   ormResult=0;
    private  void  setOrmResult(int ormresult){
        this.ormResult=ormresult;
    }
    public   int   getOrmResult(){
        return ormResult;
    }

    private  boolean connectedToDB() throws SQLException, InterruptedException {
        int tryCount=7;
        while(tryCount-->0){
            if (getConnectionisSuccess()){
                return true;
            }
            Thread.sleep(100);
        }
        return false;
    }

    private boolean getConnectionisSuccess() {
        String DBURL= PropertiesUtils.getDataBaseURL();
        String DBUSER=PropertiesUtils.getDataBaseUserName();
        String DBPASS=PropertiesUtils.getDataBasePassword();

        try {
            if (connection==null) {
                DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
                connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
                return true;
            }
            else if (connection.isClosed()){
                connection=null;
                DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
                connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
                return true;
            }
            else
            {
                connection.close();
                connection=null;
                DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
                connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
                return true;
            }

        } catch (Exception e) {
            System.out.println("connection Failed:"+e.getMessage());
            connection=null;
            return false;
        }
    }

    private  void    RunExecuteUpdate(String Command) {
        try {
            Statement statement = connection.createStatement();
            statement.executeUpdate(Command);
            connection.commit();

        } catch (Exception e) {

        }
    }
    private  void    RunExecuteQuery(String Command){

        try
        {
          connection.createStatement().executeQuery(Command);

        }catch (Exception e){
            //setSelectResult(null);
        }
    }

    private  java.sql.Timestamp getCurrentTimeStamp() {

        java.util.Date today = new java.util.Date();
        long t = today.getTime();
        return new java.sql.Timestamp(t);

    }
    private  java.sql.Date getCurrentDate() {

        java.sql.Date today = new java.sql.Date(new java.util.Date().getTime());
       return today;

    }
    private  String getCurrentTime() {

        SimpleDateFormat simpleDateFormat=new SimpleDateFormat("kkmmss");
        return simpleDateFormat.format(System.currentTimeMillis());
    }
    private  String getCurrentShamsiDate() {

        PersianDateTime persianDateTime=new PersianDateTime();
        return persianDateTime.getShamsiDateForLogPayment();

    }

    private  boolean LogToDataBase(Object objectToLog) throws Exception {
             if (objectToLog instanceof AuthenticatePin1){
                 AuthenticatePin1 authenticatePin1obj=(AuthenticatePin1) objectToLog;
                 if (authenticatePin1obj.getDoChangePin()) return accountPin1Change(authenticatePin1obj);
                 else return accountPin1authenticate(authenticatePin1obj);
             }
        else if (objectToLog instanceof AuthenticatePin2)
             {
                 AuthenticatePin2 authenticatePin2obj=(AuthenticatePin2) objectToLog;
                 if (authenticatePin2obj.getDoChangePin()) return accountPin2Change(authenticatePin2obj);
                 else return accountPin2authenticate(authenticatePin2obj);
             }
        else if (objectToLog instanceof SMSAlarmTransaction){
                 SMSAlarmTransaction smsAlarmTransaction=(SMSAlarmTransaction)objectToLog;
                 if (smsAlarmTransaction.getIsSetMobileNumber())
                 return accountMobileSet((SMSAlarmTransaction) objectToLog);
                 else   return accountMobileGet((SMSAlarmTransaction) objectToLog);
             }
        else if (objectToLog instanceof AccountInformation)           {
                 if (LoggerSettings.useQueueForLogToDataBase){
                      return accountInformation((AccountInformation) objectToLog);
                 }else{
                     return true;
                 }
             }
        else if (objectToLog instanceof BalanceForAccount)           {
                 if (LoggerSettings.useQueueForLogToDataBase){
                    return accountBalance((BalanceForAccount) objectToLog);//------
                 }else{
                     return true;
                 }
             }
        else if (objectToLog instanceof Transaction)                  {
                 if (LoggerSettings.useQueueForLogToDataBase){
                    return accountStatementList((Transaction) objectToLog);
                 }else{
                     return true;
                 }
             }
        else if (objectToLog instanceof CardNoOfAccount)              return accountGetPan((CardNoOfAccount) objectToLog);
        else if (objectToLog instanceof AccountNoOfCard)              return accountGetAccountOfPan((AccountNoOfCard) objectToLog);
        else if (objectToLog instanceof RegisterAccount)              return accountRegister((RegisterAccount) objectToLog);
        else if (objectToLog instanceof FundTransfer)                 return accountFundTransfer((FundTransfer) objectToLog);
        else if (objectToLog instanceof FollowUpTransaction)          return accountFollowUpFundTransfer((FollowUpTransaction) objectToLog);
        else if (objectToLog instanceof InternalFollowUp   )          return accountInternalFollowUpFundTransfer((InternalFollowUp) objectToLog);
        else if (objectToLog instanceof BillPayByIDAccount)           return accountBillPaymentByBillID((BillPayByIDAccount) objectToLog);
        else if (objectToLog instanceof BillPayByIDAccountValid)      return accountBillPaymentValidation((BillPayByIDAccountValid) objectToLog);
        else if (objectToLog instanceof ChequeStatus)                 return accountChequeStatus((ChequeStatus) objectToLog);
        else if (objectToLog instanceof BlockAccount)                 return accountBlock((BlockAccount) objectToLog);
        else if (objectToLog instanceof ShebaID)                      return accountShebaID((ShebaID) objectToLog);
        else if (objectToLog instanceof ISO100)                       return TelSwitchSavePendingAndRequest((ISO100) objectToLog);
        else if (objectToLog instanceof ISO110)                       return TelSwitchSaveResponse((ISO110) objectToLog);
        else if (objectToLog instanceof ISO200)                       return cardSavePendingAndRequest((ISO200) objectToLog);
        else if (objectToLog instanceof ISO210)                       return cardSaveResponse((ISO210) objectToLog);
        else if (objectToLog instanceof ISO400)                       return cardSaveRequestReverse((ISO400) objectToLog);
        else if (objectToLog instanceof ISO430)                       return cardSaveResponceReverse((ISO430) objectToLog);
        else if (objectToLog instanceof ServiceObjects.ISOShetabVer7.ISO100)return TelSwitchSavePendingAndRequest((ServiceObjects.ISOShetabVer7.ISO100) objectToLog);
        else if (objectToLog instanceof ServiceObjects.ISOShetabVer7.ISO110)return TelSwitchSaveResponse((ServiceObjects.ISOShetabVer7.ISO110) objectToLog);
        else if (objectToLog instanceof ServiceObjects.ISOShetabVer7.ISO200)return cardSavePendingAndRequest((ServiceObjects.ISOShetabVer7.ISO200) objectToLog);
        else if (objectToLog instanceof ServiceObjects.ISOShetabVer7.ISO210)return cardSaveResponse((ServiceObjects.ISOShetabVer7.ISO210) objectToLog);
        else if (objectToLog instanceof ServiceObjects.ISOShetabVer7.ISO400)return cardSaveRequestReverse((ServiceObjects.ISOShetabVer7.ISO400) objectToLog);
        else if (objectToLog instanceof ServiceObjects.ISOShetabVer7.ISO430)return cardSaveResponceReverse((ServiceObjects.ISOShetabVer7.ISO430) objectToLog);
        else if (objectToLog instanceof Msg200)                       return orm_registerCustomer((Msg200) objectToLog);
        else if (objectToLog instanceof Msg222)                       return orm_registerLegalCustomer((Msg222) objectToLog);
        else if (objectToLog instanceof Msg223)                       return orm_registerJointAccount((Msg223) objectToLog);
        else if (objectToLog instanceof Msg231)                       return orm_UpdateMigrateCustomer((Msg231) objectToLog);
        else if (objectToLog instanceof Msg500)                       return orm_updateCustomer((Msg500) objectToLog);
        else if (objectToLog instanceof Msg600)                       return orm_disableAcc((Msg600) objectToLog);
        else if (objectToLog instanceof PaymentOBJ)                   return paymentObjectLogToDB((PaymentOBJ) objectToLog);
        else if (objectToLog instanceof BillPayByBillIDPan)           return panBillPaymentByBillID((BillPayByBillIDPan) objectToLog);

        else return false;
    }

    private  boolean accountPin1authenticate(AuthenticatePin1 accountPin1auth) throws SQLException {
        if (accountPin1auth.getDoChangePin()) return accountPin1Change(accountPin1auth);
        else{
            PreparedStatement preparedStatement = null;

            String insertTableSQL = "INSERT INTO TBL_LOG_ACC_PIN1_AUTH"
                    + "(TRANSACTIONID, ACCNO, ACTIONCODE,DATETIME,CALLERID,CLIENTIP,CALLID) VALUES"
                    + "(?,?,?,?,?,?,?)";
            try {

                preparedStatement = connection.prepareStatement(insertTableSQL);

                //preparedStatement.setString(1,"123456789123");
                preparedStatement.setString(1, accountPin1auth.getMsgSeq());
                preparedStatement.setString(2, accountPin1auth.getAccountNumber());
                preparedStatement.setString(3, accountPin1auth.getActionCode());
                preparedStatement.setTimestamp(4, getCurrentTimeStamp());
                preparedStatement.setString(5, accountPin1auth.getCallerID());
                preparedStatement.setString(6, accountPin1auth.getIPOfClient());
                preparedStatement.setString(7, accountPin1auth.getCallUniqID());
                preparedStatement.executeUpdate();
            }catch (SQLException e) {
                accountPin1auth.setGatewayMessage(e.getMessage());
                if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
                if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
                return false;
            }catch (Exception e) {
                accountPin1auth.setGatewayMessage(e.getMessage());

                if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
                if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
                return false;
            }finally {
                if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
                if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
                return true;
            }
        }

    }
    private  boolean accountPin1Change(AuthenticatePin1 accountPin1authenticate) throws SQLException {

        PreparedStatement preparedStatement = null;

        String insertTableSQL = "INSERT INTO TBL_LOG_ACC_PIN1_CHANGE"
                + "(TRANSACTIONID, ACCNO, ACTIONCODE,DATETIME,CALLID) VALUES"
                + "(?,?,?,?,?)";
        try {

            preparedStatement = connection.prepareStatement(insertTableSQL);

            //preparedStatement.setString(1,"123456789123");
            preparedStatement.setString(1, accountPin1authenticate.getMsgSeq());
            preparedStatement.setString(2, accountPin1authenticate.getAccountNumber());
            preparedStatement.setString(3, accountPin1authenticate.getActionCode());
            preparedStatement.setTimestamp(4, getCurrentTimeStamp());
            preparedStatement.setString(5, accountPin1authenticate.getCallUniqID());

            preparedStatement.executeUpdate();
        }catch (SQLException e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }catch (Exception e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }finally {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return true;
        }
    }
    private  boolean accountPin2authenticate(AuthenticatePin2 accountPin2auth) throws SQLException {
        if (accountPin2auth.getDoChangePin()) return accountPin2Change(accountPin2auth);
        else{
            PreparedStatement preparedStatement = null;

            String insertTableSQL = "INSERT INTO TBL_LOG_ACC_PIN2_AUTH"
                    + "(TRANSACTIONID, ACCNO,ACTIONCODE,DATETIME,CALLERID) VALUES"
                    + "(?,?,?,?,?)";
            try {

                preparedStatement = connection.prepareStatement(insertTableSQL);

                //preparedStatement.setString(1,"123456789123");
                preparedStatement.setString(1, accountPin2auth.getMsgSeq());
                preparedStatement.setString(2, accountPin2auth.getAccountNumber());
                preparedStatement.setString(3, accountPin2auth.getActionCode());
                preparedStatement.setTimestamp(4, getCurrentTimeStamp());
                preparedStatement.setString(5, accountPin2auth.getCallerID());

                preparedStatement.executeUpdate();
            }catch (SQLException e) {
                if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
                if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
                return false;
            }catch (Exception e) {
                if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
                if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
                return false;
            }finally {
                if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
                if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
                return true;
            }
        }

    }
    private  boolean accountPin2Change(AuthenticatePin2 accountPin2authenticate) throws SQLException {

        PreparedStatement preparedStatement = null;

        String insertTableSQL = "INSERT INTO TBL_LOG_ACC_PIN2_CHANGE"
                + "(TRANSACTIONID, ACCNO, ACTIONCODE,DATETIME,CALLID) VALUES"
                + "(?,?,?,?,?)";
        try {

            preparedStatement = connection.prepareStatement(insertTableSQL);

            //preparedStatement.setString(1,"123456789123");
            preparedStatement.setString(1, accountPin2authenticate.getMsgSeq());
            preparedStatement.setString(2, accountPin2authenticate.getAccountNumber());
            preparedStatement.setString(3, accountPin2authenticate.getActionCode());
            preparedStatement.setTimestamp(4, getCurrentTimeStamp());
            preparedStatement.setString(5, accountPin2authenticate.getCallUniqID());
            preparedStatement.executeUpdate();
        }catch (SQLException e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }catch (Exception e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }finally {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return true;
        }
    }
    private  boolean accountMobileGet(SMSAlarmTransaction smsAlarmTransaction) throws SQLException {

        PreparedStatement preparedStatement = null;

        String insertTableSQL = "INSERT INTO TBL_LOG_ACC_MOBILENEMBERGET"
                + "(TRANSACTIONID, ACCOUNTNO, ACTIONCODE,DATETIME,MOBILENUMBER,CALLID) VALUES"
                + "(?,?,?,?,?,?)";
        try {

            preparedStatement = connection.prepareStatement(insertTableSQL);
            preparedStatement.setString(1, smsAlarmTransaction.getTRANSACTIONID());
            preparedStatement.setString(2, smsAlarmTransaction.getAccountNumber());
            preparedStatement.setString(3, smsAlarmTransaction.getActionCode());
            preparedStatement.setTimestamp(4, getCurrentTimeStamp());
            preparedStatement.setString(5, smsAlarmTransaction.getMobileNumber());
            preparedStatement.setString(6, smsAlarmTransaction.getCallUniqID());

            preparedStatement.executeUpdate();
        }catch (SQLException e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }catch (Exception e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }finally {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return true;
        }
    }
    private  boolean accountMobileSet(SMSAlarmTransaction smsAlarmTransaction) throws SQLException {

        PreparedStatement preparedStatement = null;

        String insertTableSQL = "INSERT INTO TBL_LOG_ACC_MOBILENUMBERSET"
                + "(TRANSACTIONID, ACCOUNTNO, ACTIONCODE,DATETIME,MOBILENUMBER,CALLID) VALUES"
                + "(?,?,?,?,?,?)";
        try {
            preparedStatement = connection.prepareStatement(insertTableSQL);
            preparedStatement.setString(1, smsAlarmTransaction.getTRANSACTIONID());
            preparedStatement.setString(2, smsAlarmTransaction.getAccountNumber());
            preparedStatement.setString(3, smsAlarmTransaction.getActionCode());
            preparedStatement.setTimestamp(4, getCurrentTimeStamp());
            preparedStatement.setString(5, smsAlarmTransaction.getMobileNumber());
            preparedStatement.setString(5, smsAlarmTransaction.getCallUniqID());
            preparedStatement.executeUpdate();
        }catch (SQLException e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }catch (Exception e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }finally {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return true;
        }
    }

    private  boolean accountBalance(BalanceForAccount balanceForAccount) throws SQLException {

        PreparedStatement preparedStatement = null;

        String insertTableSQL = "INSERT INTO TBL_LOG_ACC_BALANCE"
                              + "(TRANSACTIONID, ACCOUNTNO, AVILABLEBALANCE,ACTUALBALANCE, ACTIONCODE,DATETIME,CALLID) VALUES"
                              + "(?,?,?,?,?,?,?)";
        //PreparedStatement preparedStatementMain = null;

        String insertMainTableSQL = "INSERT INTO TBL_LOG_REQUESTRESPONSE"
                + "(TRANSACTIONID, REQUEST, RESPONSE,CALLID) VALUES"
                + "(?,?,?,?)";
        try {

            preparedStatement = connection.prepareStatement(insertTableSQL);

            //preparedStatement.setString(1,"123456789123");
            preparedStatement.setString(1, balanceForAccount.getMsgSeq());
            preparedStatement.setString(2, balanceForAccount.getAccountNumber());
            preparedStatement.setString(3, balanceForAccount.getResultFromChannel().getAvailableBalance());
            preparedStatement.setString(4, balanceForAccount.getResultFromChannel().getActualBalance());
            preparedStatement.setString(5, balanceForAccount.getResultFromChannel().getAction_code());
            preparedStatement.setTimestamp(6, getCurrentTimeStamp());
            preparedStatement.setString(7, balanceForAccount.getCallUniqID());
            preparedStatement.executeUpdate();
           if (PropertiesUtils.getSaveXMLRequestAndResponseInDB()){
               preparedStatement = connection.prepareStatement(insertMainTableSQL);
               //preparedStatement.setString(1,"123456789123");
               preparedStatement.setString(1, balanceForAccount.getMsgSeq());
               preparedStatement.setString(2, balanceForAccount.getResultFromChannel().getRequestXml());
               preparedStatement.setString(3, balanceForAccount.getResultFromChannel().getResponseXml());
               preparedStatement.setString(4, balanceForAccount.getCallUniqID());
               preparedStatement.executeUpdate();

           }

        }catch (SQLException e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }catch (Exception e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }finally {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return true;
        }
    }
    private  boolean accountBlock(BlockAccount blockAccount) throws SQLException {

        PreparedStatement preparedStatement = null;

        String insertTableSQL = "INSERT INTO TBL_LOG_ACC_BLOCKACCOUNT"
                + "(TRANSACTIONID, ACCOUNTNO, ACTIONCODE,DATETIME,CALLID) VALUES"
                + "(?,?,?,?,?)";
        //PreparedStatement preparedStatementMain = null;

        String insertMainTableSQL = "INSERT INTO TBL_LOG_REQUESTRESPONSE"
                + "(TRANSACTIONID, REQUEST, RESPONSE,CALLID) VALUES"
                + "(?,?,?,?)";
        try {

            preparedStatement = connection.prepareStatement(insertTableSQL);

            //preparedStatement.setString(1,"123456789123");
            preparedStatement.setString(1, blockAccount.getMsgSeq());
            preparedStatement.setString(2, blockAccount.getAccountNumber());
            preparedStatement.setString(3, blockAccount.getResultFromChannel().getAction_code());
            preparedStatement.setTimestamp(4, getCurrentTimeStamp());
            preparedStatement.setString(5, blockAccount.getCallUniqID());

            preparedStatement.executeUpdate();
            if (PropertiesUtils.getSaveXMLRequestAndResponseInDB()) {
                preparedStatement = connection.prepareStatement(insertMainTableSQL);
                //preparedStatement.setString(1,"123456789123");
                preparedStatement.setString(1, blockAccount.getMsgSeq());
                preparedStatement.setString(2, blockAccount.getResultFromChannel().getRequestXml());
                preparedStatement.setString(3, blockAccount.getResultFromChannel().getResponseXml());
                preparedStatement.setString(4, blockAccount.getCallUniqID());
                preparedStatement.executeUpdate();
            }
        }catch (SQLException e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }catch (Exception e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }finally {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return true;
        }
    }
    private  boolean accountInformation(AccountInformation accountInformation) throws SQLException {

        PreparedStatement preparedStatement = null;

        String insertTableSQL = "INSERT INTO TBL_LOG_ACC_ACCOUNTINFORMATION"
                + "(TRANSACTIONID, ACCOUNTNO, ACTIONCODE,DATETIME,CALLID) VALUES"
                + "(?,?,?,?,?)";
        String insertMainTableSQL = "INSERT INTO TBL_LOG_REQUESTRESPONSE"
                + "(TRANSACTIONID, REQUEST, RESPONSE,CALLID) VALUES"
                + "(?,?,?,?)";
        try {

            preparedStatement = connection.prepareStatement(insertTableSQL);

            //preparedStatement.setString(1,"123456789123");
            preparedStatement.setString(1, accountInformation.getMsgSeq());
            preparedStatement.setString(2, accountInformation.getAccountNumber());
            preparedStatement.setString(3, accountInformation.getResultFromChannel().getAction_code());
            preparedStatement.setTimestamp(4, getCurrentTimeStamp());
            preparedStatement.setString(5, accountInformation.getCallUniqID());
            preparedStatement.executeUpdate();
            if (PropertiesUtils.getSaveXMLRequestAndResponseInDB()) {
                preparedStatement = connection.prepareStatement(insertMainTableSQL);
                //preparedStatement.setString(1,"123456789123");
                preparedStatement.setString(1, accountInformation.getMsgSeq());
                preparedStatement.setString(2, accountInformation.getResultFromChannel().getRequestXml());
                preparedStatement.setString(3, accountInformation.getResultFromChannel().getResponseXml());
                preparedStatement.setString(4, accountInformation.getCallUniqID());
                preparedStatement.executeUpdate();
            }
        }catch (SQLException e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }catch (Exception e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }finally {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return true;
        }
    }
    private  boolean accountStatementList(Transaction transaction) throws SQLException {

        PreparedStatement preparedStatement = null;

        String insertTableSQL = "INSERT INTO TBL_LOG_ACC_TRANSACTION"
                + "(TRANSACTIONID, ACCOUNTNO, ACTIONCODE,STARTDATE,ENDDATE,TRANSACTIONCOUNT,DATETIME,CALLID) VALUES"
                + "(?,?,?,?,?,?,?,?)";
        String insertMainTableSQL = "INSERT INTO TBL_LOG_REQUESTRESPONSE"
                + "(TRANSACTIONID, REQUEST, RESPONSE,CALLID) VALUES"
                + "(?,?,?,?)";
        try {

            preparedStatement = connection.prepareStatement(insertTableSQL);

            //preparedStatement.setString(1,"123456789123");
            preparedStatement.setString(1, transaction.getMsgSeq());
            preparedStatement.setString(2, transaction.getAccountNumber());
            preparedStatement.setString(3, transaction.getResultFromCM().getAction_code());
            preparedStatement.setString(4, transaction.getResultFromCM().getFromDate());
            preparedStatement.setString(5, transaction.getResultFromCM().getToDate());
            preparedStatement.setString(6, transaction.getTransactionCount());
            preparedStatement.setTimestamp(7, getCurrentTimeStamp());
            preparedStatement.setString(8, transaction.getCallUniqID());
            preparedStatement.executeUpdate();
            if (PropertiesUtils.getSaveXMLRequestAndResponseInDB()) {
                preparedStatement = connection.prepareStatement(insertMainTableSQL);
                //preparedStatement.setString(1,"123456789123");
                preparedStatement.setString(1, transaction.getMsgSeq());
                preparedStatement.setString(2, transaction.getResultFromCM().getRequestXml());
                preparedStatement.setString(3, transaction.getResultFromCM().getResponseXml());
                preparedStatement.setString(4, transaction.getCallUniqID());
                preparedStatement.executeUpdate();
            }
        }catch (SQLException e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }catch (Exception e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }finally {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return true;
        }
    }
    private  boolean accountGetPan(CardNoOfAccount cardNoOfAccount) throws SQLException {

        PreparedStatement preparedStatement = null;

        String insertTableSQL = "INSERT INTO TBL_LOG_ACC_GETPANOFACCOUNT"
                + "(TRANSACTIONID, ACCOUNTNO,ACTIONCODE,PAN,DATETIME,CALLID) VALUES"
                + "(?,?,?,?,?,?)";
        //PreparedStatement preparedStatementMain = null;

        String insertMainTableSQL = "INSERT INTO TBL_LOG_REQUESTRESPONSE"
                + "(TRANSACTIONID, REQUEST, RESPONSE,CALLID) VALUES"
                + "(?,?,?,?)";
        try {

            preparedStatement = connection.prepareStatement(insertTableSQL);

            //preparedStatement.setString(1,"123456789123");
            preparedStatement.setString(1, cardNoOfAccount.getMsgSeq());
            preparedStatement.setString(2, cardNoOfAccount.getResultFromChannel().getAccNo());
            preparedStatement.setString(3, cardNoOfAccount.getResultFromChannel().getAction_code());
            preparedStatement.setString(4, cardNoOfAccount.getResultFromChannel().getPan());
            preparedStatement.setTimestamp(5, getCurrentTimeStamp());
            preparedStatement.setString(6, cardNoOfAccount.getCallUniqID());

            preparedStatement.executeUpdate();
            if (PropertiesUtils.getSaveXMLRequestAndResponseInDB()) {
                preparedStatement = connection.prepareStatement(insertMainTableSQL);
                //preparedStatement.setString(1,"123456789123");
                preparedStatement.setString(1, cardNoOfAccount.getMsgSeq());
                preparedStatement.setString(2, cardNoOfAccount.getResultFromChannel().getRequestXml());
                preparedStatement.setString(3, cardNoOfAccount.getResultFromChannel().getResponseXml());
                preparedStatement.setString(4, cardNoOfAccount.getCallUniqID());

                preparedStatement.executeUpdate();
            }
        }catch (SQLException e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }catch (Exception e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }finally {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return true;
        }
    }
    private  boolean accountGetAccountOfPan(AccountNoOfCard accountNoOfCard) throws SQLException {

        PreparedStatement preparedStatement = null;

        String insertTableSQL = "INSERT INTO TBL_LOG_ACC_GETACCOUNTOFPAN"
                + "(TRANSACTIONID, PAN,ACTIONCODE,ACCOUNT_CFS,ACCOUNT_FARAGIR,DATETIME,CALLID) VALUES"
                + "(?,?,?,?,?,?,?)";
        //PreparedStatement preparedStatementMain = null;

        String insertMainTableSQL = "INSERT INTO TBL_LOG_REQUESTRESPONSE"
                + "(TRANSACTIONID, REQUEST, RESPONSE,CALLID) VALUES"
                + "(?,?,?,?)";
        try {

            preparedStatement = connection.prepareStatement(insertTableSQL);

            //preparedStatement.setString(1,"123456789123");
            preparedStatement.setString(1, accountNoOfCard.getMsgSeq());
            preparedStatement.setString(2, accountNoOfCard.getResultFromChannel().getPan());
            preparedStatement.setString(3, accountNoOfCard.getResultFromChannel().getAction_code());
            preparedStatement.setString(4, accountNoOfCard.getResultFromChannel().getCfsAccNo());
            preparedStatement.setString(5, accountNoOfCard.getResultFromChannel().getFarAccNo());
            preparedStatement.setTimestamp(6, getCurrentTimeStamp());
            preparedStatement.setString(7, accountNoOfCard.getCallUniqID());

            preparedStatement.executeUpdate();
            if (PropertiesUtils.getSaveXMLRequestAndResponseInDB()) {
                preparedStatement = connection.prepareStatement(insertMainTableSQL);
                //preparedStatement.setString(1,"123456789123");
                preparedStatement.setString(1, accountNoOfCard.getMsgSeq());
                preparedStatement.setString(2, accountNoOfCard.getResultFromChannel().getRequestXml());
                preparedStatement.setString(3, accountNoOfCard.getResultFromChannel().getResponseXml());
                preparedStatement.setString(4, accountNoOfCard.getCallUniqID());

                preparedStatement.executeUpdate();
            }
        }catch (SQLException e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }catch (Exception e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }finally {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return true;
        }
    }
    private  boolean accountRegister(RegisterAccount registerAccount) throws SQLException {

        PreparedStatement preparedStatement = null;

        String insertTableSQL = "INSERT INTO TBL_LOG_ACC_REGISTRATION"
                + "(TRANSACTIONID, ACCOUNTNO,SERVICESTYPE,ACTIONCODE,DATETIME,CALLID) VALUES"
                + "(?,?,?,?,?,?)";
        //PreparedStatement preparedStatementMain = null;

        String insertMainTableSQL = "INSERT INTO TBL_LOG_REQUESTRESPONSE"
                + "(TRANSACTIONID, REQUEST, RESPONSE,CALLID) VALUES"
                + "(?,?,?,?)";
        try {

            preparedStatement = connection.prepareStatement(insertTableSQL);

            //preparedStatement.setString(1,"123456789123");
            preparedStatement.setString(1, registerAccount.getMsgSeq());
            preparedStatement.setString(2, registerAccount.getResultFromChannel().getAccountNumber());
            preparedStatement.setString(3, registerAccount.getResultFromChannel().getServiceType());
            preparedStatement.setString(4, registerAccount.getResultFromChannel().getAction_code());
            preparedStatement.setTimestamp(5, getCurrentTimeStamp());
            preparedStatement.setString(6, registerAccount.getCallUniqID());

            preparedStatement.executeUpdate();
            if (PropertiesUtils.getSaveXMLRequestAndResponseInDB()) {
                preparedStatement = connection.prepareStatement(insertMainTableSQL);
                //preparedStatement.setString(1,"123456789123");
                preparedStatement.setString(1, registerAccount.getMsgSeq());
                preparedStatement.setString(2, registerAccount.getResultFromChannel().getRequestXml());
                preparedStatement.setString(3, registerAccount.getResultFromChannel().getResponseXml());
                preparedStatement.setString(4, registerAccount.getCallUniqID());

                preparedStatement.executeUpdate();
            }
        }catch (SQLException e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }catch (Exception e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }finally {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return true;
        }
    }

    private  boolean accountFollowUpFundTransfer(FollowUpTransaction followUpTransaction) throws SQLException {

        PreparedStatement preparedStatement = null;

        String insertTableSQL = "INSERT INTO TBL_LOG_ACC_FOLLOWUPTRANSFER"
                + "(TRANSACTIONID, ACC_SOURCE,ACC_DESTINATION,AMOUNT,FOLLOWUPCODE,TRANS_DATE,TRANS_TIME,STATUS,ACTIONCODE,DATETIME,CALLID) VALUES"
                + "(?,?,?,?,?,?,?,?,?,?,?)";
        //PreparedStatement preparedStatementMain = null;

        String insertMainTableSQL = "INSERT INTO TBL_LOG_REQUESTRESPONSE"
                + "(TRANSACTIONID, REQUEST, RESPONSE,CALLID) VALUES"
                + "(?,?,?,?)";
        try {

            preparedStatement = connection.prepareStatement(insertTableSQL);

            //preparedStatement.setString(1,"123456789123");
            preparedStatement.setString(1, followUpTransaction.getMsgSeq());
            preparedStatement.setString(2, followUpTransaction.getResultFromChannel().getSrcAccountNumber());
            preparedStatement.setString(3, followUpTransaction.getResultFromChannel().getDstAccountNumber());
            preparedStatement.setString(4, followUpTransaction.getResultFromChannel().getTransAmount());
            preparedStatement.setString(5, followUpTransaction.getResultFromChannel().getFollowUpCode());
            preparedStatement.setString(6, followUpTransaction.getResultFromChannel().getTransDate());
            preparedStatement.setString(7, followUpTransaction.getResultFromChannel().getTransTime());
            preparedStatement.setString(8, followUpTransaction.getResultFromChannel().getTransStatus());
            preparedStatement.setString(9, followUpTransaction.getResultFromChannel().getAction_code());
            preparedStatement.setTimestamp(10, getCurrentTimeStamp());
            preparedStatement.setString(11, followUpTransaction.getCallUniqID());

            preparedStatement.executeUpdate();
            if (PropertiesUtils.getSaveXMLRequestAndResponseInDB()) {
                preparedStatement = connection.prepareStatement(insertMainTableSQL);
                //preparedStatement.setString(1,"123456789123");
                preparedStatement.setString(1, followUpTransaction.getMsgSeq());
                preparedStatement.setString(2, followUpTransaction.getResultFromChannel().getRequestXml());
                preparedStatement.setString(3, followUpTransaction.getResultFromChannel().getResponseXml());
                preparedStatement.setString(4, followUpTransaction.getCallUniqID());

                preparedStatement.executeUpdate();
            }

        }catch (SQLException e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }catch (Exception e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }finally {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return true;
        }
    }
    private  boolean accountInternalFollowUpFundTransfer(InternalFollowUp internalFollowUp) throws SQLException {

        PreparedStatement preparedStatement = null;

        String insertTableSQL = "INSERT INTO TBL_LOG_ACC_FOLLOWUPTRANSFER"
                + "(TRANSACTIONID, FOLLOWUPCODE,STATUS,ACTIONCODE,DATETIME,CALLID) VALUES"
                + "(?,?,?,?,?,?)";

        try {

            preparedStatement = connection.prepareStatement(insertTableSQL);

            //preparedStatement.setString(1,"123456789123");
            preparedStatement.setString(1, internalFollowUp.getMessageSequence());
            preparedStatement.setString(2, internalFollowUp.getFollowUpCode());
            preparedStatement.setString(3, internalFollowUp.getstatus());
            preparedStatement.setString(4, internalFollowUp.getActionCode());
            preparedStatement.setTimestamp(5, getCurrentTimeStamp());
            preparedStatement.setString(6, internalFollowUp.getCallUniqID());

            preparedStatement.executeUpdate();


        }catch (SQLException e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }catch (Exception e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }finally {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return true;
        }
    }

    private  boolean accountBillPaymentValidation(BillPayByIDAccountValid billPayByIDAccountValid) throws SQLException {

        PreparedStatement preparedStatement = null;

        String insertTableSQL = "INSERT INTO TBL_LOG_ACC_BILLPAYVALIDATION"
                + "(TRANSACTIONID,SOURCEACCNO,AMOUNT,BILLID,PAYID,SERVICECODE,COMPANUCODE,DATETIME,CALLID) VALUES"
                + "(?,?,?,?,?,?,?,?,?)";
        //PreparedStatement preparedStatementMain = null;

        String insertMainTableSQL = "INSERT INTO TBL_LOG_REQUESTRESPONSE"
                + "(TRANSACTIONID, REQUEST, RESPONSE,CALLID) VALUES"
                + "(?,?,?,?)";
        try {

            preparedStatement = connection.prepareStatement(insertTableSQL);

            //preparedStatement.setString(1,"123456789123");
            preparedStatement.setString(1, billPayByIDAccountValid.getMsgSeq());
            preparedStatement.setString(2, billPayByIDAccountValid.getResultFromChannel().getSrcAccount());
            preparedStatement.setString(3, billPayByIDAccountValid.getResultFromChannel().getAmount());
            preparedStatement.setString(4, billPayByIDAccountValid.getResultFromChannel().getBillID());
            preparedStatement.setString(5, billPayByIDAccountValid.getResultFromChannel().getPaymentID());
            preparedStatement.setString(6, billPayByIDAccountValid.getResultFromChannel().getServiceCode());
            preparedStatement.setString(7, billPayByIDAccountValid.getResultFromChannel().getCompany());
            preparedStatement.setTimestamp(8, getCurrentTimeStamp());
            preparedStatement.setString(9, billPayByIDAccountValid.getMsgSeq());

            preparedStatement.executeUpdate();
            if (PropertiesUtils.getSaveXMLRequestAndResponseInDB()) {
                preparedStatement = connection.prepareStatement(insertMainTableSQL);
                //preparedStatement.setString(1,"123456789123");
                preparedStatement.setString(1, billPayByIDAccountValid.getMsgSeq());
                preparedStatement.setString(2, billPayByIDAccountValid.getResultFromChannel().getRequestXml());
                preparedStatement.setString(3, billPayByIDAccountValid.getResultFromChannel().getResponseXml());
                preparedStatement.setString(4, billPayByIDAccountValid.getMsgSeq());

                preparedStatement.executeUpdate();
            }
        }catch (SQLException e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }catch (Exception e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }finally {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return true;
        }
    }
    private  boolean accountChequeStatus(ChequeStatus chequeStatus) throws SQLException {

        PreparedStatement preparedStatement = null;

        String insertTableSQL = "INSERT INTO TBL_LOG_ACC_CHEQUESTATUS"
                + "(TRANSACTIONID,ACCOUNTNO,SERIAL,AMOUNT,OPERATIONDATE,STATUS,STATUSDESCRIPTION,ACTIONCODE,DATETIME,CALLID) VALUES"
                + "(?,?,?,?,?,?,?,?,?,?)";
        //PreparedStatement preparedStatementMain = null;

        String insertMainTableSQL = "INSERT INTO TBL_LOG_REQUESTRESPONSE"
                + "(TRANSACTIONID, REQUEST, RESPONSE,CALLID) VALUES"
                + "(?,?,?,?)";
        try {

            preparedStatement = connection.prepareStatement(insertTableSQL);

            //preparedStatement.setString(1,"123456789123");
            preparedStatement.setString(1, chequeStatus.getMsgSeq());
            preparedStatement.setString(2, chequeStatus.getResultFromChannel().getAccountNumber());
            preparedStatement.setString(3, chequeStatus.getResultFromChannel().getChequeNumber());
            preparedStatement.setString(4, chequeStatus.getResultFromChannel().getChequeAmount());
            preparedStatement.setString(5, chequeStatus.getResultFromChannel().getChequeOpDate());
            preparedStatement.setString(6, chequeStatus.getResultFromChannel().getChequeStatus());
            preparedStatement.setString(7, chequeStatus.getResultFromChannel().getChequeStatusDesc());
            preparedStatement.setString(8, chequeStatus.getResultFromChannel().getAction_code());
            preparedStatement.setTimestamp(9, getCurrentTimeStamp());
            preparedStatement.setString(10, chequeStatus.getCallUniqID());

            preparedStatement.executeUpdate();
            if (PropertiesUtils.getSaveXMLRequestAndResponseInDB()) {
                preparedStatement = connection.prepareStatement(insertMainTableSQL);
                //preparedStatement.setString(1,"123456789123");
                preparedStatement.setString(1, chequeStatus.getMsgSeq());
                preparedStatement.setString(2, chequeStatus.getResultFromChannel().getRequestXml());
                preparedStatement.setString(3, chequeStatus.getResultFromChannel().getResponseXml());
                preparedStatement.setString(4, chequeStatus.getCallUniqID());

                preparedStatement.executeUpdate();
            }
        }catch (SQLException e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }catch (Exception e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }finally {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return true;
        }
    }
    private  boolean accountShebaID(ShebaID shebaID) throws SQLException {

        PreparedStatement preparedStatement = null;

        String insertTableSQL = "INSERT INTO TBL_LOG_ACC_GETSHEBAID"
                + "(TRANSACTIONID,ACCOUNTNO,ACTIONCODE,SHEBAID,DATETIME,CALLID) VALUES"
                + "(?,?,?,?,?,?)";
        try {

            preparedStatement = connection.prepareStatement(insertTableSQL);

            //preparedStatement.setString(1,"123456789123");
            preparedStatement.setString(2, shebaID.getAccountNumber());
            preparedStatement.setString(3, shebaID.getActionCode());
            preparedStatement.setString(4, shebaID.getShebaID());
            preparedStatement.setTimestamp(5, getCurrentTimeStamp());
            preparedStatement.setString(6, shebaID.getCallUniqID());

            preparedStatement.executeUpdate();
        }catch (SQLException e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }catch (Exception e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }finally {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return true;
        }
    }

    private  boolean accountFundTransfer(FundTransfer fundTransfer) throws SQLException {
    /*    if (PropertiesUtils.getUseOldPaymentSystem()){
            return  accountFundTransferLogToOracleOldWay(fundTransfer);
        }else {
            return  accountFundTransferLogToOracle(fundTransfer);
        }*/
       return accountFundTransferLogToOracle(fundTransfer);
    }
    private  boolean accountFundTransferLogToOracle(FundTransfer fundTransfer) throws SQLException {

        PreparedStatement preparedStatement = null;

        String insertACC_FUNDTRANSFER = "INSERT INTO TBL_LOG_ACC_FUNDTRANSFER"
                + "(TRANSACTIONID, ACC_SOURCE,ACC_DESTINATION,AMOUNT,ACTIONCODE,TRACECODE,DATETIME,CALLID) VALUES"
                + "(?,?,?,?,?,?,?,?)";
        //PreparedStatement preparedStatementMain = null;

        String insertREQUESTRESPONSE = "INSERT INTO TBL_LOG_REQUESTRESPONSE"
                + "(TRANSACTIONID, REQUEST, RESPONSE,CALLID) VALUES"
                + "(?,?,?,?)";

        try {
            if (connectedToDB()){

                preparedStatement = connection.prepareStatement(insertACC_FUNDTRANSFER);
                //preparedStatement.setString(1,"123456789123");
                preparedStatement.setString(1, fundTransfer.getMsgSeq());
                preparedStatement.setString(2, fundTransfer.getResultFromChannel().getSrcAccountNumber());
                preparedStatement.setString(3, fundTransfer.getResultFromChannel().getDstAccountNumber());
                preparedStatement.setString(4, fundTransfer.getResultFromChannel().getTransAmount());
                preparedStatement.setString(5, fundTransfer.getResultFromChannel().getAction_code());
                preparedStatement.setString(6, fundTransfer.getResultFromChannel().getRefNo());
                preparedStatement.setTimestamp(7, getCurrentTimeStamp());
                preparedStatement.setString(8, fundTransfer.getCallUniqID());

                preparedStatement.executeUpdate();
                if (PropertiesUtils.getSaveXMLRequestAndResponseInDB()) {
                    preparedStatement = connection.prepareStatement(insertREQUESTRESPONSE);
                    //preparedStatement.setString(1,"123456789123");
                    preparedStatement.setString(1, fundTransfer.getMsgSeq());
                    preparedStatement.setString(2, fundTransfer.getResultFromChannel().getRequestXml());
                    preparedStatement.setString(3, fundTransfer.getResultFromChannel().getResponseXml());
                    preparedStatement.setString(4, fundTransfer.getCallUniqID());

                    preparedStatement.executeUpdate();
                }
            }


        }catch (SQLException e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }catch (Exception e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }finally {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return accountFundTransferLogPayment(fundTransfer);
        }
    }
    private  String  getIsActiveForRetry(FundTransfer fundTransfer){
        if (fundTransfer.getTransactionDescription()!=null){
            if (fundTransfer.getTransactionDescription().equals("2")||
                    fundTransfer.getTransactionDescription().equals("3")){
                return "1";
            }else{
                return "0";
            }
        }else return "0";
    }
    private  String  getIsActiveForRetryBillPayment(BillPayByIDAccount billPayByIDAccount){
          return "hich :)";
    }
    private  String  getDoneFlag(FundTransfer fundTransfer){

       return fundTransfer.getTransactionDescription();
    }
    private  String  getDoneFlag(BillPayByIDAccount billPayByIDAccount){

        return billPayByIDAccount.getGatewayMessage();
    }
    private  String  getDoneFlag(BillPayByBillIDPan billPayByBillIDPan){
        if (billPayByBillIDPan.getActionCode().equals("0000")) return "1";
        else return "0";
    }
    private  boolean accountFundTransferLogPayment(FundTransfer fundTransfer) throws SQLException {

        PreparedStatement preparedStatement = null;

        String insertPayment= "INSERT INTO TBL_PAYMENT (" +
                "TRANSACTIONID, ISACCOUNT, ACTIONCODE,DATETIME," +
                "ISACTIVEFORRETRY, FOLLOWCODE,TRANCEDATE, TRANSTIME," +
                "SOURCEOFPAYMENT, CALLID," +
                "BILLID, PAYMENTID, AMOUNT,ISFUNDTRUSFER," +
                "ISFUNDTRANSFERWITHID, ISBILLPAYMENTOFACCOUNT, ISINSTALLMENTPAYMENT,ISBILLPAYMENTOFPAN," +
                "DATEOFTRANSACTION, DONEFLAG,DESTINATIONACCOUNT,TRANFERID"+") VALUES"
                + "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            if (connectedToDB()){

                preparedStatement = connection.prepareStatement(insertPayment);

                preparedStatement.setString(1, fundTransfer.getMsgSeq());
                preparedStatement.setString(2, "1");
                preparedStatement.setString(3, fundTransfer.getResultFromChannel().getAction_code());
                preparedStatement.setTimestamp(4,getCurrentTimeStamp());
                preparedStatement.setString(5, getIsActiveForRetry(fundTransfer));
                preparedStatement.setString(6, fundTransfer.getTraceID());
                preparedStatement.setString(7, fundTransfer.getResultFromChannel().getTxDate());
                preparedStatement.setString(8, fundTransfer.getResultFromChannel().getTxTime());
                preparedStatement.setString(9, fundTransfer.getSourceAccount());
                preparedStatement.setString(10, fundTransfer.getCallUniqID());
                preparedStatement.setString(11, "0");
                preparedStatement.setString(12, "0");
                preparedStatement.setString(13, fundTransfer.getResultFromChannel().getTransAmount());
                if (fundTransfer.getIsFundTransfer()) preparedStatement.setString(14, "1");
                else preparedStatement.setString(14, "0");
                if (fundTransfer.getIsIdentFundTranfer()) preparedStatement.setString(15, "1");
                else preparedStatement.setString(15, "0");
                preparedStatement.setString(16, "0");
                if (fundTransfer.getIsInstallmentPay()) preparedStatement.setString(17, "1");
                else preparedStatement.setString(17, "0");
                preparedStatement.setString(18, "0");
                preparedStatement.setDate(19, getCurrentDate());
                preparedStatement.setString(20, getDoneFlag(fundTransfer));
                preparedStatement.setString(21, fundTransfer.getDestinationAccount());

                if (fundTransfer.getIsIdentFundTranfer())
                    preparedStatement.setString(22, fundTransfer.getTransferID());
                else
                    preparedStatement.setString(22, "0");


                preparedStatement.executeUpdate();
            }

        }catch (SQLException e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }catch (Exception e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }finally {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return true;
        }
    }
    private  boolean accountBillPaymentLogPayment(BillPayByIDAccount billPayByIDAccount) throws SQLException {

        PreparedStatement preparedStatement = null;

        String insertPayment= "INSERT INTO TBL_PAYMENT (" +
                "TRANSACTIONID, ISACCOUNT, ACTIONCODE,DATETIME," +
                "ISACTIVEFORRETRY, FOLLOWCODE,TRANCEDATE, TRANSTIME," +
                "SOURCEOFPAYMENT, CALLID," +
                "BILLID, PAYMENTID, AMOUNT,ISFUNDTRUSFER," +
                "ISFUNDTRANSFERWITHID, ISBILLPAYMENTOFACCOUNT, ISINSTALLMENTPAYMENT,ISBILLPAYMENTOFPAN," +
                "DATEOFTRANSACTION, DONEFLAG,DESTINATIONACCOUNT,TRANFERID"+") VALUES"
                + "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            if (connectedToDB()){

                preparedStatement = connection.prepareStatement(insertPayment);

                preparedStatement.setString(1, billPayByIDAccount.getMsgSeq());
                preparedStatement.setString(2, "1");
                preparedStatement.setString(3, billPayByIDAccount.getResultFromChannel().getAction_code());
                preparedStatement.setTimestamp(4,getCurrentTimeStamp());
                preparedStatement.setString(5, "0");
                preparedStatement.setString(6, billPayByIDAccount.getTraceCode());
                preparedStatement.setString(7, billPayByIDAccount.getResultFromChannel().getTxDate());
                preparedStatement.setString(8, billPayByIDAccount.getResultFromChannel().getTxTime());
                preparedStatement.setString(9, billPayByIDAccount.getSourceAccount());
                preparedStatement.setString(10, billPayByIDAccount.getCallUniqID());
                preparedStatement.setString(11, billPayByIDAccount.getBillID());
                preparedStatement.setString(12, billPayByIDAccount.getPaymentID());
                preparedStatement.setString(13, billPayByIDAccount.getAmount());
                preparedStatement.setString(14, "0");
                preparedStatement.setString(15, "0");
                preparedStatement.setString(16, "1");
                preparedStatement.setString(17, "0");
                preparedStatement.setString(18, "0");
                preparedStatement.setDate(19, getCurrentDate());
                preparedStatement.setString(20, getDoneFlag(billPayByIDAccount));
                preparedStatement.setString(21, "0");
                preparedStatement.setString(22, "0");

                preparedStatement.executeUpdate();
            }

        }catch (SQLException e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }catch (Exception e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }finally {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return true;
        }
    }
    private  boolean panBillPaymentLogPayment(BillPayByBillIDPan billPayByBillIDPan) throws SQLException {

        PreparedStatement preparedStatement = null;

        String insertPayment= "INSERT INTO TBL_PAYMENT (" +
                "TRANSACTIONID, ISACCOUNT, ACTIONCODE,DATETIME," +
                "ISACTIVEFORRETRY, FOLLOWCODE,TRANCEDATE, TRANSTIME," +
                "SOURCEOFPAYMENT, CALLID," +
                "BILLID, PAYMENTID, AMOUNT,ISFUNDTRUSFER," +
                "ISFUNDTRANSFERWITHID, ISBILLPAYMENTOFACCOUNT, ISINSTALLMENTPAYMENT,ISBILLPAYMENTOFPAN," +
                "DATEOFTRANSACTION, DONEFLAG,DESTINATIONACCOUNT,TRANFERID"+") VALUES"
                + "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            if (connectedToDB()){

                preparedStatement = connection.prepareStatement(insertPayment);

                preparedStatement.setString(1, billPayByBillIDPan.getMsgSeq());
                preparedStatement.setString(2, "0");
                preparedStatement.setString(3, billPayByBillIDPan.getActionCode());
                preparedStatement.setTimestamp(4,getCurrentTimeStamp());
                preparedStatement.setString(5, "0");
                preparedStatement.setString(6, billPayByBillIDPan.getTraceCode());
                preparedStatement.setString(7, getCurrentShamsiDate());
                preparedStatement.setString(8, getCurrentTime());
                preparedStatement.setString(9, billPayByBillIDPan.getPan());
                preparedStatement.setString(10, billPayByBillIDPan.getCallUniqID());
                preparedStatement.setString(11, billPayByBillIDPan.getBillID());
                preparedStatement.setString(12, billPayByBillIDPan.getPaymentID());
                preparedStatement.setString(13, billPayByBillIDPan.getAmount());
                preparedStatement.setString(14, "0");
                preparedStatement.setString(15, "0");
                preparedStatement.setString(16, "0");
                preparedStatement.setString(17, "0");
                preparedStatement.setString(18, "1");
                preparedStatement.setDate(19, getCurrentDate());
                preparedStatement.setString(20, getDoneFlag(billPayByBillIDPan));
                preparedStatement.setString(21, "0");
                preparedStatement.setString(22, "0");

                preparedStatement.executeUpdate();
            }

        }catch (SQLException e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }catch (Exception e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }finally {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return true;
        }
    }

    private  boolean accountFundTransferLogToOracleOldWay(FundTransfer fundTransfer) throws SQLException {

        PreparedStatement preparedStatement = null;

        String insertTableSQL = "INSERT INTO TBL_LOG_ACC_FUNDTRANSFER"
                + "(TRANSACTIONID, ACC_SOURCE,ACC_DESTINATION,AMOUNT,ACTIONCODE,TRACECODE,DATETIME,CALLID) VALUES"
                + "(?,?,?,?,?,?,?,?)";
        //PreparedStatement preparedStatementMain = null;


        try {

            preparedStatement = connection.prepareStatement(insertTableSQL);

            //preparedStatement.setString(1,"123456789123");
            preparedStatement.setString(1, fundTransfer.getMsgSeq());
            preparedStatement.setString(2, fundTransfer.getResultFromChannel().getSrcAccountNumber());
            preparedStatement.setString(3, fundTransfer.getResultFromChannel().getDstAccountNumber());
            preparedStatement.setString(4, fundTransfer.getResultFromChannel().getTransAmount());
            preparedStatement.setString(5, fundTransfer.getResultFromChannel().getAction_code());
            preparedStatement.setString(6, fundTransfer.getResultFromChannel().getRefNo());
            preparedStatement.setTimestamp(7, getCurrentTimeStamp());
            preparedStatement.setString(8, fundTransfer.getCallUniqID());

            preparedStatement.executeUpdate();

        }catch (SQLException e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }catch (Exception e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }finally {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return true;
        }
    }

    private  boolean accountBillPaymentByBillID(BillPayByIDAccount billPayByIDAccount) throws SQLException {
      /*  if (PropertiesUtils.getUseOldPaymentSystem()){
            return  accountBillPaymentByBillIDToOracleOldWay(billPayByIDAccount);
        }else {*/
            return  accountBillPaymentByBillIDToOracle(billPayByIDAccount);
//        }
    }
    private  boolean panBillPaymentByBillID(BillPayByBillIDPan billPayByBillIDPan) throws SQLException {

            return  panBillPaymentByBillIDToOracle(billPayByBillIDPan);
    }


    private  boolean accountBillPaymentByBillIDToOracle(BillPayByIDAccount billPayByIDAccount) throws SQLException {

        PreparedStatement preparedStatement = null;

        String insertTableSQL = "INSERT INTO TBL_LOG_ACC_BILLPAYBYID"
                + "(TRANSACTIONID,SOURCEACCNO,AMOUNT," +
                "BILLID,PAYID,SERVICECODE,COMPANYCODE," +
                "FOLLOWCODE,DATETIME,ACTIONCODE,CALLID) VALUES"
                + "(?,?,?,?,?,?,?,?,?,?,?)";
        //PreparedStatement preparedStatementMain = null;

        String insertMainTableSQL = "INSERT INTO TBL_LOG_REQUESTRESPONSE"
                + "(TRANSACTIONID, REQUEST, RESPONSE,CALLID) VALUES"
                + "(?,?,?,?)";
        try {

            preparedStatement = connection.prepareStatement(insertTableSQL);

            //preparedStatement.setString(1,"123456789123");
            preparedStatement.setString(1, billPayByIDAccount.getMsgSeq());
            preparedStatement.setString(2, billPayByIDAccount.getSourceAccount());
            preparedStatement.setString(3, billPayByIDAccount.getAmount());
            preparedStatement.setString(4, billPayByIDAccount.getBillID());
            preparedStatement.setString(5, billPayByIDAccount.getPaymentID());
            preparedStatement.setString(6, billPayByIDAccount.getResultFromChannel().getServiceCode());
            preparedStatement.setString(7, billPayByIDAccount.getResultFromChannel().getCompany());
            preparedStatement.setString(8, billPayByIDAccount.getResultFromChannel().getRefNo());
            preparedStatement.setTimestamp(9, getCurrentTimeStamp());
            preparedStatement.setString(10, billPayByIDAccount.getResultFromChannel().getAction_code());
            preparedStatement.setString(11, billPayByIDAccount.getCallUniqID());

            preparedStatement.executeUpdate();
            if (PropertiesUtils.getSaveXMLRequestAndResponseInDB()) {
                preparedStatement = connection.prepareStatement(insertMainTableSQL);
                //preparedStatement.setString(1,"123456789123");
                preparedStatement.setString(1, billPayByIDAccount.getMsgSeq());
                preparedStatement.setString(2, billPayByIDAccount.getResultFromChannel().getRequestXml());
                preparedStatement.setString(3, billPayByIDAccount.getResultFromChannel().getResponseXml());
                preparedStatement.setString(4, billPayByIDAccount.getCallUniqID());

                preparedStatement.executeUpdate();
            }

        }catch (SQLException e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }catch (Exception e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }finally {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return accountBillPaymentLogPayment(billPayByIDAccount);
        }
    }
    private  boolean panBillPaymentByBillIDToOracle(BillPayByBillIDPan billPayByBillIDPan) throws SQLException {

        PreparedStatement preparedStatement = null;

        String insertTableSQL = "INSERT INTO TBL_LOG_PAN_BILLPAY"
                + "(TRANSACTIONID,PAN,BILLID," +
                "PAYID,PHONENO,ACTIONCODE,TRACECODE,CALLID,AMOUNT,DATETIME) VALUES"
                + "(?,?,?,?,?,?,?,?,?,?)";
        //PreparedStatement preparedStatementMain = null;


        try {

            preparedStatement = connection.prepareStatement(insertTableSQL);

            //preparedStatement.setString(1,"123456789123");
            preparedStatement.setString(1, billPayByBillIDPan.getMsgSeq());
            preparedStatement.setString(2, billPayByBillIDPan.getPan());
            preparedStatement.setString(3, billPayByBillIDPan.getBillID());
            preparedStatement.setString(4, billPayByBillIDPan.getPaymentID());
            preparedStatement.setString(5, "");
            preparedStatement.setString(6, billPayByBillIDPan.getActionCode());
            preparedStatement.setString(7, billPayByBillIDPan.getTraceCode());
            preparedStatement.setString(8, billPayByBillIDPan.getCallUniqID());
            preparedStatement.setString(9, billPayByBillIDPan.getAmount());
            preparedStatement.setTimestamp(10, getCurrentTimeStamp());

            preparedStatement.executeUpdate();

        }catch (SQLException e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }catch (Exception e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }finally {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return panBillPaymentLogPayment(billPayByBillIDPan);
        }
    }
    private  boolean accountBillPaymentByBillIDToOracleOldWay(BillPayByIDAccount billPayByIDAccount) throws SQLException {

        PreparedStatement preparedStatement = null;

        String insertTableSQL = "INSERT INTO TBL_LOG_ACC_BILLPAYBYID"
                + "(TRANSACTIONID,SOURCEACCNO,AMOUNT,BILLID,PAYID,SERVICECODE,COMPANYCODE,FOLLOWCODE,DATETIME,ACTIONCODE,CALLID) VALUES"
                + "(?,?,?,?,?,?,?,?,?,?,?)";


        try {

            preparedStatement = connection.prepareStatement(insertTableSQL);

            //preparedStatement.setString(1,"123456789123");
            preparedStatement.setString(1, billPayByIDAccount.getMsgSeq());
            preparedStatement.setString(2, billPayByIDAccount.getSourceAccount());
            preparedStatement.setString(3, billPayByIDAccount.getAmount());
            preparedStatement.setString(4, billPayByIDAccount.getBillID());
            preparedStatement.setString(5, billPayByIDAccount.getPaymentID());
            preparedStatement.setString(6, "");
            preparedStatement.setString(7, "");
            preparedStatement.setString(8, "");
            preparedStatement.setTimestamp(9, getCurrentTimeStamp());
            preparedStatement.setString(10, billPayByIDAccount.getActionCode());
            preparedStatement.setString(11, billPayByIDAccount.getCallUniqID());

            preparedStatement.executeUpdate();



        }catch (SQLException e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }catch (Exception e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }finally {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return true;
        }
    }

    private  boolean accountFundTransferLogToSQLServer(FundTransfer fundTransfer) throws SQLException {

        PreparedStatement preparedStatement = null;

        String insertTableSQL = "INSERT INTO TBL_LOG_ACC_FUNDTRANSFER"
                + "(TRANSACTIONID, ACC_SOURCE,ACC_DESTINATION,AMOUNT,ACTIONCODE,TRACECODE,DATETIME) VALUES"
                + "(?,?,?,?,?,?,?)";
        //PreparedStatement preparedStatementMain = null;

        String insertMainTableSQL = "INSERT INTO TBL_LOG_REQUESTRESPONSE"
                + "(TRANSACTIONID, REQUEST, RESPONSE) VALUES"
                + "(?,?,?)";
        try {

            preparedStatement = connection.prepareStatement(insertTableSQL);

            //preparedStatement.setString(1,"123456789123");
            preparedStatement.setString(1, fundTransfer.getMsgSeq());
            preparedStatement.setString(2, fundTransfer.getResultFromChannel().getSrcAccountNumber());
            preparedStatement.setString(3, fundTransfer.getResultFromChannel().getDstAccountNumber());
            preparedStatement.setString(4, fundTransfer.getResultFromChannel().getTransAmount());
            preparedStatement.setString(5, fundTransfer.getResultFromChannel().getAction_code());
            preparedStatement.setString(6, fundTransfer.getResultFromChannel().getRefNo());
            preparedStatement.setTimestamp(7, getCurrentTimeStamp());

            preparedStatement.executeUpdate();

            preparedStatement = connection.prepareStatement(insertMainTableSQL);
            //preparedStatement.setString(1,"123456789123");
            preparedStatement.setString(1, fundTransfer.getMsgSeq());
            preparedStatement.setString(2, fundTransfer.getResultFromChannel().getRequestXml());
            preparedStatement.setString(3, fundTransfer.getResultFromChannel().getResponseXml());

            preparedStatement.executeUpdate();

        }catch (SQLException e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }catch (Exception e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }finally {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return true; //logPayment(fundTransfer);
        }
    }
    private  boolean logPayment(Object object) throws SQLException {

        PaymentOBJ payment=new PaymentOBJ();
        payment.setISACCOUNT("1");
        payment.setDATETIME(getCurrentTimeStamp());

        if (object instanceof BillPayByIDAccount){
            BillPayByIDAccount billPayByIDAccount= (BillPayByIDAccount)object;
            payment.setTRANSACTIONID(billPayByIDAccount.getMsgSeq());
            payment.setACTIONCODE(billPayByIDAccount.getActionCode());
            payment.setTRANCEDATE(billPayByIDAccount.getResultFromChannel().getTxDate());
            payment.setTRANSTIME(billPayByIDAccount.getResultFromChannel().getTxTime());
            payment.setFOLLOWCODE(billPayByIDAccount.getTraceCode());
            payment.setSOURCEOFPAYMENT(billPayByIDAccount.getSourceAccount());
            payment.setCALLID(billPayByIDAccount.getCallUniqID());
        }else if (object instanceof BillPayByBillIDPan){
            BillPayByBillIDPan billPayByBillIDPan= (BillPayByBillIDPan)object;
            payment.setTRANSACTIONID(billPayByBillIDPan.getResultFromServer().getReferenceCode());
            payment.setACTIONCODE(billPayByBillIDPan.getActionCode());
            payment.setTRANCEDATE(billPayByBillIDPan.getResultFromServer().getPersianDate());
            payment.setTRANSTIME(billPayByBillIDPan.getResultFromServer().getPersianTime());
            payment.setFOLLOWCODE(billPayByBillIDPan.getResultFromServer().getTraceCode());
            payment.setCALLID(billPayByBillIDPan.getCallUniqID());
        }else if (object instanceof FundTransfer){
            FundTransfer fundTransfer= (FundTransfer)object;
            payment.setTRANSACTIONID(fundTransfer.getMsgSeq());
            payment.setACTIONCODE(fundTransfer.getActionCode());
            payment.setTRANCEDATE(fundTransfer.getResultFromChannel().getTxDate());
            payment.setTRANSTIME(fundTransfer.getResultFromChannel().getTxTime());
            payment.setFOLLOWCODE(fundTransfer.getTraceID());
            payment.setSOURCEOFPAYMENT(fundTransfer.getSourceAccount());
            payment.setCALLID(fundTransfer.getCallUniqID());
        }else if (object instanceof BillPayByBillIDPan){
            BillPayByBillIDPan billPayByBillIDPan= (BillPayByBillIDPan)object;
            payment.setTRANSACTIONID(billPayByBillIDPan.getResultFromServer().getReferenceCode());
            payment.setACTIONCODE(billPayByBillIDPan.getActionCode());
            payment.setCALLID(billPayByBillIDPan.getCallUniqID());
            // payment.setTRANCEDATE(billPayByBillIDPan.getResultFromCM().getTxDate());
            // payment.setTRANSTIME(billPayByBillIDPan.getResultFromCM().getTxTime());
            // payment.setFOLLOWCODE(billPayByBillIDPan.getTraceCode());
        }
        return paymentObjectLogToDB(payment);
    }
    private  boolean paymentObjectLogToDB(PaymentOBJ payment){
        PreparedStatement preparedStatement = null;

        String insertTableSQL = "INSERT INTO TBL_PAYMENT"
                + "(TRANSACTIONID,ISACCOUNT,ACTIONCODE,DATETIME,ISACTIVEFORRETRY,FOLLOWCODE,SOURCEOFPAYMENT,CALLID) VALUES"
                + "(?,?,?,?,?,?,?,?)";
        try {

            preparedStatement = connection.prepareStatement(insertTableSQL);

            preparedStatement.setString(1, payment.getTRANSACTIONID());
            preparedStatement.setString(2, payment.getISACCOUNT());
            preparedStatement.setString(3, payment.getACTIONCODE());
            preparedStatement.setTimestamp(4, payment.getDATETIME());
            preparedStatement.setString(5, payment.getISACTIVEFORRETRY());
            preparedStatement.setString(6, payment.getFOLLOWCODE());
            preparedStatement.setString(7, payment.getSOURCEOFPAYMENT());
            preparedStatement.setString(8, payment.getCALLID());

            preparedStatement.executeUpdate();
        }catch (SQLException e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }catch (Exception e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }finally {
            if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
            if (connection != null) {try {connection.close();connection = null;} catch (Exception var1) {}}
            return true;
        }
    }

    private  boolean cardSavePendingAndRequest(ISO200 ISO200){

            PreparedStatement preparedStatement = null;

            String insertTablePending = "INSERT INTO TBL_PAN_PENDING"
                    +  "(SWITCHMESSAGESEQUENCE, MESSAGETYPE, PAN,TRANSACTIONISOCOD,"
                    +  "SENDDATETIMETOSWITCH,TRACEID,REGISTERTIME,REGISTERDATE,BILLID,PAYMENTID,DATETIME,RESPONCEFLAG) VALUES"
                    +  "(?,?,?,?,?,?,?,?,?,?,?,?)";

            String insertTableRequest = "INSERT INTO TBL_PAN_REQUEST"
                + "(SWITCHMESSAGESEQUENCE, MESSAGETYPE, PAN,TRANSACTIONISOCOD,"
                +  "SENDDATETIMETOSWITCH,TRACEID,REGISTERTIME,REGISTERDATE,BILLID,PAYMENTID,DATETIME,RESULT) VALUES"
                + "(?,?,?,?,?,?,?,?,?,?,?,?)";

            try {
                Timestamp d=getCurrentTimeStamp();
                preparedStatement = connection.prepareStatement(insertTablePending);

                preparedStatement.setString(1, ISO200.getReferenceCode());
                preparedStatement.setString(2, ISO200.getMessageType());
                preparedStatement.setString(3, ISO200.getPan());
                preparedStatement.setString(4, ISO200.getTransactionISOCode());
                preparedStatement.setString(5, ISO200.getSendDateTimeToSwitch());
                preparedStatement.setString(6, ISO200.getTraceCode());
                preparedStatement.setString(7, ISO200.getRegisterTime());
                preparedStatement.setString(8, ISO200.getRegisterDate());
                preparedStatement.setString(9, ISO200.getBillID());
                preparedStatement.setString(10,ISO200.getPayID());
                preparedStatement.setTimestamp(11,d );
                preparedStatement.setInt(12, 0);

                preparedStatement.executeUpdate();
                Thread.sleep(777);
                preparedStatement = connection.prepareStatement(insertTableRequest);

                preparedStatement.setString(1, ISO200.getReferenceCode());
                preparedStatement.setString(2, ISO200.getMessageType());
                preparedStatement.setString(3, ISO200.getPan());
                preparedStatement.setString(4, ISO200.getTransactionISOCode());
                preparedStatement.setString(5, ISO200.getSendDateTimeToSwitch());
                preparedStatement.setString(6, ISO200.getTraceCode());
                preparedStatement.setString(7, ISO200.getRegisterTime());
                preparedStatement.setString(8, ISO200.getRegisterDate());
                preparedStatement.setString(9, ISO200.getBillID());
                preparedStatement.setString(10, ISO200.getPayID());
                preparedStatement.setTimestamp(11, d);
                preparedStatement.setInt(12, 0);

                preparedStatement.executeUpdate();

            }catch (SQLException e) {
                if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
                if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
                return false;
            }catch (Exception e) {
                if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
                if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
                return false;
            }finally {
                if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
                if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
                return true;
            }
    }
    private  boolean cardSaveResponse(ISO210 ISO210){

        PreparedStatement preparedStatement = null;
        Statement st =null;

        String insertTableResponse = "INSERT INTO TBL_PAN_RESPONSE"
                +  "(SWITCHMESSAGESEQUENCE, PAN, TRANSACTIONISOCODE,AMOUNT,"
                +  "SENDDATETIMETOSWITCH,TRACECODE,REGISTERTIME,REGISTERDATE,PROCESSDATETIMEINDESTINATION,"
                +  "RESPONSEANDLICENSETRANSACTION,RESPONSETRANSACTIONCODE,MOREINFORMATION,DATETIME,RECEIVESTRING) VALUES"
                +  "(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            Timestamp d=getCurrentTimeStamp();
            preparedStatement = connection.prepareStatement(insertTableResponse);

            preparedStatement.setString(1, ISO210.getReferenceCode());
            preparedStatement.setString(2, ISO210.getPan());
            preparedStatement.setString(3, ISO210.getTransactionISOCode());
            preparedStatement.setString(4, ISO210.getAmountCurrencyCardIssuer());
            preparedStatement.setString(5, ISO210.getSendDateTimeToSwitch());
            preparedStatement.setString(6, ISO210.getTraceCode());
            preparedStatement.setString(7, ISO210.getRegisterTime());
            preparedStatement.setString(8, ISO210.getRegisterDate());
            preparedStatement.setString(9, ISO210.getProcessDateTimeInDestination());
            preparedStatement.setString(10, ISO210.getResponseAndLicenseTransaction());
            preparedStatement.setString(11, ISO210.getResponseTransactionCode());
            preparedStatement.setString(12, ISO210.getAdditionalData());
            preparedStatement.setTimestamp(13,d );
            preparedStatement.setString(14,ISO210.getReceivedString() );

            preparedStatement.executeUpdate();
            Thread.sleep(777);
            st=null;
            st=connection.createStatement();
            st.executeUpdate( "UPDATE TBL_PAN_PENDING SET"
                             + " RESPONCEFLAG=1"
                             + " where SWITCHMESSAGESEQUENCE='" + ISO210.getReferenceCode()+"'" );

        }catch (SQLException e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            if (st != null) {try{st.close();st=null;}catch (Exception var1){}}
            return false;
        }catch (Exception e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            if (st != null) {try{st.close();st=null;}catch (Exception var1){}}

            return false;
        }finally {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            if (st != null) {try{st.close();st=null;}catch (Exception var1){}}
            return true;
        }
    }

    private  boolean cardSaveRequestReverse(ISO400 ISO400){

        PreparedStatement preparedStatement = null;

        String insertTableRequest = "INSERT INTO TBL_PAN_REVERSE_REQUEST"
                +  "(SWITCHMESSAGESEQUENCE, PAN,AMOUNT,"
                +  "SENDDATETIMETOSWITCH,TRACECODE,REGISTERTIME,REGISTERDATE,ADDITIONALDATA,DATETIME) VALUES"
                +  "(?,?,?,?,?,?,?,?,?)";
        try {
            Timestamp d=getCurrentTimeStamp();
            preparedStatement = connection.prepareStatement(insertTableRequest);

            preparedStatement.setString(1, ISO400.getReferenceCode());
            preparedStatement.setString(2, ISO400.getPan());
            preparedStatement.setString(3, ISO400.getAmountCurrencyCardIssuer());
            preparedStatement.setString(4, ISO400.getSendDateTimeToSwitch());
            preparedStatement.setString(5, ISO400.getTraceCode());
            preparedStatement.setString(6, ISO400.getRegisterTime());
            preparedStatement.setString(7, ISO400.getRegisterDate());
            preparedStatement.setString(8, ISO400.getAdditionalData());
          //  preparedStatement.setString(9, ISO400.getEncryptedRequest());
            preparedStatement.setTimestamp(9,d );

            preparedStatement.executeUpdate();
        }catch (SQLException e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }catch (Exception e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }finally {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return true;
        }
    }
    private  boolean cardSaveResponceReverse(ISO430 ISO430){

        PreparedStatement preparedStatement = null;

        String insertTableRequest = "INSERT INTO TBL_PAN_REVERSE_RESPONSE"
                +  "(SWITCHMESSAGESEQUENCE, PAN,SENDDATETIMETOSWITCH,TRACECODE,"
                +  "REGISTERTIME,REGISTERDATE,RESPONSETRANSACTIONCODE,ADDITIONALDATA,RECEIVESTRING,DATETIME) VALUES"
                +  "(?,?,?,?,?,?,?,?,?,?)";
        try {
            Timestamp d=getCurrentTimeStamp();
            preparedStatement = connection.prepareStatement(insertTableRequest);

            preparedStatement.setString(1, ISO430.getReferenceCode());
            preparedStatement.setString(2, ISO430.getPan());
            preparedStatement.setString(3, ISO430.getSendDateTimeToSwitch());
            preparedStatement.setString(4, ISO430.getTraceCode());
            preparedStatement.setString(5, ISO430.getRegisterTime());
            preparedStatement.setString(6, ISO430.getRegisterDate());
            preparedStatement.setString(7, ISO430.getResponseTransactionCode());
            preparedStatement.setString(8, ISO430.getAdditionalData());
            preparedStatement.setString(9, ISO430.getReceivedString());
            preparedStatement.setTimestamp(10,d );

            preparedStatement.executeUpdate();
        }catch (SQLException e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }catch (Exception e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }finally {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return true;
        }
    }

    private  boolean TelSwitchSavePendingAndRequest(ISO100 ISO100){

        PreparedStatement preparedStatement = null;

        String insertTablePending = "INSERT INTO TBL_TELSWITCH_PENDING"
                + "(SWITCHMESSAGESEQUENCE, MESSAGETYPE, PAN,TRANSACTIONISOCOD,"
                +  "SENDDATETIMETOSWITCH,TRACEID,REGISTERTIME,REGISTERDATE,BILLID,PAYMENTID,TELNUMBER,DATETIME,RESPONCEFLAG) VALUES"
                + "(?,?,?,?,?,?,?,?,?,?,?,?,?)";

        String insertTableRequest = "INSERT INTO TBL_TELSWITCH_REQUEST"
                + "(SWITCHMESSAGESEQUENCE, MESSAGETYPE, PAN,TRANSACTIONISOCOD,"
                +  "SENDDATETIMETOSWITCH,TRACEID,REGISTERTIME,REGISTERDATE,BILLID,PAYMENTID,TELNUMBER,DATETIME,RESULT) VALUES"
                + "(?,?,?,?,?,?,?,?,?,?,?,?,?)";


        try {
            Timestamp d=getCurrentTimeStamp();
            preparedStatement = connection.prepareStatement(insertTablePending);

            preparedStatement.setString(1, ISO100.getReferenceCode());
            preparedStatement.setString(2, ISO100.getMessageType());
            preparedStatement.setString(3, ISO100.getPan());
            preparedStatement.setString(4, ISO100.getTransactionISOCode());
            preparedStatement.setString(5, ISO100.getSendDateTimeToSwitch());
            preparedStatement.setString(6, ISO100.getTraceCode());
            preparedStatement.setString(7, ISO100.getRegisterTime());
            preparedStatement.setString(8, ISO100.getRegisterDate());
            preparedStatement.setString(9, ISO100.getBillID());
            preparedStatement.setString(10,ISO100.getPayID());
            preparedStatement.setString(11,ISO100.getTelNo());
            preparedStatement.setTimestamp(12,d );
            preparedStatement.setInt(13, 0);

            preparedStatement.executeUpdate();
            Thread.sleep(700);

            preparedStatement = connection.prepareStatement(insertTableRequest);

            preparedStatement.setString(1, ISO100.getReferenceCode());
            preparedStatement.setString(2, ISO100.getMessageType());
            preparedStatement.setString(3, ISO100.getPan());
            preparedStatement.setString(4, ISO100.getTransactionISOCode());
            preparedStatement.setString(5, ISO100.getSendDateTimeToSwitch());
            preparedStatement.setString(6, ISO100.getTraceCode());
            preparedStatement.setString(7, ISO100.getRegisterTime());
            preparedStatement.setString(8, ISO100.getRegisterDate());
            preparedStatement.setString(9, ISO100.getBillID());
            preparedStatement.setString(10, ISO100.getPayID());
            preparedStatement.setString(11,ISO100.getTelNo());
            preparedStatement.setTimestamp(12,d );
            preparedStatement.setInt(13, 0);

            preparedStatement.executeUpdate();


        }catch (SQLException e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }catch (Exception e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }finally {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return true;
        }
    }
    private  boolean TelSwitchSaveResponse(ISO110 ISO110){

        PreparedStatement preparedStatement = null;

        String insertTableResponse = "INSERT INTO TBL_TELSWITCH_RESPONSE"
                +  "(SWITCHMESSAGESEQUENCE, PAN, TRANSACTIONISOCODE,"
                +  "SENDDATETIMETOSWITCH,TRACECODE,REGISTERTIME,REGISTERDATE,CHECKOUTDATE,"
                +  "RESULT,BILLID,PAYID,AMOUNT,DATETIME,DEADLINE,RCVSTRING) VALUES"
                +  "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            Timestamp d=getCurrentTimeStamp();
            preparedStatement = connection.prepareStatement(insertTableResponse);

            preparedStatement.setString(1, ISO110.getReferenceCode());
            preparedStatement.setString(2, ISO110.getPan());
            preparedStatement.setString(3, ISO110.getTransactionISOCode());
            preparedStatement.setString(4, ISO110.getSendDateTimeToSwitch());
            preparedStatement.setString(5, ISO110.getTraceCode());
            preparedStatement.setString(6, ISO110.getRegisterTime());
            preparedStatement.setString(7, ISO110.getRegisterDate());
            preparedStatement.setString(8, ISO110.getCheckoutDate());
            preparedStatement.setString(9, ISO110.getResponseTransactionCode());
            preparedStatement.setString(10, ISO110.getBillID());
            preparedStatement.setString(11, ISO110.getPayID());
            preparedStatement.setString(12, ISO110.getAmount());
            preparedStatement.setTimestamp(13,d );
            preparedStatement.setString(14, ISO110.getDeadLine());
            preparedStatement.setString(15, ISO110.getReceivedString());

            preparedStatement.executeUpdate();

            Thread.sleep(777);
            Statement st=connection.createStatement();;

            st.executeUpdate( "UPDATE TBL_TELSWITCH_PENDING SET"
                    + " RESPONCEFLAG=1"
                    + " where SWITCHMESSAGESEQUENCE='" + ISO110.getReferenceCode()+"'" );
        }catch (SQLException e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }catch (Exception e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }finally {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return true;
        }
    }

    private  boolean cardSavePendingAndRequest(ServiceObjects.ISOShetabVer7.ISO200 ISO200){

        PreparedStatement preparedStatement = null;

        String insertTablePending = "INSERT INTO TBL_PAN_PENDING"
                +  "(SWITCHMESSAGESEQUENCE, MESSAGETYPE, PAN,TRANSACTIONISOCOD,"
                +  "SENDDATETIMETOSWITCH,TRACEID,REGISTERTIME,REGISTERDATE,BILLID,PAYMENTID,DATETIME,RESPONCEFLAG) VALUES"
                +  "(?,?,?,?,?,?,?,?,?,?,?,?)";

        String insertTableRequest = "INSERT INTO TBL_PAN_REQUEST"
                + "(SWITCHMESSAGESEQUENCE, MESSAGETYPE, PAN,TRANSACTIONISOCOD,"
                +  "SENDDATETIMETOSWITCH,TRACEID,REGISTERTIME,REGISTERDATE,BILLID,PAYMENTID,DATETIME,RESULT) VALUES"
                + "(?,?,?,?,?,?,?,?,?,?,?,?)";

        try {
            Timestamp d=getCurrentTimeStamp();
            preparedStatement = connection.prepareStatement(insertTablePending);

            preparedStatement.setString(1, ISO200.getReferenceCode());
            preparedStatement.setString(2, ISO200.getMessageType());
            preparedStatement.setString(3, ISO200.getPan());
            preparedStatement.setString(4, ISO200.getTransactionISOCode());
            preparedStatement.setString(5, ISO200.getSendDateTimeToSwitch());
            preparedStatement.setString(6, ISO200.getTraceCode());
            preparedStatement.setString(7, ISO200.getRegisterTime());
            preparedStatement.setString(8, ISO200.getRegisterDate());
            preparedStatement.setString(9, ISO200.getBillID());
            preparedStatement.setString(10,ISO200.getPayID());
            preparedStatement.setTimestamp(11,d );
            preparedStatement.setInt(12, 0);

            preparedStatement.executeUpdate();
            Thread.sleep(777);
            preparedStatement = connection.prepareStatement(insertTableRequest);

            preparedStatement.setString(1, ISO200.getReferenceCode());
            preparedStatement.setString(2, ISO200.getMessageType());
            preparedStatement.setString(3, ISO200.getPan());
            preparedStatement.setString(4, ISO200.getTransactionISOCode());
            preparedStatement.setString(5, ISO200.getSendDateTimeToSwitch());
            preparedStatement.setString(6, ISO200.getTraceCode());
            preparedStatement.setString(7, ISO200.getRegisterTime());
            preparedStatement.setString(8, ISO200.getRegisterDate());
            preparedStatement.setString(9, ISO200.getBillID());
            preparedStatement.setString(10, ISO200.getPayID());
            preparedStatement.setTimestamp(11, d);
            preparedStatement.setInt(12, 0);

            preparedStatement.executeUpdate();

        }catch (SQLException e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }catch (Exception e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }finally {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return true;
        }
    }
    private  boolean cardSaveResponse(ServiceObjects.ISOShetabVer7.ISO210 ISO210){

        PreparedStatement preparedStatement = null;
        Statement st =null;

        String insertTableResponse = "INSERT INTO TBL_PAN_RESPONSE"
                +  "(SWITCHMESSAGESEQUENCE, PAN, TRANSACTIONISOCODE,AMOUNT,"
                +  "SENDDATETIMETOSWITCH,TRACECODE,REGISTERTIME,REGISTERDATE,PROCESSDATETIMEINDESTINATION,"
                +  "RESPONSEANDLICENSETRANSACTION,RESPONSETRANSACTIONCODE,MOREINFORMATION,DATETIME,RECEIVESTRING) VALUES"
                +  "(?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            Timestamp d=getCurrentTimeStamp();
            preparedStatement = connection.prepareStatement(insertTableResponse);

            preparedStatement.setString(1, ISO210.getReferenceCode());
            preparedStatement.setString(2, ISO210.getPan());
            preparedStatement.setString(3, ISO210.getTransactionISOCode());
            preparedStatement.setString(4, ISO210.getAmountCurrencyCardIssuer());
            preparedStatement.setString(5, ISO210.getSendDateTimeToSwitch());
            preparedStatement.setString(6, ISO210.getTraceCode());
            preparedStatement.setString(7, ISO210.getRegisterTime());
            preparedStatement.setString(8, ISO210.getRegisterDate());
            preparedStatement.setString(9, ISO210.getProcessDateTimeInDestination());
            preparedStatement.setString(10, ISO210.getResponseAndLicenseTransaction());
            preparedStatement.setString(11, ISO210.getResponseTransactionCode());
            preparedStatement.setString(12, ISO210.getAdditionalData());
            preparedStatement.setTimestamp(13,d );
            preparedStatement.setString(14,ISO210.getReceivedString() );

            preparedStatement.executeUpdate();
            Thread.sleep(777);
            st=null;
            st=connection.createStatement();
            st.executeUpdate( "UPDATE TBL_PAN_PENDING SET"
                    + " RESPONCEFLAG=1"
                    + " where SWITCHMESSAGESEQUENCE='" + ISO210.getReferenceCode()+"'" );

        }catch (SQLException e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            if (st != null) {try{st.close();st=null;}catch (Exception var1){}}
            return false;
        }catch (Exception e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            if (st != null) {try{st.close();st=null;}catch (Exception var1){}}

            return false;
        }finally {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            if (st != null) {try{st.close();st=null;}catch (Exception var1){}}
            return true;
        }
    }

    private  boolean cardSaveRequestReverse(ServiceObjects.ISOShetabVer7.ISO400 ISO400){

        PreparedStatement preparedStatement = null;

        String insertTableRequest = "INSERT INTO TBL_PAN_REVERSE_REQUEST"
                +  "(SWITCHMESSAGESEQUENCE, PAN,AMOUNT,"
                +  "SENDDATETIMETOSWITCH,TRACECODE,REGISTERTIME,REGISTERDATE,ADDITIONALDATA,DATETIME) VALUES"
                +  "(?,?,?,?,?,?,?,?,?)";
        try {
            Timestamp d=getCurrentTimeStamp();
            preparedStatement = connection.prepareStatement(insertTableRequest);

            preparedStatement.setString(1, ISO400.getReferenceCode());
            preparedStatement.setString(2, ISO400.getPan());
            preparedStatement.setString(3, ISO400.getAmountCurrencyCardIssuer());
            preparedStatement.setString(4, ISO400.getSendDateTimeToSwitch());
            preparedStatement.setString(5, ISO400.getTraceCode());
            preparedStatement.setString(6, ISO400.getRegisterTime());
            preparedStatement.setString(7, ISO400.getRegisterDate());
            preparedStatement.setString(8, ISO400.getAdditionalData());
            //  preparedStatement.setString(9, ISO400.getEncryptedRequest());
            preparedStatement.setTimestamp(9,d );

            preparedStatement.executeUpdate();
        }catch (SQLException e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }catch (Exception e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }finally {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return true;
        }
    }
    private  boolean cardSaveResponceReverse(ServiceObjects.ISOShetabVer7.ISO430 ISO430){

        PreparedStatement preparedStatement = null;

        String insertTableRequest = "INSERT INTO TBL_PAN_REVERSE_RESPONSE"
                +  "(SWITCHMESSAGESEQUENCE, PAN,SENDDATETIMETOSWITCH,TRACECODE,"
                +  "REGISTERTIME,REGISTERDATE,RESPONSETRANSACTIONCODE,ADDITIONALDATA,RECEIVESTRING,DATETIME) VALUES"
                +  "(?,?,?,?,?,?,?,?,?,?)";
        try {
            Timestamp d=getCurrentTimeStamp();
            preparedStatement = connection.prepareStatement(insertTableRequest);

            preparedStatement.setString(1, ISO430.getRRN());
            preparedStatement.setString(2, ISO430.getPan());
            preparedStatement.setString(3, ISO430.getTransmissionDateTime());
            preparedStatement.setString(4, ISO430.getSTAN());
            preparedStatement.setString(5, ISO430.getLocalTransactionTime()); //getRegisterTime());
            preparedStatement.setString(6, ISO430.getLocalTransactionDate()); // getRegisterDate());
            preparedStatement.setString(7, ISO430.getResponseCode());
            preparedStatement.setString(8, ISO430.getAdditionalData());
            preparedStatement.setString(9, ISO430.getReceivedString());
            preparedStatement.setTimestamp(10,d );

            preparedStatement.executeUpdate();
        }catch (SQLException e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }catch (Exception e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }finally {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return true;
        }
    }

    private  boolean TelSwitchSavePendingAndRequest(ServiceObjects.ISOShetabVer7.ISO100 ISO100){

        PreparedStatement preparedStatement = null;

        String insertTablePending = "INSERT INTO TBL_TELSWITCH_PENDING"
                + "(SWITCHMESSAGESEQUENCE, MESSAGETYPE, PAN,TRANSACTIONISOCOD,"
                +  "SENDDATETIMETOSWITCH,TRACEID,REGISTERTIME,REGISTERDATE,BILLID,PAYMENTID,TELNUMBER,DATETIME,RESPONCEFLAG) VALUES"
                + "(?,?,?,?,?,?,?,?,?,?,?,?,?)";

        String insertTableRequest = "INSERT INTO TBL_TELSWITCH_REQUEST"
                + "(SWITCHMESSAGESEQUENCE, MESSAGETYPE, PAN,TRANSACTIONISOCOD,"
                +  "SENDDATETIMETOSWITCH,TRACEID,REGISTERTIME,REGISTERDATE,BILLID,PAYMENTID,TELNUMBER,DATETIME,RESULT) VALUES"
                + "(?,?,?,?,?,?,?,?,?,?,?,?,?)";


        try {
            Timestamp d=getCurrentTimeStamp();
            preparedStatement = connection.prepareStatement(insertTablePending);

            preparedStatement.setString(1, ISO100.getReferenceCode());
            preparedStatement.setString(2, ISO100.getMessageType());
            preparedStatement.setString(3, ISO100.getPan());
            preparedStatement.setString(4, ISO100.getTransactionISOCode());
            preparedStatement.setString(5, ISO100.getSendDateTimeToSwitch());
            preparedStatement.setString(6, ISO100.getTraceCode());
            preparedStatement.setString(7, ISO100.getRegisterTime());
            preparedStatement.setString(8, ISO100.getRegisterDate());
            preparedStatement.setString(9, ISO100.getBillID());
            preparedStatement.setString(10,ISO100.getPayID());
            preparedStatement.setString(11,ISO100.getTelNo());
            preparedStatement.setTimestamp(12,d );
            preparedStatement.setInt(13, 0);

            preparedStatement.executeUpdate();
            Thread.sleep(700);

            preparedStatement = connection.prepareStatement(insertTableRequest);

            preparedStatement.setString(1, ISO100.getReferenceCode());
            preparedStatement.setString(2, ISO100.getMessageType());
            preparedStatement.setString(3, ISO100.getPan());
            preparedStatement.setString(4, ISO100.getTransactionISOCode());
            preparedStatement.setString(5, ISO100.getSendDateTimeToSwitch());
            preparedStatement.setString(6, ISO100.getTraceCode());
            preparedStatement.setString(7, ISO100.getRegisterTime());
            preparedStatement.setString(8, ISO100.getRegisterDate());
            preparedStatement.setString(9, ISO100.getBillID());
            preparedStatement.setString(10, ISO100.getPayID());
            preparedStatement.setString(11,ISO100.getTelNo());
            preparedStatement.setTimestamp(12,d );
            preparedStatement.setInt(13, 0);

            preparedStatement.executeUpdate();


        }catch (SQLException e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }catch (Exception e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }finally {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return true;
        }
    }
    private  boolean TelSwitchSaveResponse(ServiceObjects.ISOShetabVer7.ISO110 ISO110){

        PreparedStatement preparedStatement = null;

        String insertTableResponse = "INSERT INTO TBL_TELSWITCH_RESPONSE"
                +  "(SWITCHMESSAGESEQUENCE, PAN, TRANSACTIONISOCODE,"
                +  "SENDDATETIMETOSWITCH,TRACECODE,REGISTERTIME,REGISTERDATE,CHECKOUTDATE,"
                +  "RESULT,BILLID,PAYID,AMOUNT,DATETIME,DEADLINE,RCVSTRING) VALUES"
                +  "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
        try {
            Timestamp d=getCurrentTimeStamp();
            preparedStatement = connection.prepareStatement(insertTableResponse);

            preparedStatement.setString(1, ISO110.getReferenceCode());
            preparedStatement.setString(2, ISO110.getPan());
            preparedStatement.setString(3, ISO110.getTransactionISOCode());
            preparedStatement.setString(4, ISO110.getSendDateTimeToSwitch());
            preparedStatement.setString(5, ISO110.getTraceCode());
            preparedStatement.setString(6, ISO110.getRegisterTime());
            preparedStatement.setString(7, ISO110.getRegisterDate());
            preparedStatement.setString(8, ISO110.getCheckoutDate());
            preparedStatement.setString(9, ISO110.getResponseTransactionCode());
            preparedStatement.setString(10, ISO110.getBillID());
            preparedStatement.setString(11, ISO110.getPayID());
            preparedStatement.setString(12, ISO110.getAmount());
            preparedStatement.setTimestamp(13,d );
            preparedStatement.setString(14, ISO110.getDeadLine());
            preparedStatement.setString(15, ISO110.getReceivedString());

            preparedStatement.executeUpdate();

            Thread.sleep(777);
            Statement st=connection.createStatement();;

            st.executeUpdate( "UPDATE TBL_TELSWITCH_PENDING SET"
                    + " RESPONCEFLAG=1"
                    + " where SWITCHMESSAGESEQUENCE='" + ISO110.getReferenceCode()+"'" );
        }catch (SQLException e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }catch (Exception e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return false;
        }finally {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connection != null) {try{connection.close();connection=null;}catch (Exception var1){}}
            return true;
        }
    }

    private  boolean  orm_registerCustomer(Msg200 msg200) throws Exception {
        int Result;
        boolean resOfLog=true;
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

            Sql = (new StringBuilder())
                    .append(" Delete from TBL_SPECIALSERVICEINFO " +
                            "where fldmainAccountno='")
                    .append(Accno).append("'")
                    .append(" INSERT INTO [TBL_SPECIALSERVICEINFO]" +
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
                resOfLog=true;
                orm_saveLogDb(Accno, "200", msg200.getDateAndTime(), Result, msg200.getMsgSeq().toString());
            } catch (SQLException e) {
                resOfLog=false;
                if (stmnt1 != null) {
                    stmnt1.close();
                }
                System.out.println("\nRegister Customer Fail.");

            }

            if (res > 0)  Result = 0;
            if (res < 0)  Result = 10;
            if (res == 0) Result = 82;
        } catch (Exception e) {
            resOfLog=false;
            Result = 10;
        }
        this.setOrmResult(Result);

        return resOfLog;

    }
    private  boolean  orm_registerLegalCustomer(Msg222 msg222) throws Exception {
        int Result;
        String Accno;
        String Sql;
        boolean resOfLog=true;
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
            stmnt1 = connection.createStatement();
            res = stmnt1.executeUpdate(Sql);
            orm_saveLogDb(Accno, "222", msg222.getDateAndTime(), Result, msg222.getMsgSeq().toString());

            if (stmnt1 != null) {
                stmnt1.close();
            }

            if (res > 0) Result = 0;
            if (res < 0) Result = 10;
            if (res == 0)Result = 82;

           // System.out.println("\nRegister Legal Customer Successfull.");

        } catch (SQLException e) {
            resOfLog=false;
            if (stmnt1 != null) {
                stmnt1.close();
            }

           // System.out.println("\nRegister Legal Customer Fail.");

        } catch (Exception e) {
            resOfLog=false;

            if (stmnt1 != null) {
                stmnt1.close();
            }

            //System.out.println("\nRegister Legal Customer Fail , Message False.");

            Result = 10;
        }

        this.setOrmResult(Result);

        return resOfLog;
    }
    private  boolean  orm_registerJointAccount(Msg223 msg223) throws Exception {
        int Result;
        String Accno;
        String Sql;
        boolean resOfLog=true;
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
            orm_saveLogDb(Accno, "223", msg223.getDateAndTime(), Result, msg223.getMsgSeq().toString());

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
            resOfLog=false;
            if (stmnt1 != null) {
                stmnt1.close();
            }

            if (connection != null) {
                connection.close();
            }

            System.out.println("\nRegister Legal Customer Fail.");

        } catch (Exception e) {
            resOfLog=false;
            if (stmnt1 != null) {
                stmnt1.close();
            }

            if (connection != null) {
                connection.close();
            }

            System.out.println("\nRegister Legal Customer Fail , Message False.");

            Result = 10;
        }


        this.setOrmResult(Result);

        return resOfLog;
    }
    private  boolean  orm_UpdateMigrateCustomer(Msg231 msg231)throws Exception {
        int result;
        String Sql;
        Statement stmnt1;
        boolean resOfLog=true;
        result = 10;
        String OriginalMsgSeq = Long.toString(msg231.getOriginalMsgSeq());
        String AccNo = StringUtils.leftPad(ORM.migratedRecord.get(Long.valueOf(OriginalMsgSeq)), 10, "0");
        Sql = (new StringBuilder()).append(" Update " + PropertiesUtil.getTbName() + "  Set AvaCasActive=1, SendToAvacas=1, OrmMsgID= ")
                .append(OriginalMsgSeq).append(" Where  AccNo='").append(AccNo).append("'").toString();
        int res = 0;
        try{
            stmnt1 = connection.createStatement();
            result = stmnt1.executeUpdate(Sql);

            try {
                stmnt1.close();
                connection.close();
            } catch (SQLException e) {
                resOfLog=false;
                e.printStackTrace();
            }
        }catch (SQLException e){
            resOfLog=false;
        }


        this.setOrmResult(result);

        return resOfLog;
    }
    private  boolean  orm_updateCustomer(Msg500 msg500) throws Exception {
        int Result;
        String Accno;
        String Sql;
        Result = 10;
        boolean resOfLog=true;
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

            Sql = (new StringBuilder()).append(" Update [TBL_SPECIALSERVICEINFO] Set [fldRegisterationDate]='")
                    .append(msg500.getDateAndTime().substring(0, 8))
                    .append("',")
                    .append(" [fldFirstAccountNo]='")
                    .append(msg500.getDestAccounts()[0].substring(6, 16))
                    .append("',")
                    .append(" [fldSecondAccountNo]='")
                    .append(msg500.getDestAccounts()[1].substring(6, 16))
                    .append("',")
                    .append(" [fldThirdAccountNo]='")
                    .append(msg500.getDestAccounts()[2].substring(6, 16))
                    .append("',")
                    .append(" [fldFourthAccountNo]='")
                    .append(msg500.getDestAccounts()[3].substring(6, 16))
                    .append("',")
                    .append(" [fldFifthAccountNo]='")
                    .append(msg500.getDestAccounts()[4].substring(6, 16))
                    .append("',")
                    .append(" [LastChangePin]='")
                    .append(msg500.getDateAndTime().substring(0, 8))
                    .append("',")
                    .append(" [IsFreeTransfer]='")
                    .append(isFreeTrans)
                    .append("',")
                    .append(" [fldServiceType]='")
                    .append(service)
                    .append("',")
                    .append(" [IsActive]=1,")
                    .append(" [OnlineState]=1,")
                    .append(" [AvaCasActive]=1")
                    .append(" Where fldMainAccountNo='")
                    .append(Accno)
                    .append("'").toString();
            int res;
            res = 0;

            try {
                stmnt1 = connection.createStatement();
                res = stmnt1.executeUpdate(Sql);
                orm_saveLogDb(Accno, "500", msg500.getDateAndTime(), Result, msg500.getMsgSeq().toString());

                if (stmnt1 != null) {
                    stmnt1.close();
                }

                if (connection != null) {
                    connection.close();
                }

                System.out.println("\nUpdate Customer Successfull.");
                //MainForm.sendString("Update Customer Successfull.");
            } catch (SQLException e) {
                resOfLog=false;
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
            resOfLog=false;
            System.out.println("\nUpdate Customer Fail , Message False.");
            //MainForm.sendString("Update Customer Fail , Message False.");

        }
        this.setOrmResult(Result);

        return resOfLog;
    }
    private  boolean  orm_disableAcc(Msg600 msg600) throws Exception {
        int Result;
        String Sql;
        int res;
        Result = 10;
        boolean resOfLog=true;
        //Msg600 msg600 = (Msg600) rcMsg;
        String Loginid = msg600.getLoginId();
        Loginid = StringUtils.leftPad(Loginid, 10, "0");
        Sql = (new StringBuilder()).append(" update PrivilegeRegistration set isactive=0 where fldmainaccountno='")
                .append(Loginid).append("'").toString();
        res = 0;
        Statement stmnt1 = null;
        try {
            stmnt1 = connection.createStatement();
            res = stmnt1.executeUpdate(Sql);
            orm_saveLogDb(Loginid, "600", msg600.getDateAndTime(), Result, msg600.getMsgSeq().toString());

            if (stmnt1 != null) {
                stmnt1.close();
            }
            if (connection != null) {
                connection.close();
            }
            System.out.println("\nDisable Customer Account Successfull.");
        } catch (SQLException e) {
            resOfLog=false;
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
        this.setOrmResult(Result);

        return resOfLog;
    }
    public   void     orm_saveLogDb(String Accno, String MsgType, String DateTime, int ResultCode, String MsgId) throws Exception {
        String Sql;
        Sql = (new StringBuilder()).append(" INSERT INTO [TBL_ORMLOG]([Accno], " +
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

            stmnt1 = connection.createStatement();
            stmnt1.executeUpdate(Sql);




        } catch (SQLException e) {


        }
    }



}

