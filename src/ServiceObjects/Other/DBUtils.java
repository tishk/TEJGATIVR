package ServiceObjects.Other;

import ServiceObjects.Account.BillPayByIDAccount;
import ServiceObjects.Account.FundTransfer;
import ServiceObjects.Account.InternalFollowUp;
import ServiceObjects.Pan.BillPayByBillIDPan;
import ServiceObjects.Pan.PanPayment;
import com.ibm.disthub2.impl.gd.ARangeList;
import utils.PropertiesUtils;
import utils.strUtils;

import java.io.Serializable;
import java.sql.*;
import java.util.*;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Administrator on 12/8/2015.
 */
public class DBUtils implements Serializable {
    public DBUtils() {
        try {

                if (ConnectedToDB()) connected = true;
            if (connected); //System.out.println("yes");;
            else System.out.println("no");

        } catch (SQLException e) {
        } catch (Exception e) {
        }
    }
    public DBUtils(String DataBaseName) {
        try {

            DBName=DataBaseName;
            if (ConnectedToSQLServerDB("MasterDatabase")) connected = true;
            if (connected); //System.out.println("yes");;
            else System.out.println("no");


        } catch (SQLException e) {
        } catch (Exception e) {
        }
    }
    private static  String lastMsgSeq=null;
    String DBURL = "";
    String DBUSER = "";
    String DBPASS = "";
    String url = "";
    String Driver="";
    String DBName = "";
    strUtils strutils = new strUtils();
    private Connection connection = null;
    private Connection connectionToSQL = null;
    public boolean connected = false;
    ResultSet resultSet_cashFromSQL=null;
    public static synchronized String getMsgSequence() {
        String seq;
        String sseq;
        do {
            seq = String.valueOf(System.nanoTime()).substring(0,12);//144525438988
            sseq= String.valueOf(System.currentTimeMillis());
            // System.out.println("seq is:" + seq);
        } while(seq.equals(lastMsgSeq));

        lastMsgSeq = seq;
        return seq;
    }

