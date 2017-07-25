
import java.sql.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Administrator on 6/14/2016.
 */

public class TransferPrivilageRegistration {
    PrivilegeObject[] privilegeObject=new PrivilegeObject[300];
   // Foo[] foo = new Foo[12];
    public int iCount;

    private Connection connectionToSQLServer = null;
    private Statement stForSQLServer = null;
    private ResultSet resultSetForSQLServer = null;

    private Connection connectionToOracle = null;

    private PreparedStatement preparedStatement = null;

    private  java.sql.Timestamp getCurrentTimeStamp() {

        java.util.Date today = new java.util.Date();
        long t = today.getTime();
        return new java.sql.Timestamp(t);

    }

    public TransferPrivilageRegistration() throws SQLException, InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future future = executorService.submit(new Runnable() {
            public void run() {
                try {
                    start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private boolean connectedToSQLServer(){
        try {
            String dbName = "MasterDatabase";
            String driver = "net.sourceforge.jtds.jdbc.Driver";
            String userName = "sa";
            String password = "tb4tej@rat";
            String url = "jdbc:jtds:sqlserver://10.39.172.91:1433/" + dbName;
            if (connectionToSQLServer==null) {
                System.gc();
                Class.forName(driver);
                connectionToSQLServer= DriverManager.getConnection(url, userName, password);
                return true;
            }
            else if (connectionToSQLServer.isClosed()){
                connectionToSQLServer=null;
                System.gc();
                Class.forName(driver);
                connectionToSQLServer= DriverManager.getConnection(url, userName, password);
                return true;
            }
            else
            {
                connectionToSQLServer.close();
                connectionToSQLServer=null;
                System.gc();
                Class.forName(driver);
                connectionToSQLServer= DriverManager.getConnection(url, userName, password);
                return true;
            }

        } catch (Exception e) {
            System.out.println(e.toString());

            return false;
        }
    }
    private boolean connectedToOracle() throws SQLException {
        String DBURL= "jdbc:oracle:thin:@//10.39.41.61:1521/telbank";
        String DBUSER="system";
        String DBPASS="tb4tej2rat";

        try {
            if (connectionToOracle==null) {
                DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
                connectionToOracle = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
                return true;
            }
            else if (connectionToOracle.isClosed()){
                connectionToOracle=null;
                DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
                connectionToOracle = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
                return true;
            }
            else
            {
                connectionToOracle.close();
                connectionToOracle=null;
                DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
                connectionToOracle = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
                return true;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            connectionToOracle=null;
            return false;
        }
    }
    private void start() throws InterruptedException, SQLException {
        iCount = 0;

                while (true){
                    if (connectedToSQLServer())
                        if (connectedToOracle())
                           if (cashFromSQLServerIsSuccess())
                              processInserting();

                    //Thread.wait
                }
    }

    private boolean  rollBack(int i){
        boolean cashed=false;
        try{
            connectionToSQLServer.setAutoCommit(false);
            stForSQLServer = connectionToSQLServer.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            resultSetForSQLServer = stForSQLServer.executeQuery("SELECT * FROM [MasterDatabase].[dbo].[PrivilegeRegistration] WHERE [fldMainAccountNo]=" + privilegeObject[i].fldMainAccountNo + " for UPDATE"); // AccNo
            while (resultSetForSQLServer.next()) {

                resultSetForSQLServer.updateString("SendToOracle", "0");
                resultSetForSQLServer.updateRow();


            }
            connectionToSQLServer.commit();
            connectionToSQLServer.setAutoCommit(true);
        }catch (SQLException e) {
            if (stForSQLServer != null) {try{stForSQLServer.close();stForSQLServer=null;}catch (Exception var2){}}
            if (connectionToSQLServer != null) {try{connectionToSQLServer.close();connectionToSQLServer=null;}catch (Exception var1){}}
            if (resultSetForSQLServer != null) {try{resultSetForSQLServer.close();resultSetForSQLServer=null;}catch (Exception var1){}}
            System.out.println(e.toString());
            return false;
        }catch (Exception e) {
            if (stForSQLServer != null) {try{stForSQLServer.close();stForSQLServer=null;}catch (Exception var2){}}
            if (connectionToSQLServer != null) {try{connectionToSQLServer.close();connectionToSQLServer=null;}catch (Exception var1){}}
            if (resultSetForSQLServer != null) {try{resultSetForSQLServer.close();resultSetForSQLServer=null;}catch (Exception var1){}}
            System.out.println(e.toString());
            return false;
        }finally {
            if (stForSQLServer != null) {try{stForSQLServer.close();stForSQLServer=null;}catch (Exception var2){}}
            if (connectionToSQLServer != null) {try{connectionToSQLServer.close();connectionToSQLServer=null;}catch (Exception var1){}}
            if (resultSetForSQLServer != null) {try{resultSetForSQLServer.close();resultSetForSQLServer=null;}catch (Exception var1){}}
            if (!cashed) return false;
            else return true;
        }
    }
    private boolean  cashFromSQLServerIsSuccess() throws SQLException {
        boolean cashed=false;
        try{
            connectionToSQLServer.setAutoCommit(false);
            stForSQLServer = connectionToSQLServer.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE);

            resultSetForSQLServer = stForSQLServer.executeQuery("SELECT TOP "+Main.recordCount+"  " +
             "   [fldRegisterationDate]" +
             "  ,[fldCenterCode]" +
             "  ,[fldBranchCode]" +
             "  ,[fldMainAccountNo]" +
             "  ,[fldNationalCode]" +
             "  ,[fldPersianName]" +
             "  ,[fldServiceType]" +
             "  ,[fldFirstAccountNo]" +
             "  ,[fldSecondAccountNo]" +
             "  ,[fldThirdAccountNo]" +
             "  ,[fldFourthAccountNo]" +
             "  ,[fldFifthAccountNo]" +
             "  ,[fldAccountGroup]" +
             "  ,[fldPassword]" +
             "  ,[fldPrintFlag]" +
             "  ,[IsFreeTransfer]" +
             "  ,[LastChangePin]" +
             "  ,[RegisteredInCM]" +
             "  ,[Priority]" +
             "  ,[IsActive]" +
             "  ,[OnlineState]" +
             "  ,[OrmMsgID]" +
             "  ,[MyPriority]" +
             "  ,[AvaCasActive]" +
             "  ,[SendToAvaCas]" +
             "  ,[SendToOracle]" +
             "  FROM [MasterDatabase].[dbo].[PrivilegeRegistration] WHERE [SendToOracle]<>1 for UPDATE");
            int i=0;
            while (resultSetForSQLServer.next()) { // end of modification by b.r
                privilegeObject[i]=new PrivilegeObject();
                privilegeObject[i].fldRegisterationDate=String.valueOf(resultSetForSQLServer.getString("fldRegisterationDate"));
                privilegeObject[i].fldCenterCode=String.valueOf(resultSetForSQLServer.getString("fldCenterCode"));
                privilegeObject[i].fldBranchCode=String.valueOf(resultSetForSQLServer.getString("fldBranchCode"));
                privilegeObject[i].fldMainAccountNo=String.valueOf(resultSetForSQLServer.getString("fldMainAccountNo"));
                privilegeObject[i].fldNationalCode=String.valueOf(resultSetForSQLServer.getString("fldNationalCode"));
                privilegeObject[i].fldPersianName=String.valueOf(resultSetForSQLServer.getString("fldPersianName"));
                privilegeObject[i].fldServiceType=String.valueOf(resultSetForSQLServer.getString("fldServiceType"));
                privilegeObject[i].fldFirstAccountNo=String.valueOf(resultSetForSQLServer.getString("fldFirstAccountNo"));
                privilegeObject[i].fldSecondAccountNo=String.valueOf(resultSetForSQLServer.getString("fldSecondAccountNo"));
                privilegeObject[i].fldThirdAccountNo=String.valueOf(resultSetForSQLServer.getString("fldThirdAccountNo"));
                privilegeObject[i].fldFourthAccountNo=String.valueOf(resultSetForSQLServer.getString("fldFourthAccountNo"));
                privilegeObject[i].fldFifthAccountNo=String.valueOf(resultSetForSQLServer.getString("fldFifthAccountNo"));
                privilegeObject[i].fldAccountGroup=String.valueOf(resultSetForSQLServer.getString("fldAccountGroup"));
                privilegeObject[i].fldPassword=String.valueOf(resultSetForSQLServer.getString("fldPassword"));
                privilegeObject[i].fldPrintFlag=String.valueOf(resultSetForSQLServer.getString("fldPrintFlag"));
                privilegeObject[i].IsFreeTransfer=String.valueOf(resultSetForSQLServer.getString("IsFreeTransfer"));
                privilegeObject[i].LastChangePin=String.valueOf(resultSetForSQLServer.getString("LastChangePin"));
                privilegeObject[i].RegisteredInCM=String.valueOf(resultSetForSQLServer.getString("RegisteredInCM"));
                privilegeObject[i].Priority=Integer.valueOf(resultSetForSQLServer.getString("Priority"));
                privilegeObject[i].IsActive=String.valueOf(resultSetForSQLServer.getString("IsActive"));
                privilegeObject[i].OnlineState=String.valueOf(resultSetForSQLServer.getString("OnlineState"));
                privilegeObject[i].OrmMsgID=Integer.valueOf(resultSetForSQLServer.getString("OrmMsgID"));
                privilegeObject[i].MyPriority=String.valueOf(resultSetForSQLServer.getString("MyPriority"));
                privilegeObject[i].AvaCasActive=String.valueOf(resultSetForSQLServer.getString("AvaCasActive"));
                 if (privilegeObject[i].AvaCasActive.length()>1) privilegeObject[i].AvaCasActive="0";
                privilegeObject[i].SendToAvaCas=String.valueOf(resultSetForSQLServer.getString("SendToAvaCas"));
                 if (privilegeObject[i].SendToAvaCas.length()>1) privilegeObject[i].SendToAvaCas="0";
                privilegeObject[i].SendToOracle=String.valueOf(resultSetForSQLServer.getString("SendToOracle"));
                 resultSetForSQLServer.updateString("SendToOracle", "1");
                 resultSetForSQLServer.updateRow();
                 cashed=true;
                i++;
            }
            connectionToSQLServer.commit();
            connectionToSQLServer.setAutoCommit(true);
        }catch (SQLException e) {
            if (stForSQLServer != null) {try{stForSQLServer.close();stForSQLServer=null;}catch (Exception var2){}}
            if (connectionToSQLServer != null) {try{connectionToSQLServer.close();connectionToSQLServer=null;}catch (Exception var1){}}
            if (resultSetForSQLServer != null) {try{resultSetForSQLServer.close();resultSetForSQLServer=null;}catch (Exception var1){}}
            System.out.println(e.toString());
            return false;
        }catch (Exception e) {
            if (stForSQLServer != null) {try{stForSQLServer.close();stForSQLServer=null;}catch (Exception var2){}}
            if (connectionToSQLServer != null) {try{connectionToSQLServer.close();connectionToSQLServer=null;}catch (Exception var1){}}
            if (resultSetForSQLServer != null) {try{resultSetForSQLServer.close();resultSetForSQLServer=null;}catch (Exception var1){}}
            System.out.println(e.toString());
            return false;
        }finally {
            if (stForSQLServer != null) {try{stForSQLServer.close();stForSQLServer=null;}catch (Exception var2){}}
            if (connectionToSQLServer != null) {try{connectionToSQLServer.close();connectionToSQLServer=null;}catch (Exception var1){}}
            if (resultSetForSQLServer != null) {try{resultSetForSQLServer.close();resultSetForSQLServer=null;}catch (Exception var1){}}

            if (!cashed) return false;
            else return true;
        }
    }
    private boolean  processInserting(){
        for (int i=0;i<Integer.valueOf(Main.recordCount);i++){
            if (!insertToOracleIsSuccess(i)) rollBack(i);
            else{
                iCount++;
                System.out.println("Count OF record inseted To TBL_Master is :" + Properties_DBMigration.MasterUpdateCount);
            }

        }
        if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
        if (connectionToOracle != null) {try{connectionToOracle.close();connectionToOracle=null;}catch (Exception var1){}}
        return true;
    }
    private boolean  insertToOracleIsSuccess(int i){


        String insertTable = "INSERT INTO TBL_SPECIALSERVICEINFO"+
                 " (fldRegisterationDate, fldCenterCode, fldBranchCode,fldMainAccountNo," +
                 "fldNationalCode,fldPersianName,fldServiceType,fldFirstAccountNo," +
                 "fldSecondAccountNo, fldThirdAccountNo,fldFourthAccountNo, fldFifthAccountNo," +
                 "fldAccountGroup,fldPassword,fldPrintFlag,IsFreeTransfer,LastChangePin," +
                 "RegisteredInCM,Priority,IsActive,OnlineState,OrmMsgID," +
                 "MyPriority,AvaCasActive,SendToAvaCas) VALUES"+
                 "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try {


            preparedStatement = connectionToOracle.prepareStatement(insertTable);

            preparedStatement.setString(1,privilegeObject[i].fldRegisterationDate );
            preparedStatement.setString(2,privilegeObject[i].fldCenterCode );
            preparedStatement.setString(3,privilegeObject[i].fldBranchCode );
            preparedStatement.setString(4,privilegeObject[i].fldMainAccountNo);
            preparedStatement.setString(5,privilegeObject[i].fldNationalCode );
            preparedStatement.setString(6,privilegeObject[i].fldPersianName );
            preparedStatement.setString(7,privilegeObject[i].fldServiceType);
            preparedStatement.setString(8,privilegeObject[i].fldFirstAccountNo );
            preparedStatement.setString(9,privilegeObject[i].fldSecondAccountNo );
            preparedStatement.setString(10,privilegeObject[i].fldThirdAccountNo);
            preparedStatement.setString(11,privilegeObject[i].fldFourthAccountNo );
            preparedStatement.setString(12,privilegeObject[i].fldFifthAccountNo );
            preparedStatement.setString(13,privilegeObject[i].fldAccountGroup );
            preparedStatement.setString(14,privilegeObject[i].fldPassword );
            preparedStatement.setString(15,privilegeObject[i].fldPrintFlag);
            preparedStatement.setString(16,privilegeObject[i].IsFreeTransfer );
            preparedStatement.setString(17,privilegeObject[i].LastChangePin );
            preparedStatement.setString(18,privilegeObject[i].RegisteredInCM);
            preparedStatement.setInt(19,privilegeObject[i].Priority);
            preparedStatement.setString(20,privilegeObject[i].IsActive );
            preparedStatement.setString(21,privilegeObject[i].OnlineState );
            preparedStatement.setLong(22, privilegeObject[i].OrmMsgID);
            preparedStatement.setString(23,privilegeObject[i].MyPriority );
            preparedStatement.setString(24,privilegeObject[i].AvaCasActive );
            preparedStatement.setString(25,privilegeObject[i].SendToAvaCas );


            preparedStatement.executeUpdate();

        }catch (SQLException e) {
           // if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            //if (connectionToOracle != null) {try{connectionToOracle.close();connectionToOracle=null;}catch (Exception var1){}}
            System.out.println(e.toString());
            return false;
        }catch (Exception e) {
            //if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            //if (connectionToOracle != null) {try{connectionToOracle.close();connectionToOracle=null;}catch (Exception var1){}}
            System.out.println(e.toString());
            return false;
        }finally {
            //if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
           // if (connectionToOracle != null) {try{connectionToOracle.close();connectionToOracle=null;}catch (Exception var1){}}

            Properties_DBMigration.MasterUpdateCount++;
            return true;
        }
    }



}
