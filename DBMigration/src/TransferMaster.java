import java.sql.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Administrator on 6/14/2016.
 */
public class TransferMaster {

    String AccNo="";
    String BranchNo="";
    String Name="";
    String PrimaryPassword="";
    String Password="";
    String Suffix="";
    String HesabGroup="";
    int    RegDate=0;
    int    CenterCode=0;
    int    ChangeDate=0;
    String Family="";
    String BitChange="";
    String AvaCasActive="";
    String SendToAvaCas="";
    int    OrmMsgID=0;
    String CreateDate;
    String UpdateDate;
    String SendToOracle;

    public int iCount;

    private   Connection connectionToSQLServer = null;
    private   Statement  stForSQLServer =null;
    private   ResultSet  resultSetForSQLServer=null;

    private   Connection connectionToOracle = null;

    private   PreparedStatement preparedStatement = null;

    private  java.sql.Timestamp getCurrentTimeStamp() {

        java.util.Date today = new java.util.Date();
        long t = today.getTime();
        return new java.sql.Timestamp(t);

    }

    public TransferMaster() throws SQLException, InterruptedException {
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
            String url = "jdbc:jtds:sqlserver://10.39.172.91:1433/"+dbName;
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
        if (connectedToSQLServer())
            if (connectedToOracle())
                while (true){
                    if (cashFromSQLServerIsSuccess())
                       if (!insertToOracleIsSuccess()) rollBack();
                               else iCount++;
            //System.out.println("Count OF record inseted To TBL_Master is :" + Properties_DBMigration.MasterUpdateCount);
           //Thread.wait
                }
    }

