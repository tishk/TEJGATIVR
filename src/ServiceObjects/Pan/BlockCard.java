package ServiceObjects.Pan;

import ServiceObjects.ISO.ISO210;

import java.io.Serializable;

/**
 * Created by Administrator on 10/21/2015.
 */
public class BlockCard extends BaseCardRequest   {

    private String ActionCode="";
    public  void    setActionCode(String actionCode){
        ActionCode=actionCode;
    }
    public  String getActionCode(){
        return ActionCode;
    }

    private ISO210 ResultFromServer=null;
    public  void setResultFromServer(ISO210 resultFromServer){
        this.ResultFromServer=resultFromServer;
    }
    public ISO210 getResultFromServer(){
        return this.ResultFromServer;
    }

}
