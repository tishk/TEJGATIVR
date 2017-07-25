package SystemStatus;

import java.io.Serializable;

/**
 * Created by root on 5/7/16.
 */
public class Status_TelBank implements Serializable {

    private String  ClientID= null;
    public  void    setClientID(String clientID){
        ClientID=clientID;
    }
    public  String  getClientID(){
        return ClientID;
    }


    private String  NetworkStatus=null;
    public  void    setNetworkStatus(String networkStatus){
        NetworkStatus=networkStatus;
    }
    public  String  getNetworkStatus(){
        return NetworkStatus;
    }

    private String  InRunningStatus=null;
    public  void    setInRunningStatus(String inRunningStatus){
        InRunningStatus=inRunningStatus;
    }
    public  String  getInRunningStatus(){
        return InRunningStatus;
    }

    private String  DateTime=null;
    public  void    setDateTime(String dateTime){
        DateTime=dateTime;
    }
    public  String  getDateTime(){
        return DateTime;
    }

    private String  ConnectionToGateway="";
    public  void    setConnectionToGateway(String ConnectionToGateway){
        ConnectionToGateway=ConnectionToGateway;
    }
    public  String  getConnectionToGateway(){
        return ConnectionToGateway;
    }

    private String  PersianDescription="---";
    public  void    setPersianDescription(String persianDescription){
        PersianDescription=persianDescription;
    }
    public  String  getPersianDescription(){
        return PersianDescription;
    }
}
