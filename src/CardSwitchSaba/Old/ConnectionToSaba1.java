package CardSwitchSaba.Old;

import utils.PropertiesUtils;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class ConnectionToSaba1

{
    public static boolean  Started=false;
    public boolean  Connected=false;
    public ConnectionToSaba1() throws InterruptedException {
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
                 String  t= PropertiesUtils.getSabaHost();
                 String  t1=PropertiesUtils.getSabaPort();
                socket = new Socket(PropertiesUtils.getSabaHost(), Integer.valueOf(PropertiesUtils.getSabaPort()));
                System.out.println("Connected To Saba !!!!!!!");
            }

        } catch (IOException e) {
            System.out.println("not Connected To Saba : " + e.toString());
            e.printStackTrace();
        }
        //Thread.sleep(2000);
        while (true) {

            if (!socket.isConnected()) {
                ConnectToSaba();
                System.out.println("Connected To Saba");
                Connected = socket.isConnected();
                Thread.sleep(5000);
            }
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
        synchronized (ConnectionToSaba1.socket){
            PrintWriter out = new PrintWriter(ConnectionToSaba1.socket.getOutputStream(), true);
            out.print(ISOMessage);
            System.out.println("Sent Message in :" +GetNowTimeWithSeparator());
            out.flush();
            out=null;
            Thread.sleep(100);
            return true;
        }


    } catch (IOException ioe) {
        return false;
    } catch (InterruptedException e) {
        return false;
    }
}

}
