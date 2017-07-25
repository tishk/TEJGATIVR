package ServiceObjects.Account;

import utils.strUtils;

/**
 * Created by Administrator on 28/05/2015.
 */
public class ShebaID extends BaseAccountRequest {

    public ShebaID(){

    }
    private strUtils strutils=new strUtils();
    private String AccountNumber=null;
    public  void   setAccountNumber(String accountNumber){
        AccountNumber=accountNumber;
    }
    public  String getAccountNumber(){
        return AccountNumber;
    }

    private String ShebaID=null;
    public  void   setShebaID(String shebaID){
        ShebaID=shebaID;
    }
    public  String getShebaID(){
        return ShebaID;
    }

    public  void calculateShebaID(){
        try {

            String tempAcc=strutils.fixLengthWithZero(this.getAccountNumber(),19);
            String IBAN="IR00018"+tempAcc;
            String st=strutils.leftString(IBAN,4);
            IBAN=strutils.rightString(IBAN,IBAN.length()-4)+st;
            IBAN=strutils.replaceText(IBAN,"I","18");
            IBAN=strutils.replaceText(IBAN,"R","27");
            st=strutils.leftString(IBAN,15);
            Long tempLong=Long.valueOf(st) % 97;
            String temp2=String.valueOf(tempLong)+strutils.rightString(IBAN,13);
            tempLong=Long.valueOf(temp2)%97;
            tempLong=98-tempLong;
            String res="IR"+strutils.rightString("00"+String.valueOf(tempLong),2)+"018"+tempAcc;
            this.setShebaID(res);

        }catch (Exception var1){
            this.setActionCode("6515");
        }finally {
            this.setActionCode("0000");
        }

    }




}
