import java.sql.*;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Administrator on 1/27/2017.
 */
public class Ghabz {


    public int iCount;
    GhabzObject[] ghabzObjects =new GhabzObject[300];
    private Connection connectionToSQLServer = null;
    private Statement stForSQLServer = null;
    private ResultSet resultSetForSQLServer = null;
    int threadNumber=0;
    int runCount=0;

    private Connection connectionToOracle = null;

    private PreparedStatement preparedStatement = null;

    private  java.sql.Timestamp getCurrentTimeStamp() {

        java.util.Date today = new java.util.Date();
        long t = today.getTime();
        return new java.sql.Timestamp(t);

    }
    private static Timestamp getDateTimeOFTransaction(String shamsiDate,String shamsiTime){
        PersianDateTime persianDateTime=new PersianDateTime();
        try{
            int year=Integer.valueOf("13"+shamsiDate.substring(0,2));
            int month=Integer.valueOf(shamsiDate.substring(2,4));
            int day=Integer.valueOf(shamsiDate.substring(4,6));

            persianDateTime.setIranianDate(year,month,day);
            String strDate=persianDateTime.getGregorianDate();
            DateFormat format=new SimpleDateFormat("yyyy/mm/dd hhmmss");
            java.util.Date date=(java.util.Date)format.parse(strDate+" "+shamsiTime);
            return new Timestamp(date.getTime());

        }catch (Exception e){
            return null;
        }
    }
    private  java.util.Date getDateOFTransaction(String shamsiDate){
        PersianDateTime persianDateTime=new PersianDateTime();
        try{
            int year=Integer.valueOf("13"+shamsiDate.substring(0,2));
            int month=Integer.valueOf(shamsiDate.substring(2,4));
            int day=Integer.valueOf(shamsiDate.substring(4,6));

            persianDateTime.setIranianDate(year,month,day);
            String strDate=persianDateTime.getGregorianDate();
            DateFormat format=new SimpleDateFormat("yyyy/mm/dd");
            java.util.Date date=(java.util.Date)format.parse(strDate);
            return date;

        }catch (Exception e){
            return null;
        }
    }
    private  java.sql.Date getDateFromShamsiDate(String shamsiDate) {


        PersianDateTime persianDateTime=new PersianDateTime();
        try{
            int year=Integer.valueOf("13"+shamsiDate.substring(0,2));
            int month=Integer.valueOf(shamsiDate.substring(2,4));
            int day=Integer.valueOf(shamsiDate.substring(4,6));

            persianDateTime.setIranianDate(year,month,day);
            String strDate=persianDateTime.getGregorianDate();
            DateFormat format=new SimpleDateFormat("yyyy/mm/dd");
            java.util.Date date=(java.util.Date)format.parse(strDate);
            java.sql.Date sqlDate = new java.sql.Date(date.getTime());
            return sqlDate;

        }catch (Exception e){
            return null;
        }

    }
    public   String getMsgSequence() {

        return String.valueOf(System.nanoTime()).substring(0,12);//144525438988

    }

