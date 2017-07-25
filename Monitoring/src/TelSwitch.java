import ServiceObjects.Other.BillInfoByTelNumber;
import ServiceObjects.Pan.BalanceForCard;

import java.io.IOException;
import java.rmi.server.ServerNotActiveException;
import java.sql.SQLException;

/**
 * Created by root on 4/20/16.
 */
public class TelSwitch extends Status {
    public TelSwitch(String[] params) throws ClassNotFoundException, SQLException, ServerNotActiveException, IOException {

        initializeAccountParameters(params[2]);
        checkTelSwitchStatus();
    }

    private String  gTelSwitchConnected="";
    public  void    setgTelSwitchConnected(String TelSwitchConnected){
        gTelSwitchConnected=TelSwitchConnected;
    }
    public  String  getgTelSwitchConnected(){
        return gTelSwitchConnected;
    }

    private String  gTelSwitchConnectedDateTime="";
    public  void    setgTelSwitchConnectedDateTime(String TelSwitchConnectedDateTime){
        gTelSwitchConnectedDateTime=TelSwitchConnectedDateTime;
    }
    public  String  getgTelSwitchConnectedDateTime(){
        return gTelSwitchConnectedDateTime;
    }

    private String  gTelSwitchConnectedActionCode="";
    public  void    setgTelSwitchConnectedActionCode(String TelSwitchConnectedActionCode){
        gTelSwitchConnectedActionCode=TelSwitchConnectedActionCode;
    }
    public  String  getgTelSwitchConnectedActionCode(){
        return gTelSwitchConnectedActionCode;
    }

    private String  gTelSwitchConnectedDesc="";
    public  void    setgTelSwitchConnectedDesc(String TelSwitchConnectedDesc){
        gTelSwitchConnectedDesc=TelSwitchConnectedDesc;
    }
    public  String  getgTelSwitchConnectedDesc(){
        return gTelSwitchConnectedDesc;
    }

    public  void    checkTelSwitchStatus() throws IOException, ServerNotActiveException, SQLException, ClassNotFoundException {

        BillInfoByTelNumber billInfoByTelNumber=new BillInfoByTelNumber();
        billInfoByTelNumber.setPan(getPan());
        billInfoByTelNumber.setPin(getPinOfPan());
        billInfoByTelNumber.setTelNo(getPhoneNumber());
        boolean isMobile=false;
        try{
            if (getPhoneNumber().substring(0,2).equals("09")) isMobile=true;
            else isMobile=false;
        }catch (Exception e){return;}
        billInfoByTelNumber.setIsMobile(isMobile);
        billInfoByTelNumber=(BillInfoByTelNumber) submitRequestToGateway(billInfoByTelNumber);
        setgTelSwitchConnectedActionCode(billInfoByTelNumber.getActionCode());
        setgTelSwitchConnectedDateTime(getNowDateTime());
        setgTelSwitchConnectedDesc("---");
        if (getgTelSwitchConnectedActionCode().equals("9126"))
            setgTelSwitchConnected("0");
        else setgTelSwitchConnected("1");
       // new DBUtils(this);

    }

}