    private boolean  rollBack(){
        boolean cashed=false;
        try{
            connectionToSQLServer.setAutoCommit(false);
            stForSQLServer = connectionToSQLServer.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE);
            resultSetForSQLServer = stForSQLServer.executeQuery("SELECT * FROM [MasterDatabase].[dbo].[Master] WHERE [AccNo]="+AccNo+" for UPDATE");
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
    private boolean cashFromSQLServerIsSuccess() throws SQLException {
        boolean cashed=false;
        try{
        connectionToSQLServer.setAutoCommit(false);
        stForSQLServer = connectionToSQLServer.createStatement(ResultSet.TYPE_FORWARD_ONLY,ResultSet.CONCUR_UPDATABLE);
        resultSetForSQLServer = stForSQLServer.executeQuery("SELECT TOP 1 " +
                "       [AccNo]" +
                "      ,[BranchNo]" +
                "      ,[Name]" +
                "      ,[PrimaryPassword]" +
                "      ,[Password]" +
                "      ,[Suffix]" +
                "      ,[HesabGroup]" +
                "      ,[RegDate]" +
                "      ,[CenterCode]" +
                "      ,[ChangeDate]" +
                "      ,[Family]" +
                "      ,[BitChange]" +
                "      ,[AvaCasActive]" +
                "      ,[SendToAvaCas]" +
                "      ,[OrmMsgID]" +
                "      ,[CreateDate]" +
                "      ,[UpdateDate]" +
                "      ,[SendToOracle]" +
                "  FROM [MasterDatabase].[dbo].[Master] WHERE [SendToOracle]<>1 for UPDATE");
        while (resultSetForSQLServer.next()) {
            AccNo=String.valueOf(resultSetForSQLServer.getString("AccNo"));
            BranchNo=String.valueOf(resultSetForSQLServer.getString("BranchNo"));
            Name=String.valueOf(resultSetForSQLServer.getString("Name"));
            PrimaryPassword=String.valueOf(resultSetForSQLServer.getString("PrimaryPassword"));
            Password=String.valueOf(resultSetForSQLServer.getString("Password"));
            Suffix=String.valueOf(resultSetForSQLServer.getString("Suffix"));
            HesabGroup=String.valueOf(resultSetForSQLServer.getString("HesabGroup"));
            RegDate=resultSetForSQLServer.getInt("RegDate");
            CenterCode=resultSetForSQLServer.getInt("CenterCode");
            ChangeDate=resultSetForSQLServer.getInt("ChangeDate");
            Family=String.valueOf(resultSetForSQLServer.getString("Family"));
            BitChange=String.valueOf(resultSetForSQLServer.getString("BitChange"));
            if (BitChange.length()>1) BitChange="0";
            AvaCasActive=String.valueOf(resultSetForSQLServer.getString("AvaCasActive"));
            if (AvaCasActive.length()>1) AvaCasActive="0";
            SendToAvaCas=String.valueOf(resultSetForSQLServer.getString("SendToAvaCas"));
            if (SendToAvaCas.length()>1) SendToAvaCas="0";
            OrmMsgID=resultSetForSQLServer.getInt("OrmMsgID");
            CreateDate=String.valueOf(resultSetForSQLServer.getString("CreateDate"));
            UpdateDate=String.valueOf(resultSetForSQLServer.getString("UpdateDate"));
            if (UpdateDate.equals("null"))
                UpdateDate = "2000-01-01 05:38:09.193";

            SendToOracle=String.valueOf(resultSetForSQLServer.getString("SendToOracle"));
            resultSetForSQLServer.updateString("SendToOracle", "1");
            resultSetForSQLServer.updateRow();
            cashed=true;

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
//            if (stForSQLServer != null) {try{stForSQLServer.close();stForSQLServer=null;}catch (Exception var2){}}
//            if (connectionToSQLServer != null) {try{connectionToSQLServer.close();connectionToSQLServer=null;}catch (Exception var1){}}
//            if (resultSetForSQLServer != null) {try{resultSetForSQLServer.close();resultSetForSQLServer=null;}catch (Exception var1){}}
//
            if (!cashed) return false;
            else return true;
        }
    }
    private boolean    insertToOracleIsSuccess(){


        String insertTable = "INSERT INTO TBL_MASTER"
                +" (ACCNO, BRANCHNO, NAME,PRIMARYPASSWORD, PASSWORD,SUFFIX,HESABGROUP,REGDATE, CENTERCODE, CHANGEDATE,FAMILY, BITCHANGE,AVACASACTIVE,SENDTOAVACAS,ORMMSGID,CREATEDATE,UPDATEDATE) VALUES"
                + "(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try {

            preparedStatement = connectionToOracle.prepareStatement(insertTable);

            preparedStatement.setString(1,AccNo );
            preparedStatement.setString(2,BranchNo );
            preparedStatement.setString(3,Name );
            preparedStatement.setString(4,PrimaryPassword);
            preparedStatement.setString(5,Password );
            preparedStatement.setString(6,Suffix );
            preparedStatement.setString(7,HesabGroup);
            preparedStatement.setInt(8,RegDate );
            preparedStatement.setInt(9,CenterCode );
            preparedStatement.setInt(10,ChangeDate);
            preparedStatement.setString(11,Family );
            preparedStatement.setString(12,BitChange );
            preparedStatement.setString(13,AvaCasActive);
            preparedStatement.setString(14,SendToAvaCas );
            preparedStatement.setInt(15,OrmMsgID);
            try{
                preparedStatement.setTimestamp(16,Timestamp.valueOf(CreateDate ));
            }catch (Exception e){
                preparedStatement.setTimestamp(16,getCurrentTimeStamp());
            }
            try{
                preparedStatement.setTimestamp(17,Timestamp.valueOf(UpdateDate ));
            }catch (Exception e){
                preparedStatement.setTimestamp(17,getCurrentTimeStamp());
            }

            preparedStatement.executeUpdate();

        }catch (SQLException e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connectionToOracle != null) {try{connectionToOracle.close();connectionToOracle=null;}catch (Exception var1){}}
            System.out.println(e.toString());
            return false;
        }catch (Exception e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connectionToOracle != null) {try{connectionToOracle.close();connectionToOracle=null;}catch (Exception var1){}}
            System.out.println(e.toString());
            return false;
        }finally {
//            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
//            if (connectionToOracle != null) {try{connectionToOracle.close();connectionToOracle=null;}catch (Exception var1){}}

            Properties_DBMigration.MasterUpdateCount++;
            return true;
        }
    }

}