    public Ghabz(int threadNumber) throws SQLException, InterruptedException {
        this.threadNumber=threadNumber;
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
            runCount++;
        }
    }
    private boolean  rollBack(int i){
        boolean cashed=false;
        try{
            connectionToSQLServer.setAutoCommit(false);
            stForSQLServer = connectionToSQLServer.createStatement(ResultSet.TYPE_FORWARD_ONLY, ResultSet.CONCUR_UPDATABLE);
            resultSetForSQLServer = stForSQLServer.executeQuery("SELECT * FROM [MasterDatabase].[dbo].[PrivilegeRegistration] " +
                    "WHERE [fldMainAccountNo]=" + ghabzObjects[i].AccNoBed + " for UPDATE"); // AccNo
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
                    "   [ID]" +
                    "  ,[fldPeriod]" +
                    "  ,[fldCheckPBNo]" +
                    "  ,[Device]" +
                    "  ,[CodeAmaliat]" +
                    "  ,[AccNoBed]" +
                    "  ,[AccNoBes]" +
                    "  ,[MBed]" +
                    "  ,[ShPardakht]" +
                    "  ,[ShGhabz]" +
                    "  ,[Date]" +
                    "  ,[Time]" +
                    "  ,[TextFlag]" +
                    "  ,[fldHeaderDate]" +
                    "  ,[fldPBDoneFlag]" +

                    " FROM [SpecialDataBase].[dbo].[Gabz] WHERE [TextFlag]<>7 and date>=940101 for UPDATE");
            int i=0;
            while (resultSetForSQLServer.next()) { // end of modification by b.r
                ghabzObjects[i]=new GhabzObject();
                ghabzObjects[i].ID=String.valueOf(resultSetForSQLServer.getString("fldPBDoneFlag"));
                ghabzObjects[i].fldPeriod=String.valueOf(resultSetForSQLServer.getString("fldPeriod"));
                ghabzObjects[i].fldCheckPBNo=String.valueOf(resultSetForSQLServer.getString("fldCheckPBNo"));
                ghabzObjects[i].Device=String.valueOf(resultSetForSQLServer.getString("Device"));
                ghabzObjects[i].CodeAmaliat=String.valueOf(resultSetForSQLServer.getString("CodeAmaliat"));
                ghabzObjects[i].AccNoBed=String.valueOf(resultSetForSQLServer.getString("AccNoBed"));
                ghabzObjects[i].AccNoBes=String.valueOf(resultSetForSQLServer.getString("AccNoBes"));
                ghabzObjects[i].MBed=String.valueOf(resultSetForSQLServer.getString("MBed"));
                ghabzObjects[i].ShPardakht=String.valueOf(resultSetForSQLServer.getString("ShPardakht"));
                ghabzObjects[i].ShGhabz=String.valueOf(resultSetForSQLServer.getString("ShGhabz"));
                ghabzObjects[i].Date=String.valueOf(resultSetForSQLServer.getString("Date"));
                ghabzObjects[i].Time=String.valueOf(resultSetForSQLServer.getString("Time"));
                ghabzObjects[i].TextFlag=String.valueOf(resultSetForSQLServer.getString("TextFlag"));
                ghabzObjects[i].fldHeaderDate=String.valueOf(resultSetForSQLServer.getString("fldHeaderDate"));
                ghabzObjects[i].fldPBDoneFlag=String.valueOf(resultSetForSQLServer.getString("fldPBDoneFlag"));

                resultSetForSQLServer.updateString("TextFlag", "7");
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
            System.out.println("1"+e.toString());
            return false;
        }catch (Exception e) {
            if (stForSQLServer != null) {try{stForSQLServer.close();stForSQLServer=null;}catch (Exception var2){}}
            if (connectionToSQLServer != null) {try{connectionToSQLServer.close();connectionToSQLServer=null;}catch (Exception var1){}}
            if (resultSetForSQLServer != null) {try{resultSetForSQLServer.close();resultSetForSQLServer=null;}catch (Exception var1){}}
            System.out.println("2"+e.toString());
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
            if (!insertToOracleIsSuccess(i));// rollBack(i);
            else{
                iCount++;
                System.out.println("Count OF record inseted To TBL_Master is :" + Properties_DBMigration.MasterUpdateCount);
            }

        }
        if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
        if (connectionToOracle != null) {try{connectionToOracle.close();connectionToOracle=null;}catch (Exception var1){}}
        return true;
    }
    private  String getSequenceNumber(int i){
        String id=String.valueOf(i);
        id=String.valueOf(threadNumber)+String.valueOf(runCount)+id;
        String sequence="1";
        for (int j=id.length();j<=10;j++){
            sequence=sequence+"0";
        }
        sequence=sequence+id;
        return sequence;
    }
    private String isAccount(int i){
        if (ghabzObjects[i].AccNoBed.trim().length()>13) return "0";
        else return "1";
    }
    private String getActionCodeFromDoneFlag(String doneflag){
        if (doneflag.equals("1")){
            return "0000";
        }else if (doneflag.equals("2")){
            return "9126";
        }else if (doneflag.equals("3")){
            return "1000";
        }else{
            return "6666";
        }
    }

    private boolean  insertToOracleIsSuccess(int i){

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

                boolean isAccount=isAccount(i)=="1";
                String codeAmaliat=ghabzObjects[i].CodeAmaliat;
                preparedStatement = connectionToOracle.prepareStatement(insertPayment);

                preparedStatement.setString(1, getMsgSequence());
                preparedStatement.setString(2, isAccount(i));
                preparedStatement.setString(3, getActionCodeFromDoneFlag(ghabzObjects[i].fldPBDoneFlag.trim()));
                preparedStatement.setTimestamp(4,getCurrentTimeStamp());
                preparedStatement.setString(5,"1");
                if (isAccount){
                    preparedStatement.setString(6, ghabzObjects[i].fldCheckPBNo.trim());
                }else preparedStatement.setString(6, ghabzObjects[i].AccNoBes.trim());

                preparedStatement.setString(7, ghabzObjects[i].Date.trim());
                preparedStatement.setString(8, ghabzObjects[i].Time.trim());
                preparedStatement.setString(9, ghabzObjects[i].AccNoBed.trim());
                preparedStatement.setString(10, "111111111111");
                preparedStatement.setString(11, ghabzObjects[i].ShGhabz.trim());
                preparedStatement.setString(12, ghabzObjects[i].ShPardakht.trim());
                preparedStatement.setString(13, ghabzObjects[i].MBed.trim());
                if (codeAmaliat.equals("01")){
                    preparedStatement.setString(14, "1");
                }else preparedStatement.setString(14, "0");
                if (codeAmaliat.equals("02")){
                    preparedStatement.setString(15, "1");
                }else preparedStatement.setString(15, "0");
                if (codeAmaliat.equals("03")){
                    preparedStatement.setString(16, "1");
                }else preparedStatement.setString(16, "0");
                if (codeAmaliat.equals("04")){
                    preparedStatement.setString(17, "1");
                }else preparedStatement.setString(17, "0");
                if (!isAccount){
                    preparedStatement.setString(18, "1");
                }else preparedStatement.setString(18, "0");
                preparedStatement.setDate(19, (Date) getDateFromShamsiDate(ghabzObjects[i].Date.trim()));
                preparedStatement.setString(20, ghabzObjects[i].fldPBDoneFlag.trim());
                if (isAccount){
                    preparedStatement.setString(21, ghabzObjects[i].AccNoBed.trim());
                }else preparedStatement.setString(21, "0");

                preparedStatement.setString(22, "0");

                preparedStatement.executeUpdate();


        }catch (SQLException e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
//            if (connectionToOracle != null) {try{connectionToOracle.close();connectionToOracle=null;}catch (Exception var1){}}
            System.out.println("3"+e.toString());

            return false;
        }catch (Exception e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
//            if (connectionToOracle!= null) {try{connectionToOracle.close();connectionToOracle=null;}catch (Exception var1){}}
            System.out.println("4"+e.toString());

            return false;
        }finally {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
//            if (connectionToOracle!= null) {try{connectionToOracle.close();connectionToOracle=null;}catch (Exception var1){}}
            Properties_DBMigration.MasterUpdateCount++;
            return true;
        }



    }

}
