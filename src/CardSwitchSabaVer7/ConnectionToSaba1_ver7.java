package CardSwitchSabaVer7;

import utils.PropertiesUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class ConnectionToSaba1_ver7

{
    public static boolean  Started=false;
    public boolean  Connected=false;
    public ConnectionToSaba1_ver7() throws InterruptedException {
       ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future future = executorService.submit(new Runnable() {
            public void run() {
                try {
                    StartCheckConnectivitySabaSwitch();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });

   }
    public void StartCheckConnectivitySabaSwitch() throws InterruptedException {
        try {
            if (socket == null) {
              //   String  t= PropertiesUtils.getSabaHost();
              //   String  t1=PropertiesUtils.getSabaPort();
                socket = new Socket("10.40.56.4", 11355);
                System.out.println("Connected To Saba !!!!!!!");
            }

        } catch (IOException e) {
            System.out.println("not Connected To Saba : " + e.toString());
            e.printStackTrace();
        }

    }

    public void reStartCheckConnectivitySabaSwitch() throws InterruptedException {

        try {
            socket.close();
            socket=null;
            StartCheckConnectivitySabaSwitch();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    public void ConnectToSaba(){
       try {
           socket=new Socket(PropertiesUtils.getSabaHost(),Integer.valueOf(PropertiesUtils.getSabaPort()));
           Started=true;

       } catch (IOException e) {
           Started=false;
           //utils.

       }

   }
    public static Socket socket=null;
    public static synchronized String  GetNowTimeWithSeparator(){

        Date Time=new Date();
        SimpleDateFormat DateFormat =
                //new SimpleDateFormat ("hh:mm:ss a");
                new SimpleDateFormat ("HH:mm:ss.SSS");
        String Now=DateFormat.format(Time);
        return Now;

    }
    public static synchronized boolean sendOnSocket(String ISOMessage) {
    try {
       // synchronized (ConnectionToSaba1_ver7.socket){
           try{
               PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
               out.print(ISOMessage);
               System.out.println("Sent Message in :" +GetNowTimeWithSeparator());
               System.out.println("Sent Message is :" +ISOMessage);
               out.flush();
               out=null;

           }catch (Exception e){
               System.out.println("ISOMessage = [" + ISOMessage + "]");
           }

            Thread.sleep(100);
            return true;
        //}


    } catch (InterruptedException e) {
        return false;
    }
}

}
