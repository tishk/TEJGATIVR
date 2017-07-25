package ServiceObjects.Other;

import ServiceObjects.Account.BaseAccountRequest;
import utils.SMS.TelBank;
import utils.SMS.TelBankSoap;

/**
 * Created by Administrator on 28/05/2015.
 */
public class SMSAlarmTransaction  extends BaseAccountRequest {


    private String AccountNumber=null;
    public  void   setAccountNumber(String accountNumber){
        AccountNumber=accountNumber;
    }
    public  String getAccountNumber(){
        return AccountNumber;
    }

    private String MobileNumber=null;
    public  void   setMobileNumber(String mobileNumber){
        MobileNumber=mobileNumber;
    }
    public  String getMobileNumber(){
        return MobileNumber;
    }

    private String TRANSACTIONID=null;
    private  void   setTRANSACTIONID(String tRANSACTIONID){
        TRANSACTIONID=tRANSACTIONID;
    }
    public  String getTRANSACTIONID(){
        return TRANSACTIONID;
    }

    private boolean MobileChanged=false;
    private void    setMobileChanged(boolean mobileChanged){
        MobileChanged=mobileChanged;
    }
    public  boolean getMobileChanged(){
        return MobileChanged;
    }

    private boolean IsSetMobileNumber=false;
    public  void    setIsSetMobileNumber(boolean isSetMobileNumber){
        IsSetMobileNumber=isSetMobileNumber;
    }
    public  boolean getIsSetMobileNumber(){
        return IsSetMobileNumber;
    }

    private boolean IsDeleteMobileNumber=false;
    public  void    setIsDeleteMobileNumber(boolean isDeleteMobileNumber){
        IsDeleteMobileNumber=isDeleteMobileNumber;
    }
    public  boolean getIsDeleteMobileNumber(){
        return IsDeleteMobileNumber;
    }


    private String  FixLengthWithZero(String Str,int length){
        String Temp=Str;
        for (int i=0;i<length-Str.length();i++) Temp="0"+Temp;
        return Temp;
    }
    private String  CreateSetMobileMessage(String Acc,String MobileNO,boolean isDelete){
       if (isDelete){
           return FixLengthWithZero(MobileNO,11)+";"+FixLengthWithZero(Acc,13)+";0;d";
       }else{
          return FixLengthWithZero(MobileNO,11)+";"+FixLengthWithZero(Acc,13)+";0;i";
       }

    }

    public   void   GetMobileNumberFromWebService(){

        try {
            TelBank TelBankSMSNO = new TelBank();
            TelBankSoap Tel= TelBankSMSNO.getTelBankSoap();
            setTRANSACTIONID(getNewMsgSequence());
            setMobileNumber(Tel.getAccountInfo(getAccountNumber()));
            setActionCode("0000");


        } catch (Exception e) {
            setTRANSACTIONID(getNewMsgSequence());
            System.err.println("Error occurred while sending SOAP Request to Server");
            e.printStackTrace();
            setActionCode("6512");
            setMobileNumber("-1");
        }

    }
    public   void   SetMobileNumberInWebService(){

            try {
                TelBank TelBankSMSNO = new TelBank();
                TelBankSoap Tel= TelBankSMSNO.getTelBankSoap();
                setTRANSACTIONID(getNewMsgSequence());
                if (Tel.setAccountInfo(CreateSetMobileMessage(getAccountNumber(),getMobileNumber(),getIsDeleteMobileNumber())).equals("1")){
                    setMobileChanged(true);
                    setActionCode("0000");
                }
                else {
                    setMobileChanged(false);
                    setActionCode("6513");
                }



            } catch (Exception e) {
                setTRANSACTIONID(getNewMsgSequence());
                System.err.println("Error occurred while sending SOAP Request to Server");
                e.printStackTrace();
                setMobileChanged(false);
            }

    }
    public   void   deleteMobileNumberInWebService(){

        try {
            TelBank TelBankSMSNO = new TelBank();
            TelBankSoap Tel= TelBankSMSNO.getTelBankSoap();
            setTRANSACTIONID(getNewMsgSequence());
            if (Tel.setAccountInfo(CreateSetMobileMessage(getAccountNumber(),getMobileNumber(),false)).equals("1")){
                setMobileChanged(true);
                setActionCode("0000");
            }
            else {
                setMobileChanged(false);
                setActionCode("6513");
            }



        } catch (Exception e) {
            setTRANSACTIONID(getNewMsgSequence());
            System.err.println("Error occurred while sending SOAP Request to Server");
            e.printStackTrace();
            setMobileChanged(false);
        }

    }

    public   String getNewMsgSequence() {
        String seq;
        seq = String.valueOf(System.nanoTime()).substring(0,12);
        return seq;
    }




}
