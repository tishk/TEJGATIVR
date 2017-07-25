package ResponseServices;

import Mainchannel.Exceptions.InvalidParameterException;
import Mainchannel.Exceptions.ResponseParsingException;
import Mainchannel.sender.SenderException;
import ServiceObjects.Account.FundTransfer;
import ServiceObjects.Other.DBUtilsSQLServer;
import utils.PersianDateTime;
import utils.PropertiesUtils;
import utils.strUtils;

import java.sql.*;

/**
 * Created by Administrator on 1/13/2016.
 */
public class PaymentPreM {
    FundTransfer fundTransfer=new FundTransfer();
    PersianDateTime persianDateTime=new PersianDateTime();
    GatewayServices gatewayServices=new GatewayServices();
    private String actionCede="";
    public  String getActionCede(){
        return actionCede;
    }

    String amount="";
    boolean processFinished=false;
    String today=getNowShamsiDate();
    String nowTime="";
    String DBURL = "";
    String DBUSER = "";
    String DBPASS = "";
    String url = "";
    String Driver="";
    String DBName = "";
    int MaxAmount =150000000;
    int MaxAmountOther = 30000000;
    boolean maxValid=false;
    String SeedNumber = "";
    String StartOnlineTime = "";
    String EndOnlineTime="";
    String LastUpdateDate = "";
    String MaxValid="";
    String MinValid="";
    String groupCode[]=new String[100];
    String macAddress[]=new String[1000];

