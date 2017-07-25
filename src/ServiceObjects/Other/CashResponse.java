package ServiceObjects.Other;

import ServiceObjects.ISO.*;
import utils.PropertiesUtils;

import java.sql.*;

public class CashResponse {


    public CashResponse() {

    }

    private Connection connection = null;

    private ISO100 card100 = null;

    public void setCard100(ISO100 Card100) {
        card100 = Card100;
    }

    public ISO100 getCard100() {
        return card100;
    }

    private ISO200 card200 = null;

    public void setCard200(ISO200 Card200) {
        card200 = Card200;
    }

    public ISO200 getCard200() {
        return card200;
    }

    private ISO400 card400 = null;

    public void setCard400(ISO400 Card400) {
        card400 = Card400;
    }

    public ISO400 getCard400() {
        return card400;
    }

    private ISO210 card210 = null;

    public void setCard210(ISO210 Card210) {
        card210 = Card210;
    }

    public ISO210 getCard210() {
        return card210;
    }

    private ISO110 card110 = null;

    public void setCard110(ISO110 Card110) {
        card110 = Card110;
    }

    public ISO110 getCard110() {
        return card110;
    }

    private boolean ConnectedToDB() throws SQLException {
        String DBURL = PropertiesUtils.getDataBaseURL();
        String DBUSER = PropertiesUtils.getDataBaseUserName();
        String DBPASS = PropertiesUtils.getDataBasePassword();

        try {
            if (connection == null) {
                DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
                connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
                return true;
            } else if (connection.isClosed()) {
                DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
                connection = DriverManager.getConnection(DBURL, DBUSER, DBPASS);
                return true;
            } else {
                connection.close();
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

    public boolean resultOfSentMessageIsOK() throws SQLException, InterruptedException {
        int SelectCount = 0;
        boolean foundedMsgSeq = false;
        if (ConnectedToDB()) {
            PreparedStatement preparedStatement = null;
            ResultSet rs = null;
            //System.out.println(card200.getReferenceCode());
            String selectSQL = "SELECT SWITCHMESSAGESEQUENCE FROM TBL_PAN_PENDING " +
                    "WHERE (SWITCHMESSAGESEQUENCE = ?) and (RESPONCEFLAG=?) ";
            preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, card200.getReferenceCode());
            preparedStatement.setInt(2, 1);
            try {

                while (SelectCount++ < 10) {

                    rs = preparedStatement.executeQuery();
                    while (rs.next()) {
                        foundedMsgSeq = true;
                    }
                    if (foundedMsgSeq) break;
                    //System.out.println("foundedMsgSeq:"+String.valueOf(foundedMsgSeq));
                    Thread.sleep(1000);
                }
            } catch (SQLException e) {
                if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
                if (connection != null) {try {connection.close();connection = null;} catch (Exception var1) {}}
                return false;
            } catch (Exception e) {
                if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
                if (connection != null) {try {connection.close();connection = null;} catch (Exception var1) {}}
                return false;
            } finally {
                if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
                if (connection != null) {try {connection.close();connection = null;} catch (Exception var1) {}}

                return foundedMsgSeq;
            }
        }else return false;
    }
    public boolean getResultOfSentMessageIsOK() throws SQLException, InterruptedException {
        int SelectCount = 0;
        String RCVString = "";

        if (ConnectedToDB()) {
            PreparedStatement preparedStatement = null;
            ResultSet rs = null;
            String selectSQL = "SELECT * FROM TBL_PAN_RESPONSE " +
                    "WHERE SWITCHMESSAGESEQUENCE = ?";
            preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, card200.getReferenceCode());
            try {
                rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    RCVString = rs.getString("RECEIVESTRING");
                }

            } catch (SQLException e) {
                if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
                if (connection != null) {try {connection.close();connection = null;} catch (Exception var1) {}}
                return false;
            } catch (Exception e) {
                if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
                if (connection != null) {try {connection.close();connection = null;} catch (Exception var1) {}}
                return false;
            } finally {
                if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
                if (connection != null) {try {connection.close();connection = null;} catch (Exception var1) {}}
                this.card210=new ISO210();
                this.card210.ProcessReceiveString(RCVString);
                deleteSabaPending();
                return true;
            }
        }else return false;
    }
    public boolean deleteSabaPending() throws SQLException, InterruptedException {
        PreparedStatement preparedStatement = null;

        if (ConnectedToDB()) {
            try {
            String deleteSQL = "DELETE TBL_PAN_PENDING " +
                              "WHERE SWITCHMESSAGESEQUENCE = ?";

            preparedStatement = connection.prepareStatement(deleteSQL);
            preparedStatement.setString(1, card200.getReferenceCode());

            preparedStatement.executeQuery();

            Statement st=connection.createStatement();
            st.executeUpdate("UPDATE TBL_PAN_RESPONSE SET"
                            + " RECEIVESTRING=NULL"
                            + " where SWITCHMESSAGESEQUENCE='" + card210.getReferenceCode() + "'");

            } catch (SQLException e) {
                if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
                if (connection != null) {try {connection.close();connection = null;} catch (Exception var1) {}}
                return false;
            } catch (Exception e) {
                if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
                if (connection != null) {try {connection.close();connection = null;} catch (Exception var1) {}}
                return false;
            } finally {
                if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
                if (connection != null) {try {connection.close();connection = null;} catch (Exception var1) {}}
                return true;
            }
        }else return false;

    }

