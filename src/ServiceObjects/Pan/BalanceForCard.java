package ServiceObjects.Pan;

import ServiceObjects.ISO.ISO210;
import ServiceObjects.ISO.ISO430;

/**
 * Created by Administrator on 28/05/2015.
 */
public class BalanceForCard extends BaseCardRequest   {

    private String ActionCode="";
    public  void    setActionCode(String actionCode){
        ActionCode=actionCode;
    }
    public  String getActionCode(){
        return ActionCode;
    }

    private String MACAddress="";
    public  void    setMACAddress(String macAddress){
        MACAddress=macAddress;
    }
    public  String getMACAddress(){
        return MACAddress;
    }

    private ISO210 ResultFromServer=null;
    public  void setResultFromServer(ISO210 resultFromServer){
        this.ResultFromServer=resultFromServer;
    }
    public ISO210 getResultFromServer(){
        return this.ResultFromServer;
    }

    private ISO430 ResultOfReverse=null;
    public  void setResultOfReverse(ISO430 resultOfReverse){
        this.ResultOfReverse=resultOfReverse;
    }
    public ISO430 getResultOfReverse(){
        return this.ResultOfReverse;
    }




}
