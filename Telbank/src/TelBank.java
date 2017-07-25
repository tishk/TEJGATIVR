
import org.asteriskjava.fastagi.AgiChannel;
import org.asteriskjava.fastagi.AgiRequest;
import org.asteriskjava.fastagi.BaseAgiScript;

import java.io.IOException;

public class TelBank extends BaseAgiScript
{
  public static final Util UtilOfTelBank=new Util();
    boolean hangedUpShown=false;
  @Override
  public  void    service(AgiRequest request, AgiChannel channel){
        int i=0;
        Start start= null;
        try {
           //  request.getParameter("test");
             //exec("");
            //   new showStatus();
            start = new Start();
            try {
                Util.printMessage("call hanged up for caller id : " + request.getCallerIdNumber().toString(), false);
            } catch (IOException e1) {
                e1.printStackTrace();
            }


        } catch (Exception e) {



        }
      start=null;
        System.gc();
    }

}