package ServiceObjects.Pin;

import Mainchannel.Exceptions.InvalidParameterException;
import Mainchannel.Exceptions.ResponseParsingException;
import Mainchannel.sender.SenderException;
import ResponseServices.GatewayServices;
import ServiceObjects.Account.AccountInformation;
import utils.strUtils;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Random;


public class PreMethodAuthentication implements Serializable {

    strUtils strutils=new strUtils();
    private String getReferenceCode(){

        return String.valueOf(System.nanoTime()).substring(0,12);

    }
    AuthenticatePin1 authenticatePin1=new AuthenticatePin1();
    AuthenticatePin2 authenticatePin2=new AuthenticatePin2();
    GatewayServices gatewayServices=new GatewayServices();

    private String accountNumber ="";
    private String Pin1="";
    private String Pin2="";
    private boolean isHaghighi=true;
    private boolean arziAccount=false;

    private String oldPin="";
    private String newPin="";

    private String ClientNo="777";
    private String FChannelNo="007";
    private String Cid="";
    private String DeviceCode="07";
    private String MacAddress="aa-bb-cc-dd-ee-ff";
    private String MsgSeq="";
    private String response="";

//    private String oldGatewayIP="10.39.213.253";
    private String oldGatewayIP="10.39.213.253";
//    private int    oldGatewayPort=19696;
    private int    oldGatewayPort=19696;
    public PreMethodAuthentication(Object object) throws InterruptedException {
        if (object instanceof AuthenticatePin1 && !((AuthenticatePin2) object).getDoChangePin()){
            authenticatePin1((AuthenticatePin1) object);
        }else if (object instanceof AuthenticatePin2 && ((AuthenticatePin2) object).getIsNotHaghighiAccount()){
            authenticatePin1NotHaghighi((AuthenticatePin2) object);
        }else if (object instanceof AuthenticatePin2 ){
            authenticatePin2((AuthenticatePin2) object);
        }else if (object instanceof ChangePIN1 ){
            changePin1((ChangePIN1) object);
        }else if (object instanceof ChangePIN2){
            changePin2((ChangePIN2) object);
        }
    }
    public PreMethodAuthentication(Object object,boolean isNewMethod) throws InterruptedException, InvalidParameterException, SQLException, ResponseParsingException, SenderException, ClassNotFoundException {

        if (object instanceof AuthenticatePin1){
          if (((AuthenticatePin1) object).getDoChangePin()) {

          }else{
              AccountInformation accountInformation =new AccountInformation();
              accountInformation.setAccountNumber(((AuthenticatePin1) object).getAccountNumber());
              accountInformation.setCallUniqID("777"+getReferenceCode());
              accountInformation=gatewayServices.accountInformation(accountInformation);
              if (accountInformation.getResultFromChannel().getPrsnType().equals("1")){
                   isHaghighi=true;
              }else{
                  isHaghighi=false;
              }

          }
        }else{
            if (((AuthenticatePin2) object).getDoChangePin()) {

            }else{

            }
        }
        if (object instanceof AuthenticatePin1 && !((AuthenticatePin2) object).getDoChangePin()){
            authenticatePin1((AuthenticatePin1) object);
        }else if (object instanceof AuthenticatePin2 && ((AuthenticatePin2) object).getIsNotHaghighiAccount()){
            authenticatePin1NotHaghighi((AuthenticatePin2) object);
        }else if (object instanceof AuthenticatePin2 ){
            authenticatePin2((AuthenticatePin2) object);
        }
    }
    public PreMethodAuthentication(String accountNumber, String pin, String callerID, boolean isPin1) throws InterruptedException, InvalidParameterException, SQLException, ResponseParsingException, SenderException, ClassNotFoundException {

        this.accountNumber = accountNumber;
        if (accountNumber.equals("0353056352")){
            System.out.println("test");
        }
        //System.out.println("accountNumber:"+accountNumber);

        this.Cid=callerID;
        //System.out.println("callerID:"+callerID);
        if (isPin1){
            setTypeOfAccount(accountNumber);
            this.Pin1=pin;
            if (!arziAccount){
                authenticatePin1();
            }else{
                authenticateArziAccount(accountNumber);
            }
        }
        else {
            System.out.println("is pin2");
            this.Pin2=pin;
            authenticatePin2();
        }
    }