    private boolean ConnectedToDB() throws SQLException {
         DBURL = PropertiesUtils.getDataBaseURL();
         DBUSER = PropertiesUtils.getDataBaseUserName();
         DBPASS = PropertiesUtils.getDataBasePassword();
         int tryCount=7;
        while (tryCount-->0){
            if (getConnectionIsSuccess()){
                return true;
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        return false;
      /*  while (result=getConnectionIsSuccess()){
            if (tryCount++>4) break;
        }
        return result;*/
    }

    private boolean getConnectionIsSuccess() {
        try {
            if (connection == null) {
                DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
                connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
                return true;
            } else if (connection.isClosed()) {
                connection = null;
                DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
                connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
                return true;
            } else {
                connection.close();
                connection = null;
                DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
                connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
                return true;
            }

        } catch (Exception e) {
            System.out.println("connection failed:"+e.getMessage());
            connection = null;
            return false;
        }
    }

    private boolean ConnectedToSQLServerDB(String db) throws SQLException, ClassNotFoundException {
         //DBURL =  PropertiesUtils.getSQL_DB_URL()+DBName;
         DBUSER = PropertiesUtils.getSQL_DB_USER();
         DBPASS = PropertiesUtils.getSQL_DB_PASSWORD();
         url =    PropertiesUtils.getSQL_DB_URL()+db;
         Driver=  PropertiesUtils.getSQL_DB_DRIVER();

        try {
            if (connectionToSQL == null) {
                Class.forName(Driver);
                connectionToSQL = DriverManager.getConnection(url, DBUSER, DBPASS);
                return true;
            } else if (connectionToSQL.isClosed()) {
                connectionToSQL = null;
                Class.forName(Driver);
                connectionToSQL = DriverManager.getConnection(url, DBUSER, DBPASS);
                return true;
            } else {
                connectionToSQL.close();
                connectionToSQL = null;
                Class.forName(Driver);
                connectionToSQL = DriverManager.getConnection(url, DBUSER, DBPASS);
                return true;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            connection = null;
            return false;
        }
    }

    private java.sql.Timestamp getCurrentTimeStamp() {

        java.util.Date today = new java.util.Date();
        long t = today.getTime();
        return new java.sql.Timestamp(t);

    }

    public FundTransfer accountFundTransferCashData(FundTransfer fundTransfer) throws SQLException {

        Statement preparedStatement = null;
        ResultSet rset = null;

        String selectQuery = "SELECT * FROM  TBL_SPECIALSERVICEINFO  " +
                             "WHERE fldMainAccountNo =" + fundTransfer.getSourceAccount();
        try {

            preparedStatement = connection.createStatement();
            rset = preparedStatement
                    .executeQuery(selectQuery);
            while (rset.next()) {
                fundTransfer.setIsRegistered(true);
                fundTransfer.setServiceTypes(rset.getString("SERVICETYPES"));
                fundTransfer.setNationalCode(rset.getString("NATIONALCODE"));
                fundTransfer.setDefinedAccount1(rset.getString("ACCNO_1"));
                fundTransfer.setDefinedAccount2(rset.getString("ACCNO_2"));
                fundTransfer.setDefinedAccount3(rset.getString("ACCNO_3"));
                fundTransfer.setDefinedAccount4(rset.getString("ACCNO_4"));
                fundTransfer.setDefinedAccount5(rset.getString("ACCNO_5"));
                if (rset.getString("ISFREETRANSFER").equals("1")) fundTransfer.setIsFreeTransfer(true);
            }

            if (fundTransfer.getIsInstallmentPay()){
                String selectQuery1 = "SELECT HesabGroup FROM  Master  " +
                                      "WHERE AccNo =   " + fundTransfer.getSourceAccount();
                preparedStatement = connection.createStatement();
                rset = preparedStatement
                        .executeQuery(selectQuery1);
                String accountGroup="!";
                while (rset.next()) {
                  accountGroup=rset.getString("HesabGroup");
                }
                if (!accountGroup.equals("!")){
                    fundTransfer.setIsValidAccount(true);
                    selectQuery = "SELECT * FROM  TBL_HESABGROUPDEFINE  ";
                    preparedStatement = connection.createStatement();
                    rset = preparedStatement.executeQuery(selectQuery);
                    while (rset.next()) {
                        if (rset.getString("GROUPCODE").equals(accountGroup)){
                            fundTransfer.setAccountGroupAllowed(true);
                            break;
                        }
                    }
                }else fundTransfer.setIsValidAccount(false);
            }

        } catch (SQLException e) {
            if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
            if (connection != null) {try {connection.close();connection = null;} catch (Exception var1) {}}
            if (rset != null) {try {rset.close();rset = null;} catch (Exception var2) {}}
            fundTransfer.setDataFromDbChashed(false);
        } catch (Exception e) {
            if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
            if (connection != null) {try {connection.close();connection = null;} catch (Exception var1) {}}
            if (rset != null) {try {rset.close();rset = null;} catch (Exception var2) {}}
            fundTransfer.setDataFromDbChashed(false);
        } finally {
            if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
            if (connection != null) {try {connection.close();connection = null;} catch (Exception var1) {}}
            if (rset != null) {try {rset.close();rset = null;} catch (Exception var2) {}}
            fundTransfer.setDataFromDbChashed(true);
        }
        return fundTransfer;
    }
    public boolean isAccountCodeOK(String accountGroup) throws SQLException {

        Statement preparedStatement = null;
        ResultSet rset = null;
        boolean isOK=false;
        try {
            String selectQuery= "SELECT * FROM  TBL_HESABGROUPDEFINE  WHERE GROUPCODE="+accountGroup;
            preparedStatement = connection.createStatement();
            rset = preparedStatement.executeQuery(selectQuery);
            while (rset.next()) {
                isOK=true;
            }
        } catch (SQLException e) {
            if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
            if (connection != null) {try {connection.close();connection = null;} catch (Exception var1) {}}
            if (rset != null) {try {rset.close();rset = null;} catch (Exception var2) {}}

        } catch (Exception e) {
            if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
            if (connection != null) {try {connection.close();connection = null;} catch (Exception var1) {}}
            if (rset != null) {try {rset.close();rset = null;} catch (Exception var2) {}}

        } finally {
            if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
            if (connection != null) {try {connection.close();connection = null;} catch (Exception var1) {}}
            if (rset != null) {try {rset.close();rset = null;} catch (Exception var2) {}}

        }
        return isOK;
    }

    public InternalFollowUp internalFollowUpTransaction(InternalFollowUp followUpTransaction) throws SQLException {

        Statement preparedStatement = null;
        ResultSet rset = null;
        followUpTransaction.setMessageSequence(getMsgSequence());
        String selectQuery = "SELECT * FROM  TBL_PAYMENT  " +
                "WHERE FOLLOWCODE =" + followUpTransaction.getFollowUpCode()+
                " and SOURCEOFPAYMENT="+followUpTransaction.getSourceAccount();
        try {

            preparedStatement = connection.createStatement();
            rset = preparedStatement
                    .executeQuery(selectQuery);
            while (rset.next()) {
                followUpTransaction.setActionCode("0000");
                followUpTransaction.setisValidFollowCode(true);
                followUpTransaction.setstatus(rset.getString("ACTIONCODE"));
                followUpTransaction=setDonFlag(followUpTransaction);
            }
        } catch (SQLException e) {
            if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
            if (connection != null) {try {connection.close();connection = null;} catch (Exception var1) {}}
            if (rset != null) {try {rset.close();rset = null;} catch (Exception var2) {}}
        } catch (Exception e) {
            if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
            if (connection != null) {try {connection.close();connection = null;} catch (Exception var1) {}}
            if (rset != null) {try {rset.close();rset = null;} catch (Exception var2) {}}
        } finally {
            if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
            if (connection != null) {try {connection.close();connection = null;} catch (Exception var1) {}}
            if (rset != null) {try {rset.close();rset = null;} catch (Exception var2) {}}

        }
        logTodb(followUpTransaction);
        return followUpTransaction;
    }

    private InternalFollowUp setDonFlag(InternalFollowUp followUpTransaction) {
        if (followUpTransaction.getstatus().equals("0000")){
            followUpTransaction.setdoneFlag("1");
        }else if (followUpTransaction.getstatus().equals("1000")){
            followUpTransaction.setdoneFlag("3");
        }else if (followUpTransaction.getstatus().equals("2000")){
            followUpTransaction.setdoneFlag("2");
        }else if (followUpTransaction.getstatus().equals("9126")){
            followUpTransaction.setdoneFlag("2");
        }else{
            followUpTransaction.setdoneFlag("0");
        }
        return followUpTransaction;
    }

    private  java.sql.Date getCurrentDate() {

        java.sql.Date today = new java.sql.Date(new java.util.Date().getTime());
        return today;

    }

    private String  getStatusFromActionCode(String actionCode){
        int intActionCode=-1;
        try{intActionCode=Integer.valueOf(actionCode);}catch (Exception e){intActionCode=-1;}
        switch (intActionCode){
            case    0:return "0";
            case 1000:return "1";
            case 2000:return "2";
            case 3000:return "3";
            default:  return "4";
        }
    }
    public Long  getPaymentOfToday(String accountNO){
        java.sql.Date today=getCurrentDate();
        PreparedStatement preparedStatement = null;
        ResultSet rset = null;
        Long amount=0L;

        String selectQuery = "SELECT * FROM  TBL_PAYMENT  " +
                "WHERE SOURCEOFPAYMENT =? and DATEOFTRANSACTION=? and ACTIONCODE=?";
        try {

            preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1,accountNO);
            preparedStatement.setDate(2,today);
            preparedStatement.setString(3,"0000");

            rset = preparedStatement.executeQuery();
            while (rset.next()) {

                amount=amount+Long.valueOf(rset.getString("AMOUNT"));

            }



        } catch (SQLException e) {
            if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
            if (connection != null) {try {connection.close();connection = null;} catch (Exception var1) {}}
            if (rset != null) {try {rset.close();rset = null;} catch (Exception var2) {}}
        } catch (Exception e) {
            if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
            if (connection != null) {try {connection.close();connection = null;} catch (Exception var1) {}}
            if (rset != null) {try {rset.close();rset = null;} catch (Exception var2) {}}
        } finally {
            if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
            if (connection != null) {try {connection.close();connection = null;} catch (Exception var1) {}}
            if (rset != null) {try {rset.close();rset = null;} catch (Exception var2) {}}
        }
        return amount;
    }

    public BillPayByIDAccount accountBillPayByIDAccountCashData_(BillPayByIDAccount billPayByIDAccount) throws SQLException {

        Statement preparedStatement = null;
        ResultSet rset = null;

        String selectQuery = "SELECT * FROM  TBL_SPECIALSERVICEINFO  " +
                "WHERE fldMainAccountNo =" + billPayByIDAccount.getSourceAccount();
        try {

            preparedStatement = connection.createStatement();
            rset = preparedStatement.executeQuery(selectQuery);
            while (rset.next()) {
                billPayByIDAccount.setIsRegistered(true);
                billPayByIDAccount.setServiceTypes(rset.getString("SERVICETYPES"));
                billPayByIDAccount.setNationalCode(rset.getString("NATIONALCODE"));
            }
            selectQuery = "SELECT * FROM  TBL_Bill  " +
                    "WHERE ShGhabz =" + billPayByIDAccount.getBillID() +
                    " and ShPardakht=" + billPayByIDAccount.getPaymentID() +
                    " and (fldPBDoneFlag = 1 OR fldPBDoneFlag = 2 OR fldPBDoneFlag = 3)";
            preparedStatement = connection.createStatement();
            rset = preparedStatement.executeQuery(selectQuery);
            billPayByIDAccount.sethasAlreadyBeenPaid(false);
            while (rset.next()) {
                billPayByIDAccount.sethasAlreadyBeenPaid(true);
            }
            if (!billPayByIDAccount.gethasAlreadyBeenPaid()) {
                String billId = billPayByIDAccount.getBillID();
                selectQuery = "SELECT * From ServersInfo " +
                        "WHERE fldServiceCode =" + billId.charAt(billId.length() - 2) +
                        " and  fldSubServerCode=" + strutils.midString(billId, billId.length() - 4, 3) +
                        " and  fldEnabled = 1";
                preparedStatement = connection.createStatement();
                rset = preparedStatement.executeQuery(selectQuery);
                billPayByIDAccount.setSubServiceNotDefined(false);
                String fldacc = "";
                boolean notIsEmpty = false;
                while (rset.next()) {
                    notIsEmpty = true;
                    try {
                        if (Long.valueOf(rset.getString("fldAccountNo")) > Long.valueOf("0")) {

                        } else {
                            billPayByIDAccount.setSubServiceNotDefined(true);
                        }
                    } catch (Exception e) {
                        billPayByIDAccount.setSubServiceNotDefined(true);
                    }
                }
                if (!notIsEmpty) billPayByIDAccount.setSubServiceNotDefined(true);
            }
        } catch (SQLException e) {
            if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
            if (connection != null) {try {connection.close();connection = null;} catch (Exception var1) {}}
            if (rset != null) {try {rset.close();rset = null;} catch (Exception var2) {}}
            billPayByIDAccount.setDataFromDbChashed(false);
        } catch (Exception e) {
            if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
            if (connection != null) {try {connection.close();connection = null;} catch (Exception var1) {}}
            if (rset != null) {try {rset.close();rset = null;} catch (Exception var2) {}}
            billPayByIDAccount.setDataFromDbChashed(false);
        } finally {
            if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
            if (connection != null) {try {connection.close();connection = null;} catch (Exception var1) {}}
            if (rset != null) {try {rset.close();rset = null;} catch (Exception var2) {}}
            billPayByIDAccount.setDataFromDbChashed(true);
        }
        return billPayByIDAccount;
    }
    public BillPayByIDAccount accountBillPayByIDAccountCashData(BillPayByIDAccount billPayByIDAccount) throws SQLException {

        Statement preparedStatement = null;
        ResultSet rset = null;
        billPayByIDAccount.setIsRegistered(true);
        try {
           String selectQuery = "SELECT * FROM  TBL_PAYMENT  " +
                    "WHERE BILLID =" + billPayByIDAccount.getBillID() +
                    " and PAYMENTID=" + billPayByIDAccount.getPaymentID() +
                    " and (ACTIONCODE=0000 or ACTIONCODE=1000)";
            preparedStatement = connection.createStatement();
            rset = preparedStatement.executeQuery(selectQuery);
            billPayByIDAccount.sethasAlreadyBeenPaid(false);
            while (rset.next()) {
                billPayByIDAccount.sethasAlreadyBeenPaid(true);
            }
/*            if (!billPayByIDAccount.gethasAlreadyBeenPaid()) {
                String billId = billPayByIDAccount.getBillID();
                selectQuery = "SELECT * From TBL_BILL_SERVERSINFO " +
                        "WHERE fldServiceCode =" + billId.charAt(billId.length() - 2) +
                        " and  fldSubServerCode=" + strutils.midString(billId, billId.length() - 4, 3) +
                        " and  fldEnabled = 1";
                preparedStatement = connection.createStatement();
                rset = preparedStatement.executeQuery(selectQuery);
                billPayByIDAccount.setSubServiceNotDefined(false);
                String fldacc = "";
                boolean notIsEmpty = false;
                while (rset.next()) {
                    notIsEmpty = true;
                    try {
                        if (Long.valueOf(rset.getString("fldAccountNo")) > Long.valueOf("0")) {

                        } else {
                            billPayByIDAccount.setSubServiceNotDefined(true);
                        }
                    } catch (Exception e) {
                        billPayByIDAccount.setSubServiceNotDefined(true);
                    }
                }
                if (!notIsEmpty) billPayByIDAccount.setSubServiceNotDefined(true);
            }*/
        } catch (SQLException e) {
            if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
            if (connection != null) {try {connection.close();connection = null;} catch (Exception var1) {}}
            if (rset != null) {try {rset.close();rset = null;} catch (Exception var2) {}}
            billPayByIDAccount.setDataFromDbChashed(false);
        } catch (Exception e) {
            if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
            if (connection != null) {try {connection.close();connection = null;} catch (Exception var1) {}}
            if (rset != null) {try {rset.close();rset = null;} catch (Exception var2) {}}
            billPayByIDAccount.setDataFromDbChashed(false);
        } finally {
            if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
            if (connection != null) {try {connection.close();connection = null;} catch (Exception var1) {}}
            if (rset != null) {try {rset.close();rset = null;} catch (Exception var2) {}}
            billPayByIDAccount.setDataFromDbChashed(true);
        }
        return billPayByIDAccount;
    }
    public PanPayment panBillPayByIDPanCashData(BillPayByBillIDPan billPayByBillIDPan, PanPayment panPayment) throws SQLException {

        Statement preparedStatement = null;
        ResultSet rset = null;
        panPayment.setRegistered(true);
        try {
           String selectQuery = "SELECT * FROM  TBL_PAYMENT  " +
                    "WHERE BILLID =" + billPayByBillIDPan.getBillID() +
                    " and PAYMENTID=" + billPayByBillIDPan.getPaymentID() +
                    " and ACTIONCODE=0000";
            preparedStatement = connection.createStatement();
            rset = preparedStatement.executeQuery(selectQuery);
            panPayment.setHasAlreadyBeenPaid(false);
            while (rset.next()) {
                panPayment.setHasAlreadyBeenPaid(true);
            }
            /*if (!panPayment.isHasAlreadyBeenPaid()) {
                String billId = billPayByBillIDPan.getBillID();
                selectQuery = "SELECT * From TBL_BILL_SERVERSINFO " +
                        "WHERE fldServiceCode =" + billId.charAt(billId.length() - 2) +
                        " and  fldSubServerCode=" + strutils.midString(billId, billId.length() - 4, 3) +
                        " and  fldEnabled = 1";
                preparedStatement = connection.createStatement();
                rset = preparedStatement.executeQuery(selectQuery);
                panPayment.setSubServiceNotDefined(false);
                String fldacc = "";
                boolean notIsEmpty = false;
                while (rset.next()) {
                    notIsEmpty = true;
                    try {
                        if (Long.valueOf(rset.getString("fldAccountNo")) > Long.valueOf("0")) {

                        } else {
                            panPayment.setSubServiceNotDefined(true);
                        }
                    } catch (Exception e) {
                        panPayment.setSubServiceNotDefined(true);
                    }
                }
                if (!notIsEmpty) panPayment.setSubServiceNotDefined(true);
            }*/
        } catch (SQLException e) {
            if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
            if (connection != null) {try {connection.close();connection = null;} catch (Exception var1) {}}
            if (rset != null) {try {rset.close();rset = null;} catch (Exception var2) {}}
            panPayment.setDataFromDbChashed(false);
        } catch (Exception e) {
            if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
            if (connection != null) {try {connection.close();connection = null;} catch (Exception var1) {}}
            if (rset != null) {try {rset.close();rset = null;} catch (Exception var2) {}}
            panPayment.setDataFromDbChashed(false);
        } finally {
            if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
            if (connection != null) {try {connection.close();connection = null;} catch (Exception var1) {}}
            if (rset != null) {try {rset.close();rset = null;} catch (Exception var2) {}}
            panPayment.setDataFromDbChashed(true);
        }
        return panPayment;
    }

    public boolean changePriority(String acc) throws SQLException {
        boolean res = false;

        Statement st = null;
        try {
            st = connection.createStatement();
            st.executeUpdate("update TBL_SPECIALSERVICEINFO Set RegisteredInCM=0 ,Priority=Priority+1 " +
                    "Where fldMainAccountNo=" + acc);
        } catch (SQLException e) {
            if (connection != null) {
                try {
                    connection.close();
                    connection = null;
                } catch (Exception var1) {
                }
            }
            if (st != null) {
                try {
                    st.close();
                    st = null;
                } catch (Exception var2) {
                }
            }
            res = false;
        } catch (Exception e) {
            if (connection != null) {
                try {
                    connection.close();
                    connection = null;
                } catch (Exception var1) {
                }
            }
            if (st != null) {
                try {
                    st.close();
                    st = null;
                } catch (Exception var2) {
                }
            }
            res = false;
        } finally {
            if (connection != null) {
                try {
                    connection.close();
                    connection = null;
                } catch (Exception var1) {
                }
            }
            if (st != null) {
                try {
                    st.close();
                    st = null;
                } catch (Exception var2) {
                }
            }
            res = true;
        }
        return res;
    }

    public void cashMasterTable(int count) throws SQLException, ClassNotFoundException {
        Statement preparedStatement = null;
        ResultSet rset = null;
        try {

            if (ConnectedToSQLServerDB("MasterDatabase")) {

                String selectQuery = "SELECT TOP 1000 * FROM  Master  ";
                preparedStatement = connectionToSQL.createStatement();
                resultSet_cashFromSQL = preparedStatement.executeQuery(selectQuery);

            }

        } catch (SQLException e) {
            if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
            if (connectionToSQL != null) {try {connectionToSQL.close();connectionToSQL = null;} catch (Exception var1) {}}
            if (resultSet_cashFromSQL != null) {try {resultSet_cashFromSQL.close();resultSet_cashFromSQL = null;} catch (Exception var2) {}}

        } catch (Exception e) {
            if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
            if (connectionToSQL != null) {try {connectionToSQL.close();connectionToSQL = null;} catch (Exception var1) {}}
            if (resultSet_cashFromSQL != null) {try {resultSet_cashFromSQL.close();resultSet_cashFromSQL = null;} catch (Exception var2) {}}

        } finally {


            if (ConnectedToDB()){
                String insertTableSQL =null;
                PreparedStatement pStatement = null;
                while (rset.next()){


                    insertTableSQL = "INSERT INTO TBL_MASTER"
                            + "(TRANSACTIONID, ACCNO, ACTIONCODE,DATETIME,CALLERID,CLIENTIP) VALUES"
                            + "(?,?,?,?,?,?)";


                    pStatement =connection.prepareStatement(insertTableSQL);


                    pStatement.setString(1, rset.getString("SERVICETYPES"));
                    pStatement.setString(2, rset.getString("SERVICETYPES"));
                    pStatement.setString(3, rset.getString("SERVICETYPES"));

                    pStatement.setString(5, rset.getString("SERVICETYPES"));
                    pStatement.setString(6, rset.getString("SERVICETYPES"));

                    pStatement.executeUpdate();
                }

            }

            if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
            if (connectionToSQL != null) {try {connectionToSQL.close();connectionToSQL = null;} catch (Exception var1) {}}
            if (rset != null) {try {rset.close();rset = null;} catch (Exception var2) {}}

        }


    }

    public String getTransactionDescription(String transCode) throws SQLException, ClassNotFoundException {
        Statement preparedStatement = null;
        String result="";
        ResultSet rset = null;
        try {

            if (ConnectedToDB()) {

                String selectQuery = "SELECT * FROM  TBL_TRANSCODE  " +
                        "WHERE TRANSCODE =" + transCode;


                preparedStatement = connection.createStatement();
                rset = preparedStatement
                        .executeQuery(selectQuery);
                while (rset.next()) {
                    result = rset.getString("TRANSDSCP");

                }

            }
        } catch (SQLException e) {
            if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
            if (connectionToSQL != null) {try {connectionToSQL.close();connectionToSQL = null;} catch (Exception var1) {}}
            if (rset != null) {try {rset.close();rset = null;} catch (Exception var2) {}}
        } catch (Exception e) {
            if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
            if (connectionToSQL != null) {try {connectionToSQL.close();connectionToSQL = null;} catch (Exception var1) {}}
            if (rset != null) {try {rset.close();rset = null;} catch (Exception var2) {}}
        } finally {
            if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
            if (connectionToSQL != null) {try {connectionToSQL.close();connectionToSQL = null;} catch (Exception var1) {}}
            if (rset != null) {try {rset.close();rset = null;} catch (Exception var2) {}}
        }
        return result;
    }
    public Map<String,String> getTransactionDescriptions() throws SQLException, ClassNotFoundException {
        Statement preparedStatement = null;
        String result="";

        Map<String,String> descriptions=new HashMap<String, String>();
        ResultSet rset = null;
        try {

            if (ConnectedToDB()) {

                String selectQuery = "SELECT * FROM  TBL_TRANSCODE  " ;

                preparedStatement = connection.createStatement();
                rset = preparedStatement
                        .executeQuery(selectQuery);
                while (rset.next()) {
                    descriptions.put(rset.getString("TRANSCODE"),rset.getString("TRANSDSCP"));
                }

            }
        } catch (SQLException e) {
            if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
            if (connectionToSQL != null) {try {connectionToSQL.close();connectionToSQL = null;} catch (Exception var1) {}}
            if (rset != null) {try {rset.close();rset = null;} catch (Exception var2) {}}
        } catch (Exception e) {
            if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
            if (connectionToSQL != null) {try {connectionToSQL.close();connectionToSQL = null;} catch (Exception var1) {}}
            if (rset != null) {try {rset.close();rset = null;} catch (Exception var2) {}}
        } finally {
            if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
            if (connectionToSQL != null) {try {connectionToSQL.close();connectionToSQL = null;} catch (Exception var1) {}}
            if (rset != null) {try {rset.close();rset = null;} catch (Exception var2) {}}
        }
        return descriptions;
    }

    public ArrayList<PaymentOBJ> getOfflinePayments() throws SQLException, ClassNotFoundException {
        Statement preparedStatement = null;
        String result="";
        ArrayList<PaymentOBJ> paymentOBJList=new ArrayList<PaymentOBJ>();
        ResultSet rset = null;
        try {

            if (ConnectedToDB()) {

                String selectQuery = "select * from TBL_PAYMENT " +
                        "where TBL_PAYMENT.ACTIONCODE=1000 or TBL_PAYMENT.ACTIONCODE=2000 order by DATETIME asc;";


                preparedStatement = connection.createStatement();
                rset = preparedStatement
                        .executeQuery(selectQuery);
                while (rset.next()) {
                    PaymentOBJ paymentOBJ=new PaymentOBJ();
                    paymentOBJ.setTRANSACTIONID(rset.getString("TRANSACTIONID"));
                    paymentOBJ.setISACCOUNT(rset.getString("ISACCOUNT"));
                    paymentOBJ.setACTIONCODE(rset.getString("ACTIONCODE"));
                    paymentOBJ.setDATETIME(rset.getTimestamp("DATETIME"));
                    paymentOBJ.setISACTIVEFORRETRY(rset.getString("ISACTIVEFORRETRY"));
                    paymentOBJ.setTRANCEDATE(rset.getString("TRANCEDATE"));
                    paymentOBJ.setTRANSTIME(rset.getString("TRANSTIME"));
                    paymentOBJ.setCALLID(rset.getString("CALLID"));
                    paymentOBJ.setBILLID(rset.getString("BILLID"));
                    paymentOBJ.setPAYMENTID(rset.getString("PAYMENTID"));
                    paymentOBJ.setAMOUNT(rset.getString("AMOUNT"));
                    paymentOBJ.setISFUNDTRUSFER(rset.getString("ISFUNDTRUSFER"));
                    paymentOBJ.setISFUNDTRANSFERWITHID(rset.getString("ISFUNDTRANSFERWITHID"));
                    paymentOBJ.setISBILLPAYMENTOFACCOUNT(rset.getString("ISBILLPAYMENTOFACCOUNT"));
                    paymentOBJ.setISINSTALLMENTPAYMENT(rset.getString("ISINSTALLMENTPAYMENT"));
                    paymentOBJ.setISBILLPAYMENTOFPAN(rset.getString("ISBILLPAYMENTOFPAN"));
                    paymentOBJ.setDATEOFTRANSACTION(rset.getDate("DATEOFTRANSACTION"));
                    paymentOBJ.setDONEFLAG(rset.getString("DONEFLAG"));
                    paymentOBJ.setDESTINATIONACCOUNT(rset.getString("DESTINATIONACCOUNT"));
                    paymentOBJ.setTRANFERID(rset.getString("TRANFERID"));
                    paymentOBJList.add(paymentOBJ);

                }

            }

        } catch (SQLException e) {
            if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
            if (connectionToSQL != null) {try {connectionToSQL.close();connectionToSQL = null;} catch (Exception var1) {}}
            if (rset != null) {try {rset.close();rset = null;} catch (Exception var2) {}}
        } catch (Exception e) {
            if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
            if (connectionToSQL != null) {try {connectionToSQL.close();connectionToSQL = null;} catch (Exception var1) {}}
            if (rset != null) {try {rset.close();rset = null;} catch (Exception var2) {}}
        } finally {
            if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
            if (connectionToSQL != null) {try {connectionToSQL.close();connectionToSQL = null;} catch (Exception var1) {}}
            if (rset != null) {try {rset.close();rset = null;} catch (Exception var2) {}}
        }
        return paymentOBJList;
    }
    public String getBranchName(String branchCode) throws SQLException, ClassNotFoundException {
        Statement preparedStatement = null;
        String result="";
        try{
           branchCode=String.valueOf(Integer.valueOf(branchCode));
        }catch (Exception e){
           branchCode="0";
        }
        ResultSet rset = null;
        try {

            if (ConnectedToDB()) {

                String selectQuery = "SELECT * FROM  TBL_BRANCHNAME  " +
                        "WHERE INTCODE =" +branchCode ;


                preparedStatement = connection.createStatement();
                rset = preparedStatement
                        .executeQuery(selectQuery);
                while (rset.next()) {
                    result = rset.getString("STRDESC");

                }

            }
        } catch (SQLException e) {
            if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
            if (connectionToSQL != null) {try {connectionToSQL.close();connectionToSQL = null;} catch (Exception var1) {}}
            if (rset != null) {try {rset.close();rset = null;} catch (Exception var2) {}}
        } catch (Exception e) {
            if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
            if (connectionToSQL != null) {try {connectionToSQL.close();connectionToSQL = null;} catch (Exception var1) {}}
            if (rset != null) {try {rset.close();rset = null;} catch (Exception var2) {}}
        } finally {
            if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
            if (connectionToSQL != null) {try {connectionToSQL.close();connectionToSQL = null;} catch (Exception var1) {}}
            if (rset != null) {try {rset.close();rset = null;} catch (Exception var2) {}}
        }
        return branchCode+" - "+result;
    }
    public Map<String,String> getBranchNames() throws SQLException, ClassNotFoundException {
        Statement preparedStatement = null;
        String result="";
        Map<String,String> resultMap=new HashMap<String,String>();
        ResultSet rset = null;
        try {

            if (ConnectedToDB()) {

                String selectQuery = "SELECT * FROM  TBL_BRANCHNAME  ";



                preparedStatement = connection.createStatement();
                rset = preparedStatement
                        .executeQuery(selectQuery);
                while (rset.next()) {
                    resultMap.put(rset.getString("INTCODE"),rset.getString("STRDESC"));
                }

            }
        } catch (SQLException e) {
            if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
            if (connectionToSQL != null) {try {connectionToSQL.close();connectionToSQL = null;} catch (Exception var1) {}}
            if (rset != null) {try {rset.close();rset = null;} catch (Exception var2) {}}
        } catch (Exception e) {
            if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
            if (connectionToSQL != null) {try {connectionToSQL.close();connectionToSQL = null;} catch (Exception var1) {}}
            if (rset != null) {try {rset.close();rset = null;} catch (Exception var2) {}}
        } finally {
            if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
            if (connectionToSQL != null) {try {connectionToSQL.close();connectionToSQL = null;} catch (Exception var1) {}}
            if (rset != null) {try {rset.close();rset = null;} catch (Exception var2) {}}
        }
        return resultMap;
    }
    public static void logTodb(final Object object){
        //

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future future = executorService.submit(new Runnable() {
            public void run() {
                try {
                    new LoggerToDB(object);
                    //   loToDB=loggerToDB;
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    public MonitoringStatus createMonitoringReport() {
        MonitoringStatus monitoringStatus=new MonitoringStatus();

        java.sql.Date today=getCurrentDate();
        PreparedStatement preparedStatement = null;
        ResultSet rset = null;
        String rowCount1="limit 0,1";

        try {
            String selectQuery = "select * from(SELECT * FROM  TBL_PAYMENT  " +
                    "WHERE ISFUNDTRUSFER =? and DATEOFTRANSACTION=? and ACTIONCODE=? ORDER BY DATETIME DESC ) where ROWNUM=1 ";

            preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1,"1");
            preparedStatement.setDate(2,today);
            preparedStatement.setString(3,"0000");

            rset = preparedStatement.executeQuery();
            while (rset.next()) {
               monitoringStatus.setLastFundTransferTime(rset.getString("DATETIME"));
            }
             selectQuery = "select * from(SELECT * FROM  TBL_PAYMENT  " +
                                "WHERE ISBILLPAYMENTOFACCOUNT =? and DATEOFTRANSACTION=? and ACTIONCODE=? ORDER BY DATETIME DESC) where ROWNUM=1 ";

            preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setString(1,"1");
            preparedStatement.setDate(2,today);
            preparedStatement.setString(3,"0000");

            rset = preparedStatement.executeQuery();
            while (rset.next()) {
               monitoringStatus.setLastBillPaymentTime(rset.getString("DATETIME"));
            }
            selectQuery ="SELECT * FROM TBL_LOG_ACC_PIN1_AUTH where datetime>? order by DATETIME desc";

            preparedStatement = connection.prepareStatement(selectQuery);
            preparedStatement.setDate(1,today);


            rset = preparedStatement.executeQuery();
            String ip="";
            String lastDateTime="";
            while (rset.next()) {
                ip=rset.getString("CLIENTIP");
                lastDateTime=rset.getString("DATETIME");
                if (!monitoringStatus.getBranchesStatus().containsKey(ip)){
                    monitoringStatus.getBranchesStatus().put(ip,lastDateTime);
                }
            }


        } catch (SQLException e) {
            if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
            if (connection != null) {try {connection.close();connection = null;} catch (Exception var1) {}}
            if (rset != null) {try {rset.close();rset = null;} catch (Exception var2) {}}
        } catch (Exception e) {
            if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
            if (connection != null) {try {connection.close();connection = null;} catch (Exception var1) {}}
            if (rset != null) {try {rset.close();rset = null;} catch (Exception var2) {}}
        } finally {
            if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
            if (connection != null) {try {connection.close();connection = null;} catch (Exception var1) {}}
            if (rset != null) {try {rset.close();rset = null;} catch (Exception var2) {}}
        }
        return monitoringStatus;
    }
}