    strUtils strutils = new strUtils();
    private Connection connectionToSQL = null;
    private Connection TempconnectionToSQL = null;
    public boolean connected = false;
    private boolean ConnectedToSQLServerDB(String DataBase) throws SQLException, ClassNotFoundException {
        //DBURL =  PropertiesUtils.getSQL_DB_URL()+DBName;
        DBUSER = PropertiesUtils.getSQL_DB_USER();
        DBPASS = PropertiesUtils.getSQL_DB_PASSWORD();
        url =    PropertiesUtils.getSQL_DB_URL()+DataBase;
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
            connectionToSQL = null;
            return false;
        }
    }
    private boolean TempConnectedToSQLServerDB(String DataBase) throws SQLException, ClassNotFoundException {
        //DBURL =  PropertiesUtils.getSQL_DB_URL()+DBName;
        DBUSER = PropertiesUtils.getSQL_DB_USER();
        DBPASS = PropertiesUtils.getSQL_DB_PASSWORD();
        url =    PropertiesUtils.getSQL_DB_URL()+DataBase;
        Driver=  PropertiesUtils.getSQL_DB_DRIVER();

        try {
            if (TempconnectionToSQL == null) {
                Class.forName(Driver);
                TempconnectionToSQL = DriverManager.getConnection(url, DBUSER, DBPASS);
                return true;
            } else if (TempconnectionToSQL.isClosed()) {
                TempconnectionToSQL = null;
                Class.forName(Driver);
                TempconnectionToSQL = DriverManager.getConnection(url, DBUSER, DBPASS);
                return true;
            } else {
                TempconnectionToSQL.close();
                TempconnectionToSQL = null;
                Class.forName(Driver);
                TempconnectionToSQL = DriverManager.getConnection(url, DBUSER, DBPASS);
                return true;
            }

        } catch (Exception e) {
            System.out.println(e.getMessage());
            TempconnectionToSQL = null;
            return false;
        }
    }
    private java.sql.Timestamp getCurrentTimeStamp() {

        java.util.Date today = new java.util.Date();
        long t = today.getTime();
        return new java.sql.Timestamp(t);

    }
    private String getNowShamsiDate(){
        return persianDateTime.getShamsi_Date_WithoutSeperator();
    }

    public void    DB_CashAccountInformation() throws SQLException, ClassNotFoundException {
        Statement preparedStatement = null;
        ResultSet rset = null;
        boolean isInDatabase=false;
        try {

            if (ConnectedToSQLServerDB("MasterDatabase")) {

                String selectQuery = "SELECT * FROM  PrivilegeRegistration  " +
                        "WHERE fldMainAccountNo =" + fundTransfer.getSourceAccount();


                preparedStatement = connectionToSQL.createStatement();
                rset = preparedStatement
                        .executeQuery(selectQuery);
                while (rset.next()) {
                    isInDatabase=true;
                    fundTransfer.setServiceTypes(rset.getString("fldServiceType"));
                    fundTransfer.setNationalCode(rset.getString("fldNationalCode"));
                    fundTransfer.setDefinedAccount1(rset.getString("fldFirstAccountNo"));
                    fundTransfer.setDefinedAccount2(rset.getString("fldSecondAccountNo"));
                    fundTransfer.setDefinedAccount3(rset.getString("fldThirdAccountNo"));
                    fundTransfer.setDefinedAccount4(rset.getString("fldFourthAccountNo"));
                    fundTransfer.setDefinedAccount5(rset.getString("fldFifthAccountNo"));
                    if (rset.getString("IsFreeTransfer").equals("1")) fundTransfer.setIsFreeTransfer(true);
                }
                if (isInDatabase){
                    actionCede="9600";
                    if (fundTransfer.getServiceTypes().charAt(2)=='1') actionCede="0000";
                    else actionCede="9800";
                    if ( !actionCede.equals("0000")){
                        processFinished=true;
                    }else{
                        if (destinationAccountIsValid(fundTransfer)) actionCede="0000";
                        else if (fundTransfer.getIsFreeTransfer()) actionCede="9700";
                    }
                }else{
                    actionCede="9300";
                }
            }
        } catch (SQLException e) {
            if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
            if (connectionToSQL != null) {try {connectionToSQL.close();connectionToSQL = null;} catch (Exception var1) {}}
            if (rset != null) {try {rset.close();rset = null;} catch (Exception var2) {}}
            fundTransfer.setDataFromDbChashed(false);
        } catch (Exception e) {
            if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
            if (connectionToSQL != null) {try {connectionToSQL.close();connectionToSQL = null;} catch (Exception var1) {}}
            if (rset != null) {try {rset.close();rset = null;} catch (Exception var2) {}}
            fundTransfer.setDataFromDbChashed(false);
        } finally {
            if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
            if (connectionToSQL != null) {try {connectionToSQL.close();connectionToSQL = null;} catch (Exception var1) {}}
            if (rset != null) {try {rset.close();rset = null;} catch (Exception var2) {}}
            fundTransfer.setDataFromDbChashed(true);
        }

    }
    public void    DB_CashMinMaxAmount(){
        Statement preparedStatement = null;
        ResultSet rset = null;
        boolean isInDatabase=false;
        try {

            if (ConnectedToSQLServerDB("SpecialDataBase")) {

                String ws="1,"+fundTransfer.getDefinedAccount1()+","+
                        fundTransfer.getDefinedAccount2()+","+
                        fundTransfer.getDefinedAccount3()+","+
                        fundTransfer.getDefinedAccount4()+","+
                        fundTransfer.getDefinedAccount5();
                String lastPart="";
                if (actionCede.equals("9700")) lastPart=" and AccNoBes Not In ("+ws+")";
                String query="SELECT Isnull(Sum(cast(MBed as bigint)),0) As SumBed From Gabz "+
                        " WHERE AccNoBed = "+fundTransfer.getSourceAccount()+
                        " AND CodeAmaliat='01' and  fldPBDoneFlag In(1,2,3) and [date]=" +
                        today+
                        lastPart;

                preparedStatement = connectionToSQL.createStatement();
                rset = preparedStatement
                        .executeQuery(query);
                String sumBed="";
                while (rset.next()) {
                    sumBed=rset.getString("SumBed");
                }
               if (actionCede.equals("0000")) {
                   maxValid=Integer.valueOf(sumBed)+ Integer.valueOf(fundTransfer.getTransactionAmount())<=MaxAmount;
               }else if (actionCede.equals("9700")){
                   maxValid=Integer.valueOf(sumBed)+ Integer.valueOf(fundTransfer.getTransactionAmount())<=MaxAmountOther;
               }else ;
            }
        } catch (SQLException e) {
            if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
            if (connectionToSQL != null) {try {connectionToSQL.close();connectionToSQL = null;} catch (Exception var1) {}}
            if (rset != null) {try {rset.close();rset = null;} catch (Exception var2) {}}
            fundTransfer.setDataFromDbChashed(false);
        } catch (Exception e) {
            if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
            if (connectionToSQL != null) {try {connectionToSQL.close();connectionToSQL = null;} catch (Exception var1) {}}
            if (rset != null) {try {rset.close();rset = null;} catch (Exception var2) {}}
            fundTransfer.setDataFromDbChashed(false);
        } finally {
            if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
            if (connectionToSQL != null) {try {connectionToSQL.close();connectionToSQL = null;} catch (Exception var1) {}}
            if (rset != null) {try {rset.close();rset = null;} catch (Exception var2) {}}
            fundTransfer.setDataFromDbChashed(true);
        }

    }
    public boolean DB_ChangePriority() throws SQLException {
        Statement preparedStatement = null;
        ResultSet rset = null;
        Statement st = null;
        try {

            if (ConnectedToSQLServerDB("SpecialDataBase")) {
                st = connectionToSQL.createStatement();
                st.executeUpdate("update TBL_SPECIALSERVICEINFO Set RegisteredInCM=0 ,Priority=Priority+1 " +
                        "Where fldMainAccountNo=" + fundTransfer.getSourceAccount());
            }

        } catch (SQLException e) {
            if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
            if (connectionToSQL != null) {try {connectionToSQL.close();connectionToSQL = null;} catch (Exception var1) {}}
            if (rset != null) {try {rset.close();rset = null;} catch (Exception var2) {}}
            return false;
        } catch (Exception e) {
            if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
            if (connectionToSQL != null) {try {connectionToSQL.close();connectionToSQL = null;} catch (Exception var1) {}}
            if (rset != null) {try {rset.close();rset = null;} catch (Exception var2) {}}
            return false;
        } finally {
            if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
            if (connectionToSQL != null) {try {connectionToSQL.close();connectionToSQL = null;} catch (Exception var1) {}}
            if (rset != null) {try {rset.close();rset = null;} catch (Exception var2) {}}
            return true;
        }

    }
    public void    DB_LogResult(){
        Statement preparedStatement = null;
        ResultSet rset = null;
        Statement st = null;
        try {

            if (ConnectedToSQLServerDB("SpecialDataBase")) {
                String Ws="Declare @maxPb bigint ,@maxPeriod bigint set @maxPeriod= (Select Isnull(Max(fldPeriod),0) From Gabz) "+
                           "set @maxPb=(Select Isnull(Max(fldCheckPBNo),0) From Gabz where fldPeriod=@maxPeriod)+1 if (@maxPb>999000) "+
                           "begin   set @maxPeriod=@maxPeriod+1   Set @maxPb=1 end ";
                String Ws2="INSERT INTO [AutoFollowUp]([PBNo], [Accnobed], [MsgSeq], [RegdateTime]) VALUES(@maxPb,"+
                            fundTransfer.getSourceAccount()+","+
                            fundTransfer.getResultFromChannel().getMsgSequence()+","+
                            fundTransfer.getResultFromChannel().getReqDateTime();

                String LastPart="";
                if (actionCede.equals("2")) LastPart=Ws2;
                if (fundTransfer.getIsIdentFundTranfer()){
                    Ws=  Ws+   " Insert Into Gabz "+
                                " VALUES(@maxPeriod, @maxPb ,07,04)," +
                                fundTransfer.getSourceAccount() +", "+
                                fundTransfer.getDestinationAccount() +
                                fundTransfer.getTransactionAmount()
                                +",'0000000000000','0000000000000'," +
                                today+ "," +nowTime+","+"'1','888888',"+actionCede+")"+
                                LastPart+
                                "Select MaxCode=@maxPb";
                }else{
                    Ws=  Ws+" Insert Into Gabz "+
                            " VALUES(@maxPeriod, @maxPb ,07,01)," +
                            fundTransfer.getSourceAccount() +", "+
                            fundTransfer.getDestinationAccount() +
                            fundTransfer.getTransactionAmount()
                            +",'0000000000000','0000000000000'," +
                            today+ "," +nowTime+","+"'1','888888',"+actionCede+")"+
                            LastPart+
                            "Select MaxCode=@maxPb";
                }
                actionCede="3004";
                actionCede=fundTransfer.getResultFromChannel().getAction_code();
                st = connectionToSQL.createStatement();
                st.executeUpdate(Ws);

                Thread.sleep(200);

               //other operation
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
    }
    public void    DB_CashDefinedSettings() throws SQLException, ClassNotFoundException {
        Statement preparedStatement = null;
        ResultSet rset = null;
        boolean isInDatabase=false;
        try {

            if (ConnectedToSQLServerDB("SpecialDataBase")) {

                String selectQuery = " Select * from Define ";


                preparedStatement = connectionToSQL.createStatement();
                rset = preparedStatement
                        .executeQuery(selectQuery);
                while (rset.next()) {
                    try {
                        MaxAmount = Integer.valueOf(rset.getString("fldMaxAmount"));
                    } catch (Exception e) {
                        MaxAmount = 0;
                    } ;
                    try {
                        MaxAmountOther = Integer.valueOf(rset.getString("fldMaxAmountOther"));
                    } catch (Exception e) {
                        MaxAmountOther = 0;
                    } ;
                    SeedNumber = rset.getString("SeedNumber");
                    StartOnlineTime = rset.getString("FStartOnlineTime");
                    EndOnlineTime = rset.getString("FFinishOnlineTime");
                    LastUpdateDate = strutils.rightString(rset.getString("UpdateDate"), 6);
                }
                Thread.sleep(100);
                selectQuery = " Select GroupCode from HesabGroupDefine";
                preparedStatement = connectionToSQL.createStatement();
                rset = preparedStatement
                        .executeQuery(selectQuery);
                int i=0;
                while (rset.next()) {
                    groupCode[i]=rset.getString("GroupCode");
                }Thread.sleep(100);
                selectQuery = " Select MacAdd from MacAddressList";
                preparedStatement = connectionToSQL.createStatement();
                rset = preparedStatement
                        .executeQuery(selectQuery);
                 i=0;
                while (rset.next()) {
                    macAddress[i]=rset.getString("MacAdd");
                }

            }
        } catch (SQLException e) {
            if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
            if (connectionToSQL != null) {try {connectionToSQL.close();connectionToSQL = null;} catch (Exception var1) {}}
            if (rset != null) {try {rset.close();rset = null;} catch (Exception var2) {}}
            fundTransfer.setDataFromDbChashed(false);
        } catch (Exception e) {
            if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
            if (connectionToSQL != null) {try {connectionToSQL.close();connectionToSQL = null;} catch (Exception var1) {}}
            if (rset != null) {try {rset.close();rset = null;} catch (Exception var2) {}}
            fundTransfer.setDataFromDbChashed(false);
        } finally {
            if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
            if (connectionToSQL != null) {try {connectionToSQL.close();connectionToSQL = null;} catch (Exception var1) {}}
            if (rset != null) {try {rset.close();rset = null;} catch (Exception var2) {}}
            fundTransfer.setDataFromDbChashed(true);
        }

    }

    private boolean destinationAccountIsValid(FundTransfer fundTransfer){

        if (fundTransfer.getDestinationAccount().equals(fundTransfer.getDefinedAccount1())) return true;
        if (fundTransfer.getDestinationAccount().equals(fundTransfer.getDefinedAccount2())) return true;
        if (fundTransfer.getDestinationAccount().equals(fundTransfer.getDefinedAccount3())) return true;
        if (fundTransfer.getDestinationAccount().equals(fundTransfer.getDefinedAccount4())) return true;
        if (fundTransfer.getDestinationAccount().equals(fundTransfer.getDefinedAccount5())) return true;
        return false;
    }
    private void   getAccountInformation() throws SenderException, InvalidParameterException, ResponseParsingException, SQLException, ClassNotFoundException {
        DBUtilsSQLServer dbUtilsSQLServer =new DBUtilsSQLServer("MasterDatabase");
        fundTransfer= dbUtilsSQLServer.accountFundTransferCashData(fundTransfer);
        if (dbUtilsSQLServer.connected){

        }else fundTransfer.setActionCode("9000");

    }
    private void   doFundTransfer() throws SQLException, ClassNotFoundException, SenderException, InvalidParameterException, ResponseParsingException {
        int acCode = 9999;
        DB_CashAccountInformation();
        if (!processFinished) {
            if (actionCede.equals("0000") || actionCede.equals("9700")) {
                DB_CashMinMaxAmount();
                if (maxValid) {
                    fundTransfer = gatewayServices.accountFundTransfer(fundTransfer);
                    if (fundTransfer.getResultFromChannel().getAction_code().equals("3333")) {
                        fundTransfer.getResultFromChannel().setAction_code("3000");
                    } else {

                        try {

                            acCode = Integer.valueOf(fundTransfer.getResultFromChannel().getAction_code());
                        } catch (Exception e) {
                        }
                        switch (acCode) {
                            case 0:
                                actionCede = "1";
                                break;
                            case 1000:
                                actionCede = "3";
                                break;
                            case 2000:
                                actionCede = "2";
                                break;
                            case 9126:
                                actionCede = "2";
                                break;
                            default:
                                actionCede = "0";
                        }
                    }
                    nowTime = persianDateTime.getRequestTime();
                    if (acCode==1912) DB_ChangePriority();


                } else actionCede = "9500";
            }
        }
    }
}



