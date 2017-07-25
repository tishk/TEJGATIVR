

import ServiceObjects.Account.BillPayByIDAccount;
import ServiceObjects.Account.FundTransfer;
import ServiceObjects.Account.InternalFollowUp;
import utils.strUtils;

import java.sql.*;

/**
 * Created by Administrator on 12/8/2015.
 */
public class DBUtils {

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

    public DBUtils(boolean IsOracleDBConnection) throws SQLException, ClassNotFoundException {
        if (IsOracleDBConnection)
            if (ConnectedToDB()) connected = true;
            else connected=false;
        else
        if (ConnectedToSQLServerDB(Properties_Monitoring.getSqlServer_DB_Name())) connected = true;
        else connected=false;

    }
    public DBUtils(int DBMSSwitch) throws SQLException, ClassNotFoundException {

            if (ConnectedToDB()) connected = true;
            else connected=false;
            if (ConnectedToSQLServerDB(Properties_Monitoring.getSqlServer_DB_Name())) connected = true;
        else connected=false;

    }
    public DBUtils(Object object) throws SQLException, ClassNotFoundException {
        if (ConnectedToSQLServerDB(Properties_Monitoring.getSqlServer_DB_Name())){
            saveToDB(object);
        }
    }
    public DBUtils(String responseOfInvalidRequest){

    }

    public  void saveToDB(Object object){
        if (object instanceof Gateway) log_Gateway(object);
        else if (object instanceof Channel) log_AllServicesOfGateway(object);
        else if (object instanceof CardSwitch) log_AllServicesOfGateway(object);
        else if (object instanceof TelSwitch) log_AllServicesOfGateway(object);
        else if (object instanceof Authenticate) log_AllServicesOfGateway(object);
        else if (object instanceof Telbank) log_AllServicesOfGateway(object);
    }
    private void log_AllServicesOfGateway(Object object){
        AllServicesOfGateway allServicesOfGateway=(AllServicesOfGateway)object;

        Statement st = null;
        try {
            st = connection.createStatement();
            st.executeUpdate("update tbl_Status_Gateway Set " +
                            "RegisteredInCM=0 ," +
                            "Priority=Priority+1 " +
                            "RegisteredInCM=0 ," +
                            "Priority=Priority+1 " +
                            "RegisteredInCM=0 ," +
                            "Priority=Priority+1 " +
                            "RegisteredInCM=0 ," +
                            "Priority=Priority+1 " +
                            "RegisteredInCM=0 ," +
                            "Priority=Priority+1 " +
                            "RegisteredInCM=0 ," +
                            "Priority=Priority+1 " +
                            "RegisteredInCM=0 ," +
                            "Priority=Priority+1 " +
                            "RegisteredInCM=0 ," +
                            "Priority=Priority+1 " +
                            "RegisteredInCM=0 ," +
                            "Priority=Priority+1 "
                    );
        } catch (SQLException e){
            if (connection != null) {try {connection.close();connection = null;} catch (Exception var1) {}}
            if (st != null) {try {st.close();st = null;} catch (Exception var2) {}}

        } catch (Exception e) {
            if (connection != null) {try {connection.close();connection = null;} catch (Exception var1) {}}
            if (st != null) {try {st.close();st = null;} catch (Exception var2) {}}
        } finally {
            if (connection != null) {try {connection.close();connection = null;} catch (Exception var1) {}}
            if (st != null) {try {st.close();st = null;} catch (Exception var2) {}}
        }

    }
    private void log_Gateway(Object object){

    }
    private void log_Channel(Object object){

    }
    private void log_CardSwitch(Object object){

    }
    private void log_TelSwitch(Object object){

    }
    private void log_Authenticate(Object object){

    }
    private void log_Telbank(Object object){

    }
    public  String[] getTelBankList(){
        Statement preparedStatement = null;
        ResultSet rset = null;
        int clientCount=0;
        String[] clientList=null;
        String selectQuery = "SELECT COUNT(*) FROM  TBL_RMICLIENTS  ";

        try {

            preparedStatement = connection.createStatement();
            rset = preparedStatement
                    .executeQuery(selectQuery);
            while (rset.next()) {
                clientCount=Integer.valueOf(rset.getString("COUNT(*)"));
            }
            clientList=new String[clientCount];
            selectQuery = "SELECT * FROM  TBL_RMICLIENTS  ";
            rset = preparedStatement
                    .executeQuery(selectQuery);
            int clientCounter=0;
            while (rset.next()) {
                clientList[clientCounter]=
                                rset.getString("ID")+"#"+
                                rset.getString("MACADDRESS")+"#"+
                                rset.getString("IP")+"#"+
                                rset.getString("AREA")+"#"+
                                rset.getString("LASTTIMEOFRUNNING")+"#"+
                                rset.getString("LASTTIMEOFSERVICE");
                clientCounter++;
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
        return clientList;
    }
    public  String   getTelBankIP(String clientID){
        Statement preparedStatement = null;
        ResultSet rset = null;
        String ip="!";

        try {


            String selectQuery = "SELECT * FROM  TBL_RMICLIENTS  where ID= "+clientID;
            rset = preparedStatement
                    .executeQuery(selectQuery);
            int clientCounter=0;
            while (rset.next()) {
                ip=rset.getString("IP");
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
        return ip;
    }
    private boolean ConnectedToDB() throws SQLException {
         DBURL = Properties_Monitoring.getOracle_DB_URL();
         DBUSER = Properties_Monitoring.getOracle_DB_UserName();
         DBPASS = Properties_Monitoring.getOracle_DB_Password();

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
            System.out.println(e.getMessage());
            connection = null;
            return false;
        }
    }
    private boolean ConnectedToSQLServerDB(String db) throws SQLException, ClassNotFoundException{

         DBUSER = Properties_Monitoring.getSqlServer_DB_USER();
         DBPASS = Properties_Monitoring.getSqlServer_DB_PASSWORD();
         url =    Properties_Monitoring.getSqlServer_DB_URL()+db;
         Driver=  Properties_Monitoring.getSqlServer_DB_DRIVER();

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

    private Timestamp getCurrentTimeStamp() {

        java.util.Date today = new java.util.Date();
        long t = today.getTime();
        return new Timestamp(t);

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

    public InternalFollowUp internalFollowUpTransaction(InternalFollowUp followUpTransaction) throws SQLException {

        Statement preparedStatement = null;
        ResultSet rset = null;

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
                followUpTransaction.setstatus(getStatusFromActionCode(rset.getString("ACTIONCODE")));
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
        return followUpTransaction;
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

    public BillPayByIDAccount accountBillPayByIDAccountCashData(BillPayByIDAccount billPayByIDAccount) throws SQLException {

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


}