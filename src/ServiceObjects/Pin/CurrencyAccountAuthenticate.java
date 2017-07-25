package ServiceObjects.Pin;

import java.sql.*;

/**
 * Created by root on 7/9/17.
 */
public class CurrencyAccountAuthenticate {


    private ResultSet resultSetForOracle = null;
    private Connection connectionToOracle = null;
    private Statement preparedStatement = null;


    private String accountNumber;
    private String pin;
    private String dataBasePin;
    private String actionCode;

    public String getAccountNumber() {
        return accountNumber;
    }

    public void setAccountNumber(String accountNumber) {
        this.accountNumber = accountNumber;
    }

    public String getActionCode() {
        return actionCode;
    }

    public void setActionCode(String actionCode) {
        this.actionCode = actionCode;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

    public String getDataBasePin() {
        return dataBasePin;
    }

    public void setDataBasePin(String dataBasePin) {
        this.dataBasePin = dataBasePin;
    }

    private  java.sql.Timestamp getCurrentTimeStamp() {

        java.util.Date today = new java.util.Date();
        long t = today.getTime();
        return new java.sql.Timestamp(t);

    }



    public CurrencyAccountAuthenticate(String accountNumber,String pin) throws SQLException, InterruptedException {
        this.accountNumber=accountNumber;
        this.pin=pin;
        start();
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

        if (connectedToOracle()){

            cashFromOracleIsSuccess(accountNumber);
            if (dataBasePin.equals(pin)){
                setActionCode("0000");
            }else if (dataBasePin.equals("!")){
                setActionCode("1843");
            }else{
                setActionCode("9902");
            }
        }else{
            setActionCode("9999");
        }






    }
    private boolean cashFromOracleIsSuccess(String accountNumber) throws SQLException {
        boolean cashed=false;
        try{
            String selectQuery="  SELECT PASSWORD FROM TBL_CURRENCY_ACCOUNTS  " +
                    "where AccNo="+"\'"+accountNumber+"\'";
            preparedStatement = connectionToOracle.createStatement();
            resultSetForOracle = preparedStatement
                    .executeQuery(selectQuery);


            while (resultSetForOracle.next()) {
                dataBasePin=resultSetForOracle.getString("PASSWORD");
            }
            connectionToOracle.commit();
            connectionToOracle.setAutoCommit(true);
        }catch (SQLException e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connectionToOracle != null) {try{connectionToOracle.close();connectionToOracle=null;}catch (Exception var1){}}
            if (resultSetForOracle != null) {try{
                resultSetForOracle.close();
                resultSetForOracle =null;}catch (Exception var1){}}
            System.out.println("1"+e.toString());
            return false;
        }catch (Exception e) {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connectionToOracle != null) {try{connectionToOracle.close();connectionToOracle=null;}catch (Exception var1){}}
            if (resultSetForOracle != null) {try{
                resultSetForOracle.close();
                resultSetForOracle =null;}catch (Exception var1){}}
            System.out.println("2"+e.toString());
            return false;
        }finally {
            if (preparedStatement != null) {try{preparedStatement.close();preparedStatement=null;}catch (Exception var2){}}
            if (connectionToOracle != null) {try{connectionToOracle.close();connectionToOracle=null;}catch (Exception var1){}}
            if (resultSetForOracle != null) {try{
                resultSetForOracle.close();
                resultSetForOracle =null;}catch (Exception var1){}}

            if (!cashed) return false;
            else return true;
        }
    }




}