    private void authenticateArziAccount(String account) throws SQLException, InterruptedException {
        CurrencyAccountAuthenticate currencyAccountAuthenticate=new CurrencyAccountAuthenticate(account,Pin1);
        setActionCode(currencyAccountAuthenticate.getActionCode());
    }

    private void setTypeOfAccount(String account) throws SenderException, InvalidParameterException, ResponseParsingException, SQLException, ClassNotFoundException {
        if (account.equals("0353056352")||account.equals("353056352")){
            System.out.println("Test");
        }
        AccountInformation accountInformation =new AccountInformation();
        accountInformation.setAccountNumber(account);
        accountInformation.setCallUniqID("777"+getReferenceCode());
        accountInformation=gatewayServices.accountInformation(accountInformation);
        if (accountInformation.getResultFromChannel().getAction_code().equals("0000")){


            if (!accountInformation.getResultFromChannel().getAccountcurrency().equals("000") ){
                arziAccount=true;
            }
            if (!arziAccount){

                if (accountInformation.getResultFromChannel().getPrsnType().equals("1")){
                    isHaghighi=true;
                }else{
                    isHaghighi=false;
                }
            }
        }
    }

    public PreMethodAuthentication(String accountNumber, String pin, String callerID, boolean isPin1, boolean isNotHaghighi) {

        this.accountNumber = accountNumber;

        this.Cid=callerID;

        this.Pin1=pin;

        if (accountNumber.equals("353056352")|| accountNumber.equals("0353056352")){
            System.out.println("test");
        }
        try {
            setTypeOfAccount(accountNumber);
        } catch (SenderException e) {
            e.printStackTrace();
        } catch (InvalidParameterException e) {
            e.printStackTrace();
        } catch (ResponseParsingException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        authenticatePin1();

    }
    public PreMethodAuthentication(String accountNumber, String oldPin, String newPin, String callerID, boolean isPin1) throws InterruptedException, InvalidParameterException, SQLException, ResponseParsingException, SenderException, ClassNotFoundException {
        this.accountNumber = accountNumber;
        this.Cid=callerID;
        this.oldPin=oldPin;
        this.newPin=newPin;
        if (isPin1){
            setTypeOfAccount(accountNumber);
            changePin("0");
        }
        else{
            changePin("1");
        }
    }


    private boolean sentToOldGateway(String snd) throws InterruptedException {

        InputStreamReader ioe =null;
        BufferedReader bufferedreader =null;
        PrintWriter out =null;
        Socket socket=null;
        try {
            socket=new Socket(oldGatewayIP,oldGatewayPort);
            socket.setSoTimeout(10000);
            ioe = new InputStreamReader(socket.getInputStream());
            bufferedreader = new BufferedReader(ioe);
            out = new PrintWriter(socket.getOutputStream(), true);
            out.println(snd);
            out.flush();
            for(String lineread = ""; (lineread = bufferedreader.readLine()) != null; response = response + lineread) ;
            response=response.trim();
        } catch (IOException e) {
            if (bufferedreader!=null) try {bufferedreader.close();bufferedreader = null;} catch (IOException e1) {}
            if (ioe!=null) try {ioe.close();ioe = null;} catch (IOException e1) {}
            if (out!=null) try {out.close();out = null;} catch (Exception e1) {}
            if (socket!=null) try {socket.close();socket = null;} catch (IOException e1) {}
            setResult(false);
            setActionCode("9999");
            return false;

        }finally {
            if (bufferedreader!=null) try {bufferedreader.close();bufferedreader = null;} catch (IOException e1) {}
            if (ioe!=null) try {ioe.close();ioe = null;} catch (IOException e1) {}
            if (out!=null) try {out.close();out = null;} catch (Exception e1) {}
            if (socket!=null) try {socket.close();socket = null;} catch (IOException e1) {}
            setResult(true);
            setActionCode(response);
            return true;
        }
    }

    private boolean Result=false;
    private void    setResult(boolean result){
        Result=result;
    }
    public  boolean getResult(){
        return Result;
    }

    private String ActionCode="9999";
    private void   setActionCode(String actionCode){
        ActionCode=actionCode;
    }
    public  String getActionCode(){
        return ActionCode;
    }

    private Object ResultObject="";
    public  void   setResultObject(Object resultObject){
        ResultObject=resultObject;
    }
    public  Object getResultObject(){
        return ResultObject;
    }

    private int    randInt(int min, int max) {

        Random rand = new Random();

        int randomNum = rand.nextInt((max - min) + 1) + min;

        return randomNum;
    }
    private String getRandomNumber(){
        return   strutils.fixLengthWithZero(String.valueOf(randInt(0, 99999)), 5)+
                strutils.fixLengthWithZero(String.valueOf(randInt(0, 9999)), 4) ;
    }


    private void  authenticatePin1() {
        if (accountNumber.equals("421681759")||accountNumber.equals("0421681759")){
            System.out.println("test");
        }
        try{
            MsgSeq=  ClientNo+FChannelNo+getRandomNumber();
            String sendString=  MsgSeq+
                    strutils.fixLengthWithZero(Cid,18)+
                    DeviceCode+
                    MacAddress+
                    "OA"+
                    strutils.fixLengthWithZero(accountNumber,10)+
                    strutils.fixLengthWithZero(String.valueOf(Pin1.length()),2)+
                    strutils.fixLengthWithZero(Pin1,8);
            if (isHaghighi){
                sendString=sendString+"1";
            }else{
                sendString=sendString+"2";
            }
            if (sentToOldGateway(sendString))  processAuthenticateResponse();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    private void  authenticatePin2() throws InterruptedException {
        try{
        MsgSeq=  ClientNo+FChannelNo+getRandomNumber();
        String sendString=  MsgSeq+
                strutils.fixLengthWithZero(Cid,18)+
                DeviceCode+
                MacAddress+
                "VA"+
                strutils.fixLengthWithZero(accountNumber,10)+
                //strutils.fixLengthWithZero(String.valueOf(Pin2.length()),2)+
                strutils.fixLengthWithZero("#"+Pin2,12);
            System.out.println("sentToOldGateway:"+sendString);
        if (sentToOldGateway(sendString))  processAuthenticateResponse();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void  authenticatePin1(AuthenticatePin1 authPin1) {
        this.authenticatePin1=authPin1;
        try{
            MsgSeq=  ClientNo+FChannelNo+getRandomNumber();
            String sendString=  MsgSeq+
                    strutils.fixLengthWithZero(Cid,18)+
                    DeviceCode+
                    MacAddress+
                    "OA"+
                    strutils.fixLengthWithZero(authenticatePin1.getAccountNumber(),10)+
                    strutils.fixLengthWithZero(String.valueOf(authenticatePin1.getPin().length()),2)+
                    strutils.fixLengthWithZero(authenticatePin1.getPin(),8)
                    +"1";
            //System.out.println("in authenticatePin1 len is:"+String.valueOf(sendString.length()));
            if (sentToOldGateway(sendString))  processAuthPin1ObjectResponse();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    private void  authenticatePin1NotHaghighi(AuthenticatePin2 authPin2) {
        this.authenticatePin2=authPin2;
        try{
            MsgSeq=  ClientNo+FChannelNo+getRandomNumber();
            String sendString=  MsgSeq+
                    strutils.fixLengthWithZero(Cid,18)+
                    DeviceCode+
                    MacAddress+
                    "OA"+
                    strutils.fixLengthWithZero(authenticatePin2.getAccountNumber(),10)+
                    strutils.fixLengthWithZero(String.valueOf(authenticatePin2.getPin().length()),2)+
                    strutils.fixLengthWithZero(authenticatePin2.getPin(),8)
                    +"2";
            //System.out.println("in authenticatePin1 len is:"+String.valueOf(sendString.length()));
            if (sentToOldGateway(sendString))  processAuthPin1NotHaghighiObjectResponse();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

    }
    private void  authenticatePin2(AuthenticatePin2 authPin2) throws InterruptedException {
        this.authenticatePin2=authPin2;
        try{
            MsgSeq=  ClientNo+FChannelNo+getRandomNumber();
            String sendString=  MsgSeq+
                    strutils.fixLengthWithZero(Cid,18)+
                    DeviceCode+
                    MacAddress+
                    "VA"+
                    strutils.fixLengthWithZero(authenticatePin2.getAccountNumber(),10)+
                    strutils.fixLengthWithZero("#"+authenticatePin2.getPin(),12);
            System.out.println("sentToOldGateway:"+sendString);
            if (sentToOldGateway(sendString))  processAuthPin2ObjectResponse();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void  changePin1(ChangePIN1 changePIN1) throws InterruptedException {
        //accountNumber=changePIN1.
        changePin("0");
    }
    private void  changePin2(ChangePIN2 changePIN2) throws InterruptedException {
       changePin("1");
    }

    private void  processAuthPin1ObjectResponse(){
        try{
            if (strutils.leftString(response,15).equals(MsgSeq)){
                response=strutils.rightString(response,response.length()-15);
                setActionCode(strutils.leftString(response,4));

                System.out.println("Pin autenticate Action Code is:"+strutils.leftString(response,4));
                if (getActionCode().equals("0000")) setResult(true);
                else setResult(false);
                authenticatePin1.setActionCode(getActionCode());
                authenticatePin1.setMsgSeq(getReferenceCode());
                setResultObject(authenticatePin1);
            }else{
                setResult(false);
                setActionCode("9999");
            }
        }catch (Exception e){
            setResult(false);
            setActionCode("9999");
        }
    }
    private void  processAuthPin1NotHaghighiObjectResponse(){
        try{
            if (strutils.leftString(response,15).equals(MsgSeq)){
                response=strutils.rightString(response,response.length()-15);
                setActionCode(strutils.leftString(response,4));

                System.out.println("Pin autenticate Action Code is:"+strutils.leftString(response,4));
                if (getActionCode().equals("0000")) setResult(true);
                else setResult(false);
                authenticatePin2.setActionCode(getActionCode());
                authenticatePin2.setMsgSeq(getReferenceCode());
                setResultObject(authenticatePin2);
            }else{
                setResult(false);
                setActionCode("9999");
            }
        }catch (Exception e){
            setResult(false);
            setActionCode("9999");
        }
    }
    private void  processAuthPin2ObjectResponse(){
        try{
            if (strutils.leftString(response,15).equals(MsgSeq)){
                response=strutils.rightString(response,response.length()-15);
                setActionCode(strutils.leftString(response,4));

                System.out.println("Pin autenticate Action Code is:"+strutils.leftString(response,4));
                if (getActionCode().equals("0000")) setResult(true);
                else setResult(false);
                authenticatePin2.setActionCode(getActionCode());
                authenticatePin2.setMsgSeq(getReferenceCode());
                setResultObject(authenticatePin2);
            }else{
                setResult(false);
                setActionCode("9999");
            }
        }catch (Exception e){
            setResult(false);
            setActionCode("9999");
        }
    }

    private void  processAuthenticateResponse(){
        try{
            if (strutils.leftString(response,15).equals(MsgSeq)){
                response=strutils.rightString(response,response.length()-15);
                setActionCode(strutils.leftString(response,4));
                System.out.println("Pin autenticate Action Code is:"+strutils.leftString(response,4));
                if (getActionCode().equals("0000")) setResult(true);
                else setResult(false);
            }else{
                setResult(false);
                setActionCode("9999");
            }
        }catch (Exception e){
            setResult(false);
            setActionCode("9999");
        }
    }

    private void  changePin(String KindOfPin) throws InterruptedException {
        MsgSeq=  ClientNo+FChannelNo+getRandomNumber();
        String sendString=  MsgSeq+
                            strutils.fixLengthWithZero(Cid,18)+
                            DeviceCode+
                            MacAddress+
                            "R"+
                            strutils.fixLengthWithZero(accountNumber,10)+
                            strutils.fixLengthWithZero(oldPin,12)+
                            strutils.fixLengthWithZero(newPin,12)+
                            KindOfPin+
                            strutils.fixLengthWithZero(String.valueOf(oldPin.length()),2)+
                            strutils.fixLengthWithZero(String.valueOf(newPin.length()),2);
        if (KindOfPin.equals("0")){
            if (isHaghighi){
                sendString=sendString+"1";
            }else {
                sendString=sendString+"2";
            }
        }else{
                sendString=sendString+"1";
        }


        if (sentToOldGateway(sendString))  processChangePinResponse();
    }

    private void  processChangePinResponse(){
        try{
            if (strutils.leftString(response,15).equals(MsgSeq)){
                response=strutils.rightString(response,response.length()-15);
                setActionCode(strutils.leftString(response,4));
                if (getActionCode().equals("0000")) setResult(true);
                else setResult(false);
            }else{
                setResult(false);
                setActionCode("9999");
            }
        }catch (Exception e){
            setResult(false);
            setActionCode("9999");
        }
    }




}
