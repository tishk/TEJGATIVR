package ServiceObjects.Other;

import ServiceObjects.Account.BillPayByIDAccount;
import ServiceObjects.Account.FundTransfer;
import utils.PropertiesUtils;
import utils.strUtils;

import java.io.IOException;
import java.sql.*;
import java.sql.Date;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Administrator on 12/7/2015.
 */
public class RetryPayment {

    private RetryPayment retryPayment;
    private Connection connection =null;
    private strUtils strutils=new strUtils();
    private boolean ConnectedToDB() throws SQLException {
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
                DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
                connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
                return true;
            }
            else
            {
                connection.close();
                DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
                connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
                return true;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            connection=null;
            return false;
        }
    }
    private void startFollowUp() throws ExecutionException, InterruptedException {
        boolean ResultOfCreateReceiveFromSabaRunning=false;

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future future = executorService.submit(new Runnable() {
            public void run() {
                try {
                    doFollowup();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        if  (future.get()!= null) ResultOfCreateReceiveFromSabaRunning=false; else ResultOfCreateReceiveFromSabaRunning=true;
        // return ResultOfCreateReceiveFromSabaRunning && receiveFromSaba.ResultOfRunning;
        return;
    }
    private void doFollowup(){



    }

    private void accountFundTransferCashData(FundTransfer fundTransfer) throws SQLException {

        Statement preparedStatement = null;
        ResultSet rset =null;

        String selectQuery = "SELECT * FROM  TBL_PAYMENT  " +
                "WHERE FOLLOWCODE =1000" ;
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
        }catch (SQLException e) {
            if (preparedStatement != null) {
                try{
                    preparedStatement.close();
                    preparedStatement=null;
                }catch (Exception var2){}
            }
            if (connection != null) {
                try{
                    connection.close();
                    connection=null;
                }catch (Exception var1){}
            }
            if (rset != null) {
                try{
                    rset.close();
                    rset=null;
                }catch (Exception var2){}
            }
            fundTransfer.setDataFromDbChashed(false);
        }catch (Exception e) {
            if (preparedStatement != null) {
                try{
                    preparedStatement.close();
                    preparedStatement=null;
                }catch (Exception var2){}
            }
            if (connection != null) {
                try{
                    connection.close();
                    connection=null;
                }catch (Exception var1){}
            }
            if (rset != null) {
                try{
                    rset.close();
                    rset=null;
                }catch (Exception var2){}
            }
            fundTransfer.setDataFromDbChashed(false);
        }finally {
            if (preparedStatement != null) {
                try{
                    preparedStatement.close();
                    preparedStatement=null;
                }catch (Exception var2){}
            }
            if (connection != null) {
                try{
                    connection.close();
                    connection=null;
                }catch (Exception var1){}
            }
            if (rset != null) {
                try{
                    rset.close();
                    rset=null;
                }catch (Exception var2){}
            }
            fundTransfer.setDataFromDbChashed(true);
        }

    }
    public  void accountBillPayByIDAccountCashData(BillPayByIDAccount billPayByIDAccount) throws SQLException {

        Statement preparedStatement = null;
        ResultSet rset =null;

        String selectQuery = "SELECT * FROM  TBL_SPECIALSERVICEINFO  " +
                "WHERE fldMainAccountNo ="+billPayByIDAccount.getSourceAccount();
        try {

            preparedStatement = connection.createStatement();
            rset = preparedStatement.executeQuery(selectQuery);
            while (rset.next()) {
                billPayByIDAccount.setIsRegistered(true);
                billPayByIDAccount.setServiceTypes(rset.getString("SERVICETYPES"));
                billPayByIDAccount.setNationalCode(rset.getString("NATIONALCODE"));
            }
            selectQuery = "SELECT * FROM  TBL_Bill  " +
                    "WHERE ShGhabz ="+billPayByIDAccount.getBillID()+
                    " and ShPardakht="+billPayByIDAccount.getPaymentID()+
                    " and (fldPBDoneFlag = 1 OR fldPBDoneFlag = 2 OR fldPBDoneFlag = 3)";
            preparedStatement = connection.createStatement();
            rset = preparedStatement.executeQuery(selectQuery);
            billPayByIDAccount.sethasAlreadyBeenPaid(false);
            while (rset.next()) {
                billPayByIDAccount.sethasAlreadyBeenPaid(true);
            }
            if (!billPayByIDAccount.gethasAlreadyBeenPaid()){
                String billId=billPayByIDAccount.getBillID();
                selectQuery = "SELECT * From ServersInfo " +
                        "WHERE fldServiceCode ="+billId.charAt(billId.length()-2)+
                        " and  fldSubServerCode="+strutils.midString(billId, billId.length()-4,3)+
                        " and  fldEnabled = 1";
                preparedStatement = connection.createStatement();
                rset = preparedStatement.executeQuery(selectQuery);
                billPayByIDAccount.setSubServiceNotDefined(false);
                String fldacc="";
                boolean notIsEmpty=false;
                while (rset.next()) {
                    notIsEmpty=true;
                    try {
                        if (Long.valueOf(rset.getString("fldAccountNo"))>Long.valueOf("0")){

                        }else{
                            billPayByIDAccount.setSubServiceNotDefined(true);
                        }
                    }catch (Exception e){
                        billPayByIDAccount.setSubServiceNotDefined(true);
                    }
                }
                if (!notIsEmpty) billPayByIDAccount.setSubServiceNotDefined(true);
            }
        }catch (SQLException e) {
            if (preparedStatement != null) {
                try{
                    preparedStatement.close();
                    preparedStatement=null;
                }catch (Exception var2){}
            }
            if (connection != null) {
                try{
                    connection.close();
                    connection=null;
                }catch (Exception var1){}
            }
            if (rset != null) {
                try{
                    rset.close();
                    rset=null;
                }catch (Exception var2){}
            }
            billPayByIDAccount.setDataFromDbChashed(false);
        }catch (Exception e) {
            if (preparedStatement != null) {
                try{
                    preparedStatement.close();
                    preparedStatement=null;
                }catch (Exception var2){}
            }
            if (connection != null) {
                try{
                    connection.close();
                    connection=null;
                }catch (Exception var1){}
            }
            if (rset != null) {
                try{
                    rset.close();
                    rset=null;
                }catch (Exception var2){}
            }
            billPayByIDAccount.setDataFromDbChashed(false);
        }finally {
            if (preparedStatement != null) {
                try{
                    preparedStatement.close();
                    preparedStatement=null;
                }catch (Exception var2){}
            }
            if (connection != null) {
                try{
                    connection.close();
                    connection=null;
                }catch (Exception var1){}
            }
            if (rset != null) {
                try{
                    rset.close();
                    rset=null;
                }catch (Exception var2){}
            }
            billPayByIDAccount.setDataFromDbChashed(true);
        }
        //return billPayByIDAccount;
    }
    public  boolean changePriority(String acc) throws SQLException {
        boolean res=false;

        Statement st =null;
        try{
            st=connection.createStatement();
            st.executeUpdate( "update TBL_SPECIALSERVICEINFO Set RegisteredInCM=0 ,Priority=Priority+1 " +
                    "Where fldMainAccountNo="+acc);
        }catch (SQLException e) {

            if (connection != null) {
                try{
                    connection.close();
                    connection=null;
                }catch (Exception var1){}
            }
            if (st != null) {
                try{
                    st.close();
                    st=null;
                }catch (Exception var2){}
            }
            res=false;

        }catch (Exception e) {
            if (connection != null) {
                try{
                    connection.close();
                    connection=null;
                }catch (Exception var1){}
            }
            if (st != null) {
                try{
                    st.close();
                    st=null;
                }catch (Exception var2){}
            }
            res=false;
        }finally {
            if (connection != null) {
                try{
                    connection.close();
                    connection=null;
                }catch (Exception var1){}
            }
            if (st != null) {
                try{
                    st.close();
                    st=null;
                }catch (Exception var2){}
            }
            res=true;
        }
        return res;
    }

    private void    checkIfNeedFollowUp() throws IOException {

        int delay = 1000*60*60;
        int period = 1000*60*10;

        Timer timer = new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            public void run() {
                if (getNowHour()==3) {

                    tryFollowUp();
                }
            }
        }, delay, period);
    }

    private void tryFollowUp() {

    }

    public  int     getNowHour(){
        java.util.Date Time=new java.util.Date();
        SimpleDateFormat DateFormat =new SimpleDateFormat ("HH");
        String Now=DateFormat.format(Time);
        return Integer.valueOf(Now);
    }


}
