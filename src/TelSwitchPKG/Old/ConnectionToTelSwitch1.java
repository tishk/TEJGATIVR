package TelSwitchPKG.Old;

import utils.PropertiesUtils;

import java.io.IOException;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


public class ConnectionToTelSwitch1

{
    public static boolean  Started=false;
    public boolean  Connected=false;
    public ConnectionToTelSwitch1() throws InterruptedException {
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future future = executorService.submit(new Runnable() {
            public void run() {
                try {
                    StartCheckConnectivityTelSwitch();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });


   }
    public void StartCheckConnectivityTelSwitch() throws InterruptedException {
        try {
            if (socket==null) ConnectToTelSwitch();

        } catch (Exception e) {
            e.printStackTrace();
        }
        while (true){
            if (!socket.isConnected()||socket==null) ConnectToTelSwitch();
            Connected=socket.isConnected();
            //System.out.println("Connected To TelSwitch");
            Thread.sleep(3000);
        }
    }
    public void ConnectToTelSwitch(){
       try {
           String s=PropertiesUtils.getTelSwitchHost();
           socket=new Socket(PropertiesUtils.getTelSwitchHost(),Integer.valueOf(PropertiesUtils.getTelSwitchPort()));
           Started=true;
           System.out.println("Connected To TelSwitch");
       } catch (IOException e) {
           System.out.println("notConnected To TelSwitch");
           Started=false;
       }

   }
    public static Socket socket=null;
}
