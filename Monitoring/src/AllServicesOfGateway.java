import java.sql.SQLException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by root on 4/28/16.
 */
public class AllServicesOfGateway extends Status {
    String uniqueID=null;

    String requestParameters[]=null;

    String tempResponse="";
    Gateway gateway=null;
    Channel channel=null;
    CardSwitch cardSwitch=null;
    TelSwitch telSwitch=null;
    Authenticate authenticate=null;

    public AllServicesOfGateway(String[] request) throws SQLException, ClassNotFoundException {
       this.requestParameters=request;
       this.process();

    }

    private void process() throws SQLException, ClassNotFoundException {

        getGatewayStatus();
        getChannelStatus();
        getCardSwitchStatus();
        getTelSwitchStatus();
        getAuthenticateStatus();
        freeVariables();

    }
    private void getGatewayStatus(){
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future future = executorService.submit(new Runnable() {
            public void run() {
                try {

                   gateway= new Gateway(requestParameters);
                    new DBUtils(gateway);
                } catch (Exception e) {
                    e.printStackTrace();
                   // executorService.shutdownNow();
                }finally {
                  //  executorService.shutdownNow();
                }
            }
        });
    }
    private void getChannelStatus(){
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future future = executorService.submit(new Runnable() {
            public void run() {
                try {
                   Channel channel=new Channel(requestParameters);
                    new DBUtils(channel);
                } catch (Exception e) {
                    e.printStackTrace();
                  //  executorService.shutdownNow();
                }finally {
                  //  executorService.shutdownNow();
                }
            }
        });
    }
    private void getCardSwitchStatus(){
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future future = executorService.submit(new Runnable() {
            public void run() {
                try {
                    CardSwitch cardSwitch =new CardSwitch(requestParameters);
                    new DBUtils(cardSwitch);
                } catch (Exception e) {
                    e.printStackTrace();
                    executorService.shutdownNow();
                }finally {
                    executorService.shutdownNow();
                }
            }
        });
    }
    private void getTelSwitchStatus(){
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future future = executorService.submit(new Runnable() {
            public void run() {
                try {
                    TelSwitch telSwitch=new TelSwitch(requestParameters);
                    new DBUtils(telSwitch);
                } catch (Exception e) {
                    e.printStackTrace();
                    executorService.shutdownNow();
                }finally {
                    executorService.shutdownNow();
                }
            }
        });
    }
    private void getAuthenticateStatus(){
        final ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future future = executorService.submit(new Runnable() {
            public void run() {
                try {
                    Authenticate authenticate=new Authenticate(requestParameters);
                    new DBUtils(authenticate);

                } catch (Exception e) {
                    e.printStackTrace();
                    executorService.shutdownNow();
                }finally {
                    executorService.shutdownNow();
                }
            }
        });
    }

    private void freeVariables(){
        gateway=null;
        channel=null;
        cardSwitch=null;
        telSwitch=null;
        authenticate=null;
        System.gc();
    }

}
