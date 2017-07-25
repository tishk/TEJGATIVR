package ServiceObjects.Pan;

import java.io.Serializable;

/**
 * Created by Administrator on 6/21/2015.
 */
public class SabaSwitchResponse implements Serializable {

    private static final long serialVersionUID = 7933179712342788700L;

    private boolean SendResult=false;
    public  void    setSendResult(boolean sendResult){
        SendResult=sendResult;
    }
    public  boolean getSendResult(){
        return SendResult;
    }

}
