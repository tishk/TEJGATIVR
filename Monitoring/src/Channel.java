import ServiceObjects.Account.BalanceForAccount;

import java.io.IOException;
import java.rmi.server.ServerNotActiveException;
import java.sql.SQLException;

/**
 * Created by root on 4/20/16.
 */
public class Channel extends Status {

    public Channel(String [] requestParams) throws IOException, ServerNotActiveException, SQLException, ClassNotFoundException {

        initializeAccountParameters(requestParams[2]);
        checkChannelStatus();

    }

    private String  gChannelConneted="";
    public  void    setgChannelConneted(String ChannelConneted){
        gChannelConneted=ChannelConneted;
    }
    public  String  getgChannelConneted(){
        return gChannelConneted;
    }

    private String  gChannelConnetedDateTime="";
    public  void    setgChannelConnetedDateTime(String ChannelConnetedDateTime){
        gChannelConnetedDateTime=ChannelConnetedDateTime;
    }
    public  String  getgChannelConnetedDateTime(){
        return gChannelConnetedDateTime;
    }

    private String  gChannelConnetedActionCode="";
    public  void    setgChannelConnetedActionCode(String ChannelConnetedActionCode){
        gChannelConnetedActionCode=ChannelConnetedActionCode;
    }
    public  String  getgChannelConnetedActionCode(){
        return gChannelConnetedActionCode;
    }

    private String  gChannelConnetedDesc="";
    public  void    setgChannelConnetedDesc(String ChannelConnetedDesc){
        gChannelConnetedDesc=ChannelConnetedDesc;
    }
    public  String  getgChannelConnetedDesc(){
        return gChannelConnetedDesc;
    }

    public  void    checkChannelStatus() throws IOException, ServerNotActiveException, SQLException, ClassNotFoundException {

        BalanceForAccount balanceForAccount=new BalanceForAccount();
        balanceForAccount.setAccountNumber(getAccountNumber());
        balanceForAccount=(BalanceForAccount)submitRequestToGateway(balanceForAccount);
        setgChannelConnetedActionCode(balanceForAccount.getResultFromChannel().getAction_code());
        setgChannelConnetedDateTime(getNowDateTime());
        setgChannelConnetedDesc("---");
        if (getgChannelConnetedActionCode().equals("9126"))
            setgChannelConneted("0");
        else setgChannelConneted("1");
       // new DBUtils(this);

    }
}
