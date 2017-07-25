package CardSwitchSaba.Old;

import CardSwitchSaba.SendToSabaSwitch;
import ServiceObjects.ISO.ISO210;
import ServiceObjects.ISO.ISO430;
import ServiceObjects.Other.LoggerToDB;
import utils.PropertiesUtils;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * Created by Administrator on 1/4/2016.
 */
public class ReceiveFromSaba4 {

    public ReceiveFromSaba4(){
        runServer();
    }
    public class MultiThreadedServer{

        protected int          serverPort   ;
        protected AsynchronousServerSocketChannel serverSocket = null;
        protected boolean      isStopped    = false;
        protected Thread       runningThread= null;
        public  boolean ResultOfRunning=false;

        public MultiThreadedServer(){
            this.serverPort = 1111;
            run();
        }

        public void run(){
            synchronized(this){
                this.runningThread = Thread.currentThread();
            }
            if (openedServerSocket()){
                Future acceptResult =null;
                while(! isStopped()){
                    try {
                        acceptResult=serverSocket.accept();
                    } finally {
                        try {
                           // new Thread(new WorkerRunnable(acceptResult)).start();
                            new WorkerRunnable(acceptResult);
                        } catch (ExecutionException e) {
                            e.printStackTrace();
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        } catch (Exception e){
                            e.printStackTrace();
                        }
                    }

                }
            }
            System.out.println("Saba Server Stopped") ;
        }


        private synchronized boolean isStopped() {
            return this.isStopped;
        }

        public synchronized void stop(){
            this.isStopped = true;
            try {
                this.serverSocket.close();
            } catch (IOException e) {
                throw new RuntimeException("Error closing server", e);
            }
        }

        private boolean openedServerSocket() {
            try {
                AsynchronousServerSocketChannel serverChannel = AsynchronousServerSocketChannel.open();
                InetSocketAddress hostAddress = new InetSocketAddress(PropertiesUtils.getThisHostForSaba(),
                                                                      Integer.valueOf(PropertiesUtils.getThisPortForSaba()));
                serverChannel.bind(hostAddress);
               // this.serverSocket = new ServerSocket(this.serverPort);
                return true;
            } catch (IOException e) {
                return false;
                // throw new RuntimeException("Cannot open port", e);
            }
        }

    }
    public class WorkerRunnable implements Runnable{

        protected Socket socket = null;
        protected AsynchronousSocketChannel clientChannel =null;
        public WorkerRunnable(Future acceptResult) throws ExecutionException, InterruptedException {
            AsynchronousSocketChannel tempClientChannel = (AsynchronousSocketChannel) acceptResult.get();
            this.clientChannel = tempClientChannel;
        }

        public void run() {
            byte[] messageByte = new byte[1000];
            ByteBuffer buffer = ByteBuffer.allocate(500);
            DataInputStream in = null;
            int LenOfMessage = 0;
            int RealLenOfMessage = 0;
            boolean end = false;
            String messageString = "";
            String Reqq=GetNowTimeWithSeparator();
            int bytesRead = -1;
            int Req = 0;
            try {
                Future result = clientChannel.read(buffer);

                while (! result.isDone()) {
                    // do nothing
                }

                buffer.flip();
                String message = new String(buffer.array()).trim();
                System.out.println("this *** is res: "+message);
            }
            catch (Exception e){
                if (in != null) {try {in.close();in=null;} catch (IOException ex) {}}
                if (socket != null) {try {socket.close();socket=null;} catch (IOException ex) {}}
                messageByte = null;
                in = null;
                LenOfMessage = 0;
                RealLenOfMessage = 0;
                messageString = "";
                Reqq=null;
                bytesRead = 0;
                Req = 0;
            }
            finally {
                if (in != null) {try {in.close();in=null;} catch (IOException ex) {}}
                if (socket != null) {try {socket.close();socket=null;} catch (IOException ex) {}}
                try {
                    processMessage(messageString);

                } catch (Exception e) {
                    System.out.println(e.toString());
                }
                messageByte = null;
                in = null;
                LenOfMessage = 0;
                RealLenOfMessage = 0;
                messageString = "";
                Reqq=null;
                bytesRead = 0;
                Req = 0;
            }
        }

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
    }
    public void  runServer(){
        boolean ResultOfCreateReceiveFromSabaRunning=false;

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future future = executorService.submit(new Runnable() {
            public void run() {
                try {
                    MultiThreadedServer server = new MultiThreadedServer();
                    //new Thread(server).start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });




    }
}
