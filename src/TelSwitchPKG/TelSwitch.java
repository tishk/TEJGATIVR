package TelSwitchPKG;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Administrator on 28/05/2015.
 */
public class TelSwitch {

    public TelSwitch() throws ExecutionException, InterruptedException {
        initialize();
    }
    private ReceiveFromTelSwitch receiveFromTelSwitch=null;
    private ConnectionToTelSwitch connectionToTelSwitch =null;
    public boolean ListenerIsRunning=false;
    public boolean connectionToTelSwitchIsRunning=false;

    private  void initialize() throws ExecutionException, InterruptedException {
        if (createdListenerForTelSwitch()) ListenerIsRunning=true;else ListenerIsRunning=false;
        if (createdConnectionTelSwitch()) connectionToTelSwitchIsRunning=true;else connectionToTelSwitchIsRunning=false;
    }
    private  boolean createdListenerForTelSwitch() throws ExecutionException, InterruptedException {
        boolean ResOfCreateReceiverTelSwitch=false;

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future future = executorService.submit(new Runnable() {
           public void run() {
                try {
                    receiveFromTelSwitch=new ReceiveFromTelSwitch();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
       });

        if  (future.get()!= null) ResOfCreateReceiverTelSwitch=false; else ResOfCreateReceiverTelSwitch=true;
        return ResOfCreateReceiverTelSwitch ;
    }
    private  boolean createdConnectionTelSwitch() throws ExecutionException, InterruptedException {
        boolean ResultOfCreateReceiveFromSabaRunning=false;

       ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future future = executorService.submit(new Runnable() {
            public void run() {
                try {
                    connectionToTelSwitch =new ConnectionToTelSwitch();
                } catch (Exception e) {
                    e.printStackTrace();
                }
           }
        });

        if  (future.get()!= null) ResultOfCreateReceiveFromSabaRunning=false; else ResultOfCreateReceiveFromSabaRunning=true;
        return ResultOfCreateReceiveFromSabaRunning ;
    }
}
