package CardSwitchSaba.Old;

import CardSwitchSaba.SendToSabaSwitch;
import ServiceObjects.ISO.ISO210;
import ServiceObjects.ISO.ISO430;
import ServiceObjects.Other.LoggerToDB;
import utils.PropertiesUtils;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.SocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.AsynchronousServerSocketChannel;
import java.nio.channels.AsynchronousSocketChannel;
import java.nio.channels.CompletionHandler;
import java.nio.charset.Charset;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

public class ReceiveFromSaba1 {

    public ReceiveFromSaba1() throws Exception {

        boolean ResultOfCreateReceiveFromSabaRunning=false;

        ExecutorService executorService = Executors.newSingleThreadExecutor();
        Future future = executorService.submit(new Runnable() {
            public void run() {
                try {
                    start();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });


    }
    public void start() throws Exception {
        AsynchronousServerSocketChannel server = AsynchronousServerSocketChannel.open();
        String host = PropertiesUtils.getThisHostForSaba();
        int port = Integer.valueOf(PropertiesUtils.getThisPortForSaba());
        InetSocketAddress sAddr = new InetSocketAddress(host, port);
        server.bind(sAddr);
        System.out.format("Server is listening at %s%n", sAddr);
        Attachment attach = new Attachment();
        attach.server = server;
       //
        //while (true){
            server.accept(attach, new ConnectionHandler());
        //}

        Thread.currentThread().join();
    }
    class Attachment {
        AsynchronousServerSocketChannel server;
        AsynchronousSocketChannel client;
        ByteBuffer buffer;
        SocketAddress clientAddr;
        boolean isRead;
    }

    class ConnectionHandler implements CompletionHandler<AsynchronousSocketChannel, Attachment> {
        @Override
        public void completed(AsynchronousSocketChannel client, Attachment attach) {
            try {
                SocketAddress clientAddr = client.getRemoteAddress();
                System.out.format("Accepted a  connection from  %s%n", clientAddr);
                attach.server.accept(attach, this);
                ReadWriteHandler rwHandler = new ReadWriteHandler();
                Attachment newAttach = new Attachment();
                newAttach.server = attach.server;
                newAttach.client = client;
                newAttach.buffer = ByteBuffer.allocate(2048);
                newAttach.isRead = true;
                newAttach.clientAddr = clientAddr;
                client.read(newAttach.buffer, newAttach, rwHandler);
                //client.close();
               // client.read(newAttach.buffer);
                //new ReadWriteHandler(newAttach);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void failed(Throwable e, Attachment attach) {
            System.out.println("Failed to accept a  connection.");
            e.printStackTrace();
        }
    }

    class ReadWriteHandlerr  {

        public ReadWriteHandlerr(Attachment attach) {
          int result=0;
           if (result == -1) {
                try {
                    attach.client.close();
                    System.out.format("Stopped   listening to the   client %s%n",
                            attach.clientAddr);
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                return;
            }

            if (attach.isRead) {
                attach.buffer.flip();
                int limits = attach.buffer.limit();
                byte bytes[] = new byte[limits];
                attach.buffer.get(bytes, 0, limits);
                Charset cs = Charset.forName("UTF-8");
                String msg = new String(bytes, cs);
                System.out.format("Client at  %s  says: %s%n", attach.clientAddr,
                        msg);

                attach.isRead = false; // It is a write
                attach.buffer.rewind();
                processMessage(msg);

            } else {
                // Write to the client
               // attach.client.write(attach.buffer, attach, this);
               // attach.isRead = true;
               // attach.buffer.clear();
               // attach.client.read(attach.buffer, attach, this);
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


        public void failed(Throwable e, Attachment attach) {
            e.printStackTrace();
        }
    }

    class ReadWriteHandler implements CompletionHandler<Integer, Attachment> {
        @Override
        public void completed(Integer result, Attachment attach) {
            if (result == -1) {
                try {
                    attach.client.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.format("Stopped   listening to the   client %s%n",
                        attach.clientAddr);
                return;
            }

            if (attach.isRead) {
                attach.buffer.flip();
                int limits = attach.buffer.limit();
                byte bytes[] = new byte[limits];
                attach.buffer.get(bytes, 0, limits);
                Charset cs = Charset.forName("UTF-8");
                String msg = new String(bytes, cs);
                System.out.format("Client at  %s  says: %s%n", attach.clientAddr,
                        msg);
                attach.isRead = false; // It is a write
                attach.buffer.rewind();
                // attach.client.close();
                processMessage(msg);

            } else {
                // Write to the client
                attach.client.write(attach.buffer, attach, this);
                attach.isRead = true;
                attach.buffer.clear();
                attach.client.read(attach.buffer, attach, this);
            }
        }

        @Override
        public void failed(Throwable e, Attachment attach) {
            e.printStackTrace();
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

}
