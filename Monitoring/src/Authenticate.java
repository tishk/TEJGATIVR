import ServiceObjects.Pin.AuthenticatePin1;
import ServiceObjects.Pin.AuthenticatePin2;

import java.io.IOException;
import java.rmi.server.ServerNotActiveException;
import java.sql.SQLException;

/**
 * Created by root on 4/23/16.
 */
public class Authenticate extends Status {

    private int  PinKind=0;
    public  void setPinKind(int pinKind){
        PinKind=pinKind;
    }
    public  int  getPinKind(){
        return PinKind;
    }


    public Authenticate(String[] params) throws ClassNotFoundException, SQLException, ServerNotActiveException, IOException {
        setPinKind(0);
        initializeAccountParameters(params[2]);
        getPin1Status();
        getPin2Status();
   }
    public Authenticate(String[] params,boolean isPin1) throws IOException, ServerNotActiveException, SQLException, ClassNotFoundException {
        initializeAccountParameters(params[2]);
        if (isPin1) {
            setPinKind(1);
            getPin1Status();
        }
        else{
            setPinKind(2);
            getPin2Status();
        }

    }

    public void getPin1Status() throws IOException, ServerNotActiveException, SQLException, ClassNotFoundException {
        AuthenticatePin1 authenticatePin1=new AuthenticatePin1();
        authenticatePin1.setAccountNumber(getAccountNumber());
        authenticatePin1.setPin(getPin1());
        authenticatePin1.setDoChangePin(false);
        authenticatePin1.setCallerID("2188904262");
        try{
            authenticatePin1=(AuthenticatePin1)submitRequestToGateway(authenticatePin1);
        }catch (Exception e){
            System.out.println(e.toString());
        }

        setgPin1isOKActionCode(authenticatePin1.getActionCode());
        if (getgPin1isOKActionCode().equals("0000"))
            setgPin1isOK("1");
        else
            setgPin1isOK("0");
        setgPin1isOKDateTime(getNowDateTime());
        setgPin1isOKDesc("---");

        //new DBUtils(this);
    }
    public void getPin2Status() throws IOException, ServerNotActiveException, SQLException, ClassNotFoundException {
        AuthenticatePin2 authenticatePin2=new AuthenticatePin2();
        authenticatePin2.setAccountNumber(getAccountNumber());
        authenticatePin2.setPin(getPin2());
        authenticatePin2.setDoChangePin(false);
        authenticatePin2.setCallerID("2188910250");
        authenticatePin2=(AuthenticatePin2)submitRequestToGateway(authenticatePin2);
        setgPin2isOKActionCode(authenticatePin2.getActionCode());
        if (getgPin2isOKActionCode().equals("0000"))
            setgPin2isOK("1");
        else
            setgPin2isOK("0");
        setgPin2isOKDateTime(getNowDateTime());
        setgPin2isOKDesc("---");

       // new DBUtils(this);

    }

    private String  gPin1isOK="";
    public  void    setgPin1isOK(String Pin1isOK){
        gPin1isOK=Pin1isOK;
    }
    public  String  getgPin1isOK(){
        return gPin1isOK;
    }

    private String  gPin1isOKDateTime="";
    public  void    setgPin1isOKDateTime(String Pin1isOKDateTime){
        gPin1isOKDateTime=Pin1isOKDateTime;
    }
    public  String  getgPin1isOKDateTime(){
        return gPin1isOKDateTime;
    }

    private String  gPin1isOKActionCode="";
    public  void    setgPin1isOKActionCode(String Pin1isOKActionCode){
        gPin1isOKActionCode=Pin1isOKActionCode;
    }
    public  String  getgPin1isOKActionCode(){
        return gPin1isOKActionCode;
    }

    private String  gPin1isOKDesc="";
    public  void    setgPin1isOKDesc(String Pin1isOKDesc){
        gPin1isOKDesc=Pin1isOKDesc;
    }
    public  String  getgPin1isOKDesc(){
        return gPin1isOKDesc;
    }

    private String  gPin2isOK="";
    public  void    setgPin2isOK(String Pin2isOK){
        gPin2isOK=Pin2isOK;
    }
    public  String  getgPin2isOK(){
        return gPin2isOK;
    }

    private String  gPin2isOKDateTime="";
    public  void    setgPin2isOKDateTime(String Pin2isOKDateTime){
        gPin2isOKDateTime=Pin2isOKDateTime;
    }
    public  String  getgPin2isOKDateTime(){
        return gPin2isOKDateTime;
    }

    private String  gPin2isOKActionCode="";
    public  void    setgPin2isOKActionCode(String Pin2isOKActionCode){
        gPin2isOKActionCode=Pin2isOKActionCode;
    }
    public  String  getgPin2isOKActionCode(){
        return gPin2isOKActionCode;
    }

    private String  gPin2isOKDesc="";
    public  void    setgPin2isOKDesc(String Pin2isOKDesc){
        gPin2isOKDesc=Pin2isOKDesc;
    }
    public  String  getgPin2isOKDesc(){
        return gPin2isOKDesc;
    }
}
