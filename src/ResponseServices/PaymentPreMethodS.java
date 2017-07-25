package ResponseServices;

import Mainchannel.Exceptions.InvalidParameterException;
import Mainchannel.Exceptions.ResponseParsingException;
import Mainchannel.sender.SenderException;
import ServiceObjects.Account.BillPayByIDAccount;
import ServiceObjects.Account.FundTransfer;
import ServiceObjects.Account.InternalFollowUp;
import ServiceObjects.Other.LoggerToDB;
import ServiceObjects.Pan.BillPayByBillIDPan;
import utils.strUtils;

import java.io.*;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Random;

/**
 * Created by Administrator on 1/20/2016.
 */
public class PaymentPreMethodS {

    strUtils strutils=new strUtils();

    private String ClientNo="777";
    private String FChannelNo="007";
    private String Cid="";
    private String DeviceCode="07";
    private String MacAddress="aa-bb-cc-dd-ee-ff";
    private String MsgSeq="";
    private String response="";

    private String oldGatewayIP="10.39.213.253";
    private int    oldGatewayPort=19696;

    public PaymentPreMethodS(Object ObjectForPayment) throws InvalidParameterException, SQLException, ResponseParsingException, SenderException, UnsupportedEncodingException, InterruptedException, ClassNotFoundException {

        if (ObjectForPayment instanceof FundTransfer){
            accountFundTransfer((FundTransfer) ObjectForPayment);
        }else if (ObjectForPayment instanceof BillPayByIDAccount){
            accountBillPayment((BillPayByIDAccount)ObjectForPayment);
        }else if (ObjectForPayment instanceof BillPayByBillIDPan){
            panBillPayment((BillPayByBillIDPan)ObjectForPayment);
        }else if (ObjectForPayment instanceof InternalFollowUp){
            accountFollowPayment((InternalFollowUp) ObjectForPayment);
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

    private String DoneFlag="-1";
    private void   setdoneFlag(String doneFlag){
        DoneFlag=doneFlag;
    }
    public  String getdoneFlag(){
        return DoneFlag;
    }

    private String referenceCode="";
    private void   setreferenceCode(String referencecode){
        referenceCode=referencecode;
    }
    public  String getreferenceCode(){
        return referenceCode;
    }

    private Object ResultObject="";
    public  void   setResultObject(Object resultObject){
        ResultObject=resultObject;
    }
    public  Object getResultObject(){
        return ResultObject;
    }

    private String getReferenceCode(){

        return String.valueOf(System.nanoTime()).substring(0,12);

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

    private void  processFundTranseferResponse(){
        try{
            if (strutils.leftString(response,15).equals(MsgSeq)){
                response=strutils.rightString(response,response.length()-15);
                setActionCode(strutils.leftString(response,4));

                setreferenceCode(strutils.rightString(response,6));
                //System.out.println("Pin autenticate Action Code is:"+strutils.leftString(response,4));
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
    private void  processBillPaymentResponse(){
        try{
            if (strutils.leftString(response,15).equals(MsgSeq)){
                response=response;
                response=strutils.rightString(response,response.length()-15);

                setActionCode(strutils.leftString(response,4));

                setreferenceCode(strutils.rightString(response,6));
                //System.out.println("Pin autenticate Action Code is:"+strutils.leftString(response,4));
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
    private void  processFollowUPResponse(){
        try{
            if (strutils.leftString(response,15).equals(MsgSeq)){
                response=response;
                response=strutils.rightString(response,response.length()-15);

                setActionCode(strutils.leftString(response,4));

                setreferenceCode(strutils.rightString(response,6));
                //System.out.println("Pin autenticate Action Code is:"+strutils.leftString(response,4));
                if (getActionCode().equals("0000")) setResult(true);
                else setResult(false);
                setdoneFlag(String.valueOf(response.charAt(4)));
            }else{
                setResult(false);
                setActionCode("9999");
            }
        }catch (Exception e){
            setResult(false);
            setActionCode("9999");
        }
    }


    private void  accountFundTransfer(FundTransfer fundTransfer) throws SenderException, InvalidParameterException, ResponseParsingException, SQLException, ClassNotFoundException {
        try{
            MsgSeq=  ClientNo+FChannelNo+getRandomNumber();
            String typ="";

            if (fundTransfer.getIsFundTransfer()) typ="E";
            else if (fundTransfer.getIsInstallmentPay()) typ="C";
            else if (fundTransfer.getIsIdentFundTranfer()) typ="F";
            String sendString=  MsgSeq+
                    strutils.fixLengthWithZero(Cid,18)+
                    DeviceCode+
                    MacAddress+
                    typ+
                    strutils.fixLengthWithZero(fundTransfer.getSourceAccount(),10)+
                    strutils.fixLengthWithZero(fundTransfer.getDestinationAccount(),10)+
                    strutils.fixLengthWithZero(fundTransfer.getTransactionAmount(),15)+
                    strutils.fixLengthWithZero(String.valueOf(fundTransfer.getTraceID().length()),2)+
                    strutils.fixLengthWithZero(fundTransfer.getTraceID(),20);
            //System.out.println("in authenticatePin1 len is:"+String.valueOf(sendString.length()));
            if (sentToOldGateway(sendString))  processFundTranseferResponse();
            fundTransfer.setActionCode(getActionCode());
            fundTransfer.setTraceID(getreferenceCode());
            fundTransfer.setMsgSeq(getReferenceCode());
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LoggerToDB loggerToDB =new LoggerToDB(fundTransfer);
        this.setResultObject(fundTransfer);
    }

    private void  accountBillPayment(BillPayByIDAccount billPayByIDAccount) throws SenderException, InvalidParameterException, ResponseParsingException, SQLException {

        try{
            MsgSeq=  ClientNo+FChannelNo+getRandomNumber();
            String sendString=  MsgSeq+
                    strutils.fixLengthWithZero(Cid,18)+
                    DeviceCode+
                    MacAddress+
                    "G"+
                    strutils.fixLengthWithZero(billPayByIDAccount.getSourceAccount(),10)+
                    strutils.fixLengthWithZero(billPayByIDAccount.getBillID(),13)+
                    strutils.fixLengthWithZero(billPayByIDAccount.getPaymentID(),13)+
                    strutils.fixLengthWithZero(billPayByIDAccount.getAmount(),15);
            if (sentToOldGateway(sendString))  processBillPaymentResponse();
            billPayByIDAccount.setActionCode(getActionCode());
            billPayByIDAccount.setTraceCode(getreferenceCode());
            billPayByIDAccount.setMsgSeq(getReferenceCode());

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LoggerToDB loggerToDB =new LoggerToDB(billPayByIDAccount);
        this.setResultObject(billPayByIDAccount);
    }

    private void  accountFollowPayment(InternalFollowUp internalFollowUp) throws SenderException, InvalidParameterException, ResponseParsingException, SQLException {
        try{
           //3260300003240410000000021889042620700-0B-CD-AF-0F-8CXP60379914679164940000805353
            String kind="";
            if (internalFollowUp.getIsPanPayment())
                 kind="XP";
            else kind="P";

            MsgSeq=  ClientNo+FChannelNo+getRandomNumber();
            String sendString=  MsgSeq+
                    strutils.fixLengthWithZero(Cid,18)+
                    DeviceCode+
                    MacAddress+
                    kind+
                    strutils.fixLengthWithZero(internalFollowUp.getSourceAccount(),10)+
                    strutils.fixLengthWithZero(internalFollowUp.getFollowUpCode(),10);
            if (sentToOldGateway(sendString))  processFollowUPResponse();
            internalFollowUp.setActionCode(getActionCode());
            internalFollowUp.setdoneFlag(getdoneFlag());
            internalFollowUp.setMsgSeq(getReferenceCode());

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LoggerToDB loggerToDB =new LoggerToDB(internalFollowUp);
        this.setResultObject(internalFollowUp);
    }

    private void  panFollowPayment(InternalFollowUp internalFollowUp) throws SenderException, InvalidParameterException, ResponseParsingException, SQLException {
        try{
            MsgSeq=  ClientNo+FChannelNo+getRandomNumber();
            String sendString=  MsgSeq+
                    strutils.fixLengthWithZero(Cid,18)+
                    DeviceCode+
                    MacAddress+
                    "XP"+
                    strutils.fixLengthWithZero(internalFollowUp.getSourceAccount(),16)+
                    strutils.fixLengthWithZero(internalFollowUp.getFollowUpCode(),10);
            if (sentToOldGateway(sendString))  processFollowUPResponse();
            internalFollowUp.setActionCode(getActionCode());
            internalFollowUp.setdoneFlag(getdoneFlag());
            internalFollowUp.setMsgSeq(getReferenceCode());

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LoggerToDB loggerToDB =new LoggerToDB(internalFollowUp);
        this.setResultObject(internalFollowUp);
    }

    private void  panBillPayment(BillPayByBillIDPan billPayByBillIDPan) throws SenderException, InvalidParameterException, ResponseParsingException, SQLException, UnsupportedEncodingException, InterruptedException {

        try{
            MsgSeq=  ClientNo+FChannelNo+getRandomNumber();
            String sendString=  MsgSeq+
                    strutils.fixLengthWithZero(Cid,18)+
                    DeviceCode+
                    MacAddress+
                    "XG"+
                    strutils.fixLengthWithZero(billPayByBillIDPan.getPan(),16)+
                    strutils.fixLengthWithZero(String.valueOf(billPayByBillIDPan.getPin().length()),2)+
                    strutils.fixLengthWithZero(billPayByBillIDPan.getPin(),12)+
                    strutils.fixLengthWithZero(billPayByBillIDPan.getBillID(),13)+
                    strutils.fixLengthWithZero(billPayByBillIDPan.getPaymentID(),13)+
                    strutils.fixLengthWithZero(billPayByBillIDPan.getAmount(),15);
            if (sentToOldGateway(sendString))  processBillPaymentResponse();
            billPayByBillIDPan.setActionCode(getActionCode());
            billPayByBillIDPan.setTraceCode(getreferenceCode());
            billPayByBillIDPan.setMsgSeq(getReferenceCode());

        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LoggerToDB loggerToDB =new LoggerToDB(billPayByBillIDPan);
        this.setResultObject(billPayByBillIDPan);


        //this.setResultObject(gatewayServices.cardBillPayByBillIDPan(billPayByBillIDPan));
    }

}
