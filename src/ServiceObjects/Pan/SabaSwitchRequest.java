package ServiceObjects.Pan;

import java.io.Serializable;

/**
 * Created by Administrator on 6/21/2015.
 */
public class SabaSwitchRequest implements Serializable {

    private static final long serialVersionUID = 7999999759822788700L;
    private boolean SendResult=false;
    public  void    setSendResult(boolean sendResult){
        SendResult=sendResult;
    }
    public  boolean getSendResult(){
        return SendResult;
    }



    private String ResponseValue="";
    public  void    setResponseValue(String responseValue){
        ResponseValue=responseValue;
    }
    public  String getResponseValue(){
        return ResponseValue;
    }

    private String ResponseCode="";
    public  void    setResponseCode(String responseCode){
        ResponseCode=responseCode;
    }
    public  String getResponseCode(){
        return ResponseCode;
    }



}
