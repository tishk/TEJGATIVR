package ServiceObjects.Other;

import ServiceObjects.Account.BillPayByIDAccount;
import ServiceObjects.Account.FundTransfer;
import ServiceObjects.Account.InternalFollowUp;
import com.ibm.disthub2.impl.util.StringUtil;
import utils.PropertiesUtils;
import utils.strUtils;

import java.sql.*;

/**
 * Created by Administrator on 12/31/2015.
 */
public class DBUtilsSQLServer {


    String DBURL = "";

    String DBUSER = "";
    String DBPASS = "";
    String url = "";
    String Driver="";
    String DBName = "";
    strUtils strutils = new strUtils();
    private Connection connectionToSQL = null;
    private Connection TempconnectionToSQL = null;
    public boolean connected = false;

    public DBUtilsSQLServer(String DataBaseName) {
        try {

            DBName=DataBaseName;
            if (ConnectedToSQLServerDB(DataBaseName)) connected = true;
            if (connected); //System.out.println("yes");
            else System.out.println("no");


        } catch (SQLException e) {
        } catch (Exception e) {
        }
    }


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
    private  java.sql.Date getCurrentDate() {

        java.sql.Date today = new java.sql.Date(new java.util.Date().getTime());
        return today;

    }

    public FundTransfer accountFundTransferCashData(FundTransfer fundTransfer) throws SQLException, ClassNotFoundException {
        Statement preparedStatement = null;
        ResultSet rset = null;
        try {

        if (ConnectedToSQLServerDB("MasterDatabase")){

            String selectQuery = "SELECT * FROM  PrivilegeRegistration  " +
                    "WHERE fldMainAccountNo =" + fundTransfer.getSourceAccount();


            preparedStatement = connectionToSQL.createStatement();
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
                        preparedStatement = connectionToSQL.createStatement();
                        rset = preparedStatement
                                .executeQuery(selectQuery1);
                        String accountGroup="!";
                        while (rset.next()) {
                            accountGroup=rset.getString("HesabGroup");
                        }
                    if (!accountGroup.equals("!")){
                        if (TempConnectedToSQLServerDB("SpecialDataBase")){
                        fundTransfer.setIsValidAccount(true);
                        selectQuery = "SELECT * FROM  HESABGROUPDEFINE  ";
                        preparedStatement = connectionToSQL.createStatement();
                        rset = preparedStatement.executeQuery(selectQuery);
                        while (rset.next()) {
                            if (rset.getString("GROUPCODE").equals(accountGroup)){
                                fundTransfer.setAccountGroupAllowed(true);
                                break;
                            }
                        }
                    }
                }else fundTransfer.setIsValidAccount(false);
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
        return fundTransfer;
    }

    public String getTransactionDescription(String transCode) throws SQLException, ClassNotFoundException {
        Statement preparedStatement = null;
        String result="";
        ResultSet rset = null;
        try {

            if (ConnectedToSQLServerDB("specialdatabase")) {

                String selectQuery = "SELECT * FROM  transcode  " +
                        "WHERE transCode =" + transCode;


                preparedStatement = connectionToSQL.createStatement();
                rset = preparedStatement
                        .executeQuery(selectQuery);
                while (rset.next()) {
                    result = rset.getString("transDscp");

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

    public InternalFollowUp internalFollowUpTransaction(InternalFollowUp followUpTransaction) throws SQLException {

        Statement preparedStatement = null;
        ResultSet rset = null;

        String selectQuery = "SELECT * FROM  Gabz  " +
                "WHERE FOLLOWCODE =" + followUpTransaction.getFollowUpCode()+
                " and SOURCEOFPAYMENT="+followUpTransaction.getSourceAccount();
        try {

            preparedStatement = connectionToSQL.createStatement();
            rset = preparedStatement
                    .executeQuery(selectQuery);
            while (rset.next()) {
                followUpTransaction.setActionCode("0000");
                followUpTransaction.setisValidFollowCode(true);
                followUpTransaction.setstatus(getStatusFromActionCode(rset.getString("ACTIONCODE")));
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

        String selectQuery = "SELECT * FROM  PrivilegeRegistration  " +
                "WHERE fldMainAccountNo =" + billPayByIDAccount.getSourceAccount();
        try {

            preparedStatement = connectionToSQL.createStatement();
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
            preparedStatement = connectionToSQL.createStatement();
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
                preparedStatement = connectionToSQL.createStatement();
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
            if (connectionToSQL != null) {try {connectionToSQL.close();connectionToSQL = null;} catch (Exception var1) {}}
            if (rset != null) {try {rset.close();rset = null;} catch (Exception var2) {}}
            billPayByIDAccount.setDataFromDbChashed(false);
        } catch (Exception e) {
            if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
            if (connectionToSQL != null) {try {connectionToSQL.close();connectionToSQL = null;} catch (Exception var1) {}}
            if (rset != null) {try {rset.close();rset = null;} catch (Exception var2) {}}
            billPayByIDAccount.setDataFromDbChashed(false);
        } finally {
            if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
            if (connectionToSQL != null) {try {connectionToSQL.close();connectionToSQL = null;} catch (Exception var1) {}}
            if (rset != null) {try {rset.close();rset = null;} catch (Exception var2) {}}
            billPayByIDAccount.setDataFromDbChashed(true);
        }
        return billPayByIDAccount;
    }

    public boolean changePriority(String acc) throws SQLException {
        boolean res = false;

        Statement st = null;
        try {
            st = connectionToSQL.createStatement();
            st.executeUpdate("update PrivilegeRegistration Set RegisteredInCM=0 ,Priority=Priority+1 " +
                    "Where fldMainAccountNo=" + acc);
        } catch (SQLException e) {
            if (connectionToSQL != null) {
                try {
                    connectionToSQL.close();
                    connectionToSQL = null;
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
            if (connectionToSQL != null) {
                try {
                    connectionToSQL.close();
                    connectionToSQL = null;
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
            if (connectionToSQL != null) {
                try {
                    connectionToSQL.close();
                    connectionToSQL = null;
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

    public String getFundTransfer(FundTransfer fundTransfer) {
        Statement preparedStatement = null;
        String result="";
        ResultSet rset = null;
        String fldServiceType="";
        String fldFirstAccountNo="";
        String fldSecondAccountNo="";
        String fldThirdAccountNo="";
        String fldFourthAccountNo="";
        String fldFifthAccountNo="";
        String isFreeTransfer="";
        String actionCode="";
        long sumBed=0L;
        boolean maxValid=false;
        boolean registered=false;
        String ws="";


        try {
            if (ConnectedToSQLServerDB("MasterDataBase")) {
                String selectQuery = "SELECT * FROM  PrivilegeRegistration  WHERE fldMainAccountNo =" +fundTransfer.getSourceAccount();
                preparedStatement = connectionToSQL.createStatement();
                rset = preparedStatement
                        .executeQuery(selectQuery);
                while (rset.next()) {
                    registered=true;
                    fldServiceType= rset.getString("fldServiceType");
                    fldFirstAccountNo= rset.getString("fldFirstAccountNo");
                    fldSecondAccountNo= rset.getString("fldSecondAccountNo");
                    fldThirdAccountNo= rset.getString("fldThirdAccountNo");
                    fldFourthAccountNo= rset.getString("fldFourthAccountNo");
                    fldFifthAccountNo= rset.getString("fldFifthAccountNo");
                    isFreeTransfer= rset.getString("IsFreeTransfer");
                }

            }
            String destinationAccount=fundTransfer.getDestinationAccount();
            if (registered){

                 ws="'1'"+","+
                        fldFirstAccountNo+","+
                        fldSecondAccountNo+","+
                        fldThirdAccountNo+","+
                        fldFourthAccountNo+","+
                        fldFifthAccountNo;
                if (destinationAccount==fldFifthAccountNo || destinationAccount==fldSecondAccountNo||
                        destinationAccount==fldThirdAccountNo || destinationAccount==fldFourthAccountNo||
                        destinationAccount==fldFifthAccountNo) {
                    actionCode="0000" ;
                }
                else if (Boolean.valueOf(isFreeTransfer)) {
                    actionCode="9700";
                }
            }else{
                actionCode="9300";
            }

            if (actionCode.equals("0000") || actionCode.equals("9700")||Integer.valueOf(fundTransfer.getTransactionAmount())>0){

                if (ConnectedToSQLServerDB("specialdatabase")) {
                    String ifThen=" ";
                    if (actionCode=="9700") ifThen= "and AccNoBes Not In ("+ws+")";
                    String selectQuery =
                            "SELECT Isnull(Sum(cast(MBed as bigint)),0) As SumBed From Gabz WHERE"+
                                    " AccNoBed ="+fundTransfer.getSourceAccount()+
                                    " AND CodeAmaliat=''01'' and  fldPBDoneFlag In(1,2,3) and [date]="+getCurrentDate()+
                                    ifThen;
                    preparedStatement = connectionToSQL.createStatement();
                    rset = preparedStatement
                            .executeQuery(selectQuery);
                    while (rset.next()) {
                        sumBed = rset.getLong("SumBed");

                    }

                }
                Long maxAmount=300000L;
                Long maxAmountOther=400000L;
                if (actionCode=="0000") maxValid=sumBed <maxAmount;
                if (actionCode=="9700") maxValid=sumBed <maxAmountOther;
                if (maxValid) {
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
    public String getRegistrationData(String accountNO) {
        Statement preparedStatement = null;
        String result="";
        ResultSet rset = null;
        String fldServiceType="";
        String isFreeTransfer="";
        boolean registered=false;
        String fldFirstAccountNo="";
        String fldSecondAccountNo="";
        String fldThirdAccountNo="";
        String fldFourthAccountNo="";
        String fldFifthAccountNo="";


        try {
            if (ConnectedToSQLServerDB("MasterDataBase")) {
                strUtils stringUtil=new strUtils();
                accountNO=stringUtil.fixLengthWithZero(accountNO,10);
                String selectQuery = "SELECT * FROM  PrivilegeRegistration  WHERE fldMainAccountNo =" +"\'"+accountNO+"\'";
                preparedStatement = connectionToSQL.createStatement();
                rset = preparedStatement
                        .executeQuery(selectQuery);
                while (rset.next()) {

                    registered=true;
                    result=String.valueOf(registered);
                    fldServiceType= rset.getString("fldServiceType");
                    result=result+"@"+fldServiceType;
                    fldFirstAccountNo= rset.getString("fldFirstAccountNo");
                    result=result+"@"+fldFirstAccountNo;
                    fldSecondAccountNo= rset.getString("fldSecondAccountNo");
                    result=result+"@"+fldSecondAccountNo;
                    fldThirdAccountNo= rset.getString("fldThirdAccountNo");
                    result=result+"@"+fldThirdAccountNo;
                    fldFourthAccountNo= rset.getString("fldFourthAccountNo");
                    result=result+"@"+fldFourthAccountNo;
                    fldFifthAccountNo= rset.getString("fldFifthAccountNo");
                    result=result+"@"+fldFifthAccountNo;
                    isFreeTransfer= rset.getString("IsFreeTransfer");
                    result=result+"@"+isFreeTransfer;

                }
                if (!registered) result=String.valueOf(registered)+"@"+"0";

            }else if (!registered) result=String.valueOf(registered)+"@"+"1";

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





}
