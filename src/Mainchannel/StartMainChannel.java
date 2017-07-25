package Mainchannel;

/**
 * Created by Administrator on 05/06/2015.
 */
public class StartMainChannel {
    public boolean started=false;
    public StartMainChannel(){
        MainMQ AccountServices=new MainMQ();
        try {
            AccountServices.Init();
            started=true;

        } catch (Exception e) {
            started=false;
            e.printStackTrace();
        }
    }
}
