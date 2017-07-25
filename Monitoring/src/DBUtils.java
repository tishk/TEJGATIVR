

import SystemStatus.Status_All;
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
    Statement st = null;


    public DBUtils(boolean IsOracleDBConnection) throws SQLException, ClassNotFoundException {
        if (IsOracleDBConnection)
            if (ConnectedToDB()) connected = true;
            else connected=false;
        else
        if (ConnectedToSQLServerDB(Properties_Monitoring.getSqlServer_DB_Name())) connected = true;
        else connected=false;

    }
    public DBUtils(final Object objectToSave) throws SQLException, ClassNotFoundException {

                saveObjectToDB(objectToSave);


    }
    public DBUtils(String responseOfInvalidRequest){

    }

    private void saveObjectToDB(Object obj){
        try {
            if (ConnectedToSQLServerDB(Properties_Monitoring.getSqlServer_DB_Name())){
                saveToDB(obj);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public  void saveToDB(Object object) {

        if ( object instanceof Gateway){
            log_Gateway(object);
        }
        else if (object instanceof Channel) {
            log_Channel(object);
        }
        else if (object instanceof CardSwitch){
            log_CardSwitch(object);
        }
        else if (object instanceof TelSwitch){
            log_TelSwitch(object);
        }
        else if (object instanceof Authenticate){
            log_Authenticate(object);
        }
        else if (object instanceof Status_All) {
            log_Status(object);
        }
    }

    private void log_Gateway(Object object){
        Gateway gateway=(Gateway)object;
        try {
            st = connectionToSQL.createStatement();
            if (gateway.getIsAllStatus()){
                st.executeUpdate("update tbl_Status_Gateway Set  " +
                        "gPingStatus='"+gateway.getPingStatus()+"' ," +
                        "gPingStatusDateTime='"+gateway.getPingStatusDateTime()+"' ," +
                        "gPingStatusActionCode='"+gateway.getPingStatusActionCode()+"' ," +
                        "gPingStatusDesc='"+gateway.getPingStatusDesc()+"' ," +
                        "gAppRunning='"+gateway.getAppRunning()+"' ," +
                        "gAppRunningDateTime='"+gateway.getAppRunningDateTIme()+"' ," +
                        "gAppRunningActionCode='"+gateway.getAppRunningActionCode()+"' ," +
                        "gAppRunningDesc='"+gateway.getAppRunningDesc()+"' ," +
                        "gDBConnected='"+gateway.getgDBConnected()+"'," +
                        "gDBConnectedisOKDateTime='"+gateway.getgDBConnectedisOKDateTime()+"' ," +
                        "gDBConnectedisOKActionCode='"+gateway.getgDBConnectedisOKActionCode()+"' ," +
                        "gDBConnectedisOKDesc='"+gateway.getgDBConnectedisOKDesc()+"' where gClientId='002'"
                );
            }else if (gateway.getIsDBStatus()){
                st.executeUpdate("update tbl_Status_Gateway Set " +
                        "gDBConnected="+gateway.getgDBConnected()+" ," +
                        "gDBConnectedisOKDateTime="+gateway.getgDBConnectedisOKDateTime()+" ," +
                        "gDBConnectedisOKActionCode="+gateway.getgDBConnectedisOKActionCode()+" ," +
                        "gDBConnectedisOKDesc="+gateway.getgDBConnectedisOKDesc()
                );
            }else if (gateway.getIsPingStatus()){
                st.executeUpdate("update tbl_Status_Gateway Set " +
                        "gPingStatus="+gateway.getPingStatus()+" ," +
                        "gPingStatusDateTime="+gateway.getPingStatusDateTime()+" ," +
                        "gPingStatusActionCode="+gateway.getPingStatusActionCode()+" ," +
                        "gPingStatusDesc="+gateway.getPingStatusDesc()
                );
            }else if (gateway.getIsRunningStatus()){
                st.executeUpdate("update tbl_Status_Gateway Set " +
                        "gAppRunning="+gateway.getAppRunning()+" ," +
                        "gAppRunningDateTime="+gateway.getAppRunningDateTIme()+" ," +
                        "gAppRunningActionCode="+gateway.getAppRunningActionCode()+" ," +
                        "gAppRunningDesc="+gateway.getAppRunningDesc()
                );
            }


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

    private void log_Channel(Object object){
        Channel channel=(Channel) object;
        try {
            st = connectionToSQL.createStatement();
            st.executeUpdate("update tbl_Status_Gateway Set " +
                    "gChannelConneted='"+channel.getgChannelConneted()+"' ," +
                    "gChannelConnetedDateTime='"+channel.getgChannelConnetedDateTime()+"' ," +
                    "gChannelConnetedActionCode='"+channel.getgChannelConnetedActionCode()+"' ," +
                    "gChannelConnetedDesc='"+channel.getgChannelConnetedDesc()+"' where gClientID='002'"
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

    private void log_CardSwitch(Object object){
        CardSwitch cardSwitch=(CardSwitch) object;
        try {
            st = connectionToSQL.createStatement();
            st.executeUpdate("update tbl_Status_Gateway Set " +
                    "gSabaSwitchConnected='"+cardSwitch.getgSabaSwitchConnected()+"' ," +
                    "gSabaSwitchConnectedDateTime='"+cardSwitch.getgSabaSwitchConnectedDateTime()+"' ," +
                    "gSabaSwitchConnectedActionCode='"+cardSwitch.getgSabaSwitchConnectedActionCode()+"' ," +
                    "gSabaSwitchConnectedDesc='"+cardSwitch.getgSabaSwitchConnectedDesc()+"' where gClientID='002'"
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

    private void log_TelSwitch(Object object){
        TelSwitch telSwitch=(TelSwitch) object;
        try {
            st = connectionToSQL.createStatement();
            st.executeUpdate("update tbl_Status_Gateway Set  " +
                    "gTelSwitchConnected='"+telSwitch.getgTelSwitchConnected()+"' ," +
                    "gTelSwitchConnectedDateTime='"+telSwitch.getgTelSwitchConnectedDateTime()+"' ," +
                    "gTelSwitchConnectedActionCode='"+telSwitch.getgTelSwitchConnectedActionCode()+"' ," +
                    "gTelSwitchConnectedDesc='"+telSwitch.getgTelSwitchConnectedDesc() +"'  where gClientID='002' "
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

    private void log_Authenticate(Object object){
        Authenticate authenticate=(Authenticate) object;
        try {
            if (authenticate.getPinKind()==0){
                st = connectionToSQL.createStatement();
                st.executeUpdate("update tbl_Status_Gateway Set " +
                        "gPin1isOK='"+authenticate.getgPin1isOK()+"' ," +
                        "gPin1isOKDateTime='"+authenticate.getgPin1isOKDateTime()+"' ," +
                        "gPin1isOKActionCode='"+authenticate.getgPin1isOKActionCode()+"' ," +
                        "gPin1isOKDesc='"+authenticate.getgPin1isOKDesc()+"' ," +
                        "gPin2isOK='"+authenticate.getgPin2isOK()+"' ," +
                        "gPin2isOKDateTime='"+authenticate.getgPin2isOKDateTime()+"' ," +
                        "gPin2isOKActionCode='"+authenticate.getgPin2isOKActionCode()+"' ," +
                        "gPin2isOKDesc='"+authenticate.getgPin2isOKDesc()+"' where gClientID='002'"
                );
            }else if (authenticate.getPinKind()==1){
                st = connectionToSQL.createStatement();
                st.executeUpdate("update tbl_Status_Gateway Set " +
                        "gPin1isOK='"+authenticate.getgPin1isOK()+"' ," +
                        "gPin1isOKDateTime='"+authenticate.getgPin1isOKDateTime()+"' ," +
                        "gPin1isOKActionCode='"+authenticate.getgPin1isOKActionCode()+"' ," +
                        "gPin1isOKDesc='"+authenticate.getgPin1isOKDesc()+"' where gClientID='002'"
                );
            }else if (authenticate.getPinKind()==2){
                st = connectionToSQL.createStatement();
                st.executeUpdate("update tbl_Status_Gateway Set " +
                        "gPin2isOK='"+authenticate.getgPin2isOK()+"' ," +
                        "gPin2isOKDateTime='"+authenticate.getgPin2isOKDateTime()+"' ," +
                        "gPin2isOKActionCode='"+authenticate.getgPin2isOKActionCode()+"' ," +
                        "gPin2isOKDesc='"+authenticate.getgPin2isOKDesc()+"' where gClientID='002'"
                );
            }
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

    private void log_Status(Object object){
        Status_All status_all=(Status_All)object;
        if (status_all.getIsTelBankStatus()) log_TelBank(status_all);
        else if (status_all.getIsTelBankOSStatus()) log_TelBankOS(status_all);
        else if (status_all.getIsDataBaseServer()) log_DBServerOS(status_all);
        else if (status_all.getIsOSOfGateway()) log_GatewayOS(status_all);
        else ;//nothing
    }

    private void log_TelBank(Status_All status_all){

        try {
            st = connectionToSQL.createStatement();
            st.executeUpdate("update tbl_Status_TelBank Set  " +
                    "TBPingStatus='"+status_all.getPingStatus()+"' ," +
                    "TBPingStatusDateTime='"+status_all.getPingStatusDateTime()+"' ," +
                    "TBPingStatusActionCode='"+status_all.getPingStatusActionCode()+"' ," +
                    "TBPingStatusDesc='"+status_all.getPingStatusDesc()+"' ," +
                    "TBAppRunning='"+status_all.getAppRunning()+"' ," +
                    "TBAppRunningStatusDateTime='"+status_all.getAppRunningDateTIme()+"' ," +
                    "TBAppRunningStatusActionCode='"+status_all.getAppRunningActionCode()+"' ," +
                    "TBAppRunningStatusDesc='"+status_all.getAppRunningDesc()+"' ," +
                    "TBGatewayConneted='"+status_all.getConnectionToGateway()+"' ," +
                    "TBGatewayConnetedStatusDateTime='"+status_all.getConnectionToGatewayDateTime()+"' ," +
                    "TBGatewayConnetedStatusActionCode='"+status_all.getConnectionToGatewayActionCode()+"' ," +
                    "TBGatewayConnetedStatusDesc='"+status_all.getConnectionToGatewayDesc()+"' "+

                    " where "+"TBClientID='"+status_all.getClientID()+"'"

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

    private void log_TelBankOS(Status_All status_all){

        try {
            st = connectionToSQL.createStatement();
            st.executeUpdate("update tbl_Status_TelBank Set  " +
                    "TBCPUUsingTime='"+status_all.getCPUUsingTime()+"' ," +
                    "TBCPUFreeTime='"+status_all.getCPUFreeTime()+"' ," +
                    "TBCPUUsingPercent='"+status_all.getCPUUsingPercent()+"' ," +
                    "TBCPUFreePercent='"+status_all.getCPUFreePercent()+"' ," +
                    "TBRAMAllSpace='"+status_all.getRAMAllSpace()+"' ," +
                    "TBRAMUsingSpace='"+status_all.getRAMUsingSpace()+"' ," +
                    "TBRAMFreeSpace='"+status_all.getRAMFreeSpace()+"' ," +
                    "TBRAMUsingPercent='"+status_all.getRAMUsingPercent()+"' ," +
                    "TBRAMFreePercent='"+status_all.getRAMFreePercent()+"' ," +
                    "TBNetDownloadRate='"+status_all.getNetDownloadRate()+"' ," +
                    "TBNetUploadRate='"+status_all.getNetUploadRate()+"' ," +
                    "TBNetAdapterInfo='"+status_all.getNetAdapterInfo()+"' ," +
                    "TBSwapAllSpace='"+status_all.getSwapAllSpace()+"' ," +
                    "TBSwapUsingSpace='"+status_all.getSwapUsingSpace()+"' ," +
                    "TBSwapFreeSpace='"+status_all.getSwapFreeSpace()+"' ," +
                    "TBSwapUsingPercent='"+status_all.getSwapUsingPercent()+"' ," +
                    "TBSwapFreePercent='"+status_all.getSwapFreePercent()+"' ," +
                    "TBDiskAllSpace='"+status_all.getDiskAllSpace()+"' ," +
                    "TBDiskUsingSpace='"+status_all.getDiskUsingSpace()+"' ," +
                    "TBDiskFreeSpace='"+status_all.getDiskFreeSpace()+"' " +

                    " where  "+"TBClientID='"+status_all.getClientID()+"'"
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

    private void log_DBServerOS(Status_All status_all){

        try {
            st = connectionToSQL.createStatement();

            st.executeUpdate("update tbl_Status_Database Set  " +
                    "DBPingStatus='"+status_all.getPingStatus()+"' ," +
                    "DBPingStatusDateTime='"+ status_all.getPingStatusDateTime()+"'," +
                    "DBPingStatusActionCode='"+status_all.getPingStatusActionCode()+"' ," +
                    "DBPingStatusDesc='"+status_all.getPingStatusDesc()+"' ," +
                    "DBCPUUsingTime='"+status_all.getCPUUsingTime()+"' ," +
                    "DBCPUFreeTime='"+status_all.getCPUFreeTime()+"' ," +
                    "DBCPUUsingPercent='"+status_all.getCPUUsingPercent()+"' ," +
                    "DBCPUFreePercent='"+status_all.getCPUFreePercent()+"' ," +
                    "DBRAMAllSpace='"+status_all.getRAMAllSpace()+"' ," +
                    "DBRAMUsingSpace='"+status_all.getRAMUsingSpace()+"' ," +
                    "DBRAMFreeSpace='"+status_all.getRAMFreeSpace()+"' ," +
                    "DBRAMUsingPercent='"+status_all.getRAMUsingPercent()+"' ," +
                    "DBRAMFreePercent='"+status_all.getRAMFreePercent()+"' ," +
                    "DBNetDownloadRate='"+status_all.getNetDownloadRate()+"' ," +
                    "DBNetUploadRate='"+status_all.getNetUploadRate()+"' ," +
                    "DBNetAdapterInfo='"+status_all.getNetAdapterInfo()+"' ," +
                    "DBSwapAllSpace='"+status_all.getSwapAllSpace()+"' ," +
                    "DBSwapUsingSpace='"+status_all.getSwapUsingSpace()+"' ," +
                    "DBSwapFreeSpace='"+status_all.getSwapFreeSpace()+"' ," +
                    "DBSwapUsingPercent='"+status_all.getSwapUsingPercent()+"' ," +
                    "DBSwapFreePercent='"+status_all.getSwapFreePercent()+"' ," +
                    "DBDiskAllSpace='"+status_all.getDiskAllSpace()+"' ," +
                    "DBDiskUsingSpace='"+status_all.getDiskUsingSpace()+"' ," +
                    "DBDiskFreeSpace='"+status_all.getDiskFreeSpace()+"'  where DBCLientID='001'"
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

    private void log_GatewayOS(Status_All status_all){
        try {

                st = connectionToSQL.createStatement();
                st.executeUpdate("update tbl_Status_Gateway Set  " +
                        "gCPUUsingTime='"+status_all.getCPUUsingTime()+"' ," +
                        "gCPUFreeTime='"+status_all.getCPUFreeTime()+"' ," +
                        "gCPUUsingPercent='"+status_all.getCPUUsingPercent()+"' ," +
                        "gCPUFreePercent='"+status_all.getCPUFreePercent()+"' ," +
                        "gRAMAllSpace='"+status_all.getRAMAllSpace()+"' ," +
                        "gRAMUsingSpace='"+status_all.getRAMUsingSpace()+"' ," +
                        "gRAMFreeSpace='"+status_all.getRAMFreeSpace()+"' ," +
                        "gRAMUsingPercent='"+status_all.getRAMUsingPercent()+"' ," +
                        "gRAMFreePercent='"+status_all.getRAMFreePercent()+"' ," +
                        "gNetDownloadRate='"+status_all.getNetDownloadRate()+"' ," +
                        "gNetUploadRate='"+status_all.getNetUploadRate()+"' ," +
                        "gNetAdapterInfo='"+status_all.getNetAdapterInfo()+"' ," +
                        "gSwapAllSpace='"+status_all.getSwapAllSpace()+"' ," +
                        "gSwapUsingSpace='"+status_all.getSwapUsingSpace()+"' ," +
                        "gSwapFreeSpace='"+status_all.getSwapFreeSpace()+"' ," +
                        "gSwapUsingPercent='"+status_all.getSwapUsingPercent()+"' ," +
                        "gSwapFreePercent='"+status_all.getSwapFreePercent()+"' ," +
                        "gDiskAllSpace='"+status_all.getDiskAllSpace()+"' ," +
                        "gDiskUsingSpace='"+status_all.getDiskUsingSpace()+"' ," +
                        "gDiskFreeSpace='"+status_all.getDiskFreeSpace() +"'  where gClientID='002'"
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
    public  String   getTelBankIP(String clientID) throws SQLException {
        Statement preparedStatement = connection.createStatement();
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


}