    public boolean resultOfTelSwitchSentMessageIsOK() throws SQLException, InterruptedException {
        int SelectCount = 0;
        boolean foundedMsgSeq = false;
        if (ConnectedToDB()) {
            PreparedStatement preparedStatement = null;
            ResultSet rs = null;
            String selectSQL = "SELECT SWITCHMESSAGESEQUENCE FROM TBL_TELSWITCH_PENDING " +
                    "WHERE (SWITCHMESSAGESEQUENCE = ?) and (RESPONCEFLAG=?) ";
            preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, card100.getReferenceCode());
            preparedStatement.setInt(2, 1);
            try {

                while (SelectCount++ < 15) {

                    rs = preparedStatement.executeQuery();
                    while (rs.next()) {
                        foundedMsgSeq = true;
                    }
                    if (foundedMsgSeq) break;
                    Thread.sleep(1000);
                }
            } catch (SQLException e) {
                if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
                if (connection != null) {try {connection.close();connection = null;} catch (Exception var1) {}}
                return false;
            } catch (Exception e) {
                if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
                if (connection != null) {try {connection.close();connection = null;} catch (Exception var1) {}}
                return false;
            } finally {
                if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
                if (connection != null) {try {connection.close();connection = null;} catch (Exception var1) {}}

                return foundedMsgSeq;
            }
        }else return false;
    }
    public boolean getResultOfTelSwitchSentMessageIsOK() throws SQLException, InterruptedException {
        int SelectCount = 0;
        String RCVString = "";

        if (ConnectedToDB()) {
            PreparedStatement preparedStatement = null;
            ResultSet rs = null;
            String selectSQL = "SELECT * FROM TBL_TELSWITCH_RESPONSE " +
                    "WHERE SWITCHMESSAGESEQUENCE = ?";
            preparedStatement = connection.prepareStatement(selectSQL);
            preparedStatement.setString(1, card100.getReferenceCode());
            try {
                rs = preparedStatement.executeQuery();
                while (rs.next()) {
                    RCVString = rs.getString("RCVSTRING");
                }

            }catch (SQLException e) {
                if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
                if (connection != null) {try {connection.close();connection = null;} catch (Exception var1) {}}
                return false;
            } catch (Exception e) {
                if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
                if (connection != null) {try {connection.close();connection = null;} catch (Exception var1) {}}
                return false;
            } finally {
                if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
                if (connection != null) {try {connection.close();connection = null;} catch (Exception var1) {}}
                this.card110=new ISO110();
                this.card110.ProcessReceiveString(RCVString);
                deleteTelSwitchPending();
                return true;
            }
        }else return false;
    }
    public boolean deleteTelSwitchPending() throws SQLException, InterruptedException {
        PreparedStatement preparedStatement = null;

        if (ConnectedToDB()) {
            String deleteSQL = "DELETE TBL_TELSWITCH_PENDING " +
                    "WHERE SWITCHMESSAGESEQUENCE = ?";

            preparedStatement = connection.prepareStatement(deleteSQL);
            preparedStatement.setString(1, card100.getReferenceCode());
            Thread.sleep(777);
            Statement st=connection.createStatement();
            st.executeUpdate("UPDATE TBL_TELSWITCH_RESPONSE SET"
                    + " RCVSTRING=NULL"
                    + " where SWITCHMESSAGESEQUENCE='" + card100.getReferenceCode() + "'");
            try {
                preparedStatement.executeQuery();
            } catch (SQLException e) {
                if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
                if (connection != null) {try {connection.close();connection = null;} catch (Exception var1) {}}
                return false;
            } catch (Exception e) {
                if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
                if (connection != null) {try {connection.close();connection = null;} catch (Exception var1) {}}
                return false;
            } finally {
                if (preparedStatement != null) {try {preparedStatement.close();preparedStatement = null;} catch (Exception var2) {}}
                if (connection != null) {try {connection.close();connection = null;} catch (Exception var1) {}}
                return true;
            }
        }else return false;

    }

    public boolean getResultOfReverseSend(){
        return false;
    }

}

