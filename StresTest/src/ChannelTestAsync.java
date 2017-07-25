import Mainchannel.Exceptions.InvalidParameterException;
import Mainchannel.Exceptions.ResponseParsingException;
import Mainchannel.sender.SenderException;
import ServiceObjects.Account.AccountInformation;
import ServiceObjects.Account.BalanceForAccount;
import ServiceObjects.Account.CardNoOfAccount;
import ServiceObjects.Account.Transaction;

import java.io.IOException;
import java.net.MalformedURLException;
import java.rmi.Naming;
import java.rmi.NotBoundException;
import java.rmi.RemoteException;
import java.rmi.server.ServerNotActiveException;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by root on 4/13/16.
 */
public class ChannelTestAsync {
    String parameters[];
    int threadCount;
    Object counter;
    int timeDelay;
    int threadID=1;
    ArrayList<String> accounts;
    public ChannelTestAsync(String Params[],ArrayList<String> acc) throws InterruptedException, SQLException, IOException {
     accounts=acc;
        parameters=Params;
        counter=(Object)0;

        threadCount=Integer.valueOf(parameters[2]);
     timeDelay=Integer.valueOf(parameters[3]);
     if (parameters[4].equals("0"))   initializeThreadWithOutLoop();
        else initializeThreadsWithSleepinThread();


    }
    private void initializeThreadsWithSleepinThread(){
        while (threadID<=threadCount){
            runTestWithSleep(threadID,parameters,accounts);
            threadID++;
        }
    }
    private void initializeThreadWithOutLoop() throws InterruptedException, IOException, SQLException {
       while (true){
           startTestLoop();
           Thread.sleep(timeDelay);
       }



    }
    private void runTestWithSleep(final int threadNO,final String Par[],final ArrayList<String> accList){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future future = executorService.submit(new Runnable() {
            public void run() {
                try {
                   new testCaseWithSleep(threadNO,Par,accList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    private void runTestWithOutSleep(final int threadNO,final String Par[],final ArrayList<String> accList){
        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future future = executorService.submit(new Runnable() {
            public void run() {
                try {
                    new testCaseWithOutSleep(threadNO,Par,accList);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }
    private void startTestLoop() throws IOException, SQLException, InterruptedException {
        int Counter=1;
        while(Counter<=threadCount){
            runTestWithOutSleep(Counter,parameters,accounts);
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
        int accountSelectCounter;
        ArrayList<String> accountList;
        public testCaseWithSleep(int threadNO,String Params[],ArrayList<String> accounts) throws InterruptedException, SQLException, IOException {
            accountList=accounts;
            parameters=Params;
            ipOfServer=(parameters[0]);
            portOfServer=(parameters[1]);
            threadID=threadNO;
            timeDelay=Integer.valueOf(parameters[3]);
            accountSelectCounter=threadNO;
            startTestLoop();
        }
        private void startTestLoop() throws IOException, SQLException, InterruptedException {
            while(true){
                if ((threadID+internalLoop)%2==0) accountBalance();
                else{
                    if ((threadID+internalLoop)%3==0) accountAccountInformation();
                    else if ((threadID+internalLoop)%5==0) accountStatementList();
                    else accountGetPan();
                }
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

        private String getAnAccount(){
            accountSelectCounter++;
            if (accountSelectCounter>100) accountSelectCounter=1;
            return accounts.get(accountSelectCounter);
        }

        public  void accountBalance() throws IOException,RemoteException, SQLException {
            BalanceForAccount balanceForAccount=new BalanceForAccount();
            balanceForAccount.setAccountNumber(getAnAccount());

            balanceForAccount=(BalanceForAccount) submitRequestToGateway(balanceForAccount);
            printToScreen("Counter is:"+String.valueOf(counter)+
                    " :in balanceForAccount Action Code is:"+balanceForAccount.getResultFromChannel().getAction_code()+
                    " :balance is :"+balanceForAccount.getResultFromChannel().getActualBalance());
            // String resultHint="Counter is:"+String.valueOf(counter)+":in balance Action Code is:"+balanceForAccount.getResultFromCM().getAction_code();
            //loggerToFile.getInstance().logInfo(resultHint);

        }
        public  void accountAccountInformation() throws IOException ,RemoteException, SQLException {
            AccountInformation accountInformation=new AccountInformation();
            accountInformation.setAccountNumber(getAnAccount());

            accountInformation=(AccountInformation) submitRequestToGateway(accountInformation);
            printToScreen("Counter is:"+String.valueOf(counter)+
                    " :in accountInformation Action Code is:"+accountInformation.getResultFromChannel().getAction_code()+
                    " :Person Type is:"+accountInformation.getResultFromChannel().getPrsnType());

            // String resultHint="Counter is:"+String.valueOf(counter)+":in accountInformation Action Code is:"+accountInformation.getResultFromCM().getAction_code();
            //loggerToFile.getInstance().logInfo(resultHint);

        }
        public  void accountStatementList() throws IOException ,RemoteException, SQLException {
            Transaction transaction=new Transaction();
            transaction.setAccountNumber(getAnAccount());
            transaction.setStatementType("8");
            transaction.setTransactionCount("3");

            transaction=(Transaction) submitRequestToGateway(transaction);
            printToScreen("Counter is:"+String.valueOf(counter)+
                    " :in StatementList Action Code is:"+transaction.getResultFromCM().getAction_code()+
                    " :Balance of 2th trasaction is:"+transaction.getResultFromCM().getStatementMessage(1).getAmount());

            // String resultHint="Counter is:"+String.valueOf(counter)+" :in StatementList Action Code is:"+transaction.getResultFromCM().getAction_code();
            //loggerToFile.getInstance().logInfo(resultHint);
        }
        public  void accountGetPan() throws IOException ,RemoteException, SQLException {
            CardNoOfAccount cardNoOfAccount=new CardNoOfAccount();
            cardNoOfAccount.setAccountNumber(getAnAccount());
            cardNoOfAccount=(CardNoOfAccount)submitRequestToGateway(cardNoOfAccount);
            printToScreen("Counter is:"+String.valueOf(counter)+
                    " :in getPan Action Code is:"+cardNoOfAccount.getResultFromChannel().getAction_code()+
                    " :pan of Account is:"+cardNoOfAccount.getResultFromChannel().getPan());
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
        int accountSelectCounter;
        ArrayList<String> accountList;
        public testCaseWithOutSleep(int threadNO,String Params[],final ArrayList<String> accList) throws InterruptedException, SQLException, IOException {
            accountList=accList;
            accountSelectCounter=threadNO;
            parameters=Params;
            ipOfServer=(parameters[0]);
            portOfServer=(parameters[1]);
            threadID=threadNO;
            timeDelay=Integer.valueOf(parameters[3]);
            startTestLoop();
        }
        private String getAnAccount(){
            accountSelectCounter++;
            if (accountSelectCounter>100) accountSelectCounter=1;
            return accounts.get(accountSelectCounter);
        }

        private void startTestLoop() throws IOException, SQLException, InterruptedException {

                if ((threadID+internalLoop)%2==0) balanceForAccount();
                else{
                    if ((threadID+internalLoop)%3==0) accountInformation();
                    else if ((threadID+internalLoop)%5==0) accountStatementList();
                    else accountgetPan();
                }
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
        public  void balanceForAccount() throws IOException,RemoteException, SQLException {
            BalanceForAccount balanceForAccount=new BalanceForAccount();
            balanceForAccount.setAccountNumber(getAnAccount());

            balanceForAccount=(BalanceForAccount) submitRequestToGateway(balanceForAccount);
            printToScreen("Counter is:"+String.valueOf(counter)+
                    " :in balanceForAccount Action Code is:"+balanceForAccount.getResultFromChannel().getAction_code()+
                    " :balance is :"+balanceForAccount.getResultFromChannel().getActualBalance());
            // String resultHint="Counter is:"+String.valueOf(counter)+":in balance Action Code is:"+balanceForAccount.getResultFromCM().getAction_code();
            //loggerToFile.getInstance().logInfo(resultHint);

        }
        public  void accountInformation() throws IOException ,RemoteException, SQLException {
            AccountInformation accountInformation=new AccountInformation();
            accountInformation.setAccountNumber(getAnAccount());

            accountInformation=(AccountInformation) submitRequestToGateway(accountInformation);
            printToScreen("Counter is:"+String.valueOf(counter)+
                    " :in accountInformation Action Code is:"+accountInformation.getResultFromChannel().getAction_code()+
                    " :Person Type is:"+accountInformation.getResultFromChannel().getPrsnType());

            // String resultHint="Counter is:"+String.valueOf(counter)+":in accountInformation Action Code is:"+accountInformation.getResultFromCM().getAction_code();
            //loggerToFile.getInstance().logInfo(resultHint);

        }
        public  void accountStatementList() throws IOException ,RemoteException, SQLException {
            Transaction transaction=new Transaction();
            transaction.setAccountNumber(getAnAccount());
            transaction.setStatementType("8");
            transaction.setTransactionCount("3");

            transaction=(Transaction) submitRequestToGateway(transaction);
            printToScreen("Counter is:"+String.valueOf(counter)+
                    " :in StatementList Action Code is:"+transaction.getResultFromCM().getAction_code()+
                    " :Balance of 2th trasaction is:"+transaction.getResultFromCM().getStatementMessage(1).getAmount());

            // String resultHint="Counter is:"+String.valueOf(counter)+" :in StatementList Action Code is:"+transaction.getResultFromCM().getAction_code();
            //loggerToFile.getInstance().logInfo(resultHint);
        }
        public  void accountgetPan() throws IOException ,RemoteException, SQLException {
            CardNoOfAccount cardNoOfAccount=new CardNoOfAccount();
            cardNoOfAccount.setAccountNumber(getAnAccount());
            cardNoOfAccount=(CardNoOfAccount)submitRequestToGateway(cardNoOfAccount);
            printToScreen("Counter is:"+String.valueOf(counter)+
                    " :in getPan Action Code is:"+cardNoOfAccount.getResultFromChannel().getAction_code()+
                    " :pan of Account is:"+cardNoOfAccount.getResultFromChannel().getPan());
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
