import ServiceObjects.Account.BalanceForAccount;
import ServiceObjects.Pan.BalanceForCard;

import java.io.IOException;
import java.rmi.server.ServerNotActiveException;
import java.sql.SQLException;

/**
 * Created by root on 4/20/16.
 */
public class CardSwitch  extends Status {

    public CardSwitch(String[] params) throws ClassNotFoundException, SQLException, ServerNotActiveException, IOException {

        initializeAccountParameters(params[2]);
        checkSabaSwitchStatus();
    }

    private String  gSabaSwitchConnected="";
    public  void    setgSabaSwitchConnected(String SabaSwitchConnected){
        gSabaSwitchConnected=SabaSwitchConnected;
    }
    public  String  getgSabaSwitchConnected(){
        return gSabaSwitchConnected;
    }

    private String  gSabaSwitchConnectedDateTime="";
    public  void    setgSabaSwitchConnectedDateTime(String SabaSwitchConnectedDateTime){
        gSabaSwitchConnectedDateTime=SabaSwitchConnectedDateTime;
    }
    public  String  getgSabaSwitchConnectedDateTime(){
        return gSabaSwitchConnectedDateTime;
    }

    private String  gSabaSwitchConnectedActionCode="";
    public  void    setgSabaSwitchConnectedActionCode(String SabaSwitchConnectedActionCode){
        gSabaSwitchConnectedActionCode=SabaSwitchConnectedActionCode;
    }
    public  String  getgSabaSwitchConnectedActionCode(){
        return gSabaSwitchConnectedActionCode;
    }

    private String  gSabaSwitchConnectedDesc="";
    public  void    setgSabaSwitchConnectedDesc(String SabaSwitchConnectedDesc){
        gSabaSwitchConnectedDesc=SabaSwitchConnectedDesc;
    }
    public  String  getgSabaSwitchConnectedDesc(){
        return gSabaSwitchConnectedDesc;
    }

    public  void    checkSabaSwitchStatus() throws IOException, ServerNotActiveException, SQLException, ClassNotFoundException {

        BalanceForCard balanceForCard=new BalanceForCard();
        balanceForCard.setPan(getPan());
        balanceForCard.setPin(getPinOfPan());
        balanceForCard=(BalanceForCard) submitRequestToGateway(balanceForCard);
        setgSabaSwitchConnectedActionCode(balanceForCard.getActionCode());
        setgSabaSwitchConnectedDateTime(getNowDateTime());
        setgSabaSwitchConnectedDesc("---");
        if (getgSabaSwitchConnectedActionCode().equals("0000"))
            setgSabaSwitchConnected("1");
        else setgSabaSwitchConnected("0");
       // new DBUtils(this);

    }

}
