package CardSwitchSaba;

import ResponseServices.GatewayServices;
import ServiceObjects.Pan.BalanceForCard;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Administrator on 10/18/2015.
 */
public class CardSwitch {
    final GatewayServices gatewayServices=new GatewayServices();
    BalanceForCard balanceForCard=new BalanceForCard();
    public CardSwitch() throws ExecutionException, InterruptedException {
        initialize();
        checkSabaConnection();

    }
    private ReceiveFromSaba receiveFromSaba=null;
    private ConnectionToSaba connectionToSaba =null;
    public boolean ListenerIsRunning=false;
    public boolean connectionToSabaIsRunning=false;

    public   void initialize() throws ExecutionException, InterruptedException {
        if (createdListenerForSaba()) ListenerIsRunning=true;else ListenerIsRunning=false;
        if (createdConnectionToSaba()) connectionToSabaIsRunning=true;else connectionToSabaIsRunning=false;
    }
    public   void restartConnections() throws ExecutionException, InterruptedException {
      //  receiveFromSaba.reStartReceiver();
       // connectionToSaba.reStartCheckConnectivitySabaSwitch();
    }
    private  boolean createdListenerForSaba() throws ExecutionException, InterruptedException {
        boolean ResultOfCreateReceiveFromSabaRunning=false;

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future future = executorService.submit(new Runnable() {
            public void run() {
                try {
                     receiveFromSaba=new ReceiveFromSaba();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        if  (future.get()!= null) ResultOfCreateReceiveFromSabaRunning=false; else ResultOfCreateReceiveFromSabaRunning=true;
        //return ResultOfCreateReceiveFromSabaRunning && receiveFromSaba.ResultOfRunning;
        return ResultOfCreateReceiveFromSabaRunning ;
    }
    private  boolean createdConnectionToSaba() throws ExecutionException, InterruptedException {
        boolean ResultOfCreateReceiveFromSabaRunning=false;

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future future = executorService.submit(new Runnable() {
            public void run() {
                try {
                    connectionToSaba =new ConnectionToSaba();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        if  (future.get()!= null) ResultOfCreateReceiveFromSabaRunning=false; else ResultOfCreateReceiveFromSabaRunning=true;
        return ResultOfCreateReceiveFromSabaRunning ;
    }
    private  boolean checkSabaConnection() throws ExecutionException, InterruptedException {
        boolean ResultOfCreateReceiveFromSabaRunning=false;
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future future = executorService.submit(new Runnable() {
            public void run() {
                sendTestRequest();
            }
        });
        return true;
    }
    private  boolean sendTestRequest(){
        try {
            while (true){

                Thread.sleep(1000*60*30);
                //Thread.sleep(1000*1*1);
                balanceForCard=new BalanceForCard();
                balanceForCard.setPan("5859830000000000");
                balanceForCard.setPin("123456");
                balanceForCard=gatewayServices.cardBalance(balanceForCard);
                if (balanceForCard.getActionCode().equals("")){
                    System.out.println("is null");
                }
                System.out.println("actionCode:" + balanceForCard.getActionCode());
                if (!balanceForCard.getActionCode().equals("5014"))  restartConnections();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }
}
