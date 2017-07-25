package CardSwitchSaba.Old;

import CardSwitchSaba.SendToSabaSwitch;
import ServiceObjects.ISO.ISO210;
import ServiceObjects.ISO.ISO430;
import ServiceObjects.Other.LoggerToDB;
import com.neda.DateUtil;
import utils.PropertiesUtils;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Administrator on 1/4/2016.
 */
public class ReceiveFromSaba3 {

    public ReceiveFromSaba3(){
        boolean ResultOfCreateReceiveFromSabaRunning=false;

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future future = executorService.submit(new Runnable() {
            public void run() {
                try {
                    initializeAndManageSocket();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

    }

    ExecutorService runClient;
    ServerSocket serverSocket;
    Socket connection;
    private void initializeAndManageSocket() {
        runClient = Executors.newFixedThreadPool(1);
        int clientNumber=0;
        try {
            serverSocket = new ServerSocket(Integer.valueOf(PropertiesUtils.getThisPortForSaba()));
            System.out.println("Started Saba Connection...");

//            FileLogger.getInstance(com.neda.util.logger.FileLogger.LoggingType.OperationHistoryLog).log("Awaiting connection from clients...\n");
            do {
                WorkingThread wt = new WorkingThread(serverSocket.accept(), ++clientNumber);
                runClient.execute(wt);
            } while (true);
        } catch (IOException e) {
            //FileLogger.getInstance(com.neda.util.logger.FileLogger.LoggingType.AVACAS).log(e);
            e.printStackTrace();
            return;
        }
    }

    public class WorkingThread implements Runnable {
        BufferedInputStream input;

        public void run() {
            String accno = "";
            String DateTime = "";
            DateTime = DateUtil.msgDateTime();
            String MsgId = "";
            String MsgType = "";
            int ResCode = 0;

            try {
                input = new BufferedInputStream(connection.getInputStream());

            } catch (IOException e) {
                e.printStackTrace();
            }
            try {

                byte tmpreceivedBytes[] = new byte[1000];

                input.read(tmpreceivedBytes);
                //input.close();
                //tmpreceivedBytes=null;
                String St = new String(tmpreceivedBytes);
                //System.out.println(St);
                processMessage(St);
                //connection.close();
            } catch (Exception e) {
                //FileLogger.getInstance(com.neda.util.logger.FileLogger.LoggingType.AVACAS).log((new StringBuilder()).append("Error in 'write' ").append(e).toString());
                e.printStackTrace();
            }
        }

        private Socket connection;
        public  String  GetNowTimeWithSeparator(){

            Date Time=new Date();
            SimpleDateFormat DateFormat =
                    //new SimpleDateFormat ("hh:mm:ss a");
                    new SimpleDateFormat ("HH:mm:ss.SSS");
            String Now=DateFormat.format(Time);
            return Now;

        }
        public int getKindOfMessage(final String msg){

            try {
                if (msg.substring(4,8).equals("0210")) return  0;
                if (msg.substring(4,8).equals("0430")) return  1;
                else return  -1;
            } catch (Exception e) {
                return -1;
            }
        }
        private void processMessage(final String RCVMessage){
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            Future future = executorService.submit(new Runnable() {
                public void run() {
                    int kind = getKindOfMessage(RCVMessage);
                    if (kind == 0)  logISO210(RCVMessage);
                    else if (kind == 1) logISO430(RCVMessage);
                    else ;
                }
            });

        }
        private void logISO210(final String messageString ){

            final ExecutorService executorService = Executors.newSingleThreadExecutor();
            Future future = executorService.submit(new Runnable() {
                public void run() {
                    LoggerToDB loggerToDB =null;
                    ISO210 iso210=null;
                    try {
                        iso210=new ISO210();
                        iso210.ProcessReceiveString(messageString);
                        if (iso210.getResponseTransactionCode().equals("80") ||
                                iso210.getResponseTransactionCode().equals("84") ||
                                iso210.getResponseTransactionCode().equals("91") ||
                                iso210.getResponseTransactionCode().equals("34") ){

                            SendToSabaSwitch c=new SendToSabaSwitch(iso210);


                        }
                        // System.out.println("MESSAGE Recieved  is :  " +iso210.getReceivedString());
                        System.out.println("MESSAGE Recieved  Result is :  " +iso210.getResponseTransactionCode()+" Time is:"+GetNowTimeWithSeparator());
                        System.out.println("Pan is :  " +iso210.getPan());
                        System.out.println("Action Code is :  " +iso210.getActionCode());

                        loggerToDB = new LoggerToDB(iso210);

                    } catch (Exception e) {
                        loggerToDB = null;
                        iso210=null;
                        e.printStackTrace();
                        executorService.shutdown();
                    }finally {
                        loggerToDB = null;
                        iso210=null;
                        executorService.shutdown();

                    }
                }
            });
        }
        private void logISO430(final String messageString ){
            ExecutorService executorService = Executors.newSingleThreadExecutor();
            Future future = executorService.submit(new Runnable() {
                public void run() {
                    ISO430 iso430=null;
                    LoggerToDB loggerToDB =null;
                    try {
                        iso430=new ISO430();
                        iso430.ProcessReceiveString(messageString);
                        loggerToDB = new LoggerToDB(iso430);
                        loggerToDB = null;
                    } catch (Exception e) {
                        iso430=null;
                        loggerToDB=null;
                        e.printStackTrace();
                    }finally {
                        iso430=null;
                        loggerToDB=null;
                    }
                }
            });
        }
        public WorkingThread(Socket connection, int number) {
            super();
            this.connection = connection;

        }
    }
}
