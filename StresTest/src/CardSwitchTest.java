import Mainchannel.Exceptions.InvalidParameterException;
import Mainchannel.Exceptions.ResponseParsingException;
import Mainchannel.sender.SenderException;
import ServiceObjects.Account.AccountInformation;
import ServiceObjects.Account.BalanceForAccount;
import ServiceObjects.Account.CardNoOfAccount;
import ServiceObjects.Account.Transaction;
import ServiceObjects.Pan.BalanceForCard;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by root on 4/14/16.
 */
public class CardSwitchTest {


    String parameters[];
    int threadCount;
    Object counter;
    int timeDelay;
    int threadID=1;
    public CardSwitchTest(String Params[]) throws InterruptedException, SQLException, IOException {
        parameters=Params;
        counter=(Object)0;

        threadCount=Integer.valueOf(parameters[2]);
        timeDelay=Integer.valueOf(parameters[3]);
        if (parameters[4].equals("2"))   initializeThreadWithOutLoop();
        else  if (parameters[4].equals("3"))initializeThreadsWithSleepinThread();


    }
    private void initializeThreadsWithSleepinThread(){
        while (threadID<=threadCount){
            runTestWithSleep(threadID,parameters);
            threadID++;
        }
    }
    private void initializeThreadWithOutLoop() throws InterruptedException, IOException, SQLException {
        while (true){
            startTestLoop();
            Thread.sleep(timeDelay);
        }



    }
    private void runTestWithSleep(final int threadNO,final String Par[]){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future future = executorService.submit(new Runnable() {
            public void run() {
                try {
                    new testCaseWithSleep(threadNO,Par);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void runTestWithOutSleep(final int threadNO,final String Par[]){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future future = executorService.submit(new Runnable() {
            public void run() {
                try {
                    new testCaseWithOutSleep(threadNO,Par);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
    private void startTestLoop() throws IOException, SQLException, InterruptedException {
        int Counter=1;
        while(Counter<=threadCount){
            runTestWithOutSleep(Counter,parameters);
            Thread.sleep(10);
            Counter++;
        }
    }

    private class testCaseWithSleep{
        String parameters[];
        int threadID=1;
        String ipOfServer;
        String portOfServer;
        int timeDelay;
        int internalLoop;
        int tempCounter;
        public testCaseWithSleep(int threadNO,String Params[]) throws InterruptedException, SQLException, IOException {
            parameters=Params;
            ipOfServer=(parameters[0]);
            portOfServer=(parameters[1]);
            threadID=threadNO;
            timeDelay=Integer.valueOf(parameters[3]);
            startTestLoop();
        }
        private void startTestLoop() throws IOException, SQLException, InterruptedException {
            while(true){
                balanceForAccountan();
                increaseCounter();
                Thread.sleep(timeDelay);
            }
        }
        private void increaseCounter(){
            synchronized (counter){
                tempCounter=(int)counter;
                internalLoop++;
                tempCounter++;
                counter=(Object)tempCounter;
            }
        }
        public  void balanceForAccountan() throws IOException,RemoteException, SQLException {
            BalanceForCard balanceForCard=new BalanceForCard();
            balanceForCard.setPan("5859831000001247");
            balanceForCard.setPin("123456");

            balanceForCard=(BalanceForCard) submitRequestToGateway(balanceForCard);
            printToScreen("Counter is:"+String.valueOf(counter)+
                    " :in balanceForPan Action Code is:"+balanceForCard.getActionCode()+
                    " :balance is :"+balanceForCard.getResultFromServer().getActualBalance());

        }

        public  void printToScreen(String S) {
            System.out.println("Time :"+GetNowTimeWithSeparator()+":ThreadID:"+threadID+" "+S);

        }
        public  Object submitRequestToGateway(Object object) throws IOException {
            Bank bb=null;
            try {
                bb=(Bank) Naming.lookup("rmi://"+ipOfServer+":"+portOfServer+"/Gateway");
                object=bb.submitRequest(object);
                bb=null;
                System.gc();
            } catch (NotBoundException e) {
                e.printStackTrace();
                printToScreen("1:" + e.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
                printToScreen("2:" + e.toString());
            } catch (RemoteException e) {
                e.printStackTrace();
                printToScreen("3:" + e.toString());
            } catch (InvalidParameterException e) {
                e.printStackTrace();
                printToScreen("4:" + e.toString());
            } catch (ResponseParsingException e) {
                e.printStackTrace();
                printToScreen("5:" + e.toString());
            } catch (SenderException e) {
                e.printStackTrace();
                printToScreen("6:" + e.toString());
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ServerNotActiveException e) {
                e.printStackTrace();
            }

            return object;
        }
        public  String  GetNowTimeWithSeparator(){

            Date Time=new Date();
            SimpleDateFormat DateFormat =
                    //new SimpleDateFormat ("hh:mm:ss a");
                    new SimpleDateFormat ("HH:mm:ss.SSS");
            String Now=DateFormat.format(Time);
            return Now;

        }

    }
    private class testCaseWithOutSleep{
        String parameters[];
        int threadID=1;
        String ipOfServer;
        String  portOfServer;
        int timeDelay;
        int internalLoop;
        int tempCounter;
        public testCaseWithOutSleep(int threadNO,String Params[]) throws InterruptedException, SQLException, IOException {
            parameters=Params;
            ipOfServer=(parameters[0]);
            portOfServer=(parameters[1]);
            threadID=threadNO;
            timeDelay=Integer.valueOf(parameters[3]);
            startTestLoop();
        }
        private void startTestLoop() throws IOException, SQLException, InterruptedException {

            balanceForPan();
            increaseCounter();

        }
        private void increaseCounter(){
            synchronized (counter){
                tempCounter=(int)counter;
                internalLoop++;
                tempCounter++;
                counter=(Object)tempCounter;
            }
        }
        public  void balanceForPan() throws IOException,RemoteException, SQLException {
            BalanceForCard balanceForCard=new BalanceForCard();
            balanceForCard.setPan("5859831000001247");
            balanceForCard.setPin("123456");

            balanceForCard=(BalanceForCard) submitRequestToGateway(balanceForCard);
            printToScreen("Counter is:"+String.valueOf(counter)+
                    " :in balanceForPan Action Code is:"+balanceForCard.getActionCode()+
                    " :balance is :"+balanceForCard.getResultFromServer().getActualBalance());

        }

        public  void printToScreen(String S) {
            System.out.println("Time :"+GetNowTimeWithSeparator()+":ThreadID:"+threadID+" "+S);

        }
        public  Object submitRequestToGateway(Object object) throws IOException {
            Bank bb=null;
            try {
                bb=(Bank) Naming.lookup("rmi://"+ipOfServer+":"+portOfServer+"/Gateway");
                object=bb.submitRequest(object);
                bb=null;
                System.gc();
            } catch (NotBoundException e) {
                e.printStackTrace();
                printToScreen("1:" + e.toString());
            } catch (MalformedURLException e) {
                e.printStackTrace();
                printToScreen("2:" + e.toString());
            } catch (RemoteException e) {
                e.printStackTrace();
                printToScreen("3:" + e.toString());
            } catch (InvalidParameterException e) {
                e.printStackTrace();
                printToScreen("4:" + e.toString());
            } catch (ResponseParsingException e) {
                e.printStackTrace();
                printToScreen("5:" + e.toString());
            } catch (SenderException e) {
                e.printStackTrace();
                printToScreen("6:" + e.toString());
            } catch (SQLException e) {
                e.printStackTrace();
            } catch (ServerNotActiveException e) {
                e.printStackTrace();
            }

            return object;
        }
        public  String  GetNowTimeWithSeparator(){

            Date Time=new Date();
            SimpleDateFormat DateFormat =
                    //new SimpleDateFormat ("hh:mm:ss a");
                    new SimpleDateFormat ("HH:mm:ss.SSS");
            String Now=DateFormat.format(Time);
            return Now;

        }

    }


}
